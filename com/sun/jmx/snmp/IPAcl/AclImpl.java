/*     */ package com.sun.jmx.snmp.IPAcl;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.security.Principal;
/*     */ import java.security.acl.Acl;
/*     */ import java.security.acl.AclEntry;
/*     */ import java.security.acl.NotOwnerException;
/*     */ import java.security.acl.Permission;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Vector;
/*     */ 
/*     */ class AclImpl extends OwnerImpl
/*     */   implements Acl, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -2250957591085270029L;
/*  58 */   private Vector<AclEntry> entryList = null;
/*  59 */   private String aclName = null;
/*     */ 
/*     */   public AclImpl(PrincipalImpl paramPrincipalImpl, String paramString)
/*     */   {
/*  68 */     super(paramPrincipalImpl);
/*  69 */     this.entryList = new Vector();
/*  70 */     this.aclName = paramString;
/*     */   }
/*     */ 
/*     */   public void setName(Principal paramPrincipal, String paramString)
/*     */     throws NotOwnerException
/*     */   {
/*  86 */     if (!isOwner(paramPrincipal))
/*  87 */       throw new NotOwnerException();
/*  88 */     this.aclName = paramString;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/*  97 */     return this.aclName;
/*     */   }
/*     */ 
/*     */   public boolean addEntry(Principal paramPrincipal, AclEntry paramAclEntry)
/*     */     throws NotOwnerException
/*     */   {
/* 118 */     if (!isOwner(paramPrincipal)) {
/* 119 */       throw new NotOwnerException();
/*     */     }
/* 121 */     if (this.entryList.contains(paramAclEntry)) {
/* 122 */       return false;
/*     */     }
/*     */ 
/* 131 */     this.entryList.addElement(paramAclEntry);
/* 132 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean removeEntry(Principal paramPrincipal, AclEntry paramAclEntry)
/*     */     throws NotOwnerException
/*     */   {
/* 149 */     if (!isOwner(paramPrincipal)) {
/* 150 */       throw new NotOwnerException();
/*     */     }
/* 152 */     return this.entryList.removeElement(paramAclEntry);
/*     */   }
/*     */ 
/*     */   public void removeAll(Principal paramPrincipal)
/*     */     throws NotOwnerException
/*     */   {
/* 166 */     if (!isOwner(paramPrincipal))
/* 167 */       throw new NotOwnerException();
/* 168 */     this.entryList.removeAllElements();
/*     */   }
/*     */ 
/*     */   public Enumeration<Permission> getPermissions(Principal paramPrincipal)
/*     */   {
/* 189 */     Vector localVector = new Vector();
/* 190 */     for (Enumeration localEnumeration = this.entryList.elements(); localEnumeration.hasMoreElements(); ) {
/* 191 */       AclEntry localAclEntry = (AclEntry)localEnumeration.nextElement();
/* 192 */       if (localAclEntry.getPrincipal().equals(paramPrincipal))
/* 193 */         return localAclEntry.permissions();
/*     */     }
/* 195 */     return localVector.elements();
/*     */   }
/*     */ 
/*     */   public Enumeration<AclEntry> entries()
/*     */   {
/* 205 */     return this.entryList.elements();
/*     */   }
/*     */ 
/*     */   public boolean checkPermission(Principal paramPrincipal, Permission paramPermission)
/*     */   {
/* 226 */     for (Enumeration localEnumeration = this.entryList.elements(); localEnumeration.hasMoreElements(); ) {
/* 227 */       AclEntry localAclEntry = (AclEntry)localEnumeration.nextElement();
/* 228 */       if ((localAclEntry.getPrincipal().equals(paramPrincipal)) && 
/* 229 */         (localAclEntry.checkPermission(paramPermission))) return true;
/*     */     }
/* 231 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean checkPermission(Principal paramPrincipal, String paramString, Permission paramPermission)
/*     */   {
/* 253 */     for (Enumeration localEnumeration = this.entryList.elements(); localEnumeration.hasMoreElements(); ) {
/* 254 */       AclEntryImpl localAclEntryImpl = (AclEntryImpl)localEnumeration.nextElement();
/* 255 */       if ((localAclEntryImpl.getPrincipal().equals(paramPrincipal)) && 
/* 256 */         (localAclEntryImpl.checkPermission(paramPermission)) && (localAclEntryImpl.checkCommunity(paramString))) return true;
/*     */     }
/* 258 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean checkCommunity(String paramString)
/*     */   {
/* 272 */     for (Enumeration localEnumeration = this.entryList.elements(); localEnumeration.hasMoreElements(); ) {
/* 273 */       AclEntryImpl localAclEntryImpl = (AclEntryImpl)localEnumeration.nextElement();
/* 274 */       if (localAclEntryImpl.checkCommunity(paramString)) return true;
/*     */     }
/* 276 */     return false;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 285 */     return "AclImpl: " + getName();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.IPAcl.AclImpl
 * JD-Core Version:    0.6.2
 */