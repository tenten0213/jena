/*
 * (c) Copyright 2007 Hewlett-Packard Development Company, LP
 * All rights reserved.
 * [See end of file]
 */

package com.hp.hpl.jena.sparql.sse;



public class ParseHandlerDebug implements ParseHandler 
{
    int count = 0 ;
    
    public void parseStart()
    { System.out.println("<<<<") ; }

    public void parseFinish()
    { System.out.println(">>>>") ; }

    public String resolvePName(String pname) { return pname ; }

    public Item itemNode(Item item)
    {
        return null ;
    }

    public Item itemWord(Item item)
    {
        return null ;
    }

    public void listAdd(Item item, Item elt)
    {
        if ( elt.isList() )
            return ;
        indent() ;
        System.out.println(elt) ;
    }

    public void listStart(Item item)
    {   
        indent() ;
        System.out.println("<<") ;
        count++ ; 
    }
    
    public Item listFinish(Item item)
    {   
        count-- ; 
        indent() ;
        System.out.println(">>") ;
        return item ;
    }
    

    private void indent()
    {
        for ( int i = 0 ; i < count ; i++ ) System.out.print("  ") ;
    }
}

/*
 * (c) Copyright 2007 Hewlett-Packard Development Company, LP
 * All rights reserved.
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
 *
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