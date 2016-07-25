/*    */ package com.sun.corba.se.spi.extension;
/*    */ 
/*    */ import org.omg.CORBA.LocalObject;
/*    */ import org.omg.CORBA.Policy;
/*    */ 
/*    */ public class CopyObjectPolicy extends LocalObject
/*    */   implements Policy
/*    */ {
/*    */   private final int value;
/*    */ 
/*    */   public CopyObjectPolicy(int paramInt)
/*    */   {
/* 40 */     this.value = paramInt;
/*    */   }
/*    */ 
/*    */   public int getValue()
/*    */   {
/* 45 */     return this.value;
/*    */   }
/*    */ 
/*    */   public int policy_type()
/*    */   {
/* 50 */     return 1398079490;
/*    */   }
/*    */ 
/*    */   public Policy copy()
/*    */   {
/* 55 */     return this;
/*    */   }
/*    */ 
/*    */   public void destroy()
/*    */   {
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 65 */     return "CopyObjectPolicy[" + this.value + "]";
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.extension.CopyObjectPolicy
 * JD-Core Version:    0.6.2
 */