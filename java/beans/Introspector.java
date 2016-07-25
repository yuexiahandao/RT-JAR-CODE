/*      */ package java.beans;
/*      */ 
/*      */ import com.sun.beans.TypeResolver;
/*      */ import com.sun.beans.WeakCache;
/*      */ import com.sun.beans.finder.BeanInfoFinder;
/*      */ import com.sun.beans.finder.ClassFinder;
/*      */ import java.awt.Component;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.lang.reflect.Type;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.EventListener;
/*      */ import java.util.EventObject;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.TooManyListenersException;
/*      */ import java.util.TreeMap;
/*      */ import sun.reflect.misc.ReflectUtil;
/*      */ 
/*      */ public class Introspector
/*      */ {
/*      */   public static final int USE_ALL_BEANINFO = 1;
/*      */   public static final int IGNORE_IMMEDIATE_BEANINFO = 2;
/*      */   public static final int IGNORE_ALL_BEANINFO = 3;
/*  100 */   private static final WeakCache<Class<?>, Method[]> declaredMethodCache = new WeakCache();
/*      */   private Class beanClass;
/*      */   private BeanInfo explicitBeanInfo;
/*      */   private BeanInfo superBeanInfo;
/*      */   private BeanInfo[] additionalBeanInfo;
/*  107 */   private boolean propertyChangeSource = false;
/*  108 */   private static Class eventListenerType = EventListener.class;
/*      */   private String defaultEventName;
/*      */   private String defaultPropertyName;
/*  113 */   private int defaultEventIndex = -1;
/*  114 */   private int defaultPropertyIndex = -1;
/*      */   private Map methods;
/*      */   private Map properties;
/*      */   private Map events;
/*  125 */   private static final EventSetDescriptor[] EMPTY_EVENTSETDESCRIPTORS = new EventSetDescriptor[0];
/*      */   static final String ADD_PREFIX = "add";
/*      */   static final String REMOVE_PREFIX = "remove";
/*      */   static final String GET_PREFIX = "get";
/*      */   static final String SET_PREFIX = "set";
/*      */   static final String IS_PREFIX = "is";
/*  561 */   private HashMap pdStore = new HashMap();
/*      */ 
/*      */   public static BeanInfo getBeanInfo(Class<?> paramClass)
/*      */     throws IntrospectionException
/*      */   {
/*  154 */     if (!ReflectUtil.isPackageAccessible(paramClass)) {
/*  155 */       return new Introspector(paramClass, null, 1).getBeanInfo();
/*      */     }
/*  157 */     ThreadGroupContext localThreadGroupContext = ThreadGroupContext.getContext();
/*      */     BeanInfo localBeanInfo;
/*  159 */     synchronized (declaredMethodCache) {
/*  160 */       localBeanInfo = localThreadGroupContext.getBeanInfo(paramClass);
/*      */     }
/*  162 */     if (localBeanInfo == null) {
/*  163 */       localBeanInfo = new Introspector(paramClass, null, 1).getBeanInfo();
/*  164 */       synchronized (declaredMethodCache) {
/*  165 */         localThreadGroupContext.putBeanInfo(paramClass, localBeanInfo);
/*      */       }
/*      */     }
/*  168 */     return localBeanInfo;
/*      */   }
/*      */ 
/*      */   public static BeanInfo getBeanInfo(Class<?> paramClass, int paramInt)
/*      */     throws IntrospectionException
/*      */   {
/*  194 */     return getBeanInfo(paramClass, null, paramInt);
/*      */   }
/*      */ 
/*      */   public static BeanInfo getBeanInfo(Class<?> paramClass1, Class<?> paramClass2)
/*      */     throws IntrospectionException
/*      */   {
/*  214 */     return getBeanInfo(paramClass1, paramClass2, 1);
/*      */   }
/*      */ 
/*      */   public static BeanInfo getBeanInfo(Class<?> paramClass1, Class<?> paramClass2, int paramInt)
/*      */     throws IntrospectionException
/*      */   {
/*      */     BeanInfo localBeanInfo;
/*  248 */     if ((paramClass2 == null) && (paramInt == 1))
/*      */     {
/*  250 */       localBeanInfo = getBeanInfo(paramClass1);
/*      */     }
/*  252 */     else localBeanInfo = new Introspector(paramClass1, paramClass2, paramInt).getBeanInfo();
/*      */ 
/*  254 */     return localBeanInfo;
/*      */   }
/*      */ 
/*      */   public static String decapitalize(String paramString)
/*      */   {
/*  275 */     if ((paramString == null) || (paramString.length() == 0)) {
/*  276 */       return paramString;
/*      */     }
/*  278 */     if ((paramString.length() > 1) && (Character.isUpperCase(paramString.charAt(1))) && (Character.isUpperCase(paramString.charAt(0))))
/*      */     {
/*  280 */       return paramString;
/*      */     }
/*  282 */     char[] arrayOfChar = paramString.toCharArray();
/*  283 */     arrayOfChar[0] = Character.toLowerCase(arrayOfChar[0]);
/*  284 */     return new String(arrayOfChar);
/*      */   }
/*      */ 
/*      */   public static String[] getBeanInfoSearchPath()
/*      */   {
/*  298 */     return ThreadGroupContext.getContext().getBeanInfoFinder().getPackages();
/*      */   }
/*      */ 
/*      */   public static void setBeanInfoSearchPath(String[] paramArrayOfString)
/*      */   {
/*  318 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  319 */     if (localSecurityManager != null) {
/*  320 */       localSecurityManager.checkPropertiesAccess();
/*      */     }
/*  322 */     ThreadGroupContext.getContext().getBeanInfoFinder().setPackages(paramArrayOfString);
/*      */   }
/*      */ 
/*      */   public static void flushCaches()
/*      */   {
/*  334 */     synchronized (declaredMethodCache) {
/*  335 */       ThreadGroupContext.getContext().clearBeanInfoCache();
/*  336 */       declaredMethodCache.clear();
/*      */     }
/*      */   }
/*      */ 
/*      */   public static void flushFromCaches(Class<?> paramClass)
/*      */   {
/*  356 */     if (paramClass == null) {
/*  357 */       throw new NullPointerException();
/*      */     }
/*  359 */     synchronized (declaredMethodCache) {
/*  360 */       ThreadGroupContext.getContext().removeBeanInfo(paramClass);
/*  361 */       declaredMethodCache.put(paramClass, null);
/*      */     }
/*      */   }
/*      */ 
/*      */   private Introspector(Class paramClass1, Class paramClass2, int paramInt)
/*      */     throws IntrospectionException
/*      */   {
/*  371 */     this.beanClass = paramClass1;
/*      */ 
/*  374 */     if (paramClass2 != null) {
/*  375 */       int i = 0;
/*  376 */       for (Class localClass2 = paramClass1.getSuperclass(); localClass2 != null; localClass2 = localClass2.getSuperclass()) {
/*  377 */         if (localClass2 == paramClass2) {
/*  378 */           i = 1;
/*      */         }
/*      */       }
/*  381 */       if (i == 0) {
/*  382 */         throw new IntrospectionException(paramClass2.getName() + " not superclass of " + paramClass1.getName());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  387 */     if (paramInt == 1) {
/*  388 */       this.explicitBeanInfo = findExplicitBeanInfo(paramClass1);
/*      */     }
/*      */ 
/*  391 */     Class localClass1 = paramClass1.getSuperclass();
/*  392 */     if (localClass1 != paramClass2) {
/*  393 */       int j = paramInt;
/*  394 */       if (j == 2) {
/*  395 */         j = 1;
/*      */       }
/*  397 */       this.superBeanInfo = getBeanInfo(localClass1, paramClass2, j);
/*      */     }
/*  399 */     if (this.explicitBeanInfo != null) {
/*  400 */       this.additionalBeanInfo = this.explicitBeanInfo.getAdditionalBeanInfo();
/*      */     }
/*  402 */     if (this.additionalBeanInfo == null)
/*  403 */       this.additionalBeanInfo = new BeanInfo[0];
/*      */   }
/*      */ 
/*      */   private BeanInfo getBeanInfo()
/*      */     throws IntrospectionException
/*      */   {
/*  415 */     BeanDescriptor localBeanDescriptor = getTargetBeanDescriptor();
/*  416 */     MethodDescriptor[] arrayOfMethodDescriptor = getTargetMethodInfo();
/*  417 */     EventSetDescriptor[] arrayOfEventSetDescriptor = getTargetEventInfo();
/*  418 */     PropertyDescriptor[] arrayOfPropertyDescriptor = getTargetPropertyInfo();
/*      */ 
/*  420 */     int i = getTargetDefaultEventIndex();
/*  421 */     int j = getTargetDefaultPropertyIndex();
/*      */ 
/*  423 */     return new GenericBeanInfo(localBeanDescriptor, arrayOfEventSetDescriptor, i, arrayOfPropertyDescriptor, j, arrayOfMethodDescriptor, this.explicitBeanInfo);
/*      */   }
/*      */ 
/*      */   private static BeanInfo findExplicitBeanInfo(Class paramClass)
/*      */   {
/*  438 */     return (BeanInfo)ThreadGroupContext.getContext().getBeanInfoFinder().find(paramClass);
/*      */   }
/*      */ 
/*      */   private PropertyDescriptor[] getTargetPropertyInfo()
/*      */   {
/*  450 */     PropertyDescriptor[] arrayOfPropertyDescriptor = null;
/*  451 */     if (this.explicitBeanInfo != null) {
/*  452 */       arrayOfPropertyDescriptor = getPropertyDescriptors(this.explicitBeanInfo);
/*      */     }
/*      */ 
/*  455 */     if ((arrayOfPropertyDescriptor == null) && (this.superBeanInfo != null))
/*      */     {
/*  457 */       addPropertyDescriptors(getPropertyDescriptors(this.superBeanInfo));
/*      */     }
/*      */ 
/*  460 */     for (int i = 0; i < this.additionalBeanInfo.length; i++)
/*  461 */       addPropertyDescriptors(this.additionalBeanInfo[i].getPropertyDescriptors());
/*      */     int j;
/*  464 */     if (arrayOfPropertyDescriptor != null)
/*      */     {
/*  466 */       addPropertyDescriptors(arrayOfPropertyDescriptor);
/*      */     }
/*      */     else
/*      */     {
/*  473 */       localObject1 = getPublicDeclaredMethods(this.beanClass);
/*      */ 
/*  476 */       for (j = 0; j < localObject1.length; j++) {
/*  477 */         Method localMethod = localObject1[j];
/*  478 */         if (localMethod != null)
/*      */         {
/*  482 */           int k = localMethod.getModifiers();
/*  483 */           if (!Modifier.isStatic(k))
/*      */           {
/*  486 */             String str = localMethod.getName();
/*  487 */             Class[] arrayOfClass = localMethod.getParameterTypes();
/*  488 */             Class localClass = localMethod.getReturnType();
/*  489 */             int m = arrayOfClass.length;
/*  490 */             Object localObject2 = null;
/*      */ 
/*  492 */             if ((str.length() > 3) || (str.startsWith("is")))
/*      */             {
/*      */               try
/*      */               {
/*  499 */                 if (m == 0) {
/*  500 */                   if (str.startsWith("get"))
/*      */                   {
/*  502 */                     localObject2 = new PropertyDescriptor(this.beanClass, str.substring(3), localMethod, null);
/*  503 */                   } else if ((localClass == Boolean.TYPE) && (str.startsWith("is")))
/*      */                   {
/*  505 */                     localObject2 = new PropertyDescriptor(this.beanClass, str.substring(2), localMethod, null);
/*      */                   }
/*  507 */                 } else if (m == 1) {
/*  508 */                   if ((Integer.TYPE.equals(arrayOfClass[0])) && (str.startsWith("get"))) {
/*  509 */                     localObject2 = new IndexedPropertyDescriptor(this.beanClass, str.substring(3), null, null, localMethod, null);
/*  510 */                   } else if ((Void.TYPE.equals(localClass)) && (str.startsWith("set")))
/*      */                   {
/*  512 */                     localObject2 = new PropertyDescriptor(this.beanClass, str.substring(3), null, localMethod);
/*  513 */                     if (throwsException(localMethod, PropertyVetoException.class))
/*  514 */                       ((PropertyDescriptor)localObject2).setConstrained(true);
/*      */                   }
/*      */                 }
/*  517 */                 else if ((m == 2) && 
/*  518 */                   (Void.TYPE.equals(localClass)) && (Integer.TYPE.equals(arrayOfClass[0])) && (str.startsWith("set"))) {
/*  519 */                   localObject2 = new IndexedPropertyDescriptor(this.beanClass, str.substring(3), null, null, null, localMethod);
/*  520 */                   if (throwsException(localMethod, PropertyVetoException.class)) {
/*  521 */                     ((PropertyDescriptor)localObject2).setConstrained(true);
/*      */                   }
/*      */ 
/*      */                 }
/*      */ 
/*      */               }
/*      */               catch (IntrospectionException localIntrospectionException)
/*      */               {
/*  530 */                 localObject2 = null;
/*      */               }
/*      */ 
/*  533 */               if (localObject2 != null)
/*      */               {
/*  536 */                 if (this.propertyChangeSource) {
/*  537 */                   ((PropertyDescriptor)localObject2).setBound(true);
/*      */                 }
/*  539 */                 addPropertyDescriptor((PropertyDescriptor)localObject2);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  543 */     processPropertyDescriptors();
/*      */ 
/*  546 */     Object localObject1 = new PropertyDescriptor[this.properties.size()];
/*  547 */     localObject1 = (PropertyDescriptor[])this.properties.values().toArray((Object[])localObject1);
/*      */ 
/*  550 */     if (this.defaultPropertyName != null) {
/*  551 */       for (j = 0; j < localObject1.length; j++) {
/*  552 */         if (this.defaultPropertyName.equals(localObject1[j].getName())) {
/*  553 */           this.defaultPropertyIndex = j;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  558 */     return localObject1;
/*      */   }
/*      */ 
/*      */   private void addPropertyDescriptor(PropertyDescriptor paramPropertyDescriptor)
/*      */   {
/*  567 */     String str = paramPropertyDescriptor.getName();
/*  568 */     Object localObject = (List)this.pdStore.get(str);
/*  569 */     if (localObject == null) {
/*  570 */       localObject = new ArrayList();
/*  571 */       this.pdStore.put(str, localObject);
/*      */     }
/*  573 */     if (this.beanClass != paramPropertyDescriptor.getClass0())
/*      */     {
/*  577 */       Method localMethod1 = paramPropertyDescriptor.getReadMethod();
/*  578 */       Method localMethod2 = paramPropertyDescriptor.getWriteMethod();
/*  579 */       int i = 1;
/*  580 */       if (localMethod1 != null) i = (i != 0) && ((localMethod1.getGenericReturnType() instanceof Class)) ? 1 : 0;
/*  581 */       if (localMethod2 != null) i = (i != 0) && ((localMethod2.getGenericParameterTypes()[0] instanceof Class)) ? 1 : 0;
/*  582 */       if ((paramPropertyDescriptor instanceof IndexedPropertyDescriptor)) {
/*  583 */         IndexedPropertyDescriptor localIndexedPropertyDescriptor = (IndexedPropertyDescriptor)paramPropertyDescriptor;
/*  584 */         Method localMethod3 = localIndexedPropertyDescriptor.getIndexedReadMethod();
/*  585 */         Method localMethod4 = localIndexedPropertyDescriptor.getIndexedWriteMethod();
/*  586 */         if (localMethod3 != null) i = (i != 0) && ((localMethod3.getGenericReturnType() instanceof Class)) ? 1 : 0;
/*  587 */         if (localMethod4 != null) i = (i != 0) && ((localMethod4.getGenericParameterTypes()[1] instanceof Class)) ? 1 : 0;
/*  588 */         if (i == 0) {
/*  589 */           paramPropertyDescriptor = new IndexedPropertyDescriptor(localIndexedPropertyDescriptor);
/*  590 */           paramPropertyDescriptor.updateGenericsFor(this.beanClass);
/*      */         }
/*      */       }
/*  593 */       else if (i == 0) {
/*  594 */         paramPropertyDescriptor = new PropertyDescriptor(paramPropertyDescriptor);
/*  595 */         paramPropertyDescriptor.updateGenericsFor(this.beanClass);
/*      */       }
/*      */     }
/*  598 */     ((List)localObject).add(paramPropertyDescriptor);
/*      */   }
/*      */ 
/*      */   private void addPropertyDescriptors(PropertyDescriptor[] paramArrayOfPropertyDescriptor) {
/*  602 */     if (paramArrayOfPropertyDescriptor != null)
/*  603 */       for (PropertyDescriptor localPropertyDescriptor : paramArrayOfPropertyDescriptor)
/*  604 */         addPropertyDescriptor(localPropertyDescriptor);
/*      */   }
/*      */ 
/*      */   private PropertyDescriptor[] getPropertyDescriptors(BeanInfo paramBeanInfo)
/*      */   {
/*  610 */     PropertyDescriptor[] arrayOfPropertyDescriptor = paramBeanInfo.getPropertyDescriptors();
/*  611 */     int i = paramBeanInfo.getDefaultPropertyIndex();
/*  612 */     if ((0 <= i) && (i < arrayOfPropertyDescriptor.length)) {
/*  613 */       this.defaultPropertyName = arrayOfPropertyDescriptor[i].getName();
/*      */     }
/*  615 */     return arrayOfPropertyDescriptor;
/*      */   }
/*      */ 
/*      */   private void processPropertyDescriptors()
/*      */   {
/*  623 */     if (this.properties == null) {
/*  624 */       this.properties = new TreeMap();
/*      */     }
/*      */ 
/*  632 */     Iterator localIterator = this.pdStore.values().iterator();
/*  633 */     while (localIterator.hasNext()) {
/*  634 */       Object localObject1 = null; Object localObject2 = null; Object localObject3 = null;
/*  635 */       IndexedPropertyDescriptor localIndexedPropertyDescriptor = null; Object localObject4 = null; Object localObject5 = null;
/*      */ 
/*  637 */       List localList = (List)localIterator.next();
/*      */ 
/*  641 */       for (int i = 0; i < localList.size(); i++) {
/*  642 */         localObject1 = (PropertyDescriptor)localList.get(i);
/*  643 */         if ((localObject1 instanceof IndexedPropertyDescriptor)) {
/*  644 */           localIndexedPropertyDescriptor = (IndexedPropertyDescriptor)localObject1;
/*  645 */           if (localIndexedPropertyDescriptor.getIndexedReadMethod() != null) {
/*  646 */             if (localObject4 != null)
/*  647 */               localObject4 = new IndexedPropertyDescriptor((PropertyDescriptor)localObject4, localIndexedPropertyDescriptor);
/*      */             else {
/*  649 */               localObject4 = localIndexedPropertyDescriptor;
/*      */             }
/*      */           }
/*      */         }
/*  653 */         else if (((PropertyDescriptor)localObject1).getReadMethod() != null) {
/*  654 */           if (localObject2 != null)
/*      */           {
/*  657 */             Method localMethod = ((PropertyDescriptor)localObject2).getReadMethod();
/*  658 */             if (!localMethod.getName().startsWith("is"))
/*  659 */               localObject2 = new PropertyDescriptor((PropertyDescriptor)localObject2, (PropertyDescriptor)localObject1);
/*      */           }
/*      */           else {
/*  662 */             localObject2 = localObject1;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  670 */       for (i = 0; i < localList.size(); i++) {
/*  671 */         localObject1 = (PropertyDescriptor)localList.get(i);
/*  672 */         if ((localObject1 instanceof IndexedPropertyDescriptor)) {
/*  673 */           localIndexedPropertyDescriptor = (IndexedPropertyDescriptor)localObject1;
/*  674 */           if (localIndexedPropertyDescriptor.getIndexedWriteMethod() != null) {
/*  675 */             if (localObject4 != null) {
/*  676 */               if (((IndexedPropertyDescriptor)localObject4).getIndexedPropertyType() == localIndexedPropertyDescriptor.getIndexedPropertyType())
/*      */               {
/*  678 */                 if (localObject5 != null)
/*  679 */                   localObject5 = new IndexedPropertyDescriptor((PropertyDescriptor)localObject5, localIndexedPropertyDescriptor);
/*      */                 else {
/*  681 */                   localObject5 = localIndexedPropertyDescriptor;
/*      */                 }
/*      */               }
/*      */             }
/*  685 */             else if (localObject5 != null)
/*  686 */               localObject5 = new IndexedPropertyDescriptor((PropertyDescriptor)localObject5, localIndexedPropertyDescriptor);
/*      */             else {
/*  688 */               localObject5 = localIndexedPropertyDescriptor;
/*      */             }
/*      */           }
/*      */ 
/*      */         }
/*  693 */         else if (((PropertyDescriptor)localObject1).getWriteMethod() != null) {
/*  694 */           if (localObject2 != null) {
/*  695 */             if (((PropertyDescriptor)localObject2).getPropertyType() == ((PropertyDescriptor)localObject1).getPropertyType()) {
/*  696 */               if (localObject3 != null)
/*  697 */                 localObject3 = new PropertyDescriptor((PropertyDescriptor)localObject3, (PropertyDescriptor)localObject1);
/*      */               else {
/*  699 */                 localObject3 = localObject1;
/*      */               }
/*      */             }
/*      */           }
/*  703 */           else if (localObject3 != null)
/*  704 */             localObject3 = new PropertyDescriptor((PropertyDescriptor)localObject3, (PropertyDescriptor)localObject1);
/*      */           else {
/*  706 */             localObject3 = localObject1;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  717 */       localObject1 = null; localIndexedPropertyDescriptor = null;
/*      */ 
/*  719 */       if ((localObject4 != null) && (localObject5 != null))
/*      */       {
/*      */         PropertyDescriptor localPropertyDescriptor;
/*  722 */         if (localObject2 != null) {
/*  723 */           localPropertyDescriptor = mergePropertyDescriptor((IndexedPropertyDescriptor)localObject4, (PropertyDescriptor)localObject2);
/*  724 */           if ((localPropertyDescriptor instanceof IndexedPropertyDescriptor)) {
/*  725 */             localObject4 = (IndexedPropertyDescriptor)localPropertyDescriptor;
/*      */           }
/*      */         }
/*  728 */         if (localObject3 != null) {
/*  729 */           localPropertyDescriptor = mergePropertyDescriptor((IndexedPropertyDescriptor)localObject5, (PropertyDescriptor)localObject3);
/*  730 */           if ((localPropertyDescriptor instanceof IndexedPropertyDescriptor)) {
/*  731 */             localObject5 = (IndexedPropertyDescriptor)localPropertyDescriptor;
/*      */           }
/*      */         }
/*  734 */         if (localObject4 == localObject5)
/*  735 */           localObject1 = localObject4;
/*      */         else
/*  737 */           localObject1 = mergePropertyDescriptor((IndexedPropertyDescriptor)localObject4, (IndexedPropertyDescriptor)localObject5);
/*      */       }
/*  739 */       else if ((localObject2 != null) && (localObject3 != null))
/*      */       {
/*  741 */         if (localObject2 == localObject3)
/*  742 */           localObject1 = localObject2;
/*      */         else
/*  744 */           localObject1 = mergePropertyDescriptor((PropertyDescriptor)localObject2, (PropertyDescriptor)localObject3);
/*      */       }
/*  746 */       else if (localObject5 != null)
/*      */       {
/*  748 */         localObject1 = localObject5;
/*      */ 
/*  750 */         if (localObject3 != null) {
/*  751 */           localObject1 = mergePropertyDescriptor((IndexedPropertyDescriptor)localObject5, (PropertyDescriptor)localObject3);
/*      */         }
/*  753 */         if (localObject2 != null)
/*  754 */           localObject1 = mergePropertyDescriptor((IndexedPropertyDescriptor)localObject5, (PropertyDescriptor)localObject2);
/*      */       }
/*  756 */       else if (localObject4 != null)
/*      */       {
/*  758 */         localObject1 = localObject4;
/*      */ 
/*  760 */         if (localObject2 != null) {
/*  761 */           localObject1 = mergePropertyDescriptor((IndexedPropertyDescriptor)localObject4, (PropertyDescriptor)localObject2);
/*      */         }
/*  763 */         if (localObject3 != null)
/*  764 */           localObject1 = mergePropertyDescriptor((IndexedPropertyDescriptor)localObject4, (PropertyDescriptor)localObject3);
/*      */       }
/*  766 */       else if (localObject3 != null)
/*      */       {
/*  768 */         localObject1 = localObject3;
/*  769 */       } else if (localObject2 != null)
/*      */       {
/*  771 */         localObject1 = localObject2;
/*      */       }
/*      */ 
/*  778 */       if ((localObject1 instanceof IndexedPropertyDescriptor)) {
/*  779 */         localIndexedPropertyDescriptor = (IndexedPropertyDescriptor)localObject1;
/*  780 */         if ((localIndexedPropertyDescriptor.getIndexedReadMethod() == null) && (localIndexedPropertyDescriptor.getIndexedWriteMethod() == null)) {
/*  781 */           localObject1 = new PropertyDescriptor(localIndexedPropertyDescriptor);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  788 */       if ((localObject1 == null) && (localList.size() > 0)) {
/*  789 */         localObject1 = (PropertyDescriptor)localList.get(0);
/*      */       }
/*      */ 
/*  792 */       if (localObject1 != null)
/*  793 */         this.properties.put(((PropertyDescriptor)localObject1).getName(), localObject1);
/*      */     }
/*      */   }
/*      */ 
/*      */   private PropertyDescriptor mergePropertyDescriptor(IndexedPropertyDescriptor paramIndexedPropertyDescriptor, PropertyDescriptor paramPropertyDescriptor)
/*      */   {
/*  806 */     Object localObject = null;
/*      */ 
/*  808 */     Class localClass1 = paramPropertyDescriptor.getPropertyType();
/*  809 */     Class localClass2 = paramIndexedPropertyDescriptor.getIndexedPropertyType();
/*      */ 
/*  811 */     if ((localClass1.isArray()) && (localClass1.getComponentType() == localClass2)) {
/*  812 */       if (paramPropertyDescriptor.getClass0().isAssignableFrom(paramIndexedPropertyDescriptor.getClass0()))
/*  813 */         localObject = new IndexedPropertyDescriptor(paramPropertyDescriptor, paramIndexedPropertyDescriptor);
/*      */       else {
/*  815 */         localObject = new IndexedPropertyDescriptor(paramIndexedPropertyDescriptor, paramPropertyDescriptor);
/*      */       }
/*      */ 
/*      */     }
/*  820 */     else if (paramPropertyDescriptor.getClass0().isAssignableFrom(paramIndexedPropertyDescriptor.getClass0())) {
/*  821 */       localObject = paramIndexedPropertyDescriptor;
/*      */     } else {
/*  823 */       localObject = paramPropertyDescriptor;
/*      */ 
/*  826 */       Method localMethod1 = ((PropertyDescriptor)localObject).getWriteMethod();
/*  827 */       Method localMethod2 = ((PropertyDescriptor)localObject).getReadMethod();
/*      */ 
/*  829 */       if ((localMethod2 == null) && (localMethod1 != null)) {
/*  830 */         localMethod2 = findMethod(((PropertyDescriptor)localObject).getClass0(), "get" + NameGenerator.capitalize(((PropertyDescriptor)localObject).getName()), 0);
/*      */ 
/*  832 */         if (localMethod2 != null)
/*      */           try {
/*  834 */             ((PropertyDescriptor)localObject).setReadMethod(localMethod2);
/*      */           }
/*      */           catch (IntrospectionException localIntrospectionException1)
/*      */           {
/*      */           }
/*      */       }
/*  840 */       if ((localMethod1 == null) && (localMethod2 != null)) {
/*  841 */         localMethod1 = findMethod(((PropertyDescriptor)localObject).getClass0(), "set" + NameGenerator.capitalize(((PropertyDescriptor)localObject).getName()), 1, new Class[] { FeatureDescriptor.getReturnType(((PropertyDescriptor)localObject).getClass0(), localMethod2) });
/*      */ 
/*  844 */         if (localMethod1 != null) {
/*      */           try {
/*  846 */             ((PropertyDescriptor)localObject).setWriteMethod(localMethod1);
/*      */           }
/*      */           catch (IntrospectionException localIntrospectionException2)
/*      */           {
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  854 */     return localObject;
/*      */   }
/*      */ 
/*      */   private PropertyDescriptor mergePropertyDescriptor(PropertyDescriptor paramPropertyDescriptor1, PropertyDescriptor paramPropertyDescriptor2)
/*      */   {
/*  860 */     if (paramPropertyDescriptor1.getClass0().isAssignableFrom(paramPropertyDescriptor2.getClass0())) {
/*  861 */       return new PropertyDescriptor(paramPropertyDescriptor1, paramPropertyDescriptor2);
/*      */     }
/*  863 */     return new PropertyDescriptor(paramPropertyDescriptor2, paramPropertyDescriptor1);
/*      */   }
/*      */ 
/*      */   private PropertyDescriptor mergePropertyDescriptor(IndexedPropertyDescriptor paramIndexedPropertyDescriptor1, IndexedPropertyDescriptor paramIndexedPropertyDescriptor2)
/*      */   {
/*  870 */     if (paramIndexedPropertyDescriptor1.getClass0().isAssignableFrom(paramIndexedPropertyDescriptor2.getClass0())) {
/*  871 */       return new IndexedPropertyDescriptor(paramIndexedPropertyDescriptor1, paramIndexedPropertyDescriptor2);
/*      */     }
/*  873 */     return new IndexedPropertyDescriptor(paramIndexedPropertyDescriptor2, paramIndexedPropertyDescriptor1);
/*      */   }
/*      */ 
/*      */   private EventSetDescriptor[] getTargetEventInfo()
/*      */     throws IntrospectionException
/*      */   {
/*  882 */     if (this.events == null) {
/*  883 */       this.events = new HashMap();
/*      */     }
/*      */ 
/*  888 */     EventSetDescriptor[] arrayOfEventSetDescriptor1 = null;
/*  889 */     if (this.explicitBeanInfo != null) {
/*  890 */       arrayOfEventSetDescriptor1 = this.explicitBeanInfo.getEventSetDescriptors();
/*  891 */       int i = this.explicitBeanInfo.getDefaultEventIndex();
/*  892 */       if ((i >= 0) && (i < arrayOfEventSetDescriptor1.length)) {
/*  893 */         this.defaultEventName = arrayOfEventSetDescriptor1[i].getName();
/*      */       }
/*      */     }
/*      */ 
/*  897 */     if ((arrayOfEventSetDescriptor1 == null) && (this.superBeanInfo != null))
/*      */     {
/*  899 */       EventSetDescriptor[] arrayOfEventSetDescriptor2 = this.superBeanInfo.getEventSetDescriptors();
/*  900 */       for (int k = 0; k < arrayOfEventSetDescriptor2.length; k++) {
/*  901 */         addEvent(arrayOfEventSetDescriptor2[k]);
/*      */       }
/*  903 */       k = this.superBeanInfo.getDefaultEventIndex();
/*  904 */       if ((k >= 0) && (k < arrayOfEventSetDescriptor2.length))
/*  905 */         this.defaultEventName = arrayOfEventSetDescriptor2[k].getName();
/*      */     }
/*      */     Object localObject2;
/*  909 */     for (int j = 0; j < this.additionalBeanInfo.length; j++) {
/*  910 */       localObject2 = this.additionalBeanInfo[j].getEventSetDescriptors();
/*  911 */       if (localObject2 != null)
/*  912 */         for (int n = 0; n < localObject2.length; n++)
/*  913 */           addEvent(localObject2[n]);
/*      */     }
/*      */     Object localObject1;
/*  918 */     if (arrayOfEventSetDescriptor1 != null)
/*      */     {
/*  920 */       for (j = 0; j < arrayOfEventSetDescriptor1.length; j++) {
/*  921 */         addEvent(arrayOfEventSetDescriptor1[j]);
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*  929 */       localObject1 = getPublicDeclaredMethods(this.beanClass);
/*      */ 
/*  934 */       localObject2 = null;
/*  935 */       HashMap localHashMap1 = null;
/*  936 */       HashMap localHashMap2 = null;
/*      */       Object localObject3;
/*      */       Object localObject4;
/*      */       Object localObject5;
/*      */       Object localObject6;
/*      */       Class localClass;
/*      */       Object localObject7;
/*  938 */       for (int i1 = 0; i1 < localObject1.length; i1++) {
/*  939 */         localObject3 = localObject1[i1];
/*  940 */         if (localObject3 != null)
/*      */         {
/*  944 */           int i2 = ((Method)localObject3).getModifiers();
/*  945 */           if (!Modifier.isStatic(i2))
/*      */           {
/*  948 */             localObject4 = ((Method)localObject3).getName();
/*      */ 
/*  950 */             if ((((String)localObject4).startsWith("add")) || (((String)localObject4).startsWith("remove")) || (((String)localObject4).startsWith("get")))
/*      */             {
/*  955 */               if (((String)localObject4).startsWith("add")) {
/*  956 */                 localObject5 = ((Method)localObject3).getReturnType();
/*  957 */                 if (localObject5 == Void.TYPE) {
/*  958 */                   localObject6 = ((Method)localObject3).getGenericParameterTypes();
/*  959 */                   if (localObject6.length == 1) {
/*  960 */                     localClass = TypeResolver.erase(TypeResolver.resolveInClass(this.beanClass, localObject6[0]));
/*  961 */                     if (isSubclass(localClass, eventListenerType)) {
/*  962 */                       localObject7 = ((String)localObject4).substring(3);
/*  963 */                       if ((((String)localObject7).length() > 0) && (localClass.getName().endsWith((String)localObject7)))
/*      */                       {
/*  965 */                         if (localObject2 == null) {
/*  966 */                           localObject2 = new HashMap();
/*      */                         }
/*  968 */                         ((Map)localObject2).put(localObject7, localObject3);
/*      */                       }
/*      */                     }
/*      */                   }
/*      */                 }
/*      */               }
/*  974 */               else if (((String)localObject4).startsWith("remove")) {
/*  975 */                 localObject5 = ((Method)localObject3).getReturnType();
/*  976 */                 if (localObject5 == Void.TYPE) {
/*  977 */                   localObject6 = ((Method)localObject3).getGenericParameterTypes();
/*  978 */                   if (localObject6.length == 1) {
/*  979 */                     localClass = TypeResolver.erase(TypeResolver.resolveInClass(this.beanClass, localObject6[0]));
/*  980 */                     if (isSubclass(localClass, eventListenerType)) {
/*  981 */                       localObject7 = ((String)localObject4).substring(6);
/*  982 */                       if ((((String)localObject7).length() > 0) && (localClass.getName().endsWith((String)localObject7)))
/*      */                       {
/*  984 */                         if (localHashMap1 == null) {
/*  985 */                           localHashMap1 = new HashMap();
/*      */                         }
/*  987 */                         localHashMap1.put(localObject7, localObject3);
/*      */                       }
/*      */                     }
/*      */                   }
/*      */                 }
/*      */               }
/*  993 */               else if (((String)localObject4).startsWith("get")) {
/*  994 */                 localObject5 = ((Method)localObject3).getParameterTypes();
/*  995 */                 if (localObject5.length == 0) {
/*  996 */                   localObject6 = FeatureDescriptor.getReturnType(this.beanClass, (Method)localObject3);
/*  997 */                   if (((Class)localObject6).isArray()) {
/*  998 */                     localClass = ((Class)localObject6).getComponentType();
/*  999 */                     if (isSubclass(localClass, eventListenerType)) {
/* 1000 */                       localObject7 = ((String)localObject4).substring(3, ((String)localObject4).length() - 1);
/* 1001 */                       if ((((String)localObject7).length() > 0) && (localClass.getName().endsWith((String)localObject7)))
/*      */                       {
/* 1003 */                         if (localHashMap2 == null) {
/* 1004 */                           localHashMap2 = new HashMap();
/*      */                         }
/* 1006 */                         localHashMap2.put(localObject7, localObject3);
/*      */                       }
/*      */                     }
/*      */                   }
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/* 1014 */       if ((localObject2 != null) && (localHashMap1 != null))
/*      */       {
/* 1017 */         Iterator localIterator = ((Map)localObject2).keySet().iterator();
/* 1018 */         while (localIterator.hasNext()) {
/* 1019 */           localObject3 = (String)localIterator.next();
/*      */ 
/* 1022 */           if ((localHashMap1.get(localObject3) != null) && (((String)localObject3).endsWith("Listener")))
/*      */           {
/* 1025 */             String str = decapitalize(((String)localObject3).substring(0, ((String)localObject3).length() - 8));
/* 1026 */             localObject4 = (Method)((Map)localObject2).get(localObject3);
/* 1027 */             localObject5 = (Method)localHashMap1.get(localObject3);
/* 1028 */             localObject6 = null;
/* 1029 */             if (localHashMap2 != null) {
/* 1030 */               localObject6 = (Method)localHashMap2.get(localObject3);
/*      */             }
/* 1032 */             localClass = FeatureDescriptor.getParameterTypes(this.beanClass, localObject4)[0];
/*      */ 
/* 1035 */             localObject7 = getPublicDeclaredMethods(localClass);
/* 1036 */             ArrayList localArrayList = new ArrayList(localObject7.length);
/* 1037 */             for (int i3 = 0; i3 < localObject7.length; i3++) {
/* 1038 */               if (localObject7[i3] != null)
/*      */               {
/* 1042 */                 if (isEventHandler(localObject7[i3]))
/* 1043 */                   localArrayList.add(localObject7[i3]);
/*      */               }
/*      */             }
/* 1046 */             Method[] arrayOfMethod = (Method[])localArrayList.toArray(new Method[localArrayList.size()]);
/*      */ 
/* 1048 */             EventSetDescriptor localEventSetDescriptor = new EventSetDescriptor(str, localClass, arrayOfMethod, (Method)localObject4, (Method)localObject5, (Method)localObject6);
/*      */ 
/* 1055 */             if (throwsException((Method)localObject4, TooManyListenersException.class))
/*      */             {
/* 1057 */               localEventSetDescriptor.setUnicast(true);
/*      */             }
/* 1059 */             addEvent(localEventSetDescriptor);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1064 */     if (this.events.size() == 0) {
/* 1065 */       localObject1 = EMPTY_EVENTSETDESCRIPTORS;
/*      */     }
/*      */     else {
/* 1068 */       localObject1 = new EventSetDescriptor[this.events.size()];
/* 1069 */       localObject1 = (EventSetDescriptor[])this.events.values().toArray((Object[])localObject1);
/*      */ 
/* 1072 */       if (this.defaultEventName != null) {
/* 1073 */         for (int m = 0; m < localObject1.length; m++) {
/* 1074 */           if (this.defaultEventName.equals(localObject1[m].getName())) {
/* 1075 */             this.defaultEventIndex = m;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1080 */     return localObject1;
/*      */   }
/*      */ 
/*      */   private void addEvent(EventSetDescriptor paramEventSetDescriptor) {
/* 1084 */     String str = paramEventSetDescriptor.getName();
/* 1085 */     if (paramEventSetDescriptor.getName().equals("propertyChange")) {
/* 1086 */       this.propertyChangeSource = true;
/*      */     }
/* 1088 */     EventSetDescriptor localEventSetDescriptor1 = (EventSetDescriptor)this.events.get(str);
/* 1089 */     if (localEventSetDescriptor1 == null) {
/* 1090 */       this.events.put(str, paramEventSetDescriptor);
/* 1091 */       return;
/*      */     }
/* 1093 */     EventSetDescriptor localEventSetDescriptor2 = new EventSetDescriptor(localEventSetDescriptor1, paramEventSetDescriptor);
/* 1094 */     this.events.put(str, localEventSetDescriptor2);
/*      */   }
/*      */ 
/*      */   private MethodDescriptor[] getTargetMethodInfo()
/*      */   {
/* 1102 */     if (this.methods == null) {
/* 1103 */       this.methods = new HashMap(100);
/*      */     }
/*      */ 
/* 1108 */     MethodDescriptor[] arrayOfMethodDescriptor1 = null;
/* 1109 */     if (this.explicitBeanInfo != null) {
/* 1110 */       arrayOfMethodDescriptor1 = this.explicitBeanInfo.getMethodDescriptors();
/*      */     }
/*      */ 
/* 1113 */     if ((arrayOfMethodDescriptor1 == null) && (this.superBeanInfo != null))
/*      */     {
/* 1115 */       MethodDescriptor[] arrayOfMethodDescriptor2 = this.superBeanInfo.getMethodDescriptors();
/* 1116 */       for (int j = 0; j < arrayOfMethodDescriptor2.length; j++) {
/* 1117 */         addMethod(arrayOfMethodDescriptor2[j]);
/*      */       }
/*      */     }
/*      */ 
/* 1121 */     for (int i = 0; i < this.additionalBeanInfo.length; i++) {
/* 1122 */       MethodDescriptor[] arrayOfMethodDescriptor3 = this.additionalBeanInfo[i].getMethodDescriptors();
/* 1123 */       if (arrayOfMethodDescriptor3 != null) {
/* 1124 */         for (int m = 0; m < arrayOfMethodDescriptor3.length; m++) {
/* 1125 */           addMethod(arrayOfMethodDescriptor3[m]);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1130 */     if (arrayOfMethodDescriptor1 != null)
/*      */     {
/* 1132 */       for (i = 0; i < arrayOfMethodDescriptor1.length; i++) {
/* 1133 */         addMethod(arrayOfMethodDescriptor1[i]);
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 1141 */       localObject = getPublicDeclaredMethods(this.beanClass);
/*      */ 
/* 1144 */       for (int k = 0; k < localObject.length; k++) {
/* 1145 */         Method localMethod = localObject[k];
/* 1146 */         if (localMethod != null)
/*      */         {
/* 1149 */           MethodDescriptor localMethodDescriptor = new MethodDescriptor(localMethod);
/* 1150 */           addMethod(localMethodDescriptor);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1155 */     Object localObject = new MethodDescriptor[this.methods.size()];
/* 1156 */     localObject = (MethodDescriptor[])this.methods.values().toArray((Object[])localObject);
/*      */ 
/* 1158 */     return localObject;
/*      */   }
/*      */ 
/*      */   private void addMethod(MethodDescriptor paramMethodDescriptor)
/*      */   {
/* 1165 */     String str = paramMethodDescriptor.getName();
/*      */ 
/* 1167 */     MethodDescriptor localMethodDescriptor1 = (MethodDescriptor)this.methods.get(str);
/* 1168 */     if (localMethodDescriptor1 == null)
/*      */     {
/* 1170 */       this.methods.put(str, paramMethodDescriptor);
/* 1171 */       return;
/*      */     }
/*      */ 
/* 1177 */     String[] arrayOfString1 = paramMethodDescriptor.getParamNames();
/* 1178 */     String[] arrayOfString2 = localMethodDescriptor1.getParamNames();
/*      */ 
/* 1180 */     int i = 0;
/* 1181 */     if (arrayOfString1.length == arrayOfString2.length) {
/* 1182 */       i = 1;
/* 1183 */       for (int j = 0; j < arrayOfString1.length; j++) {
/* 1184 */         if (arrayOfString1[j] != arrayOfString2[j]) {
/* 1185 */           i = 0;
/* 1186 */           break;
/*      */         }
/*      */       }
/*      */     }
/* 1190 */     if (i != 0) {
/* 1191 */       localObject = new MethodDescriptor(localMethodDescriptor1, paramMethodDescriptor);
/* 1192 */       this.methods.put(str, localObject);
/* 1193 */       return;
/*      */     }
/*      */ 
/* 1199 */     Object localObject = makeQualifiedMethodName(str, arrayOfString1);
/* 1200 */     localMethodDescriptor1 = (MethodDescriptor)this.methods.get(localObject);
/* 1201 */     if (localMethodDescriptor1 == null) {
/* 1202 */       this.methods.put(localObject, paramMethodDescriptor);
/* 1203 */       return;
/*      */     }
/* 1205 */     MethodDescriptor localMethodDescriptor2 = new MethodDescriptor(localMethodDescriptor1, paramMethodDescriptor);
/* 1206 */     this.methods.put(localObject, localMethodDescriptor2);
/*      */   }
/*      */ 
/*      */   private static String makeQualifiedMethodName(String paramString, String[] paramArrayOfString)
/*      */   {
/* 1213 */     StringBuffer localStringBuffer = new StringBuffer(paramString);
/* 1214 */     localStringBuffer.append('=');
/* 1215 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/* 1216 */       localStringBuffer.append(':');
/* 1217 */       localStringBuffer.append(paramArrayOfString[i]);
/*      */     }
/* 1219 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   private int getTargetDefaultEventIndex() {
/* 1223 */     return this.defaultEventIndex;
/*      */   }
/*      */ 
/*      */   private int getTargetDefaultPropertyIndex() {
/* 1227 */     return this.defaultPropertyIndex;
/*      */   }
/*      */ 
/*      */   private BeanDescriptor getTargetBeanDescriptor()
/*      */   {
/* 1232 */     if (this.explicitBeanInfo != null) {
/* 1233 */       BeanDescriptor localBeanDescriptor = this.explicitBeanInfo.getBeanDescriptor();
/* 1234 */       if (localBeanDescriptor != null) {
/* 1235 */         return localBeanDescriptor;
/*      */       }
/*      */     }
/*      */ 
/* 1239 */     return new BeanDescriptor(this.beanClass, findCustomizerClass(this.beanClass));
/*      */   }
/*      */ 
/*      */   private static Class<?> findCustomizerClass(Class<?> paramClass) {
/* 1243 */     String str = paramClass.getName() + "Customizer";
/*      */     try {
/* 1245 */       paramClass = ClassFinder.findClass(str, paramClass.getClassLoader());
/*      */ 
/* 1248 */       if ((Component.class.isAssignableFrom(paramClass)) && (Customizer.class.isAssignableFrom(paramClass))) {
/* 1249 */         return paramClass;
/*      */       }
/*      */     }
/*      */     catch (Exception localException)
/*      */     {
/*      */     }
/* 1255 */     return null;
/*      */   }
/*      */ 
/*      */   private boolean isEventHandler(Method paramMethod)
/*      */   {
/* 1261 */     Type[] arrayOfType = paramMethod.getGenericParameterTypes();
/* 1262 */     if (arrayOfType.length != 1) {
/* 1263 */       return false;
/*      */     }
/* 1265 */     return isSubclass(TypeResolver.erase(TypeResolver.resolveInClass(this.beanClass, arrayOfType[0])), EventObject.class);
/*      */   }
/*      */ 
/*      */   private static Method[] getPublicDeclaredMethods(Class paramClass)
/*      */   {
/* 1274 */     if (!ReflectUtil.isPackageAccessible(paramClass)) {
/* 1275 */       return new Method[0];
/*      */     }
/* 1277 */     synchronized (declaredMethodCache) {
/* 1278 */       Method[] arrayOfMethod = (Method[])declaredMethodCache.get(paramClass);
/* 1279 */       if (arrayOfMethod == null) {
/* 1280 */         arrayOfMethod = paramClass.getMethods();
/* 1281 */         for (int i = 0; i < arrayOfMethod.length; i++) {
/* 1282 */           Method localMethod = arrayOfMethod[i];
/* 1283 */           if (!localMethod.getDeclaringClass().equals(paramClass)) {
/* 1284 */             arrayOfMethod[i] = null;
/*      */           }
/*      */         }
/* 1287 */         declaredMethodCache.put(paramClass, arrayOfMethod);
/*      */       }
/* 1289 */       return arrayOfMethod;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static Method internalFindMethod(Class paramClass, String paramString, int paramInt, Class[] paramArrayOfClass)
/*      */   {
/* 1306 */     Method localMethod = null;
/*      */ 
/* 1308 */     for (Object localObject = paramClass; localObject != null; localObject = ((Class)localObject).getSuperclass()) {
/* 1309 */       Method[] arrayOfMethod = getPublicDeclaredMethods((Class)localObject);
/* 1310 */       for (int j = 0; j < arrayOfMethod.length; j++) {
/* 1311 */         localMethod = arrayOfMethod[j];
/* 1312 */         if (localMethod != null)
/*      */         {
/* 1317 */           if (localMethod.getName().equals(paramString)) {
/* 1318 */             Type[] arrayOfType = localMethod.getGenericParameterTypes();
/* 1319 */             if (arrayOfType.length == paramInt) {
/* 1320 */               if (paramArrayOfClass != null) {
/* 1321 */                 int k = 0;
/* 1322 */                 if (paramInt > 0) {
/* 1323 */                   for (int m = 0; m < paramInt; m++) {
/* 1324 */                     if (TypeResolver.erase(TypeResolver.resolveInClass(paramClass, arrayOfType[m])) != paramArrayOfClass[m]) {
/* 1325 */                       k = 1;
/*      */                     }
/*      */                   }
/*      */ 
/* 1329 */                   if (k != 0) {
/*      */                     continue;
/*      */                   }
/*      */                 }
/*      */               }
/* 1334 */               return localMethod;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1339 */     localMethod = null;
/*      */ 
/* 1344 */     localObject = paramClass.getInterfaces();
/* 1345 */     for (int i = 0; i < localObject.length; i++)
/*      */     {
/* 1349 */       localMethod = internalFindMethod(localObject[i], paramString, paramInt, null);
/* 1350 */       if (localMethod != null) {
/*      */         break;
/*      */       }
/*      */     }
/* 1354 */     return localMethod;
/*      */   }
/*      */ 
/*      */   static Method findMethod(Class paramClass, String paramString, int paramInt)
/*      */   {
/* 1361 */     return findMethod(paramClass, paramString, paramInt, null);
/*      */   }
/*      */ 
/*      */   static Method findMethod(Class paramClass, String paramString, int paramInt, Class[] paramArrayOfClass)
/*      */   {
/* 1378 */     if (paramString == null) {
/* 1379 */       return null;
/*      */     }
/* 1381 */     return internalFindMethod(paramClass, paramString, paramInt, paramArrayOfClass);
/*      */   }
/*      */ 
/*      */   static boolean isSubclass(Class paramClass1, Class paramClass2)
/*      */   {
/* 1394 */     if (paramClass1 == paramClass2) {
/* 1395 */       return true;
/*      */     }
/* 1397 */     if ((paramClass1 == null) || (paramClass2 == null)) {
/* 1398 */       return false;
/*      */     }
/* 1400 */     for (Class localClass = paramClass1; localClass != null; localClass = localClass.getSuperclass()) {
/* 1401 */       if (localClass == paramClass2) {
/* 1402 */         return true;
/*      */       }
/* 1404 */       if (paramClass2.isInterface()) {
/* 1405 */         Class[] arrayOfClass = localClass.getInterfaces();
/* 1406 */         for (int i = 0; i < arrayOfClass.length; i++) {
/* 1407 */           if (isSubclass(arrayOfClass[i], paramClass2)) {
/* 1408 */             return true;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1413 */     return false;
/*      */   }
/*      */ 
/*      */   private boolean throwsException(Method paramMethod, Class paramClass)
/*      */   {
/* 1420 */     Class[] arrayOfClass = paramMethod.getExceptionTypes();
/* 1421 */     for (int i = 0; i < arrayOfClass.length; i++) {
/* 1422 */       if (arrayOfClass[i] == paramClass) {
/* 1423 */         return true;
/*      */       }
/*      */     }
/* 1426 */     return false;
/*      */   }
/*      */ 
/*      */   static Object instantiate(Class paramClass, String paramString)
/*      */     throws InstantiationException, IllegalAccessException, ClassNotFoundException
/*      */   {
/* 1438 */     ClassLoader localClassLoader = paramClass.getClassLoader();
/* 1439 */     Class localClass = ClassFinder.findClass(paramString, localClassLoader);
/* 1440 */     return localClass.newInstance();
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.beans.Introspector
 * JD-Core Version:    0.6.2
 */