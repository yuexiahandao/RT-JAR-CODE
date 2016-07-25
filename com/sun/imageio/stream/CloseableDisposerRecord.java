/*    */ package com.sun.imageio.stream;
/*    */ 
/*    */ import java.io.Closeable;
/*    */ import java.io.IOException;
/*    */ import sun.java2d.DisposerRecord;
/*    */ 
/*    */ public class CloseableDisposerRecord
/*    */   implements DisposerRecord
/*    */ {
/*    */   private Closeable closeable;
/*    */ 
/*    */   public CloseableDisposerRecord(Closeable paramCloseable)
/*    */   {
/* 41 */     this.closeable = paramCloseable;
/*    */   }
/*    */ 
/*    */   public synchronized void dispose() {
/* 45 */     if (this.closeable != null)
/*    */       try {
/* 47 */         this.closeable.close();
/*    */       } catch (IOException localIOException) {
/*    */       } finally {
/* 50 */         this.closeable = null;
/*    */       }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.stream.CloseableDisposerRecord
 * JD-Core Version:    0.6.2
 */