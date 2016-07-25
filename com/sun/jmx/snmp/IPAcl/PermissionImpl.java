/*    */ package com.sun.jmx.snmp.IPAcl;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.security.acl.Permission;
/*    */ 
/*    */ class PermissionImpl
/*    */   implements Permission, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 4478110422746916589L;
/* 43 */   private String perm = null;
/*    */ 
/*    */   public PermissionImpl(String paramString)
/*    */   {
/* 51 */     this.perm = paramString;
/*    */   }
/*    */ 
/*    */   public int hashCode() {
/* 55 */     return super.hashCode();
/*    */   }
/*    */ 
/*    */   public boolean equals(Object paramObject)
/*    */   {
/* 65 */     if ((paramObject instanceof PermissionImpl)) {
/* 66 */       return this.perm.equals(((PermissionImpl)paramObject).getString());
/*    */     }
/* 68 */     return false;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 78 */     return this.perm;
/*    */   }
/*    */ 
/*    */   public String getString()
/*    */   {
/* 87 */     return this.perm;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.IPAcl.PermissionImpl
 * JD-Core Version:    0.6.2
 */