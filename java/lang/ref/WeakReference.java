/*    */
package java.lang.ref;

/*    */
/*    */ public class WeakReference<T> extends Reference<T>
/*    */ {
    /*    */
    public WeakReference(T paramT)
/*    */ {
/* 57 */
        super(paramT);
/*    */
    }

    /*    */
/*    */
    public WeakReference(T paramT, ReferenceQueue<? super T> paramReferenceQueue)
/*    */ {
/* 69 */
        super(paramT, paramReferenceQueue);
/*    */
    }
/*    */
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.ref.WeakReference
 * JD-Core Version:    0.6.2
 */