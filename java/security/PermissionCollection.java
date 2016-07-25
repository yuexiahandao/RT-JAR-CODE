/*     */ package java.security;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Enumeration;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ public abstract class PermissionCollection
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -6727011328946861783L;
/*     */   private volatile boolean readOnly;
/*     */ 
/*     */   public abstract void add(Permission paramPermission);
/*     */ 
/*     */   public abstract boolean implies(Permission paramPermission);
/*     */ 
/*     */   public abstract Enumeration<Permission> elements();
/*     */ 
/*     */   public void setReadOnly()
/*     */   {
/* 140 */     this.readOnly = true;
/*     */   }
/*     */ 
/*     */   public boolean isReadOnly()
/*     */   {
/* 155 */     return this.readOnly;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 181 */     Enumeration localEnumeration = elements();
/* 182 */     StringBuilder localStringBuilder = new StringBuilder();
/* 183 */     localStringBuilder.append(super.toString() + " (\n");
/* 184 */     while (localEnumeration.hasMoreElements())
/*     */       try {
/* 186 */         localStringBuilder.append(" ");
/* 187 */         localStringBuilder.append(((Permission)localEnumeration.nextElement()).toString());
/* 188 */         localStringBuilder.append("\n");
/*     */       }
/*     */       catch (NoSuchElementException localNoSuchElementException)
/*     */       {
/*     */       }
/* 193 */     localStringBuilder.append(")\n");
/* 194 */     return localStringBuilder.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.PermissionCollection
 * JD-Core Version:    0.6.2
 */