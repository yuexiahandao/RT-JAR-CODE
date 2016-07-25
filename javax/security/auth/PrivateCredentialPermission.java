/*     */ package javax.security.auth;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Serializable;
/*     */ import java.security.Permission;
/*     */ import java.security.PermissionCollection;
/*     */ import java.security.Principal;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import java.util.StringTokenizer;
/*     */ import sun.security.util.ResourcesMgr;
/*     */ 
/*     */ public final class PrivateCredentialPermission extends Permission
/*     */ {
/*     */   private static final long serialVersionUID = 5284372143517237068L;
/* 108 */   private static final CredOwner[] EMPTY_PRINCIPALS = new CredOwner[0];
/*     */   private String credentialClass;
/*     */   private Set principals;
/*     */   private transient CredOwner[] credOwners;
/* 126 */   private boolean testing = false;
/*     */ 
/*     */   PrivateCredentialPermission(String paramString, Set<Principal> paramSet)
/*     */   {
/* 135 */     super(paramString);
/* 136 */     this.credentialClass = paramString;
/*     */ 
/* 138 */     synchronized (paramSet) {
/* 139 */       if (paramSet.size() == 0) {
/* 140 */         this.credOwners = EMPTY_PRINCIPALS;
/*     */       } else {
/* 142 */         this.credOwners = new CredOwner[paramSet.size()];
/* 143 */         int i = 0;
/* 144 */         Iterator localIterator = paramSet.iterator();
/* 145 */         while (localIterator.hasNext()) {
/* 146 */           Principal localPrincipal = (Principal)localIterator.next();
/* 147 */           this.credOwners[(i++)] = new CredOwner(localPrincipal.getClass().getName(), localPrincipal.getName());
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public PrivateCredentialPermission(String paramString1, String paramString2)
/*     */   {
/* 171 */     super(paramString1);
/*     */ 
/* 173 */     if (!"read".equalsIgnoreCase(paramString2)) {
/* 174 */       throw new IllegalArgumentException(ResourcesMgr.getString("actions.can.only.be.read."));
/*     */     }
/* 176 */     init(paramString1);
/*     */   }
/*     */ 
/*     */   public String getCredentialClass()
/*     */   {
/* 189 */     return this.credentialClass;
/*     */   }
/*     */ 
/*     */   public String[][] getPrincipals()
/*     */   {
/* 212 */     if ((this.credOwners == null) || (this.credOwners.length == 0)) {
/* 213 */       return new String[0][0];
/*     */     }
/*     */ 
/* 216 */     String[][] arrayOfString = new String[this.credOwners.length][2];
/* 217 */     for (int i = 0; i < this.credOwners.length; i++) {
/* 218 */       arrayOfString[i][0] = this.credOwners[i].principalClass;
/* 219 */       arrayOfString[i][1] = this.credOwners[i].principalName;
/*     */     }
/* 221 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   public boolean implies(Permission paramPermission)
/*     */   {
/* 251 */     if ((paramPermission == null) || (!(paramPermission instanceof PrivateCredentialPermission))) {
/* 252 */       return false;
/*     */     }
/* 254 */     PrivateCredentialPermission localPrivateCredentialPermission = (PrivateCredentialPermission)paramPermission;
/*     */ 
/* 256 */     if (!impliesCredentialClass(this.credentialClass, localPrivateCredentialPermission.credentialClass)) {
/* 257 */       return false;
/*     */     }
/* 259 */     return impliesPrincipalSet(this.credOwners, localPrivateCredentialPermission.credOwners);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 280 */     if (paramObject == this) {
/* 281 */       return true;
/*     */     }
/* 283 */     if (!(paramObject instanceof PrivateCredentialPermission)) {
/* 284 */       return false;
/*     */     }
/* 286 */     PrivateCredentialPermission localPrivateCredentialPermission = (PrivateCredentialPermission)paramObject;
/*     */ 
/* 288 */     return (implies(localPrivateCredentialPermission)) && (localPrivateCredentialPermission.implies(this));
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 297 */     return this.credentialClass.hashCode();
/*     */   }
/*     */ 
/*     */   public String getActions()
/*     */   {
/* 309 */     return "read";
/*     */   }
/*     */ 
/*     */   public PermissionCollection newPermissionCollection()
/*     */   {
/* 323 */     return null;
/*     */   }
/*     */ 
/*     */   private void init(String paramString)
/*     */   {
/* 328 */     if ((paramString == null) || (paramString.trim().length() == 0)) {
/* 329 */       throw new IllegalArgumentException("invalid empty name");
/*     */     }
/*     */ 
/* 332 */     ArrayList localArrayList = new ArrayList();
/* 333 */     StringTokenizer localStringTokenizer = new StringTokenizer(paramString, " ", true);
/* 334 */     String str1 = null;
/* 335 */     String str2 = null;
/*     */ 
/* 337 */     if (this.testing) {
/* 338 */       System.out.println("whole name = " + paramString);
/*     */     }
/*     */ 
/* 341 */     this.credentialClass = localStringTokenizer.nextToken();
/* 342 */     if (this.testing)
/* 343 */       System.out.println("Credential Class = " + this.credentialClass);
/*     */     MessageFormat localMessageFormat;
/*     */     Object[] arrayOfObject;
/* 345 */     if (!localStringTokenizer.hasMoreTokens()) {
/* 346 */       localMessageFormat = new MessageFormat(ResourcesMgr.getString("permission.name.name.syntax.invalid."));
/*     */ 
/* 348 */       arrayOfObject = new Object[] { paramString };
/* 349 */       throw new IllegalArgumentException(localMessageFormat.format(arrayOfObject) + ResourcesMgr.getString("Credential.Class.not.followed.by.a.Principal.Class.and.Name"));
/*     */     }
/*     */ 
/* 354 */     while (localStringTokenizer.hasMoreTokens())
/*     */     {
/* 357 */       localStringTokenizer.nextToken();
/*     */ 
/* 360 */       str1 = localStringTokenizer.nextToken();
/* 361 */       if (this.testing) {
/* 362 */         System.out.println("    Principal Class = " + str1);
/*     */       }
/* 364 */       if (!localStringTokenizer.hasMoreTokens()) {
/* 365 */         localMessageFormat = new MessageFormat(ResourcesMgr.getString("permission.name.name.syntax.invalid."));
/*     */ 
/* 367 */         arrayOfObject = new Object[] { paramString };
/* 368 */         throw new IllegalArgumentException(localMessageFormat.format(arrayOfObject) + ResourcesMgr.getString("Principal.Class.not.followed.by.a.Principal.Name"));
/*     */       }
/*     */ 
/* 374 */       localStringTokenizer.nextToken();
/*     */ 
/* 377 */       str2 = localStringTokenizer.nextToken();
/*     */ 
/* 379 */       if (!str2.startsWith("\"")) {
/* 380 */         localMessageFormat = new MessageFormat(ResourcesMgr.getString("permission.name.name.syntax.invalid."));
/*     */ 
/* 382 */         arrayOfObject = new Object[] { paramString };
/* 383 */         throw new IllegalArgumentException(localMessageFormat.format(arrayOfObject) + ResourcesMgr.getString("Principal.Name.must.be.surrounded.by.quotes"));
/*     */       }
/*     */ 
/* 388 */       if (!str2.endsWith("\""))
/*     */       {
/* 394 */         while (localStringTokenizer.hasMoreTokens()) {
/* 395 */           str2 = str2 + localStringTokenizer.nextToken();
/* 396 */           if (str2.endsWith("\"")) {
/* 397 */             break;
/*     */           }
/*     */         }
/* 400 */         if (!str2.endsWith("\"")) {
/* 401 */           localMessageFormat = new MessageFormat(ResourcesMgr.getString("permission.name.name.syntax.invalid."));
/*     */ 
/* 404 */           arrayOfObject = new Object[] { paramString };
/* 405 */           throw new IllegalArgumentException(localMessageFormat.format(arrayOfObject) + ResourcesMgr.getString("Principal.Name.missing.end.quote"));
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 411 */       if (this.testing) {
/* 412 */         System.out.println("\tprincipalName = '" + str2 + "'");
/*     */       }
/* 414 */       str2 = str2.substring(1, str2.length() - 1);
/*     */ 
/* 417 */       if ((str1.equals("*")) && (!str2.equals("*")))
/*     */       {
/* 419 */         throw new IllegalArgumentException(ResourcesMgr.getString("PrivateCredentialPermission.Principal.Class.can.not.be.a.wildcard.value.if.Principal.Name.is.not.a.wildcard.value"));
/*     */       }
/*     */ 
/* 423 */       if (this.testing) {
/* 424 */         System.out.println("\tprincipalName = '" + str2 + "'");
/*     */       }
/* 426 */       localArrayList.add(new CredOwner(str1, str2));
/*     */     }
/*     */ 
/* 429 */     this.credOwners = new CredOwner[localArrayList.size()];
/* 430 */     localArrayList.toArray(this.credOwners);
/*     */   }
/*     */ 
/*     */   private boolean impliesCredentialClass(String paramString1, String paramString2)
/*     */   {
/* 436 */     if ((paramString1 == null) || (paramString2 == null)) {
/* 437 */       return false;
/*     */     }
/* 439 */     if (this.testing) {
/* 440 */       System.out.println("credential class comparison: " + paramString1 + "/" + paramString2);
/*     */     }
/*     */ 
/* 443 */     if (paramString1.equals("*")) {
/* 444 */       return true;
/*     */     }
/*     */ 
/* 457 */     return paramString1.equals(paramString2);
/*     */   }
/*     */ 
/*     */   private boolean impliesPrincipalSet(CredOwner[] paramArrayOfCredOwner1, CredOwner[] paramArrayOfCredOwner2)
/*     */   {
/* 463 */     if ((paramArrayOfCredOwner1 == null) || (paramArrayOfCredOwner2 == null)) {
/* 464 */       return false;
/*     */     }
/* 466 */     if (paramArrayOfCredOwner2.length == 0) {
/* 467 */       return true;
/*     */     }
/* 469 */     if (paramArrayOfCredOwner1.length == 0) {
/* 470 */       return false;
/*     */     }
/* 472 */     for (int i = 0; i < paramArrayOfCredOwner1.length; i++) {
/* 473 */       int j = 0;
/* 474 */       for (int k = 0; k < paramArrayOfCredOwner2.length; k++) {
/* 475 */         if (paramArrayOfCredOwner1[i].implies(paramArrayOfCredOwner2[k])) {
/* 476 */           j = 1;
/* 477 */           break;
/*     */         }
/*     */       }
/* 480 */       if (j == 0) {
/* 481 */         return false;
/*     */       }
/*     */     }
/* 484 */     return true;
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 494 */     paramObjectInputStream.defaultReadObject();
/*     */ 
/* 498 */     if ((getName().indexOf(" ") == -1) && (getName().indexOf("\"") == -1))
/*     */     {
/* 501 */       this.credentialClass = getName();
/* 502 */       this.credOwners = EMPTY_PRINCIPALS;
/*     */     }
/*     */     else
/*     */     {
/* 507 */       init(getName());
/*     */     }
/*     */   }
/*     */ 
/*     */   static class CredOwner
/*     */     implements Serializable
/*     */   {
/*     */     private static final long serialVersionUID = -5607449830436408266L;
/*     */     String principalClass;
/*     */     String principalName;
/*     */ 
/*     */     CredOwner(String paramString1, String paramString2)
/*     */     {
/* 528 */       this.principalClass = paramString1;
/* 529 */       this.principalName = paramString2;
/*     */     }
/*     */ 
/*     */     public boolean implies(Object paramObject) {
/* 533 */       if ((paramObject == null) || (!(paramObject instanceof CredOwner))) {
/* 534 */         return false;
/*     */       }
/* 536 */       CredOwner localCredOwner = (CredOwner)paramObject;
/*     */ 
/* 538 */       if ((this.principalClass.equals("*")) || (this.principalClass.equals(localCredOwner.principalClass)))
/*     */       {
/* 541 */         if ((this.principalName.equals("*")) || (this.principalName.equals(localCredOwner.principalName)))
/*     */         {
/* 543 */           return true;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 551 */       return false;
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 555 */       MessageFormat localMessageFormat = new MessageFormat(ResourcesMgr.getString("CredOwner.Principal.Class.class.Principal.Name.name"));
/*     */ 
/* 557 */       Object[] arrayOfObject = { this.principalClass, this.principalName };
/* 558 */       return localMessageFormat.format(arrayOfObject);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.security.auth.PrivateCredentialPermission
 * JD-Core Version:    0.6.2
 */