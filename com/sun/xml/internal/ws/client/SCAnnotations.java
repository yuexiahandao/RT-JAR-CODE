/*    */ package com.sun.xml.internal.ws.client;
/*    */ 
/*    */ import com.sun.xml.internal.ws.util.JAXWSUtils;
/*    */ import java.io.IOException;
/*    */ import java.lang.reflect.Method;
/*    */ import java.net.URL;
/*    */ import java.security.AccessController;
/*    */ import java.security.PrivilegedAction;
/*    */ import java.util.ArrayList;
/*    */ import javax.xml.namespace.QName;
/*    */ import javax.xml.ws.WebEndpoint;
/*    */ import javax.xml.ws.WebServiceClient;
/*    */ import javax.xml.ws.WebServiceException;
/*    */ 
/*    */ final class SCAnnotations
/*    */ {
/*    */   QName serviceQName;
/* 85 */   final ArrayList<QName> portQNames = new ArrayList();
/* 86 */   final ArrayList<Class> classes = new ArrayList();
/*    */   URL wsdlLocation;
/*    */ 
/*    */   SCAnnotations(final Class<?> sc)
/*    */   {
/* 50 */     AccessController.doPrivileged(new PrivilegedAction() {
/*    */       public Void run() {
/* 52 */         WebServiceClient wsc = (WebServiceClient)sc.getAnnotation(WebServiceClient.class);
/* 53 */         if (wsc == null) {
/* 54 */           throw new WebServiceException("Service Interface Annotations required, exiting...");
/*    */         }
/* 56 */         String name = wsc.name();
/* 57 */         String tns = wsc.targetNamespace();
/* 58 */         SCAnnotations.this.serviceQName = new QName(tns, name);
/*    */         try {
/* 60 */           SCAnnotations.this.wsdlLocation = JAXWSUtils.getFileOrURL(wsc.wsdlLocation());
/*    */         }
/*    */         catch (IOException e) {
/* 63 */           throw new WebServiceException(e);
/*    */         }
/*    */ 
/* 66 */         for (Method method : sc.getDeclaredMethods()) {
/* 67 */           WebEndpoint webEndpoint = (WebEndpoint)method.getAnnotation(WebEndpoint.class);
/* 68 */           if (webEndpoint != null) {
/* 69 */             String endpointName = webEndpoint.name();
/* 70 */             QName portQName = new QName(tns, endpointName);
/* 71 */             SCAnnotations.this.portQNames.add(portQName);
/*    */           }
/* 73 */           Class seiClazz = method.getReturnType();
/* 74 */           if (seiClazz != Void.TYPE) {
/* 75 */             SCAnnotations.this.classes.add(seiClazz);
/*    */           }
/*    */         }
/*    */ 
/* 79 */         return null;
/*    */       }
/*    */     });
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.client.SCAnnotations
 * JD-Core Version:    0.6.2
 */