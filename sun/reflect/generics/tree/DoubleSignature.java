/*    */ package sun.reflect.generics.tree;
/*    */ 
/*    */ import sun.reflect.generics.visitor.TypeTreeVisitor;
/*    */ 
/*    */ public class DoubleSignature
/*    */   implements BaseType
/*    */ {
/* 32 */   private static DoubleSignature singleton = new DoubleSignature();
/*    */ 
/*    */   public static DoubleSignature make()
/*    */   {
/* 36 */     return singleton;
/*    */   }
/* 38 */   public void accept(TypeTreeVisitor<?> paramTypeTreeVisitor) { paramTypeTreeVisitor.visitDoubleSignature(this); }
/*    */ 
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.generics.tree.DoubleSignature
 * JD-Core Version:    0.6.2
 */