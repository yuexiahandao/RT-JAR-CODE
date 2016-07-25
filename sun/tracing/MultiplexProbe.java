/*     */ package sun.tracing;
/*     */ 
/*     */ import com.sun.tracing.Probe;
/*     */ import com.sun.tracing.Provider;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ 
/*     */ class MultiplexProbe extends ProbeSkeleton
/*     */ {
/*     */   private Set<Probe> probes;
/*     */ 
/*     */   MultiplexProbe(Method paramMethod, Set<Provider> paramSet)
/*     */   {
/*  98 */     super(paramMethod.getParameterTypes());
/*  99 */     this.probes = new HashSet();
/* 100 */     for (Provider localProvider : paramSet) {
/* 101 */       Probe localProbe = localProvider.getProbe(paramMethod);
/* 102 */       if (localProbe != null)
/* 103 */         this.probes.add(localProbe);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isEnabled()
/*     */   {
/* 109 */     for (Probe localProbe : this.probes) {
/* 110 */       if (localProbe.isEnabled()) {
/* 111 */         return true;
/*     */       }
/*     */     }
/* 114 */     return false;
/*     */   }
/*     */ 
/*     */   public void uncheckedTrigger(Object[] paramArrayOfObject) {
/* 118 */     for (Probe localProbe : this.probes)
/*     */       try
/*     */       {
/* 121 */         ProbeSkeleton localProbeSkeleton = (ProbeSkeleton)localProbe;
/* 122 */         localProbeSkeleton.uncheckedTrigger(paramArrayOfObject);
/*     */       }
/*     */       catch (ClassCastException localClassCastException)
/*     */       {
/*     */         try {
/* 127 */           Method localMethod = Probe.class.getMethod("trigger", new Class[] { Class.forName("[java.lang.Object") });
/*     */ 
/* 129 */           localMethod.invoke(localProbe, paramArrayOfObject);
/*     */         } catch (Exception localException) {
/* 131 */           if (!$assertionsDisabled) throw new AssertionError();
/*     */         }
/*     */       }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.tracing.MultiplexProbe
 * JD-Core Version:    0.6.2
 */