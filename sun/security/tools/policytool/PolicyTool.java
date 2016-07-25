/*     */ package sun.security.tools.policytool;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileReader;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.security.KeyStore;
/*     */ import java.security.KeyStoreException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.NoSuchProviderException;
/*     */ import java.security.Permission;
/*     */ import java.security.PublicKey;
/*     */ import java.security.UnrecoverableKeyException;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.text.Collator;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.Enumeration;
/*     */ import java.util.LinkedList;
/*     */ import java.util.ListIterator;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.Vector;
/*     */ import javax.security.auth.login.LoginException;
/*     */ import javax.security.auth.x500.X500Principal;
/*     */ import sun.security.provider.PolicyParser;
/*     */ import sun.security.provider.PolicyParser.GrantEntry;
/*     */ import sun.security.provider.PolicyParser.ParsingException;
/*     */ import sun.security.provider.PolicyParser.PermissionEntry;
/*     */ import sun.security.provider.PolicyParser.PrincipalEntry;
/*     */ import sun.security.util.PolicyUtil;
/*     */ import sun.security.util.PropertyExpander;
/*     */ import sun.security.util.PropertyExpander.ExpandException;
/*     */ 
/*     */ public class PolicyTool
/*     */ {
/*  62 */   static final ResourceBundle rb = ResourceBundle.getBundle("sun.security.util.Resources");
/*     */ 
/*  64 */   static final Collator collator = Collator.getInstance();
/*     */   Vector<String> warnings;
/*  72 */   boolean newWarning = false;
/*     */ 
/*  76 */   boolean modified = false;
/*     */   private static final boolean testing = false;
/*  79 */   private static final Class[] TWOPARAMS = { String.class, String.class };
/*  80 */   private static final Class[] ONEPARAMS = { String.class };
/*  81 */   private static final Class[] NOPARAMS = new Class[0];
/*     */ 
/*  89 */   private static String policyFileName = null;
/*  90 */   private Vector<PolicyEntry> policyEntries = null;
/*  91 */   private PolicyParser parser = null;
/*     */ 
/*  94 */   private KeyStore keyStore = null;
/*  95 */   private String keyStoreName = " ";
/*  96 */   private String keyStoreType = " ";
/*  97 */   private String keyStoreProvider = " ";
/*  98 */   private String keyStorePwdURL = " ";
/*     */   private static final String P11KEYSTORE = "PKCS11";
/*     */   private static final String NONE = "NONE";
/*     */ 
/*     */   private PolicyTool()
/*     */   {
/* 110 */     this.policyEntries = new Vector();
/* 111 */     this.parser = new PolicyParser();
/* 112 */     this.warnings = new Vector();
/*     */   }
/*     */ 
/*     */   String getPolicyFileName()
/*     */   {
/* 119 */     return policyFileName;
/*     */   }
/*     */ 
/*     */   void setPolicyFileName(String paramString)
/*     */   {
/* 126 */     policyFileName = paramString;
/*     */   }
/*     */ 
/*     */   void clearKeyStoreInfo()
/*     */   {
/* 133 */     this.keyStoreName = null;
/* 134 */     this.keyStoreType = null;
/* 135 */     this.keyStoreProvider = null;
/* 136 */     this.keyStorePwdURL = null;
/*     */ 
/* 138 */     this.keyStore = null;
/*     */   }
/*     */ 
/*     */   String getKeyStoreName()
/*     */   {
/* 145 */     return this.keyStoreName;
/*     */   }
/*     */ 
/*     */   String getKeyStoreType()
/*     */   {
/* 152 */     return this.keyStoreType;
/*     */   }
/*     */ 
/*     */   String getKeyStoreProvider()
/*     */   {
/* 159 */     return this.keyStoreProvider;
/*     */   }
/*     */ 
/*     */   String getKeyStorePwdURL()
/*     */   {
/* 166 */     return this.keyStorePwdURL;
/*     */   }
/*     */ 
/*     */   void openPolicy(String paramString)
/*     */     throws FileNotFoundException, PolicyParser.ParsingException, KeyStoreException, CertificateException, InstantiationException, MalformedURLException, IOException, NoSuchAlgorithmException, IllegalAccessException, NoSuchMethodException, UnrecoverableKeyException, NoSuchProviderException, ClassNotFoundException, PropertyExpander.ExpandException, InvocationTargetException
/*     */   {
/* 188 */     this.newWarning = false;
/*     */ 
/* 191 */     this.policyEntries = new Vector();
/* 192 */     this.parser = new PolicyParser();
/* 193 */     this.warnings = new Vector();
/* 194 */     setPolicyFileName(null);
/* 195 */     clearKeyStoreInfo();
/*     */ 
/* 198 */     if (paramString == null) {
/* 199 */       this.modified = false;
/* 200 */       return;
/*     */     }
/*     */ 
/* 208 */     setPolicyFileName(paramString);
/* 209 */     this.parser.read(new FileReader(paramString));
/*     */ 
/* 212 */     openKeyStore(this.parser.getKeyStoreUrl(), this.parser.getKeyStoreType(), this.parser.getKeyStoreProvider(), this.parser.getStorePassURL());
/*     */ 
/* 218 */     Enumeration localEnumeration = this.parser.grantElements();
/* 219 */     while (localEnumeration.hasMoreElements()) {
/* 220 */       PolicyParser.GrantEntry localGrantEntry = (PolicyParser.GrantEntry)localEnumeration.nextElement();
/*     */       MessageFormat localMessageFormat1;
/*     */       Object localObject4;
/* 223 */       if (localGrantEntry.signedBy != null)
/*     */       {
/* 225 */         localObject1 = parseSigners(localGrantEntry.signedBy);
/* 226 */         for (int i = 0; i < localObject1.length; i++) {
/* 227 */           PublicKey localPublicKey = getPublicKeyAlias(localObject1[i]);
/* 228 */           if (localPublicKey == null) {
/* 229 */             this.newWarning = true;
/* 230 */             localMessageFormat1 = new MessageFormat(rb.getString("Warning.A.public.key.for.alias.signers.i.does.not.exist.Make.sure.a.KeyStore.is.properly.configured."));
/*     */ 
/* 232 */             localObject4 = new Object[] { localObject1[i] };
/* 233 */             this.warnings.addElement(localMessageFormat1.format(localObject4));
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 239 */       Object localObject1 = localGrantEntry.principals.listIterator(0);
/*     */ 
/* 241 */       while (((ListIterator)localObject1).hasNext()) {
/* 242 */         localObject2 = (PolicyParser.PrincipalEntry)((ListIterator)localObject1).next();
/*     */         try {
/* 244 */           verifyPrincipal(((PolicyParser.PrincipalEntry)localObject2).getPrincipalClass(), ((PolicyParser.PrincipalEntry)localObject2).getPrincipalName());
/*     */         }
/*     */         catch (ClassNotFoundException localClassNotFoundException1) {
/* 247 */           this.newWarning = true;
/* 248 */           localMessageFormat1 = new MessageFormat(rb.getString("Warning.Class.not.found.class"));
/*     */ 
/* 250 */           localObject4 = new Object[] { ((PolicyParser.PrincipalEntry)localObject2).getPrincipalClass() };
/* 251 */           this.warnings.addElement(localMessageFormat1.format(localObject4));
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 256 */       Object localObject2 = localGrantEntry.permissionElements();
/*     */ 
/* 258 */       while (((Enumeration)localObject2).hasMoreElements()) { localObject3 = (PolicyParser.PermissionEntry)((Enumeration)localObject2).nextElement();
/*     */         Object localObject5;
/*     */         try { verifyPermission(((PolicyParser.PermissionEntry)localObject3).permission, ((PolicyParser.PermissionEntry)localObject3).name, ((PolicyParser.PermissionEntry)localObject3).action);
/*     */         } catch (ClassNotFoundException localClassNotFoundException2) {
/* 263 */           this.newWarning = true;
/* 264 */           localObject4 = new MessageFormat(rb.getString("Warning.Class.not.found.class"));
/*     */ 
/* 266 */           localObject5 = new Object[] { ((PolicyParser.PermissionEntry)localObject3).permission };
/* 267 */           this.warnings.addElement(((MessageFormat)localObject4).format(localObject5));
/*     */         } catch (InvocationTargetException localInvocationTargetException) {
/* 269 */           this.newWarning = true;
/* 270 */           localObject4 = new MessageFormat(rb.getString("Warning.Invalid.argument.s.for.constructor.arg"));
/*     */ 
/* 272 */           localObject5 = new Object[] { ((PolicyParser.PermissionEntry)localObject3).permission };
/* 273 */           this.warnings.addElement(((MessageFormat)localObject4).format(localObject5));
/*     */         }
/*     */ 
/* 277 */         if (((PolicyParser.PermissionEntry)localObject3).signedBy != null)
/*     */         {
/* 279 */           String[] arrayOfString = parseSigners(((PolicyParser.PermissionEntry)localObject3).signedBy);
/*     */ 
/* 281 */           for (int j = 0; j < arrayOfString.length; j++) {
/* 282 */             localObject5 = getPublicKeyAlias(arrayOfString[j]);
/* 283 */             if (localObject5 == null) {
/* 284 */               this.newWarning = true;
/* 285 */               MessageFormat localMessageFormat2 = new MessageFormat(rb.getString("Warning.A.public.key.for.alias.signers.i.does.not.exist.Make.sure.a.KeyStore.is.properly.configured."));
/*     */ 
/* 287 */               Object[] arrayOfObject = { arrayOfString[j] };
/* 288 */               this.warnings.addElement(localMessageFormat2.format(arrayOfObject));
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 293 */       Object localObject3 = new PolicyEntry(this, localGrantEntry);
/* 294 */       this.policyEntries.addElement(localObject3);
/*     */     }
/*     */ 
/* 298 */     this.modified = false;
/*     */   }
/*     */ 
/*     */   void savePolicy(String paramString)
/*     */     throws FileNotFoundException, IOException
/*     */   {
/* 308 */     this.parser.setKeyStoreUrl(this.keyStoreName);
/* 309 */     this.parser.setKeyStoreType(this.keyStoreType);
/* 310 */     this.parser.setKeyStoreProvider(this.keyStoreProvider);
/* 311 */     this.parser.setStorePassURL(this.keyStorePwdURL);
/* 312 */     this.parser.write(new FileWriter(paramString));
/* 313 */     this.modified = false;
/*     */   }
/*     */ 
/*     */   void openKeyStore(String paramString1, String paramString2, String paramString3, String paramString4)
/*     */     throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException, IOException, CertificateException, NoSuchProviderException, PropertyExpander.ExpandException
/*     */   {
/* 330 */     if ((paramString1 == null) && (paramString2 == null) && (paramString3 == null) && (paramString4 == null))
/*     */     {
/* 336 */       this.keyStoreName = null;
/* 337 */       this.keyStoreType = null;
/* 338 */       this.keyStoreProvider = null;
/* 339 */       this.keyStorePwdURL = null;
/*     */ 
/* 343 */       return;
/*     */     }
/*     */ 
/* 346 */     URL localURL = null;
/* 347 */     if (policyFileName != null) {
/* 348 */       File localFile = new File(policyFileName);
/* 349 */       localURL = new URL("file:" + localFile.getCanonicalPath());
/*     */     }
/*     */ 
/* 357 */     if ((paramString1 != null) && (paramString1.length() > 0)) {
/* 358 */       paramString1 = PropertyExpander.expand(paramString1).replace(File.separatorChar, '/');
/*     */     }
/*     */ 
/* 361 */     if ((paramString2 == null) || (paramString2.length() == 0)) {
/* 362 */       paramString2 = KeyStore.getDefaultType();
/*     */     }
/* 364 */     if ((paramString4 != null) && (paramString4.length() > 0)) {
/* 365 */       paramString4 = PropertyExpander.expand(paramString4).replace(File.separatorChar, '/');
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 370 */       this.keyStore = PolicyUtil.getKeyStore(localURL, paramString1, paramString2, paramString3, paramString4, null);
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/* 379 */       String str = "no password provided, and no callback handler available for retrieving password";
/*     */ 
/* 382 */       Throwable localThrowable = localIOException.getCause();
/* 383 */       if ((localThrowable != null) && ((localThrowable instanceof LoginException)) && (str.equals(localThrowable.getMessage())))
/*     */       {
/* 388 */         throw new IOException(str);
/*     */       }
/* 390 */       throw localIOException;
/*     */     }
/*     */ 
/* 394 */     this.keyStoreName = paramString1;
/* 395 */     this.keyStoreType = paramString2;
/* 396 */     this.keyStoreProvider = paramString3;
/* 397 */     this.keyStorePwdURL = paramString4;
/*     */   }
/*     */ 
/*     */   boolean addEntry(PolicyEntry paramPolicyEntry, int paramInt)
/*     */   {
/* 408 */     if (paramInt < 0)
/*     */     {
/* 410 */       this.policyEntries.addElement(paramPolicyEntry);
/* 411 */       this.parser.add(paramPolicyEntry.getGrantEntry());
/*     */     }
/*     */     else {
/* 414 */       PolicyEntry localPolicyEntry = (PolicyEntry)this.policyEntries.elementAt(paramInt);
/* 415 */       this.parser.replace(localPolicyEntry.getGrantEntry(), paramPolicyEntry.getGrantEntry());
/* 416 */       this.policyEntries.setElementAt(paramPolicyEntry, paramInt);
/*     */     }
/* 418 */     return true;
/*     */   }
/*     */ 
/*     */   boolean addPrinEntry(PolicyEntry paramPolicyEntry, PolicyParser.PrincipalEntry paramPrincipalEntry, int paramInt)
/*     */   {
/* 432 */     PolicyParser.GrantEntry localGrantEntry = paramPolicyEntry.getGrantEntry();
/* 433 */     if (localGrantEntry.contains(paramPrincipalEntry) == true) {
/* 434 */       return false;
/*     */     }
/* 436 */     LinkedList localLinkedList = localGrantEntry.principals;
/*     */ 
/* 438 */     if (paramInt != -1)
/* 439 */       localLinkedList.set(paramInt, paramPrincipalEntry);
/*     */     else {
/* 441 */       localLinkedList.add(paramPrincipalEntry);
/*     */     }
/* 443 */     this.modified = true;
/* 444 */     return true;
/*     */   }
/*     */ 
/*     */   boolean addPermEntry(PolicyEntry paramPolicyEntry, PolicyParser.PermissionEntry paramPermissionEntry, int paramInt)
/*     */   {
/* 458 */     PolicyParser.GrantEntry localGrantEntry = paramPolicyEntry.getGrantEntry();
/* 459 */     if (localGrantEntry.contains(paramPermissionEntry) == true) {
/* 460 */       return false;
/*     */     }
/* 462 */     Vector localVector = localGrantEntry.permissionEntries;
/*     */ 
/* 464 */     if (paramInt != -1)
/* 465 */       localVector.setElementAt(paramPermissionEntry, paramInt);
/*     */     else {
/* 467 */       localVector.addElement(paramPermissionEntry);
/*     */     }
/* 469 */     this.modified = true;
/* 470 */     return true;
/*     */   }
/*     */ 
/*     */   boolean removePermEntry(PolicyEntry paramPolicyEntry, PolicyParser.PermissionEntry paramPermissionEntry)
/*     */   {
/* 480 */     PolicyParser.GrantEntry localGrantEntry = paramPolicyEntry.getGrantEntry();
/* 481 */     this.modified = localGrantEntry.remove(paramPermissionEntry);
/* 482 */     return this.modified;
/*     */   }
/*     */ 
/*     */   boolean removeEntry(PolicyEntry paramPolicyEntry)
/*     */   {
/* 490 */     this.parser.remove(paramPolicyEntry.getGrantEntry());
/* 491 */     this.modified = true;
/* 492 */     return this.policyEntries.removeElement(paramPolicyEntry);
/*     */   }
/*     */ 
/*     */   PolicyEntry[] getEntry()
/*     */   {
/* 500 */     if (this.policyEntries.size() > 0) {
/* 501 */       PolicyEntry[] arrayOfPolicyEntry = new PolicyEntry[this.policyEntries.size()];
/* 502 */       for (int i = 0; i < this.policyEntries.size(); i++)
/* 503 */         arrayOfPolicyEntry[i] = ((PolicyEntry)this.policyEntries.elementAt(i));
/* 504 */       return arrayOfPolicyEntry;
/*     */     }
/* 506 */     return null;
/*     */   }
/*     */ 
/*     */   PublicKey getPublicKeyAlias(String paramString)
/*     */     throws KeyStoreException
/*     */   {
/* 514 */     if (this.keyStore == null) {
/* 515 */       return null;
/*     */     }
/*     */ 
/* 518 */     Certificate localCertificate = this.keyStore.getCertificate(paramString);
/* 519 */     if (localCertificate == null) {
/* 520 */       return null;
/*     */     }
/* 522 */     PublicKey localPublicKey = localCertificate.getPublicKey();
/* 523 */     return localPublicKey;
/*     */   }
/*     */ 
/*     */   String[] getPublicKeyAlias()
/*     */     throws KeyStoreException
/*     */   {
/* 531 */     int i = 0;
/* 532 */     String[] arrayOfString = null;
/*     */ 
/* 534 */     if (this.keyStore == null) {
/* 535 */       return null;
/*     */     }
/* 537 */     Enumeration localEnumeration = this.keyStore.aliases();
/*     */ 
/* 540 */     while (localEnumeration.hasMoreElements()) {
/* 541 */       localEnumeration.nextElement();
/* 542 */       i++;
/*     */     }
/*     */ 
/* 545 */     if (i > 0)
/*     */     {
/* 547 */       arrayOfString = new String[i];
/* 548 */       i = 0;
/* 549 */       localEnumeration = this.keyStore.aliases();
/* 550 */       while (localEnumeration.hasMoreElements()) {
/* 551 */         arrayOfString[i] = new String((String)localEnumeration.nextElement());
/* 552 */         i++;
/*     */       }
/*     */     }
/* 555 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   String[] parseSigners(String paramString)
/*     */   {
/* 564 */     String[] arrayOfString = null;
/* 565 */     int i = 1;
/* 566 */     int j = 0;
/* 567 */     int k = 0;
/* 568 */     int m = 0;
/*     */ 
/* 571 */     while (k >= 0) {
/* 572 */       k = paramString.indexOf(',', j);
/* 573 */       if (k >= 0) {
/* 574 */         i++;
/* 575 */         j = k + 1;
/*     */       }
/*     */     }
/* 578 */     arrayOfString = new String[i];
/*     */ 
/* 581 */     k = 0;
/* 582 */     j = 0;
/* 583 */     while (k >= 0) {
/* 584 */       if ((k = paramString.indexOf(',', j)) >= 0)
/*     */       {
/* 586 */         arrayOfString[m] = paramString.substring(j, k).trim();
/*     */ 
/* 588 */         m++;
/* 589 */         j = k + 1;
/*     */       }
/*     */       else {
/* 592 */         arrayOfString[m] = paramString.substring(j).trim();
/*     */       }
/*     */     }
/* 595 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   void verifyPrincipal(String paramString1, String paramString2)
/*     */     throws ClassNotFoundException, InstantiationException
/*     */   {
/* 605 */     if ((paramString1.equals("WILDCARD_PRINCIPAL_CLASS")) || (paramString1.equals("PolicyParser.REPLACE_NAME")))
/*     */     {
/* 607 */       return;
/*     */     }
/* 609 */     Class localClass1 = Class.forName("java.security.Principal");
/* 610 */     Class localClass2 = Class.forName(paramString1, true, Thread.currentThread().getContextClassLoader());
/*     */     Object localObject;
/* 612 */     if (!localClass1.isAssignableFrom(localClass2)) {
/* 613 */       localObject = new MessageFormat(rb.getString("Illegal.Principal.Type.type"));
/*     */ 
/* 615 */       Object[] arrayOfObject = { paramString1 };
/* 616 */       throw new InstantiationException(((MessageFormat)localObject).format(arrayOfObject));
/*     */     }
/*     */ 
/* 619 */     if ("javax.security.auth.x500.X500Principal".equals(localClass2.getName()))
/*     */     {
/* 625 */       localObject = new X500Principal(paramString2);
/*     */     }
/*     */   }
/*     */ 
/*     */   void verifyPermission(String paramString1, String paramString2, String paramString3)
/*     */     throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException
/*     */   {
/* 643 */     Class localClass = Class.forName(paramString1, true, Thread.currentThread().getContextClassLoader());
/*     */ 
/* 645 */     Constructor localConstructor = null;
/* 646 */     Vector localVector = new Vector(2);
/* 647 */     if (paramString2 != null) localVector.add(paramString2);
/* 648 */     if (paramString3 != null) localVector.add(paramString3);
/* 649 */     switch (localVector.size()) {
/*     */     case 0:
/*     */       try {
/* 652 */         localConstructor = localClass.getConstructor(NOPARAMS);
/*     */       }
/*     */       catch (NoSuchMethodException localNoSuchMethodException1)
/*     */       {
/* 656 */         localVector.add(null);
/*     */       }
/*     */     case 1:
/*     */       try {
/* 660 */         localConstructor = localClass.getConstructor(ONEPARAMS);
/*     */       }
/*     */       catch (NoSuchMethodException localNoSuchMethodException2)
/*     */       {
/* 664 */         localVector.add(null);
/*     */       }
/*     */     case 2:
/* 667 */       localConstructor = localClass.getConstructor(TWOPARAMS);
/*     */     }
/*     */ 
/* 670 */     Object[] arrayOfObject = localVector.toArray();
/* 671 */     Permission localPermission = (Permission)localConstructor.newInstance(arrayOfObject);
/*     */   }
/*     */ 
/*     */   static void parseArgs(String[] paramArrayOfString)
/*     */   {
/* 679 */     int i = 0;
/*     */ 
/* 681 */     for (i = 0; (i < paramArrayOfString.length) && (paramArrayOfString[i].startsWith("-")); i++)
/*     */     {
/* 683 */       String str = paramArrayOfString[i];
/*     */ 
/* 685 */       if (collator.compare(str, "-file") == 0) {
/* 686 */         i++; if (i == paramArrayOfString.length) usage();
/* 687 */         policyFileName = paramArrayOfString[i];
/*     */       } else {
/* 689 */         MessageFormat localMessageFormat = new MessageFormat(rb.getString("Illegal.option.option"));
/*     */ 
/* 691 */         Object[] arrayOfObject = { str };
/* 692 */         System.err.println(localMessageFormat.format(arrayOfObject));
/* 693 */         usage();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static void usage() {
/* 699 */     System.out.println(rb.getString("Usage.policytool.options."));
/* 700 */     System.out.println();
/* 701 */     System.out.println(rb.getString(".file.file.policy.file.location"));
/*     */ 
/* 703 */     System.out.println();
/*     */ 
/* 705 */     System.exit(1);
/*     */   }
/*     */ 
/*     */   public static void main(String[] paramArrayOfString)
/*     */   {
/* 712 */     parseArgs(paramArrayOfString);
/* 713 */     ToolWindow localToolWindow = new ToolWindow(new PolicyTool());
/* 714 */     localToolWindow.displayToolWindow(paramArrayOfString);
/*     */   }
/*     */ 
/*     */   static String splitToWords(String paramString)
/*     */   {
/* 721 */     return paramString.replaceAll("([A-Z])", " $1");
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  67 */     collator.setStrength(0);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.tools.policytool.PolicyTool
 * JD-Core Version:    0.6.2
 */