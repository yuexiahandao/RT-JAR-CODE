/*    */ package com.sun.xml.internal.ws.model.soap;
/*    */ 
/*    */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*    */ import com.sun.xml.internal.ws.api.model.soap.SOAPBinding;
/*    */ import javax.jws.soap.SOAPBinding.Style;
/*    */ 
/*    */ public class SOAPBindingImpl extends SOAPBinding
/*    */ {
/*    */   public SOAPBindingImpl()
/*    */   {
/*    */   }
/*    */ 
/*    */   public SOAPBindingImpl(SOAPBinding sb)
/*    */   {
/* 45 */     this.use = sb.getUse();
/* 46 */     this.style = sb.getStyle();
/* 47 */     this.soapVersion = sb.getSOAPVersion();
/* 48 */     this.soapAction = sb.getSOAPAction();
/*    */   }
/*    */ 
/*    */   public void setStyle(SOAPBinding.Style style)
/*    */   {
/* 55 */     this.style = style;
/*    */   }
/*    */ 
/*    */   public void setSOAPVersion(SOAPVersion version)
/*    */   {
/* 62 */     this.soapVersion = version;
/*    */   }
/*    */ 
/*    */   public void setSOAPAction(String soapAction)
/*    */   {
/* 69 */     this.soapAction = soapAction;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.model.soap.SOAPBindingImpl
 * JD-Core Version:    0.6.2
 */