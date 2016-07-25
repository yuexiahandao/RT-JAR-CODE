/*    */ package sun.reflect.generics.tree;
/*    */ 
/*    */ import sun.reflect.generics.visitor.TypeTreeVisitor;
/*    */ 
/*    */ public class ArrayTypeSignature
/*    */   implements FieldTypeSignature
/*    */ {
/*    */   private TypeSignature componentType;
/*    */ 
/*    */   private ArrayTypeSignature(TypeSignature paramTypeSignature)
/*    */   {
/* 33 */     this.componentType = paramTypeSignature;
/*    */   }
/*    */   public static ArrayTypeSignature make(TypeSignature paramTypeSignature) {
/* 36 */     return new ArrayTypeSignature(paramTypeSignature);
/*    */   }
/*    */   public TypeSignature getComponentType() {
/* 39 */     return this.componentType;
/*    */   }
/*    */   public void accept(TypeTreeVisitor<?> paramTypeTreeVisitor) {
/* 42 */     paramTypeTreeVisitor.visitArrayTypeSignature(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.generics.tree.ArrayTypeSignature
 * JD-Core Version:    0.6.2
 */