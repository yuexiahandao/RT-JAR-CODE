/*    */ package sun.reflect.generics.tree;
/*    */ 
/*    */ import sun.reflect.generics.visitor.TypeTreeVisitor;
/*    */ 
/*    */ public class ShortSignature
/*    */   implements BaseType
/*    */ {
/* 32 */   private static ShortSignature singleton = new ShortSignature();
/*    */ 
/*    */   public static ShortSignature make()
/*    */   {
/* 36 */     return singleton;
/*    */   }
/*    */   public void accept(TypeTreeVisitor<?> paramTypeTreeVisitor) {
/* 39 */     paramTypeTreeVisitor.visitShortSignature(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.generics.tree.ShortSignature
 * JD-Core Version:    0.6.2
 */