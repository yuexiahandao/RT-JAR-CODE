/*    */ package com.sun.xml.internal.ws.addressing;
/*    */ 
/*    */ import javax.xml.bind.annotation.XmlElement;
/*    */ import javax.xml.bind.annotation.XmlRootElement;
/*    */ 
/*    */ @XmlRootElement(name="ProblemAction", namespace="http://www.w3.org/2005/08/addressing")
/*    */ public class ProblemAction
/*    */ {
/*    */ 
/*    */   @XmlElement(name="Action", namespace="http://www.w3.org/2005/08/addressing")
/*    */   private String action;
/*    */ 
/*    */   @XmlElement(name="SoapAction", namespace="http://www.w3.org/2005/08/addressing")
/*    */   private String soapAction;
/*    */ 
/*    */   public ProblemAction()
/*    */   {
/*    */   }
/*    */ 
/*    */   public ProblemAction(String action)
/*    */   {
/* 50 */     this.action = action;
/*    */   }
/*    */ 
/*    */   public ProblemAction(String action, String soapAction) {
/* 54 */     this.action = action;
/* 55 */     this.soapAction = soapAction;
/*    */   }
/*    */ 
/*    */   public String getAction() {
/* 59 */     return this.action;
/*    */   }
/*    */ 
/*    */   public String getSoapAction() {
/* 63 */     return this.soapAction;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.addressing.ProblemAction
 * JD-Core Version:    0.6.2
 */