/*      */ package javax.management.loading;
/*      */ 
/*      */ import com.sun.jmx.defaults.JmxProperties;
/*      */ import com.sun.jmx.remote.util.EnvHelp;
/*      */ import java.io.Externalizable;
/*      */ import java.io.File;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.ObjectInput;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutput;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.URL;
/*      */ import java.net.URLClassLoader;
/*      */ import java.net.URLStreamHandlerFactory;
/*      */ import java.nio.file.Files;
/*      */ import java.nio.file.Path;
/*      */ import java.nio.file.attribute.FileAttribute;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import javax.management.InstanceAlreadyExistsException;
/*      */ import javax.management.InstanceNotFoundException;
/*      */ import javax.management.MBeanException;
/*      */ import javax.management.MBeanRegistration;
/*      */ import javax.management.MBeanRegistrationException;
/*      */ import javax.management.MBeanServer;
/*      */ import javax.management.NotCompliantMBeanException;
/*      */ import javax.management.ObjectInstance;
/*      */ import javax.management.ObjectName;
/*      */ import javax.management.ReflectionException;
/*      */ import javax.management.ServiceNotFoundException;
/*      */ 
/*      */ public class MLet extends URLClassLoader
/*      */   implements MLetMBean, MBeanRegistration, Externalizable
/*      */ {
/*      */   private static final long serialVersionUID = 3636148327800330130L;
/*  186 */   private MBeanServer server = null;
/*      */ 
/*  194 */   private List<MLetContent> mletList = new ArrayList();
/*      */   private String libraryDirectory;
/*  207 */   private ObjectName mletObjectName = null;
/*      */ 
/*  213 */   private URL[] myUrls = null;
/*      */   private transient ClassLoaderRepository currentClr;
/*      */   private transient boolean delegateToCLR;
/*  230 */   private Map<String, Class<?>> primitiveClasses = new HashMap(8);
/*      */ 
/*      */   public MLet()
/*      */   {
/*  264 */     this(new URL[0]);
/*      */   }
/*      */ 
/*      */   public MLet(URL[] paramArrayOfURL)
/*      */   {
/*  277 */     this(paramArrayOfURL, true);
/*      */   }
/*      */ 
/*      */   public MLet(URL[] paramArrayOfURL, ClassLoader paramClassLoader)
/*      */   {
/*  292 */     this(paramArrayOfURL, paramClassLoader, true);
/*      */   }
/*      */ 
/*      */   public MLet(URL[] paramArrayOfURL, ClassLoader paramClassLoader, URLStreamHandlerFactory paramURLStreamHandlerFactory)
/*      */   {
/*  310 */     this(paramArrayOfURL, paramClassLoader, paramURLStreamHandlerFactory, true);
/*      */   }
/*      */ 
/*      */   public MLet(URL[] paramArrayOfURL, boolean paramBoolean)
/*      */   {
/*  326 */     super(paramArrayOfURL);
/*      */ 
/*  233 */     this.primitiveClasses.put(Boolean.TYPE.toString(), Boolean.class);
/*  234 */     this.primitiveClasses.put(Character.TYPE.toString(), Character.class);
/*  235 */     this.primitiveClasses.put(Byte.TYPE.toString(), Byte.class);
/*  236 */     this.primitiveClasses.put(Short.TYPE.toString(), Short.class);
/*  237 */     this.primitiveClasses.put(Integer.TYPE.toString(), Integer.class);
/*  238 */     this.primitiveClasses.put(Long.TYPE.toString(), Long.class);
/*  239 */     this.primitiveClasses.put(Float.TYPE.toString(), Float.class);
/*  240 */     this.primitiveClasses.put(Double.TYPE.toString(), Double.class);
/*      */ 
/*  327 */     init(paramBoolean);
/*      */   }
/*      */ 
/*      */   public MLet(URL[] paramArrayOfURL, ClassLoader paramClassLoader, boolean paramBoolean)
/*      */   {
/*  345 */     super(paramArrayOfURL, paramClassLoader);
/*      */ 
/*  233 */     this.primitiveClasses.put(Boolean.TYPE.toString(), Boolean.class);
/*  234 */     this.primitiveClasses.put(Character.TYPE.toString(), Character.class);
/*  235 */     this.primitiveClasses.put(Byte.TYPE.toString(), Byte.class);
/*  236 */     this.primitiveClasses.put(Short.TYPE.toString(), Short.class);
/*  237 */     this.primitiveClasses.put(Integer.TYPE.toString(), Integer.class);
/*  238 */     this.primitiveClasses.put(Long.TYPE.toString(), Long.class);
/*  239 */     this.primitiveClasses.put(Float.TYPE.toString(), Float.class);
/*  240 */     this.primitiveClasses.put(Double.TYPE.toString(), Double.class);
/*      */ 
/*  346 */     init(paramBoolean);
/*      */   }
/*      */ 
/*      */   public MLet(URL[] paramArrayOfURL, ClassLoader paramClassLoader, URLStreamHandlerFactory paramURLStreamHandlerFactory, boolean paramBoolean)
/*      */   {
/*  368 */     super(paramArrayOfURL, paramClassLoader, paramURLStreamHandlerFactory);
/*      */ 
/*  233 */     this.primitiveClasses.put(Boolean.TYPE.toString(), Boolean.class);
/*  234 */     this.primitiveClasses.put(Character.TYPE.toString(), Character.class);
/*  235 */     this.primitiveClasses.put(Byte.TYPE.toString(), Byte.class);
/*  236 */     this.primitiveClasses.put(Short.TYPE.toString(), Short.class);
/*  237 */     this.primitiveClasses.put(Integer.TYPE.toString(), Integer.class);
/*  238 */     this.primitiveClasses.put(Long.TYPE.toString(), Long.class);
/*  239 */     this.primitiveClasses.put(Float.TYPE.toString(), Float.class);
/*  240 */     this.primitiveClasses.put(Double.TYPE.toString(), Double.class);
/*      */ 
/*  369 */     init(paramBoolean);
/*      */   }
/*      */ 
/*      */   private void init(boolean paramBoolean) {
/*  373 */     this.delegateToCLR = paramBoolean;
/*      */     try
/*      */     {
/*  376 */       this.libraryDirectory = System.getProperty("jmx.mlet.library.dir");
/*  377 */       if (this.libraryDirectory == null)
/*  378 */         this.libraryDirectory = getTmpDir();
/*      */     }
/*      */     catch (SecurityException localSecurityException)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   public void addURL(URL paramURL)
/*      */   {
/*  400 */     if (!Arrays.asList(getURLs()).contains(paramURL))
/*  401 */       super.addURL(paramURL);
/*      */   }
/*      */ 
/*      */   public void addURL(String paramString)
/*      */     throws ServiceNotFoundException
/*      */   {
/*      */     try
/*      */     {
/*  411 */       URL localURL = new URL(paramString);
/*  412 */       if (!Arrays.asList(getURLs()).contains(localURL))
/*  413 */         super.addURL(localURL);
/*      */     } catch (MalformedURLException localMalformedURLException) {
/*  415 */       if (JmxProperties.MLET_LOGGER.isLoggable(Level.FINEST)) {
/*  416 */         JmxProperties.MLET_LOGGER.logp(Level.FINEST, MLet.class.getName(), "addUrl", "Malformed URL: " + paramString, localMalformedURLException);
/*      */       }
/*      */ 
/*  419 */       throw new ServiceNotFoundException("The specified URL is malformed");
/*      */     }
/*      */   }
/*      */ 
/*      */   public URL[] getURLs()
/*      */   {
/*  429 */     return super.getURLs();
/*      */   }
/*      */ 
/*      */   public Set<Object> getMBeansFromURL(URL paramURL)
/*      */     throws ServiceNotFoundException
/*      */   {
/*  452 */     if (paramURL == null) {
/*  453 */       throw new ServiceNotFoundException("The specified URL is null");
/*      */     }
/*  455 */     return getMBeansFromURL(paramURL.toString());
/*      */   }
/*      */ 
/*      */   public Set<Object> getMBeansFromURL(String paramString)
/*      */     throws ServiceNotFoundException
/*      */   {
/*  484 */     String str1 = "getMBeansFromURL";
/*      */ 
/*  486 */     if (this.server == null) {
/*  487 */       throw new IllegalStateException("This MLet MBean is not registered with an MBeanServer.");
/*      */     }
/*      */ 
/*  491 */     if (paramString == null) {
/*  492 */       JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), str1, "URL is null");
/*      */ 
/*  494 */       throw new ServiceNotFoundException("The specified URL is null");
/*      */     }
/*  496 */     paramString = paramString.replace(File.separatorChar, '/');
/*      */ 
/*  498 */     if (JmxProperties.MLET_LOGGER.isLoggable(Level.FINER)) {
/*  499 */       JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), str1, "<URL = " + paramString + ">");
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  505 */       MLetParser localMLetParser = new MLetParser();
/*  506 */       this.mletList = localMLetParser.parseURL(paramString);
/*      */     } catch (Exception localException1) {
/*  508 */       localObject2 = "Problems while parsing URL [" + paramString + "], got exception [" + localException1.toString() + "]";
/*      */ 
/*  511 */       JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), str1, (String)localObject2);
/*  512 */       throw ((ServiceNotFoundException)EnvHelp.initCause(new ServiceNotFoundException((String)localObject2), localException1));
/*      */     }
/*      */ 
/*  516 */     if (this.mletList.size() == 0) {
/*  517 */       localObject1 = "File " + paramString + " not found or MLET tag not defined in file";
/*      */ 
/*  519 */       JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), str1, (String)localObject1);
/*  520 */       throw new ServiceNotFoundException((String)localObject1);
/*      */     }
/*      */ 
/*  524 */     Object localObject1 = new HashSet();
/*  525 */     for (Object localObject2 = this.mletList.iterator(); ((Iterator)localObject2).hasNext(); ) { MLetContent localMLetContent = (MLetContent)((Iterator)localObject2).next();
/*      */ 
/*  527 */       String str2 = localMLetContent.getCode();
/*  528 */       if ((str2 != null) && 
/*  529 */         (str2.endsWith(".class"))) {
/*  530 */         str2 = str2.substring(0, str2.length() - 6);
/*      */       }
/*      */ 
/*  533 */       String str3 = localMLetContent.getName();
/*  534 */       URL localURL1 = localMLetContent.getCodeBase();
/*  535 */       String str4 = localMLetContent.getVersion();
/*  536 */       String str5 = localMLetContent.getSerializedObject();
/*  537 */       String str6 = localMLetContent.getJarFiles();
/*  538 */       URL localURL2 = localMLetContent.getDocumentBase();
/*      */ 
/*  541 */       if (JmxProperties.MLET_LOGGER.isLoggable(Level.FINER)) {
/*  542 */         localObject3 = new StringBuilder().append("\n\tMLET TAG     = ").append(localMLetContent.getAttributes()).append("\n\tCODEBASE     = ").append(localURL1).append("\n\tARCHIVE      = ").append(str6).append("\n\tCODE         = ").append(str2).append("\n\tOBJECT       = ").append(str5).append("\n\tNAME         = ").append(str3).append("\n\tVERSION      = ").append(str4).append("\n\tDOCUMENT URL = ").append(localURL2);
/*      */ 
/*  551 */         JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), str1, ((StringBuilder)localObject3).toString());
/*      */       }
/*      */ 
/*  556 */       Object localObject3 = new StringTokenizer(str6, ",", false);
/*      */       Object localObject4;
/*  557 */       while (((StringTokenizer)localObject3).hasMoreTokens()) {
/*  558 */         localObject4 = ((StringTokenizer)localObject3).nextToken().trim();
/*  559 */         if (JmxProperties.MLET_LOGGER.isLoggable(Level.FINER)) {
/*  560 */           JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), str1, "Load archive for codebase <" + localURL1 + ">, file <" + (String)localObject4 + ">");
/*      */         }
/*      */ 
/*      */         try
/*      */         {
/*  571 */           localURL1 = check(str4, localURL1, (String)localObject4, localMLetContent);
/*      */         } catch (Exception localException2) {
/*  573 */           JmxProperties.MLET_LOGGER.logp(Level.FINEST, MLet.class.getName(), str1, "Got unexpected exception", localException2);
/*      */ 
/*  575 */           ((Set)localObject1).add(localException2);
/*  576 */         }continue;
/*      */         try
/*      */         {
/*  582 */           if (!Arrays.asList(getURLs()).contains(new URL(localURL1.toString() + (String)localObject4)))
/*      */           {
/*  584 */             addURL(localURL1 + (String)localObject4);
/*      */           }
/*      */ 
/*      */         }
/*      */         catch (MalformedURLException localMalformedURLException)
/*      */         {
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  598 */       if ((str2 != null) && (str5 != null))
/*      */       {
/*  602 */         JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), str1, "CODE and OBJECT parameters cannot be specified at the same time in tag MLET");
/*  603 */         ((Set)localObject1).add(new Error("CODE and OBJECT parameters cannot be specified at the same time in tag MLET"));
/*      */       }
/*  606 */       else if ((str2 == null) && (str5 == null))
/*      */       {
/*  610 */         JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), str1, "Either CODE or OBJECT parameter must be specified in tag MLET");
/*  611 */         ((Set)localObject1).add(new Error("Either CODE or OBJECT parameter must be specified in tag MLET"));
/*      */       } else {
/*      */         ObjectInstance localObjectInstance;
/*      */         try {
/*  615 */           if (str2 != null)
/*      */           {
/*  617 */             List localList1 = localMLetContent.getParameterTypes();
/*  618 */             List localList2 = localMLetContent.getParameterValues();
/*  619 */             ArrayList localArrayList = new ArrayList();
/*      */ 
/*  621 */             for (int i = 0; i < localList1.size(); i++) {
/*  622 */               localArrayList.add(constructParameter((String)localList2.get(i), (String)localList1.get(i)));
/*      */             }
/*      */ 
/*  625 */             if (localList1.isEmpty()) {
/*  626 */               if (str3 == null) {
/*  627 */                 localObjectInstance = this.server.createMBean(str2, null, this.mletObjectName);
/*      */               }
/*      */               else {
/*  630 */                 localObjectInstance = this.server.createMBean(str2, new ObjectName(str3), this.mletObjectName);
/*      */               }
/*      */             }
/*      */             else
/*      */             {
/*  635 */               Object[] arrayOfObject = localArrayList.toArray();
/*  636 */               String[] arrayOfString = new String[localList1.size()];
/*  637 */               localList1.toArray(arrayOfString);
/*  638 */               if (JmxProperties.MLET_LOGGER.isLoggable(Level.FINEST)) {
/*  639 */                 StringBuilder localStringBuilder = new StringBuilder();
/*  640 */                 for (int j = 0; j < arrayOfString.length; j++) {
/*  641 */                   localStringBuilder.append("\n\tSignature     = ").append(arrayOfString[j]).append("\t\nParams        = ").append(arrayOfObject[j]);
/*      */                 }
/*      */ 
/*  646 */                 JmxProperties.MLET_LOGGER.logp(Level.FINEST, MLet.class.getName(), str1, localStringBuilder.toString());
/*      */               }
/*      */ 
/*  650 */               if (str3 == null) {
/*  651 */                 localObjectInstance = this.server.createMBean(str2, null, this.mletObjectName, arrayOfObject, arrayOfString);
/*      */               }
/*      */               else
/*      */               {
/*  655 */                 localObjectInstance = this.server.createMBean(str2, new ObjectName(str3), this.mletObjectName, arrayOfObject, arrayOfString);
/*      */               }
/*      */             }
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/*  662 */             localObject4 = loadSerializedObject(localURL1, str5);
/*  663 */             if (str3 == null)
/*  664 */               this.server.registerMBean(localObject4, null);
/*      */             else {
/*  666 */               this.server.registerMBean(localObject4, new ObjectName(str3));
/*      */             }
/*  668 */             localObjectInstance = new ObjectInstance(str3, localObject4.getClass().getName());
/*      */           }
/*      */         } catch (ReflectionException localReflectionException) {
/*  671 */           JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), str1, "ReflectionException", localReflectionException);
/*      */ 
/*  673 */           ((Set)localObject1).add(localReflectionException);
/*  674 */           continue;
/*      */         } catch (InstanceAlreadyExistsException localInstanceAlreadyExistsException) {
/*  676 */           JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), str1, "InstanceAlreadyExistsException", localInstanceAlreadyExistsException);
/*      */ 
/*  678 */           ((Set)localObject1).add(localInstanceAlreadyExistsException);
/*  679 */           continue;
/*      */         } catch (MBeanRegistrationException localMBeanRegistrationException) {
/*  681 */           JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), str1, "MBeanRegistrationException", localMBeanRegistrationException);
/*      */ 
/*  683 */           ((Set)localObject1).add(localMBeanRegistrationException);
/*  684 */           continue;
/*      */         } catch (MBeanException localMBeanException) {
/*  686 */           JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), str1, "MBeanException", localMBeanException);
/*      */ 
/*  688 */           ((Set)localObject1).add(localMBeanException);
/*  689 */           continue;
/*      */         } catch (NotCompliantMBeanException localNotCompliantMBeanException) {
/*  691 */           JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), str1, "NotCompliantMBeanException", localNotCompliantMBeanException);
/*      */ 
/*  693 */           ((Set)localObject1).add(localNotCompliantMBeanException);
/*  694 */           continue;
/*      */         } catch (InstanceNotFoundException localInstanceNotFoundException) {
/*  696 */           JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), str1, "InstanceNotFoundException", localInstanceNotFoundException);
/*      */ 
/*  698 */           ((Set)localObject1).add(localInstanceNotFoundException);
/*  699 */           continue;
/*      */         } catch (IOException localIOException) {
/*  701 */           JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), str1, "IOException", localIOException);
/*      */ 
/*  703 */           ((Set)localObject1).add(localIOException);
/*  704 */           continue;
/*      */         } catch (SecurityException localSecurityException) {
/*  706 */           JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), str1, "SecurityException", localSecurityException);
/*      */ 
/*  708 */           ((Set)localObject1).add(localSecurityException);
/*  709 */           continue;
/*      */         } catch (Exception localException3) {
/*  711 */           JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), str1, "Exception", localException3);
/*      */ 
/*  713 */           ((Set)localObject1).add(localException3);
/*  714 */           continue;
/*      */         } catch (Error localError) {
/*  716 */           JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), str1, "Error", localError);
/*      */ 
/*  718 */           ((Set)localObject1).add(localError);
/*  719 */         }continue;
/*      */ 
/*  721 */         ((Set)localObject1).add(localObjectInstance);
/*      */       } }
/*  723 */     return localObject1;
/*      */   }
/*      */ 
/*      */   public synchronized String getLibraryDirectory()
/*      */   {
/*  738 */     return this.libraryDirectory;
/*      */   }
/*      */ 
/*      */   public synchronized void setLibraryDirectory(String paramString)
/*      */   {
/*  753 */     this.libraryDirectory = paramString;
/*      */   }
/*      */ 
/*      */   public ObjectName preRegister(MBeanServer paramMBeanServer, ObjectName paramObjectName)
/*      */     throws Exception
/*      */   {
/*  774 */     setMBeanServer(paramMBeanServer);
/*      */ 
/*  777 */     if (paramObjectName == null) {
/*  778 */       paramObjectName = new ObjectName(paramMBeanServer.getDefaultDomain() + ":" + "type=MLet");
/*      */     }
/*      */ 
/*  781 */     this.mletObjectName = paramObjectName;
/*  782 */     return this.mletObjectName;
/*      */   }
/*      */ 
/*      */   public void postRegister(Boolean paramBoolean)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void preDeregister()
/*      */     throws Exception
/*      */   {
/*      */   }
/*      */ 
/*      */   public void postDeregister()
/*      */   {
/*      */   }
/*      */ 
/*      */   public void writeExternal(ObjectOutput paramObjectOutput)
/*      */     throws IOException, UnsupportedOperationException
/*      */   {
/*  836 */     throw new UnsupportedOperationException("MLet.writeExternal");
/*      */   }
/*      */ 
/*      */   public void readExternal(ObjectInput paramObjectInput)
/*      */     throws IOException, ClassNotFoundException, UnsupportedOperationException
/*      */   {
/*  862 */     throw new UnsupportedOperationException("MLet.readExternal");
/*      */   }
/*      */ 
/*      */   public synchronized Class<?> loadClass(String paramString, ClassLoaderRepository paramClassLoaderRepository)
/*      */     throws ClassNotFoundException
/*      */   {
/*  891 */     ClassLoaderRepository localClassLoaderRepository = this.currentClr;
/*      */     try {
/*  893 */       this.currentClr = paramClassLoaderRepository;
/*  894 */       return loadClass(paramString);
/*      */     } finally {
/*  896 */       this.currentClr = localClassLoaderRepository;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected Class<?> findClass(String paramString)
/*      */     throws ClassNotFoundException
/*      */   {
/*  922 */     return findClass(paramString, this.currentClr);
/*      */   }
/*      */ 
/*      */   Class<?> findClass(String paramString, ClassLoaderRepository paramClassLoaderRepository)
/*      */     throws ClassNotFoundException
/*      */   {
/*  939 */     Class localClass = null;
/*  940 */     JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), "findClass", paramString);
/*      */     try
/*      */     {
/*  943 */       localClass = super.findClass(paramString);
/*  944 */       if (JmxProperties.MLET_LOGGER.isLoggable(Level.FINER)) {
/*  945 */         JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), "findClass", "Class " + paramString + " loaded through MLet classloader");
/*      */       }
/*      */ 
/*      */     }
/*      */     catch (ClassNotFoundException localClassNotFoundException1)
/*      */     {
/*  951 */       if (JmxProperties.MLET_LOGGER.isLoggable(Level.FINEST)) {
/*  952 */         JmxProperties.MLET_LOGGER.logp(Level.FINEST, MLet.class.getName(), "findClass", "Class " + paramString + " not found locally");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  958 */     if ((localClass == null) && (this.delegateToCLR) && (paramClassLoaderRepository != null))
/*      */     {
/*      */       try
/*      */       {
/*  962 */         if (JmxProperties.MLET_LOGGER.isLoggable(Level.FINEST)) {
/*  963 */           JmxProperties.MLET_LOGGER.logp(Level.FINEST, MLet.class.getName(), "findClass", "Class " + paramString + " : looking in CLR");
/*      */         }
/*      */ 
/*  967 */         localClass = paramClassLoaderRepository.loadClassBefore(this, paramString);
/*      */ 
/*  970 */         if (JmxProperties.MLET_LOGGER.isLoggable(Level.FINER)) {
/*  971 */           JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), "findClass", "Class " + paramString + " loaded through " + "the default classloader repository");
/*      */         }
/*      */ 
/*      */       }
/*      */       catch (ClassNotFoundException localClassNotFoundException2)
/*      */       {
/*  978 */         if (JmxProperties.MLET_LOGGER.isLoggable(Level.FINEST)) {
/*  979 */           JmxProperties.MLET_LOGGER.logp(Level.FINEST, MLet.class.getName(), "findClass", "Class " + paramString + " not found in CLR");
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  985 */     if (localClass == null) {
/*  986 */       JmxProperties.MLET_LOGGER.logp(Level.FINEST, MLet.class.getName(), "findClass", "Failed to load class " + paramString);
/*      */ 
/*  988 */       throw new ClassNotFoundException(paramString);
/*      */     }
/*  990 */     return localClass;
/*      */   }
/*      */ 
/*      */   protected String findLibrary(String paramString)
/*      */   {
/* 1035 */     String str2 = "findLibrary";
/*      */ 
/* 1039 */     String str3 = System.mapLibraryName(paramString);
/*      */ 
/* 1044 */     if (JmxProperties.MLET_LOGGER.isLoggable(Level.FINER)) {
/* 1045 */       JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), str2, "Search " + paramString + " in all JAR files");
/*      */     }
/*      */ 
/* 1054 */     if (JmxProperties.MLET_LOGGER.isLoggable(Level.FINER)) {
/* 1055 */       JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), str2, "loadLibraryAsResource(" + str3 + ")");
/*      */     }
/*      */ 
/* 1058 */     String str1 = loadLibraryAsResource(str3);
/* 1059 */     if (str1 != null) {
/* 1060 */       if (JmxProperties.MLET_LOGGER.isLoggable(Level.FINER)) {
/* 1061 */         JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), str2, str3 + " loaded, absolute path = " + str1);
/*      */       }
/*      */ 
/* 1064 */       return str1;
/*      */     }
/*      */ 
/* 1072 */     str3 = removeSpace(System.getProperty("os.name")) + File.separator + removeSpace(System.getProperty("os.arch")) + File.separator + removeSpace(System.getProperty("os.version")) + File.separator + "lib" + File.separator + str3;
/*      */ 
/* 1076 */     if (JmxProperties.MLET_LOGGER.isLoggable(Level.FINER)) {
/* 1077 */       JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), str2, "loadLibraryAsResource(" + str3 + ")");
/*      */     }
/*      */ 
/* 1081 */     str1 = loadLibraryAsResource(str3);
/* 1082 */     if (str1 != null) {
/* 1083 */       if (JmxProperties.MLET_LOGGER.isLoggable(Level.FINER)) {
/* 1084 */         JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), str2, str3 + " loaded, absolute path = " + str1);
/*      */       }
/*      */ 
/* 1087 */       return str1;
/*      */     }
/*      */ 
/* 1094 */     if (JmxProperties.MLET_LOGGER.isLoggable(Level.FINER)) {
/* 1095 */       JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), str2, paramString + " not found in any JAR file");
/*      */ 
/* 1097 */       JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), str2, "Search " + paramString + " along the path " + "specified as the java.library.path property");
/*      */     }
/*      */ 
/* 1105 */     return null;
/*      */   }
/*      */ 
/*      */   private String getTmpDir()
/*      */   {
/* 1117 */     String str1 = System.getProperty("java.io.tmpdir");
/* 1118 */     if (str1 != null) return str1;
/*      */ 
/* 1121 */     File localFile = null;
/*      */     try
/*      */     {
/* 1124 */       localFile = File.createTempFile("tmp", "jmx");
/* 1125 */       if (localFile == null)
/*      */       {
/*      */         boolean bool1;
/* 1125 */         return null;
/* 1126 */       }Object localObject1 = localFile.getParentFile();
/* 1127 */       if (localObject1 == null)
/*      */       {
/*      */         boolean bool2;
/* 1127 */         return null;
/*      */       }
/*      */       boolean bool3;
/* 1128 */       return ((File)localObject1).getAbsolutePath();
/*      */     }
/*      */     catch (Exception localException1)
/*      */     {
/*      */       String str2;
/* 1130 */       JmxProperties.MLET_LOGGER.logp(Level.FINEST, MLet.class.getName(), "getTmpDir", "Failed to determine system temporary dir");
/*      */       boolean bool4;
/* 1132 */       return null;
/*      */     }
/*      */     finally {
/* 1135 */       if (localFile != null)
/*      */         try {
/* 1137 */           boolean bool5 = localFile.delete();
/* 1138 */           if (!bool5)
/* 1139 */             JmxProperties.MLET_LOGGER.logp(Level.FINEST, MLet.class.getName(), "getTmpDir", "Failed to delete temp file");
/*      */         }
/*      */         catch (Exception localException6)
/*      */         {
/* 1143 */           JmxProperties.MLET_LOGGER.logp(Level.FINEST, MLet.class.getName(), "getTmpDir", "Failed to delete temporary file", localException6);
/*      */         }
/*      */     }
/*      */   }
/*      */ 
/*      */   private synchronized String loadLibraryAsResource(String paramString)
/*      */   {
/*      */     try
/*      */     {
/* 1158 */       InputStream localInputStream = getResourceAsStream(paramString.replace(File.separatorChar, '/'));
/*      */ 
/* 1160 */       if (localInputStream != null) try { File localFile1 = new File(this.libraryDirectory);
/* 1163 */           localFile1.mkdirs();
/* 1164 */           File localFile2 = Files.createTempFile(localFile1.toPath(), paramString + ".", null, new FileAttribute[0]).toFile();
/*      */ 
/* 1167 */           localFile2.deleteOnExit();
/* 1168 */           FileOutputStream localFileOutputStream = new FileOutputStream(localFile2);
/*      */           Object localObject1;
/*      */           try { localObject1 = new byte[4096];
/*      */             int i;
/* 1172 */             while ((i = localInputStream.read((byte[])localObject1)) >= 0)
/* 1173 */               localFileOutputStream.write((byte[])localObject1, 0, i);
/*      */           }
/*      */           finally
/*      */           {
/*      */           }
/* 1178 */           if (localFile2.exists())
/* 1179 */             return localFile2.getAbsolutePath();
/*      */         } finally
/*      */         {
/* 1182 */           localInputStream.close();
/*      */         } 
/*      */     }
/*      */     catch (Exception localException)
/*      */     {
/* 1186 */       JmxProperties.MLET_LOGGER.logp(Level.FINEST, MLet.class.getName(), "loadLibraryAsResource", "Failed to load library : " + paramString, localException);
/*      */ 
/* 1189 */       return null;
/*      */     }
/* 1191 */     return null;
/*      */   }
/*      */ 
/*      */   private static String removeSpace(String paramString)
/*      */   {
/* 1199 */     return paramString.trim().replace(" ", "");
/*      */   }
/*      */ 
/*      */   protected URL check(String paramString1, URL paramURL, String paramString2, MLetContent paramMLetContent)
/*      */     throws Exception
/*      */   {
/* 1231 */     return paramURL;
/*      */   }
/*      */ 
/*      */   private Object loadSerializedObject(URL paramURL, String paramString)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 1248 */     if (paramString != null) {
/* 1249 */       paramString = paramString.replace(File.separatorChar, '/');
/*      */     }
/* 1251 */     if (JmxProperties.MLET_LOGGER.isLoggable(Level.FINER)) {
/* 1252 */       JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), "loadSerializedObject", paramURL.toString() + paramString);
/*      */     }
/*      */ 
/* 1255 */     InputStream localInputStream = getResourceAsStream(paramString);
/* 1256 */     if (localInputStream != null) {
/*      */       try {
/* 1258 */         MLetObjectInputStream localMLetObjectInputStream = new MLetObjectInputStream(localInputStream, this);
/* 1259 */         Object localObject = localMLetObjectInputStream.readObject();
/* 1260 */         localMLetObjectInputStream.close();
/* 1261 */         return localObject;
/*      */       } catch (IOException localIOException) {
/* 1263 */         if (JmxProperties.MLET_LOGGER.isLoggable(Level.FINEST)) {
/* 1264 */           JmxProperties.MLET_LOGGER.logp(Level.FINEST, MLet.class.getName(), "loadSerializedObject", "Exception while deserializing " + paramString, localIOException);
/*      */         }
/*      */ 
/* 1268 */         throw localIOException;
/*      */       } catch (ClassNotFoundException localClassNotFoundException) {
/* 1270 */         if (JmxProperties.MLET_LOGGER.isLoggable(Level.FINEST)) {
/* 1271 */           JmxProperties.MLET_LOGGER.logp(Level.FINEST, MLet.class.getName(), "loadSerializedObject", "Exception while deserializing " + paramString, localClassNotFoundException);
/*      */         }
/*      */ 
/* 1275 */         throw localClassNotFoundException;
/*      */       }
/*      */     }
/* 1278 */     if (JmxProperties.MLET_LOGGER.isLoggable(Level.FINEST)) {
/* 1279 */       JmxProperties.MLET_LOGGER.logp(Level.FINEST, MLet.class.getName(), "loadSerializedObject", "Error: File " + paramString + " containing serialized object not found");
/*      */     }
/*      */ 
/* 1283 */     throw new Error("File " + paramString + " containing serialized object not found");
/*      */   }
/*      */ 
/*      */   private Object constructParameter(String paramString1, String paramString2)
/*      */   {
/* 1293 */     Class localClass = (Class)this.primitiveClasses.get(paramString2);
/* 1294 */     if (localClass != null) {
/*      */       try {
/* 1296 */         Constructor localConstructor = localClass.getConstructor(new Class[] { String.class });
/*      */ 
/* 1298 */         Object[] arrayOfObject = new Object[1];
/* 1299 */         arrayOfObject[0] = paramString1;
/* 1300 */         return localConstructor.newInstance(arrayOfObject);
/*      */       }
/*      */       catch (Exception localException) {
/* 1303 */         JmxProperties.MLET_LOGGER.logp(Level.FINEST, MLet.class.getName(), "constructParameter", "Got unexpected exception", localException);
/*      */       }
/*      */     }
/*      */ 
/* 1307 */     if (paramString2.compareTo("java.lang.Boolean") == 0)
/* 1308 */       return Boolean.valueOf(paramString1);
/* 1309 */     if (paramString2.compareTo("java.lang.Byte") == 0)
/* 1310 */       return new Byte(paramString1);
/* 1311 */     if (paramString2.compareTo("java.lang.Short") == 0)
/* 1312 */       return new Short(paramString1);
/* 1313 */     if (paramString2.compareTo("java.lang.Long") == 0)
/* 1314 */       return new Long(paramString1);
/* 1315 */     if (paramString2.compareTo("java.lang.Integer") == 0)
/* 1316 */       return new Integer(paramString1);
/* 1317 */     if (paramString2.compareTo("java.lang.Float") == 0)
/* 1318 */       return new Float(paramString1);
/* 1319 */     if (paramString2.compareTo("java.lang.Double") == 0)
/* 1320 */       return new Double(paramString1);
/* 1321 */     if (paramString2.compareTo("java.lang.String") == 0) {
/* 1322 */       return paramString1;
/*      */     }
/* 1324 */     return paramString1;
/*      */   }
/*      */ 
/*      */   private synchronized void setMBeanServer(final MBeanServer paramMBeanServer) {
/* 1328 */     this.server = paramMBeanServer;
/* 1329 */     PrivilegedAction local1 = new PrivilegedAction()
/*      */     {
/*      */       public ClassLoaderRepository run() {
/* 1332 */         return paramMBeanServer.getClassLoaderRepository();
/*      */       }
/*      */     };
/* 1335 */     this.currentClr = ((ClassLoaderRepository)AccessController.doPrivileged(local1));
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.loading.MLet
 * JD-Core Version:    0.6.2
 */