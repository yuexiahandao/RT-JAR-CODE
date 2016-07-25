/*    */ package com.sun.jmx.snmp.agent;
/*    */ 
/*    */ import com.sun.jmx.snmp.SnmpStatusException;
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public abstract class SnmpMibEntry extends SnmpMibNode
/*    */   implements Serializable
/*    */ {
/*    */   public abstract boolean isVariable(long paramLong);
/*    */ 
/*    */   public abstract boolean isReadable(long paramLong);
/*    */ 
/*    */   public long getNextVarId(long paramLong, Object paramObject)
/*    */     throws SnmpStatusException
/*    */   {
/* 74 */     long l = super.getNextVarId(paramLong, paramObject);
/* 75 */     while (!isReadable(l))
/* 76 */       l = super.getNextVarId(l, paramObject);
/* 77 */     return l;
/*    */   }
/*    */ 
/*    */   public void validateVarId(long paramLong, Object paramObject)
/*    */     throws SnmpStatusException
/*    */   {
/* 94 */     if (!isVariable(paramLong)) throw new SnmpStatusException(2);
/*    */   }
/*    */ 
/*    */   public abstract void get(SnmpMibSubRequest paramSnmpMibSubRequest, int paramInt)
/*    */     throws SnmpStatusException;
/*    */ 
/*    */   public abstract void set(SnmpMibSubRequest paramSnmpMibSubRequest, int paramInt)
/*    */     throws SnmpStatusException;
/*    */ 
/*    */   public abstract void check(SnmpMibSubRequest paramSnmpMibSubRequest, int paramInt)
/*    */     throws SnmpStatusException;
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.agent.SnmpMibEntry
 * JD-Core Version:    0.6.2
 */