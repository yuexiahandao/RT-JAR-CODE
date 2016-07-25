/*     */ package com.sun.corba.se.impl.encoding;
/*     */ 
/*     */ import com.sun.corba.se.impl.protocol.giopmsgheaders.FragmentMessage;
/*     */ import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;
/*     */ import com.sun.corba.se.impl.protocol.giopmsgheaders.MessageBase;
/*     */ import com.sun.corba.se.pept.encoding.OutputObject;
/*     */ import com.sun.corba.se.pept.protocol.MessageMediator;
/*     */ import com.sun.corba.se.pept.transport.Connection;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.orb.ORBData;
/*     */ import com.sun.corba.se.spi.protocol.PIHandler;
/*     */ import java.nio.ByteBuffer;
/*     */ import org.omg.CORBA.SystemException;
/*     */ 
/*     */ public class BufferManagerWriteStream extends BufferManagerWrite
/*     */ {
/*  47 */   private int fragmentCount = 0;
/*     */ 
/*     */   BufferManagerWriteStream(ORB paramORB)
/*     */   {
/*  51 */     super(paramORB);
/*     */   }
/*     */ 
/*     */   public boolean sentFragment() {
/*  55 */     return this.fragmentCount > 0;
/*     */   }
/*     */ 
/*     */   public int getBufferSize()
/*     */   {
/*  63 */     return this.orb.getORBData().getGIOPFragmentSize();
/*     */   }
/*     */ 
/*     */   public void overflow(ByteBufferWithInfo paramByteBufferWithInfo)
/*     */   {
/*  69 */     MessageBase.setFlag(paramByteBufferWithInfo.byteBuffer, 2);
/*     */     try
/*     */     {
/*  72 */       sendFragment(false);
/*     */     } catch (SystemException localSystemException) {
/*  74 */       this.orb.getPIHandler().invokeClientPIEndingPoint(2, localSystemException);
/*     */ 
/*  76 */       throw localSystemException;
/*     */     }
/*     */ 
/*  84 */     paramByteBufferWithInfo.position(0);
/*  85 */     paramByteBufferWithInfo.buflen = paramByteBufferWithInfo.byteBuffer.limit();
/*  86 */     paramByteBufferWithInfo.fragmented = true;
/*     */ 
/*  93 */     FragmentMessage localFragmentMessage = ((CDROutputObject)this.outputObject).getMessageHeader().createFragmentMessage();
/*     */ 
/*  95 */     localFragmentMessage.write((CDROutputObject)this.outputObject);
/*     */   }
/*     */ 
/*     */   private void sendFragment(boolean paramBoolean)
/*     */   {
/* 100 */     Connection localConnection = ((OutputObject)this.outputObject).getMessageMediator().getConnection();
/*     */ 
/* 104 */     localConnection.writeLock();
/*     */     try
/*     */     {
/* 108 */       localConnection.sendWithoutLock((OutputObject)this.outputObject);
/*     */ 
/* 110 */       this.fragmentCount += 1;
/*     */     }
/*     */     finally
/*     */     {
/* 114 */       localConnection.writeUnlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void sendMessage()
/*     */   {
/* 122 */     sendFragment(true);
/*     */ 
/* 124 */     this.sentFullMessage = true;
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.encoding.BufferManagerWriteStream
 * JD-Core Version:    0.6.2
 */