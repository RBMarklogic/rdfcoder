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


package com.asemantics.model;

import com.asemantics.RDFCoder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This abstract class describes a code org.asemantics.model.
 *
 * A code org.asemantics.model is a rapresentation of a code structure
 * containing packages, classes, attributes methods and so on.
 *
 * @author Michele Mostarda (michele.mostarda@gmail.com)
 */
public abstract class JavaCodeModel extends CodeModelBase {

    /* Primitive types. */

    private static final String TYPE_VOID_STR    = "void";

    private static final String TYPE_CHAR_STR    = "char";

    private static final String TYPE_BYTE_STR    = "byte";

    private static final String TYPE_SHORT_STR   = "short";

    private static final String TYPE_INT_STR     = "int";

    private static final String TYPE_LONG_STR    = "long";

    private static final String TYPE_FLOAT_STR   = "float";

    private static final String TYPE_DOUBLE_STR  = "double";

    private static final String TYPE_BOOLEAN_STR = "boolean";

    private static final String TYPE_ARRAY_STR   = "array";

    /**
     * The primitive types.
     */
    public interface JType {

        public String getInternalIdentifier();

        public String getIdentifier();

        public String getIdentifier(String internalRepresentation);

        public JType cloneType();

    }

    protected static abstract class JPrimitiveType implements JType {

        public String getIdentifier() {
            return toURI(getInternalIdentifier());
        }

        public String getIdentifier(String internalRepresentation) {
            return toURI(internalRepresentation);
        }

        public JType cloneType() {
            return this;
        }

        public String toString() {
            return getInternalIdentifier();
        }
    }

    public static class TYPE_VOID extends JPrimitiveType {
        public String getInternalIdentifier() {
            return TYPE_VOID_STR;
        }
    }
    public static final TYPE_VOID VOID = new TYPE_VOID();

    public static class TYPE_BOOL extends JPrimitiveType {
        public String getInternalIdentifier() {
            return TYPE_BOOLEAN_STR;
        }
    }
    public static final TYPE_BOOL BOOL = new TYPE_BOOL();

    public static class TYPE_CHAR extends JPrimitiveType {
        public String getInternalIdentifier() {
            return TYPE_CHAR_STR;
        }
    }
    public static final TYPE_CHAR CHAR = new TYPE_CHAR();

    public static class TYPE_BYTE extends JPrimitiveType {
        public String getInternalIdentifier() {
            return TYPE_BYTE_STR;
        }
    }
    public static final TYPE_BYTE BYTE = new TYPE_BYTE();

    public static class TYPE_SHORT extends JPrimitiveType {
        public String getInternalIdentifier() {
            return TYPE_SHORT_STR;
        }
    }
    public static final TYPE_SHORT SHORT = new TYPE_SHORT();

    public static class TYPE_INT extends JPrimitiveType {
        public String getInternalIdentifier() {
            return TYPE_INT_STR;
        }
    }
    public static final TYPE_INT INT = new TYPE_INT();

    public static class TYPE_LONG extends JPrimitiveType {
        public String getInternalIdentifier() {
            return TYPE_LONG_STR;
        }
    }
    public static final TYPE_LONG LONG = new TYPE_LONG();

    public static class TYPE_FLOAT extends JPrimitiveType {
        public String getInternalIdentifier() {
            return TYPE_FLOAT_STR;
        }
    }
    public static final TYPE_FLOAT FLOAT = new TYPE_FLOAT();

    public static class TYPE_DOUBLE extends JPrimitiveType {
        public String getInternalIdentifier() {
            return TYPE_DOUBLE_STR;
        }
    }
    public static final TYPE_DOUBLE DOUBLE = new TYPE_DOUBLE();

    /**
     * Base of all structured types.
     */
    public static abstract class StructuredType implements JType {

        public JType cloneType() {
            try {
                return (JType) clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException("Cannot clone JType");
            }
        }
    }

    /**
     * The Object type.
     */
    public static class ObjectType extends StructuredType {

        private String pathToObject;

        public ObjectType(String pto) {
            pathToObject = pto;
        }

        public void setInternalIdentifier(String id) {
            if( pathToObject != null ) {
                throw new IllegalStateException("Invalid renaming");
            }
            pathToObject = id;
        }

        public String getInternalIdentifier() {
            return pathToObject;
        }

        public String getIdentifier() {
            return CLASS_PREFIX + pathToObject;
        }

        public String getIdentifier(String internalRepresentation) {
            return CLASS_PREFIX + internalRepresentation;
        }

        public String toString() {
            return getInternalIdentifier();
        }

        public static ObjectType rdfTypeToType(String rdfType) {
            if(rdfType.indexOf(CLASS_PREFIX) != 0) {
                return null;
            }
            return new ObjectType( rdfType.substring(CLASS_PREFIX.length()) );
        }
    }

    /**
     *  The Exception Type.
     */
    public static class ExceptionType extends ObjectType {

        public ExceptionType(String pte) {
            super(pte);
        }
    }

    /**
     * The interface type.
     */
    public static class InterfaceType extends StructuredType {

        private String pathToInterface;

        public InterfaceType(String pti) {
            pathToInterface = pti;
        }

        public void setInternalIdentifier(String id) {
            if( pathToInterface != null ) {
                throw new IllegalStateException("Invalid renaming");
            }
            pathToInterface = id;
        }

        public String getInternalIdentifier() {
            return pathToInterface;
        }

        public String getIdentifier() {
            return INTERFACE_PREFIX + pathToInterface;
        }

        public String getIdentifier(String internalRepresentation) {
            return INTERFACE_PREFIX + internalRepresentation;
        }

        public static InterfaceType rdfTypeToType(String rdfType) {
            if(rdfType.indexOf(INTERFACE_PREFIX) != 0) {
                return null;
            }
            return new InterfaceType( rdfType.substring(INTERFACE_PREFIX.length()) );
        }
    }

    /**
     * The array type.
     */
    public static class ArrayType extends StructuredType {

        private JType type;
        private int   size;

        public ArrayType(JType t, int s) {
            type = t;
            size = s; 
        }

        public JType getType() {
            return type;
        }

        public int getSize() {
            return size;
        }

        public void setInternalIdentifier(String id) {
            if(type instanceof ObjectType) {
                ( (ObjectType) type).setInternalIdentifier(id);
            } else if( type instanceof ArrayType) {
                ( (ArrayType) type).setInternalIdentifier(id);
            } else {
                throw new IllegalStateException();
            }
        }

        public String getInternalIdentifier() {
            return type.getInternalIdentifier();
        }

        public String getIdentifier() {
            return toIdentifier(type.getIdentifier());
        }

        public String getIdentifier(String internalRepresentation) {
            return toIdentifier(internalRepresentation);
        }

        private String toIdentifier(String id) {
            return toURI(
                    TYPE_ARRAY_STR + ":" +
                    ( type instanceof JPrimitiveType ?
                            type.getInternalIdentifier()
                                :
                            "<" + id + ">" // Mark object type.
                    ) +
                    ":" + size
            );
        }

        public String toString() {
            return getInternalIdentifier() + "["+ getSize() + "]";
        }

        private static String ARRAY_PREFIX = toURI(TYPE_ARRAY_STR);

        public static ArrayType rdfTypeToType(String rdfType) {
            if(rdfType.indexOf(ARRAY_PREFIX) != 0) {
                return null;
            }
            int i = rdfType.lastIndexOf(":");
            int arraySize = Integer.parseInt(rdfType.substring(i + 1));
            String type = rdfType.substring(ARRAY_PREFIX.length() + 1, i);
            boolean typeIsObj = type.charAt(0) == '<';
            if(typeIsObj) {
                type = type.substring(1, type.length() -2);
            }
            return new ArrayType( typeIsObj ? new ObjectType(type) : javaTypeToJType(type), arraySize );
        }
    }

    /**
     * Create an object type.
     * @param s the object type string.
     * @return the object type.
     */
    public static final ObjectType createObjectType(String s) {
        return new ObjectType(s);
    }

    /**
     * Converts a string representation of a RDF rdfType to a <code>JType</code>.
     * @param rdfType the <code>JType</code> as string.
     * @return the returned <code>JType</code> object.
     */
    public static final JType rdfTypeToJType(String rdfType) {

        // Primitive types.
        if(CHAR.getIdentifier().equals(rdfType)) {
            return CHAR;
        } else if(BYTE.getIdentifier().equals(rdfType)) {
            return BYTE;
        } else if(SHORT.getIdentifier().equals(rdfType)) {
            return SHORT;
        } else if(INT.getIdentifier().equals(rdfType)) {
            return INT;
        } else if(LONG.getIdentifier().equals(rdfType)) {
            return LONG;
        } else if(FLOAT.getIdentifier().equals(rdfType)) {
            return FLOAT;
        } else if(DOUBLE.getIdentifier().equals(rdfType)) {
            return DOUBLE;
        }  else if(BOOL.getIdentifier().equals(rdfType)) {
            return BOOL;
        } else if(VOID.getIdentifier().equals(rdfType)) {
            return VOID;
        }

        /* Complex types. */

        Object result;

        // Object type.
        result = ObjectType.rdfTypeToType(rdfType);
        if(result != null) {
            return (ObjectType) result;
        }

        // Interface type.
        result = InterfaceType.rdfTypeToType(rdfType);
        if(result != null) {
            return (InterfaceType) result;
        }

        // Array type.
        result = ArrayType.rdfTypeToType(rdfType);
        if(result != null) {
            return (ArrayType) result;
        }

        throw new IllegalArgumentException("Cannot convert RDF rdfType to a valid JType: '" + rdfType + "'.");
    }

    /**
     * Converts a Java type name to a <code>JType</code>.
     * @param type the Java type.
     * @return the returned <code>JType</code> object.
     */
    public static final JType javaTypeToJType(String type) {
        if(CHAR.getInternalIdentifier().equals(type)) {
            return CHAR;
        } else if(BYTE.getInternalIdentifier().equals(type)) {
            return BYTE;
        } else if(SHORT.getInternalIdentifier().equals(type)) {
            return SHORT;
        } else if(INT.getInternalIdentifier().equals(type)) {
            return INT;
        } else if(LONG.getInternalIdentifier().equals(type)) {
            return LONG;
        } else if(FLOAT.getInternalIdentifier().equals(type)) {
            return FLOAT;
        } else if(DOUBLE.getInternalIdentifier().equals(type)) {
            return DOUBLE;
        }  else if(BOOL.getInternalIdentifier().equals(type)) {
            return BOOL;
        } else if(VOID.getInternalIdentifier().equals(type)) {
            return VOID;
        } else {
            throw new IllegalArgumentException("Cannot convert Java type to a valid JType: '" + type + "'.");
        }
    }

    /**
     * Defines the visibility level of an entity.
     */
    public enum JVisibility {
        PUBLIC {
            protected String getInternalIdentifier() {
                return "public";
            }
        },
        PROTECTED {
            protected String getInternalIdentifier() {
                return "protected";
            }
        },
        DEFAULT {
            protected String getInternalIdentifier() {
                return "default";
            }
        },
        PRIVATE{
            protected String getInternalIdentifier() {
                return "private";
            }
        };

        protected abstract String getInternalIdentifier();

        public String getIdentifier() {
            return toURI(getInternalIdentifier());
        }

        /**
         * Converts a JVisibility string to an enumerated value.
         * @param s
         * @return
         */
        public static JVisibility toJVisibility(String s) {
            for(JVisibility v : JVisibility.values() ) {
                if(v.getIdentifier().equals(s)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    /**
     * Defines the Java modifiers.
     */
    public enum JModifier {

        /**
         * Entity is declared abstract.
         */
        ABSTRACT {
            private static final byte FA = 0x1;
            public byte value() {
                return FA;
            }
            public boolean isValue(byte b) {
                return (b & FA) == FA;
            }
        },

        /**
         * Entity is declared final.
         */
        FINAL {
            private static final byte FB = 0x2;
            public byte value() {
                return FB;
            }
            public boolean isValue(byte b) {
                return (b & FB) == FB; 
            }
        },

        /**
         * Entity is declared static.
         */
        STATIC {
            private static final byte FS = 0x4;
            public byte value() {
                return FS;
            }
            public boolean isValue(byte b) {
                return (b & FS) == FS;
            }
        },

        /**
         * Entity is declared volatile.
         */
        VOLATILE {
            private static final byte FV = 0x8;
            public byte value() {
                return FV;
            }
            public boolean isValue(byte b) {
                return (b & FV) == FV;
            }
        },

        /**
         * Entity is declared native.
         */
        NATIVE {
            private static final byte FN = 0x10;
            public byte value() {
                return FN;
            }
            public boolean isValue(byte b) {
                return (b & FN) == FN;
            }
        },

        /**
         * Entity is declared transient.
         */
        TRANSIENT {
            private static final byte TR = 0x20;
            public byte value() {
                return TR;
            }
            public boolean isValue(byte b) {
                return (b & TR) == TR;
            }
        },

        /**
         * Entity is declared synchronized.
         */
        SYNCHRONIZED {
            private static final byte SY = 0x40;
            public byte value() {
                return SY;
            }
            public boolean isValue(byte b) {
                return (b & SY) == SY;
            }
        };

        //TODO: manage CONST modifier.

        public abstract byte    value();
        public abstract boolean isValue(byte b);

        public static JModifier[] getModifiers(byte b) {
            List<JModifier> modifiers = new ArrayList<JModifier>(JModifier.values().length);
            for( JModifier v : JModifier.values()) {
                if(v.isValue(b)) {
                    modifiers.add(v);
                }
            }
            return modifiers.toArray(new JModifier[modifiers.size()]);
        }

        public static JModifier[] toModifiers(String m) {
            byte b = Byte.parseByte(m);
            return getModifiers(b);
        }

        public static Byte toByte(JModifier[] modifiers) {
            byte result = 0;
            for(JModifier m : modifiers) {
                result |= m.value();
            }
            return result;
        }
    }


    /* BEGIN: RDF classes. */

    /**
     * The Asset unique class.
     */
    public static final String JASSET       = toURI("jasset");

    public static final String JPACKAGE     = toURI("jpackage");

    public static final String JINTERFACE   = toURI("jinterface");

    public static final String JCLASS       = toURI("jclass");

    public static final String JATTRIBUTE   = toURI("jattribute");

    public static final String JCONSTRUCTOR = toURI("jconstructor");

    public static final String JMETHOD      = toURI("jmethod");

    public static final String JENUMERATION = toURI("jenumeration");

    public static final String JSIGNATURE   = toURI("jsignature");

    public static final String JPARAMETER   = toURI("jparameter");

    /* END: RDF classes. */


    /* Entity prefixes. */

    //TODO: entity prefixes and RDF classes MUST be the same.

    public static final String JASSET_PREFIX      = toPrefix("jasset");

    public static final String PACKAGE_PREFIX     = toPrefix("jpackage");

    public static final String CLASS_PREFIX       = toPrefix("jclass");

    public static final String INTERFACE_PREFIX   = toPrefix("jinterface");

    public static final String ATTRIBUTE_PREFIX   = toPrefix("jattribute");

    public static final String CONSTRUCTOR_PREFIX = toPrefix("jconstructor");

    public static final String METHOD_PREFIX      = toPrefix("jmethod");

    public static final String ENUMERATION_PREFIX = toPrefix("jenumeration");

    public static final String ELEMENT_PREFIX     = toPrefix("jelement");

    public static final String SIGNATURE_PREFIX   = toPrefix("jsignature");

    public static final String PARAMETER_PREFIX   = toPrefix("jparameter");


    /* RDFS properties. */

    /**
     * A package contains a package.
     */
    public static final String CONTAINS_PACKAGE   = toURI("contains_package");

    /**
     * A container contains an interface.
     */
    public static final String CONTAINS_INTERFACE = toURI("contains_interface");

    /**
     * A container contains a class.
     */
    public static final String CONTAINS_CLASS     = toURI("contains_class");

    /**
     * Connects a class to a contained attribute.
     */
    public static final String CONTAINS_ATTRIBUTE = toURI("contains_attribute");

    /**
     * Connects a class to a contained constructor.
     */
    public static final String CONTAINS_CONSTRUCTOR = toURI("contains_contructor");

    /**
     * The attribute type.
     */
    public static final String ATTRIBUTE_TYPE     = toURI("attribute_type");

    /**
     * The attribute value.
     */
    public static final String ATTRIBUTE_VALUE    = toURI("attribute_value");

    /**
     * A class contains a method.
     */
    public static final String CONTAINS_METHOD    = toURI("contains_method");

    /**
     * A class or interface contains an enumeration.
     */
    public static final String CONTAINS_ENUMERATION = toURI("contains_enumeration");

    /**
     * An enumeration contains an element.
     */
    public static final String CONTAINS_ELEMENT = toURI("contains_element");

    /**
     * A method or constructor contain a signature.
     */
    public static final String CONTAINS_SIGNATURE = toURI("contains_signature");

    /**
     * A signature contains a parameter.
     */
    public static final String CONTAINS_PARAMETER = toURI("contains_parameter");

    /**
     * A parameter type.
     */
    public static final String PARAMETER_TYPE     = toURI("parameter_type");

    /**
     * Return type of a signature.
     */
    public static final String RETURN_TYPE        = toURI("return_type");

    /**
     * The base class of a class.
     */
    public static final String EXTENDS_CLASS      = toURI("extends_class");

    /**
     * Connects a class to an implemented interface.
     */
    public static final String IMPLEMENTS_INT     = toURI("implements_int");

    /**
     * Connects an interface to an extended interface.
     */
    public static final String EXTENDS_INT        = toURI("extends_int");

    /**
     * Connects a constructor or method to the thrown exceptions.
     */
    public static final String THROWS             = toURI("throws");

    /**
     * Defines the visibility level of an entity.
     */
    public static final String HAS_VISIBILITY     = toURI("has_visibility");

    /**
     * Defines the modifier of an entity.
     */
    public static final String HAS_MODIFIERS      = toURI("has_modifiers");

}
