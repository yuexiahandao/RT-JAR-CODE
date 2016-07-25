/*    */ package com.sun.xml.internal.messaging.saaj.util;
/*    */ 
/*    */ import java.io.CharArrayWriter;
/*    */ 
/*    */ public class CharWriter extends CharArrayWriter
/*    */ {
/*    */   public CharWriter()
/*    */   {
/*    */   }
/*    */ 
/*    */   public CharWriter(int size)
/*    */   {
/* 38 */     super(size);
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
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.util.CharWriter
 * JD-Core Version:    0.6.2
 */