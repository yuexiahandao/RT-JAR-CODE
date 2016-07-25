/*     */ package com.sun.org.apache.xml.internal.serializer.utils;
/*     */ 
/*     */ import java.util.ListResourceBundle;
/*     */ 
/*     */ public class SerializerMessages_ca extends ListResourceBundle
/*     */ {
/*     */   public Object[][] getContents()
/*     */   {
/*  30 */     Object[][] contents = { { "BAD_MSGKEY", "The message key ''{0}'' is not in the message class ''{1}''" }, { "BAD_MSGFORMAT", "The format of message ''{0}'' in message class ''{1}'' failed." }, { "ER_SERIALIZER_NOT_CONTENTHANDLER", "The serializer class ''{0}'' does not implement org.xml.sax.ContentHandler." }, { "ER_RESOURCE_COULD_NOT_FIND", "The resource [ {0} ] could not be found.\n {1}" }, { "ER_RESOURCE_COULD_NOT_LOAD", "The resource [ {0} ] could not load: {1} \n {2} \n {3}" }, { "ER_BUFFER_SIZE_LESSTHAN_ZERO", "Buffer size <=0" }, { "ER_INVALID_UTF16_SURROGATE", "Invalid UTF-16 surrogate detected: {0} ?" }, { "ER_OIERROR", "IO error" }, { "ER_ILLEGAL_ATTRIBUTE_POSITION", "Cannot add attribute {0} after child nodes or before an element is produced.  Attribute will be ignored." }, { "ER_NAMESPACE_PREFIX", "Namespace for prefix ''{0}'' has not been declared." }, { "ER_STRAY_ATTRIBUTE", "Attribute ''{0}'' outside of element." }, { "ER_STRAY_NAMESPACE", "Namespace declaration ''{0}''=''{1}'' outside of element." }, { "ER_COULD_NOT_LOAD_RESOURCE", "Could not load ''{0}'' (check CLASSPATH), now using just the defaults" }, { "ER_ILLEGAL_CHARACTER", "Attempt to output character of integral value {0} that is not represented in specified output encoding of {1}." }, { "ER_COULD_NOT_LOAD_METHOD_PROPERTY", "Could not load the propery file ''{0}'' for output method ''{1}'' (check CLASSPATH)" }, { "ER_INVALID_PORT", "Invalid port number" }, { "ER_PORT_WHEN_HOST_NULL", "Port cannot be set when host is null" }, { "ER_HOST_ADDRESS_NOT_WELLFORMED", "Host is not a well formed address" }, { "ER_SCHEME_NOT_CONFORMANT", "The scheme is not conformant." }, { "ER_SCHEME_FROM_NULL_STRING", "Cannot set scheme from null string" }, { "ER_PATH_CONTAINS_INVALID_ESCAPE_SEQUENCE", "Path contains invalid escape sequence" }, { "ER_PATH_INVALID_CHAR", "Path contains invalid character: {0}" }, { "ER_FRAG_INVALID_CHAR", "Fragment contains invalid character" }, { "ER_FRAG_WHEN_PATH_NULL", "Fragment cannot be set when path is null" }, { "ER_FRAG_FOR_GENERIC_URI", "Fragment can only be set for a generic URI" }, { "ER_NO_SCHEME_IN_URI", "No scheme found in URI" }, { "ER_CANNOT_INIT_URI_EMPTY_PARMS", "Cannot initialize URI with empty parameters" }, { "ER_NO_FRAGMENT_STRING_IN_PATH", "Fragment cannot be specified in both the path and fragment" }, { "ER_NO_QUERY_STRING_IN_PATH", "Query string cannot be specified in path and query string" }, { "ER_NO_PORT_IF_NO_HOST", "Port may not be specified if host is not specified" }, { "ER_NO_USERINFO_IF_NO_HOST", "Userinfo may not be specified if host is not specified" }, { "ER_SCHEME_REQUIRED", "Scheme is required!" } };
/*     */ 
/* 128 */     return contents;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.serializer.utils.SerializerMessages_ca
 * JD-Core Version:    0.6.2
 */