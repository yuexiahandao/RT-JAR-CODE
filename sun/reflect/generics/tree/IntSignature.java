/*    */ package sun.reflect.generics.tree;
/*    */ 
/*    */ import sun.reflect.generics.visitor.TypeTreeVisitor;
/*    */ 
/*    */ public class IntSignature
/*    */   implements BaseType
/*    */ {
/* 32 */   private static IntSignature singleton = new IntSignature();
/*    */ 
/*    */   public static IntSignature make()
/*    */   {
/* 36 */     return singleton;
/*    */   }
/* 38 */   public void accept(TypeTreeVisitor<?> paramTypeTreeVisitor) { paramTypeTreeVisitor.visitIntSignature(this); }
/*    */ 
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.generics.tree.IntSignature
 * JD-Core Version:    0.6.2
 */