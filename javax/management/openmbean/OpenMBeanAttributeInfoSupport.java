/*      */ package javax.management.openmbean;
/*      */ 
/*      */ import com.sun.jmx.remote.util.EnvHelp;
/*      */ import java.lang.reflect.Array;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import javax.management.Descriptor;
/*      */ import javax.management.DescriptorRead;
/*      */ import javax.management.ImmutableDescriptor;
/*      */ import javax.management.MBeanAttributeInfo;
/*      */ import sun.reflect.misc.MethodUtil;
/*      */ import sun.reflect.misc.ReflectUtil;
/*      */ 
/*      */ public class OpenMBeanAttributeInfoSupport extends MBeanAttributeInfo
/*      */   implements OpenMBeanAttributeInfo
/*      */ {
/*      */   static final long serialVersionUID = -4867215622149721849L;
/*      */   private OpenType<?> openType;
/*      */   private final Object defaultValue;
/*      */   private final Set<?> legalValues;
/*      */   private final Comparable<?> minValue;
/*      */   private final Comparable<?> maxValue;
/*   94 */   private transient Integer myHashCode = null;
/*   95 */   private transient String myToString = null;
/*      */ 
/*      */   public OpenMBeanAttributeInfoSupport(String paramString1, String paramString2, OpenType<?> paramOpenType, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
/*      */   {
/*  129 */     this(paramString1, paramString2, paramOpenType, paramBoolean1, paramBoolean2, paramBoolean3, (Descriptor)null);
/*      */   }
/*      */ 
/*      */   public OpenMBeanAttributeInfoSupport(String paramString1, String paramString2, OpenType<?> paramOpenType, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, Descriptor paramDescriptor)
/*      */   {
/*  179 */     super(paramString1, paramOpenType == null ? null : paramOpenType.getClassName(), paramString2, paramBoolean1, paramBoolean2, paramBoolean3, ImmutableDescriptor.union(new Descriptor[] { paramDescriptor, paramOpenType == null ? null : paramOpenType.getDescriptor() }));
/*      */ 
/*  190 */     this.openType = paramOpenType;
/*      */ 
/*  192 */     paramDescriptor = getDescriptor();
/*  193 */     this.defaultValue = valueFrom(paramDescriptor, "defaultValue", paramOpenType);
/*  194 */     this.legalValues = valuesFrom(paramDescriptor, "legalValues", paramOpenType);
/*  195 */     this.minValue = comparableValueFrom(paramDescriptor, "minValue", paramOpenType);
/*  196 */     this.maxValue = comparableValueFrom(paramDescriptor, "maxValue", paramOpenType);
/*      */     try
/*      */     {
/*  199 */       check(this);
/*      */     } catch (OpenDataException localOpenDataException) {
/*  201 */       throw new IllegalArgumentException(localOpenDataException.getMessage(), localOpenDataException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public <T> OpenMBeanAttributeInfoSupport(String paramString1, String paramString2, OpenType<T> paramOpenType, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, T paramT)
/*      */     throws OpenDataException
/*      */   {
/*  252 */     this(paramString1, paramString2, paramOpenType, paramBoolean1, paramBoolean2, paramBoolean3, paramT, (Object[])null);
/*      */   }
/*      */ 
/*      */   public <T> OpenMBeanAttributeInfoSupport(String paramString1, String paramString2, OpenType<T> paramOpenType, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, T paramT, T[] paramArrayOfT)
/*      */     throws OpenDataException
/*      */   {
/*  322 */     this(paramString1, paramString2, paramOpenType, paramBoolean1, paramBoolean2, paramBoolean3, paramT, paramArrayOfT, null, null);
/*      */   }
/*      */ 
/*      */   public <T> OpenMBeanAttributeInfoSupport(String paramString1, String paramString2, OpenType<T> paramOpenType, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, T paramT, Comparable<T> paramComparable1, Comparable<T> paramComparable2)
/*      */     throws OpenDataException
/*      */   {
/*  395 */     this(paramString1, paramString2, paramOpenType, paramBoolean1, paramBoolean2, paramBoolean3, paramT, null, paramComparable1, paramComparable2);
/*      */   }
/*      */ 
/*      */   private <T> OpenMBeanAttributeInfoSupport(String paramString1, String paramString2, OpenType<T> paramOpenType, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, T paramT, T[] paramArrayOfT, Comparable<T> paramComparable1, Comparable<T> paramComparable2)
/*      */     throws OpenDataException
/*      */   {
/*  410 */     super(paramString1, paramOpenType == null ? null : paramOpenType.getClassName(), paramString2, paramBoolean1, paramBoolean2, paramBoolean3, makeDescriptor(paramOpenType, paramT, paramArrayOfT, paramComparable1, paramComparable2));
/*      */ 
/*  419 */     this.openType = paramOpenType;
/*      */ 
/*  421 */     Descriptor localDescriptor = getDescriptor();
/*  422 */     this.defaultValue = paramT;
/*  423 */     this.minValue = paramComparable1;
/*  424 */     this.maxValue = paramComparable2;
/*      */ 
/*  427 */     this.legalValues = ((Set)localDescriptor.getFieldValue("legalValues"));
/*      */ 
/*  429 */     check(this);
/*      */   }
/*      */ 
/*      */   private Object readResolve()
/*      */   {
/*  441 */     if (getDescriptor().getFieldNames().length == 0) {
/*  442 */       OpenType localOpenType = (OpenType)cast(this.openType);
/*  443 */       Set localSet = (Set)cast(this.legalValues);
/*  444 */       Comparable localComparable1 = (Comparable)cast(this.minValue);
/*  445 */       Comparable localComparable2 = (Comparable)cast(this.maxValue);
/*  446 */       return new OpenMBeanAttributeInfoSupport(this.name, this.description, this.openType, isReadable(), isWritable(), isIs(), makeDescriptor(localOpenType, this.defaultValue, localSet, localComparable1, localComparable2));
/*      */     }
/*      */ 
/*  452 */     return this;
/*      */   }
/*      */ 
/*      */   static void check(OpenMBeanParameterInfo paramOpenMBeanParameterInfo) throws OpenDataException {
/*  456 */     OpenType localOpenType = paramOpenMBeanParameterInfo.getOpenType();
/*  457 */     if (localOpenType == null) {
/*  458 */       throw new IllegalArgumentException("OpenType cannot be null");
/*      */     }
/*  460 */     if ((paramOpenMBeanParameterInfo.getName() == null) || (paramOpenMBeanParameterInfo.getName().trim().equals("")))
/*      */     {
/*  462 */       throw new IllegalArgumentException("Name cannot be null or empty");
/*      */     }
/*  464 */     if ((paramOpenMBeanParameterInfo.getDescription() == null) || (paramOpenMBeanParameterInfo.getDescription().trim().equals("")))
/*      */     {
/*  466 */       throw new IllegalArgumentException("Description cannot be null or empty");
/*      */     }
/*      */     Object localObject1;
/*  470 */     if (paramOpenMBeanParameterInfo.hasDefaultValue())
/*      */     {
/*  473 */       if ((localOpenType.isArray()) || ((localOpenType instanceof TabularType))) {
/*  474 */         throw new OpenDataException("Default value not supported for ArrayType and TabularType");
/*      */       }
/*      */ 
/*  478 */       if (!localOpenType.isValue(paramOpenMBeanParameterInfo.getDefaultValue())) {
/*  479 */         localObject1 = "Argument defaultValue's class [\"" + paramOpenMBeanParameterInfo.getDefaultValue().getClass().getName() + "\"] does not match the one defined in openType[\"" + localOpenType.getClassName() + "\"]";
/*      */ 
/*  484 */         throw new OpenDataException((String)localObject1);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  490 */     if ((paramOpenMBeanParameterInfo.hasLegalValues()) && ((paramOpenMBeanParameterInfo.hasMinValue()) || (paramOpenMBeanParameterInfo.hasMaxValue())))
/*      */     {
/*  492 */       throw new OpenDataException("cannot have both legalValue and minValue or maxValue");
/*      */     }
/*      */ 
/*  497 */     if ((paramOpenMBeanParameterInfo.hasMinValue()) && (!localOpenType.isValue(paramOpenMBeanParameterInfo.getMinValue()))) {
/*  498 */       localObject1 = "Type of minValue [" + paramOpenMBeanParameterInfo.getMinValue().getClass().getName() + "] does not match OpenType [" + localOpenType.getClassName() + "]";
/*      */ 
/*  501 */       throw new OpenDataException((String)localObject1);
/*      */     }
/*  503 */     if ((paramOpenMBeanParameterInfo.hasMaxValue()) && (!localOpenType.isValue(paramOpenMBeanParameterInfo.getMaxValue()))) {
/*  504 */       localObject1 = "Type of maxValue [" + paramOpenMBeanParameterInfo.getMaxValue().getClass().getName() + "] does not match OpenType [" + localOpenType.getClassName() + "]";
/*      */ 
/*  507 */       throw new OpenDataException((String)localObject1);
/*      */     }
/*      */ 
/*  512 */     if (paramOpenMBeanParameterInfo.hasDefaultValue()) {
/*  513 */       localObject1 = paramOpenMBeanParameterInfo.getDefaultValue();
/*  514 */       if ((paramOpenMBeanParameterInfo.hasLegalValues()) && (!paramOpenMBeanParameterInfo.getLegalValues().contains(localObject1)))
/*      */       {
/*  516 */         throw new OpenDataException("defaultValue is not contained in legalValues");
/*      */       }
/*      */ 
/*  522 */       if ((paramOpenMBeanParameterInfo.hasMinValue()) && 
/*  523 */         (compare(paramOpenMBeanParameterInfo.getMinValue(), localObject1) > 0)) {
/*  524 */         throw new OpenDataException("minValue cannot be greater than defaultValue");
/*      */       }
/*      */ 
/*  528 */       if ((paramOpenMBeanParameterInfo.hasMaxValue()) && 
/*  529 */         (compare(paramOpenMBeanParameterInfo.getMaxValue(), localObject1) < 0)) {
/*  530 */         throw new OpenDataException("maxValue cannot be less than defaultValue");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  538 */     if (paramOpenMBeanParameterInfo.hasLegalValues())
/*      */     {
/*  540 */       if (((localOpenType instanceof TabularType)) || (localOpenType.isArray())) {
/*  541 */         throw new OpenDataException("Legal values not supported for TabularType and arrays");
/*      */       }
/*      */ 
/*  545 */       for (localObject1 = paramOpenMBeanParameterInfo.getLegalValues().iterator(); ((Iterator)localObject1).hasNext(); ) { Object localObject2 = ((Iterator)localObject1).next();
/*  546 */         if (!localOpenType.isValue(localObject2)) {
/*  547 */           String str = "Element of legalValues [" + localObject2 + "] is not a valid value for the specified openType [" + localOpenType.toString() + "]";
/*      */ 
/*  551 */           throw new OpenDataException(str);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  559 */     if ((paramOpenMBeanParameterInfo.hasMinValue()) && (paramOpenMBeanParameterInfo.hasMaxValue()) && 
/*  560 */       (compare(paramOpenMBeanParameterInfo.getMinValue(), paramOpenMBeanParameterInfo.getMaxValue()) > 0))
/*  561 */       throw new OpenDataException("minValue cannot be greater than maxValue");
/*      */   }
/*      */ 
/*      */   static int compare(Object paramObject1, Object paramObject2)
/*      */   {
/*  570 */     return ((Comparable)paramObject1).compareTo(paramObject2);
/*      */   }
/*      */ 
/*      */   static <T> Descriptor makeDescriptor(OpenType<T> paramOpenType, T paramT, T[] paramArrayOfT, Comparable<T> paramComparable1, Comparable<T> paramComparable2)
/*      */   {
/*  578 */     HashMap localHashMap = new HashMap();
/*  579 */     if (paramT != null)
/*  580 */       localHashMap.put("defaultValue", paramT);
/*  581 */     if (paramArrayOfT != null) {
/*  582 */       Object localObject = new HashSet();
/*  583 */       for (T ? : paramArrayOfT)
/*  584 */         ((Set)localObject).add(?);
/*  585 */       localObject = Collections.unmodifiableSet((Set)localObject);
/*  586 */       localHashMap.put("legalValues", localObject);
/*      */     }
/*  588 */     if (paramComparable1 != null)
/*  589 */       localHashMap.put("minValue", paramComparable1);
/*  590 */     if (paramComparable2 != null)
/*  591 */       localHashMap.put("maxValue", paramComparable2);
/*  592 */     if (localHashMap.isEmpty()) {
/*  593 */       return paramOpenType.getDescriptor();
/*      */     }
/*  595 */     localHashMap.put("openType", paramOpenType);
/*  596 */     return new ImmutableDescriptor(localHashMap);
/*      */   }
/*      */ 
/*      */   static <T> Descriptor makeDescriptor(OpenType<T> paramOpenType, T paramT, Set<T> paramSet, Comparable<T> paramComparable1, Comparable<T> paramComparable2)
/*      */   {
/*      */     Object[] arrayOfObject;
/*  606 */     if (paramSet == null) {
/*  607 */       arrayOfObject = null;
/*      */     } else {
/*  609 */       arrayOfObject = (Object[])cast(new Object[paramSet.size()]);
/*  610 */       paramSet.toArray(arrayOfObject);
/*      */     }
/*  612 */     return makeDescriptor(paramOpenType, paramT, arrayOfObject, paramComparable1, paramComparable2);
/*      */   }
/*      */ 
/*      */   static <T> T valueFrom(Descriptor paramDescriptor, String paramString, OpenType<T> paramOpenType)
/*      */   {
/*  617 */     Object localObject = paramDescriptor.getFieldValue(paramString);
/*  618 */     if (localObject == null)
/*  619 */       return null;
/*      */     try {
/*  621 */       return convertFrom(localObject, paramOpenType);
/*      */     } catch (Exception localException) {
/*  623 */       String str = "Cannot convert descriptor field " + paramString + "  to " + paramOpenType.getTypeName();
/*      */ 
/*  626 */       throw ((IllegalArgumentException)EnvHelp.initCause(new IllegalArgumentException(str), localException));
/*      */     }
/*      */   }
/*      */ 
/*      */   static <T> Set<T> valuesFrom(Descriptor paramDescriptor, String paramString, OpenType<T> paramOpenType)
/*      */   {
/*  632 */     Object localObject1 = paramDescriptor.getFieldValue(paramString);
/*  633 */     if (localObject1 == null)
/*  634 */       return null;
/*      */     Object localObject4;
/*      */     Object localObject2;
/*  636 */     if ((localObject1 instanceof Set)) {
/*  637 */       localObject3 = (Set)localObject1;
/*  638 */       int i = 1;
/*  639 */       for (localObject4 = ((Set)localObject3).iterator(); ((Iterator)localObject4).hasNext(); ) { Object localObject5 = ((Iterator)localObject4).next();
/*  640 */         if (!paramOpenType.isValue(localObject5)) {
/*  641 */           i = 0;
/*  642 */           break;
/*      */         }
/*      */       }
/*  645 */       if (i != 0)
/*  646 */         return (Set)cast(localObject3);
/*  647 */       localObject2 = localObject3;
/*  648 */     } else if ((localObject1 instanceof Object[])) {
/*  649 */       localObject2 = Arrays.asList((Object[])localObject1);
/*      */     } else {
/*  651 */       localObject3 = "Descriptor value for " + paramString + " must be a Set or " + "an array: " + localObject1.getClass().getName();
/*      */ 
/*  654 */       throw new IllegalArgumentException((String)localObject3);
/*      */     }
/*      */ 
/*  657 */     Object localObject3 = new HashSet();
/*  658 */     for (Iterator localIterator = ((Collection)localObject2).iterator(); localIterator.hasNext(); ) { localObject4 = localIterator.next();
/*  659 */       ((Set)localObject3).add(convertFrom(localObject4, paramOpenType)); }
/*  660 */     return localObject3;
/*      */   }
/*      */ 
/*      */   static <T> Comparable<?> comparableValueFrom(Descriptor paramDescriptor, String paramString, OpenType<T> paramOpenType)
/*      */   {
/*  665 */     Object localObject = valueFrom(paramDescriptor, paramString, paramOpenType);
/*  666 */     if ((localObject == null) || ((localObject instanceof Comparable)))
/*  667 */       return (Comparable)localObject;
/*  668 */     String str = "Descriptor field " + paramString + " with value " + localObject + " is not Comparable";
/*      */ 
/*  671 */     throw new IllegalArgumentException(str);
/*      */   }
/*      */ 
/*      */   private static <T> T convertFrom(Object paramObject, OpenType<T> paramOpenType) {
/*  675 */     if (paramOpenType.isValue(paramObject)) {
/*  676 */       Object localObject = cast(paramObject);
/*  677 */       return localObject;
/*      */     }
/*  679 */     return convertFromStrings(paramObject, paramOpenType);
/*      */   }
/*      */ 
/*      */   private static <T> T convertFromStrings(Object paramObject, OpenType<T> paramOpenType) {
/*  683 */     if ((paramOpenType instanceof ArrayType))
/*  684 */       return convertFromStringArray(paramObject, paramOpenType);
/*  685 */     if ((paramObject instanceof String))
/*  686 */       return convertFromString((String)paramObject, paramOpenType);
/*  687 */     String str = "Cannot convert value " + paramObject + " of type " + paramObject.getClass().getName() + " to type " + paramOpenType.getTypeName();
/*      */ 
/*  690 */     throw new IllegalArgumentException(str);
/*      */   }
/*      */ 
/*      */   private static <T> T convertFromString(String paramString, OpenType<T> paramOpenType) {
/*      */     Class localClass;
/*      */     try {
/*  696 */       ReflectUtil.checkPackageAccess(paramOpenType.safeGetClassName());
/*  697 */       localClass = (Class)cast(Class.forName(paramOpenType.safeGetClassName()));
/*      */     } catch (ClassNotFoundException localClassNotFoundException) {
/*  699 */       throw new NoClassDefFoundError(localClassNotFoundException.toString());
/*      */     }
/*      */ 
/*      */     Method localMethod;
/*      */     try
/*      */     {
/*  707 */       localMethod = localClass.getMethod("valueOf", new Class[] { String.class });
/*  708 */       if ((!Modifier.isStatic(localMethod.getModifiers())) || (localMethod.getReturnType() != localClass))
/*      */       {
/*  710 */         localMethod = null;
/*      */       }
/*      */     } catch (NoSuchMethodException localNoSuchMethodException1) { localMethod = null; }
/*      */ 
/*  714 */     if (localMethod != null) {
/*      */       try {
/*  716 */         return localClass.cast(MethodUtil.invoke(localMethod, null, new Object[] { paramString }));
/*      */       } catch (Exception localException1) {
/*  718 */         String str1 = "Could not convert \"" + paramString + "\" using method: " + localMethod;
/*      */ 
/*  720 */         throw new IllegalArgumentException(str1, localException1);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*      */     Constructor localConstructor;
/*      */     try
/*      */     {
/*  729 */       localConstructor = localClass.getConstructor(new Class[] { String.class });
/*      */     } catch (NoSuchMethodException localNoSuchMethodException2) {
/*  731 */       localConstructor = null;
/*      */     }
/*  733 */     if (localConstructor != null) {
/*      */       try {
/*  735 */         return localConstructor.newInstance(new Object[] { paramString });
/*      */       } catch (Exception localException2) {
/*  737 */         String str2 = "Could not convert \"" + paramString + "\" using constructor: " + localConstructor;
/*      */ 
/*  739 */         throw new IllegalArgumentException(str2, localException2);
/*      */       }
/*      */     }
/*      */ 
/*  743 */     throw new IllegalArgumentException("Don't know how to convert string to " + paramOpenType.getTypeName());
/*      */   }
/*      */ 
/*      */   private static <T> T convertFromStringArray(Object paramObject, OpenType<T> paramOpenType)
/*      */   {
/*  756 */     ArrayType localArrayType = (ArrayType)paramOpenType;
/*  757 */     OpenType localOpenType = localArrayType.getElementOpenType();
/*  758 */     int i = localArrayType.getDimension();
/*  759 */     String str = "[";
/*  760 */     for (int j = 1; j < i; j++)
/*  761 */       str = str + "["; Class localClass1;
/*      */     Class localClass2;
/*      */     try {
/*  765 */       localClass1 = Class.forName(str + "Ljava.lang.String;");
/*      */ 
/*  767 */       localClass2 = Class.forName(str + "L" + localOpenType.safeGetClassName() + ";");
/*      */     }
/*      */     catch (ClassNotFoundException localClassNotFoundException)
/*      */     {
/*  771 */       throw new NoClassDefFoundError(localClassNotFoundException.toString());
/*      */     }
/*      */     Object localObject1;
/*  773 */     if (!localClass1.isInstance(paramObject)) {
/*  774 */       localObject1 = "Value for " + i + "-dimensional array of " + localOpenType.getTypeName() + " must be same type or a String " + "array with same dimensions";
/*      */ 
/*  778 */       throw new IllegalArgumentException((String)localObject1);
/*      */     }
/*      */ 
/*  781 */     if (i == 1)
/*  782 */       localObject1 = localOpenType;
/*      */     else {
/*      */       try {
/*  785 */         localObject1 = new ArrayType(i - 1, localOpenType);
/*      */       } catch (OpenDataException localOpenDataException) {
/*  787 */         throw new IllegalArgumentException(localOpenDataException.getMessage(), localOpenDataException);
/*      */       }
/*      */     }
/*      */ 
/*  791 */     int k = Array.getLength(paramObject);
/*  792 */     Object[] arrayOfObject = (Object[])Array.newInstance(localClass2.getComponentType(), k);
/*      */ 
/*  794 */     for (int m = 0; m < k; m++) {
/*  795 */       Object localObject2 = Array.get(paramObject, m);
/*  796 */       Object localObject3 = convertFromStrings(localObject2, (OpenType)localObject1);
/*      */ 
/*  798 */       Array.set(arrayOfObject, m, localObject3);
/*      */     }
/*  800 */     return cast(arrayOfObject);
/*      */   }
/*      */ 
/*      */   static <T> T cast(Object paramObject)
/*      */   {
/*  805 */     return paramObject;
/*      */   }
/*      */ 
/*      */   public OpenType<?> getOpenType()
/*      */   {
/*  813 */     return this.openType;
/*      */   }
/*      */ 
/*      */   public Object getDefaultValue()
/*      */   {
/*  829 */     return this.defaultValue;
/*      */   }
/*      */ 
/*      */   public Set<?> getLegalValues()
/*      */   {
/*  846 */     return this.legalValues;
/*      */   }
/*      */ 
/*      */   public Comparable<?> getMinValue()
/*      */   {
/*  859 */     return this.minValue;
/*      */   }
/*      */ 
/*      */   public Comparable<?> getMaxValue()
/*      */   {
/*  872 */     return this.maxValue;
/*      */   }
/*      */ 
/*      */   public boolean hasDefaultValue()
/*      */   {
/*  883 */     return this.defaultValue != null;
/*      */   }
/*      */ 
/*      */   public boolean hasLegalValues()
/*      */   {
/*  894 */     return this.legalValues != null;
/*      */   }
/*      */ 
/*      */   public boolean hasMinValue()
/*      */   {
/*  905 */     return this.minValue != null;
/*      */   }
/*      */ 
/*      */   public boolean hasMaxValue()
/*      */   {
/*  916 */     return this.maxValue != null;
/*      */   }
/*      */ 
/*      */   public boolean isValue(Object paramObject)
/*      */   {
/*  933 */     return isValue(this, paramObject);
/*      */   }
/*      */ 
/*      */   static boolean isValue(OpenMBeanParameterInfo paramOpenMBeanParameterInfo, Object paramObject)
/*      */   {
/*  938 */     if ((paramOpenMBeanParameterInfo.hasDefaultValue()) && (paramObject == null))
/*  939 */       return true;
/*  940 */     return (paramOpenMBeanParameterInfo.getOpenType().isValue(paramObject)) && ((!paramOpenMBeanParameterInfo.hasLegalValues()) || (paramOpenMBeanParameterInfo.getLegalValues().contains(paramObject))) && ((!paramOpenMBeanParameterInfo.hasMinValue()) || (paramOpenMBeanParameterInfo.getMinValue().compareTo(paramObject) <= 0)) && ((!paramOpenMBeanParameterInfo.hasMaxValue()) || (paramOpenMBeanParameterInfo.getMaxValue().compareTo(paramObject) >= 0));
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject)
/*      */   {
/*  980 */     if (!(paramObject instanceof OpenMBeanAttributeInfo)) {
/*  981 */       return false;
/*      */     }
/*  983 */     OpenMBeanAttributeInfo localOpenMBeanAttributeInfo = (OpenMBeanAttributeInfo)paramObject;
/*      */ 
/*  985 */     return (isReadable() == localOpenMBeanAttributeInfo.isReadable()) && (isWritable() == localOpenMBeanAttributeInfo.isWritable()) && (isIs() == localOpenMBeanAttributeInfo.isIs()) && (equal(this, localOpenMBeanAttributeInfo));
/*      */   }
/*      */ 
/*      */   static boolean equal(OpenMBeanParameterInfo paramOpenMBeanParameterInfo1, OpenMBeanParameterInfo paramOpenMBeanParameterInfo2)
/*      */   {
/*  993 */     if ((paramOpenMBeanParameterInfo1 instanceof DescriptorRead)) {
/*  994 */       if (!(paramOpenMBeanParameterInfo2 instanceof DescriptorRead))
/*  995 */         return false;
/*  996 */       Descriptor localDescriptor1 = ((DescriptorRead)paramOpenMBeanParameterInfo1).getDescriptor();
/*  997 */       Descriptor localDescriptor2 = ((DescriptorRead)paramOpenMBeanParameterInfo2).getDescriptor();
/*  998 */       if (!localDescriptor1.equals(localDescriptor2))
/*  999 */         return false;
/* 1000 */     } else if ((paramOpenMBeanParameterInfo2 instanceof DescriptorRead)) {
/* 1001 */       return false;
/*      */     }
/* 1003 */     return (paramOpenMBeanParameterInfo1.getName().equals(paramOpenMBeanParameterInfo2.getName())) && (paramOpenMBeanParameterInfo1.getOpenType().equals(paramOpenMBeanParameterInfo2.getOpenType())) && (paramOpenMBeanParameterInfo1.hasDefaultValue() ? paramOpenMBeanParameterInfo1.getDefaultValue().equals(paramOpenMBeanParameterInfo2.getDefaultValue()) : !paramOpenMBeanParameterInfo2.hasDefaultValue()) && (paramOpenMBeanParameterInfo1.hasMinValue() ? paramOpenMBeanParameterInfo1.getMinValue().equals(paramOpenMBeanParameterInfo2.getMinValue()) : !paramOpenMBeanParameterInfo2.hasMinValue()) && (paramOpenMBeanParameterInfo1.hasMaxValue() ? paramOpenMBeanParameterInfo1.getMaxValue().equals(paramOpenMBeanParameterInfo2.getMaxValue()) : !paramOpenMBeanParameterInfo2.hasMaxValue()) && (paramOpenMBeanParameterInfo1.hasLegalValues() ? paramOpenMBeanParameterInfo1.getLegalValues().equals(paramOpenMBeanParameterInfo2.getLegalValues()) : !paramOpenMBeanParameterInfo2.hasLegalValues());
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/* 1055 */     if (this.myHashCode == null) {
/* 1056 */       this.myHashCode = Integer.valueOf(hashCode(this));
/*      */     }
/*      */ 
/* 1060 */     return this.myHashCode.intValue();
/*      */   }
/*      */ 
/*      */   static int hashCode(OpenMBeanParameterInfo paramOpenMBeanParameterInfo) {
/* 1064 */     int i = 0;
/* 1065 */     i += paramOpenMBeanParameterInfo.getName().hashCode();
/* 1066 */     i += paramOpenMBeanParameterInfo.getOpenType().hashCode();
/* 1067 */     if (paramOpenMBeanParameterInfo.hasDefaultValue())
/* 1068 */       i += paramOpenMBeanParameterInfo.getDefaultValue().hashCode();
/* 1069 */     if (paramOpenMBeanParameterInfo.hasMinValue())
/* 1070 */       i += paramOpenMBeanParameterInfo.getMinValue().hashCode();
/* 1071 */     if (paramOpenMBeanParameterInfo.hasMaxValue())
/* 1072 */       i += paramOpenMBeanParameterInfo.getMaxValue().hashCode();
/* 1073 */     if (paramOpenMBeanParameterInfo.hasLegalValues())
/* 1074 */       i += paramOpenMBeanParameterInfo.getLegalValues().hashCode();
/* 1075 */     if ((paramOpenMBeanParameterInfo instanceof DescriptorRead))
/* 1076 */       i += ((DescriptorRead)paramOpenMBeanParameterInfo).getDescriptor().hashCode();
/* 1077 */     return i;
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1104 */     if (this.myToString == null) {
/* 1105 */       this.myToString = toString(this);
/*      */     }
/*      */ 
/* 1110 */     return this.myToString;
/*      */   }
/*      */ 
/*      */   static String toString(OpenMBeanParameterInfo paramOpenMBeanParameterInfo) {
/* 1114 */     Object localObject = (paramOpenMBeanParameterInfo instanceof DescriptorRead) ? ((DescriptorRead)paramOpenMBeanParameterInfo).getDescriptor() : null;
/*      */ 
/* 1116 */     return paramOpenMBeanParameterInfo.getClass().getName() + "(name=" + paramOpenMBeanParameterInfo.getName() + ",openType=" + paramOpenMBeanParameterInfo.getOpenType() + ",default=" + paramOpenMBeanParameterInfo.getDefaultValue() + ",minValue=" + paramOpenMBeanParameterInfo.getMinValue() + ",maxValue=" + paramOpenMBeanParameterInfo.getMaxValue() + ",legalValues=" + paramOpenMBeanParameterInfo.getLegalValues() + (localObject == null ? "" : new StringBuilder().append(",descriptor=").append(localObject).toString()) + ")";
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.openmbean.OpenMBeanAttributeInfoSupport
 * JD-Core Version:    0.6.2
 */