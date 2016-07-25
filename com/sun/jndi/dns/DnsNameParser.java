/*    */ package com.sun.jndi.dns;
/*    */ 
/*    */ import javax.naming.Name;
/*    */ import javax.naming.NameParser;
/*    */ import javax.naming.NamingException;
/*    */ 
/*    */ class DnsNameParser
/*    */   implements NameParser
/*    */ {
/*    */   public Name parse(String paramString)
/*    */     throws NamingException
/*    */   {
/* 42 */     return new DnsName(paramString);
/*    */   }
/*    */ 
/*    */   public boolean equals(Object paramObject)
/*    */   {
/* 49 */     return paramObject instanceof DnsNameParser;
/*    */   }
/*    */ 
/*    */   public int hashCode() {
/* 53 */     return DnsNameParser.class.hashCode() + 1;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.dns.DnsNameParser
 * JD-Core Version:    0.6.2
 */