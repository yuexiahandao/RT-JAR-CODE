/*     */ package com.sun.security.auth;
/*     */ 
/*     */ import java.text.MessageFormat;
/*     */ import sun.security.util.ResourcesMgr;
/*     */ 
/*     */ public class NTNumericCredential
/*     */ {
/*     */   private long impersonationToken;
/*     */ 
/*     */   public NTNumericCredential(long paramLong)
/*     */   {
/*  47 */     this.impersonationToken = paramLong;
/*     */   }
/*     */ 
/*     */   public long getToken()
/*     */   {
/*  60 */     return this.impersonationToken;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  71 */     MessageFormat localMessageFormat = new MessageFormat(ResourcesMgr.getString("NTNumericCredential.name", "sun.security.util.AuthResources"));
/*     */ 
/*  75 */     Object[] arrayOfObject = { Long.toString(this.impersonationToken) };
/*  76 */     return localMessageFormat.format(arrayOfObject);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  94 */     if (paramObject == null) {
/*  95 */       return false;
/*     */     }
/*  97 */     if (this == paramObject) {
/*  98 */       return true;
/*     */     }
/* 100 */     if (!(paramObject instanceof NTNumericCredential))
/* 101 */       return false;
/* 102 */     NTNumericCredential localNTNumericCredential = (NTNumericCredential)paramObject;
/*     */ 
/* 104 */     if (this.impersonationToken == localNTNumericCredential.getToken())
/* 105 */       return true;
/* 106 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 117 */     return (int)this.impersonationToken;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.auth.NTNumericCredential
 * JD-Core Version:    0.6.2
 */