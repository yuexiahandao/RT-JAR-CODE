/*    */ package sun.reflect.generics.scope;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.lang.reflect.Method;
/*    */ 
/*    */ public class ClassScope extends AbstractScope<Class<?>>
/*    */   implements Scope
/*    */ {
/*    */   private ClassScope(Class<?> paramClass)
/*    */   {
/* 40 */     super(paramClass);
/*    */   }
/*    */ 
/*    */   protected Scope computeEnclosingScope()
/*    */   {
/* 48 */     Class localClass1 = (Class)getRecvr();
/*    */ 
/* 50 */     Method localMethod = localClass1.getEnclosingMethod();
/* 51 */     if (localMethod != null)
/*    */     {
/* 54 */       return MethodScope.make(localMethod);
/*    */     }
/* 56 */     Constructor localConstructor = localClass1.getEnclosingConstructor();
/* 57 */     if (localConstructor != null)
/*    */     {
/* 60 */       return ConstructorScope.make(localConstructor);
/*    */     }
/* 62 */     Class localClass2 = localClass1.getEnclosingClass();
/*    */ 
/* 65 */     if (localClass2 != null)
/*    */     {
/* 68 */       return make(localClass2);
/*    */     }
/*    */ 
/* 72 */     return DummyScope.make();
/*    */   }
/*    */ 
/*    */   public static ClassScope make(Class<?> paramClass)
/*    */   {
/* 81 */     return new ClassScope(paramClass);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.generics.scope.ClassScope
 * JD-Core Version:    0.6.2
 */