/*     */ package javax.security.auth;
/*     */ 
/*     */ import java.security.BasicPermission;
/*     */ 
/*     */ public final class AuthPermission extends BasicPermission
/*     */ {
/*     */   private static final long serialVersionUID = 5806031445061587174L;
/*     */ 
/*     */   public AuthPermission(String paramString)
/*     */   {
/* 148 */     super("createLoginContext".equals(paramString) ? "createLoginContext.*" : paramString);
/*     */   }
/*     */ 
/*     */   public AuthPermission(String paramString1, String paramString2)
/*     */   {
/* 169 */     super("createLoginContext".equals(paramString1) ? "createLoginContext.*" : paramString1, paramString2);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.security.auth.AuthPermission
 * JD-Core Version:    0.6.2
 */