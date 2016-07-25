/*    */ package com.sun.xml.internal.ws.server.sei;
/*    */ 
/*    */ import com.sun.xml.internal.ws.model.ParameterImpl;
/*    */ import javax.jws.WebParam.Mode;
/*    */ import javax.xml.ws.Holder;
/*    */ 
/*    */  enum ValueGetter
/*    */ {
/* 54 */   PLAIN, 
/*    */ 
/* 67 */   HOLDER;
/*    */ 
/*    */   abstract Object get(Object paramObject);
/*    */ 
/*    */   static ValueGetter get(ParameterImpl p)
/*    */   {
/* 86 */     if ((p.getMode() == WebParam.Mode.IN) || (p.getIndex() == -1)) {
/* 87 */       return PLAIN;
/*    */     }
/* 89 */     return HOLDER;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.server.sei.ValueGetter
 * JD-Core Version:    0.6.2
 */