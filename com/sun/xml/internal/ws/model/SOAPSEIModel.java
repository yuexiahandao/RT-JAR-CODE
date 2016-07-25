/*    */ package com.sun.xml.internal.ws.model;
/*    */ 
/*    */ import com.sun.xml.internal.ws.api.model.ParameterBinding;
/*    */ import java.util.HashSet;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import javax.jws.WebParam.Mode;
/*    */ import javax.xml.namespace.QName;
/*    */ import javax.xml.ws.WebServiceFeature;
/*    */ 
/*    */ public class SOAPSEIModel extends AbstractSEIModelImpl
/*    */ {
/*    */   public SOAPSEIModel(WebServiceFeature[] features)
/*    */   {
/* 45 */     super(features);
/*    */   }
/*    */ 
/*    */   protected void populateMaps()
/*    */   {
/* 50 */     int emptyBodyCount = 0;
/* 51 */     for (JavaMethodImpl jm : getJavaMethods()) {
/* 52 */       put(jm.getMethod(), jm);
/* 53 */       boolean bodyFound = false;
/* 54 */       for (ParameterImpl p : jm.getRequestParameters()) {
/* 55 */         ParameterBinding binding = p.getBinding();
/* 56 */         if (binding.isBody()) {
/* 57 */           put(p.getName(), jm);
/* 58 */           bodyFound = true;
/*    */         }
/*    */       }
/* 61 */       if (!bodyFound) {
/* 62 */         put(this.emptyBodyName, jm);
/*    */ 
/* 64 */         emptyBodyCount++;
/*    */       }
/*    */     }
/* 67 */     if (emptyBodyCount > 1);
/*    */   }
/*    */ 
/*    */   public Set<QName> getKnownHeaders()
/*    */   {
/* 74 */     Set headers = new HashSet();
/* 75 */     for (JavaMethodImpl method : getJavaMethods())
/*    */     {
/* 77 */       Iterator params = method.getRequestParameters().iterator();
/* 78 */       fillHeaders(params, headers, WebParam.Mode.IN);
/*    */ 
/* 81 */       params = method.getResponseParameters().iterator();
/* 82 */       fillHeaders(params, headers, WebParam.Mode.OUT);
/*    */     }
/* 84 */     return headers;
/*    */   }
/*    */ 
/*    */   private void fillHeaders(Iterator<ParameterImpl> params, Set<QName> headers, WebParam.Mode mode)
/*    */   {
/* 92 */     while (params.hasNext()) {
/* 93 */       ParameterImpl param = (ParameterImpl)params.next();
/* 94 */       ParameterBinding binding = mode == WebParam.Mode.IN ? param.getInBinding() : param.getOutBinding();
/* 95 */       QName name = param.getName();
/* 96 */       if ((binding.isHeader()) && (!headers.contains(name)))
/* 97 */         headers.add(name);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.model.SOAPSEIModel
 * JD-Core Version:    0.6.2
 */