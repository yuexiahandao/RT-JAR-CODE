/*    */ package com.sun.xml.internal.messaging.saaj.soap.dynamic;
/*    */ 
/*    */ import com.sun.xml.internal.messaging.saaj.soap.SOAPDocumentImpl;
/*    */ import com.sun.xml.internal.messaging.saaj.soap.SOAPFactoryImpl;
/*    */ import javax.xml.soap.Detail;
/*    */ import javax.xml.soap.SOAPException;
/*    */ 
/*    */ public class SOAPFactoryDynamicImpl extends SOAPFactoryImpl
/*    */ {
/*    */   protected SOAPDocumentImpl createDocument()
/*    */   {
/* 40 */     return null;
/*    */   }
/*    */ 
/*    */   public Detail createDetail() throws SOAPException {
/* 44 */     throw new UnsupportedOperationException("createDetail() not supported for Dynamic Protocol");
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.soap.dynamic.SOAPFactoryDynamicImpl
 * JD-Core Version:    0.6.2
 */