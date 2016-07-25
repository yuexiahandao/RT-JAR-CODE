/*    */ package com.sun.org.apache.xerces.internal.dom;
/*    */ 
/*    */ import org.w3c.dom.CDATASection;
/*    */ 
/*    */ public class CDATASectionImpl extends TextImpl
/*    */   implements CDATASection
/*    */ {
/*    */   static final long serialVersionUID = 2372071297878177780L;
/*    */ 
/*    */   public CDATASectionImpl(CoreDocumentImpl ownerDoc, String data)
/*    */   {
/* 69 */     super(ownerDoc, data);
/*    */   }
/*    */ 
/*    */   public short getNodeType()
/*    */   {
/* 81 */     return 4;
/*    */   }
/*    */ 
/*    */   public String getNodeName()
/*    */   {
/* 86 */     return "#cdata-section";
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.dom.CDATASectionImpl
 * JD-Core Version:    0.6.2
 */