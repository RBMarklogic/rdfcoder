/*
 * Copyright 2007-2008 Michele Mostarda ( michele.mostarda@gmail.com ).
 * All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the 'License');
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an 'AS IS' BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.asemantics.rdfcoder.model.ontology;

import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;

/**
 * Defines the ontology generated by a {@link com.asemantics.rdfcoder.model.java.JavaCodeHandler}
 */
public interface Ontology {

    /**
     * Defines a relation <i>Subject</i>, <i>predicate</i>,
     * <i>object</i> in the ontology.
     *
     * @param subjectPrefix
     * @param predicate
     * @param objectPrefix
     */
    void defineRelation(String subjectPrefix, URL predicate, String objectPrefix) throws OntologyException;

    /**
     * Defines a relation <i>Subject</i>, <i>predicate</i>,
     * <i>list</i> in the ontology.
     *
     * @param subjectPrefix
     * @param predicate
     * @param listBounds
     * @throws OntologyException
     */
    void defineRelation(String subjectPrefix, URL predicate, ListBounds listBounds) throws OntologyException;

    /**
     * Defines a relation <i>Subject</i>, <i>predicate</i>,
     * <i>literal object</i> in the ontology.
     *
     * @param subjectPrefix
     * @param predicate
     */
    void defineRelation(String subjectPrefix, URL predicate) throws OntologyException;

    /**
     * Defines a relation *  , <i>predicate</i> , * in the ontology. 
     *
     * @param predicate
     */
    void defineRelation(URL predicate) throws OntologyException;

    /**
     * Removes an already defined relation from the ontology.
     *
     * @param subjectPrefix
     * @param predicate
     * @param objectPrefix
     */
    void undefineRelation(String subjectPrefix, URL predicate, String objectPrefix) throws OntologyException;

    /**
     *  Removes an already defined relation from the ontology.
     *
     * @param subjectPrefix
     * @param predicate
     */
    void undefineRelation(String subjectPrefix, URL predicate) throws OntologyException;

    /**
     * Removes an already defined relation wich object is a list.
     *
     * @param subjectPrefix
     * @param predicate
     */
    void undefineRelationList(String subjectPrefix, URL predicate) throws OntologyException;

    /**
     * Validates a triple over the current ontology.
     *
     * @param subject the triple subject.
     * @param predicate the triple predicate.
     * @param object the triple object.
     */
    void validateTriple(String subject, String predicate, Object object) throws OntologyException;

    /**
     * Validates a triple literal over the current ontology.
     *
     * @param subject the triple subject.
     * @param predicate the triple object.
     */
    void validateTripleLiteral(String subject, String predicate) throws OntologyException;

    /**
     * Returns the number of relations defined in the ontology.
     *
     * @return a positive number.
     */
    int getRelationsCount();

    /**
     * Returns <code>true</code> if the i-th <i>object</i> is a literal,
     * <code>false</code> otherwise.
     * 
     * @param i
     * @return <code>true<code> if literal.
     */
    boolean isLiteralRelation(int i);

    /**
     * Returns the relation i-th subject prefix.
     *
     * @param i
     * @return the subject prefix string.
     */
    String getRelationSubjectPrefix(int i);

     /**
     * Returns the relation i-th predicate.
      *
     * @param i
     * @return the predicate.
     */
    URL getRelationPredicate(int i);

    /**
     * Returns the relation i-th object prefix.
     *
     * @param i index discriminative of the object.
     * @return the prefix.
     */
    String getRelationObjectPrefix(int i);

    /**
     * Prints the current ontology in a readable form.
     *
     * @param ps
     */
    void printOntology(PrintStream ps);

    /**
     * Prints the current ontology in OWL format.
     *
     * @param os
     */
    void toOWL(OutputStream os);

    /**
     * Declares the restriction bounds for an array.
     */
    class ListBounds {

        int minSize;
        int maxSize;

        /**
         * Constructor for bounds in interval <i>[minSize, maxSize]</i>.
         *
         * @param minSize minimum accepted size.
         * @param maxSize maximum expected size.
         */
        public ListBounds(int minSize, int maxSize) {
            this.minSize = minSize;
            this.maxSize = maxSize;
        }

        public ListBounds() {
            this(0, Integer.MAX_VALUE);
        }

        boolean inBounds(String[] values) {
            return values.length >= minSize && values.length <= maxSize;
        }

        @Override
        public String toString() {
            return String.format("%s %d %d", ListBounds.this.getClass().getSimpleName(), minSize, maxSize);
        }

        @Override
        public boolean equals(Object obj) {
            if(obj == null) {
                return false;
            }
            if(obj == this) {
                return true;
            }
            if(obj instanceof ListBounds) {
                final ListBounds other = (ListBounds) obj;
                return minSize == other.minSize && maxSize == other.maxSize;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return minSize * maxSize;
        }
    }
}
