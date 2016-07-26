/*    */
package java.lang.ref;

/*    */
/*    */ class FinalReference<T> extends Reference<T>
/*    */ {
    /*    */
    public FinalReference(T paramT, ReferenceQueue<? super T> paramReferenceQueue)
/*    */ {
/* 34 */
        super(paramT, paramReferenceQueue);
/*    */
    }
/*    */
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.ref.FinalReference
 * JD-Core Version:    0.6.2
 */