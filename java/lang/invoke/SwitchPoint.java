/*     */ package java.lang.invoke;
/*     */ 
/*     */ public class SwitchPoint
/*     */ {
/* 114 */   private static final MethodHandle K_true = MethodHandles.constant(Boolean.TYPE, Boolean.valueOf(true));
/* 115 */   private static final MethodHandle K_false = MethodHandles.constant(Boolean.TYPE, Boolean.valueOf(false));
/*     */   private final MutableCallSite mcs;
/*     */   private final MethodHandle mcsInvoker;
/*     */ 
/*     */   public SwitchPoint()
/*     */   {
/* 124 */     this.mcs = new MutableCallSite(K_true);
/* 125 */     this.mcsInvoker = this.mcs.dynamicInvoker();
/*     */   }
/*     */ 
/*     */   public boolean hasBeenInvalidated()
/*     */   {
/* 152 */     return this.mcs.getTarget() != K_true;
/*     */   }
/*     */ 
/*     */   public MethodHandle guardWithTest(MethodHandle paramMethodHandle1, MethodHandle paramMethodHandle2)
/*     */   {
/* 171 */     if (this.mcs.getTarget() == K_false)
/* 172 */       return paramMethodHandle2;
/* 173 */     return MethodHandles.guardWithTest(this.mcsInvoker, paramMethodHandle1, paramMethodHandle2);
/*     */   }
/*     */ 
/*     */   public static void invalidateAll(SwitchPoint[] paramArrayOfSwitchPoint)
/*     */   {
/* 218 */     if (paramArrayOfSwitchPoint.length == 0) return;
/* 219 */     MutableCallSite[] arrayOfMutableCallSite = new MutableCallSite[paramArrayOfSwitchPoint.length];
/* 220 */     for (int i = 0; i < paramArrayOfSwitchPoint.length; i++) {
/* 221 */       SwitchPoint localSwitchPoint = paramArrayOfSwitchPoint[i];
/* 222 */       if (localSwitchPoint == null) break;
/* 223 */       arrayOfMutableCallSite[i] = localSwitchPoint.mcs;
/* 224 */       localSwitchPoint.mcs.setTarget(K_false);
/*     */     }
/* 226 */     MutableCallSite.syncAll(arrayOfMutableCallSite);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.invoke.SwitchPoint
 * JD-Core Version:    0.6.2
 */