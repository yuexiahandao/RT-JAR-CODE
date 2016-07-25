/*    */ package sun.reflect.generics.scope;
/*    */ 
/*    */ import java.lang.reflect.TypeVariable;
/*    */ 
/*    */ public class DummyScope
/*    */   implements Scope
/*    */ {
/* 41 */   private static DummyScope singleton = new DummyScope();
/*    */ 
/*    */   public static DummyScope make()
/*    */   {
/* 51 */     return singleton;
/*    */   }
/*    */ 
/*    */   public TypeVariable<?> lookup(String paramString)
/*    */   {
/* 60 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.generics.scope.DummyScope
 * JD-Core Version:    0.6.2
 */