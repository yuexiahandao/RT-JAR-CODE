/*     */ package sun.security.acl;
/*     */ 
/*     */ import java.security.Principal;
/*     */ import java.security.acl.Acl;
/*     */ import java.security.acl.AclEntry;
/*     */ import java.security.acl.Group;
/*     */ import java.security.acl.NotOwnerException;
/*     */ import java.security.acl.Permission;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class AclImpl extends OwnerImpl
/*     */   implements Acl
/*     */ {
/*  43 */   private Hashtable<Principal, AclEntry> allowedUsersTable = new Hashtable(23);
/*     */ 
/*  45 */   private Hashtable<Principal, AclEntry> allowedGroupsTable = new Hashtable(23);
/*     */ 
/*  47 */   private Hashtable<Principal, AclEntry> deniedUsersTable = new Hashtable(23);
/*     */ 
/*  49 */   private Hashtable<Principal, AclEntry> deniedGroupsTable = new Hashtable(23);
/*     */ 
/*  51 */   private String aclName = null;
/*  52 */   private Vector<Permission> zeroSet = new Vector(1, 1);
/*     */ 
/*     */   public AclImpl(Principal paramPrincipal, String paramString)
/*     */   {
/*  59 */     super(paramPrincipal);
/*     */     try {
/*  61 */       setName(paramPrincipal, paramString);
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setName(Principal paramPrincipal, String paramString)
/*     */     throws NotOwnerException
/*     */   {
/*  75 */     if (!isOwner(paramPrincipal)) {
/*  76 */       throw new NotOwnerException();
/*     */     }
/*  78 */     this.aclName = paramString;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/*  86 */     return this.aclName;
/*     */   }
/*     */ 
/*     */   public synchronized boolean addEntry(Principal paramPrincipal, AclEntry paramAclEntry)
/*     */     throws NotOwnerException
/*     */   {
/* 106 */     if (!isOwner(paramPrincipal)) {
/* 107 */       throw new NotOwnerException();
/*     */     }
/* 109 */     Hashtable localHashtable = findTable(paramAclEntry);
/* 110 */     Principal localPrincipal = paramAclEntry.getPrincipal();
/*     */ 
/* 112 */     if (localHashtable.get(localPrincipal) != null) {
/* 113 */       return false;
/*     */     }
/* 115 */     localHashtable.put(localPrincipal, paramAclEntry);
/* 116 */     return true;
/*     */   }
/*     */ 
/*     */   public synchronized boolean removeEntry(Principal paramPrincipal, AclEntry paramAclEntry)
/*     */     throws NotOwnerException
/*     */   {
/* 132 */     if (!isOwner(paramPrincipal)) {
/* 133 */       throw new NotOwnerException();
/*     */     }
/* 135 */     Hashtable localHashtable = findTable(paramAclEntry);
/* 136 */     Principal localPrincipal = paramAclEntry.getPrincipal();
/*     */ 
/* 138 */     AclEntry localAclEntry = (AclEntry)localHashtable.remove(localPrincipal);
/* 139 */     return localAclEntry != null;
/*     */   }
/*     */ 
/*     */   public synchronized Enumeration<Permission> getPermissions(Principal paramPrincipal)
/*     */   {
/* 184 */     Enumeration localEnumeration3 = subtract(getGroupPositive(paramPrincipal), getGroupNegative(paramPrincipal));
/*     */ 
/* 186 */     Enumeration localEnumeration4 = subtract(getGroupNegative(paramPrincipal), getGroupPositive(paramPrincipal));
/*     */ 
/* 188 */     Enumeration localEnumeration1 = subtract(getIndividualPositive(paramPrincipal), getIndividualNegative(paramPrincipal));
/*     */ 
/* 190 */     Enumeration localEnumeration2 = subtract(getIndividualNegative(paramPrincipal), getIndividualPositive(paramPrincipal));
/*     */ 
/* 197 */     Enumeration localEnumeration5 = subtract(localEnumeration3, localEnumeration2);
/*     */ 
/* 199 */     Enumeration localEnumeration6 = union(localEnumeration1, localEnumeration5);
/*     */ 
/* 205 */     localEnumeration1 = subtract(getIndividualPositive(paramPrincipal), getIndividualNegative(paramPrincipal));
/*     */ 
/* 207 */     localEnumeration2 = subtract(getIndividualNegative(paramPrincipal), getIndividualPositive(paramPrincipal));
/*     */ 
/* 214 */     localEnumeration5 = subtract(localEnumeration4, localEnumeration1);
/* 215 */     Enumeration localEnumeration7 = union(localEnumeration2, localEnumeration5);
/*     */ 
/* 217 */     return subtract(localEnumeration6, localEnumeration7);
/*     */   }
/*     */ 
/*     */   public boolean checkPermission(Principal paramPrincipal, Permission paramPermission)
/*     */   {
/* 233 */     Enumeration localEnumeration = getPermissions(paramPrincipal);
/* 234 */     while (localEnumeration.hasMoreElements()) {
/* 235 */       Permission localPermission = (Permission)localEnumeration.nextElement();
/* 236 */       if (localPermission.equals(paramPermission))
/* 237 */         return true;
/*     */     }
/* 239 */     return false;
/*     */   }
/*     */ 
/*     */   public synchronized Enumeration<AclEntry> entries()
/*     */   {
/* 246 */     return new AclEnumerator(this, this.allowedUsersTable, this.allowedGroupsTable, this.deniedUsersTable, this.deniedGroupsTable);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 256 */     StringBuffer localStringBuffer = new StringBuffer();
/* 257 */     Enumeration localEnumeration = entries();
/* 258 */     while (localEnumeration.hasMoreElements()) {
/* 259 */       AclEntry localAclEntry = (AclEntry)localEnumeration.nextElement();
/* 260 */       localStringBuffer.append(localAclEntry.toString().trim());
/* 261 */       localStringBuffer.append("\n");
/*     */     }
/*     */ 
/* 264 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   private Hashtable<Principal, AclEntry> findTable(AclEntry paramAclEntry)
/*     */   {
/* 275 */     Hashtable localHashtable = null;
/*     */ 
/* 277 */     Principal localPrincipal = paramAclEntry.getPrincipal();
/* 278 */     if ((localPrincipal instanceof Group)) {
/* 279 */       if (paramAclEntry.isNegative())
/* 280 */         localHashtable = this.deniedGroupsTable;
/*     */       else
/* 282 */         localHashtable = this.allowedGroupsTable;
/*     */     }
/* 284 */     else if (paramAclEntry.isNegative())
/* 285 */       localHashtable = this.deniedUsersTable;
/*     */     else {
/* 287 */       localHashtable = this.allowedUsersTable;
/*     */     }
/* 289 */     return localHashtable;
/*     */   }
/*     */ 
/*     */   private static Enumeration<Permission> union(Enumeration<Permission> paramEnumeration1, Enumeration<Permission> paramEnumeration2)
/*     */   {
/* 297 */     Vector localVector = new Vector(20, 20);
/*     */ 
/* 299 */     while (paramEnumeration1.hasMoreElements()) {
/* 300 */       localVector.addElement(paramEnumeration1.nextElement());
/*     */     }
/* 302 */     while (paramEnumeration2.hasMoreElements()) {
/* 303 */       Permission localPermission = (Permission)paramEnumeration2.nextElement();
/* 304 */       if (!localVector.contains(localPermission)) {
/* 305 */         localVector.addElement(localPermission);
/*     */       }
/*     */     }
/* 308 */     return localVector.elements();
/*     */   }
/*     */ 
/*     */   private Enumeration<Permission> subtract(Enumeration<Permission> paramEnumeration1, Enumeration<Permission> paramEnumeration2)
/*     */   {
/* 316 */     Vector localVector = new Vector(20, 20);
/*     */ 
/* 318 */     while (paramEnumeration1.hasMoreElements()) {
/* 319 */       localVector.addElement(paramEnumeration1.nextElement());
/*     */     }
/* 321 */     while (paramEnumeration2.hasMoreElements()) {
/* 322 */       Permission localPermission = (Permission)paramEnumeration2.nextElement();
/* 323 */       if (localVector.contains(localPermission)) {
/* 324 */         localVector.removeElement(localPermission);
/*     */       }
/*     */     }
/* 327 */     return localVector.elements();
/*     */   }
/*     */ 
/*     */   private Enumeration<Permission> getGroupPositive(Principal paramPrincipal) {
/* 331 */     Enumeration localEnumeration1 = this.zeroSet.elements();
/* 332 */     Enumeration localEnumeration2 = this.allowedGroupsTable.keys();
/* 333 */     while (localEnumeration2.hasMoreElements()) {
/* 334 */       Group localGroup = (Group)localEnumeration2.nextElement();
/* 335 */       if (localGroup.isMember(paramPrincipal)) {
/* 336 */         AclEntry localAclEntry = (AclEntry)this.allowedGroupsTable.get(localGroup);
/* 337 */         localEnumeration1 = union(localAclEntry.permissions(), localEnumeration1);
/*     */       }
/*     */     }
/* 340 */     return localEnumeration1;
/*     */   }
/*     */ 
/*     */   private Enumeration<Permission> getGroupNegative(Principal paramPrincipal) {
/* 344 */     Enumeration localEnumeration1 = this.zeroSet.elements();
/* 345 */     Enumeration localEnumeration2 = this.deniedGroupsTable.keys();
/* 346 */     while (localEnumeration2.hasMoreElements()) {
/* 347 */       Group localGroup = (Group)localEnumeration2.nextElement();
/* 348 */       if (localGroup.isMember(paramPrincipal)) {
/* 349 */         AclEntry localAclEntry = (AclEntry)this.deniedGroupsTable.get(localGroup);
/* 350 */         localEnumeration1 = union(localAclEntry.permissions(), localEnumeration1);
/*     */       }
/*     */     }
/* 353 */     return localEnumeration1;
/*     */   }
/*     */ 
/*     */   private Enumeration<Permission> getIndividualPositive(Principal paramPrincipal) {
/* 357 */     Enumeration localEnumeration = this.zeroSet.elements();
/* 358 */     AclEntry localAclEntry = (AclEntry)this.allowedUsersTable.get(paramPrincipal);
/* 359 */     if (localAclEntry != null)
/* 360 */       localEnumeration = localAclEntry.permissions();
/* 361 */     return localEnumeration;
/*     */   }
/*     */ 
/*     */   private Enumeration<Permission> getIndividualNegative(Principal paramPrincipal) {
/* 365 */     Enumeration localEnumeration = this.zeroSet.elements();
/* 366 */     AclEntry localAclEntry = (AclEntry)this.deniedUsersTable.get(paramPrincipal);
/* 367 */     if (localAclEntry != null)
/* 368 */       localEnumeration = localAclEntry.permissions();
/* 369 */     return localEnumeration;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.acl.AclImpl
 * JD-Core Version:    0.6.2
 */