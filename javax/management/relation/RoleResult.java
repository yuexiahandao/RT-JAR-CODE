/*     */ package javax.management.relation;
/*     */ 
/*     */ import com.sun.jmx.mbeanserver.GetPropertyAction;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectInputStream.GetField;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.ObjectOutputStream.PutField;
/*     */ import java.io.ObjectStreamField;
/*     */ import java.io.Serializable;
/*     */ import java.security.AccessController;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ public class RoleResult
/*     */   implements Serializable
/*     */ {
/*     */   private static final long oldSerialVersionUID = 3786616013762091099L;
/*     */   private static final long newSerialVersionUID = -6304063118040985512L;
/*  64 */   private static final ObjectStreamField[] oldSerialPersistentFields = { new ObjectStreamField("myRoleList", RoleList.class), new ObjectStreamField("myRoleUnresList", RoleUnresolvedList.class) };
/*     */ 
/*  71 */   private static final ObjectStreamField[] newSerialPersistentFields = { new ObjectStreamField("roleList", RoleList.class), new ObjectStreamField("unresolvedRoleList", RoleUnresolvedList.class) };
/*     */   private static final long serialVersionUID;
/*     */   private static final ObjectStreamField[] serialPersistentFields;
/*  84 */   private static boolean compat = false;
/*     */ 
/* 111 */   private RoleList roleList = null;
/*     */ 
/* 116 */   private RoleUnresolvedList unresolvedRoleList = null;
/*     */ 
/*     */   public RoleResult(RoleList paramRoleList, RoleUnresolvedList paramRoleUnresolvedList)
/*     */   {
/* 132 */     setRoles(paramRoleList);
/* 133 */     setRolesUnresolved(paramRoleUnresolvedList);
/*     */   }
/*     */ 
/*     */   public RoleList getRoles()
/*     */   {
/* 149 */     return this.roleList;
/*     */   }
/*     */ 
/*     */   public RoleUnresolvedList getRolesUnresolved()
/*     */   {
/* 160 */     return this.unresolvedRoleList;
/*     */   }
/*     */ 
/*     */   public void setRoles(RoleList paramRoleList)
/*     */   {
/* 171 */     if (paramRoleList != null)
/*     */     {
/* 173 */       this.roleList = new RoleList();
/*     */ 
/* 175 */       Iterator localIterator = paramRoleList.iterator();
/* 176 */       while (localIterator.hasNext()) {
/* 177 */         Role localRole = (Role)localIterator.next();
/* 178 */         this.roleList.add((Role)localRole.clone());
/*     */       }
/*     */     } else {
/* 181 */       this.roleList = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setRolesUnresolved(RoleUnresolvedList paramRoleUnresolvedList)
/*     */   {
/* 194 */     if (paramRoleUnresolvedList != null)
/*     */     {
/* 196 */       this.unresolvedRoleList = new RoleUnresolvedList();
/*     */ 
/* 198 */       Iterator localIterator = paramRoleUnresolvedList.iterator();
/* 199 */       while (localIterator.hasNext()) {
/* 200 */         RoleUnresolved localRoleUnresolved = (RoleUnresolved)localIterator.next();
/*     */ 
/* 202 */         this.unresolvedRoleList.add((RoleUnresolved)localRoleUnresolved.clone());
/*     */       }
/*     */     } else {
/* 205 */       this.unresolvedRoleList = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 215 */     if (compat)
/*     */     {
/* 219 */       ObjectInputStream.GetField localGetField = paramObjectInputStream.readFields();
/* 220 */       this.roleList = ((RoleList)localGetField.get("myRoleList", null));
/* 221 */       if (localGetField.defaulted("myRoleList"))
/*     */       {
/* 223 */         throw new NullPointerException("myRoleList");
/*     */       }
/* 225 */       this.unresolvedRoleList = ((RoleUnresolvedList)localGetField.get("myRoleUnresList", null));
/* 226 */       if (localGetField.defaulted("myRoleUnresList"))
/*     */       {
/* 228 */         throw new NullPointerException("myRoleUnresList");
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 235 */       paramObjectInputStream.defaultReadObject();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 245 */     if (compat)
/*     */     {
/* 249 */       ObjectOutputStream.PutField localPutField = paramObjectOutputStream.putFields();
/* 250 */       localPutField.put("myRoleList", this.roleList);
/* 251 */       localPutField.put("myRoleUnresList", this.unresolvedRoleList);
/* 252 */       paramObjectOutputStream.writeFields();
/*     */     }
/*     */     else
/*     */     {
/* 258 */       paramObjectOutputStream.defaultWriteObject();
/*     */     }
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  87 */       GetPropertyAction localGetPropertyAction = new GetPropertyAction("jmx.serial.form");
/*  88 */       String str = (String)AccessController.doPrivileged(localGetPropertyAction);
/*  89 */       compat = (str != null) && (str.equals("1.0"));
/*     */     }
/*     */     catch (Exception localException) {
/*     */     }
/*  93 */     if (compat) {
/*  94 */       serialPersistentFields = oldSerialPersistentFields;
/*  95 */       serialVersionUID = 3786616013762091099L;
/*     */     } else {
/*  97 */       serialPersistentFields = newSerialPersistentFields;
/*  98 */       serialVersionUID = -6304063118040985512L;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.relation.RoleResult
 * JD-Core Version:    0.6.2
 */