/*    */ package sun.reflect.generics.tree;
/*    */ 
/*    */ import sun.reflect.generics.visitor.TypeTreeVisitor;
/*    */ 
/*    */ public class FormalTypeParameter
/*    */   implements TypeTree
/*    */ {
/*    */   private String name;
/*    */   private FieldTypeSignature[] bounds;
/*    */ 
/*    */   private FormalTypeParameter(String paramString, FieldTypeSignature[] paramArrayOfFieldTypeSignature)
/*    */   {
/* 36 */     this.name = paramString;
/* 37 */     this.bounds = paramArrayOfFieldTypeSignature;
/*    */   }
/*    */ 
/*    */   public static FormalTypeParameter make(String paramString, FieldTypeSignature[] paramArrayOfFieldTypeSignature)
/*    */   {
/* 48 */     return new FormalTypeParameter(paramString, paramArrayOfFieldTypeSignature);
/*    */   }
/*    */   public FieldTypeSignature[] getBounds() {
/* 51 */     return this.bounds; } 
/* 52 */   public String getName() { return this.name; } 
/*    */   public void accept(TypeTreeVisitor<?> paramTypeTreeVisitor) {
/* 54 */     paramTypeTreeVisitor.visitFormalTypeParameter(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.generics.tree.FormalTypeParameter
 * JD-Core Version:    0.6.2
 */