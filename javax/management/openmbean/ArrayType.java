/*      */ package javax.management.openmbean;
/*      */ 
/*      */ import java.io.ObjectStreamException;
/*      */ import java.lang.reflect.Array;
/*      */ 
/*      */ public class ArrayType<T> extends OpenType<T>
/*      */ {
/*      */   static final long serialVersionUID = 720504429830309770L;
/*      */   private int dimension;
/*      */   private OpenType<?> elementType;
/*      */   private boolean primitiveArray;
/*  139 */   private transient Integer myHashCode = null;
/*  140 */   private transient String myToString = null;
/*      */   private static final int PRIMITIVE_WRAPPER_NAME_INDEX = 0;
/*      */   private static final int PRIMITIVE_TYPE_NAME_INDEX = 1;
/*      */   private static final int PRIMITIVE_TYPE_KEY_INDEX = 2;
/*      */   private static final int PRIMITIVE_OPEN_TYPE_INDEX = 3;
/*  148 */   private static final Object[][] PRIMITIVE_ARRAY_TYPES = { { Boolean.class.getName(), Boolean.TYPE.getName(), "Z", SimpleType.BOOLEAN }, { Character.class.getName(), Character.TYPE.getName(), "C", SimpleType.CHARACTER }, { Byte.class.getName(), Byte.TYPE.getName(), "B", SimpleType.BYTE }, { Short.class.getName(), Short.TYPE.getName(), "S", SimpleType.SHORT }, { Integer.class.getName(), Integer.TYPE.getName(), "I", SimpleType.INTEGER }, { Long.class.getName(), Long.TYPE.getName(), "J", SimpleType.LONG }, { Float.class.getName(), Float.TYPE.getName(), "F", SimpleType.FLOAT }, { Double.class.getName(), Double.TYPE.getName(), "D", SimpleType.DOUBLE } };
/*      */ 
/*      */   static boolean isPrimitiveContentType(String paramString)
/*      */   {
/*  160 */     for (Object[] arrayOfObject1 : PRIMITIVE_ARRAY_TYPES) {
/*  161 */       if (arrayOfObject1[2].equals(paramString)) {
/*  162 */         return true;
/*      */       }
/*      */     }
/*  165 */     return false;
/*      */   }
/*      */ 
/*      */   static String getPrimitiveTypeKey(String paramString)
/*      */   {
/*  178 */     for (Object[] arrayOfObject1 : PRIMITIVE_ARRAY_TYPES) {
/*  179 */       if (paramString.equals(arrayOfObject1[0]))
/*  180 */         return (String)arrayOfObject1[2];
/*      */     }
/*  182 */     return null;
/*      */   }
/*      */ 
/*      */   static String getPrimitiveTypeName(String paramString)
/*      */   {
/*  196 */     for (Object[] arrayOfObject1 : PRIMITIVE_ARRAY_TYPES) {
/*  197 */       if (paramString.equals(arrayOfObject1[0]))
/*  198 */         return (String)arrayOfObject1[1];
/*      */     }
/*  200 */     return null;
/*      */   }
/*      */ 
/*      */   static SimpleType<?> getPrimitiveOpenType(String paramString)
/*      */   {
/*  215 */     for (Object[] arrayOfObject1 : PRIMITIVE_ARRAY_TYPES) {
/*  216 */       if (paramString.equals(arrayOfObject1[1]))
/*  217 */         return (SimpleType)arrayOfObject1[3];
/*      */     }
/*  219 */     return null;
/*      */   }
/*      */ 
/*      */   public ArrayType(int paramInt, OpenType<?> paramOpenType)
/*      */     throws OpenDataException
/*      */   {
/*  292 */     super(buildArrayClassName(paramInt, paramOpenType), buildArrayClassName(paramInt, paramOpenType), buildArrayDescription(paramInt, paramOpenType));
/*      */ 
/*  298 */     if (paramOpenType.isArray()) {
/*  299 */       ArrayType localArrayType = (ArrayType)paramOpenType;
/*  300 */       this.dimension = (localArrayType.getDimension() + paramInt);
/*  301 */       this.elementType = localArrayType.getElementOpenType();
/*  302 */       this.primitiveArray = localArrayType.isPrimitiveArray();
/*      */     } else {
/*  304 */       this.dimension = paramInt;
/*  305 */       this.elementType = paramOpenType;
/*  306 */       this.primitiveArray = false;
/*      */     }
/*      */   }
/*      */ 
/*      */   public ArrayType(SimpleType<?> paramSimpleType, boolean paramBoolean)
/*      */     throws OpenDataException
/*      */   {
/*  373 */     super(buildArrayClassName(1, paramSimpleType, paramBoolean), buildArrayClassName(1, paramSimpleType, paramBoolean), buildArrayDescription(1, paramSimpleType, paramBoolean), true);
/*      */ 
/*  380 */     this.dimension = 1;
/*  381 */     this.elementType = paramSimpleType;
/*  382 */     this.primitiveArray = paramBoolean;
/*      */   }
/*      */ 
/*      */   ArrayType(String paramString1, String paramString2, String paramString3, int paramInt, OpenType<?> paramOpenType, boolean paramBoolean)
/*      */   {
/*  389 */     super(paramString1, paramString2, paramString3, true);
/*  390 */     this.dimension = paramInt;
/*  391 */     this.elementType = paramOpenType;
/*  392 */     this.primitiveArray = paramBoolean;
/*      */   }
/*      */ 
/*      */   private static String buildArrayClassName(int paramInt, OpenType<?> paramOpenType)
/*      */     throws OpenDataException
/*      */   {
/*  398 */     boolean bool = false;
/*  399 */     if (paramOpenType.isArray()) {
/*  400 */       bool = ((ArrayType)paramOpenType).isPrimitiveArray();
/*      */     }
/*  402 */     return buildArrayClassName(paramInt, paramOpenType, bool);
/*      */   }
/*      */ 
/*      */   private static String buildArrayClassName(int paramInt, OpenType<?> paramOpenType, boolean paramBoolean)
/*      */     throws OpenDataException
/*      */   {
/*  409 */     if (paramInt < 1) {
/*  410 */       throw new IllegalArgumentException("Value of argument dimension must be greater than 0");
/*      */     }
/*      */ 
/*  413 */     StringBuilder localStringBuilder = new StringBuilder();
/*  414 */     String str1 = paramOpenType.getClassName();
/*      */ 
/*  416 */     for (int i = 1; i <= paramInt; i++) {
/*  417 */       localStringBuilder.append('[');
/*      */     }
/*  419 */     if (paramOpenType.isArray()) {
/*  420 */       localStringBuilder.append(str1);
/*      */     }
/*  422 */     else if (paramBoolean) {
/*  423 */       String str2 = getPrimitiveTypeKey(str1);
/*      */ 
/*  428 */       if (str2 == null) {
/*  429 */         throw new OpenDataException("Element type is not primitive: " + str1);
/*      */       }
/*  431 */       localStringBuilder.append(str2);
/*      */     } else {
/*  433 */       localStringBuilder.append("L");
/*  434 */       localStringBuilder.append(str1);
/*  435 */       localStringBuilder.append(';');
/*      */     }
/*      */ 
/*  438 */     return localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   private static String buildArrayDescription(int paramInt, OpenType<?> paramOpenType)
/*      */     throws OpenDataException
/*      */   {
/*  444 */     boolean bool = false;
/*  445 */     if (paramOpenType.isArray()) {
/*  446 */       bool = ((ArrayType)paramOpenType).isPrimitiveArray();
/*      */     }
/*  448 */     return buildArrayDescription(paramInt, paramOpenType, bool);
/*      */   }
/*      */ 
/*      */   private static String buildArrayDescription(int paramInt, OpenType<?> paramOpenType, boolean paramBoolean)
/*      */     throws OpenDataException
/*      */   {
/*  455 */     if (paramOpenType.isArray()) {
/*  456 */       localObject = (ArrayType)paramOpenType;
/*  457 */       paramInt += ((ArrayType)localObject).getDimension();
/*  458 */       paramOpenType = ((ArrayType)localObject).getElementOpenType();
/*  459 */       paramBoolean = ((ArrayType)localObject).isPrimitiveArray();
/*      */     }
/*  461 */     Object localObject = new StringBuilder(paramInt + "-dimension array of ");
/*      */ 
/*  463 */     String str1 = paramOpenType.getClassName();
/*  464 */     if (paramBoolean)
/*      */     {
/*  466 */       String str2 = getPrimitiveTypeName(str1);
/*      */ 
/*  473 */       if (str2 == null) {
/*  474 */         throw new OpenDataException("Element is not a primitive type: " + str1);
/*      */       }
/*  476 */       ((StringBuilder)localObject).append(str2);
/*      */     } else {
/*  478 */       ((StringBuilder)localObject).append(str1);
/*      */     }
/*  480 */     return ((StringBuilder)localObject).toString();
/*      */   }
/*      */ 
/*      */   public int getDimension()
/*      */   {
/*  492 */     return this.dimension;
/*      */   }
/*      */ 
/*      */   public OpenType<?> getElementOpenType()
/*      */   {
/*  502 */     return this.elementType;
/*      */   }
/*      */ 
/*      */   public boolean isPrimitiveArray()
/*      */   {
/*  515 */     return this.primitiveArray;
/*      */   }
/*      */ 
/*      */   public boolean isValue(Object paramObject)
/*      */   {
/*  550 */     if (paramObject == null) {
/*  551 */       return false;
/*      */     }
/*      */ 
/*  554 */     Class localClass1 = paramObject.getClass();
/*  555 */     String str = localClass1.getName();
/*      */ 
/*  559 */     if (!localClass1.isArray()) {
/*  560 */       return false;
/*      */     }
/*      */ 
/*  566 */     if (getClassName().equals(str)) {
/*  567 */       return true;
/*      */     }
/*      */ 
/*  583 */     if ((this.elementType.getClassName().equals(TabularData.class.getName())) || (this.elementType.getClassName().equals(CompositeData.class.getName())))
/*      */     {
/*  586 */       boolean bool = this.elementType.getClassName().equals(TabularData.class.getName());
/*      */ 
/*  588 */       int[] arrayOfInt = new int[getDimension()];
/*  589 */       CompositeData localCompositeData = bool ? TabularData.class : CompositeData.class;
/*  590 */       Class localClass2 = Array.newInstance(localCompositeData, arrayOfInt).getClass();
/*      */ 
/*  593 */       if (!localClass2.isAssignableFrom(localClass1)) {
/*  594 */         return false;
/*      */       }
/*      */ 
/*  598 */       if (!checkElementsType((Object[])paramObject, this.dimension)) {
/*  599 */         return false;
/*      */       }
/*      */ 
/*  602 */       return true;
/*      */     }
/*      */ 
/*  606 */     return false;
/*      */   }
/*      */ 
/*      */   private boolean checkElementsType(Object[] paramArrayOfObject, int paramInt)
/*      */   {
/*  619 */     if (paramInt > 1) {
/*  620 */       for (i = 0; i < paramArrayOfObject.length; i++) {
/*  621 */         if (!checkElementsType((Object[])paramArrayOfObject[i], paramInt - 1)) {
/*  622 */           return false;
/*      */         }
/*      */       }
/*  625 */       return true;
/*      */     }
/*      */ 
/*  629 */     for (int i = 0; i < paramArrayOfObject.length; i++) {
/*  630 */       if ((paramArrayOfObject[i] != null) && (!getElementOpenType().isValue(paramArrayOfObject[i]))) {
/*  631 */         return false;
/*      */       }
/*      */     }
/*  634 */     return true;
/*      */   }
/*      */ 
/*      */   boolean isAssignableFrom(OpenType<?> paramOpenType)
/*      */   {
/*  640 */     if (!(paramOpenType instanceof ArrayType))
/*  641 */       return false;
/*  642 */     ArrayType localArrayType = (ArrayType)paramOpenType;
/*  643 */     return (localArrayType.getDimension() == getDimension()) && (localArrayType.isPrimitiveArray() == isPrimitiveArray()) && (localArrayType.getElementOpenType().isAssignableFrom(getElementOpenType()));
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject)
/*      */   {
/*  672 */     if (paramObject == null) {
/*  673 */       return false;
/*      */     }
/*      */ 
/*  678 */     if (!(paramObject instanceof ArrayType))
/*  679 */       return false;
/*  680 */     ArrayType localArrayType = (ArrayType)paramObject;
/*      */ 
/*  684 */     if (this.dimension != localArrayType.dimension) {
/*  685 */       return false;
/*      */     }
/*      */ 
/*  690 */     if (!this.elementType.equals(localArrayType.elementType)) {
/*  691 */       return false;
/*      */     }
/*      */ 
/*  696 */     return this.primitiveArray == localArrayType.primitiveArray;
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/*  724 */     if (this.myHashCode == null) {
/*  725 */       int i = 0;
/*  726 */       i += this.dimension;
/*  727 */       i += this.elementType.hashCode();
/*  728 */       i += Boolean.valueOf(this.primitiveArray).hashCode();
/*  729 */       this.myHashCode = Integer.valueOf(i);
/*      */     }
/*      */ 
/*  734 */     return this.myHashCode.intValue();
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/*  756 */     if (this.myToString == null) {
/*  757 */       this.myToString = (getClass().getName() + "(name=" + getTypeName() + ",dimension=" + this.dimension + ",elementType=" + this.elementType + ",primitiveArray=" + this.primitiveArray + ")");
/*      */     }
/*      */ 
/*  766 */     return this.myToString;
/*      */   }
/*      */ 
/*      */   public static <E> ArrayType<E[]> getArrayType(OpenType<E> paramOpenType)
/*      */     throws OpenDataException
/*      */   {
/*  812 */     return new ArrayType(1, paramOpenType);
/*      */   }
/*      */ 
/*      */   public static <T> ArrayType<T> getPrimitiveArrayType(Class<T> paramClass)
/*      */   {
/*  853 */     if (!paramClass.isArray()) {
/*  854 */       throw new IllegalArgumentException("arrayClass must be an array");
/*      */     }
/*      */ 
/*  859 */     int i = 1;
/*  860 */     Class localClass = paramClass.getComponentType();
/*  861 */     while (localClass.isArray()) {
/*  862 */       i++;
/*  863 */       localClass = localClass.getComponentType();
/*      */     }
/*  865 */     String str = localClass.getName();
/*      */ 
/*  869 */     if (!localClass.isPrimitive()) {
/*  870 */       throw new IllegalArgumentException("component type of the array must be a primitive type");
/*      */     }
/*      */ 
/*  876 */     SimpleType localSimpleType = getPrimitiveOpenType(str);
/*      */     try
/*      */     {
/*  883 */       ArrayType localArrayType = new ArrayType(localSimpleType, true);
/*  884 */       if (i > 1);
/*  885 */       return new ArrayType(i - 1, localArrayType);
/*      */     }
/*      */     catch (OpenDataException localOpenDataException) {
/*  888 */       throw new IllegalArgumentException(localOpenDataException);
/*      */     }
/*      */   }
/*      */ 
/*      */   private Object readResolve()
/*      */     throws ObjectStreamException
/*      */   {
/*  931 */     if (this.primitiveArray) {
/*  932 */       return convertFromWrapperToPrimitiveTypes();
/*      */     }
/*  934 */     return this;
/*      */   }
/*      */ 
/*      */   private <T> ArrayType<T> convertFromWrapperToPrimitiveTypes()
/*      */   {
/*  939 */     String str1 = getClassName();
/*  940 */     String str2 = getTypeName();
/*  941 */     String str3 = getDescription();
/*  942 */     for (Object[] arrayOfObject1 : PRIMITIVE_ARRAY_TYPES) {
/*  943 */       if (str1.indexOf((String)arrayOfObject1[0]) != -1) {
/*  944 */         str1 = str1.replaceFirst("L" + arrayOfObject1[0] + ";", (String)arrayOfObject1[2]);
/*      */ 
/*  947 */         str2 = str2.replaceFirst("L" + arrayOfObject1[0] + ";", (String)arrayOfObject1[2]);
/*      */ 
/*  950 */         str3 = str3.replaceFirst((String)arrayOfObject1[0], (String)arrayOfObject1[1]);
/*      */ 
/*  953 */         break;
/*      */       }
/*      */     }
/*  956 */     return new ArrayType(str1, str2, str3, this.dimension, this.elementType, this.primitiveArray);
/*      */   }
/*      */ 
/*      */   private Object writeReplace()
/*      */     throws ObjectStreamException
/*      */   {
/*  999 */     if (this.primitiveArray) {
/* 1000 */       return convertFromPrimitiveToWrapperTypes();
/*      */     }
/* 1002 */     return this;
/*      */   }
/*      */ 
/*      */   private <T> ArrayType<T> convertFromPrimitiveToWrapperTypes()
/*      */   {
/* 1007 */     String str1 = getClassName();
/* 1008 */     String str2 = getTypeName();
/* 1009 */     String str3 = getDescription();
/* 1010 */     for (Object[] arrayOfObject1 : PRIMITIVE_ARRAY_TYPES) {
/* 1011 */       if (str1.indexOf((String)arrayOfObject1[2]) != -1) {
/* 1012 */         str1 = str1.replaceFirst((String)arrayOfObject1[2], "L" + arrayOfObject1[0] + ";");
/*      */ 
/* 1015 */         str2 = str2.replaceFirst((String)arrayOfObject1[2], "L" + arrayOfObject1[0] + ";");
/*      */ 
/* 1018 */         str3 = str3.replaceFirst((String)arrayOfObject1[1], (String)arrayOfObject1[0]);
/*      */ 
/* 1021 */         break;
/*      */       }
/*      */     }
/* 1024 */     return new ArrayType(str1, str2, str3, this.dimension, this.elementType, this.primitiveArray);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.openmbean.ArrayType
 * JD-Core Version:    0.6.2
 */