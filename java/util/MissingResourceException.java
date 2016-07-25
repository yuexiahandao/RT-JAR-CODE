/*     */ package java.util;
/*     */ 
/*     */ public class MissingResourceException extends RuntimeException
/*     */ {
/*     */   private static final long serialVersionUID = -4876345176062000401L;
/*     */   private String className;
/*     */   private String key;
/*     */ 
/*     */   public MissingResourceException(String paramString1, String paramString2, String paramString3)
/*     */   {
/*  61 */     super(paramString1);
/*  62 */     this.className = paramString2;
/*  63 */     this.key = paramString3;
/*     */   }
/*     */ 
/*     */   MissingResourceException(String paramString1, String paramString2, String paramString3, Throwable paramThrowable)
/*     */   {
/*  85 */     super(paramString1, paramThrowable);
/*  86 */     this.className = paramString2;
/*  87 */     this.key = paramString3;
/*     */   }
/*     */ 
/*     */   public String getClassName()
/*     */   {
/*  96 */     return this.className;
/*     */   }
/*     */ 
/*     */   public String getKey()
/*     */   {
/* 105 */     return this.key;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.MissingResourceException
 * JD-Core Version:    0.6.2
 */