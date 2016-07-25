/*    */ package com.sun.corba.se.impl.encoding;
/*    */ 
/*    */ import com.sun.corba.se.pept.encoding.OutputObject;
/*    */ import com.sun.corba.se.pept.protocol.MessageMediator;
/*    */ import com.sun.corba.se.pept.transport.Connection;
/*    */ import com.sun.corba.se.spi.orb.ORB;
/*    */ import com.sun.corba.se.spi.orb.ORBData;
/*    */ 
/*    */ public class BufferManagerWriteGrow extends BufferManagerWrite
/*    */ {
/*    */   BufferManagerWriteGrow(ORB paramORB)
/*    */   {
/* 39 */     super(paramORB);
/*    */   }
/*    */ 
/*    */   public boolean sentFragment() {
/* 43 */     return false;
/*    */   }
/*    */ 
/*    */   public int getBufferSize()
/*    */   {
/* 51 */     return this.orb.getORBData().getGIOPBufferSize();
/*    */   }
/*    */ 
/*    */   public void overflow(ByteBufferWithInfo paramByteBufferWithInfo)
/*    */   {
/* 60 */     paramByteBufferWithInfo.growBuffer(this.orb);
/*    */ 
/* 63 */     paramByteBufferWithInfo.fragmented = false;
/*    */   }
/*    */ 
/*    */   public void sendMessage()
/*    */   {
/* 68 */     Connection localConnection = ((OutputObject)this.outputObject).getMessageMediator().getConnection();
/*    */ 
/* 71 */     localConnection.writeLock();
/*    */     try
/*    */     {
/* 75 */       localConnection.sendWithoutLock((OutputObject)this.outputObject);
/*    */ 
/* 77 */       this.sentFullMessage = true;
/*    */     }
/*    */     finally
/*    */     {
/* 81 */       localConnection.writeUnlock();
/*    */     }
/*    */   }
/*    */ 
/*    */   public void close()
/*    */   {
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.encoding.BufferManagerWriteGrow
 * JD-Core Version:    0.6.2
 */