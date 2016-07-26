/*     */
package java.lang.invoke;
/*     */ 
/*     */

import java.util.concurrent.atomic.AtomicInteger;

/*     */
/*     */ public class MutableCallSite extends CallSite
/*     */ {
    /* 282 */   private static final AtomicInteger STORE_BARRIER = new AtomicInteger();

    /*     */
/*     */
    public MutableCallSite(MethodType paramMethodType)
/*     */ {
/* 100 */
        super(paramMethodType);
/*     */
    }

    /*     */
/*     */
    public MutableCallSite(MethodHandle paramMethodHandle)
/*     */ {
/* 110 */
        super(paramMethodHandle);
/*     */
    }

    /*     */
/*     */
    public final MethodHandle getTarget()
/*     */ {
/* 129 */
        return this.target;
/*     */
    }

    /*     */
/*     */
    public void setTarget(MethodHandle paramMethodHandle)
/*     */ {
/* 153 */
        checkTargetChange(this.target, paramMethodHandle);
/* 154 */
        setTargetNormal(paramMethodHandle);
/*     */
    }

    /*     */
/*     */
    public final MethodHandle dynamicInvoker()
/*     */ {
/* 162 */
        return makeDynamicInvoker();
/*     */
    }

    /*     */
/*     */
    public static void syncAll(MutableCallSite[] paramArrayOfMutableCallSite)
/*     */ {
/* 275 */
        if (paramArrayOfMutableCallSite.length == 0) return;
/* 276 */
        STORE_BARRIER.lazySet(0);
/* 277 */
        for (int i = 0; i < paramArrayOfMutableCallSite.length; i++)
/* 278 */
            paramArrayOfMutableCallSite[i].getClass();
/*     */
    }
/*     */
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.invoke.MutableCallSite
 * JD-Core Version:    0.6.2
 */