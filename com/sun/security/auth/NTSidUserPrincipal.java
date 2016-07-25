/*     */ package com.sun.security.auth;
/*     */ 
/*     */ import java.text.MessageFormat;
/*     */ import sun.security.util.ResourcesMgr;
/*     */ 
/*     */ public class NTSidUserPrincipal extends NTSid
/*     */ {
/*     */   private static final long serialVersionUID = -5573239889517749525L;
/*     */ 
/*     */   public NTSidUserPrincipal(String paramString)
/*     */   {
/*  57 */     super(paramString);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  68 */     MessageFormat localMessageFormat = new MessageFormat(ResourcesMgr.getString("NTSidUserPrincipal.name", "sun.security.util.AuthResources"));
/*     */ 
/*  72 */     Object[] arrayOfObject = { getName() };
/*  73 */     return localMessageFormat.format(arrayOfObject);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  91 */     if (paramObject == null) {
/*  92 */       return false;
/*     */     }
/*  94 */     if (this == paramObject) {
/*  95 */       return true;
/*     */     }
/*  97 */     if (!(paramObject instanceof NTSidUserPrincipal)) {
/*  98 */       return false;
/*     */     }
/* 100 */     return super.equals(paramObject);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.auth.NTSidUserPrincipal
 * JD-Core Version:    0.6.2
 */