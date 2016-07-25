/*     */ package com.sun.nio.sctp;
/*     */ 
/*     */ public class Association
/*     */ {
/*     */   private final int associationID;
/*     */   private final int maxInStreams;
/*     */   private final int maxOutStreams;
/*     */ 
/*     */   protected Association(int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/*  65 */     this.associationID = paramInt1;
/*  66 */     this.maxInStreams = paramInt2;
/*  67 */     this.maxOutStreams = paramInt3;
/*     */   }
/*     */ 
/*     */   public final int associationID()
/*     */   {
/*  76 */     return this.associationID;
/*     */   }
/*     */ 
/*     */   public final int maxInboundStreams()
/*     */   {
/*  89 */     return this.maxInStreams;
/*     */   }
/*     */ 
/*     */   public final int maxOutboundStreams()
/*     */   {
/* 102 */     return this.maxOutStreams;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.nio.sctp.Association
 * JD-Core Version:    0.6.2
 */