/*    */ package sun.reflect.generics.repository;
/*    */ 
/*    */ import java.lang.reflect.TypeVariable;
/*    */ import sun.reflect.generics.factory.GenericsFactory;
/*    */ import sun.reflect.generics.tree.FormalTypeParameter;
/*    */ import sun.reflect.generics.tree.Signature;
/*    */ import sun.reflect.generics.visitor.Reifier;
/*    */ 
/*    */ public abstract class GenericDeclRepository<S extends Signature> extends AbstractRepository<S>
/*    */ {
/*    */   private TypeVariable[] typeParams;
/*    */ 
/*    */   protected GenericDeclRepository(String paramString, GenericsFactory paramGenericsFactory)
/*    */   {
/* 48 */     super(paramString, paramGenericsFactory);
/*    */   }
/*    */ 
/*    */   public TypeVariable[] getTypeParameters()
/*    */   {
/* 68 */     if (this.typeParams == null)
/*    */     {
/* 70 */       FormalTypeParameter[] arrayOfFormalTypeParameter = ((Signature)getTree()).getFormalTypeParameters();
/*    */ 
/* 72 */       TypeVariable[] arrayOfTypeVariable = new TypeVariable[arrayOfFormalTypeParameter.length];
/*    */ 
/* 74 */       for (int i = 0; i < arrayOfFormalTypeParameter.length; i++) {
/* 75 */         Reifier localReifier = getReifier();
/* 76 */         arrayOfFormalTypeParameter[i].accept(localReifier);
/*    */ 
/* 78 */         arrayOfTypeVariable[i] = ((TypeVariable)localReifier.getResult());
/*    */       }
/* 80 */       this.typeParams = arrayOfTypeVariable;
/*    */     }
/* 82 */     return (TypeVariable[])this.typeParams.clone();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.generics.repository.GenericDeclRepository
 * JD-Core Version:    0.6.2
 */