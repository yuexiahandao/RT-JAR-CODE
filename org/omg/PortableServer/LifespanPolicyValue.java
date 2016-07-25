/*    */ package org.omg.PortableServer;
/*    */ 
/*    */ import org.omg.CORBA.BAD_PARAM;
/*    */ import org.omg.CORBA.portable.IDLEntity;
/*    */ 
/*    */ public class LifespanPolicyValue
/*    */   implements IDLEntity
/*    */ {
/*    */   private int __value;
/* 23 */   private static int __size = 2;
/* 24 */   private static LifespanPolicyValue[] __array = new LifespanPolicyValue[__size];
/*    */   public static final int _TRANSIENT = 0;
/* 27 */   public static final LifespanPolicyValue TRANSIENT = new LifespanPolicyValue(0);
/*    */   public static final int _PERSISTENT = 1;
/* 29 */   public static final LifespanPolicyValue PERSISTENT = new LifespanPolicyValue(1);
/*    */ 
/*    */   public int value()
/*    */   {
/* 33 */     return this.__value;
/*    */   }
/*    */ 
/*    */   public static LifespanPolicyValue from_int(int paramInt)
/*    */   {
/* 38 */     if ((paramInt >= 0) && (paramInt < __size)) {
/* 39 */       return __array[paramInt];
/*    */     }
/* 41 */     throw new BAD_PARAM();
/*    */   }
/*    */ 
/*    */   protected LifespanPolicyValue(int paramInt)
/*    */   {
/* 46 */     this.__value = paramInt;
/* 47 */     __array[this.__value] = this;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.PortableServer.LifespanPolicyValue
 * JD-Core Version:    0.6.2
 */