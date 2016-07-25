/*    */ package com.sun.xml.internal.org.jvnet.fastinfoset;
/*    */ 
/*    */ public class ExternalVocabulary
/*    */ {
/*    */   public final String URI;
/*    */   public final Vocabulary vocabulary;
/*    */ 
/*    */   public ExternalVocabulary(String URI, Vocabulary vocabulary)
/*    */   {
/* 50 */     if ((URI == null) || (vocabulary == null)) {
/* 51 */       throw new IllegalArgumentException();
/*    */     }
/*    */ 
/* 54 */     this.URI = URI;
/* 55 */     this.vocabulary = vocabulary;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.org.jvnet.fastinfoset.ExternalVocabulary
 * JD-Core Version:    0.6.2
 */