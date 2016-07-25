/*    */ package com.sun.org.apache.xerces.internal.impl.xs.traversers;
/*    */ 
/*    */ import com.sun.org.apache.xerces.internal.impl.xs.opti.ElementImpl;
/*    */ import org.w3c.dom.Element;
/*    */ 
/*    */ final class XSAnnotationInfo
/*    */ {
/*    */   String fAnnotation;
/*    */   int fLine;
/*    */   int fColumn;
/*    */   int fCharOffset;
/*    */   XSAnnotationInfo next;
/*    */ 
/*    */   XSAnnotationInfo(String annotation, int line, int column, int charOffset)
/*    */   {
/* 52 */     this.fAnnotation = annotation;
/* 53 */     this.fLine = line;
/* 54 */     this.fColumn = column;
/* 55 */     this.fCharOffset = charOffset;
/*    */   }
/*    */ 
/*    */   XSAnnotationInfo(String annotation, Element annotationDecl) {
/* 59 */     this.fAnnotation = annotation;
/* 60 */     if ((annotationDecl instanceof ElementImpl)) {
/* 61 */       ElementImpl annotationDeclImpl = (ElementImpl)annotationDecl;
/* 62 */       this.fLine = annotationDeclImpl.getLineNumber();
/* 63 */       this.fColumn = annotationDeclImpl.getColumnNumber();
/* 64 */       this.fCharOffset = annotationDeclImpl.getCharacterOffset();
/*    */     }
/*    */     else {
/* 67 */       this.fLine = -1;
/* 68 */       this.fColumn = -1;
/* 69 */       this.fCharOffset = -1;
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.traversers.XSAnnotationInfo
 * JD-Core Version:    0.6.2
 */