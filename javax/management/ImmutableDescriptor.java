/*     */ package javax.management;
/*     */ 
/*     */ import com.sun.jmx.mbeanserver.Util;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.TreeMap;
/*     */ 
/*     */ public class ImmutableDescriptor
/*     */   implements Descriptor
/*     */ {
/*     */   private static final long serialVersionUID = 8853308591080540165L;
/*     */   private final String[] names;
/*     */   private final Object[] values;
/*  57 */   private transient int hashCode = -1;
/*     */ 
/*  62 */   public static final ImmutableDescriptor EMPTY_DESCRIPTOR = new ImmutableDescriptor(new String[0]);
/*     */ 
/*     */   public ImmutableDescriptor(String[] paramArrayOfString, Object[] paramArrayOfObject)
/*     */   {
/*  74 */     this(makeMap(paramArrayOfString, paramArrayOfObject));
/*     */   }
/*     */ 
/*     */   public ImmutableDescriptor(String[] paramArrayOfString)
/*     */   {
/*  90 */     this(makeMap(paramArrayOfString));
/*     */   }
/*     */ 
/*     */   public ImmutableDescriptor(Map<String, ?> paramMap)
/*     */   {
/* 103 */     if (paramMap == null)
/* 104 */       throw new IllegalArgumentException("Null Map");
/* 105 */     TreeMap localTreeMap = new TreeMap(String.CASE_INSENSITIVE_ORDER);
/*     */ 
/* 107 */     for (Map.Entry localEntry : paramMap.entrySet()) {
/* 108 */       String str = (String)localEntry.getKey();
/* 109 */       if ((str == null) || (str.equals("")))
/* 110 */         throw new IllegalArgumentException("Empty or null field name");
/* 111 */       if (localTreeMap.containsKey(str))
/* 112 */         throw new IllegalArgumentException("Duplicate name: " + str);
/* 113 */       localTreeMap.put(str, localEntry.getValue());
/*     */     }
/* 115 */     int i = localTreeMap.size();
/* 116 */     this.names = ((String[])localTreeMap.keySet().toArray(new String[i]));
/* 117 */     this.values = localTreeMap.values().toArray(new Object[i]);
/*     */   }
/*     */ 
/*     */   private Object readResolve()
/*     */     throws InvalidObjectException
/*     */   {
/* 132 */     int i = 0;
/* 133 */     if ((this.names == null) || (this.values == null) || (this.names.length != this.values.length))
/* 134 */       i = 1;
/* 135 */     if (i == 0) {
/* 136 */       if ((this.names.length == 0) && (getClass() == ImmutableDescriptor.class))
/* 137 */         return EMPTY_DESCRIPTOR;
/* 138 */       Comparator localComparator = String.CASE_INSENSITIVE_ORDER;
/* 139 */       String str = "";
/* 140 */       for (int j = 0; j < this.names.length; j++) {
/* 141 */         if ((this.names[j] == null) || (localComparator.compare(str, this.names[j]) >= 0))
/*     */         {
/* 143 */           i = 1;
/* 144 */           break;
/*     */         }
/* 146 */         str = this.names[j];
/*     */       }
/*     */     }
/* 149 */     if (i != 0) {
/* 150 */       throw new InvalidObjectException("Bad names or values");
/*     */     }
/* 152 */     return this;
/*     */   }
/*     */ 
/*     */   private static SortedMap<String, ?> makeMap(String[] paramArrayOfString, Object[] paramArrayOfObject)
/*     */   {
/* 157 */     if ((paramArrayOfString == null) || (paramArrayOfObject == null))
/* 158 */       throw new IllegalArgumentException("Null array parameter");
/* 159 */     if (paramArrayOfString.length != paramArrayOfObject.length)
/* 160 */       throw new IllegalArgumentException("Different size arrays");
/* 161 */     TreeMap localTreeMap = new TreeMap(String.CASE_INSENSITIVE_ORDER);
/*     */ 
/* 163 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/* 164 */       String str = paramArrayOfString[i];
/* 165 */       if ((str == null) || (str.equals("")))
/* 166 */         throw new IllegalArgumentException("Empty or null field name");
/* 167 */       Object localObject = localTreeMap.put(str, paramArrayOfObject[i]);
/* 168 */       if (localObject != null) {
/* 169 */         throw new IllegalArgumentException("Duplicate field name: " + str);
/*     */       }
/*     */     }
/*     */ 
/* 173 */     return localTreeMap;
/*     */   }
/*     */ 
/*     */   private static SortedMap<String, ?> makeMap(String[] paramArrayOfString) {
/* 177 */     if (paramArrayOfString == null)
/* 178 */       throw new IllegalArgumentException("Null fields parameter");
/* 179 */     String[] arrayOfString1 = new String[paramArrayOfString.length];
/* 180 */     String[] arrayOfString2 = new String[paramArrayOfString.length];
/* 181 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/* 182 */       String str = paramArrayOfString[i];
/* 183 */       int j = str.indexOf('=');
/* 184 */       if (j < 0) {
/* 185 */         throw new IllegalArgumentException("Missing = character: " + str);
/*     */       }
/*     */ 
/* 188 */       arrayOfString1[i] = str.substring(0, j);
/*     */ 
/* 190 */       arrayOfString2[i] = str.substring(j + 1);
/*     */     }
/* 192 */     return makeMap(arrayOfString1, arrayOfString2);
/*     */   }
/*     */ 
/*     */   public static ImmutableDescriptor union(Descriptor[] paramArrayOfDescriptor)
/*     */   {
/* 229 */     int i = findNonEmpty(paramArrayOfDescriptor, 0);
/* 230 */     if (i < 0)
/* 231 */       return EMPTY_DESCRIPTOR;
/* 232 */     if (((paramArrayOfDescriptor[i] instanceof ImmutableDescriptor)) && (findNonEmpty(paramArrayOfDescriptor, i + 1) < 0))
/*     */     {
/* 234 */       return (ImmutableDescriptor)paramArrayOfDescriptor[i];
/*     */     }
/* 236 */     TreeMap localTreeMap = new TreeMap(String.CASE_INSENSITIVE_ORDER);
/*     */ 
/* 238 */     Object localObject1 = EMPTY_DESCRIPTOR;
/* 239 */     for (Descriptor localDescriptor : paramArrayOfDescriptor) {
/* 240 */       if (localDescriptor != null)
/*     */       {
/*     */         Object localObject2;
/*     */         String[] arrayOfString;
/* 242 */         if ((localDescriptor instanceof ImmutableDescriptor)) {
/* 243 */           localObject2 = (ImmutableDescriptor)localDescriptor;
/* 244 */           arrayOfString = ((ImmutableDescriptor)localObject2).names;
/* 245 */           if ((localObject2.getClass() == ImmutableDescriptor.class) && (arrayOfString.length > ((ImmutableDescriptor)localObject1).names.length))
/*     */           {
/* 247 */             localObject1 = localObject2;
/*     */           }
/*     */         } else { arrayOfString = localDescriptor.getFieldNames(); }
/* 250 */         for (String str1 : arrayOfString) {
/* 251 */           Object localObject3 = localDescriptor.getFieldValue(str1);
/* 252 */           Object localObject4 = localTreeMap.put(str1, localObject3);
/* 253 */           if (localObject4 != null)
/*     */           {
/*     */             boolean bool;
/* 255 */             if (localObject4.getClass().isArray()) {
/* 256 */               bool = Arrays.deepEquals(new Object[] { localObject4 }, new Object[] { localObject3 });
/*     */             }
/*     */             else
/* 259 */               bool = localObject4.equals(localObject3);
/* 260 */             if (!bool) {
/* 261 */               String str2 = "Inconsistent values for descriptor field " + str1 + ": " + localObject4 + " :: " + localObject3;
/*     */ 
/* 264 */               throw new IllegalArgumentException(str2);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 270 */     if (((ImmutableDescriptor)localObject1).names.length == localTreeMap.size())
/* 271 */       return localObject1;
/* 272 */     return new ImmutableDescriptor(localTreeMap);
/*     */   }
/*     */ 
/*     */   private static boolean isEmpty(Descriptor paramDescriptor) {
/* 276 */     if (paramDescriptor == null)
/* 277 */       return true;
/* 278 */     if ((paramDescriptor instanceof ImmutableDescriptor)) {
/* 279 */       return ((ImmutableDescriptor)paramDescriptor).names.length == 0;
/*     */     }
/* 281 */     return paramDescriptor.getFieldNames().length == 0;
/*     */   }
/*     */ 
/*     */   private static int findNonEmpty(Descriptor[] paramArrayOfDescriptor, int paramInt) {
/* 285 */     for (int i = paramInt; i < paramArrayOfDescriptor.length; i++) {
/* 286 */       if (!isEmpty(paramArrayOfDescriptor[i]))
/* 287 */         return i;
/*     */     }
/* 289 */     return -1;
/*     */   }
/*     */ 
/*     */   private int fieldIndex(String paramString) {
/* 293 */     return Arrays.binarySearch(this.names, paramString, String.CASE_INSENSITIVE_ORDER);
/*     */   }
/*     */ 
/*     */   public final Object getFieldValue(String paramString) {
/* 297 */     checkIllegalFieldName(paramString);
/* 298 */     int i = fieldIndex(paramString);
/* 299 */     if (i < 0)
/* 300 */       return null;
/* 301 */     Object localObject1 = this.values[i];
/* 302 */     if ((localObject1 == null) || (!localObject1.getClass().isArray()))
/* 303 */       return localObject1;
/* 304 */     if ((localObject1 instanceof Object[])) {
/* 305 */       return ((Object[])localObject1).clone();
/*     */     }
/* 307 */     int j = Array.getLength(localObject1);
/* 308 */     Object localObject2 = Array.newInstance(localObject1.getClass().getComponentType(), j);
/* 309 */     System.arraycopy(localObject1, 0, localObject2, 0, j);
/* 310 */     return localObject2;
/*     */   }
/*     */ 
/*     */   public final String[] getFields() {
/* 314 */     String[] arrayOfString = new String[this.names.length];
/* 315 */     for (int i = 0; i < arrayOfString.length; i++) {
/* 316 */       Object localObject = this.values[i];
/* 317 */       if (localObject == null)
/* 318 */         localObject = "";
/* 319 */       else if (!(localObject instanceof String))
/* 320 */         localObject = "(" + localObject + ")";
/* 321 */       arrayOfString[i] = (this.names[i] + "=" + localObject);
/*     */     }
/* 323 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   public final Object[] getFieldValues(String[] paramArrayOfString) {
/* 327 */     if (paramArrayOfString == null)
/* 328 */       return (Object[])this.values.clone();
/* 329 */     Object[] arrayOfObject = new Object[paramArrayOfString.length];
/* 330 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/* 331 */       String str = paramArrayOfString[i];
/* 332 */       if ((str != null) && (!str.equals("")))
/* 333 */         arrayOfObject[i] = getFieldValue(str);
/*     */     }
/* 335 */     return arrayOfObject;
/*     */   }
/*     */ 
/*     */   public final String[] getFieldNames() {
/* 339 */     return (String[])this.names.clone();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 368 */     if (paramObject == this)
/* 369 */       return true;
/* 370 */     if (!(paramObject instanceof Descriptor))
/* 371 */       return false;
/*     */     String[] arrayOfString;
/* 373 */     if ((paramObject instanceof ImmutableDescriptor)) {
/* 374 */       arrayOfString = ((ImmutableDescriptor)paramObject).names;
/*     */     } else {
/* 376 */       arrayOfString = ((Descriptor)paramObject).getFieldNames();
/* 377 */       Arrays.sort(arrayOfString, String.CASE_INSENSITIVE_ORDER);
/*     */     }
/* 379 */     if (this.names.length != arrayOfString.length)
/* 380 */       return false;
/* 381 */     for (int i = 0; i < this.names.length; i++)
/* 382 */       if (!this.names[i].equalsIgnoreCase(arrayOfString[i]))
/* 383 */         return false;
/*     */     Object[] arrayOfObject;
/* 386 */     if ((paramObject instanceof ImmutableDescriptor))
/* 387 */       arrayOfObject = ((ImmutableDescriptor)paramObject).values;
/*     */     else
/* 389 */       arrayOfObject = ((Descriptor)paramObject).getFieldValues(arrayOfString);
/* 390 */     return Arrays.deepEquals(this.values, arrayOfObject);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 417 */     if (this.hashCode == -1) {
/* 418 */       this.hashCode = Util.hashCode(this.names, this.values);
/*     */     }
/* 420 */     return this.hashCode;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 425 */     StringBuilder localStringBuilder = new StringBuilder("{");
/* 426 */     for (int i = 0; i < this.names.length; i++) {
/* 427 */       if (i > 0)
/* 428 */         localStringBuilder.append(", ");
/* 429 */       localStringBuilder.append(this.names[i]).append("=");
/* 430 */       Object localObject = this.values[i];
/* 431 */       if ((localObject != null) && (localObject.getClass().isArray())) {
/* 432 */         String str = Arrays.deepToString(new Object[] { localObject });
/* 433 */         str = str.substring(1, str.length() - 1);
/* 434 */         localObject = str;
/*     */       }
/* 436 */       localStringBuilder.append(String.valueOf(localObject));
/*     */     }
/* 438 */     return "}";
/*     */   }
/*     */ 
/*     */   public boolean isValid()
/*     */   {
/* 453 */     return true;
/*     */   }
/*     */ 
/*     */   public Descriptor clone()
/*     */   {
/* 472 */     return this;
/*     */   }
/*     */ 
/*     */   public final void setFields(String[] paramArrayOfString, Object[] paramArrayOfObject)
/*     */     throws RuntimeOperationsException
/*     */   {
/* 486 */     if ((paramArrayOfString == null) || (paramArrayOfObject == null))
/* 487 */       illegal("Null argument");
/* 488 */     if (paramArrayOfString.length != paramArrayOfObject.length)
/* 489 */       illegal("Different array sizes");
/* 490 */     for (int i = 0; i < paramArrayOfString.length; i++)
/* 491 */       checkIllegalFieldName(paramArrayOfString[i]);
/* 492 */     for (i = 0; i < paramArrayOfString.length; i++)
/* 493 */       setField(paramArrayOfString[i], paramArrayOfObject[i]);
/*     */   }
/*     */ 
/*     */   public final void setField(String paramString, Object paramObject)
/*     */     throws RuntimeOperationsException
/*     */   {
/* 507 */     checkIllegalFieldName(paramString);
/* 508 */     int i = fieldIndex(paramString);
/* 509 */     if (i < 0)
/* 510 */       unsupported();
/* 511 */     Object localObject = this.values[i];
/* 512 */     if (localObject == null ? paramObject != null : !localObject.equals(paramObject))
/*     */     {
/* 515 */       unsupported();
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void removeField(String paramString)
/*     */   {
/* 530 */     if ((paramString != null) && (fieldIndex(paramString) >= 0))
/* 531 */       unsupported();
/*     */   }
/*     */ 
/*     */   static Descriptor nonNullDescriptor(Descriptor paramDescriptor) {
/* 535 */     if (paramDescriptor == null) {
/* 536 */       return EMPTY_DESCRIPTOR;
/*     */     }
/* 538 */     return paramDescriptor;
/*     */   }
/*     */ 
/*     */   private static void checkIllegalFieldName(String paramString) {
/* 542 */     if ((paramString == null) || (paramString.equals("")))
/* 543 */       illegal("Null or empty field name");
/*     */   }
/*     */ 
/*     */   private static void unsupported() {
/* 547 */     UnsupportedOperationException localUnsupportedOperationException = new UnsupportedOperationException("Descriptor is read-only");
/*     */ 
/* 549 */     throw new RuntimeOperationsException(localUnsupportedOperationException);
/*     */   }
/*     */ 
/*     */   private static void illegal(String paramString) {
/* 553 */     IllegalArgumentException localIllegalArgumentException = new IllegalArgumentException(paramString);
/* 554 */     throw new RuntimeOperationsException(localIllegalArgumentException);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.ImmutableDescriptor
 * JD-Core Version:    0.6.2
 */