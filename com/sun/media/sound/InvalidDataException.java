/*    */ package com.sun.media.sound;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class InvalidDataException extends IOException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */ 
/*    */   public InvalidDataException()
/*    */   {
/* 39 */     super("Invalid Data!");
/*    */   }
/*    */ 
/*    */   public InvalidDataException(String paramString) {
/* 43 */     super(paramString);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.InvalidDataException
 * JD-Core Version:    0.6.2
 */