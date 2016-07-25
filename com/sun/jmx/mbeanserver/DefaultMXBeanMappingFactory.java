/*      */ package com.sun.jmx.mbeanserver;
/*      */ 
/*      */ import com.sun.jmx.remote.util.EnvHelp;
/*      */ import java.beans.ConstructorProperties;
/*      */ import java.io.InvalidObjectException;
/*      */ import java.lang.annotation.ElementType;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.lang.reflect.Array;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.GenericArrayType;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.lang.reflect.ParameterizedType;
/*      */ import java.lang.reflect.Proxy;
/*      */ import java.lang.reflect.Type;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.BitSet;
/*      */ import java.util.Collection;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ import java.util.SortedMap;
/*      */ import java.util.SortedSet;
/*      */ import java.util.TreeSet;
/*      */ import java.util.WeakHashMap;
/*      */ import javax.management.JMX;
/*      */ import javax.management.ObjectName;
/*      */ import javax.management.openmbean.ArrayType;
/*      */ import javax.management.openmbean.CompositeData;
/*      */ import javax.management.openmbean.CompositeDataInvocationHandler;
/*      */ import javax.management.openmbean.CompositeDataSupport;
/*      */ import javax.management.openmbean.CompositeDataView;
/*      */ import javax.management.openmbean.CompositeType;
/*      */ import javax.management.openmbean.OpenDataException;
/*      */ import javax.management.openmbean.OpenType;
/*      */ import javax.management.openmbean.SimpleType;
/*      */ import javax.management.openmbean.TabularData;
/*      */ import javax.management.openmbean.TabularDataSupport;
/*      */ import javax.management.openmbean.TabularType;
/*      */ import sun.reflect.misc.MethodUtil;
/*      */ import sun.reflect.misc.ReflectUtil;
/*      */ 
/*      */ public class DefaultMXBeanMappingFactory extends MXBeanMappingFactory
/*      */ {
/*      */   private static final Mappings mappings;
/*      */   private static final List<MXBeanMapping> permanentMappings;
/*  351 */   private static final String[] keyArray = { "key" };
/*  352 */   private static final String[] keyValueArray = { "key", "value" };
/*      */ 
/* 1497 */   private static final Map<Type, Type> inProgress = Util.newIdentityHashMap();
/*      */ 
/*      */   static boolean isIdentity(MXBeanMapping paramMXBeanMapping)
/*      */   {
/*  159 */     return ((paramMXBeanMapping instanceof NonNullMXBeanMapping)) && (((NonNullMXBeanMapping)paramMXBeanMapping).isIdentity());
/*      */   }
/*      */ 
/*      */   private static synchronized MXBeanMapping getMapping(Type paramType)
/*      */   {
/*  173 */     WeakReference localWeakReference = (WeakReference)mappings.get(paramType);
/*  174 */     return localWeakReference == null ? null : (MXBeanMapping)localWeakReference.get();
/*      */   }
/*      */ 
/*      */   private static synchronized void putMapping(Type paramType, MXBeanMapping paramMXBeanMapping) {
/*  178 */     WeakReference localWeakReference = new WeakReference(paramMXBeanMapping);
/*      */ 
/*  180 */     mappings.put(paramType, localWeakReference);
/*      */   }
/*      */ 
/*      */   private static synchronized void putPermanentMapping(Type paramType, MXBeanMapping paramMXBeanMapping)
/*      */   {
/*  185 */     putMapping(paramType, paramMXBeanMapping);
/*  186 */     permanentMappings.add(paramMXBeanMapping);
/*      */   }
/*      */ 
/*      */   public synchronized MXBeanMapping mappingForType(Type paramType, MXBeanMappingFactory paramMXBeanMappingFactory)
/*      */     throws OpenDataException
/*      */   {
/*  244 */     if (inProgress.containsKey(paramType)) {
/*  245 */       throw new OpenDataException("Recursive data structure, including " + MXBeanIntrospector.typeName(paramType));
/*      */     }
/*      */ 
/*  251 */     MXBeanMapping localMXBeanMapping = getMapping(paramType);
/*  252 */     if (localMXBeanMapping != null) {
/*  253 */       return localMXBeanMapping;
/*      */     }
/*  255 */     inProgress.put(paramType, paramType);
/*      */     try {
/*  257 */       localMXBeanMapping = makeMapping(paramType, paramMXBeanMappingFactory);
/*      */     } catch (OpenDataException localOpenDataException) {
/*  259 */       throw openDataException("Cannot convert type: " + MXBeanIntrospector.typeName(paramType), localOpenDataException);
/*      */     } finally {
/*  261 */       inProgress.remove(paramType);
/*      */     }
/*      */ 
/*  264 */     putMapping(paramType, localMXBeanMapping);
/*  265 */     return localMXBeanMapping;
/*      */   }
/*      */ 
/*      */   private MXBeanMapping makeMapping(Type paramType, MXBeanMappingFactory paramMXBeanMappingFactory)
/*      */     throws OpenDataException
/*      */   {
/*      */     Object localObject;
/*  274 */     if ((paramType instanceof GenericArrayType)) {
/*  275 */       localObject = ((GenericArrayType)paramType).getGenericComponentType();
/*      */ 
/*  277 */       return makeArrayOrCollectionMapping(paramType, (Type)localObject, paramMXBeanMappingFactory);
/*  278 */     }if ((paramType instanceof Class)) {
/*  279 */       localObject = (Class)paramType;
/*  280 */       if (((Class)localObject).isEnum())
/*      */       {
/*  284 */         return makeEnumMapping((Class)localObject, ElementType.class);
/*  285 */       }if (((Class)localObject).isArray()) {
/*  286 */         Class localClass = ((Class)localObject).getComponentType();
/*  287 */         return makeArrayOrCollectionMapping((Type)localObject, localClass, paramMXBeanMappingFactory);
/*      */       }
/*  289 */       if (JMX.isMXBeanInterface((Class)localObject)) {
/*  290 */         return makeMXBeanRefMapping((Type)localObject);
/*      */       }
/*  292 */       return makeCompositeMapping((Class)localObject, paramMXBeanMappingFactory);
/*      */     }
/*  294 */     if ((paramType instanceof ParameterizedType)) {
/*  295 */       return makeParameterizedTypeMapping((ParameterizedType)paramType, paramMXBeanMappingFactory);
/*      */     }
/*      */ 
/*  298 */     throw new OpenDataException("Cannot map type: " + paramType);
/*      */   }
/*      */ 
/*      */   private static <T extends Enum<T>> MXBeanMapping makeEnumMapping(Class<?> paramClass, Class<T> paramClass1)
/*      */   {
/*  303 */     ReflectUtil.checkPackageAccess(paramClass);
/*  304 */     return new EnumMapping((Class)Util.cast(paramClass));
/*      */   }
/*      */ 
/*      */   private MXBeanMapping makeArrayOrCollectionMapping(Type paramType1, Type paramType2, MXBeanMappingFactory paramMXBeanMappingFactory)
/*      */     throws OpenDataException
/*      */   {
/*  317 */     MXBeanMapping localMXBeanMapping = paramMXBeanMappingFactory.mappingForType(paramType2, paramMXBeanMappingFactory);
/*  318 */     OpenType localOpenType = localMXBeanMapping.getOpenType();
/*  319 */     ArrayType localArrayType = ArrayType.getArrayType(localOpenType);
/*  320 */     Class localClass1 = localMXBeanMapping.getOpenClass();
/*      */     String str;
/*  324 */     if (localClass1.isArray())
/*  325 */       str = "[" + localClass1.getName();
/*      */     else
/*  327 */       str = "[L" + localClass1.getName() + ";"; Class localClass2;
/*      */     try {
/*  329 */       localClass2 = Class.forName(str);
/*      */     } catch (ClassNotFoundException localClassNotFoundException) {
/*  331 */       throw openDataException("Cannot obtain array class", localClassNotFoundException);
/*      */     }
/*      */ 
/*  334 */     if ((paramType1 instanceof ParameterizedType)) {
/*  335 */       return new CollectionMapping(paramType1, localArrayType, localClass2, localMXBeanMapping);
/*      */     }
/*      */ 
/*  339 */     if (isIdentity(localMXBeanMapping)) {
/*  340 */       return new IdentityMapping(paramType1, localArrayType);
/*      */     }
/*      */ 
/*  343 */     return new ArrayMapping(paramType1, localArrayType, localClass2, localMXBeanMapping);
/*      */   }
/*      */ 
/*      */   private MXBeanMapping makeTabularMapping(Type paramType1, boolean paramBoolean, Type paramType2, Type paramType3, MXBeanMappingFactory paramMXBeanMappingFactory)
/*      */     throws OpenDataException
/*      */   {
/*  360 */     String str = MXBeanIntrospector.typeName(paramType1);
/*  361 */     MXBeanMapping localMXBeanMapping1 = paramMXBeanMappingFactory.mappingForType(paramType2, paramMXBeanMappingFactory);
/*  362 */     MXBeanMapping localMXBeanMapping2 = paramMXBeanMappingFactory.mappingForType(paramType3, paramMXBeanMappingFactory);
/*  363 */     OpenType localOpenType1 = localMXBeanMapping1.getOpenType();
/*  364 */     OpenType localOpenType2 = localMXBeanMapping2.getOpenType();
/*  365 */     CompositeType localCompositeType = new CompositeType(str, str, keyValueArray, keyValueArray, new OpenType[] { localOpenType1, localOpenType2 });
/*      */ 
/*  371 */     TabularType localTabularType = new TabularType(str, str, localCompositeType, keyArray);
/*      */ 
/*  373 */     return new TabularMapping(paramType1, paramBoolean, localTabularType, localMXBeanMapping1, localMXBeanMapping2);
/*      */   }
/*      */ 
/*      */   private MXBeanMapping makeParameterizedTypeMapping(ParameterizedType paramParameterizedType, MXBeanMappingFactory paramMXBeanMappingFactory)
/*      */     throws OpenDataException
/*      */   {
/*  387 */     Type localType = paramParameterizedType.getRawType();
/*      */ 
/*  389 */     if ((localType instanceof Class)) {
/*  390 */       Class localClass = (Class)localType;
/*  391 */       if ((localClass == List.class) || (localClass == Set.class) || (localClass == SortedSet.class)) {
/*  392 */         Type[] arrayOfType1 = paramParameterizedType.getActualTypeArguments();
/*  393 */         assert (arrayOfType1.length == 1);
/*  394 */         if (localClass == SortedSet.class)
/*  395 */           mustBeComparable(localClass, arrayOfType1[0]);
/*  396 */         return makeArrayOrCollectionMapping(paramParameterizedType, arrayOfType1[0], paramMXBeanMappingFactory);
/*      */       }
/*  398 */       boolean bool = localClass == SortedMap.class;
/*  399 */       if ((localClass == Map.class) || (bool)) {
/*  400 */         Type[] arrayOfType2 = paramParameterizedType.getActualTypeArguments();
/*  401 */         assert (arrayOfType2.length == 2);
/*  402 */         if (bool)
/*  403 */           mustBeComparable(localClass, arrayOfType2[0]);
/*  404 */         return makeTabularMapping(paramParameterizedType, bool, arrayOfType2[0], arrayOfType2[1], paramMXBeanMappingFactory);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  409 */     throw new OpenDataException("Cannot convert type: " + paramParameterizedType);
/*      */   }
/*      */ 
/*      */   private static MXBeanMapping makeMXBeanRefMapping(Type paramType) throws OpenDataException
/*      */   {
/*  414 */     return new MXBeanRefMapping(paramType);
/*      */   }
/*      */ 
/*      */   private MXBeanMapping makeCompositeMapping(Class<?> paramClass, MXBeanMappingFactory paramMXBeanMappingFactory)
/*      */     throws OpenDataException
/*      */   {
/*  424 */     int i = (paramClass.getName().equals("com.sun.management.GcInfo")) && (paramClass.getClassLoader() == null) ? 1 : 0;
/*      */ 
/*  428 */     ReflectUtil.checkPackageAccess(paramClass);
/*  429 */     List localList = MBeanAnalyzer.eliminateCovariantMethods(Arrays.asList(paramClass.getMethods()));
/*      */ 
/*  431 */     SortedMap localSortedMap = Util.newSortedMap();
/*      */ 
/*  436 */     for (Iterator localIterator = localList.iterator(); localIterator.hasNext(); ) { localObject1 = (Method)localIterator.next();
/*  437 */       localObject2 = propertyName((Method)localObject1);
/*      */ 
/*  439 */       if ((localObject2 != null) && (
/*  441 */         (i == 0) || (!((String)localObject2).equals("CompositeType"))))
/*      */       {
/*  444 */         localObject3 = (Method)localSortedMap.put(decapitalize((String)localObject2), localObject1);
/*      */ 
/*  447 */         if (localObject3 != null) {
/*  448 */           String str = "Class " + paramClass.getName() + " has method name clash: " + ((Method)localObject3).getName() + ", " + ((Method)localObject1).getName();
/*      */ 
/*  451 */           throw new OpenDataException(str);
/*      */         }
/*      */       }
/*      */     }
/*  455 */     int j = localSortedMap.size();
/*      */ 
/*  457 */     if (j == 0) {
/*  458 */       throw new OpenDataException("Can't map " + paramClass.getName() + " to an open data type");
/*      */     }
/*      */ 
/*  462 */     Object localObject1 = new Method[j];
/*  463 */     Object localObject2 = new String[j];
/*  464 */     Object localObject3 = new OpenType[j];
/*  465 */     int k = 0;
/*  466 */     for (Object localObject4 = localSortedMap.entrySet().iterator(); ((Iterator)localObject4).hasNext(); ) { Map.Entry localEntry = (Map.Entry)((Iterator)localObject4).next();
/*  467 */       localObject2[k] = ((String)localEntry.getKey());
/*  468 */       Method localMethod = (Method)localEntry.getValue();
/*  469 */       localObject1[k] = localMethod;
/*  470 */       Type localType = localMethod.getGenericReturnType();
/*  471 */       localObject3[k] = paramMXBeanMappingFactory.mappingForType(localType, paramMXBeanMappingFactory).getOpenType();
/*  472 */       k++;
/*      */     }
/*      */ 
/*  475 */     localObject4 = new CompositeType(paramClass.getName(), paramClass.getName(), (String[])localObject2, (String[])localObject2, (OpenType[])localObject3);
/*      */ 
/*  482 */     return new CompositeMapping(paramClass, (CompositeType)localObject4, (String[])localObject2, (Method[])localObject1, paramMXBeanMappingFactory);
/*      */   }
/*      */ 
/*      */   static InvalidObjectException invalidObjectException(String paramString, Throwable paramThrowable)
/*      */   {
/* 1415 */     return (InvalidObjectException)EnvHelp.initCause(new InvalidObjectException(paramString), paramThrowable);
/*      */   }
/*      */ 
/*      */   static InvalidObjectException invalidObjectException(Throwable paramThrowable) {
/* 1419 */     return invalidObjectException(paramThrowable.getMessage(), paramThrowable);
/*      */   }
/*      */ 
/*      */   static OpenDataException openDataException(String paramString, Throwable paramThrowable) {
/* 1423 */     return (OpenDataException)EnvHelp.initCause(new OpenDataException(paramString), paramThrowable);
/*      */   }
/*      */ 
/*      */   static OpenDataException openDataException(Throwable paramThrowable) {
/* 1427 */     return openDataException(paramThrowable.getMessage(), paramThrowable);
/*      */   }
/*      */ 
/*      */   static void mustBeComparable(Class<?> paramClass, Type paramType) throws OpenDataException
/*      */   {
/* 1432 */     if ((!(paramType instanceof Class)) || (!Comparable.class.isAssignableFrom((Class)paramType)))
/*      */     {
/* 1434 */       String str = "Parameter class " + paramType + " of " + paramClass.getName() + " does not implement " + Comparable.class.getName();
/*      */ 
/* 1438 */       throw new OpenDataException(str);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static String decapitalize(String paramString)
/*      */   {
/* 1456 */     if ((paramString == null) || (paramString.length() == 0)) {
/* 1457 */       return paramString;
/*      */     }
/* 1459 */     int i = Character.offsetByCodePoints(paramString, 0, 1);
/*      */ 
/* 1461 */     if ((i < paramString.length()) && (Character.isUpperCase(paramString.codePointAt(i))))
/*      */     {
/* 1463 */       return paramString;
/* 1464 */     }return paramString.substring(0, i).toLowerCase() + paramString.substring(i);
/*      */   }
/*      */ 
/*      */   static String capitalize(String paramString)
/*      */   {
/* 1475 */     if ((paramString == null) || (paramString.length() == 0))
/* 1476 */       return paramString;
/* 1477 */     int i = paramString.offsetByCodePoints(0, 1);
/* 1478 */     return paramString.substring(0, i).toUpperCase() + paramString.substring(i);
/*      */   }
/*      */ 
/*      */   public static String propertyName(Method paramMethod)
/*      */   {
/* 1483 */     String str1 = null;
/* 1484 */     String str2 = paramMethod.getName();
/* 1485 */     if (str2.startsWith("get"))
/* 1486 */       str1 = str2.substring(3);
/* 1487 */     else if ((str2.startsWith("is")) && (paramMethod.getReturnType() == Boolean.TYPE))
/* 1488 */       str1 = str2.substring(2);
/* 1489 */     if ((str1 == null) || (str1.length() == 0) || (paramMethod.getParameterTypes().length > 0) || (paramMethod.getReturnType() == Void.TYPE) || (str2.equals("getClass")))
/*      */     {
/* 1493 */       return null;
/* 1494 */     }return str1;
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  166 */     mappings = new Mappings(null);
/*      */ 
/*  170 */     permanentMappings = Util.newList();
/*      */ 
/*  192 */     OpenType[] arrayOfOpenType = { SimpleType.BIGDECIMAL, SimpleType.BIGINTEGER, SimpleType.BOOLEAN, SimpleType.BYTE, SimpleType.CHARACTER, SimpleType.DATE, SimpleType.DOUBLE, SimpleType.FLOAT, SimpleType.INTEGER, SimpleType.LONG, SimpleType.OBJECTNAME, SimpleType.SHORT, SimpleType.STRING, SimpleType.VOID };
/*      */ 
/*  198 */     for (int i = 0; i < arrayOfOpenType.length; i++) { OpenType localOpenType = arrayOfOpenType[i];
/*      */       Class localClass1;
/*      */       try {
/*  202 */         localClass1 = Class.forName(localOpenType.getClassName(), false, ObjectName.class.getClassLoader());
/*      */       }
/*      */       catch (ClassNotFoundException localClassNotFoundException)
/*      */       {
/*  206 */         throw new Error(localClassNotFoundException);
/*      */       }
/*  208 */       IdentityMapping localIdentityMapping1 = new IdentityMapping(localClass1, localOpenType);
/*  209 */       putPermanentMapping(localClass1, localIdentityMapping1);
/*      */ 
/*  211 */       if (localClass1.getName().startsWith("java.lang."))
/*      */         try {
/*  213 */           Field localField = localClass1.getField("TYPE");
/*  214 */           Class localClass2 = (Class)localField.get(null);
/*  215 */           IdentityMapping localIdentityMapping2 = new IdentityMapping(localClass2, localOpenType);
/*      */ 
/*  217 */           putPermanentMapping(localClass2, localIdentityMapping2);
/*  218 */           if (localClass2 != Void.TYPE) {
/*  219 */             Class localClass3 = Array.newInstance(localClass2, 0).getClass();
/*      */ 
/*  221 */             ArrayType localArrayType = ArrayType.getPrimitiveArrayType(localClass3);
/*      */ 
/*  223 */             IdentityMapping localIdentityMapping3 = new IdentityMapping(localClass3, localArrayType);
/*      */ 
/*  226 */             putPermanentMapping(localClass3, localIdentityMapping3);
/*      */           }
/*      */         }
/*      */         catch (NoSuchFieldException localNoSuchFieldException)
/*      */         {
/*      */         }
/*      */         catch (IllegalAccessException localIllegalAccessException) {
/*  233 */           if (!$assertionsDisabled) throw new AssertionError();
/*      */         }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static final class ArrayMapping extends DefaultMXBeanMappingFactory.NonNullMXBeanMapping
/*      */   {
/*      */     private final MXBeanMapping elementMapping;
/*      */ 
/*      */     ArrayMapping(Type paramType, ArrayType<?> paramArrayType, Class<?> paramClass, MXBeanMapping paramMXBeanMapping)
/*      */     {
/*  547 */       super(paramArrayType);
/*  548 */       this.elementMapping = paramMXBeanMapping;
/*      */     }
/*      */ 
/*      */     final Object toNonNullOpenValue(Object paramObject)
/*      */       throws OpenDataException
/*      */     {
/*  554 */       Object[] arrayOfObject1 = (Object[])paramObject;
/*  555 */       int i = arrayOfObject1.length;
/*  556 */       Object[] arrayOfObject2 = (Object[])Array.newInstance(getOpenClass().getComponentType(), i);
/*      */ 
/*  558 */       for (int j = 0; j < i; j++)
/*  559 */         arrayOfObject2[j] = this.elementMapping.toOpenValue(arrayOfObject1[j]);
/*  560 */       return arrayOfObject2;
/*      */     }
/*      */ 
/*      */     final Object fromNonNullOpenValue(Object paramObject)
/*      */       throws InvalidObjectException
/*      */     {
/*  566 */       Object[] arrayOfObject1 = (Object[])paramObject;
/*  567 */       Type localType = getJavaType();
/*      */       Object localObject;
/*  570 */       if ((localType instanceof GenericArrayType)) {
/*  571 */         localObject = ((GenericArrayType)localType).getGenericComponentType();
/*      */       }
/*  573 */       else if (((localType instanceof Class)) && (((Class)localType).isArray()))
/*      */       {
/*  575 */         localObject = ((Class)localType).getComponentType();
/*      */       }
/*  577 */       else throw new IllegalArgumentException("Not an array: " + localType);
/*      */ 
/*  580 */       Object[] arrayOfObject2 = (Object[])Array.newInstance((Class)localObject, arrayOfObject1.length);
/*      */ 
/*  582 */       for (int i = 0; i < arrayOfObject1.length; i++)
/*  583 */         arrayOfObject2[i] = this.elementMapping.fromOpenValue(arrayOfObject1[i]);
/*  584 */       return arrayOfObject2;
/*      */     }
/*      */ 
/*      */     public void checkReconstructible() throws InvalidObjectException {
/*  588 */       this.elementMapping.checkReconstructible();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static final class CollectionMapping extends DefaultMXBeanMappingFactory.NonNullMXBeanMapping
/*      */   {
/*      */     private final Class<? extends Collection<?>> collectionClass;
/*      */     private final MXBeanMapping elementMapping;
/*      */ 
/*      */     CollectionMapping(Type paramType, ArrayType<?> paramArrayType, Class<?> paramClass, MXBeanMapping paramMXBeanMapping)
/*      */     {
/*  604 */       super(paramArrayType);
/*  605 */       this.elementMapping = paramMXBeanMapping;
/*      */ 
/*  611 */       Type localType = ((ParameterizedType)paramType).getRawType();
/*  612 */       Class localClass = (Class)localType;
/*      */       Object localObject;
/*  614 */       if (localClass == List.class) {
/*  615 */         localObject = ArrayList.class;
/*  616 */       } else if (localClass == Set.class) {
/*  617 */         localObject = HashSet.class;
/*  618 */       } else if (localClass == SortedSet.class) {
/*  619 */         localObject = TreeSet.class;
/*      */       } else {
/*  621 */         if (!$assertionsDisabled) throw new AssertionError();
/*  622 */         localObject = null;
/*      */       }
/*  624 */       this.collectionClass = ((Class)Util.cast(localObject));
/*      */     }
/*      */ 
/*      */     final Object toNonNullOpenValue(Object paramObject)
/*      */       throws OpenDataException
/*      */     {
/*  630 */       Collection localCollection = (Collection)paramObject;
/*  631 */       if ((localCollection instanceof SortedSet)) {
/*  632 */         localObject1 = ((SortedSet)localCollection).comparator();
/*      */ 
/*  634 */         if (localObject1 != null) {
/*  635 */           String str = "Cannot convert SortedSet with non-null comparator: " + localObject1;
/*      */ 
/*  638 */           throw DefaultMXBeanMappingFactory.openDataException(str, new IllegalArgumentException(str));
/*      */         }
/*      */       }
/*  641 */       Object localObject1 = (Object[])Array.newInstance(getOpenClass().getComponentType(), localCollection.size());
/*      */ 
/*  644 */       int i = 0;
/*  645 */       for (Iterator localIterator = localCollection.iterator(); localIterator.hasNext(); ) { Object localObject2 = localIterator.next();
/*  646 */         localObject1[(i++)] = this.elementMapping.toOpenValue(localObject2); }
/*  647 */       return localObject1;
/*      */     }
/*      */ 
/*      */     final Object fromNonNullOpenValue(Object paramObject) throws InvalidObjectException
/*      */     {
/*  653 */       Object[] arrayOfObject1 = (Object[])paramObject;
/*      */       Collection localCollection;
/*      */       try {
/*  656 */         localCollection = (Collection)Util.cast(this.collectionClass.newInstance());
/*      */       } catch (Exception localException) {
/*  658 */         throw DefaultMXBeanMappingFactory.invalidObjectException("Cannot create collection", localException);
/*      */       }
/*  660 */       for (Object localObject1 : arrayOfObject1) {
/*  661 */         Object localObject2 = this.elementMapping.fromOpenValue(localObject1);
/*  662 */         if (!localCollection.add(localObject2)) {
/*  663 */           String str = "Could not add " + localObject1 + " to " + this.collectionClass.getName() + " (duplicate set element?)";
/*      */ 
/*  667 */           throw new InvalidObjectException(str);
/*      */         }
/*      */       }
/*  670 */       return localCollection;
/*      */     }
/*      */ 
/*      */     public void checkReconstructible() throws InvalidObjectException {
/*  674 */       this.elementMapping.checkReconstructible();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static abstract class CompositeBuilder
/*      */   {
/*      */     private final Class<?> targetClass;
/*      */     private final String[] itemNames;
/*      */ 
/*      */     CompositeBuilder(Class<?> paramClass, String[] paramArrayOfString)
/*      */     {
/*  932 */       this.targetClass = paramClass;
/*  933 */       this.itemNames = paramArrayOfString;
/*      */     }
/*      */ 
/*      */     Class<?> getTargetClass() {
/*  937 */       return this.targetClass;
/*      */     }
/*      */ 
/*      */     String[] getItemNames() {
/*  941 */       return this.itemNames;
/*      */     }
/*      */ 
/*      */     abstract String applicable(Method[] paramArrayOfMethod)
/*      */       throws InvalidObjectException;
/*      */ 
/*      */     Throwable possibleCause()
/*      */     {
/*  959 */       return null;
/*      */     }
/*      */ 
/*      */     abstract Object fromCompositeData(CompositeData paramCompositeData, String[] paramArrayOfString, MXBeanMapping[] paramArrayOfMXBeanMapping)
/*      */       throws InvalidObjectException;
/*      */   }
/*      */ 
/*      */   private static class CompositeBuilderCheckGetters extends DefaultMXBeanMappingFactory.CompositeBuilder
/*      */   {
/*      */     private final MXBeanMapping[] getterConverters;
/*      */     private Throwable possibleCause;
/*      */ 
/*      */     CompositeBuilderCheckGetters(Class<?> paramClass, String[] paramArrayOfString, MXBeanMapping[] paramArrayOfMXBeanMapping)
/*      */     {
/* 1040 */       super(paramArrayOfString);
/* 1041 */       this.getterConverters = paramArrayOfMXBeanMapping;
/*      */     }
/*      */ 
/*      */     String applicable(Method[] paramArrayOfMethod) {
/* 1045 */       for (int i = 0; i < paramArrayOfMethod.length; i++) {
/*      */         try {
/* 1047 */           this.getterConverters[i].checkReconstructible();
/*      */         } catch (InvalidObjectException localInvalidObjectException) {
/* 1049 */           this.possibleCause = localInvalidObjectException;
/* 1050 */           return "method " + paramArrayOfMethod[i].getName() + " returns type " + "that cannot be mapped back from OpenData";
/*      */         }
/*      */       }
/*      */ 
/* 1054 */       return "";
/*      */     }
/*      */ 
/*      */     Throwable possibleCause()
/*      */     {
/* 1059 */       return this.possibleCause;
/*      */     }
/*      */ 
/*      */     final Object fromCompositeData(CompositeData paramCompositeData, String[] paramArrayOfString, MXBeanMapping[] paramArrayOfMXBeanMapping)
/*      */     {
/* 1065 */       throw new Error();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static final class CompositeBuilderViaConstructor extends DefaultMXBeanMappingFactory.CompositeBuilder
/*      */   {
/*      */     private List<Constr> annotatedConstructors;
/*      */ 
/*      */     CompositeBuilderViaConstructor(Class<?> paramClass, String[] paramArrayOfString)
/*      */     {
/* 1140 */       super(paramArrayOfString);
/*      */     }
/*      */ 
/*      */     String applicable(Method[] paramArrayOfMethod) throws InvalidObjectException
/*      */     {
/* 1145 */       ConstructorProperties localConstructorProperties = ConstructorProperties.class;
/*      */ 
/* 1147 */       Class localClass = getTargetClass();
/* 1148 */       Constructor[] arrayOfConstructor = localClass.getConstructors();
/*      */ 
/* 1151 */       List localList = Util.newList();
/* 1152 */       for (localObject2 : arrayOfConstructor) {
/* 1153 */         if ((Modifier.isPublic(((Constructor)localObject2).getModifiers())) && (((Constructor)localObject2).getAnnotation(localConstructorProperties) != null))
/*      */         {
/* 1155 */           localList.add(localObject2);
/*      */         }
/*      */       }
/* 1158 */       if (localList.isEmpty()) {
/* 1159 */         return "no constructor has @ConstructorProperties annotation";
/*      */       }
/* 1161 */       this.annotatedConstructors = Util.newList();
/*      */ 
/* 1167 */       ??? = Util.newMap();
/* 1168 */       String[] arrayOfString1 = getItemNames();
/* 1169 */       for (??? = 0; ??? < arrayOfString1.length; ???++) {
/* 1170 */         ((Map)???).put(arrayOfString1[???], Integer.valueOf(???));
/*      */       }
/*      */ 
/* 1181 */       Set localSet = Util.newSet();
/* 1182 */       for (Object localObject2 = localList.iterator(); ((Iterator)localObject2).hasNext(); ) { localObject3 = (Constructor)((Iterator)localObject2).next();
/* 1183 */         String[] arrayOfString2 = ((ConstructorProperties)((Constructor)localObject3).getAnnotation(localConstructorProperties)).value();
/*      */ 
/* 1186 */         localObject4 = ((Constructor)localObject3).getGenericParameterTypes();
/* 1187 */         if (localObject4.length != arrayOfString2.length) {
/* 1188 */           localObject5 = "Number of constructor params does not match @ConstructorProperties annotation: " + localObject3;
/*      */ 
/* 1191 */           throw new InvalidObjectException((String)localObject5);
/*      */         }
/*      */ 
/* 1194 */         localObject5 = new int[paramArrayOfMethod.length];
/* 1195 */         for (int m = 0; m < paramArrayOfMethod.length; m++)
/* 1196 */           localObject5[m] = -1;
/* 1197 */         localBitSet = new BitSet();
/*      */ 
/* 1199 */         for (int n = 0; n < arrayOfString2.length; n++) {
/* 1200 */           String str1 = arrayOfString2[n];
/* 1201 */           if (!((Map)???).containsKey(str1)) {
/* 1202 */             String str3 = "@ConstructorProperties includes name " + str1 + " which does not correspond to a property";
/*      */ 
/* 1205 */             for (localObject7 = ((Map)???).keySet().iterator(); ((Iterator)localObject7).hasNext(); ) { localObject8 = (String)((Iterator)localObject7).next();
/* 1206 */               if (((String)localObject8).equalsIgnoreCase(str1)) {
/* 1207 */                 str3 = str3 + " (differs only in case from property " + (String)localObject8 + ")";
/*      */               }
/*      */             }
/*      */ 
/* 1211 */             str3 = str3 + ": " + localObject3;
/* 1212 */             throw new InvalidObjectException(str3);
/*      */           }
/* 1214 */           int i2 = ((Integer)((Map)???).get(str1)).intValue();
/* 1215 */           localObject5[i2] = n;
/* 1216 */           if (localBitSet.get(i2)) {
/* 1217 */             localObject7 = "@ConstructorProperties contains property " + str1 + " more than once: " + localObject3;
/*      */ 
/* 1220 */             throw new InvalidObjectException((String)localObject7);
/*      */           }
/* 1222 */           localBitSet.set(i2);
/* 1223 */           Object localObject7 = paramArrayOfMethod[i2];
/* 1224 */           Object localObject8 = ((Method)localObject7).getGenericReturnType();
/* 1225 */           if (!localObject8.equals(localObject4[n])) {
/* 1226 */             String str4 = "@ConstructorProperties gives property " + str1 + " of type " + localObject8 + " for parameter " + " of type " + localObject4[n] + ": " + localObject3;
/*      */ 
/* 1230 */             throw new InvalidObjectException(str4);
/*      */           }
/*      */         }
/*      */ 
/* 1234 */         if (!localSet.add(localBitSet)) {
/* 1235 */           localObject6 = "More than one constructor has a @ConstructorProperties annotation with this set of names: " + Arrays.toString(arrayOfString2);
/*      */ 
/* 1239 */           throw new InvalidObjectException((String)localObject6);
/*      */         }
/*      */ 
/* 1242 */         localObject6 = new Constr((Constructor)localObject3, (int[])localObject5, localBitSet);
/* 1243 */         this.annotatedConstructors.add(localObject6);
/*      */       }
/* 1261 */       Object localObject3;
/*      */       Object localObject4;
/*      */       Object localObject5;
/*      */       BitSet localBitSet;
/*      */       Object localObject6;
/* 1261 */       for (localObject2 = localSet.iterator(); ((Iterator)localObject2).hasNext(); ) { localObject3 = (BitSet)((Iterator)localObject2).next();
/* 1262 */         k = 0;
/* 1263 */         for (localObject4 = localSet.iterator(); ((Iterator)localObject4).hasNext(); ) { localObject5 = (BitSet)((Iterator)localObject4).next();
/* 1264 */           if (localObject3 == localObject5) {
/* 1265 */             k = 1;
/* 1266 */           } else if (k != 0) {
/* 1267 */             localBitSet = new BitSet();
/* 1268 */             localBitSet.or((BitSet)localObject3); localBitSet.or((BitSet)localObject5);
/* 1269 */             if (!localSet.contains(localBitSet)) {
/* 1270 */               localObject6 = new TreeSet();
/* 1271 */               for (int i1 = localBitSet.nextSetBit(0); i1 >= 0; 
/* 1272 */                 i1 = localBitSet.nextSetBit(i1 + 1))
/* 1273 */                 ((Set)localObject6).add(arrayOfString1[i1]);
/* 1274 */               String str2 = "Constructors with @ConstructorProperties annotation  would be ambiguous for these items: " + localObject6;
/*      */ 
/* 1278 */               throw new InvalidObjectException(str2);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       int k;
/* 1284 */       return null;
/*      */     }
/*      */ 
/*      */     final Object fromCompositeData(CompositeData paramCompositeData, String[] paramArrayOfString, MXBeanMapping[] paramArrayOfMXBeanMapping)
/*      */       throws InvalidObjectException
/*      */     {
/* 1296 */       CompositeType localCompositeType = paramCompositeData.getCompositeType();
/* 1297 */       BitSet localBitSet = new BitSet();
/* 1298 */       for (int i = 0; i < paramArrayOfString.length; i++) {
/* 1299 */         if (localCompositeType.getType(paramArrayOfString[i]) != null) {
/* 1300 */           localBitSet.set(i);
/*      */         }
/*      */       }
/* 1303 */       Object localObject1 = null;
/* 1304 */       for (Object localObject2 = this.annotatedConstructors.iterator(); ((Iterator)localObject2).hasNext(); ) { Constr localConstr = (Constr)((Iterator)localObject2).next();
/* 1305 */         if ((subset(localConstr.presentParams, localBitSet)) && ((localObject1 == null) || (subset(localObject1.presentParams, localConstr.presentParams))))
/*      */         {
/* 1308 */           localObject1 = localConstr;
/*      */         }
/*      */       }
/* 1311 */       if (localObject1 == null) {
/* 1312 */         localObject2 = "No constructor has a @ConstructorProperties for this set of items: " + localCompositeType.keySet();
/*      */ 
/* 1315 */         throw new InvalidObjectException((String)localObject2);
/*      */       }
/*      */ 
/* 1318 */       localObject2 = new Object[localObject1.presentParams.cardinality()];
/*      */       Object localObject3;
/* 1319 */       for (int j = 0; j < paramArrayOfString.length; j++)
/* 1320 */         if (localObject1.presentParams.get(j))
/*      */         {
/* 1322 */           localObject3 = paramCompositeData.get(paramArrayOfString[j]);
/* 1323 */           Object localObject4 = paramArrayOfMXBeanMapping[j].fromOpenValue(localObject3);
/* 1324 */           int k = localObject1.paramIndexes[j];
/* 1325 */           if (k >= 0)
/* 1326 */             localObject2[k] = localObject4;
/*      */         }
/*      */       try
/*      */       {
/* 1330 */         ReflectUtil.checkPackageAccess(localObject1.constructor.getDeclaringClass());
/* 1331 */         return localObject1.constructor.newInstance((Object[])localObject2);
/*      */       } catch (Exception localException) {
/* 1333 */         localObject3 = "Exception constructing " + getTargetClass().getName();
/*      */ 
/* 1335 */         throw DefaultMXBeanMappingFactory.invalidObjectException((String)localObject3, localException);
/*      */       }
/*      */     }
/*      */ 
/*      */     private static boolean subset(BitSet paramBitSet1, BitSet paramBitSet2) {
/* 1340 */       BitSet localBitSet = (BitSet)paramBitSet1.clone();
/* 1341 */       localBitSet.andNot(paramBitSet2);
/* 1342 */       return localBitSet.isEmpty();
/*      */     }
/*      */     private static class Constr {
/*      */       final Constructor<?> constructor;
/*      */       final int[] paramIndexes;
/*      */       final BitSet presentParams;
/*      */ 
/* 1351 */       Constr(Constructor<?> paramConstructor, int[] paramArrayOfInt, BitSet paramBitSet) { this.constructor = paramConstructor;
/* 1352 */         this.paramIndexes = paramArrayOfInt;
/* 1353 */         this.presentParams = paramBitSet;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static final class CompositeBuilderViaFrom extends DefaultMXBeanMappingFactory.CompositeBuilder
/*      */   {
/*      */     private Method fromMethod;
/*      */ 
/*      */     CompositeBuilderViaFrom(Class<?> paramClass, String[] paramArrayOfString)
/*      */     {
/*  977 */       super(paramArrayOfString);
/*      */     }
/*      */ 
/*      */     String applicable(Method[] paramArrayOfMethod)
/*      */       throws InvalidObjectException
/*      */     {
/*  983 */       Class localClass = getTargetClass();
/*      */       try {
/*  985 */         Method localMethod = localClass.getMethod("from", new Class[] { CompositeData.class });
/*      */ 
/*  988 */         if (!Modifier.isStatic(localMethod.getModifiers()))
/*      */         {
/*  991 */           throw new InvalidObjectException("Method from(CompositeData) is not static");
/*      */         }
/*      */ 
/*  994 */         if (localMethod.getReturnType() != getTargetClass()) {
/*  995 */           String str = "Method from(CompositeData) returns " + MXBeanIntrospector.typeName(localMethod.getReturnType()) + " not " + MXBeanIntrospector.typeName(localClass);
/*      */ 
/*  999 */           throw new InvalidObjectException(str);
/*      */         }
/*      */ 
/* 1002 */         this.fromMethod = localMethod;
/* 1003 */         return null;
/*      */       } catch (InvalidObjectException localInvalidObjectException) {
/* 1005 */         throw localInvalidObjectException;
/*      */       } catch (Exception localException) {
/*      */       }
/* 1008 */       return "no method from(CompositeData)";
/*      */     }
/*      */ 
/*      */     final Object fromCompositeData(CompositeData paramCompositeData, String[] paramArrayOfString, MXBeanMapping[] paramArrayOfMXBeanMapping)
/*      */       throws InvalidObjectException
/*      */     {
/*      */       try
/*      */       {
/* 1017 */         return MethodUtil.invoke(this.fromMethod, null, new Object[] { paramCompositeData });
/*      */       }
/*      */       catch (Exception localException) {
/* 1020 */         throw DefaultMXBeanMappingFactory.invalidObjectException("Failed to invoke from(CompositeData)", localException);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static final class CompositeBuilderViaProxy extends DefaultMXBeanMappingFactory.CompositeBuilder
/*      */   {
/*      */     CompositeBuilderViaProxy(Class<?> paramClass, String[] paramArrayOfString)
/*      */     {
/* 1368 */       super(paramArrayOfString);
/*      */     }
/*      */ 
/*      */     String applicable(Method[] paramArrayOfMethod) {
/* 1372 */       Class localClass = getTargetClass();
/* 1373 */       if (!localClass.isInterface())
/* 1374 */         return "not an interface";
/* 1375 */       Set localSet = Util.newSet(Arrays.asList(localClass.getMethods()));
/*      */ 
/* 1377 */       localSet.removeAll(Arrays.asList(paramArrayOfMethod));
/*      */ 
/* 1381 */       Object localObject = null;
/* 1382 */       for (Method localMethod1 : localSet) {
/* 1383 */         String str = localMethod1.getName();
/* 1384 */         Class[] arrayOfClass = localMethod1.getParameterTypes();
/*      */         try {
/* 1386 */           Method localMethod2 = Object.class.getMethod(str, arrayOfClass);
/* 1387 */           if (!Modifier.isPublic(localMethod2.getModifiers()))
/* 1388 */             localObject = str;
/*      */         } catch (NoSuchMethodException localNoSuchMethodException) {
/* 1390 */           localObject = str;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1397 */       if (localObject != null)
/* 1398 */         return "contains methods other than getters (" + localObject + ")";
/* 1399 */       return null;
/*      */     }
/*      */ 
/*      */     final Object fromCompositeData(CompositeData paramCompositeData, String[] paramArrayOfString, MXBeanMapping[] paramArrayOfMXBeanMapping)
/*      */     {
/* 1405 */       Class localClass = getTargetClass();
/* 1406 */       return Proxy.newProxyInstance(localClass.getClassLoader(), new Class[] { localClass }, new CompositeDataInvocationHandler(paramCompositeData));
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class CompositeBuilderViaSetters extends DefaultMXBeanMappingFactory.CompositeBuilder
/*      */   {
/*      */     private Method[] setters;
/*      */ 
/*      */     CompositeBuilderViaSetters(Class<?> paramClass, String[] paramArrayOfString)
/*      */     {
/* 1076 */       super(paramArrayOfString);
/*      */     }
/*      */ 
/*      */     String applicable(Method[] paramArrayOfMethod) {
/*      */       try {
/* 1081 */         Constructor localConstructor = getTargetClass().getConstructor(new Class[0]);
/*      */       } catch (Exception localException1) {
/* 1083 */         return "does not have a public no-arg constructor";
/*      */       }
/*      */ 
/* 1086 */       Method[] arrayOfMethod = new Method[paramArrayOfMethod.length];
/* 1087 */       for (int i = 0; i < paramArrayOfMethod.length; i++) { Method localMethod1 = paramArrayOfMethod[i];
/* 1089 */         Class localClass = localMethod1.getReturnType();
/* 1090 */         String str1 = DefaultMXBeanMappingFactory.propertyName(localMethod1);
/* 1091 */         String str2 = "set" + str1;
/*      */         Method localMethod2;
/*      */         try { localMethod2 = getTargetClass().getMethod(str2, new Class[] { localClass });
/* 1095 */           if (localMethod2.getReturnType() != Void.TYPE)
/* 1096 */             throw new Exception();
/*      */         } catch (Exception localException2) {
/* 1098 */           return "not all getters have corresponding setters (" + localMethod1 + ")";
/*      */         }
/*      */ 
/* 1101 */         arrayOfMethod[i] = localMethod2;
/*      */       }
/* 1103 */       this.setters = arrayOfMethod;
/* 1104 */       return null;
/*      */     }
/*      */ 
/*      */     Object fromCompositeData(CompositeData paramCompositeData, String[] paramArrayOfString, MXBeanMapping[] paramArrayOfMXBeanMapping)
/*      */       throws InvalidObjectException
/*      */     {
/*      */       Object localObject1;
/*      */       try
/*      */       {
/* 1113 */         Class localClass = getTargetClass();
/* 1114 */         ReflectUtil.checkPackageAccess(localClass);
/* 1115 */         localObject1 = localClass.newInstance();
/* 1116 */         for (int i = 0; i < paramArrayOfString.length; i++)
/* 1117 */           if (paramCompositeData.containsKey(paramArrayOfString[i])) {
/* 1118 */             Object localObject2 = paramCompositeData.get(paramArrayOfString[i]);
/* 1119 */             Object localObject3 = paramArrayOfMXBeanMapping[i].fromOpenValue(localObject2);
/*      */ 
/* 1121 */             MethodUtil.invoke(this.setters[i], localObject1, new Object[] { localObject3 });
/*      */           }
/*      */       }
/*      */       catch (Exception localException) {
/* 1125 */         throw DefaultMXBeanMappingFactory.invalidObjectException(localException);
/*      */       }
/* 1127 */       return localObject1;
/*      */     }
/*      */   }
/*      */ 
/*      */   private final class CompositeMapping extends DefaultMXBeanMappingFactory.NonNullMXBeanMapping
/*      */   {
/*      */     private final String[] itemNames;
/*      */     private final Method[] getters;
/*      */     private final MXBeanMapping[] getterMappings;
/*      */     private DefaultMXBeanMappingFactory.CompositeBuilder compositeBuilder;
/*      */ 
/*      */     CompositeMapping(CompositeType paramArrayOfString, String[] paramArrayOfMethod, Method[] paramMXBeanMappingFactory, MXBeanMappingFactory arg5)
/*      */       throws OpenDataException
/*      */     {
/*  809 */       super(paramArrayOfMethod);
/*      */       Object localObject;
/*  811 */       assert (paramMXBeanMappingFactory.length == localObject.length);
/*      */ 
/*  813 */       this.itemNames = paramMXBeanMappingFactory;
/*  814 */       this.getters = localObject;
/*  815 */       this.getterMappings = new MXBeanMapping[localObject.length];
/*  816 */       for (int i = 0; i < localObject.length; i++) {
/*  817 */         Type localType = localObject[i].getGenericReturnType();
/*      */         MXBeanMappingFactory localMXBeanMappingFactory;
/*  818 */         this.getterMappings[i] = localMXBeanMappingFactory.mappingForType(localType, localMXBeanMappingFactory);
/*      */       }
/*      */     }
/*      */ 
/*      */     final Object toNonNullOpenValue(Object paramObject)
/*      */       throws OpenDataException
/*      */     {
/*  825 */       CompositeType localCompositeType = (CompositeType)getOpenType();
/*  826 */       if ((paramObject instanceof CompositeDataView))
/*  827 */         return ((CompositeDataView)paramObject).toCompositeData(localCompositeType);
/*  828 */       if (paramObject == null) {
/*  829 */         return null;
/*      */       }
/*  831 */       Object[] arrayOfObject = new Object[this.getters.length];
/*  832 */       for (int i = 0; i < this.getters.length; i++) {
/*      */         try {
/*  834 */           Object localObject = MethodUtil.invoke(this.getters[i], paramObject, (Object[])null);
/*  835 */           arrayOfObject[i] = this.getterMappings[i].toOpenValue(localObject);
/*      */         } catch (Exception localException) {
/*  837 */           throw DefaultMXBeanMappingFactory.openDataException("Error calling getter for " + this.itemNames[i] + ": " + localException, localException);
/*      */         }
/*      */       }
/*      */ 
/*  841 */       return new CompositeDataSupport(localCompositeType, this.itemNames, arrayOfObject);
/*      */     }
/*      */ 
/*      */     private synchronized void makeCompositeBuilder()
/*      */       throws InvalidObjectException
/*      */     {
/*  850 */       if (this.compositeBuilder != null) {
/*  851 */         return;
/*      */       }
/*  853 */       Class localClass = (Class)getJavaType();
/*      */ 
/*  857 */       DefaultMXBeanMappingFactory.CompositeBuilder[][] arrayOfCompositeBuilder; = { { new DefaultMXBeanMappingFactory.CompositeBuilderViaFrom(localClass, this.itemNames) }, { new DefaultMXBeanMappingFactory.CompositeBuilderViaConstructor(localClass, this.itemNames) }, { new DefaultMXBeanMappingFactory.CompositeBuilderCheckGetters(localClass, this.itemNames, this.getterMappings), new DefaultMXBeanMappingFactory.CompositeBuilderViaSetters(localClass, this.itemNames), new DefaultMXBeanMappingFactory.CompositeBuilderViaProxy(localClass, this.itemNames) } };
/*      */ 
/*  871 */       Object localObject1 = null;
/*      */ 
/*  875 */       StringBuilder localStringBuilder = new StringBuilder();
/*  876 */       Object localObject2 = null;
/*      */ 
/*  878 */       for (Object localObject4 : arrayOfCompositeBuilder;) {
/*  879 */         for (int k = 0; k < localObject4.length; k++) {
/*  880 */           Object localObject5 = localObject4[k];
/*  881 */           String str = localObject5.applicable(this.getters);
/*  882 */           if (str == null) {
/*  883 */             localObject1 = localObject5;
/*  884 */             break label268;
/*      */           }
/*  886 */           Throwable localThrowable = localObject5.possibleCause();
/*  887 */           if (localThrowable != null)
/*  888 */             localObject2 = localThrowable;
/*  889 */           if (str.length() > 0) {
/*  890 */             if (localStringBuilder.length() > 0)
/*  891 */               localStringBuilder.append("; ");
/*  892 */             localStringBuilder.append(str);
/*  893 */             if (k == 0)
/*      */               break;
/*      */           }
/*      */         }
/*      */       }
/*  898 */       label268: if (localObject1 == null) {
/*  899 */         ??? = "Do not know how to make a " + localClass.getName() + " from a CompositeData: " + localStringBuilder;
/*      */ 
/*  902 */         if (localObject2 != null)
/*  903 */           ??? = (String)??? + ". Remaining exceptions show a POSSIBLE cause.";
/*  904 */         throw DefaultMXBeanMappingFactory.invalidObjectException((String)???, localObject2);
/*      */       }
/*  906 */       this.compositeBuilder = localObject1;
/*      */     }
/*      */ 
/*      */     public void checkReconstructible() throws InvalidObjectException
/*      */     {
/*  911 */       makeCompositeBuilder();
/*      */     }
/*      */ 
/*      */     final Object fromNonNullOpenValue(Object paramObject)
/*      */       throws InvalidObjectException
/*      */     {
/*  917 */       makeCompositeBuilder();
/*  918 */       return this.compositeBuilder.fromCompositeData((CompositeData)paramObject, this.itemNames, this.getterMappings);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static final class EnumMapping<T extends Enum<T>> extends DefaultMXBeanMappingFactory.NonNullMXBeanMapping
/*      */   {
/*      */     private final Class<T> enumClass;
/*      */ 
/*      */     EnumMapping(Class<T> paramClass)
/*      */     {
/*  520 */       super(SimpleType.STRING);
/*  521 */       this.enumClass = paramClass;
/*      */     }
/*      */ 
/*      */     final Object toNonNullOpenValue(Object paramObject)
/*      */     {
/*  526 */       return ((Enum)paramObject).name();
/*      */     }
/*      */ 
/*      */     final T fromNonNullOpenValue(Object paramObject) throws InvalidObjectException
/*      */     {
/*      */       try
/*      */       {
/*  533 */         return Enum.valueOf(this.enumClass, (String)paramObject);
/*      */       } catch (Exception localException) {
/*  535 */         throw DefaultMXBeanMappingFactory.invalidObjectException("Cannot convert to enum: " + paramObject, localException);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static final class IdentityMapping extends DefaultMXBeanMappingFactory.NonNullMXBeanMapping
/*      */   {
/*      */     IdentityMapping(Type paramType, OpenType<?> paramOpenType)
/*      */     {
/*  497 */       super(paramOpenType);
/*      */     }
/*      */ 
/*      */     boolean isIdentity() {
/*  501 */       return true;
/*      */     }
/*      */ 
/*      */     Object fromNonNullOpenValue(Object paramObject)
/*      */       throws InvalidObjectException
/*      */     {
/*  507 */       return paramObject;
/*      */     }
/*      */ 
/*      */     Object toNonNullOpenValue(Object paramObject) throws OpenDataException
/*      */     {
/*  512 */       return paramObject;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static final class MXBeanRefMapping extends DefaultMXBeanMappingFactory.NonNullMXBeanMapping
/*      */   {
/*      */     MXBeanRefMapping(Type paramType)
/*      */     {
/*  683 */       super(SimpleType.OBJECTNAME);
/*      */     }
/*      */ 
/*      */     final Object toNonNullOpenValue(Object paramObject)
/*      */       throws OpenDataException
/*      */     {
/*  689 */       MXBeanLookup localMXBeanLookup = lookupNotNull(OpenDataException.class);
/*  690 */       ObjectName localObjectName = localMXBeanLookup.mxbeanToObjectName(paramObject);
/*  691 */       if (localObjectName == null)
/*  692 */         throw new OpenDataException("No name for object: " + paramObject);
/*  693 */       return localObjectName;
/*      */     }
/*      */ 
/*      */     final Object fromNonNullOpenValue(Object paramObject)
/*      */       throws InvalidObjectException
/*      */     {
/*  699 */       MXBeanLookup localMXBeanLookup = lookupNotNull(InvalidObjectException.class);
/*  700 */       ObjectName localObjectName = (ObjectName)paramObject;
/*  701 */       Object localObject = localMXBeanLookup.objectNameToMXBean(localObjectName, (Class)getJavaType());
/*      */ 
/*  703 */       if (localObject == null) {
/*  704 */         String str = "No MXBean for name: " + localObjectName;
/*      */ 
/*  706 */         throw new InvalidObjectException(str);
/*      */       }
/*  708 */       return localObject;
/*      */     }
/*      */ 
/*      */     private <T extends Exception> MXBeanLookup lookupNotNull(Class<T> paramClass)
/*      */       throws Exception
/*      */     {
/*  714 */       MXBeanLookup localMXBeanLookup = MXBeanLookup.getLookup();
/*  715 */       if (localMXBeanLookup == null)
/*      */       {
/*      */         Exception localException1;
/*      */         try
/*      */         {
/*  720 */           Constructor localConstructor = paramClass.getConstructor(new Class[] { String.class });
/*  721 */           localException1 = (Exception)localConstructor.newInstance(new Object[] { "Cannot convert MXBean interface in this context" });
/*      */         } catch (Exception localException2) {
/*  723 */           throw new RuntimeException(localException2);
/*      */         }
/*  725 */         throw localException1;
/*      */       }
/*  727 */       return localMXBeanLookup;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static final class Mappings extends WeakHashMap<Type, WeakReference<MXBeanMapping>>
/*      */   {
/*      */   }
/*      */ 
/*      */   static abstract class NonNullMXBeanMapping extends MXBeanMapping
/*      */   {
/*      */     NonNullMXBeanMapping(Type paramType, OpenType<?> paramOpenType)
/*      */     {
/*  123 */       super(paramOpenType);
/*      */     }
/*      */ 
/*      */     public final Object fromOpenValue(Object paramObject)
/*      */       throws InvalidObjectException
/*      */     {
/*  129 */       if (paramObject == null) {
/*  130 */         return null;
/*      */       }
/*  132 */       return fromNonNullOpenValue(paramObject);
/*      */     }
/*      */ 
/*      */     public final Object toOpenValue(Object paramObject) throws OpenDataException
/*      */     {
/*  137 */       if (paramObject == null) {
/*  138 */         return null;
/*      */       }
/*  140 */       return toNonNullOpenValue(paramObject);
/*      */     }
/*      */ 
/*      */     abstract Object fromNonNullOpenValue(Object paramObject)
/*      */       throws InvalidObjectException;
/*      */ 
/*      */     abstract Object toNonNullOpenValue(Object paramObject)
/*      */       throws OpenDataException;
/*      */ 
/*      */     boolean isIdentity()
/*      */     {
/*  154 */       return false;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static final class TabularMapping extends DefaultMXBeanMappingFactory.NonNullMXBeanMapping
/*      */   {
/*      */     private final boolean sortedMap;
/*      */     private final MXBeanMapping keyMapping;
/*      */     private final MXBeanMapping valueMapping;
/*      */ 
/*      */     TabularMapping(Type paramType, boolean paramBoolean, TabularType paramTabularType, MXBeanMapping paramMXBeanMapping1, MXBeanMapping paramMXBeanMapping2)
/*      */     {
/*  737 */       super(paramTabularType);
/*  738 */       this.sortedMap = paramBoolean;
/*  739 */       this.keyMapping = paramMXBeanMapping1;
/*  740 */       this.valueMapping = paramMXBeanMapping2;
/*      */     }
/*      */ 
/*      */     final Object toNonNullOpenValue(Object paramObject) throws OpenDataException
/*      */     {
/*  745 */       Map localMap = (Map)Util.cast(paramObject);
/*  746 */       if ((localMap instanceof SortedMap)) {
/*  747 */         localObject1 = ((SortedMap)localMap).comparator();
/*  748 */         if (localObject1 != null) {
/*  749 */           localObject2 = "Cannot convert SortedMap with non-null comparator: " + localObject1;
/*      */ 
/*  752 */           throw DefaultMXBeanMappingFactory.openDataException((String)localObject2, new IllegalArgumentException((String)localObject2));
/*      */         }
/*      */       }
/*  755 */       Object localObject1 = (TabularType)getOpenType();
/*  756 */       Object localObject2 = new TabularDataSupport((TabularType)localObject1);
/*  757 */       CompositeType localCompositeType = ((TabularType)localObject1).getRowType();
/*  758 */       for (Map.Entry localEntry : localMap.entrySet()) {
/*  759 */         Object localObject3 = this.keyMapping.toOpenValue(localEntry.getKey());
/*  760 */         Object localObject4 = this.valueMapping.toOpenValue(localEntry.getValue());
/*      */ 
/*  762 */         CompositeDataSupport localCompositeDataSupport = new CompositeDataSupport(localCompositeType, DefaultMXBeanMappingFactory.keyValueArray, new Object[] { localObject3, localObject4 });
/*      */ 
/*  766 */         ((TabularData)localObject2).put(localCompositeDataSupport);
/*      */       }
/*  768 */       return localObject2;
/*      */     }
/*      */ 
/*      */     final Object fromNonNullOpenValue(Object paramObject)
/*      */       throws InvalidObjectException
/*      */     {
/*  774 */       TabularData localTabularData = (TabularData)paramObject;
/*  775 */       Collection localCollection = (Collection)Util.cast(localTabularData.values());
/*  776 */       Map localMap = this.sortedMap ? Util.newSortedMap() : Util.newInsertionOrderMap();
/*      */ 
/*  778 */       for (CompositeData localCompositeData : localCollection) {
/*  779 */         Object localObject1 = this.keyMapping.fromOpenValue(localCompositeData.get("key"));
/*      */ 
/*  781 */         Object localObject2 = this.valueMapping.fromOpenValue(localCompositeData.get("value"));
/*      */ 
/*  783 */         if (localMap.put(localObject1, localObject2) != null) {
/*  784 */           String str = "Duplicate entry in TabularData: key=" + localObject1;
/*      */ 
/*  786 */           throw new InvalidObjectException(str);
/*      */         }
/*      */       }
/*  789 */       return localMap;
/*      */     }
/*      */ 
/*      */     public void checkReconstructible() throws InvalidObjectException
/*      */     {
/*  794 */       this.keyMapping.checkReconstructible();
/*  795 */       this.valueMapping.checkReconstructible();
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.mbeanserver.DefaultMXBeanMappingFactory
 * JD-Core Version:    0.6.2
 */