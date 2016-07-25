/*    */ package sun.reflect.generics.tree;
/*    */ 
/*    */ import sun.reflect.generics.visitor.TypeTreeVisitor;
/*    */ 
/*    */ public class BottomSignature
/*    */   implements FieldTypeSignature
/*    */ {
/* 31 */   private static BottomSignature singleton = new BottomSignature();
/*    */ 
/*    */   public static BottomSignature make()
/*    */   {
/* 35 */     return singleton;
/*    */   }
/* 37 */   public void accept(TypeTreeVisitor<?> paramTypeTreeVisitor) { paramTypeTreeVisitor.visitBottomSignature(this); }
/*    */ 
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.generics.tree.BottomSignature
 * JD-Core Version:    0.6.2
 */