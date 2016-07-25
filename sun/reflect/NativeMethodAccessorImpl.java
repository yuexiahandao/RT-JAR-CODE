/*    */ package sun.reflect;
/*    */ 
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import java.lang.reflect.Method;
/*    */ 
/*    */ class NativeMethodAccessorImpl extends MethodAccessorImpl
/*    */ {
/*    */   private Method method;
/*    */   private DelegatingMethodAccessorImpl parent;
/*    */   private int numInvocations;
/*    */ 
/*    */   NativeMethodAccessorImpl(Method paramMethod)
/*    */   {
/* 39 */     this.method = paramMethod;
/*    */   }
/*    */ 
/*    */   public Object invoke(Object paramObject, Object[] paramArrayOfObject)
/*    */     throws IllegalArgumentException, InvocationTargetException
/*    */   {
/* 45 */     if (++this.numInvocations > ReflectionFactory.inflationThreshold()) {
/* 46 */       MethodAccessorImpl localMethodAccessorImpl = (MethodAccessorImpl)new MethodAccessorGenerator().generateMethod(this.method.getDeclaringClass(), this.method.getName(), this.method.getParameterTypes(), this.method.getReturnType(), this.method.getExceptionTypes(), this.method.getModifiers());
/*    */ 
/* 54 */       this.parent.setDelegate(localMethodAccessorImpl);
/*    */     }
/*    */ 
/* 57 */     return invoke0(this.method, paramObject, paramArrayOfObject);
/*    */   }
/*    */ 
/*    */   void setParent(DelegatingMethodAccessorImpl paramDelegatingMethodAccessorImpl) {
/* 61 */     this.parent = paramDelegatingMethodAccessorImpl;
/*    */   }
/*    */ 
/*    */   private static native Object invoke0(Method paramMethod, Object paramObject, Object[] paramArrayOfObject);
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.NativeMethodAccessorImpl
 * JD-Core Version:    0.6.2
 */