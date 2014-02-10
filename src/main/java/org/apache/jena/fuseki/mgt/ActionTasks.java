/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.jena.fuseki.mgt;
import static java.lang.String.format ;

import javax.servlet.http.HttpServletRequest ;
import javax.servlet.http.HttpServletResponse ;

import org.apache.jena.fuseki.Fuseki ;
import org.apache.jena.fuseki.servlets.ActionBase ;
import org.apache.jena.fuseki.servlets.HttpAction ;
import org.apache.jena.fuseki.servlets.ServletOps ;
import org.apache.jena.web.HttpSC ;

public class ActionTasks extends ActionBase //ActionContainerItem
{
    public ActionTasks() { super(Fuseki.serverLog) ; }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        doCommon(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        doCommon(request, response);
    }

    private static String prefix = "/" ;
    
    @Override
    protected void execCommonWorker(HttpAction action) {
        String name = extractItemName(action) ;
        if ( name != null ) {
            if ( name.startsWith(prefix))
                name = name.substring(prefix.length()) ; 
            else
                log.warn("Unexpected task name : "+name) ;
        }
        
        String method = action.request.getMethod() ;
        if ( method.equals(METHOD_GET) )
            execGet(action, name) ;
        else if ( method.equals(METHOD_POST) )
            execPost(action, name) ;
        else
            ServletOps.error(HttpSC.METHOD_NOT_ALLOWED_405) ;
    }
    

    private void execGet(HttpAction action, String name) {
        String _name = (name==null)?"''":name ;
        log.info(format("[%d] Task %s", action.id, _name));
        ServletOps.success(action);
    }

    private void execPost(HttpAction action, String name) {
        
    }
}

