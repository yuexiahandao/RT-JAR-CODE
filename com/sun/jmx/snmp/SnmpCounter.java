/*    */ package com.sun.jmx.snmp;
/*    */ 
/*    */ public class SnmpCounter extends SnmpUnsignedInt
/*    */ {
/*    */   private static final long serialVersionUID = 4655264728839396879L;
/*    */   static final String name = "Counter32";
/*    */ 
/*    */   public SnmpCounter(int paramInt)
/*    */     throws IllegalArgumentException
/*    */   {
/* 50 */     super(paramInt);
/*    */   }
/*    */ 
/*    */   public SnmpCounter(Integer paramInteger)
/*    */     throws IllegalArgumentException
/*    */   {
/* 60 */     super(paramInteger);
/*    */   }
/*    */ 
/*    */   public SnmpCounter(long paramLong)
/*    */     throws IllegalArgumentException
/*    */   {
/* 70 */     super(paramLong);
/*    */   }
/*    */ 
/*    */   public SnmpCounter(Long paramLong)
/*    */     throws IllegalArgumentException
/*    */   {
/* 80 */     super(paramLong);
/*    */   }
/*    */ 
/*    */   public final String getTypeName()
/*    */   {
/* 90 */     return "Counter32";
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.SnmpCounter
 * JD-Core Version:    0.6.2
 */