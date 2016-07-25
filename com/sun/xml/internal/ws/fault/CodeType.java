/*    */ package com.sun.xml.internal.ws.fault;
/*    */ 
/*    */ import javax.xml.bind.annotation.XmlAccessType;
/*    */ import javax.xml.bind.annotation.XmlAccessorType;
/*    */ import javax.xml.bind.annotation.XmlElement;
/*    */ import javax.xml.bind.annotation.XmlTransient;
/*    */ import javax.xml.bind.annotation.XmlType;
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ @XmlAccessorType(XmlAccessType.FIELD)
/*    */ @XmlType(name="CodeType", namespace="http://www.w3.org/2003/05/soap-envelope", propOrder={"Value", "Subcode"})
/*    */ class CodeType
/*    */ {
/*    */ 
/*    */   @XmlTransient
/*    */   private static final String ns = "http://www.w3.org/2003/05/soap-envelope";
/*    */ 
/*    */   @XmlElement(namespace="http://www.w3.org/2003/05/soap-envelope")
/*    */   private QName Value;
/*    */ 
/*    */   @XmlElement(namespace="http://www.w3.org/2003/05/soap-envelope")
/*    */   private SubcodeType Subcode;
/*    */ 
/*    */   CodeType(QName value)
/*    */   {
/* 70 */     this.Value = value;
/*    */   }
/*    */ 
/*    */   CodeType() {
/*    */   }
/*    */ 
/*    */   QName getValue() {
/* 77 */     return this.Value;
/*    */   }
/*    */ 
/*    */   SubcodeType getSubcode() {
/* 81 */     return this.Subcode;
/*    */   }
/*    */ 
/*    */   void setSubcode(SubcodeType subcode) {
/* 85 */     this.Subcode = subcode;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.fault.CodeType
 * JD-Core Version:    0.6.2
 */