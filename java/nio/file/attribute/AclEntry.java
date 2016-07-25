/*     */ package java.nio.file.attribute;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.EnumSet;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ 
/*     */ public final class AclEntry
/*     */ {
/*     */   private final AclEntryType type;
/*     */   private final UserPrincipal who;
/*     */   private final Set<AclEntryPermission> perms;
/*     */   private final Set<AclEntryFlag> flags;
/*     */   private volatile int hash;
/*     */ 
/*     */   private AclEntry(AclEntryType paramAclEntryType, UserPrincipal paramUserPrincipal, Set<AclEntryPermission> paramSet, Set<AclEntryFlag> paramSet1)
/*     */   {
/*  80 */     this.type = paramAclEntryType;
/*  81 */     this.who = paramUserPrincipal;
/*  82 */     this.perms = paramSet;
/*  83 */     this.flags = paramSet1;
/*     */   }
/*     */ 
/*     */   public static Builder newBuilder()
/*     */   {
/* 250 */     Set localSet1 = Collections.emptySet();
/* 251 */     Set localSet2 = Collections.emptySet();
/* 252 */     return new Builder(null, null, localSet1, localSet2, null);
/*     */   }
/*     */ 
/*     */   public static Builder newBuilder(AclEntry paramAclEntry)
/*     */   {
/* 264 */     return new Builder(paramAclEntry.type, paramAclEntry.who, paramAclEntry.perms, paramAclEntry.flags, null);
/*     */   }
/*     */ 
/*     */   public AclEntryType type()
/*     */   {
/* 271 */     return this.type;
/*     */   }
/*     */ 
/*     */   public UserPrincipal principal()
/*     */   {
/* 278 */     return this.who;
/*     */   }
/*     */ 
/*     */   public Set<AclEntryPermission> permissions()
/*     */   {
/* 287 */     return new HashSet(this.perms);
/*     */   }
/*     */ 
/*     */   public Set<AclEntryFlag> flags()
/*     */   {
/* 296 */     return new HashSet(this.flags);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 319 */     if (paramObject == this)
/* 320 */       return true;
/* 321 */     if ((paramObject == null) || (!(paramObject instanceof AclEntry)))
/* 322 */       return false;
/* 323 */     AclEntry localAclEntry = (AclEntry)paramObject;
/* 324 */     if (this.type != localAclEntry.type)
/* 325 */       return false;
/* 326 */     if (!this.who.equals(localAclEntry.who))
/* 327 */       return false;
/* 328 */     if (!this.perms.equals(localAclEntry.perms))
/* 329 */       return false;
/* 330 */     if (!this.flags.equals(localAclEntry.flags))
/* 331 */       return false;
/* 332 */     return true;
/*     */   }
/*     */ 
/*     */   private static int hash(int paramInt, Object paramObject) {
/* 336 */     return paramInt * 127 + paramObject.hashCode();
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 348 */     if (this.hash != 0)
/* 349 */       return this.hash;
/* 350 */     int i = this.type.hashCode();
/* 351 */     i = hash(i, this.who);
/* 352 */     i = hash(i, this.perms);
/* 353 */     i = hash(i, this.flags);
/* 354 */     this.hash = i;
/* 355 */     return this.hash;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 365 */     StringBuilder localStringBuilder = new StringBuilder();
/*     */ 
/* 368 */     localStringBuilder.append(this.who.getName());
/* 369 */     localStringBuilder.append(':');
/*     */ 
/* 372 */     for (Iterator localIterator = this.perms.iterator(); localIterator.hasNext(); ) { localObject = (AclEntryPermission)localIterator.next();
/* 373 */       localStringBuilder.append(((AclEntryPermission)localObject).name());
/* 374 */       localStringBuilder.append('/');
/*     */     }
/*     */     Object localObject;
/* 376 */     localStringBuilder.setLength(localStringBuilder.length() - 1);
/* 377 */     localStringBuilder.append(':');
/*     */ 
/* 380 */     if (!this.flags.isEmpty()) {
/* 381 */       for (localIterator = this.flags.iterator(); localIterator.hasNext(); ) { localObject = (AclEntryFlag)localIterator.next();
/* 382 */         localStringBuilder.append(((AclEntryFlag)localObject).name());
/* 383 */         localStringBuilder.append('/');
/*     */       }
/* 385 */       localStringBuilder.setLength(localStringBuilder.length() - 1);
/* 386 */       localStringBuilder.append(':');
/*     */     }
/*     */ 
/* 390 */     localStringBuilder.append(this.type.name());
/* 391 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public static final class Builder
/*     */   {
/*     */     private AclEntryType type;
/*     */     private UserPrincipal who;
/*     */     private Set<AclEntryPermission> perms;
/*     */     private Set<AclEntryFlag> flags;
/*     */ 
/*     */     private Builder(AclEntryType paramAclEntryType, UserPrincipal paramUserPrincipal, Set<AclEntryPermission> paramSet, Set<AclEntryFlag> paramSet1)
/*     */     {
/* 109 */       assert ((paramSet != null) && (paramSet1 != null));
/* 110 */       this.type = paramAclEntryType;
/* 111 */       this.who = paramUserPrincipal;
/* 112 */       this.perms = paramSet;
/* 113 */       this.flags = paramSet1;
/*     */     }
/*     */ 
/*     */     public AclEntry build()
/*     */     {
/* 127 */       if (this.type == null)
/* 128 */         throw new IllegalStateException("Missing type component");
/* 129 */       if (this.who == null)
/* 130 */         throw new IllegalStateException("Missing who component");
/* 131 */       return new AclEntry(this.type, this.who, this.perms, this.flags, null);
/*     */     }
/*     */ 
/*     */     public Builder setType(AclEntryType paramAclEntryType)
/*     */     {
/* 140 */       if (paramAclEntryType == null)
/* 141 */         throw new NullPointerException();
/* 142 */       this.type = paramAclEntryType;
/* 143 */       return this;
/*     */     }
/*     */ 
/*     */     public Builder setPrincipal(UserPrincipal paramUserPrincipal)
/*     */     {
/* 152 */       if (paramUserPrincipal == null)
/* 153 */         throw new NullPointerException();
/* 154 */       this.who = paramUserPrincipal;
/* 155 */       return this;
/*     */     }
/*     */ 
/*     */     private static void checkSet(Set<?> paramSet, Class<?> paramClass)
/*     */     {
/* 160 */       for (Iterator localIterator = paramSet.iterator(); localIterator.hasNext(); ) { Object localObject = localIterator.next();
/* 161 */         if (localObject == null)
/* 162 */           throw new NullPointerException();
/* 163 */         paramClass.cast(localObject);
/*     */       }
/*     */     }
/*     */ 
/*     */     public Builder setPermissions(Set<AclEntryPermission> paramSet)
/*     */     {
/* 179 */       paramSet = EnumSet.copyOf(paramSet);
/* 180 */       checkSet(paramSet, AclEntryPermission.class);
/* 181 */       this.perms = paramSet;
/* 182 */       return this;
/*     */     }
/*     */ 
/*     */     public Builder setPermissions(AclEntryPermission[] paramArrayOfAclEntryPermission)
/*     */     {
/* 193 */       EnumSet localEnumSet = EnumSet.noneOf(AclEntryPermission.class);
/*     */ 
/* 195 */       for (AclEntryPermission localAclEntryPermission : paramArrayOfAclEntryPermission) {
/* 196 */         if (localAclEntryPermission == null)
/* 197 */           throw new NullPointerException();
/* 198 */         localEnumSet.add(localAclEntryPermission);
/*     */       }
/* 200 */       this.perms = localEnumSet;
/* 201 */       return this;
/*     */     }
/*     */ 
/*     */     public Builder setFlags(Set<AclEntryFlag> paramSet)
/*     */     {
/* 216 */       paramSet = EnumSet.copyOf(paramSet);
/* 217 */       checkSet(paramSet, AclEntryFlag.class);
/* 218 */       this.flags = paramSet;
/* 219 */       return this;
/*     */     }
/*     */ 
/*     */     public Builder setFlags(AclEntryFlag[] paramArrayOfAclEntryFlag)
/*     */     {
/* 230 */       EnumSet localEnumSet = EnumSet.noneOf(AclEntryFlag.class);
/*     */ 
/* 232 */       for (AclEntryFlag localAclEntryFlag : paramArrayOfAclEntryFlag) {
/* 233 */         if (localAclEntryFlag == null)
/* 234 */           throw new NullPointerException();
/* 235 */         localEnumSet.add(localAclEntryFlag);
/*     */       }
/* 237 */       this.flags = localEnumSet;
/* 238 */       return this;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.file.attribute.AclEntry
 * JD-Core Version:    0.6.2
 */