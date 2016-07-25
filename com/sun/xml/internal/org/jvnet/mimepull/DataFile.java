/*    */ package com.sun.xml.internal.org.jvnet.mimepull;
/*    */ 
/*    */ import java.io.File;
/*    */ 
/*    */ final class DataFile
/*    */ {
/*    */   private WeakDataFile weak;
/*    */   private long writePointer;
/*    */ 
/*    */   DataFile(File file)
/*    */   {
/* 42 */     this.writePointer = 0L;
/* 43 */     this.weak = new WeakDataFile(this, file);
/*    */   }
/*    */ 
/*    */   void close()
/*    */   {
/* 50 */     this.weak.close();
/*    */   }
/*    */ 
/*    */   synchronized void read(long pointer, byte[] buf, int offset, int length)
/*    */   {
/* 62 */     this.weak.read(pointer, buf, offset, length);
/*    */   }
/*    */ 
/*    */   void renameTo(File f) {
/* 66 */     this.weak.renameTo(f);
/*    */   }
/*    */ 
/*    */   synchronized long writeTo(byte[] data, int offset, int length)
/*    */   {
/* 79 */     long temp = this.writePointer;
/* 80 */     this.writePointer = this.weak.writeTo(this.writePointer, data, offset, length);
/* 81 */     return temp;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.org.jvnet.mimepull.DataFile
 * JD-Core Version:    0.6.2
 */