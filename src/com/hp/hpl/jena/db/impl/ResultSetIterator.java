/*
 *  (c) Copyright 2003 Hewlett-Packard Development Company, LP
 *  All rights reserved.
 *
 *
 */

//=======================================================================
// Package
package com.hp.hpl.jena.db.impl;

//=======================================================================
// Imports
import java.sql.*;
import java.util.*;
import com.hp.hpl.jena.util.iterator.*;
import com.hp.hpl.jena.shared.*;

import org.apache.log4j.Logger;

//=======================================================================
/**
* Iterates over an SQL result set returning each row as an ArrayList of
* objects. The returned array is shared at each iteration so calling next() or even hasNext()
* changes the array contents. When the iterator terminates the resources
* are cleaned up and the underlying SQL PreparedStatement is returned to
* the SQLCache pool from whence it came.
* 
* <p>Override the extractRow, getRow, and remove methods in subclasses 
* to return an object collection derived from the row contents instead 
* of the raw row contents.
*
* @author <a href="mailto:der@hplb.hpl.hp.com">Dave Reynolds</a>
* @version $Revision: 1.6 $ on $Date: 2003-11-17 07:23:50 $
*/

public class ResultSetIterator implements ExtendedIterator {

    /** The ResultSet being iterated over */
    protected ResultSet m_resultSet;

    /** The originating SQLcache to return the statement to, can be null */
    protected SQLCache m_sqlCache;

    /** The source Statement to be cleaned up when the iterator finishes - return it to cache or close it if no cache */
    protected PreparedStatement m_statement;

    /** The name of the original operation that lead to this statement, can be null if SQLCache is null */
    protected String m_opname;

    /** The contents of the current row */
    protected ArrayList m_row;

    /** The number of columns in this result set */
    protected int m_nCols;

    /** Flag that the iteration has finished */
    protected boolean m_finished = false;

    /** Flag if we have prefeteched the next row but not yet returned it */
    protected boolean m_prefetched = false;

    protected static Logger logger = Logger.getLogger( ResultSetIterator.class );
    /**
     * Create an empty iterator.
     * Needs to be initialized by reset
     * before it can be accessed. Useful to allow generic functions like
     * {@link SQLCache#runSQLQuery runSQLQuery}
     * to return different iterator types to the client.
     */
    public ResultSetIterator() {
        m_finished = true;      // Prevent reading until reset
    }

    /**
     * Iterate over the results of a PreparedStatement generated by an SQLCache
     * @param resultSet the result set being iterated over
     * @param sourceStatement The source Statement to be cleaned up when the iterator finishes - return it to cache or close it if no cache
     * @param cache The originating SQLcache to return the statement to, can be null
     * @param opname The name of the original operation that lead to this statement, can be null if SQLCache is null
     */
    public ResultSetIterator(ResultSet resultSet, PreparedStatement sourceStatement, SQLCache cache, String opname) {
        m_resultSet = resultSet;
        m_sqlCache = cache;
        m_statement = sourceStatement;
        m_opname = opname;
    }

    /**
     * Iterate over the results of a PreparedStatement, close the statement when finished.
     * @param resultSet the result set being iterated over
     * @param sourceStatement The source Statement to be closed when the iterator finishes
     */
    public ResultSetIterator(ResultSet resultSet, PreparedStatement sourceStatement) {
        m_resultSet = resultSet;
        m_statement = sourceStatement;
    }

    /**
     * Reset an existing iterator to scan a new result set.
     * @param resultSet the result set being iterated over
     * @param sourceStatement The source Statement to be cleaned up when the iterator finishes - return it to cache or close it if no cache
     * @param cache The originating SQLcache to return the statement to, can be null
     * @param opname The name of the original operation that lead to this statement, can be null if SQLCache is null
     */
    public void reset(ResultSet resultSet, PreparedStatement sourceStatement, SQLCache cache, String opname) {
        m_resultSet = resultSet;
        m_sqlCache = cache;
        m_statement = sourceStatement;
        m_opname = opname;
        m_finished = false;
        m_prefetched = false;
        m_row = null;
    }

    /**
     * Test if there is a next result to return
     */
    public boolean hasNext() {
        if (!m_finished && !m_prefetched) moveForward();
        return !m_finished;
    }
    
    public Object removeNext()
        { cantRemove(); return null; }

    /**
     * Return the current row
     */
    public Object next() {
        if (!m_finished && !m_prefetched) moveForward();
        m_prefetched = false;
        if (m_finished) {
            throw new NoSuchElementException();
        }
        return getRow();
    }

    /**
     * Delete the current row entry
     */
    public void remove() {
        cantRemove();
    }
    
    protected void cantRemove() {
        throw new UnsupportedOperationException("ResultSetIterator can't remove database rows");
    }

    /**
     * More forward one row. Sets the m_finished flag if there is no more to fetch
     */
    protected void moveForward() {
        try {
            if (!m_finished && m_resultSet.next()) {
                extractRow();
                m_prefetched = true;
            } else {
                close();
            }
        } catch (Exception e) {
            //  TODO do we need this catch at all?
            logger.warn("Problem in iterator over db result set, op = " + m_opname, e);
            // Added by kers for debugging
            throw new JenaException( e );
        }
    }

    /**
     * Extract the current row
     * Override in subclasses.
     */
    protected void extractRow() throws Exception {
        if (m_row == null) {
            m_nCols = m_resultSet.getMetaData().getColumnCount();
            m_row = new ArrayList(m_nCols);
            for (int i = 0; i < m_nCols; i++) m_row.add(null);
        }
        for (int i = 0; i < m_nCols; i++) {
            m_row.set(i, m_resultSet.getObject(i+1));
        }
    }

    /**
     * Return the current row,should have already been extracted.
     * Override in subclasses.
     */
    protected Object getRow() {
        return m_row;
    }

    /**
     * Clean up the allocated resources - result set and statement.
     * If we know of an SQLCache return the statement there, otherwise close it.
     */
    public void close() {
        if (!m_finished) {
            if (m_resultSet != null) {
                try {
                    m_resultSet.close();
                    m_resultSet = null;
                } catch (SQLException e) {
                    logger.warn("Error while finalizing result set iterator", e);
                }
            }
            if (m_sqlCache != null && m_opname != null) {
                m_sqlCache.returnPreparedSQLStatement(m_statement, m_opname);
            } else {
                try {
                    m_statement.close();
                } catch (SQLException e) {
                    logger.warn("Error while finalizing result set iterator", e);
                }
            }
        }
        m_finished = true;
    }

    /**
     * Get a singleton result (single column from single row) and close the iterator.
     * This may be too specialized but seems to come up a lot - rethink.
     */
    public Object getSingleton() throws SQLException {
        List row = (List) next();
        close();
        return row.get(0);
    }

    /**
     * Clean up the database cursor. Noramlly the client should read to the end
     * or explicity close but....
     */
    protected void finalize() throws SQLException {
        if (!m_finished && m_resultSet != null) close();
    }

	/**
         return a new iterator which delivers all the elements of this iterator and
         then all the elements of the other iterator. Does not copy either iterator;
         they are consumed as the result iterator is consumed.
     */
	public ExtendedIterator andThen(ClosableIterator other) {
		return NiceIterator.andThen(this, other);
	}

	/* (non-Javadoc)
	 * @see com.hp.hpl.jena.util.iterator.ExtendedIterator#filterKeep(com.hp.hpl.jena.util.iterator.Filter)
	 */
	public ExtendedIterator filterKeep(Filter f) {
		return new FilterIterator( f, this );
	}

	/* (non-Javadoc)
	 * @see com.hp.hpl.jena.util.iterator.ExtendedIterator#filterDrop(com.hp.hpl.jena.util.iterator.Filter)
	 */
	public ExtendedIterator filterDrop(final Filter f) {
		Filter notF = new Filter() { public boolean accept( Object x ) { return !f.accept( x ); } };
		return new FilterIterator( notF, this ); 
	}

	/* (non-Javadoc)
	 * @see com.hp.hpl.jena.util.iterator.ExtendedIterator#mapWith(com.hp.hpl.jena.util.iterator.Map1)
	 */
	public ExtendedIterator mapWith(Map1 map1) {
		return new Map1Iterator( map1, this ); 
	}

} // End class

/*
 *  (c) Copyright 2000, 2001 Hewlett-Packard Development Company, LP
 *  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.

 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
