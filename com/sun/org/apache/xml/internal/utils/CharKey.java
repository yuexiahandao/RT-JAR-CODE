/*    */ package com.sun.org.apache.xml.internal.utils;
/*    */ 
/*    */ public class CharKey
/*    */ {
/*    */   private char m_char;
/*    */ 
/*    */   public CharKey(char key)
/*    */   {
/* 43 */     this.m_char = key;
/*    */   }
/*    */ 
/*    */   public CharKey()
/*    */   {
/*    */   }
/*    */ 
/*    */   public final void setChar(char c)
/*    */   {
/* 60 */     this.m_char = c;
/*    */   }
/*    */ 
/*    */   public final int hashCode()
/*    */   {
/* 72 */     return this.m_char;
/*    */   }
/*    */ 
/*    */   public final boolean equals(Object obj)
/*    */   {
/* 84 */     return ((CharKey)obj).m_char == this.m_char;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.utils.CharKey
 * JD-Core Version:    0.6.2
 */