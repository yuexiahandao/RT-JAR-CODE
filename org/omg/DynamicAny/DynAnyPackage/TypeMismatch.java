/*    */ package org.omg.DynamicAny.DynAnyPackage;
/*    */ 
/*    */ import org.omg.CORBA.UserException;
/*    */ 
/*    */ public final class TypeMismatch extends UserException
/*    */ {
/*    */   public TypeMismatch()
/*    */   {
/* 16 */     super(TypeMismatchHelper.id());
/*    */   }
/*    */ 
/*    */   public TypeMismatch(String paramString)
/*    */   {
/* 22 */     super(TypeMismatchHelper.id() + "  " + paramString);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.DynamicAny.DynAnyPackage.TypeMismatch
 * JD-Core Version:    0.6.2
 */