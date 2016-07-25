/*     */ package sun.security.acl;
/*     */ 
/*     */ import java.security.Principal;
/*     */ import java.security.acl.AclEntry;
/*     */ import java.security.acl.Group;
/*     */ import java.security.acl.Permission;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class AclEntryImpl
/*     */   implements AclEntry
/*     */ {
/*  39 */   private Principal user = null;
/*  40 */   private Vector<Permission> permissionSet = new Vector(10, 10);
/*  41 */   private boolean negative = false;
/*     */ 
/*     */   public AclEntryImpl(Principal paramPrincipal)
/*     */   {
/*  49 */     this.user = paramPrincipal;
/*     */   }
/*     */ 
/*     */   public AclEntryImpl()
/*     */   {
/*     */   }
/*     */ 
/*     */   public boolean setPrincipal(Principal paramPrincipal)
/*     */   {
/*  67 */     if (this.user != null)
/*  68 */       return false;
/*  69 */     this.user = paramPrincipal;
/*  70 */     return true;
/*     */   }
/*     */ 
/*     */   public void setNegativePermissions()
/*     */   {
/*  79 */     this.negative = true;
/*     */   }
/*     */ 
/*     */   public boolean isNegative()
/*     */   {
/*  86 */     return this.negative;
/*     */   }
/*     */ 
/*     */   public boolean addPermission(Permission paramPermission)
/*     */   {
/*  99 */     if (this.permissionSet.contains(paramPermission)) {
/* 100 */       return false;
/*     */     }
/* 102 */     this.permissionSet.addElement(paramPermission);
/*     */ 
/* 104 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean removePermission(Permission paramPermission)
/*     */   {
/* 116 */     return this.permissionSet.removeElement(paramPermission);
/*     */   }
/*     */ 
/*     */   public boolean checkPermission(Permission paramPermission)
/*     */   {
/* 128 */     return this.permissionSet.contains(paramPermission);
/*     */   }
/*     */ 
/*     */   public Enumeration<Permission> permissions()
/*     */   {
/* 135 */     return this.permissionSet.elements();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 142 */     StringBuffer localStringBuffer = new StringBuffer();
/* 143 */     if (this.negative)
/* 144 */       localStringBuffer.append("-");
/*     */     else
/* 146 */       localStringBuffer.append("+");
/* 147 */     if ((this.user instanceof Group))
/* 148 */       localStringBuffer.append("Group.");
/*     */     else
/* 150 */       localStringBuffer.append("User.");
/* 151 */     localStringBuffer.append(this.user + "=");
/* 152 */     Enumeration localEnumeration = permissions();
/* 153 */     while (localEnumeration.hasMoreElements()) {
/* 154 */       Permission localPermission = (Permission)localEnumeration.nextElement();
/* 155 */       localStringBuffer.append(localPermission);
/* 156 */       if (localEnumeration.hasMoreElements())
/* 157 */         localStringBuffer.append(",");
/*     */     }
/* 159 */     return new String(localStringBuffer);
/*     */   }
/*     */ 
/*     */   public synchronized Object clone()
/*     */   {
/* 167 */     AclEntryImpl localAclEntryImpl = new AclEntryImpl(this.user);
/* 168 */     localAclEntryImpl.permissionSet = ((Vector)this.permissionSet.clone());
/* 169 */     localAclEntryImpl.negative = this.negative;
/* 170 */     return localAclEntryImpl;
/*     */   }
/*     */ 
/*     */   public Principal getPrincipal()
/*     */   {
/* 179 */     return this.user;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.acl.AclEntryImpl
 * JD-Core Version:    0.6.2
 */