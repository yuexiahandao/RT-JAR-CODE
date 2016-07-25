/*    */ package com.sun.org.apache.xalan.internal.xsltc.runtime;
/*    */ 
/*    */ import com.sun.org.apache.xalan.internal.xsltc.DOM;
/*    */ import org.xml.sax.AttributeList;
/*    */ 
/*    */ public final class Attributes
/*    */   implements AttributeList
/*    */ {
/*    */   private int _element;
/*    */   private DOM _document;
/*    */ 
/*    */   public Attributes(DOM document, int element)
/*    */   {
/* 38 */     this._element = element;
/* 39 */     this._document = document;
/*    */   }
/*    */ 
/*    */   public int getLength() {
/* 43 */     return 0;
/*    */   }
/*    */ 
/*    */   public String getName(int i) {
/* 47 */     return null;
/*    */   }
/*    */ 
/*    */   public String getType(int i) {
/* 51 */     return null;
/*    */   }
/*    */ 
/*    */   public String getType(String name) {
/* 55 */     return null;
/*    */   }
/*    */ 
/*    */   public String getValue(int i) {
/* 59 */     return null;
/*    */   }
/*    */ 
/*    */   public String getValue(String name) {
/* 63 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.runtime.Attributes
 * JD-Core Version:    0.6.2
 */