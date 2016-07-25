/*     */ package javax.net.ssl;
/*     */ 
/*     */ import java.security.BasicPermission;
/*     */ 
/*     */ public final class SSLPermission extends BasicPermission
/*     */ {
/*     */   private static final long serialVersionUID = -3456898025505876775L;
/*     */ 
/*     */   public SSLPermission(String paramString)
/*     */   {
/* 125 */     super(paramString);
/*     */   }
/*     */ 
/*     */   public SSLPermission(String paramString1, String paramString2)
/*     */   {
/* 142 */     super(paramString1, paramString2);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.net.ssl.SSLPermission
 * JD-Core Version:    0.6.2
 */