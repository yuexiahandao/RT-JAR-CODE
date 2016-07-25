/*     */ package java.io;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import java.util.Locale;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ class Win32FileSystem extends FileSystem
/*     */ {
/*     */   private final char slash;
/*     */   private final char altSlash;
/*     */   private final char semicolon;
/* 293 */   private static String[] driveDirCache = new String[26];
/*     */ 
/* 370 */   private ExpiringCache cache = new ExpiringCache();
/* 371 */   private ExpiringCache prefixCache = new ExpiringCache();
/*     */ 
/*     */   public Win32FileSystem()
/*     */   {
/*  40 */     this.slash = ((String)AccessController.doPrivileged(new GetPropertyAction("file.separator"))).charAt(0);
/*     */ 
/*  42 */     this.semicolon = ((String)AccessController.doPrivileged(new GetPropertyAction("path.separator"))).charAt(0);
/*     */ 
/*  44 */     this.altSlash = (this.slash == '\\' ? '/' : '\\');
/*     */   }
/*     */ 
/*     */   private boolean isSlash(char paramChar) {
/*  48 */     return (paramChar == '\\') || (paramChar == '/');
/*     */   }
/*     */ 
/*     */   private boolean isLetter(char paramChar) {
/*  52 */     return ((paramChar >= 'a') && (paramChar <= 'z')) || ((paramChar >= 'A') && (paramChar <= 'Z'));
/*     */   }
/*     */ 
/*     */   private String slashify(String paramString) {
/*  56 */     if ((paramString.length() > 0) && (paramString.charAt(0) != this.slash)) return this.slash + paramString;
/*  57 */     return paramString;
/*     */   }
/*     */ 
/*     */   public char getSeparator()
/*     */   {
/*  64 */     return this.slash;
/*     */   }
/*     */ 
/*     */   public char getPathSeparator() {
/*  68 */     return this.semicolon;
/*     */   }
/*     */ 
/*     */   private int normalizePrefix(String paramString, int paramInt, StringBuffer paramStringBuffer)
/*     */   {
/*  85 */     int i = 0;
/*  86 */     while ((i < paramInt) && (isSlash(paramString.charAt(i)))) i++;
/*     */     char c;
/*  88 */     if ((paramInt - i >= 2) && (isLetter(c = paramString.charAt(i))) && (paramString.charAt(i + 1) == ':'))
/*     */     {
/*  95 */       paramStringBuffer.append(c);
/*  96 */       paramStringBuffer.append(':');
/*  97 */       i += 2;
/*     */     } else {
/*  99 */       i = 0;
/* 100 */       if ((paramInt >= 2) && (isSlash(paramString.charAt(0))) && (isSlash(paramString.charAt(1))))
/*     */       {
/* 108 */         i = 1;
/* 109 */         paramStringBuffer.append(this.slash);
/*     */       }
/*     */     }
/* 112 */     return i;
/*     */   }
/*     */ 
/*     */   private String normalize(String paramString, int paramInt1, int paramInt2)
/*     */   {
/* 118 */     if (paramInt1 == 0) return paramString;
/* 119 */     if (paramInt2 < 3) paramInt2 = 0;
/*     */ 
/* 121 */     char c1 = this.slash;
/* 122 */     StringBuffer localStringBuffer = new StringBuffer(paramInt1);
/*     */     int i;
/* 124 */     if (paramInt2 == 0)
/*     */     {
/* 126 */       i = normalizePrefix(paramString, paramInt1, localStringBuffer);
/*     */     }
/*     */     else {
/* 129 */       i = paramInt2;
/* 130 */       localStringBuffer.append(paramString.substring(0, paramInt2));
/*     */     }
/*     */ 
/* 135 */     while (i < paramInt1) {
/* 136 */       char c2 = paramString.charAt(i++);
/* 137 */       if (isSlash(c2)) {
/* 138 */         while ((i < paramInt1) && (isSlash(paramString.charAt(i)))) i++;
/* 139 */         if (i == paramInt1)
/*     */         {
/* 141 */           int j = localStringBuffer.length();
/* 142 */           if ((j == 2) && (localStringBuffer.charAt(1) == ':'))
/*     */           {
/* 144 */             localStringBuffer.append(c1);
/* 145 */             break;
/*     */           }
/* 147 */           if (j == 0)
/*     */           {
/* 149 */             localStringBuffer.append(c1);
/* 150 */             break;
/*     */           }
/* 152 */           if ((j != 1) || (!isSlash(localStringBuffer.charAt(0))))
/*     */           {
/*     */             break;
/*     */           }
/*     */ 
/* 160 */           localStringBuffer.append(c1);
/* 161 */           break;
/*     */         }
/*     */ 
/* 167 */         localStringBuffer.append(c1);
/*     */       }
/*     */       else {
/* 170 */         localStringBuffer.append(c2);
/*     */       }
/*     */     }
/*     */ 
/* 174 */     String str = localStringBuffer.toString();
/* 175 */     return str;
/*     */   }
/*     */ 
/*     */   public String normalize(String paramString)
/*     */   {
/* 182 */     int i = paramString.length();
/* 183 */     int j = this.slash;
/* 184 */     int k = this.altSlash;
/* 185 */     int m = 0;
/* 186 */     for (int n = 0; n < i; n++) {
/* 187 */       int i1 = paramString.charAt(n);
/* 188 */       if (i1 == k)
/* 189 */         return normalize(paramString, i, m == j ? n - 1 : n);
/* 190 */       if ((i1 == j) && (m == j) && (n > 1))
/* 191 */         return normalize(paramString, i, n - 1);
/* 192 */       if ((i1 == 58) && (n > 1))
/* 193 */         return normalize(paramString, i, 0);
/* 194 */       m = i1;
/*     */     }
/* 196 */     if (m == j) return normalize(paramString, i, i - 1);
/* 197 */     return paramString;
/*     */   }
/*     */ 
/*     */   public int prefixLength(String paramString) {
/* 201 */     int i = this.slash;
/* 202 */     int j = paramString.length();
/* 203 */     if (j == 0) return 0;
/* 204 */     char c = paramString.charAt(0);
/* 205 */     int k = j > 1 ? paramString.charAt(1) : 0;
/* 206 */     if (c == i) {
/* 207 */       if (k == i) return 2;
/* 208 */       return 1;
/*     */     }
/* 210 */     if ((isLetter(c)) && (k == 58)) {
/* 211 */       if ((j > 2) && (paramString.charAt(2) == i))
/* 212 */         return 3;
/* 213 */       return 2;
/*     */     }
/* 215 */     return 0;
/*     */   }
/*     */ 
/*     */   public String resolve(String paramString1, String paramString2) {
/* 219 */     int i = paramString1.length();
/* 220 */     if (i == 0) return paramString2;
/* 221 */     int j = paramString2.length();
/* 222 */     if (j == 0) return paramString1;
/*     */ 
/* 224 */     String str = paramString2;
/* 225 */     int k = 0;
/* 226 */     int m = i;
/*     */ 
/* 228 */     if ((j > 1) && (str.charAt(0) == this.slash)) {
/* 229 */       if (str.charAt(1) == this.slash)
/*     */       {
/* 231 */         k = 2;
/*     */       }
/*     */       else {
/* 234 */         k = 1;
/*     */       }
/*     */ 
/* 237 */       if (j == k) {
/* 238 */         if (paramString1.charAt(i - 1) == this.slash)
/* 239 */           return paramString1.substring(0, i - 1);
/* 240 */         return paramString1;
/*     */       }
/*     */     }
/*     */ 
/* 244 */     if (paramString1.charAt(i - 1) == this.slash) {
/* 245 */       m--;
/*     */     }
/* 247 */     int n = m + j - k;
/* 248 */     char[] arrayOfChar = null;
/* 249 */     if (paramString2.charAt(k) == this.slash) {
/* 250 */       arrayOfChar = new char[n];
/* 251 */       paramString1.getChars(0, m, arrayOfChar, 0);
/* 252 */       paramString2.getChars(k, j, arrayOfChar, m);
/*     */     } else {
/* 254 */       arrayOfChar = new char[n + 1];
/* 255 */       paramString1.getChars(0, m, arrayOfChar, 0);
/* 256 */       arrayOfChar[m] = this.slash;
/* 257 */       paramString2.getChars(k, j, arrayOfChar, m + 1);
/*     */     }
/* 259 */     return new String(arrayOfChar);
/*     */   }
/*     */ 
/*     */   public String getDefaultParent() {
/* 263 */     return "" + this.slash;
/*     */   }
/*     */ 
/*     */   public String fromURIPath(String paramString) {
/* 267 */     String str = paramString;
/* 268 */     if ((str.length() > 2) && (str.charAt(2) == ':'))
/*     */     {
/* 270 */       str = str.substring(1);
/*     */ 
/* 272 */       if ((str.length() > 3) && (str.endsWith("/")))
/* 273 */         str = str.substring(0, str.length() - 1);
/* 274 */     } else if ((str.length() > 1) && (str.endsWith("/")))
/*     */     {
/* 276 */       str = str.substring(0, str.length() - 1);
/*     */     }
/* 278 */     return str;
/*     */   }
/*     */ 
/*     */   public boolean isAbsolute(File paramFile)
/*     */   {
/* 286 */     int i = paramFile.getPrefixLength();
/* 287 */     return ((i == 2) && (paramFile.getPath().charAt(0) == this.slash)) || (i == 3);
/*     */   }
/*     */ 
/*     */   protected native String getDriveDirectory(int paramInt);
/*     */ 
/*     */   private static int driveIndex(char paramChar)
/*     */   {
/* 296 */     if ((paramChar >= 'a') && (paramChar <= 'z')) return paramChar - 'a';
/* 297 */     if ((paramChar >= 'A') && (paramChar <= 'Z')) return paramChar - 'A';
/* 298 */     return -1;
/*     */   }
/*     */ 
/*     */   private String getDriveDirectory(char paramChar) {
/* 302 */     int i = driveIndex(paramChar);
/* 303 */     if (i < 0) return null;
/* 304 */     String str = driveDirCache[i];
/* 305 */     if (str != null) return str;
/* 306 */     str = getDriveDirectory(i + 1);
/* 307 */     driveDirCache[i] = str;
/* 308 */     return str;
/*     */   }
/*     */ 
/*     */   private String getUserPath()
/*     */   {
/* 314 */     return normalize(System.getProperty("user.dir"));
/*     */   }
/*     */ 
/*     */   private String getDrive(String paramString) {
/* 318 */     int i = prefixLength(paramString);
/* 319 */     return i == 3 ? paramString.substring(0, 2) : null;
/*     */   }
/*     */ 
/*     */   public String resolve(File paramFile) {
/* 323 */     String str1 = paramFile.getPath();
/* 324 */     int i = paramFile.getPrefixLength();
/* 325 */     if ((i == 2) && (str1.charAt(0) == this.slash))
/* 326 */       return str1;
/* 327 */     if (i == 3)
/* 328 */       return str1;
/* 329 */     if (i == 0)
/* 330 */       return getUserPath() + slashify(str1);
/*     */     String str2;
/*     */     String str3;
/* 331 */     if (i == 1) {
/* 332 */       str2 = getUserPath();
/* 333 */       str3 = getDrive(str2);
/* 334 */       if (str3 != null) return str3 + str1;
/* 335 */       return str2 + str1;
/*     */     }
/* 337 */     if (i == 2) {
/* 338 */       str2 = getUserPath();
/* 339 */       str3 = getDrive(str2);
/* 340 */       if ((str3 != null) && (str1.startsWith(str3)))
/* 341 */         return str2 + slashify(str1.substring(2));
/* 342 */       char c = str1.charAt(0);
/* 343 */       String str4 = getDriveDirectory(c);
/*     */ 
/* 345 */       if (str4 != null)
/*     */       {
/* 349 */         String str5 = c + ':' + str4 + slashify(str1.substring(2));
/* 350 */         SecurityManager localSecurityManager = System.getSecurityManager();
/*     */         try {
/* 352 */           if (localSecurityManager != null) localSecurityManager.checkRead(str5); 
/*     */         }
/*     */         catch (SecurityException localSecurityException)
/*     */         {
/* 355 */           throw new SecurityException("Cannot resolve path " + str1);
/*     */         }
/* 357 */         return str5;
/*     */       }
/* 359 */       return c + ":" + slashify(str1.substring(2));
/*     */     }
/* 361 */     throw new InternalError("Unresolvable path: " + str1);
/*     */   }
/*     */ 
/*     */   public String canonicalize(String paramString)
/*     */     throws IOException
/*     */   {
/* 375 */     int i = paramString.length();
/*     */     int j;
/* 376 */     if ((i == 2) && (isLetter(paramString.charAt(0))) && (paramString.charAt(1) == ':'))
/*     */     {
/* 379 */       j = paramString.charAt(0);
/* 380 */       if ((j >= 65) && (j <= 90))
/* 381 */         return paramString;
/* 382 */       return "" + (char)(j - 32) + ':';
/* 383 */     }if ((i == 3) && (isLetter(paramString.charAt(0))) && (paramString.charAt(1) == ':') && (paramString.charAt(2) == '\\'))
/*     */     {
/* 387 */       j = paramString.charAt(0);
/* 388 */       if ((j >= 65) && (j <= 90))
/* 389 */         return paramString;
/* 390 */       return "" + (char)(j - 32) + ':' + '\\';
/*     */     }
/* 392 */     if (!useCanonCaches) {
/* 393 */       return canonicalize0(paramString);
/*     */     }
/* 395 */     String str1 = this.cache.get(paramString);
/* 396 */     if (str1 == null) {
/* 397 */       String str2 = null;
/* 398 */       String str3 = null;
/*     */       Object localObject;
/* 399 */       if (useCanonPrefixCache) {
/* 400 */         str2 = parentOrNull(paramString);
/* 401 */         if (str2 != null) {
/* 402 */           str3 = this.prefixCache.get(str2);
/* 403 */           if (str3 != null)
/*     */           {
/* 407 */             localObject = paramString.substring(1 + str2.length());
/* 408 */             str1 = canonicalizeWithPrefix(str3, (String)localObject);
/* 409 */             this.cache.put(str2 + File.separatorChar + (String)localObject, str1);
/*     */           }
/*     */         }
/*     */       }
/* 413 */       if (str1 == null) {
/* 414 */         str1 = canonicalize0(paramString);
/* 415 */         this.cache.put(paramString, str1);
/* 416 */         if ((useCanonPrefixCache) && (str2 != null)) {
/* 417 */           str3 = parentOrNull(str1);
/* 418 */           if (str3 != null) {
/* 419 */             localObject = new File(str1);
/* 420 */             if ((((File)localObject).exists()) && (!((File)localObject).isDirectory())) {
/* 421 */               this.prefixCache.put(str2, str3);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 427 */     return str1;
/*     */   }
/*     */ 
/*     */   protected native String canonicalize0(String paramString)
/*     */     throws IOException;
/*     */ 
/*     */   protected String canonicalizeWithPrefix(String paramString1, String paramString2)
/*     */     throws IOException
/*     */   {
/* 436 */     return canonicalizeWithPrefix0(paramString1, paramString1 + File.separatorChar + paramString2);
/*     */   }
/*     */ 
/*     */   protected native String canonicalizeWithPrefix0(String paramString1, String paramString2)
/*     */     throws IOException;
/*     */ 
/*     */   static String parentOrNull(String paramString)
/*     */   {
/* 453 */     if (paramString == null) return null;
/* 454 */     int i = File.separatorChar;
/* 455 */     int j = 47;
/* 456 */     int k = paramString.length() - 1;
/* 457 */     int m = k;
/* 458 */     int n = 0;
/* 459 */     int i1 = 0;
/* 460 */     while (m > 0) {
/* 461 */       int i2 = paramString.charAt(m);
/* 462 */       if (i2 == 46) {
/* 463 */         n++; if (n >= 2)
/*     */         {
/* 465 */           return null;
/*     */         }
/* 467 */         if (i1 == 0)
/*     */         {
/* 469 */           return null;
/*     */         }
/*     */       } else { if (i2 == i) {
/* 472 */           if ((n == 1) && (i1 == 0))
/*     */           {
/* 474 */             return null;
/*     */           }
/* 476 */           if ((m == 0) || (m >= k - 1) || (paramString.charAt(m - 1) == i) || (paramString.charAt(m - 1) == j))
/*     */           {
/* 482 */             return null;
/*     */           }
/* 484 */           return paramString.substring(0, m);
/* 485 */         }if (i2 == j)
/*     */         {
/* 488 */           return null;
/* 489 */         }if ((i2 == 42) || (i2 == 63))
/*     */         {
/* 491 */           return null;
/*     */         }
/* 493 */         i1++;
/* 494 */         n = 0;
/*     */       }
/* 496 */       m--;
/*     */     }
/* 498 */     return null;
/*     */   }
/*     */ 
/*     */   public native int getBooleanAttributes(File paramFile);
/*     */ 
/*     */   public native boolean checkAccess(File paramFile, int paramInt);
/*     */ 
/*     */   public native long getLastModifiedTime(File paramFile);
/*     */ 
/*     */   public native long getLength(File paramFile);
/*     */ 
/*     */   public native boolean setPermission(File paramFile, int paramInt, boolean paramBoolean1, boolean paramBoolean2);
/*     */ 
/*     */   public native boolean createFileExclusively(String paramString)
/*     */     throws IOException;
/*     */ 
/*     */   public boolean delete(File paramFile)
/*     */   {
/* 520 */     this.cache.clear();
/* 521 */     this.prefixCache.clear();
/* 522 */     return delete0(paramFile);
/*     */   }
/*     */ 
/*     */   protected native boolean delete0(File paramFile);
/*     */ 
/*     */   public native String[] list(File paramFile);
/*     */ 
/*     */   public native boolean createDirectory(File paramFile);
/*     */ 
/*     */   public boolean rename(File paramFile1, File paramFile2)
/*     */   {
/* 533 */     this.cache.clear();
/* 534 */     this.prefixCache.clear();
/* 535 */     return rename0(paramFile1, paramFile2);
/*     */   }
/*     */ 
/*     */   protected native boolean rename0(File paramFile1, File paramFile2);
/*     */ 
/*     */   public native boolean setLastModifiedTime(File paramFile, long paramLong);
/*     */ 
/*     */   public native boolean setReadOnly(File paramFile);
/*     */ 
/*     */   private boolean access(String paramString) {
/*     */     try {
/* 546 */       SecurityManager localSecurityManager = System.getSecurityManager();
/* 547 */       if (localSecurityManager != null) localSecurityManager.checkRead(paramString);
/* 548 */       return true; } catch (SecurityException localSecurityException) {
/*     */     }
/* 550 */     return false;
/*     */   }
/*     */ 
/*     */   private static native int listRoots0();
/*     */ 
/*     */   public File[] listRoots()
/*     */   {
/* 557 */     int i = listRoots0();
/* 558 */     int j = 0;
/* 559 */     for (int k = 0; k < 26; k++) {
/* 560 */       if ((i >> k & 0x1) != 0) {
/* 561 */         if (!access((char)(65 + k) + ":" + this.slash))
/* 562 */           i &= (1 << k ^ 0xFFFFFFFF);
/*     */         else
/* 564 */           j++;
/*     */       }
/*     */     }
/* 567 */     File[] arrayOfFile = new File[j];
/* 568 */     int m = 0;
/* 569 */     char c = this.slash;
/* 570 */     for (int n = 0; n < 26; n++) {
/* 571 */       if ((i >> n & 0x1) != 0)
/* 572 */         arrayOfFile[(m++)] = new File((char)(65 + n) + ":" + c);
/*     */     }
/* 574 */     return arrayOfFile;
/*     */   }
/*     */ 
/*     */   public long getSpace(File paramFile, int paramInt)
/*     */   {
/* 580 */     if (paramFile.exists()) {
/* 581 */       File localFile = paramFile.isDirectory() ? paramFile : paramFile.getParentFile();
/* 582 */       return getSpace0(localFile, paramInt);
/*     */     }
/* 584 */     return 0L;
/*     */   }
/*     */ 
/*     */   private native long getSpace0(File paramFile, int paramInt);
/*     */ 
/*     */   public int compare(File paramFile1, File paramFile2)
/*     */   {
/* 593 */     return paramFile1.getPath().compareToIgnoreCase(paramFile2.getPath());
/*     */   }
/*     */ 
/*     */   public int hashCode(File paramFile)
/*     */   {
/* 598 */     return paramFile.getPath().toLowerCase(Locale.ENGLISH).hashCode() ^ 0x12D591;
/*     */   }
/*     */ 
/*     */   private static native void initIDs();
/*     */ 
/*     */   static
/*     */   {
/* 605 */     initIDs();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.Win32FileSystem
 * JD-Core Version:    0.6.2
 */