/*    */ package com.sun.jmx.snmp.IPAcl;
/*    */ 
/*    */ import java.net.UnknownHostException;
/*    */ 
/*    */ class JDMHostName extends Host
/*    */ {
/*    */   private static final long serialVersionUID = -9120082068923591122L;
/* 36 */   protected StringBuffer name = new StringBuffer();
/*    */ 
/*    */   JDMHostName(int paramInt) {
/* 39 */     super(paramInt);
/*    */   }
/*    */ 
/*    */   JDMHostName(Parser paramParser, int paramInt) {
/* 43 */     super(paramParser, paramInt);
/*    */   }
/*    */ 
/*    */   public static Node jjtCreate(int paramInt) {
/* 47 */     return new JDMHostName(paramInt);
/*    */   }
/*    */ 
/*    */   public static Node jjtCreate(Parser paramParser, int paramInt) {
/* 51 */     return new JDMHostName(paramParser, paramInt);
/*    */   }
/*    */ 
/*    */   protected String getHname() {
/* 55 */     return this.name.toString();
/*    */   }
/*    */ 
/*    */   protected PrincipalImpl createAssociatedPrincipal() throws UnknownHostException
/*    */   {
/* 60 */     return new PrincipalImpl(this.name.toString());
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.IPAcl.JDMHostName
 * JD-Core Version:    0.6.2
 */