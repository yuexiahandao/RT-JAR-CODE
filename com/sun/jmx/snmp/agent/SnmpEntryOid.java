/*    */ package com.sun.jmx.snmp.agent;
/*    */ 
/*    */ import com.sun.jmx.snmp.SnmpOid;
/*    */ 
/*    */ class SnmpEntryOid extends SnmpOid
/*    */ {
/*    */   private static final long serialVersionUID = 9212653887791059564L;
/*    */ 
/*    */   public SnmpEntryOid(long[] paramArrayOfLong, int paramInt)
/*    */   {
/* 45 */     int i = paramArrayOfLong.length - paramInt;
/* 46 */     long[] arrayOfLong = new long[i];
/* 47 */     System.arraycopy(paramArrayOfLong, paramInt, arrayOfLong, 0, i);
/* 48 */     this.components = arrayOfLong;
/* 49 */     this.componentCount = i;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.agent.SnmpEntryOid
 * JD-Core Version:    0.6.2
 */