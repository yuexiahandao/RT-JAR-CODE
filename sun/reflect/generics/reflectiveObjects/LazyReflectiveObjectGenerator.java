/*    */ package sun.reflect.generics.reflectiveObjects;
/*    */ 
/*    */ import sun.reflect.generics.factory.GenericsFactory;
/*    */ import sun.reflect.generics.visitor.Reifier;
/*    */ 
/*    */ public abstract class LazyReflectiveObjectGenerator
/*    */ {
/*    */   private GenericsFactory factory;
/*    */ 
/*    */   protected LazyReflectiveObjectGenerator(GenericsFactory paramGenericsFactory)
/*    */   {
/* 46 */     this.factory = paramGenericsFactory;
/*    */   }
/*    */ 
/*    */   private GenericsFactory getFactory()
/*    */   {
/* 51 */     return this.factory;
/*    */   }
/*    */ 
/*    */   protected Reifier getReifier() {
/* 55 */     return Reifier.make(getFactory());
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.generics.reflectiveObjects.LazyReflectiveObjectGenerator
 * JD-Core Version:    0.6.2
 */