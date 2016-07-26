/*    */
package java.lang;

/*    */
/*    */ public class IllegalStateException extends RuntimeException
/*    */ {
    /*    */   static final long serialVersionUID = -1848914673093119416L;

    /*    */
/*    */
    public IllegalStateException()
/*    */ {
/*    */
    }

    /*    */
/*    */
    public IllegalStateException(String paramString)
/*    */ {
/* 55 */
        super(paramString);
/*    */
    }

    /*    */
/*    */
    public IllegalStateException(String paramString, Throwable paramThrowable)
/*    */ {
/* 75 */
        super(paramString, paramThrowable);
/*    */
    }

    /*    */
/*    */
    public IllegalStateException(Throwable paramThrowable)
/*    */ {
/* 93 */
        super(paramThrowable);
/*    */
    }
/*    */
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.IllegalStateException
 * JD-Core Version:    0.6.2
 */