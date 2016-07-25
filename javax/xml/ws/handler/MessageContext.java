/*     */ package javax.xml.ws.handler;
/*     */ 
/*     */ import java.util.Map;
/*     */ 
/*     */ public abstract interface MessageContext extends Map<String, Object>
/*     */ {
/*     */   public static final String MESSAGE_OUTBOUND_PROPERTY = "javax.xml.ws.handler.message.outbound";
/*     */   public static final String INBOUND_MESSAGE_ATTACHMENTS = "javax.xml.ws.binding.attachments.inbound";
/*     */   public static final String OUTBOUND_MESSAGE_ATTACHMENTS = "javax.xml.ws.binding.attachments.outbound";
/*     */   public static final String WSDL_DESCRIPTION = "javax.xml.ws.wsdl.description";
/*     */   public static final String WSDL_SERVICE = "javax.xml.ws.wsdl.service";
/*     */   public static final String WSDL_PORT = "javax.xml.ws.wsdl.port";
/*     */   public static final String WSDL_INTERFACE = "javax.xml.ws.wsdl.interface";
/*     */   public static final String WSDL_OPERATION = "javax.xml.ws.wsdl.operation";
/*     */   public static final String HTTP_RESPONSE_CODE = "javax.xml.ws.http.response.code";
/*     */   public static final String HTTP_REQUEST_HEADERS = "javax.xml.ws.http.request.headers";
/*     */   public static final String HTTP_RESPONSE_HEADERS = "javax.xml.ws.http.response.headers";
/*     */   public static final String HTTP_REQUEST_METHOD = "javax.xml.ws.http.request.method";
/*     */   public static final String SERVLET_REQUEST = "javax.xml.ws.servlet.request";
/*     */   public static final String SERVLET_RESPONSE = "javax.xml.ws.servlet.response";
/*     */   public static final String SERVLET_CONTEXT = "javax.xml.ws.servlet.context";
/*     */   public static final String QUERY_STRING = "javax.xml.ws.http.request.querystring";
/*     */   public static final String PATH_INFO = "javax.xml.ws.http.request.pathinfo";
/*     */   public static final String REFERENCE_PARAMETERS = "javax.xml.ws.reference.parameters";
/*     */ 
/*     */   public abstract void setScope(String paramString, Scope paramScope);
/*     */ 
/*     */   public abstract Scope getScope(String paramString);
/*     */ 
/*     */   public static enum Scope
/*     */   {
/* 183 */     APPLICATION, HANDLER;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.ws.handler.MessageContext
 * JD-Core Version:    0.6.2
 */