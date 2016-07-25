/*     */ package com.sun.security.auth;
/*     */ 
/*     */ import java.text.MessageFormat;
/*     */ import sun.security.util.ResourcesMgr;
/*     */ 
/*     */ public class NTSidGroupPrincipal extends NTSid
/*     */ {
/*     */   private static final long serialVersionUID = -1373347438636198229L;
/*     */ 
/*     */   public NTSidGroupPrincipal(String paramString)
/*     */   {
/*  58 */     super(paramString);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  69 */     MessageFormat localMessageFormat = new MessageFormat(ResourcesMgr.getString("NTSidGroupPrincipal.name", "sun.security.util.AuthResources"));
/*     */ 
/*  73 */     Object[] arrayOfObject = { getName() };
/*  74 */     return localMessageFormat.format(arrayOfObject);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  92 */     if (paramObject == null) {
/*  93 */       return false;
/*     */     }
/*  95 */     if (this == paramObject) {
/*  96 */       return true;
/*     */     }
/*  98 */     if (!(paramObject instanceof NTSidGroupPrincipal)) {
/*  99 */       return false;
/*     */     }
/* 101 */     return super.equals(paramObject);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.auth.NTSidGroupPrincipal
 * JD-Core Version:    0.6.2
 */