/*     */ package com.sun.jmx.remote.security;
/*     */ 
/*     */ import com.sun.jmx.mbeanserver.GetPropertyAction;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.security.AccessController;
/*     */ import java.util.Set;
/*     */ import javax.management.Attribute;
/*     */ import javax.management.AttributeList;
/*     */ import javax.management.AttributeNotFoundException;
/*     */ import javax.management.InstanceAlreadyExistsException;
/*     */ import javax.management.InstanceNotFoundException;
/*     */ import javax.management.IntrospectionException;
/*     */ import javax.management.InvalidAttributeValueException;
/*     */ import javax.management.ListenerNotFoundException;
/*     */ import javax.management.MBeanException;
/*     */ import javax.management.MBeanInfo;
/*     */ import javax.management.MBeanRegistrationException;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.NotCompliantMBeanException;
/*     */ import javax.management.NotificationFilter;
/*     */ import javax.management.NotificationListener;
/*     */ import javax.management.ObjectInstance;
/*     */ import javax.management.ObjectName;
/*     */ import javax.management.OperationsException;
/*     */ import javax.management.QueryExp;
/*     */ import javax.management.ReflectionException;
/*     */ import javax.management.loading.ClassLoaderRepository;
/*     */ import javax.management.remote.MBeanServerForwarder;
/*     */ 
/*     */ public abstract class MBeanServerAccessController
/*     */   implements MBeanServerForwarder
/*     */ {
/*     */   private MBeanServer mbs;
/*     */ 
/*     */   public MBeanServer getMBeanServer()
/*     */   {
/*  90 */     return this.mbs;
/*     */   }
/*     */ 
/*     */   public void setMBeanServer(MBeanServer paramMBeanServer) {
/*  94 */     if (paramMBeanServer == null)
/*  95 */       throw new IllegalArgumentException("Null MBeanServer");
/*  96 */     if (this.mbs != null) {
/*  97 */       throw new IllegalArgumentException("MBeanServer object already initialized");
/*     */     }
/*  99 */     this.mbs = paramMBeanServer;
/*     */   }
/*     */ 
/*     */   protected abstract void checkRead();
/*     */ 
/*     */   protected abstract void checkWrite();
/*     */ 
/*     */   protected void checkCreate(String paramString)
/*     */   {
/* 119 */     checkWrite();
/*     */   }
/*     */ 
/*     */   protected void checkUnregister(ObjectName paramObjectName)
/*     */   {
/* 127 */     checkWrite();
/*     */   }
/*     */ 
/*     */   public void addNotificationListener(ObjectName paramObjectName, NotificationListener paramNotificationListener, NotificationFilter paramNotificationFilter, Object paramObject)
/*     */     throws InstanceNotFoundException
/*     */   {
/* 147 */     checkRead();
/* 148 */     getMBeanServer().addNotificationListener(paramObjectName, paramNotificationListener, paramNotificationFilter, paramObject);
/*     */   }
/*     */ 
/*     */   public void addNotificationListener(ObjectName paramObjectName1, ObjectName paramObjectName2, NotificationFilter paramNotificationFilter, Object paramObject)
/*     */     throws InstanceNotFoundException
/*     */   {
/* 161 */     checkRead();
/* 162 */     getMBeanServer().addNotificationListener(paramObjectName1, paramObjectName2, paramNotificationFilter, paramObject);
/*     */   }
/*     */ 
/*     */   public ObjectInstance createMBean(String paramString, ObjectName paramObjectName)
/*     */     throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException
/*     */   {
/* 177 */     checkCreate(paramString);
/* 178 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 179 */     if (localSecurityManager == null) {
/* 180 */       Object localObject = getMBeanServer().instantiate(paramString);
/* 181 */       checkClassLoader(localObject);
/* 182 */       return getMBeanServer().registerMBean(localObject, paramObjectName);
/*     */     }
/* 184 */     return getMBeanServer().createMBean(paramString, paramObjectName);
/*     */   }
/*     */ 
/*     */   public ObjectInstance createMBean(String paramString, ObjectName paramObjectName, Object[] paramArrayOfObject, String[] paramArrayOfString)
/*     */     throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException
/*     */   {
/* 200 */     checkCreate(paramString);
/* 201 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 202 */     if (localSecurityManager == null) {
/* 203 */       Object localObject = getMBeanServer().instantiate(paramString, paramArrayOfObject, paramArrayOfString);
/*     */ 
/* 206 */       checkClassLoader(localObject);
/* 207 */       return getMBeanServer().registerMBean(localObject, paramObjectName);
/*     */     }
/* 209 */     return getMBeanServer().createMBean(paramString, paramObjectName, paramArrayOfObject, paramArrayOfString);
/*     */   }
/*     */ 
/*     */   public ObjectInstance createMBean(String paramString, ObjectName paramObjectName1, ObjectName paramObjectName2)
/*     */     throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException
/*     */   {
/* 228 */     checkCreate(paramString);
/* 229 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 230 */     if (localSecurityManager == null) {
/* 231 */       Object localObject = getMBeanServer().instantiate(paramString, paramObjectName2);
/*     */ 
/* 233 */       checkClassLoader(localObject);
/* 234 */       return getMBeanServer().registerMBean(localObject, paramObjectName1);
/*     */     }
/* 236 */     return getMBeanServer().createMBean(paramString, paramObjectName1, paramObjectName2);
/*     */   }
/*     */ 
/*     */   public ObjectInstance createMBean(String paramString, ObjectName paramObjectName1, ObjectName paramObjectName2, Object[] paramArrayOfObject, String[] paramArrayOfString)
/*     */     throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException
/*     */   {
/* 256 */     checkCreate(paramString);
/* 257 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 258 */     if (localSecurityManager == null) {
/* 259 */       Object localObject = getMBeanServer().instantiate(paramString, paramObjectName2, paramArrayOfObject, paramArrayOfString);
/*     */ 
/* 263 */       checkClassLoader(localObject);
/* 264 */       return getMBeanServer().registerMBean(localObject, paramObjectName1);
/*     */     }
/* 266 */     return getMBeanServer().createMBean(paramString, paramObjectName1, paramObjectName2, paramArrayOfObject, paramArrayOfString);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public ObjectInputStream deserialize(ObjectName paramObjectName, byte[] paramArrayOfByte)
/*     */     throws InstanceNotFoundException, OperationsException
/*     */   {
/* 278 */     checkRead();
/* 279 */     return getMBeanServer().deserialize(paramObjectName, paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public ObjectInputStream deserialize(String paramString, byte[] paramArrayOfByte)
/*     */     throws OperationsException, ReflectionException
/*     */   {
/* 289 */     checkRead();
/* 290 */     return getMBeanServer().deserialize(paramString, paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public ObjectInputStream deserialize(String paramString, ObjectName paramObjectName, byte[] paramArrayOfByte)
/*     */     throws InstanceNotFoundException, OperationsException, ReflectionException
/*     */   {
/* 305 */     checkRead();
/* 306 */     return getMBeanServer().deserialize(paramString, paramObjectName, paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   public Object getAttribute(ObjectName paramObjectName, String paramString)
/*     */     throws MBeanException, AttributeNotFoundException, InstanceNotFoundException, ReflectionException
/*     */   {
/* 319 */     checkRead();
/* 320 */     return getMBeanServer().getAttribute(paramObjectName, paramString);
/*     */   }
/*     */ 
/*     */   public AttributeList getAttributes(ObjectName paramObjectName, String[] paramArrayOfString)
/*     */     throws InstanceNotFoundException, ReflectionException
/*     */   {
/* 329 */     checkRead();
/* 330 */     return getMBeanServer().getAttributes(paramObjectName, paramArrayOfString);
/*     */   }
/*     */ 
/*     */   public ClassLoader getClassLoader(ObjectName paramObjectName)
/*     */     throws InstanceNotFoundException
/*     */   {
/* 339 */     checkRead();
/* 340 */     return getMBeanServer().getClassLoader(paramObjectName);
/*     */   }
/*     */ 
/*     */   public ClassLoader getClassLoaderFor(ObjectName paramObjectName)
/*     */     throws InstanceNotFoundException
/*     */   {
/* 349 */     checkRead();
/* 350 */     return getMBeanServer().getClassLoaderFor(paramObjectName);
/*     */   }
/*     */ 
/*     */   public ClassLoaderRepository getClassLoaderRepository()
/*     */   {
/* 358 */     checkRead();
/* 359 */     return getMBeanServer().getClassLoaderRepository();
/*     */   }
/*     */ 
/*     */   public String getDefaultDomain()
/*     */   {
/* 367 */     checkRead();
/* 368 */     return getMBeanServer().getDefaultDomain();
/*     */   }
/*     */ 
/*     */   public String[] getDomains()
/*     */   {
/* 376 */     checkRead();
/* 377 */     return getMBeanServer().getDomains();
/*     */   }
/*     */ 
/*     */   public Integer getMBeanCount()
/*     */   {
/* 385 */     checkRead();
/* 386 */     return getMBeanServer().getMBeanCount();
/*     */   }
/*     */ 
/*     */   public MBeanInfo getMBeanInfo(ObjectName paramObjectName)
/*     */     throws InstanceNotFoundException, IntrospectionException, ReflectionException
/*     */   {
/* 398 */     checkRead();
/* 399 */     return getMBeanServer().getMBeanInfo(paramObjectName);
/*     */   }
/*     */ 
/*     */   public ObjectInstance getObjectInstance(ObjectName paramObjectName)
/*     */     throws InstanceNotFoundException
/*     */   {
/* 408 */     checkRead();
/* 409 */     return getMBeanServer().getObjectInstance(paramObjectName);
/*     */   }
/*     */ 
/*     */   public Object instantiate(String paramString)
/*     */     throws ReflectionException, MBeanException
/*     */   {
/* 418 */     checkCreate(paramString);
/* 419 */     return getMBeanServer().instantiate(paramString);
/*     */   }
/*     */ 
/*     */   public Object instantiate(String paramString, Object[] paramArrayOfObject, String[] paramArrayOfString)
/*     */     throws ReflectionException, MBeanException
/*     */   {
/* 430 */     checkCreate(paramString);
/* 431 */     return getMBeanServer().instantiate(paramString, paramArrayOfObject, paramArrayOfString);
/*     */   }
/*     */ 
/*     */   public Object instantiate(String paramString, ObjectName paramObjectName)
/*     */     throws ReflectionException, MBeanException, InstanceNotFoundException
/*     */   {
/* 440 */     checkCreate(paramString);
/* 441 */     return getMBeanServer().instantiate(paramString, paramObjectName);
/*     */   }
/*     */ 
/*     */   public Object instantiate(String paramString, ObjectName paramObjectName, Object[] paramArrayOfObject, String[] paramArrayOfString)
/*     */     throws ReflectionException, MBeanException, InstanceNotFoundException
/*     */   {
/* 451 */     checkCreate(paramString);
/* 452 */     return getMBeanServer().instantiate(paramString, paramObjectName, paramArrayOfObject, paramArrayOfString);
/*     */   }
/*     */ 
/*     */   public Object invoke(ObjectName paramObjectName, String paramString, Object[] paramArrayOfObject, String[] paramArrayOfString)
/*     */     throws InstanceNotFoundException, MBeanException, ReflectionException
/*     */   {
/* 466 */     checkWrite();
/* 467 */     checkMLetMethods(paramObjectName, paramString);
/* 468 */     return getMBeanServer().invoke(paramObjectName, paramString, paramArrayOfObject, paramArrayOfString);
/*     */   }
/*     */ 
/*     */   public boolean isInstanceOf(ObjectName paramObjectName, String paramString)
/*     */     throws InstanceNotFoundException
/*     */   {
/* 477 */     checkRead();
/* 478 */     return getMBeanServer().isInstanceOf(paramObjectName, paramString);
/*     */   }
/*     */ 
/*     */   public boolean isRegistered(ObjectName paramObjectName)
/*     */   {
/* 486 */     checkRead();
/* 487 */     return getMBeanServer().isRegistered(paramObjectName);
/*     */   }
/*     */ 
/*     */   public Set<ObjectInstance> queryMBeans(ObjectName paramObjectName, QueryExp paramQueryExp)
/*     */   {
/* 495 */     checkRead();
/* 496 */     return getMBeanServer().queryMBeans(paramObjectName, paramQueryExp);
/*     */   }
/*     */ 
/*     */   public Set<ObjectName> queryNames(ObjectName paramObjectName, QueryExp paramQueryExp)
/*     */   {
/* 504 */     checkRead();
/* 505 */     return getMBeanServer().queryNames(paramObjectName, paramQueryExp);
/*     */   }
/*     */ 
/*     */   public ObjectInstance registerMBean(Object paramObject, ObjectName paramObjectName)
/*     */     throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException
/*     */   {
/* 517 */     checkWrite();
/* 518 */     return getMBeanServer().registerMBean(paramObject, paramObjectName);
/*     */   }
/*     */ 
/*     */   public void removeNotificationListener(ObjectName paramObjectName, NotificationListener paramNotificationListener)
/*     */     throws InstanceNotFoundException, ListenerNotFoundException
/*     */   {
/* 528 */     checkRead();
/* 529 */     getMBeanServer().removeNotificationListener(paramObjectName, paramNotificationListener);
/*     */   }
/*     */ 
/*     */   public void removeNotificationListener(ObjectName paramObjectName, NotificationListener paramNotificationListener, NotificationFilter paramNotificationFilter, Object paramObject)
/*     */     throws InstanceNotFoundException, ListenerNotFoundException
/*     */   {
/* 541 */     checkRead();
/* 542 */     getMBeanServer().removeNotificationListener(paramObjectName, paramNotificationListener, paramNotificationFilter, paramObject);
/*     */   }
/*     */ 
/*     */   public void removeNotificationListener(ObjectName paramObjectName1, ObjectName paramObjectName2)
/*     */     throws InstanceNotFoundException, ListenerNotFoundException
/*     */   {
/* 553 */     checkRead();
/* 554 */     getMBeanServer().removeNotificationListener(paramObjectName1, paramObjectName2);
/*     */   }
/*     */ 
/*     */   public void removeNotificationListener(ObjectName paramObjectName1, ObjectName paramObjectName2, NotificationFilter paramNotificationFilter, Object paramObject)
/*     */     throws InstanceNotFoundException, ListenerNotFoundException
/*     */   {
/* 566 */     checkRead();
/* 567 */     getMBeanServer().removeNotificationListener(paramObjectName1, paramObjectName2, paramNotificationFilter, paramObject);
/*     */   }
/*     */ 
/*     */   public void setAttribute(ObjectName paramObjectName, Attribute paramAttribute)
/*     */     throws InstanceNotFoundException, AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException
/*     */   {
/* 582 */     checkWrite();
/* 583 */     getMBeanServer().setAttribute(paramObjectName, paramAttribute);
/*     */   }
/*     */ 
/*     */   public AttributeList setAttributes(ObjectName paramObjectName, AttributeList paramAttributeList)
/*     */     throws InstanceNotFoundException, ReflectionException
/*     */   {
/* 593 */     checkWrite();
/* 594 */     return getMBeanServer().setAttributes(paramObjectName, paramAttributeList);
/*     */   }
/*     */ 
/*     */   public void unregisterMBean(ObjectName paramObjectName)
/*     */     throws InstanceNotFoundException, MBeanRegistrationException
/*     */   {
/* 603 */     checkUnregister(paramObjectName);
/* 604 */     getMBeanServer().unregisterMBean(paramObjectName);
/*     */   }
/*     */ 
/*     */   private void checkClassLoader(Object paramObject)
/*     */   {
/* 612 */     if ((paramObject instanceof ClassLoader))
/* 613 */       throw new SecurityException("Access denied! Creating an MBean that is a ClassLoader is forbidden unless a security manager is installed.");
/*     */   }
/*     */ 
/*     */   private void checkMLetMethods(ObjectName paramObjectName, String paramString)
/*     */     throws InstanceNotFoundException
/*     */   {
/* 622 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 623 */     if (localSecurityManager != null) {
/* 624 */       return;
/*     */     }
/*     */ 
/* 627 */     if ((!paramString.equals("addURL")) && (!paramString.equals("getMBeansFromURL")))
/*     */     {
/* 629 */       return;
/*     */     }
/*     */ 
/* 632 */     if (!getMBeanServer().isInstanceOf(paramObjectName, "javax.management.loading.MLet"))
/*     */     {
/* 634 */       return;
/*     */     }
/*     */ 
/* 637 */     if (paramString.equals("addURL")) {
/* 638 */       throw new SecurityException("Access denied! MLet method addURL cannot be invoked unless a security manager is installed.");
/*     */     }
/*     */ 
/* 647 */     GetPropertyAction localGetPropertyAction = new GetPropertyAction("jmx.remote.x.mlet.allow.getMBeansFromURL");
/* 648 */     String str = (String)AccessController.doPrivileged(localGetPropertyAction);
/* 649 */     boolean bool = "true".equalsIgnoreCase(str);
/* 650 */     if (!bool)
/* 651 */       throw new SecurityException("Access denied! MLet method getMBeansFromURL cannot be invoked unless a security manager is installed or the system property -Djmx.remote.x.mlet.allow.getMBeansFromURL=true is specified.");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.remote.security.MBeanServerAccessController
 * JD-Core Version:    0.6.2
 */