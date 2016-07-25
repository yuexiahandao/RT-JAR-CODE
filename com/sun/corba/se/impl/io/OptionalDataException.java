/*    */ package com.sun.corba.se.impl.io;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class OptionalDataException extends IOException
/*    */ {
/*    */   public int length;
/*    */   public boolean eof;
/*    */ 
/*    */   OptionalDataException(int paramInt)
/*    */   {
/* 52 */     this.eof = false;
/* 53 */     this.length = paramInt;
/*    */   }
/*    */ 
/*    */   OptionalDataException(boolean paramBoolean)
/*    */   {
/* 61 */     this.length = 0;
/* 62 */     this.eof = paramBoolean;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.io.OptionalDataException
 * JD-Core Version:    0.6.2
 */