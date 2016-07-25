/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler.util;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.utils.SecuritySupport;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.Stylesheet;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.Locale;
/*     */ import java.util.ResourceBundle;
/*     */ 
/*     */ public final class ErrorMsg
/*     */ {
/*     */   private String _code;
/*     */   private int _line;
/*  45 */   private String _message = null;
/*  46 */   private String _url = null;
/*  47 */   Object[] _params = null;
/*     */   private boolean _isWarningError;
/*     */   Throwable _cause;
/*     */   public static final String MULTIPLE_STYLESHEET_ERR = "MULTIPLE_STYLESHEET_ERR";
/*     */   public static final String TEMPLATE_REDEF_ERR = "TEMPLATE_REDEF_ERR";
/*     */   public static final String TEMPLATE_UNDEF_ERR = "TEMPLATE_UNDEF_ERR";
/*     */   public static final String VARIABLE_REDEF_ERR = "VARIABLE_REDEF_ERR";
/*     */   public static final String VARIABLE_UNDEF_ERR = "VARIABLE_UNDEF_ERR";
/*     */   public static final String CLASS_NOT_FOUND_ERR = "CLASS_NOT_FOUND_ERR";
/*     */   public static final String METHOD_NOT_FOUND_ERR = "METHOD_NOT_FOUND_ERR";
/*     */   public static final String ARGUMENT_CONVERSION_ERR = "ARGUMENT_CONVERSION_ERR";
/*     */   public static final String FILE_NOT_FOUND_ERR = "FILE_NOT_FOUND_ERR";
/*     */   public static final String INVALID_URI_ERR = "INVALID_URI_ERR";
/*     */   public static final String FILE_ACCESS_ERR = "FILE_ACCESS_ERR";
/*     */   public static final String MISSING_ROOT_ERR = "MISSING_ROOT_ERR";
/*     */   public static final String NAMESPACE_UNDEF_ERR = "NAMESPACE_UNDEF_ERR";
/*     */   public static final String FUNCTION_RESOLVE_ERR = "FUNCTION_RESOLVE_ERR";
/*     */   public static final String NEED_LITERAL_ERR = "NEED_LITERAL_ERR";
/*     */   public static final String XPATH_PARSER_ERR = "XPATH_PARSER_ERR";
/*     */   public static final String REQUIRED_ATTR_ERR = "REQUIRED_ATTR_ERR";
/*     */   public static final String ILLEGAL_CHAR_ERR = "ILLEGAL_CHAR_ERR";
/*     */   public static final String ILLEGAL_PI_ERR = "ILLEGAL_PI_ERR";
/*     */   public static final String STRAY_ATTRIBUTE_ERR = "STRAY_ATTRIBUTE_ERR";
/*     */   public static final String ILLEGAL_ATTRIBUTE_ERR = "ILLEGAL_ATTRIBUTE_ERR";
/*     */   public static final String CIRCULAR_INCLUDE_ERR = "CIRCULAR_INCLUDE_ERR";
/*     */   public static final String RESULT_TREE_SORT_ERR = "RESULT_TREE_SORT_ERR";
/*     */   public static final String SYMBOLS_REDEF_ERR = "SYMBOLS_REDEF_ERR";
/*     */   public static final String XSL_VERSION_ERR = "XSL_VERSION_ERR";
/*     */   public static final String CIRCULAR_VARIABLE_ERR = "CIRCULAR_VARIABLE_ERR";
/*     */   public static final String ILLEGAL_BINARY_OP_ERR = "ILLEGAL_BINARY_OP_ERR";
/*     */   public static final String ILLEGAL_ARG_ERR = "ILLEGAL_ARG_ERR";
/*     */   public static final String DOCUMENT_ARG_ERR = "DOCUMENT_ARG_ERR";
/*     */   public static final String MISSING_WHEN_ERR = "MISSING_WHEN_ERR";
/*     */   public static final String MULTIPLE_OTHERWISE_ERR = "MULTIPLE_OTHERWISE_ERR";
/*     */   public static final String STRAY_OTHERWISE_ERR = "STRAY_OTHERWISE_ERR";
/*     */   public static final String STRAY_WHEN_ERR = "STRAY_WHEN_ERR";
/*     */   public static final String WHEN_ELEMENT_ERR = "WHEN_ELEMENT_ERR";
/*     */   public static final String UNNAMED_ATTRIBSET_ERR = "UNNAMED_ATTRIBSET_ERR";
/*     */   public static final String ILLEGAL_CHILD_ERR = "ILLEGAL_CHILD_ERR";
/*     */   public static final String ILLEGAL_ELEM_NAME_ERR = "ILLEGAL_ELEM_NAME_ERR";
/*     */   public static final String ILLEGAL_ATTR_NAME_ERR = "ILLEGAL_ATTR_NAME_ERR";
/*     */   public static final String ILLEGAL_TEXT_NODE_ERR = "ILLEGAL_TEXT_NODE_ERR";
/*     */   public static final String SAX_PARSER_CONFIG_ERR = "SAX_PARSER_CONFIG_ERR";
/*     */   public static final String INTERNAL_ERR = "INTERNAL_ERR";
/*     */   public static final String UNSUPPORTED_XSL_ERR = "UNSUPPORTED_XSL_ERR";
/*     */   public static final String UNSUPPORTED_EXT_ERR = "UNSUPPORTED_EXT_ERR";
/*     */   public static final String MISSING_XSLT_URI_ERR = "MISSING_XSLT_URI_ERR";
/*     */   public static final String MISSING_XSLT_TARGET_ERR = "MISSING_XSLT_TARGET_ERR";
/*     */   public static final String ACCESSING_XSLT_TARGET_ERR = "ACCESSING_XSLT_TARGET_ERR";
/*     */   public static final String NOT_IMPLEMENTED_ERR = "NOT_IMPLEMENTED_ERR";
/*     */   public static final String NOT_STYLESHEET_ERR = "NOT_STYLESHEET_ERR";
/*     */   public static final String ELEMENT_PARSE_ERR = "ELEMENT_PARSE_ERR";
/*     */   public static final String KEY_USE_ATTR_ERR = "KEY_USE_ATTR_ERR";
/*     */   public static final String OUTPUT_VERSION_ERR = "OUTPUT_VERSION_ERR";
/*     */   public static final String ILLEGAL_RELAT_OP_ERR = "ILLEGAL_RELAT_OP_ERR";
/*     */   public static final String ATTRIBSET_UNDEF_ERR = "ATTRIBSET_UNDEF_ERR";
/*     */   public static final String ATTR_VAL_TEMPLATE_ERR = "ATTR_VAL_TEMPLATE_ERR";
/*     */   public static final String UNKNOWN_SIG_TYPE_ERR = "UNKNOWN_SIG_TYPE_ERR";
/*     */   public static final String DATA_CONVERSION_ERR = "DATA_CONVERSION_ERR";
/*     */   public static final String NO_TRANSLET_CLASS_ERR = "NO_TRANSLET_CLASS_ERR";
/*     */   public static final String NO_MAIN_TRANSLET_ERR = "NO_MAIN_TRANSLET_ERR";
/*     */   public static final String TRANSLET_CLASS_ERR = "TRANSLET_CLASS_ERR";
/*     */   public static final String TRANSLET_OBJECT_ERR = "TRANSLET_OBJECT_ERR";
/*     */   public static final String ERROR_LISTENER_NULL_ERR = "ERROR_LISTENER_NULL_ERR";
/*     */   public static final String JAXP_UNKNOWN_SOURCE_ERR = "JAXP_UNKNOWN_SOURCE_ERR";
/*     */   public static final String JAXP_NO_SOURCE_ERR = "JAXP_NO_SOURCE_ERR";
/*     */   public static final String JAXP_COMPILE_ERR = "JAXP_COMPILE_ERR";
/*     */   public static final String JAXP_INVALID_ATTR_ERR = "JAXP_INVALID_ATTR_ERR";
/*     */   public static final String JAXP_INVALID_ATTR_VALUE_ERR = "JAXP_INVALID_ATTR_VALUE_ERR";
/*     */   public static final String JAXP_SET_RESULT_ERR = "JAXP_SET_RESULT_ERR";
/*     */   public static final String JAXP_NO_TRANSLET_ERR = "JAXP_NO_TRANSLET_ERR";
/*     */   public static final String JAXP_NO_HANDLER_ERR = "JAXP_NO_HANDLER_ERR";
/*     */   public static final String JAXP_NO_RESULT_ERR = "JAXP_NO_RESULT_ERR";
/*     */   public static final String JAXP_UNKNOWN_PROP_ERR = "JAXP_UNKNOWN_PROP_ERR";
/*     */   public static final String SAX2DOM_ADAPTER_ERR = "SAX2DOM_ADAPTER_ERR";
/*     */   public static final String XSLTC_SOURCE_ERR = "XSLTC_SOURCE_ERR";
/*     */   public static final String ER_RESULT_NULL = "ER_RESULT_NULL";
/*     */   public static final String JAXP_INVALID_SET_PARAM_VALUE = "JAXP_INVALID_SET_PARAM_VALUE";
/*     */   public static final String JAXP_SET_FEATURE_NULL_NAME = "JAXP_SET_FEATURE_NULL_NAME";
/*     */   public static final String JAXP_GET_FEATURE_NULL_NAME = "JAXP_GET_FEATURE_NULL_NAME";
/*     */   public static final String JAXP_UNSUPPORTED_FEATURE = "JAXP_UNSUPPORTED_FEATURE";
/*     */   public static final String JAXP_SECUREPROCESSING_FEATURE = "JAXP_SECUREPROCESSING_FEATURE";
/*     */   public static final String COMPILE_STDIN_ERR = "COMPILE_STDIN_ERR";
/*     */   public static final String COMPILE_USAGE_STR = "COMPILE_USAGE_STR";
/*     */   public static final String TRANSFORM_USAGE_STR = "TRANSFORM_USAGE_STR";
/*     */   public static final String STRAY_SORT_ERR = "STRAY_SORT_ERR";
/*     */   public static final String UNSUPPORTED_ENCODING = "UNSUPPORTED_ENCODING";
/*     */   public static final String SYNTAX_ERR = "SYNTAX_ERR";
/*     */   public static final String CONSTRUCTOR_NOT_FOUND = "CONSTRUCTOR_NOT_FOUND";
/*     */   public static final String NO_JAVA_FUNCT_THIS_REF = "NO_JAVA_FUNCT_THIS_REF";
/*     */   public static final String TYPE_CHECK_ERR = "TYPE_CHECK_ERR";
/*     */   public static final String TYPE_CHECK_UNK_LOC_ERR = "TYPE_CHECK_UNK_LOC_ERR";
/*     */   public static final String ILLEGAL_CMDLINE_OPTION_ERR = "ILLEGAL_CMDLINE_OPTION_ERR";
/*     */   public static final String CMDLINE_OPT_MISSING_ARG_ERR = "CMDLINE_OPT_MISSING_ARG_ERR";
/*     */   public static final String WARNING_PLUS_WRAPPED_MSG = "WARNING_PLUS_WRAPPED_MSG";
/*     */   public static final String WARNING_MSG = "WARNING_MSG";
/*     */   public static final String FATAL_ERR_PLUS_WRAPPED_MSG = "FATAL_ERR_PLUS_WRAPPED_MSG";
/*     */   public static final String FATAL_ERR_MSG = "FATAL_ERR_MSG";
/*     */   public static final String ERROR_PLUS_WRAPPED_MSG = "ERROR_PLUS_WRAPPED_MSG";
/*     */   public static final String ERROR_MSG = "ERROR_MSG";
/*     */   public static final String TRANSFORM_WITH_TRANSLET_STR = "TRANSFORM_WITH_TRANSLET_STR";
/*     */   public static final String TRANSFORM_WITH_JAR_STR = "TRANSFORM_WITH_JAR_STR";
/*     */   public static final String COULD_NOT_CREATE_TRANS_FACT = "COULD_NOT_CREATE_TRANS_FACT";
/*     */   public static final String TRANSLET_NAME_JAVA_CONFLICT = "TRANSLET_NAME_JAVA_CONFLICT";
/*     */   public static final String INVALID_QNAME_ERR = "INVALID_QNAME_ERR";
/*     */   public static final String INVALID_NCNAME_ERR = "INVALID_NCNAME_ERR";
/*     */   public static final String INVALID_METHOD_IN_OUTPUT = "INVALID_METHOD_IN_OUTPUT";
/*     */   public static final String OUTLINE_ERR_TRY_CATCH = "OUTLINE_ERR_TRY_CATCH";
/*     */   public static final String OUTLINE_ERR_UNBALANCED_MARKERS = "OUTLINE_ERR_UNBALANCED_MARKERS";
/*     */   public static final String OUTLINE_ERR_DELETED_TARGET = "OUTLINE_ERR_DELETED_TARGET";
/*     */   public static final String OUTLINE_ERR_METHOD_TOO_BIG = "OUTLINE_ERR_METHOD_TOO_BIG";
/*     */   public static final String DESERIALIZE_TRANSLET_ERR = "DESERIALIZE_TEMPLATES_ERR";
/* 185 */   private static ResourceBundle _bundle = SecuritySupport.getResourceBundle("com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMessages", Locale.getDefault());
/*     */   public static final String ERROR_MESSAGES_KEY = "ERROR_MESSAGES_KEY";
/*     */   public static final String COMPILER_ERROR_KEY = "COMPILER_ERROR_KEY";
/*     */   public static final String COMPILER_WARNING_KEY = "COMPILER_WARNING_KEY";
/*     */   public static final String RUNTIME_ERROR_KEY = "RUNTIME_ERROR_KEY";
/*     */ 
/*     */   public ErrorMsg(String code)
/*     */   {
/* 191 */     this._code = code;
/* 192 */     this._line = 0;
/*     */   }
/*     */ 
/*     */   public ErrorMsg(String code, Throwable e) {
/* 196 */     this._code = code;
/* 197 */     this._message = e.getMessage();
/* 198 */     this._line = 0;
/* 199 */     this._cause = e;
/*     */   }
/*     */ 
/*     */   public ErrorMsg(String message, int line) {
/* 203 */     this._code = null;
/* 204 */     this._message = message;
/* 205 */     this._line = line;
/*     */   }
/*     */ 
/*     */   public ErrorMsg(String code, int line, Object param) {
/* 209 */     this._code = code;
/* 210 */     this._line = line;
/* 211 */     this._params = new Object[] { param };
/*     */   }
/*     */ 
/*     */   public ErrorMsg(String code, Object param) {
/* 215 */     this(code);
/* 216 */     this._params = new Object[1];
/* 217 */     this._params[0] = param;
/*     */   }
/*     */ 
/*     */   public ErrorMsg(String code, Object param1, Object param2) {
/* 221 */     this(code);
/* 222 */     this._params = new Object[2];
/* 223 */     this._params[0] = param1;
/* 224 */     this._params[1] = param2;
/*     */   }
/*     */ 
/*     */   public ErrorMsg(String code, SyntaxTreeNode node) {
/* 228 */     this._code = code;
/* 229 */     this._url = getFileName(node);
/* 230 */     this._line = node.getLineNumber();
/*     */   }
/*     */ 
/*     */   public ErrorMsg(String code, Object param1, SyntaxTreeNode node) {
/* 234 */     this._code = code;
/* 235 */     this._url = getFileName(node);
/* 236 */     this._line = node.getLineNumber();
/* 237 */     this._params = new Object[1];
/* 238 */     this._params[0] = param1;
/*     */   }
/*     */ 
/*     */   public ErrorMsg(String code, Object param1, Object param2, SyntaxTreeNode node)
/*     */   {
/* 243 */     this._code = code;
/* 244 */     this._url = getFileName(node);
/* 245 */     this._line = node.getLineNumber();
/* 246 */     this._params = new Object[2];
/* 247 */     this._params[0] = param1;
/* 248 */     this._params[1] = param2;
/*     */   }
/*     */ 
/*     */   public Throwable getCause() {
/* 252 */     return this._cause;
/*     */   }
/*     */ 
/*     */   private String getFileName(SyntaxTreeNode node) {
/* 256 */     Stylesheet stylesheet = node.getStylesheet();
/* 257 */     if (stylesheet != null) {
/* 258 */       return stylesheet.getSystemId();
/*     */     }
/* 260 */     return null;
/*     */   }
/*     */ 
/*     */   private String formatLine() {
/* 264 */     StringBuffer result = new StringBuffer();
/* 265 */     if (this._url != null) {
/* 266 */       result.append(this._url);
/* 267 */       result.append(": ");
/*     */     }
/* 269 */     if (this._line > 0) {
/* 270 */       result.append("line ");
/* 271 */       result.append(Integer.toString(this._line));
/* 272 */       result.append(": ");
/*     */     }
/* 274 */     return result.toString();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 283 */     String suffix = this._params == null ? this._message : null != this._code ? getErrorMessage() : MessageFormat.format(getErrorMessage(), this._params);
/*     */ 
/* 286 */     return formatLine() + suffix;
/*     */   }
/*     */ 
/*     */   public String toString(Object obj) {
/* 290 */     Object[] params = new Object[1];
/* 291 */     params[0] = obj.toString();
/* 292 */     String suffix = MessageFormat.format(getErrorMessage(), params);
/* 293 */     return formatLine() + suffix;
/*     */   }
/*     */ 
/*     */   public String toString(Object obj0, Object obj1) {
/* 297 */     Object[] params = new Object[2];
/* 298 */     params[0] = obj0.toString();
/* 299 */     params[1] = obj1.toString();
/* 300 */     String suffix = MessageFormat.format(getErrorMessage(), params);
/* 301 */     return formatLine() + suffix;
/*     */   }
/*     */ 
/*     */   private String getErrorMessage()
/*     */   {
/* 312 */     return _bundle.getString(this._code);
/*     */   }
/*     */ 
/*     */   public void setWarningError(boolean flag)
/*     */   {
/* 320 */     this._isWarningError = flag;
/*     */   }
/*     */ 
/*     */   public boolean isWarningError() {
/* 324 */     return this._isWarningError;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg
 * JD-Core Version:    0.6.2
 */