/*    */ package com.sun.corba.se.spi.extension;
/*    */ 
/*    */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*    */ import org.omg.CORBA.LocalObject;
/*    */ import org.omg.CORBA.Policy;
/*    */ 
/*    */ public class RequestPartitioningPolicy extends LocalObject
/*    */   implements Policy
/*    */ {
/* 41 */   private static ORBUtilSystemException wrapper = ORBUtilSystemException.get("oa.ior");
/*    */   public static final int DEFAULT_VALUE = 0;
/*    */   private final int value;
/*    */ 
/*    */   public RequestPartitioningPolicy(int paramInt)
/*    */   {
/* 48 */     if ((paramInt < 0) || (paramInt > 63))
/*    */     {
/* 50 */       throw wrapper.invalidRequestPartitioningPolicyValue(new Integer(paramInt), new Integer(0), new Integer(63));
/*    */     }
/*    */ 
/* 57 */     this.value = paramInt;
/*    */   }
/*    */ 
/*    */   public int getValue()
/*    */   {
/* 62 */     return this.value;
/*    */   }
/*    */ 
/*    */   public int policy_type()
/*    */   {
/* 67 */     return 1398079491;
/*    */   }
/*    */ 
/*    */   public Policy copy()
/*    */   {
/* 72 */     return this;
/*    */   }
/*    */ 
/*    */   public void destroy()
/*    */   {
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 82 */     return "RequestPartitioningPolicy[" + this.value + "]";
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.extension.RequestPartitioningPolicy
 * JD-Core Version:    0.6.2
 */