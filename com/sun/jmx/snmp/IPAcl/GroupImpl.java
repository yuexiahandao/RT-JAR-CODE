/*     */ package com.sun.jmx.snmp.IPAcl;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.security.Principal;
/*     */ import java.security.acl.Group;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Vector;
/*     */ 
/*     */ class GroupImpl extends PrincipalImpl
/*     */   implements Group, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -7777387035032541168L;
/*     */ 
/*     */   public GroupImpl()
/*     */     throws UnknownHostException
/*     */   {
/*     */   }
/*     */ 
/*     */   public GroupImpl(String paramString)
/*     */     throws UnknownHostException
/*     */   {
/*  65 */     super(paramString);
/*     */   }
/*     */ 
/*     */   public boolean addMember(Principal paramPrincipal)
/*     */   {
/*  78 */     return true;
/*     */   }
/*     */ 
/*     */   public int hashCode() {
/*  82 */     return super.hashCode();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  94 */     if (((paramObject instanceof PrincipalImpl)) || ((paramObject instanceof GroupImpl))) {
/*  95 */       if ((super.hashCode() & paramObject.hashCode()) == paramObject.hashCode()) return true;
/*  96 */       return false;
/*     */     }
/*  98 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isMember(Principal paramPrincipal)
/*     */   {
/* 109 */     if ((paramPrincipal.hashCode() & super.hashCode()) == paramPrincipal.hashCode()) return true;
/* 110 */     return false;
/*     */   }
/*     */ 
/*     */   public Enumeration<? extends Principal> members()
/*     */   {
/* 119 */     Vector localVector = new Vector(1);
/* 120 */     localVector.addElement(this);
/* 121 */     return localVector.elements();
/*     */   }
/*     */ 
/*     */   public boolean removeMember(Principal paramPrincipal)
/*     */   {
/* 131 */     return true;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 140 */     return "GroupImpl :" + super.getAddress().toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.IPAcl.GroupImpl
 * JD-Core Version:    0.6.2
 */