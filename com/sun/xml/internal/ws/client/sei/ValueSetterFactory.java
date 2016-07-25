/*    */ package com.sun.xml.internal.ws.client.sei;
/*    */ 
/*    */ import com.sun.xml.internal.ws.model.ParameterImpl;
/*    */ import javax.xml.ws.WebServiceException;
/*    */ 
/*    */ abstract class ValueSetterFactory
/*    */ {
/* 41 */   static final ValueSetterFactory SYNC = new ValueSetterFactory() {
/*    */     ValueSetter get(ParameterImpl p) {
/* 43 */       return ValueSetter.getSync(p);
/*    */     }
/* 41 */   };
/*    */ 
/* 47 */   static final ValueSetterFactory NONE = new ValueSetterFactory() {
/*    */     ValueSetter get(ParameterImpl p) {
/* 49 */       throw new WebServiceException("This shouldn't happen. No response parameters.");
/*    */     }
/* 47 */   };
/*    */ 
/* 53 */   static final ValueSetterFactory SINGLE = new ValueSetterFactory() {
/*    */     ValueSetter get(ParameterImpl p) {
/* 55 */       return ValueSetter.SINGLE_VALUE;
/*    */     }
/* 53 */   };
/*    */ 
/*    */   abstract ValueSetter get(ParameterImpl paramParameterImpl);
/*    */ 
/*    */   static final class AsyncBeanValueSetterFactory extends ValueSetterFactory
/*    */   {
/*    */     private Class asyncBean;
/*    */ 
/*    */     AsyncBeanValueSetterFactory(Class asyncBean)
/*    */     {
/* 63 */       this.asyncBean = asyncBean;
/*    */     }
/*    */ 
/*    */     ValueSetter get(ParameterImpl p) {
/* 67 */       return new ValueSetter.AsyncBeanValueSetter(p, this.asyncBean);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.client.sei.ValueSetterFactory
 * JD-Core Version:    0.6.2
 */