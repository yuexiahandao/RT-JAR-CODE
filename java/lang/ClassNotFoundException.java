/*     */ package java.lang;
/*     */ 
/*     */ public class ClassNotFoundException extends ReflectiveOperationException
/*     */ {
/*     */   private static final long serialVersionUID = 9176873029745254542L;
/*     */   private Throwable ex;
/*     */ 
/*     */   public ClassNotFoundException()
/*     */   {
/*  72 */     super((Throwable)null);
/*     */   }
/*     */ 
/*     */   public ClassNotFoundException(String paramString)
/*     */   {
/*  82 */     super(paramString, null);
/*     */   }
/*     */ 
/*     */   public ClassNotFoundException(String paramString, Throwable paramThrowable)
/*     */   {
/*  95 */     super(paramString, null);
/*  96 */     this.ex = paramThrowable;
/*     */   }
/*     */ 
/*     */   public Throwable getException()
/*     */   {
/* 111 */     return this.ex;
/*     */   }
/*     */ 
/*     */   public Throwable getCause()
/*     */   {
/* 123 */     return this.ex;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.ClassNotFoundException
 * JD-Core Version:    0.6.2
 */