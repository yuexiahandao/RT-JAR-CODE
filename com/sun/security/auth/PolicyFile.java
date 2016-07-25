/*      */ package com.sun.security.auth;
/*      */ 
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.PrintStream;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.URL;
/*      */ import java.security.AccessController;
/*      */ import java.security.AllPermission;
/*      */ import java.security.CodeSource;
/*      */ import java.security.KeyStore;
/*      */ import java.security.KeyStoreException;
/*      */ import java.security.Permission;
/*      */ import java.security.PermissionCollection;
/*      */ import java.security.Permissions;
/*      */ import java.security.Principal;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.security.Security;
/*      */ import java.security.UnresolvedPermission;
/*      */ import java.security.cert.Certificate;
/*      */ import java.security.cert.X509Certificate;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedList;
/*      */ import java.util.ListIterator;
/*      */ import java.util.ResourceBundle;
/*      */ import java.util.Set;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.Vector;
/*      */ import javax.security.auth.AuthPermission;
/*      */ import javax.security.auth.Policy;
/*      */ import javax.security.auth.PrivateCredentialPermission;
/*      */ import javax.security.auth.Subject;
/*      */ import sun.security.util.Debug;
/*      */ import sun.security.util.PropertyExpander;
/*      */ 
/*      */ @Deprecated
/*      */ public class PolicyFile extends Policy
/*      */ {
/*  245 */   static final ResourceBundle rb = (ResourceBundle)AccessController.doPrivileged(new PrivilegedAction()
/*      */   {
/*      */     public ResourceBundle run()
/*      */     {
/*  249 */       return ResourceBundle.getBundle("sun.security.util.AuthResources");
/*      */     }
/*      */   });
/*      */ 
/*  255 */   private static final Debug debug = Debug.getInstance("policy", "\t[Auth Policy]");
/*      */   private static final String AUTH_POLICY = "java.security.auth.policy";
/*      */   private static final String SECURITY_MANAGER = "java.security.manager";
/*      */   private static final String AUTH_POLICY_URL = "auth.policy.url.";
/*      */   private Vector<PolicyEntry> policyEntries;
/*      */   private Hashtable aliasMapping;
/*  265 */   private boolean initialized = false;
/*      */ 
/*  267 */   private boolean expandProperties = true;
/*  268 */   private boolean ignoreIdentityScope = true;
/*      */ 
/*  272 */   private static final Class[] PARAMS = { String.class, String.class };
/*      */ 
/*      */   public PolicyFile()
/*      */   {
/*  281 */     String str = System.getProperty("java.security.auth.policy");
/*      */ 
/*  283 */     if (str == null) {
/*  284 */       str = System.getProperty("java.security.manager");
/*      */     }
/*  286 */     if (str != null)
/*  287 */       init();
/*      */   }
/*      */ 
/*      */   private synchronized void init()
/*      */   {
/*  292 */     if (this.initialized) {
/*  293 */       return;
/*      */     }
/*  295 */     this.policyEntries = new Vector();
/*  296 */     this.aliasMapping = new Hashtable(11);
/*      */ 
/*  298 */     initPolicyFile();
/*  299 */     this.initialized = true;
/*      */   }
/*      */ 
/*      */   public synchronized void refresh()
/*      */   {
/*  313 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  314 */     if (localSecurityManager != null) {
/*  315 */       localSecurityManager.checkPermission(new AuthPermission("refreshPolicy"));
/*      */     }
/*      */ 
/*  330 */     this.initialized = false;
/*  331 */     AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public Void run() {
/*  334 */         PolicyFile.this.init();
/*  335 */         return null;
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   private KeyStore initKeyStore(URL paramURL, String paramString1, String paramString2)
/*      */   {
/*  342 */     if (paramString1 != null)
/*      */     {
/*      */       try
/*      */       {
/*  348 */         URL localURL = null;
/*      */         try {
/*  350 */           localURL = new URL(paramString1);
/*      */         }
/*      */         catch (MalformedURLException localMalformedURLException)
/*      */         {
/*  354 */           localURL = new URL(paramURL, paramString1);
/*      */         }
/*      */ 
/*  357 */         if (debug != null) {
/*  358 */           debug.println("reading keystore" + localURL);
/*      */         }
/*      */ 
/*  361 */         BufferedInputStream localBufferedInputStream = new BufferedInputStream(getInputStream(localURL));
/*      */         KeyStore localKeyStore;
/*  365 */         if (paramString2 != null)
/*  366 */           localKeyStore = KeyStore.getInstance(paramString2);
/*      */         else
/*  368 */           localKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
/*  369 */         localKeyStore.load(localBufferedInputStream, null);
/*  370 */         localBufferedInputStream.close();
/*  371 */         return localKeyStore;
/*      */       }
/*      */       catch (Exception localException) {
/*  374 */         if (debug != null) {
/*  375 */           localException.printStackTrace();
/*      */         }
/*  377 */         return null;
/*      */       }
/*      */     }
/*  380 */     return null;
/*      */   }
/*      */ 
/*      */   private void initPolicyFile()
/*      */   {
/*  385 */     String str1 = Security.getProperty("policy.expandProperties");
/*      */ 
/*  387 */     if (str1 != null) this.expandProperties = str1.equalsIgnoreCase("true");
/*      */ 
/*  389 */     String str2 = Security.getProperty("policy.ignoreIdentityScope");
/*      */ 
/*  391 */     if (str2 != null) this.ignoreIdentityScope = str2.equalsIgnoreCase("true");
/*      */ 
/*  393 */     String str3 = Security.getProperty("policy.allowSystemProperty");
/*      */ 
/*  395 */     if ((str3 != null) && (str3.equalsIgnoreCase("true")))
/*      */     {
/*  397 */       String str4 = System.getProperty("java.security.auth.policy");
/*  398 */       if (str4 != null) {
/*  399 */         j = 0;
/*  400 */         if (str4.startsWith("=")) {
/*  401 */           j = 1;
/*  402 */           str4 = str4.substring(1);
/*      */         }
/*      */         try {
/*  405 */           str4 = PropertyExpander.expand(str4);
/*      */ 
/*  407 */           File localFile = new File(str4);
/*      */           URL localURL;
/*  408 */           if (localFile.exists()) {
/*  409 */             localURL = new URL("file:" + localFile.getCanonicalPath());
/*      */           }
/*      */           else {
/*  412 */             localURL = new URL(str4);
/*      */           }
/*  414 */           if (debug != null)
/*  415 */             debug.println("reading " + localURL);
/*  416 */           init(localURL);
/*      */         }
/*      */         catch (Exception localException1) {
/*  419 */           if (debug != null) {
/*  420 */             debug.println("caught exception: " + localException1);
/*      */           }
/*      */         }
/*      */ 
/*  424 */         if (j != 0) {
/*  425 */           if (debug != null) {
/*  426 */             debug.println("overriding other policies!");
/*      */           }
/*  428 */           return;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  433 */     int i = 1;
/*  434 */     int j = 0;
/*      */     String str5;
/*  437 */     while ((str5 = Security.getProperty("auth.policy.url." + i)) != null) {
/*      */       try {
/*  439 */         str5 = PropertyExpander.expand(str5).replace(File.separatorChar, '/');
/*      */ 
/*  441 */         if (debug != null)
/*  442 */           debug.println("reading " + str5);
/*  443 */         init(new URL(str5));
/*  444 */         j = 1;
/*      */       } catch (Exception localException2) {
/*  446 */         if (debug != null) {
/*  447 */           debug.println("error reading policy " + localException2);
/*  448 */           localException2.printStackTrace();
/*      */         }
/*      */       }
/*      */ 
/*  452 */       i++;
/*      */     }
/*      */ 
/*  455 */     if (j == 0);
/*      */   }
/*      */ 
/*      */   private boolean checkForTrustedIdentity(Certificate paramCertificate)
/*      */   {
/*  468 */     return false;
/*      */   }
/*      */ 
/*      */   private void init(URL paramURL)
/*      */   {
/*  478 */     PolicyParser localPolicyParser = new PolicyParser(this.expandProperties);
/*      */     try {
/*  480 */       InputStreamReader localInputStreamReader = new InputStreamReader(getInputStream(paramURL));
/*      */ 
/*  482 */       localPolicyParser.read(localInputStreamReader);
/*  483 */       localInputStreamReader.close();
/*  484 */       KeyStore localKeyStore = initKeyStore(paramURL, localPolicyParser.getKeyStoreUrl(), localPolicyParser.getKeyStoreType());
/*      */ 
/*  486 */       Enumeration localEnumeration = localPolicyParser.grantElements();
/*  487 */       while (localEnumeration.hasMoreElements()) {
/*  488 */         PolicyParser.GrantEntry localGrantEntry = (PolicyParser.GrantEntry)localEnumeration.nextElement();
/*  489 */         addGrantEntry(localGrantEntry, localKeyStore);
/*      */       }
/*      */     } catch (PolicyParser.ParsingException localParsingException) {
/*  492 */       System.err.println("java.security.auth.policy" + rb.getString(".error.parsing.") + paramURL);
/*      */ 
/*  494 */       System.err.println("java.security.auth.policy" + rb.getString("COLON") + localParsingException.getMessage());
/*      */ 
/*  497 */       if (debug != null)
/*  498 */         localParsingException.printStackTrace();
/*      */     }
/*      */     catch (Exception localException) {
/*  501 */       if (debug != null) {
/*  502 */         debug.println("error parsing " + paramURL);
/*  503 */         debug.println(localException.toString());
/*  504 */         localException.printStackTrace();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private InputStream getInputStream(URL paramURL)
/*      */     throws IOException
/*      */   {
/*  517 */     if ("file".equals(paramURL.getProtocol())) {
/*  518 */       String str = paramURL.getFile().replace('/', File.separatorChar);
/*  519 */       return new FileInputStream(str);
/*      */     }
/*  521 */     return paramURL.openStream();
/*      */   }
/*      */ 
/*      */   CodeSource getCodeSource(PolicyParser.GrantEntry paramGrantEntry, KeyStore paramKeyStore)
/*      */     throws MalformedURLException
/*      */   {
/*  533 */     Certificate[] arrayOfCertificate = null;
/*  534 */     if (paramGrantEntry.signedBy != null) {
/*  535 */       arrayOfCertificate = getCertificates(paramKeyStore, paramGrantEntry.signedBy);
/*  536 */       if (arrayOfCertificate == null)
/*      */       {
/*  539 */         if (debug != null) {
/*  540 */           debug.println(" no certs for alias " + paramGrantEntry.signedBy + ", ignoring.");
/*      */         }
/*      */ 
/*  543 */         return null;
/*      */       }
/*      */     }
/*      */     URL localURL;
/*  549 */     if (paramGrantEntry.codeBase != null)
/*  550 */       localURL = new URL(paramGrantEntry.codeBase);
/*      */     else {
/*  552 */       localURL = null;
/*      */     }
/*  554 */     if ((paramGrantEntry.principals == null) || (paramGrantEntry.principals.size() == 0)) {
/*  555 */       return canonicalizeCodebase(new CodeSource(localURL, arrayOfCertificate), false);
/*      */     }
/*      */ 
/*  559 */     return canonicalizeCodebase(new SubjectCodeSource(null, paramGrantEntry.principals, localURL, arrayOfCertificate), false);
/*      */   }
/*      */ 
/*      */   private void addGrantEntry(PolicyParser.GrantEntry paramGrantEntry, KeyStore paramKeyStore)
/*      */   {
/*      */     Object localObject1;
/*      */     Object localObject2;
/*  571 */     if (debug != null) {
/*  572 */       debug.println("Adding policy entry: ");
/*  573 */       debug.println("  signedBy " + paramGrantEntry.signedBy);
/*  574 */       debug.println("  codeBase " + paramGrantEntry.codeBase);
/*  575 */       if ((paramGrantEntry.principals != null) && (paramGrantEntry.principals.size() > 0)) {
/*  576 */         localObject1 = paramGrantEntry.principals.listIterator();
/*      */ 
/*  578 */         while (((ListIterator)localObject1).hasNext()) {
/*  579 */           localObject2 = (PolicyParser.PrincipalEntry)((ListIterator)localObject1).next();
/*  580 */           debug.println("  " + ((PolicyParser.PrincipalEntry)localObject2).principalClass + " " + ((PolicyParser.PrincipalEntry)localObject2).principalName);
/*      */         }
/*      */       }
/*      */ 
/*  584 */       debug.println();
/*      */     }
/*      */     try
/*      */     {
/*  588 */       localObject1 = getCodeSource(paramGrantEntry, paramKeyStore);
/*      */ 
/*  590 */       if (localObject1 == null) return;
/*      */ 
/*  592 */       localObject2 = new PolicyEntry((CodeSource)localObject1);
/*  593 */       Enumeration localEnumeration = paramGrantEntry.permissionElements();
/*      */ 
/*  595 */       while (localEnumeration.hasMoreElements()) {
/*  596 */         PolicyParser.PermissionEntry localPermissionEntry = (PolicyParser.PermissionEntry)localEnumeration.nextElement();
/*      */         try
/*      */         {
/*      */           Permission localPermission;
/*  600 */           if ((localPermissionEntry.permission.equals("javax.security.auth.PrivateCredentialPermission")) && (localPermissionEntry.name.endsWith(" self")))
/*      */           {
/*  603 */             localPermission = getInstance(localPermissionEntry.permission, localPermissionEntry.name + " \"self\"", localPermissionEntry.action);
/*      */           }
/*      */           else
/*      */           {
/*  607 */             localPermission = getInstance(localPermissionEntry.permission, localPermissionEntry.name, localPermissionEntry.action);
/*      */           }
/*      */ 
/*  611 */           ((PolicyEntry)localObject2).add(localPermission);
/*  612 */           if (debug != null)
/*  613 */             debug.println("  " + localPermission);
/*      */         }
/*      */         catch (ClassNotFoundException localClassNotFoundException)
/*      */         {
/*      */           Certificate[] arrayOfCertificate;
/*  617 */           if (localPermissionEntry.signedBy != null)
/*  618 */             arrayOfCertificate = getCertificates(paramKeyStore, localPermissionEntry.signedBy);
/*      */           else {
/*  620 */             arrayOfCertificate = null;
/*      */           }
/*      */ 
/*  624 */           if ((arrayOfCertificate != null) || (localPermissionEntry.signedBy == null)) {
/*  625 */             UnresolvedPermission localUnresolvedPermission = new UnresolvedPermission(localPermissionEntry.permission, localPermissionEntry.name, localPermissionEntry.action, arrayOfCertificate);
/*      */ 
/*  630 */             ((PolicyEntry)localObject2).add(localUnresolvedPermission);
/*  631 */             if (debug != null)
/*  632 */               debug.println("  " + localUnresolvedPermission);
/*      */           }
/*      */         }
/*      */         catch (InvocationTargetException localInvocationTargetException) {
/*  636 */           System.err.println("java.security.auth.policy" + rb.getString(".error.adding.Permission.") + localPermissionEntry.permission + rb.getString("SPACE") + localInvocationTargetException.getTargetException());
/*      */         }
/*      */         catch (Exception localException2)
/*      */         {
/*  643 */           System.err.println("java.security.auth.policy" + rb.getString(".error.adding.Permission.") + localPermissionEntry.permission + rb.getString("SPACE") + localException2);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  651 */       this.policyEntries.addElement(localObject2);
/*      */     } catch (Exception localException1) {
/*  653 */       System.err.println("java.security.auth.policy" + rb.getString(".error.adding.Entry.") + paramGrantEntry + rb.getString("SPACE") + localException1);
/*      */     }
/*      */ 
/*  661 */     if (debug != null)
/*  662 */       debug.println();
/*      */   }
/*      */ 
/*      */   private static final Permission getInstance(String paramString1, String paramString2, String paramString3)
/*      */     throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException
/*      */   {
/*  705 */     Class localClass = Class.forName(paramString1);
/*  706 */     Constructor localConstructor = localClass.getConstructor(PARAMS);
/*  707 */     return (Permission)localConstructor.newInstance(new Object[] { paramString2, paramString3 });
/*      */   }
/*      */ 
/*      */   Certificate[] getCertificates(KeyStore paramKeyStore, String paramString)
/*      */   {
/*  716 */     Vector localVector = null;
/*      */ 
/*  718 */     StringTokenizer localStringTokenizer = new StringTokenizer(paramString, ",");
/*  719 */     int i = 0;
/*      */     Object localObject;
/*  721 */     while (localStringTokenizer.hasMoreTokens()) {
/*  722 */       localObject = localStringTokenizer.nextToken().trim();
/*  723 */       i++;
/*  724 */       Certificate localCertificate = null;
/*      */ 
/*  726 */       localCertificate = (Certificate)this.aliasMapping.get(localObject);
/*  727 */       if ((localCertificate == null) && (paramKeyStore != null))
/*      */       {
/*      */         try {
/*  730 */           localCertificate = paramKeyStore.getCertificate((String)localObject);
/*      */         }
/*      */         catch (KeyStoreException localKeyStoreException)
/*      */         {
/*      */         }
/*  735 */         if (localCertificate != null) {
/*  736 */           this.aliasMapping.put(localObject, localCertificate);
/*  737 */           this.aliasMapping.put(localCertificate, localObject);
/*      */         }
/*      */       }
/*      */ 
/*  741 */       if (localCertificate != null) {
/*  742 */         if (localVector == null)
/*  743 */           localVector = new Vector();
/*  744 */         localVector.addElement(localCertificate);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  749 */     if ((localVector != null) && (i == localVector.size())) {
/*  750 */       localObject = new Certificate[localVector.size()];
/*  751 */       localVector.copyInto((Object[])localObject);
/*  752 */       return localObject;
/*      */     }
/*  754 */     return null;
/*      */   }
/*      */ 
/*      */   private final synchronized Enumeration<PolicyEntry> elements()
/*      */   {
/*  765 */     return this.policyEntries.elements();
/*      */   }
/*      */ 
/*      */   public PermissionCollection getPermissions(final Subject paramSubject, final CodeSource paramCodeSource)
/*      */   {
/*  843 */     return (PermissionCollection)AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public PermissionCollection run() {
/*  846 */         SubjectCodeSource localSubjectCodeSource = new SubjectCodeSource(paramSubject, null, paramCodeSource == null ? null : paramCodeSource.getLocation(), paramCodeSource == null ? null : paramCodeSource.getCertificates());
/*      */ 
/*  851 */         if (PolicyFile.this.initialized) {
/*  852 */           return PolicyFile.this.getPermissions(new Permissions(), localSubjectCodeSource);
/*      */         }
/*  854 */         return new PolicyPermissions(PolicyFile.this, localSubjectCodeSource);
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   PermissionCollection getPermissions(CodeSource paramCodeSource)
/*      */   {
/*  872 */     if (this.initialized) {
/*  873 */       return getPermissions(new Permissions(), paramCodeSource);
/*      */     }
/*  875 */     return new PolicyPermissions(this, paramCodeSource);
/*      */   }
/*      */ 
/*      */   Permissions getPermissions(Permissions paramPermissions, CodeSource paramCodeSource)
/*      */   {
/*  893 */     if (!this.initialized) {
/*  894 */       init();
/*      */     }
/*      */ 
/*  897 */     CodeSource[] arrayOfCodeSource = { null };
/*      */ 
/*  899 */     arrayOfCodeSource[0] = canonicalizeCodebase(paramCodeSource, true);
/*      */ 
/*  901 */     if (debug != null) {
/*  902 */       debug.println("evaluate(" + arrayOfCodeSource[0] + ")\n");
/*      */     }
/*      */ 
/*  909 */     for (int i = 0; i < this.policyEntries.size(); i++)
/*      */     {
/*  911 */       PolicyEntry localPolicyEntry = (PolicyEntry)this.policyEntries.elementAt(i);
/*      */ 
/*  913 */       if (debug != null) {
/*  914 */         debug.println("PolicyFile CodeSource implies: " + localPolicyEntry.codesource.toString() + "\n\n" + "\t" + arrayOfCodeSource[0].toString() + "\n\n");
/*      */       }
/*      */ 
/*  919 */       if (localPolicyEntry.codesource.implies(arrayOfCodeSource[0])) {
/*  920 */         for (int k = 0; k < localPolicyEntry.permissions.size(); k++) {
/*  921 */           Permission localPermission = (Permission)localPolicyEntry.permissions.elementAt(k);
/*  922 */           if (debug != null) {
/*  923 */             debug.println("  granting " + localPermission);
/*      */           }
/*  925 */           if (!addSelfPermissions(localPermission, localPolicyEntry.codesource, arrayOfCodeSource[0], paramPermissions))
/*      */           {
/*  931 */             paramPermissions.add(localPermission);
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  939 */     if (!this.ignoreIdentityScope) {
/*  940 */       Certificate[] arrayOfCertificate = arrayOfCodeSource[0].getCertificates();
/*  941 */       if (arrayOfCertificate != null) {
/*  942 */         for (int j = 0; j < arrayOfCertificate.length; j++) {
/*  943 */           if ((this.aliasMapping.get(arrayOfCertificate[j]) == null) && (checkForTrustedIdentity(arrayOfCertificate[j])))
/*      */           {
/*  949 */             paramPermissions.add(new AllPermission());
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  954 */     return paramPermissions;
/*      */   }
/*      */ 
/*      */   private boolean addSelfPermissions(Permission paramPermission, CodeSource paramCodeSource1, CodeSource paramCodeSource2, Permissions paramPermissions)
/*      */   {
/*  978 */     if (!(paramPermission instanceof PrivateCredentialPermission)) {
/*  979 */       return false;
/*      */     }
/*  981 */     if (!(paramCodeSource1 instanceof SubjectCodeSource)) {
/*  982 */       return false;
/*      */     }
/*      */ 
/*  985 */     PrivateCredentialPermission localPrivateCredentialPermission1 = (PrivateCredentialPermission)paramPermission;
/*  986 */     SubjectCodeSource localSubjectCodeSource = (SubjectCodeSource)paramCodeSource1;
/*      */ 
/*  989 */     String[][] arrayOfString1 = localPrivateCredentialPermission1.getPrincipals();
/*  990 */     if ((arrayOfString1.length <= 0) || (!arrayOfString1[0][0].equalsIgnoreCase("self")) || (!arrayOfString1[0][1].equalsIgnoreCase("self")))
/*      */     {
/*  995 */       return false;
/*      */     }
/*      */ 
/* 1002 */     if (localSubjectCodeSource.getPrincipals() == null)
/*      */     {
/* 1004 */       return true;
/*      */     }
/*      */ 
/* 1007 */     ListIterator localListIterator = localSubjectCodeSource.getPrincipals().listIterator();
/*      */ 
/* 1009 */     while (localListIterator.hasNext())
/*      */     {
/* 1011 */       PolicyParser.PrincipalEntry localPrincipalEntry = (PolicyParser.PrincipalEntry)localListIterator.next();
/*      */ 
/* 1024 */       String[][] arrayOfString2 = getPrincipalInfo(localPrincipalEntry, paramCodeSource2);
/*      */ 
/* 1027 */       for (int i = 0; i < arrayOfString2.length; i++)
/*      */       {
/* 1031 */         PrivateCredentialPermission localPrivateCredentialPermission2 = new PrivateCredentialPermission(localPrivateCredentialPermission1.getCredentialClass() + " " + arrayOfString2[i][0] + " " + "\"" + arrayOfString2[i][1] + "\"", "read");
/*      */ 
/* 1040 */         if (debug != null) {
/* 1041 */           debug.println("adding SELF permission: " + localPrivateCredentialPermission2.toString());
/*      */         }
/*      */ 
/* 1045 */         paramPermissions.add(localPrivateCredentialPermission2);
/*      */       }
/*      */     }
/*      */ 
/* 1049 */     return true;
/*      */   }
/*      */ 
/*      */   private String[][] getPrincipalInfo(PolicyParser.PrincipalEntry paramPrincipalEntry, CodeSource paramCodeSource)
/*      */   {
/* 1067 */     if ((!paramPrincipalEntry.principalClass.equals("WILDCARD_PRINCIPAL_CLASS")) && (!paramPrincipalEntry.principalName.equals("WILDCARD_PRINCIPAL_NAME")))
/*      */     {
/* 1074 */       localObject = new String[1][2];
/* 1075 */       localObject[0][0] = paramPrincipalEntry.principalClass;
/* 1076 */       localObject[0][1] = paramPrincipalEntry.principalName;
/* 1077 */       return localObject;
/*      */     }
/*      */     Principal localPrincipal;
/* 1079 */     if ((!paramPrincipalEntry.principalClass.equals("WILDCARD_PRINCIPAL_CLASS")) && (paramPrincipalEntry.principalName.equals("WILDCARD_PRINCIPAL_NAME")))
/*      */     {
/* 1089 */       localObject = (SubjectCodeSource)paramCodeSource;
/*      */ 
/* 1091 */       localSet = null;
/*      */       try {
/* 1093 */         Class localClass = Class.forName(paramPrincipalEntry.principalClass, false, ClassLoader.getSystemClassLoader());
/*      */ 
/* 1095 */         localSet = ((SubjectCodeSource)localObject).getSubject().getPrincipals(localClass);
/*      */       } catch (Exception localException) {
/* 1097 */         if (debug != null) {
/* 1098 */           debug.println("problem finding Principal Class when expanding SELF permission: " + localException.toString());
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1104 */       if (localSet == null)
/*      */       {
/* 1106 */         return new String[0][0];
/*      */       }
/*      */ 
/* 1109 */       arrayOfString = new String[localSet.size()][2];
/* 1110 */       localIterator = localSet.iterator();
/*      */ 
/* 1112 */       i = 0;
/* 1113 */       while (localIterator.hasNext()) {
/* 1114 */         localPrincipal = (Principal)localIterator.next();
/* 1115 */         arrayOfString[i][0] = localPrincipal.getClass().getName();
/* 1116 */         arrayOfString[i][1] = localPrincipal.getName();
/* 1117 */         i++;
/*      */       }
/* 1119 */       return arrayOfString;
/*      */     }
/*      */ 
/* 1128 */     Object localObject = (SubjectCodeSource)paramCodeSource;
/* 1129 */     Set localSet = ((SubjectCodeSource)localObject).getSubject().getPrincipals();
/*      */ 
/* 1131 */     String[][] arrayOfString = new String[localSet.size()][2];
/* 1132 */     Iterator localIterator = localSet.iterator();
/*      */ 
/* 1134 */     int i = 0;
/* 1135 */     while (localIterator.hasNext()) {
/* 1136 */       localPrincipal = (Principal)localIterator.next();
/* 1137 */       arrayOfString[i][0] = localPrincipal.getClass().getName();
/* 1138 */       arrayOfString[i][1] = localPrincipal.getName();
/* 1139 */       i++;
/*      */     }
/* 1141 */     return arrayOfString;
/*      */   }
/*      */ 
/*      */   Certificate[] getSignerCertificates(CodeSource paramCodeSource)
/*      */   {
/* 1159 */     Certificate[] arrayOfCertificate1 = null;
/* 1160 */     if ((arrayOfCertificate1 = paramCodeSource.getCertificates()) == null)
/* 1161 */       return null;
/* 1162 */     for (int i = 0; i < arrayOfCertificate1.length; i++) {
/* 1163 */       if (!(arrayOfCertificate1[i] instanceof X509Certificate)) {
/* 1164 */         return paramCodeSource.getCertificates();
/*      */       }
/*      */     }
/*      */ 
/* 1168 */     i = 0;
/* 1169 */     int j = 0;
/* 1170 */     while (i < arrayOfCertificate1.length) {
/* 1171 */       j++;
/*      */ 
/* 1173 */       while ((i + 1 < arrayOfCertificate1.length) && (((X509Certificate)arrayOfCertificate1[i]).getIssuerDN().equals(((X509Certificate)arrayOfCertificate1[(i + 1)]).getSubjectDN())))
/*      */       {
/* 1175 */         i++;
/*      */       }
/* 1177 */       i++;
/*      */     }
/* 1179 */     if (j == arrayOfCertificate1.length)
/*      */     {
/* 1181 */       return arrayOfCertificate1;
/*      */     }
/* 1183 */     ArrayList localArrayList = new ArrayList();
/* 1184 */     i = 0;
/* 1185 */     while (i < arrayOfCertificate1.length) {
/* 1186 */       localArrayList.add(arrayOfCertificate1[i]);
/*      */ 
/* 1188 */       while ((i + 1 < arrayOfCertificate1.length) && (((X509Certificate)arrayOfCertificate1[i]).getIssuerDN().equals(((X509Certificate)arrayOfCertificate1[(i + 1)]).getSubjectDN())))
/*      */       {
/* 1190 */         i++;
/*      */       }
/* 1192 */       i++;
/*      */     }
/* 1194 */     Certificate[] arrayOfCertificate2 = new Certificate[localArrayList.size()];
/* 1195 */     localArrayList.toArray(arrayOfCertificate2);
/* 1196 */     return arrayOfCertificate2;
/*      */   }
/*      */ 
/*      */   private CodeSource canonicalizeCodebase(CodeSource paramCodeSource, boolean paramBoolean)
/*      */   {
/* 1201 */     Object localObject1 = paramCodeSource;
/* 1202 */     if ((paramCodeSource.getLocation() != null) && (paramCodeSource.getLocation().getProtocol().equalsIgnoreCase("file"))) {
/*      */       try
/*      */       {
/* 1205 */         String str = paramCodeSource.getLocation().getFile().replace('/', File.separatorChar);
/*      */ 
/* 1208 */         localObject2 = null;
/* 1209 */         if (str.endsWith("*"))
/*      */         {
/* 1212 */           str = str.substring(0, str.length() - 1);
/* 1213 */           int i = 0;
/* 1214 */           if (str.endsWith(File.separator))
/* 1215 */             i = 1;
/* 1216 */           if (str.equals("")) {
/* 1217 */             str = System.getProperty("user.dir");
/*      */           }
/* 1219 */           File localFile = new File(str);
/* 1220 */           str = localFile.getCanonicalPath();
/* 1221 */           StringBuffer localStringBuffer = new StringBuffer(str);
/*      */ 
/* 1225 */           if ((!str.endsWith(File.separator)) && ((i != 0) || (localFile.isDirectory())))
/*      */           {
/* 1227 */             localStringBuffer.append(File.separatorChar);
/* 1228 */           }localStringBuffer.append('*');
/* 1229 */           str = localStringBuffer.toString();
/*      */         } else {
/* 1231 */           str = new File(str).getCanonicalPath();
/*      */         }
/* 1233 */         localObject2 = new File(str).toURL();
/*      */ 
/* 1235 */         if ((paramCodeSource instanceof SubjectCodeSource)) {
/* 1236 */           SubjectCodeSource localSubjectCodeSource2 = (SubjectCodeSource)paramCodeSource;
/* 1237 */           if (paramBoolean) {
/* 1238 */             localObject1 = new SubjectCodeSource(localSubjectCodeSource2.getSubject(), localSubjectCodeSource2.getPrincipals(), (URL)localObject2, getSignerCertificates(localSubjectCodeSource2));
/*      */           }
/*      */           else
/*      */           {
/* 1244 */             localObject1 = new SubjectCodeSource(localSubjectCodeSource2.getSubject(), localSubjectCodeSource2.getPrincipals(), (URL)localObject2, localSubjectCodeSource2.getCertificates());
/*      */           }
/*      */ 
/*      */         }
/* 1251 */         else if (paramBoolean) {
/* 1252 */           localObject1 = new CodeSource((URL)localObject2, getSignerCertificates(paramCodeSource));
/*      */         }
/*      */         else {
/* 1255 */           localObject1 = new CodeSource((URL)localObject2, paramCodeSource.getCertificates());
/*      */         }
/*      */       }
/*      */       catch (IOException localIOException)
/*      */       {
/*      */         Object localObject2;
/* 1262 */         if (paramBoolean) {
/* 1263 */           if (!(paramCodeSource instanceof SubjectCodeSource)) {
/* 1264 */             localObject1 = new CodeSource(paramCodeSource.getLocation(), getSignerCertificates(paramCodeSource));
/*      */           }
/*      */           else {
/* 1267 */             localObject2 = (SubjectCodeSource)paramCodeSource;
/* 1268 */             localObject1 = new SubjectCodeSource(((SubjectCodeSource)localObject2).getSubject(), ((SubjectCodeSource)localObject2).getPrincipals(), ((SubjectCodeSource)localObject2).getLocation(), getSignerCertificates((CodeSource)localObject2));
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/* 1276 */     else if (paramBoolean) {
/* 1277 */       if (!(paramCodeSource instanceof SubjectCodeSource)) {
/* 1278 */         localObject1 = new CodeSource(paramCodeSource.getLocation(), getSignerCertificates(paramCodeSource));
/*      */       }
/*      */       else {
/* 1281 */         SubjectCodeSource localSubjectCodeSource1 = (SubjectCodeSource)paramCodeSource;
/* 1282 */         localObject1 = new SubjectCodeSource(localSubjectCodeSource1.getSubject(), localSubjectCodeSource1.getPrincipals(), localSubjectCodeSource1.getLocation(), getSignerCertificates(localSubjectCodeSource1));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1289 */     return localObject1;
/*      */   }
/*      */ 
/*      */   private static class PolicyEntry
/*      */   {
/*      */     CodeSource codesource;
/*      */     Vector<Permission> permissions;
/*      */ 
/*      */     PolicyEntry(CodeSource paramCodeSource)
/*      */     {
/* 1356 */       this.codesource = paramCodeSource;
/* 1357 */       this.permissions = new Vector();
/*      */     }
/*      */ 
/*      */     void add(Permission paramPermission)
/*      */     {
/* 1364 */       this.permissions.addElement(paramPermission);
/*      */     }
/*      */ 
/*      */     CodeSource getCodeSource()
/*      */     {
/* 1371 */       return this.codesource;
/*      */     }
/*      */ 
/*      */     public String toString() {
/* 1375 */       StringBuffer localStringBuffer = new StringBuffer();
/* 1376 */       localStringBuffer.append(PolicyFile.rb.getString("LPARAM"));
/* 1377 */       localStringBuffer.append(getCodeSource());
/* 1378 */       localStringBuffer.append("\n");
/* 1379 */       for (int i = 0; i < this.permissions.size(); i++) {
/* 1380 */         Permission localPermission = (Permission)this.permissions.elementAt(i);
/* 1381 */         localStringBuffer.append(PolicyFile.rb.getString("SPACE"));
/* 1382 */         localStringBuffer.append(PolicyFile.rb.getString("SPACE"));
/* 1383 */         localStringBuffer.append(localPermission);
/* 1384 */         localStringBuffer.append(PolicyFile.rb.getString("NEWLINE"));
/*      */       }
/* 1386 */       localStringBuffer.append(PolicyFile.rb.getString("RPARAM"));
/* 1387 */       localStringBuffer.append(PolicyFile.rb.getString("NEWLINE"));
/* 1388 */       return localStringBuffer.toString();
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.auth.PolicyFile
 * JD-Core Version:    0.6.2
 */