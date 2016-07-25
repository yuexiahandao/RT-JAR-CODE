/*     */ package sun.invoke.util;
/*     */ 
/*     */ import java.lang.invoke.MethodType;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ public class BytecodeDescriptor
/*     */ {
/*     */   public static List<Class<?>> parseMethod(String paramString, ClassLoader paramClassLoader)
/*     */   {
/*  41 */     return parseMethod(paramString, 0, paramString.length(), paramClassLoader);
/*     */   }
/*     */ 
/*     */   static List<Class<?>> parseMethod(String paramString, int paramInt1, int paramInt2, ClassLoader paramClassLoader)
/*     */   {
/*  46 */     if (paramClassLoader == null)
/*  47 */       paramClassLoader = ClassLoader.getSystemClassLoader();
/*  48 */     String str = paramString;
/*  49 */     int[] arrayOfInt = { paramInt1 };
/*  50 */     ArrayList localArrayList = new ArrayList();
/*  51 */     if ((arrayOfInt[0] < paramInt2) && (str.charAt(arrayOfInt[0]) == '(')) {
/*  52 */       arrayOfInt[0] += 1;
/*  53 */       while ((arrayOfInt[0] < paramInt2) && (str.charAt(arrayOfInt[0]) != ')')) {
/*  54 */         localClass = parseSig(str, arrayOfInt, paramInt2, paramClassLoader);
/*  55 */         if ((localClass == null) || (localClass == Void.TYPE))
/*  56 */           parseError(str, "bad argument type");
/*  57 */         localArrayList.add(localClass);
/*     */       }
/*  59 */       arrayOfInt[0] += 1;
/*     */     } else {
/*  61 */       parseError(str, "not a method type");
/*     */     }
/*  63 */     Class localClass = parseSig(str, arrayOfInt, paramInt2, paramClassLoader);
/*  64 */     if ((localClass == null) || (arrayOfInt[0] != paramInt2))
/*  65 */       parseError(str, "bad return type");
/*  66 */     localArrayList.add(localClass);
/*  67 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   private static void parseError(String paramString1, String paramString2) {
/*  71 */     throw new IllegalArgumentException("bad signature: " + paramString1 + ": " + paramString2);
/*     */   }
/*     */ 
/*     */   private static Class<?> parseSig(String paramString, int[] paramArrayOfInt, int paramInt, ClassLoader paramClassLoader) {
/*  75 */     if (paramArrayOfInt[0] == paramInt) return null;
/*     */     int tmp12_11 = 0;
/*     */     int[] tmp12_10 = paramArrayOfInt;
/*     */     int tmp14_13 = tmp12_10[tmp12_11]; tmp12_10[tmp12_11] = (tmp14_13 + 1); char c = paramString.charAt(tmp14_13);
/*  77 */     if (c == 'L') {
/*  78 */       int i = paramArrayOfInt[0]; int j = paramString.indexOf(';', i);
/*  79 */       if (j < 0) return null;
/*  80 */       paramArrayOfInt[0] = (j + 1);
/*  81 */       String str = paramString.substring(i, j).replace('/', '.');
/*     */       try {
/*  83 */         return paramClassLoader.loadClass(str);
/*     */       } catch (ClassNotFoundException localClassNotFoundException) {
/*  85 */         throw new TypeNotPresentException(str, localClassNotFoundException);
/*     */       }
/*     */     }
/*  87 */     if (c == '[') {
/*  88 */       Class localClass = parseSig(paramString, paramArrayOfInt, paramInt, paramClassLoader);
/*  89 */       if (localClass != null)
/*  90 */         localClass = Array.newInstance(localClass, 0).getClass();
/*  91 */       return localClass;
/*     */     }
/*  93 */     return Wrapper.forBasicType(c).primitiveType();
/*     */   }
/*     */ 
/*     */   public static String unparse(Class<?> paramClass)
/*     */   {
/*  98 */     StringBuilder localStringBuilder = new StringBuilder();
/*  99 */     unparseSig(paramClass, localStringBuilder);
/* 100 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public static String unparse(MethodType paramMethodType) {
/* 104 */     return unparseMethod(paramMethodType.returnType(), paramMethodType.parameterList());
/*     */   }
/*     */ 
/*     */   public static String unparse(Object paramObject) {
/* 108 */     if ((paramObject instanceof Class))
/* 109 */       return unparse((Class)paramObject);
/* 110 */     if ((paramObject instanceof MethodType))
/* 111 */       return unparse((MethodType)paramObject);
/* 112 */     return (String)paramObject;
/*     */   }
/*     */ 
/*     */   public static String unparseMethod(Class<?> paramClass, List<Class<?>> paramList) {
/* 116 */     StringBuilder localStringBuilder = new StringBuilder();
/* 117 */     localStringBuilder.append('(');
/* 118 */     for (Class localClass : paramList)
/* 119 */       unparseSig(localClass, localStringBuilder);
/* 120 */     localStringBuilder.append(')');
/* 121 */     unparseSig(paramClass, localStringBuilder);
/* 122 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   private static void unparseSig(Class<?> paramClass, StringBuilder paramStringBuilder) {
/* 126 */     char c = Wrapper.forBasicType(paramClass).basicTypeChar();
/* 127 */     if (c != 'L') {
/* 128 */       paramStringBuilder.append(c);
/*     */     } else {
/* 130 */       int i = !paramClass.isArray() ? 1 : 0;
/* 131 */       if (i != 0) paramStringBuilder.append('L');
/* 132 */       paramStringBuilder.append(paramClass.getName().replace('.', '/'));
/* 133 */       if (i != 0) paramStringBuilder.append(';');
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.invoke.util.BytecodeDescriptor
 * JD-Core Version:    0.6.2
 */