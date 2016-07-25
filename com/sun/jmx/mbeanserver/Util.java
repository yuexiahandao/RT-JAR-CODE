/*     */ package com.sun.jmx.mbeanserver;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.TreeMap;
/*     */ import javax.management.MalformedObjectNameException;
/*     */ import javax.management.ObjectName;
/*     */ 
/*     */ public class Util
/*     */ {
/*     */   public static ObjectName newObjectName(String paramString)
/*     */   {
/*     */     try
/*     */     {
/*  48 */       return new ObjectName(paramString);
/*     */     } catch (MalformedObjectNameException localMalformedObjectNameException) {
/*  50 */       throw new IllegalArgumentException(localMalformedObjectNameException);
/*     */     }
/*     */   }
/*     */ 
/*     */   static <K, V> Map<K, V> newMap() {
/*  55 */     return new HashMap();
/*     */   }
/*     */ 
/*     */   static <K, V> Map<K, V> newSynchronizedMap() {
/*  59 */     return Collections.synchronizedMap(newMap());
/*     */   }
/*     */ 
/*     */   static <K, V> IdentityHashMap<K, V> newIdentityHashMap() {
/*  63 */     return new IdentityHashMap();
/*     */   }
/*     */ 
/*     */   static <K, V> Map<K, V> newSynchronizedIdentityHashMap() {
/*  67 */     IdentityHashMap localIdentityHashMap = newIdentityHashMap();
/*  68 */     return Collections.synchronizedMap(localIdentityHashMap);
/*     */   }
/*     */ 
/*     */   static <K, V> SortedMap<K, V> newSortedMap() {
/*  72 */     return new TreeMap();
/*     */   }
/*     */ 
/*     */   static <K, V> SortedMap<K, V> newSortedMap(Comparator<? super K> paramComparator) {
/*  76 */     return new TreeMap(paramComparator);
/*     */   }
/*     */ 
/*     */   static <K, V> Map<K, V> newInsertionOrderMap() {
/*  80 */     return new LinkedHashMap();
/*     */   }
/*     */ 
/*     */   static <E> Set<E> newSet() {
/*  84 */     return new HashSet();
/*     */   }
/*     */ 
/*     */   static <E> Set<E> newSet(Collection<E> paramCollection) {
/*  88 */     return new HashSet(paramCollection);
/*     */   }
/*     */ 
/*     */   static <E> List<E> newList() {
/*  92 */     return new ArrayList();
/*     */   }
/*     */ 
/*     */   static <E> List<E> newList(Collection<E> paramCollection) {
/*  96 */     return new ArrayList(paramCollection);
/*     */   }
/*     */ 
/*     */   public static <T> T cast(Object paramObject)
/*     */   {
/* 110 */     return paramObject;
/*     */   }
/*     */ 
/*     */   public static int hashCode(String[] paramArrayOfString, Object[] paramArrayOfObject)
/*     */   {
/* 120 */     int i = 0;
/* 121 */     for (int j = 0; j < paramArrayOfString.length; j++) {
/* 122 */       Object localObject = paramArrayOfObject[j];
/*     */       int k;
/* 124 */       if (localObject == null)
/* 125 */         k = 0;
/* 126 */       else if ((localObject instanceof Object[]))
/* 127 */         k = Arrays.deepHashCode((Object[])localObject);
/* 128 */       else if (localObject.getClass().isArray()) {
/* 129 */         k = Arrays.deepHashCode(new Object[] { localObject }) - 31;
/*     */       }
/*     */       else
/*     */       {
/* 133 */         k = localObject.hashCode();
/*     */       }
/* 135 */       i += (paramArrayOfString[j].toLowerCase().hashCode() ^ k);
/*     */     }
/* 137 */     return i;
/*     */   }
/*     */ 
/*     */   private static boolean wildmatch(String paramString1, String paramString2, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/*     */     int j;
/* 186 */     int i = j = -1;
/*     */     while (true)
/*     */     {
/* 192 */       if (paramInt3 < paramInt4) {
/* 193 */         int k = paramString2.charAt(paramInt3);
/* 194 */         switch (k) {
/*     */         case 63:
/* 196 */           if (paramInt1 != paramInt2)
/*     */           {
/* 198 */             paramInt1++;
/* 199 */             paramInt3++;
/* 200 */           }break;
/*     */         case 42:
/* 202 */           paramInt3++;
/* 203 */           j = paramInt3;
/* 204 */           i = paramInt1;
/* 205 */           break;
/*     */         default:
/* 207 */           if ((paramInt1 < paramInt2) && (paramString1.charAt(paramInt1) == k)) {
/* 208 */             paramInt1++;
/* 209 */             paramInt3++;
/* 210 */             continue;
/*     */           }
/*     */ 
/* 214 */           break; }  } else { if (paramInt1 == paramInt2) {
/* 215 */           return true;
/*     */         }
/*     */ 
/* 218 */         if ((j < 0) || (i == paramInt2)) {
/* 219 */           return false;
/*     */         }
/*     */ 
/* 222 */         paramInt3 = j;
/* 223 */         i++;
/* 224 */         paramInt1 = i;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static boolean wildmatch(String paramString1, String paramString2)
/*     */   {
/* 239 */     return wildmatch(paramString1, paramString2, 0, paramString1.length(), 0, paramString2.length());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.mbeanserver.Util
 * JD-Core Version:    0.6.2
 */