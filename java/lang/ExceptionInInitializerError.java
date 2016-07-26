/*     */
package java.lang;

/*     */
/*     */ public class ExceptionInInitializerError extends LinkageError
/*     */ {
    /*     */   private static final long serialVersionUID = 1521711792217232256L;
    /*     */   private Throwable exception;

    /*     */
/*     */
    public ExceptionInInitializerError()
/*     */ {
/*  67 */
        initCause(null);
/*     */
    }

    /*     */
/*     */
    public ExceptionInInitializerError(Throwable paramThrowable)
/*     */ {
/*  79 */
        initCause(null);
/*  80 */
        this.exception = paramThrowable;
/*     */
    }

    /*     */
/*     */
    public ExceptionInInitializerError(String paramString)
/*     */ {
/*  94 */
        super(paramString);
/*  95 */
        initCause(null);
/*     */
    }

    /*     */
/*     */
    public Throwable getException()
/*     */ {
/* 112 */
        return this.exception;
/*     */
    }

    /*     */
/*     */
    public Throwable getCause()
/*     */ {
/* 124 */
        return this.exception;
/*     */
    }
/*     */
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.ExceptionInInitializerError
 * JD-Core Version:    0.6.2
 */