/*     */ package java.lang;
/*     */ 
/*     */ import java.security.BasicPermission;
/*     */ 
/*     */ public final class RuntimePermission extends BasicPermission
/*     */ {
/*     */   private static final long serialVersionUID = 7399184964622342223L;
/*     */ 
/*     */   public RuntimePermission(String paramString)
/*     */   {
/* 369 */     super(paramString);
/*     */   }
/*     */ 
/*     */   public RuntimePermission(String paramString1, String paramString2)
/*     */   {
/* 386 */     super(paramString1, paramString2);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.RuntimePermission
 * JD-Core Version:    0.6.2
 */