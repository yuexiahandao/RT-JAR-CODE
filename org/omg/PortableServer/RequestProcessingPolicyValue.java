/*    */ package org.omg.PortableServer;
/*    */ 
/*    */ import org.omg.CORBA.BAD_PARAM;
/*    */ import org.omg.CORBA.portable.IDLEntity;
/*    */ 
/*    */ public class RequestProcessingPolicyValue
/*    */   implements IDLEntity
/*    */ {
/*    */   private int __value;
/* 33 */   private static int __size = 3;
/* 34 */   private static RequestProcessingPolicyValue[] __array = new RequestProcessingPolicyValue[__size];
/*    */   public static final int _USE_ACTIVE_OBJECT_MAP_ONLY = 0;
/* 37 */   public static final RequestProcessingPolicyValue USE_ACTIVE_OBJECT_MAP_ONLY = new RequestProcessingPolicyValue(0);
/*    */   public static final int _USE_DEFAULT_SERVANT = 1;
/* 39 */   public static final RequestProcessingPolicyValue USE_DEFAULT_SERVANT = new RequestProcessingPolicyValue(1);
/*    */   public static final int _USE_SERVANT_MANAGER = 2;
/* 41 */   public static final RequestProcessingPolicyValue USE_SERVANT_MANAGER = new RequestProcessingPolicyValue(2);
/*    */ 
/*    */   public int value()
/*    */   {
/* 45 */     return this.__value;
/*    */   }
/*    */ 
/*    */   public static RequestProcessingPolicyValue from_int(int paramInt)
/*    */   {
/* 50 */     if ((paramInt >= 0) && (paramInt < __size)) {
/* 51 */       return __array[paramInt];
/*    */     }
/* 53 */     throw new BAD_PARAM();
/*    */   }
/*    */ 
/*    */   protected RequestProcessingPolicyValue(int paramInt)
/*    */   {
/* 58 */     this.__value = paramInt;
/* 59 */     __array[this.__value] = this;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.PortableServer.RequestProcessingPolicyValue
 * JD-Core Version:    0.6.2
 */