/*      */ package com.sun.jmx.interceptor;
/*      */ 
/*      */ import com.sun.jmx.defaults.JmxProperties;
/*      */ import com.sun.jmx.mbeanserver.DynamicMBean2;
/*      */ import com.sun.jmx.mbeanserver.Introspector;
/*      */ import com.sun.jmx.mbeanserver.MBeanInstantiator;
/*      */ import com.sun.jmx.mbeanserver.ModifiableClassLoaderRepository;
/*      */ import com.sun.jmx.mbeanserver.NamedObject;
/*      */ import com.sun.jmx.mbeanserver.Repository;
/*      */ import com.sun.jmx.mbeanserver.Repository.RegistrationContext;
/*      */ import com.sun.jmx.mbeanserver.Util;
/*      */ import com.sun.jmx.remote.util.EnvHelp;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.security.AccessControlContext;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.security.ProtectionDomain;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Set;
/*      */ import java.util.WeakHashMap;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import javax.management.Attribute;
/*      */ import javax.management.AttributeList;
/*      */ import javax.management.AttributeNotFoundException;
/*      */ import javax.management.DynamicMBean;
/*      */ import javax.management.InstanceAlreadyExistsException;
/*      */ import javax.management.InstanceNotFoundException;
/*      */ import javax.management.IntrospectionException;
/*      */ import javax.management.InvalidAttributeValueException;
/*      */ import javax.management.JMRuntimeException;
/*      */ import javax.management.ListenerNotFoundException;
/*      */ import javax.management.MBeanException;
/*      */ import javax.management.MBeanInfo;
/*      */ import javax.management.MBeanPermission;
/*      */ import javax.management.MBeanRegistration;
/*      */ import javax.management.MBeanRegistrationException;
/*      */ import javax.management.MBeanServer;
/*      */ import javax.management.MBeanServerDelegate;
/*      */ import javax.management.MBeanServerNotification;
/*      */ import javax.management.MBeanTrustPermission;
/*      */ import javax.management.NotCompliantMBeanException;
/*      */ import javax.management.Notification;
/*      */ import javax.management.NotificationBroadcaster;
/*      */ import javax.management.NotificationEmitter;
/*      */ import javax.management.NotificationFilter;
/*      */ import javax.management.NotificationListener;
/*      */ import javax.management.ObjectInstance;
/*      */ import javax.management.ObjectName;
/*      */ import javax.management.OperationsException;
/*      */ import javax.management.QueryEval;
/*      */ import javax.management.QueryExp;
/*      */ import javax.management.ReflectionException;
/*      */ import javax.management.RuntimeErrorException;
/*      */ import javax.management.RuntimeMBeanException;
/*      */ import javax.management.RuntimeOperationsException;
/*      */ import javax.management.loading.ClassLoaderRepository;
/*      */ 
/*      */ public class DefaultMBeanServerInterceptor
/*      */   implements MBeanServerInterceptor
/*      */ {
/*      */   private final transient MBeanInstantiator instantiator;
/*  121 */   private transient MBeanServer server = null;
/*      */   private final transient MBeanServerDelegate delegate;
/*      */   private final transient Repository repository;
/*  132 */   private final transient WeakHashMap<ListenerWrapper, WeakReference<ListenerWrapper>> listenerWrappers = new WeakHashMap();
/*      */   private final String domain;
/*  350 */   private final Set<ObjectName> beingUnregistered = new HashSet();
/*      */ 
/*      */   public DefaultMBeanServerInterceptor(MBeanServer paramMBeanServer, MBeanServerDelegate paramMBeanServerDelegate, MBeanInstantiator paramMBeanInstantiator, Repository paramRepository)
/*      */   {
/*  164 */     if (paramMBeanServer == null) throw new IllegalArgumentException("outer MBeanServer cannot be null");
/*      */ 
/*  166 */     if (paramMBeanServerDelegate == null) throw new IllegalArgumentException("MBeanServerDelegate cannot be null");
/*      */ 
/*  168 */     if (paramMBeanInstantiator == null) throw new IllegalArgumentException("MBeanInstantiator cannot be null");
/*      */ 
/*  170 */     if (paramRepository == null) throw new IllegalArgumentException("Repository cannot be null");
/*      */ 
/*  173 */     this.server = paramMBeanServer;
/*  174 */     this.delegate = paramMBeanServerDelegate;
/*  175 */     this.instantiator = paramMBeanInstantiator;
/*  176 */     this.repository = paramRepository;
/*  177 */     this.domain = paramRepository.getDefaultDomain();
/*      */   }
/*      */ 
/*      */   public ObjectInstance createMBean(String paramString, ObjectName paramObjectName)
/*      */     throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException
/*      */   {
/*  185 */     return createMBean(paramString, paramObjectName, (Object[])null, (String[])null);
/*      */   }
/*      */ 
/*      */   public ObjectInstance createMBean(String paramString, ObjectName paramObjectName1, ObjectName paramObjectName2)
/*      */     throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException
/*      */   {
/*  195 */     return createMBean(paramString, paramObjectName1, paramObjectName2, (Object[])null, (String[])null);
/*      */   }
/*      */ 
/*      */   public ObjectInstance createMBean(String paramString, ObjectName paramObjectName, Object[] paramArrayOfObject, String[] paramArrayOfString)
/*      */     throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException
/*      */   {
/*      */     try
/*      */     {
/*  206 */       return createMBean(paramString, paramObjectName, null, true, paramArrayOfObject, paramArrayOfString);
/*      */     }
/*      */     catch (InstanceNotFoundException localInstanceNotFoundException)
/*      */     {
/*  211 */       throw ((IllegalArgumentException)EnvHelp.initCause(new IllegalArgumentException("Unexpected exception: " + localInstanceNotFoundException), localInstanceNotFoundException));
/*      */     }
/*      */   }
/*      */ 
/*      */   public ObjectInstance createMBean(String paramString, ObjectName paramObjectName1, ObjectName paramObjectName2, Object[] paramArrayOfObject, String[] paramArrayOfString)
/*      */     throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException
/*      */   {
/*  223 */     return createMBean(paramString, paramObjectName1, paramObjectName2, false, paramArrayOfObject, paramArrayOfString);
/*      */   }
/*      */ 
/*      */   private ObjectInstance createMBean(String paramString, ObjectName paramObjectName1, ObjectName paramObjectName2, boolean paramBoolean, Object[] paramArrayOfObject, String[] paramArrayOfString)
/*      */     throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException
/*      */   {
/*  237 */     if (paramString == null) {
/*  238 */       localObject = new IllegalArgumentException("The class name cannot be null");
/*      */ 
/*  240 */       throw new RuntimeOperationsException((RuntimeException)localObject, "Exception occurred during MBean creation");
/*      */     }
/*      */ 
/*  244 */     if (paramObjectName1 != null) {
/*  245 */       if (paramObjectName1.isPattern()) {
/*  246 */         localObject = new IllegalArgumentException("Invalid name->" + paramObjectName1.toString());
/*      */ 
/*  250 */         throw new RuntimeOperationsException((RuntimeException)localObject, "Exception occurred during MBean creation");
/*      */       }
/*      */ 
/*  253 */       paramObjectName1 = nonDefaultDomain(paramObjectName1);
/*      */     }
/*      */ 
/*  256 */     checkMBeanPermission(paramString, null, null, "instantiate");
/*  257 */     checkMBeanPermission(paramString, null, paramObjectName1, "registerMBean");
/*      */     Class localClass;
/*  260 */     if (paramBoolean) {
/*  261 */       if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINER)) {
/*  262 */         JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINER, DefaultMBeanServerInterceptor.class.getName(), "createMBean", "ClassName = " + paramString + ", ObjectName = " + paramObjectName1);
/*      */       }
/*      */ 
/*  267 */       localClass = this.instantiator.findClassWithDefaultLoaderRepository(paramString);
/*      */     }
/*  269 */     else if (paramObjectName2 == null) {
/*  270 */       if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINER)) {
/*  271 */         JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINER, DefaultMBeanServerInterceptor.class.getName(), "createMBean", "ClassName = " + paramString + ", ObjectName = " + paramObjectName1 + ", Loader name = null");
/*      */       }
/*      */ 
/*  277 */       localClass = this.instantiator.findClass(paramString, this.server.getClass().getClassLoader());
/*      */     }
/*      */     else {
/*  280 */       paramObjectName2 = nonDefaultDomain(paramObjectName2);
/*      */ 
/*  282 */       if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINER)) {
/*  283 */         JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINER, DefaultMBeanServerInterceptor.class.getName(), "createMBean", "ClassName = " + paramString + ", ObjectName = " + paramObjectName1 + ", Loader name = " + paramObjectName2);
/*      */       }
/*      */ 
/*  290 */       localClass = this.instantiator.findClass(paramString, paramObjectName2);
/*      */     }
/*      */ 
/*  293 */     checkMBeanTrustPermission(localClass);
/*      */ 
/*  296 */     Introspector.testCreation(localClass);
/*      */ 
/*  299 */     Introspector.checkCompliance(localClass);
/*      */ 
/*  301 */     Object localObject = this.instantiator.instantiate(localClass, paramArrayOfObject, paramArrayOfString, this.server.getClass().getClassLoader());
/*      */ 
/*  304 */     String str = getNewMBeanClassName(localObject);
/*      */ 
/*  306 */     return registerObject(str, localObject, paramObjectName1);
/*      */   }
/*      */ 
/*      */   public ObjectInstance registerMBean(Object paramObject, ObjectName paramObjectName)
/*      */     throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException
/*      */   {
/*  315 */     Class localClass = paramObject.getClass();
/*      */ 
/*  317 */     Introspector.checkCompliance(localClass);
/*      */ 
/*  319 */     String str = getNewMBeanClassName(paramObject);
/*      */ 
/*  321 */     checkMBeanPermission(str, null, paramObjectName, "registerMBean");
/*  322 */     checkMBeanTrustPermission(localClass);
/*      */ 
/*  324 */     return registerObject(str, paramObject, paramObjectName);
/*      */   }
/*      */ 
/*      */   private static String getNewMBeanClassName(Object paramObject) throws NotCompliantMBeanException
/*      */   {
/*  329 */     if ((paramObject instanceof DynamicMBean)) { DynamicMBean localDynamicMBean = (DynamicMBean)paramObject;
/*      */       String str;
/*      */       try {
/*  333 */         str = localDynamicMBean.getMBeanInfo().getClassName();
/*      */       }
/*      */       catch (Exception localException) {
/*  336 */         NotCompliantMBeanException localNotCompliantMBeanException = new NotCompliantMBeanException("Bad getMBeanInfo()");
/*      */ 
/*  338 */         localNotCompliantMBeanException.initCause(localException);
/*  339 */         throw localNotCompliantMBeanException;
/*      */       }
/*  341 */       if (str == null)
/*      */       {
/*  343 */         throw new NotCompliantMBeanException("MBeanInfo has null class name");
/*      */       }
/*  345 */       return str;
/*      */     }
/*  347 */     return paramObject.getClass().getName();
/*      */   }
/*      */ 
/*      */   public void unregisterMBean(ObjectName paramObjectName)
/*      */     throws InstanceNotFoundException, MBeanRegistrationException
/*      */   {
/*  356 */     if (paramObjectName == null) {
/*  357 */       IllegalArgumentException localIllegalArgumentException = new IllegalArgumentException("Object name cannot be null");
/*      */ 
/*  359 */       throw new RuntimeOperationsException(localIllegalArgumentException, "Exception occurred trying to unregister the MBean");
/*      */     }
/*      */ 
/*  363 */     paramObjectName = nonDefaultDomain(paramObjectName);
/*      */ 
/*  401 */     synchronized (this.beingUnregistered) {
/*  402 */       while (this.beingUnregistered.contains(paramObjectName)) {
/*      */         try {
/*  404 */           this.beingUnregistered.wait();
/*      */         } catch (InterruptedException localInterruptedException) {
/*  406 */           throw new MBeanRegistrationException(localInterruptedException, localInterruptedException.toString());
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  411 */       this.beingUnregistered.add(paramObjectName);
/*      */     }
/*      */     try
/*      */     {
/*  415 */       exclusiveUnregisterMBean(paramObjectName);
/*      */     } finally {
/*  417 */       synchronized (this.beingUnregistered) {
/*  418 */         this.beingUnregistered.remove(paramObjectName);
/*  419 */         this.beingUnregistered.notifyAll();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void exclusiveUnregisterMBean(ObjectName paramObjectName)
/*      */     throws InstanceNotFoundException, MBeanRegistrationException
/*      */   {
/*  427 */     DynamicMBean localDynamicMBean = getMBean(paramObjectName);
/*      */ 
/*  430 */     checkMBeanPermission(localDynamicMBean, null, paramObjectName, "unregisterMBean");
/*      */ 
/*  432 */     if ((localDynamicMBean instanceof MBeanRegistration)) {
/*  433 */       preDeregisterInvoke((MBeanRegistration)localDynamicMBean);
/*      */     }
/*  435 */     Object localObject1 = getResource(localDynamicMBean);
/*      */ 
/*  447 */     ResourceContext localResourceContext = unregisterFromRepository(localObject1, localDynamicMBean, paramObjectName);
/*      */     try
/*      */     {
/*  451 */       if ((localDynamicMBean instanceof MBeanRegistration))
/*  452 */         postDeregisterInvoke(paramObjectName, (MBeanRegistration)localDynamicMBean);
/*      */     } finally {
/*  454 */       localResourceContext.done();
/*      */     }
/*      */   }
/*      */ 
/*      */   public ObjectInstance getObjectInstance(ObjectName paramObjectName)
/*      */     throws InstanceNotFoundException
/*      */   {
/*  461 */     paramObjectName = nonDefaultDomain(paramObjectName);
/*  462 */     DynamicMBean localDynamicMBean = getMBean(paramObjectName);
/*      */ 
/*  464 */     checkMBeanPermission(localDynamicMBean, null, paramObjectName, "getObjectInstance");
/*      */ 
/*  466 */     String str = getClassName(localDynamicMBean);
/*      */ 
/*  468 */     return new ObjectInstance(paramObjectName, str);
/*      */   }
/*      */ 
/*      */   public Set<ObjectInstance> queryMBeans(ObjectName paramObjectName, QueryExp paramQueryExp) {
/*  472 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  473 */     if (localSecurityManager != null)
/*      */     {
/*  476 */       checkMBeanPermission((String)null, null, null, "queryMBeans");
/*      */ 
/*  480 */       Set localSet = queryMBeansImpl(paramObjectName, null);
/*      */ 
/*  485 */       HashSet localHashSet = new HashSet(localSet.size());
/*      */ 
/*  487 */       for (ObjectInstance localObjectInstance : localSet) {
/*      */         try {
/*  489 */           checkMBeanPermission(localObjectInstance.getClassName(), null, localObjectInstance.getObjectName(), "queryMBeans");
/*      */ 
/*  491 */           localHashSet.add(localObjectInstance);
/*      */         }
/*      */         catch (SecurityException localSecurityException)
/*      */         {
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  499 */       return filterListOfObjectInstances(localHashSet, paramQueryExp);
/*      */     }
/*      */ 
/*  503 */     return queryMBeansImpl(paramObjectName, paramQueryExp);
/*      */   }
/*      */ 
/*      */   private Set<ObjectInstance> queryMBeansImpl(ObjectName paramObjectName, QueryExp paramQueryExp)
/*      */   {
/*  511 */     Set localSet = this.repository.query(paramObjectName, paramQueryExp);
/*      */ 
/*  513 */     return objectInstancesFromFilteredNamedObjects(localSet, paramQueryExp);
/*      */   }
/*      */ 
/*      */   public Set<ObjectName> queryNames(ObjectName paramObjectName, QueryExp paramQueryExp)
/*      */   {
/*  518 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*      */     Object localObject3;
/*      */     Object localObject1;
/*  519 */     if (localSecurityManager != null)
/*      */     {
/*  522 */       checkMBeanPermission((String)null, null, null, "queryNames");
/*      */ 
/*  526 */       Set localSet = queryMBeansImpl(paramObjectName, null);
/*      */ 
/*  531 */       HashSet localHashSet = new HashSet(localSet.size());
/*      */ 
/*  533 */       for (Object localObject2 = localSet.iterator(); ((Iterator)localObject2).hasNext(); ) { localObject3 = (ObjectInstance)((Iterator)localObject2).next();
/*      */         try {
/*  535 */           checkMBeanPermission(((ObjectInstance)localObject3).getClassName(), null, ((ObjectInstance)localObject3).getObjectName(), "queryNames");
/*      */ 
/*  537 */           localHashSet.add(localObject3);
/*      */         }
/*      */         catch (SecurityException localSecurityException)
/*      */         {
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  545 */       localObject2 = filterListOfObjectInstances(localHashSet, paramQueryExp);
/*      */ 
/*  547 */       localObject1 = new HashSet(((Set)localObject2).size());
/*  548 */       for (localObject3 = ((Set)localObject2).iterator(); ((Iterator)localObject3).hasNext(); ) { ObjectInstance localObjectInstance = (ObjectInstance)((Iterator)localObject3).next();
/*  549 */         ((Set)localObject1).add(localObjectInstance.getObjectName());
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  554 */       localObject1 = queryNamesImpl(paramObjectName, paramQueryExp);
/*      */     }
/*  556 */     return localObject1;
/*      */   }
/*      */ 
/*      */   private Set<ObjectName> queryNamesImpl(ObjectName paramObjectName, QueryExp paramQueryExp)
/*      */   {
/*  562 */     Set localSet = this.repository.query(paramObjectName, paramQueryExp);
/*      */ 
/*  564 */     return objectNamesFromFilteredNamedObjects(localSet, paramQueryExp);
/*      */   }
/*      */ 
/*      */   public boolean isRegistered(ObjectName paramObjectName) {
/*  568 */     if (paramObjectName == null) {
/*  569 */       throw new RuntimeOperationsException(new IllegalArgumentException("Object name cannot be null"), "Object name cannot be null");
/*      */     }
/*      */ 
/*  574 */     paramObjectName = nonDefaultDomain(paramObjectName);
/*      */ 
/*  579 */     return this.repository.contains(paramObjectName);
/*      */   }
/*      */ 
/*      */   public String[] getDomains() {
/*  583 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  584 */     if (localSecurityManager != null)
/*      */     {
/*  587 */       checkMBeanPermission((String)null, null, null, "getDomains");
/*      */ 
/*  591 */       String[] arrayOfString = this.repository.getDomains();
/*      */ 
/*  596 */       ArrayList localArrayList = new ArrayList(arrayOfString.length);
/*  597 */       for (int i = 0; i < arrayOfString.length; i++) {
/*      */         try {
/*  599 */           ObjectName localObjectName = Util.newObjectName(arrayOfString[i] + ":x=x");
/*  600 */           checkMBeanPermission((String)null, null, localObjectName, "getDomains");
/*  601 */           localArrayList.add(arrayOfString[i]);
/*      */         }
/*      */         catch (SecurityException localSecurityException)
/*      */         {
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  609 */       return (String[])localArrayList.toArray(new String[localArrayList.size()]);
/*      */     }
/*  611 */     return this.repository.getDomains();
/*      */   }
/*      */ 
/*      */   public Integer getMBeanCount()
/*      */   {
/*  616 */     return this.repository.getCount();
/*      */   }
/*      */ 
/*      */   public Object getAttribute(ObjectName paramObjectName, String paramString)
/*      */     throws MBeanException, AttributeNotFoundException, InstanceNotFoundException, ReflectionException
/*      */   {
/*  623 */     if (paramObjectName == null) {
/*  624 */       throw new RuntimeOperationsException(new IllegalArgumentException("Object name cannot be null"), "Exception occurred trying to invoke the getter on the MBean");
/*      */     }
/*      */ 
/*  628 */     if (paramString == null) {
/*  629 */       throw new RuntimeOperationsException(new IllegalArgumentException("Attribute cannot be null"), "Exception occurred trying to invoke the getter on the MBean");
/*      */     }
/*      */ 
/*  634 */     paramObjectName = nonDefaultDomain(paramObjectName);
/*      */ 
/*  636 */     if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINER)) {
/*  637 */       JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINER, DefaultMBeanServerInterceptor.class.getName(), "getAttribute", "Attribute = " + paramString + ", ObjectName = " + paramObjectName);
/*      */     }
/*      */ 
/*  643 */     DynamicMBean localDynamicMBean = getMBean(paramObjectName);
/*  644 */     checkMBeanPermission(localDynamicMBean, paramString, paramObjectName, "getAttribute");
/*      */     try
/*      */     {
/*  647 */       return localDynamicMBean.getAttribute(paramString);
/*      */     } catch (AttributeNotFoundException localAttributeNotFoundException) {
/*  649 */       throw localAttributeNotFoundException;
/*      */     } catch (Throwable localThrowable) {
/*  651 */       rethrowMaybeMBeanException(localThrowable);
/*  652 */     }throw new AssertionError();
/*      */   }
/*      */ 
/*      */   public AttributeList getAttributes(ObjectName paramObjectName, String[] paramArrayOfString)
/*      */     throws InstanceNotFoundException, ReflectionException
/*      */   {
/*  659 */     if (paramObjectName == null) {
/*  660 */       throw new RuntimeOperationsException(new IllegalArgumentException("ObjectName name cannot be null"), "Exception occurred trying to invoke the getter on the MBean");
/*      */     }
/*      */ 
/*  665 */     if (paramArrayOfString == null) {
/*  666 */       throw new RuntimeOperationsException(new IllegalArgumentException("Attributes cannot be null"), "Exception occurred trying to invoke the getter on the MBean");
/*      */     }
/*      */ 
/*  671 */     paramObjectName = nonDefaultDomain(paramObjectName);
/*      */ 
/*  673 */     if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINER)) {
/*  674 */       JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINER, DefaultMBeanServerInterceptor.class.getName(), "getAttributes", "ObjectName = " + paramObjectName);
/*      */     }
/*      */ 
/*  679 */     DynamicMBean localDynamicMBean = getMBean(paramObjectName);
/*      */ 
/*  681 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*      */     String[] arrayOfString1;
/*  682 */     if (localSecurityManager == null) {
/*  683 */       arrayOfString1 = paramArrayOfString;
/*      */     } else {
/*  685 */       String str1 = getClassName(localDynamicMBean);
/*      */ 
/*  689 */       checkMBeanPermission(str1, null, paramObjectName, "getAttribute");
/*      */ 
/*  694 */       ArrayList localArrayList = new ArrayList(paramArrayOfString.length);
/*      */ 
/*  696 */       for (String str2 : paramArrayOfString)
/*      */         try {
/*  698 */           checkMBeanPermission(str1, str2, paramObjectName, "getAttribute");
/*  699 */           localArrayList.add(str2);
/*      */         }
/*      */         catch (SecurityException localSecurityException)
/*      */         {
/*      */         }
/*  704 */       arrayOfString1 = (String[])localArrayList.toArray(new String[localArrayList.size()]);
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  709 */       return localDynamicMBean.getAttributes(arrayOfString1);
/*      */     } catch (Throwable localThrowable) {
/*  711 */       rethrow(localThrowable);
/*  712 */     }throw new AssertionError();
/*      */   }
/*      */ 
/*      */   public void setAttribute(ObjectName paramObjectName, Attribute paramAttribute)
/*      */     throws InstanceNotFoundException, AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException
/*      */   {
/*  721 */     if (paramObjectName == null) {
/*  722 */       throw new RuntimeOperationsException(new IllegalArgumentException("ObjectName name cannot be null"), "Exception occurred trying to invoke the setter on the MBean");
/*      */     }
/*      */ 
/*  727 */     if (paramAttribute == null) {
/*  728 */       throw new RuntimeOperationsException(new IllegalArgumentException("Attribute cannot be null"), "Exception occurred trying to invoke the setter on the MBean");
/*      */     }
/*      */ 
/*  733 */     paramObjectName = nonDefaultDomain(paramObjectName);
/*      */ 
/*  735 */     if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINER)) {
/*  736 */       JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINER, DefaultMBeanServerInterceptor.class.getName(), "setAttribute", "ObjectName = " + paramObjectName + ", Attribute = " + paramAttribute.getName());
/*      */     }
/*      */ 
/*  742 */     DynamicMBean localDynamicMBean = getMBean(paramObjectName);
/*  743 */     checkMBeanPermission(localDynamicMBean, paramAttribute.getName(), paramObjectName, "setAttribute");
/*      */     try
/*      */     {
/*  746 */       localDynamicMBean.setAttribute(paramAttribute);
/*      */     } catch (AttributeNotFoundException localAttributeNotFoundException) {
/*  748 */       throw localAttributeNotFoundException;
/*      */     } catch (InvalidAttributeValueException localInvalidAttributeValueException) {
/*  750 */       throw localInvalidAttributeValueException;
/*      */     } catch (Throwable localThrowable) {
/*  752 */       rethrowMaybeMBeanException(localThrowable);
/*  753 */       throw new AssertionError();
/*      */     }
/*      */   }
/*      */ 
/*      */   public AttributeList setAttributes(ObjectName paramObjectName, AttributeList paramAttributeList)
/*      */     throws InstanceNotFoundException, ReflectionException
/*      */   {
/*  761 */     if (paramObjectName == null) {
/*  762 */       throw new RuntimeOperationsException(new IllegalArgumentException("ObjectName name cannot be null"), "Exception occurred trying to invoke the setter on the MBean");
/*      */     }
/*      */ 
/*  767 */     if (paramAttributeList == null) {
/*  768 */       throw new RuntimeOperationsException(new IllegalArgumentException("AttributeList  cannot be null"), "Exception occurred trying to invoke the setter on the MBean");
/*      */     }
/*      */ 
/*  773 */     paramObjectName = nonDefaultDomain(paramObjectName);
/*      */ 
/*  775 */     DynamicMBean localDynamicMBean = getMBean(paramObjectName);
/*      */ 
/*  777 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*      */     AttributeList localAttributeList;
/*      */     String str;
/*  778 */     if (localSecurityManager == null) {
/*  779 */       localAttributeList = paramAttributeList;
/*      */     } else {
/*  781 */       str = getClassName(localDynamicMBean);
/*      */ 
/*  785 */       checkMBeanPermission(str, null, paramObjectName, "setAttribute");
/*      */ 
/*  790 */       localAttributeList = new AttributeList(paramAttributeList.size());
/*  791 */       for (Attribute localAttribute : paramAttributeList.asList())
/*      */         try {
/*  793 */           checkMBeanPermission(str, localAttribute.getName(), paramObjectName, "setAttribute");
/*      */ 
/*  795 */           localAttributeList.add(localAttribute);
/*      */         }
/*      */         catch (SecurityException localSecurityException)
/*      */         {
/*      */         }
/*      */     }
/*      */     try {
/*  802 */       return localDynamicMBean.setAttributes(localAttributeList);
/*      */     } catch (Throwable localThrowable) {
/*  804 */       rethrow(localThrowable);
/*  805 */     }throw new AssertionError();
/*      */   }
/*      */ 
/*      */   public Object invoke(ObjectName paramObjectName, String paramString, Object[] paramArrayOfObject, String[] paramArrayOfString)
/*      */     throws InstanceNotFoundException, MBeanException, ReflectionException
/*      */   {
/*  814 */     paramObjectName = nonDefaultDomain(paramObjectName);
/*      */ 
/*  816 */     DynamicMBean localDynamicMBean = getMBean(paramObjectName);
/*  817 */     checkMBeanPermission(localDynamicMBean, paramString, paramObjectName, "invoke");
/*      */     try {
/*  819 */       return localDynamicMBean.invoke(paramString, paramArrayOfObject, paramArrayOfString);
/*      */     } catch (Throwable localThrowable) {
/*  821 */       rethrowMaybeMBeanException(localThrowable);
/*  822 */     }throw new AssertionError();
/*      */   }
/*      */ 
/*      */   private static void rethrow(Throwable paramThrowable)
/*      */     throws ReflectionException
/*      */   {
/*      */     try
/*      */     {
/*  831 */       throw paramThrowable;
/*      */     } catch (ReflectionException localReflectionException) {
/*  833 */       throw localReflectionException;
/*      */     } catch (RuntimeOperationsException localRuntimeOperationsException) {
/*  835 */       throw localRuntimeOperationsException;
/*      */     } catch (RuntimeErrorException localRuntimeErrorException) {
/*  837 */       throw localRuntimeErrorException;
/*      */     } catch (RuntimeException localRuntimeException) {
/*  839 */       throw new RuntimeMBeanException(localRuntimeException, localRuntimeException.toString());
/*      */     } catch (Error localError) {
/*  841 */       throw new RuntimeErrorException(localError, localError.toString());
/*      */     }
/*      */     catch (Throwable localThrowable) {
/*  844 */       throw new RuntimeException("Unexpected exception", localThrowable);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void rethrowMaybeMBeanException(Throwable paramThrowable) throws ReflectionException, MBeanException
/*      */   {
/*  850 */     if ((paramThrowable instanceof MBeanException))
/*  851 */       throw ((MBeanException)paramThrowable);
/*  852 */     rethrow(paramThrowable);
/*      */   }
/*      */ 
/*      */   private ObjectInstance registerObject(String paramString, Object paramObject, ObjectName paramObjectName)
/*      */     throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException
/*      */   {
/*  891 */     if (paramObject == null) {
/*  892 */       localObject = new IllegalArgumentException("Cannot add null object");
/*      */ 
/*  894 */       throw new RuntimeOperationsException((RuntimeException)localObject, "Exception occurred trying to register the MBean");
/*      */     }
/*      */ 
/*  898 */     Object localObject = Introspector.makeDynamicMBean(paramObject);
/*      */ 
/*  900 */     return registerDynamicMBean(paramString, (DynamicMBean)localObject, paramObjectName);
/*      */   }
/*      */ 
/*      */   private ObjectInstance registerDynamicMBean(String paramString, DynamicMBean paramDynamicMBean, ObjectName paramObjectName)
/*      */     throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException
/*      */   {
/*  911 */     paramObjectName = nonDefaultDomain(paramObjectName);
/*      */ 
/*  913 */     if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINER)) {
/*  914 */       JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINER, DefaultMBeanServerInterceptor.class.getName(), "registerMBean", "ObjectName = " + paramObjectName);
/*      */     }
/*      */ 
/*  919 */     ObjectName localObjectName = preRegister(paramDynamicMBean, this.server, paramObjectName);
/*      */ 
/*  923 */     boolean bool1 = false;
/*  924 */     boolean bool2 = false;
/*  925 */     ResourceContext localResourceContext = null;
/*      */     try
/*      */     {
/*  928 */       if ((paramDynamicMBean instanceof DynamicMBean2)) {
/*      */         try {
/*  930 */           ((DynamicMBean2)paramDynamicMBean).preRegister2(this.server, localObjectName);
/*  931 */           bool2 = true;
/*      */         } catch (Exception localException) {
/*  933 */           if ((localException instanceof RuntimeException))
/*  934 */             throw ((RuntimeException)localException);
/*  935 */           if ((localException instanceof InstanceAlreadyExistsException))
/*  936 */             throw ((InstanceAlreadyExistsException)localException);
/*  937 */           throw new RuntimeException(localException);
/*      */         }
/*      */       }
/*      */ 
/*  941 */       if ((localObjectName != paramObjectName) && (localObjectName != null)) {
/*  942 */         localObjectName = ObjectName.getInstance(nonDefaultDomain(localObjectName));
/*      */       }
/*      */ 
/*  946 */       checkMBeanPermission(paramString, null, localObjectName, "registerMBean");
/*      */ 
/*  948 */       if (localObjectName == null) {
/*  949 */         localObject1 = new IllegalArgumentException("No object name specified");
/*      */ 
/*  951 */         throw new RuntimeOperationsException((RuntimeException)localObject1, "Exception occurred trying to register the MBean");
/*      */       }
/*      */ 
/*  955 */       Object localObject1 = getResource(paramDynamicMBean);
/*      */ 
/*  966 */       localResourceContext = registerWithRepository(localObject1, paramDynamicMBean, localObjectName);
/*      */ 
/*  969 */       bool2 = false;
/*  970 */       bool1 = true;
/*      */     }
/*      */     finally {
/*      */       try {
/*  974 */         postRegister(localObjectName, paramDynamicMBean, bool1, bool2);
/*      */       } finally {
/*  976 */         if ((bool1) && (localResourceContext != null)) localResourceContext.done();
/*      */       }
/*      */     }
/*  979 */     return new ObjectInstance(localObjectName, paramString);
/*      */   }
/*      */ 
/*      */   private static void throwMBeanRegistrationException(Throwable paramThrowable, String paramString) throws MBeanRegistrationException
/*      */   {
/*  984 */     if ((paramThrowable instanceof RuntimeException)) {
/*  985 */       throw new RuntimeMBeanException((RuntimeException)paramThrowable, "RuntimeException thrown " + paramString);
/*      */     }
/*  987 */     if ((paramThrowable instanceof Error)) {
/*  988 */       throw new RuntimeErrorException((Error)paramThrowable, "Error thrown " + paramString);
/*      */     }
/*  990 */     if ((paramThrowable instanceof MBeanRegistrationException))
/*  991 */       throw ((MBeanRegistrationException)paramThrowable);
/*  992 */     if ((paramThrowable instanceof Exception)) {
/*  993 */       throw new MBeanRegistrationException((Exception)paramThrowable, "Exception thrown " + paramString);
/*      */     }
/*      */ 
/*  996 */     throw new RuntimeException(paramThrowable);
/*      */   }
/*      */ 
/*      */   private static ObjectName preRegister(DynamicMBean paramDynamicMBean, MBeanServer paramMBeanServer, ObjectName paramObjectName)
/*      */     throws InstanceAlreadyExistsException, MBeanRegistrationException
/*      */   {
/* 1003 */     ObjectName localObjectName = null;
/*      */     try
/*      */     {
/* 1006 */       if ((paramDynamicMBean instanceof MBeanRegistration))
/* 1007 */         localObjectName = ((MBeanRegistration)paramDynamicMBean).preRegister(paramMBeanServer, paramObjectName);
/*      */     } catch (Throwable localThrowable) {
/* 1009 */       throwMBeanRegistrationException(localThrowable, "in preRegister method");
/*      */     }
/*      */ 
/* 1012 */     if (localObjectName != null) return localObjectName;
/* 1013 */     return paramObjectName;
/*      */   }
/*      */ 
/*      */   private static void postRegister(ObjectName paramObjectName, DynamicMBean paramDynamicMBean, boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/* 1020 */     if ((paramBoolean2) && ((paramDynamicMBean instanceof DynamicMBean2)))
/* 1021 */       ((DynamicMBean2)paramDynamicMBean).registerFailed();
/*      */     try {
/* 1023 */       if ((paramDynamicMBean instanceof MBeanRegistration))
/* 1024 */         ((MBeanRegistration)paramDynamicMBean).postRegister(Boolean.valueOf(paramBoolean1));
/*      */     } catch (RuntimeException localRuntimeException) {
/* 1026 */       JmxProperties.MBEANSERVER_LOGGER.fine("While registering MBean [" + paramObjectName + "]: " + "Exception thrown by postRegister: " + "rethrowing <" + localRuntimeException + ">, but keeping the MBean registered");
/*      */ 
/* 1029 */       throw new RuntimeMBeanException(localRuntimeException, "RuntimeException thrown in postRegister method: rethrowing <" + localRuntimeException + ">, but keeping the MBean registered");
/*      */     }
/*      */     catch (Error localError)
/*      */     {
/* 1033 */       JmxProperties.MBEANSERVER_LOGGER.fine("While registering MBean [" + paramObjectName + "]: " + "Error thrown by postRegister: " + "rethrowing <" + localError + ">, but keeping the MBean registered");
/*      */ 
/* 1036 */       throw new RuntimeErrorException(localError, "Error thrown in postRegister method: rethrowing <" + localError + ">, but keeping the MBean registered");
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void preDeregisterInvoke(MBeanRegistration paramMBeanRegistration)
/*      */     throws MBeanRegistrationException
/*      */   {
/*      */     try
/*      */     {
/* 1045 */       paramMBeanRegistration.preDeregister();
/*      */     } catch (Throwable localThrowable) {
/* 1047 */       throwMBeanRegistrationException(localThrowable, "in preDeregister method");
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void postDeregisterInvoke(ObjectName paramObjectName, MBeanRegistration paramMBeanRegistration)
/*      */   {
/*      */     try {
/* 1054 */       paramMBeanRegistration.postDeregister();
/*      */     } catch (RuntimeException localRuntimeException) {
/* 1056 */       JmxProperties.MBEANSERVER_LOGGER.fine("While unregistering MBean [" + paramObjectName + "]: " + "Exception thrown by postDeregister: " + "rethrowing <" + localRuntimeException + ">, although the MBean is succesfully " + "unregistered");
/*      */ 
/* 1060 */       throw new RuntimeMBeanException(localRuntimeException, "RuntimeException thrown in postDeregister method: rethrowing <" + localRuntimeException + ">, although the MBean is sucessfully unregistered");
/*      */     }
/*      */     catch (Error localError)
/*      */     {
/* 1065 */       JmxProperties.MBEANSERVER_LOGGER.fine("While unregistering MBean [" + paramObjectName + "]: " + "Error thrown by postDeregister: " + "rethrowing <" + localError + ">, although the MBean is succesfully " + "unregistered");
/*      */ 
/* 1069 */       throw new RuntimeErrorException(localError, "Error thrown in postDeregister method: rethrowing <" + localError + ">, although the MBean is sucessfully unregistered");
/*      */     }
/*      */   }
/*      */ 
/*      */   private DynamicMBean getMBean(ObjectName paramObjectName)
/*      */     throws InstanceNotFoundException
/*      */   {
/* 1083 */     if (paramObjectName == null) {
/* 1084 */       throw new RuntimeOperationsException(new IllegalArgumentException("Object name cannot be null"), "Exception occurred trying to get an MBean");
/*      */     }
/*      */ 
/* 1088 */     DynamicMBean localDynamicMBean = this.repository.retrieve(paramObjectName);
/* 1089 */     if (localDynamicMBean == null) {
/* 1090 */       if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINER)) {
/* 1091 */         JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINER, DefaultMBeanServerInterceptor.class.getName(), "getMBean", paramObjectName + " : Found no object");
/*      */       }
/*      */ 
/* 1095 */       throw new InstanceNotFoundException(paramObjectName.toString());
/*      */     }
/* 1097 */     return localDynamicMBean;
/*      */   }
/*      */ 
/*      */   private static Object getResource(DynamicMBean paramDynamicMBean) {
/* 1101 */     if ((paramDynamicMBean instanceof DynamicMBean2)) {
/* 1102 */       return ((DynamicMBean2)paramDynamicMBean).getResource();
/*      */     }
/* 1104 */     return paramDynamicMBean;
/*      */   }
/*      */ 
/*      */   private ObjectName nonDefaultDomain(ObjectName paramObjectName) {
/* 1108 */     if ((paramObjectName == null) || (paramObjectName.getDomain().length() > 0)) {
/* 1109 */       return paramObjectName;
/*      */     }
/*      */ 
/* 1118 */     String str = this.domain + paramObjectName;
/*      */ 
/* 1120 */     return Util.newObjectName(str);
/*      */   }
/*      */ 
/*      */   public String getDefaultDomain() {
/* 1124 */     return this.domain;
/*      */   }
/*      */ 
/*      */   public void addNotificationListener(ObjectName paramObjectName, NotificationListener paramNotificationListener, NotificationFilter paramNotificationFilter, Object paramObject)
/*      */     throws InstanceNotFoundException
/*      */   {
/* 1179 */     if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINER)) {
/* 1180 */       JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINER, DefaultMBeanServerInterceptor.class.getName(), "addNotificationListener", "ObjectName = " + paramObjectName);
/*      */     }
/*      */ 
/* 1185 */     DynamicMBean localDynamicMBean = getMBean(paramObjectName);
/* 1186 */     checkMBeanPermission(localDynamicMBean, null, paramObjectName, "addNotificationListener");
/*      */ 
/* 1188 */     NotificationBroadcaster localNotificationBroadcaster = getNotificationBroadcaster(paramObjectName, localDynamicMBean, NotificationBroadcaster.class);
/*      */ 
/* 1195 */     if (paramNotificationListener == null) {
/* 1196 */       throw new RuntimeOperationsException(new IllegalArgumentException("Null listener"), "Null listener");
/*      */     }
/*      */ 
/* 1200 */     NotificationListener localNotificationListener = getListenerWrapper(paramNotificationListener, paramObjectName, localDynamicMBean, true);
/*      */ 
/* 1202 */     localNotificationBroadcaster.addNotificationListener(localNotificationListener, paramNotificationFilter, paramObject);
/*      */   }
/*      */ 
/*      */   public void addNotificationListener(ObjectName paramObjectName1, ObjectName paramObjectName2, NotificationFilter paramNotificationFilter, Object paramObject)
/*      */     throws InstanceNotFoundException
/*      */   {
/* 1217 */     DynamicMBean localDynamicMBean = getMBean(paramObjectName2);
/* 1218 */     Object localObject = getResource(localDynamicMBean);
/* 1219 */     if (!(localObject instanceof NotificationListener)) {
/* 1220 */       throw new RuntimeOperationsException(new IllegalArgumentException(paramObjectName2.getCanonicalName()), "The MBean " + paramObjectName2.getCanonicalName() + "does not implement the NotificationListener interface");
/*      */     }
/*      */ 
/* 1229 */     if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINER)) {
/* 1230 */       JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINER, DefaultMBeanServerInterceptor.class.getName(), "addNotificationListener", "ObjectName = " + paramObjectName1 + ", Listener = " + paramObjectName2);
/*      */     }
/*      */ 
/* 1235 */     this.server.addNotificationListener(paramObjectName1, (NotificationListener)localObject, paramNotificationFilter, paramObject);
/*      */   }
/*      */ 
/*      */   public void removeNotificationListener(ObjectName paramObjectName, NotificationListener paramNotificationListener)
/*      */     throws InstanceNotFoundException, ListenerNotFoundException
/*      */   {
/* 1242 */     removeNotificationListener(paramObjectName, paramNotificationListener, null, null, true);
/*      */   }
/*      */ 
/*      */   public void removeNotificationListener(ObjectName paramObjectName, NotificationListener paramNotificationListener, NotificationFilter paramNotificationFilter, Object paramObject)
/*      */     throws InstanceNotFoundException, ListenerNotFoundException
/*      */   {
/* 1250 */     removeNotificationListener(paramObjectName, paramNotificationListener, paramNotificationFilter, paramObject, false);
/*      */   }
/*      */ 
/*      */   public void removeNotificationListener(ObjectName paramObjectName1, ObjectName paramObjectName2)
/*      */     throws InstanceNotFoundException, ListenerNotFoundException
/*      */   {
/* 1256 */     NotificationListener localNotificationListener = getListener(paramObjectName2);
/*      */ 
/* 1258 */     if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINER)) {
/* 1259 */       JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINER, DefaultMBeanServerInterceptor.class.getName(), "removeNotificationListener", "ObjectName = " + paramObjectName1 + ", Listener = " + paramObjectName2);
/*      */     }
/*      */ 
/* 1264 */     this.server.removeNotificationListener(paramObjectName1, localNotificationListener);
/*      */   }
/*      */ 
/*      */   public void removeNotificationListener(ObjectName paramObjectName1, ObjectName paramObjectName2, NotificationFilter paramNotificationFilter, Object paramObject)
/*      */     throws InstanceNotFoundException, ListenerNotFoundException
/*      */   {
/* 1273 */     NotificationListener localNotificationListener = getListener(paramObjectName2);
/*      */ 
/* 1275 */     if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINER)) {
/* 1276 */       JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINER, DefaultMBeanServerInterceptor.class.getName(), "removeNotificationListener", "ObjectName = " + paramObjectName1 + ", Listener = " + paramObjectName2);
/*      */     }
/*      */ 
/* 1281 */     this.server.removeNotificationListener(paramObjectName1, localNotificationListener, paramNotificationFilter, paramObject);
/*      */   }
/*      */ 
/*      */   private NotificationListener getListener(ObjectName paramObjectName)
/*      */     throws ListenerNotFoundException
/*      */   {
/*      */     DynamicMBean localDynamicMBean;
/*      */     try
/*      */     {
/* 1291 */       localDynamicMBean = getMBean(paramObjectName);
/*      */     } catch (InstanceNotFoundException localInstanceNotFoundException) {
/* 1293 */       throw ((ListenerNotFoundException)EnvHelp.initCause(new ListenerNotFoundException(localInstanceNotFoundException.getMessage()), localInstanceNotFoundException));
/*      */     }
/*      */ 
/* 1297 */     Object localObject = getResource(localDynamicMBean);
/* 1298 */     if (!(localObject instanceof NotificationListener)) {
/* 1299 */       IllegalArgumentException localIllegalArgumentException = new IllegalArgumentException(paramObjectName.getCanonicalName());
/*      */ 
/* 1301 */       String str = "MBean " + paramObjectName.getCanonicalName() + " does not " + "implement " + NotificationListener.class.getName();
/*      */ 
/* 1304 */       throw new RuntimeOperationsException(localIllegalArgumentException, str);
/*      */     }
/* 1306 */     return (NotificationListener)localObject;
/*      */   }
/*      */ 
/*      */   private void removeNotificationListener(ObjectName paramObjectName, NotificationListener paramNotificationListener, NotificationFilter paramNotificationFilter, Object paramObject, boolean paramBoolean)
/*      */     throws InstanceNotFoundException, ListenerNotFoundException
/*      */   {
/* 1316 */     if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINER)) {
/* 1317 */       JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINER, DefaultMBeanServerInterceptor.class.getName(), "removeNotificationListener", "ObjectName = " + paramObjectName);
/*      */     }
/*      */ 
/* 1322 */     DynamicMBean localDynamicMBean = getMBean(paramObjectName);
/* 1323 */     checkMBeanPermission(localDynamicMBean, null, paramObjectName, "removeNotificationListener");
/*      */ 
/* 1330 */     NotificationEmitter localNotificationEmitter1 = paramBoolean ? NotificationBroadcaster.class : NotificationEmitter.class;
/*      */ 
/* 1332 */     NotificationBroadcaster localNotificationBroadcaster = getNotificationBroadcaster(paramObjectName, localDynamicMBean, localNotificationEmitter1);
/*      */ 
/* 1335 */     NotificationListener localNotificationListener = getListenerWrapper(paramNotificationListener, paramObjectName, localDynamicMBean, false);
/*      */ 
/* 1338 */     if (localNotificationListener == null) {
/* 1339 */       throw new ListenerNotFoundException("Unknown listener");
/*      */     }
/* 1341 */     if (paramBoolean) {
/* 1342 */       localNotificationBroadcaster.removeNotificationListener(localNotificationListener);
/*      */     } else {
/* 1344 */       NotificationEmitter localNotificationEmitter2 = (NotificationEmitter)localNotificationBroadcaster;
/* 1345 */       localNotificationEmitter2.removeNotificationListener(localNotificationListener, paramNotificationFilter, paramObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static <T extends NotificationBroadcaster> T getNotificationBroadcaster(ObjectName paramObjectName, Object paramObject, Class<T> paramClass)
/*      */   {
/* 1354 */     if (paramClass.isInstance(paramObject))
/* 1355 */       return (NotificationBroadcaster)paramClass.cast(paramObject);
/* 1356 */     if ((paramObject instanceof DynamicMBean2))
/* 1357 */       paramObject = ((DynamicMBean2)paramObject).getResource();
/* 1358 */     if (paramClass.isInstance(paramObject))
/* 1359 */       return (NotificationBroadcaster)paramClass.cast(paramObject);
/* 1360 */     IllegalArgumentException localIllegalArgumentException = new IllegalArgumentException(paramObjectName.getCanonicalName());
/*      */ 
/* 1362 */     String str = "MBean " + paramObjectName.getCanonicalName() + " does not " + "implement " + paramClass.getName();
/*      */ 
/* 1365 */     throw new RuntimeOperationsException(localIllegalArgumentException, str);
/*      */   }
/*      */ 
/*      */   public MBeanInfo getMBeanInfo(ObjectName paramObjectName)
/*      */     throws InstanceNotFoundException, IntrospectionException, ReflectionException
/*      */   {
/* 1375 */     DynamicMBean localDynamicMBean = getMBean(paramObjectName);
/*      */     MBeanInfo localMBeanInfo;
/*      */     try
/*      */     {
/* 1378 */       localMBeanInfo = localDynamicMBean.getMBeanInfo();
/*      */     } catch (RuntimeMBeanException localRuntimeMBeanException) {
/* 1380 */       throw localRuntimeMBeanException;
/*      */     } catch (RuntimeErrorException localRuntimeErrorException) {
/* 1382 */       throw localRuntimeErrorException;
/*      */     } catch (RuntimeException localRuntimeException) {
/* 1384 */       throw new RuntimeMBeanException(localRuntimeException, "getMBeanInfo threw RuntimeException");
/*      */     }
/*      */     catch (Error localError) {
/* 1387 */       throw new RuntimeErrorException(localError, "getMBeanInfo threw Error");
/*      */     }
/* 1389 */     if (localMBeanInfo == null) {
/* 1390 */       throw new JMRuntimeException("MBean " + paramObjectName + "has no MBeanInfo");
/*      */     }
/*      */ 
/* 1393 */     checkMBeanPermission(localMBeanInfo.getClassName(), null, paramObjectName, "getMBeanInfo");
/*      */ 
/* 1395 */     return localMBeanInfo;
/*      */   }
/*      */ 
/*      */   public boolean isInstanceOf(ObjectName paramObjectName, String paramString)
/*      */     throws InstanceNotFoundException
/*      */   {
/* 1401 */     DynamicMBean localDynamicMBean = getMBean(paramObjectName);
/* 1402 */     checkMBeanPermission(localDynamicMBean, null, paramObjectName, "isInstanceOf");
/*      */     try
/*      */     {
/* 1405 */       Object localObject = getResource(localDynamicMBean);
/*      */ 
/* 1407 */       String str = (localObject instanceof DynamicMBean) ? getClassName((DynamicMBean)localObject) : localObject.getClass().getName();
/*      */ 
/* 1412 */       if (str.equals(paramString))
/* 1413 */         return true;
/* 1414 */       ClassLoader localClassLoader = localObject.getClass().getClassLoader();
/*      */ 
/* 1416 */       Class localClass1 = Class.forName(paramString, false, localClassLoader);
/* 1417 */       if (localClass1.isInstance(localObject)) {
/* 1418 */         return true;
/*      */       }
/* 1420 */       Class localClass2 = Class.forName(str, false, localClassLoader);
/* 1421 */       return localClass1.isAssignableFrom(localClass2);
/*      */     }
/*      */     catch (Exception localException) {
/* 1424 */       if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINEST)) {
/* 1425 */         JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINEST, DefaultMBeanServerInterceptor.class.getName(), "isInstanceOf", "Exception calling isInstanceOf", localException);
/*      */       }
/*      */     }
/*      */ 
/* 1429 */     return false;
/*      */   }
/*      */ 
/*      */   public ClassLoader getClassLoaderFor(ObjectName paramObjectName)
/*      */     throws InstanceNotFoundException
/*      */   {
/* 1444 */     DynamicMBean localDynamicMBean = getMBean(paramObjectName);
/* 1445 */     checkMBeanPermission(localDynamicMBean, null, paramObjectName, "getClassLoaderFor");
/* 1446 */     return getResource(localDynamicMBean).getClass().getClassLoader();
/*      */   }
/*      */ 
/*      */   public ClassLoader getClassLoader(ObjectName paramObjectName)
/*      */     throws InstanceNotFoundException
/*      */   {
/* 1459 */     if (paramObjectName == null) {
/* 1460 */       checkMBeanPermission((String)null, null, null, "getClassLoader");
/* 1461 */       return this.server.getClass().getClassLoader();
/*      */     }
/*      */ 
/* 1464 */     DynamicMBean localDynamicMBean = getMBean(paramObjectName);
/* 1465 */     checkMBeanPermission(localDynamicMBean, null, paramObjectName, "getClassLoader");
/*      */ 
/* 1467 */     Object localObject = getResource(localDynamicMBean);
/*      */ 
/* 1470 */     if (!(localObject instanceof ClassLoader)) {
/* 1471 */       throw new InstanceNotFoundException(paramObjectName.toString() + " is not a classloader");
/*      */     }
/*      */ 
/* 1474 */     return (ClassLoader)localObject;
/*      */   }
/*      */ 
/*      */   private void sendNotification(String paramString, ObjectName paramObjectName)
/*      */   {
/* 1489 */     MBeanServerNotification localMBeanServerNotification = new MBeanServerNotification(paramString, MBeanServerDelegate.DELEGATE_NAME, 0L, paramObjectName);
/*      */ 
/* 1492 */     if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINER)) {
/* 1493 */       JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINER, DefaultMBeanServerInterceptor.class.getName(), "sendNotification", paramString + " " + paramObjectName);
/*      */     }
/*      */ 
/* 1498 */     this.delegate.sendNotification(localMBeanServerNotification);
/*      */   }
/*      */ 
/*      */   private Set<ObjectName> objectNamesFromFilteredNamedObjects(Set<NamedObject> paramSet, QueryExp paramQueryExp)
/*      */   {
/* 1507 */     HashSet localHashSet = new HashSet();
/*      */     Object localObject1;
/*      */     Object localObject2;
/* 1509 */     if (paramQueryExp == null) {
/* 1510 */       for (localObject1 = paramSet.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (NamedObject)((Iterator)localObject1).next();
/* 1511 */         localHashSet.add(((NamedObject)localObject2).getName()); }
/*      */     }
/*      */     else
/*      */     {
/* 1515 */       localObject1 = QueryEval.getMBeanServer();
/* 1516 */       paramQueryExp.setMBeanServer(this.server);
/*      */       try {
/* 1518 */         for (localObject2 = paramSet.iterator(); ((Iterator)localObject2).hasNext(); ) { NamedObject localNamedObject = (NamedObject)((Iterator)localObject2).next();
/*      */           boolean bool;
/*      */           try { bool = paramQueryExp.apply(localNamedObject.getName());
/*      */           } catch (Exception localException) {
/* 1523 */             bool = false;
/*      */           }
/* 1525 */           if (bool) {
/* 1526 */             localHashSet.add(localNamedObject.getName());
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */       finally
/*      */       {
/* 1539 */         paramQueryExp.setMBeanServer((MBeanServer)localObject1);
/*      */       }
/*      */     }
/* 1542 */     return localHashSet;
/*      */   }
/*      */ 
/*      */   private Set<ObjectInstance> objectInstancesFromFilteredNamedObjects(Set<NamedObject> paramSet, QueryExp paramQueryExp)
/*      */   {
/* 1551 */     HashSet localHashSet = new HashSet();
/*      */     Object localObject1;
/*      */     Object localObject2;
/*      */     Object localObject3;
/*      */     Object localObject4;
/* 1553 */     if (paramQueryExp == null) {
/* 1554 */       for (localObject1 = paramSet.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (NamedObject)((Iterator)localObject1).next();
/* 1555 */         localObject3 = ((NamedObject)localObject2).getObject();
/* 1556 */         localObject4 = safeGetClassName((DynamicMBean)localObject3);
/* 1557 */         localHashSet.add(new ObjectInstance(((NamedObject)localObject2).getName(), (String)localObject4)); }
/*      */     }
/*      */     else
/*      */     {
/* 1561 */       localObject1 = QueryEval.getMBeanServer();
/* 1562 */       paramQueryExp.setMBeanServer(this.server);
/*      */       try {
/* 1564 */         for (localObject2 = paramSet.iterator(); ((Iterator)localObject2).hasNext(); ) { localObject3 = (NamedObject)((Iterator)localObject2).next();
/* 1565 */           localObject4 = ((NamedObject)localObject3).getObject();
/*      */           boolean bool;
/*      */           try { bool = paramQueryExp.apply(((NamedObject)localObject3).getName());
/*      */           } catch (Exception localException) {
/* 1570 */             bool = false;
/*      */           }
/* 1572 */           if (bool) {
/* 1573 */             String str = safeGetClassName((DynamicMBean)localObject4);
/* 1574 */             localHashSet.add(new ObjectInstance(((NamedObject)localObject3).getName(), str));
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */       finally
/*      */       {
/* 1587 */         paramQueryExp.setMBeanServer((MBeanServer)localObject1);
/*      */       }
/*      */     }
/* 1590 */     return localHashSet;
/*      */   }
/*      */ 
/*      */   private static String safeGetClassName(DynamicMBean paramDynamicMBean) {
/*      */     try {
/* 1595 */       return getClassName(paramDynamicMBean);
/*      */     } catch (Exception localException) {
/* 1597 */       if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINEST)) {
/* 1598 */         JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINEST, DefaultMBeanServerInterceptor.class.getName(), "safeGetClassName", "Exception getting MBean class name", localException);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1603 */     return null;
/*      */   }
/*      */ 
/*      */   private Set<ObjectInstance> filterListOfObjectInstances(Set<ObjectInstance> paramSet, QueryExp paramQueryExp)
/*      */   {
/* 1615 */     if (paramQueryExp == null) {
/* 1616 */       return paramSet;
/*      */     }
/* 1618 */     HashSet localHashSet = new HashSet();
/*      */ 
/* 1621 */     for (ObjectInstance localObjectInstance : paramSet) {
/* 1622 */       boolean bool = false;
/* 1623 */       MBeanServer localMBeanServer = QueryEval.getMBeanServer();
/* 1624 */       paramQueryExp.setMBeanServer(this.server);
/*      */       try {
/* 1626 */         bool = paramQueryExp.apply(localObjectInstance.getObjectName());
/*      */       } catch (Exception localException) {
/* 1628 */         bool = false;
/*      */       }
/*      */       finally
/*      */       {
/* 1639 */         paramQueryExp.setMBeanServer(localMBeanServer);
/*      */       }
/* 1641 */       if (bool) {
/* 1642 */         localHashSet.add(localObjectInstance);
/*      */       }
/*      */     }
/* 1645 */     return localHashSet;
/*      */   }
/*      */ 
/*      */   private NotificationListener getListenerWrapper(NotificationListener paramNotificationListener, ObjectName paramObjectName, DynamicMBean paramDynamicMBean, boolean paramBoolean)
/*      */   {
/* 1669 */     Object localObject1 = getResource(paramDynamicMBean);
/* 1670 */     ListenerWrapper localListenerWrapper = new ListenerWrapper(paramNotificationListener, paramObjectName, localObject1);
/* 1671 */     synchronized (this.listenerWrappers) {
/* 1672 */       WeakReference localWeakReference = (WeakReference)this.listenerWrappers.get(localListenerWrapper);
/* 1673 */       if (localWeakReference != null) {
/* 1674 */         NotificationListener localNotificationListener = (NotificationListener)localWeakReference.get();
/* 1675 */         if (localNotificationListener != null)
/* 1676 */           return localNotificationListener;
/*      */       }
/* 1678 */       if (paramBoolean) {
/* 1679 */         localWeakReference = new WeakReference(localListenerWrapper);
/* 1680 */         this.listenerWrappers.put(localListenerWrapper, localWeakReference);
/* 1681 */         return localListenerWrapper;
/*      */       }
/* 1683 */       return null;
/*      */     }
/*      */   }
/*      */ 
/*      */   public Object instantiate(String paramString) throws ReflectionException, MBeanException
/*      */   {
/* 1689 */     throw new UnsupportedOperationException("Not supported yet.");
/*      */   }
/*      */ 
/*      */   public Object instantiate(String paramString, ObjectName paramObjectName)
/*      */     throws ReflectionException, MBeanException, InstanceNotFoundException
/*      */   {
/* 1695 */     throw new UnsupportedOperationException("Not supported yet.");
/*      */   }
/*      */ 
/*      */   public Object instantiate(String paramString, Object[] paramArrayOfObject, String[] paramArrayOfString) throws ReflectionException, MBeanException
/*      */   {
/* 1700 */     throw new UnsupportedOperationException("Not supported yet.");
/*      */   }
/*      */ 
/*      */   public Object instantiate(String paramString, ObjectName paramObjectName, Object[] paramArrayOfObject, String[] paramArrayOfString)
/*      */     throws ReflectionException, MBeanException, InstanceNotFoundException
/*      */   {
/* 1707 */     throw new UnsupportedOperationException("Not supported yet.");
/*      */   }
/*      */ 
/*      */   public ObjectInputStream deserialize(ObjectName paramObjectName, byte[] paramArrayOfByte) throws InstanceNotFoundException, OperationsException
/*      */   {
/* 1712 */     throw new UnsupportedOperationException("Not supported yet.");
/*      */   }
/*      */ 
/*      */   public ObjectInputStream deserialize(String paramString, byte[] paramArrayOfByte) throws OperationsException, ReflectionException
/*      */   {
/* 1717 */     throw new UnsupportedOperationException("Not supported yet.");
/*      */   }
/*      */ 
/*      */   public ObjectInputStream deserialize(String paramString, ObjectName paramObjectName, byte[] paramArrayOfByte)
/*      */     throws InstanceNotFoundException, OperationsException, ReflectionException
/*      */   {
/* 1723 */     throw new UnsupportedOperationException("Not supported yet.");
/*      */   }
/*      */ 
/*      */   public ClassLoaderRepository getClassLoaderRepository() {
/* 1727 */     throw new UnsupportedOperationException("Not supported yet.");
/*      */   }
/*      */ 
/*      */   private static String getClassName(DynamicMBean paramDynamicMBean)
/*      */   {
/* 1801 */     if ((paramDynamicMBean instanceof DynamicMBean2)) {
/* 1802 */       return ((DynamicMBean2)paramDynamicMBean).getClassName();
/*      */     }
/* 1804 */     return paramDynamicMBean.getMBeanInfo().getClassName();
/*      */   }
/*      */ 
/*      */   private static void checkMBeanPermission(DynamicMBean paramDynamicMBean, String paramString1, ObjectName paramObjectName, String paramString2)
/*      */   {
/* 1811 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 1812 */     if (localSecurityManager != null)
/* 1813 */       checkMBeanPermission(safeGetClassName(paramDynamicMBean), paramString1, paramObjectName, paramString2);
/*      */   }
/*      */ 
/*      */   private static void checkMBeanPermission(String paramString1, String paramString2, ObjectName paramObjectName, String paramString3)
/*      */   {
/* 1824 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 1825 */     if (localSecurityManager != null) {
/* 1826 */       MBeanPermission localMBeanPermission = new MBeanPermission(paramString1, paramString2, paramObjectName, paramString3);
/*      */ 
/* 1830 */       localSecurityManager.checkPermission(localMBeanPermission);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void checkMBeanTrustPermission(Class<?> paramClass) throws SecurityException
/*      */   {
/* 1836 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 1837 */     if (localSecurityManager != null) {
/* 1838 */       MBeanTrustPermission localMBeanTrustPermission = new MBeanTrustPermission("register");
/* 1839 */       PrivilegedAction local1 = new PrivilegedAction()
/*      */       {
/*      */         public ProtectionDomain run() {
/* 1842 */           return this.val$theClass.getProtectionDomain();
/*      */         }
/*      */       };
/* 1845 */       ProtectionDomain localProtectionDomain = (ProtectionDomain)AccessController.doPrivileged(local1);
/* 1846 */       AccessControlContext localAccessControlContext = new AccessControlContext(new ProtectionDomain[] { localProtectionDomain });
/*      */ 
/* 1848 */       localSecurityManager.checkPermission(localMBeanTrustPermission, localAccessControlContext);
/*      */     }
/*      */   }
/*      */ 
/*      */   private ResourceContext registerWithRepository(Object paramObject, DynamicMBean paramDynamicMBean, ObjectName paramObjectName)
/*      */     throws InstanceAlreadyExistsException, MBeanRegistrationException
/*      */   {
/* 1894 */     ResourceContext localResourceContext = makeResourceContextFor(paramObject, paramObjectName);
/*      */ 
/* 1898 */     this.repository.addMBean(paramDynamicMBean, paramObjectName, localResourceContext);
/*      */ 
/* 1904 */     if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINER)) {
/* 1905 */       JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINER, DefaultMBeanServerInterceptor.class.getName(), "addObject", "Send create notification of object " + paramObjectName.getCanonicalName());
/*      */     }
/*      */ 
/* 1911 */     sendNotification("JMX.mbean.registered", paramObjectName);
/*      */ 
/* 1915 */     return localResourceContext;
/*      */   }
/*      */ 
/*      */   private ResourceContext unregisterFromRepository(Object paramObject, DynamicMBean paramDynamicMBean, ObjectName paramObjectName)
/*      */     throws InstanceNotFoundException
/*      */   {
/* 1936 */     ResourceContext localResourceContext = makeResourceContextFor(paramObject, paramObjectName);
/*      */ 
/* 1940 */     this.repository.remove(paramObjectName, localResourceContext);
/*      */ 
/* 1945 */     if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINER)) {
/* 1946 */       JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINER, DefaultMBeanServerInterceptor.class.getName(), "unregisterMBean", "Send delete notification of object " + paramObjectName.getCanonicalName());
/*      */     }
/*      */ 
/* 1952 */     sendNotification("JMX.mbean.unregistered", paramObjectName);
/*      */ 
/* 1954 */     return localResourceContext;
/*      */   }
/*      */ 
/*      */   private void addClassLoader(ClassLoader paramClassLoader, ObjectName paramObjectName)
/*      */   {
/* 1976 */     ModifiableClassLoaderRepository localModifiableClassLoaderRepository = getInstantiatorCLR();
/* 1977 */     if (localModifiableClassLoaderRepository == null) {
/* 1978 */       IllegalArgumentException localIllegalArgumentException = new IllegalArgumentException("Dynamic addition of class loaders is not supported");
/*      */ 
/* 1982 */       throw new RuntimeOperationsException(localIllegalArgumentException, "Exception occurred trying to register the MBean as a class loader");
/*      */     }
/*      */ 
/* 1986 */     localModifiableClassLoaderRepository.addClassLoader(paramObjectName, paramClassLoader);
/*      */   }
/*      */ 
/*      */   private void removeClassLoader(ClassLoader paramClassLoader, ObjectName paramObjectName)
/*      */   {
/* 2001 */     if (paramClassLoader != this.server.getClass().getClassLoader()) {
/* 2002 */       ModifiableClassLoaderRepository localModifiableClassLoaderRepository = getInstantiatorCLR();
/* 2003 */       if (localModifiableClassLoaderRepository != null)
/* 2004 */         localModifiableClassLoaderRepository.removeClassLoader(paramObjectName);
/*      */     }
/*      */   }
/*      */ 
/*      */   private ResourceContext createClassLoaderContext(final ClassLoader paramClassLoader, final ObjectName paramObjectName)
/*      */   {
/* 2027 */     return new ResourceContext()
/*      */     {
/*      */       public void registering() {
/* 2030 */         DefaultMBeanServerInterceptor.this.addClassLoader(paramClassLoader, paramObjectName);
/*      */       }
/*      */ 
/*      */       public void unregistered() {
/* 2034 */         DefaultMBeanServerInterceptor.this.removeClassLoader(paramClassLoader, paramObjectName);
/*      */       }
/*      */ 
/*      */       public void done()
/*      */       {
/*      */       }
/*      */     };
/*      */   }
/*      */ 
/*      */   private ResourceContext makeResourceContextFor(Object paramObject, ObjectName paramObjectName)
/*      */   {
/* 2054 */     if ((paramObject instanceof ClassLoader)) {
/* 2055 */       return createClassLoaderContext((ClassLoader)paramObject, paramObjectName);
/*      */     }
/*      */ 
/* 2058 */     return ResourceContext.NONE;
/*      */   }
/*      */ 
/*      */   private ModifiableClassLoaderRepository getInstantiatorCLR() {
/* 2062 */     return (ModifiableClassLoaderRepository)AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public ModifiableClassLoaderRepository run() {
/* 2065 */         return DefaultMBeanServerInterceptor.this.instantiator != null ? DefaultMBeanServerInterceptor.this.instantiator.getClassLoaderRepository() : null;
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   private static class ListenerWrapper
/*      */     implements NotificationListener
/*      */   {
/*      */     private NotificationListener listener;
/*      */     private ObjectName name;
/*      */     private Object mbean;
/*      */ 
/*      */     ListenerWrapper(NotificationListener paramNotificationListener, ObjectName paramObjectName, Object paramObject)
/*      */     {
/* 1733 */       this.listener = paramNotificationListener;
/* 1734 */       this.name = paramObjectName;
/* 1735 */       this.mbean = paramObject;
/*      */     }
/*      */ 
/*      */     public void handleNotification(Notification paramNotification, Object paramObject)
/*      */     {
/* 1740 */       if ((paramNotification != null) && 
/* 1741 */         (paramNotification.getSource() == this.mbean)) {
/* 1742 */         paramNotification.setSource(this.name);
/*      */       }
/*      */ 
/* 1754 */       this.listener.handleNotification(paramNotification, paramObject);
/*      */     }
/*      */ 
/*      */     public boolean equals(Object paramObject)
/*      */     {
/* 1759 */       if (!(paramObject instanceof ListenerWrapper))
/* 1760 */         return false;
/* 1761 */       ListenerWrapper localListenerWrapper = (ListenerWrapper)paramObject;
/* 1762 */       return (localListenerWrapper.listener == this.listener) && (localListenerWrapper.mbean == this.mbean) && (localListenerWrapper.name.equals(this.name));
/*      */     }
/*      */ 
/*      */     public int hashCode()
/*      */     {
/* 1776 */       return System.identityHashCode(this.listener) ^ System.identityHashCode(this.mbean);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static abstract interface ResourceContext extends Repository.RegistrationContext
/*      */   {
/* 1869 */     public static final ResourceContext NONE = new ResourceContext() { public void done() {  } 
/*      */       public void registering() {  } 
/* 1869 */       public void unregistered() {  }  } ;
/*      */ 
/*      */     public abstract void done();
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.interceptor.DefaultMBeanServerInterceptor
 * JD-Core Version:    0.6.2
 */