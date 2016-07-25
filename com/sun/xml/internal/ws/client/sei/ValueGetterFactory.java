/*    */ package com.sun.xml.internal.ws.client.sei;
/*    */ 
/*    */ import com.sun.xml.internal.ws.model.ParameterImpl;
/*    */ import javax.jws.WebParam.Mode;
/*    */ 
/*    */ abstract class ValueGetterFactory
/*    */ {
/* 41 */   static final ValueGetterFactory SYNC = new ValueGetterFactory() {
/*    */     ValueGetter get(ParameterImpl p) {
/* 43 */       return (p.getMode() == WebParam.Mode.IN) || (p.getIndex() == -1) ? ValueGetter.PLAIN : ValueGetter.HOLDER;
/*    */     }
/* 41 */   };
/*    */ 
/* 52 */   static final ValueGetterFactory ASYNC = new ValueGetterFactory() {
/*    */     ValueGetter get(ParameterImpl p) {
/* 54 */       return ValueGetter.PLAIN;
/*    */     }
/* 52 */   };
/*    */ 
/*    */   abstract ValueGetter get(ParameterImpl paramParameterImpl);
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.client.sei.ValueGetterFactory
 * JD-Core Version:    0.6.2
 */