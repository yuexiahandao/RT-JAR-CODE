/*    */ package com.sun.xml.internal.org.jvnet.mimepull;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ 
/*    */ final class Chunk
/*    */ {
/*    */   volatile Chunk next;
/*    */   volatile Data data;
/*    */ 
/*    */   public Chunk(Data data)
/*    */   {
/* 38 */     this.data = data;
/*    */   }
/*    */ 
/*    */   public Chunk createNext(DataHead dataHead, ByteBuffer buf)
/*    */   {
/* 49 */     return this.next = new Chunk(this.data.createNext(dataHead, buf));
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.org.jvnet.mimepull.Chunk
 * JD-Core Version:    0.6.2
 */