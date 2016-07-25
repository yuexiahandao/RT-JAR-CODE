/*    */ package sun.reflect.generics.scope;
/*    */ 
/*    */ import java.lang.reflect.GenericDeclaration;
/*    */ import java.lang.reflect.TypeVariable;
/*    */ 
/*    */ public abstract class AbstractScope<D extends GenericDeclaration>
/*    */   implements Scope
/*    */ {
/*    */   private D recvr;
/*    */   private Scope enclosingScope;
/*    */ 
/*    */   protected AbstractScope(D paramD)
/*    */   {
/* 53 */     this.recvr = paramD;
/*    */   }
/*    */ 
/*    */   protected D getRecvr()
/*    */   {
/* 60 */     return this.recvr;
/*    */   }
/*    */ 
/*    */   protected abstract Scope computeEnclosingScope();
/*    */ 
/*    */   protected Scope getEnclosingScope()
/*    */   {
/* 74 */     if (this.enclosingScope == null) this.enclosingScope = computeEnclosingScope();
/* 75 */     return this.enclosingScope;
/*    */   }
/*    */ 
/*    */   public TypeVariable<?> lookup(String paramString)
/*    */   {
/* 86 */     TypeVariable[] arrayOfTypeVariable1 = getRecvr().getTypeParameters();
/* 87 */     for (TypeVariable localTypeVariable : arrayOfTypeVariable1) {
/* 88 */       if (localTypeVariable.getName().equals(paramString)) return localTypeVariable;
/*    */     }
/* 90 */     return getEnclosingScope().lookup(paramString);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.generics.scope.AbstractScope
 * JD-Core Version:    0.6.2
 */