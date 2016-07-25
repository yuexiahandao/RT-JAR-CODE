/*     */ package com.sun.xml.internal.ws.api;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;
/*     */ import com.sun.xml.internal.ws.api.server.Container;
/*     */ import com.sun.xml.internal.ws.client.WSServiceDelegate;
/*     */ import java.lang.reflect.Field;
/*     */ import java.net.URL;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import javax.xml.bind.JAXBContext;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.ws.Dispatch;
/*     */ import javax.xml.ws.Service;
/*     */ import javax.xml.ws.Service.Mode;
/*     */ import javax.xml.ws.WebServiceFeature;
/*     */ import javax.xml.ws.spi.ServiceDelegate;
/*     */ 
/*     */ public abstract class WSService extends ServiceDelegate
/*     */ {
/* 161 */   protected static final ThreadLocal<InitParams> INIT_PARAMS = new ThreadLocal();
/*     */ 
/* 166 */   protected static final InitParams EMPTY_PARAMS = new InitParams();
/*     */ 
/*     */   public abstract <T> T getPort(WSEndpointReference paramWSEndpointReference, Class<T> paramClass, WebServiceFeature[] paramArrayOfWebServiceFeature);
/*     */ 
/*     */   public abstract <T> Dispatch<T> createDispatch(QName paramQName, WSEndpointReference paramWSEndpointReference, Class<T> paramClass, Service.Mode paramMode, WebServiceFeature[] paramArrayOfWebServiceFeature);
/*     */ 
/*     */   public abstract Dispatch<Object> createDispatch(QName paramQName, WSEndpointReference paramWSEndpointReference, JAXBContext paramJAXBContext, Service.Mode paramMode, WebServiceFeature[] paramArrayOfWebServiceFeature);
/*     */ 
/*     */   @NotNull
/*     */   public abstract Container getContainer();
/*     */ 
/*     */   public static WSService create(URL wsdlDocumentLocation, QName serviceName)
/*     */   {
/* 116 */     return new WSServiceDelegate(wsdlDocumentLocation, serviceName, Service.class);
/*     */   }
/*     */ 
/*     */   public static WSService create(QName serviceName)
/*     */   {
/* 127 */     return create(null, serviceName);
/*     */   }
/*     */ 
/*     */   public static WSService create()
/*     */   {
/* 134 */     return create(null, new QName(WSService.class.getName(), "dummy"));
/*     */   }
/*     */ 
/*     */   public static Service create(URL wsdlDocumentLocation, QName serviceName, InitParams properties)
/*     */   {
/* 186 */     if (INIT_PARAMS.get() != null)
/* 187 */       throw new IllegalStateException("someone left non-null InitParams");
/* 188 */     INIT_PARAMS.set(properties);
/*     */     try {
/* 190 */       Service svc = Service.create(wsdlDocumentLocation, serviceName);
/* 191 */       if (INIT_PARAMS.get() != null)
/* 192 */         throw new IllegalStateException("Service " + svc + " didn't recognize InitParams");
/* 193 */       return svc;
/*     */     }
/*     */     finally {
/* 196 */       INIT_PARAMS.set(null);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static WSService unwrap(Service svc)
/*     */   {
/* 207 */     return (WSService)AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public WSService run() {
/*     */         try {
/* 210 */           Field f = this.val$svc.getClass().getField("delegate");
/* 211 */           f.setAccessible(true);
/* 212 */           Object delegate = f.get(this.val$svc);
/* 213 */           if (!(delegate instanceof WSService))
/* 214 */             throw new IllegalArgumentException();
/* 215 */           return (WSService)delegate;
/*     */         } catch (NoSuchFieldException e) {
/* 217 */           AssertionError x = new AssertionError("Unexpected service API implementation");
/* 218 */           x.initCause(e);
/* 219 */           throw x;
/*     */         } catch (IllegalAccessException e) {
/* 221 */           IllegalAccessError x = new IllegalAccessError(e.getMessage());
/* 222 */           x.initCause(e);
/* 223 */           throw x;
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public static final class InitParams
/*     */   {
/*     */     private Container container;
/*     */ 
/*     */     public void setContainer(Container c)
/*     */     {
/* 150 */       this.container = c;
/*     */     }
/*     */     public Container getContainer() {
/* 153 */       return this.container;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.WSService
 * JD-Core Version:    0.6.2
 */