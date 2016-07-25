/*     */ package java.lang.reflect;
/*     */ 
/*     */ public class InvocationTargetException extends ReflectiveOperationException
/*     */ {
/*     */   private static final long serialVersionUID = 4085088731926701167L;
/*     */   private Throwable target;
/*     */ 
/*     */   protected InvocationTargetException()
/*     */   {
/*  63 */     super((Throwable)null);
/*     */   }
/*     */ 
/*     */   public InvocationTargetException(Throwable paramThrowable)
/*     */   {
/*  72 */     super((Throwable)null);
/*  73 */     this.target = paramThrowable;
/*     */   }
/*     */ 
/*     */   public InvocationTargetException(Throwable paramThrowable, String paramString)
/*     */   {
/*  84 */     super(paramString, null);
/*  85 */     this.target = paramThrowable;
/*     */   }
/*     */ 
/*     */   public Throwable getTargetException()
/*     */   {
/*  98 */     return this.target;
/*     */   }
/*     */ 
/*     */   public Throwable getCause()
/*     */   {
/* 109 */     return this.target;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.reflect.InvocationTargetException
 * JD-Core Version:    0.6.2
 */