/*     */ package sun.nio.fs;
/*     */ 
/*     */ import com.sun.nio.file.ExtendedWatchEventModifier;
/*     */ import java.io.IOError;
/*     */ import java.io.IOException;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.net.URI;
/*     */ import java.nio.file.InvalidPathException;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.ProviderMismatchException;
/*     */ import java.nio.file.WatchEvent.Kind;
/*     */ import java.nio.file.WatchEvent.Modifier;
/*     */ import java.nio.file.WatchKey;
/*     */ import java.nio.file.WatchService;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Objects;
/*     */ 
/*     */ class WindowsPath extends AbstractPath
/*     */ {
/*     */   private static final int MAX_PATH = 247;
/*     */   private static final int MAX_LONG_PATH = 32000;
/*     */   private final WindowsFileSystem fs;
/*     */   private final WindowsPathType type;
/*     */   private final String root;
/*     */   private final String path;
/*     */   private volatile WeakReference<String> pathForWin32Calls;
/*     */   private volatile Integer[] offsets;
/*     */   private int hash;
/*     */ 
/*     */   private WindowsPath(WindowsFileSystem paramWindowsFileSystem, WindowsPathType paramWindowsPathType, String paramString1, String paramString2)
/*     */   {
/*  84 */     this.fs = paramWindowsFileSystem;
/*  85 */     this.type = paramWindowsPathType;
/*  86 */     this.root = paramString1;
/*  87 */     this.path = paramString2;
/*     */   }
/*     */ 
/*     */   static WindowsPath parse(WindowsFileSystem paramWindowsFileSystem, String paramString)
/*     */   {
/*  94 */     WindowsPathParser.Result localResult = WindowsPathParser.parse(paramString);
/*  95 */     return new WindowsPath(paramWindowsFileSystem, localResult.type(), localResult.root(), localResult.path());
/*     */   }
/*     */ 
/*     */   static WindowsPath createFromNormalizedPath(WindowsFileSystem paramWindowsFileSystem, String paramString, BasicFileAttributes paramBasicFileAttributes)
/*     */   {
/*     */     try
/*     */     {
/* 106 */       WindowsPathParser.Result localResult = WindowsPathParser.parseNormalizedPath(paramString);
/*     */ 
/* 108 */       if (paramBasicFileAttributes == null) {
/* 109 */         return new WindowsPath(paramWindowsFileSystem, localResult.type(), localResult.root(), localResult.path());
/*     */       }
/*     */ 
/* 114 */       return new WindowsPathWithAttributes(paramWindowsFileSystem, localResult.type(), localResult.root(), localResult.path(), paramBasicFileAttributes);
/*     */     }
/*     */     catch (InvalidPathException localInvalidPathException)
/*     */     {
/* 121 */       throw new AssertionError(localInvalidPathException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   static WindowsPath createFromNormalizedPath(WindowsFileSystem paramWindowsFileSystem, String paramString)
/*     */   {
/* 131 */     return createFromNormalizedPath(paramWindowsFileSystem, paramString, null);
/*     */   }
/*     */ 
/*     */   String getPathForExceptionMessage()
/*     */   {
/* 168 */     return this.path;
/*     */   }
/*     */ 
/*     */   String getPathForPermissionCheck()
/*     */   {
/* 173 */     return this.path;
/*     */   }
/*     */ 
/*     */   String getPathForWin32Calls()
/*     */     throws WindowsException
/*     */   {
/* 180 */     if ((isAbsolute()) && (this.path.length() <= 247)) {
/* 181 */       return this.path;
/*     */     }
/*     */ 
/* 184 */     WeakReference localWeakReference = this.pathForWin32Calls;
/* 185 */     String str = localWeakReference != null ? (String)localWeakReference.get() : null;
/* 186 */     if (str != null)
/*     */     {
/* 188 */       return str;
/*     */     }
/*     */ 
/* 192 */     str = getAbsolutePath();
/*     */ 
/* 199 */     if (str.length() > 247) {
/* 200 */       if (str.length() > 32000) {
/* 201 */         throw new WindowsException("Cannot access file with path exceeding 32000 characters");
/*     */       }
/*     */ 
/* 204 */       str = addPrefixIfNeeded(WindowsNativeDispatcher.GetFullPathName(str));
/*     */     }
/*     */ 
/* 210 */     if (this.type != WindowsPathType.DRIVE_RELATIVE) {
/* 211 */       synchronized (this.path) {
/* 212 */         this.pathForWin32Calls = new WeakReference(str);
/*     */       }
/*     */     }
/* 215 */     return str;
/*     */   }
/*     */ 
/*     */   private String getAbsolutePath() throws WindowsException
/*     */   {
/* 220 */     if (isAbsolute())
/* 221 */       return this.path;
/*     */     String str1;
/*     */     Object localObject;
/* 224 */     if (this.type == WindowsPathType.RELATIVE) {
/* 225 */       str1 = getFileSystem().defaultDirectory();
/* 226 */       if (isEmpty())
/* 227 */         return str1;
/* 228 */       if (str1.endsWith("\\")) {
/* 229 */         return str1 + this.path;
/*     */       }
/* 231 */       localObject = new StringBuilder(str1.length() + this.path.length() + 1);
/*     */ 
/* 233 */       return str1 + '\\' + this.path;
/*     */     }
/*     */ 
/* 238 */     if (this.type == WindowsPathType.DIRECTORY_RELATIVE) {
/* 239 */       str1 = getFileSystem().defaultRoot();
/* 240 */       return str1 + this.path.substring(1);
/*     */     }
/*     */ 
/* 244 */     if (isSameDrive(this.root, getFileSystem().defaultRoot()))
/*     */     {
/* 246 */       str1 = this.path.substring(this.root.length());
/* 247 */       localObject = getFileSystem().defaultDirectory();
/*     */       String str3;
/* 249 */       if (((String)localObject).endsWith("\\"))
/* 250 */         str3 = (String)localObject + str1;
/*     */       else {
/* 252 */         str3 = (String)localObject + "\\" + str1;
/*     */       }
/* 254 */       return str3;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 259 */       int i = WindowsNativeDispatcher.GetDriveType(this.root + "\\");
/* 260 */       if ((i == 0) || (i == 1))
/* 261 */         throw new WindowsException("");
/* 262 */       str1 = WindowsNativeDispatcher.GetFullPathName(this.root + ".");
/*     */     } catch (WindowsException localWindowsException) {
/* 264 */       throw new WindowsException("Unable to get working directory of drive '" + Character.toUpperCase(this.root.charAt(0)) + "'");
/*     */     }
/*     */ 
/* 267 */     String str2 = str1;
/* 268 */     if (str1.endsWith("\\")) {
/* 269 */       str2 = str2 + this.path.substring(this.root.length());
/*     */     }
/* 271 */     else if (this.path.length() > this.root.length()) {
/* 272 */       str2 = str2 + "\\" + this.path.substring(this.root.length());
/*     */     }
/* 274 */     return str2;
/*     */   }
/*     */ 
/*     */   private static boolean isSameDrive(String paramString1, String paramString2)
/*     */   {
/* 280 */     return Character.toUpperCase(paramString1.charAt(0)) == Character.toUpperCase(paramString2.charAt(0));
/*     */   }
/*     */ 
/*     */   static String addPrefixIfNeeded(String paramString)
/*     */   {
/* 286 */     if (paramString.length() > 247) {
/* 287 */       if (paramString.startsWith("\\\\"))
/* 288 */         paramString = "\\\\?\\UNC" + paramString.substring(1, paramString.length());
/*     */       else {
/* 290 */         paramString = "\\\\?\\" + paramString;
/*     */       }
/*     */     }
/* 293 */     return paramString;
/*     */   }
/*     */ 
/*     */   public WindowsFileSystem getFileSystem()
/*     */   {
/* 298 */     return this.fs;
/*     */   }
/*     */ 
/*     */   private boolean isEmpty()
/*     */   {
/* 304 */     return this.path.length() == 0;
/*     */   }
/*     */ 
/*     */   private WindowsPath emptyPath() {
/* 308 */     return new WindowsPath(getFileSystem(), WindowsPathType.RELATIVE, "", "");
/*     */   }
/*     */ 
/*     */   public Path getFileName()
/*     */   {
/* 313 */     int i = this.path.length();
/*     */ 
/* 315 */     if (i == 0) {
/* 316 */       return this;
/*     */     }
/* 318 */     if (this.root.length() == i)
/* 319 */       return null;
/* 320 */     int j = this.path.lastIndexOf('\\');
/* 321 */     if (j < this.root.length())
/* 322 */       j = this.root.length();
/*     */     else
/* 324 */       j++;
/* 325 */     return new WindowsPath(getFileSystem(), WindowsPathType.RELATIVE, "", this.path.substring(j));
/*     */   }
/*     */ 
/*     */   public WindowsPath getParent()
/*     */   {
/* 331 */     if (this.root.length() == this.path.length())
/* 332 */       return null;
/* 333 */     int i = this.path.lastIndexOf('\\');
/* 334 */     if (i < this.root.length()) {
/* 335 */       return getRoot();
/*     */     }
/* 337 */     return new WindowsPath(getFileSystem(), this.type, this.root, this.path.substring(0, i));
/*     */   }
/*     */ 
/*     */   public WindowsPath getRoot()
/*     */   {
/* 345 */     if (this.root.length() == 0)
/* 346 */       return null;
/* 347 */     return new WindowsPath(getFileSystem(), this.type, this.root, this.root);
/*     */   }
/*     */ 
/*     */   WindowsPathType type()
/*     */   {
/* 352 */     return this.type;
/*     */   }
/*     */ 
/*     */   boolean isUnc()
/*     */   {
/* 357 */     return this.type == WindowsPathType.UNC;
/*     */   }
/*     */ 
/*     */   boolean needsSlashWhenResolving() {
/* 361 */     if (this.path.endsWith("\\"))
/* 362 */       return false;
/* 363 */     return this.path.length() > this.root.length();
/*     */   }
/*     */ 
/*     */   public boolean isAbsolute()
/*     */   {
/* 368 */     return (this.type == WindowsPathType.ABSOLUTE) || (this.type == WindowsPathType.UNC);
/*     */   }
/*     */ 
/*     */   static WindowsPath toWindowsPath(Path paramPath) {
/* 372 */     if (paramPath == null)
/* 373 */       throw new NullPointerException();
/* 374 */     if (!(paramPath instanceof WindowsPath)) {
/* 375 */       throw new ProviderMismatchException();
/*     */     }
/* 377 */     return (WindowsPath)paramPath;
/*     */   }
/*     */ 
/*     */   public WindowsPath relativize(Path paramPath)
/*     */   {
/* 382 */     WindowsPath localWindowsPath = toWindowsPath(paramPath);
/* 383 */     if (equals(localWindowsPath)) {
/* 384 */       return emptyPath();
/*     */     }
/*     */ 
/* 387 */     if (this.type != localWindowsPath.type) {
/* 388 */       throw new IllegalArgumentException("'other' is different type of Path");
/*     */     }
/*     */ 
/* 391 */     if (!this.root.equalsIgnoreCase(localWindowsPath.root)) {
/* 392 */       throw new IllegalArgumentException("'other' has different root");
/*     */     }
/* 394 */     int i = getNameCount();
/* 395 */     int j = localWindowsPath.getNameCount();
/*     */ 
/* 398 */     int k = i > j ? j : i;
/* 399 */     int m = 0;
/* 400 */     while ((m < k) && 
/* 401 */       (getName(m).equals(localWindowsPath.getName(m))))
/*     */     {
/* 403 */       m++;
/*     */     }
/*     */ 
/* 407 */     StringBuilder localStringBuilder = new StringBuilder();
/* 408 */     for (int n = m; n < i; n++) {
/* 409 */       localStringBuilder.append("..\\");
/*     */     }
/*     */ 
/* 413 */     for (n = m; n < j; n++) {
/* 414 */       localStringBuilder.append(localWindowsPath.getName(n).toString());
/* 415 */       localStringBuilder.append("\\");
/*     */     }
/*     */ 
/* 419 */     localStringBuilder.setLength(localStringBuilder.length() - 1);
/* 420 */     return createFromNormalizedPath(getFileSystem(), localStringBuilder.toString());
/*     */   }
/*     */ 
/*     */   public Path normalize()
/*     */   {
/* 425 */     int i = getNameCount();
/* 426 */     if ((i == 0) || (isEmpty())) {
/* 427 */       return this;
/*     */     }
/* 429 */     boolean[] arrayOfBoolean = new boolean[i];
/* 430 */     int j = i;
/*     */     int k;
/*     */     do {
/* 435 */       k = j;
/* 436 */       int m = -1;
/* 437 */       for (n = 0; n < i; n++)
/* 438 */         if (arrayOfBoolean[n] == 0)
/*     */         {
/* 441 */           String str = elementAsString(n);
/*     */ 
/* 444 */           if (str.length() > 2) {
/* 445 */             m = n;
/*     */           }
/* 450 */           else if (str.length() == 1)
/*     */           {
/* 452 */             if (str.charAt(0) == '.') {
/* 453 */               arrayOfBoolean[n] = true;
/* 454 */               j--;
/*     */             } else {
/* 456 */               m = n;
/*     */             }
/*     */ 
/*     */           }
/* 462 */           else if ((str.charAt(0) != '.') || (str.charAt(1) != '.')) {
/* 463 */             m = n;
/*     */           }
/* 468 */           else if (m >= 0)
/*     */           {
/* 471 */             arrayOfBoolean[m] = true;
/* 472 */             arrayOfBoolean[n] = true;
/* 473 */             j -= 2;
/* 474 */             m = -1;
/*     */           }
/* 480 */           else if ((isAbsolute()) || (this.type == WindowsPathType.DIRECTORY_RELATIVE)) {
/* 481 */             int i1 = 0;
/* 482 */             for (int i2 = 0; i2 < n; i2++) {
/* 483 */               if (arrayOfBoolean[i2] == 0) {
/* 484 */                 i1 = 1;
/* 485 */                 break;
/*     */               }
/*     */             }
/* 488 */             if (i1 == 0)
/*     */             {
/* 490 */               arrayOfBoolean[n] = true;
/* 491 */               j--;
/*     */             }
/*     */           }
/*     */         }
/*     */     }
/* 496 */     while (k > j);
/*     */ 
/* 499 */     if (j == i) {
/* 500 */       return this;
/*     */     }
/*     */ 
/* 503 */     if (j == 0) {
/* 504 */       return this.root.length() == 0 ? emptyPath() : getRoot();
/*     */     }
/*     */ 
/* 508 */     StringBuilder localStringBuilder = new StringBuilder();
/* 509 */     if (this.root != null)
/* 510 */       localStringBuilder.append(this.root);
/* 511 */     for (int n = 0; n < i; n++) {
/* 512 */       if (arrayOfBoolean[n] == 0) {
/* 513 */         localStringBuilder.append(getName(n));
/* 514 */         localStringBuilder.append("\\");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 519 */     localStringBuilder.setLength(localStringBuilder.length() - 1);
/* 520 */     return createFromNormalizedPath(getFileSystem(), localStringBuilder.toString());
/*     */   }
/*     */ 
/*     */   public WindowsPath resolve(Path paramPath)
/*     */   {
/* 525 */     WindowsPath localWindowsPath = toWindowsPath(paramPath);
/* 526 */     if (localWindowsPath.isEmpty())
/* 527 */       return this;
/* 528 */     if (localWindowsPath.isAbsolute())
/* 529 */       return localWindowsPath;
/*     */     String str1;
/* 531 */     switch (1.$SwitchMap$sun$nio$fs$WindowsPathType[localWindowsPath.type.ordinal()])
/*     */     {
/*     */     case 1:
/* 534 */       if ((this.path.endsWith("\\")) || (this.root.length() == this.path.length()))
/* 535 */         str1 = this.path + localWindowsPath.path;
/*     */       else {
/* 537 */         str1 = this.path + "\\" + localWindowsPath.path;
/*     */       }
/* 539 */       return new WindowsPath(getFileSystem(), this.type, this.root, str1);
/*     */     case 2:
/* 544 */       if (this.root.endsWith("\\"))
/* 545 */         str1 = this.root + localWindowsPath.path.substring(1);
/*     */       else {
/* 547 */         str1 = this.root + localWindowsPath.path;
/*     */       }
/* 549 */       return createFromNormalizedPath(getFileSystem(), str1);
/*     */     case 3:
/* 553 */       if (!this.root.endsWith("\\")) {
/* 554 */         return localWindowsPath;
/*     */       }
/* 556 */       str1 = this.root.substring(0, this.root.length() - 1);
/* 557 */       if (!str1.equalsIgnoreCase(localWindowsPath.root)) {
/* 558 */         return localWindowsPath;
/*     */       }
/* 560 */       String str2 = localWindowsPath.path.substring(localWindowsPath.root.length());
/*     */       String str3;
/* 562 */       if (this.path.endsWith("\\"))
/* 563 */         str3 = this.path + str2;
/*     */       else {
/* 565 */         str3 = this.path + "\\" + str2;
/*     */       }
/* 567 */       return createFromNormalizedPath(getFileSystem(), str3);
/*     */     }
/*     */ 
/* 571 */     throw new AssertionError();
/*     */   }
/*     */ 
/*     */   private void initOffsets()
/*     */   {
/* 577 */     if (this.offsets == null) {
/* 578 */       ArrayList localArrayList = new ArrayList();
/* 579 */       if (isEmpty())
/*     */       {
/* 581 */         localArrayList.add(Integer.valueOf(0));
/*     */       } else {
/* 583 */         int i = this.root.length();
/* 584 */         int j = this.root.length();
/* 585 */         while (j < this.path.length()) {
/* 586 */           if (this.path.charAt(j) != '\\') {
/* 587 */             j++;
/*     */           } else {
/* 589 */             localArrayList.add(Integer.valueOf(i));
/* 590 */             j++; i = j;
/*     */           }
/*     */         }
/* 593 */         if (i != j)
/* 594 */           localArrayList.add(Integer.valueOf(i));
/*     */       }
/* 596 */       synchronized (this) {
/* 597 */         if (this.offsets == null)
/* 598 */           this.offsets = ((Integer[])localArrayList.toArray(new Integer[localArrayList.size()]));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getNameCount()
/*     */   {
/* 605 */     initOffsets();
/* 606 */     return this.offsets.length;
/*     */   }
/*     */ 
/*     */   private String elementAsString(int paramInt) {
/* 610 */     initOffsets();
/* 611 */     if (paramInt == this.offsets.length - 1)
/* 612 */       return this.path.substring(this.offsets[paramInt].intValue());
/* 613 */     return this.path.substring(this.offsets[paramInt].intValue(), this.offsets[(paramInt + 1)].intValue() - 1);
/*     */   }
/*     */ 
/*     */   public WindowsPath getName(int paramInt)
/*     */   {
/* 618 */     initOffsets();
/* 619 */     if ((paramInt < 0) || (paramInt >= this.offsets.length))
/* 620 */       throw new IllegalArgumentException();
/* 621 */     return new WindowsPath(getFileSystem(), WindowsPathType.RELATIVE, "", elementAsString(paramInt));
/*     */   }
/*     */ 
/*     */   public WindowsPath subpath(int paramInt1, int paramInt2)
/*     */   {
/* 626 */     initOffsets();
/* 627 */     if (paramInt1 < 0)
/* 628 */       throw new IllegalArgumentException();
/* 629 */     if (paramInt1 >= this.offsets.length)
/* 630 */       throw new IllegalArgumentException();
/* 631 */     if (paramInt2 > this.offsets.length)
/* 632 */       throw new IllegalArgumentException();
/* 633 */     if (paramInt1 >= paramInt2) {
/* 634 */       throw new IllegalArgumentException();
/*     */     }
/* 636 */     StringBuilder localStringBuilder = new StringBuilder();
/* 637 */     Integer[] arrayOfInteger = new Integer[paramInt2 - paramInt1];
/* 638 */     for (int i = paramInt1; i < paramInt2; i++) {
/* 639 */       arrayOfInteger[(i - paramInt1)] = Integer.valueOf(localStringBuilder.length());
/* 640 */       localStringBuilder.append(elementAsString(i));
/* 641 */       if (i != paramInt2 - 1)
/* 642 */         localStringBuilder.append("\\");
/*     */     }
/* 644 */     return new WindowsPath(getFileSystem(), WindowsPathType.RELATIVE, "", localStringBuilder.toString());
/*     */   }
/*     */ 
/*     */   public boolean startsWith(Path paramPath)
/*     */   {
/* 649 */     if (!(Objects.requireNonNull(paramPath) instanceof WindowsPath))
/* 650 */       return false;
/* 651 */     WindowsPath localWindowsPath = (WindowsPath)paramPath;
/*     */ 
/* 654 */     if (!this.root.equalsIgnoreCase(localWindowsPath.root)) {
/* 655 */       return false;
/*     */     }
/*     */ 
/* 659 */     if (localWindowsPath.isEmpty()) {
/* 660 */       return isEmpty();
/*     */     }
/*     */ 
/* 663 */     int i = getNameCount();
/* 664 */     int j = localWindowsPath.getNameCount();
/* 665 */     if (j <= i) {
/*     */       while (true) { j--; if (j < 0) break;
/* 667 */         String str1 = elementAsString(j);
/* 668 */         String str2 = localWindowsPath.elementAsString(j);
/*     */ 
/* 670 */         if (!str1.equalsIgnoreCase(str2))
/* 671 */           return false;
/*     */       }
/* 673 */       return true;
/*     */     }
/* 675 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean endsWith(Path paramPath)
/*     */   {
/* 680 */     if (!(Objects.requireNonNull(paramPath) instanceof WindowsPath))
/* 681 */       return false;
/* 682 */     WindowsPath localWindowsPath = (WindowsPath)paramPath;
/*     */ 
/* 685 */     if (localWindowsPath.path.length() > this.path.length()) {
/* 686 */       return false;
/*     */     }
/*     */ 
/* 690 */     if (localWindowsPath.isEmpty()) {
/* 691 */       return isEmpty();
/*     */     }
/*     */ 
/* 694 */     int i = getNameCount();
/* 695 */     int j = localWindowsPath.getNameCount();
/*     */ 
/* 698 */     if (j > i) {
/* 699 */       return false;
/*     */     }
/*     */ 
/* 703 */     if (localWindowsPath.root.length() > 0) {
/* 704 */       if (j < i) {
/* 705 */         return false;
/*     */       }
/* 707 */       if (!this.root.equalsIgnoreCase(localWindowsPath.root)) {
/* 708 */         return false;
/*     */       }
/*     */     }
/*     */ 
/* 712 */     int k = i - j;
/*     */     while (true) { j--; if (j < 0) break;
/* 714 */       String str1 = elementAsString(k + j);
/* 715 */       String str2 = localWindowsPath.elementAsString(j);
/*     */ 
/* 717 */       if (!str1.equalsIgnoreCase(str2))
/* 718 */         return false;
/*     */     }
/* 720 */     return true;
/*     */   }
/*     */ 
/*     */   public int compareTo(Path paramPath)
/*     */   {
/* 725 */     if (paramPath == null)
/* 726 */       throw new NullPointerException();
/* 727 */     String str1 = this.path;
/* 728 */     String str2 = ((WindowsPath)paramPath).path;
/* 729 */     int i = str1.length();
/* 730 */     int j = str2.length();
/* 731 */     int k = Math.min(i, j);
/* 732 */     for (int m = 0; m < k; m++) {
/* 733 */       char c1 = str1.charAt(m);
/* 734 */       char c2 = str2.charAt(m);
/* 735 */       if (c1 != c2) {
/* 736 */         c1 = Character.toUpperCase(c1);
/* 737 */         c2 = Character.toUpperCase(c2);
/* 738 */         if (c1 != c2) {
/* 739 */           return c1 - c2;
/*     */         }
/*     */       }
/*     */     }
/* 743 */     return i - j;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 748 */     if ((paramObject != null) && ((paramObject instanceof WindowsPath))) {
/* 749 */       return compareTo((Path)paramObject) == 0;
/*     */     }
/* 751 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 757 */     int i = this.hash;
/* 758 */     if (i == 0) {
/* 759 */       for (int j = 0; j < this.path.length(); j++) {
/* 760 */         i = 31 * i + Character.toUpperCase(this.path.charAt(j));
/*     */       }
/* 762 */       this.hash = i;
/*     */     }
/* 764 */     return i;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 769 */     return this.path;
/*     */   }
/*     */ 
/*     */   long openForReadAttributeAccess(boolean paramBoolean)
/*     */     throws WindowsException
/*     */   {
/* 778 */     int i = 33554432;
/* 779 */     if ((!paramBoolean) && (getFileSystem().supportsLinks()))
/* 780 */       i |= 2097152;
/* 781 */     return WindowsNativeDispatcher.CreateFile(getPathForWin32Calls(), 128, 7, 0L, 3, i);
/*     */   }
/*     */ 
/*     */   void checkRead()
/*     */   {
/* 790 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 791 */     if (localSecurityManager != null)
/* 792 */       localSecurityManager.checkRead(getPathForPermissionCheck());
/*     */   }
/*     */ 
/*     */   void checkWrite()
/*     */   {
/* 797 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 798 */     if (localSecurityManager != null)
/* 799 */       localSecurityManager.checkWrite(getPathForPermissionCheck());
/*     */   }
/*     */ 
/*     */   void checkDelete()
/*     */   {
/* 804 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 805 */     if (localSecurityManager != null)
/* 806 */       localSecurityManager.checkDelete(getPathForPermissionCheck());
/*     */   }
/*     */ 
/*     */   public URI toUri()
/*     */   {
/* 812 */     return WindowsUriSupport.toUri(this);
/*     */   }
/*     */ 
/*     */   public WindowsPath toAbsolutePath()
/*     */   {
/* 817 */     if (isAbsolute()) {
/* 818 */       return this;
/*     */     }
/*     */ 
/* 821 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 822 */     if (localSecurityManager != null) {
/* 823 */       localSecurityManager.checkPropertyAccess("user.dir");
/*     */     }
/*     */     try
/*     */     {
/* 827 */       return createFromNormalizedPath(getFileSystem(), getAbsolutePath());
/*     */     } catch (WindowsException localWindowsException) {
/* 829 */       throw new IOError(new IOException(localWindowsException.getMessage()));
/*     */     }
/*     */   }
/*     */ 
/*     */   public WindowsPath toRealPath(LinkOption[] paramArrayOfLinkOption) throws IOException
/*     */   {
/* 835 */     checkRead();
/* 836 */     String str = WindowsLinkSupport.getRealPath(this, Util.followLinks(paramArrayOfLinkOption));
/* 837 */     return createFromNormalizedPath(getFileSystem(), str);
/*     */   }
/*     */ 
/*     */   public WatchKey register(WatchService paramWatchService, WatchEvent.Kind<?>[] paramArrayOfKind, WatchEvent.Modifier[] paramArrayOfModifier)
/*     */     throws IOException
/*     */   {
/* 846 */     if (paramWatchService == null)
/* 847 */       throw new NullPointerException();
/* 848 */     if (!(paramWatchService instanceof WindowsWatchService)) {
/* 849 */       throw new ProviderMismatchException();
/*     */     }
/*     */ 
/* 855 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 856 */     if (localSecurityManager != null) {
/* 857 */       int i = 0;
/* 858 */       int j = paramArrayOfModifier.length;
/* 859 */       if (j > 0) {
/* 860 */         paramArrayOfModifier = (WatchEvent.Modifier[])Arrays.copyOf(paramArrayOfModifier, j);
/* 861 */         int k = 0;
/* 862 */         while (k < j) {
/* 863 */           if (paramArrayOfModifier[(k++)] == ExtendedWatchEventModifier.FILE_TREE) {
/* 864 */             i = 1;
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 869 */       String str = getPathForPermissionCheck();
/* 870 */       localSecurityManager.checkRead(str);
/* 871 */       if (i != 0) {
/* 872 */         localSecurityManager.checkRead(str + "\\-");
/*     */       }
/*     */     }
/* 875 */     return ((WindowsWatchService)paramWatchService).register(this, paramArrayOfKind, paramArrayOfModifier);
/*     */   }
/*     */ 
/*     */   private static class WindowsPathWithAttributes extends WindowsPath
/*     */     implements BasicFileAttributesHolder
/*     */   {
/*     */     final WeakReference<BasicFileAttributes> ref;
/*     */ 
/*     */     WindowsPathWithAttributes(WindowsFileSystem paramWindowsFileSystem, WindowsPathType paramWindowsPathType, String paramString1, String paramString2, BasicFileAttributes paramBasicFileAttributes)
/*     */     {
/* 149 */       super(paramWindowsPathType, paramString1, paramString2, null);
/* 150 */       this.ref = new WeakReference(paramBasicFileAttributes);
/*     */     }
/*     */ 
/*     */     public BasicFileAttributes get()
/*     */     {
/* 155 */       return (BasicFileAttributes)this.ref.get();
/*     */     }
/*     */ 
/*     */     public void invalidate()
/*     */     {
/* 160 */       this.ref.clear();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.fs.WindowsPath
 * JD-Core Version:    0.6.2
 */