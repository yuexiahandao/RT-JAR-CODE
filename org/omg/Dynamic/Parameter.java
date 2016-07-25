/*    */ package org.omg.Dynamic;
/*    */ 
/*    */ import org.omg.CORBA.Any;
/*    */ import org.omg.CORBA.ParameterMode;
/*    */ import org.omg.CORBA.portable.IDLEntity;
/*    */ 
/*    */ public final class Parameter
/*    */   implements IDLEntity
/*    */ {
/* 13 */   public Any argument = null;
/* 14 */   public ParameterMode mode = null;
/*    */ 
/*    */   public Parameter()
/*    */   {
/*    */   }
/*    */ 
/*    */   public Parameter(Any paramAny, ParameterMode paramParameterMode)
/*    */   {
/* 22 */     this.argument = paramAny;
/* 23 */     this.mode = paramParameterMode;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.Dynamic.Parameter
 * JD-Core Version:    0.6.2
 */