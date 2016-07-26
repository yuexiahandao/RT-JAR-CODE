/*     */
package java.lang.invoke;

/*     */
/*     */ public class ConstantCallSite extends CallSite
/*     */ {
    /*     */   private final boolean isFrozen;

    /*     */
/*     */
    public ConstantCallSite(MethodHandle paramMethodHandle)
/*     */ {
/*  43 */
        super(paramMethodHandle);
/*  44 */
        this.isFrozen = true;
/*     */
    }

    /*     */
/*     */
    protected ConstantCallSite(MethodType paramMethodType, MethodHandle paramMethodHandle)
/*     */     throws Throwable
/*     */ {
/*  81 */
        super(paramMethodType, paramMethodHandle);
/*  82 */
        this.isFrozen = true;
/*     */
    }

    /*     */
/*     */
    public final MethodHandle getTarget()
/*     */ {
/*  95 */
        if (!this.isFrozen) throw new IllegalStateException();
/*  96 */
        return this.target;
/*     */
    }

    /*     */
/*     */
    public final void setTarget(MethodHandle paramMethodHandle)
/*     */ {
/* 106 */
        throw new UnsupportedOperationException();
/*     */
    }

    /*     */
/*     */
    public final MethodHandle dynamicInvoker()
/*     */ {
/* 118 */
        return getTarget();
/*     */
    }
/*     */
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.invoke.ConstantCallSite
 * JD-Core Version:    0.6.2
 */