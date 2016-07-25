/*     */ package java.security;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectInputStream.GetField;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.ObjectOutputStream.PutField;
/*     */ import java.io.ObjectStreamField;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Hashtable;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.Vector;
/*     */ 
/*     */ final class UnresolvedPermissionCollection extends PermissionCollection
/*     */   implements Serializable
/*     */ {
/*     */   private transient Map<String, List<UnresolvedPermission>> perms;
/*     */   private static final long serialVersionUID = -7176153071733132400L;
/* 147 */   private static final ObjectStreamField[] serialPersistentFields = { new ObjectStreamField("permissions", Hashtable.class) };
/*     */ 
/*     */   public UnresolvedPermissionCollection()
/*     */   {
/*  64 */     this.perms = new HashMap(11);
/*     */   }
/*     */ 
/*     */   public void add(Permission paramPermission)
/*     */   {
/*  76 */     if (!(paramPermission instanceof UnresolvedPermission)) {
/*  77 */       throw new IllegalArgumentException("invalid permission: " + paramPermission);
/*     */     }
/*  79 */     UnresolvedPermission localUnresolvedPermission = (UnresolvedPermission)paramPermission;
/*     */     Object localObject1;
/*  82 */     synchronized (this) {
/*  83 */       localObject1 = (List)this.perms.get(localUnresolvedPermission.getName());
/*  84 */       if (localObject1 == null) {
/*  85 */         localObject1 = new ArrayList();
/*  86 */         this.perms.put(localUnresolvedPermission.getName(), localObject1);
/*     */       }
/*     */     }
/*  89 */     synchronized (localObject1) {
/*  90 */       ((List)localObject1).add(localUnresolvedPermission);
/*     */     }
/*     */   }
/*     */ 
/*     */   List<UnresolvedPermission> getUnresolvedPermissions(Permission paramPermission)
/*     */   {
/*  99 */     synchronized (this) {
/* 100 */       return (List)this.perms.get(paramPermission.getClass().getName());
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean implies(Permission paramPermission)
/*     */   {
/* 110 */     return false;
/*     */   }
/*     */ 
/*     */   public Enumeration<Permission> elements()
/*     */   {
/* 121 */     ArrayList localArrayList = new ArrayList();
/*     */ 
/* 125 */     synchronized (this) {
/* 126 */       for (List localList : this.perms.values()) {
/* 127 */         synchronized (localList) {
/* 128 */           localArrayList.addAll(localList);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 133 */     return Collections.enumeration(localArrayList);
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 163 */     Hashtable localHashtable = new Hashtable(this.perms.size() * 2);
/*     */ 
/* 167 */     synchronized (this) {
/* 168 */       Set localSet = this.perms.entrySet();
/* 169 */       for (Map.Entry localEntry : localSet)
/*     */       {
/* 171 */         List localList = (List)localEntry.getValue();
/* 172 */         Vector localVector = new Vector(localList.size());
/* 173 */         synchronized (localList) {
/* 174 */           localVector.addAll(localList);
/*     */         }
/*     */ 
/* 178 */         localHashtable.put(localEntry.getKey(), localVector);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 183 */     ??? = paramObjectOutputStream.putFields();
/* 184 */     ((ObjectOutputStream.PutField)???).put("permissions", localHashtable);
/* 185 */     paramObjectOutputStream.writeFields();
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 197 */     ObjectInputStream.GetField localGetField = paramObjectInputStream.readFields();
/*     */ 
/* 200 */     Hashtable localHashtable = (Hashtable)localGetField.get("permissions", null);
/*     */ 
/* 202 */     this.perms = new HashMap(localHashtable.size() * 2);
/*     */ 
/* 205 */     Set localSet = localHashtable.entrySet();
/* 206 */     for (Map.Entry localEntry : localSet)
/*     */     {
/* 208 */       Vector localVector = (Vector)localEntry.getValue();
/* 209 */       ArrayList localArrayList = new ArrayList(localVector.size());
/* 210 */       localArrayList.addAll(localVector);
/*     */ 
/* 213 */       this.perms.put(localEntry.getKey(), localArrayList);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.UnresolvedPermissionCollection
 * JD-Core Version:    0.6.2
 */