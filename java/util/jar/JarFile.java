/*     */ package java.util.jar;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.net.URL;
/*     */ import java.security.AccessController;
/*     */ import java.security.CodeSigner;
/*     */ import java.security.CodeSource;
/*     */ import java.security.cert.Certificate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipFile;
/*     */ import sun.misc.IOUtils;
/*     */ import sun.misc.SharedSecrets;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ import sun.security.util.Debug;
/*     */ import sun.security.util.ManifestEntryVerifier;
/*     */ import sun.security.util.SignatureFileVerifier;
/*     */ 
/*     */ public class JarFile extends ZipFile
/*     */ {
/*     */   private SoftReference<Manifest> manRef;
/*     */   private JarEntry manEntry;
/*     */   private JarVerifier jv;
/*     */   private boolean jvInitialized;
/*     */   private boolean verify;
/*     */   private boolean computedHasClassPathAttribute;
/*     */   private boolean hasClassPathAttribute;
/*     */   public static final String MANIFEST_NAME = "META-INF/MANIFEST.MF";
/*     */   private static int[] lastOcc;
/*     */   private static int[] optoSft;
/*     */   private static char[] src;
/*     */   private static String javaHome;
/*     */   private static String[] jarNames;
/*     */ 
/*     */   public JarFile(String paramString)
/*     */     throws IOException
/*     */   {
/*  91 */     this(new File(paramString), true, 1);
/*     */   }
/*     */ 
/*     */   public JarFile(String paramString, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 105 */     this(new File(paramString), paramBoolean, 1);
/*     */   }
/*     */ 
/*     */   public JarFile(File paramFile)
/*     */     throws IOException
/*     */   {
/* 118 */     this(paramFile, true, 1);
/*     */   }
/*     */ 
/*     */   public JarFile(File paramFile, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 133 */     this(paramFile, paramBoolean, 1);
/*     */   }
/*     */ 
/*     */   public JarFile(File paramFile, boolean paramBoolean, int paramInt)
/*     */     throws IOException
/*     */   {
/* 154 */     super(paramFile, paramInt);
/* 155 */     this.verify = paramBoolean;
/*     */   }
/*     */ 
/*     */   public Manifest getManifest()
/*     */     throws IOException
/*     */   {
/* 167 */     return getManifestFromReference();
/*     */   }
/*     */ 
/*     */   private Manifest getManifestFromReference() throws IOException {
/* 171 */     Manifest localManifest = this.manRef != null ? (Manifest)this.manRef.get() : null;
/*     */ 
/* 173 */     if (localManifest == null)
/*     */     {
/* 175 */       JarEntry localJarEntry = getManEntry();
/*     */ 
/* 178 */       if (localJarEntry != null) {
/* 179 */         if (this.verify) {
/* 180 */           byte[] arrayOfByte = getBytes(localJarEntry);
/* 181 */           localManifest = new Manifest(new ByteArrayInputStream(arrayOfByte));
/* 182 */           if (!this.jvInitialized)
/* 183 */             this.jv = new JarVerifier(arrayOfByte);
/*     */         }
/*     */         else {
/* 186 */           localManifest = new Manifest(super.getInputStream(localJarEntry));
/*     */         }
/* 188 */         this.manRef = new SoftReference(localManifest);
/*     */       }
/*     */     }
/* 191 */     return localManifest;
/*     */   }
/*     */ 
/*     */   private native String[] getMetaInfEntryNames();
/*     */ 
/*     */   public JarEntry getJarEntry(String paramString)
/*     */   {
/* 210 */     return (JarEntry)getEntry(paramString);
/*     */   }
/*     */ 
/*     */   public ZipEntry getEntry(String paramString)
/*     */   {
/* 227 */     ZipEntry localZipEntry = super.getEntry(paramString);
/* 228 */     if (localZipEntry != null) {
/* 229 */       return new JarFileEntry(localZipEntry);
/*     */     }
/* 231 */     return null;
/*     */   }
/*     */ 
/*     */   public Enumeration<JarEntry> entries()
/*     */   {
/* 238 */     final Enumeration localEnumeration = super.entries();
/* 239 */     return new Enumeration() {
/*     */       public boolean hasMoreElements() {
/* 241 */         return localEnumeration.hasMoreElements();
/*     */       }
/*     */       public JarFile.JarFileEntry nextElement() {
/* 244 */         ZipEntry localZipEntry = (ZipEntry)localEnumeration.nextElement();
/* 245 */         return new JarFile.JarFileEntry(JarFile.this, localZipEntry);
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   private void maybeInstantiateVerifier()
/*     */     throws IOException
/*     */   {
/* 293 */     if (this.jv != null) {
/* 294 */       return;
/*     */     }
/*     */ 
/* 297 */     if (this.verify) {
/* 298 */       String[] arrayOfString = getMetaInfEntryNames();
/* 299 */       if (arrayOfString != null) {
/* 300 */         for (int i = 0; i < arrayOfString.length; i++) {
/* 301 */           String str = arrayOfString[i].toUpperCase(Locale.ENGLISH);
/* 302 */           if ((str.endsWith(".DSA")) || (str.endsWith(".RSA")) || (str.endsWith(".EC")) || (str.endsWith(".SF")))
/*     */           {
/* 309 */             getManifest();
/* 310 */             return;
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 316 */       this.verify = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void initializeVerifier()
/*     */   {
/* 326 */     ManifestEntryVerifier localManifestEntryVerifier = null;
/*     */     try
/*     */     {
/* 330 */       String[] arrayOfString = getMetaInfEntryNames();
/* 331 */       if (arrayOfString != null) {
/* 332 */         for (int i = 0; i < arrayOfString.length; i++) {
/* 333 */           String str = arrayOfString[i].toUpperCase(Locale.ENGLISH);
/* 334 */           if (("META-INF/MANIFEST.MF".equals(str)) || (SignatureFileVerifier.isBlockOrSF(str)))
/*     */           {
/* 336 */             JarEntry localJarEntry = getJarEntry(arrayOfString[i]);
/* 337 */             if (localJarEntry == null) {
/* 338 */               throw new JarException("corrupted jar file");
/*     */             }
/* 340 */             if (localManifestEntryVerifier == null) {
/* 341 */               localManifestEntryVerifier = new ManifestEntryVerifier(getManifestFromReference());
/*     */             }
/*     */ 
/* 344 */             byte[] arrayOfByte = getBytes(localJarEntry);
/* 345 */             if ((arrayOfByte != null) && (arrayOfByte.length > 0)) {
/* 346 */               this.jv.beginEntry(localJarEntry, localManifestEntryVerifier);
/* 347 */               this.jv.update(arrayOfByte.length, arrayOfByte, 0, arrayOfByte.length, localManifestEntryVerifier);
/* 348 */               this.jv.update(-1, null, 0, 0, localManifestEntryVerifier);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/* 356 */       this.jv = null;
/* 357 */       this.verify = false;
/* 358 */       if (JarVerifier.debug != null) {
/* 359 */         JarVerifier.debug.println("jarfile parsing error!");
/* 360 */         localIOException.printStackTrace();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 367 */     if (this.jv != null)
/*     */     {
/* 369 */       this.jv.doneWithMeta();
/* 370 */       if (JarVerifier.debug != null) {
/* 371 */         JarVerifier.debug.println("done with meta!");
/*     */       }
/*     */ 
/* 374 */       if (this.jv.nothingToVerify()) {
/* 375 */         if (JarVerifier.debug != null) {
/* 376 */           JarVerifier.debug.println("nothing to verify!");
/*     */         }
/* 378 */         this.jv = null;
/* 379 */         this.verify = false;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private byte[] getBytes(ZipEntry paramZipEntry)
/*     */     throws IOException
/*     */   {
/* 389 */     InputStream localInputStream = super.getInputStream(paramZipEntry); Object localObject1 = null;
/*     */     try { return IOUtils.readFully(localInputStream, (int)paramZipEntry.getSize(), true); }
/*     */     catch (Throwable localThrowable1)
/*     */     {
/* 389 */       localObject1 = localThrowable1; throw localThrowable1;
/*     */     } finally {
/* 391 */       if (localInputStream != null) if (localObject1 != null) try { localInputStream.close(); } catch (Throwable localThrowable3) { localObject1.addSuppressed(localThrowable3); } else localInputStream.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized InputStream getInputStream(ZipEntry paramZipEntry)
/*     */     throws IOException
/*     */   {
/* 410 */     maybeInstantiateVerifier();
/* 411 */     if (this.jv == null) {
/* 412 */       return super.getInputStream(paramZipEntry);
/*     */     }
/* 414 */     if (!this.jvInitialized) {
/* 415 */       initializeVerifier();
/* 416 */       this.jvInitialized = true;
/*     */ 
/* 420 */       if (this.jv == null) {
/* 421 */         return super.getInputStream(paramZipEntry);
/*     */       }
/*     */     }
/*     */ 
/* 425 */     return new JarVerifier.VerifierStream(getManifestFromReference(), (paramZipEntry instanceof JarFileEntry) ? (JarEntry)paramZipEntry : getJarEntry(paramZipEntry.getName()), super.getInputStream(paramZipEntry), this.jv);
/*     */   }
/*     */ 
/*     */   private JarEntry getManEntry()
/*     */   {
/* 457 */     if (this.manEntry == null)
/*     */     {
/* 459 */       this.manEntry = getJarEntry("META-INF/MANIFEST.MF");
/* 460 */       if (this.manEntry == null)
/*     */       {
/* 463 */         String[] arrayOfString = getMetaInfEntryNames();
/* 464 */         if (arrayOfString != null) {
/* 465 */           for (int i = 0; i < arrayOfString.length; i++) {
/* 466 */             if ("META-INF/MANIFEST.MF".equals(arrayOfString[i].toUpperCase(Locale.ENGLISH)))
/*     */             {
/* 468 */               this.manEntry = getJarEntry(arrayOfString[i]);
/* 469 */               break;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 475 */     return this.manEntry;
/*     */   }
/*     */ 
/*     */   boolean hasClassPathAttribute()
/*     */     throws IOException
/*     */   {
/* 483 */     if (this.computedHasClassPathAttribute) {
/* 484 */       return this.hasClassPathAttribute;
/*     */     }
/*     */ 
/* 487 */     this.hasClassPathAttribute = false;
/* 488 */     if (!isKnownToNotHaveClassPathAttribute()) {
/* 489 */       JarEntry localJarEntry = getManEntry();
/* 490 */       if (localJarEntry != null) {
/* 491 */         byte[] arrayOfByte = getBytes(localJarEntry);
/* 492 */         int i = arrayOfByte.length - src.length;
/* 493 */         int j = 0;
/*     */ 
/* 495 */         if (j <= i) {
/* 496 */           for (int k = 9; ; k--) { if (k < 0) break label150;
/* 497 */             int m = (char)arrayOfByte[(j + k)];
/* 498 */             m = (m - 65 | 90 - m) >= 0 ? (char)(m + 32) : m;
/* 499 */             if (m != src[k]) {
/* 500 */               j += Math.max(k + 1 - lastOcc[(m & 0x7F)], optoSft[k]);
/* 501 */               break;
/*     */             }
/*     */           }
/* 504 */           label150: this.hasClassPathAttribute = true;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 509 */     this.computedHasClassPathAttribute = true;
/* 510 */     return this.hasClassPathAttribute;
/*     */   }
/*     */ 
/*     */   private boolean isKnownToNotHaveClassPathAttribute()
/*     */   {
/* 520 */     if (javaHome == null) {
/* 521 */       javaHome = (String)AccessController.doPrivileged(new GetPropertyAction("java.home"));
/*     */     }
/*     */ 
/* 524 */     if (jarNames == null) {
/* 525 */       localObject = new String[10];
/* 526 */       str = File.separator;
/* 527 */       int i = 0;
/* 528 */       localObject[(i++)] = (str + "rt.jar");
/* 529 */       localObject[(i++)] = (str + "sunrsasign.jar");
/* 530 */       localObject[(i++)] = (str + "jsse.jar");
/* 531 */       localObject[(i++)] = (str + "jce.jar");
/* 532 */       localObject[(i++)] = (str + "charsets.jar");
/* 533 */       localObject[(i++)] = (str + "dnsns.jar");
/* 534 */       localObject[(i++)] = (str + "ldapsec.jar");
/* 535 */       localObject[(i++)] = (str + "localedata.jar");
/* 536 */       localObject[(i++)] = (str + "sunjce_provider.jar");
/* 537 */       localObject[(i++)] = (str + "sunpkcs11.jar");
/* 538 */       jarNames = (String[])localObject;
/*     */     }
/*     */ 
/* 541 */     Object localObject = getName();
/* 542 */     String str = javaHome;
/* 543 */     if (((String)localObject).startsWith(str)) {
/* 544 */       String[] arrayOfString = jarNames;
/* 545 */       for (int j = 0; j < arrayOfString.length; j++) {
/* 546 */         if (((String)localObject).endsWith(arrayOfString[j])) {
/* 547 */           return true;
/*     */         }
/*     */       }
/*     */     }
/* 551 */     return false;
/*     */   }
/*     */ 
/*     */   private synchronized void ensureInitialization() {
/*     */     try {
/* 556 */       maybeInstantiateVerifier();
/*     */     } catch (IOException localIOException) {
/* 558 */       throw new RuntimeException(localIOException);
/*     */     }
/* 560 */     if ((this.jv != null) && (!this.jvInitialized)) {
/* 561 */       initializeVerifier();
/* 562 */       this.jvInitialized = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   JarEntry newEntry(ZipEntry paramZipEntry) {
/* 567 */     return new JarFileEntry(paramZipEntry);
/*     */   }
/*     */ 
/*     */   Enumeration<String> entryNames(CodeSource[] paramArrayOfCodeSource) {
/* 571 */     ensureInitialization();
/* 572 */     if (this.jv != null) {
/* 573 */       return this.jv.entryNames(this, paramArrayOfCodeSource);
/*     */     }
/*     */ 
/* 580 */     int i = 0;
/* 581 */     for (int j = 0; j < paramArrayOfCodeSource.length; j++) {
/* 582 */       if (paramArrayOfCodeSource[j].getCodeSigners() == null) {
/* 583 */         i = 1;
/* 584 */         break;
/*     */       }
/*     */     }
/* 587 */     if (i != 0) {
/* 588 */       return unsignedEntryNames();
/*     */     }
/* 590 */     return new Enumeration()
/*     */     {
/*     */       public boolean hasMoreElements() {
/* 593 */         return false;
/*     */       }
/*     */ 
/*     */       public String nextElement() {
/* 597 */         throw new NoSuchElementException();
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   Enumeration<JarEntry> entries2()
/*     */   {
/* 609 */     ensureInitialization();
/* 610 */     if (this.jv != null) {
/* 611 */       return this.jv.entries2(this, super.entries());
/*     */     }
/*     */ 
/* 615 */     final Enumeration localEnumeration = super.entries();
/* 616 */     return new Enumeration()
/*     */     {
/*     */       ZipEntry entry;
/*     */ 
/*     */       public boolean hasMoreElements() {
/* 621 */         if (this.entry != null) {
/* 622 */           return true;
/*     */         }
/* 624 */         while (localEnumeration.hasMoreElements()) {
/* 625 */           ZipEntry localZipEntry = (ZipEntry)localEnumeration.nextElement();
/* 626 */           if (!JarVerifier.isSigningRelated(localZipEntry.getName()))
/*     */           {
/* 629 */             this.entry = localZipEntry;
/* 630 */             return true;
/*     */           }
/*     */         }
/* 632 */         return false;
/*     */       }
/*     */ 
/*     */       public JarFile.JarFileEntry nextElement() {
/* 636 */         if (hasMoreElements()) {
/* 637 */           ZipEntry localZipEntry = this.entry;
/* 638 */           this.entry = null;
/* 639 */           return new JarFile.JarFileEntry(JarFile.this, localZipEntry);
/*     */         }
/* 641 */         throw new NoSuchElementException();
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   CodeSource[] getCodeSources(URL paramURL) {
/* 647 */     ensureInitialization();
/* 648 */     if (this.jv != null) {
/* 649 */       return this.jv.getCodeSources(this, paramURL);
/*     */     }
/*     */ 
/* 656 */     Enumeration localEnumeration = unsignedEntryNames();
/* 657 */     if (localEnumeration.hasMoreElements()) {
/* 658 */       return new CodeSource[] { JarVerifier.getUnsignedCS(paramURL) };
/*     */     }
/* 660 */     return null;
/*     */   }
/*     */ 
/*     */   private Enumeration<String> unsignedEntryNames()
/*     */   {
/* 665 */     final Enumeration localEnumeration = entries();
/* 666 */     return new Enumeration()
/*     */     {
/*     */       String name;
/*     */ 
/*     */       public boolean hasMoreElements()
/*     */       {
/* 675 */         if (this.name != null) {
/* 676 */           return true;
/*     */         }
/* 678 */         while (localEnumeration.hasMoreElements())
/*     */         {
/* 680 */           ZipEntry localZipEntry = (ZipEntry)localEnumeration.nextElement();
/* 681 */           String str = localZipEntry.getName();
/* 682 */           if ((!localZipEntry.isDirectory()) && (!JarVerifier.isSigningRelated(str)))
/*     */           {
/* 685 */             this.name = str;
/* 686 */             return true;
/*     */           }
/*     */         }
/* 688 */         return false;
/*     */       }
/*     */ 
/*     */       public String nextElement() {
/* 692 */         if (hasMoreElements()) {
/* 693 */           String str = this.name;
/* 694 */           this.name = null;
/* 695 */           return str;
/*     */         }
/* 697 */         throw new NoSuchElementException();
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   CodeSource getCodeSource(URL paramURL, String paramString) {
/* 703 */     ensureInitialization();
/* 704 */     if (this.jv != null) {
/* 705 */       if (this.jv.eagerValidation) {
/* 706 */         CodeSource localCodeSource = null;
/* 707 */         JarEntry localJarEntry = getJarEntry(paramString);
/* 708 */         if (localJarEntry != null)
/* 709 */           localCodeSource = this.jv.getCodeSource(paramURL, this, localJarEntry);
/*     */         else {
/* 711 */           localCodeSource = this.jv.getCodeSource(paramURL, paramString);
/*     */         }
/* 713 */         return localCodeSource;
/*     */       }
/* 715 */       return this.jv.getCodeSource(paramURL, paramString);
/*     */     }
/*     */ 
/* 719 */     return JarVerifier.getUnsignedCS(paramURL);
/*     */   }
/*     */ 
/*     */   void setEagerValidation(boolean paramBoolean) {
/*     */     try {
/* 724 */       maybeInstantiateVerifier();
/*     */     } catch (IOException localIOException) {
/* 726 */       throw new RuntimeException(localIOException);
/*     */     }
/* 728 */     if (this.jv != null)
/* 729 */       this.jv.setEagerValidation(paramBoolean);
/*     */   }
/*     */ 
/*     */   List getManifestDigests()
/*     */   {
/* 734 */     ensureInitialization();
/* 735 */     if (this.jv != null) {
/* 736 */       return this.jv.getManifestDigests();
/*     */     }
/* 738 */     return new ArrayList();
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  73 */     SharedSecrets.setJavaUtilJarAccess(new JavaUtilJarAccessImpl());
/*     */ 
/* 439 */     src = new char[] { 'c', 'l', 'a', 's', 's', '-', 'p', 'a', 't', 'h' };
/*     */ 
/* 441 */     lastOcc = new int[''];
/* 442 */     optoSft = new int[10];
/* 443 */     lastOcc[99] = 1;
/* 444 */     lastOcc[108] = 2;
/* 445 */     lastOcc[115] = 5;
/* 446 */     lastOcc[45] = 6;
/* 447 */     lastOcc[112] = 7;
/* 448 */     lastOcc[97] = 8;
/* 449 */     lastOcc[116] = 9;
/* 450 */     lastOcc[104] = 10;
/* 451 */     for (int i = 0; i < 9; i++)
/* 452 */       optoSft[i] = 10;
/* 453 */     optoSft[9] = 1;
/*     */   }
/*     */ 
/*     */   private class JarFileEntry extends JarEntry
/*     */   {
/*     */     JarFileEntry(ZipEntry arg2)
/*     */     {
/* 252 */       super();
/*     */     }
/*     */     public Attributes getAttributes() throws IOException {
/* 255 */       Manifest localManifest = JarFile.this.getManifest();
/* 256 */       if (localManifest != null) {
/* 257 */         return localManifest.getAttributes(getName());
/*     */       }
/* 259 */       return null;
/*     */     }
/*     */ 
/*     */     public Certificate[] getCertificates() {
/*     */       try {
/* 264 */         JarFile.this.maybeInstantiateVerifier();
/*     */       } catch (IOException localIOException) {
/* 266 */         throw new RuntimeException(localIOException);
/*     */       }
/* 268 */       if ((this.certs == null) && (JarFile.this.jv != null)) {
/* 269 */         this.certs = JarFile.this.jv.getCerts(JarFile.this, this);
/*     */       }
/* 271 */       return this.certs == null ? null : (Certificate[])this.certs.clone();
/*     */     }
/*     */     public CodeSigner[] getCodeSigners() {
/*     */       try {
/* 275 */         JarFile.this.maybeInstantiateVerifier();
/*     */       } catch (IOException localIOException) {
/* 277 */         throw new RuntimeException(localIOException);
/*     */       }
/* 279 */       if ((this.signers == null) && (JarFile.this.jv != null)) {
/* 280 */         this.signers = JarFile.this.jv.getCodeSigners(JarFile.this, this);
/*     */       }
/* 282 */       return this.signers == null ? null : (CodeSigner[])this.signers.clone();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.jar.JarFile
 * JD-Core Version:    0.6.2
 */