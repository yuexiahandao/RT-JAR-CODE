/*      */ package javax.management;
/*      */ 
/*      */ import com.sun.jmx.defaults.JmxProperties;
/*      */ import com.sun.jmx.mbeanserver.DescriptorCache;
/*      */ import com.sun.jmx.mbeanserver.Introspector;
/*      */ import com.sun.jmx.mbeanserver.MBeanSupport;
/*      */ import com.sun.jmx.mbeanserver.MXBeanSupport;
/*      */ import com.sun.jmx.mbeanserver.StandardMBeanSupport;
/*      */ import com.sun.jmx.mbeanserver.Util;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.HashMap;
/*      */ import java.util.Map;
/*      */ import java.util.WeakHashMap;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import javax.management.openmbean.OpenMBeanAttributeInfo;
/*      */ import javax.management.openmbean.OpenMBeanAttributeInfoSupport;
/*      */ import javax.management.openmbean.OpenMBeanConstructorInfo;
/*      */ import javax.management.openmbean.OpenMBeanConstructorInfoSupport;
/*      */ import javax.management.openmbean.OpenMBeanOperationInfo;
/*      */ import javax.management.openmbean.OpenMBeanOperationInfoSupport;
/*      */ import javax.management.openmbean.OpenMBeanParameterInfo;
/*      */ import javax.management.openmbean.OpenMBeanParameterInfoSupport;
/*      */ 
/*      */ public class StandardMBean
/*      */   implements DynamicMBean, MBeanRegistration
/*      */ {
/*  128 */   private static final DescriptorCache descriptors = DescriptorCache.getInstance(JMX.proof);
/*      */   private volatile MBeanSupport<?> mbean;
/*      */   private volatile MBeanInfo cachedMBeanInfo;
/* 1150 */   private static final Map<Class<?>, Boolean> mbeanInfoSafeMap = new WeakHashMap();
/*      */ 
/*      */   private <T> void construct(T paramT, Class<T> paramClass, boolean paramBoolean1, boolean paramBoolean2)
/*      */     throws NotCompliantMBeanException
/*      */   {
/*  163 */     if (paramT == null)
/*      */     {
/*  166 */       if (paramBoolean1)
/*  167 */         paramT = Util.cast(this);
/*  168 */       else throw new IllegalArgumentException("implementation is null");
/*      */     }
/*  170 */     if (paramBoolean2) {
/*  171 */       if (paramClass == null) {
/*  172 */         paramClass = (Class)Util.cast(Introspector.getMXBeanInterface(paramT.getClass()));
/*      */       }
/*      */ 
/*  175 */       this.mbean = new MXBeanSupport(paramT, paramClass);
/*      */     } else {
/*  177 */       if (paramClass == null) {
/*  178 */         paramClass = (Class)Util.cast(Introspector.getStandardMBeanInterface(paramT.getClass()));
/*      */       }
/*      */ 
/*  181 */       this.mbean = new StandardMBeanSupport(paramT, paramClass);
/*      */     }
/*      */   }
/*      */ 
/*      */   public <T> StandardMBean(T paramT, Class<T> paramClass)
/*      */     throws NotCompliantMBeanException
/*      */   {
/*  212 */     construct(paramT, paramClass, false, false);
/*      */   }
/*      */ 
/*      */   protected StandardMBean(Class<?> paramClass)
/*      */     throws NotCompliantMBeanException
/*      */   {
/*  232 */     construct(null, paramClass, true, false);
/*      */   }
/*      */ 
/*      */   public <T> StandardMBean(T paramT, Class<T> paramClass, boolean paramBoolean)
/*      */   {
/*      */     try
/*      */     {
/*  269 */       construct(paramT, paramClass, false, paramBoolean);
/*      */     } catch (NotCompliantMBeanException localNotCompliantMBeanException) {
/*  271 */       throw new IllegalArgumentException(localNotCompliantMBeanException);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected StandardMBean(Class<?> paramClass, boolean paramBoolean)
/*      */   {
/*      */     try
/*      */     {
/*  300 */       construct(null, paramClass, true, paramBoolean);
/*      */     } catch (NotCompliantMBeanException localNotCompliantMBeanException) {
/*  302 */       throw new IllegalArgumentException(localNotCompliantMBeanException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setImplementation(Object paramObject)
/*      */     throws NotCompliantMBeanException
/*      */   {
/*  327 */     if (paramObject == null) {
/*  328 */       throw new IllegalArgumentException("implementation is null");
/*      */     }
/*  330 */     if (isMXBean()) {
/*  331 */       this.mbean = new MXBeanSupport(paramObject, (Class)Util.cast(getMBeanInterface()));
/*      */     }
/*      */     else
/*  334 */       this.mbean = new StandardMBeanSupport(paramObject, (Class)Util.cast(getMBeanInterface()));
/*      */   }
/*      */ 
/*      */   public Object getImplementation()
/*      */   {
/*  346 */     return this.mbean.getResource();
/*      */   }
/*      */ 
/*      */   public final Class<?> getMBeanInterface()
/*      */   {
/*  354 */     return this.mbean.getMBeanInterface();
/*      */   }
/*      */ 
/*      */   public Class<?> getImplementationClass()
/*      */   {
/*  362 */     return this.mbean.getResource().getClass();
/*      */   }
/*      */ 
/*      */   public Object getAttribute(String paramString)
/*      */     throws AttributeNotFoundException, MBeanException, ReflectionException
/*      */   {
/*  372 */     return this.mbean.getAttribute(paramString);
/*      */   }
/*      */ 
/*      */   public void setAttribute(Attribute paramAttribute)
/*      */     throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException
/*      */   {
/*  383 */     this.mbean.setAttribute(paramAttribute);
/*      */   }
/*      */ 
/*      */   public AttributeList getAttributes(String[] paramArrayOfString)
/*      */   {
/*  390 */     return this.mbean.getAttributes(paramArrayOfString);
/*      */   }
/*      */ 
/*      */   public AttributeList setAttributes(AttributeList paramAttributeList)
/*      */   {
/*  397 */     return this.mbean.setAttributes(paramAttributeList);
/*      */   }
/*      */ 
/*      */   public Object invoke(String paramString, Object[] paramArrayOfObject, String[] paramArrayOfString)
/*      */     throws MBeanException, ReflectionException
/*      */   {
/*  405 */     return this.mbean.invoke(paramString, paramArrayOfObject, paramArrayOfString);
/*      */   }
/*      */ 
/*      */   public MBeanInfo getMBeanInfo()
/*      */   {
/*      */     try
/*      */     {
/*  432 */       MBeanInfo localMBeanInfo1 = getCachedMBeanInfo();
/*  433 */       if (localMBeanInfo1 != null) return localMBeanInfo1; 
/*      */     }
/*  435 */     catch (RuntimeException localRuntimeException1) { if (JmxProperties.MISC_LOGGER.isLoggable(Level.FINEST)) {
/*  436 */         JmxProperties.MISC_LOGGER.logp(Level.FINEST, MBeanServerFactory.class.getName(), "getMBeanInfo", "Failed to get cached MBeanInfo", localRuntimeException1);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  442 */     if (JmxProperties.MISC_LOGGER.isLoggable(Level.FINER)) {
/*  443 */       JmxProperties.MISC_LOGGER.logp(Level.FINER, MBeanServerFactory.class.getName(), "getMBeanInfo", "Building MBeanInfo for " + getImplementationClass().getName());
/*      */     }
/*      */ 
/*  449 */     MBeanSupport localMBeanSupport = this.mbean;
/*  450 */     MBeanInfo localMBeanInfo2 = localMBeanSupport.getMBeanInfo();
/*  451 */     Object localObject = localMBeanSupport.getResource();
/*      */ 
/*  453 */     boolean bool = immutableInfo(getClass());
/*      */ 
/*  455 */     String str1 = getClassName(localMBeanInfo2);
/*  456 */     String str2 = getDescription(localMBeanInfo2);
/*  457 */     MBeanConstructorInfo[] arrayOfMBeanConstructorInfo = getConstructors(localMBeanInfo2, localObject);
/*  458 */     MBeanAttributeInfo[] arrayOfMBeanAttributeInfo = getAttributes(localMBeanInfo2);
/*  459 */     MBeanOperationInfo[] arrayOfMBeanOperationInfo = getOperations(localMBeanInfo2);
/*  460 */     MBeanNotificationInfo[] arrayOfMBeanNotificationInfo = getNotifications(localMBeanInfo2);
/*  461 */     Descriptor localDescriptor = getDescriptor(localMBeanInfo2, bool);
/*      */ 
/*  463 */     MBeanInfo localMBeanInfo3 = new MBeanInfo(str1, str2, arrayOfMBeanAttributeInfo, arrayOfMBeanConstructorInfo, arrayOfMBeanOperationInfo, arrayOfMBeanNotificationInfo, localDescriptor);
/*      */     try
/*      */     {
/*  466 */       cacheMBeanInfo(localMBeanInfo3);
/*      */     } catch (RuntimeException localRuntimeException2) {
/*  468 */       if (JmxProperties.MISC_LOGGER.isLoggable(Level.FINEST)) {
/*  469 */         JmxProperties.MISC_LOGGER.logp(Level.FINEST, MBeanServerFactory.class.getName(), "getMBeanInfo", "Failed to cache MBeanInfo", localRuntimeException2);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  475 */     return localMBeanInfo3;
/*      */   }
/*      */ 
/*      */   protected String getClassName(MBeanInfo paramMBeanInfo)
/*      */   {
/*  490 */     if (paramMBeanInfo == null) return getImplementationClass().getName();
/*  491 */     return paramMBeanInfo.getClassName();
/*      */   }
/*      */ 
/*      */   protected String getDescription(MBeanInfo paramMBeanInfo)
/*      */   {
/*  506 */     if (paramMBeanInfo == null) return null;
/*  507 */     return paramMBeanInfo.getDescription();
/*      */   }
/*      */ 
/*      */   protected String getDescription(MBeanFeatureInfo paramMBeanFeatureInfo)
/*      */   {
/*  529 */     if (paramMBeanFeatureInfo == null) return null;
/*  530 */     return paramMBeanFeatureInfo.getDescription();
/*      */   }
/*      */ 
/*      */   protected String getDescription(MBeanAttributeInfo paramMBeanAttributeInfo)
/*      */   {
/*  546 */     return getDescription(paramMBeanAttributeInfo);
/*      */   }
/*      */ 
/*      */   protected String getDescription(MBeanConstructorInfo paramMBeanConstructorInfo)
/*      */   {
/*  563 */     return getDescription(paramMBeanConstructorInfo);
/*      */   }
/*      */ 
/*      */   protected String getDescription(MBeanConstructorInfo paramMBeanConstructorInfo, MBeanParameterInfo paramMBeanParameterInfo, int paramInt)
/*      */   {
/*  585 */     if (paramMBeanParameterInfo == null) return null;
/*  586 */     return paramMBeanParameterInfo.getDescription();
/*      */   }
/*      */ 
/*      */   protected String getParameterName(MBeanConstructorInfo paramMBeanConstructorInfo, MBeanParameterInfo paramMBeanParameterInfo, int paramInt)
/*      */   {
/*  608 */     if (paramMBeanParameterInfo == null) return null;
/*  609 */     return paramMBeanParameterInfo.getName();
/*      */   }
/*      */ 
/*      */   protected String getDescription(MBeanOperationInfo paramMBeanOperationInfo)
/*      */   {
/*  625 */     return getDescription(paramMBeanOperationInfo);
/*      */   }
/*      */ 
/*      */   protected int getImpact(MBeanOperationInfo paramMBeanOperationInfo)
/*      */   {
/*  640 */     if (paramMBeanOperationInfo == null) return 3;
/*  641 */     return paramMBeanOperationInfo.getImpact();
/*      */   }
/*      */ 
/*      */   protected String getParameterName(MBeanOperationInfo paramMBeanOperationInfo, MBeanParameterInfo paramMBeanParameterInfo, int paramInt)
/*      */   {
/*  663 */     if (paramMBeanParameterInfo == null) return null;
/*  664 */     return paramMBeanParameterInfo.getName();
/*      */   }
/*      */ 
/*      */   protected String getDescription(MBeanOperationInfo paramMBeanOperationInfo, MBeanParameterInfo paramMBeanParameterInfo, int paramInt)
/*      */   {
/*  686 */     if (paramMBeanParameterInfo == null) return null;
/*  687 */     return paramMBeanParameterInfo.getDescription();
/*      */   }
/*      */ 
/*      */   protected MBeanConstructorInfo[] getConstructors(MBeanConstructorInfo[] paramArrayOfMBeanConstructorInfo, Object paramObject)
/*      */   {
/*  713 */     if (paramArrayOfMBeanConstructorInfo == null) return null;
/*  714 */     if ((paramObject != null) && (paramObject != this)) return null;
/*  715 */     return paramArrayOfMBeanConstructorInfo;
/*      */   }
/*      */ 
/*      */   MBeanNotificationInfo[] getNotifications(MBeanInfo paramMBeanInfo)
/*      */   {
/*  729 */     return null;
/*      */   }
/*      */ 
/*      */   Descriptor getDescriptor(MBeanInfo paramMBeanInfo, boolean paramBoolean)
/*      */   {
/*      */     Object localObject1;
/*      */     Object localObject2;
/*      */     ImmutableDescriptor localImmutableDescriptor;
/*  754 */     if ((paramMBeanInfo == null) || (paramMBeanInfo.getDescriptor() == null) || (paramMBeanInfo.getDescriptor().getFieldNames().length == 0))
/*      */     {
/*  757 */       localObject1 = "interfaceClassName=" + getMBeanInterface().getName();
/*      */ 
/*  759 */       localObject2 = "immutableInfo=" + paramBoolean;
/*      */ 
/*  761 */       localImmutableDescriptor = new ImmutableDescriptor(new String[] { localObject1, localObject2 });
/*  762 */       localImmutableDescriptor = descriptors.get(localImmutableDescriptor);
/*      */     } else {
/*  764 */       localObject1 = paramMBeanInfo.getDescriptor();
/*  765 */       localObject2 = new HashMap();
/*  766 */       for (String str : ((Descriptor)localObject1).getFieldNames()) {
/*  767 */         if (str.equals("immutableInfo"))
/*      */         {
/*  771 */           ((Map)localObject2).put(str, Boolean.toString(paramBoolean));
/*      */         }
/*  773 */         else ((Map)localObject2).put(str, ((Descriptor)localObject1).getFieldValue(str));
/*      */       }
/*      */ 
/*  776 */       localImmutableDescriptor = new ImmutableDescriptor((Map)localObject2);
/*      */     }
/*  778 */     return localImmutableDescriptor;
/*      */   }
/*      */ 
/*      */   protected MBeanInfo getCachedMBeanInfo()
/*      */   {
/*  794 */     return this.cachedMBeanInfo;
/*      */   }
/*      */ 
/*      */   protected void cacheMBeanInfo(MBeanInfo paramMBeanInfo)
/*      */   {
/*  815 */     this.cachedMBeanInfo = paramMBeanInfo;
/*      */   }
/*      */ 
/*      */   private boolean isMXBean() {
/*  819 */     return this.mbean.isMXBean();
/*      */   }
/*      */ 
/*      */   private static <T> boolean identicalArrays(T[] paramArrayOfT1, T[] paramArrayOfT2) {
/*  823 */     if (paramArrayOfT1 == paramArrayOfT2)
/*  824 */       return true;
/*  825 */     if ((paramArrayOfT1 == null) || (paramArrayOfT2 == null) || (paramArrayOfT1.length != paramArrayOfT2.length))
/*  826 */       return false;
/*  827 */     for (int i = 0; i < paramArrayOfT1.length; i++) {
/*  828 */       if (paramArrayOfT1[i] != paramArrayOfT2[i])
/*  829 */         return false;
/*      */     }
/*  831 */     return true;
/*      */   }
/*      */ 
/*      */   private static <T> boolean equal(T paramT1, T paramT2) {
/*  835 */     if (paramT1 == paramT2)
/*  836 */       return true;
/*  837 */     if ((paramT1 == null) || (paramT2 == null))
/*  838 */       return false;
/*  839 */     return paramT1.equals(paramT2);
/*      */   }
/*      */ 
/*      */   private static MBeanParameterInfo customize(MBeanParameterInfo paramMBeanParameterInfo, String paramString1, String paramString2)
/*      */   {
/*  846 */     if ((equal(paramString1, paramMBeanParameterInfo.getName())) && (equal(paramString2, paramMBeanParameterInfo.getDescription())))
/*      */     {
/*  848 */       return paramMBeanParameterInfo;
/*  849 */     }if ((paramMBeanParameterInfo instanceof OpenMBeanParameterInfo)) {
/*  850 */       OpenMBeanParameterInfo localOpenMBeanParameterInfo = (OpenMBeanParameterInfo)paramMBeanParameterInfo;
/*  851 */       return new OpenMBeanParameterInfoSupport(paramString1, paramString2, localOpenMBeanParameterInfo.getOpenType(), paramMBeanParameterInfo.getDescriptor());
/*      */     }
/*      */ 
/*  856 */     return new MBeanParameterInfo(paramString1, paramMBeanParameterInfo.getType(), paramString2, paramMBeanParameterInfo.getDescriptor());
/*      */   }
/*      */ 
/*      */   private static MBeanConstructorInfo customize(MBeanConstructorInfo paramMBeanConstructorInfo, String paramString, MBeanParameterInfo[] paramArrayOfMBeanParameterInfo)
/*      */   {
/*  867 */     if ((equal(paramString, paramMBeanConstructorInfo.getDescription())) && (identicalArrays(paramArrayOfMBeanParameterInfo, paramMBeanConstructorInfo.getSignature())))
/*      */     {
/*  869 */       return paramMBeanConstructorInfo;
/*  870 */     }if ((paramMBeanConstructorInfo instanceof OpenMBeanConstructorInfo)) {
/*  871 */       OpenMBeanParameterInfo[] arrayOfOpenMBeanParameterInfo = paramsToOpenParams(paramArrayOfMBeanParameterInfo);
/*      */ 
/*  873 */       return new OpenMBeanConstructorInfoSupport(paramMBeanConstructorInfo.getName(), paramString, arrayOfOpenMBeanParameterInfo, paramMBeanConstructorInfo.getDescriptor());
/*      */     }
/*      */ 
/*  878 */     return new MBeanConstructorInfo(paramMBeanConstructorInfo.getName(), paramString, paramArrayOfMBeanParameterInfo, paramMBeanConstructorInfo.getDescriptor());
/*      */   }
/*      */ 
/*      */   private static MBeanOperationInfo customize(MBeanOperationInfo paramMBeanOperationInfo, String paramString, MBeanParameterInfo[] paramArrayOfMBeanParameterInfo, int paramInt)
/*      */   {
/*  890 */     if ((equal(paramString, paramMBeanOperationInfo.getDescription())) && (identicalArrays(paramArrayOfMBeanParameterInfo, paramMBeanOperationInfo.getSignature())) && (paramInt == paramMBeanOperationInfo.getImpact()))
/*      */     {
/*  893 */       return paramMBeanOperationInfo;
/*  894 */     }if ((paramMBeanOperationInfo instanceof OpenMBeanOperationInfo)) {
/*  895 */       OpenMBeanOperationInfo localOpenMBeanOperationInfo = (OpenMBeanOperationInfo)paramMBeanOperationInfo;
/*  896 */       OpenMBeanParameterInfo[] arrayOfOpenMBeanParameterInfo = paramsToOpenParams(paramArrayOfMBeanParameterInfo);
/*      */ 
/*  898 */       return new OpenMBeanOperationInfoSupport(paramMBeanOperationInfo.getName(), paramString, arrayOfOpenMBeanParameterInfo, localOpenMBeanOperationInfo.getReturnOpenType(), paramInt, paramMBeanOperationInfo.getDescriptor());
/*      */     }
/*      */ 
/*  905 */     return new MBeanOperationInfo(paramMBeanOperationInfo.getName(), paramString, paramArrayOfMBeanParameterInfo, paramMBeanOperationInfo.getReturnType(), paramInt, paramMBeanOperationInfo.getDescriptor());
/*      */   }
/*      */ 
/*      */   private static MBeanAttributeInfo customize(MBeanAttributeInfo paramMBeanAttributeInfo, String paramString)
/*      */   {
/*  917 */     if (equal(paramString, paramMBeanAttributeInfo.getDescription()))
/*  918 */       return paramMBeanAttributeInfo;
/*  919 */     if ((paramMBeanAttributeInfo instanceof OpenMBeanAttributeInfo)) {
/*  920 */       OpenMBeanAttributeInfo localOpenMBeanAttributeInfo = (OpenMBeanAttributeInfo)paramMBeanAttributeInfo;
/*  921 */       return new OpenMBeanAttributeInfoSupport(paramMBeanAttributeInfo.getName(), paramString, localOpenMBeanAttributeInfo.getOpenType(), paramMBeanAttributeInfo.isReadable(), paramMBeanAttributeInfo.isWritable(), paramMBeanAttributeInfo.isIs(), paramMBeanAttributeInfo.getDescriptor());
/*      */     }
/*      */ 
/*  929 */     return new MBeanAttributeInfo(paramMBeanAttributeInfo.getName(), paramMBeanAttributeInfo.getType(), paramString, paramMBeanAttributeInfo.isReadable(), paramMBeanAttributeInfo.isWritable(), paramMBeanAttributeInfo.isIs(), paramMBeanAttributeInfo.getDescriptor());
/*      */   }
/*      */ 
/*      */   private static OpenMBeanParameterInfo[] paramsToOpenParams(MBeanParameterInfo[] paramArrayOfMBeanParameterInfo)
/*      */   {
/*  941 */     if ((paramArrayOfMBeanParameterInfo instanceof OpenMBeanParameterInfo[]))
/*  942 */       return (OpenMBeanParameterInfo[])paramArrayOfMBeanParameterInfo;
/*  943 */     OpenMBeanParameterInfoSupport[] arrayOfOpenMBeanParameterInfoSupport = new OpenMBeanParameterInfoSupport[paramArrayOfMBeanParameterInfo.length];
/*      */ 
/*  945 */     System.arraycopy(paramArrayOfMBeanParameterInfo, 0, arrayOfOpenMBeanParameterInfoSupport, 0, paramArrayOfMBeanParameterInfo.length);
/*  946 */     return arrayOfOpenMBeanParameterInfoSupport;
/*      */   }
/*      */ 
/*      */   private MBeanConstructorInfo[] getConstructors(MBeanInfo paramMBeanInfo, Object paramObject)
/*      */   {
/*  954 */     MBeanConstructorInfo[] arrayOfMBeanConstructorInfo1 = getConstructors(paramMBeanInfo.getConstructors(), paramObject);
/*      */ 
/*  956 */     if (arrayOfMBeanConstructorInfo1 == null)
/*  957 */       return null;
/*  958 */     int i = arrayOfMBeanConstructorInfo1.length;
/*  959 */     MBeanConstructorInfo[] arrayOfMBeanConstructorInfo2 = new MBeanConstructorInfo[i];
/*  960 */     for (int j = 0; j < i; j++) {
/*  961 */       MBeanConstructorInfo localMBeanConstructorInfo = arrayOfMBeanConstructorInfo1[j];
/*  962 */       MBeanParameterInfo[] arrayOfMBeanParameterInfo1 = localMBeanConstructorInfo.getSignature();
/*      */       MBeanParameterInfo[] arrayOfMBeanParameterInfo2;
/*  964 */       if (arrayOfMBeanParameterInfo1 != null) {
/*  965 */         int k = arrayOfMBeanParameterInfo1.length;
/*  966 */         arrayOfMBeanParameterInfo2 = new MBeanParameterInfo[k];
/*  967 */         for (int m = 0; m < k; m++) {
/*  968 */           MBeanParameterInfo localMBeanParameterInfo = arrayOfMBeanParameterInfo1[m];
/*  969 */           arrayOfMBeanParameterInfo2[m] = customize(localMBeanParameterInfo, getParameterName(localMBeanConstructorInfo, localMBeanParameterInfo, m), getDescription(localMBeanConstructorInfo, localMBeanParameterInfo, m));
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*  974 */         arrayOfMBeanParameterInfo2 = null;
/*      */       }
/*  976 */       arrayOfMBeanConstructorInfo2[j] = customize(localMBeanConstructorInfo, getDescription(localMBeanConstructorInfo), arrayOfMBeanParameterInfo2);
/*      */     }
/*      */ 
/*  979 */     return arrayOfMBeanConstructorInfo2;
/*      */   }
/*      */ 
/*      */   private MBeanOperationInfo[] getOperations(MBeanInfo paramMBeanInfo)
/*      */   {
/*  986 */     MBeanOperationInfo[] arrayOfMBeanOperationInfo1 = paramMBeanInfo.getOperations();
/*  987 */     if (arrayOfMBeanOperationInfo1 == null)
/*  988 */       return null;
/*  989 */     int i = arrayOfMBeanOperationInfo1.length;
/*  990 */     MBeanOperationInfo[] arrayOfMBeanOperationInfo2 = new MBeanOperationInfo[i];
/*  991 */     for (int j = 0; j < i; j++) {
/*  992 */       MBeanOperationInfo localMBeanOperationInfo = arrayOfMBeanOperationInfo1[j];
/*  993 */       MBeanParameterInfo[] arrayOfMBeanParameterInfo1 = localMBeanOperationInfo.getSignature();
/*      */       MBeanParameterInfo[] arrayOfMBeanParameterInfo2;
/*  995 */       if (arrayOfMBeanParameterInfo1 != null) {
/*  996 */         int k = arrayOfMBeanParameterInfo1.length;
/*  997 */         arrayOfMBeanParameterInfo2 = new MBeanParameterInfo[k];
/*  998 */         for (int m = 0; m < k; m++) {
/*  999 */           MBeanParameterInfo localMBeanParameterInfo = arrayOfMBeanParameterInfo1[m];
/* 1000 */           arrayOfMBeanParameterInfo2[m] = customize(localMBeanParameterInfo, getParameterName(localMBeanOperationInfo, localMBeanParameterInfo, m), getDescription(localMBeanOperationInfo, localMBeanParameterInfo, m));
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1005 */         arrayOfMBeanParameterInfo2 = null;
/*      */       }
/* 1007 */       arrayOfMBeanOperationInfo2[j] = customize(localMBeanOperationInfo, getDescription(localMBeanOperationInfo), arrayOfMBeanParameterInfo2, getImpact(localMBeanOperationInfo));
/*      */     }
/* 1009 */     return arrayOfMBeanOperationInfo2;
/*      */   }
/*      */ 
/*      */   private MBeanAttributeInfo[] getAttributes(MBeanInfo paramMBeanInfo)
/*      */   {
/* 1016 */     MBeanAttributeInfo[] arrayOfMBeanAttributeInfo1 = paramMBeanInfo.getAttributes();
/* 1017 */     if (arrayOfMBeanAttributeInfo1 == null) {
/* 1018 */       return null;
/*      */     }
/* 1020 */     int i = arrayOfMBeanAttributeInfo1.length;
/* 1021 */     MBeanAttributeInfo[] arrayOfMBeanAttributeInfo2 = new MBeanAttributeInfo[i];
/* 1022 */     for (int j = 0; j < i; j++) {
/* 1023 */       MBeanAttributeInfo localMBeanAttributeInfo = arrayOfMBeanAttributeInfo1[j];
/* 1024 */       arrayOfMBeanAttributeInfo2[j] = customize(localMBeanAttributeInfo, getDescription(localMBeanAttributeInfo));
/*      */     }
/* 1026 */     return arrayOfMBeanAttributeInfo2;
/*      */   }
/*      */ 
/*      */   public ObjectName preRegister(MBeanServer paramMBeanServer, ObjectName paramObjectName)
/*      */     throws Exception
/*      */   {
/* 1075 */     this.mbean.register(paramMBeanServer, paramObjectName);
/* 1076 */     return paramObjectName;
/*      */   }
/*      */ 
/*      */   public void postRegister(Boolean paramBoolean)
/*      */   {
/* 1099 */     if (!paramBoolean.booleanValue())
/* 1100 */       this.mbean.unregister();
/*      */   }
/*      */ 
/*      */   public void preDeregister()
/*      */     throws Exception
/*      */   {
/*      */   }
/*      */ 
/*      */   public void postDeregister()
/*      */   {
/* 1137 */     this.mbean.unregister();
/*      */   }
/*      */ 
/*      */   static boolean immutableInfo(Class<? extends StandardMBean> paramClass)
/*      */   {
/* 1161 */     if ((paramClass == StandardMBean.class) || (paramClass == StandardEmitterMBean.class))
/*      */     {
/* 1163 */       return true;
/* 1164 */     }synchronized (mbeanInfoSafeMap) {
/* 1165 */       Boolean localBoolean = (Boolean)mbeanInfoSafeMap.get(paramClass);
/* 1166 */       if (localBoolean == null) {
/*      */         try {
/* 1168 */           MBeanInfoSafeAction localMBeanInfoSafeAction = new MBeanInfoSafeAction(paramClass);
/*      */ 
/* 1170 */           localBoolean = (Boolean)AccessController.doPrivileged(localMBeanInfoSafeAction);
/*      */         }
/*      */         catch (Exception localException) {
/* 1173 */           localBoolean = Boolean.valueOf(false);
/*      */         }
/* 1175 */         mbeanInfoSafeMap.put(paramClass, localBoolean);
/*      */       }
/* 1177 */       return localBoolean.booleanValue();
/*      */     }
/*      */   }
/*      */ 
/*      */   static boolean overrides(Class<?> paramClass1, Class<?> paramClass2, String paramString, Class<?>[] paramArrayOfClass)
/*      */   {
/* 1183 */     for (Object localObject = paramClass1; localObject != paramClass2; localObject = ((Class)localObject).getSuperclass())
/*      */       try {
/* 1185 */         ((Class)localObject).getDeclaredMethod(paramString, paramArrayOfClass);
/* 1186 */         return true;
/*      */       }
/*      */       catch (NoSuchMethodException localNoSuchMethodException)
/*      */       {
/*      */       }
/* 1191 */     return false;
/*      */   }
/*      */ 
/*      */   private static class MBeanInfoSafeAction implements PrivilegedAction<Boolean>
/*      */   {
/*      */     private final Class<?> subclass;
/*      */ 
/*      */     MBeanInfoSafeAction(Class<?> paramClass)
/*      */     {
/* 1200 */       this.subclass = paramClass;
/*      */     }
/*      */ 
/*      */     public Boolean run()
/*      */     {
/* 1206 */       if (StandardMBean.overrides(this.subclass, StandardMBean.class, "cacheMBeanInfo", new Class[] { MBeanInfo.class }))
/*      */       {
/* 1208 */         return Boolean.valueOf(false);
/*      */       }
/*      */ 
/* 1212 */       if (StandardMBean.overrides(this.subclass, StandardMBean.class, "getCachedMBeanInfo", (Class[])null))
/*      */       {
/* 1214 */         return Boolean.valueOf(false);
/*      */       }
/*      */ 
/* 1218 */       if (StandardMBean.overrides(this.subclass, StandardMBean.class, "getMBeanInfo", (Class[])null))
/*      */       {
/* 1220 */         return Boolean.valueOf(false);
/*      */       }
/*      */ 
/* 1230 */       if ((StandardEmitterMBean.class.isAssignableFrom(this.subclass)) && 
/* 1231 */         (StandardMBean.overrides(this.subclass, StandardEmitterMBean.class, "getNotificationInfo", (Class[])null)))
/*      */       {
/* 1233 */         return Boolean.valueOf(false);
/* 1234 */       }return Boolean.valueOf(true);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.StandardMBean
 * JD-Core Version:    0.6.2
 */