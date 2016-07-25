/*    */ package com.sun.corba.se.impl.corba;
/*    */ 
/*    */ import org.omg.CORBA.Principal;
/*    */ 
/*    */ public class PrincipalImpl extends Principal
/*    */ {
/*    */   private byte[] value;
/*    */ 
/*    */   public void name(byte[] paramArrayOfByte)
/*    */   {
/* 41 */     this.value = paramArrayOfByte;
/*    */   }
/*    */ 
/*    */   public byte[] name()
/*    */   {
/* 46 */     return this.value;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.corba.PrincipalImpl
 * JD-Core Version:    0.6.2
 */