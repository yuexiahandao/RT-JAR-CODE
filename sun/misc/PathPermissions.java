/*     */ package sun.misc;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FilePermission;
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.security.AccessController;
/*     */ import java.security.Permission;
/*     */ import java.security.PermissionCollection;
/*     */ import java.security.Permissions;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Enumeration;
/*     */ import java.util.PropertyPermission;
/*     */ import sun.security.util.SecurityConstants;
/*     */ 
/*     */ class PathPermissions extends PermissionCollection
/*     */ {
/*     */   private static final long serialVersionUID = 8133287259134945693L;
/*     */   private File[] path;
/*     */   private Permissions perms;
/*     */   URL codeBase;
/*     */ 
/*     */   PathPermissions(File[] paramArrayOfFile)
/*     */   {
/* 502 */     this.path = paramArrayOfFile;
/* 503 */     this.perms = null;
/* 504 */     this.codeBase = null;
/*     */   }
/*     */ 
/*     */   URL getCodeBase()
/*     */   {
/* 509 */     return this.codeBase;
/*     */   }
/*     */ 
/*     */   public void add(Permission paramPermission) {
/* 513 */     throw new SecurityException("attempt to add a permission");
/*     */   }
/*     */ 
/*     */   private synchronized void init()
/*     */   {
/* 518 */     if (this.perms != null) {
/* 519 */       return;
/*     */     }
/* 521 */     this.perms = new Permissions();
/*     */ 
/* 524 */     this.perms.add(SecurityConstants.CREATE_CLASSLOADER_PERMISSION);
/*     */ 
/* 527 */     this.perms.add(new PropertyPermission("java.*", "read"));
/*     */ 
/* 530 */     AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public Void run() {
/* 532 */         for (int i = 0; i < PathPermissions.this.path.length; i++) { File localFile = PathPermissions.this.path[i];
/*     */           String str;
/*     */           try {
/* 536 */             str = localFile.getCanonicalPath();
/*     */           } catch (IOException localIOException) {
/* 538 */             str = localFile.getAbsolutePath();
/*     */           }
/* 540 */           if (i == 0) {
/* 541 */             PathPermissions.this.codeBase = Launcher.getFileURL(new File(str));
/*     */           }
/* 543 */           if (localFile.isDirectory()) {
/* 544 */             if (str.endsWith(File.separator)) {
/* 545 */               PathPermissions.this.perms.add(new FilePermission(str + "-", "read"));
/*     */             }
/*     */             else {
/* 548 */               PathPermissions.this.perms.add(new FilePermission(str + File.separator + "-", "read"));
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/* 553 */             int j = str.lastIndexOf(File.separatorChar);
/* 554 */             if (j != -1) {
/* 555 */               str = str.substring(0, j + 1) + "-";
/* 556 */               PathPermissions.this.perms.add(new FilePermission(str, "read"));
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 563 */         return null;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public boolean implies(Permission paramPermission) {
/* 569 */     if (this.perms == null)
/* 570 */       init();
/* 571 */     return this.perms.implies(paramPermission);
/*     */   }
/*     */ 
/*     */   public Enumeration<Permission> elements() {
/* 575 */     if (this.perms == null)
/* 576 */       init();
/* 577 */     synchronized (this.perms) {
/* 578 */       return this.perms.elements();
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 583 */     if (this.perms == null)
/* 584 */       init();
/* 585 */     return this.perms.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.PathPermissions
 * JD-Core Version:    0.6.2
 */