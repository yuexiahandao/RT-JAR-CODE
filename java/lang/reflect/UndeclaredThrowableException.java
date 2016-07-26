/*     */
package java.lang.reflect;

/*     */
/*     */ public class UndeclaredThrowableException extends RuntimeException
/*     */ {
    /*     */   static final long serialVersionUID = 330127114055056639L;
    /*     */   private Throwable undeclaredThrowable;

    /*     */
/*     */
    public UndeclaredThrowableException(Throwable paramThrowable)
/*     */ {
/*  75 */
        super((Throwable) null);
/*  76 */
        this.undeclaredThrowable = paramThrowable;
/*     */
    }

    /*     */
/*     */
    public UndeclaredThrowableException(Throwable paramThrowable, String paramString)
/*     */ {
/*  90 */
        super(paramString, null);
/*  91 */
        this.undeclaredThrowable = paramThrowable;
/*     */
    }

    /*     */
/*     */
    public Throwable getUndeclaredThrowable()
/*     */ {
/* 105 */
        return this.undeclaredThrowable;
/*     */
    }

    /*     */
/*     */
    public Throwable getCause()
/*     */ {
/* 117 */
        return this.undeclaredThrowable;
/*     */
    }
/*     */
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.reflect.UndeclaredThrowableException
 * JD-Core Version:    0.6.2
 */