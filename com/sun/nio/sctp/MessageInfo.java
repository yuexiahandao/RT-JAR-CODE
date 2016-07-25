/*     */ package com.sun.nio.sctp;
/*     */ 
/*     */ import java.net.SocketAddress;
/*     */ import sun.nio.ch.SctpMessageInfoImpl;
/*     */ 
/*     */ public abstract class MessageInfo
/*     */ {
/*     */   public static MessageInfo createOutgoing(SocketAddress paramSocketAddress, int paramInt)
/*     */   {
/*  94 */     if ((paramInt < 0) || (paramInt > 65536)) {
/*  95 */       throw new IllegalArgumentException("Invalid stream number");
/*     */     }
/*  97 */     return new SctpMessageInfoImpl(null, paramSocketAddress, paramInt);
/*     */   }
/*     */ 
/*     */   public static MessageInfo createOutgoing(Association paramAssociation, SocketAddress paramSocketAddress, int paramInt)
/*     */   {
/* 130 */     if (paramAssociation == null) {
/* 131 */       throw new IllegalArgumentException("association cannot be null");
/*     */     }
/* 133 */     if ((paramInt < 0) || (paramInt > 65536)) {
/* 134 */       throw new IllegalArgumentException("Invalid stream number");
/*     */     }
/* 136 */     return new SctpMessageInfoImpl(paramAssociation, paramSocketAddress, paramInt);
/*     */   }
/*     */ 
/*     */   public abstract SocketAddress address();
/*     */ 
/*     */   public abstract Association association();
/*     */ 
/*     */   public abstract int bytes();
/*     */ 
/*     */   public abstract boolean isComplete();
/*     */ 
/*     */   public abstract MessageInfo complete(boolean paramBoolean);
/*     */ 
/*     */   public abstract boolean isUnordered();
/*     */ 
/*     */   public abstract MessageInfo unordered(boolean paramBoolean);
/*     */ 
/*     */   public abstract int payloadProtocolID();
/*     */ 
/*     */   public abstract MessageInfo payloadProtocolID(int paramInt);
/*     */ 
/*     */   public abstract int streamNumber();
/*     */ 
/*     */   public abstract MessageInfo streamNumber(int paramInt);
/*     */ 
/*     */   public abstract long timeToLive();
/*     */ 
/*     */   public abstract MessageInfo timeToLive(long paramLong);
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.nio.sctp.MessageInfo
 * JD-Core Version:    0.6.2
 */