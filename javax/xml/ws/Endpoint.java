/*     */ package javax.xml.ws;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.Executor;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.ws.spi.Provider;
/*     */ import javax.xml.ws.spi.http.HttpContext;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ public abstract class Endpoint
/*     */ {
/*     */   public static final String WSDL_SERVICE = "javax.xml.ws.wsdl.service";
/*     */   public static final String WSDL_PORT = "javax.xml.ws.wsdl.port";
/*     */ 
/*     */   public static Endpoint create(Object implementor)
/*     */   {
/* 101 */     return create(null, implementor);
/*     */   }
/*     */ 
/*     */   public static Endpoint create(Object implementor, WebServiceFeature[] features)
/*     */   {
/* 126 */     return create(null, implementor, features);
/*     */   }
/*     */ 
/*     */   public static Endpoint create(String bindingId, Object implementor)
/*     */   {
/* 147 */     return Provider.provider().createEndpoint(bindingId, implementor);
/*     */   }
/*     */ 
/*     */   public static Endpoint create(String bindingId, Object implementor, WebServiceFeature[] features)
/*     */   {
/* 172 */     return Provider.provider().createEndpoint(bindingId, implementor, features);
/*     */   }
/*     */ 
/*     */   public abstract Binding getBinding();
/*     */ 
/*     */   public abstract Object getImplementor();
/*     */ 
/*     */   public abstract void publish(String paramString);
/*     */ 
/*     */   public static Endpoint publish(String address, Object implementor)
/*     */   {
/* 240 */     return Provider.provider().createAndPublishEndpoint(address, implementor);
/*     */   }
/*     */ 
/*     */   public static Endpoint publish(String address, Object implementor, WebServiceFeature[] features)
/*     */   {
/* 272 */     return Provider.provider().createAndPublishEndpoint(address, implementor, features);
/*     */   }
/*     */ 
/*     */   public abstract void publish(Object paramObject);
/*     */ 
/*     */   public void publish(HttpContext serverContext)
/*     */   {
/* 336 */     throw new UnsupportedOperationException("JAX-WS 2.2 implementation must override this default behaviour.");
/*     */   }
/*     */ 
/*     */   public abstract void stop();
/*     */ 
/*     */   public abstract boolean isPublished();
/*     */ 
/*     */   public abstract List<Source> getMetadata();
/*     */ 
/*     */   public abstract void setMetadata(List<Source> paramList);
/*     */ 
/*     */   public abstract Executor getExecutor();
/*     */ 
/*     */   public abstract void setExecutor(Executor paramExecutor);
/*     */ 
/*     */   public abstract Map<String, Object> getProperties();
/*     */ 
/*     */   public abstract void setProperties(Map<String, Object> paramMap);
/*     */ 
/*     */   public abstract EndpointReference getEndpointReference(Element[] paramArrayOfElement);
/*     */ 
/*     */   public abstract <T extends EndpointReference> T getEndpointReference(Class<T> paramClass, Element[] paramArrayOfElement);
/*     */ 
/*     */   public void setEndpointContext(EndpointContext ctxt)
/*     */   {
/* 496 */     throw new UnsupportedOperationException("JAX-WS 2.2 implementation must override this default behaviour.");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.ws.Endpoint
 * JD-Core Version:    0.6.2
 */