/*     */ package com.sun.jmx.snmp.IPAcl;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.security.Principal;
/*     */ import java.security.acl.LastOwnerException;
/*     */ import java.security.acl.NotOwnerException;
/*     */ import java.security.acl.Owner;
/*     */ import java.util.Vector;
/*     */ 
/*     */ class OwnerImpl
/*     */   implements Owner, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -576066072046319874L;
/*  51 */   private Vector<Principal> ownerList = null;
/*     */ 
/*     */   public OwnerImpl()
/*     */   {
/*  57 */     this.ownerList = new Vector();
/*     */   }
/*     */ 
/*     */   public OwnerImpl(PrincipalImpl paramPrincipalImpl)
/*     */   {
/*  66 */     this.ownerList = new Vector();
/*  67 */     this.ownerList.addElement(paramPrincipalImpl);
/*     */   }
/*     */ 
/*     */   public boolean addOwner(Principal paramPrincipal1, Principal paramPrincipal2)
/*     */     throws NotOwnerException
/*     */   {
/*  85 */     if (!this.ownerList.contains(paramPrincipal1)) {
/*  86 */       throw new NotOwnerException();
/*     */     }
/*  88 */     if (this.ownerList.contains(paramPrincipal2)) {
/*  89 */       return false;
/*     */     }
/*  91 */     this.ownerList.addElement(paramPrincipal2);
/*  92 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean deleteOwner(Principal paramPrincipal1, Principal paramPrincipal2)
/*     */     throws NotOwnerException, LastOwnerException
/*     */   {
/* 113 */     if (!this.ownerList.contains(paramPrincipal1)) {
/* 114 */       throw new NotOwnerException();
/*     */     }
/* 116 */     if (!this.ownerList.contains(paramPrincipal2)) {
/* 117 */       return false;
/*     */     }
/* 119 */     if (this.ownerList.size() == 1) {
/* 120 */       throw new LastOwnerException();
/*     */     }
/* 122 */     this.ownerList.removeElement(paramPrincipal2);
/* 123 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean isOwner(Principal paramPrincipal)
/*     */   {
/* 135 */     return this.ownerList.contains(paramPrincipal);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.IPAcl.OwnerImpl
 * JD-Core Version:    0.6.2
 */