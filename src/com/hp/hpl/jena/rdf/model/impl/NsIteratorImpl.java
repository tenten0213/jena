/*
  (c) Copyright 2003, Hewlett-Packard Company, all rights reserved.
  [See end of file]
  $Id: NsIteratorImpl.java,v 1.3 2003-03-26 12:33:05 chris-dollin Exp $
*/

package com.hp.hpl.jena.rdf.model.impl;

import com.hp.hpl.jena.util.iterator.*;
import com.hp.hpl.jena.rdf.model.*;

import java.util.Iterator;
import java.util.NoSuchElementException;

/** An NsIterator implementation
 *
 * @author  bwm
 * @version   Release='$Name: not supported by cvs2svn $' Revision='$Revision: 1.3 $' Date='$Date: 2003-03-26 12:33:05 $'
 */
public class NsIteratorImpl extends ClosableWrapper implements NsIterator {
    
    /** Creates new NsIteratorImpl, ignores _o_ */
    public NsIteratorImpl(Iterator iter, Object o) {
        super( iter ); 
    }

    /** the cast is probably unnecessary */
    public Object next() {
        return (String) super.next();
    }
    
    public String nextNs() throws NoSuchElementException, RDFException {
        return (String) super.next();
    }
    
}

/*
 *  (c) Copyright Hewlett-Packard Company 2000, 2003 
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
 *
 * NsIteratorImpl.java
 *
 * Created on 07 August 2000, 06:55
 */

