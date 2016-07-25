/*    */ package com.sun.jndi.ldap;
/*    */ 
/*    */ import java.util.Vector;
/*    */ import javax.naming.directory.Attributes;
/*    */ 
/*    */ final class LdapEntry
/*    */ {
/*    */   String DN;
/*    */   Attributes attributes;
/* 41 */   Vector respCtls = null;
/*    */ 
/*    */   LdapEntry(String paramString, Attributes paramAttributes) {
/* 44 */     this.DN = paramString;
/* 45 */     this.attributes = paramAttributes;
/*    */   }
/*    */ 
/*    */   LdapEntry(String paramString, Attributes paramAttributes, Vector paramVector) {
/* 49 */     this.DN = paramString;
/* 50 */     this.attributes = paramAttributes;
/* 51 */     this.respCtls = paramVector;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.ldap.LdapEntry
 * JD-Core Version:    0.6.2
 */