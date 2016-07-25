/*    */ package com.sun.org.omg.CORBA;
/*    */ 
/*    */ import org.omg.CORBA.StructMember;
/*    */ import org.omg.CORBA.portable.IDLEntity;
/*    */ 
/*    */ public final class Initializer
/*    */   implements IDLEntity
/*    */ {
/* 41 */   public StructMember[] members = null;
/* 42 */   public String name = null;
/*    */ 
/*    */   public Initializer()
/*    */   {
/*    */   }
/*    */ 
/*    */   public Initializer(StructMember[] paramArrayOfStructMember, String paramString)
/*    */   {
/* 52 */     this.members = paramArrayOfStructMember;
/* 53 */     this.name = paramString;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.omg.CORBA.Initializer
 * JD-Core Version:    0.6.2
 */