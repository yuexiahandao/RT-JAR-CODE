/*      */ package com.sun.security.auth;
/*      */ 
/*      */ import java.security.CodeSource;
/*      */ import java.security.Permission;
/*      */ import java.security.PermissionCollection;
/*      */ import java.security.Permissions;
/*      */ import java.util.Enumeration;
/*      */ import java.util.ResourceBundle;
/*      */ import java.util.Vector;
/*      */ 
/*      */ class PolicyPermissions extends PermissionCollection
/*      */ {
/*      */   private static final long serialVersionUID = -1954188373270545523L;
/*      */   private CodeSource codesource;
/*      */   private Permissions perms;
/*      */   private PolicyFile policy;
/*      */   private boolean notInit;
/*      */   private Vector<Permission> additionalPerms;
/*      */ 
/*      */   PolicyPermissions(PolicyFile paramPolicyFile, CodeSource paramCodeSource)
/*      */   {
/* 1407 */     this.codesource = paramCodeSource;
/* 1408 */     this.policy = paramPolicyFile;
/* 1409 */     this.perms = null;
/* 1410 */     this.notInit = true;
/* 1411 */     this.additionalPerms = null;
/*      */   }
/*      */ 
/*      */   public void add(Permission paramPermission) {
/* 1415 */     if (isReadOnly()) {
/* 1416 */       throw new SecurityException(PolicyFile.rb.getString("attempt.to.add.a.Permission.to.a.readonly.PermissionCollection"));
/*      */     }
/*      */ 
/* 1420 */     if (this.perms == null) {
/* 1421 */       if (this.additionalPerms == null)
/* 1422 */         this.additionalPerms = new Vector();
/* 1423 */       this.additionalPerms.add(paramPermission);
/*      */     } else {
/* 1425 */       this.perms.add(paramPermission);
/*      */     }
/*      */   }
/*      */ 
/*      */   private synchronized void init() {
/* 1430 */     if (this.notInit) {
/* 1431 */       if (this.perms == null) {
/* 1432 */         this.perms = new Permissions();
/*      */       }
/* 1434 */       if (this.additionalPerms != null) {
/* 1435 */         Enumeration localEnumeration = this.additionalPerms.elements();
/* 1436 */         while (localEnumeration.hasMoreElements()) {
/* 1437 */           this.perms.add((Permission)localEnumeration.nextElement());
/*      */         }
/* 1439 */         this.additionalPerms = null;
/*      */       }
/* 1441 */       this.policy.getPermissions(this.perms, this.codesource);
/* 1442 */       this.notInit = false;
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean implies(Permission paramPermission) {
/* 1447 */     if (this.notInit)
/* 1448 */       init();
/* 1449 */     return this.perms.implies(paramPermission);
/*      */   }
/*      */ 
/*      */   public Enumeration<Permission> elements() {
/* 1453 */     if (this.notInit)
/* 1454 */       init();
/* 1455 */     return this.perms.elements();
/*      */   }
/*      */ 
/*      */   public String toString() {
/* 1459 */     if (this.notInit)
/* 1460 */       init();
/* 1461 */     return this.perms.toString();
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.auth.PolicyPermissions
 * JD-Core Version:    0.6.2
 */