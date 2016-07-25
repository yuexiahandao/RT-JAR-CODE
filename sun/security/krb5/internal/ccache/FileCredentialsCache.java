/*     */ package sun.security.krb5.internal.ccache;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.Vector;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ import sun.security.krb5.Asn1Exception;
/*     */ import sun.security.krb5.KrbException;
/*     */ import sun.security.krb5.PrincipalName;
/*     */ import sun.security.krb5.Realm;
/*     */ import sun.security.krb5.internal.KerberosTime;
/*     */ import sun.security.krb5.internal.Krb5;
/*     */ import sun.security.krb5.internal.LoginOptions;
/*     */ import sun.security.krb5.internal.TicketFlags;
/*     */ 
/*     */ public class FileCredentialsCache extends CredentialsCache
/*     */   implements FileCCacheConstants
/*     */ {
/*     */   public int version;
/*     */   public Tag tag;
/*     */   public PrincipalName primaryPrincipal;
/*     */   public Realm primaryRealm;
/*     */   private Vector<Credentials> credentialsList;
/*     */   private static String dir;
/*  65 */   private static boolean DEBUG = Krb5.DEBUG;
/*     */ 
/*     */   public static synchronized FileCredentialsCache acquireInstance(PrincipalName paramPrincipalName, String paramString)
/*     */   {
/*     */     try {
/*  70 */       FileCredentialsCache localFileCredentialsCache = new FileCredentialsCache();
/*  71 */       if (paramString == null)
/*  72 */         cacheName = getDefaultCacheName();
/*     */       else {
/*  74 */         cacheName = checkValidation(paramString);
/*     */       }
/*  76 */       if ((cacheName == null) || (!new File(cacheName).exists()))
/*     */       {
/*  78 */         return null;
/*     */       }
/*  80 */       if (paramPrincipalName != null) {
/*  81 */         localFileCredentialsCache.primaryPrincipal = paramPrincipalName;
/*  82 */         localFileCredentialsCache.primaryRealm = paramPrincipalName.getRealm();
/*     */       }
/*  84 */       localFileCredentialsCache.load(cacheName);
/*  85 */       return localFileCredentialsCache;
/*     */     }
/*     */     catch (IOException localIOException) {
/*  88 */       if (DEBUG)
/*  89 */         localIOException.printStackTrace();
/*     */     }
/*     */     catch (KrbException localKrbException)
/*     */     {
/*  93 */       if (DEBUG) {
/*  94 */         localKrbException.printStackTrace();
/*     */       }
/*     */     }
/*  97 */     return null;
/*     */   }
/*     */ 
/*     */   public static FileCredentialsCache acquireInstance() {
/* 101 */     return acquireInstance(null, null);
/*     */   }
/*     */ 
/*     */   static synchronized FileCredentialsCache New(PrincipalName paramPrincipalName, String paramString)
/*     */   {
/*     */     try {
/* 107 */       FileCredentialsCache localFileCredentialsCache = new FileCredentialsCache();
/* 108 */       cacheName = checkValidation(paramString);
/* 109 */       if (cacheName == null)
/*     */       {
/* 111 */         return null;
/*     */       }
/* 113 */       localFileCredentialsCache.init(paramPrincipalName, cacheName);
/* 114 */       return localFileCredentialsCache;
/*     */     }
/*     */     catch (IOException localIOException) {
/*     */     }
/*     */     catch (KrbException localKrbException) {
/*     */     }
/* 120 */     return null;
/*     */   }
/*     */ 
/*     */   static synchronized FileCredentialsCache New(PrincipalName paramPrincipalName) {
/*     */     try {
/* 125 */       FileCredentialsCache localFileCredentialsCache = new FileCredentialsCache();
/* 126 */       cacheName = getDefaultCacheName();
/* 127 */       localFileCredentialsCache.init(paramPrincipalName, cacheName);
/* 128 */       return localFileCredentialsCache;
/*     */     }
/*     */     catch (IOException localIOException) {
/* 131 */       if (DEBUG)
/* 132 */         localIOException.printStackTrace();
/*     */     }
/*     */     catch (KrbException localKrbException) {
/* 135 */       if (DEBUG) {
/* 136 */         localKrbException.printStackTrace();
/*     */       }
/*     */     }
/*     */ 
/* 140 */     return null;
/*     */   }
/*     */ 
/*     */   boolean exists(String paramString)
/*     */   {
/* 147 */     File localFile = new File(paramString);
/* 148 */     if (localFile.exists())
/* 149 */       return true;
/* 150 */     return false;
/*     */   }
/*     */ 
/*     */   synchronized void init(PrincipalName paramPrincipalName, String paramString) throws IOException, KrbException
/*     */   {
/* 155 */     this.primaryPrincipal = paramPrincipalName;
/* 156 */     this.primaryRealm = paramPrincipalName.getRealm();
/* 157 */     FileOutputStream localFileOutputStream = new FileOutputStream(paramString); Object localObject1 = null;
/*     */     try { CCacheOutputStream localCCacheOutputStream = new CCacheOutputStream(localFileOutputStream);
/*     */ 
/* 157 */       Object localObject2 = null;
/*     */       try {
/* 159 */         this.version = 1283;
/* 160 */         localCCacheOutputStream.writeHeader(this.primaryPrincipal, this.version);
/*     */       }
/*     */       catch (Throwable localThrowable4)
/*     */       {
/* 157 */         localObject2 = localThrowable4; throw localThrowable4; } finally {  } } catch (Throwable localThrowable2) { localObject1 = localThrowable2; throw localThrowable2;
/*     */     }
/*     */     finally
/*     */     {
/* 161 */       if (localFileOutputStream != null) if (localObject1 != null) try { localFileOutputStream.close(); } catch (Throwable localThrowable6) { localObject1.addSuppressed(localThrowable6); } else localFileOutputStream.close(); 
/*     */     }
/* 162 */     load(paramString);
/*     */   }
/*     */ 
/*     */   synchronized void load(String paramString) throws IOException, KrbException
/*     */   {
/* 167 */     FileInputStream localFileInputStream = new FileInputStream(paramString); Object localObject1 = null;
/*     */     try { CCacheInputStream localCCacheInputStream = new CCacheInputStream(localFileInputStream);
/*     */ 
/* 167 */       Object localObject2 = null;
/*     */       try {
/* 169 */         this.version = localCCacheInputStream.readVersion();
/* 170 */         if (this.version == 1284) {
/* 171 */           this.tag = localCCacheInputStream.readTag();
/*     */         } else {
/* 173 */           this.tag = null;
/* 174 */           if ((this.version == 1281) || (this.version == 1282)) {
/* 175 */             localCCacheInputStream.setNativeByteOrder();
/*     */           }
/*     */         }
/* 178 */         PrincipalName localPrincipalName = localCCacheInputStream.readPrincipal(this.version);
/*     */ 
/* 180 */         if (this.primaryPrincipal != null) {
/* 181 */           if (!this.primaryPrincipal.match(localPrincipalName))
/* 182 */             throw new IOException("Primary principals don't match.");
/*     */         }
/*     */         else
/* 185 */           this.primaryPrincipal = localPrincipalName;
/* 186 */         this.primaryRealm = this.primaryPrincipal.getRealm();
/* 187 */         this.credentialsList = new Vector();
/* 188 */         while (localCCacheInputStream.available() > 0) {
/* 189 */           Credentials localCredentials = localCCacheInputStream.readCred(this.version);
/* 190 */           if (localCredentials != null)
/* 191 */             this.credentialsList.addElement(localCredentials);
/*     */         }
/*     */       }
/*     */       catch (Throwable localThrowable4)
/*     */       {
/* 167 */         localObject2 = localThrowable4; throw localThrowable4; } finally {  } } catch (Throwable localThrowable2) { localObject1 = localThrowable2; throw localThrowable2;
/*     */     }
/*     */     finally
/*     */     {
/* 194 */       if (localFileInputStream != null) if (localObject1 != null) try { localFileInputStream.close(); } catch (Throwable localThrowable6) { localObject1.addSuppressed(localThrowable6); } else localFileInputStream.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void update(Credentials paramCredentials)
/*     */   {
/* 206 */     if (this.credentialsList != null)
/* 207 */       if (this.credentialsList.isEmpty()) {
/* 208 */         this.credentialsList.addElement(paramCredentials);
/*     */       } else {
/* 210 */         Credentials localCredentials = null;
/* 211 */         int i = 0;
/*     */ 
/* 213 */         for (int j = 0; j < this.credentialsList.size(); j++) {
/* 214 */           localCredentials = (Credentials)this.credentialsList.elementAt(j);
/* 215 */           if ((match(paramCredentials.sname.getNameStrings(), localCredentials.sname.getNameStrings())) && (paramCredentials.sname.getRealmString().equalsIgnoreCase(localCredentials.sname.getRealmString())))
/*     */           {
/* 219 */             i = 1;
/* 220 */             if (paramCredentials.endtime.getTime() >= localCredentials.endtime.getTime()) {
/* 221 */               if (DEBUG) {
/* 222 */                 System.out.println(" >>> FileCredentialsCache Ticket matched, overwrite the old one.");
/*     */               }
/*     */ 
/* 226 */               this.credentialsList.removeElementAt(j);
/* 227 */               this.credentialsList.addElement(paramCredentials);
/*     */             }
/*     */           }
/*     */         }
/* 231 */         if (i == 0) {
/* 232 */           if (DEBUG) {
/* 233 */             System.out.println(" >>> FileCredentialsCache Ticket not exactly matched, add new one into cache.");
/*     */           }
/*     */ 
/* 238 */           this.credentialsList.addElement(paramCredentials);
/*     */         }
/*     */       }
/*     */   }
/*     */ 
/*     */   public synchronized PrincipalName getPrimaryPrincipal()
/*     */   {
/* 245 */     return this.primaryPrincipal;
/*     */   }
/*     */ 
/*     */   public synchronized void save()
/*     */     throws IOException, Asn1Exception
/*     */   {
/* 253 */     FileOutputStream localFileOutputStream = new FileOutputStream(cacheName); Object localObject1 = null;
/*     */     try { CCacheOutputStream localCCacheOutputStream = new CCacheOutputStream(localFileOutputStream);
/*     */ 
/* 253 */       Object localObject2 = null;
/*     */       try {
/* 255 */         localCCacheOutputStream.writeHeader(this.primaryPrincipal, this.version);
/* 256 */         Credentials[] arrayOfCredentials = null;
/* 257 */         if ((arrayOfCredentials = getCredsList()) != null)
/* 258 */           for (int i = 0; i < arrayOfCredentials.length; i++)
/* 259 */             localCCacheOutputStream.addCreds(arrayOfCredentials[i]);
/*     */       }
/*     */       catch (Throwable localThrowable4)
/*     */       {
/* 253 */         localObject2 = localThrowable4; throw localThrowable4; } finally {  } } catch (Throwable localThrowable2) { localObject1 = localThrowable2; throw localThrowable2;
/*     */     }
/*     */     finally
/*     */     {
/* 262 */       if (localFileOutputStream != null) if (localObject1 != null) try { localFileOutputStream.close(); } catch (Throwable localThrowable6) { localObject1.addSuppressed(localThrowable6); } else localFileOutputStream.close();  
/*     */     }
/*     */   }
/*     */ 
/*     */   boolean match(String[] paramArrayOfString1, String[] paramArrayOfString2)
/*     */   {
/* 266 */     if (paramArrayOfString1.length != paramArrayOfString2.length) {
/* 267 */       return false;
/*     */     }
/* 269 */     for (int i = 0; i < paramArrayOfString1.length; i++) {
/* 270 */       if (!paramArrayOfString1[i].equalsIgnoreCase(paramArrayOfString2[i])) {
/* 271 */         return false;
/*     */       }
/*     */     }
/*     */ 
/* 275 */     return true;
/*     */   }
/*     */ 
/*     */   public synchronized Credentials[] getCredsList()
/*     */   {
/* 282 */     if ((this.credentialsList == null) || (this.credentialsList.isEmpty())) {
/* 283 */       return null;
/*     */     }
/* 285 */     Credentials[] arrayOfCredentials = new Credentials[this.credentialsList.size()];
/* 286 */     for (int i = 0; i < this.credentialsList.size(); i++) {
/* 287 */       arrayOfCredentials[i] = ((Credentials)this.credentialsList.elementAt(i));
/*     */     }
/* 289 */     return arrayOfCredentials;
/*     */   }
/*     */ 
/*     */   public Credentials getCreds(LoginOptions paramLoginOptions, PrincipalName paramPrincipalName, Realm paramRealm)
/*     */   {
/* 296 */     if (paramLoginOptions == null) {
/* 297 */       return getCreds(paramPrincipalName, paramRealm);
/*     */     }
/* 299 */     Credentials[] arrayOfCredentials = getCredsList();
/* 300 */     if (arrayOfCredentials == null) {
/* 301 */       return null;
/*     */     }
/* 303 */     for (int i = 0; i < arrayOfCredentials.length; i++) {
/* 304 */       if ((paramPrincipalName.match(arrayOfCredentials[i].sname)) && (paramRealm.toString().equals(arrayOfCredentials[i].srealm.toString())))
/*     */       {
/* 306 */         if (arrayOfCredentials[i].flags.match(paramLoginOptions)) {
/* 307 */           return arrayOfCredentials[i];
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 312 */     return null;
/*     */   }
/*     */ 
/*     */   public Credentials getCreds(PrincipalName paramPrincipalName, Realm paramRealm)
/*     */   {
/* 323 */     Credentials[] arrayOfCredentials = getCredsList();
/* 324 */     if (arrayOfCredentials == null) {
/* 325 */       return null;
/*     */     }
/* 327 */     for (int i = 0; i < arrayOfCredentials.length; i++) {
/* 328 */       if ((paramPrincipalName.match(arrayOfCredentials[i].sname)) && (paramRealm.toString().equals(arrayOfCredentials[i].srealm.toString())))
/*     */       {
/* 330 */         return arrayOfCredentials[i];
/*     */       }
/*     */     }
/*     */ 
/* 334 */     return null;
/*     */   }
/*     */ 
/*     */   public Credentials getDefaultCreds() {
/* 338 */     Credentials[] arrayOfCredentials = getCredsList();
/* 339 */     if (arrayOfCredentials == null) {
/* 340 */       return null;
/*     */     }
/* 342 */     for (int i = arrayOfCredentials.length - 1; i >= 0; i--) {
/* 343 */       if (arrayOfCredentials[i].sname.toString().startsWith("krbtgt")) {
/* 344 */         String[] arrayOfString = arrayOfCredentials[i].sname.getNameStrings();
/*     */ 
/* 346 */         if (arrayOfString[1].equals(arrayOfCredentials[i].srealm.toString())) {
/* 347 */           return arrayOfCredentials[i];
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 352 */     return null;
/*     */   }
/*     */ 
/*     */   public static String getDefaultCacheName()
/*     */   {
/* 367 */     String str1 = "krb5cc";
/*     */ 
/* 372 */     String str2 = (String)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public String run()
/*     */       {
/* 376 */         String str = System.getenv("KRB5CCNAME");
/* 377 */         if ((str != null) && (str.length() >= 5) && (str.regionMatches(true, 0, "FILE:", 0, 5)))
/*     */         {
/* 380 */           str = str.substring(5);
/*     */         }
/* 382 */         return str;
/*     */       }
/*     */     });
/* 385 */     if (str2 != null) {
/* 386 */       if (DEBUG) {
/* 387 */         System.out.println(">>>KinitOptions cache name is " + str2);
/*     */       }
/* 389 */       return str2;
/*     */     }
/*     */ 
/* 393 */     String str3 = (String)AccessController.doPrivileged(new GetPropertyAction("os.name"));
/*     */ 
/* 408 */     if (str3 != null) {
/* 409 */       str4 = null;
/* 410 */       str5 = null;
/* 411 */       long l = 0L;
/*     */ 
/* 413 */       if ((str3.startsWith("SunOS")) || (str3.startsWith("Linux"))) {
/*     */         try
/*     */         {
/* 416 */           Class localClass = Class.forName("com.sun.security.auth.module.UnixSystem");
/*     */ 
/* 418 */           Constructor localConstructor = localClass.getConstructor(new Class[0]);
/* 419 */           Object localObject = localConstructor.newInstance(new Object[0]);
/* 420 */           Method localMethod = localClass.getMethod("getUid", new Class[0]);
/* 421 */           l = ((Long)localMethod.invoke(localObject, new Object[0])).longValue();
/* 422 */           str2 = File.separator + "tmp" + File.separator + str1 + "_" + l;
/*     */ 
/* 424 */           if (DEBUG) {
/* 425 */             System.out.println(">>>KinitOptions cache name is " + str2);
/*     */           }
/*     */ 
/* 428 */           return str2;
/*     */         } catch (Exception localException) {
/* 430 */           if (DEBUG) {
/* 431 */             System.out.println("Exception in obtaining uid for Unix platforms Using user's home directory");
/*     */ 
/* 436 */             localException.printStackTrace();
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 445 */     String str4 = (String)AccessController.doPrivileged(new GetPropertyAction("user.name"));
/*     */ 
/* 449 */     String str5 = (String)AccessController.doPrivileged(new GetPropertyAction("user.home"));
/*     */ 
/* 453 */     if (str5 == null) {
/* 454 */       str5 = (String)AccessController.doPrivileged(new GetPropertyAction("user.dir"));
/*     */     }
/*     */ 
/* 459 */     if (str4 != null) {
/* 460 */       str2 = str5 + File.separator + str1 + "_" + str4;
/*     */     }
/*     */     else {
/* 463 */       str2 = str5 + File.separator + str1;
/*     */     }
/*     */ 
/* 466 */     if (DEBUG) {
/* 467 */       System.out.println(">>>KinitOptions cache name is " + str2);
/*     */     }
/*     */ 
/* 470 */     return str2;
/*     */   }
/*     */ 
/*     */   public static String checkValidation(String paramString) {
/* 474 */     String str = null;
/* 475 */     if (paramString == null) {
/* 476 */       return null;
/*     */     }
/*     */     try
/*     */     {
/* 480 */       str = new File(paramString).getCanonicalPath();
/* 481 */       File localFile1 = new File(str);
/* 482 */       if (!localFile1.exists())
/*     */       {
/* 484 */         File localFile2 = new File(localFile1.getParent());
/*     */ 
/* 486 */         if (!localFile2.isDirectory())
/* 487 */           str = null;
/* 488 */         localFile2 = null;
/*     */       }
/* 490 */       localFile1 = null;
/*     */     }
/*     */     catch (IOException localIOException) {
/* 493 */       str = null;
/*     */     }
/* 495 */     return str;
/*     */   }
/*     */ 
/*     */   private static String exec(String paramString)
/*     */   {
/* 500 */     StringTokenizer localStringTokenizer = new StringTokenizer(paramString);
/* 501 */     Vector localVector = new Vector();
/* 502 */     while (localStringTokenizer.hasMoreTokens()) {
/* 503 */       localVector.addElement(localStringTokenizer.nextToken());
/*     */     }
/* 505 */     String[] arrayOfString = new String[localVector.size()];
/* 506 */     localVector.copyInto(arrayOfString);
/*     */     try
/*     */     {
/* 509 */       Process localProcess = (Process)AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public Process run()
/*     */         {
/*     */           try {
/* 514 */             return Runtime.getRuntime().exec(this.val$command);
/*     */           } catch (IOException localIOException) {
/* 516 */             if (FileCredentialsCache.DEBUG)
/* 517 */               localIOException.printStackTrace();
/*     */           }
/* 519 */           return null;
/*     */         }
/*     */       });
/* 523 */       if (localProcess == null)
/*     */       {
/* 525 */         return null;
/*     */       }
/*     */ 
/* 528 */       BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(localProcess.getInputStream(), "8859_1"));
/*     */ 
/* 531 */       String str = null;
/* 532 */       if ((arrayOfString.length == 1) && (arrayOfString[0].equals("/usr/bin/env")));
/* 534 */       while ((str = localBufferedReader.readLine()) != null)
/* 535 */         if ((str.length() >= 11) && 
/* 536 */           (str.substring(0, 11).equalsIgnoreCase("KRB5CCNAME=")))
/*     */         {
/* 538 */           str = str.substring(11);
/* 539 */           break;
/*     */ 
/* 543 */           str = localBufferedReader.readLine();
/*     */         }
/* 544 */       localBufferedReader.close();
/* 545 */       return str;
/*     */     } catch (Exception localException) {
/* 547 */       if (DEBUG) {
/* 548 */         localException.printStackTrace();
/*     */       }
/*     */     }
/* 551 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.ccache.FileCredentialsCache
 * JD-Core Version:    0.6.2
 */