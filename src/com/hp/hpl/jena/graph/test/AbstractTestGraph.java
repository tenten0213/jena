/*
  (c) Copyright 2003, Hewlett-Packard Company, all rights reserved.
  [See end of file]
  $Id: AbstractTestGraph.java,v 1.29 2003-08-08 13:03:08 chris-dollin Exp $
*/

package com.hp.hpl.jena.graph.test;

import com.hp.hpl.jena.util.iterator.*;
import com.hp.hpl.jena.graph.*;
import com.hp.hpl.jena.graph.impl.*;
import com.hp.hpl.jena.graph.query.*;
import com.hp.hpl.jena.shared.*;

import java.util.*;

/**
    AbstractTestGraph provides a bunch of basic tests for something that
    purports to be a Graph. The abstract method getGraph must be overridden
    in subclasses to deliver a Graph of interest. 
    
 	@author kers
*/
public abstract class AbstractTestGraph extends GraphTestBase
    {
    public AbstractTestGraph( String name )
        { super( name ); }
        
    /**
        Returns a Graph to take part in the test. Must be overridden in
        a subclass.
    */
    public abstract Graph getGraph();
    
    public Graph getGraphWith( String facts )
        {
        Graph g = getGraph();
        graphAdd( g, facts );
        return g;    
        }
        
    /**
        This test case was generated by Ian and was caused by GraphMem
        not keeping up with changes to the find interface. 
    */
    public void testFindAndContains()
        {
        Graph g = getGraph();
        Node r = Node.create( "r" ), s = Node.create( "s" ), p = Node.create( "P" );
        g.add( Triple.create( r, p,  s ) );
        assertTrue( g.contains( r, p, Node.ANY ) );
        assertTrue( g.find( r, p, Node.ANY ).hasNext() );
        }
        
    public void testFindByFluidTriple()
        {
        Graph g = getGraph();
        g.add( triple( "x y z ") );
        assertTrue( g.find( triple( "?? y z" ) ).hasNext() );
        assertTrue( g.find( triple( "x ?? z" ) ).hasNext() );
        assertTrue( g.find( triple( "x y ??" ) ).hasNext() );
        }
        
    public void testContainsConcrete()
        {
        Graph g = getGraph();
        graphAdd( g, "s P o; _x _R _y; x S 0" );
        assertTrue( g.contains( triple( "s P o" ) ) );
        assertTrue( g.contains( triple( "_x _R _y" ) ) );
        assertTrue( g.contains( triple( "x S 0" ) ) );
    /* */
        assertFalse( g.contains( triple( "s P Oh" ) ) );
        assertFalse( g.contains( triple( "S P O" ) ) );
        assertFalse( g.contains( triple( "s p o" ) ) );
        assertFalse( g.contains( triple( "_x _r _y" ) ) );
        assertFalse( g.contains( triple( "x S 1" ) ) );
        }
        
    public void testContainsFluid()
        {
        Graph g = getGraph();
        graphAdd( g, "x R y; a P b" );
        assertTrue( g.contains( triple( "?? R y" ) ) );
        assertTrue( g.contains( triple( "x ?? y" ) ) );
        assertTrue( g.contains( triple( "x R ??" ) ) );
        assertTrue( g.contains( triple( "?? P b" ) ) );
        assertTrue( g.contains( triple( "a ?? b" ) ) );
        assertTrue( g.contains( triple( "a P ??" ) ) );
        assertTrue( g.contains( triple( "?? R y" ) ) );
    /* */
        assertFalse( g.contains( triple( "?? R b" ) ) );
        assertFalse( g.contains( triple( "a ?? y" ) ) );
        assertFalse( g.contains( triple( "x P ??" ) ) );
        assertFalse( g.contains( triple( "?? R x" ) ) );
        assertFalse( g.contains( triple( "x ?? R" ) ) );
        assertFalse( g.contains( triple( "a S ??" ) ) );
        }
        
    public void testAGraph()
        {
        String title = this.getClass().getName();
        Graph g = getGraph();
        int baseSize = g.size();
        graphAdd( g, "x R y; p S q; a T b" );
    /* */
        assertContainsAll( title + ": simple graph", g, "x R y; p S q; a T b" );
        assertEquals( title + ": size", baseSize + 3, g.size() );
        graphAdd( g, "spindizzies lift cities; Diracs communicate instantaneously" );
        assertEquals( title + ": size after adding", baseSize + 5, g.size() );
        g.delete( triple( "x R y" ) );
        g.delete( triple( "a T b" ) );
        assertEquals( title + ": size after deleting", baseSize + 3, g.size() );
        assertContainsAll( title + ": modified simple graph", g, "p S q; spindizzies lift cities; Diracs communicate instantaneously" );
        assertOmitsAll( title + ": modified simple graph", g, "x R y; a T b" );
    /* */ 
        ClosableIterator it = g.find( null, node("lift"), null );
        assertTrue( title + ": finds some triple(s)", it.hasNext() );
        assertEquals( title + ": finds a 'lift' triple", triple("spindizzies lift cities"), it.next() );
        assertFalse( title + ": finds exactly one triple", it.hasNext() );
        }

//    public void testStuff()
//        {
////        testAGraph( "StoreMem", new GraphMem() );
////        testAGraph( "StoreMemBySubject", new GraphMem() );
////        String [] empty = new String [] {};
////        Graph g = graphWith( "x R y; p S q; a T b" );
////    /* */
////        assertContainsAll( "simple graph", g, "x R y; p S q; a T b" );
////        graphAdd( g, "spindizzies lift cities; Diracs communicate instantaneously" );
////        g.delete( triple( "x R y" ) );
////        g.delete( triple( "a T b" ) );
////        assertContainsAll( "modified simple graph", g, "p S q; spindizzies lift cities; Diracs communicate instantaneously" );
////        assertOmitsAll( "modified simple graph", g, "x R y; a T b" );
//        }
                                      
    public void testReificationControl()
        {
        Graph g1 = graphWith( "x rdf:subject S" );
        Graph g2 = GraphBase.withReification( g1, Reifier.Convenient );
        assertEquals( "should not hide reification triple", 1, g1.size() );
        assertEquals( "should not hide reification triple", 1, g2.size() );
        g2.add( triple( "x rdf:object O" ) );
        assertEquals( "", 1, g1.size() );
        assertEquals( "", 1, g2.size() );
        }

    /**
        Test that Graphs have transaction support methods, and that if they fail
        on some g they fail because they do not support the operation.
    */
    public void testHasTransactions()
        {
        Graph g = getGraph();
        TransactionHandler th = g.getTransactionHandler();
        th.transactionsSupported();
        try { th.begin(); } catch (UnsupportedOperationException x) {}
        try { th.abort(); } catch (UnsupportedOperationException x) {}
        try { th.commit(); } catch (UnsupportedOperationException x) {}
    /* */
        Command cmd = new Command() 
            { public Object execute() { return null; } };
        try { th.executeInTransaction( cmd ); } 
        catch (UnsupportedOperationException x) {}
        }

    static final Triple [] tripleArray = tripleArray( "S P O; A R B; X Q Y" );

    static final List tripleList = Arrays.asList( tripleArray( "i lt j; p equals q" ) );
        
    static final Triple [] setTriples = tripleArray
        ( "scissors cut paper; paper wraps stone; stone breaks scissors" );
        
    static final Set tripleSet = new HashSet( Arrays.asList( setTriples ) );
                
    public void testBulkUpdate()
        {
        Graph g = getGraph();
        BulkUpdateHandler bu = g.getBulkUpdateHandler();
        Graph items = graphWith( "pigs might fly; dead can dance" );
        int initialSize = g.size();
    /* */
        bu.add( tripleArray );
        testContains( g, tripleArray );
        testOmits( g, tripleList );
    /* */
        bu.add( tripleList );
        testContains( g, tripleList );
        testContains( g, tripleArray );
    /* */
        bu.add( tripleSet.iterator() );
        testContains( g, tripleSet.iterator() );
        testContains( g, tripleList );
        testContains( g, tripleArray );
    /* */
        bu.add( items );
        testContains( g, items );
        testContains( g, tripleSet.iterator() );
        testContains( g, tripleArray );
        testContains( g, tripleList );
    /* */
        bu.delete( tripleArray );
        testOmits( g, tripleArray );
        testContains( g, tripleList );
        testContains( g, tripleSet.iterator() );
        testContains( g, items );
    /* */
        bu.delete( tripleSet.iterator() );
        testOmits( g, tripleSet.iterator() );
        testOmits( g, tripleArray );
        testContains( g, tripleList );
        testContains( g, items );
    /* */
        bu.delete( items );
        testOmits( g, tripleSet.iterator() );
        testOmits( g, tripleArray );
        testContains( g, tripleList );
        testOmits( g, items ); 
    /* */
        bu.delete( tripleList );
        assertEquals( "graph has original size", initialSize, g.size() );
        }
        
    public static void assertIsomorphic( Graph expected, Graph got )
        {
        if (!expected.isIsomorphicWith( got ))
            fail( "wanted " + expected + " but got " + got );
        }
        
    public void testBulkAddWithReification()
        {        
        testBulkAddWithReification( true );
        testBulkAddWithReification( false );
        }
        
    public void testBulkAddWithReification( boolean withReifications )
        {
        Graph g = getGraph();
        BulkUpdateHandler bu = g.getBulkUpdateHandler();
        Graph items = graphWith( "pigs might fly; dead can dance" );
        Reifier gr = g.getReifier(), ir = items.getReifier();
        xSPOyXYZ( ir );
        bu.add( items, withReifications );
        assertIsomorphic
            ( 
            withReifications ? ir.getHiddenTriples() : graphWith( "" ), 
            gr.getHiddenTriples() 
            );
        }
        
    protected void xSPOyXYZ( Reifier r )
        {
        xSPO( r );
        r.reifyAs( Node.create( "y" ), Triple.create( "X Y Z" ) );       
        }

    protected void aABC( Reifier r )
        { r.reifyAs( Node.create( "a" ), Triple.create( "A B C" ) ); }
        
    protected void xSPO( Reifier r )
        { r.reifyAs( Node.create( "x" ), Triple.create( "S P O" ) ); }
        
    public void testBulkRemoveWithReification()
        {        
        testBulkUpdateRemoveWithReification( true );
        testBulkUpdateRemoveWithReification( false );
        }
        
    public void testBulkUpdateRemoveWithReification( boolean withReifications )
        {
        Graph g = getGraph();
        BulkUpdateHandler bu = g.getBulkUpdateHandler();
        Graph items = graphWith( "pigs might fly; dead can dance" );
        Reifier gr = g.getReifier(), ir = items.getReifier();
        xSPOyXYZ( ir );
        xSPO( gr ); aABC( gr ); 
        bu.delete( items, withReifications );
        Graph answer = graphWith( "" );
        Reifier ar = answer.getReifier();
        if (withReifications)
            aABC( ar ); 
        else
            {
            xSPO( ar );
            aABC( ar );
            }
        assertIsomorphic( ar.getHiddenTriples(), gr.getHiddenTriples() );
        }
                                        
    public void testHasCapabilities()
        {
        Graph g = getGraph();
        /* Capabilities c = */ g.getCapabilities();
        }
        
    public void testFind()
        {
        Graph g = getGraph();
        graphAdd( g, "S P O" );
        assertTrue( g.find( Node.ANY, null, null ).hasNext() );
        assertTrue( g.find( null, Node.ANY, null ).hasNext() );
        }
        
    public void testFind2()
        {
         Graph g = getGraphWith( "S P O" );   
         TripleIterator waitingForABigRefactoringHere = null;
         ExtendedIterator it = g.find( Node.ANY, Node.ANY, Node.ANY );
        }
        
    /**
        test the isEmpty component of a query handler.
    */
    public void testIsEmpty()
        {
        Graph g = getGraph();
        if (canBeEmpty( g ))
            {
            QueryHandler q = g.queryHandler();
            assertTrue( q.isEmpty() );
            g.add( Triple.create( "S P O" ) );
            assertFalse( q.isEmpty() );
            g.add( Triple.create( "A B C" ) );
            assertFalse( q.isEmpty() );
            g.add( Triple.create( "S P O" ) );
            assertFalse( q.isEmpty() );
            g.delete( Triple.create( "S P O" ) );
            assertFalse( q.isEmpty() );
            g.delete( Triple.create( "A B C" ) );
            assertTrue( q.isEmpty() );
            }
        }
        
    protected boolean canBeEmpty( Graph g )
        {
        return g.queryHandler().isEmpty();
        }
        
    public void testEventRegister()
        {
        Graph g = getGraph();
        GraphEventManager gem = g.getEventManager();
        assertSame( gem, gem.register( new RecordingListener() ) );
        }
        
    /**
        Test that we can safely unregister a listener that isn't registered.
    */
    public void testEventUnregister()
        {
        getGraph().getEventManager().unregister( L );
        }
        
    /**
        Handy triple for test purposes.
    */
    protected Triple SPO = Triple.create( "S P O" );
    protected RecordingListener L = new RecordingListener();
    
    /**
        Utility: get a graph, register L with its manager, return the graph.
    */
    protected Graph getAndRegister( GraphListener gl )
        {
        Graph g = getGraph();
        g.getEventManager().register( gl );
        return g;
        }
        
    public void testAddTriple()
        {
        getAndRegister( L ).add( SPO );
        assertTrue( L.has( new Object[] {"add", SPO} ) );
        }
        
    public void testDeleteTriple()
        {        
        getAndRegister( L ).delete( SPO );
        assertTrue( L.has( new Object[] { "delete", SPO} ) );
        }
        
    public void testTwoListeners()
        {
        RecordingListener L1 = new RecordingListener();
        RecordingListener L2 = new RecordingListener();
        Graph g = getGraph();
        GraphEventManager gem = g.getEventManager();
        gem.register( L1 ).register( L2 );
        g.add( SPO );
        assertTrue( L2.has( new Object[] {"add", SPO} ) );
        assertTrue( L1.has( new Object[] {"add", SPO} ) );
        }
        
    public void testUnregisterWorks()
        {
        Graph g = getGraph();
        GraphEventManager gem = g.getEventManager();
        gem.register( L ).unregister( L );
        g.add( SPO );
        assertTrue( L.has( new Object[] {} ) );
        }
        
    public void testRegisterTwice()
        {
        Graph g = getAndRegister( L );
        g.getEventManager().register( L );
        g.add( SPO );
        assertTrue( L.has( new Object[] {"add", SPO, "add", SPO} ) );
        }
        
    public void testUnregisterOnce()
        {
        Graph g = getAndRegister( L );
        g.getEventManager().register( L ).unregister( L );
        g.delete( SPO );
        assertTrue( L.has( new Object[] {"delete", SPO} ) );
        }
        
    public void testBulkAddArray()
        {
        Graph g = getAndRegister( L );
        Triple [] triples = tripleArray( "x R y; a P b" );
        g.getBulkUpdateHandler().add( triples );
        L.assertHas( new Object[] {"add[]", triples} );
        }
        
    public void testBulkAddList()
        {
        Graph g = getAndRegister( L );
        List elems = Arrays.asList( tripleArray( "bells ring loudly; pigs might fly" ) );
        g.getBulkUpdateHandler().add( elems );
        L.assertHas( new Object[] {"addList", elems} );
        }
    
    public void testBulkDeleteArray()
        {
        Graph g = getAndRegister( L );
        Triple [] triples = tripleArray( "x R y; a P b" );
        g.getBulkUpdateHandler().delete( triples );
        L.assertHas( new Object[] {"delete[]", triples} );
        }
        
    public void testBulkDeleteList()
        {
        Graph g = getAndRegister( L );
        List elems = Arrays.asList( tripleArray( "bells ring loudly; pigs might fly" ) );
        g.getBulkUpdateHandler().delete( elems );
        L.assertHas( new Object[] {"deleteList", elems} );
        }
        
    public void testBulkAddIterator()
        {
        Graph g = getAndRegister( L ); 
        Triple [] triples = tripleArray( "I wrote this; you read that; I wrote this" );
        g.getBulkUpdateHandler().add( asIterator( triples ) );
        L.assertHas( new Object[] {"addIterator", Arrays.asList( triples )} );
        }
        
    public void testBulkDeleteIterator()
        {
        Graph g = getAndRegister( L );
        Triple [] triples = tripleArray( "I wrote this; you read that; I wrote this" );
        g.getBulkUpdateHandler().delete( asIterator( triples ) );
        L.assertHas( new Object[] {"deleteIterator", Arrays.asList( triples )} );
        }
        
    public Iterator asIterator( Triple [] triples )
        { return Arrays.asList( triples ).iterator(); }
    
    public void testBulkAddGraph()
        {
        Graph g = getAndRegister( L );
        Graph triples = graphWith( "this type graph; I type slowly" );
        g.getBulkUpdateHandler().add( triples );
        L.assertHas( new Object[] {"addGraph", triples} );
        }
        
    public void testBulkDeleteGraph()
        {        
        Graph g = getAndRegister( L );
        Graph triples = graphWith( "this type graph; I type slowly" );
        g.getBulkUpdateHandler().delete( triples );
        L.assertHas( new Object[] {"deleteGraph", triples} );
        }
        
    public void testContainsNode()
        {
        Graph g = getGraph();
        graphAdd( g, "a P b; _c _Q _d; 10 11 12" );
        QueryHandler qh = g.queryHandler();
        assertTrue( qh.containsNode( node( "a" ) ) );
        assertTrue( qh.containsNode( node( "P" ) ) );
        assertTrue( qh.containsNode( node( "b" ) ) );
        assertTrue( qh.containsNode( node( "_c" ) ) );
        assertTrue( qh.containsNode( node( "_Q" ) ) );
        assertTrue( qh.containsNode( node( "_d" ) ) );
        assertTrue( qh.containsNode( node( "10" ) ) );
        assertTrue( qh.containsNode( node( "11" ) ) );
        assertTrue( qh.containsNode( node( "12" ) ) );
    /* */
        assertFalse( qh.containsNode( node( "x" ) ) );
        assertFalse( qh.containsNode( node( "_y" ) ) );
        assertFalse( qh.containsNode( node( "99" ) ) );
        }
        
    protected Graph getClosed()
        {
        Graph result = getGraph();
        result.close();
        return result;
        }
        
//    public void testClosedDelete()
//        {
//        try { getClosed().delete( triple( "x R y" ) ); fail( "delete when closed" ); }
//        catch (ClosedException c) { /* as required */ }
//        }
//        
//    public void testClosedAdd()
//        {
//        try { getClosed().add( triple( "x R y" ) ); fail( "add when closed" ); }
//        catch (ClosedException c) { /* as required */ }
//        }
//        
//    public void testClosedContainsTriple()
//        {
//        try { getClosed().contains( triple( "x R y" ) ); fail( "contains[triple] when closed" ); }
//        catch (ClosedException c) { /* as required */ }
//        }
//        
//    public void testClosedContainsSPO()
//        {
//        Node a = Node.ANY;
//        try { getClosed().contains( a, a, a ); fail( "contains[SPO] when closed" ); }
//        catch (ClosedException c) { /* as required */ }
//        }
//        
//    public void testClosedFindTriple()
//        {
//        try { getClosed().find( triple( "x R y" ) ); fail( "find [triple] when closed" ); }
//        catch (ClosedException c) { /* as required */ }
//        }
//        
//    public void testClosedFindSPO()
//        {
//        Node a = Node.ANY;
//        try { getClosed().find( a, a, a ); fail( "find[SPO] when closed" ); }
//        catch (ClosedException c) { /* as required */ }
//        }
//        
//    public void testClosedSize()
//        {
//        try { getClosed().size(); fail( "size when closed (" + this.getClass() + ")" ); }
//        catch (ClosedException c) { /* as required */ }
//        }
        
    }


/*
    (c) Copyright Hewlett-Packard Company 2003
    All rights reserved.

    Redistribution and use in source and binary forms, with or without
    modification, are permitted provided that the following conditions
    are met:

    1. Redistributions of source code must retain the above copyright
       notice, this list of conditions and the following disclaimer.

    2. Redistributions in binary form must reproduce the above copyright
       notice, this list of conditions and the following disclaimer in the
       documentation and/or other materials provided with the distribution.

    3. The name of the author may not be used to endorse or promote products
       derived from this software without specific prior written permission.

    THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
    IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
    OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
    IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
    INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
    NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
    DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
    THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
    (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
    THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/