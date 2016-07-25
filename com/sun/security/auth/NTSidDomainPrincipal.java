/*     */ package com.sun.security.auth;
/*     */ 
/*     */ import java.text.MessageFormat;
/*     */ import sun.security.util.ResourcesMgr;
/*     */ 
/*     */ public class NTSidDomainPrincipal extends NTSid
/*     */ {
/*     */   private static final long serialVersionUID = 5247810785821650912L;
/*     */ 
/*     */   public NTSidDomainPrincipal(String paramString)
/*     */   {
/*  62 */     super(paramString);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  74 */     MessageFormat localMessageFormat = new MessageFormat(ResourcesMgr.getString("NTSidDomainPrincipal.name", "sun.security.util.AuthResources"));
/*     */ 
/*  78 */     Object[] arrayOfObject = { getName() };
/*  79 */     return localMessageFormat.format(arrayOfObject);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  97 */     if (paramObject == null) {
/*  98 */       return false;
/*     */     }
/* 100 */     if (this == paramObject) {
/* 101 */       return true;
/*     */     }
/* 103 */     if (!(paramObject instanceof NTSidDomainPrincipal)) {
/* 104 */       return false;
/*     */     }
/* 106 */     return super.equals(paramObject);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.auth.NTSidDomainPrincipal
 * JD-Core Version:    0.6.2
 */