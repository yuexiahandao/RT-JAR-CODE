/*    */ package sun.reflect.generics.scope;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
/*    */ 
/*    */ public class ConstructorScope extends AbstractScope<Constructor>
/*    */ {
/*    */   private ConstructorScope(Constructor paramConstructor)
/*    */   {
/* 39 */     super(paramConstructor);
/*    */   }
/*    */ 
/*    */   private Class<?> getEnclosingClass()
/*    */   {
/* 45 */     return ((Constructor)getRecvr()).getDeclaringClass();
/*    */   }
/*    */ 
/*    */   protected Scope computeEnclosingScope()
/*    */   {
/* 55 */     return ClassScope.make(getEnclosingClass());
/*    */   }
/*    */ 
/*    */   public static ConstructorScope make(Constructor paramConstructor)
/*    */   {
/* 65 */     return new ConstructorScope(paramConstructor);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.generics.scope.ConstructorScope
 * JD-Core Version:    0.6.2
 */