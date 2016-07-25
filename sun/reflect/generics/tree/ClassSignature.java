/*    */ package sun.reflect.generics.tree;
/*    */ 
/*    */ import sun.reflect.generics.visitor.Visitor;
/*    */ 
/*    */ public class ClassSignature
/*    */   implements Signature
/*    */ {
/*    */   private FormalTypeParameter[] formalTypeParams;
/*    */   private ClassTypeSignature superclass;
/*    */   private ClassTypeSignature[] superInterfaces;
/*    */ 
/*    */   private ClassSignature(FormalTypeParameter[] paramArrayOfFormalTypeParameter, ClassTypeSignature paramClassTypeSignature, ClassTypeSignature[] paramArrayOfClassTypeSignature)
/*    */   {
/* 38 */     this.formalTypeParams = paramArrayOfFormalTypeParameter;
/* 39 */     this.superclass = paramClassTypeSignature;
/* 40 */     this.superInterfaces = paramArrayOfClassTypeSignature;
/*    */   }
/*    */ 
/*    */   public static ClassSignature make(FormalTypeParameter[] paramArrayOfFormalTypeParameter, ClassTypeSignature paramClassTypeSignature, ClassTypeSignature[] paramArrayOfClassTypeSignature)
/*    */   {
/* 46 */     return new ClassSignature(paramArrayOfFormalTypeParameter, paramClassTypeSignature, paramArrayOfClassTypeSignature);
/*    */   }
/*    */ 
/*    */   public FormalTypeParameter[] getFormalTypeParameters() {
/* 50 */     return this.formalTypeParams;
/*    */   }
/* 52 */   public ClassTypeSignature getSuperclass() { return this.superclass; } 
/* 53 */   public ClassTypeSignature[] getSuperInterfaces() { return this.superInterfaces; } 
/*    */   public void accept(Visitor paramVisitor) {
/* 55 */     paramVisitor.visitClassSignature(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.generics.tree.ClassSignature
 * JD-Core Version:    0.6.2
 */