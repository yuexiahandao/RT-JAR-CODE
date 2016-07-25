/*    */ package org.omg.PortableServer;
/*    */ 
/*    */ import org.omg.CORBA.BAD_PARAM;
/*    */ import org.omg.CORBA.portable.IDLEntity;
/*    */ 
/*    */ public class ServantRetentionPolicyValue
/*    */   implements IDLEntity
/*    */ {
/*    */   private int __value;
/* 23 */   private static int __size = 2;
/* 24 */   private static ServantRetentionPolicyValue[] __array = new ServantRetentionPolicyValue[__size];
/*    */   public static final int _RETAIN = 0;
/* 27 */   public static final ServantRetentionPolicyValue RETAIN = new ServantRetentionPolicyValue(0);
/*    */   public static final int _NON_RETAIN = 1;
/* 29 */   public static final ServantRetentionPolicyValue NON_RETAIN = new ServantRetentionPolicyValue(1);
/*    */ 
/*    */   public int value()
/*    */   {
/* 33 */     return this.__value;
/*    */   }
/*    */ 
/*    */   public static ServantRetentionPolicyValue from_int(int paramInt)
/*    */   {
/* 38 */     if ((paramInt >= 0) && (paramInt < __size)) {
/* 39 */       return __array[paramInt];
/*    */     }
/* 41 */     throw new BAD_PARAM();
/*    */   }
/*    */ 
/*    */   protected ServantRetentionPolicyValue(int paramInt)
/*    */   {
/* 46 */     this.__value = paramInt;
/* 47 */     __array[this.__value] = this;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.PortableServer.ServantRetentionPolicyValue
 * JD-Core Version:    0.6.2
 */