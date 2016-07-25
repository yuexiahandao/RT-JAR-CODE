/*    */ package com.sun.xml.internal.org.jvnet.mimepull;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ 
/*    */ final class ChunkInputStream extends InputStream
/*    */ {
/*    */   Chunk current;
/*    */   int offset;
/*    */   int len;
/*    */   final MIMEMessage msg;
/*    */   final MIMEPart part;
/*    */   byte[] buf;
/*    */ 
/*    */   public ChunkInputStream(MIMEMessage msg, MIMEPart part, Chunk startPos)
/*    */   {
/* 46 */     this.current = startPos;
/* 47 */     this.len = this.current.data.size();
/* 48 */     this.buf = this.current.data.read();
/* 49 */     this.msg = msg;
/* 50 */     this.part = part;
/*    */   }
/*    */ 
/*    */   public int read(byte[] b, int off, int sz) throws IOException
/*    */   {
/* 55 */     if (!fetch()) return -1;
/*    */ 
/* 57 */     sz = Math.min(sz, this.len - this.offset);
/* 58 */     System.arraycopy(this.buf, this.offset, b, off, sz);
/* 59 */     return sz;
/*    */   }
/*    */ 
/*    */   public int read() throws IOException {
/* 63 */     if (!fetch()) return -1;
/* 64 */     return this.buf[(this.offset++)] & 0xFF;
/*    */   }
/*    */ 
/*    */   private boolean fetch()
/*    */   {
/* 72 */     if (this.current == null) {
/* 73 */       throw new IllegalStateException("Stream already closed");
/*    */     }
/* 75 */     while (this.offset == this.len) {
/* 76 */       while ((!this.part.parsed) && (this.current.next == null)) {
/* 77 */         this.msg.makeProgress();
/*    */       }
/* 79 */       this.current = this.current.next;
/*    */ 
/* 81 */       if (this.current == null) {
/* 82 */         return false;
/*    */       }
/* 84 */       this.offset = 0;
/* 85 */       this.buf = this.current.data.read();
/* 86 */       this.len = this.current.data.size();
/*    */     }
/* 88 */     return true;
/*    */   }
/*    */ 
/*    */   public void close() throws IOException {
/* 92 */     super.close();
/* 93 */     this.current = null;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.org.jvnet.mimepull.ChunkInputStream
 * JD-Core Version:    0.6.2
 */