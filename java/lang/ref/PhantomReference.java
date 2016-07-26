/*    */
package java.lang.ref;

/*    */
/*    */ public class PhantomReference<T> extends Reference<T>
/*    */ {
    /*    */
    public T get()
/*    */ {
/* 63 */
        return null;
/*    */
    }

    /*    */
/*    */
    public PhantomReference(T paramT, ReferenceQueue<? super T> paramReferenceQueue)
/*    */ {
/* 80 */
        super(paramT, paramReferenceQueue);
/*    */
    }
/*    */
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.ref.PhantomReference
 * JD-Core Version:    0.6.2
 */