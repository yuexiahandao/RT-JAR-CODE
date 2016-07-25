/*    */ package org.omg.PortableServer;
/*    */ 
/*    */ import org.omg.CORBA.BAD_PARAM;
/*    */ import org.omg.CORBA.portable.IDLEntity;
/*    */ 
/*    */ public class ImplicitActivationPolicyValue
/*    */   implements IDLEntity
/*    */ {
/*    */   private int __value;
/* 24 */   private static int __size = 2;
/* 25 */   private static ImplicitActivationPolicyValue[] __array = new ImplicitActivationPolicyValue[__size];
/*    */   public static final int _IMPLICIT_ACTIVATION = 0;
/* 28 */   public static final ImplicitActivationPolicyValue IMPLICIT_ACTIVATION = new ImplicitActivationPolicyValue(0);
/*    */   public static final int _NO_IMPLICIT_ACTIVATION = 1;
/* 30 */   public static final ImplicitActivationPolicyValue NO_IMPLICIT_ACTIVATION = new ImplicitActivationPolicyValue(1);
/*    */ 
/*    */   public int value()
/*    */   {
/* 34 */     return this.__value;
/*    */   }
/*    */ 
/*    */   public static ImplicitActivationPolicyValue from_int(int paramInt)
/*    */   {
/* 39 */     if ((paramInt >= 0) && (paramInt < __size)) {
/* 40 */       return __array[paramInt];
/*    */     }
/* 42 */     throw new BAD_PARAM();
/*    */   }
/*    */ 
/*    */   protected ImplicitActivationPolicyValue(int paramInt)
/*    */   {
/* 47 */     this.__value = paramInt;
/* 48 */     __array[this.__value] = this;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.PortableServer.ImplicitActivationPolicyValue
 * JD-Core Version:    0.6.2
 */