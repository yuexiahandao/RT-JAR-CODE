/*    */ package com.sun.jmx.snmp.IPAcl;
/*    */ 
/*    */ import java.net.UnknownHostException;
/*    */ 
/*    */ class JDMIpMask extends Host
/*    */ {
/*    */   private static final long serialVersionUID = -8211312690652331386L;
/* 37 */   protected StringBuffer address = new StringBuffer();
/*    */ 
/*    */   JDMIpMask(int paramInt) {
/* 40 */     super(paramInt);
/*    */   }
/*    */ 
/*    */   JDMIpMask(Parser paramParser, int paramInt) {
/* 44 */     super(paramParser, paramInt);
/*    */   }
/*    */ 
/*    */   public static Node jjtCreate(int paramInt) {
/* 48 */     return new JDMIpMask(paramInt);
/*    */   }
/*    */ 
/*    */   public static Node jjtCreate(Parser paramParser, int paramInt) {
/* 52 */     return new JDMIpMask(paramParser, paramInt);
/*    */   }
/*    */ 
/*    */   protected String getHname() {
/* 56 */     return this.address.toString();
/*    */   }
/*    */ 
/*    */   protected PrincipalImpl createAssociatedPrincipal() throws UnknownHostException
/*    */   {
/* 61 */     return new GroupImpl(this.address.toString());
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.IPAcl.JDMIpMask
 * JD-Core Version:    0.6.2
 */