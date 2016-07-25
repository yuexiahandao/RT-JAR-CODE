/*    */ package com.sun.jndi.ldap;
/*    */ 
/*    */ import javax.naming.Name;
/*    */ import javax.naming.NameParser;
/*    */ import javax.naming.NamingException;
/*    */ import javax.naming.ldap.LdapName;
/*    */ 
/*    */ class LdapNameParser
/*    */   implements NameParser
/*    */ {
/*    */   public Name parse(String paramString)
/*    */     throws NamingException
/*    */   {
/* 39 */     return new LdapName(paramString);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.ldap.LdapNameParser
 * JD-Core Version:    0.6.2
 */