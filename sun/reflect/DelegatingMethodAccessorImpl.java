/*    */ package sun.reflect;
/*    */ 
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ 
/*    */ class DelegatingMethodAccessorImpl extends MethodAccessorImpl
/*    */ {
/*    */   private MethodAccessorImpl delegate;
/*    */ 
/*    */   DelegatingMethodAccessorImpl(MethodAccessorImpl paramMethodAccessorImpl)
/*    */   {
/* 37 */     setDelegate(paramMethodAccessorImpl);
/*    */   }
/*    */ 
/*    */   public Object invoke(Object paramObject, Object[] paramArrayOfObject)
/*    */     throws IllegalArgumentException, InvocationTargetException
/*    */   {
/* 43 */     return this.delegate.invoke(paramObject, paramArrayOfObject);
/*    */   }
/*    */ 
/*    */   void setDelegate(MethodAccessorImpl paramMethodAccessorImpl) {
/* 47 */     this.delegate = paramMethodAccessorImpl;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.DelegatingMethodAccessorImpl
 * JD-Core Version:    0.6.2
 */