/*      */ package sun.security.provider;
/*      */ 
/*      */ import com.sun.security.auth.PrincipalComparator;
/*      */ import java.io.File;
/*      */ import java.io.FilePermission;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.PrintStream;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.NetPermission;
/*      */ import java.net.SocketPermission;
/*      */ import java.net.URI;
/*      */ import java.net.URL;
/*      */ import java.security.AccessController;
/*      */ import java.security.AllPermission;
/*      */ import java.security.CodeSource;
/*      */ import java.security.KeyStore;
/*      */ import java.security.KeyStoreException;
/*      */ import java.security.Permission;
/*      */ import java.security.PermissionCollection;
/*      */ import java.security.Permissions;
/*      */ import java.security.Policy;
/*      */ import java.security.Principal;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.security.ProtectionDomain;
/*      */ import java.security.Security;
/*      */ import java.security.UnresolvedPermission;
/*      */ import java.security.cert.Certificate;
/*      */ import java.security.cert.X509Certificate;
/*      */ import java.text.MessageFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.Map;
/*      */ import java.util.PropertyPermission;
/*      */ import java.util.Random;
/*      */ import java.util.Set;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.concurrent.atomic.AtomicReference;
/*      */ import javax.security.auth.Subject;
/*      */ import javax.security.auth.x500.X500Principal;
/*      */ import sun.misc.JavaSecurityProtectionDomainAccess;
/*      */ import sun.misc.JavaSecurityProtectionDomainAccess.ProtectionDomainCache;
/*      */ import sun.misc.SharedSecrets;
/*      */ import sun.net.www.ParseUtil;
/*      */ import sun.security.util.Debug;
/*      */ import sun.security.util.PolicyUtil;
/*      */ import sun.security.util.PropertyExpander;
/*      */ import sun.security.util.ResourcesMgr;
/*      */ import sun.security.util.SecurityConstants;
/*      */ 
/*      */ public class PolicyFile extends Policy
/*      */ {
/*  285 */   private static final Debug debug = Debug.getInstance("policy");
/*      */   private static final String NONE = "NONE";
/*      */   private static final String P11KEYSTORE = "PKCS11";
/*      */   private static final String SELF = "${{self}}";
/*      */   private static final String X500PRINCIPAL = "javax.security.auth.x500.X500Principal";
/*      */   private static final String POLICY = "java.security.policy";
/*      */   private static final String SECURITY_MANAGER = "java.security.manager";
/*      */   private static final String POLICY_URL = "policy.url.";
/*      */   private static final String AUTH_POLICY = "java.security.auth.policy";
/*      */   private static final String AUTH_POLICY_URL = "auth.policy.url.";
/*      */   private static final int DEFAULT_CACHE_SIZE = 1;
/*  302 */   private AtomicReference<PolicyInfo> policyInfo = new AtomicReference();
/*  303 */   private boolean constructed = false;
/*      */ 
/*  305 */   private boolean expandProperties = true;
/*  306 */   private boolean ignoreIdentityScope = true;
/*  307 */   private boolean allowSystemProperties = true;
/*  308 */   private boolean notUtf8 = false;
/*      */   private URL url;
/*  313 */   private static final Class[] PARAMS0 = new Class[0];
/*  314 */   private static final Class[] PARAMS1 = { String.class };
/*  315 */   private static final Class[] PARAMS2 = { String.class, String.class };
/*      */ 
/*      */   public PolicyFile()
/*      */   {
/*  322 */     init((URL)null);
/*      */   }
/*      */ 
/*      */   public PolicyFile(URL paramURL)
/*      */   {
/*  330 */     this.url = paramURL;
/*  331 */     init(paramURL);
/*      */   }
/*      */ 
/*      */   private void init(URL paramURL)
/*      */   {
/*  438 */     String str = (String)AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public String run() {
/*  441 */         PolicyFile.this.expandProperties = "true".equalsIgnoreCase(Security.getProperty("policy.expandProperties"));
/*      */ 
/*  443 */         PolicyFile.this.ignoreIdentityScope = "true".equalsIgnoreCase(Security.getProperty("policy.ignoreIdentityScope"));
/*      */ 
/*  445 */         PolicyFile.this.allowSystemProperties = "true".equalsIgnoreCase(Security.getProperty("policy.allowSystemProperty"));
/*      */ 
/*  447 */         PolicyFile.this.notUtf8 = "false".equalsIgnoreCase(System.getProperty("sun.security.policy.utf8"));
/*      */ 
/*  449 */         return System.getProperty("sun.security.policy.numcaches");
/*      */       }
/*      */     });
/*      */     int i;
/*  453 */     if (str != null)
/*      */       try {
/*  455 */         i = Integer.parseInt(str);
/*      */       } catch (NumberFormatException localNumberFormatException) {
/*  457 */         i = 1;
/*      */       }
/*      */     else {
/*  460 */       i = 1;
/*      */     }
/*      */ 
/*  463 */     PolicyInfo localPolicyInfo = new PolicyInfo(i);
/*  464 */     initPolicyFile(localPolicyInfo, paramURL);
/*  465 */     this.policyInfo.set(localPolicyInfo);
/*      */   }
/*      */ 
/*      */   private void initPolicyFile(final PolicyInfo paramPolicyInfo, final URL paramURL)
/*      */   {
/*  470 */     if (paramURL != null)
/*      */     {
/*  477 */       if (debug != null) {
/*  478 */         debug.println("reading " + paramURL);
/*      */       }
/*  480 */       AccessController.doPrivileged(new PrivilegedAction() {
/*      */         public Void run() {
/*  482 */           if (!PolicyFile.this.init(paramURL, paramPolicyInfo))
/*      */           {
/*  484 */             PolicyFile.this.initStaticPolicy(paramPolicyInfo);
/*      */           }
/*  486 */           return null;
/*      */         }
/*      */ 
/*      */       });
/*      */     }
/*      */     else
/*      */     {
/*  505 */       boolean bool = initPolicyFile("java.security.policy", "policy.url.", paramPolicyInfo);
/*      */ 
/*  508 */       if (!bool)
/*      */       {
/*  510 */         initStaticPolicy(paramPolicyInfo);
/*      */       }
/*      */ 
/*  513 */       initPolicyFile("java.security.auth.policy", "auth.policy.url.", paramPolicyInfo);
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean initPolicyFile(final String paramString1, final String paramString2, final PolicyInfo paramPolicyInfo)
/*      */   {
/*  519 */     Boolean localBoolean = (Boolean)AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public Boolean run() {
/*  522 */         boolean bool = false;
/*      */         Object localObject;
/*  524 */         if (PolicyFile.this.allowSystemProperties) {
/*  525 */           String str1 = System.getProperty(paramString1);
/*  526 */           if (str1 != null) {
/*  527 */             int j = 0;
/*  528 */             if (str1.startsWith("=")) {
/*  529 */               j = 1;
/*  530 */               str1 = str1.substring(1);
/*      */             }
/*      */             try {
/*  533 */               str1 = PropertyExpander.expand(str1);
/*      */ 
/*  537 */               localObject = new File(str1);
/*      */               URL localURL1;
/*  538 */               if (((File)localObject).exists()) {
/*  539 */                 localURL1 = ParseUtil.fileToEncodedURL(new File(((File)localObject).getCanonicalPath()));
/*      */               }
/*      */               else {
/*  542 */                 localURL1 = new URL(str1);
/*      */               }
/*  544 */               if (PolicyFile.debug != null)
/*  545 */                 PolicyFile.debug.println("reading " + localURL1);
/*  546 */               if (PolicyFile.this.init(localURL1, paramPolicyInfo))
/*  547 */                 bool = true;
/*      */             }
/*      */             catch (Exception localException1) {
/*  550 */               if (PolicyFile.debug != null) {
/*  551 */                 PolicyFile.debug.println("caught exception: " + localException1);
/*      */               }
/*      */             }
/*  554 */             if (j != 0) {
/*  555 */               if (PolicyFile.debug != null) {
/*  556 */                 PolicyFile.debug.println("overriding other policies!");
/*      */               }
/*  558 */               return Boolean.valueOf(bool);
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/*  563 */         int i = 1;
/*      */         String str2;
/*  566 */         while ((str2 = Security.getProperty(paramString2 + i)) != null) {
/*      */           try {
/*  568 */             URL localURL2 = null;
/*  569 */             localObject = PropertyExpander.expand(str2).replace(File.separatorChar, '/');
/*      */ 
/*  572 */             if ((str2.startsWith("file:${java.home}/")) || (str2.startsWith("file:${user.home}/")))
/*      */             {
/*  579 */               localURL2 = new File(((String)localObject).substring(5)).toURI().toURL();
/*      */             }
/*      */             else {
/*  582 */               localURL2 = new URI((String)localObject).toURL();
/*      */             }
/*      */ 
/*  585 */             if (PolicyFile.debug != null)
/*  586 */               PolicyFile.debug.println("reading " + localURL2);
/*  587 */             if (PolicyFile.this.init(localURL2, paramPolicyInfo))
/*  588 */               bool = true;
/*      */           } catch (Exception localException2) {
/*  590 */             if (PolicyFile.debug != null) {
/*  591 */               PolicyFile.debug.println("error reading policy " + localException2);
/*  592 */               localException2.printStackTrace();
/*      */             }
/*      */           }
/*      */ 
/*  596 */           i++;
/*      */         }
/*  598 */         return Boolean.valueOf(bool);
/*      */       }
/*      */     });
/*  602 */     return localBoolean.booleanValue();
/*      */   }
/*      */ 
/*      */   private boolean init(URL paramURL, PolicyInfo paramPolicyInfo)
/*      */   {
/*  612 */     boolean bool = false;
/*  613 */     PolicyParser localPolicyParser = new PolicyParser(this.expandProperties);
/*  614 */     InputStreamReader localInputStreamReader = null;
/*      */     try
/*      */     {
/*  622 */       if (this.notUtf8) {
/*  623 */         localInputStreamReader = new InputStreamReader(PolicyUtil.getInputStream(paramURL));
/*      */       }
/*      */       else {
/*  626 */         localInputStreamReader = new InputStreamReader(PolicyUtil.getInputStream(paramURL), "UTF-8");
/*      */       }
/*      */ 
/*  630 */       localPolicyParser.read(localInputStreamReader);
/*      */ 
/*  632 */       KeyStore localKeyStore = null;
/*      */       try {
/*  634 */         localKeyStore = PolicyUtil.getKeyStore(paramURL, localPolicyParser.getKeyStoreUrl(), localPolicyParser.getKeyStoreType(), localPolicyParser.getKeyStoreProvider(), localPolicyParser.getStorePassURL(), debug);
/*      */       }
/*      */       catch (Exception localException2)
/*      */       {
/*  643 */         if (debug != null) {
/*  644 */           localException2.printStackTrace();
/*      */         }
/*      */       }
/*      */ 
/*  648 */       localObject1 = localPolicyParser.grantElements();
/*  649 */       while (((Enumeration)localObject1).hasMoreElements()) {
/*  650 */         localObject2 = (PolicyParser.GrantEntry)((Enumeration)localObject1).nextElement();
/*  651 */         addGrantEntry((PolicyParser.GrantEntry)localObject2, localKeyStore, paramPolicyInfo);
/*      */       }
/*      */     } catch (PolicyParser.ParsingException localParsingException) {
/*  654 */       Object localObject1 = new MessageFormat(ResourcesMgr.getString("java.security.policy.error.parsing.policy.message"));
/*      */ 
/*  656 */       Object localObject2 = { paramURL, localParsingException.getLocalizedMessage() };
/*  657 */       System.err.println(((MessageFormat)localObject1).format(localObject2));
/*  658 */       if (debug != null)
/*  659 */         localParsingException.printStackTrace();
/*      */     }
/*      */     catch (Exception localException1) {
/*  662 */       if (debug != null) {
/*  663 */         debug.println("error parsing " + paramURL);
/*  664 */         debug.println(localException1.toString());
/*  665 */         localException1.printStackTrace();
/*      */       }
/*      */     } finally {
/*  668 */       if (localInputStreamReader != null)
/*      */         try {
/*  670 */           localInputStreamReader.close();
/*  671 */           bool = true;
/*      */         }
/*      */         catch (IOException localIOException4) {
/*      */         }
/*      */       else {
/*  676 */         bool = true;
/*      */       }
/*      */     }
/*      */ 
/*  680 */     return bool;
/*      */   }
/*      */ 
/*      */   private void initStaticPolicy(final PolicyInfo paramPolicyInfo) {
/*  684 */     AccessController.doPrivileged(new PrivilegedAction() {
/*      */       public Void run() {
/*  686 */         PolicyFile.PolicyEntry localPolicyEntry = new PolicyFile.PolicyEntry(new CodeSource(null, (Certificate[])null));
/*      */ 
/*  688 */         localPolicyEntry.add(SecurityConstants.LOCAL_LISTEN_PERMISSION);
/*  689 */         localPolicyEntry.add(new PropertyPermission("java.version", "read"));
/*      */ 
/*  691 */         localPolicyEntry.add(new PropertyPermission("java.vendor", "read"));
/*      */ 
/*  693 */         localPolicyEntry.add(new PropertyPermission("java.vendor.url", "read"));
/*      */ 
/*  695 */         localPolicyEntry.add(new PropertyPermission("java.class.version", "read"));
/*      */ 
/*  697 */         localPolicyEntry.add(new PropertyPermission("os.name", "read"));
/*      */ 
/*  699 */         localPolicyEntry.add(new PropertyPermission("os.version", "read"));
/*      */ 
/*  701 */         localPolicyEntry.add(new PropertyPermission("os.arch", "read"));
/*      */ 
/*  703 */         localPolicyEntry.add(new PropertyPermission("file.separator", "read"));
/*      */ 
/*  705 */         localPolicyEntry.add(new PropertyPermission("path.separator", "read"));
/*      */ 
/*  707 */         localPolicyEntry.add(new PropertyPermission("line.separator", "read"));
/*      */ 
/*  709 */         localPolicyEntry.add(new PropertyPermission("java.specification.version", "read"));
/*      */ 
/*  712 */         localPolicyEntry.add(new PropertyPermission("java.specification.vendor", "read"));
/*      */ 
/*  715 */         localPolicyEntry.add(new PropertyPermission("java.specification.name", "read"));
/*      */ 
/*  718 */         localPolicyEntry.add(new PropertyPermission("java.vm.specification.version", "read"));
/*      */ 
/*  721 */         localPolicyEntry.add(new PropertyPermission("java.vm.specification.vendor", "read"));
/*      */ 
/*  724 */         localPolicyEntry.add(new PropertyPermission("java.vm.specification.name", "read"));
/*      */ 
/*  727 */         localPolicyEntry.add(new PropertyPermission("java.vm.version", "read"));
/*      */ 
/*  729 */         localPolicyEntry.add(new PropertyPermission("java.vm.vendor", "read"));
/*      */ 
/*  731 */         localPolicyEntry.add(new PropertyPermission("java.vm.name", "read"));
/*      */ 
/*  735 */         paramPolicyInfo.policyEntries.add(localPolicyEntry);
/*      */ 
/*  738 */         String[] arrayOfString = PolicyParser.parseExtDirs("${{java.ext.dirs}}", 0);
/*      */ 
/*  740 */         if ((arrayOfString != null) && (arrayOfString.length > 0)) {
/*  741 */           for (int i = 0; i < arrayOfString.length; i++) {
/*      */             try {
/*  743 */               localPolicyEntry = new PolicyFile.PolicyEntry(PolicyFile.this.canonicalizeCodebase(new CodeSource(new URL(arrayOfString[i]), (Certificate[])null), false));
/*      */ 
/*  746 */               localPolicyEntry.add(SecurityConstants.ALL_PERMISSION);
/*      */ 
/*  750 */               paramPolicyInfo.policyEntries.add(localPolicyEntry);
/*      */             }
/*      */             catch (Exception localException)
/*      */             {
/*      */             }
/*      */           }
/*      */         }
/*  757 */         return null;
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   private CodeSource getCodeSource(PolicyParser.GrantEntry paramGrantEntry, KeyStore paramKeyStore, PolicyInfo paramPolicyInfo)
/*      */     throws MalformedURLException
/*      */   {
/*  770 */     Certificate[] arrayOfCertificate = null;
/*  771 */     if (paramGrantEntry.signedBy != null) {
/*  772 */       arrayOfCertificate = getCertificates(paramKeyStore, paramGrantEntry.signedBy, paramPolicyInfo);
/*  773 */       if (arrayOfCertificate == null)
/*      */       {
/*  776 */         if (debug != null) {
/*  777 */           debug.println("  -- No certs for alias '" + paramGrantEntry.signedBy + "' - ignoring entry");
/*      */         }
/*      */ 
/*  780 */         return null;
/*      */       }
/*      */     }
/*      */     URL localURL;
/*  786 */     if (paramGrantEntry.codeBase != null)
/*  787 */       localURL = new URL(paramGrantEntry.codeBase);
/*      */     else {
/*  789 */       localURL = null;
/*      */     }
/*  791 */     return canonicalizeCodebase(new CodeSource(localURL, arrayOfCertificate), false);
/*      */   }
/*      */ 
/*      */   private void addGrantEntry(PolicyParser.GrantEntry paramGrantEntry, KeyStore paramKeyStore, PolicyInfo paramPolicyInfo)
/*      */   {
/*      */     Object localObject1;
/*      */     Object localObject2;
/*  800 */     if (debug != null) {
/*  801 */       debug.println("Adding policy entry: ");
/*  802 */       debug.println("  signedBy " + paramGrantEntry.signedBy);
/*  803 */       debug.println("  codeBase " + paramGrantEntry.codeBase);
/*  804 */       if ((paramGrantEntry.principals != null) && (paramGrantEntry.principals.size() > 0)) {
/*  805 */         localObject1 = paramGrantEntry.principals.listIterator();
/*      */ 
/*  807 */         while (((ListIterator)localObject1).hasNext()) {
/*  808 */           localObject2 = (PolicyParser.PrincipalEntry)((ListIterator)localObject1).next();
/*  809 */           debug.println("  " + ((PolicyParser.PrincipalEntry)localObject2).toString());
/*      */         }
/*      */       }
/*      */     }
/*      */     try
/*      */     {
/*  815 */       localObject1 = getCodeSource(paramGrantEntry, paramKeyStore, paramPolicyInfo);
/*      */ 
/*  817 */       if (localObject1 == null) return;
/*      */ 
/*  823 */       if (!replacePrincipals(paramGrantEntry.principals, paramKeyStore))
/*  824 */         return;
/*  825 */       localObject2 = new PolicyEntry((CodeSource)localObject1, paramGrantEntry.principals);
/*  826 */       localObject3 = paramGrantEntry.permissionElements();
/*      */ 
/*  828 */       while (((Enumeration)localObject3).hasMoreElements()) {
/*  829 */         PolicyParser.PermissionEntry localPermissionEntry = (PolicyParser.PermissionEntry)((Enumeration)localObject3).nextElement();
/*      */         try
/*      */         {
/*  833 */           expandPermissionName(localPermissionEntry, paramKeyStore);
/*      */ 
/*  837 */           if ((localPermissionEntry.permission.equals("javax.security.auth.PrivateCredentialPermission")) && (localPermissionEntry.name.endsWith(" self")))
/*      */           {
/*  840 */             localPermissionEntry.name = (localPermissionEntry.name.substring(0, localPermissionEntry.name.indexOf("self")) + "${{self}}");
/*      */           }
/*      */           Object localObject4;
/*  844 */           if ((localPermissionEntry.name != null) && (localPermissionEntry.name.indexOf("${{self}}") != -1))
/*      */           {
/*  850 */             if (localPermissionEntry.signedBy != null) {
/*  851 */               localObject5 = getCertificates(paramKeyStore, localPermissionEntry.signedBy, paramPolicyInfo);
/*      */             }
/*      */             else
/*      */             {
/*  855 */               localObject5 = null;
/*      */             }
/*  857 */             localObject4 = new SelfPermission(localPermissionEntry.permission, localPermissionEntry.name, localPermissionEntry.action, (Certificate[])localObject5);
/*      */           }
/*      */           else
/*      */           {
/*  862 */             localObject4 = getInstance(localPermissionEntry.permission, localPermissionEntry.name, localPermissionEntry.action);
/*      */           }
/*      */ 
/*  866 */           ((PolicyEntry)localObject2).add((Permission)localObject4);
/*  867 */           if (debug != null)
/*  868 */             debug.println("  " + localObject4);
/*      */         }
/*      */         catch (ClassNotFoundException localClassNotFoundException)
/*      */         {
/*  872 */           if (localPermissionEntry.signedBy != null) {
/*  873 */             localObject5 = getCertificates(paramKeyStore, localPermissionEntry.signedBy, paramPolicyInfo);
/*      */           }
/*      */           else
/*      */           {
/*  877 */             localObject5 = null;
/*      */           }
/*      */ 
/*  882 */           if ((localObject5 != null) || (localPermissionEntry.signedBy == null)) {
/*  883 */             localObject6 = new UnresolvedPermission(localPermissionEntry.permission, localPermissionEntry.name, localPermissionEntry.action, (Certificate[])localObject5);
/*      */ 
/*  888 */             ((PolicyEntry)localObject2).add((Permission)localObject6);
/*  889 */             if (debug != null)
/*  890 */               debug.println("  " + localObject6);
/*      */           }
/*      */         }
/*      */         catch (InvocationTargetException localInvocationTargetException) {
/*  894 */           localObject5 = new MessageFormat(ResourcesMgr.getString("java.security.policy.error.adding.Permission.perm.message"));
/*      */ 
/*  898 */           localObject6 = new Object[] { localPermissionEntry.permission, localInvocationTargetException.getTargetException().toString() };
/*      */ 
/*  900 */           System.err.println(((MessageFormat)localObject5).format(localObject6));
/*      */         } catch (Exception localException2) {
/*  902 */           Object localObject5 = new MessageFormat(ResourcesMgr.getString("java.security.policy.error.adding.Permission.perm.message"));
/*      */ 
/*  906 */           Object localObject6 = { localPermissionEntry.permission, localException2.toString() };
/*      */ 
/*  908 */           System.err.println(((MessageFormat)localObject5).format(localObject6));
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  913 */       paramPolicyInfo.policyEntries.add(localObject2);
/*      */     } catch (Exception localException1) {
/*  915 */       localObject2 = new MessageFormat(ResourcesMgr.getString("java.security.policy.error.adding.Entry.message"));
/*      */ 
/*  918 */       Object localObject3 = { localException1.toString() };
/*  919 */       System.err.println(((MessageFormat)localObject2).format(localObject3));
/*      */     }
/*  921 */     if (debug != null)
/*  922 */       debug.println();
/*      */   }
/*      */ 
/*      */   private static final Permission getInstance(String paramString1, String paramString2, String paramString3)
/*      */     throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException
/*      */   {
/*  965 */     Class localClass = Class.forName(paramString1);
/*  966 */     Permission localPermission = getKnownInstance(localClass, paramString2, paramString3);
/*  967 */     if (localPermission != null) {
/*  968 */       return localPermission;
/*      */     }
/*      */ 
/*  971 */     if ((paramString2 == null) && (paramString3 == null)) {
/*      */       try {
/*  973 */         Constructor localConstructor1 = localClass.getConstructor(PARAMS0);
/*  974 */         return (Permission)localConstructor1.newInstance(new Object[0]);
/*      */       } catch (NoSuchMethodException localNoSuchMethodException1) {
/*      */         try {
/*  977 */           Constructor localConstructor4 = localClass.getConstructor(PARAMS1);
/*  978 */           return (Permission)localConstructor4.newInstance(new Object[] { paramString2 });
/*      */         }
/*      */         catch (NoSuchMethodException localNoSuchMethodException3) {
/*  981 */           Constructor localConstructor6 = localClass.getConstructor(PARAMS2);
/*  982 */           return (Permission)localConstructor6.newInstance(new Object[] { paramString2, paramString3 });
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  987 */     if ((paramString2 != null) && (paramString3 == null)) {
/*      */       try {
/*  989 */         Constructor localConstructor2 = localClass.getConstructor(PARAMS1);
/*  990 */         return (Permission)localConstructor2.newInstance(new Object[] { paramString2 });
/*      */       } catch (NoSuchMethodException localNoSuchMethodException2) {
/*  992 */         Constructor localConstructor5 = localClass.getConstructor(PARAMS2);
/*  993 */         return (Permission)localConstructor5.newInstance(new Object[] { paramString2, paramString3 });
/*      */       }
/*      */     }
/*      */ 
/*  997 */     Constructor localConstructor3 = localClass.getConstructor(PARAMS2);
/*  998 */     return (Permission)localConstructor3.newInstance(new Object[] { paramString2, paramString3 });
/*      */   }
/*      */ 
/*      */   private static final Permission getKnownInstance(Class paramClass, String paramString1, String paramString2)
/*      */   {
/* 1012 */     if (paramClass.equals(FilePermission.class))
/* 1013 */       return new FilePermission(paramString1, paramString2);
/* 1014 */     if (paramClass.equals(SocketPermission.class))
/* 1015 */       return new SocketPermission(paramString1, paramString2);
/* 1016 */     if (paramClass.equals(RuntimePermission.class))
/* 1017 */       return new RuntimePermission(paramString1, paramString2);
/* 1018 */     if (paramClass.equals(PropertyPermission.class))
/* 1019 */       return new PropertyPermission(paramString1, paramString2);
/* 1020 */     if (paramClass.equals(NetPermission.class))
/* 1021 */       return new NetPermission(paramString1, paramString2);
/* 1022 */     if (paramClass.equals(AllPermission.class)) {
/* 1023 */       return SecurityConstants.ALL_PERMISSION;
/*      */     }
/*      */ 
/* 1049 */     return null;
/*      */   }
/*      */ 
/*      */   private Certificate[] getCertificates(KeyStore paramKeyStore, String paramString, PolicyInfo paramPolicyInfo)
/*      */   {
/* 1059 */     ArrayList localArrayList = null;
/*      */ 
/* 1061 */     StringTokenizer localStringTokenizer = new StringTokenizer(paramString, ",");
/* 1062 */     int i = 0;
/*      */     Object localObject1;
/* 1064 */     while (localStringTokenizer.hasMoreTokens()) {
/* 1065 */       localObject1 = localStringTokenizer.nextToken().trim();
/* 1066 */       i++;
/* 1067 */       Certificate localCertificate = null;
/*      */ 
/* 1069 */       synchronized (paramPolicyInfo.aliasMapping) {
/* 1070 */         localCertificate = (Certificate)paramPolicyInfo.aliasMapping.get(localObject1);
/*      */ 
/* 1072 */         if ((localCertificate == null) && (paramKeyStore != null))
/*      */         {
/*      */           try {
/* 1075 */             localCertificate = paramKeyStore.getCertificate((String)localObject1);
/*      */           }
/*      */           catch (KeyStoreException localKeyStoreException)
/*      */           {
/*      */           }
/* 1080 */           if (localCertificate != null) {
/* 1081 */             paramPolicyInfo.aliasMapping.put(localObject1, localCertificate);
/* 1082 */             paramPolicyInfo.aliasMapping.put(localCertificate, localObject1);
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/* 1087 */       if (localCertificate != null) {
/* 1088 */         if (localArrayList == null)
/* 1089 */           localArrayList = new ArrayList();
/* 1090 */         localArrayList.add(localCertificate);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1095 */     if ((localArrayList != null) && (i == localArrayList.size())) {
/* 1096 */       localObject1 = new Certificate[localArrayList.size()];
/* 1097 */       localArrayList.toArray((Object[])localObject1);
/* 1098 */       return localObject1;
/*      */     }
/* 1100 */     return null;
/*      */   }
/*      */ 
/*      */   public void refresh()
/*      */   {
/* 1108 */     init(this.url);
/*      */   }
/*      */ 
/*      */   public boolean implies(ProtectionDomain paramProtectionDomain, Permission paramPermission)
/*      */   {
/* 1126 */     PolicyInfo localPolicyInfo = (PolicyInfo)this.policyInfo.get();
/* 1127 */     JavaSecurityProtectionDomainAccess.ProtectionDomainCache localProtectionDomainCache = localPolicyInfo.getPdMapping();
/*      */ 
/* 1129 */     PermissionCollection localPermissionCollection = localProtectionDomainCache.get(paramProtectionDomain);
/*      */ 
/* 1131 */     if (localPermissionCollection != null) {
/* 1132 */       return localPermissionCollection.implies(paramPermission);
/*      */     }
/*      */ 
/* 1135 */     localPermissionCollection = getPermissions(paramProtectionDomain);
/* 1136 */     if (localPermissionCollection == null) {
/* 1137 */       return false;
/*      */     }
/*      */ 
/* 1141 */     localProtectionDomainCache.put(paramProtectionDomain, localPermissionCollection);
/* 1142 */     return localPermissionCollection.implies(paramPermission);
/*      */   }
/*      */ 
/*      */   public PermissionCollection getPermissions(ProtectionDomain paramProtectionDomain)
/*      */   {
/* 1175 */     Permissions localPermissions = new Permissions();
/*      */ 
/* 1177 */     if (paramProtectionDomain == null) {
/* 1178 */       return localPermissions;
/*      */     }
/*      */ 
/* 1181 */     getPermissions(localPermissions, paramProtectionDomain);
/*      */ 
/* 1186 */     PermissionCollection localPermissionCollection = paramProtectionDomain.getPermissions();
/* 1187 */     if (localPermissionCollection != null) {
/* 1188 */       synchronized (localPermissionCollection) {
/* 1189 */         Enumeration localEnumeration = localPermissionCollection.elements();
/* 1190 */         while (localEnumeration.hasMoreElements()) {
/* 1191 */           localPermissions.add((Permission)localEnumeration.nextElement());
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1196 */     return localPermissions;
/*      */   }
/*      */ 
/*      */   public PermissionCollection getPermissions(CodeSource paramCodeSource)
/*      */   {
/* 1211 */     return getPermissions(new Permissions(), paramCodeSource);
/*      */   }
/*      */ 
/*      */   private PermissionCollection getPermissions(Permissions paramPermissions, ProtectionDomain paramProtectionDomain)
/*      */   {
/* 1226 */     if (debug != null) {
/* 1227 */       debug.println("getPermissions:\n\t" + printPD(paramProtectionDomain));
/*      */     }
/*      */ 
/* 1230 */     final CodeSource localCodeSource1 = paramProtectionDomain.getCodeSource();
/* 1231 */     if (localCodeSource1 == null) {
/* 1232 */       return paramPermissions;
/*      */     }
/* 1234 */     CodeSource localCodeSource2 = (CodeSource)AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public CodeSource run() {
/* 1237 */         return PolicyFile.this.canonicalizeCodebase(localCodeSource1, true);
/*      */       }
/*      */     });
/* 1240 */     return getPermissions(paramPermissions, localCodeSource2, paramProtectionDomain.getPrincipals());
/*      */   }
/*      */ 
/*      */   private PermissionCollection getPermissions(Permissions paramPermissions, final CodeSource paramCodeSource)
/*      */   {
/* 1258 */     CodeSource localCodeSource = (CodeSource)AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public CodeSource run() {
/* 1261 */         return PolicyFile.this.canonicalizeCodebase(paramCodeSource, true);
/*      */       }
/*      */     });
/* 1265 */     return getPermissions(paramPermissions, localCodeSource, null);
/*      */   }
/*      */ 
/*      */   private Permissions getPermissions(Permissions paramPermissions, CodeSource paramCodeSource, Principal[] paramArrayOfPrincipal)
/*      */   {
/* 1271 */     PolicyInfo localPolicyInfo = (PolicyInfo)this.policyInfo.get();
/*      */ 
/* 1273 */     for (Iterator localIterator = localPolicyInfo.policyEntries.iterator(); localIterator.hasNext(); ) { localObject1 = (PolicyEntry)localIterator.next();
/* 1274 */       addPermissions(paramPermissions, paramCodeSource, paramArrayOfPrincipal, (PolicyEntry)localObject1);
/*      */     }
/*      */     Object localObject1;
/*      */     Object localObject2;
/* 1279 */     synchronized (localPolicyInfo.identityPolicyEntries) {
/* 1280 */       for (localObject1 = localPolicyInfo.identityPolicyEntries.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (PolicyEntry)((Iterator)localObject1).next();
/* 1281 */         addPermissions(paramPermissions, paramCodeSource, paramArrayOfPrincipal, (PolicyEntry)localObject2);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1286 */     if (!this.ignoreIdentityScope) {
/* 1287 */       ??? = paramCodeSource.getCertificates();
/* 1288 */       if (??? != null) {
/* 1289 */         for (int i = 0; i < ???.length; i++) {
/* 1290 */           localObject2 = localPolicyInfo.aliasMapping.get(???[i]);
/* 1291 */           if ((localObject2 == null) && (checkForTrustedIdentity(???[i], localPolicyInfo)))
/*      */           {
/* 1297 */             paramPermissions.add(SecurityConstants.ALL_PERMISSION);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1302 */     return paramPermissions;
/*      */   }
/*      */ 
/*      */   private void addPermissions(Permissions paramPermissions, final CodeSource paramCodeSource, Principal[] paramArrayOfPrincipal, final PolicyEntry paramPolicyEntry)
/*      */   {
/* 1310 */     if (debug != null) {
/* 1311 */       debug.println("evaluate codesources:\n\tPolicy CodeSource: " + paramPolicyEntry.getCodeSource() + "\n" + "\tActive CodeSource: " + paramCodeSource);
/*      */     }
/*      */ 
/* 1317 */     Boolean localBoolean = (Boolean)AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public Boolean run() {
/* 1320 */         return new Boolean(paramPolicyEntry.getCodeSource().implies(paramCodeSource));
/*      */       }
/*      */     });
/* 1323 */     if (!localBoolean.booleanValue()) {
/* 1324 */       if (debug != null) {
/* 1325 */         debug.println("evaluation (codesource) failed");
/*      */       }
/*      */ 
/* 1329 */       return;
/*      */     }
/*      */ 
/* 1334 */     List localList = paramPolicyEntry.getPrincipals();
/* 1335 */     if (debug != null) {
/* 1336 */       ArrayList localArrayList = new ArrayList();
/* 1337 */       if (paramArrayOfPrincipal != null) {
/* 1338 */         for (int j = 0; j < paramArrayOfPrincipal.length; j++) {
/* 1339 */           localArrayList.add(new PolicyParser.PrincipalEntry(paramArrayOfPrincipal[j].getClass().getName(), paramArrayOfPrincipal[j].getName()));
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1344 */       debug.println("evaluate principals:\n\tPolicy Principals: " + localList + "\n" + "\tActive Principals: " + localArrayList);
/*      */     }
/*      */ 
/* 1349 */     if ((localList == null) || (localList.size() == 0))
/*      */     {
/* 1354 */       addPerms(paramPermissions, paramArrayOfPrincipal, paramPolicyEntry);
/* 1355 */       if (debug != null) {
/* 1356 */         debug.println("evaluation (codesource/principals) passed");
/*      */       }
/* 1358 */       return;
/*      */     }
/* 1360 */     if ((paramArrayOfPrincipal == null) || (paramArrayOfPrincipal.length == 0))
/*      */     {
/* 1365 */       if (debug != null) {
/* 1366 */         debug.println("evaluation (principals) failed");
/*      */       }
/* 1368 */       return;
/*      */     }
/*      */ 
/* 1375 */     for (int i = 0; i < localList.size(); i++) {
/* 1376 */       PolicyParser.PrincipalEntry localPrincipalEntry = (PolicyParser.PrincipalEntry)localList.get(i);
/*      */       try
/*      */       {
/* 1381 */         Class localClass = Class.forName(localPrincipalEntry.principalClass, true, Thread.currentThread().getContextClassLoader());
/*      */ 
/* 1386 */         if (!PrincipalComparator.class.isAssignableFrom(localClass))
/*      */         {
/* 1391 */           if (!checkEntryPs(paramArrayOfPrincipal, localPrincipalEntry)) {
/* 1392 */             if (debug != null) {
/* 1393 */               debug.println("evaluation (principals) failed");
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/* 1405 */           Constructor localConstructor = localClass.getConstructor(PARAMS1);
/* 1406 */           PrincipalComparator localPrincipalComparator = (PrincipalComparator)localConstructor.newInstance(new Object[] { localPrincipalEntry.principalName });
/*      */ 
/* 1409 */           if (debug != null) {
/* 1410 */             debug.println("found PrincipalComparator " + localPrincipalComparator.getClass().getName());
/*      */           }
/*      */ 
/* 1417 */           HashSet localHashSet = new HashSet(paramArrayOfPrincipal.length);
/* 1418 */           for (int k = 0; k < paramArrayOfPrincipal.length; k++) {
/* 1419 */             localHashSet.add(paramArrayOfPrincipal[k]);
/*      */           }
/* 1421 */           Subject localSubject = new Subject(true, localHashSet, Collections.EMPTY_SET, Collections.EMPTY_SET);
/*      */ 
/* 1426 */           if (!localPrincipalComparator.implies(localSubject)) {
/* 1427 */             if (debug != null) {
/* 1428 */               debug.println("evaluation (principal comparator) failed");
/*      */             }
/*      */ 
/* 1434 */             return;
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */       catch (Exception localException)
/*      */       {
/* 1441 */         if (debug != null) {
/* 1442 */           localException.printStackTrace();
/*      */         }
/*      */ 
/* 1445 */         if (!checkEntryPs(paramArrayOfPrincipal, localPrincipalEntry)) {
/* 1446 */           if (debug != null) {
/* 1447 */             debug.println("evaluation (principals) failed");
/*      */           }
/*      */ 
/* 1452 */           return;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1464 */     if (debug != null) {
/* 1465 */       debug.println("evaluation (codesource/principals) passed");
/*      */     }
/* 1467 */     addPerms(paramPermissions, paramArrayOfPrincipal, paramPolicyEntry);
/*      */   }
/*      */ 
/*      */   private void addPerms(Permissions paramPermissions, Principal[] paramArrayOfPrincipal, PolicyEntry paramPolicyEntry)
/*      */   {
/* 1473 */     for (int i = 0; i < paramPolicyEntry.permissions.size(); i++) {
/* 1474 */       Permission localPermission = (Permission)paramPolicyEntry.permissions.get(i);
/* 1475 */       if (debug != null) {
/* 1476 */         debug.println("  granting " + localPermission);
/*      */       }
/*      */ 
/* 1479 */       if ((localPermission instanceof SelfPermission))
/*      */       {
/* 1481 */         expandSelf((SelfPermission)localPermission, paramPolicyEntry.getPrincipals(), paramArrayOfPrincipal, paramPermissions);
/*      */       }
/*      */       else
/*      */       {
/* 1486 */         paramPermissions.add(localPermission);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean checkEntryPs(Principal[] paramArrayOfPrincipal, PolicyParser.PrincipalEntry paramPrincipalEntry)
/*      */   {
/* 1513 */     for (int i = 0; i < paramArrayOfPrincipal.length; i++)
/*      */     {
/* 1515 */       if ((paramPrincipalEntry.principalClass.equals("WILDCARD_PRINCIPAL_CLASS")) || (paramPrincipalEntry.principalClass.equals(paramArrayOfPrincipal[i].getClass().getName())))
/*      */       {
/* 1520 */         if ((paramPrincipalEntry.principalName.equals("WILDCARD_PRINCIPAL_NAME")) || (paramPrincipalEntry.principalName.equals(paramArrayOfPrincipal[i].getName())))
/*      */         {
/* 1525 */           return true;
/*      */         }
/*      */       }
/*      */     }
/* 1529 */     return false;
/*      */   }
/*      */ 
/*      */   private void expandSelf(SelfPermission paramSelfPermission, List<PolicyParser.PrincipalEntry> paramList, Principal[] paramArrayOfPrincipal, Permissions paramPermissions)
/*      */   {
/* 1550 */     if ((paramList == null) || (paramList.size() == 0))
/*      */     {
/* 1552 */       if (debug != null) {
/* 1553 */         debug.println("Ignoring permission " + paramSelfPermission.getSelfType() + " with target name (" + paramSelfPermission.getSelfName() + ").  " + "No Principal(s) specified " + "in the grant clause.  " + "SELF-based target names are " + "only valid in the context " + "of a Principal-based grant entry.");
/*      */       }
/*      */ 
/* 1564 */       return;
/*      */     }
/* 1566 */     int i = 0;
/*      */ 
/* 1568 */     StringBuilder localStringBuilder = new StringBuilder();
/*      */     int j;
/*      */     Object localObject1;
/* 1569 */     while ((j = paramSelfPermission.getSelfName().indexOf("${{self}}", i)) != -1)
/*      */     {
/* 1572 */       localStringBuilder.append(paramSelfPermission.getSelfName().substring(i, j));
/*      */ 
/* 1575 */       ListIterator localListIterator = paramList.listIterator();
/*      */ 
/* 1577 */       while (localListIterator.hasNext()) {
/* 1578 */         localObject1 = (PolicyParser.PrincipalEntry)localListIterator.next();
/* 1579 */         String[][] arrayOfString = getPrincipalInfo((PolicyParser.PrincipalEntry)localObject1, paramArrayOfPrincipal);
/* 1580 */         for (int k = 0; k < arrayOfString.length; k++) {
/* 1581 */           if (k != 0) {
/* 1582 */             localStringBuilder.append(", ");
/*      */           }
/* 1584 */           localStringBuilder.append(arrayOfString[k][0] + " " + "\"" + arrayOfString[k][1] + "\"");
/*      */         }
/*      */ 
/* 1587 */         if (localListIterator.hasNext()) {
/* 1588 */           localStringBuilder.append(", ");
/*      */         }
/*      */       }
/* 1591 */       i = j + "${{self}}".length();
/*      */     }
/*      */ 
/* 1594 */     localStringBuilder.append(paramSelfPermission.getSelfName().substring(i));
/*      */ 
/* 1596 */     if (debug != null) {
/* 1597 */       debug.println("  expanded:\n\t" + paramSelfPermission.getSelfName() + "\n  into:\n\t" + localStringBuilder.toString());
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/* 1602 */       paramPermissions.add(getInstance(paramSelfPermission.getSelfType(), localStringBuilder.toString(), paramSelfPermission.getSelfActions()));
/*      */     }
/*      */     catch (ClassNotFoundException localClassNotFoundException)
/*      */     {
/* 1611 */       localObject1 = null;
/* 1612 */       synchronized (paramPermissions) {
/* 1613 */         Enumeration localEnumeration = paramPermissions.elements();
/* 1614 */         while (localEnumeration.hasMoreElements()) {
/* 1615 */           Permission localPermission = (Permission)localEnumeration.nextElement();
/* 1616 */           if (localPermission.getClass().getName().equals(paramSelfPermission.getSelfType())) {
/* 1617 */             localObject1 = localPermission.getClass();
/* 1618 */             break;
/*      */           }
/*      */         }
/*      */       }
/* 1622 */       if (localObject1 == null)
/*      */       {
/* 1624 */         paramPermissions.add(new UnresolvedPermission(paramSelfPermission.getSelfType(), localStringBuilder.toString(), paramSelfPermission.getSelfActions(), paramSelfPermission.getCerts()));
/*      */       }
/*      */       else
/*      */       {
/*      */         try
/*      */         {
/* 1634 */           if (paramSelfPermission.getSelfActions() == null) {
/*      */             try {
/* 1636 */               ??? = ((Class)localObject1).getConstructor(PARAMS1);
/* 1637 */               paramPermissions.add((Permission)((Constructor)???).newInstance(new Object[] { localStringBuilder.toString() }));
/*      */             }
/*      */             catch (NoSuchMethodException localNoSuchMethodException) {
/* 1640 */               ??? = ((Class)localObject1).getConstructor(PARAMS2);
/* 1641 */               paramPermissions.add((Permission)((Constructor)???).newInstance(new Object[] { localStringBuilder.toString(), paramSelfPermission.getSelfActions() }));
/*      */             }
/*      */           }
/*      */           else
/*      */           {
/* 1646 */             ??? = ((Class)localObject1).getConstructor(PARAMS2);
/* 1647 */             paramPermissions.add((Permission)((Constructor)???).newInstance(new Object[] { localStringBuilder.toString(), paramSelfPermission.getSelfActions() }));
/*      */           }
/*      */         }
/*      */         catch (Exception localException2)
/*      */         {
/* 1652 */           if (debug != null) {
/* 1653 */             debug.println("self entry expansion  instantiation failed: " + localException2.toString());
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (Exception localException1)
/*      */     {
/* 1660 */       if (debug != null)
/* 1661 */         debug.println(localException1.toString());
/*      */     }
/*      */   }
/*      */ 
/*      */   private String[][] getPrincipalInfo(PolicyParser.PrincipalEntry paramPrincipalEntry, Principal[] paramArrayOfPrincipal)
/*      */   {
/* 1680 */     if ((!paramPrincipalEntry.principalClass.equals("WILDCARD_PRINCIPAL_CLASS")) && (!paramPrincipalEntry.principalName.equals("WILDCARD_PRINCIPAL_NAME")))
/*      */     {
/* 1687 */       localObject = new String[1][2];
/* 1688 */       localObject[0][0] = paramPrincipalEntry.principalClass;
/* 1689 */       localObject[0][1] = paramPrincipalEntry.principalName;
/* 1690 */       return localObject;
/*      */     }
/* 1692 */     if ((!paramPrincipalEntry.principalClass.equals("WILDCARD_PRINCIPAL_CLASS")) && (paramPrincipalEntry.principalName.equals("WILDCARD_PRINCIPAL_NAME")))
/*      */     {
/* 1700 */       localObject = new ArrayList();
/* 1701 */       for (int i = 0; i < paramArrayOfPrincipal.length; i++) {
/* 1702 */         if (paramPrincipalEntry.principalClass.equals(paramArrayOfPrincipal[i].getClass().getName()))
/* 1703 */           ((List)localObject).add(paramArrayOfPrincipal[i]);
/*      */       }
/* 1705 */       String[][] arrayOfString = new String[((List)localObject).size()][2];
/* 1706 */       int k = 0;
/* 1707 */       Iterator localIterator = ((List)localObject).iterator();
/* 1708 */       while (localIterator.hasNext()) {
/* 1709 */         Principal localPrincipal = (Principal)localIterator.next();
/* 1710 */         arrayOfString[k][0] = localPrincipal.getClass().getName();
/* 1711 */         arrayOfString[k][1] = localPrincipal.getName();
/* 1712 */         k++;
/*      */       }
/* 1714 */       return arrayOfString;
/*      */     }
/*      */ 
/* 1721 */     Object localObject = new String[paramArrayOfPrincipal.length][2];
/*      */ 
/* 1723 */     for (int j = 0; j < paramArrayOfPrincipal.length; j++) {
/* 1724 */       localObject[j][0] = paramArrayOfPrincipal[j].getClass().getName();
/* 1725 */       localObject[j][1] = paramArrayOfPrincipal[j].getName();
/*      */     }
/* 1727 */     return localObject;
/*      */   }
/*      */ 
/*      */   protected Certificate[] getSignerCertificates(CodeSource paramCodeSource)
/*      */   {
/* 1746 */     Certificate[] arrayOfCertificate1 = null;
/* 1747 */     if ((arrayOfCertificate1 = paramCodeSource.getCertificates()) == null)
/* 1748 */       return null;
/* 1749 */     for (int i = 0; i < arrayOfCertificate1.length; i++) {
/* 1750 */       if (!(arrayOfCertificate1[i] instanceof X509Certificate)) {
/* 1751 */         return paramCodeSource.getCertificates();
/*      */       }
/*      */     }
/*      */ 
/* 1755 */     i = 0;
/* 1756 */     int j = 0;
/* 1757 */     while (i < arrayOfCertificate1.length) {
/* 1758 */       j++;
/*      */ 
/* 1760 */       while ((i + 1 < arrayOfCertificate1.length) && (((X509Certificate)arrayOfCertificate1[i]).getIssuerDN().equals(((X509Certificate)arrayOfCertificate1[(i + 1)]).getSubjectDN())))
/*      */       {
/* 1762 */         i++;
/*      */       }
/* 1764 */       i++;
/*      */     }
/* 1766 */     if (j == arrayOfCertificate1.length)
/*      */     {
/* 1768 */       return arrayOfCertificate1;
/*      */     }
/* 1770 */     ArrayList localArrayList = new ArrayList();
/* 1771 */     i = 0;
/* 1772 */     while (i < arrayOfCertificate1.length) {
/* 1773 */       localArrayList.add(arrayOfCertificate1[i]);
/*      */ 
/* 1775 */       while ((i + 1 < arrayOfCertificate1.length) && (((X509Certificate)arrayOfCertificate1[i]).getIssuerDN().equals(((X509Certificate)arrayOfCertificate1[(i + 1)]).getSubjectDN())))
/*      */       {
/* 1777 */         i++;
/*      */       }
/* 1779 */       i++;
/*      */     }
/* 1781 */     Certificate[] arrayOfCertificate2 = new Certificate[localArrayList.size()];
/* 1782 */     localArrayList.toArray(arrayOfCertificate2);
/* 1783 */     return arrayOfCertificate2;
/*      */   }
/*      */ 
/*      */   private CodeSource canonicalizeCodebase(CodeSource paramCodeSource, boolean paramBoolean)
/*      */   {
/* 1789 */     String str1 = null;
/*      */ 
/* 1791 */     CodeSource localCodeSource = paramCodeSource;
/* 1792 */     URL localURL1 = paramCodeSource.getLocation();
/* 1793 */     if (localURL1 != null) {
/* 1794 */       if (localURL1.getProtocol().equals("jar"))
/*      */       {
/* 1796 */         String str2 = localURL1.getFile();
/* 1797 */         int j = str2.indexOf("!/");
/* 1798 */         if (j != -1) {
/*      */           try {
/* 1800 */             localURL1 = new URL(str2.substring(0, j));
/*      */           }
/*      */           catch (MalformedURLException localMalformedURLException)
/*      */           {
/*      */           }
/*      */         }
/*      */       }
/* 1807 */       if (localURL1.getProtocol().equals("file")) {
/* 1808 */         int i = 0;
/* 1809 */         String str3 = localURL1.getHost();
/* 1810 */         i = (str3 == null) || (str3.equals("")) || (str3.equals("~")) || (str3.equalsIgnoreCase("localhost")) ? 1 : 0;
/*      */ 
/* 1813 */         if (i != 0) {
/* 1814 */           str1 = localURL1.getFile().replace('/', File.separatorChar);
/* 1815 */           str1 = ParseUtil.decode(str1);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1820 */     if (str1 != null) {
/*      */       try {
/* 1822 */         URL localURL2 = null;
/* 1823 */         str1 = canonPath(str1);
/* 1824 */         localURL2 = ParseUtil.fileToEncodedURL(new File(str1));
/*      */ 
/* 1826 */         if (paramBoolean) {
/* 1827 */           localCodeSource = new CodeSource(localURL2, getSignerCertificates(paramCodeSource));
/*      */         }
/*      */         else {
/* 1830 */           localCodeSource = new CodeSource(localURL2, paramCodeSource.getCertificates());
/*      */         }
/*      */ 
/*      */       }
/*      */       catch (IOException localIOException)
/*      */       {
/* 1836 */         if (paramBoolean) {
/* 1837 */           localCodeSource = new CodeSource(paramCodeSource.getLocation(), getSignerCertificates(paramCodeSource));
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/* 1842 */     else if (paramBoolean) {
/* 1843 */       localCodeSource = new CodeSource(paramCodeSource.getLocation(), getSignerCertificates(paramCodeSource));
/*      */     }
/*      */ 
/* 1847 */     return localCodeSource;
/*      */   }
/*      */ 
/*      */   private static String canonPath(String paramString)
/*      */     throws IOException
/*      */   {
/* 1853 */     if (paramString.endsWith("*")) {
/* 1854 */       paramString = paramString.substring(0, paramString.length() - 1) + "-";
/* 1855 */       paramString = new File(paramString).getCanonicalPath();
/* 1856 */       return paramString.substring(0, paramString.length() - 1) + "*";
/*      */     }
/* 1858 */     return new File(paramString).getCanonicalPath();
/*      */   }
/*      */ 
/*      */   private String printPD(ProtectionDomain paramProtectionDomain)
/*      */   {
/* 1863 */     Principal[] arrayOfPrincipal = paramProtectionDomain.getPrincipals();
/* 1864 */     String str = "<no principals>";
/* 1865 */     if ((arrayOfPrincipal != null) && (arrayOfPrincipal.length > 0)) {
/* 1866 */       StringBuilder localStringBuilder = new StringBuilder("(principals ");
/* 1867 */       for (int i = 0; i < arrayOfPrincipal.length; i++) {
/* 1868 */         localStringBuilder.append(arrayOfPrincipal[i].getClass().getName() + " \"" + arrayOfPrincipal[i].getName() + "\"");
/*      */ 
/* 1871 */         if (i < arrayOfPrincipal.length - 1)
/* 1872 */           localStringBuilder.append(", ");
/*      */         else
/* 1874 */           localStringBuilder.append(")");
/*      */       }
/* 1876 */       str = localStringBuilder.toString();
/*      */     }
/* 1878 */     return "PD CodeSource: " + paramProtectionDomain.getCodeSource() + "\n\t" + "PD ClassLoader: " + paramProtectionDomain.getClassLoader() + "\n\t" + "PD Principals: " + str;
/*      */   }
/*      */ 
/*      */   private boolean replacePrincipals(List<PolicyParser.PrincipalEntry> paramList, KeyStore paramKeyStore)
/*      */   {
/* 1893 */     if ((paramList == null) || (paramList.size() == 0) || (paramKeyStore == null)) {
/* 1894 */       return true;
/*      */     }
/* 1896 */     ListIterator localListIterator = paramList.listIterator();
/* 1897 */     while (localListIterator.hasNext()) {
/* 1898 */       PolicyParser.PrincipalEntry localPrincipalEntry = (PolicyParser.PrincipalEntry)localListIterator.next();
/* 1899 */       if (localPrincipalEntry.principalClass.equals("PolicyParser.REPLACE_NAME"))
/*      */       {
/*      */         String str;
/* 1904 */         if ((str = getDN(localPrincipalEntry.principalName, paramKeyStore)) == null) {
/* 1905 */           return false;
/*      */         }
/*      */ 
/* 1908 */         if (debug != null) {
/* 1909 */           debug.println("  Replacing \"" + localPrincipalEntry.principalName + "\" with " + "javax.security.auth.x500.X500Principal" + "/\"" + str + "\"");
/*      */         }
/*      */ 
/* 1917 */         localPrincipalEntry.principalClass = "javax.security.auth.x500.X500Principal";
/* 1918 */         localPrincipalEntry.principalName = str;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1923 */     return true;
/*      */   }
/*      */ 
/*      */   private void expandPermissionName(PolicyParser.PermissionEntry paramPermissionEntry, KeyStore paramKeyStore)
/*      */     throws Exception
/*      */   {
/* 1929 */     if ((paramPermissionEntry.name == null) || (paramPermissionEntry.name.indexOf("${{", 0) == -1)) {
/* 1930 */       return;
/*      */     }
/*      */ 
/* 1933 */     int i = 0;
/*      */ 
/* 1935 */     StringBuilder localStringBuilder = new StringBuilder();
/*      */     int j;
/* 1936 */     while ((j = paramPermissionEntry.name.indexOf("${{", i)) != -1) {
/* 1937 */       int k = paramPermissionEntry.name.indexOf("}}", j);
/* 1938 */       if (k < 1) {
/*      */         break;
/*      */       }
/* 1941 */       localStringBuilder.append(paramPermissionEntry.name.substring(i, j));
/*      */ 
/* 1944 */       String str1 = paramPermissionEntry.name.substring(j + 3, k);
/*      */ 
/* 1948 */       String str2 = str1;
/*      */       int m;
/* 1950 */       if ((m = str1.indexOf(":")) != -1) {
/* 1951 */         str2 = str1.substring(0, m);
/*      */       }
/*      */ 
/* 1955 */       if (str2.equalsIgnoreCase("self"))
/*      */       {
/* 1957 */         localStringBuilder.append(paramPermissionEntry.name.substring(j, k + 2));
/* 1958 */         i = k + 2;
/*      */       }
/*      */       else
/*      */       {
/*      */         MessageFormat localMessageFormat;
/*      */         Object[] arrayOfObject;
/* 1960 */         if (str2.equalsIgnoreCase("alias"))
/*      */         {
/* 1962 */           if (m == -1) {
/* 1963 */             localMessageFormat = new MessageFormat(ResourcesMgr.getString("alias.name.not.provided.pe.name."));
/*      */ 
/* 1966 */             arrayOfObject = new Object[] { paramPermissionEntry.name };
/* 1967 */             throw new Exception(localMessageFormat.format(arrayOfObject));
/*      */           }
/* 1969 */           String str3 = str1.substring(m + 1);
/* 1970 */           if ((str3 = getDN(str3, paramKeyStore)) == null) {
/* 1971 */             localMessageFormat = new MessageFormat(ResourcesMgr.getString("unable.to.perform.substitution.on.alias.suffix"));
/*      */ 
/* 1974 */             arrayOfObject = new Object[] { str1.substring(m + 1) };
/* 1975 */             throw new Exception(localMessageFormat.format(arrayOfObject));
/*      */           }
/*      */ 
/* 1978 */           localStringBuilder.append("javax.security.auth.x500.X500Principal \"" + str3 + "\"");
/* 1979 */           i = k + 2;
/*      */         } else {
/* 1981 */           localMessageFormat = new MessageFormat(ResourcesMgr.getString("substitution.value.prefix.unsupported"));
/*      */ 
/* 1984 */           arrayOfObject = new Object[] { str2 };
/* 1985 */           throw new Exception(localMessageFormat.format(arrayOfObject));
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1990 */     localStringBuilder.append(paramPermissionEntry.name.substring(i));
/*      */ 
/* 1993 */     if (debug != null) {
/* 1994 */       debug.println("  Permission name expanded from:\n\t" + paramPermissionEntry.name + "\nto\n\t" + localStringBuilder.toString());
/*      */     }
/*      */ 
/* 1997 */     paramPermissionEntry.name = localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   private String getDN(String paramString, KeyStore paramKeyStore) {
/* 2001 */     Certificate localCertificate = null;
/*      */     try {
/* 2003 */       localCertificate = paramKeyStore.getCertificate(paramString);
/*      */     } catch (Exception localException) {
/* 2005 */       if (debug != null) {
/* 2006 */         debug.println("  Error retrieving certificate for '" + paramString + "': " + localException.toString());
/*      */       }
/*      */ 
/* 2011 */       return null;
/*      */     }
/*      */ 
/* 2014 */     if ((localCertificate == null) || (!(localCertificate instanceof X509Certificate))) {
/* 2015 */       if (debug != null) {
/* 2016 */         debug.println("  -- No certificate for '" + paramString + "' - ignoring entry");
/*      */       }
/*      */ 
/* 2020 */       return null;
/*      */     }
/* 2022 */     X509Certificate localX509Certificate = (X509Certificate)localCertificate;
/*      */ 
/* 2028 */     X500Principal localX500Principal = new X500Principal(localX509Certificate.getSubjectX500Principal().toString());
/*      */ 
/* 2030 */     return localX500Principal.getName();
/*      */   }
/*      */ 
/*      */   private boolean checkForTrustedIdentity(Certificate paramCertificate, PolicyInfo paramPolicyInfo)
/*      */   {
/* 2042 */     return false;
/*      */   }
/*      */ 
/*      */   private static class PolicyEntry
/*      */   {
/*      */     private final CodeSource codesource;
/*      */     final List<Permission> permissions;
/*      */     private final List<PolicyParser.PrincipalEntry> principals;
/*      */ 
/*      */     PolicyEntry(CodeSource paramCodeSource, List<PolicyParser.PrincipalEntry> paramList)
/*      */     {
/* 2110 */       this.codesource = paramCodeSource;
/* 2111 */       this.permissions = new ArrayList();
/* 2112 */       this.principals = paramList;
/*      */     }
/*      */ 
/*      */     PolicyEntry(CodeSource paramCodeSource)
/*      */     {
/* 2117 */       this(paramCodeSource, null);
/*      */     }
/*      */ 
/*      */     List<PolicyParser.PrincipalEntry> getPrincipals() {
/* 2121 */       return this.principals;
/*      */     }
/*      */ 
/*      */     void add(Permission paramPermission)
/*      */     {
/* 2130 */       this.permissions.add(paramPermission);
/*      */     }
/*      */ 
/*      */     CodeSource getCodeSource()
/*      */     {
/* 2137 */       return this.codesource;
/*      */     }
/*      */ 
/*      */     public String toString() {
/* 2141 */       StringBuilder localStringBuilder = new StringBuilder();
/* 2142 */       localStringBuilder.append(ResourcesMgr.getString("LPARAM"));
/* 2143 */       localStringBuilder.append(getCodeSource());
/* 2144 */       localStringBuilder.append("\n");
/* 2145 */       for (int i = 0; i < this.permissions.size(); i++) {
/* 2146 */         Permission localPermission = (Permission)this.permissions.get(i);
/* 2147 */         localStringBuilder.append(ResourcesMgr.getString("SPACE"));
/* 2148 */         localStringBuilder.append(ResourcesMgr.getString("SPACE"));
/* 2149 */         localStringBuilder.append(localPermission);
/* 2150 */         localStringBuilder.append(ResourcesMgr.getString("NEWLINE"));
/*      */       }
/* 2152 */       localStringBuilder.append(ResourcesMgr.getString("RPARAM"));
/* 2153 */       localStringBuilder.append(ResourcesMgr.getString("NEWLINE"));
/* 2154 */       return localStringBuilder.toString();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class PolicyInfo
/*      */   {
/*      */     private static final boolean verbose = false;
/*      */     final List<PolicyFile.PolicyEntry> policyEntries;
/*      */     final List<PolicyFile.PolicyEntry> identityPolicyEntries;
/*      */     final Map aliasMapping;
/*      */     private final JavaSecurityProtectionDomainAccess.ProtectionDomainCache[] pdMapping;
/*      */     private Random random;
/*      */ 
/*      */     PolicyInfo(int paramInt)
/*      */     {
/* 2413 */       this.policyEntries = new ArrayList();
/* 2414 */       this.identityPolicyEntries = Collections.synchronizedList(new ArrayList(2));
/*      */ 
/* 2416 */       this.aliasMapping = Collections.synchronizedMap(new HashMap(11));
/*      */ 
/* 2418 */       this.pdMapping = new JavaSecurityProtectionDomainAccess.ProtectionDomainCache[paramInt];
/* 2419 */       JavaSecurityProtectionDomainAccess localJavaSecurityProtectionDomainAccess = SharedSecrets.getJavaSecurityProtectionDomainAccess();
/*      */ 
/* 2421 */       for (int i = 0; i < paramInt; i++) {
/* 2422 */         this.pdMapping[i] = localJavaSecurityProtectionDomainAccess.getProtectionDomainCache();
/*      */       }
/* 2424 */       if (paramInt > 1)
/* 2425 */         this.random = new Random();
/*      */     }
/*      */ 
/*      */     JavaSecurityProtectionDomainAccess.ProtectionDomainCache getPdMapping() {
/* 2429 */       if (this.pdMapping.length == 1) {
/* 2430 */         return this.pdMapping[0];
/*      */       }
/* 2432 */       int i = Math.abs(this.random.nextInt() % this.pdMapping.length);
/* 2433 */       return this.pdMapping[i];
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class SelfPermission extends Permission
/*      */   {
/*      */     private static final long serialVersionUID = -8315562579967246806L;
/*      */     private String type;
/*      */     private String name;
/*      */     private String actions;
/*      */     private Certificate[] certs;
/*      */ 
/*      */     public SelfPermission(String paramString1, String paramString2, String paramString3, Certificate[] paramArrayOfCertificate)
/*      */     {
/* 2207 */       super();
/* 2208 */       if (paramString1 == null) {
/* 2209 */         throw new NullPointerException(ResourcesMgr.getString("type.can.t.be.null"));
/*      */       }
/*      */ 
/* 2212 */       this.type = paramString1;
/* 2213 */       this.name = paramString2;
/* 2214 */       this.actions = paramString3;
/* 2215 */       if (paramArrayOfCertificate != null)
/*      */       {
/* 2217 */         for (int i = 0; i < paramArrayOfCertificate.length; i++) {
/* 2218 */           if (!(paramArrayOfCertificate[i] instanceof X509Certificate))
/*      */           {
/* 2221 */             this.certs = ((Certificate[])paramArrayOfCertificate.clone());
/* 2222 */             break;
/*      */           }
/*      */         }
/*      */ 
/* 2226 */         if (this.certs == null)
/*      */         {
/* 2229 */           i = 0;
/* 2230 */           int j = 0;
/* 2231 */           while (i < paramArrayOfCertificate.length) {
/* 2232 */             j++;
/* 2233 */             while ((i + 1 < paramArrayOfCertificate.length) && (((X509Certificate)paramArrayOfCertificate[i]).getIssuerDN().equals(((X509Certificate)paramArrayOfCertificate[(i + 1)]).getSubjectDN())))
/*      */             {
/* 2236 */               i++;
/*      */             }
/* 2238 */             i++;
/*      */           }
/* 2240 */           if (j == paramArrayOfCertificate.length)
/*      */           {
/* 2243 */             this.certs = ((Certificate[])paramArrayOfCertificate.clone());
/*      */           }
/*      */ 
/* 2246 */           if (this.certs == null)
/*      */           {
/* 2248 */             ArrayList localArrayList = new ArrayList();
/*      */ 
/* 2250 */             i = 0;
/* 2251 */             while (i < paramArrayOfCertificate.length) {
/* 2252 */               localArrayList.add(paramArrayOfCertificate[i]);
/* 2253 */               while ((i + 1 < paramArrayOfCertificate.length) && (((X509Certificate)paramArrayOfCertificate[i]).getIssuerDN().equals(((X509Certificate)paramArrayOfCertificate[(i + 1)]).getSubjectDN())))
/*      */               {
/* 2256 */                 i++;
/*      */               }
/* 2258 */               i++;
/*      */             }
/* 2260 */             this.certs = new Certificate[localArrayList.size()];
/* 2261 */             localArrayList.toArray(this.certs);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public boolean implies(Permission paramPermission)
/*      */     {
/* 2277 */       return false;
/*      */     }
/*      */ 
/*      */     public boolean equals(Object paramObject)
/*      */     {
/* 2294 */       if (paramObject == this) {
/* 2295 */         return true;
/*      */       }
/* 2297 */       if (!(paramObject instanceof SelfPermission))
/* 2298 */         return false;
/* 2299 */       SelfPermission localSelfPermission = (SelfPermission)paramObject;
/*      */ 
/* 2301 */       if ((!this.type.equals(localSelfPermission.type)) || (!this.name.equals(localSelfPermission.name)) || (!this.actions.equals(localSelfPermission.actions)))
/*      */       {
/* 2304 */         return false;
/*      */       }
/* 2306 */       if (this.certs.length != localSelfPermission.certs.length)
/* 2307 */         return false;
/*      */       int k;
/*      */       int j;
/* 2312 */       for (int i = 0; i < this.certs.length; i++) {
/* 2313 */         k = 0;
/* 2314 */         for (j = 0; j < localSelfPermission.certs.length; j++) {
/* 2315 */           if (this.certs[i].equals(localSelfPermission.certs[j])) {
/* 2316 */             k = 1;
/* 2317 */             break;
/*      */           }
/*      */         }
/* 2320 */         if (k == 0) return false;
/*      */       }
/*      */ 
/* 2323 */       for (i = 0; i < localSelfPermission.certs.length; i++) {
/* 2324 */         k = 0;
/* 2325 */         for (j = 0; j < this.certs.length; j++) {
/* 2326 */           if (localSelfPermission.certs[i].equals(this.certs[j])) {
/* 2327 */             k = 1;
/* 2328 */             break;
/*      */           }
/*      */         }
/* 2331 */         if (k == 0) return false;
/*      */       }
/* 2333 */       return true;
/*      */     }
/*      */ 
/*      */     public int hashCode()
/*      */     {
/* 2342 */       int i = this.type.hashCode();
/* 2343 */       if (this.name != null)
/* 2344 */         i ^= this.name.hashCode();
/* 2345 */       if (this.actions != null)
/* 2346 */         i ^= this.actions.hashCode();
/* 2347 */       return i;
/*      */     }
/*      */ 
/*      */     public String getActions()
/*      */     {
/* 2361 */       return "";
/*      */     }
/*      */ 
/*      */     public String getSelfType() {
/* 2365 */       return this.type;
/*      */     }
/*      */ 
/*      */     public String getSelfName() {
/* 2369 */       return this.name;
/*      */     }
/*      */ 
/*      */     public String getSelfActions() {
/* 2373 */       return this.actions;
/*      */     }
/*      */ 
/*      */     public Certificate[] getCerts() {
/* 2377 */       return this.certs;
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/* 2388 */       return "(SelfPermission " + this.type + " " + this.name + " " + this.actions + ")";
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.PolicyFile
 * JD-Core Version:    0.6.2
 */