/*     */ package com.sun.rmi.rmid;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.security.Permission;
/*     */ import java.security.PermissionCollection;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ public final class ExecOptionPermission extends Permission
/*     */ {
/*     */   private transient boolean wildcard;
/*     */   private transient String name;
/*     */   private static final long serialVersionUID = 5842294756823092756L;
/*     */ 
/*     */   public ExecOptionPermission(String paramString)
/*     */   {
/*  59 */     super(paramString);
/*  60 */     init(paramString);
/*     */   }
/*     */ 
/*     */   public ExecOptionPermission(String paramString1, String paramString2) {
/*  64 */     this(paramString1);
/*     */   }
/*     */ 
/*     */   public boolean implies(Permission paramPermission)
/*     */   {
/*  86 */     if (!(paramPermission instanceof ExecOptionPermission)) {
/*  87 */       return false;
/*     */     }
/*  89 */     ExecOptionPermission localExecOptionPermission = (ExecOptionPermission)paramPermission;
/*     */ 
/*  91 */     if (this.wildcard) {
/*  92 */       if (localExecOptionPermission.wildcard)
/*     */       {
/*  94 */         return localExecOptionPermission.name.startsWith(this.name);
/*     */       }
/*     */ 
/*  97 */       return (localExecOptionPermission.name.length() > this.name.length()) && (localExecOptionPermission.name.startsWith(this.name));
/*     */     }
/*     */ 
/* 101 */     if (localExecOptionPermission.wildcard)
/*     */     {
/* 103 */       return false;
/*     */     }
/* 105 */     return this.name.equals(localExecOptionPermission.name);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 120 */     if (paramObject == this) {
/* 121 */       return true;
/*     */     }
/* 123 */     if ((paramObject == null) || (paramObject.getClass() != getClass())) {
/* 124 */       return false;
/*     */     }
/* 126 */     ExecOptionPermission localExecOptionPermission = (ExecOptionPermission)paramObject;
/*     */ 
/* 128 */     return getName().equals(localExecOptionPermission.getName());
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 141 */     return getName().hashCode();
/*     */   }
/*     */ 
/*     */   public String getActions()
/*     */   {
/* 150 */     return "";
/*     */   }
/*     */ 
/*     */   public PermissionCollection newPermissionCollection()
/*     */   {
/* 169 */     return new ExecOptionPermissionCollection();
/*     */   }
/*     */ 
/*     */   private synchronized void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 179 */     paramObjectInputStream.defaultReadObject();
/*     */ 
/* 181 */     init(getName());
/*     */   }
/*     */ 
/*     */   private void init(String paramString)
/*     */   {
/* 190 */     if (paramString == null) {
/* 191 */       throw new NullPointerException("name can't be null");
/*     */     }
/* 193 */     if (paramString.equals("")) {
/* 194 */       throw new IllegalArgumentException("name can't be empty");
/*     */     }
/*     */ 
/* 197 */     if ((paramString.endsWith(".*")) || (paramString.endsWith("=*")) || (paramString.equals("*"))) {
/* 198 */       this.wildcard = true;
/* 199 */       if (paramString.length() == 1)
/* 200 */         this.name = "";
/*     */       else
/* 202 */         this.name = paramString.substring(0, paramString.length() - 1);
/*     */     }
/*     */     else {
/* 205 */       this.name = paramString;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class ExecOptionPermissionCollection extends PermissionCollection
/*     */     implements Serializable
/*     */   {
/*     */     private Hashtable<String, Permission> permissions;
/*     */     private boolean all_allowed;
/*     */     private static final long serialVersionUID = -1242475729790124375L;
/*     */ 
/*     */     public ExecOptionPermissionCollection()
/*     */     {
/* 234 */       this.permissions = new Hashtable(11);
/* 235 */       this.all_allowed = false;
/*     */     }
/*     */ 
/*     */     public void add(Permission paramPermission)
/*     */     {
/* 253 */       if (!(paramPermission instanceof ExecOptionPermission)) {
/* 254 */         throw new IllegalArgumentException("invalid permission: " + paramPermission);
/*     */       }
/* 256 */       if (isReadOnly()) {
/* 257 */         throw new SecurityException("attempt to add a Permission to a readonly PermissionCollection");
/*     */       }
/* 259 */       ExecOptionPermission localExecOptionPermission = (ExecOptionPermission)paramPermission;
/*     */ 
/* 261 */       this.permissions.put(localExecOptionPermission.getName(), paramPermission);
/* 262 */       if ((!this.all_allowed) && 
/* 263 */         (localExecOptionPermission.getName().equals("*")))
/* 264 */         this.all_allowed = true;
/*     */     }
/*     */ 
/*     */     public boolean implies(Permission paramPermission)
/*     */     {
/* 279 */       if (!(paramPermission instanceof ExecOptionPermission)) {
/* 280 */         return false;
/*     */       }
/* 282 */       ExecOptionPermission localExecOptionPermission = (ExecOptionPermission)paramPermission;
/*     */ 
/* 285 */       if (this.all_allowed) {
/* 286 */         return true;
/*     */       }
/*     */ 
/* 292 */       String str = localExecOptionPermission.getName();
/*     */ 
/* 294 */       Permission localPermission = (Permission)this.permissions.get(str);
/*     */ 
/* 296 */       if (localPermission != null)
/*     */       {
/* 298 */         return localPermission.implies(paramPermission);
/*     */       }
/*     */ 
/* 304 */       int j = str.length() - 1;
/*     */       int i;
/* 306 */       while ((i = str.lastIndexOf(".", j)) != -1)
/*     */       {
/* 308 */         str = str.substring(0, i + 1) + "*";
/* 309 */         localPermission = (Permission)this.permissions.get(str);
/*     */ 
/* 311 */         if (localPermission != null) {
/* 312 */           return localPermission.implies(paramPermission);
/*     */         }
/* 314 */         j = i - 1;
/*     */       }
/*     */ 
/* 318 */       str = localExecOptionPermission.getName();
/* 319 */       j = str.length() - 1;
/*     */ 
/* 321 */       while ((i = str.lastIndexOf("=", j)) != -1)
/*     */       {
/* 323 */         str = str.substring(0, i + 1) + "*";
/* 324 */         localPermission = (Permission)this.permissions.get(str);
/*     */ 
/* 326 */         if (localPermission != null) {
/* 327 */           return localPermission.implies(paramPermission);
/*     */         }
/* 329 */         j = i - 1;
/*     */       }
/*     */ 
/* 334 */       return false;
/*     */     }
/*     */ 
/*     */     public Enumeration<Permission> elements()
/*     */     {
/* 346 */       return this.permissions.elements();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.rmi.rmid.ExecOptionPermission
 * JD-Core Version:    0.6.2
 */