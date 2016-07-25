/*    */ package sun.reflect.generics.tree;
/*    */ 
/*    */ import sun.reflect.generics.visitor.Visitor;
/*    */ 
/*    */ public class MethodTypeSignature
/*    */   implements Signature
/*    */ {
/*    */   private FormalTypeParameter[] formalTypeParams;
/*    */   private TypeSignature[] parameterTypes;
/*    */   private ReturnType returnType;
/*    */   private FieldTypeSignature[] exceptionTypes;
/*    */ 
/*    */   private MethodTypeSignature(FormalTypeParameter[] paramArrayOfFormalTypeParameter, TypeSignature[] paramArrayOfTypeSignature, ReturnType paramReturnType, FieldTypeSignature[] paramArrayOfFieldTypeSignature)
/*    */   {
/* 40 */     this.formalTypeParams = paramArrayOfFormalTypeParameter;
/* 41 */     this.parameterTypes = paramArrayOfTypeSignature;
/* 42 */     this.returnType = paramReturnType;
/* 43 */     this.exceptionTypes = paramArrayOfFieldTypeSignature;
/*    */   }
/*    */ 
/*    */   public static MethodTypeSignature make(FormalTypeParameter[] paramArrayOfFormalTypeParameter, TypeSignature[] paramArrayOfTypeSignature, ReturnType paramReturnType, FieldTypeSignature[] paramArrayOfFieldTypeSignature)
/*    */   {
/* 50 */     return new MethodTypeSignature(paramArrayOfFormalTypeParameter, paramArrayOfTypeSignature, paramReturnType, paramArrayOfFieldTypeSignature);
/*    */   }
/*    */ 
/*    */   public FormalTypeParameter[] getFormalTypeParameters() {
/* 54 */     return this.formalTypeParams;
/*    */   }
/* 56 */   public TypeSignature[] getParameterTypes() { return this.parameterTypes; } 
/* 57 */   public ReturnType getReturnType() { return this.returnType; } 
/* 58 */   public FieldTypeSignature[] getExceptionTypes() { return this.exceptionTypes; } 
/*    */   public void accept(Visitor paramVisitor) {
/* 60 */     paramVisitor.visitMethodTypeSignature(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.generics.tree.MethodTypeSignature
 * JD-Core Version:    0.6.2
 */