/*    */ package java.lang;
/*    */ 
/*    */ class CharacterDataUndefined extends CharacterData
/*    */ {
/* 93 */   static final CharacterData instance = new CharacterDataUndefined();
/*    */ 
/*    */   int getProperties(int paramInt)
/*    */   {
/* 34 */     return 0;
/*    */   }
/*    */ 
/*    */   int getType(int paramInt) {
/* 38 */     return 0;
/*    */   }
/*    */ 
/*    */   boolean isJavaIdentifierStart(int paramInt) {
/* 42 */     return false;
/*    */   }
/*    */ 
/*    */   boolean isJavaIdentifierPart(int paramInt) {
/* 46 */     return false;
/*    */   }
/*    */ 
/*    */   boolean isUnicodeIdentifierStart(int paramInt) {
/* 50 */     return false;
/*    */   }
/*    */ 
/*    */   boolean isUnicodeIdentifierPart(int paramInt) {
/* 54 */     return false;
/*    */   }
/*    */ 
/*    */   boolean isIdentifierIgnorable(int paramInt) {
/* 58 */     return false;
/*    */   }
/*    */ 
/*    */   int toLowerCase(int paramInt) {
/* 62 */     return paramInt;
/*    */   }
/*    */ 
/*    */   int toUpperCase(int paramInt) {
/* 66 */     return paramInt;
/*    */   }
/*    */ 
/*    */   int toTitleCase(int paramInt) {
/* 70 */     return paramInt;
/*    */   }
/*    */ 
/*    */   int digit(int paramInt1, int paramInt2) {
/* 74 */     return -1;
/*    */   }
/*    */ 
/*    */   int getNumericValue(int paramInt) {
/* 78 */     return -1;
/*    */   }
/*    */ 
/*    */   boolean isWhitespace(int paramInt) {
/* 82 */     return false;
/*    */   }
/*    */ 
/*    */   byte getDirectionality(int paramInt) {
/* 86 */     return -1;
/*    */   }
/*    */ 
/*    */   boolean isMirrored(int paramInt) {
/* 90 */     return false;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.CharacterDataUndefined
 * JD-Core Version:    0.6.2
 */