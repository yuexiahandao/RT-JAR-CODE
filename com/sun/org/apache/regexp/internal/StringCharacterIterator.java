/*    */ package com.sun.org.apache.regexp.internal;
/*    */ 
/*    */ public final class StringCharacterIterator
/*    */   implements CharacterIterator
/*    */ {
/*    */   private final String src;
/*    */ 
/*    */   public StringCharacterIterator(String src)
/*    */   {
/* 36 */     this.src = src;
/*    */   }
/*    */ 
/*    */   public String substring(int beginIndex, int endIndex)
/*    */   {
/* 42 */     return this.src.substring(beginIndex, endIndex);
/*    */   }
/*    */ 
/*    */   public String substring(int beginIndex)
/*    */   {
/* 48 */     return this.src.substring(beginIndex);
/*    */   }
/*    */ 
/*    */   public char charAt(int pos)
/*    */   {
/* 54 */     return this.src.charAt(pos);
/*    */   }
/*    */ 
/*    */   public boolean isEnd(int pos)
/*    */   {
/* 60 */     return pos >= this.src.length();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.regexp.internal.StringCharacterIterator
 * JD-Core Version:    0.6.2
 */