/*    */ package com.sun.xml.internal.ws.fault;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import javax.xml.bind.annotation.XmlAttribute;
/*    */ import javax.xml.bind.annotation.XmlType;
/*    */ import javax.xml.bind.annotation.XmlValue;
/*    */ 
/*    */ @XmlType(name="TextType", namespace="http://www.w3.org/2003/05/soap-envelope")
/*    */ class TextType
/*    */ {
/*    */ 
/*    */   @XmlValue
/*    */   private String text;
/*    */ 
/*    */   @XmlAttribute(name="lang", namespace="http://www.w3.org/XML/1998/namespace", required=true)
/*    */   private String lang;
/*    */ 
/*    */   TextType()
/*    */   {
/*    */   }
/*    */ 
/*    */   TextType(String text)
/*    */   {
/* 52 */     this.text = text;
/* 53 */     this.lang = Locale.getDefault().getLanguage();
/*    */   }
/*    */ 
/*    */   String getText() {
/* 57 */     return this.text;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.fault.TextType
 * JD-Core Version:    0.6.2
 */