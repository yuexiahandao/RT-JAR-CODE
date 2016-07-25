/*     */ package java.lang.invoke;
/*     */ 
/*     */ public class VolatileCallSite extends CallSite
/*     */ {
/*     */   public VolatileCallSite(MethodType paramMethodType)
/*     */   {
/*  53 */     super(paramMethodType);
/*     */   }
/*     */ 
/*     */   public VolatileCallSite(MethodHandle paramMethodHandle)
/*     */   {
/*  63 */     super(paramMethodHandle);
/*     */   }
/*     */ 
/*     */   public final MethodHandle getTarget()
/*     */   {
/*  81 */     return getTargetVolatile();
/*     */   }
/*     */ 
/*     */   public void setTarget(MethodHandle paramMethodHandle)
/*     */   {
/*  98 */     checkTargetChange(getTargetVolatile(), paramMethodHandle);
/*  99 */     setTargetVolatile(paramMethodHandle);
/*     */   }
/*     */ 
/*     */   public final MethodHandle dynamicInvoker()
/*     */   {
/* 107 */     return makeDynamicInvoker();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.invoke.VolatileCallSite
 * JD-Core Version:    0.6.2
 */