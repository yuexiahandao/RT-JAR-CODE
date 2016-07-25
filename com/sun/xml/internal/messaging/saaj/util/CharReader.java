/*    */ package com.sun.xml.internal.messaging.saaj.util;
/*    */ 
/*    */ import java.io.CharArrayReader;
/*    */ 
/*    */ public class CharReader extends CharArrayReader
/*    */ {
/*    */   public CharReader(char[] buf, int length)
/*    */   {
/* 34 */     super(buf, 0, length);
/*    */   }
/*    */ 
/*    */   public CharReader(char[] buf, int offset, int length) {
/* 38 */     super(buf, offset, length);
/*    */   }
/*    */ 
/*    */   public char[] getChars() {
/* 42 */     return this.buf;
/*    */   }
/*    */ 
/*    */   public int getCount() {
/* 46 */     return this.count;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.util.CharReader
 * JD-Core Version:    0.6.2
 */