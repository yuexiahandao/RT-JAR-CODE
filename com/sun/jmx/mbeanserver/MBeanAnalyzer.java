/*     */ package com.sun.jmx.mbeanserver;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import javax.management.NotCompliantMBeanException;
/*     */ 
/*     */ class MBeanAnalyzer<M>
/*     */ {
/*  78 */   private Map<String, List<M>> opMap = Util.newInsertionOrderMap();
/*     */ 
/*  80 */   private Map<String, AttrMethods<M>> attrMap = Util.newInsertionOrderMap();
/*     */ 
/*     */   void visit(MBeanVisitor<M> paramMBeanVisitor)
/*     */   {
/*  64 */     for (Iterator localIterator = this.attrMap.entrySet().iterator(); localIterator.hasNext(); ) { localEntry = (Map.Entry)localIterator.next();
/*  65 */       localObject1 = (String)localEntry.getKey();
/*  66 */       localObject2 = (AttrMethods)localEntry.getValue();
/*  67 */       paramMBeanVisitor.visitAttribute((String)localObject1, ((AttrMethods)localObject2).getter, ((AttrMethods)localObject2).setter);
/*     */     }
/*  71 */     Map.Entry localEntry;
/*     */     Object localObject1;
/*     */     Object localObject2;
/*  71 */     for (localIterator = this.opMap.entrySet().iterator(); localIterator.hasNext(); ) { localEntry = (Map.Entry)localIterator.next();
/*  72 */       for (localObject1 = ((List)localEntry.getValue()).iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = ((Iterator)localObject1).next();
/*  73 */         paramMBeanVisitor.visitOperation((String)localEntry.getKey(), localObject2);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static <M> MBeanAnalyzer<M> analyzer(Class<?> paramClass, MBeanIntrospector<M> paramMBeanIntrospector)
/*     */     throws NotCompliantMBeanException
/*     */   {
/* 101 */     return new MBeanAnalyzer(paramClass, paramMBeanIntrospector);
/*     */   }
/*     */ 
/*     */   private MBeanAnalyzer(Class<?> paramClass, MBeanIntrospector<M> paramMBeanIntrospector)
/*     */     throws NotCompliantMBeanException
/*     */   {
/* 107 */     if (!paramClass.isInterface()) {
/* 108 */       throw new NotCompliantMBeanException("Not an interface: " + paramClass.getName());
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 113 */       initMaps(paramClass, paramMBeanIntrospector);
/*     */     } catch (Exception localException) {
/* 115 */       throw Introspector.throwException(paramClass, localException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void initMaps(Class<?> paramClass, MBeanIntrospector<M> paramMBeanIntrospector)
/*     */     throws Exception
/*     */   {
/* 123 */     List localList1 = paramMBeanIntrospector.getMethods(paramClass);
/* 124 */     List localList2 = eliminateCovariantMethods(localList1);
/*     */ 
/* 128 */     for (Iterator localIterator = localList2.iterator(); localIterator.hasNext(); ) { localObject1 = (Method)localIterator.next();
/* 129 */       localObject2 = ((Method)localObject1).getName();
/* 130 */       int i = ((Method)localObject1).getParameterTypes().length;
/*     */ 
/* 132 */       Object localObject3 = paramMBeanIntrospector.mFrom((Method)localObject1);
/*     */ 
/* 134 */       String str2 = "";
/* 135 */       if (((String)localObject2).startsWith("get"))
/* 136 */         str2 = ((String)localObject2).substring(3);
/* 137 */       else if ((((String)localObject2).startsWith("is")) && (((Method)localObject1).getReturnType() == Boolean.TYPE))
/*     */       {
/* 139 */         str2 = ((String)localObject2).substring(2);
/*     */       }
/*     */       Object localObject4;
/*     */       String str3;
/* 141 */       if ((str2.length() != 0) && (i == 0) && (((Method)localObject1).getReturnType() != Void.TYPE))
/*     */       {
/* 145 */         localObject4 = (AttrMethods)this.attrMap.get(str2);
/* 146 */         if (localObject4 == null) {
/* 147 */           localObject4 = new AttrMethods(null);
/*     */         }
/* 149 */         else if (((AttrMethods)localObject4).getter != null) {
/* 150 */           str3 = "Attribute " + str2 + " has more than one getter";
/*     */ 
/* 152 */           throw new NotCompliantMBeanException(str3);
/*     */         }
/*     */ 
/* 155 */         ((AttrMethods)localObject4).getter = localObject3;
/* 156 */         this.attrMap.put(str2, localObject4);
/* 157 */       } else if ((((String)localObject2).startsWith("set")) && (((String)localObject2).length() > 3) && (i == 1) && (((Method)localObject1).getReturnType() == Void.TYPE))
/*     */       {
/* 161 */         str2 = ((String)localObject2).substring(3);
/* 162 */         localObject4 = (AttrMethods)this.attrMap.get(str2);
/* 163 */         if (localObject4 == null) {
/* 164 */           localObject4 = new AttrMethods(null);
/* 165 */         } else if (((AttrMethods)localObject4).setter != null) {
/* 166 */           str3 = "Attribute " + str2 + " has more than one setter";
/*     */ 
/* 168 */           throw new NotCompliantMBeanException(str3);
/*     */         }
/* 170 */         ((AttrMethods)localObject4).setter = localObject3;
/* 171 */         this.attrMap.put(str2, localObject4);
/*     */       }
/*     */       else {
/* 174 */         localObject4 = (List)this.opMap.get(localObject2);
/* 175 */         if (localObject4 == null)
/* 176 */           localObject4 = Util.newList();
/* 177 */         ((List)localObject4).add(localObject3);
/* 178 */         this.opMap.put(localObject2, localObject4);
/*     */       }
/*     */     }
/* 182 */     Object localObject1;
/*     */     Object localObject2;
/* 182 */     for (localIterator = this.attrMap.entrySet().iterator(); localIterator.hasNext(); ) { localObject1 = (Map.Entry)localIterator.next();
/* 183 */       localObject2 = (AttrMethods)((Map.Entry)localObject1).getValue();
/* 184 */       if (!paramMBeanIntrospector.consistent(((AttrMethods)localObject2).getter, ((AttrMethods)localObject2).setter)) {
/* 185 */         String str1 = "Getter and setter for " + (String)((Map.Entry)localObject1).getKey() + " have inconsistent types";
/*     */ 
/* 187 */         throw new NotCompliantMBeanException(str1);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static List<Method> eliminateCovariantMethods(List<Method> paramList)
/*     */   {
/* 243 */     int i = paramList.size();
/* 244 */     Method[] arrayOfMethod = (Method[])paramList.toArray(new Method[i]);
/* 245 */     Arrays.sort(arrayOfMethod, MethodOrder.instance);
/* 246 */     Set localSet = Util.newSet();
/* 247 */     for (int j = 1; j < i; j++) {
/* 248 */       Method localMethod1 = arrayOfMethod[(j - 1)];
/* 249 */       Method localMethod2 = arrayOfMethod[j];
/*     */ 
/* 252 */       if (localMethod1.getName().equals(localMethod2.getName()))
/*     */       {
/* 257 */         if (Arrays.equals(localMethod1.getParameterTypes(), localMethod2.getParameterTypes()))
/*     */         {
/* 259 */           if (!localSet.add(localMethod1))
/* 260 */             throw new RuntimeException("Internal error: duplicate Method");
/*     */         }
/*     */       }
/*     */     }
/* 264 */     List localList = Util.newList(paramList);
/* 265 */     localList.removeAll(localSet);
/* 266 */     return localList;
/*     */   }
/*     */ 
/*     */   private static class AttrMethods<M>
/*     */   {
/*     */     M getter;
/*     */     M setter;
/*     */   }
/*     */ 
/*     */   static abstract interface MBeanVisitor<M>
/*     */   {
/*     */     public abstract void visitAttribute(String paramString, M paramM1, M paramM2);
/*     */ 
/*     */     public abstract void visitOperation(String paramString, M paramM);
/*     */   }
/*     */ 
/*     */   private static class MethodOrder
/*     */     implements Comparator<Method>
/*     */   {
/* 223 */     public static final MethodOrder instance = new MethodOrder();
/*     */ 
/*     */     public int compare(Method paramMethod1, Method paramMethod2)
/*     */     {
/* 204 */       int i = paramMethod1.getName().compareTo(paramMethod2.getName());
/* 205 */       if (i != 0) return i;
/* 206 */       Class[] arrayOfClass1 = paramMethod1.getParameterTypes();
/* 207 */       Class[] arrayOfClass2 = paramMethod2.getParameterTypes();
/* 208 */       if (arrayOfClass1.length != arrayOfClass2.length)
/* 209 */         return arrayOfClass1.length - arrayOfClass2.length;
/* 210 */       if (!Arrays.equals(arrayOfClass1, arrayOfClass2)) {
/* 211 */         return Arrays.toString(arrayOfClass1).compareTo(Arrays.toString(arrayOfClass2));
/*     */       }
/*     */ 
/* 214 */       Class localClass1 = paramMethod1.getReturnType();
/* 215 */       Class localClass2 = paramMethod2.getReturnType();
/* 216 */       if (localClass1 == localClass2) return 0;
/*     */ 
/* 219 */       if (localClass1.isAssignableFrom(localClass2))
/* 220 */         return -1;
/* 221 */       return 1;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.mbeanserver.MBeanAnalyzer
 * JD-Core Version:    0.6.2
 */