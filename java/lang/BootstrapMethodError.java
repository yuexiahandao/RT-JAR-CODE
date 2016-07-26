/*    */
package java.lang;

/*    */
/*    */ public class BootstrapMethodError extends LinkageError
/*    */ {
    /*    */   private static final long serialVersionUID = 292L;

    /*    */
/*    */
    public BootstrapMethodError()
/*    */ {
/*    */
    }

    /*    */
/*    */
    public BootstrapMethodError(String paramString)
/*    */ {
/* 55 */
        super(paramString);
/*    */
    }

    /*    */
/*    */
    public BootstrapMethodError(String paramString, Throwable paramThrowable)
/*    */ {
/* 66 */
        super(paramString, paramThrowable);
/*    */
    }

    /*    */
/*    */
    public BootstrapMethodError(Throwable paramThrowable)
/*    */ {
/* 77 */
        super(paramThrowable == null ? null : paramThrowable.toString());
/* 78 */
        initCause(paramThrowable);
/*    */
    }
/*    */
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.BootstrapMethodError
 * JD-Core Version:    0.6.2
 */