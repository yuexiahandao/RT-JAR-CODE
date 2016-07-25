/*    */ package sun.reflect.generics.scope;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ 
/*    */ public class MethodScope extends AbstractScope<Method>
/*    */ {
/*    */   private MethodScope(Method paramMethod)
/*    */   {
/* 39 */     super(paramMethod);
/*    */   }
/*    */ 
/*    */   private Class<?> getEnclosingClass()
/*    */   {
/* 45 */     return ((Method)getRecvr()).getDeclaringClass();
/*    */   }
/*    */ 
/*    */   protected Scope computeEnclosingScope()
/*    */   {
/* 55 */     return ClassScope.make(getEnclosingClass());
/*    */   }
/*    */ 
/*    */   public static MethodScope make(Method paramMethod)
/*    */   {
/* 65 */     return new MethodScope(paramMethod);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.generics.scope.MethodScope
 * JD-Core Version:    0.6.2
 */