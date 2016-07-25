/*    */ package com.sun.jmx.snmp;
/*    */ 
/*    */ public class SnmpSecurityException extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = 5574448147432833480L;
/* 39 */   public SnmpVarBind[] list = null;
/*    */ 
/* 43 */   public int status = 242;
/*    */ 
/* 47 */   public SnmpSecurityParameters params = null;
/*    */ 
/* 51 */   public byte[] contextEngineId = null;
/*    */ 
/* 55 */   public byte[] contextName = null;
/*    */ 
/* 59 */   public byte flags = 0;
/*    */ 
/*    */   public SnmpSecurityException(String paramString)
/*    */   {
/* 65 */     super(paramString);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.SnmpSecurityException
 * JD-Core Version:    0.6.2
 */