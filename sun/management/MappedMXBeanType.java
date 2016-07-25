/*     */ package sun.management;
/*     */ 
/*     */ import com.sun.management.VMOption;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.lang.management.LockInfo;
/*     */ import java.lang.management.MemoryNotificationInfo;
/*     */ import java.lang.management.MemoryUsage;
/*     */ import java.lang.management.MonitorInfo;
/*     */ import java.lang.management.ThreadInfo;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.GenericArrayType;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.WeakHashMap;
/*     */ import javax.management.openmbean.ArrayType;
/*     */ import javax.management.openmbean.CompositeData;
/*     */ import javax.management.openmbean.CompositeDataSupport;
/*     */ import javax.management.openmbean.CompositeType;
/*     */ import javax.management.openmbean.OpenDataException;
/*     */ import javax.management.openmbean.OpenType;
/*     */ import javax.management.openmbean.SimpleType;
/*     */ import javax.management.openmbean.TabularData;
/*     */ import javax.management.openmbean.TabularDataSupport;
/*     */ import javax.management.openmbean.TabularType;
/*     */ 
/*     */ public abstract class MappedMXBeanType
/*     */ {
/*  64 */   private static final WeakHashMap<Type, MappedMXBeanType> convertedTypes = new WeakHashMap();
/*     */   boolean isBasicType;
/*     */   OpenType openType;
/*     */   Class mappedTypeClass;
/*     */   private static final String KEY = "key";
/*     */   private static final String VALUE = "value";
/* 486 */   private static final String[] mapIndexNames = { "key" };
/* 487 */   private static final String[] mapItemNames = { "key", "value" };
/*     */ 
/* 573 */   private static final Class<?> COMPOSITE_DATA_CLASS = CompositeData.class;
/*     */   private static final OpenType inProgress;
/*     */   private static final OpenType[] simpleTypes;
/*     */ 
/*     */   public MappedMXBeanType()
/*     */   {
/*  67 */     this.isBasicType = false;
/*  68 */     this.openType = inProgress;
/*     */   }
/*     */ 
/*     */   static synchronized MappedMXBeanType newMappedType(Type paramType)
/*     */     throws OpenDataException
/*     */   {
/*  74 */     Object localObject1 = null;
/*     */     Object localObject2;
/*  75 */     if ((paramType instanceof Class)) {
/*  76 */       localObject2 = (Class)paramType;
/*  77 */       if (((Class)localObject2).isEnum())
/*  78 */         localObject1 = new EnumMXBeanType((Class)localObject2);
/*  79 */       else if (((Class)localObject2).isArray())
/*  80 */         localObject1 = new ArrayMXBeanType((Class)localObject2);
/*     */       else
/*  82 */         localObject1 = new CompositeDataMXBeanType((Class)localObject2);
/*     */     }
/*  84 */     else if ((paramType instanceof ParameterizedType)) {
/*  85 */       localObject2 = (ParameterizedType)paramType;
/*  86 */       Type localType = ((ParameterizedType)localObject2).getRawType();
/*  87 */       if ((localType instanceof Class)) {
/*  88 */         Class localClass = (Class)localType;
/*  89 */         if (localClass == List.class)
/*  90 */           localObject1 = new ListMXBeanType((ParameterizedType)localObject2);
/*  91 */         else if (localClass == Map.class)
/*  92 */           localObject1 = new MapMXBeanType((ParameterizedType)localObject2);
/*     */       }
/*     */     }
/*  95 */     else if ((paramType instanceof GenericArrayType)) {
/*  96 */       localObject2 = (GenericArrayType)paramType;
/*  97 */       localObject1 = new GenericArrayMXBeanType((GenericArrayType)localObject2);
/*     */     }
/*     */ 
/* 100 */     if (localObject1 == null) {
/* 101 */       throw new OpenDataException(paramType + " is not a supported MXBean type.");
/*     */     }
/*     */ 
/* 104 */     convertedTypes.put(paramType, localObject1);
/* 105 */     return localObject1;
/*     */   }
/*     */ 
/*     */   static synchronized MappedMXBeanType newBasicType(Class paramClass, OpenType paramOpenType)
/*     */     throws OpenDataException
/*     */   {
/* 111 */     BasicMXBeanType localBasicMXBeanType = new BasicMXBeanType(paramClass, paramOpenType);
/* 112 */     convertedTypes.put(paramClass, localBasicMXBeanType);
/* 113 */     return localBasicMXBeanType;
/*     */   }
/*     */ 
/*     */   static synchronized MappedMXBeanType getMappedType(Type paramType) throws OpenDataException
/*     */   {
/* 118 */     MappedMXBeanType localMappedMXBeanType = (MappedMXBeanType)convertedTypes.get(paramType);
/* 119 */     if (localMappedMXBeanType == null) {
/* 120 */       localMappedMXBeanType = newMappedType(paramType);
/*     */     }
/*     */ 
/* 123 */     if ((localMappedMXBeanType.getOpenType() instanceof InProgress)) {
/* 124 */       throw new OpenDataException("Recursive data structure");
/*     */     }
/* 126 */     return localMappedMXBeanType;
/*     */   }
/*     */ 
/*     */   public static synchronized OpenType toOpenType(Type paramType)
/*     */     throws OpenDataException
/*     */   {
/* 132 */     MappedMXBeanType localMappedMXBeanType = getMappedType(paramType);
/* 133 */     return localMappedMXBeanType.getOpenType();
/*     */   }
/*     */ 
/*     */   public static Object toJavaTypeData(Object paramObject, Type paramType) throws OpenDataException, InvalidObjectException
/*     */   {
/* 138 */     if (paramObject == null) {
/* 139 */       return null;
/*     */     }
/* 141 */     MappedMXBeanType localMappedMXBeanType = getMappedType(paramType);
/* 142 */     return localMappedMXBeanType.toJavaTypeData(paramObject);
/*     */   }
/*     */ 
/*     */   public static Object toOpenTypeData(Object paramObject, Type paramType) throws OpenDataException
/*     */   {
/* 147 */     if (paramObject == null) {
/* 148 */       return null;
/*     */     }
/* 150 */     MappedMXBeanType localMappedMXBeanType = getMappedType(paramType);
/* 151 */     return localMappedMXBeanType.toOpenTypeData(paramObject);
/*     */   }
/*     */ 
/*     */   OpenType getOpenType()
/*     */   {
/* 156 */     return this.openType;
/*     */   }
/*     */ 
/*     */   boolean isBasicType() {
/* 160 */     return this.isBasicType;
/*     */   }
/*     */ 
/*     */   String getTypeName()
/*     */   {
/* 167 */     return getMappedTypeClass().getName();
/*     */   }
/*     */ 
/*     */   Class getMappedTypeClass()
/*     */   {
/* 172 */     return this.mappedTypeClass;
/*     */   }
/*     */ 
/*     */   abstract Type getJavaType();
/*     */ 
/*     */   abstract String getName();
/*     */ 
/*     */   abstract Object toOpenTypeData(Object paramObject)
/*     */     throws OpenDataException;
/*     */ 
/*     */   abstract Object toJavaTypeData(Object paramObject)
/*     */     throws OpenDataException, InvalidObjectException;
/*     */ 
/*     */   private static String decapitalize(String paramString)
/*     */   {
/* 848 */     if ((paramString == null) || (paramString.length() == 0)) {
/* 849 */       return paramString;
/*     */     }
/* 851 */     if ((paramString.length() > 1) && (Character.isUpperCase(paramString.charAt(1))) && (Character.isUpperCase(paramString.charAt(0))))
/*     */     {
/* 853 */       return paramString;
/*     */     }
/* 855 */     char[] arrayOfChar = paramString.toCharArray();
/* 856 */     arrayOfChar[0] = Character.toLowerCase(arrayOfChar[0]);
/* 857 */     return new String(arrayOfChar);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     InProgress localInProgress;
/*     */     try
/*     */     {
/* 786 */       localInProgress = new InProgress();
/*     */     }
/*     */     catch (OpenDataException localOpenDataException2) {
/* 789 */       throw new AssertionError(localOpenDataException2);
/*     */     }
/* 791 */     inProgress = localInProgress;
/*     */ 
/* 794 */     simpleTypes = new OpenType[] { SimpleType.BIGDECIMAL, SimpleType.BIGINTEGER, SimpleType.BOOLEAN, SimpleType.BYTE, SimpleType.CHARACTER, SimpleType.DATE, SimpleType.DOUBLE, SimpleType.FLOAT, SimpleType.INTEGER, SimpleType.LONG, SimpleType.OBJECTNAME, SimpleType.SHORT, SimpleType.STRING, SimpleType.VOID };
/*     */     try
/*     */     {
/* 801 */       for (int i = 0; i < simpleTypes.length; i++) { OpenType localOpenType = simpleTypes[i];
/*     */         Class localClass1;
/*     */         try {
/* 805 */           localClass1 = Class.forName(localOpenType.getClassName(), false, String.class.getClassLoader());
/*     */ 
/* 807 */           newBasicType(localClass1, localOpenType);
/*     */         }
/*     */         catch (ClassNotFoundException localClassNotFoundException)
/*     */         {
/* 811 */           throw new AssertionError(localClassNotFoundException);
/*     */         } catch (OpenDataException localOpenDataException3) {
/* 813 */           throw new AssertionError(localOpenDataException3);
/*     */         }
/*     */ 
/* 816 */         if (localClass1.getName().startsWith("java.lang."))
/*     */           try {
/* 818 */             Field localField = localClass1.getField("TYPE");
/* 819 */             Class localClass2 = (Class)localField.get(null);
/* 820 */             newBasicType(localClass2, localOpenType);
/*     */           }
/*     */           catch (NoSuchFieldException localNoSuchFieldException) {
/*     */           }
/*     */           catch (IllegalAccessException localIllegalAccessException) {
/* 825 */             throw new AssertionError(localIllegalAccessException);
/*     */           } }
/*     */     }
/*     */     catch (OpenDataException localOpenDataException1)
/*     */     {
/* 830 */       throw new AssertionError(localOpenDataException1);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class ArrayMXBeanType extends MappedMXBeanType
/*     */   {
/*     */     final Class arrayClass;
/*     */     protected MappedMXBeanType componentType;
/*     */     protected MappedMXBeanType baseElementType;
/*     */ 
/*     */     ArrayMXBeanType(Class paramClass)
/*     */       throws OpenDataException
/*     */     {
/* 277 */       this.arrayClass = paramClass;
/* 278 */       this.componentType = getMappedType(paramClass.getComponentType());
/*     */ 
/* 280 */       StringBuilder localStringBuilder = new StringBuilder();
/* 281 */       Class localClass = paramClass;
/*     */ 
/* 283 */       for (int i = 0; localClass.isArray(); i++) {
/* 284 */         localStringBuilder.append('[');
/* 285 */         localClass = localClass.getComponentType();
/*     */       }
/* 287 */       this.baseElementType = getMappedType(localClass);
/* 288 */       if (localClass.isPrimitive())
/* 289 */         localStringBuilder = new StringBuilder(paramClass.getName());
/*     */       else
/* 291 */         localStringBuilder.append("L" + this.baseElementType.getTypeName() + ";");
/*     */       try
/*     */       {
/* 294 */         this.mappedTypeClass = Class.forName(localStringBuilder.toString());
/*     */       } catch (ClassNotFoundException localClassNotFoundException) {
/* 296 */         OpenDataException localOpenDataException = new OpenDataException("Cannot obtain array class");
/*     */ 
/* 298 */         localOpenDataException.initCause(localClassNotFoundException);
/* 299 */         throw localOpenDataException;
/*     */       }
/*     */ 
/* 302 */       this.openType = new ArrayType(i, this.baseElementType.getOpenType());
/*     */     }
/*     */ 
/*     */     protected ArrayMXBeanType() {
/* 306 */       this.arrayClass = null;
/*     */     }
/*     */ 
/*     */     Type getJavaType() {
/* 310 */       return this.arrayClass;
/*     */     }
/*     */ 
/*     */     String getName() {
/* 314 */       return this.arrayClass.getName();
/*     */     }
/*     */ 
/*     */     Object toOpenTypeData(Object paramObject)
/*     */       throws OpenDataException
/*     */     {
/* 321 */       if (this.baseElementType.isBasicType()) {
/* 322 */         return paramObject;
/*     */       }
/*     */ 
/* 325 */       Object[] arrayOfObject1 = (Object[])paramObject;
/* 326 */       Object[] arrayOfObject2 = (Object[])Array.newInstance(this.componentType.getMappedTypeClass(), arrayOfObject1.length);
/*     */ 
/* 329 */       int i = 0;
/* 330 */       for (Object localObject : arrayOfObject1) {
/* 331 */         if (localObject == null)
/* 332 */           arrayOfObject2[i] = null;
/*     */         else {
/* 334 */           arrayOfObject2[i] = this.componentType.toOpenTypeData(localObject);
/*     */         }
/* 336 */         i++;
/*     */       }
/* 338 */       return arrayOfObject2;
/*     */     }
/*     */ 
/*     */     Object toJavaTypeData(Object paramObject)
/*     */       throws OpenDataException, InvalidObjectException
/*     */     {
/* 347 */       if (this.baseElementType.isBasicType()) {
/* 348 */         return paramObject;
/*     */       }
/*     */ 
/* 351 */       Object[] arrayOfObject1 = (Object[])paramObject;
/* 352 */       Object[] arrayOfObject2 = (Object[])Array.newInstance((Class)this.componentType.getJavaType(), arrayOfObject1.length);
/*     */ 
/* 355 */       int i = 0;
/* 356 */       for (Object localObject : arrayOfObject1) {
/* 357 */         if (localObject == null)
/* 358 */           arrayOfObject2[i] = null;
/*     */         else {
/* 360 */           arrayOfObject2[i] = this.componentType.toJavaTypeData(localObject);
/*     */         }
/* 362 */         i++;
/*     */       }
/* 364 */       return arrayOfObject2;
/*     */     }
/*     */   }
/*     */ 
/*     */   static class BasicMXBeanType extends MappedMXBeanType
/*     */   {
/*     */     final Class basicType;
/*     */ 
/*     */     BasicMXBeanType(Class paramClass, OpenType paramOpenType)
/*     */     {
/* 197 */       this.basicType = paramClass;
/* 198 */       this.openType = paramOpenType;
/* 199 */       this.mappedTypeClass = paramClass;
/* 200 */       this.isBasicType = true;
/*     */     }
/*     */ 
/*     */     Type getJavaType() {
/* 204 */       return this.basicType;
/*     */     }
/*     */ 
/*     */     String getName() {
/* 208 */       return this.basicType.getName();
/*     */     }
/*     */ 
/*     */     Object toOpenTypeData(Object paramObject) throws OpenDataException {
/* 212 */       return paramObject;
/*     */     }
/*     */ 
/*     */     Object toJavaTypeData(Object paramObject)
/*     */       throws OpenDataException, InvalidObjectException
/*     */     {
/* 218 */       return paramObject;
/*     */     }
/*     */   }
/*     */ 
/*     */   static class CompositeDataMXBeanType extends MappedMXBeanType
/*     */   {
/*     */     final Class<?> javaClass;
/*     */     final boolean isCompositeData;
/* 608 */     Method fromMethod = null;
/*     */ 
/*     */     CompositeDataMXBeanType(Class paramClass) throws OpenDataException {
/* 611 */       this.javaClass = paramClass;
/* 612 */       this.mappedTypeClass = MappedMXBeanType.COMPOSITE_DATA_CLASS;
/*     */       try
/*     */       {
/* 616 */         this.fromMethod = ((Method)AccessController.doPrivileged(new PrivilegedExceptionAction() {
/*     */           public Method run() throws NoSuchMethodException {
/* 618 */             return MappedMXBeanType.CompositeDataMXBeanType.this.javaClass.getMethod("from", new Class[] { MappedMXBeanType.COMPOSITE_DATA_CLASS });
/*     */           }
/*     */         }));
/*     */       }
/*     */       catch (PrivilegedActionException localPrivilegedActionException)
/*     */       {
/*     */       }
/*     */ 
/* 626 */       if (MappedMXBeanType.COMPOSITE_DATA_CLASS.isAssignableFrom(paramClass))
/*     */       {
/* 630 */         this.isCompositeData = true;
/* 631 */         this.openType = null;
/*     */       } else {
/* 633 */         this.isCompositeData = false;
/*     */ 
/* 636 */         Method[] arrayOfMethod = (Method[])AccessController.doPrivileged(new PrivilegedAction()
/*     */         {
/*     */           public Method[] run() {
/* 639 */             return MappedMXBeanType.CompositeDataMXBeanType.this.javaClass.getMethods();
/*     */           }
/*     */         });
/* 642 */         ArrayList localArrayList1 = new ArrayList();
/* 643 */         ArrayList localArrayList2 = new ArrayList();
/*     */ 
/* 648 */         for (int i = 0; i < arrayOfMethod.length; i++) {
/* 649 */           Method localMethod = arrayOfMethod[i];
/* 650 */           String str1 = localMethod.getName();
/* 651 */           Type localType = localMethod.getGenericReturnType();
/*     */           String str2;
/* 653 */           if (str1.startsWith("get")) {
/* 654 */             str2 = str1.substring(3); } else {
/* 655 */             if ((!str1.startsWith("is")) || (!(localType instanceof Class)) || ((Class)localType != Boolean.TYPE)) {
/*     */               continue;
/*     */             }
/* 658 */             str2 = str1.substring(2);
/*     */           }
/*     */ 
/* 664 */           if ((!str2.equals("")) && (localMethod.getParameterTypes().length <= 0) && (localType != Void.TYPE) && (!str2.equals("Class")))
/*     */           {
/* 672 */             localArrayList1.add(MappedMXBeanType.decapitalize(str2));
/* 673 */             localArrayList2.add(toOpenType(localType));
/*     */           }
/*     */         }
/* 676 */         String[] arrayOfString = (String[])localArrayList1.toArray(new String[0]);
/* 677 */         this.openType = new CompositeType(paramClass.getName(), paramClass.getName(), arrayOfString, arrayOfString, (OpenType[])localArrayList2.toArray(new OpenType[0]));
/*     */       }
/*     */     }
/*     */ 
/*     */     Type getJavaType()
/*     */     {
/* 686 */       return this.javaClass;
/*     */     }
/*     */ 
/*     */     String getName() {
/* 690 */       return this.javaClass.getName();
/*     */     }
/*     */ 
/*     */     Object toOpenTypeData(Object paramObject) throws OpenDataException {
/* 694 */       if ((paramObject instanceof MemoryUsage)) {
/* 695 */         return MemoryUsageCompositeData.toCompositeData((MemoryUsage)paramObject);
/*     */       }
/*     */ 
/* 698 */       if ((paramObject instanceof ThreadInfo)) {
/* 699 */         return ThreadInfoCompositeData.toCompositeData((ThreadInfo)paramObject);
/*     */       }
/*     */ 
/* 702 */       if ((paramObject instanceof LockInfo)) {
/* 703 */         if ((paramObject instanceof MonitorInfo)) {
/* 704 */           return MonitorInfoCompositeData.toCompositeData((MonitorInfo)paramObject);
/*     */         }
/* 706 */         return LockDataConverter.toLockInfoCompositeData((LockInfo)paramObject);
/*     */       }
/*     */ 
/* 709 */       if ((paramObject instanceof MemoryNotificationInfo)) {
/* 710 */         return MemoryNotifInfoCompositeData.toCompositeData((MemoryNotificationInfo)paramObject);
/*     */       }
/*     */ 
/* 714 */       if ((paramObject instanceof VMOption)) {
/* 715 */         return VMOptionCompositeData.toCompositeData((VMOption)paramObject);
/*     */       }
/*     */ 
/* 718 */       if (this.isCompositeData)
/*     */       {
/* 723 */         CompositeData localCompositeData = (CompositeData)paramObject;
/* 724 */         CompositeType localCompositeType = localCompositeData.getCompositeType();
/* 725 */         String[] arrayOfString = (String[])localCompositeType.keySet().toArray(new String[0]);
/* 726 */         Object[] arrayOfObject = localCompositeData.getAll(arrayOfString);
/* 727 */         return new CompositeDataSupport(localCompositeType, arrayOfString, arrayOfObject);
/*     */       }
/*     */ 
/* 730 */       throw new OpenDataException(this.javaClass.getName() + " is not supported for platform MXBeans");
/*     */     }
/*     */ 
/*     */     Object toJavaTypeData(Object paramObject)
/*     */       throws OpenDataException, InvalidObjectException
/*     */     {
/* 737 */       if (this.fromMethod == null) {
/* 738 */         throw new AssertionError("Does not support data conversion");
/*     */       }
/*     */       try
/*     */       {
/* 742 */         return this.fromMethod.invoke(null, new Object[] { paramObject });
/*     */       }
/*     */       catch (IllegalAccessException localIllegalAccessException) {
/* 745 */         throw new AssertionError(localIllegalAccessException);
/*     */       } catch (InvocationTargetException localInvocationTargetException) {
/* 747 */         OpenDataException localOpenDataException = new OpenDataException("Failed to invoke " + this.fromMethod.getName() + " to convert CompositeData " + " to " + this.javaClass.getName());
/*     */ 
/* 751 */         localOpenDataException.initCause(localInvocationTargetException);
/* 752 */         throw localOpenDataException;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static class EnumMXBeanType extends MappedMXBeanType
/*     */   {
/*     */     final Class enumClass;
/*     */ 
/*     */     EnumMXBeanType(Class paramClass)
/*     */     {
/* 232 */       this.enumClass = paramClass;
/* 233 */       this.openType = SimpleType.STRING;
/* 234 */       this.mappedTypeClass = String.class;
/*     */     }
/*     */ 
/*     */     Type getJavaType() {
/* 238 */       return this.enumClass;
/*     */     }
/*     */ 
/*     */     String getName() {
/* 242 */       return this.enumClass.getName();
/*     */     }
/*     */ 
/*     */     Object toOpenTypeData(Object paramObject) throws OpenDataException {
/* 246 */       return ((Enum)paramObject).name();
/*     */     }
/*     */ 
/*     */     Object toJavaTypeData(Object paramObject) throws OpenDataException, InvalidObjectException
/*     */     {
/*     */       try
/*     */       {
/* 253 */         return Enum.valueOf(this.enumClass, (String)paramObject);
/*     */       }
/*     */       catch (IllegalArgumentException localIllegalArgumentException) {
/* 256 */         InvalidObjectException localInvalidObjectException = new InvalidObjectException("Enum constant named " + (String)paramObject + " is missing");
/*     */ 
/* 259 */         localInvalidObjectException.initCause(localIllegalArgumentException);
/* 260 */         throw localInvalidObjectException;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static class GenericArrayMXBeanType extends MappedMXBeanType.ArrayMXBeanType
/*     */   {
/*     */     final GenericArrayType gtype;
/*     */ 
/*     */     GenericArrayMXBeanType(GenericArrayType paramGenericArrayType)
/*     */       throws OpenDataException
/*     */     {
/* 372 */       this.gtype = paramGenericArrayType;
/* 373 */       this.componentType = getMappedType(paramGenericArrayType.getGenericComponentType());
/*     */ 
/* 375 */       StringBuilder localStringBuilder = new StringBuilder();
/* 376 */       Object localObject = paramGenericArrayType;
/*     */ 
/* 378 */       for (int i = 0; (localObject instanceof GenericArrayType); i++) {
/* 379 */         localStringBuilder.append('[');
/* 380 */         GenericArrayType localGenericArrayType = (GenericArrayType)localObject;
/* 381 */         localObject = localGenericArrayType.getGenericComponentType();
/*     */       }
/* 383 */       this.baseElementType = getMappedType((Type)localObject);
/* 384 */       if (((localObject instanceof Class)) && (((Class)localObject).isPrimitive()))
/* 385 */         localStringBuilder = new StringBuilder(paramGenericArrayType.toString());
/*     */       else
/* 387 */         localStringBuilder.append("L" + this.baseElementType.getTypeName() + ";");
/*     */       try
/*     */       {
/* 390 */         this.mappedTypeClass = Class.forName(localStringBuilder.toString());
/*     */       } catch (ClassNotFoundException localClassNotFoundException) {
/* 392 */         OpenDataException localOpenDataException = new OpenDataException("Cannot obtain array class");
/*     */ 
/* 394 */         localOpenDataException.initCause(localClassNotFoundException);
/* 395 */         throw localOpenDataException;
/*     */       }
/*     */ 
/* 398 */       this.openType = new ArrayType(i, this.baseElementType.getOpenType());
/*     */     }
/*     */ 
/*     */     Type getJavaType() {
/* 402 */       return this.gtype;
/*     */     }
/*     */ 
/*     */     String getName() {
/* 406 */       return this.gtype.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class InProgress extends OpenType
/*     */   {
/*     */     private static final String description = "Marker to detect recursive type use -- internal use only!";
/*     */     private static final long serialVersionUID = -3413063475064374490L;
/*     */ 
/*     */     InProgress()
/*     */       throws OpenDataException
/*     */     {
/* 762 */       super("java.lang.String", "Marker to detect recursive type use -- internal use only!");
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 766 */       return "Marker to detect recursive type use -- internal use only!";
/*     */     }
/*     */ 
/*     */     public int hashCode() {
/* 770 */       return 0;
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject) {
/* 774 */       return false;
/*     */     }
/*     */ 
/*     */     public boolean isValue(Object paramObject) {
/* 778 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */   static class ListMXBeanType extends MappedMXBeanType
/*     */   {
/*     */     final ParameterizedType javaType;
/*     */     final MappedMXBeanType paramType;
/*     */     final String typeName;
/*     */ 
/*     */     ListMXBeanType(ParameterizedType paramParameterizedType)
/*     */       throws OpenDataException
/*     */     {
/* 422 */       this.javaType = paramParameterizedType;
/*     */ 
/* 424 */       Type[] arrayOfType = paramParameterizedType.getActualTypeArguments();
/* 425 */       assert (arrayOfType.length == 1);
/*     */ 
/* 427 */       if (!(arrayOfType[0] instanceof Class)) {
/* 428 */         throw new OpenDataException("Element Type for " + paramParameterizedType + " not supported");
/*     */       }
/*     */ 
/* 431 */       Class localClass = (Class)arrayOfType[0];
/* 432 */       if (localClass.isArray()) {
/* 433 */         throw new OpenDataException("Element Type for " + paramParameterizedType + " not supported");
/*     */       }
/*     */ 
/* 436 */       this.paramType = getMappedType(localClass);
/* 437 */       this.typeName = ("List<" + this.paramType.getName() + ">");
/*     */       try
/*     */       {
/* 440 */         this.mappedTypeClass = Class.forName("[L" + this.paramType.getTypeName() + ";");
/*     */       }
/*     */       catch (ClassNotFoundException localClassNotFoundException) {
/* 443 */         OpenDataException localOpenDataException = new OpenDataException("Array class not found");
/*     */ 
/* 445 */         localOpenDataException.initCause(localClassNotFoundException);
/* 446 */         throw localOpenDataException;
/*     */       }
/* 448 */       this.openType = new ArrayType(1, this.paramType.getOpenType());
/*     */     }
/*     */ 
/*     */     Type getJavaType() {
/* 452 */       return this.javaType;
/*     */     }
/*     */ 
/*     */     String getName() {
/* 456 */       return this.typeName;
/*     */     }
/*     */ 
/*     */     Object toOpenTypeData(Object paramObject) throws OpenDataException {
/* 460 */       List localList = (List)paramObject;
/*     */ 
/* 462 */       Object[] arrayOfObject = (Object[])Array.newInstance(this.paramType.getMappedTypeClass(), localList.size());
/*     */ 
/* 465 */       int i = 0;
/* 466 */       for (Iterator localIterator = localList.iterator(); localIterator.hasNext(); ) { Object localObject = localIterator.next();
/* 467 */         arrayOfObject[(i++)] = this.paramType.toOpenTypeData(localObject);
/*     */       }
/* 469 */       return arrayOfObject;
/*     */     }
/*     */ 
/*     */     Object toJavaTypeData(Object paramObject)
/*     */       throws OpenDataException, InvalidObjectException
/*     */     {
/* 475 */       Object[] arrayOfObject1 = (Object[])paramObject;
/* 476 */       ArrayList localArrayList = new ArrayList(arrayOfObject1.length);
/* 477 */       for (Object localObject : arrayOfObject1) {
/* 478 */         localArrayList.add(this.paramType.toJavaTypeData(localObject));
/*     */       }
/* 480 */       return localArrayList;
/*     */     }
/*     */   }
/*     */ 
/*     */   static class MapMXBeanType extends MappedMXBeanType
/*     */   {
/*     */     final ParameterizedType javaType;
/*     */     final MappedMXBeanType keyType;
/*     */     final MappedMXBeanType valueType;
/*     */     final String typeName;
/*     */ 
/*     */     MapMXBeanType(ParameterizedType paramParameterizedType)
/*     */       throws OpenDataException
/*     */     {
/* 506 */       this.javaType = paramParameterizedType;
/*     */ 
/* 508 */       Type[] arrayOfType = paramParameterizedType.getActualTypeArguments();
/* 509 */       assert (arrayOfType.length == 2);
/* 510 */       this.keyType = getMappedType(arrayOfType[0]);
/* 511 */       this.valueType = getMappedType(arrayOfType[1]);
/*     */ 
/* 515 */       this.typeName = ("Map<" + this.keyType.getName() + "," + this.valueType.getName() + ">");
/*     */ 
/* 517 */       OpenType[] arrayOfOpenType = { this.keyType.getOpenType(), this.valueType.getOpenType() };
/*     */ 
/* 521 */       CompositeType localCompositeType = new CompositeType(this.typeName, this.typeName, MappedMXBeanType.mapItemNames, MappedMXBeanType.mapItemNames, arrayOfOpenType);
/*     */ 
/* 528 */       this.openType = new TabularType(this.typeName, this.typeName, localCompositeType, MappedMXBeanType.mapIndexNames);
/* 529 */       this.mappedTypeClass = TabularData.class;
/*     */     }
/*     */ 
/*     */     Type getJavaType() {
/* 533 */       return this.javaType;
/*     */     }
/*     */ 
/*     */     String getName() {
/* 537 */       return this.typeName;
/*     */     }
/*     */ 
/*     */     Object toOpenTypeData(Object paramObject) throws OpenDataException {
/* 541 */       Map localMap = (Map)paramObject;
/* 542 */       TabularType localTabularType = (TabularType)this.openType;
/* 543 */       TabularDataSupport localTabularDataSupport = new TabularDataSupport(localTabularType);
/* 544 */       CompositeType localCompositeType = localTabularType.getRowType();
/*     */ 
/* 546 */       for (Map.Entry localEntry : localMap.entrySet()) {
/* 547 */         Object localObject1 = this.keyType.toOpenTypeData(localEntry.getKey());
/* 548 */         Object localObject2 = this.valueType.toOpenTypeData(localEntry.getValue());
/* 549 */         CompositeDataSupport localCompositeDataSupport = new CompositeDataSupport(localCompositeType, MappedMXBeanType.mapItemNames, new Object[] { localObject1, localObject2 });
/*     */ 
/* 553 */         localTabularDataSupport.put(localCompositeDataSupport);
/*     */       }
/* 555 */       return localTabularDataSupport;
/*     */     }
/*     */ 
/*     */     Object toJavaTypeData(Object paramObject)
/*     */       throws OpenDataException, InvalidObjectException
/*     */     {
/* 561 */       TabularData localTabularData = (TabularData)paramObject;
/*     */ 
/* 563 */       HashMap localHashMap = new HashMap();
/* 564 */       for (CompositeData localCompositeData : localTabularData.values()) {
/* 565 */         Object localObject1 = this.keyType.toJavaTypeData(localCompositeData.get("key"));
/* 566 */         Object localObject2 = this.valueType.toJavaTypeData(localCompositeData.get("value"));
/* 567 */         localHashMap.put(localObject1, localObject2);
/*     */       }
/* 569 */       return localHashMap;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.MappedMXBeanType
 * JD-Core Version:    0.6.2
 */