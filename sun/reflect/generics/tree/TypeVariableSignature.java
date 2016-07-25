/*    */ package sun.reflect.generics.tree;
/*    */ 
/*    */ import sun.reflect.generics.visitor.TypeTreeVisitor;
/*    */ 
/*    */ public class TypeVariableSignature
/*    */   implements FieldTypeSignature
/*    */ {
/*    */   private String identifier;
/*    */ 
/*    */   private TypeVariableSignature(String paramString)
/*    */   {
/* 33 */     this.identifier = paramString;
/*    */   }
/*    */ 
/*    */   public static TypeVariableSignature make(String paramString) {
/* 37 */     return new TypeVariableSignature(paramString);
/*    */   }
/*    */   public String getIdentifier() {
/* 40 */     return this.identifier;
/*    */   }
/*    */   public void accept(TypeTreeVisitor<?> paramTypeTreeVisitor) {
/* 43 */     paramTypeTreeVisitor.visitTypeVariableSignature(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.generics.tree.TypeVariableSignature
 * JD-Core Version:    0.6.2
 */