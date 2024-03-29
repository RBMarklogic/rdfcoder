/*
 * Copyright 2007-2008 Michele Mostarda ( michele.mostarda@gmail.com ).
 * All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the 'License');
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an 'AS IS' BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.asemantics.rdfcoder.model.java;

import com.asemantics.rdfcoder.model.Identifier;
import com.asemantics.rdfcoder.model.QueryModelException;

/**
 * Represents a <i>Java</i> package.
 *
 * @author Michele Mostarda (michele.mostarda@gmail.com)
 */
public class JPackage extends JContainer {

    /**
     * Constructor.
     *
     * @param codeModel
     * @param identifier
     */
    protected JPackage(JavaQueryModel codeModel, Identifier identifier)
    throws QueryModelException {
        super(codeModel, identifier);
    }

    protected boolean exists(Identifier identifier) {
        return getQueryModel().packageExists(identifier);
    }

    protected String getHierarchyElemType() {
        return this.getClass().getSimpleName();
    }

    public JavaCodeModel.JVisibility getVisibility() throws QueryModelException {
        return JavaCodeModel.JVisibility.PUBLIC;
    }
}
