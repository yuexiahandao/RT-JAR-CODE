/*    */
package java.lang;

/*    */
/*    */ public class LinkageError extends Error
/*    */ {
    /*    */   private static final long serialVersionUID = 3579600108157160122L;

    /*    */
/*    */
    public LinkageError()
/*    */ {
/*    */
    }

    /*    */
/*    */
    public LinkageError(String paramString)
/*    */ {
/* 55 */
        super(paramString);
/*    */
    }

    /*    */
/*    */
    public LinkageError(String paramString, Throwable paramThrowable)
/*    */ {
/* 67 */
        super(paramString, paramThrowable);
/*    */
    }
/*    */
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.LinkageError
 * JD-Core Version:    0.6.2
 */