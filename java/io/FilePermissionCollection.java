/*     */ package java.io;
/*     */ 
/*     */ import java.security.Permission;
/*     */ import java.security.PermissionCollection;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.List;
/*     */ import java.util.Vector;
/*     */ 
/*     */ final class FilePermissionCollection extends PermissionCollection
/*     */   implements Serializable
/*     */ {
/*     */   private transient List<Permission> perms;
/*     */   private static final long serialVersionUID = 2202956749081564585L;
/* 818 */   private static final ObjectStreamField[] serialPersistentFields = { new ObjectStreamField("permissions", Vector.class) };
/*     */ 
/*     */   public FilePermissionCollection()
/*     */   {
/* 728 */     this.perms = new ArrayList();
/*     */   }
/*     */ 
/*     */   public void add(Permission paramPermission)
/*     */   {
/* 746 */     if (!(paramPermission instanceof FilePermission)) {
/* 747 */       throw new IllegalArgumentException("invalid permission: " + paramPermission);
/*     */     }
/* 749 */     if (isReadOnly()) {
/* 750 */       throw new SecurityException("attempt to add a Permission to a readonly PermissionCollection");
/*     */     }
/*     */ 
/* 753 */     synchronized (this) {
/* 754 */       this.perms.add(paramPermission);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean implies(Permission paramPermission)
/*     */   {
/* 770 */     if (!(paramPermission instanceof FilePermission)) {
/* 771 */       return false;
/*     */     }
/* 773 */     FilePermission localFilePermission1 = (FilePermission)paramPermission;
/*     */ 
/* 775 */     int i = localFilePermission1.getMask();
/* 776 */     int j = 0;
/* 777 */     int k = i;
/*     */ 
/* 779 */     synchronized (this) {
/* 780 */       int m = this.perms.size();
/* 781 */       for (int n = 0; n < m; n++) {
/* 782 */         FilePermission localFilePermission2 = (FilePermission)this.perms.get(n);
/* 783 */         if (((k & localFilePermission2.getMask()) != 0) && (localFilePermission2.impliesIgnoreMask(localFilePermission1))) {
/* 784 */           j |= localFilePermission2.getMask();
/* 785 */           if ((j & i) == i)
/* 786 */             return true;
/* 787 */           k = i ^ j;
/*     */         }
/*     */       }
/*     */     }
/* 791 */     return false;
/*     */   }
/*     */ 
/*     */   public Enumeration elements()
/*     */   {
/* 803 */     synchronized (this) {
/* 804 */       return Collections.enumeration(this.perms);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 833 */     Vector localVector = new Vector(this.perms.size());
/* 834 */     synchronized (this) {
/* 835 */       localVector.addAll(this.perms);
/*     */     }
/*     */ 
/* 838 */     ??? = paramObjectOutputStream.putFields();
/* 839 */     ((ObjectOutputStream.PutField)???).put("permissions", localVector);
/* 840 */     paramObjectOutputStream.writeFields();
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 852 */     ObjectInputStream.GetField localGetField = paramObjectInputStream.readFields();
/*     */ 
/* 855 */     Vector localVector = (Vector)localGetField.get("permissions", null);
/* 856 */     this.perms = new ArrayList(localVector.size());
/* 857 */     this.perms.addAll(localVector);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.FilePermissionCollection
 * JD-Core Version:    0.6.2
 */