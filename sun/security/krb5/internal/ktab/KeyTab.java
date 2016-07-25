/*     */ package sun.security.krb5.internal.ktab;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.security.AccessController;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.Vector;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ import sun.security.krb5.Config;
/*     */ import sun.security.krb5.EncryptionKey;
/*     */ import sun.security.krb5.KrbException;
/*     */ import sun.security.krb5.PrincipalName;
/*     */ import sun.security.krb5.RealmException;
/*     */ import sun.security.krb5.internal.KerberosTime;
/*     */ import sun.security.krb5.internal.Krb5;
/*     */ import sun.security.krb5.internal.crypto.EType;
/*     */ 
/*     */ public class KeyTab
/*     */   implements KeyTabConstants
/*     */ {
/*  65 */   private static final boolean DEBUG = Krb5.DEBUG;
/*  66 */   private static String defaultTabName = null;
/*     */ 
/*  70 */   private static Map<String, KeyTab> map = new HashMap();
/*     */ 
/*  73 */   private boolean isMissing = false;
/*     */ 
/*  76 */   private boolean isValid = true;
/*     */   private final String tabName;
/*     */   private long lastModified;
/*  80 */   private int kt_vno = 1282;
/*     */ 
/*  82 */   private Vector<KeyTabEntry> entries = new Vector();
/*     */ 
/*     */   private KeyTab(String paramString)
/*     */   {
/*  92 */     this.tabName = paramString;
/*     */     try {
/*  94 */       this.lastModified = new File(this.tabName).lastModified();
/*  95 */       KeyTabInputStream localKeyTabInputStream = new KeyTabInputStream(new FileInputStream(paramString)); Object localObject1 = null;
/*     */       try {
/*  97 */         load(localKeyTabInputStream);
/*     */       }
/*     */       catch (Throwable localThrowable2)
/*     */       {
/*  95 */         localObject1 = localThrowable2; throw localThrowable2;
/*     */       }
/*     */       finally {
/*  98 */         if (localKeyTabInputStream != null) if (localObject1 != null) try { localKeyTabInputStream.close(); } catch (Throwable localThrowable3) { localObject1.addSuppressed(localThrowable3); } else localKeyTabInputStream.close();  
/*     */       }
/*     */     } catch (FileNotFoundException localFileNotFoundException) { this.entries.clear();
/* 101 */       this.isMissing = true;
/*     */     } catch (Exception localException) {
/* 103 */       this.entries.clear();
/* 104 */       this.isValid = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static synchronized KeyTab getInstance0(String paramString)
/*     */   {
/* 119 */     long l = new File(paramString).lastModified();
/* 120 */     KeyTab localKeyTab1 = (KeyTab)map.get(paramString);
/* 121 */     if ((localKeyTab1 != null) && (localKeyTab1.isValid()) && (localKeyTab1.lastModified == l)) {
/* 122 */       return localKeyTab1;
/*     */     }
/* 124 */     KeyTab localKeyTab2 = new KeyTab(paramString);
/* 125 */     if (localKeyTab2.isValid()) {
/* 126 */       map.put(paramString, localKeyTab2);
/* 127 */       return localKeyTab2;
/* 128 */     }if (localKeyTab1 != null) {
/* 129 */       return localKeyTab1;
/*     */     }
/* 131 */     return localKeyTab2;
/*     */   }
/*     */ 
/*     */   public static KeyTab getInstance(String paramString)
/*     */   {
/* 141 */     if (paramString == null) {
/* 142 */       return getInstance();
/*     */     }
/* 144 */     return getInstance0(normalize(paramString));
/*     */   }
/*     */ 
/*     */   public static KeyTab getInstance(File paramFile)
/*     */   {
/* 154 */     if (paramFile == null) {
/* 155 */       return getInstance();
/*     */     }
/* 157 */     return getInstance0(paramFile.getPath());
/*     */   }
/*     */ 
/*     */   public static KeyTab getInstance()
/*     */   {
/* 166 */     return getInstance(getDefaultTabName());
/*     */   }
/*     */ 
/*     */   public boolean isMissing() {
/* 170 */     return this.isMissing;
/*     */   }
/*     */ 
/*     */   public boolean isValid() {
/* 174 */     return this.isValid;
/*     */   }
/*     */ 
/*     */   private static String getDefaultTabName()
/*     */   {
/* 184 */     if (defaultTabName != null) {
/* 185 */       return defaultTabName;
/*     */     }
/* 187 */     String str1 = null;
/*     */     try {
/* 189 */       String str2 = Config.getInstance().getDefault("default_keytab_name", "libdefaults");
/*     */ 
/* 191 */       if (str2 != null) {
/* 192 */         StringTokenizer localStringTokenizer = new StringTokenizer(str2, " ");
/* 193 */         while (localStringTokenizer.hasMoreTokens()) {
/* 194 */           str1 = normalize(localStringTokenizer.nextToken());
/* 195 */           if (new File(str1).exists())
/* 196 */             break;
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (KrbException localKrbException) {
/* 201 */       str1 = null;
/*     */     }
/*     */ 
/* 204 */     if (str1 == null) {
/* 205 */       String str3 = (String)AccessController.doPrivileged(new GetPropertyAction("user.home"));
/*     */ 
/* 209 */       if (str3 == null) {
/* 210 */         str3 = (String)AccessController.doPrivileged(new GetPropertyAction("user.dir"));
/*     */       }
/*     */ 
/* 215 */       str1 = str3 + File.separator + "krb5.keytab";
/*     */     }
/* 217 */     defaultTabName = str1;
/* 218 */     return str1;
/*     */   }
/*     */ 
/*     */   public static String normalize(String paramString)
/*     */   {
/*     */     String str;
/* 231 */     if ((paramString.length() >= 5) && (paramString.substring(0, 5).equalsIgnoreCase("FILE:")))
/*     */     {
/* 233 */       str = paramString.substring(5);
/* 234 */     } else if ((paramString.length() >= 9) && (paramString.substring(0, 9).equalsIgnoreCase("ANY:FILE:")))
/*     */     {
/* 237 */       str = paramString.substring(9);
/* 238 */     } else if ((paramString.length() >= 7) && (paramString.substring(0, 7).equalsIgnoreCase("SRVTAB:")))
/*     */     {
/* 241 */       str = paramString.substring(7);
/*     */     }
/* 243 */     else str = paramString;
/* 244 */     return str;
/*     */   }
/*     */ 
/*     */   private void load(KeyTabInputStream paramKeyTabInputStream)
/*     */     throws IOException, RealmException
/*     */   {
/* 250 */     this.entries.clear();
/* 251 */     this.kt_vno = paramKeyTabInputStream.readVersion();
/* 252 */     if (this.kt_vno == 1281) {
/* 253 */       paramKeyTabInputStream.setNativeByteOrder();
/*     */     }
/* 255 */     int i = 0;
/*     */ 
/* 257 */     while (paramKeyTabInputStream.available() > 0) {
/* 258 */       i = paramKeyTabInputStream.readEntryLength();
/* 259 */       KeyTabEntry localKeyTabEntry = paramKeyTabInputStream.readEntry(i, this.kt_vno);
/* 260 */       if (DEBUG) {
/* 261 */         System.out.println(">>> KeyTab: load() entry length: " + i + "; type: " + (localKeyTabEntry != null ? localKeyTabEntry.keyType : 0));
/*     */       }
/*     */ 
/* 265 */       if (localKeyTabEntry != null)
/* 266 */         this.entries.addElement(localKeyTabEntry);
/*     */     }
/*     */   }
/*     */ 
/*     */   public EncryptionKey[] readServiceKeys(PrincipalName paramPrincipalName)
/*     */   {
/* 280 */     int i = this.entries.size();
/* 281 */     ArrayList localArrayList = new ArrayList(i);
/* 282 */     for (int j = i - 1; j >= 0; j--) {
/* 283 */       KeyTabEntry localKeyTabEntry = (KeyTabEntry)this.entries.elementAt(j);
/* 284 */       if (localKeyTabEntry.service.match(paramPrincipalName)) {
/* 285 */         if (EType.isSupported(localKeyTabEntry.keyType)) {
/* 286 */           EncryptionKey localEncryptionKey = new EncryptionKey(localKeyTabEntry.keyblock, localKeyTabEntry.keyType, new Integer(localKeyTabEntry.keyVersion));
/*     */ 
/* 289 */           localArrayList.add(localEncryptionKey);
/* 290 */           if (DEBUG) {
/* 291 */             System.out.println("Added key: " + localKeyTabEntry.keyType + "version: " + localKeyTabEntry.keyVersion);
/*     */           }
/*     */         }
/* 294 */         else if (DEBUG) {
/* 295 */           System.out.println("Found unsupported keytype (" + localKeyTabEntry.keyType + ") for " + paramPrincipalName);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 300 */     i = localArrayList.size();
/* 301 */     EncryptionKey[] arrayOfEncryptionKey = (EncryptionKey[])localArrayList.toArray(new EncryptionKey[i]);
/*     */ 
/* 304 */     if (DEBUG) {
/* 305 */       System.out.println("Ordering keys wrt default_tkt_enctypes list");
/*     */     }
/*     */ 
/* 308 */     final int[] arrayOfInt = EType.getDefaults("default_tkt_enctypes");
/*     */ 
/* 313 */     Arrays.sort(arrayOfEncryptionKey, new Comparator()
/*     */     {
/*     */       public int compare(EncryptionKey paramAnonymousEncryptionKey1, EncryptionKey paramAnonymousEncryptionKey2) {
/* 316 */         if (arrayOfInt != null) {
/* 317 */           int i = paramAnonymousEncryptionKey1.getEType();
/* 318 */           int j = paramAnonymousEncryptionKey2.getEType();
/* 319 */           if (i != j) {
/* 320 */             for (int k = 0; k < arrayOfInt.length; k++) {
/* 321 */               if (arrayOfInt[k] == i)
/* 322 */                 return -1;
/* 323 */               if (arrayOfInt[k] == j) {
/* 324 */                 return 1;
/*     */               }
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 332 */         return paramAnonymousEncryptionKey2.getKeyVersionNumber().intValue() - paramAnonymousEncryptionKey1.getKeyVersionNumber().intValue();
/*     */       }
/*     */     });
/* 337 */     return arrayOfEncryptionKey;
/*     */   }
/*     */ 
/*     */   public boolean findServiceEntry(PrincipalName paramPrincipalName)
/*     */   {
/* 351 */     for (int i = 0; i < this.entries.size(); i++) {
/* 352 */       KeyTabEntry localKeyTabEntry = (KeyTabEntry)this.entries.elementAt(i);
/* 353 */       if (localKeyTabEntry.service.match(paramPrincipalName)) {
/* 354 */         if (EType.isSupported(localKeyTabEntry.keyType))
/* 355 */           return true;
/* 356 */         if (DEBUG) {
/* 357 */           System.out.println("Found unsupported keytype (" + localKeyTabEntry.keyType + ") for " + paramPrincipalName);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 362 */     return false;
/*     */   }
/*     */ 
/*     */   public String tabName() {
/* 366 */     return this.tabName;
/*     */   }
/*     */ 
/*     */   public void addEntry(PrincipalName paramPrincipalName, char[] paramArrayOfChar, int paramInt, boolean paramBoolean)
/*     */     throws KrbException
/*     */   {
/* 383 */     EncryptionKey[] arrayOfEncryptionKey = EncryptionKey.acquireSecretKeys(paramArrayOfChar, paramPrincipalName.getSalt());
/*     */ 
/* 389 */     int i = 0;
/* 390 */     for (int j = this.entries.size() - 1; j >= 0; j--) {
/* 391 */       KeyTabEntry localKeyTabEntry1 = (KeyTabEntry)this.entries.get(j);
/* 392 */       if (localKeyTabEntry1.service.match(paramPrincipalName)) {
/* 393 */         if (localKeyTabEntry1.keyVersion > i) {
/* 394 */           i = localKeyTabEntry1.keyVersion;
/*     */         }
/* 396 */         if ((!paramBoolean) || (localKeyTabEntry1.keyVersion == paramInt)) {
/* 397 */           this.entries.removeElementAt(j);
/*     */         }
/*     */       }
/*     */     }
/* 401 */     if (paramInt == -1) {
/* 402 */       paramInt = i + 1;
/*     */     }
/*     */ 
/* 405 */     for (j = 0; (arrayOfEncryptionKey != null) && (j < arrayOfEncryptionKey.length); j++) {
/* 406 */       int k = arrayOfEncryptionKey[j].getEType();
/* 407 */       byte[] arrayOfByte = arrayOfEncryptionKey[j].getBytes();
/*     */ 
/* 409 */       KeyTabEntry localKeyTabEntry2 = new KeyTabEntry(paramPrincipalName, paramPrincipalName.getRealm(), new KerberosTime(System.currentTimeMillis()), paramInt, k, arrayOfByte);
/*     */ 
/* 413 */       this.entries.addElement(localKeyTabEntry2);
/*     */     }
/*     */   }
/*     */ 
/*     */   public KeyTabEntry[] getEntries()
/*     */   {
/* 422 */     KeyTabEntry[] arrayOfKeyTabEntry = new KeyTabEntry[this.entries.size()];
/* 423 */     for (int i = 0; i < arrayOfKeyTabEntry.length; i++) {
/* 424 */       arrayOfKeyTabEntry[i] = ((KeyTabEntry)this.entries.elementAt(i));
/*     */     }
/* 426 */     return arrayOfKeyTabEntry;
/*     */   }
/*     */ 
/*     */   public static synchronized KeyTab create()
/*     */     throws IOException, RealmException
/*     */   {
/* 434 */     String str = getDefaultTabName();
/* 435 */     return create(str);
/*     */   }
/*     */ 
/*     */   public static synchronized KeyTab create(String paramString)
/*     */     throws IOException, RealmException
/*     */   {
/* 444 */     KeyTabOutputStream localKeyTabOutputStream = new KeyTabOutputStream(new FileOutputStream(paramString)); Object localObject1 = null;
/*     */     try {
/* 446 */       localKeyTabOutputStream.writeVersion(1282);
/*     */     }
/*     */     catch (Throwable localThrowable2)
/*     */     {
/* 444 */       localObject1 = localThrowable2; throw localThrowable2;
/*     */     }
/*     */     finally {
/* 447 */       if (localKeyTabOutputStream != null) if (localObject1 != null) try { localKeyTabOutputStream.close(); } catch (Throwable localThrowable3) { localObject1.addSuppressed(localThrowable3); } else localKeyTabOutputStream.close(); 
/*     */     }
/* 448 */     return new KeyTab(paramString);
/*     */   }
/*     */ 
/*     */   public synchronized void save()
/*     */     throws IOException
/*     */   {
/* 455 */     KeyTabOutputStream localKeyTabOutputStream = new KeyTabOutputStream(new FileOutputStream(this.tabName)); Object localObject1 = null;
/*     */     try {
/* 457 */       localKeyTabOutputStream.writeVersion(this.kt_vno);
/* 458 */       for (int i = 0; i < this.entries.size(); i++)
/* 459 */         localKeyTabOutputStream.writeEntry((KeyTabEntry)this.entries.elementAt(i));
/*     */     }
/*     */     catch (Throwable localThrowable2)
/*     */     {
/* 455 */       localObject1 = localThrowable2; throw localThrowable2;
/*     */     }
/*     */     finally
/*     */     {
/* 461 */       if (localKeyTabOutputStream != null) if (localObject1 != null) try { localKeyTabOutputStream.close(); } catch (Throwable localThrowable3) { localObject1.addSuppressed(localThrowable3); } else localKeyTabOutputStream.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public int deleteEntries(PrincipalName paramPrincipalName, int paramInt1, int paramInt2)
/*     */   {
/* 472 */     int i = 0;
/*     */ 
/* 475 */     HashMap localHashMap = new HashMap();
/*     */     KeyTabEntry localKeyTabEntry;
/*     */     int k;
/* 477 */     for (int j = this.entries.size() - 1; j >= 0; j--) {
/* 478 */       localKeyTabEntry = (KeyTabEntry)this.entries.get(j);
/* 479 */       if ((paramPrincipalName.match(localKeyTabEntry.getService())) && (
/* 480 */         (paramInt1 == -1) || (localKeyTabEntry.keyType == paramInt1))) {
/* 481 */         if (paramInt2 == -2)
/*     */         {
/* 484 */           if (localHashMap.containsKey(Integer.valueOf(localKeyTabEntry.keyType))) {
/* 485 */             k = ((Integer)localHashMap.get(Integer.valueOf(localKeyTabEntry.keyType))).intValue();
/* 486 */             if (localKeyTabEntry.keyVersion > k)
/* 487 */               localHashMap.put(Integer.valueOf(localKeyTabEntry.keyType), Integer.valueOf(localKeyTabEntry.keyVersion));
/*     */           }
/*     */           else {
/* 490 */             localHashMap.put(Integer.valueOf(localKeyTabEntry.keyType), Integer.valueOf(localKeyTabEntry.keyVersion));
/*     */           }
/* 492 */         } else if ((paramInt2 == -1) || (localKeyTabEntry.keyVersion == paramInt2)) {
/* 493 */           this.entries.removeElementAt(j);
/* 494 */           i++;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 501 */     if (paramInt2 == -2) {
/* 502 */       for (j = this.entries.size() - 1; j >= 0; j--) {
/* 503 */         localKeyTabEntry = (KeyTabEntry)this.entries.get(j);
/* 504 */         if ((paramPrincipalName.match(localKeyTabEntry.getService())) && (
/* 505 */           (paramInt1 == -1) || (localKeyTabEntry.keyType == paramInt1))) {
/* 506 */           k = ((Integer)localHashMap.get(Integer.valueOf(localKeyTabEntry.keyType))).intValue();
/* 507 */           if (localKeyTabEntry.keyVersion != k) {
/* 508 */             this.entries.removeElementAt(j);
/* 509 */             i++;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 515 */     return i;
/*     */   }
/*     */ 
/*     */   public synchronized void createVersion(File paramFile)
/*     */     throws IOException
/*     */   {
/* 524 */     KeyTabOutputStream localKeyTabOutputStream = new KeyTabOutputStream(new FileOutputStream(paramFile)); Object localObject1 = null;
/*     */     try {
/* 526 */       localKeyTabOutputStream.write16(1282);
/*     */     }
/*     */     catch (Throwable localThrowable2)
/*     */     {
/* 524 */       localObject1 = localThrowable2; throw localThrowable2;
/*     */     }
/*     */     finally {
/* 527 */       if (localKeyTabOutputStream != null) if (localObject1 != null) try { localKeyTabOutputStream.close(); } catch (Throwable localThrowable3) { localObject1.addSuppressed(localThrowable3); } else localKeyTabOutputStream.close();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.ktab.KeyTab
 * JD-Core Version:    0.6.2
 */