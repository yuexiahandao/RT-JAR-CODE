/*    */ package sun.reflect.generics.tree;
/*    */ 
/*    */ import sun.reflect.generics.visitor.TypeTreeVisitor;
/*    */ 
/*    */ public class VoidDescriptor
/*    */   implements ReturnType
/*    */ {
/* 33 */   private static VoidDescriptor singleton = new VoidDescriptor();
/*    */ 
/*    */   public static VoidDescriptor make()
/*    */   {
/* 37 */     return singleton;
/*    */   }
/*    */ 
/*    */   public void accept(TypeTreeVisitor<?> paramTypeTreeVisitor) {
/* 41 */     paramTypeTreeVisitor.visitVoidDescriptor(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.generics.tree.VoidDescriptor
 * JD-Core Version:    0.6.2
 */