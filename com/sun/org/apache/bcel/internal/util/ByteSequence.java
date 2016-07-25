/*    */ package com.sun.org.apache.bcel.internal.util;
/*    */ 
/*    */ import java.io.ByteArrayInputStream;
/*    */ import java.io.DataInputStream;
/*    */ 
/*    */ public final class ByteSequence extends DataInputStream
/*    */ {
/*    */   private ByteArrayStream byte_stream;
/*    */ 
/*    */   public ByteSequence(byte[] bytes)
/*    */   {
/* 73 */     super(new ByteArrayStream(bytes));
/* 74 */     this.byte_stream = ((ByteArrayStream)this.in);
/*    */   }
/*    */   public final int getIndex() {
/* 77 */     return this.byte_stream.getPosition(); } 
/* 78 */   final void unreadByte() { this.byte_stream.unreadByte(); } 
/*    */   private static final class ByteArrayStream extends ByteArrayInputStream {
/*    */     ByteArrayStream(byte[] bytes) {
/* 81 */       super(); } 
/* 82 */     final int getPosition() { return this.pos; } 
/* 83 */     final void unreadByte() { if (this.pos > 0) this.pos -= 1;
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.util.ByteSequence
 * JD-Core Version:    0.6.2
 */