/*    */ package sun.reflect.generics.tree;
/*    */ 
/*    */ import sun.reflect.generics.visitor.TypeTreeVisitor;
/*    */ 
/*    */ public class SimpleClassTypeSignature
/*    */   implements FieldTypeSignature
/*    */ {
/*    */   private boolean dollar;
/*    */   private String name;
/*    */   private TypeArgument[] typeArgs;
/*    */ 
/*    */   private SimpleClassTypeSignature(String paramString, boolean paramBoolean, TypeArgument[] paramArrayOfTypeArgument)
/*    */   {
/* 36 */     this.name = paramString;
/* 37 */     this.dollar = paramBoolean;
/* 38 */     this.typeArgs = paramArrayOfTypeArgument;
/*    */   }
/*    */ 
/*    */   public static SimpleClassTypeSignature make(String paramString, boolean paramBoolean, TypeArgument[] paramArrayOfTypeArgument)
/*    */   {
/* 44 */     return new SimpleClassTypeSignature(paramString, paramBoolean, paramArrayOfTypeArgument);
/*    */   }
/*    */ 
/*    */   public boolean getDollar()
/*    */   {
/* 53 */     return this.dollar; } 
/* 54 */   public String getName() { return this.name; } 
/* 55 */   public TypeArgument[] getTypeArguments() { return this.typeArgs; }
/*    */ 
/*    */   public void accept(TypeTreeVisitor<?> paramTypeTreeVisitor) {
/* 58 */     paramTypeTreeVisitor.visitSimpleClassTypeSignature(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.generics.tree.SimpleClassTypeSignature
 * JD-Core Version:    0.6.2
 */