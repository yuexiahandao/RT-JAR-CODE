/*     */ package java.lang.management;
/*     */ 
/*     */ import java.security.BasicPermission;
/*     */ 
/*     */ public final class ManagementPermission extends BasicPermission
/*     */ {
/*     */   private static final long serialVersionUID = 1897496590799378737L;
/*     */ 
/*     */   public ManagementPermission(String paramString)
/*     */   {
/*  97 */     super(paramString);
/*  98 */     if ((!paramString.equals("control")) && (!paramString.equals("monitor")))
/*  99 */       throw new IllegalArgumentException("name: " + paramString);
/*     */   }
/*     */ 
/*     */   public ManagementPermission(String paramString1, String paramString2)
/*     */     throws IllegalArgumentException
/*     */   {
/* 115 */     super(paramString1);
/* 116 */     if ((!paramString1.equals("control")) && (!paramString1.equals("monitor"))) {
/* 117 */       throw new IllegalArgumentException("name: " + paramString1);
/*     */     }
/* 119 */     if ((paramString2 != null) && (paramString2.length() > 0))
/* 120 */       throw new IllegalArgumentException("actions: " + paramString2);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.management.ManagementPermission
 * JD-Core Version:    0.6.2
 */