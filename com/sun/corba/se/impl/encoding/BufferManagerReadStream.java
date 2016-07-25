/*     */ package com.sun.corba.se.impl.encoding;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.impl.orbutil.ORBUtility;
/*     */ import com.sun.corba.se.impl.protocol.RequestCanceledException;
/*     */ import com.sun.corba.se.impl.protocol.giopmsgheaders.FragmentMessage;
/*     */ import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;
/*     */ import com.sun.corba.se.pept.transport.ByteBufferPool;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.LinkedList;
/*     */ import java.util.ListIterator;
/*     */ 
/*     */ public class BufferManagerReadStream
/*     */   implements BufferManagerRead, MarkAndResetHandler
/*     */ {
/*  41 */   private boolean receivedCancel = false;
/*  42 */   private int cancelReqId = 0;
/*     */ 
/*  45 */   private boolean endOfStream = true;
/*  46 */   private BufferQueue fragmentQueue = new BufferQueue();
/*  47 */   private long FRAGMENT_TIMEOUT = 60000L;
/*     */   private ORB orb;
/*     */   private ORBUtilSystemException wrapper;
/*  55 */   private boolean debug = false;
/*     */ 
/* 289 */   private boolean markEngaged = false;
/*     */ 
/* 293 */   private LinkedList fragmentStack = null;
/* 294 */   private RestorableInputStream inputStream = null;
/*     */ 
/* 297 */   private Object streamMemento = null;
/*     */ 
/*     */   BufferManagerReadStream(ORB paramORB)
/*     */   {
/*  59 */     this.orb = paramORB;
/*  60 */     this.wrapper = ORBUtilSystemException.get(paramORB, "rpc.encoding");
/*     */ 
/*  62 */     this.debug = paramORB.transportDebugFlag;
/*     */   }
/*     */ 
/*     */   public void cancelProcessing(int paramInt) {
/*  66 */     synchronized (this.fragmentQueue) {
/*  67 */       this.receivedCancel = true;
/*  68 */       this.cancelReqId = paramInt;
/*  69 */       this.fragmentQueue.notify();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void processFragment(ByteBuffer paramByteBuffer, FragmentMessage paramFragmentMessage)
/*     */   {
/*  75 */     ByteBufferWithInfo localByteBufferWithInfo = new ByteBufferWithInfo(this.orb, paramByteBuffer, paramFragmentMessage.getHeaderLength());
/*     */ 
/*  78 */     synchronized (this.fragmentQueue) {
/*  79 */       if (this.debug)
/*     */       {
/*  82 */         int i = System.identityHashCode(paramByteBuffer);
/*  83 */         StringBuffer localStringBuffer = new StringBuffer(80);
/*  84 */         localStringBuffer.append("processFragment() - queueing ByteBuffer id (");
/*  85 */         localStringBuffer.append(i).append(") to fragment queue.");
/*  86 */         String str = localStringBuffer.toString();
/*  87 */         dprint(str);
/*     */       }
/*  89 */       this.fragmentQueue.enqueue(localByteBufferWithInfo);
/*  90 */       this.endOfStream = (!paramFragmentMessage.moreFragmentsToFollow());
/*  91 */       this.fragmentQueue.notify();
/*     */     }
/*     */   }
/*     */ 
/*     */   public ByteBufferWithInfo underflow(ByteBufferWithInfo paramByteBufferWithInfo)
/*     */   {
/*  98 */     ByteBufferWithInfo localByteBufferWithInfo = null;
/*     */     try
/*     */     {
/* 103 */       synchronized (this.fragmentQueue)
/*     */       {
/* 105 */         if (this.receivedCancel)
/* 106 */           throw new RequestCanceledException(this.cancelReqId);
/*     */         int i;
/* 109 */         while (this.fragmentQueue.size() == 0)
/*     */         {
/* 111 */           if (this.endOfStream) {
/* 112 */             throw this.wrapper.endOfStream();
/*     */           }
/*     */ 
/* 115 */           i = 0;
/*     */           try {
/* 117 */             this.fragmentQueue.wait(this.FRAGMENT_TIMEOUT);
/*     */           } catch (InterruptedException localInterruptedException) {
/* 119 */             i = 1;
/*     */           }
/*     */ 
/* 122 */           if ((i == 0) && (this.fragmentQueue.size() == 0)) {
/* 123 */             throw this.wrapper.bufferReadManagerTimeout();
/*     */           }
/*     */ 
/* 126 */           if (this.receivedCancel) {
/* 127 */             throw new RequestCanceledException(this.cancelReqId);
/*     */           }
/*     */         }
/*     */ 
/* 131 */         localByteBufferWithInfo = this.fragmentQueue.dequeue();
/* 132 */         localByteBufferWithInfo.fragmented = true;
/*     */         Object localObject1;
/* 134 */         if (this.debug)
/*     */         {
/* 137 */           i = System.identityHashCode(localByteBufferWithInfo.byteBuffer);
/* 138 */           StringBuffer localStringBuffer = new StringBuffer(80);
/* 139 */           localStringBuffer.append("underflow() - dequeued ByteBuffer id (");
/* 140 */           localStringBuffer.append(i).append(") from fragment queue.");
/* 141 */           localObject1 = localStringBuffer.toString();
/* 142 */           dprint((String)localObject1);
/*     */         }
/*     */ 
/* 148 */         if ((!this.markEngaged) && (paramByteBufferWithInfo != null) && (paramByteBufferWithInfo.byteBuffer != null))
/*     */         {
/* 150 */           ByteBufferPool localByteBufferPool = getByteBufferPool();
/*     */ 
/* 152 */           if (this.debug)
/*     */           {
/* 155 */             int j = System.identityHashCode(paramByteBufferWithInfo.byteBuffer);
/* 156 */             localObject1 = new StringBuffer(80);
/* 157 */             ((StringBuffer)localObject1).append("underflow() - releasing ByteBuffer id (");
/* 158 */             ((StringBuffer)localObject1).append(j).append(") to ByteBufferPool.");
/* 159 */             String str = ((StringBuffer)localObject1).toString();
/* 160 */             dprint(str);
/*     */           }
/*     */ 
/* 163 */           localByteBufferPool.releaseByteBuffer(paramByteBufferWithInfo.byteBuffer);
/* 164 */           paramByteBufferWithInfo.byteBuffer = null;
/* 165 */           paramByteBufferWithInfo = null;
/*     */         }
/*     */       }
/* 168 */       ??? = localByteBufferWithInfo; return ???;
/*     */     }
/*     */     finally {
/*     */     }
/*     */   }
/*     */ 
/*     */   public void init(Message paramMessage) {
/* 175 */     if (paramMessage != null)
/* 176 */       this.endOfStream = (!paramMessage.moreFragmentsToFollow());
/*     */   }
/*     */ 
/*     */   public void close(ByteBufferWithInfo paramByteBufferWithInfo)
/*     */   {
/* 183 */     int i = 0;
/*     */     Object localObject1;
/*     */     Object localObject2;
/*     */     int j;
/*     */     StringBuffer localStringBuffer;
/*     */     String str;
/* 186 */     if (this.fragmentQueue != null)
/*     */     {
/* 188 */       synchronized (this.fragmentQueue)
/*     */       {
/* 197 */         if (paramByteBufferWithInfo != null)
/*     */         {
/* 199 */           i = System.identityHashCode(paramByteBufferWithInfo.byteBuffer);
/*     */         }
/*     */ 
/* 202 */         localObject1 = null;
/* 203 */         localObject2 = getByteBufferPool();
/* 204 */         while (this.fragmentQueue.size() != 0)
/*     */         {
/* 206 */           localObject1 = this.fragmentQueue.dequeue();
/* 207 */           if ((localObject1 != null) && (((ByteBufferWithInfo)localObject1).byteBuffer != null))
/*     */           {
/* 209 */             j = System.identityHashCode(((ByteBufferWithInfo)localObject1).byteBuffer);
/* 210 */             if (i != j)
/*     */             {
/* 212 */               if (this.debug)
/*     */               {
/* 215 */                 localStringBuffer = new StringBuffer(80);
/* 216 */                 localStringBuffer.append("close() - fragmentQueue is ").append("releasing ByteBuffer id (").append(j).append(") to ").append("ByteBufferPool.");
/*     */ 
/* 220 */                 str = localStringBuffer.toString();
/* 221 */                 dprint(str);
/*     */               }
/*     */             }
/* 224 */             ((ByteBufferPool)localObject2).releaseByteBuffer(((ByteBufferWithInfo)localObject1).byteBuffer);
/*     */           }
/*     */         }
/*     */       }
/* 228 */       this.fragmentQueue = null;
/*     */     }
/*     */ 
/* 232 */     if ((this.fragmentStack != null) && (this.fragmentStack.size() != 0))
/*     */     {
/* 241 */       if (paramByteBufferWithInfo != null)
/*     */       {
/* 243 */         i = System.identityHashCode(paramByteBufferWithInfo.byteBuffer);
/*     */       }
/*     */ 
/* 246 */       ??? = null;
/* 247 */       localObject1 = getByteBufferPool();
/* 248 */       localObject2 = this.fragmentStack.listIterator();
/* 249 */       while (((ListIterator)localObject2).hasNext())
/*     */       {
/* 251 */         ??? = (ByteBufferWithInfo)((ListIterator)localObject2).next();
/*     */ 
/* 253 */         if ((??? != null) && (((ByteBufferWithInfo)???).byteBuffer != null))
/*     */         {
/* 255 */           j = System.identityHashCode(((ByteBufferWithInfo)???).byteBuffer);
/* 256 */           if (i != j)
/*     */           {
/* 258 */             if (this.debug)
/*     */             {
/* 261 */               localStringBuffer = new StringBuffer(80);
/* 262 */               localStringBuffer.append("close() - fragmentStack - releasing ").append("ByteBuffer id (" + j + ") to ").append("ByteBufferPool.");
/*     */ 
/* 265 */               str = localStringBuffer.toString();
/* 266 */               dprint(str);
/*     */             }
/* 268 */             ((ByteBufferPool)localObject1).releaseByteBuffer(((ByteBufferWithInfo)???).byteBuffer);
/*     */           }
/*     */         }
/*     */       }
/* 272 */       this.fragmentStack = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected ByteBufferPool getByteBufferPool()
/*     */   {
/* 279 */     return this.orb.getByteBufferPool();
/*     */   }
/*     */ 
/*     */   private void dprint(String paramString)
/*     */   {
/* 284 */     ORBUtility.dprint("BufferManagerReadStream", paramString);
/*     */   }
/*     */ 
/*     */   public void mark(RestorableInputStream paramRestorableInputStream)
/*     */   {
/* 301 */     this.inputStream = paramRestorableInputStream;
/* 302 */     this.markEngaged = true;
/*     */ 
/* 306 */     this.streamMemento = paramRestorableInputStream.createStreamMemento();
/*     */ 
/* 308 */     if (this.fragmentStack != null)
/* 309 */       this.fragmentStack.clear();
/*     */   }
/*     */ 
/*     */   public void fragmentationOccured(ByteBufferWithInfo paramByteBufferWithInfo)
/*     */   {
/* 316 */     if (!this.markEngaged) {
/* 317 */       return;
/*     */     }
/* 319 */     if (this.fragmentStack == null) {
/* 320 */       this.fragmentStack = new LinkedList();
/*     */     }
/* 322 */     this.fragmentStack.addFirst(new ByteBufferWithInfo(paramByteBufferWithInfo));
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/* 327 */     if (!this.markEngaged)
/*     */     {
/* 329 */       return;
/*     */     }
/*     */ 
/* 332 */     this.markEngaged = false;
/*     */ 
/* 337 */     if ((this.fragmentStack != null) && (this.fragmentStack.size() != 0)) {
/* 338 */       ListIterator localListIterator = this.fragmentStack.listIterator();
/*     */ 
/* 340 */       synchronized (this.fragmentQueue) {
/* 341 */         while (localListIterator.hasNext()) {
/* 342 */           this.fragmentQueue.push((ByteBufferWithInfo)localListIterator.next());
/*     */         }
/*     */       }
/*     */ 
/* 346 */       this.fragmentStack.clear();
/*     */     }
/*     */ 
/* 351 */     this.inputStream.restoreInternalState(this.streamMemento);
/*     */   }
/*     */ 
/*     */   public MarkAndResetHandler getMarkAndResetHandler() {
/* 355 */     return this;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.encoding.BufferManagerReadStream
 * JD-Core Version:    0.6.2
 */