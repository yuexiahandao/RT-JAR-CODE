/*    */ package sun.tracing.dtrace;
/*    */ 
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import java.lang.reflect.Method;
/*    */ import sun.tracing.ProbeSkeleton;
/*    */ 
/*    */ class DTraceProbe extends ProbeSkeleton
/*    */ {
/*    */   private Object proxy;
/*    */   private Method declared_method;
/*    */   private Method implementing_method;
/*    */ 
/*    */   DTraceProbe(Object paramObject, Method paramMethod)
/*    */   {
/* 39 */     super(paramMethod.getParameterTypes());
/* 40 */     this.proxy = paramObject;
/* 41 */     this.declared_method = paramMethod;
/*    */     try
/*    */     {
/* 45 */       this.implementing_method = paramObject.getClass().getMethod(paramMethod.getName(), paramMethod.getParameterTypes());
/*    */     }
/*    */     catch (NoSuchMethodException localNoSuchMethodException) {
/* 48 */       throw new RuntimeException("Internal error, wrong proxy class");
/*    */     }
/*    */   }
/*    */ 
/*    */   public boolean isEnabled() {
/* 53 */     return JVM.isEnabled(this.implementing_method);
/*    */   }
/*    */ 
/*    */   public void uncheckedTrigger(Object[] paramArrayOfObject) {
/*    */     try {
/* 58 */       this.implementing_method.invoke(this.proxy, paramArrayOfObject);
/*    */     } catch (IllegalAccessException localIllegalAccessException) {
/* 60 */       if (!$assertionsDisabled) throw new AssertionError(); 
/*    */     }
/* 62 */     catch (InvocationTargetException localInvocationTargetException) { if (!$assertionsDisabled) throw new AssertionError(); 
/*    */     }
/*    */   }
/*    */ 
/*    */   String getProbeName()
/*    */   {
/* 67 */     return DTraceProvider.getProbeName(this.declared_method);
/*    */   }
/*    */ 
/*    */   String getFunctionName() {
/* 71 */     return DTraceProvider.getFunctionName(this.declared_method);
/*    */   }
/*    */ 
/*    */   Method getMethod() {
/* 75 */     return this.implementing_method;
/*    */   }
/*    */ 
/*    */   Class<?>[] getParameterTypes() {
/* 79 */     return this.parameters;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.tracing.dtrace.DTraceProbe
 * JD-Core Version:    0.6.2
 */