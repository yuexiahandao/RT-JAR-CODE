/*    */ package sun.reflect;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ 
/*    */ class NativeConstructorAccessorImpl extends ConstructorAccessorImpl
/*    */ {
/*    */   private Constructor c;
/*    */   private DelegatingConstructorAccessorImpl parent;
/*    */   private int numInvocations;
/*    */ 
/*    */   NativeConstructorAccessorImpl(Constructor paramConstructor)
/*    */   {
/* 39 */     this.c = paramConstructor;
/*    */   }
/*    */ 
/*    */   public Object newInstance(Object[] paramArrayOfObject)
/*    */     throws InstantiationException, IllegalArgumentException, InvocationTargetException
/*    */   {
/* 47 */     if (++this.numInvocations > ReflectionFactory.inflationThreshold()) {
/* 48 */       ConstructorAccessorImpl localConstructorAccessorImpl = (ConstructorAccessorImpl)new MethodAccessorGenerator().generateConstructor(this.c.getDeclaringClass(), this.c.getParameterTypes(), this.c.getExceptionTypes(), this.c.getModifiers());
/*    */ 
/* 54 */       this.parent.setDelegate(localConstructorAccessorImpl);
/*    */     }
/*    */ 
/* 57 */     return newInstance0(this.c, paramArrayOfObject);
/*    */   }
/*    */ 
/*    */   void setParent(DelegatingConstructorAccessorImpl paramDelegatingConstructorAccessorImpl) {
/* 61 */     this.parent = paramDelegatingConstructorAccessorImpl;
/*    */   }
/*    */ 
/*    */   private static native Object newInstance0(Constructor paramConstructor, Object[] paramArrayOfObject)
/*    */     throws InstantiationException, IllegalArgumentException, InvocationTargetException;
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.NativeConstructorAccessorImpl
 * JD-Core Version:    0.6.2
 */