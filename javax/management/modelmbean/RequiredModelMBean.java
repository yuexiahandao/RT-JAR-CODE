/*      */ package javax.management.modelmbean;
/*      */ 
/*      */ import com.sun.jmx.defaults.JmxProperties;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Method;
/*      */ import java.security.AccessControlContext;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.Date;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.Vector;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import javax.management.Attribute;
/*      */ import javax.management.AttributeChangeNotification;
/*      */ import javax.management.AttributeChangeNotificationFilter;
/*      */ import javax.management.AttributeList;
/*      */ import javax.management.AttributeNotFoundException;
/*      */ import javax.management.Descriptor;
/*      */ import javax.management.InstanceNotFoundException;
/*      */ import javax.management.InvalidAttributeValueException;
/*      */ import javax.management.ListenerNotFoundException;
/*      */ import javax.management.MBeanAttributeInfo;
/*      */ import javax.management.MBeanConstructorInfo;
/*      */ import javax.management.MBeanException;
/*      */ import javax.management.MBeanInfo;
/*      */ import javax.management.MBeanNotificationInfo;
/*      */ import javax.management.MBeanOperationInfo;
/*      */ import javax.management.MBeanRegistration;
/*      */ import javax.management.MBeanServer;
/*      */ import javax.management.MBeanServerFactory;
/*      */ import javax.management.Notification;
/*      */ import javax.management.NotificationBroadcasterSupport;
/*      */ import javax.management.NotificationEmitter;
/*      */ import javax.management.NotificationFilter;
/*      */ import javax.management.NotificationListener;
/*      */ import javax.management.ObjectName;
/*      */ import javax.management.ReflectionException;
/*      */ import javax.management.RuntimeErrorException;
/*      */ import javax.management.RuntimeOperationsException;
/*      */ import javax.management.ServiceNotFoundException;
/*      */ import javax.management.loading.ClassLoaderRepository;
/*      */ import sun.misc.JavaSecurityAccess;
/*      */ import sun.misc.SharedSecrets;
/*      */ import sun.reflect.misc.MethodUtil;
/*      */ import sun.reflect.misc.ReflectUtil;
/*      */ 
/*      */ public class RequiredModelMBean
/*      */   implements ModelMBean, MBeanRegistration, NotificationEmitter
/*      */ {
/*      */   ModelMBeanInfo modelMBeanInfo;
/*  131 */   private NotificationBroadcasterSupport generalBroadcaster = null;
/*      */ 
/*  134 */   private NotificationBroadcasterSupport attributeBroadcaster = null;
/*      */ 
/*  138 */   private Object managedResource = null;
/*      */ 
/*  142 */   private boolean registered = false;
/*  143 */   private transient MBeanServer server = null;
/*      */ 
/*  145 */   private static final JavaSecurityAccess javaSecurityAccess = SharedSecrets.getJavaSecurityAccess();
/*  146 */   private final AccessControlContext acc = AccessController.getContext();
/*      */ 
/* 1167 */   private static final Class<?>[] primitiveClasses = { Integer.TYPE, Long.TYPE, Boolean.TYPE, Double.TYPE, Float.TYPE, Short.TYPE, Byte.TYPE, Character.TYPE };
/*      */ 
/* 1171 */   private static final Map<String, Class<?>> primitiveClassMap = new HashMap();
/*      */   private static Set<String> rmmbMethodNames;
/* 2981 */   private static final String[] primitiveTypes = { Boolean.TYPE.getName(), Byte.TYPE.getName(), Character.TYPE.getName(), Short.TYPE.getName(), Integer.TYPE.getName(), Long.TYPE.getName(), Float.TYPE.getName(), Double.TYPE.getName(), Void.TYPE.getName() };
/*      */ 
/* 2992 */   private static final String[] primitiveWrappers = { Boolean.class.getName(), Byte.class.getName(), Character.class.getName(), Short.class.getName(), Integer.class.getName(), Long.class.getName(), Float.class.getName(), Double.class.getName(), Void.class.getName() };
/*      */ 
/*      */   public RequiredModelMBean()
/*      */     throws MBeanException, RuntimeOperationsException
/*      */   {
/*  169 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/*  170 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "RequiredModelMBean()", "Entry");
/*      */     }
/*      */ 
/*  174 */     this.modelMBeanInfo = createDefaultModelMBeanInfo();
/*  175 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER))
/*  176 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "RequiredModelMBean()", "Exit");
/*      */   }
/*      */ 
/*      */   public RequiredModelMBean(ModelMBeanInfo paramModelMBeanInfo)
/*      */     throws MBeanException, RuntimeOperationsException
/*      */   {
/*  205 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/*  206 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "RequiredModelMBean(MBeanInfo)", "Entry");
/*      */     }
/*      */ 
/*  210 */     setModelMBeanInfo(paramModelMBeanInfo);
/*      */ 
/*  212 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER))
/*  213 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "RequiredModelMBean(MBeanInfo)", "Exit");
/*      */   }
/*      */ 
/*      */   public void setModelMBeanInfo(ModelMBeanInfo paramModelMBeanInfo)
/*      */     throws MBeanException, RuntimeOperationsException
/*      */   {
/*  260 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/*  261 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "setModelMBeanInfo(ModelMBeanInfo)", "Entry");
/*      */     }
/*      */ 
/*  266 */     if (paramModelMBeanInfo == null) {
/*  267 */       if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/*  268 */         JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "setModelMBeanInfo(ModelMBeanInfo)", "ModelMBeanInfo is null: Raising exception.");
/*      */       }
/*      */ 
/*  273 */       IllegalArgumentException localIllegalArgumentException = new IllegalArgumentException("ModelMBeanInfo must not be null");
/*      */ 
/*  278 */       throw new RuntimeOperationsException(localIllegalArgumentException, "Exception occurred trying to initialize the ModelMBeanInfo of the RequiredModelMBean");
/*      */     }
/*      */ 
/*  281 */     if (this.registered) {
/*  282 */       if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/*  283 */         JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "setModelMBeanInfo(ModelMBeanInfo)", "RequiredMBean is registered: Raising exception.");
/*      */       }
/*      */ 
/*  291 */       IllegalStateException localIllegalStateException = new IllegalStateException("cannot call setModelMBeanInfo while ModelMBean is registered");
/*      */ 
/*  293 */       throw new RuntimeOperationsException(localIllegalStateException, "Exception occurred trying to set the ModelMBeanInfo of the RequiredModelMBean");
/*      */     }
/*      */ 
/*  296 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/*  297 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "setModelMBeanInfo(ModelMBeanInfo)", "Setting ModelMBeanInfo to " + printModelMBeanInfo(paramModelMBeanInfo));
/*      */ 
/*  301 */       int i = 0;
/*  302 */       if (paramModelMBeanInfo.getNotifications() != null) {
/*  303 */         i = paramModelMBeanInfo.getNotifications().length;
/*      */       }
/*  305 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "setModelMBeanInfo(ModelMBeanInfo)", "ModelMBeanInfo notifications has " + i + " elements");
/*      */     }
/*      */ 
/*  312 */     this.modelMBeanInfo = ((ModelMBeanInfo)paramModelMBeanInfo.clone());
/*      */ 
/*  314 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/*  315 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "setModelMBeanInfo(ModelMBeanInfo)", "set mbeanInfo to: " + printModelMBeanInfo(this.modelMBeanInfo));
/*      */ 
/*  319 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "setModelMBeanInfo(ModelMBeanInfo)", "Exit");
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setManagedResource(Object paramObject, String paramString)
/*      */     throws MBeanException, RuntimeOperationsException, InstanceNotFoundException, InvalidTargetObjectTypeException
/*      */   {
/*  349 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/*  350 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "setManagedResource(Object,String)", "Entry");
/*      */     }
/*      */ 
/*  357 */     if ((paramString == null) || (!paramString.equalsIgnoreCase("objectReference")))
/*      */     {
/*  359 */       if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/*  360 */         JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "setManagedResource(Object,String)", "Managed Resouce Type is not supported: " + paramString);
/*      */       }
/*      */ 
/*  365 */       throw new InvalidTargetObjectTypeException(paramString);
/*      */     }
/*      */ 
/*  368 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/*  369 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "setManagedResource(Object,String)", "Managed Resouce is valid");
/*      */     }
/*      */ 
/*  374 */     this.managedResource = paramObject;
/*      */ 
/*  376 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER))
/*  377 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "setManagedResource(Object, String)", "Exit");
/*      */   }
/*      */ 
/*      */   public void load()
/*      */     throws MBeanException, RuntimeOperationsException, InstanceNotFoundException
/*      */   {
/*  406 */     ServiceNotFoundException localServiceNotFoundException = new ServiceNotFoundException("Persistence not supported for this MBean");
/*      */ 
/*  408 */     throw new MBeanException(localServiceNotFoundException, localServiceNotFoundException.getMessage());
/*      */   }
/*      */ 
/*      */   public void store()
/*      */     throws MBeanException, RuntimeOperationsException, InstanceNotFoundException
/*      */   {
/*  448 */     ServiceNotFoundException localServiceNotFoundException = new ServiceNotFoundException("Persistence not supported for this MBean");
/*      */ 
/*  450 */     throw new MBeanException(localServiceNotFoundException, localServiceNotFoundException.getMessage());
/*      */   }
/*      */ 
/*      */   private Object resolveForCacheValue(Descriptor paramDescriptor)
/*      */     throws MBeanException, RuntimeOperationsException
/*      */   {
/*  482 */     boolean bool1 = JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER);
/*      */ 
/*  484 */     if (bool1) {
/*  485 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "resolveForCacheValue(Descriptor)", "Entry");
/*      */     }
/*      */ 
/*  489 */     Object localObject1 = null;
/*  490 */     boolean bool2 = false; boolean bool3 = true;
/*  491 */     long l1 = 0L;
/*      */ 
/*  493 */     if (paramDescriptor == null) {
/*  494 */       if (bool1) {
/*  495 */         JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "resolveForCacheValue(Descriptor)", "Input Descriptor is null");
/*      */       }
/*      */ 
/*  499 */       return localObject1;
/*      */     }
/*      */ 
/*  502 */     if (bool1) {
/*  503 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "resolveForCacheValue(Descriptor)", "descriptor is " + paramDescriptor);
/*      */     }
/*      */ 
/*  508 */     Descriptor localDescriptor = this.modelMBeanInfo.getMBeanDescriptor();
/*  509 */     if ((localDescriptor == null) && 
/*  510 */       (bool1)) {
/*  511 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "resolveForCacheValue(Descriptor)", "MBean Descriptor is null");
/*      */     }
/*      */ 
/*  518 */     Object localObject2 = paramDescriptor.getFieldValue("currencyTimeLimit");
/*      */     String str1;
/*  521 */     if (localObject2 != null)
/*  522 */       str1 = localObject2.toString();
/*      */     else {
/*  524 */       str1 = null;
/*      */     }
/*      */ 
/*  527 */     if ((str1 == null) && (localDescriptor != null)) {
/*  528 */       localObject2 = localDescriptor.getFieldValue("currencyTimeLimit");
/*  529 */       if (localObject2 != null)
/*  530 */         str1 = localObject2.toString();
/*      */       else {
/*  532 */         str1 = null;
/*      */       }
/*      */     }
/*      */ 
/*  536 */     if (str1 != null) {
/*  537 */       if (bool1) {
/*  538 */         JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "resolveForCacheValue(Descriptor)", "currencyTimeLimit: " + str1);
/*      */       }
/*      */ 
/*  544 */       l1 = new Long(str1).longValue() * 1000L;
/*      */       Object localObject3;
/*  545 */       if (l1 < 0L)
/*      */       {
/*  547 */         bool3 = false;
/*  548 */         bool2 = true;
/*  549 */         if (bool1) {
/*  550 */           JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "resolveForCacheValue(Descriptor)", l1 + ": never Cached");
/*      */         }
/*      */ 
/*      */       }
/*  554 */       else if (l1 == 0L)
/*      */       {
/*  556 */         bool3 = true;
/*  557 */         bool2 = false;
/*  558 */         if (bool1) {
/*  559 */           JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "resolveForCacheValue(Descriptor)", "always valid Cache");
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*  564 */         localObject3 = paramDescriptor.getFieldValue("lastUpdatedTimeStamp");
/*      */         String str2;
/*  568 */         if (localObject3 != null) str2 = localObject3.toString(); else {
/*  569 */           str2 = null;
/*      */         }
/*  571 */         if (bool1) {
/*  572 */           JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "resolveForCacheValue(Descriptor)", "lastUpdatedTimeStamp: " + str2);
/*      */         }
/*      */ 
/*  577 */         if (str2 == null) {
/*  578 */           str2 = "0";
/*      */         }
/*  580 */         long l2 = new Long(str2).longValue();
/*      */ 
/*  582 */         if (bool1) {
/*  583 */           JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "resolveForCacheValue(Descriptor)", "currencyPeriod:" + l1 + " lastUpdatedTimeStamp:" + l2);
/*      */         }
/*      */ 
/*  589 */         long l3 = new Date().getTime();
/*      */ 
/*  591 */         if (l3 < l2 + l1) {
/*  592 */           bool3 = true;
/*  593 */           bool2 = false;
/*  594 */           if (bool1) {
/*  595 */             JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "resolveForCacheValue(Descriptor)", " timed valid Cache for " + l3 + " < " + (l2 + l1));
/*      */           }
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*  601 */           bool3 = false;
/*  602 */           bool2 = true;
/*  603 */           if (bool1) {
/*  604 */             JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "resolveForCacheValue(Descriptor)", "timed expired cache for " + l3 + " > " + (l2 + l1));
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  611 */       if (bool1) {
/*  612 */         JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "resolveForCacheValue(Descriptor)", "returnCachedValue:" + bool3 + " resetValue: " + bool2);
/*      */       }
/*      */ 
/*  618 */       if (bool3 == true) {
/*  619 */         localObject3 = paramDescriptor.getFieldValue("value");
/*  620 */         if (localObject3 != null)
/*      */         {
/*  622 */           localObject1 = localObject3;
/*      */ 
/*  624 */           if (bool1) {
/*  625 */             JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "resolveForCacheValue(Descriptor)", "valid Cache value: " + localObject3);
/*      */           }
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*  631 */           localObject1 = null;
/*  632 */           if (bool1) {
/*  633 */             JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "resolveForCacheValue(Descriptor)", "no Cached value");
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  640 */       if (bool2 == true)
/*      */       {
/*  642 */         paramDescriptor.removeField("lastUpdatedTimeStamp");
/*  643 */         paramDescriptor.removeField("value");
/*  644 */         localObject1 = null;
/*  645 */         this.modelMBeanInfo.setDescriptor(paramDescriptor, null);
/*  646 */         if (bool1) {
/*  647 */           JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "resolveForCacheValue(Descriptor)", "reset cached value to null");
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  654 */     if (bool1) {
/*  655 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "resolveForCacheValue(Descriptor)", "Exit");
/*      */     }
/*      */ 
/*  659 */     return localObject1;
/*      */   }
/*      */ 
/*      */   public MBeanInfo getMBeanInfo()
/*      */   {
/*  672 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/*  673 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "getMBeanInfo()", "Entry");
/*      */     }
/*      */ 
/*  678 */     if (this.modelMBeanInfo == null) {
/*  679 */       if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/*  680 */         JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "getMBeanInfo()", "modelMBeanInfo is null");
/*      */       }
/*      */ 
/*  684 */       this.modelMBeanInfo = createDefaultModelMBeanInfo();
/*      */     }
/*      */ 
/*  688 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/*  689 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "getMBeanInfo()", "ModelMBeanInfo is " + this.modelMBeanInfo.getClassName() + " for " + this.modelMBeanInfo.getDescription());
/*      */ 
/*  694 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "getMBeanInfo()", printModelMBeanInfo(this.modelMBeanInfo));
/*      */     }
/*      */ 
/*  699 */     return (MBeanInfo)this.modelMBeanInfo.clone();
/*      */   }
/*      */ 
/*      */   private String printModelMBeanInfo(ModelMBeanInfo paramModelMBeanInfo) {
/*  703 */     StringBuilder localStringBuilder = new StringBuilder();
/*  704 */     if (paramModelMBeanInfo == null) {
/*  705 */       if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/*  706 */         JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "printModelMBeanInfo(ModelMBeanInfo)", "ModelMBeanInfo to print is null, printing local ModelMBeanInfo");
/*      */       }
/*      */ 
/*  712 */       paramModelMBeanInfo = this.modelMBeanInfo;
/*      */     }
/*      */ 
/*  715 */     localStringBuilder.append("\nMBeanInfo for ModelMBean is:");
/*  716 */     localStringBuilder.append("\nCLASSNAME: \t" + paramModelMBeanInfo.getClassName());
/*  717 */     localStringBuilder.append("\nDESCRIPTION: \t" + paramModelMBeanInfo.getDescription());
/*      */     try
/*      */     {
/*  721 */       localStringBuilder.append("\nMBEAN DESCRIPTOR: \t" + paramModelMBeanInfo.getMBeanDescriptor());
/*      */     }
/*      */     catch (Exception localException) {
/*  724 */       localStringBuilder.append("\nMBEAN DESCRIPTOR: \t is invalid");
/*      */     }
/*      */ 
/*  727 */     localStringBuilder.append("\nATTRIBUTES");
/*      */ 
/*  729 */     MBeanAttributeInfo[] arrayOfMBeanAttributeInfo = paramModelMBeanInfo.getAttributes();
/*  730 */     if ((arrayOfMBeanAttributeInfo != null) && (arrayOfMBeanAttributeInfo.length > 0)) {
/*  731 */       for (int i = 0; i < arrayOfMBeanAttributeInfo.length; i++) {
/*  732 */         ModelMBeanAttributeInfo localModelMBeanAttributeInfo = (ModelMBeanAttributeInfo)arrayOfMBeanAttributeInfo[i];
/*      */ 
/*  734 */         localStringBuilder.append(" ** NAME: \t" + localModelMBeanAttributeInfo.getName());
/*  735 */         localStringBuilder.append("    DESCR: \t" + localModelMBeanAttributeInfo.getDescription());
/*  736 */         localStringBuilder.append("    TYPE: \t" + localModelMBeanAttributeInfo.getType() + "    READ: \t" + localModelMBeanAttributeInfo.isReadable() + "    WRITE: \t" + localModelMBeanAttributeInfo.isWritable());
/*      */ 
/*  739 */         localStringBuilder.append("    DESCRIPTOR: " + localModelMBeanAttributeInfo.getDescriptor().toString());
/*      */       }
/*      */     }
/*      */     else {
/*  743 */       localStringBuilder.append(" ** No attributes **");
/*      */     }
/*      */ 
/*  746 */     localStringBuilder.append("\nCONSTRUCTORS");
/*  747 */     MBeanConstructorInfo[] arrayOfMBeanConstructorInfo = paramModelMBeanInfo.getConstructors();
/*  748 */     if ((arrayOfMBeanConstructorInfo != null) && (arrayOfMBeanConstructorInfo.length > 0)) {
/*  749 */       for (int j = 0; j < arrayOfMBeanConstructorInfo.length; j++) {
/*  750 */         ModelMBeanConstructorInfo localModelMBeanConstructorInfo = (ModelMBeanConstructorInfo)arrayOfMBeanConstructorInfo[j];
/*      */ 
/*  752 */         localStringBuilder.append(" ** NAME: \t" + localModelMBeanConstructorInfo.getName());
/*  753 */         localStringBuilder.append("    DESCR: \t" + localModelMBeanConstructorInfo.getDescription());
/*      */ 
/*  755 */         localStringBuilder.append("    PARAM: \t" + localModelMBeanConstructorInfo.getSignature().length + " parameter(s)");
/*      */ 
/*  758 */         localStringBuilder.append("    DESCRIPTOR: " + localModelMBeanConstructorInfo.getDescriptor().toString());
/*      */       }
/*      */     }
/*      */     else {
/*  762 */       localStringBuilder.append(" ** No Constructors **");
/*      */     }
/*      */ 
/*  765 */     localStringBuilder.append("\nOPERATIONS");
/*  766 */     MBeanOperationInfo[] arrayOfMBeanOperationInfo = paramModelMBeanInfo.getOperations();
/*  767 */     if ((arrayOfMBeanOperationInfo != null) && (arrayOfMBeanOperationInfo.length > 0)) {
/*  768 */       for (int k = 0; k < arrayOfMBeanOperationInfo.length; k++) {
/*  769 */         ModelMBeanOperationInfo localModelMBeanOperationInfo = (ModelMBeanOperationInfo)arrayOfMBeanOperationInfo[k];
/*      */ 
/*  771 */         localStringBuilder.append(" ** NAME: \t" + localModelMBeanOperationInfo.getName());
/*  772 */         localStringBuilder.append("    DESCR: \t" + localModelMBeanOperationInfo.getDescription());
/*  773 */         localStringBuilder.append("    PARAM: \t" + localModelMBeanOperationInfo.getSignature().length + " parameter(s)");
/*      */ 
/*  776 */         localStringBuilder.append("    DESCRIPTOR: " + localModelMBeanOperationInfo.getDescriptor().toString());
/*      */       }
/*      */     }
/*      */     else {
/*  780 */       localStringBuilder.append(" ** No operations ** ");
/*      */     }
/*      */ 
/*  783 */     localStringBuilder.append("\nNOTIFICATIONS");
/*      */ 
/*  785 */     MBeanNotificationInfo[] arrayOfMBeanNotificationInfo = paramModelMBeanInfo.getNotifications();
/*  786 */     if ((arrayOfMBeanNotificationInfo != null) && (arrayOfMBeanNotificationInfo.length > 0)) {
/*  787 */       for (int m = 0; m < arrayOfMBeanNotificationInfo.length; m++) {
/*  788 */         ModelMBeanNotificationInfo localModelMBeanNotificationInfo = (ModelMBeanNotificationInfo)arrayOfMBeanNotificationInfo[m];
/*      */ 
/*  790 */         localStringBuilder.append(" ** NAME: \t" + localModelMBeanNotificationInfo.getName());
/*  791 */         localStringBuilder.append("    DESCR: \t" + localModelMBeanNotificationInfo.getDescription());
/*  792 */         localStringBuilder.append("    DESCRIPTOR: " + localModelMBeanNotificationInfo.getDescriptor().toString());
/*      */       }
/*      */     }
/*      */     else {
/*  796 */       localStringBuilder.append(" ** No notifications **");
/*      */     }
/*      */ 
/*  799 */     localStringBuilder.append(" ** ModelMBean: End of MBeanInfo ** ");
/*      */ 
/*  801 */     return localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   public Object invoke(String paramString, Object[] paramArrayOfObject, String[] paramArrayOfString)
/*      */     throws MBeanException, ReflectionException
/*      */   {
/*  914 */     boolean bool = JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER);
/*      */ 
/*  917 */     if (bool) {
/*  918 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "invoke(String, Object[], String[])", "Entry");
/*      */     }
/*      */ 
/*  922 */     if (paramString == null) {
/*  923 */       localObject1 = new IllegalArgumentException("Method name must not be null");
/*      */ 
/*  925 */       throw new RuntimeOperationsException((RuntimeException)localObject1, "An exception occurred while trying to invoke a method on a RequiredModelMBean");
/*      */     }
/*      */ 
/*  930 */     Object localObject1 = null;
/*      */ 
/*  934 */     int i = paramString.lastIndexOf(".");
/*  935 */     if (i > 0) {
/*  936 */       localObject1 = paramString.substring(0, i);
/*  937 */       str1 = paramString.substring(i + 1);
/*      */     } else {
/*  939 */       str1 = paramString;
/*      */     }
/*      */ 
/*  943 */     i = str1.indexOf("(");
/*  944 */     if (i > 0) {
/*  945 */       str1 = str1.substring(0, i);
/*      */     }
/*  947 */     if (bool) {
/*  948 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "invoke(String, Object[], String[])", "Finding operation " + paramString + " as " + str1);
/*      */     }
/*      */ 
/*  953 */     ModelMBeanOperationInfo localModelMBeanOperationInfo = this.modelMBeanInfo.getOperation(str1);
/*      */ 
/*  955 */     if (localModelMBeanOperationInfo == null) {
/*  956 */       localObject2 = "Operation " + paramString + " not in ModelMBeanInfo";
/*      */ 
/*  958 */       throw new MBeanException(new ServiceNotFoundException((String)localObject2), (String)localObject2);
/*      */     }
/*      */ 
/*  961 */     Object localObject2 = localModelMBeanOperationInfo.getDescriptor();
/*  962 */     if (localObject2 == null)
/*      */     {
/*  964 */       throw new MBeanException(new ServiceNotFoundException("Operation descriptor null"), "Operation descriptor null");
/*      */     }
/*      */ 
/*  967 */     Object localObject3 = resolveForCacheValue((Descriptor)localObject2);
/*  968 */     if (localObject3 != null) {
/*  969 */       if (bool) {
/*  970 */         JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "invoke(String, Object[], String[])", "Returning cached value");
/*      */       }
/*      */ 
/*  975 */       return localObject3;
/*      */     }
/*      */ 
/*  978 */     if (localObject1 == null) {
/*  979 */       localObject1 = (String)((Descriptor)localObject2).getFieldValue("class");
/*      */     }
/*      */ 
/*  982 */     String str1 = (String)((Descriptor)localObject2).getFieldValue("name");
/*  983 */     if (str1 == null)
/*      */     {
/*  986 */       throw new MBeanException(new ServiceNotFoundException("Method descriptor must include `name' field"), "Method descriptor must include `name' field");
/*      */     }
/*      */ 
/*  989 */     String str2 = (String)((Descriptor)localObject2).getFieldValue("targetType");
/*      */ 
/*  991 */     if ((str2 != null) && (!str2.equalsIgnoreCase("objectReference")))
/*      */     {
/*  993 */       localObject4 = "Target type must be objectReference: " + str2;
/*      */ 
/*  995 */       throw new MBeanException(new InvalidTargetObjectTypeException((String)localObject4), (String)localObject4);
/*      */     }
/*      */ 
/*  999 */     Object localObject4 = ((Descriptor)localObject2).getFieldValue("targetObject");
/* 1000 */     if ((bool) && (localObject4 != null)) {
/* 1001 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "invoke(String, Object[], String[])", "Found target object in descriptor");
/*      */     }
/*      */ 
/* 1011 */     Method localMethod = findRMMBMethod(str1, localObject4, (String)localObject1, paramArrayOfString);
/*      */     Object localObject5;
/* 1014 */     if (localMethod != null) {
/* 1015 */       localObject5 = this;
/*      */     } else {
/* 1017 */       if (bool)
/* 1018 */         JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "invoke(String, Object[], String[])", "looking for method in managedResource class");
/*      */       Object localObject7;
/* 1022 */       if (localObject4 != null) {
/* 1023 */         localObject5 = localObject4;
/*      */       } else {
/* 1025 */         localObject5 = this.managedResource;
/* 1026 */         if (localObject5 == null) {
/* 1027 */           localObject6 = "managedResource for invoke " + paramString + " is null";
/*      */ 
/* 1030 */           localObject7 = new ServiceNotFoundException((String)localObject6);
/* 1031 */           throw new MBeanException((Exception)localObject7);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1037 */       if (localObject1 != null)
/*      */         try {
/* 1039 */           localObject7 = AccessController.getContext();
/* 1040 */           localObject8 = localObject5;
/* 1041 */           final Object localObject9 = localObject1;
/* 1042 */           final ClassNotFoundException[] arrayOfClassNotFoundException = new ClassNotFoundException[1];
/*      */ 
/* 1044 */           localObject6 = (Class)javaSecurityAccess.doIntersectionPrivilege(new PrivilegedAction()
/*      */           {
/*      */             public Class<?> run()
/*      */             {
/*      */               try {
/* 1049 */                 ReflectUtil.checkPackageAccess(localObject9);
/* 1050 */                 ClassLoader localClassLoader = localObject8.getClass().getClassLoader();
/*      */ 
/* 1052 */                 return Class.forName(localObject9, false, localClassLoader);
/*      */               }
/*      */               catch (ClassNotFoundException localClassNotFoundException) {
/* 1055 */                 arrayOfClassNotFoundException[0] = localClassNotFoundException;
/*      */               }
/* 1057 */               return null;
/*      */             }
/*      */           }
/*      */           , (AccessControlContext)localObject7, this.acc);
/*      */ 
/* 1061 */           if (arrayOfClassNotFoundException[0] != null)
/* 1062 */             throw arrayOfClassNotFoundException[0];
/*      */         }
/*      */         catch (ClassNotFoundException localClassNotFoundException) {
/* 1065 */           final Object localObject8 = "class for invoke " + paramString + " not found";
/*      */ 
/* 1067 */           throw new ReflectionException(localClassNotFoundException, (String)localObject8);
/*      */         }
/*      */       else {
/* 1070 */         localObject6 = localObject5.getClass();
/*      */       }
/* 1072 */       localMethod = resolveMethod((Class)localObject6, str1, paramArrayOfString);
/*      */     }
/*      */ 
/* 1075 */     if (bool) {
/* 1076 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "invoke(String, Object[], String[])", "found " + str1 + ", now invoking");
/*      */     }
/*      */ 
/* 1081 */     Object localObject6 = invokeMethod(paramString, localMethod, localObject5, paramArrayOfObject);
/*      */ 
/* 1084 */     if (bool) {
/* 1085 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "invoke(String, Object[], String[])", "successfully invoked method");
/*      */     }
/*      */ 
/* 1090 */     if (localObject6 != null) {
/* 1091 */       cacheResult(localModelMBeanOperationInfo, (Descriptor)localObject2, localObject6);
/*      */     }
/* 1093 */     return localObject6;
/*      */   }
/*      */ 
/*      */   private Method resolveMethod(Class<?> paramClass, String paramString, final String[] paramArrayOfString)
/*      */     throws ReflectionException
/*      */   {
/* 1100 */     final boolean bool = JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER);
/*      */ 
/* 1102 */     if (bool)
/* 1103 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "resolveMethod", "resolving " + paramClass.getName() + "." + paramString);
/*      */     final Class[] arrayOfClass;
/*      */     Object localObject;
/* 1110 */     if (paramArrayOfString == null) {
/* 1111 */       arrayOfClass = null;
/*      */     } else {
/* 1113 */       AccessControlContext localAccessControlContext = AccessController.getContext();
/* 1114 */       localObject = new ReflectionException[1];
/* 1115 */       final ClassLoader localClassLoader = paramClass.getClassLoader();
/* 1116 */       arrayOfClass = new Class[paramArrayOfString.length];
/*      */ 
/* 1118 */       javaSecurityAccess.doIntersectionPrivilege(new PrivilegedAction()
/*      */       {
/*      */         public Void run()
/*      */         {
/* 1122 */           for (int i = 0; i < paramArrayOfString.length; i++) {
/* 1123 */             if (bool) {
/* 1124 */               JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "resolveMethod", "resolve type " + paramArrayOfString[i]);
/*      */             }
/*      */ 
/* 1128 */             arrayOfClass[i] = ((Class)RequiredModelMBean.primitiveClassMap.get(paramArrayOfString[i]));
/* 1129 */             if (arrayOfClass[i] == null) {
/*      */               try {
/* 1131 */                 ReflectUtil.checkPackageAccess(paramArrayOfString[i]);
/* 1132 */                 arrayOfClass[i] = Class.forName(paramArrayOfString[i], false, localClassLoader);
/*      */               }
/*      */               catch (ClassNotFoundException localClassNotFoundException) {
/* 1135 */                 if (bool) {
/* 1136 */                   JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "resolveMethod", "class not found");
/*      */                 }
/*      */ 
/* 1142 */                 this.val$caughtException[0] = new ReflectionException(localClassNotFoundException, "Parameter class not found");
/*      */               }
/*      */             }
/*      */           }
/* 1146 */           return null;
/*      */         }
/*      */       }
/*      */       , localAccessControlContext, this.acc);
/*      */ 
/* 1150 */       if (localObject[0] != null) {
/* 1151 */         throw localObject[0];
/*      */       }
/*      */     }
/*      */     try
/*      */     {
/* 1156 */       return paramClass.getMethod(paramString, arrayOfClass);
/*      */     } catch (NoSuchMethodException localNoSuchMethodException) {
/* 1158 */       localObject = "Target method not found: " + paramClass.getName() + "." + paramString;
/*      */ 
/* 1161 */       throw new ReflectionException(localNoSuchMethodException, (String)localObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   private Method findRMMBMethod(String paramString1, Object paramObject, String paramString2, String[] paramArrayOfString)
/*      */   {
/* 1187 */     boolean bool = JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER);
/*      */ 
/* 1189 */     if (bool) {
/* 1190 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "invoke(String, Object[], String[])", "looking for method in RequiredModelMBean class");
/*      */     }
/*      */ 
/* 1196 */     if (!isRMMBMethodName(paramString1))
/* 1197 */       return null;
/* 1198 */     if (paramObject != null)
/* 1199 */       return null;
/* 1200 */     final RequiredModelMBean localRequiredModelMBean = RequiredModelMBean.class;
/*      */     Object localObject;
/* 1202 */     if (paramString2 == null) {
/* 1203 */       localObject = localRequiredModelMBean;
/*      */     } else {
/* 1205 */       AccessControlContext localAccessControlContext = AccessController.getContext();
/* 1206 */       final String str = paramString2;
/* 1207 */       localObject = (Class)javaSecurityAccess.doIntersectionPrivilege(new PrivilegedAction()
/*      */       {
/*      */         public Class<?> run()
/*      */         {
/*      */           try {
/* 1212 */             ReflectUtil.checkPackageAccess(str);
/* 1213 */             ClassLoader localClassLoader = localRequiredModelMBean.getClassLoader();
/*      */ 
/* 1215 */             Class localClass = Class.forName(str, false, localClassLoader);
/*      */ 
/* 1217 */             if (!localRequiredModelMBean.isAssignableFrom(localClass))
/* 1218 */               return null;
/* 1219 */             return localClass; } catch (ClassNotFoundException localClassNotFoundException) {
/*      */           }
/* 1221 */           return null;
/*      */         }
/*      */       }
/*      */       , localAccessControlContext, this.acc);
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/* 1227 */       return localObject != null ? resolveMethod((Class)localObject, paramString1, paramArrayOfString) : null; } catch (ReflectionException localReflectionException) {
/*      */     }
/* 1229 */     return null;
/*      */   }
/*      */ 
/*      */   private Object invokeMethod(String paramString, final Method paramMethod, final Object paramObject, final Object[] paramArrayOfObject)
/*      */     throws MBeanException, ReflectionException
/*      */   {
/*      */     try
/*      */     {
/* 1241 */       final Throwable[] arrayOfThrowable = new Throwable[1];
/* 1242 */       localObject1 = AccessController.getContext();
/* 1243 */       Object localObject2 = javaSecurityAccess.doIntersectionPrivilege(new PrivilegedAction()
/*      */       {
/*      */         public Object run()
/*      */         {
/*      */           try {
/* 1248 */             ReflectUtil.checkPackageAccess(paramMethod.getDeclaringClass());
/* 1249 */             return MethodUtil.invoke(paramMethod, paramObject, paramArrayOfObject);
/*      */           } catch (InvocationTargetException localInvocationTargetException) {
/* 1251 */             arrayOfThrowable[0] = localInvocationTargetException;
/*      */           } catch (IllegalAccessException localIllegalAccessException) {
/* 1253 */             arrayOfThrowable[0] = localIllegalAccessException;
/*      */           }
/* 1255 */           return null;
/*      */         }
/*      */       }
/*      */       , (AccessControlContext)localObject1, this.acc);
/*      */ 
/* 1258 */       if (arrayOfThrowable[0] != null) {
/* 1259 */         if ((arrayOfThrowable[0] instanceof Exception))
/* 1260 */           throw ((Exception)arrayOfThrowable[0]);
/* 1261 */         if ((arrayOfThrowable[0] instanceof Error)) {
/* 1262 */           throw ((Error)arrayOfThrowable[0]);
/*      */         }
/*      */       }
/* 1265 */       return localObject2;
/*      */     } catch (RuntimeErrorException localRuntimeErrorException) {
/* 1267 */       throw new RuntimeOperationsException(localRuntimeErrorException, "RuntimeException occurred in RequiredModelMBean while trying to invoke operation " + paramString);
/*      */     }
/*      */     catch (RuntimeException localRuntimeException)
/*      */     {
/* 1271 */       throw new RuntimeOperationsException(localRuntimeException, "RuntimeException occurred in RequiredModelMBean while trying to invoke operation " + paramString);
/*      */     }
/*      */     catch (IllegalAccessException localIllegalAccessException)
/*      */     {
/* 1275 */       throw new ReflectionException(localIllegalAccessException, "IllegalAccessException occurred in RequiredModelMBean while trying to invoke operation " + paramString);
/*      */     }
/*      */     catch (InvocationTargetException localInvocationTargetException)
/*      */     {
/* 1280 */       Object localObject1 = localInvocationTargetException.getTargetException();
/* 1281 */       if ((localObject1 instanceof RuntimeException)) {
/* 1282 */         throw new MBeanException((RuntimeException)localObject1, "RuntimeException thrown in RequiredModelMBean while trying to invoke operation " + paramString);
/*      */       }
/*      */ 
/* 1285 */       if ((localObject1 instanceof Error)) {
/* 1286 */         throw new RuntimeErrorException((Error)localObject1, "Error occurred in RequiredModelMBean while trying to invoke operation " + paramString);
/*      */       }
/*      */ 
/* 1289 */       if ((localObject1 instanceof ReflectionException)) {
/* 1290 */         throw ((ReflectionException)localObject1);
/*      */       }
/* 1292 */       throw new MBeanException((Exception)localObject1, "Exception thrown in RequiredModelMBean while trying to invoke operation " + paramString);
/*      */     }
/*      */     catch (Error localError)
/*      */     {
/* 1297 */       throw new RuntimeErrorException(localError, "Error occurred in RequiredModelMBean while trying to invoke operation " + paramString);
/*      */     }
/*      */     catch (Exception localException)
/*      */     {
/* 1301 */       throw new ReflectionException(localException, "Exception occurred in RequiredModelMBean while trying to invoke operation " + paramString);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void cacheResult(ModelMBeanOperationInfo paramModelMBeanOperationInfo, Descriptor paramDescriptor, Object paramObject)
/*      */     throws MBeanException
/*      */   {
/* 1317 */     Descriptor localDescriptor = this.modelMBeanInfo.getMBeanDescriptor();
/*      */ 
/* 1320 */     Object localObject = paramDescriptor.getFieldValue("currencyTimeLimit");
/*      */     String str;
/* 1323 */     if (localObject != null)
/* 1324 */       str = localObject.toString();
/*      */     else {
/* 1326 */       str = null;
/*      */     }
/* 1328 */     if ((str == null) && (localDescriptor != null)) {
/* 1329 */       localObject = localDescriptor.getFieldValue("currencyTimeLimit");
/*      */ 
/* 1331 */       if (localObject != null)
/* 1332 */         str = localObject.toString();
/*      */       else {
/* 1334 */         str = null;
/*      */       }
/*      */     }
/* 1337 */     if ((str != null) && (!str.equals("-1"))) {
/* 1338 */       paramDescriptor.setField("value", paramObject);
/* 1339 */       paramDescriptor.setField("lastUpdatedTimeStamp", String.valueOf(new Date().getTime()));
/*      */ 
/* 1343 */       this.modelMBeanInfo.setDescriptor(paramDescriptor, "operation");
/*      */ 
/* 1345 */       if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER))
/* 1346 */         JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "invoke(String,Object[],Object[])", "new descriptor is " + paramDescriptor);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static synchronized boolean isRMMBMethodName(String paramString)
/*      */   {
/* 1370 */     if (rmmbMethodNames == null) {
/*      */       try {
/* 1372 */         HashSet localHashSet = new HashSet();
/* 1373 */         Method[] arrayOfMethod = RequiredModelMBean.class.getMethods();
/* 1374 */         for (int i = 0; i < arrayOfMethod.length; i++)
/* 1375 */           localHashSet.add(arrayOfMethod[i].getName());
/* 1376 */         rmmbMethodNames = localHashSet;
/*      */       } catch (Exception localException) {
/* 1378 */         return true;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1383 */     return rmmbMethodNames.contains(paramString);
/*      */   }
/*      */ 
/*      */   public Object getAttribute(String paramString)
/*      */     throws AttributeNotFoundException, MBeanException, ReflectionException
/*      */   {
/* 1495 */     if (paramString == null) {
/* 1496 */       throw new RuntimeOperationsException(new IllegalArgumentException("attributeName must not be null"), "Exception occurred trying to get attribute of a RequiredModelMBean");
/*      */     }
/*      */ 
/* 1501 */     boolean bool1 = JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER);
/* 1502 */     if (bool1) {
/* 1503 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "getAttribute(String)", "Entry with " + paramString);
/*      */     }
/*      */ 
/*      */     Object localObject1;
/*      */     try
/*      */     {
/* 1512 */       if (this.modelMBeanInfo == null) {
/* 1513 */         throw new AttributeNotFoundException("getAttribute failed: ModelMBeanInfo not found for " + paramString);
/*      */       }
/*      */ 
/* 1517 */       ModelMBeanAttributeInfo localModelMBeanAttributeInfo = this.modelMBeanInfo.getAttribute(paramString);
/* 1518 */       Descriptor localDescriptor1 = this.modelMBeanInfo.getMBeanDescriptor();
/*      */ 
/* 1520 */       if (localModelMBeanAttributeInfo == null) {
/* 1521 */         throw new AttributeNotFoundException("getAttribute failed: ModelMBeanAttributeInfo not found for " + paramString);
/*      */       }
/*      */ 
/* 1524 */       Descriptor localDescriptor2 = localModelMBeanAttributeInfo.getDescriptor();
/* 1525 */       if (localDescriptor2 != null) {
/* 1526 */         if (!localModelMBeanAttributeInfo.isReadable()) {
/* 1527 */           throw new AttributeNotFoundException("getAttribute failed: " + paramString + " is not readable ");
/*      */         }
/*      */ 
/* 1531 */         localObject1 = resolveForCacheValue(localDescriptor2);
/*      */ 
/* 1534 */         if (bool1)
/* 1535 */           JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "getAttribute(String)", "*** cached value is " + localObject1);
/*      */         Object localObject2;
/* 1540 */         if (localObject1 == null)
/*      */         {
/* 1542 */           if (bool1) {
/* 1543 */             JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "getAttribute(String)", "**** cached value is null - getting getMethod");
/*      */           }
/*      */ 
/* 1547 */           str1 = (String)localDescriptor2.getFieldValue("getMethod");
/*      */ 
/* 1550 */           if (str1 != null)
/*      */           {
/* 1552 */             if (bool1) {
/* 1553 */               JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "getAttribute(String)", "invoking a getMethod for " + paramString);
/*      */             }
/*      */ 
/* 1558 */             localObject2 = invoke(str1, new Object[0], new String[0]);
/*      */ 
/* 1562 */             if (localObject2 != null)
/*      */             {
/* 1564 */               if (bool1) {
/* 1565 */                 JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "getAttribute(String)", "got a non-null response from getMethod\n");
/*      */               }
/*      */ 
/* 1571 */               localObject1 = localObject2;
/*      */ 
/* 1574 */               Object localObject3 = localDescriptor2.getFieldValue("currencyTimeLimit");
/*      */               String str2;
/* 1578 */               if (localObject3 != null) str2 = localObject3.toString(); else {
/* 1579 */                 str2 = null;
/*      */               }
/* 1581 */               if ((str2 == null) && (localDescriptor1 != null)) {
/* 1582 */                 localObject3 = localDescriptor1.getFieldValue("currencyTimeLimit");
/*      */ 
/* 1584 */                 if (localObject3 != null) str2 = localObject3.toString(); else {
/* 1585 */                   str2 = null;
/*      */                 }
/*      */               }
/* 1588 */               if ((str2 != null) && (!str2.equals("-1"))) {
/* 1589 */                 if (bool1) {
/* 1590 */                   JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "getAttribute(String)", "setting cached value and lastUpdatedTime in descriptor");
/*      */                 }
/*      */ 
/* 1596 */                 localDescriptor2.setField("value", localObject1);
/* 1597 */                 String str3 = String.valueOf(new Date().getTime());
/*      */ 
/* 1599 */                 localDescriptor2.setField("lastUpdatedTimeStamp", str3);
/*      */ 
/* 1601 */                 localModelMBeanAttributeInfo.setDescriptor(localDescriptor2);
/* 1602 */                 this.modelMBeanInfo.setDescriptor(localDescriptor2, "attribute");
/*      */ 
/* 1604 */                 if (bool1) {
/* 1605 */                   JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "getAttribute(String)", "new descriptor is " + localDescriptor2);
/*      */ 
/* 1608 */                   JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "getAttribute(String)", "AttributeInfo descriptor is " + localModelMBeanAttributeInfo.getDescriptor());
/*      */ 
/* 1612 */                   String str4 = this.modelMBeanInfo.getDescriptor(paramString, "attribute").toString();
/*      */ 
/* 1615 */                   JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "getAttribute(String)", "modelMBeanInfo: AttributeInfo descriptor is " + str4);
/*      */                 }
/*      */ 
/*      */               }
/*      */ 
/*      */             }
/*      */             else
/*      */             {
/* 1624 */               if (bool1) {
/* 1625 */                 JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "getAttribute(String)", "got a null response from getMethod\n");
/*      */               }
/*      */ 
/* 1629 */               localObject1 = null;
/*      */             }
/*      */           }
/*      */           else {
/* 1633 */             localObject2 = "";
/* 1634 */             localObject1 = localDescriptor2.getFieldValue("value");
/* 1635 */             if (localObject1 == null) {
/* 1636 */               localObject2 = "default ";
/* 1637 */               localObject1 = localDescriptor2.getFieldValue("default");
/*      */             }
/* 1639 */             if (bool1) {
/* 1640 */               JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "getAttribute(String)", "could not find getMethod for " + paramString + ", returning descriptor " + (String)localObject2 + "value");
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1650 */         final String str1 = localModelMBeanAttributeInfo.getType();
/* 1651 */         if (localObject1 != null) {
/* 1652 */           localObject2 = localObject1.getClass().getName();
/* 1653 */           if (!str1.equals(localObject2)) {
/* 1654 */             int i = 0;
/* 1655 */             int j = 0;
/* 1656 */             int k = 0;
/* 1657 */             for (int m = 0; m < primitiveTypes.length; m++) {
/* 1658 */               if (str1.equals(primitiveTypes[m])) {
/* 1659 */                 j = 1;
/* 1660 */                 if (!((String)localObject2).equals(primitiveWrappers[m])) break;
/* 1661 */                 k = 1; break;
/*      */               }
/*      */             }
/*      */ 
/* 1665 */             if (j != 0)
/*      */             {
/* 1667 */               if (k == 0)
/* 1668 */                 i = 1;
/*      */             }
/*      */             else {
/*      */               boolean bool2;
/*      */               try {
/* 1673 */                 final Class localClass1 = localObject1.getClass();
/* 1674 */                 final Exception[] arrayOfException = new Exception[1];
/*      */ 
/* 1676 */                 AccessControlContext localAccessControlContext = AccessController.getContext();
/*      */ 
/* 1678 */                 Class localClass2 = (Class)javaSecurityAccess.doIntersectionPrivilege(new PrivilegedAction()
/*      */                 {
/*      */                   public Class<?> run()
/*      */                   {
/*      */                     try {
/* 1683 */                       ReflectUtil.checkPackageAccess(str1);
/* 1684 */                       ClassLoader localClassLoader = localClass1.getClassLoader();
/*      */ 
/* 1686 */                       return Class.forName(str1, true, localClassLoader);
/*      */                     } catch (Exception localException) {
/* 1688 */                       arrayOfException[0] = localException;
/*      */                     }
/* 1690 */                     return null;
/*      */                   }
/*      */                 }
/*      */                 , localAccessControlContext, this.acc);
/*      */ 
/* 1694 */                 if (arrayOfException[0] != null) {
/* 1695 */                   throw arrayOfException[0];
/*      */                 }
/*      */ 
/* 1698 */                 bool2 = localClass2.isInstance(localObject1);
/*      */               } catch (Exception localException2) {
/* 1700 */                 bool2 = false;
/*      */ 
/* 1702 */                 if (bool1) {
/* 1703 */                   JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "getAttribute(String)", "Exception: ", localException2);
/*      */                 }
/*      */ 
/*      */               }
/*      */ 
/* 1708 */               if (!bool2)
/* 1709 */                 i = 1;
/*      */             }
/* 1711 */             if (i != 0) {
/* 1712 */               if (bool1) {
/* 1713 */                 JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "getAttribute(String)", "Wrong response type '" + str1 + "'");
/*      */               }
/*      */ 
/* 1719 */               throw new MBeanException(new InvalidAttributeValueException("Wrong value type received for get attribute"), "An exception occurred while trying to get an attribute value through a RequiredModelMBean");
/*      */             }
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 1728 */         if (bool1) {
/* 1729 */           JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "getAttribute(String)", "getMethod failed " + paramString + " not in attributeDescriptor\n");
/*      */         }
/*      */ 
/* 1734 */         throw new MBeanException(new InvalidAttributeValueException("Unable to resolve attribute value, no getMethod defined in descriptor for attribute"), "An exception occurred while trying to get an attribute value through a RequiredModelMBean");
/*      */       }
/*      */ 
/*      */     }
/*      */     catch (MBeanException localMBeanException)
/*      */     {
/* 1743 */       throw localMBeanException;
/*      */     } catch (AttributeNotFoundException localAttributeNotFoundException) {
/* 1745 */       throw localAttributeNotFoundException;
/*      */     } catch (Exception localException1) {
/* 1747 */       if (bool1) {
/* 1748 */         JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "getAttribute(String)", "getMethod failed with " + localException1.getMessage() + " exception type " + localException1.getClass().toString());
/*      */       }
/*      */ 
/* 1753 */       throw new MBeanException(localException1, "An exception occurred while trying to get an attribute value: " + localException1.getMessage());
/*      */     }
/*      */ 
/* 1757 */     if (bool1) {
/* 1758 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "getAttribute(String)", "Exit");
/*      */     }
/*      */ 
/* 1762 */     return localObject1;
/*      */   }
/*      */ 
/*      */   public AttributeList getAttributes(String[] paramArrayOfString)
/*      */   {
/* 1782 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 1783 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "getAttributes(String[])", "Entry");
/*      */     }
/*      */ 
/* 1788 */     if (paramArrayOfString == null) {
/* 1789 */       throw new RuntimeOperationsException(new IllegalArgumentException("attributeNames must not be null"), "Exception occurred trying to get attributes of a RequiredModelMBean");
/*      */     }
/*      */ 
/* 1794 */     AttributeList localAttributeList = new AttributeList();
/* 1795 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/*      */       try {
/* 1797 */         localAttributeList.add(new Attribute(paramArrayOfString[i], getAttribute(paramArrayOfString[i])));
/*      */       }
/*      */       catch (Exception localException)
/*      */       {
/* 1802 */         if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 1803 */           JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "getAttributes(String[])", "Failed to get \"" + paramArrayOfString[i] + "\": ", localException);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1811 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 1812 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "getAttributes(String[])", "Exit");
/*      */     }
/*      */ 
/* 1817 */     return localAttributeList;
/*      */   }
/*      */ 
/*      */   public void setAttribute(Attribute paramAttribute)
/*      */     throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException
/*      */   {
/* 1899 */     boolean bool = JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER);
/* 1900 */     if (bool) {
/* 1901 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "setAttribute()", "Entry");
/*      */     }
/*      */ 
/* 1906 */     if (paramAttribute == null) {
/* 1907 */       throw new RuntimeOperationsException(new IllegalArgumentException("attribute must not be null"), "Exception occurred trying to set an attribute of a RequiredModelMBean");
/*      */     }
/*      */ 
/* 1917 */     String str1 = paramAttribute.getName();
/* 1918 */     Object localObject1 = paramAttribute.getValue();
/* 1919 */     int i = 0;
/*      */ 
/* 1921 */     ModelMBeanAttributeInfo localModelMBeanAttributeInfo = this.modelMBeanInfo.getAttribute(str1);
/*      */ 
/* 1924 */     if (localModelMBeanAttributeInfo == null) {
/* 1925 */       throw new AttributeNotFoundException("setAttribute failed: " + str1 + " is not found ");
/*      */     }
/*      */ 
/* 1928 */     Descriptor localDescriptor1 = this.modelMBeanInfo.getMBeanDescriptor();
/* 1929 */     Descriptor localDescriptor2 = localModelMBeanAttributeInfo.getDescriptor();
/*      */ 
/* 1931 */     if (localDescriptor2 != null) {
/* 1932 */       if (!localModelMBeanAttributeInfo.isWritable()) {
/* 1933 */         throw new AttributeNotFoundException("setAttribute failed: " + str1 + " is not writable ");
/*      */       }
/*      */ 
/* 1936 */       String str2 = (String)localDescriptor2.getFieldValue("setMethod");
/*      */ 
/* 1938 */       String str3 = (String)localDescriptor2.getFieldValue("getMethod");
/*      */ 
/* 1941 */       String str4 = localModelMBeanAttributeInfo.getType();
/* 1942 */       Object localObject2 = "Unknown";
/*      */       try
/*      */       {
/* 1945 */         localObject2 = getAttribute(str1);
/*      */       }
/*      */       catch (Throwable localThrowable)
/*      */       {
/*      */       }
/* 1950 */       Attribute localAttribute = new Attribute(str1, localObject2);
/*      */ 
/* 1953 */       if (str2 == null) {
/* 1954 */         if (localObject1 != null) {
/*      */           try {
/* 1956 */             Class localClass = loadClass(str4);
/* 1957 */             if (!localClass.isInstance(localObject1)) throw new InvalidAttributeValueException(localClass.getName() + " expected, " + localObject1.getClass().getName() + " received.");
/*      */ 
/*      */           }
/*      */           catch (ClassNotFoundException localClassNotFoundException)
/*      */           {
/* 1963 */             if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 1964 */               JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "setAttribute(Attribute)", "Class " + str4 + " for attribute " + str1 + " not found: ", localClassNotFoundException);
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1972 */         i = 1;
/*      */       } else {
/* 1974 */         invoke(str2, new Object[] { localObject1 }, new String[] { str4 });
/*      */       }
/*      */ 
/* 1980 */       Object localObject3 = localDescriptor2.getFieldValue("currencyTimeLimit");
/*      */       String str5;
/* 1982 */       if (localObject3 != null) str5 = localObject3.toString(); else {
/* 1983 */         str5 = null;
/*      */       }
/* 1985 */       if ((str5 == null) && (localDescriptor1 != null)) {
/* 1986 */         localObject3 = localDescriptor1.getFieldValue("currencyTimeLimit");
/* 1987 */         if (localObject3 != null) str5 = localObject3.toString(); else {
/* 1988 */           str5 = null;
/*      */         }
/*      */       }
/* 1991 */       int j = (str5 != null) && (!str5.equals("-1")) ? 1 : 0;
/*      */ 
/* 1993 */       if ((str2 == null) && (j == 0) && (str3 != null)) {
/* 1994 */         throw new MBeanException(new ServiceNotFoundException("No setMethod field is defined in the descriptor for " + str1 + " attribute and caching is not enabled " + "for it"));
/*      */       }
/*      */ 
/* 1999 */       if ((j != 0) || (i != 0)) {
/* 2000 */         if (bool) {
/* 2001 */           JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "setAttribute(Attribute)", "setting cached value of " + str1 + " to " + localObject1);
/*      */         }
/*      */ 
/* 2008 */         localDescriptor2.setField("value", localObject1);
/*      */         Object localObject4;
/* 2010 */         if (j != 0) {
/* 2011 */           localObject4 = String.valueOf(new Date().getTime());
/*      */ 
/* 2014 */           localDescriptor2.setField("lastUpdatedTimeStamp", localObject4);
/*      */         }
/*      */ 
/* 2017 */         localModelMBeanAttributeInfo.setDescriptor(localDescriptor2);
/*      */ 
/* 2019 */         this.modelMBeanInfo.setDescriptor(localDescriptor2, "attribute");
/* 2020 */         if (bool) {
/* 2021 */           localObject4 = new StringBuilder().append("new descriptor is ").append(localDescriptor2).append(". AttributeInfo descriptor is ").append(localModelMBeanAttributeInfo.getDescriptor()).append(". AttributeInfo descriptor is ").append(this.modelMBeanInfo.getDescriptor(str1, "attribute"));
/*      */ 
/* 2027 */           JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "setAttribute(Attribute)", ((StringBuilder)localObject4).toString());
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2034 */       if (bool) {
/* 2035 */         JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "setAttribute(Attribute)", "sending sendAttributeNotification");
/*      */       }
/*      */ 
/* 2039 */       sendAttributeChangeNotification(localAttribute, paramAttribute);
/*      */     }
/*      */     else
/*      */     {
/* 2043 */       if (bool) {
/* 2044 */         JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "setAttribute(Attribute)", "setMethod failed " + str1 + " not in attributeDescriptor\n");
/*      */       }
/*      */ 
/* 2050 */       throw new InvalidAttributeValueException("Unable to resolve attribute value, no defined in descriptor for attribute");
/*      */     }
/*      */ 
/* 2055 */     if (bool)
/* 2056 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "setAttribute(Attribute)", "Exit");
/*      */   }
/*      */ 
/*      */   public AttributeList setAttributes(AttributeList paramAttributeList)
/*      */   {
/* 2081 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 2082 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "setAttribute(Attribute)", "Entry");
/*      */     }
/*      */ 
/* 2087 */     if (paramAttributeList == null) {
/* 2088 */       throw new RuntimeOperationsException(new IllegalArgumentException("attributes must not be null"), "Exception occurred trying to set attributes of a RequiredModelMBean");
/*      */     }
/*      */ 
/* 2093 */     AttributeList localAttributeList = new AttributeList();
/*      */ 
/* 2096 */     for (Attribute localAttribute : paramAttributeList.asList()) {
/*      */       try {
/* 2098 */         setAttribute(localAttribute);
/* 2099 */         localAttributeList.add(localAttribute);
/*      */       } catch (Exception localException) {
/* 2101 */         localAttributeList.remove(localAttribute);
/*      */       }
/*      */     }
/*      */ 
/* 2105 */     return localAttributeList;
/*      */   }
/*      */ 
/*      */   private ModelMBeanInfo createDefaultModelMBeanInfo()
/*      */   {
/* 2111 */     return new ModelMBeanInfoSupport(getClass().getName(), "Default ModelMBean", null, null, null, null);
/*      */   }
/*      */ 
/*      */   private synchronized void writeToLog(String paramString1, String paramString2)
/*      */     throws Exception
/*      */   {
/* 2123 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 2124 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "writeToLog(String, String)", "Notification Logging to " + paramString1 + ": " + paramString2);
/*      */     }
/*      */ 
/* 2129 */     if ((paramString1 == null) || (paramString2 == null)) {
/* 2130 */       if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 2131 */         JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "writeToLog(String, String)", "Bad input parameters, will not log this entry.");
/*      */       }
/*      */ 
/* 2136 */       return;
/*      */     }
/*      */ 
/* 2139 */     FileOutputStream localFileOutputStream = new FileOutputStream(paramString1, true);
/*      */     try {
/* 2141 */       PrintStream localPrintStream = new PrintStream(localFileOutputStream);
/* 2142 */       localPrintStream.println(paramString2);
/* 2143 */       localPrintStream.close();
/* 2144 */       if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 2145 */         JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "writeToLog(String, String)", "Successfully opened log " + paramString1);
/*      */       }
/*      */ 
/*      */     }
/*      */     catch (Exception localException)
/*      */     {
/* 2151 */       if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 2152 */         JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "writeToLog(String, String)", "Exception " + localException.toString() + " trying to write to the Notification log file " + paramString1);
/*      */       }
/*      */ 
/* 2159 */       throw localException;
/*      */     } finally {
/* 2161 */       localFileOutputStream.close();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void addNotificationListener(NotificationListener paramNotificationListener, NotificationFilter paramNotificationFilter, Object paramObject)
/*      */     throws IllegalArgumentException
/*      */   {
/* 2191 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 2192 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "addNotificationListener(NotificationListener, NotificationFilter, Object)", "Entry");
/*      */     }
/*      */ 
/* 2196 */     if (paramNotificationListener == null) {
/* 2197 */       throw new IllegalArgumentException("notification listener must not be null");
/*      */     }
/*      */ 
/* 2200 */     if (this.generalBroadcaster == null) {
/* 2201 */       this.generalBroadcaster = new NotificationBroadcasterSupport();
/*      */     }
/* 2203 */     this.generalBroadcaster.addNotificationListener(paramNotificationListener, paramNotificationFilter, paramObject);
/*      */ 
/* 2205 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 2206 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "addNotificationListener(NotificationListener, NotificationFilter, Object)", "NotificationListener added");
/*      */ 
/* 2209 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "addNotificationListener(NotificationListener, NotificationFilter, Object)", "Exit");
/*      */     }
/*      */   }
/*      */ 
/*      */   public void removeNotificationListener(NotificationListener paramNotificationListener)
/*      */     throws ListenerNotFoundException
/*      */   {
/* 2228 */     if (paramNotificationListener == null) {
/* 2229 */       throw new ListenerNotFoundException("Notification listener is null");
/*      */     }
/*      */ 
/* 2233 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 2234 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "removeNotificationListener(NotificationListener)", "Entry");
/*      */     }
/*      */ 
/* 2238 */     if (this.generalBroadcaster == null) {
/* 2239 */       throw new ListenerNotFoundException("No notification listeners registered");
/*      */     }
/*      */ 
/* 2243 */     this.generalBroadcaster.removeNotificationListener(paramNotificationListener);
/* 2244 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER))
/* 2245 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "removeNotificationListener(NotificationListener)", "Exit");
/*      */   }
/*      */ 
/*      */   public void removeNotificationListener(NotificationListener paramNotificationListener, NotificationFilter paramNotificationFilter, Object paramObject)
/*      */     throws ListenerNotFoundException
/*      */   {
/* 2256 */     if (paramNotificationListener == null) {
/* 2257 */       throw new ListenerNotFoundException("Notification listener is null");
/*      */     }
/*      */ 
/* 2263 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 2264 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "removeNotificationListener(NotificationListener, NotificationFilter, Object)", "Entry");
/*      */     }
/*      */ 
/* 2268 */     if (this.generalBroadcaster == null) {
/* 2269 */       throw new ListenerNotFoundException("No notification listeners registered");
/*      */     }
/*      */ 
/* 2273 */     this.generalBroadcaster.removeNotificationListener(paramNotificationListener, paramNotificationFilter, paramObject);
/*      */ 
/* 2276 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER))
/* 2277 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "removeNotificationListener(NotificationListener, NotificationFilter, Object)", "Exit");
/*      */   }
/*      */ 
/*      */   public void sendNotification(Notification paramNotification)
/*      */     throws MBeanException, RuntimeOperationsException
/*      */   {
/* 2285 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 2286 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "sendNotification(Notification)", "Entry");
/*      */     }
/*      */ 
/* 2291 */     if (paramNotification == null) {
/* 2292 */       throw new RuntimeOperationsException(new IllegalArgumentException("notification object must not be null"), "Exception occurred trying to send a notification from a RequiredModelMBean");
/*      */     }
/*      */ 
/* 2300 */     Descriptor localDescriptor1 = this.modelMBeanInfo.getDescriptor(paramNotification.getType(), "notification");
/*      */ 
/* 2302 */     Descriptor localDescriptor2 = this.modelMBeanInfo.getMBeanDescriptor();
/*      */ 
/* 2304 */     if (localDescriptor1 != null) {
/* 2305 */       String str1 = (String)localDescriptor1.getFieldValue("log");
/*      */ 
/* 2307 */       if ((str1 == null) && 
/* 2308 */         (localDescriptor2 != null)) {
/* 2309 */         str1 = (String)localDescriptor2.getFieldValue("log");
/*      */       }
/*      */ 
/* 2312 */       if ((str1 != null) && ((str1.equalsIgnoreCase("t")) || (str1.equalsIgnoreCase("true"))))
/*      */       {
/* 2316 */         String str2 = (String)localDescriptor1.getFieldValue("logfile");
/* 2317 */         if ((str2 == null) && 
/* 2318 */           (localDescriptor2 != null)) {
/* 2319 */           str2 = (String)localDescriptor2.getFieldValue("logfile");
/*      */         }
/* 2321 */         if (str2 != null) {
/*      */           try {
/* 2323 */             writeToLog(str2, "LogMsg: " + new Date(paramNotification.getTimeStamp()).toString() + " " + paramNotification.getType() + " " + paramNotification.getMessage() + " Severity = " + (String)localDescriptor1.getFieldValue("severity"));
/*      */           }
/*      */           catch (Exception localException)
/*      */           {
/* 2329 */             if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINE)) {
/* 2330 */               JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINE, RequiredModelMBean.class.getName(), "sendNotification(Notification)", "Failed to log " + paramNotification.getType() + " notification: ", localException);
/*      */             }
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2340 */     if (this.generalBroadcaster != null) {
/* 2341 */       this.generalBroadcaster.sendNotification(paramNotification);
/*      */     }
/*      */ 
/* 2344 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 2345 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "sendNotification(Notification)", "sendNotification sent provided notification object");
/*      */ 
/* 2349 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "sendNotification(Notification)", " Exit");
/*      */     }
/*      */   }
/*      */ 
/*      */   public void sendNotification(String paramString)
/*      */     throws MBeanException, RuntimeOperationsException
/*      */   {
/* 2359 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 2360 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "sendNotification(String)", "Entry");
/*      */     }
/*      */ 
/* 2365 */     if (paramString == null) {
/* 2366 */       throw new RuntimeOperationsException(new IllegalArgumentException("notification message must not be null"), "Exception occurred trying to send a text notification from a ModelMBean");
/*      */     }
/*      */ 
/* 2372 */     Notification localNotification = new Notification("jmx.modelmbean.generic", this, 1L, paramString);
/*      */ 
/* 2374 */     sendNotification(localNotification);
/* 2375 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 2376 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "sendNotification(String)", "Notification sent");
/*      */ 
/* 2379 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "sendNotification(String)", "Exit");
/*      */     }
/*      */   }
/*      */ 
/*      */   private static final boolean hasNotification(ModelMBeanInfo paramModelMBeanInfo, String paramString)
/*      */   {
/*      */     try
/*      */     {
/* 2393 */       if (paramModelMBeanInfo == null) return false;
/* 2394 */       return paramModelMBeanInfo.getNotification(paramString) != null;
/*      */     } catch (MBeanException localMBeanException) {
/* 2396 */       return false; } catch (RuntimeOperationsException localRuntimeOperationsException) {
/*      */     }
/* 2398 */     return false;
/*      */   }
/*      */ 
/*      */   private static final ModelMBeanNotificationInfo makeGenericInfo()
/*      */   {
/* 2407 */     DescriptorSupport localDescriptorSupport = new DescriptorSupport(new String[] { "name=GENERIC", "descriptorType=notification", "log=T", "severity=6", "displayName=jmx.modelmbean.generic" });
/*      */ 
/* 2415 */     return new ModelMBeanNotificationInfo(new String[] { "jmx.modelmbean.generic" }, "GENERIC", "A text notification has been issued by the managed resource", localDescriptorSupport);
/*      */   }
/*      */ 
/*      */   private static final ModelMBeanNotificationInfo makeAttributeChangeInfo()
/*      */   {
/* 2428 */     DescriptorSupport localDescriptorSupport = new DescriptorSupport(new String[] { "name=ATTRIBUTE_CHANGE", "descriptorType=notification", "log=T", "severity=6", "displayName=jmx.attribute.change" });
/*      */ 
/* 2436 */     return new ModelMBeanNotificationInfo(new String[] { "jmx.attribute.change" }, "ATTRIBUTE_CHANGE", "Signifies that an observed MBean attribute value has changed", localDescriptorSupport);
/*      */   }
/*      */ 
/*      */   public MBeanNotificationInfo[] getNotificationInfo()
/*      */   {
/* 2461 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 2462 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "getNotificationInfo()", "Entry");
/*      */     }
/*      */ 
/* 2472 */     boolean bool1 = hasNotification(this.modelMBeanInfo, "GENERIC");
/*      */ 
/* 2477 */     boolean bool2 = hasNotification(this.modelMBeanInfo, "ATTRIBUTE_CHANGE");
/*      */ 
/* 2482 */     ModelMBeanNotificationInfo[] arrayOfModelMBeanNotificationInfo1 = (ModelMBeanNotificationInfo[])this.modelMBeanInfo.getNotifications();
/*      */ 
/* 2489 */     int i = (arrayOfModelMBeanNotificationInfo1 == null ? 0 : arrayOfModelMBeanNotificationInfo1.length) + (bool1 ? 0 : 1) + (bool2 ? 0 : 1);
/*      */ 
/* 2494 */     ModelMBeanNotificationInfo[] arrayOfModelMBeanNotificationInfo2 = new ModelMBeanNotificationInfo[i];
/*      */ 
/* 2503 */     int j = 0;
/* 2504 */     if (!bool1)
/*      */     {
/* 2507 */       arrayOfModelMBeanNotificationInfo2[(j++)] = makeGenericInfo();
/*      */     }
/*      */ 
/* 2510 */     if (!bool2)
/*      */     {
/* 2513 */       arrayOfModelMBeanNotificationInfo2[(j++)] = makeAttributeChangeInfo();
/*      */     }
/*      */ 
/* 2517 */     int k = arrayOfModelMBeanNotificationInfo1.length;
/* 2518 */     int m = j;
/* 2519 */     for (int n = 0; n < k; n++) {
/* 2520 */       arrayOfModelMBeanNotificationInfo2[(m + n)] = arrayOfModelMBeanNotificationInfo1[n];
/*      */     }
/*      */ 
/* 2523 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 2524 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "getNotificationInfo()", "Exit");
/*      */     }
/*      */ 
/* 2529 */     return arrayOfModelMBeanNotificationInfo2;
/*      */   }
/*      */ 
/*      */   public void addAttributeChangeNotificationListener(NotificationListener paramNotificationListener, String paramString, Object paramObject)
/*      */     throws MBeanException, RuntimeOperationsException, IllegalArgumentException
/*      */   {
/* 2543 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 2544 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "addAttributeChangeNotificationListener(NotificationListener, String, Object)", "Entry");
/*      */     }
/*      */ 
/* 2548 */     if (paramNotificationListener == null) {
/* 2549 */       throw new IllegalArgumentException("Listener to be registered must not be null");
/*      */     }
/*      */ 
/* 2553 */     if (this.attributeBroadcaster == null) {
/* 2554 */       this.attributeBroadcaster = new NotificationBroadcasterSupport();
/*      */     }
/* 2556 */     AttributeChangeNotificationFilter localAttributeChangeNotificationFilter = new AttributeChangeNotificationFilter();
/*      */ 
/* 2559 */     MBeanAttributeInfo[] arrayOfMBeanAttributeInfo = this.modelMBeanInfo.getAttributes();
/* 2560 */     int i = 0;
/*      */     int j;
/* 2561 */     if (paramString == null) {
/* 2562 */       if ((arrayOfMBeanAttributeInfo != null) && (arrayOfMBeanAttributeInfo.length > 0))
/* 2563 */         for (j = 0; j < arrayOfMBeanAttributeInfo.length; j++)
/* 2564 */           localAttributeChangeNotificationFilter.enableAttribute(arrayOfMBeanAttributeInfo[j].getName());
/*      */     }
/*      */     else
/*      */     {
/* 2568 */       if ((arrayOfMBeanAttributeInfo != null) && (arrayOfMBeanAttributeInfo.length > 0)) {
/* 2569 */         for (j = 0; j < arrayOfMBeanAttributeInfo.length; j++) {
/* 2570 */           if (paramString.equals(arrayOfMBeanAttributeInfo[j].getName())) {
/* 2571 */             i = 1;
/* 2572 */             localAttributeChangeNotificationFilter.enableAttribute(paramString);
/* 2573 */             break;
/*      */           }
/*      */         }
/*      */       }
/* 2577 */       if (i == 0) {
/* 2578 */         throw new RuntimeOperationsException(new IllegalArgumentException("The attribute name does not exist"), "Exception occurred trying to add an AttributeChangeNotification listener");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2586 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 2587 */       Vector localVector = localAttributeChangeNotificationFilter.getEnabledAttributes();
/* 2588 */       String str = localVector.size() > 1 ? "[" + (String)localVector.firstElement() + ", ...]" : localVector.toString();
/*      */ 
/* 2591 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "addAttributeChangeNotificationListener(NotificationListener, String, Object)", "Set attribute change filter to " + str);
/*      */     }
/*      */ 
/* 2596 */     this.attributeBroadcaster.addNotificationListener(paramNotificationListener, localAttributeChangeNotificationFilter, paramObject);
/*      */ 
/* 2598 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 2599 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "addAttributeChangeNotificationListener(NotificationListener, String, Object)", "Notification listener added for " + paramString);
/*      */ 
/* 2602 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "addAttributeChangeNotificationListener(NotificationListener, String, Object)", "Exit");
/*      */     }
/*      */   }
/*      */ 
/*      */   public void removeAttributeChangeNotificationListener(NotificationListener paramNotificationListener, String paramString)
/*      */     throws MBeanException, RuntimeOperationsException, ListenerNotFoundException
/*      */   {
/* 2611 */     if (paramNotificationListener == null) throw new ListenerNotFoundException("Notification listener is null");
/*      */ 
/* 2617 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 2618 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "removeAttributeChangeNotificationListener(NotificationListener, String)", "Entry");
/*      */     }
/*      */ 
/* 2623 */     if (this.attributeBroadcaster == null) {
/* 2624 */       throw new ListenerNotFoundException("No attribute change notification listeners registered");
/*      */     }
/*      */ 
/* 2628 */     MBeanAttributeInfo[] arrayOfMBeanAttributeInfo = this.modelMBeanInfo.getAttributes();
/* 2629 */     int i = 0;
/* 2630 */     if ((arrayOfMBeanAttributeInfo != null) && (arrayOfMBeanAttributeInfo.length > 0)) {
/* 2631 */       for (int j = 0; j < arrayOfMBeanAttributeInfo.length; j++) {
/* 2632 */         if (arrayOfMBeanAttributeInfo[j].getName().equals(paramString)) {
/* 2633 */           i = 1;
/* 2634 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 2639 */     if ((i == 0) && (paramString != null)) {
/* 2640 */       throw new RuntimeOperationsException(new IllegalArgumentException("Invalid attribute name"), "Exception occurred trying to remove attribute change notification listener");
/*      */     }
/*      */ 
/* 2651 */     this.attributeBroadcaster.removeNotificationListener(paramNotificationListener);
/*      */ 
/* 2653 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER))
/* 2654 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "removeAttributeChangeNotificationListener(NotificationListener, String)", "Exit");
/*      */   }
/*      */ 
/*      */   public void sendAttributeChangeNotification(AttributeChangeNotification paramAttributeChangeNotification)
/*      */     throws MBeanException, RuntimeOperationsException
/*      */   {
/* 2665 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 2666 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "sendAttributeChangeNotification(AttributeChangeNotification)", "Entry");
/*      */     }
/*      */ 
/* 2670 */     if (paramAttributeChangeNotification == null) {
/* 2671 */       throw new RuntimeOperationsException(new IllegalArgumentException("attribute change notification object must not be null"), "Exception occurred trying to send attribute change notification of a ModelMBean");
/*      */     }
/*      */ 
/* 2677 */     Object localObject1 = paramAttributeChangeNotification.getOldValue();
/* 2678 */     Object localObject2 = paramAttributeChangeNotification.getNewValue();
/*      */ 
/* 2680 */     if (localObject1 == null) localObject1 = "null";
/* 2681 */     if (localObject2 == null) localObject2 = "null";
/*      */ 
/* 2683 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 2684 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "sendAttributeChangeNotification(AttributeChangeNotification)", "Sending AttributeChangeNotification with " + paramAttributeChangeNotification.getAttributeName() + paramAttributeChangeNotification.getAttributeType() + paramAttributeChangeNotification.getNewValue() + paramAttributeChangeNotification.getOldValue());
/*      */     }
/*      */ 
/* 2692 */     Descriptor localDescriptor1 = this.modelMBeanInfo.getDescriptor(paramAttributeChangeNotification.getType(), "notification");
/*      */ 
/* 2694 */     Descriptor localDescriptor2 = this.modelMBeanInfo.getMBeanDescriptor();
/*      */     String str1;
/*      */     String str2;
/* 2698 */     if (localDescriptor1 != null) {
/* 2699 */       str1 = (String)localDescriptor1.getFieldValue("log");
/* 2700 */       if ((str1 == null) && 
/* 2701 */         (localDescriptor2 != null)) {
/* 2702 */         str1 = (String)localDescriptor2.getFieldValue("log");
/*      */       }
/* 2704 */       if ((str1 != null) && ((str1.equalsIgnoreCase("t")) || (str1.equalsIgnoreCase("true"))))
/*      */       {
/* 2707 */         str2 = (String)localDescriptor1.getFieldValue("logfile");
/* 2708 */         if ((str2 == null) && 
/* 2709 */           (localDescriptor2 != null)) {
/* 2710 */           str2 = (String)localDescriptor2.getFieldValue("logfile");
/*      */         }
/*      */ 
/* 2713 */         if (str2 != null) {
/*      */           try {
/* 2715 */             writeToLog(str2, "LogMsg: " + new Date(paramAttributeChangeNotification.getTimeStamp()).toString() + " " + paramAttributeChangeNotification.getType() + " " + paramAttributeChangeNotification.getMessage() + " Name = " + paramAttributeChangeNotification.getAttributeName() + " Old value = " + localObject1 + " New value = " + localObject2);
/*      */           }
/*      */           catch (Exception localException1)
/*      */           {
/* 2723 */             if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINE)) {
/* 2724 */               JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINE, RequiredModelMBean.class.getName(), "sendAttributeChangeNotification(AttributeChangeNotification)", "Failed to log " + paramAttributeChangeNotification.getType() + " notification: ", localException1);
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/* 2732 */     else if (localDescriptor2 != null) {
/* 2733 */       str1 = (String)localDescriptor2.getFieldValue("log");
/* 2734 */       if ((str1 != null) && ((str1.equalsIgnoreCase("t")) || (str1.equalsIgnoreCase("true"))))
/*      */       {
/* 2737 */         str2 = (String)localDescriptor2.getFieldValue("logfile");
/*      */ 
/* 2739 */         if (str2 != null) {
/*      */           try {
/* 2741 */             writeToLog(str2, "LogMsg: " + new Date(paramAttributeChangeNotification.getTimeStamp()).toString() + " " + paramAttributeChangeNotification.getType() + " " + paramAttributeChangeNotification.getMessage() + " Name = " + paramAttributeChangeNotification.getAttributeName() + " Old value = " + localObject1 + " New value = " + localObject2);
/*      */           }
/*      */           catch (Exception localException2)
/*      */           {
/* 2749 */             if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINE)) {
/* 2750 */               JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINE, RequiredModelMBean.class.getName(), "sendAttributeChangeNotification(AttributeChangeNotification)", "Failed to log " + paramAttributeChangeNotification.getType() + " notification: ", localException2);
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2759 */     if (this.attributeBroadcaster != null) {
/* 2760 */       this.attributeBroadcaster.sendNotification(paramAttributeChangeNotification);
/*      */     }
/*      */ 
/* 2769 */     if (this.generalBroadcaster != null) {
/* 2770 */       this.generalBroadcaster.sendNotification(paramAttributeChangeNotification);
/*      */     }
/*      */ 
/* 2773 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 2774 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "sendAttributeChangeNotification(AttributeChangeNotification)", "sent notification");
/*      */ 
/* 2777 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "sendAttributeChangeNotification(AttributeChangeNotification)", "Exit");
/*      */     }
/*      */   }
/*      */ 
/*      */   public void sendAttributeChangeNotification(Attribute paramAttribute1, Attribute paramAttribute2)
/*      */     throws MBeanException, RuntimeOperationsException
/*      */   {
/* 2788 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 2789 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "sendAttributeChangeNotification(Attribute, Attribute)", "Entry");
/*      */     }
/*      */ 
/* 2795 */     if ((paramAttribute1 == null) || (paramAttribute2 == null)) {
/* 2796 */       throw new RuntimeOperationsException(new IllegalArgumentException("Attribute object must not be null"), "Exception occurred trying to send attribute change notification of a ModelMBean");
/*      */     }
/*      */ 
/* 2802 */     if (!paramAttribute1.getName().equals(paramAttribute2.getName())) {
/* 2803 */       throw new RuntimeOperationsException(new IllegalArgumentException("Attribute names are not the same"), "Exception occurred trying to send attribute change notification of a ModelMBean");
/*      */     }
/*      */ 
/* 2809 */     Object localObject1 = paramAttribute2.getValue();
/* 2810 */     Object localObject2 = paramAttribute1.getValue();
/* 2811 */     String str = "unknown";
/* 2812 */     if (localObject1 != null)
/* 2813 */       str = localObject1.getClass().getName();
/* 2814 */     if (localObject2 != null) {
/* 2815 */       str = localObject2.getClass().getName();
/*      */     }
/* 2817 */     AttributeChangeNotification localAttributeChangeNotification = new AttributeChangeNotification(this, 1L, new Date().getTime(), "AttributeChangeDetected", paramAttribute1.getName(), str, paramAttribute1.getValue(), paramAttribute2.getValue());
/*      */ 
/* 2827 */     sendAttributeChangeNotification(localAttributeChangeNotification);
/*      */ 
/* 2829 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER))
/* 2830 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, RequiredModelMBean.class.getName(), "sendAttributeChangeNotification(Attribute, Attribute)", "Exit");
/*      */   }
/*      */ 
/*      */   protected ClassLoaderRepository getClassLoaderRepository()
/*      */   {
/* 2847 */     return MBeanServerFactory.getClassLoaderRepository(this.server);
/*      */   }
/*      */ 
/*      */   private Class<?> loadClass(final String paramString) throws ClassNotFoundException
/*      */   {
/* 2852 */     AccessControlContext localAccessControlContext = AccessController.getContext();
/* 2853 */     final ClassNotFoundException[] arrayOfClassNotFoundException = new ClassNotFoundException[1];
/*      */ 
/* 2855 */     Class localClass = (Class)javaSecurityAccess.doIntersectionPrivilege(new PrivilegedAction()
/*      */     {
/*      */       public Class<?> run()
/*      */       {
/*      */         try {
/* 2860 */           ReflectUtil.checkPackageAccess(paramString);
/* 2861 */           return Class.forName(paramString);
/*      */         } catch (ClassNotFoundException localClassNotFoundException1) {
/* 2863 */           ClassLoaderRepository localClassLoaderRepository = RequiredModelMBean.this.getClassLoaderRepository();
/*      */           try
/*      */           {
/* 2866 */             if (localClassLoaderRepository == null) throw new ClassNotFoundException(paramString);
/* 2867 */             return localClassLoaderRepository.loadClass(paramString);
/*      */           } catch (ClassNotFoundException localClassNotFoundException2) {
/* 2869 */             arrayOfClassNotFoundException[0] = localClassNotFoundException2;
/*      */           }
/*      */         }
/* 2872 */         return null;
/*      */       }
/*      */     }
/*      */     , localAccessControlContext, this.acc);
/*      */ 
/* 2876 */     if (arrayOfClassNotFoundException[0] != null) {
/* 2877 */       throw arrayOfClassNotFoundException[0];
/*      */     }
/*      */ 
/* 2880 */     return localClass;
/*      */   }
/*      */ 
/*      */   public ObjectName preRegister(MBeanServer paramMBeanServer, ObjectName paramObjectName)
/*      */     throws Exception
/*      */   {
/* 2924 */     if (paramObjectName == null) throw new NullPointerException("name of RequiredModelMBean to registered is null");
/*      */ 
/* 2926 */     this.server = paramMBeanServer;
/* 2927 */     return paramObjectName;
/*      */   }
/*      */ 
/*      */   public void postRegister(Boolean paramBoolean)
/*      */   {
/* 2944 */     this.registered = paramBoolean.booleanValue();
/*      */   }
/*      */ 
/*      */   public void preDeregister()
/*      */     throws Exception
/*      */   {
/*      */   }
/*      */ 
/*      */   public void postDeregister()
/*      */   {
/* 2974 */     this.registered = false;
/* 2975 */     this.server = null;
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/* 1174 */     for (int i = 0; i < primitiveClasses.length; i++) {
/* 1175 */       Class localClass = primitiveClasses[i];
/* 1176 */       primitiveClassMap.put(localClass.getName(), localClass);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.modelmbean.RequiredModelMBean
 * JD-Core Version:    0.6.2
 */