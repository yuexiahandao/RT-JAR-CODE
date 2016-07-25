/*    */ package com.sun.org.omg.CORBA;
/*    */ 
/*    */ import org.omg.CORBA.IDLType;
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.IDLEntity;
/*    */ 
/*    */ public final class ParameterDescription
/*    */   implements IDLEntity
/*    */ {
/* 39 */   public String name = null;
/* 40 */   public TypeCode type = null;
/*    */ 
/* 43 */   public IDLType type_def = null;
/* 44 */   public ParameterMode mode = null;
/*    */ 
/*    */   public ParameterDescription()
/*    */   {
/*    */   }
/*    */ 
/*    */   public ParameterDescription(String paramString, TypeCode paramTypeCode, IDLType paramIDLType, ParameterMode paramParameterMode)
/*    */   {
/* 54 */     this.name = paramString;
/* 55 */     this.type = paramTypeCode;
/* 56 */     this.type_def = paramIDLType;
/* 57 */     this.mode = paramParameterMode;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.omg.CORBA.ParameterDescription
 * JD-Core Version:    0.6.2
 */