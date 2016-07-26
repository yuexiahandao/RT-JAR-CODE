/*    */
package java.lang;

/*    */
/*    */ public class StringIndexOutOfBoundsException extends IndexOutOfBoundsException
/*    */ {
    /*    */   private static final long serialVersionUID = -6762910422159637258L;

    /*    */
/*    */
    public StringIndexOutOfBoundsException()
/*    */ {
/*    */
    }

    /*    */
/*    */
    public StringIndexOutOfBoundsException(String paramString)
/*    */ {
/* 59 */
        super(paramString);
/*    */
    }

    /*    */
/*    */
    public StringIndexOutOfBoundsException(int paramInt)
/*    */ {
/* 69 */
        super("String index out of range: " + paramInt);
/*    */
    }
/*    */
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.StringIndexOutOfBoundsException
 * JD-Core Version:    0.6.2
 */