/*     */ package javax.management.openmbean;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
/*     */ 
/*     */ public class CompositeDataSupport
/*     */   implements CompositeData, Serializable
/*     */ {
/*     */   static final long serialVersionUID = 8003518976613702244L;
/*     */   private final SortedMap<String, Object> contents;
/*     */   private final CompositeType compositeType;
/*     */ 
/*     */   public CompositeDataSupport(CompositeType paramCompositeType, String[] paramArrayOfString, Object[] paramArrayOfObject)
/*     */     throws OpenDataException
/*     */   {
/* 118 */     this(makeMap(paramArrayOfString, paramArrayOfObject), paramCompositeType);
/*     */   }
/*     */ 
/*     */   private static SortedMap<String, Object> makeMap(String[] paramArrayOfString, Object[] paramArrayOfObject)
/*     */     throws OpenDataException
/*     */   {
/* 125 */     if ((paramArrayOfString == null) || (paramArrayOfObject == null))
/* 126 */       throw new IllegalArgumentException("Null itemNames or itemValues");
/* 127 */     if ((paramArrayOfString.length == 0) || (paramArrayOfObject.length == 0))
/* 128 */       throw new IllegalArgumentException("Empty itemNames or itemValues");
/* 129 */     if (paramArrayOfString.length != paramArrayOfObject.length) {
/* 130 */       throw new IllegalArgumentException("Different lengths: itemNames[" + paramArrayOfString.length + "], itemValues[" + paramArrayOfObject.length + "]");
/*     */     }
/*     */ 
/* 135 */     TreeMap localTreeMap = new TreeMap();
/* 136 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/* 137 */       String str = paramArrayOfString[i];
/* 138 */       if ((str == null) || (str.equals("")))
/* 139 */         throw new IllegalArgumentException("Null or empty item name");
/* 140 */       if (localTreeMap.containsKey(str))
/* 141 */         throw new OpenDataException("Duplicate item name " + str);
/* 142 */       localTreeMap.put(paramArrayOfString[i], paramArrayOfObject[i]);
/*     */     }
/*     */ 
/* 145 */     return localTreeMap;
/*     */   }
/*     */ 
/*     */   public CompositeDataSupport(CompositeType paramCompositeType, Map<String, ?> paramMap)
/*     */     throws OpenDataException
/*     */   {
/* 176 */     this(makeMap(paramMap), paramCompositeType);
/*     */   }
/*     */ 
/*     */   private static SortedMap<String, Object> makeMap(Map<String, ?> paramMap) {
/* 180 */     if ((paramMap == null) || (paramMap.isEmpty())) {
/* 181 */       throw new IllegalArgumentException("Null or empty items map");
/*     */     }
/* 183 */     TreeMap localTreeMap = new TreeMap();
/* 184 */     for (Iterator localIterator = paramMap.keySet().iterator(); localIterator.hasNext(); ) { Object localObject = localIterator.next();
/* 185 */       if ((localObject == null) || (localObject.equals("")))
/* 186 */         throw new IllegalArgumentException("Null or empty item name");
/* 187 */       if (!(localObject instanceof String)) {
/* 188 */         throw new ArrayStoreException("Item name is not string: " + localObject);
/*     */       }
/*     */ 
/* 193 */       localTreeMap.put((String)localObject, paramMap.get(localObject));
/*     */     }
/* 195 */     return localTreeMap;
/*     */   }
/*     */ 
/*     */   private CompositeDataSupport(SortedMap<String, Object> paramSortedMap, CompositeType paramCompositeType)
/*     */     throws OpenDataException
/*     */   {
/* 204 */     if (paramCompositeType == null) {
/* 205 */       throw new IllegalArgumentException("Argument compositeType cannot be null.");
/*     */     }
/*     */ 
/* 209 */     Set localSet1 = paramCompositeType.keySet();
/* 210 */     Set localSet2 = paramSortedMap.keySet();
/*     */     Object localObject2;
/* 214 */     if (!localSet1.equals(localSet2)) {
/* 215 */       localObject1 = new TreeSet(localSet1);
/* 216 */       ((Set)localObject1).removeAll(localSet2);
/* 217 */       localObject2 = new TreeSet(localSet2);
/* 218 */       ((Set)localObject2).removeAll(localSet1);
/* 219 */       if ((!((Set)localObject1).isEmpty()) || (!((Set)localObject2).isEmpty())) {
/* 220 */         throw new OpenDataException("Item names do not match CompositeType: names in items but not in CompositeType: " + localObject2 + "; names in CompositeType but not in items: " + localObject1);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 229 */     for (Object localObject1 = localSet1.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (String)((Iterator)localObject1).next();
/* 230 */       Object localObject3 = paramSortedMap.get(localObject2);
/* 231 */       if (localObject3 != null) {
/* 232 */         OpenType localOpenType = paramCompositeType.getType((String)localObject2);
/* 233 */         if (!localOpenType.isValue(localObject3)) {
/* 234 */           throw new OpenDataException("Argument value of wrong type for item " + (String)localObject2 + ": value " + localObject3 + ", type " + localOpenType);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 243 */     this.compositeType = paramCompositeType;
/* 244 */     this.contents = paramSortedMap;
/*     */   }
/*     */ 
/*     */   public CompositeType getCompositeType()
/*     */   {
/* 252 */     return this.compositeType;
/*     */   }
/*     */ 
/*     */   public Object get(String paramString)
/*     */   {
/* 265 */     if ((paramString == null) || (paramString.trim().equals(""))) {
/* 266 */       throw new IllegalArgumentException("Argument key cannot be a null or empty String.");
/*     */     }
/* 268 */     if (!this.contents.containsKey(paramString.trim())) {
/* 269 */       throw new InvalidKeyException("Argument key=\"" + paramString.trim() + "\" is not an existing item name for this CompositeData instance.");
/*     */     }
/* 271 */     return this.contents.get(paramString.trim());
/*     */   }
/*     */ 
/*     */   public Object[] getAll(String[] paramArrayOfString)
/*     */   {
/* 286 */     if ((paramArrayOfString == null) || (paramArrayOfString.length == 0)) {
/* 287 */       return new Object[0];
/*     */     }
/* 289 */     Object[] arrayOfObject = new Object[paramArrayOfString.length];
/* 290 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/* 291 */       arrayOfObject[i] = get(paramArrayOfString[i]);
/*     */     }
/* 293 */     return arrayOfObject;
/*     */   }
/*     */ 
/*     */   public boolean containsKey(String paramString)
/*     */   {
/* 303 */     if ((paramString == null) || (paramString.trim().equals(""))) {
/* 304 */       return false;
/*     */     }
/* 306 */     return this.contents.containsKey(paramString);
/*     */   }
/*     */ 
/*     */   public boolean containsValue(Object paramObject)
/*     */   {
/* 316 */     return this.contents.containsValue(paramObject);
/*     */   }
/*     */ 
/*     */   public Collection<?> values()
/*     */   {
/* 328 */     return Collections.unmodifiableCollection(this.contents.values());
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 360 */     if (this == paramObject) {
/* 361 */       return true;
/*     */     }
/*     */ 
/* 365 */     if (!(paramObject instanceof Serializable)) {
/* 366 */       return false;
/*     */     }
/*     */ 
/* 369 */     CompositeData localCompositeData = (Serializable)paramObject;
/*     */ 
/* 372 */     if (!getCompositeType().equals(localCompositeData.getCompositeType())) {
/* 373 */       return false;
/*     */     }
/*     */ 
/* 376 */     if (this.contents.size() != localCompositeData.values().size()) {
/* 377 */       return false;
/*     */     }
/*     */ 
/* 380 */     for (Map.Entry localEntry : this.contents.entrySet()) {
/* 381 */       Object localObject1 = localEntry.getValue();
/* 382 */       Object localObject2 = localCompositeData.get((String)localEntry.getKey());
/*     */ 
/* 384 */       if (localObject1 != localObject2)
/*     */       {
/* 386 */         if (localObject1 == null) {
/* 387 */           return false;
/*     */         }
/* 389 */         boolean bool = localObject1.getClass().isArray() ? Arrays.deepEquals(new Object[] { localObject1 }, new Object[] { localObject2 }) : localObject1.equals(localObject2);
/*     */ 
/* 393 */         if (!bool) {
/* 394 */           return false;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 399 */     return true;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 425 */     int i = this.compositeType.hashCode();
/*     */ 
/* 427 */     for (Iterator localIterator = this.contents.values().iterator(); localIterator.hasNext(); ) { Object localObject = localIterator.next();
/* 428 */       if ((localObject instanceof Object[]))
/* 429 */         i += Arrays.deepHashCode((Object[])localObject);
/* 430 */       else if ((localObject instanceof byte[]))
/* 431 */         i += Arrays.hashCode((byte[])localObject);
/* 432 */       else if ((localObject instanceof short[]))
/* 433 */         i += Arrays.hashCode((short[])localObject);
/* 434 */       else if ((localObject instanceof int[]))
/* 435 */         i += Arrays.hashCode((int[])localObject);
/* 436 */       else if ((localObject instanceof long[]))
/* 437 */         i += Arrays.hashCode((long[])localObject);
/* 438 */       else if ((localObject instanceof char[]))
/* 439 */         i += Arrays.hashCode((char[])localObject);
/* 440 */       else if ((localObject instanceof float[]))
/* 441 */         i += Arrays.hashCode((float[])localObject);
/* 442 */       else if ((localObject instanceof double[]))
/* 443 */         i += Arrays.hashCode((double[])localObject);
/* 444 */       else if ((localObject instanceof boolean[]))
/* 445 */         i += Arrays.hashCode((boolean[])localObject);
/* 446 */       else if (localObject != null) {
/* 447 */         i += localObject.hashCode();
/*     */       }
/*     */     }
/* 450 */     return i;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 464 */     return getClass().getName() + "(compositeType=" + this.compositeType.toString() + ",contents=" + contentString() + ")";
/*     */   }
/*     */ 
/*     */   private String contentString()
/*     */   {
/* 475 */     StringBuilder localStringBuilder = new StringBuilder("{");
/* 476 */     String str1 = "";
/* 477 */     for (Map.Entry localEntry : this.contents.entrySet()) {
/* 478 */       localStringBuilder.append(str1).append((String)localEntry.getKey()).append("=");
/* 479 */       String str2 = Arrays.deepToString(new Object[] { localEntry.getValue() });
/* 480 */       localStringBuilder.append(str2.substring(1, str2.length() - 1));
/* 481 */       str1 = ", ";
/*     */     }
/* 483 */     localStringBuilder.append("}");
/* 484 */     return localStringBuilder.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.openmbean.CompositeDataSupport
 * JD-Core Version:    0.6.2
 */