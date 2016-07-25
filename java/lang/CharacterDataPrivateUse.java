/*    */ package java.lang;
/*    */ 
/*    */ class CharacterDataPrivateUse extends CharacterData
/*    */ {
/* 97 */   static final CharacterData instance = new CharacterDataPrivateUse();
/*    */ 
/*    */   int getProperties(int paramInt)
/*    */   {
/* 34 */     return 0;
/*    */   }
/*    */ 
/*    */   int getType(int paramInt) {
/* 38 */     return (paramInt & 0xFFFE) == 65534 ? 0 : 18;
/*    */   }
/*    */ 
/*    */   boolean isJavaIdentifierStart(int paramInt)
/*    */   {
/* 44 */     return false;
/*    */   }
/*    */ 
/*    */   boolean isJavaIdentifierPart(int paramInt) {
/* 48 */     return false;
/*    */   }
/*    */ 
/*    */   boolean isUnicodeIdentifierStart(int paramInt) {
/* 52 */     return false;
/*    */   }
/*    */ 
/*    */   boolean isUnicodeIdentifierPart(int paramInt) {
/* 56 */     return false;
/*    */   }
/*    */ 
/*    */   boolean isIdentifierIgnorable(int paramInt) {
/* 60 */     return false;
/*    */   }
/*    */ 
/*    */   int toLowerCase(int paramInt) {
/* 64 */     return paramInt;
/*    */   }
/*    */ 
/*    */   int toUpperCase(int paramInt) {
/* 68 */     return paramInt;
/*    */   }
/*    */ 
/*    */   int toTitleCase(int paramInt) {
/* 72 */     return paramInt;
/*    */   }
/*    */ 
/*    */   int digit(int paramInt1, int paramInt2) {
/* 76 */     return -1;
/*    */   }
/*    */ 
/*    */   int getNumericValue(int paramInt) {
/* 80 */     return -1;
/*    */   }
/*    */ 
/*    */   boolean isWhitespace(int paramInt) {
/* 84 */     return false;
/*    */   }
/*    */ 
/*    */   byte getDirectionality(int paramInt) {
/* 88 */     return (paramInt & 0xFFFE) == 65534 ? -1 : 0;
/*    */   }
/*    */ 
/*    */   boolean isMirrored(int paramInt)
/*    */   {
/* 94 */     return false;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.CharacterDataPrivateUse
 * JD-Core Version:    0.6.2
 */