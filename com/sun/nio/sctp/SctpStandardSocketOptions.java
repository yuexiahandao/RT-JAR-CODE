/*     */ package com.sun.nio.sctp;
/*     */ 
/*     */ import java.net.SocketAddress;
/*     */ import sun.nio.ch.SctpStdSocketOption;
/*     */ 
/*     */ public class SctpStandardSocketOptions
/*     */ {
/*  51 */   public static final SctpSocketOption<Boolean> SCTP_DISABLE_FRAGMENTS = new SctpStdSocketOption("SCTP_DISABLE_FRAGMENTS", Boolean.class, 1);
/*     */ 
/*  70 */   public static final SctpSocketOption<Boolean> SCTP_EXPLICIT_COMPLETE = new SctpStdSocketOption("SCTP_EXPLICIT_COMPLETE", Boolean.class, 2);
/*     */ 
/* 120 */   public static final SctpSocketOption<Integer> SCTP_FRAGMENT_INTERLEAVE = new SctpStdSocketOption("SCTP_FRAGMENT_INTERLEAVE", Integer.class, 3);
/*     */ 
/* 160 */   public static final SctpSocketOption<InitMaxStreams> SCTP_INIT_MAXSTREAMS = new SctpStdSocketOption("SCTP_INIT_MAXSTREAMS", InitMaxStreams.class);
/*     */ 
/* 172 */   public static final SctpSocketOption<Boolean> SCTP_NODELAY = new SctpStdSocketOption("SCTP_NODELAY", Boolean.class, 4);
/*     */ 
/* 193 */   public static final SctpSocketOption<SocketAddress> SCTP_PRIMARY_ADDR = new SctpStdSocketOption("SCTP_PRIMARY_ADDR", SocketAddress.class);
/*     */ 
/* 218 */   public static final SctpSocketOption<SocketAddress> SCTP_SET_PEER_PRIMARY_ADDR = new SctpStdSocketOption("SCTP_SET_PEER_PRIMARY_ADDR", SocketAddress.class);
/*     */ 
/* 247 */   public static final SctpSocketOption<Integer> SO_SNDBUF = new SctpStdSocketOption("SO_SNDBUF", Integer.class, 5);
/*     */ 
/* 274 */   public static final SctpSocketOption<Integer> SO_RCVBUF = new SctpStdSocketOption("SO_RCVBUF", Integer.class, 6);
/*     */ 
/* 305 */   public static final SctpSocketOption<Integer> SO_LINGER = new SctpStdSocketOption("SO_LINGER", Integer.class, 7);
/*     */ 
/*     */   public static class InitMaxStreams
/*     */   {
/*     */     private int maxInStreams;
/*     */     private int maxOutStreams;
/*     */ 
/*     */     private InitMaxStreams(int paramInt1, int paramInt2)
/*     */     {
/* 323 */       this.maxInStreams = paramInt1;
/* 324 */       this.maxOutStreams = paramInt2;
/*     */     }
/*     */ 
/*     */     public static InitMaxStreams create(int paramInt1, int paramInt2)
/*     */     {
/* 345 */       if ((paramInt2 < 0) || (paramInt2 > 65535)) {
/* 346 */         throw new IllegalArgumentException("Invalid maxOutStreams value");
/*     */       }
/* 348 */       if ((paramInt1 < 0) || (paramInt1 > 65535)) {
/* 349 */         throw new IllegalArgumentException("Invalid maxInStreams value");
/*     */       }
/*     */ 
/* 352 */       return new InitMaxStreams(paramInt1, paramInt2);
/*     */     }
/*     */ 
/*     */     public int maxInStreams()
/*     */     {
/* 361 */       return this.maxInStreams;
/*     */     }
/*     */ 
/*     */     public int maxOutStreams()
/*     */     {
/* 370 */       return this.maxOutStreams;
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 381 */       StringBuilder localStringBuilder = new StringBuilder();
/* 382 */       localStringBuilder.append(super.toString()).append(" [");
/* 383 */       localStringBuilder.append("maxInStreams:").append(this.maxInStreams);
/* 384 */       localStringBuilder.append("maxOutStreams:").append(this.maxOutStreams).append("]");
/* 385 */       return localStringBuilder.toString();
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject)
/*     */     {
/* 401 */       if ((paramObject != null) && ((paramObject instanceof InitMaxStreams))) {
/* 402 */         InitMaxStreams localInitMaxStreams = (InitMaxStreams)paramObject;
/* 403 */         if ((this.maxInStreams == localInitMaxStreams.maxInStreams) && (this.maxOutStreams == localInitMaxStreams.maxOutStreams))
/*     */         {
/* 405 */           return true;
/*     */         }
/*     */       }
/* 407 */       return false;
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 415 */       int i = 0x7 ^ this.maxInStreams ^ this.maxOutStreams;
/* 416 */       return i;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.nio.sctp.SctpStandardSocketOptions
 * JD-Core Version:    0.6.2
 */