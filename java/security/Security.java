/*      */ package java.security;
/*      */ 
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.lang.reflect.Field;
/*      */ import java.net.URL;
/*      */ import java.util.Collections;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashSet;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Properties;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import sun.security.jca.GetInstance;
/*      */ import sun.security.jca.GetInstance.Instance;
/*      */ import sun.security.jca.ProviderList;
/*      */ import sun.security.jca.Providers;
/*      */ import sun.security.util.Debug;
/*      */ import sun.security.util.PropertyExpander;
/*      */ 
/*      */ public final class Security
/*      */ {
/*   50 */   private static final Debug sdebug = Debug.getInstance("properties");
/*      */   private static Properties props;
/*  663 */   private static final Map<String, Class> spiMap = new ConcurrentHashMap();
/*      */ 
/*      */   private static void initialize()
/*      */   {
/*   76 */     props = new Properties();
/*   77 */     int i = 0;
/*   78 */     int j = 0;
/*      */ 
/*   82 */     File localFile = securityPropFile("java.security");
/*      */     Object localObject1;
/*   83 */     if (localFile.exists()) {
/*   84 */       localObject1 = null;
/*      */       try {
/*   86 */         FileInputStream localFileInputStream = new FileInputStream(localFile);
/*   87 */         localObject1 = new BufferedInputStream(localFileInputStream);
/*   88 */         props.load((InputStream)localObject1);
/*   89 */         i = 1;
/*      */ 
/*   91 */         if (sdebug != null)
/*   92 */           sdebug.println("reading security properties file: " + localFile);
/*      */       }
/*      */       catch (IOException localIOException2)
/*      */       {
/*   96 */         if (sdebug != null) {
/*   97 */           sdebug.println("unable to load security properties from " + localFile);
/*      */ 
/*   99 */           localIOException2.printStackTrace();
/*      */         }
/*      */       } finally {
/*  102 */         if (localObject1 != null) {
/*      */           try {
/*  104 */             ((InputStream)localObject1).close();
/*      */           } catch (IOException localIOException6) {
/*  106 */             if (sdebug != null) {
/*  107 */               sdebug.println("unable to close input stream");
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  114 */     if ("true".equalsIgnoreCase(props.getProperty("security.overridePropertiesFile")))
/*      */     {
/*  117 */       localObject1 = System.getProperty("java.security.properties");
/*      */ 
/*  119 */       if ((localObject1 != null) && (((String)localObject1).startsWith("="))) {
/*  120 */         j = 1;
/*  121 */         localObject1 = ((String)localObject1).substring(1);
/*      */       }
/*      */ 
/*  124 */       if (j != 0) {
/*  125 */         props = new Properties();
/*  126 */         if (sdebug != null) {
/*  127 */           sdebug.println("overriding other security properties files!");
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  134 */       if (localObject1 != null) {
/*  135 */         BufferedInputStream localBufferedInputStream = null;
/*      */         try
/*      */         {
/*  139 */           localObject1 = PropertyExpander.expand((String)localObject1);
/*  140 */           localFile = new File((String)localObject1);
/*      */           URL localURL;
/*  141 */           if (localFile.exists()) {
/*  142 */             localURL = new URL("file:" + localFile.getCanonicalPath());
/*      */           }
/*      */           else {
/*  145 */             localURL = new URL((String)localObject1);
/*      */           }
/*  147 */           localBufferedInputStream = new BufferedInputStream(localURL.openStream());
/*  148 */           props.load(localBufferedInputStream);
/*  149 */           i = 1;
/*      */ 
/*  151 */           if (sdebug != null) {
/*  152 */             sdebug.println("reading security properties file: " + localURL);
/*      */ 
/*  154 */             if (j != 0)
/*  155 */               sdebug.println("overriding other security properties files!");
/*      */           }
/*      */         }
/*      */         catch (Exception localException)
/*      */         {
/*  160 */           if (sdebug != null) {
/*  161 */             sdebug.println("unable to load security properties from " + (String)localObject1);
/*      */ 
/*  164 */             localException.printStackTrace();
/*      */           }
/*      */         } finally {
/*  167 */           if (localBufferedInputStream != null) {
/*      */             try {
/*  169 */               localBufferedInputStream.close();
/*      */             } catch (IOException localIOException7) {
/*  171 */               if (sdebug != null) {
/*  172 */                 sdebug.println("unable to close input stream");
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  180 */     if (i == 0) {
/*  181 */       initializeStatic();
/*  182 */       if (sdebug != null)
/*  183 */         sdebug.println("unable to load security properties -- using defaults");
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void initializeStatic()
/*      */   {
/*  195 */     props.put("security.provider.1", "sun.security.provider.Sun");
/*  196 */     props.put("security.provider.2", "sun.security.rsa.SunRsaSign");
/*  197 */     props.put("security.provider.3", "com.sun.net.ssl.internal.ssl.Provider");
/*  198 */     props.put("security.provider.4", "com.sun.crypto.provider.SunJCE");
/*  199 */     props.put("security.provider.5", "sun.security.jgss.SunProvider");
/*  200 */     props.put("security.provider.6", "com.sun.security.sasl.Provider");
/*      */   }
/*      */ 
/*      */   private static File securityPropFile(String paramString)
/*      */   {
/*  212 */     String str = File.separator;
/*  213 */     return new File(System.getProperty("java.home") + str + "lib" + str + "security" + str + paramString);
/*      */   }
/*      */ 
/*      */   private static ProviderProperty getProviderProperty(String paramString)
/*      */   {
/*  225 */     ProviderProperty localProviderProperty = null;
/*      */ 
/*  227 */     List localList = Providers.getProviderList().providers();
/*  228 */     for (int i = 0; i < localList.size(); i++)
/*      */     {
/*  230 */       String str1 = null;
/*  231 */       Provider localProvider = (Provider)localList.get(i);
/*  232 */       String str2 = localProvider.getProperty(paramString);
/*      */       Object localObject;
/*  234 */       if (str2 == null)
/*      */       {
/*  237 */         localObject = localProvider.keys();
/*  238 */         while ((((Enumeration)localObject).hasMoreElements()) && (str2 == null)) {
/*  239 */           str1 = (String)((Enumeration)localObject).nextElement();
/*  240 */           if (paramString.equalsIgnoreCase(str1)) {
/*  241 */             str2 = localProvider.getProperty(str1);
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  247 */       if (str2 != null) {
/*  248 */         localObject = new ProviderProperty(null);
/*  249 */         ((ProviderProperty)localObject).className = str2;
/*  250 */         ((ProviderProperty)localObject).provider = localProvider;
/*  251 */         return localObject;
/*      */       }
/*      */     }
/*      */ 
/*  255 */     return localProviderProperty;
/*      */   }
/*      */ 
/*      */   private static String getProviderProperty(String paramString, Provider paramProvider)
/*      */   {
/*  262 */     String str1 = paramProvider.getProperty(paramString);
/*  263 */     if (str1 == null)
/*      */     {
/*  266 */       Enumeration localEnumeration = paramProvider.keys();
/*  267 */       while ((localEnumeration.hasMoreElements()) && (str1 == null)) {
/*  268 */         String str2 = (String)localEnumeration.nextElement();
/*  269 */         if (paramString.equalsIgnoreCase(str2)) {
/*  270 */           str1 = paramProvider.getProperty(str2);
/*  271 */           break;
/*      */         }
/*      */       }
/*      */     }
/*  275 */     return str1;
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public static String getAlgorithmProperty(String paramString1, String paramString2)
/*      */   {
/*  305 */     ProviderProperty localProviderProperty = getProviderProperty("Alg." + paramString2 + "." + paramString1);
/*      */ 
/*  307 */     if (localProviderProperty != null) {
/*  308 */       return localProviderProperty.className;
/*      */     }
/*  310 */     return null;
/*      */   }
/*      */ 
/*      */   public static synchronized int insertProviderAt(Provider paramProvider, int paramInt)
/*      */   {
/*  360 */     String str = paramProvider.getName();
/*  361 */     check("insertProvider." + str);
/*  362 */     ProviderList localProviderList1 = Providers.getFullProviderList();
/*  363 */     ProviderList localProviderList2 = ProviderList.insertAt(localProviderList1, paramProvider, paramInt - 1);
/*  364 */     if (localProviderList1 == localProviderList2) {
/*  365 */       return -1;
/*      */     }
/*  367 */     Providers.setProviderList(localProviderList2);
/*  368 */     return localProviderList2.getIndex(str) + 1;
/*      */   }
/*      */ 
/*      */   public static int addProvider(Provider paramProvider)
/*      */   {
/*  409 */     return insertProviderAt(paramProvider, 0);
/*      */   }
/*      */ 
/*      */   public static synchronized void removeProvider(String paramString)
/*      */   {
/*  445 */     check("removeProvider." + paramString);
/*  446 */     ProviderList localProviderList1 = Providers.getFullProviderList();
/*  447 */     ProviderList localProviderList2 = ProviderList.remove(localProviderList1, paramString);
/*  448 */     Providers.setProviderList(localProviderList2);
/*      */   }
/*      */ 
/*      */   public static Provider[] getProviders()
/*      */   {
/*  458 */     return Providers.getFullProviderList().toArray();
/*      */   }
/*      */ 
/*      */   public static Provider getProvider(String paramString)
/*      */   {
/*  474 */     return Providers.getProviderList().getProvider(paramString);
/*      */   }
/*      */ 
/*      */   public static Provider[] getProviders(String paramString)
/*      */   {
/*  537 */     String str1 = null;
/*  538 */     String str2 = null;
/*  539 */     int i = paramString.indexOf(':');
/*      */ 
/*  541 */     if (i == -1) {
/*  542 */       str1 = paramString;
/*  543 */       str2 = "";
/*      */     } else {
/*  545 */       str1 = paramString.substring(0, i);
/*  546 */       str2 = paramString.substring(i + 1);
/*      */     }
/*      */ 
/*  549 */     Hashtable localHashtable = new Hashtable(1);
/*  550 */     localHashtable.put(str1, str2);
/*      */ 
/*  552 */     return getProviders(localHashtable);
/*      */   }
/*      */ 
/*      */   public static Provider[] getProviders(Map<String, String> paramMap)
/*      */   {
/*  607 */     Provider[] arrayOfProvider = getProviders();
/*  608 */     Set localSet = paramMap.keySet();
/*  609 */     Object localObject1 = new LinkedHashSet(5);
/*      */ 
/*  613 */     if ((localSet == null) || (arrayOfProvider == null)) {
/*  614 */       return arrayOfProvider;
/*      */     }
/*      */ 
/*  617 */     int i = 1;
/*      */ 
/*  621 */     for (Object localObject2 = localSet.iterator(); ((Iterator)localObject2).hasNext(); ) {
/*  622 */       localObject3 = (String)((Iterator)localObject2).next();
/*  623 */       String str = (String)paramMap.get(localObject3);
/*      */ 
/*  625 */       LinkedHashSet localLinkedHashSet = getAllQualifyingCandidates((String)localObject3, str, arrayOfProvider);
/*      */ 
/*  627 */       if (i != 0) {
/*  628 */         localObject1 = localLinkedHashSet;
/*  629 */         i = 0;
/*      */       }
/*      */ 
/*  632 */       if ((localLinkedHashSet != null) && (!localLinkedHashSet.isEmpty()))
/*      */       {
/*  636 */         Iterator localIterator = ((LinkedHashSet)localObject1).iterator();
/*  637 */         while (localIterator.hasNext()) {
/*  638 */           Provider localProvider = (Provider)localIterator.next();
/*  639 */           if (!localLinkedHashSet.contains(localProvider))
/*  640 */             localIterator.remove();
/*      */         }
/*      */       }
/*      */       else {
/*  644 */         localObject1 = null;
/*  645 */         break;
/*      */       }
/*      */     }
/*      */ 
/*  649 */     if ((localObject1 == null) || (((LinkedHashSet)localObject1).isEmpty())) {
/*  650 */       return null;
/*      */     }
/*  652 */     localObject2 = ((LinkedHashSet)localObject1).toArray();
/*  653 */     Object localObject3 = new Provider[localObject2.length];
/*      */ 
/*  655 */     for (int j = 0; j < localObject3.length; j++) {
/*  656 */       localObject3[j] = ((Provider)localObject2[j]);
/*      */     }
/*      */ 
/*  659 */     return localObject3;
/*      */   }
/*      */ 
/*      */   private static Class getSpiClass(String paramString)
/*      */   {
/*  671 */     Class localClass = (Class)spiMap.get(paramString);
/*  672 */     if (localClass != null)
/*  673 */       return localClass;
/*      */     try
/*      */     {
/*  676 */       localClass = Class.forName("java.security." + paramString + "Spi");
/*  677 */       spiMap.put(paramString, localClass);
/*  678 */       return localClass;
/*      */     } catch (ClassNotFoundException localClassNotFoundException) {
/*  680 */       throw new AssertionError("Spi class not found", localClassNotFoundException);
/*      */     }
/*      */   }
/*      */ 
/*      */   static Object[] getImpl(String paramString1, String paramString2, String paramString3)
/*      */     throws NoSuchAlgorithmException, NoSuchProviderException
/*      */   {
/*  694 */     if (paramString3 == null) {
/*  695 */       return GetInstance.getInstance(paramString2, getSpiClass(paramString2), paramString1).toArray();
/*      */     }
/*      */ 
/*  698 */     return GetInstance.getInstance(paramString2, getSpiClass(paramString2), paramString1, paramString3).toArray();
/*      */   }
/*      */ 
/*      */   static Object[] getImpl(String paramString1, String paramString2, String paramString3, Object paramObject)
/*      */     throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException
/*      */   {
/*  706 */     if (paramString3 == null) {
/*  707 */       return GetInstance.getInstance(paramString2, getSpiClass(paramString2), paramString1, paramObject).toArray();
/*      */     }
/*      */ 
/*  710 */     return GetInstance.getInstance(paramString2, getSpiClass(paramString2), paramString1, paramObject, paramString3).toArray();
/*      */   }
/*      */ 
/*      */   static Object[] getImpl(String paramString1, String paramString2, Provider paramProvider)
/*      */     throws NoSuchAlgorithmException
/*      */   {
/*  724 */     return GetInstance.getInstance(paramString2, getSpiClass(paramString2), paramString1, paramProvider).toArray();
/*      */   }
/*      */ 
/*      */   static Object[] getImpl(String paramString1, String paramString2, Provider paramProvider, Object paramObject)
/*      */     throws NoSuchAlgorithmException, InvalidAlgorithmParameterException
/*      */   {
/*  731 */     return GetInstance.getInstance(paramString2, getSpiClass(paramString2), paramString1, paramObject, paramProvider).toArray();
/*      */   }
/*      */ 
/*      */   public static String getProperty(String paramString)
/*      */   {
/*  759 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  760 */     if (localSecurityManager != null) {
/*  761 */       localSecurityManager.checkPermission(new SecurityPermission("getProperty." + paramString));
/*      */     }
/*      */ 
/*  764 */     String str = props.getProperty(paramString);
/*  765 */     if (str != null)
/*  766 */       str = str.trim();
/*  767 */     return str;
/*      */   }
/*      */ 
/*      */   public static void setProperty(String paramString1, String paramString2)
/*      */   {
/*  793 */     check("setProperty." + paramString1);
/*  794 */     props.put(paramString1, paramString2);
/*  795 */     invalidateSMCache(paramString1);
/*      */   }
/*      */ 
/*      */   private static void invalidateSMCache(String paramString)
/*      */   {
/*  810 */     boolean bool1 = paramString.equals("package.access");
/*  811 */     boolean bool2 = paramString.equals("package.definition");
/*      */ 
/*  813 */     if ((bool1) || (bool2))
/*  814 */       AccessController.doPrivileged(new PrivilegedAction()
/*      */       {
/*      */         public Void run() {
/*      */           try {
/*  818 */             Class localClass = Class.forName("java.lang.SecurityManager", false, null);
/*      */ 
/*  820 */             Field localField = null;
/*  821 */             boolean bool = false;
/*      */ 
/*  823 */             if (this.val$pa) {
/*  824 */               localField = localClass.getDeclaredField("packageAccessValid");
/*  825 */               bool = localField.isAccessible();
/*  826 */               localField.setAccessible(true);
/*      */             } else {
/*  828 */               localField = localClass.getDeclaredField("packageDefinitionValid");
/*  829 */               bool = localField.isAccessible();
/*  830 */               localField.setAccessible(true);
/*      */             }
/*  832 */             localField.setBoolean(localField, false);
/*  833 */             localField.setAccessible(bool);
/*      */           }
/*      */           catch (Exception localException)
/*      */           {
/*      */           }
/*      */ 
/*  846 */           return null;
/*      */         }
/*      */       });
/*      */   }
/*      */ 
/*      */   private static void check(String paramString)
/*      */   {
/*  853 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  854 */     if (localSecurityManager != null)
/*  855 */       localSecurityManager.checkSecurityAccess(paramString);
/*      */   }
/*      */ 
/*      */   private static LinkedHashSet<Provider> getAllQualifyingCandidates(String paramString1, String paramString2, Provider[] paramArrayOfProvider)
/*      */   {
/*  867 */     String[] arrayOfString = getFilterComponents(paramString1, paramString2);
/*      */ 
/*  873 */     String str1 = arrayOfString[0];
/*  874 */     String str2 = arrayOfString[1];
/*  875 */     String str3 = arrayOfString[2];
/*      */ 
/*  877 */     return getProvidersNotUsingCache(str1, str2, str3, paramString2, paramArrayOfProvider);
/*      */   }
/*      */ 
/*      */   private static LinkedHashSet<Provider> getProvidersNotUsingCache(String paramString1, String paramString2, String paramString3, String paramString4, Provider[] paramArrayOfProvider)
/*      */   {
/*  887 */     LinkedHashSet localLinkedHashSet = new LinkedHashSet(5);
/*  888 */     for (int i = 0; i < paramArrayOfProvider.length; i++) {
/*  889 */       if (isCriterionSatisfied(paramArrayOfProvider[i], paramString1, paramString2, paramString3, paramString4))
/*      */       {
/*  892 */         localLinkedHashSet.add(paramArrayOfProvider[i]);
/*      */       }
/*      */     }
/*  895 */     return localLinkedHashSet;
/*      */   }
/*      */ 
/*      */   private static boolean isCriterionSatisfied(Provider paramProvider, String paramString1, String paramString2, String paramString3, String paramString4)
/*      */   {
/*  907 */     String str1 = paramString1 + '.' + paramString2;
/*      */ 
/*  909 */     if (paramString3 != null) {
/*  910 */       str1 = str1 + ' ' + paramString3;
/*      */     }
/*      */ 
/*  914 */     String str2 = getProviderProperty(str1, paramProvider);
/*      */ 
/*  916 */     if (str2 == null)
/*      */     {
/*  919 */       String str3 = getProviderProperty("Alg.Alias." + paramString1 + "." + paramString2, paramProvider);
/*      */ 
/*  923 */       if (str3 != null) {
/*  924 */         str1 = paramString1 + "." + str3;
/*      */ 
/*  926 */         if (paramString3 != null) {
/*  927 */           str1 = str1 + ' ' + paramString3;
/*      */         }
/*      */ 
/*  930 */         str2 = getProviderProperty(str1, paramProvider);
/*      */       }
/*      */ 
/*  933 */       if (str2 == null)
/*      */       {
/*  936 */         return false;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  944 */     if (paramString3 == null) {
/*  945 */       return true;
/*      */     }
/*      */ 
/*  950 */     if (isStandardAttr(paramString3)) {
/*  951 */       return isConstraintSatisfied(paramString3, paramString4, str2);
/*      */     }
/*  953 */     return paramString4.equalsIgnoreCase(str2);
/*      */   }
/*      */ 
/*      */   private static boolean isStandardAttr(String paramString)
/*      */   {
/*  964 */     if (paramString.equalsIgnoreCase("KeySize")) {
/*  965 */       return true;
/*      */     }
/*  967 */     if (paramString.equalsIgnoreCase("ImplementedIn")) {
/*  968 */       return true;
/*      */     }
/*  970 */     return false;
/*      */   }
/*      */ 
/*      */   private static boolean isConstraintSatisfied(String paramString1, String paramString2, String paramString3)
/*      */   {
/*  982 */     if (paramString1.equalsIgnoreCase("KeySize")) {
/*  983 */       int i = Integer.parseInt(paramString2);
/*  984 */       int j = Integer.parseInt(paramString3);
/*  985 */       if (i <= j) {
/*  986 */         return true;
/*      */       }
/*  988 */       return false;
/*      */     }
/*      */ 
/*  994 */     if (paramString1.equalsIgnoreCase("ImplementedIn")) {
/*  995 */       return paramString2.equalsIgnoreCase(paramString3);
/*      */     }
/*      */ 
/*  998 */     return false;
/*      */   }
/*      */ 
/*      */   static String[] getFilterComponents(String paramString1, String paramString2) {
/* 1002 */     int i = paramString1.indexOf('.');
/*      */ 
/* 1004 */     if (i < 0)
/*      */     {
/* 1007 */       throw new InvalidParameterException("Invalid filter");
/*      */     }
/*      */ 
/* 1010 */     String str1 = paramString1.substring(0, i);
/* 1011 */     String str2 = null;
/* 1012 */     String str3 = null;
/*      */ 
/* 1014 */     if (paramString2.length() == 0)
/*      */     {
/* 1017 */       str2 = paramString1.substring(i + 1).trim();
/* 1018 */       if (str2.length() == 0)
/*      */       {
/* 1020 */         throw new InvalidParameterException("Invalid filter");
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 1026 */       int j = paramString1.indexOf(' ');
/*      */ 
/* 1028 */       if (j == -1)
/*      */       {
/* 1030 */         throw new InvalidParameterException("Invalid filter");
/*      */       }
/* 1032 */       str3 = paramString1.substring(j + 1).trim();
/* 1033 */       if (str3.length() == 0)
/*      */       {
/* 1035 */         throw new InvalidParameterException("Invalid filter");
/*      */       }
/*      */ 
/* 1040 */       if ((j < i) || (i == j - 1))
/*      */       {
/* 1042 */         throw new InvalidParameterException("Invalid filter");
/*      */       }
/* 1044 */       str2 = paramString1.substring(i + 1, j);
/*      */     }
/*      */ 
/* 1048 */     String[] arrayOfString = new String[3];
/* 1049 */     arrayOfString[0] = str1;
/* 1050 */     arrayOfString[1] = str2;
/* 1051 */     arrayOfString[2] = str3;
/*      */ 
/* 1053 */     return arrayOfString;
/*      */   }
/*      */ 
/*      */   public static Set<String> getAlgorithms(String paramString)
/*      */   {
/* 1079 */     if ((paramString == null) || (paramString.length() == 0) || (paramString.endsWith(".")))
/*      */     {
/* 1081 */       return Collections.EMPTY_SET;
/*      */     }
/*      */ 
/* 1084 */     HashSet localHashSet = new HashSet();
/* 1085 */     Provider[] arrayOfProvider = getProviders();
/*      */ 
/* 1087 */     for (int i = 0; i < arrayOfProvider.length; i++)
/*      */     {
/* 1089 */       Enumeration localEnumeration = arrayOfProvider[i].keys();
/* 1090 */       while (localEnumeration.hasMoreElements()) {
/* 1091 */         String str = ((String)localEnumeration.nextElement()).toUpperCase();
/* 1092 */         if (str.startsWith(paramString.toUpperCase()))
/*      */         {
/* 1099 */           if (str.indexOf(" ") < 0) {
/* 1100 */             localHashSet.add(str.substring(paramString.length() + 1));
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1105 */     return Collections.unmodifiableSet(localHashSet);
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*   67 */     AccessController.doPrivileged(new PrivilegedAction() {
/*      */       public Void run() {
/*   69 */         Security.access$000();
/*   70 */         return null;
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   private static class ProviderProperty
/*      */   {
/*      */     String className;
/*      */     Provider provider;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.Security
 * JD-Core Version:    0.6.2
 */