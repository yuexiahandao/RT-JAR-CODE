/*    */ package com.sun.org.omg.CORBA;
/*    */ 
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.IDLEntity;
/*    */ 
/*    */ public final class OperationDescription
/*    */   implements IDLEntity
/*    */ {
/* 37 */   public String name = null;
/* 38 */   public String id = null;
/* 39 */   public String defined_in = null;
/* 40 */   public String version = null;
/* 41 */   public TypeCode result = null;
/* 42 */   public OperationMode mode = null;
/* 43 */   public String[] contexts = null;
/* 44 */   public ParameterDescription[] parameters = null;
/* 45 */   public ExceptionDescription[] exceptions = null;
/*    */ 
/*    */   public OperationDescription()
/*    */   {
/*    */   }
/*    */ 
/*    */   public OperationDescription(String paramString1, String paramString2, String paramString3, String paramString4, TypeCode paramTypeCode, OperationMode paramOperationMode, String[] paramArrayOfString, ParameterDescription[] paramArrayOfParameterDescription, ExceptionDescription[] paramArrayOfExceptionDescription)
/*    */   {
/* 53 */     this.name = paramString1;
/* 54 */     this.id = paramString2;
/* 55 */     this.defined_in = paramString3;
/* 56 */     this.version = paramString4;
/* 57 */     this.result = paramTypeCode;
/* 58 */     this.mode = paramOperationMode;
/* 59 */     this.contexts = paramArrayOfString;
/* 60 */     this.parameters = paramArrayOfParameterDescription;
/* 61 */     this.exceptions = paramArrayOfExceptionDescription;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.omg.CORBA.OperationDescription
 * JD-Core Version:    0.6.2
 */