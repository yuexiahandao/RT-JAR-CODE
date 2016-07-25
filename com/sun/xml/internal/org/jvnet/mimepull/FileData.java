/*    */ package com.sun.xml.internal.org.jvnet.mimepull;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ 
/*    */ final class FileData
/*    */   implements Data
/*    */ {
/*    */   private final DataFile file;
/*    */   private final long pointer;
/*    */   private final int length;
/*    */ 
/*    */   FileData(DataFile file, ByteBuffer buf)
/*    */   {
/* 42 */     this(file, file.writeTo(buf.array(), 0, buf.limit()), buf.limit());
/*    */   }
/*    */ 
/*    */   FileData(DataFile file, long pointer, int length) {
/* 46 */     this.file = file;
/* 47 */     this.pointer = pointer;
/* 48 */     this.length = length;
/*    */   }
/*    */ 
/*    */   public byte[] read() {
/* 52 */     byte[] buf = new byte[this.length];
/* 53 */     this.file.read(this.pointer, buf, 0, this.length);
/* 54 */     return buf;
/*    */   }
/*    */ 
/*    */   public long writeTo(DataFile file)
/*    */   {
/* 61 */     throw new IllegalStateException();
/*    */   }
/*    */ 
/*    */   public int size() {
/* 65 */     return this.length;
/*    */   }
/*    */ 
/*    */   public Data createNext(DataHead dataHead, ByteBuffer buf)
/*    */   {
/* 72 */     return new FileData(this.file, buf);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.org.jvnet.mimepull.FileData
 * JD-Core Version:    0.6.2
 */