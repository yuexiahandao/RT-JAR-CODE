/*     */ package javax.security.auth.kerberos;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.security.BasicPermission;
/*     */ import java.security.Permission;
/*     */ import java.security.PermissionCollection;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ public final class DelegationPermission extends BasicPermission
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 883133252142523922L;
/*     */   private transient String subordinate;
/*     */   private transient String service;
/*     */ 
/*     */   public DelegationPermission(String paramString)
/*     */   {
/*  85 */     super(paramString);
/*  86 */     init(paramString);
/*     */   }
/*     */ 
/*     */   public DelegationPermission(String paramString1, String paramString2)
/*     */   {
/* 102 */     super(paramString1, paramString2);
/* 103 */     init(paramString1);
/*     */   }
/*     */ 
/*     */   private void init(String paramString)
/*     */   {
/* 112 */     StringTokenizer localStringTokenizer = null;
/* 113 */     if (!paramString.startsWith("\"")) {
/* 114 */       throw new IllegalArgumentException("service principal [" + paramString + "] syntax invalid: " + "improperly quoted");
/*     */     }
/*     */ 
/* 119 */     localStringTokenizer = new StringTokenizer(paramString, "\"", false);
/* 120 */     this.subordinate = localStringTokenizer.nextToken();
/* 121 */     if (localStringTokenizer.countTokens() == 2) {
/* 122 */       localStringTokenizer.nextToken();
/* 123 */       this.service = localStringTokenizer.nextToken();
/* 124 */     } else if (localStringTokenizer.countTokens() > 0) {
/* 125 */       throw new IllegalArgumentException("service principal [" + localStringTokenizer.nextToken() + "] syntax invalid: " + "improperly quoted");
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean implies(Permission paramPermission)
/*     */   {
/* 144 */     if (!(paramPermission instanceof DelegationPermission)) {
/* 145 */       return false;
/*     */     }
/* 147 */     DelegationPermission localDelegationPermission = (DelegationPermission)paramPermission;
/* 148 */     if ((this.subordinate.equals(localDelegationPermission.subordinate)) && (this.service.equals(localDelegationPermission.service)))
/*     */     {
/* 150 */       return true;
/*     */     }
/* 152 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 166 */     if (paramObject == this) {
/* 167 */       return true;
/*     */     }
/* 169 */     if (!(paramObject instanceof DelegationPermission)) {
/* 170 */       return false;
/*     */     }
/* 172 */     DelegationPermission localDelegationPermission = (DelegationPermission)paramObject;
/* 173 */     return implies(localDelegationPermission);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 183 */     return getName().hashCode();
/*     */   }
/*     */ 
/*     */   public PermissionCollection newPermissionCollection()
/*     */   {
/* 201 */     return new KrbDelegationPermissionCollection();
/*     */   }
/*     */ 
/*     */   private synchronized void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 212 */     paramObjectOutputStream.defaultWriteObject();
/*     */   }
/*     */ 
/*     */   private synchronized void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 223 */     paramObjectInputStream.defaultReadObject();
/* 224 */     init(getName());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.security.auth.kerberos.DelegationPermission
 * JD-Core Version:    0.6.2
 */