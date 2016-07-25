/*     */ package com.sun.corba.se.impl.encoding;
/*     */ 
/*     */ import com.sun.corba.se.impl.orbutil.ORBUtility;
/*     */ import com.sun.corba.se.impl.protocol.giopmsgheaders.FragmentMessage;
/*     */ import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;
/*     */ import com.sun.corba.se.impl.protocol.giopmsgheaders.MessageBase;
/*     */ import com.sun.corba.se.pept.encoding.OutputObject;
/*     */ import com.sun.corba.se.pept.protocol.MessageMediator;
/*     */ import com.sun.corba.se.pept.transport.ByteBufferPool;
/*     */ import com.sun.corba.se.pept.transport.Connection;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.orb.ORBData;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ public class BufferManagerWriteCollect extends BufferManagerWrite
/*     */ {
/*  51 */   private BufferQueue queue = new BufferQueue();
/*     */ 
/*  53 */   private boolean sentFragment = false;
/*  54 */   private boolean debug = false;
/*     */ 
/*     */   BufferManagerWriteCollect(ORB paramORB)
/*     */   {
/*  59 */     super(paramORB);
/*  60 */     if (paramORB != null)
/*  61 */       this.debug = paramORB.transportDebugFlag;
/*     */   }
/*     */ 
/*     */   public boolean sentFragment() {
/*  65 */     return this.sentFragment;
/*     */   }
/*     */ 
/*     */   public int getBufferSize()
/*     */   {
/*  73 */     return this.orb.getORBData().getGIOPFragmentSize();
/*     */   }
/*     */ 
/*     */   public void overflow(ByteBufferWithInfo paramByteBufferWithInfo)
/*     */   {
/*  81 */     MessageBase.setFlag(paramByteBufferWithInfo.byteBuffer, 2);
/*     */ 
/*  84 */     this.queue.enqueue(paramByteBufferWithInfo);
/*     */ 
/*  87 */     ByteBufferWithInfo localByteBufferWithInfo = new ByteBufferWithInfo(this.orb, this);
/*  88 */     localByteBufferWithInfo.fragmented = true;
/*     */ 
/*  91 */     ((CDROutputObject)this.outputObject).setByteBufferWithInfo(localByteBufferWithInfo);
/*     */ 
/*  99 */     FragmentMessage localFragmentMessage = ((CDROutputObject)this.outputObject).getMessageHeader().createFragmentMessage();
/*     */ 
/* 103 */     localFragmentMessage.write((CDROutputObject)this.outputObject);
/*     */   }
/*     */ 
/*     */   public void sendMessage()
/*     */   {
/* 110 */     this.queue.enqueue(((CDROutputObject)this.outputObject).getByteBufferWithInfo());
/*     */ 
/* 112 */     Iterator localIterator = iterator();
/*     */ 
/* 114 */     Connection localConnection = ((OutputObject)this.outputObject).getMessageMediator().getConnection();
/*     */ 
/* 124 */     localConnection.writeLock();
/*     */     try
/*     */     {
/* 130 */       ByteBufferPool localByteBufferPool = this.orb.getByteBufferPool();
/*     */ 
/* 132 */       while (localIterator.hasNext())
/*     */       {
/* 134 */         ByteBufferWithInfo localByteBufferWithInfo = (ByteBufferWithInfo)localIterator.next();
/* 135 */         ((CDROutputObject)this.outputObject).setByteBufferWithInfo(localByteBufferWithInfo);
/*     */ 
/* 137 */         localConnection.sendWithoutLock((CDROutputObject)this.outputObject);
/*     */ 
/* 139 */         this.sentFragment = true;
/*     */ 
/* 143 */         if (this.debug)
/*     */         {
/* 146 */           int i = System.identityHashCode(localByteBufferWithInfo.byteBuffer);
/* 147 */           StringBuffer localStringBuffer = new StringBuffer(80);
/* 148 */           localStringBuffer.append("sendMessage() - releasing ByteBuffer id (");
/* 149 */           localStringBuffer.append(i).append(") to ByteBufferPool.");
/* 150 */           String str = localStringBuffer.toString();
/* 151 */           dprint(str);
/*     */         }
/* 153 */         localByteBufferPool.releaseByteBuffer(localByteBufferWithInfo.byteBuffer);
/* 154 */         localByteBufferWithInfo.byteBuffer = null;
/* 155 */         localByteBufferWithInfo = null;
/*     */       }
/*     */ 
/* 158 */       this.sentFullMessage = true;
/*     */     }
/*     */     finally
/*     */     {
/* 162 */       localConnection.writeUnlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/* 178 */     Iterator localIterator = iterator();
/*     */ 
/* 180 */     ByteBufferPool localByteBufferPool = this.orb.getByteBufferPool();
/*     */ 
/* 182 */     while (localIterator.hasNext())
/*     */     {
/* 184 */       ByteBufferWithInfo localByteBufferWithInfo = (ByteBufferWithInfo)localIterator.next();
/* 185 */       if ((localByteBufferWithInfo != null) && (localByteBufferWithInfo.byteBuffer != null))
/*     */       {
/* 187 */         if (this.debug)
/*     */         {
/* 190 */           int i = System.identityHashCode(localByteBufferWithInfo.byteBuffer);
/* 191 */           StringBuffer localStringBuffer = new StringBuffer(80);
/* 192 */           localStringBuffer.append("close() - releasing ByteBuffer id (");
/* 193 */           localStringBuffer.append(i).append(") to ByteBufferPool.");
/* 194 */           String str = localStringBuffer.toString();
/* 195 */           dprint(str);
/*     */         }
/* 197 */         localByteBufferPool.releaseByteBuffer(localByteBufferWithInfo.byteBuffer);
/* 198 */         localByteBufferWithInfo.byteBuffer = null;
/* 199 */         localByteBufferWithInfo = null;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void dprint(String paramString)
/*     */   {
/* 206 */     ORBUtility.dprint("BufferManagerWriteCollect", paramString);
/*     */   }
/*     */ 
/*     */   private Iterator iterator()
/*     */   {
/* 211 */     return new BufferManagerWriteCollectIterator(null);
/*     */   }
/*     */   private class BufferManagerWriteCollectIterator implements Iterator {
/*     */     private BufferManagerWriteCollectIterator() {
/*     */     }
/*     */ 
/*     */     public boolean hasNext() {
/* 218 */       return BufferManagerWriteCollect.this.queue.size() != 0;
/*     */     }
/*     */ 
/*     */     public Object next()
/*     */     {
/* 223 */       return BufferManagerWriteCollect.this.queue.dequeue();
/*     */     }
/*     */ 
/*     */     public void remove()
/*     */     {
/* 228 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.encoding.BufferManagerWriteCollect
 * JD-Core Version:    0.6.2
 */