/*
 *   Copyright 2004 The Apache Software Foundation
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */
package org.apache.eve.exception;


import javax.naming.NamingException;

import org.apache.ldap.common.message.ResultCodeEnum;


/**
 * Extends the root NamingException by adding LDAP specific properties to it.
 *
 * @author <a href="mailto:directory-dev@incubator.apache.org">Apache Directory Project</a>
 * @version $Rev$
 */
public class EveNamingException extends NamingException implements EveException
{
    /** the LDAP resultCode this exception is associated with */
    private final ResultCodeEnum resultCode;


    /**
     * Creates an Eve NamingException.
     *
     * @param resultCode the LDAP resultCode this exception is associated with
     */
    public EveNamingException( ResultCodeEnum resultCode )
    {
        super();

        this.resultCode = resultCode;
    }


    /**
     * Creates an Eve NamingException.
     * 
     * @param explanation an explanation for the failure
     * @param resultCode the LDAP resultCode this exception is associated with
     */
    public EveNamingException( String explanation, ResultCodeEnum resultCode )
    {
        super( explanation );

        this.resultCode = resultCode;
    }


    /**
     * Gets the LDAP resultCode this exception is associated with.
     *
     * @return the LDAP resultCode this exception is associated with
     */
    public ResultCodeEnum getResultCode()
    {
        return this.resultCode;
    }
}
