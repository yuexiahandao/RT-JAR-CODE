/*     */ package javax.management.openmbean;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ 
/*     */ public class CompositeType extends OpenType<CompositeData>
/*     */ {
/*     */   static final long serialVersionUID = -5366242454346948798L;
/*     */   private TreeMap<String, String> nameToDescription;
/*     */   private TreeMap<String, OpenType<?>> nameToType;
/*  65 */   private transient Integer myHashCode = null;
/*  66 */   private transient String myToString = null;
/*  67 */   private transient Set<String> myNamesSet = null;
/*     */ 
/*     */   public CompositeType(String paramString1, String paramString2, String[] paramArrayOfString1, String[] paramArrayOfString2, OpenType<?>[] paramArrayOfOpenType)
/*     */     throws OpenDataException
/*     */   {
/* 126 */     super(CompositeData.class.getName(), paramString1, paramString2, false);
/*     */ 
/* 130 */     checkForNullElement(paramArrayOfString1, "itemNames");
/* 131 */     checkForNullElement(paramArrayOfString2, "itemDescriptions");
/* 132 */     checkForNullElement(paramArrayOfOpenType, "itemTypes");
/* 133 */     checkForEmptyString(paramArrayOfString1, "itemNames");
/* 134 */     checkForEmptyString(paramArrayOfString2, "itemDescriptions");
/*     */ 
/* 138 */     if ((paramArrayOfString1.length != paramArrayOfString2.length) || (paramArrayOfString1.length != paramArrayOfOpenType.length)) {
/* 139 */       throw new IllegalArgumentException("Array arguments itemNames[], itemDescriptions[] and itemTypes[] should be of same length (got " + paramArrayOfString1.length + ", " + paramArrayOfString2.length + " and " + paramArrayOfOpenType.length + ").");
/*     */     }
/*     */ 
/* 147 */     this.nameToDescription = new TreeMap();
/* 148 */     this.nameToType = new TreeMap();
/*     */ 
/* 150 */     for (int i = 0; i < paramArrayOfString1.length; i++) {
/* 151 */       String str = paramArrayOfString1[i].trim();
/* 152 */       if (this.nameToDescription.containsKey(str)) {
/* 153 */         throw new OpenDataException("Argument's element itemNames[" + i + "]=\"" + paramArrayOfString1[i] + "\" duplicates a previous item names.");
/*     */       }
/*     */ 
/* 156 */       this.nameToDescription.put(str, paramArrayOfString2[i].trim());
/* 157 */       this.nameToType.put(str, paramArrayOfOpenType[i]);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void checkForNullElement(Object[] paramArrayOfObject, String paramString) {
/* 162 */     if ((paramArrayOfObject == null) || (paramArrayOfObject.length == 0)) {
/* 163 */       throw new IllegalArgumentException("Argument " + paramString + "[] cannot be null or empty.");
/*     */     }
/* 165 */     for (int i = 0; i < paramArrayOfObject.length; i++)
/* 166 */       if (paramArrayOfObject[i] == null)
/* 167 */         throw new IllegalArgumentException("Argument's element " + paramString + "[" + i + "] cannot be null.");
/*     */   }
/*     */ 
/*     */   private static void checkForEmptyString(String[] paramArrayOfString, String paramString)
/*     */   {
/* 173 */     for (int i = 0; i < paramArrayOfString.length; i++)
/* 174 */       if (paramArrayOfString[i].trim().equals(""))
/* 175 */         throw new IllegalArgumentException("Argument's element " + paramString + "[" + i + "] cannot be an empty string.");
/*     */   }
/*     */ 
/*     */   public boolean containsKey(String paramString)
/*     */   {
/* 192 */     if (paramString == null) {
/* 193 */       return false;
/*     */     }
/* 195 */     return this.nameToDescription.containsKey(paramString);
/*     */   }
/*     */ 
/*     */   public String getDescription(String paramString)
/*     */   {
/* 209 */     if (paramString == null) {
/* 210 */       return null;
/*     */     }
/* 212 */     return (String)this.nameToDescription.get(paramString);
/*     */   }
/*     */ 
/*     */   public OpenType<?> getType(String paramString)
/*     */   {
/* 226 */     if (paramString == null) {
/* 227 */       return null;
/*     */     }
/* 229 */     return (OpenType)this.nameToType.get(paramString);
/*     */   }
/*     */ 
/*     */   public Set<String> keySet()
/*     */   {
/* 241 */     if (this.myNamesSet == null) {
/* 242 */       this.myNamesSet = Collections.unmodifiableSet(this.nameToDescription.keySet());
/*     */     }
/*     */ 
/* 245 */     return this.myNamesSet;
/*     */   }
/*     */ 
/*     */   public boolean isValue(Object paramObject)
/*     */   {
/* 305 */     if (!(paramObject instanceof CompositeData)) {
/* 306 */       return false;
/*     */     }
/*     */ 
/* 311 */     CompositeData localCompositeData = (CompositeData)paramObject;
/*     */ 
/* 315 */     CompositeType localCompositeType = localCompositeData.getCompositeType();
/* 316 */     return isAssignableFrom(localCompositeType);
/*     */   }
/*     */ 
/*     */   boolean isAssignableFrom(OpenType<?> paramOpenType)
/*     */   {
/* 333 */     if (!(paramOpenType instanceof CompositeType))
/* 334 */       return false;
/* 335 */     CompositeType localCompositeType = (CompositeType)paramOpenType;
/* 336 */     if (!localCompositeType.getTypeName().equals(getTypeName()))
/* 337 */       return false;
/* 338 */     for (String str : keySet()) {
/* 339 */       OpenType localOpenType1 = localCompositeType.getType(str);
/* 340 */       OpenType localOpenType2 = getType(str);
/* 341 */       if ((localOpenType1 == null) || (!localOpenType2.isAssignableFrom(localOpenType1)))
/*     */       {
/* 343 */         return false;
/*     */       }
/*     */     }
/* 345 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 369 */     if (paramObject == null) {
/* 370 */       return false;
/*     */     }
/*     */ 
/*     */     CompositeType localCompositeType;
/*     */     try
/*     */     {
/* 377 */       localCompositeType = (CompositeType)paramObject;
/*     */     } catch (ClassCastException localClassCastException) {
/* 379 */       return false;
/*     */     }
/*     */ 
/* 386 */     if (!getTypeName().equals(localCompositeType.getTypeName())) {
/* 387 */       return false;
/*     */     }
/*     */ 
/* 391 */     if (!this.nameToType.equals(localCompositeType.nameToType)) {
/* 392 */       return false;
/*     */     }
/*     */ 
/* 397 */     return true;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 420 */     if (this.myHashCode == null) {
/* 421 */       int i = 0;
/* 422 */       i += getTypeName().hashCode();
/* 423 */       for (String str : this.nameToDescription.keySet()) {
/* 424 */         i += str.hashCode();
/* 425 */         i += ((OpenType)this.nameToType.get(str)).hashCode();
/*     */       }
/* 427 */       this.myHashCode = Integer.valueOf(i);
/*     */     }
/*     */ 
/* 432 */     return this.myHashCode.intValue();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 451 */     if (this.myToString == null) {
/* 452 */       StringBuilder localStringBuilder = new StringBuilder();
/* 453 */       localStringBuilder.append(getClass().getName());
/* 454 */       localStringBuilder.append("(name=");
/* 455 */       localStringBuilder.append(getTypeName());
/* 456 */       localStringBuilder.append(",items=(");
/* 457 */       int i = 0;
/* 458 */       Iterator localIterator = this.nameToType.keySet().iterator();
/*     */ 
/* 460 */       while (localIterator.hasNext()) {
/* 461 */         String str = (String)localIterator.next();
/* 462 */         if (i > 0) localStringBuilder.append(",");
/* 463 */         localStringBuilder.append("(itemName=");
/* 464 */         localStringBuilder.append(str);
/* 465 */         localStringBuilder.append(",itemType=");
/* 466 */         localStringBuilder.append(((OpenType)this.nameToType.get(str)).toString() + ")");
/* 467 */         i++;
/*     */       }
/* 469 */       localStringBuilder.append("))");
/* 470 */       this.myToString = localStringBuilder.toString();
/*     */     }
/*     */ 
/* 475 */     return this.myToString;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.openmbean.CompositeType
 * JD-Core Version:    0.6.2
 */