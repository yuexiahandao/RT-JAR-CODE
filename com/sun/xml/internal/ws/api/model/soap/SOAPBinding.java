/*    */ package com.sun.xml.internal.ws.api.model.soap;
/*    */ 
/*    */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*    */ import javax.jws.soap.SOAPBinding.Style;
/*    */ import javax.jws.soap.SOAPBinding.Use;
/*    */ 
/*    */ public abstract class SOAPBinding
/*    */ {
/* 42 */   protected SOAPBinding.Use use = SOAPBinding.Use.LITERAL;
/* 43 */   protected SOAPBinding.Style style = SOAPBinding.Style.DOCUMENT;
/* 44 */   protected SOAPVersion soapVersion = SOAPVersion.SOAP_11;
/* 45 */   protected String soapAction = "";
/*    */ 
/*    */   public SOAPBinding.Use getUse()
/*    */   {
/* 51 */     return this.use;
/*    */   }
/*    */ 
/*    */   public SOAPBinding.Style getStyle()
/*    */   {
/* 58 */     return this.style;
/*    */   }
/*    */ 
/*    */   public SOAPVersion getSOAPVersion()
/*    */   {
/* 65 */     return this.soapVersion;
/*    */   }
/*    */ 
/*    */   public boolean isDocLit()
/*    */   {
/* 72 */     return (this.style == SOAPBinding.Style.DOCUMENT) && (this.use == SOAPBinding.Use.LITERAL);
/*    */   }
/*    */ 
/*    */   public boolean isRpcLit()
/*    */   {
/* 79 */     return (this.style == SOAPBinding.Style.RPC) && (this.use == SOAPBinding.Use.LITERAL);
/*    */   }
/*    */ 
/*    */   public String getSOAPAction()
/*    */   {
/* 99 */     return this.soapAction;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.model.soap.SOAPBinding
 * JD-Core Version:    0.6.2
 */