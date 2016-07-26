/*    */
package java.lang;

/*    */
/*    */ public class IllegalArgumentException extends RuntimeException
/*    */ {
    /*    */   private static final long serialVersionUID = -5365630128856068164L;

    /*    */
/*    */
    public IllegalArgumentException()
/*    */ {
/*    */
    }

    /*    */
/*    */
    public IllegalArgumentException(String paramString)
/*    */ {
/* 53 */
        super(paramString);
/*    */
    }

    /*    */
/*    */
    public IllegalArgumentException(String paramString, Throwable paramThrowable)
/*    */ {
/* 73 */
        super(paramString, paramThrowable);
/*    */
    }

    /*    */
/*    */
    public IllegalArgumentException(Throwable paramThrowable)
/*    */ {
/* 91 */
        super(paramThrowable);
/*    */
    }
/*    */
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.IllegalArgumentException
 * JD-Core Version:    0.6.2
 */