/*    */ package com.sun.jmx.snmp.agent;
/*    */ 
/*    */ import javax.management.Notification;
/*    */ import javax.management.ObjectName;
/*    */ 
/*    */ public class SnmpTableEntryNotification extends Notification
/*    */ {
/*    */   public static final String SNMP_ENTRY_ADDED = "jmx.snmp.table.entry.added";
/*    */   public static final String SNMP_ENTRY_REMOVED = "jmx.snmp.table.entry.removed";
/*    */   private final Object entry;
/*    */   private final ObjectName name;
/*    */   private static final long serialVersionUID = 5832592016227890252L;
/*    */ 
/*    */   SnmpTableEntryNotification(String paramString, Object paramObject1, long paramLong1, long paramLong2, Object paramObject2, ObjectName paramObjectName)
/*    */   {
/* 74 */     super(paramString, paramObject1, paramLong1, paramLong2);
/* 75 */     this.entry = paramObject2;
/* 76 */     this.name = paramObjectName;
/*    */   }
/*    */ 
/*    */   public Object getEntry()
/*    */   {
/* 87 */     return this.entry;
/*    */   }
/*    */ 
/*    */   public ObjectName getEntryName()
/*    */   {
/* 98 */     return this.name;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.agent.SnmpTableEntryNotification
 * JD-Core Version:    0.6.2
 */