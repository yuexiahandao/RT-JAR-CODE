/*     */ package com.sun.rmi.rmid;
/*     */ 
/*     */ import java.io.FilePermission;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.security.Permission;
/*     */ import java.security.PermissionCollection;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public final class ExecPermission extends Permission
/*     */ {
/*     */   private static final long serialVersionUID = -6208470287358147919L;
/*     */   private transient FilePermission fp;
/*     */ 
/*     */   public ExecPermission(String paramString)
/*     */   {
/*  86 */     super(paramString);
/*  87 */     init(paramString);
/*     */   }
/*     */ 
/*     */   public ExecPermission(String paramString1, String paramString2)
/*     */   {
/* 111 */     this(paramString1);
/*     */   }
/*     */ 
/*     */   public boolean implies(Permission paramPermission)
/*     */   {
/* 131 */     if (!(paramPermission instanceof ExecPermission)) {
/* 132 */       return false;
/*     */     }
/* 134 */     ExecPermission localExecPermission = (ExecPermission)paramPermission;
/*     */ 
/* 136 */     return this.fp.implies(localExecPermission.fp);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 149 */     if (paramObject == this) {
/* 150 */       return true;
/*     */     }
/* 152 */     if (!(paramObject instanceof ExecPermission)) {
/* 153 */       return false;
/*     */     }
/* 155 */     ExecPermission localExecPermission = (ExecPermission)paramObject;
/*     */ 
/* 157 */     return this.fp.equals(localExecPermission.fp);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 166 */     return this.fp.hashCode();
/*     */   }
/*     */ 
/*     */   public String getActions()
/*     */   {
/* 175 */     return "";
/*     */   }
/*     */ 
/*     */   public PermissionCollection newPermissionCollection()
/*     */   {
/* 194 */     return new ExecPermissionCollection();
/*     */   }
/*     */ 
/*     */   private synchronized void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 204 */     paramObjectInputStream.defaultReadObject();
/*     */ 
/* 206 */     init(getName());
/*     */   }
/*     */ 
/*     */   private void init(String paramString)
/*     */   {
/* 214 */     this.fp = new FilePermission(paramString, "execute");
/*     */   }
/*     */ 
/*     */   private static class ExecPermissionCollection extends PermissionCollection
/*     */     implements Serializable
/*     */   {
/*     */     private Vector<Permission> permissions;
/*     */     private static final long serialVersionUID = -3352558508888368273L;
/*     */ 
/*     */     public ExecPermissionCollection()
/*     */     {
/* 238 */       this.permissions = new Vector();
/*     */     }
/*     */ 
/*     */     public void add(Permission paramPermission)
/*     */     {
/* 254 */       if (!(paramPermission instanceof ExecPermission)) {
/* 255 */         throw new IllegalArgumentException("invalid permission: " + paramPermission);
/*     */       }
/* 257 */       if (isReadOnly()) {
/* 258 */         throw new SecurityException("attempt to add a Permission to a readonly PermissionCollection");
/*     */       }
/* 260 */       this.permissions.addElement(paramPermission);
/*     */     }
/*     */ 
/*     */     public boolean implies(Permission paramPermission)
/*     */     {
/* 274 */       if (!(paramPermission instanceof ExecPermission)) {
/* 275 */         return false;
/*     */       }
/* 277 */       Enumeration localEnumeration = this.permissions.elements();
/*     */ 
/* 279 */       while (localEnumeration.hasMoreElements()) {
/* 280 */         ExecPermission localExecPermission = (ExecPermission)localEnumeration.nextElement();
/* 281 */         if (localExecPermission.implies(paramPermission)) {
/* 282 */           return true;
/*     */         }
/*     */       }
/* 285 */       return false;
/*     */     }
/*     */ 
/*     */     public Enumeration<Permission> elements()
/*     */     {
/* 296 */       return this.permissions.elements();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.rmi.rmid.ExecPermission
 * JD-Core Version:    0.6.2
 */