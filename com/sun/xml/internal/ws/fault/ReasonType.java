/*    */ package com.sun.xml.internal.ws.fault;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import javax.xml.bind.annotation.XmlElements;
/*    */ 
/*    */ class ReasonType
/*    */ {
/*    */ 
/*    */   @XmlElements({@javax.xml.bind.annotation.XmlElement(name="Text", namespace="http://www.w3.org/2003/05/soap-envelope", type=TextType.class)})
/* 53 */   private final List<TextType> text = new ArrayList();
/*    */ 
/*    */   ReasonType()
/*    */   {
/*    */   }
/*    */ 
/*    */   ReasonType(String txt)
/*    */   {
/* 45 */     this.text.add(new TextType(txt));
/*    */   }
/*    */ 
/*    */   List<TextType> texts()
/*    */   {
/* 57 */     return this.text;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.fault.ReasonType
 * JD-Core Version:    0.6.2
 */