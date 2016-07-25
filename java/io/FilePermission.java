/*     */ package java.io;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import java.security.Permission;
/*     */ import java.security.PermissionCollection;
/*     */ import java.security.PrivilegedAction;
/*     */ 
/*     */ public final class FilePermission extends Permission
/*     */   implements Serializable
/*     */ {
/*     */   private static final int EXECUTE = 1;
/*     */   private static final int WRITE = 2;
/*     */   private static final int READ = 4;
/*     */   private static final int DELETE = 8;
/*     */   private static final int READLINK = 16;
/*     */   private static final int ALL = 31;
/*     */   private static final int NONE = 0;
/*     */   private transient int mask;
/*     */   private transient boolean directory;
/*     */   private transient boolean recursive;
/*     */   private String actions;
/*     */   private transient String cpath;
/*     */   private static final char RECURSIVE_CHAR = '-';
/*     */   private static final char WILD_CHAR = '*';
/*     */   private static final long serialVersionUID = 7930732926638008763L;
/*     */ 
/*     */   private void init(int paramInt)
/*     */   {
/* 190 */     if ((paramInt & 0x1F) != paramInt) {
/* 191 */       throw new IllegalArgumentException("invalid actions mask");
/*     */     }
/* 193 */     if (paramInt == 0) {
/* 194 */       throw new IllegalArgumentException("invalid actions mask");
/*     */     }
/* 196 */     if ((this.cpath = getName()) == null) {
/* 197 */       throw new NullPointerException("name can't be null");
/*     */     }
/* 199 */     this.mask = paramInt;
/*     */ 
/* 201 */     if (this.cpath.equals("<<ALL FILES>>")) {
/* 202 */       this.directory = true;
/* 203 */       this.recursive = true;
/* 204 */       this.cpath = "";
/* 205 */       return;
/*     */     }
/*     */ 
/* 209 */     this.cpath = ((String)AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public String run() {
/*     */         try {
/* 212 */           String str = FilePermission.this.cpath;
/* 213 */           if (FilePermission.this.cpath.endsWith("*"))
/*     */           {
/* 217 */             str = str.substring(0, str.length() - 1) + "-";
/* 218 */             str = new File(str).getCanonicalPath();
/* 219 */             return str.substring(0, str.length() - 1) + "*";
/*     */           }
/* 221 */           return new File(str).getCanonicalPath();
/*     */         } catch (IOException localIOException) {
/*     */         }
/* 224 */         return FilePermission.this.cpath;
/*     */       }
/*     */     }));
/* 229 */     int i = this.cpath.length();
/* 230 */     int j = i > 0 ? this.cpath.charAt(i - 1) : 0;
/*     */ 
/* 232 */     if ((j == 45) && (this.cpath.charAt(i - 2) == File.separatorChar))
/*     */     {
/* 234 */       this.directory = true;
/* 235 */       this.recursive = true;
/* 236 */       this.cpath = this.cpath.substring(0, --i);
/* 237 */     } else if ((j == 42) && (this.cpath.charAt(i - 2) == File.separatorChar))
/*     */     {
/* 239 */       this.directory = true;
/*     */ 
/* 241 */       this.cpath = this.cpath.substring(0, --i);
/*     */     }
/*     */   }
/*     */ 
/*     */   public FilePermission(String paramString1, String paramString2)
/*     */   {
/* 284 */     super(paramString1);
/* 285 */     init(getMask(paramString2));
/*     */   }
/*     */ 
/*     */   FilePermission(String paramString, int paramInt)
/*     */   {
/* 302 */     super(paramString);
/* 303 */     init(paramInt);
/*     */   }
/*     */ 
/*     */   public boolean implies(Permission paramPermission)
/*     */   {
/* 327 */     if (!(paramPermission instanceof FilePermission)) {
/* 328 */       return false;
/*     */     }
/* 330 */     FilePermission localFilePermission = (FilePermission)paramPermission;
/*     */ 
/* 335 */     return ((this.mask & localFilePermission.mask) == localFilePermission.mask) && (impliesIgnoreMask(localFilePermission));
/*     */   }
/*     */ 
/*     */   boolean impliesIgnoreMask(FilePermission paramFilePermission)
/*     */   {
/* 348 */     if (this.directory) {
/* 349 */       if (this.recursive)
/*     */       {
/* 352 */         if (paramFilePermission.directory) {
/* 353 */           return (paramFilePermission.cpath.length() >= this.cpath.length()) && (paramFilePermission.cpath.startsWith(this.cpath));
/*     */         }
/*     */ 
/* 356 */         return (paramFilePermission.cpath.length() > this.cpath.length()) && (paramFilePermission.cpath.startsWith(this.cpath));
/*     */       }
/*     */ 
/* 360 */       if (paramFilePermission.directory)
/*     */       {
/* 365 */         if (paramFilePermission.recursive) {
/* 366 */           return false;
/*     */         }
/* 368 */         return this.cpath.equals(paramFilePermission.cpath);
/*     */       }
/* 370 */       int i = paramFilePermission.cpath.lastIndexOf(File.separatorChar);
/* 371 */       if (i == -1) {
/* 372 */         return false;
/*     */       }
/*     */ 
/* 376 */       return (this.cpath.length() == i + 1) && (this.cpath.regionMatches(0, paramFilePermission.cpath, 0, i + 1));
/*     */     }
/*     */ 
/* 381 */     if (paramFilePermission.directory)
/*     */     {
/* 384 */       return false;
/*     */     }
/* 386 */     return this.cpath.equals(paramFilePermission.cpath);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 400 */     if (paramObject == this) {
/* 401 */       return true;
/*     */     }
/* 403 */     if (!(paramObject instanceof FilePermission)) {
/* 404 */       return false;
/*     */     }
/* 406 */     FilePermission localFilePermission = (FilePermission)paramObject;
/*     */ 
/* 408 */     return (this.mask == localFilePermission.mask) && (this.cpath.equals(localFilePermission.cpath)) && (this.directory == localFilePermission.directory) && (this.recursive == localFilePermission.recursive);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 421 */     return 0;
/*     */   }
/*     */ 
/*     */   private static int getMask(String paramString)
/*     */   {
/* 432 */     int i = 0;
/*     */ 
/* 435 */     if (paramString == null) {
/* 436 */       return i;
/*     */     }
/*     */ 
/* 439 */     if (paramString == "read")
/* 440 */       return 4;
/* 441 */     if (paramString == "write")
/* 442 */       return 2;
/* 443 */     if (paramString == "execute")
/* 444 */       return 1;
/* 445 */     if (paramString == "delete")
/* 446 */       return 8;
/* 447 */     if (paramString == "readlink") {
/* 448 */       return 16;
/*     */     }
/*     */ 
/* 451 */     char[] arrayOfChar = paramString.toCharArray();
/*     */ 
/* 453 */     int j = arrayOfChar.length - 1;
/* 454 */     if (j < 0) {
/* 455 */       return i;
/*     */     }
/* 457 */     while (j != -1)
/*     */     {
/*     */       int k;
/* 461 */       while ((j != -1) && (((k = arrayOfChar[j]) == ' ') || (k == 13) || (k == 10) || (k == 12) || (k == 9)))
/*     */       {
/* 466 */         j--;
/*     */       }
/*     */       int m;
/* 471 */       if ((j >= 3) && ((arrayOfChar[(j - 3)] == 'r') || (arrayOfChar[(j - 3)] == 'R')) && ((arrayOfChar[(j - 2)] == 'e') || (arrayOfChar[(j - 2)] == 'E')) && ((arrayOfChar[(j - 1)] == 'a') || (arrayOfChar[(j - 1)] == 'A')) && ((arrayOfChar[j] == 'd') || (arrayOfChar[j] == 'D')))
/*     */       {
/* 476 */         m = 4;
/* 477 */         i |= 4;
/*     */       }
/* 479 */       else if ((j >= 4) && ((arrayOfChar[(j - 4)] == 'w') || (arrayOfChar[(j - 4)] == 'W')) && ((arrayOfChar[(j - 3)] == 'r') || (arrayOfChar[(j - 3)] == 'R')) && ((arrayOfChar[(j - 2)] == 'i') || (arrayOfChar[(j - 2)] == 'I')) && ((arrayOfChar[(j - 1)] == 't') || (arrayOfChar[(j - 1)] == 'T')) && ((arrayOfChar[j] == 'e') || (arrayOfChar[j] == 'E')))
/*     */       {
/* 485 */         m = 5;
/* 486 */         i |= 2;
/*     */       }
/* 488 */       else if ((j >= 6) && ((arrayOfChar[(j - 6)] == 'e') || (arrayOfChar[(j - 6)] == 'E')) && ((arrayOfChar[(j - 5)] == 'x') || (arrayOfChar[(j - 5)] == 'X')) && ((arrayOfChar[(j - 4)] == 'e') || (arrayOfChar[(j - 4)] == 'E')) && ((arrayOfChar[(j - 3)] == 'c') || (arrayOfChar[(j - 3)] == 'C')) && ((arrayOfChar[(j - 2)] == 'u') || (arrayOfChar[(j - 2)] == 'U')) && ((arrayOfChar[(j - 1)] == 't') || (arrayOfChar[(j - 1)] == 'T')) && ((arrayOfChar[j] == 'e') || (arrayOfChar[j] == 'E')))
/*     */       {
/* 496 */         m = 7;
/* 497 */         i |= 1;
/*     */       }
/* 499 */       else if ((j >= 5) && ((arrayOfChar[(j - 5)] == 'd') || (arrayOfChar[(j - 5)] == 'D')) && ((arrayOfChar[(j - 4)] == 'e') || (arrayOfChar[(j - 4)] == 'E')) && ((arrayOfChar[(j - 3)] == 'l') || (arrayOfChar[(j - 3)] == 'L')) && ((arrayOfChar[(j - 2)] == 'e') || (arrayOfChar[(j - 2)] == 'E')) && ((arrayOfChar[(j - 1)] == 't') || (arrayOfChar[(j - 1)] == 'T')) && ((arrayOfChar[j] == 'e') || (arrayOfChar[j] == 'E')))
/*     */       {
/* 506 */         m = 6;
/* 507 */         i |= 8;
/*     */       }
/* 509 */       else if ((j >= 7) && ((arrayOfChar[(j - 7)] == 'r') || (arrayOfChar[(j - 7)] == 'R')) && ((arrayOfChar[(j - 6)] == 'e') || (arrayOfChar[(j - 6)] == 'E')) && ((arrayOfChar[(j - 5)] == 'a') || (arrayOfChar[(j - 5)] == 'A')) && ((arrayOfChar[(j - 4)] == 'd') || (arrayOfChar[(j - 4)] == 'D')) && ((arrayOfChar[(j - 3)] == 'l') || (arrayOfChar[(j - 3)] == 'L')) && ((arrayOfChar[(j - 2)] == 'i') || (arrayOfChar[(j - 2)] == 'I')) && ((arrayOfChar[(j - 1)] == 'n') || (arrayOfChar[(j - 1)] == 'N')) && ((arrayOfChar[j] == 'k') || (arrayOfChar[j] == 'K')))
/*     */       {
/* 518 */         m = 8;
/* 519 */         i |= 16;
/*     */       }
/*     */       else
/*     */       {
/* 523 */         throw new IllegalArgumentException("invalid permission: " + paramString);
/*     */       }
/*     */ 
/* 529 */       int n = 0;
/* 530 */       while ((j >= m) && (n == 0)) {
/* 531 */         switch (arrayOfChar[(j - m)]) {
/*     */         case ',':
/* 533 */           n = 1;
/*     */         case '\t':
/*     */         case '\n':
/*     */         case '\f':
/*     */         case '\r':
/*     */         case ' ':
/* 537 */           break;
/*     */         default:
/* 539 */           throw new IllegalArgumentException("invalid permission: " + paramString);
/*     */         }
/*     */ 
/* 542 */         j--;
/*     */       }
/*     */ 
/* 546 */       j -= m;
/*     */     }
/*     */ 
/* 549 */     return i;
/*     */   }
/*     */ 
/*     */   int getMask()
/*     */   {
/* 559 */     return this.mask;
/*     */   }
/*     */ 
/*     */   private static String getActions(int paramInt)
/*     */   {
/* 571 */     StringBuilder localStringBuilder = new StringBuilder();
/* 572 */     int i = 0;
/*     */ 
/* 574 */     if ((paramInt & 0x4) == 4) {
/* 575 */       i = 1;
/* 576 */       localStringBuilder.append("read");
/*     */     }
/*     */ 
/* 579 */     if ((paramInt & 0x2) == 2) {
/* 580 */       if (i != 0) localStringBuilder.append(','); else
/* 581 */         i = 1;
/* 582 */       localStringBuilder.append("write");
/*     */     }
/*     */ 
/* 585 */     if ((paramInt & 0x1) == 1) {
/* 586 */       if (i != 0) localStringBuilder.append(','); else
/* 587 */         i = 1;
/* 588 */       localStringBuilder.append("execute");
/*     */     }
/*     */ 
/* 591 */     if ((paramInt & 0x8) == 8) {
/* 592 */       if (i != 0) localStringBuilder.append(','); else
/* 593 */         i = 1;
/* 594 */       localStringBuilder.append("delete");
/*     */     }
/*     */ 
/* 597 */     if ((paramInt & 0x10) == 16) {
/* 598 */       if (i != 0) localStringBuilder.append(','); else
/* 599 */         i = 1;
/* 600 */       localStringBuilder.append("readlink");
/*     */     }
/*     */ 
/* 603 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public String getActions()
/*     */   {
/* 617 */     if (this.actions == null) {
/* 618 */       this.actions = getActions(this.mask);
/*     */     }
/* 620 */     return this.actions;
/*     */   }
/*     */ 
/*     */   public PermissionCollection newPermissionCollection()
/*     */   {
/* 657 */     return new FilePermissionCollection();
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 670 */     if (this.actions == null)
/* 671 */       getActions();
/* 672 */     paramObjectOutputStream.defaultWriteObject();
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 683 */     paramObjectInputStream.defaultReadObject();
/* 684 */     init(getMask(this.actions));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.FilePermission
 * JD-Core Version:    0.6.2
 */