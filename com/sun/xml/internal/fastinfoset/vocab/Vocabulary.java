/*    */ package com.sun.xml.internal.fastinfoset.vocab;
/*    */ 
/*    */ public abstract class Vocabulary
/*    */ {
/*    */   public static final int RESTRICTED_ALPHABET = 0;
/*    */   public static final int ENCODING_ALGORITHM = 1;
/*    */   public static final int PREFIX = 2;
/*    */   public static final int NAMESPACE_NAME = 3;
/*    */   public static final int LOCAL_NAME = 4;
/*    */   public static final int OTHER_NCNAME = 5;
/*    */   public static final int OTHER_URI = 6;
/*    */   public static final int ATTRIBUTE_VALUE = 7;
/*    */   public static final int OTHER_STRING = 8;
/*    */   public static final int CHARACTER_CONTENT_CHUNK = 9;
/*    */   public static final int ELEMENT_NAME = 10;
/*    */   public static final int ATTRIBUTE_NAME = 11;
/*    */   protected boolean _hasInitialReadOnlyVocabulary;
/*    */   protected String _referencedVocabularyURI;
/*    */ 
/*    */   public boolean hasInitialVocabulary()
/*    */   {
/* 50 */     return this._hasInitialReadOnlyVocabulary;
/*    */   }
/*    */ 
/*    */   protected void setInitialReadOnlyVocabulary(boolean hasInitialReadOnlyVocabulary) {
/* 54 */     this._hasInitialReadOnlyVocabulary = hasInitialReadOnlyVocabulary;
/*    */   }
/*    */ 
/*    */   public boolean hasExternalVocabulary() {
/* 58 */     return this._referencedVocabularyURI != null;
/*    */   }
/*    */ 
/*    */   public String getExternalVocabularyURI() {
/* 62 */     return this._referencedVocabularyURI;
/*    */   }
/*    */ 
/*    */   protected void setExternalVocabularyURI(String referencedVocabularyURI) {
/* 66 */     this._referencedVocabularyURI = referencedVocabularyURI;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.vocab.Vocabulary
 * JD-Core Version:    0.6.2
 */