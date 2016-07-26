/*     */
package java.lang;

/*     */
/*     */ public class RuntimeException extends Exception
/*     */ {
    /*     */   static final long serialVersionUID = -7034897190745766939L;

    /*     */
/*     */
    public RuntimeException()
/*     */ {
/*     */
    }

    /*     */
/*     */
    public RuntimeException(String paramString)
/*     */ {
/*  62 */
        super(paramString);
/*     */
    }

    /*     */
/*     */
    public RuntimeException(String paramString, Throwable paramThrowable)
/*     */ {
/*  80 */
        super(paramString, paramThrowable);
/*     */
    }

    /*     */
/*     */
    public RuntimeException(Throwable paramThrowable)
/*     */ {
/*  96 */
        super(paramThrowable);
/*     */
    }

    /*     */
/*     */
    protected RuntimeException(String paramString, Throwable paramThrowable, boolean paramBoolean1, boolean paramBoolean2)
/*     */ {
/* 117 */
        super(paramString, paramThrowable, paramBoolean1, paramBoolean2);
/*     */
    }
/*     */
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.RuntimeException
 * JD-Core Version:    0.6.2
 */