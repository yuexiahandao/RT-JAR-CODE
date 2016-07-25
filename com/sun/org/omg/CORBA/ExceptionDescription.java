/*    */ package com.sun.org.omg.CORBA;
/*    */ 
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.IDLEntity;
/*    */ 
/*    */ public final class ExceptionDescription
/*    */   implements IDLEntity
/*    */ {
/* 37 */   public String name = null;
/* 38 */   public String id = null;
/* 39 */   public String defined_in = null;
/* 40 */   public String version = null;
/* 41 */   public TypeCode type = null;
/*    */ 
/*    */   public ExceptionDescription()
/*    */   {
/*    */   }
/*    */ 
/*    */   public ExceptionDescription(String paramString1, String paramString2, String paramString3, String paramString4, TypeCode paramTypeCode)
/*    */   {
/* 49 */     this.name = paramString1;
/* 50 */     this.id = paramString2;
/* 51 */     this.defined_in = paramString3;
/* 52 */     this.version = paramString4;
/* 53 */     this.type = paramTypeCode;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.omg.CORBA.ExceptionDescription
 * JD-Core Version:    0.6.2
 */