/*     */ package sun.net.httpserver;
/*     */ 
/*     */ import com.sun.net.httpserver.Authenticator;
/*     */ import com.sun.net.httpserver.Filter;
/*     */ import com.sun.net.httpserver.HttpContext;
/*     */ import com.sun.net.httpserver.HttpHandler;
/*     */ import com.sun.net.httpserver.HttpServer;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ class HttpContextImpl extends HttpContext
/*     */ {
/*     */   private String path;
/*     */   private String protocol;
/*     */   private HttpHandler handler;
/*  46 */   private Map<String, Object> attributes = new HashMap();
/*     */   private ServerImpl server;
/*  49 */   private LinkedList<Filter> sfilters = new LinkedList();
/*     */ 
/*  51 */   private LinkedList<Filter> ufilters = new LinkedList();
/*     */   private Authenticator authenticator;
/*     */   private AuthFilter authfilter;
/*     */ 
/*     */   HttpContextImpl(String paramString1, String paramString2, HttpHandler paramHttpHandler, ServerImpl paramServerImpl)
/*     */   {
/*  59 */     if ((paramString2 == null) || (paramString1 == null) || (paramString2.length() < 1) || (paramString2.charAt(0) != '/')) {
/*  60 */       throw new IllegalArgumentException("Illegal value for path or protocol");
/*     */     }
/*  62 */     this.protocol = paramString1.toLowerCase();
/*  63 */     this.path = paramString2;
/*  64 */     if ((!this.protocol.equals("http")) && (!this.protocol.equals("https"))) {
/*  65 */       throw new IllegalArgumentException("Illegal value for protocol");
/*     */     }
/*  67 */     this.handler = paramHttpHandler;
/*  68 */     this.server = paramServerImpl;
/*  69 */     this.authfilter = new AuthFilter(null);
/*  70 */     this.sfilters.add(this.authfilter);
/*     */   }
/*     */ 
/*     */   public HttpHandler getHandler()
/*     */   {
/*  78 */     return this.handler;
/*     */   }
/*     */ 
/*     */   public void setHandler(HttpHandler paramHttpHandler) {
/*  82 */     if (paramHttpHandler == null) {
/*  83 */       throw new NullPointerException("Null handler parameter");
/*     */     }
/*  85 */     if (this.handler != null) {
/*  86 */       throw new IllegalArgumentException("handler already set");
/*     */     }
/*  88 */     this.handler = paramHttpHandler;
/*     */   }
/*     */ 
/*     */   public String getPath()
/*     */   {
/*  96 */     return this.path;
/*     */   }
/*     */ 
/*     */   public HttpServer getServer()
/*     */   {
/* 104 */     return this.server.getWrapper();
/*     */   }
/*     */ 
/*     */   ServerImpl getServerImpl() {
/* 108 */     return this.server;
/*     */   }
/*     */ 
/*     */   public String getProtocol()
/*     */   {
/* 116 */     return this.protocol;
/*     */   }
/*     */ 
/*     */   public Map<String, Object> getAttributes()
/*     */   {
/* 128 */     return this.attributes;
/*     */   }
/*     */ 
/*     */   public List<Filter> getFilters() {
/* 132 */     return this.ufilters;
/*     */   }
/*     */ 
/*     */   List<Filter> getSystemFilters() {
/* 136 */     return this.sfilters;
/*     */   }
/*     */ 
/*     */   public Authenticator setAuthenticator(Authenticator paramAuthenticator) {
/* 140 */     Authenticator localAuthenticator = this.authenticator;
/* 141 */     this.authenticator = paramAuthenticator;
/* 142 */     this.authfilter.setAuthenticator(paramAuthenticator);
/* 143 */     return localAuthenticator;
/*     */   }
/*     */ 
/*     */   public Authenticator getAuthenticator() {
/* 147 */     return this.authenticator;
/*     */   }
/*     */   Logger getLogger() {
/* 150 */     return this.server.getLogger();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.httpserver.HttpContextImpl
 * JD-Core Version:    0.6.2
 */