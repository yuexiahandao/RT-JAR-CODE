/*     */ package com.sun.jmx.snmp.IPAcl;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.net.UnknownHostException;
/*     */ import java.security.Principal;
/*     */ import java.security.acl.AclEntry;
/*     */ import java.security.acl.Permission;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Vector;
/*     */ 
/*     */ class AclEntryImpl
/*     */   implements AclEntry, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -5047185131260073216L;
/* 259 */   private Principal princ = null;
/* 260 */   private boolean neg = false;
/* 261 */   private Vector<Permission> permList = null;
/* 262 */   private Vector<String> commList = null;
/*     */ 
/*     */   private AclEntryImpl(AclEntryImpl paramAclEntryImpl)
/*     */     throws UnknownHostException
/*     */   {
/*  53 */     setPrincipal(paramAclEntryImpl.getPrincipal());
/*  54 */     this.permList = new Vector();
/*  55 */     this.commList = new Vector();
/*     */ 
/*  57 */     for (Enumeration localEnumeration = paramAclEntryImpl.communities(); localEnumeration.hasMoreElements(); ) {
/*  58 */       addCommunity((String)localEnumeration.nextElement());
/*     */     }
/*     */ 
/*  61 */     for (localEnumeration = paramAclEntryImpl.permissions(); localEnumeration.hasMoreElements(); ) {
/*  62 */       addPermission((Permission)localEnumeration.nextElement());
/*     */     }
/*  64 */     if (paramAclEntryImpl.isNegative()) setNegativePermissions();
/*     */   }
/*     */ 
/*     */   public AclEntryImpl()
/*     */   {
/*  71 */     this.princ = null;
/*  72 */     this.permList = new Vector();
/*  73 */     this.commList = new Vector();
/*     */   }
/*     */ 
/*     */   public AclEntryImpl(Principal paramPrincipal)
/*     */     throws UnknownHostException
/*     */   {
/*  82 */     this.princ = paramPrincipal;
/*  83 */     this.permList = new Vector();
/*  84 */     this.commList = new Vector();
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*     */     AclEntryImpl localAclEntryImpl;
/*     */     try
/*     */     {
/*  95 */       localAclEntryImpl = new AclEntryImpl(this);
/*     */     } catch (UnknownHostException localUnknownHostException) {
/*  97 */       localAclEntryImpl = null;
/*     */     }
/*  99 */     return localAclEntryImpl;
/*     */   }
/*     */ 
/*     */   public boolean isNegative()
/*     */   {
/* 109 */     return this.neg;
/*     */   }
/*     */ 
/*     */   public boolean addPermission(Permission paramPermission)
/*     */   {
/* 123 */     if (this.permList.contains(paramPermission)) return false;
/* 124 */     this.permList.addElement(paramPermission);
/* 125 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean removePermission(Permission paramPermission)
/*     */   {
/* 136 */     if (!this.permList.contains(paramPermission)) return false;
/* 137 */     this.permList.removeElement(paramPermission);
/* 138 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean checkPermission(Permission paramPermission)
/*     */   {
/* 151 */     return this.permList.contains(paramPermission);
/*     */   }
/*     */ 
/*     */   public Enumeration<Permission> permissions()
/*     */   {
/* 160 */     return this.permList.elements();
/*     */   }
/*     */ 
/*     */   public void setNegativePermissions()
/*     */   {
/* 172 */     this.neg = true;
/*     */   }
/*     */ 
/*     */   public Principal getPrincipal()
/*     */   {
/* 182 */     return this.princ;
/*     */   }
/*     */ 
/*     */   public boolean setPrincipal(Principal paramPrincipal)
/*     */   {
/* 195 */     if (this.princ != null)
/* 196 */       return false;
/* 197 */     this.princ = paramPrincipal;
/* 198 */     return true;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 207 */     return "AclEntry:" + this.princ.toString();
/*     */   }
/*     */ 
/*     */   public Enumeration<String> communities()
/*     */   {
/* 216 */     return this.commList.elements();
/*     */   }
/*     */ 
/*     */   public boolean addCommunity(String paramString)
/*     */   {
/* 229 */     if (this.commList.contains(paramString)) return false;
/* 230 */     this.commList.addElement(paramString);
/* 231 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean removeCommunity(String paramString)
/*     */   {
/* 242 */     if (!this.commList.contains(paramString)) return false;
/* 243 */     this.commList.removeElement(paramString);
/* 244 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean checkCommunity(String paramString)
/*     */   {
/* 256 */     return this.commList.contains(paramString);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.IPAcl.AclEntryImpl
 * JD-Core Version:    0.6.2
 */