/*    */ package com.sun.corba.se.impl.encoding;
/*    */ 
/*    */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*    */ import com.sun.corba.se.impl.protocol.giopmsgheaders.FragmentMessage;
/*    */ import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;
/*    */ import com.sun.corba.se.spi.orb.ORB;
/*    */ import java.nio.ByteBuffer;
/*    */ 
/*    */ public class BufferManagerReadGrow
/*    */   implements BufferManagerRead, MarkAndResetHandler
/*    */ {
/*    */   private ORB orb;
/*    */   private ORBUtilSystemException wrapper;
/*    */   private Object streamMemento;
/*    */   private RestorableInputStream inputStream;
/* 72 */   private boolean markEngaged = false;
/*    */ 
/*    */   BufferManagerReadGrow(ORB paramORB)
/*    */   {
/* 48 */     this.orb = paramORB;
/* 49 */     this.wrapper = ORBUtilSystemException.get(paramORB, "rpc.encoding");
/*    */   }
/*    */ 
/*    */   public void processFragment(ByteBuffer paramByteBuffer, FragmentMessage paramFragmentMessage)
/*    */   {
/*    */   }
/*    */ 
/*    */   public void init(Message paramMessage)
/*    */   {
/*    */   }
/*    */ 
/*    */   public ByteBufferWithInfo underflow(ByteBufferWithInfo paramByteBufferWithInfo)
/*    */   {
/* 63 */     throw this.wrapper.unexpectedEof();
/*    */   }
/*    */ 
/*    */   public void cancelProcessing(int paramInt)
/*    */   {
/*    */   }
/*    */ 
/*    */   public MarkAndResetHandler getMarkAndResetHandler()
/*    */   {
/* 75 */     return this;
/*    */   }
/*    */ 
/*    */   public void mark(RestorableInputStream paramRestorableInputStream) {
/* 79 */     this.markEngaged = true;
/* 80 */     this.inputStream = paramRestorableInputStream;
/* 81 */     this.streamMemento = this.inputStream.createStreamMemento();
/*    */   }
/*    */ 
/*    */   public void fragmentationOccured(ByteBufferWithInfo paramByteBufferWithInfo)
/*    */   {
/*    */   }
/*    */ 
/*    */   public void reset() {
/* 89 */     if (!this.markEngaged) {
/* 90 */       return;
/*    */     }
/* 92 */     this.markEngaged = false;
/* 93 */     this.inputStream.restoreInternalState(this.streamMemento);
/* 94 */     this.streamMemento = null;
/*    */   }
/*    */ 
/*    */   public void close(ByteBufferWithInfo paramByteBufferWithInfo)
/*    */   {
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.encoding.BufferManagerReadGrow
 * JD-Core Version:    0.6.2
 */