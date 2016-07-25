/*     */ package com.sun.corba.se.impl.transport;
/*     */ 
/*     */ import com.sun.corba.se.impl.encoding.BufferManagerReadStream;
/*     */ import com.sun.corba.se.impl.encoding.CDRInputObject;
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.impl.orbutil.ORBUtility;
/*     */ import com.sun.corba.se.impl.protocol.giopmsgheaders.LocateReplyOrReplyMessage;
/*     */ import com.sun.corba.se.pept.encoding.InputObject;
/*     */ import com.sun.corba.se.pept.protocol.MessageMediator;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.protocol.CorbaMessageMediator;
/*     */ import com.sun.corba.se.spi.transport.CorbaConnection;
/*     */ import com.sun.corba.se.spi.transport.CorbaResponseWaitingRoom;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.omg.CORBA.CompletionStatus;
/*     */ import org.omg.CORBA.SystemException;
/*     */ 
/*     */ public class CorbaResponseWaitingRoomImpl
/*     */   implements CorbaResponseWaitingRoom
/*     */ {
/*     */   private ORB orb;
/*     */   private ORBUtilSystemException wrapper;
/*     */   private CorbaConnection connection;
/*     */   private final Map<Integer, OutCallDesc> out_calls;
/*     */ 
/*     */   public CorbaResponseWaitingRoomImpl(ORB paramORB, CorbaConnection paramCorbaConnection)
/*     */   {
/*  78 */     this.orb = paramORB;
/*  79 */     this.wrapper = ORBUtilSystemException.get(paramORB, "rpc.transport");
/*     */ 
/*  81 */     this.connection = paramCorbaConnection;
/*  82 */     this.out_calls = Collections.synchronizedMap(new HashMap());
/*     */   }
/*     */ 
/*     */   public void registerWaiter(MessageMediator paramMessageMediator)
/*     */   {
/*  93 */     CorbaMessageMediator localCorbaMessageMediator = (CorbaMessageMediator)paramMessageMediator;
/*     */ 
/*  95 */     if (this.orb.transportDebugFlag) {
/*  96 */       dprint(".registerWaiter: " + opAndId(localCorbaMessageMediator));
/*     */     }
/*     */ 
/*  99 */     Integer localInteger = localCorbaMessageMediator.getRequestIdInteger();
/*     */ 
/* 101 */     OutCallDesc localOutCallDesc = new OutCallDesc();
/* 102 */     localOutCallDesc.thread = Thread.currentThread();
/* 103 */     localOutCallDesc.messageMediator = localCorbaMessageMediator;
/* 104 */     this.out_calls.put(localInteger, localOutCallDesc);
/*     */   }
/*     */ 
/*     */   public void unregisterWaiter(MessageMediator paramMessageMediator)
/*     */   {
/* 109 */     CorbaMessageMediator localCorbaMessageMediator = (CorbaMessageMediator)paramMessageMediator;
/*     */ 
/* 111 */     if (this.orb.transportDebugFlag) {
/* 112 */       dprint(".unregisterWaiter: " + opAndId(localCorbaMessageMediator));
/*     */     }
/*     */ 
/* 115 */     Integer localInteger = localCorbaMessageMediator.getRequestIdInteger();
/*     */ 
/* 117 */     this.out_calls.remove(localInteger);
/*     */   }
/*     */ 
/*     */   public InputObject waitForResponse(MessageMediator paramMessageMediator)
/*     */   {
/* 122 */     CorbaMessageMediator localCorbaMessageMediator = (CorbaMessageMediator)paramMessageMediator;
/*     */     try
/*     */     {
/* 126 */       InputObject localInputObject = null;
/*     */ 
/* 128 */       if (this.orb.transportDebugFlag) {
/* 129 */         dprint(".waitForResponse->: " + opAndId(localCorbaMessageMediator));
/*     */       }
/*     */ 
/* 132 */       Integer localInteger = localCorbaMessageMediator.getRequestIdInteger();
/*     */ 
/* 134 */       if (localCorbaMessageMediator.isOneWay())
/*     */       {
/* 138 */         if (this.orb.transportDebugFlag) {
/* 139 */           dprint(".waitForResponse: one way - not waiting: " + opAndId(localCorbaMessageMediator));
/*     */         }
/*     */ 
/* 143 */         return null;
/*     */       }
/*     */ 
/* 146 */       Object localObject1 = (OutCallDesc)this.out_calls.get(localInteger);
/* 147 */       if (localObject1 == null) {
/* 148 */         throw this.wrapper.nullOutCall(CompletionStatus.COMPLETED_MAYBE);
/*     */       }
/*     */ 
/* 151 */       synchronized (((OutCallDesc)localObject1).done)
/*     */       {
/* 153 */         while ((((OutCallDesc)localObject1).inputObject == null) && (((OutCallDesc)localObject1).exception == null))
/*     */         {
/*     */           try
/*     */           {
/* 158 */             if (this.orb.transportDebugFlag) {
/* 159 */               dprint(".waitForResponse: waiting: " + opAndId(localCorbaMessageMediator));
/*     */             }
/*     */ 
/* 162 */             ((OutCallDesc)localObject1).done.wait();
/*     */           } catch (InterruptedException localInterruptedException) {
/*     */           }
/*     */         }
/* 166 */         if (((OutCallDesc)localObject1).exception != null)
/*     */         {
/* 171 */           throw ((OutCallDesc)localObject1).exception;
/*     */         }
/*     */ 
/* 174 */         localInputObject = ((OutCallDesc)localObject1).inputObject;
/*     */       }
/*     */ 
/* 180 */       if (localInputObject != null)
/*     */       {
/* 186 */         ((CDRInputObject)localInputObject).unmarshalHeader();
/*     */       }
/*     */ 
/* 189 */       return localInputObject;
/*     */     }
/*     */     finally {
/* 192 */       if (this.orb.transportDebugFlag)
/* 193 */         dprint(".waitForResponse<-: " + opAndId(localCorbaMessageMediator));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void responseReceived(InputObject paramInputObject)
/*     */   {
/* 200 */     CDRInputObject localCDRInputObject = (CDRInputObject)paramInputObject;
/* 201 */     LocateReplyOrReplyMessage localLocateReplyOrReplyMessage = (LocateReplyOrReplyMessage)localCDRInputObject.getMessageHeader();
/*     */ 
/* 203 */     Integer localInteger = new Integer(localLocateReplyOrReplyMessage.getRequestId());
/* 204 */     OutCallDesc localOutCallDesc = (OutCallDesc)this.out_calls.get(localInteger);
/*     */ 
/* 206 */     if (this.orb.transportDebugFlag) {
/* 207 */       dprint(".responseReceived: id/" + localInteger + ": " + localLocateReplyOrReplyMessage);
/*     */     }
/*     */ 
/* 220 */     if (localOutCallDesc == null) {
/* 221 */       if (this.orb.transportDebugFlag) {
/* 222 */         dprint(".responseReceived: id/" + localInteger + ": no waiter: " + localLocateReplyOrReplyMessage);
/*     */       }
/*     */ 
/* 227 */       return;
/*     */     }
/*     */ 
/* 235 */     synchronized (localOutCallDesc.done) {
/* 236 */       CorbaMessageMediator localCorbaMessageMediator = (CorbaMessageMediator)localOutCallDesc.messageMediator;
/*     */ 
/* 239 */       if (this.orb.transportDebugFlag) {
/* 240 */         dprint(".responseReceived: " + opAndId(localCorbaMessageMediator) + ": notifying waiters");
/*     */       }
/*     */ 
/* 245 */       localCorbaMessageMediator.setReplyHeader(localLocateReplyOrReplyMessage);
/* 246 */       localCorbaMessageMediator.setInputObject(paramInputObject);
/* 247 */       localCDRInputObject.setMessageMediator(localCorbaMessageMediator);
/* 248 */       localOutCallDesc.inputObject = paramInputObject;
/* 249 */       localOutCallDesc.done.notify();
/*     */     }
/*     */   }
/*     */ 
/*     */   public int numberRegistered()
/*     */   {
/* 255 */     return this.out_calls.size();
/*     */   }
/*     */ 
/*     */   public void signalExceptionToAllWaiters(SystemException paramSystemException)
/*     */   {
/* 266 */     if (this.orb.transportDebugFlag) {
/* 267 */       dprint(".signalExceptionToAllWaiters: " + paramSystemException);
/*     */     }
/*     */ 
/* 270 */     synchronized (this.out_calls) {
/* 271 */       if (this.orb.transportDebugFlag) {
/* 272 */         dprint(".signalExceptionToAllWaiters: out_calls size :" + this.out_calls.size());
/*     */       }
/*     */ 
/* 276 */       for (OutCallDesc localOutCallDesc : this.out_calls.values()) {
/* 277 */         if (this.orb.transportDebugFlag) {
/* 278 */           dprint(".signalExceptionToAllWaiters: signaling " + localOutCallDesc);
/*     */         }
/*     */ 
/* 281 */         synchronized (localOutCallDesc.done)
/*     */         {
/*     */           try
/*     */           {
/* 285 */             CorbaMessageMediator localCorbaMessageMediator = (CorbaMessageMediator)localOutCallDesc.messageMediator;
/*     */ 
/* 287 */             CDRInputObject localCDRInputObject = (CDRInputObject)localCorbaMessageMediator.getInputObject();
/*     */ 
/* 291 */             if (localCDRInputObject != null) {
/* 292 */               BufferManagerReadStream localBufferManagerReadStream = (BufferManagerReadStream)localCDRInputObject.getBufferManager();
/*     */ 
/* 294 */               int i = localCorbaMessageMediator.getRequestId();
/* 295 */               localBufferManagerReadStream.cancelProcessing(i);
/*     */             }
/*     */           } catch (Exception localException) {
/*     */           }
/*     */           finally {
/* 300 */             localOutCallDesc.inputObject = null;
/* 301 */             localOutCallDesc.exception = paramSystemException;
/* 302 */             localOutCallDesc.done.notifyAll();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public MessageMediator getMessageMediator(int paramInt)
/*     */   {
/* 311 */     Integer localInteger = new Integer(paramInt);
/* 312 */     OutCallDesc localOutCallDesc = (OutCallDesc)this.out_calls.get(localInteger);
/* 313 */     if (localOutCallDesc == null)
/*     */     {
/* 316 */       return null;
/*     */     }
/* 318 */     return localOutCallDesc.messageMediator;
/*     */   }
/*     */ 
/*     */   protected void dprint(String paramString)
/*     */   {
/* 328 */     ORBUtility.dprint("CorbaResponseWaitingRoomImpl", paramString);
/*     */   }
/*     */ 
/*     */   protected String opAndId(CorbaMessageMediator paramCorbaMessageMediator)
/*     */   {
/* 333 */     return ORBUtility.operationNameAndRequestId(paramCorbaMessageMediator);
/*     */   }
/*     */ 
/*     */   static final class OutCallDesc
/*     */   {
/*  62 */     Object done = new Object();
/*     */     Thread thread;
/*     */     MessageMediator messageMediator;
/*     */     SystemException exception;
/*     */     InputObject inputObject;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.transport.CorbaResponseWaitingRoomImpl
 * JD-Core Version:    0.6.2
 */