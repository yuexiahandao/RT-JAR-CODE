/*     */ package com.sun.security.auth.module;
/*     */ 
/*     */ public class NTSystem
/*     */ {
/*     */   private String userName;
/*     */   private String domain;
/*     */   private String domainSID;
/*     */   private String userSID;
/*     */   private String[] groupIDs;
/*     */   private String primaryGroupID;
/*     */   private long impersonationToken;
/*     */ 
/*     */   private native void getCurrent(boolean paramBoolean);
/*     */ 
/*     */   private native long getImpersonationToken0();
/*     */ 
/*     */   public NTSystem()
/*     */   {
/*  51 */     this(false);
/*     */   }
/*     */ 
/*     */   NTSystem(boolean paramBoolean)
/*     */   {
/*  59 */     loadNative();
/*  60 */     getCurrent(paramBoolean);
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/*  71 */     return this.userName;
/*     */   }
/*     */ 
/*     */   public String getDomain()
/*     */   {
/*  82 */     return this.domain;
/*     */   }
/*     */ 
/*     */   public String getDomainSID()
/*     */   {
/*  93 */     return this.domainSID;
/*     */   }
/*     */ 
/*     */   public String getUserSID()
/*     */   {
/* 104 */     return this.userSID;
/*     */   }
/*     */ 
/*     */   public String getPrimaryGroupID()
/*     */   {
/* 115 */     return this.primaryGroupID;
/*     */   }
/*     */ 
/*     */   public String[] getGroupIDs()
/*     */   {
/* 126 */     return this.groupIDs == null ? null : (String[])this.groupIDs.clone();
/*     */   }
/*     */ 
/*     */   public synchronized long getImpersonationToken()
/*     */   {
/* 137 */     if (this.impersonationToken == 0L) {
/* 138 */       this.impersonationToken = getImpersonationToken0();
/*     */     }
/* 140 */     return this.impersonationToken;
/*     */   }
/*     */ 
/*     */   private void loadNative()
/*     */   {
/* 145 */     System.loadLibrary("jaas_nt");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.auth.module.NTSystem
 * JD-Core Version:    0.6.2
 */