/*    */ package com.sun.xml.internal.ws.addressing.v200408;
/*    */ 
/*    */ import javax.xml.bind.annotation.XmlRootElement;
/*    */ import javax.xml.bind.annotation.XmlValue;
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ @XmlRootElement(name="ProblemHeaderQName", namespace="http://schemas.xmlsoap.org/ws/2004/08/addressing")
/*    */ public class ProblemHeaderQName
/*    */ {
/*    */ 
/*    */   @XmlValue
/*    */   private QName value;
/*    */ 
/*    */   public ProblemHeaderQName()
/*    */   {
/*    */   }
/*    */ 
/*    */   public ProblemHeaderQName(QName name)
/*    */   {
/* 47 */     this.value = name;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.addressing.v200408.ProblemHeaderQName
 * JD-Core Version:    0.6.2
 */