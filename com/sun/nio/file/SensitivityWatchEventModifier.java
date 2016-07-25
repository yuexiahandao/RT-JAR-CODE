/*    */ package com.sun.nio.file;
/*    */ 
/*    */ import java.nio.file.WatchEvent.Modifier;
/*    */ 
/*    */ public enum SensitivityWatchEventModifier
/*    */   implements WatchEvent.Modifier
/*    */ {
/* 41 */   HIGH(2), 
/*    */ 
/* 45 */   MEDIUM(10), 
/*    */ 
/* 49 */   LOW(30);
/*    */ 
/*    */   private final int sensitivity;
/*    */ 
/*    */   public int sensitivityValueInSeconds()
/*    */   {
/* 55 */     return this.sensitivity;
/*    */   }
/*    */ 
/*    */   private SensitivityWatchEventModifier(int paramInt)
/*    */   {
/* 60 */     this.sensitivity = paramInt;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.nio.file.SensitivityWatchEventModifier
 * JD-Core Version:    0.6.2
 */