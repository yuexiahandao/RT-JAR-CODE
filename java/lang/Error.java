/*     */
package java.lang;

/*     */
/*     */ public class Error extends Throwable
/*     */ {
    /*     */   static final long serialVersionUID = 4980196508277280342L;

    /*     */
/*     */
    public Error()
/*     */ {
/*     */
    }

    /*     */
/*     */
    public Error(String paramString)
/*     */ {
/*  70 */
        super(paramString);
/*     */
    }

    /*     */
/*     */
    public Error(String paramString, Throwable paramThrowable)
/*     */ {
/*  88 */
        super(paramString, paramThrowable);
/*     */
    }

    /*     */
/*     */
    public Error(Throwable paramThrowable)
/*     */ {
/* 105 */
        super(paramThrowable);
/*     */
    }

    /*     */
/*     */
    protected Error(String paramString, Throwable paramThrowable, boolean paramBoolean1, boolean paramBoolean2)
/*     */ {
/* 126 */
        super(paramString, paramThrowable, paramBoolean1, paramBoolean2);
/*     */
    }
/*     */
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.Error
 * JD-Core Version:    0.6.2
 */