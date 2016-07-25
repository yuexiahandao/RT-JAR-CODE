/*    */ package sun.reflect.generics.tree;
/*    */ 
/*    */ import java.util.List;
/*    */ import sun.reflect.generics.visitor.TypeTreeVisitor;
/*    */ 
/*    */ public class ClassTypeSignature
/*    */   implements FieldTypeSignature
/*    */ {
/*    */   private List<SimpleClassTypeSignature> path;
/*    */ 
/*    */   private ClassTypeSignature(List<SimpleClassTypeSignature> paramList)
/*    */   {
/* 40 */     this.path = paramList;
/*    */   }
/*    */ 
/*    */   public static ClassTypeSignature make(List<SimpleClassTypeSignature> paramList) {
/* 44 */     return new ClassTypeSignature(paramList);
/*    */   }
/*    */   public List<SimpleClassTypeSignature> getPath() {
/* 47 */     return this.path;
/*    */   }
/* 49 */   public void accept(TypeTreeVisitor<?> paramTypeTreeVisitor) { paramTypeTreeVisitor.visitClassTypeSignature(this); }
/*    */ 
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.generics.tree.ClassTypeSignature
 * JD-Core Version:    0.6.2
 */