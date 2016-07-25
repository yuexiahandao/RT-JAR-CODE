/*     */ package com.sun.security.auth;
/*     */ 
/*     */ import java.text.MessageFormat;
/*     */ import sun.security.util.ResourcesMgr;
/*     */ 
/*     */ public class NTSidPrimaryGroupPrincipal extends NTSid
/*     */ {
/*     */   private static final long serialVersionUID = 8011978367305190527L;
/*     */ 
/*     */   public NTSidPrimaryGroupPrincipal(String paramString)
/*     */   {
/*  58 */     super(paramString);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  71 */     MessageFormat localMessageFormat = new MessageFormat(ResourcesMgr.getString("NTSidPrimaryGroupPrincipal.name", "sun.security.util.AuthResources"));
/*     */ 
/*  75 */     Object[] arrayOfObject = { getName() };
/*  76 */     return localMessageFormat.format(arrayOfObject);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  95 */     if (paramObject == null) {
/*  96 */       return false;
/*     */     }
/*  98 */     if (this == paramObject) {
/*  99 */       return true;
/*     */     }
/* 101 */     if (!(paramObject instanceof NTSidPrimaryGroupPrincipal)) {
/* 102 */       return false;
/*     */     }
/* 104 */     return super.equals(paramObject);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.auth.NTSidPrimaryGroupPrincipal
 * JD-Core Version:    0.6.2
 */