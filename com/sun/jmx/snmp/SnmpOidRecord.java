/*    */ package com.sun.jmx.snmp;
/*    */ 
/*    */ public class SnmpOidRecord
/*    */ {
/*    */   private String name;
/*    */   private String oid;
/*    */   private String type;
/*    */ 
/*    */   public SnmpOidRecord(String paramString1, String paramString2, String paramString3)
/*    */   {
/* 47 */     this.name = paramString1;
/* 48 */     this.oid = paramString2;
/* 49 */     this.type = paramString3;
/*    */   }
/*    */ 
/*    */   public String getName()
/*    */   {
/* 57 */     return this.name;
/*    */   }
/*    */ 
/*    */   public String getOid()
/*    */   {
/* 65 */     return this.oid;
/*    */   }
/*    */ 
/*    */   public String getType()
/*    */   {
/* 73 */     return this.type;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.SnmpOidRecord
 * JD-Core Version:    0.6.2
 */