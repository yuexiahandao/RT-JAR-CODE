/*     */ package com.sun.net.ssl;
/*     */ 
/*     */ import java.security.BasicPermission;
/*     */ 
/*     */ @Deprecated
/*     */ public final class SSLPermission extends BasicPermission
/*     */ {
/*     */   private static final long serialVersionUID = -2583684302506167542L;
/*     */ 
/*     */   public SSLPermission(String paramString)
/*     */   {
/* 119 */     super(paramString);
/*     */   }
/*     */ 
/*     */   public SSLPermission(String paramString1, String paramString2)
/*     */   {
/* 135 */     super(paramString1, paramString2);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.net.ssl.SSLPermission
 * JD-Core Version:    0.6.2
 */