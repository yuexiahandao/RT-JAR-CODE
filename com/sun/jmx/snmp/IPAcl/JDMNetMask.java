/*    */ package com.sun.jmx.snmp.IPAcl;
/*    */ 
/*    */ import java.net.UnknownHostException;
/*    */ 
/*    */ class JDMNetMask extends Host
/*    */ {
/*    */   private static final long serialVersionUID = -1979318280250821787L;
/* 33 */   protected StringBuffer address = new StringBuffer();
/* 34 */   protected String mask = null;
/*    */ 
/* 36 */   public JDMNetMask(int paramInt) { super(paramInt); }
/*    */ 
/*    */   public JDMNetMask(Parser paramParser, int paramInt)
/*    */   {
/* 40 */     super(paramParser, paramInt);
/*    */   }
/*    */   public static Node jjtCreate(int paramInt) {
/* 43 */     return new JDMNetMask(paramInt);
/*    */   }
/*    */ 
/*    */   public static Node jjtCreate(Parser paramParser, int paramInt) {
/* 47 */     return new JDMNetMask(paramParser, paramInt);
/*    */   }
/*    */ 
/*    */   protected String getHname() {
/* 51 */     return this.address.toString();
/*    */   }
/*    */ 
/*    */   protected PrincipalImpl createAssociatedPrincipal() throws UnknownHostException
/*    */   {
/* 56 */     return new NetMaskImpl(this.address.toString(), Integer.parseInt(this.mask));
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.IPAcl.JDMNetMask
 * JD-Core Version:    0.6.2
 */