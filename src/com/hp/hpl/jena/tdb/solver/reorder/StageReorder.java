/*
 * (c) Copyright 2008 Hewlett-Packard Development Company, LP
 * All rights reserved.
 * [See end of file]
 */

package com.hp.hpl.jena.tdb.solver.reorder;

import java.util.Iterator;

import iterator.RepeatApplyIterator;

import com.hp.hpl.jena.sparql.core.BasicPattern;
import com.hp.hpl.jena.sparql.core.Substitute;
import com.hp.hpl.jena.sparql.engine.ExecutionContext;
import com.hp.hpl.jena.sparql.engine.binding.Binding;
import com.hp.hpl.jena.sparql.engine.main.StageGenerator;
import com.hp.hpl.jena.tdb.TDBException;

public class StageReorder extends RepeatApplyIterator<Binding>
{
    private ReorderProc reorderProc = null ;
    private ReorderTransformation transform ;
    private StageGenerator stageGenerator ;
    private BasicPattern pattern ;

    public StageReorder(BasicPattern pattern, Iterator<Binding> _input, ReorderTransformation transform,
                        ExecutionContext execCxt, StageGenerator stageGen)
    {
        super(_input) ;
        this.pattern = pattern ;
        this.transform = transform ;
        this.stageGenerator = stageGen ;
    }

    @Override
    protected Iterator<Binding> makeNextStage(Binding b)
    {
        // ---- Reorder
        BasicPattern pattern2 = Substitute.substitute(pattern, b) ;

        if ( transform != null && pattern.size() > 1 )
        {
            if ( reorderProc == null )
                // Cache the reorder processor - ie. the first binding is used
                // as a template for later input bindings.   
                reorderProc = transform.reorderIndexes(pattern2) ;
            pattern2 = reorderProc.reorder(pattern2) ;
        }
        
        // ---- Execute
        throw new TDBException("NOT READY: StageReorder") ;
    }

}

/*
 * (c) Copyright 2008 Hewlett-Packard Development Company, LP
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