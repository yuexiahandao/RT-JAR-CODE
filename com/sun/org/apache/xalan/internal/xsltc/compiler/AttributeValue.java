/*    */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*    */ 
/*    */ abstract class AttributeValue extends Expression
/*    */ {
/*    */   public static final AttributeValue create(SyntaxTreeNode parent, String text, Parser parser)
/*    */   {
/*    */     AttributeValue result;
/*    */     AttributeValue result;
/* 37 */     if (text.indexOf('{') != -1) {
/* 38 */       result = new AttributeValueTemplate(text, parser, parent);
/*    */     }
/*    */     else
/*    */     {
/*    */       AttributeValue result;
/* 40 */       if (text.indexOf('}') != -1) {
/* 41 */         result = new AttributeValueTemplate(text, parser, parent);
/*    */       }
/*    */       else {
/* 44 */         result = new SimpleAttributeValue(text);
/* 45 */         result.setParser(parser);
/* 46 */         result.setParent(parent);
/*    */       }
/*    */     }
/* 48 */     return result;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.AttributeValue
 * JD-Core Version:    0.6.2
 */