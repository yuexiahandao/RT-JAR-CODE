/*    */ package com.sun.corba.se.impl.encoding;
/*    */ 
/*    */ import java.util.LinkedList;
/*    */ import java.util.NoSuchElementException;
/*    */ 
/*    */ public class BufferQueue
/*    */ {
/* 37 */   private LinkedList list = new LinkedList();
/*    */ 
/*    */   public void enqueue(ByteBufferWithInfo paramByteBufferWithInfo)
/*    */   {
/* 41 */     this.list.addLast(paramByteBufferWithInfo);
/*    */   }
/*    */ 
/*    */   public ByteBufferWithInfo dequeue() throws NoSuchElementException
/*    */   {
/* 46 */     return (ByteBufferWithInfo)this.list.removeFirst();
/*    */   }
/*    */ 
/*    */   public int size()
/*    */   {
/* 51 */     return this.list.size();
/*    */   }
/*    */ 
/*    */   public void push(ByteBufferWithInfo paramByteBufferWithInfo)
/*    */   {
/* 58 */     this.list.addFirst(paramByteBufferWithInfo);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.encoding.BufferQueue
 * JD-Core Version:    0.6.2
 */