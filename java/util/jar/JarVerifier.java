/*     */ package java.util.jar;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.security.CodeSigner;
/*     */ import java.security.CodeSource;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.SignatureException;
/*     */ import java.security.cert.CertPath;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import java.util.zip.ZipEntry;
/*     */ import sun.security.util.Debug;
/*     */ import sun.security.util.ManifestDigester;
/*     */ import sun.security.util.ManifestEntryVerifier;
/*     */ import sun.security.util.SignatureFileVerifier;
/*     */ 
/*     */ class JarVerifier
/*     */ {
/*  48 */   static final Debug debug = Debug.getInstance("jar");
/*     */   private Hashtable verifiedSigners;
/*     */   private Hashtable sigFileSigners;
/*     */   private Hashtable sigFileData;
/*     */   private ArrayList pendingBlocks;
/*     */   private ArrayList signerCache;
/*  69 */   private boolean parsingBlockOrSF = false;
/*     */ 
/*  72 */   private boolean parsingMeta = true;
/*     */ 
/*  75 */   private boolean anyToVerify = true;
/*     */   private ByteArrayOutputStream baos;
/*     */   private volatile ManifestDigester manDig;
/*  85 */   byte[] manifestRawBytes = null;
/*     */   boolean eagerValidation;
/*  91 */   private Object csdomain = new Object();
/*     */   private List manifestDigests;
/* 508 */   private Map urlToCodeSourceMap = new HashMap();
/* 509 */   private Map signerToCodeSource = new HashMap();
/*     */   private URL lastURL;
/*     */   private Map lastURLMap;
/* 550 */   private CodeSigner[] emptySigner = new CodeSigner[0];
/*     */   private Map signerMap;
/* 781 */   private Enumeration emptyEnumeration = new Enumeration()
/*     */   {
/*     */     public boolean hasMoreElements() {
/* 784 */       return false;
/*     */     }
/*     */ 
/*     */     public String nextElement() {
/* 788 */       throw new NoSuchElementException();
/*     */     }
/* 781 */   };
/*     */   private List jarCodeSigners;
/*     */ 
/*     */   public JarVerifier(byte[] paramArrayOfByte)
/*     */   {
/*  97 */     this.manifestRawBytes = paramArrayOfByte;
/*  98 */     this.sigFileSigners = new Hashtable();
/*  99 */     this.verifiedSigners = new Hashtable();
/* 100 */     this.sigFileData = new Hashtable(11);
/* 101 */     this.pendingBlocks = new ArrayList();
/* 102 */     this.baos = new ByteArrayOutputStream();
/* 103 */     this.manifestDigests = new ArrayList();
/*     */   }
/*     */ 
/*     */   public void beginEntry(JarEntry paramJarEntry, ManifestEntryVerifier paramManifestEntryVerifier)
/*     */     throws IOException
/*     */   {
/* 114 */     if (paramJarEntry == null) {
/* 115 */       return;
/*     */     }
/* 117 */     if (debug != null) {
/* 118 */       debug.println("beginEntry " + paramJarEntry.getName());
/*     */     }
/*     */ 
/* 121 */     String str1 = paramJarEntry.getName();
/*     */ 
/* 133 */     if (this.parsingMeta) {
/* 134 */       String str2 = str1.toUpperCase(Locale.ENGLISH);
/* 135 */       if ((str2.startsWith("META-INF/")) || (str2.startsWith("/META-INF/")))
/*     */       {
/* 138 */         if (paramJarEntry.isDirectory()) {
/* 139 */           paramManifestEntryVerifier.setEntry(null, paramJarEntry);
/* 140 */           return;
/*     */         }
/*     */ 
/* 143 */         if ((str2.equals("META-INF/MANIFEST.MF")) || (str2.equals("META-INF/INDEX.LIST")))
/*     */         {
/* 145 */           return;
/*     */         }
/*     */ 
/* 148 */         if (SignatureFileVerifier.isBlockOrSF(str2))
/*     */         {
/* 150 */           this.parsingBlockOrSF = true;
/* 151 */           this.baos.reset();
/* 152 */           paramManifestEntryVerifier.setEntry(null, paramJarEntry);
/* 153 */           return;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 162 */     if (this.parsingMeta) {
/* 163 */       doneWithMeta();
/*     */     }
/*     */ 
/* 166 */     if (paramJarEntry.isDirectory()) {
/* 167 */       paramManifestEntryVerifier.setEntry(null, paramJarEntry);
/* 168 */       return;
/*     */     }
/*     */ 
/* 173 */     if (str1.startsWith("./")) {
/* 174 */       str1 = str1.substring(2);
/*     */     }
/*     */ 
/* 178 */     if (str1.startsWith("/")) {
/* 179 */       str1 = str1.substring(1);
/*     */     }
/*     */ 
/* 183 */     if ((this.sigFileSigners.get(str1) != null) || (this.verifiedSigners.get(str1) != null))
/*     */     {
/* 185 */       paramManifestEntryVerifier.setEntry(str1, paramJarEntry);
/* 186 */       return;
/*     */     }
/*     */ 
/* 190 */     paramManifestEntryVerifier.setEntry(null, paramJarEntry);
/*     */   }
/*     */ 
/*     */   public void update(int paramInt, ManifestEntryVerifier paramManifestEntryVerifier)
/*     */     throws IOException
/*     */   {
/* 202 */     if (paramInt != -1) {
/* 203 */       if (this.parsingBlockOrSF)
/* 204 */         this.baos.write(paramInt);
/*     */       else
/* 206 */         paramManifestEntryVerifier.update((byte)paramInt);
/*     */     }
/*     */     else
/* 209 */       processEntry(paramManifestEntryVerifier);
/*     */   }
/*     */ 
/*     */   public void update(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3, ManifestEntryVerifier paramManifestEntryVerifier)
/*     */     throws IOException
/*     */   {
/* 221 */     if (paramInt1 != -1) {
/* 222 */       if (this.parsingBlockOrSF)
/* 223 */         this.baos.write(paramArrayOfByte, paramInt2, paramInt1);
/*     */       else
/* 225 */         paramManifestEntryVerifier.update(paramArrayOfByte, paramInt2, paramInt1);
/*     */     }
/*     */     else
/* 228 */       processEntry(paramManifestEntryVerifier);
/*     */   }
/*     */ 
/*     */   private void processEntry(ManifestEntryVerifier paramManifestEntryVerifier)
/*     */     throws IOException
/*     */   {
/*     */     Object localObject1;
/* 238 */     if (!this.parsingBlockOrSF) {
/* 239 */       localObject1 = paramManifestEntryVerifier.getEntry();
/* 240 */       if ((localObject1 != null) && (((JarEntry)localObject1).signers == null)) {
/* 241 */         ((JarEntry)localObject1).signers = paramManifestEntryVerifier.verify(this.verifiedSigners, this.sigFileSigners);
/* 242 */         ((JarEntry)localObject1).certs = mapSignersToCertArray(((JarEntry)localObject1).signers);
/*     */       }
/*     */     }
/*     */     else {
/*     */       try {
/* 247 */         this.parsingBlockOrSF = false;
/*     */ 
/* 249 */         if (debug != null) {
/* 250 */           debug.println("processEntry: processing block");
/*     */         }
/*     */ 
/* 253 */         localObject1 = paramManifestEntryVerifier.getEntry().getName().toUpperCase(Locale.ENGLISH);
/*     */         Object localObject2;
/* 256 */         if (((String)localObject1).endsWith(".SF")) {
/* 257 */           str = ((String)localObject1).substring(0, ((String)localObject1).length() - 3);
/* 258 */           byte[] arrayOfByte = this.baos.toByteArray();
/*     */ 
/* 260 */           this.sigFileData.put(str, arrayOfByte);
/*     */ 
/* 263 */           localObject2 = this.pendingBlocks.iterator();
/* 264 */           while (((Iterator)localObject2).hasNext()) {
/* 265 */             SignatureFileVerifier localSignatureFileVerifier = (SignatureFileVerifier)((Iterator)localObject2).next();
/*     */ 
/* 267 */             if (localSignatureFileVerifier.needSignatureFile(str)) {
/* 268 */               if (debug != null) {
/* 269 */                 debug.println("processEntry: processing pending block");
/*     */               }
/*     */ 
/* 273 */               localSignatureFileVerifier.setSignatureFile(arrayOfByte);
/* 274 */               localSignatureFileVerifier.process(this.sigFileSigners, this.manifestDigests);
/*     */             }
/*     */           }
/* 277 */           return;
/*     */         }
/*     */ 
/* 282 */         String str = ((String)localObject1).substring(0, ((String)localObject1).lastIndexOf("."));
/*     */ 
/* 284 */         if (this.signerCache == null) {
/* 285 */           this.signerCache = new ArrayList();
/*     */         }
/* 287 */         if (this.manDig == null) {
/* 288 */           synchronized (this.manifestRawBytes) {
/* 289 */             if (this.manDig == null) {
/* 290 */               this.manDig = new ManifestDigester(this.manifestRawBytes);
/* 291 */               this.manifestRawBytes = null;
/*     */             }
/*     */           }
/*     */         }
/*     */ 
/* 296 */         ??? = new SignatureFileVerifier(this.signerCache, this.manDig, (String)localObject1, this.baos.toByteArray());
/*     */ 
/* 300 */         if (((SignatureFileVerifier)???).needSignatureFileBytes())
/*     */         {
/* 302 */           localObject2 = (byte[])this.sigFileData.get(str);
/*     */ 
/* 304 */           if (localObject2 == null)
/*     */           {
/* 308 */             if (debug != null) {
/* 309 */               debug.println("adding pending block");
/*     */             }
/* 311 */             this.pendingBlocks.add(???);
/* 312 */             return;
/*     */           }
/* 314 */           ((SignatureFileVerifier)???).setSignatureFile((byte[])localObject2);
/*     */         }
/*     */ 
/* 317 */         ((SignatureFileVerifier)???).process(this.sigFileSigners, this.manifestDigests);
/*     */       }
/*     */       catch (IOException localIOException)
/*     */       {
/* 321 */         if (debug != null) debug.println("processEntry caught: " + localIOException); 
/*     */       }
/*     */       catch (SignatureException localSignatureException)
/*     */       {
/* 324 */         if (debug != null) debug.println("processEntry caught: " + localSignatureException); 
/*     */       }
/*     */       catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
/*     */       {
/* 327 */         if (debug != null) debug.println("processEntry caught: " + localNoSuchAlgorithmException); 
/*     */       }
/*     */       catch (CertificateException localCertificateException)
/*     */       {
/* 330 */         if (debug != null) debug.println("processEntry caught: " + localCertificateException);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public Certificate[] getCerts(String paramString)
/*     */   {
/* 343 */     return mapSignersToCertArray(getCodeSigners(paramString));
/*     */   }
/*     */ 
/*     */   public Certificate[] getCerts(JarFile paramJarFile, JarEntry paramJarEntry)
/*     */   {
/* 348 */     return mapSignersToCertArray(getCodeSigners(paramJarFile, paramJarEntry));
/*     */   }
/*     */ 
/*     */   public CodeSigner[] getCodeSigners(String paramString)
/*     */   {
/* 358 */     return (CodeSigner[])this.verifiedSigners.get(paramString);
/*     */   }
/*     */ 
/*     */   public CodeSigner[] getCodeSigners(JarFile paramJarFile, JarEntry paramJarEntry)
/*     */   {
/* 363 */     String str = paramJarEntry.getName();
/* 364 */     if ((this.eagerValidation) && (this.sigFileSigners.get(str) != null))
/*     */     {
/*     */       try
/*     */       {
/* 370 */         InputStream localInputStream = paramJarFile.getInputStream(paramJarEntry);
/* 371 */         byte[] arrayOfByte = new byte[1024];
/* 372 */         int i = arrayOfByte.length;
/* 373 */         while (i != -1) {
/* 374 */           i = localInputStream.read(arrayOfByte, 0, arrayOfByte.length);
/*     */         }
/* 376 */         localInputStream.close();
/*     */       } catch (IOException localIOException) {
/*     */       }
/*     */     }
/* 380 */     return getCodeSigners(str);
/*     */   }
/*     */ 
/*     */   private static Certificate[] mapSignersToCertArray(CodeSigner[] paramArrayOfCodeSigner)
/*     */   {
/* 390 */     if (paramArrayOfCodeSigner != null) {
/* 391 */       ArrayList localArrayList = new ArrayList();
/* 392 */       for (int i = 0; i < paramArrayOfCodeSigner.length; i++) {
/* 393 */         localArrayList.addAll(paramArrayOfCodeSigner[i].getSignerCertPath().getCertificates());
/*     */       }
/*     */ 
/* 398 */       return (Certificate[])localArrayList.toArray(new Certificate[localArrayList.size()]);
/*     */     }
/*     */ 
/* 402 */     return null;
/*     */   }
/*     */ 
/*     */   boolean nothingToVerify()
/*     */   {
/* 412 */     return !this.anyToVerify;
/*     */   }
/*     */ 
/*     */   void doneWithMeta()
/*     */   {
/* 423 */     this.parsingMeta = false;
/* 424 */     this.anyToVerify = (!this.sigFileSigners.isEmpty());
/* 425 */     this.baos = null;
/* 426 */     this.sigFileData = null;
/* 427 */     this.pendingBlocks = null;
/* 428 */     this.signerCache = null;
/* 429 */     this.manDig = null;
/*     */ 
/* 432 */     if (this.sigFileSigners.containsKey("META-INF/MANIFEST.MF"))
/* 433 */       this.verifiedSigners.put("META-INF/MANIFEST.MF", this.sigFileSigners.remove("META-INF/MANIFEST.MF"));
/*     */   }
/*     */ 
/*     */   private synchronized CodeSource mapSignersToCodeSource(URL paramURL, CodeSigner[] paramArrayOfCodeSigner)
/*     */   {
/*     */     Object localObject1;
/* 520 */     if (paramURL == this.lastURL) {
/* 521 */       localObject1 = this.lastURLMap;
/*     */     } else {
/* 523 */       localObject1 = (Map)this.urlToCodeSourceMap.get(paramURL);
/* 524 */       if (localObject1 == null) {
/* 525 */         localObject1 = new HashMap();
/* 526 */         this.urlToCodeSourceMap.put(paramURL, localObject1);
/*     */       }
/* 528 */       this.lastURLMap = ((Map)localObject1);
/* 529 */       this.lastURL = paramURL;
/*     */     }
/* 531 */     Object localObject2 = (CodeSource)((Map)localObject1).get(paramArrayOfCodeSigner);
/* 532 */     if (localObject2 == null) {
/* 533 */       localObject2 = new VerifierCodeSource(this.csdomain, paramURL, paramArrayOfCodeSigner);
/* 534 */       this.signerToCodeSource.put(paramArrayOfCodeSigner, localObject2);
/*     */     }
/* 536 */     return localObject2;
/*     */   }
/*     */ 
/*     */   private CodeSource[] mapSignersToCodeSources(URL paramURL, List paramList, boolean paramBoolean) {
/* 540 */     ArrayList localArrayList = new ArrayList();
/*     */ 
/* 542 */     for (int i = 0; i < paramList.size(); i++) {
/* 543 */       localArrayList.add(mapSignersToCodeSource(paramURL, (CodeSigner[])paramList.get(i)));
/*     */     }
/* 545 */     if (paramBoolean) {
/* 546 */       localArrayList.add(mapSignersToCodeSource(paramURL, null));
/*     */     }
/* 548 */     return (CodeSource[])localArrayList.toArray(new CodeSource[localArrayList.size()]);
/*     */   }
/*     */ 
/*     */   private CodeSigner[] findMatchingSigners(CodeSource paramCodeSource)
/*     */   {
/* 556 */     if ((paramCodeSource instanceof VerifierCodeSource)) {
/* 557 */       localObject = (VerifierCodeSource)paramCodeSource;
/* 558 */       if (((VerifierCodeSource)localObject).isSameDomain(this.csdomain)) {
/* 559 */         return ((VerifierCodeSource)paramCodeSource).getPrivateSigners();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 567 */     Object localObject = mapSignersToCodeSources(paramCodeSource.getLocation(), getJarCodeSigners(), true);
/* 568 */     ArrayList localArrayList = new ArrayList();
/* 569 */     for (int i = 0; i < localObject.length; i++) {
/* 570 */       localArrayList.add(localObject[i]);
/*     */     }
/* 572 */     i = localArrayList.indexOf(paramCodeSource);
/* 573 */     if (i != -1)
/*     */     {
/* 575 */       CodeSigner[] arrayOfCodeSigner = ((VerifierCodeSource)localArrayList.get(i)).getPrivateSigners();
/* 576 */       if (arrayOfCodeSigner == null) {
/* 577 */         arrayOfCodeSigner = this.emptySigner;
/*     */       }
/* 579 */       return arrayOfCodeSigner;
/*     */     }
/* 581 */     return null;
/*     */   }
/*     */ 
/*     */   private synchronized Map signerMap()
/*     */   {
/* 659 */     if (this.signerMap == null)
/*     */     {
/* 665 */       this.signerMap = new HashMap(this.verifiedSigners.size() + this.sigFileSigners.size());
/* 666 */       this.signerMap.putAll(this.verifiedSigners);
/* 667 */       this.signerMap.putAll(this.sigFileSigners);
/*     */     }
/* 669 */     return this.signerMap;
/*     */   }
/*     */ 
/*     */   public synchronized Enumeration<String> entryNames(JarFile paramJarFile, CodeSource[] paramArrayOfCodeSource) {
/* 673 */     Map localMap = signerMap();
/* 674 */     final Iterator localIterator = localMap.entrySet().iterator();
/* 675 */     int i = 0;
/*     */ 
/* 681 */     ArrayList localArrayList1 = new ArrayList(paramArrayOfCodeSource.length);
/* 682 */     for (int j = 0; j < paramArrayOfCodeSource.length; j++) {
/* 683 */       localObject = findMatchingSigners(paramArrayOfCodeSource[j]);
/* 684 */       if (localObject != null) {
/* 685 */         if (localObject.length > 0)
/* 686 */           localArrayList1.add(localObject);
/*     */         else
/* 688 */           i = 1;
/*     */       }
/*     */       else {
/* 691 */         i = 1;
/*     */       }
/*     */     }
/*     */ 
/* 695 */     final ArrayList localArrayList2 = localArrayList1;
/* 696 */     Object localObject = i != 0 ? unsignedEntryNames(paramJarFile) : this.emptyEnumeration;
/*     */ 
/* 698 */     return new Enumeration()
/*     */     {
/*     */       String name;
/*     */ 
/*     */       public boolean hasMoreElements() {
/* 703 */         if (this.name != null) {
/* 704 */           return true;
/*     */         }
/*     */ 
/* 707 */         while (localIterator.hasNext()) {
/* 708 */           Map.Entry localEntry = (Map.Entry)localIterator.next();
/* 709 */           if (localArrayList2.contains((CodeSigner[])localEntry.getValue())) {
/* 710 */             this.name = ((String)localEntry.getKey());
/* 711 */             return true;
/*     */           }
/*     */         }
/* 714 */         if (this.val$enum2.hasMoreElements()) {
/* 715 */           this.name = ((String)this.val$enum2.nextElement());
/* 716 */           return true;
/*     */         }
/* 718 */         return false;
/*     */       }
/*     */ 
/*     */       public String nextElement() {
/* 722 */         if (hasMoreElements()) {
/* 723 */           String str = this.name;
/* 724 */           this.name = null;
/* 725 */           return str;
/*     */         }
/* 727 */         throw new NoSuchElementException();
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public Enumeration<JarEntry> entries2(final JarFile paramJarFile, Enumeration paramEnumeration)
/*     */   {
/* 737 */     final HashMap localHashMap = new HashMap();
/* 738 */     localHashMap.putAll(signerMap());
/* 739 */     final Enumeration localEnumeration = paramEnumeration;
/* 740 */     return new Enumeration() {
/* 742 */       Enumeration signers = null;
/*     */       JarEntry entry;
/*     */ 
/*     */       public boolean hasMoreElements() {
/* 746 */         if (this.entry != null)
/* 747 */           return true;
/*     */         Object localObject;
/* 749 */         while (localEnumeration.hasMoreElements()) {
/* 750 */           localObject = (ZipEntry)localEnumeration.nextElement();
/* 751 */           if (!JarVerifier.isSigningRelated(((ZipEntry)localObject).getName()))
/*     */           {
/* 754 */             this.entry = paramJarFile.newEntry((ZipEntry)localObject);
/* 755 */             return true;
/*     */           }
/*     */         }
/* 757 */         if (this.signers == null) {
/* 758 */           this.signers = Collections.enumeration(localHashMap.keySet());
/*     */         }
/* 760 */         if (this.signers.hasMoreElements()) {
/* 761 */           localObject = (String)this.signers.nextElement();
/* 762 */           this.entry = paramJarFile.newEntry(new ZipEntry((String)localObject));
/* 763 */           return true;
/*     */         }
/*     */ 
/* 767 */         return false;
/*     */       }
/*     */ 
/*     */       public JarEntry nextElement() {
/* 771 */         if (hasMoreElements()) {
/* 772 */           JarEntry localJarEntry = this.entry;
/* 773 */           localHashMap.remove(localJarEntry.getName());
/* 774 */           this.entry = null;
/* 775 */           return localJarEntry;
/*     */         }
/* 777 */         throw new NoSuchElementException();
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   static boolean isSigningRelated(String paramString)
/*     */   {
/* 794 */     return SignatureFileVerifier.isSigningRelated(paramString);
/*     */   }
/*     */ 
/*     */   private Enumeration<String> unsignedEntryNames(JarFile paramJarFile) {
/* 798 */     final Map localMap = signerMap();
/* 799 */     final Enumeration localEnumeration = paramJarFile.entries();
/* 800 */     return new Enumeration()
/*     */     {
/*     */       String name;
/*     */ 
/*     */       public boolean hasMoreElements()
/*     */       {
/* 809 */         if (this.name != null) {
/* 810 */           return true;
/*     */         }
/* 812 */         while (localEnumeration.hasMoreElements())
/*     */         {
/* 814 */           ZipEntry localZipEntry = (ZipEntry)localEnumeration.nextElement();
/* 815 */           String str = localZipEntry.getName();
/* 816 */           if ((!localZipEntry.isDirectory()) && (!JarVerifier.isSigningRelated(str)))
/*     */           {
/* 819 */             if (localMap.get(str) == null) {
/* 820 */               this.name = str;
/* 821 */               return true;
/*     */             }
/*     */           }
/*     */         }
/* 824 */         return false;
/*     */       }
/*     */ 
/*     */       public String nextElement() {
/* 828 */         if (hasMoreElements()) {
/* 829 */           String str = this.name;
/* 830 */           this.name = null;
/* 831 */           return str;
/*     */         }
/* 833 */         throw new NoSuchElementException();
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   private synchronized List getJarCodeSigners()
/*     */   {
/* 841 */     if (this.jarCodeSigners == null) {
/* 842 */       HashSet localHashSet = new HashSet();
/* 843 */       localHashSet.addAll(signerMap().values());
/* 844 */       this.jarCodeSigners = new ArrayList();
/* 845 */       this.jarCodeSigners.addAll(localHashSet);
/*     */     }
/* 847 */     return this.jarCodeSigners;
/*     */   }
/*     */ 
/*     */   public synchronized CodeSource[] getCodeSources(JarFile paramJarFile, URL paramURL) {
/* 851 */     boolean bool = unsignedEntryNames(paramJarFile).hasMoreElements();
/*     */ 
/* 853 */     return mapSignersToCodeSources(paramURL, getJarCodeSigners(), bool);
/*     */   }
/*     */ 
/*     */   public CodeSource getCodeSource(URL paramURL, String paramString)
/*     */   {
/* 859 */     CodeSigner[] arrayOfCodeSigner = (CodeSigner[])signerMap().get(paramString);
/* 860 */     return mapSignersToCodeSource(paramURL, arrayOfCodeSigner);
/*     */   }
/*     */ 
/*     */   public CodeSource getCodeSource(URL paramURL, JarFile paramJarFile, JarEntry paramJarEntry)
/*     */   {
/* 866 */     return mapSignersToCodeSource(paramURL, getCodeSigners(paramJarFile, paramJarEntry));
/*     */   }
/*     */ 
/*     */   public void setEagerValidation(boolean paramBoolean) {
/* 870 */     this.eagerValidation = paramBoolean;
/*     */   }
/*     */ 
/*     */   public synchronized List getManifestDigests() {
/* 874 */     return Collections.unmodifiableList(this.manifestDigests);
/*     */   }
/*     */ 
/*     */   static CodeSource getUnsignedCS(URL paramURL) {
/* 878 */     return new VerifierCodeSource(null, paramURL, (Certificate[])null);
/*     */   }
/*     */ 
/*     */   private static class VerifierCodeSource extends CodeSource
/*     */   {
/*     */     URL vlocation;
/*     */     CodeSigner[] vsigners;
/*     */     Certificate[] vcerts;
/*     */     Object csdomain;
/*     */ 
/*     */     VerifierCodeSource(Object paramObject, URL paramURL, CodeSigner[] paramArrayOfCodeSigner)
/*     */     {
/* 596 */       super(paramArrayOfCodeSigner);
/* 597 */       this.csdomain = paramObject;
/* 598 */       this.vlocation = paramURL;
/* 599 */       this.vsigners = paramArrayOfCodeSigner;
/*     */     }
/*     */ 
/*     */     VerifierCodeSource(Object paramObject, URL paramURL, Certificate[] paramArrayOfCertificate) {
/* 603 */       super(paramArrayOfCertificate);
/* 604 */       this.csdomain = paramObject;
/* 605 */       this.vlocation = paramURL;
/* 606 */       this.vcerts = paramArrayOfCertificate;
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject)
/*     */     {
/* 616 */       if (paramObject == this) {
/* 617 */         return true;
/*     */       }
/* 619 */       if ((paramObject instanceof VerifierCodeSource)) {
/* 620 */         VerifierCodeSource localVerifierCodeSource = (VerifierCodeSource)paramObject;
/*     */ 
/* 627 */         if (isSameDomain(localVerifierCodeSource.csdomain)) {
/* 628 */           if ((localVerifierCodeSource.vsigners != this.vsigners) || (localVerifierCodeSource.vcerts != this.vcerts))
/*     */           {
/* 630 */             return false;
/*     */           }
/* 632 */           if (localVerifierCodeSource.vlocation != null)
/* 633 */             return localVerifierCodeSource.vlocation.equals(this.vlocation);
/* 634 */           if (this.vlocation != null) {
/* 635 */             return this.vlocation.equals(localVerifierCodeSource.vlocation);
/*     */           }
/* 637 */           return true;
/*     */         }
/*     */       }
/*     */ 
/* 641 */       return super.equals(paramObject);
/*     */     }
/*     */ 
/*     */     boolean isSameDomain(Object paramObject) {
/* 645 */       return this.csdomain == paramObject;
/*     */     }
/*     */ 
/*     */     private CodeSigner[] getPrivateSigners() {
/* 649 */       return this.vsigners;
/*     */     }
/*     */ 
/*     */     private Certificate[] getPrivateCertificates() {
/* 653 */       return this.vcerts;
/*     */     }
/*     */   }
/*     */ 
/*     */   static class VerifierStream extends InputStream
/*     */   {
/*     */     private InputStream is;
/*     */     private JarVerifier jv;
/*     */     private ManifestEntryVerifier mev;
/*     */     private long numLeft;
/*     */ 
/*     */     VerifierStream(Manifest paramManifest, JarEntry paramJarEntry, InputStream paramInputStream, JarVerifier paramJarVerifier)
/*     */       throws IOException
/*     */     {
/* 450 */       this.is = paramInputStream;
/* 451 */       this.jv = paramJarVerifier;
/* 452 */       this.mev = new ManifestEntryVerifier(paramManifest);
/* 453 */       this.jv.beginEntry(paramJarEntry, this.mev);
/* 454 */       this.numLeft = paramJarEntry.getSize();
/* 455 */       if (this.numLeft == 0L)
/* 456 */         this.jv.update(-1, this.mev);
/*     */     }
/*     */ 
/*     */     public int read() throws IOException
/*     */     {
/* 461 */       if (this.numLeft > 0L) {
/* 462 */         int i = this.is.read();
/* 463 */         this.jv.update(i, this.mev);
/* 464 */         this.numLeft -= 1L;
/* 465 */         if (this.numLeft == 0L)
/* 466 */           this.jv.update(-1, this.mev);
/* 467 */         return i;
/*     */       }
/* 469 */       return -1;
/*     */     }
/*     */ 
/*     */     public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException
/*     */     {
/* 474 */       if ((this.numLeft > 0L) && (this.numLeft < paramInt2)) {
/* 475 */         paramInt2 = (int)this.numLeft;
/*     */       }
/*     */ 
/* 478 */       if (this.numLeft > 0L) {
/* 479 */         int i = this.is.read(paramArrayOfByte, paramInt1, paramInt2);
/* 480 */         this.jv.update(i, paramArrayOfByte, paramInt1, paramInt2, this.mev);
/* 481 */         this.numLeft -= i;
/* 482 */         if (this.numLeft == 0L)
/* 483 */           this.jv.update(-1, paramArrayOfByte, paramInt1, paramInt2, this.mev);
/* 484 */         return i;
/*     */       }
/* 486 */       return -1;
/*     */     }
/*     */ 
/*     */     public void close()
/*     */       throws IOException
/*     */     {
/* 493 */       if (this.is != null)
/* 494 */         this.is.close();
/* 495 */       this.is = null;
/* 496 */       this.mev = null;
/* 497 */       this.jv = null;
/*     */     }
/*     */ 
/*     */     public int available() throws IOException {
/* 501 */       return this.is.available();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.jar.JarVerifier
 * JD-Core Version:    0.6.2
 */