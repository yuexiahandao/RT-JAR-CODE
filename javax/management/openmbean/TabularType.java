/*     */ package javax.management.openmbean;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ public class TabularType extends OpenType<TabularData>
/*     */ {
/*     */   static final long serialVersionUID = 6554071860220659261L;
/*     */   private CompositeType rowType;
/*     */   private List<String> indexNames;
/*  65 */   private transient Integer myHashCode = null;
/*  66 */   private transient String myToString = null;
/*     */ 
/*     */   public TabularType(String paramString1, String paramString2, CompositeType paramCompositeType, String[] paramArrayOfString)
/*     */     throws OpenDataException
/*     */   {
/* 111 */     super(TabularData.class.getName(), paramString1, paramString2, false);
/*     */ 
/* 115 */     if (paramCompositeType == null) {
/* 116 */       throw new IllegalArgumentException("Argument rowType cannot be null.");
/*     */     }
/*     */ 
/* 121 */     checkForNullElement(paramArrayOfString, "indexNames");
/* 122 */     checkForEmptyString(paramArrayOfString, "indexNames");
/*     */ 
/* 126 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/* 127 */       if (!paramCompositeType.containsKey(paramArrayOfString[i])) {
/* 128 */         throw new OpenDataException("Argument's element value indexNames[" + i + "]=\"" + paramArrayOfString[i] + "\" is not a valid item name for rowType.");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 135 */     this.rowType = paramCompositeType;
/*     */ 
/* 141 */     ArrayList localArrayList = new ArrayList(paramArrayOfString.length + 1);
/* 142 */     for (int j = 0; j < paramArrayOfString.length; j++) {
/* 143 */       localArrayList.add(paramArrayOfString[j]);
/*     */     }
/* 145 */     this.indexNames = Collections.unmodifiableList(localArrayList);
/*     */   }
/*     */ 
/*     */   private static void checkForNullElement(Object[] paramArrayOfObject, String paramString)
/*     */   {
/* 153 */     if ((paramArrayOfObject == null) || (paramArrayOfObject.length == 0)) {
/* 154 */       throw new IllegalArgumentException("Argument " + paramString + "[] cannot be null or empty.");
/*     */     }
/* 156 */     for (int i = 0; i < paramArrayOfObject.length; i++)
/* 157 */       if (paramArrayOfObject[i] == null)
/* 158 */         throw new IllegalArgumentException("Argument's element " + paramString + "[" + i + "] cannot be null.");
/*     */   }
/*     */ 
/*     */   private static void checkForEmptyString(String[] paramArrayOfString, String paramString)
/*     */   {
/* 167 */     for (int i = 0; i < paramArrayOfString.length; i++)
/* 168 */       if (paramArrayOfString[i].trim().equals(""))
/* 169 */         throw new IllegalArgumentException("Argument's element " + paramString + "[" + i + "] cannot be an empty string.");
/*     */   }
/*     */ 
/*     */   public CompositeType getRowType()
/*     */   {
/* 185 */     return this.rowType;
/*     */   }
/*     */ 
/*     */   public List<String> getIndexNames()
/*     */   {
/* 201 */     return this.indexNames;
/*     */   }
/*     */ 
/*     */   public boolean isValue(Object paramObject)
/*     */   {
/* 229 */     if (!(paramObject instanceof TabularData)) {
/* 230 */       return false;
/*     */     }
/*     */ 
/* 234 */     TabularData localTabularData = (TabularData)paramObject;
/* 235 */     TabularType localTabularType = localTabularData.getTabularType();
/* 236 */     return isAssignableFrom(localTabularType);
/*     */   }
/*     */ 
/*     */   boolean isAssignableFrom(OpenType<?> paramOpenType)
/*     */   {
/* 241 */     if (!(paramOpenType instanceof TabularType))
/* 242 */       return false;
/* 243 */     TabularType localTabularType = (TabularType)paramOpenType;
/* 244 */     if ((!getTypeName().equals(localTabularType.getTypeName())) || (!getIndexNames().equals(localTabularType.getIndexNames())))
/*     */     {
/* 246 */       return false;
/* 247 */     }return getRowType().isAssignableFrom(localTabularType.getRowType());
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 272 */     if (paramObject == null) {
/* 273 */       return false;
/*     */     }
/*     */ 
/*     */     TabularType localTabularType;
/*     */     try
/*     */     {
/* 280 */       localTabularType = (TabularType)paramObject;
/*     */     } catch (ClassCastException localClassCastException) {
/* 282 */       return false;
/*     */     }
/*     */ 
/* 289 */     if (!getTypeName().equals(localTabularType.getTypeName())) {
/* 290 */       return false;
/*     */     }
/*     */ 
/* 294 */     if (!this.rowType.equals(localTabularType.rowType)) {
/* 295 */       return false;
/*     */     }
/*     */ 
/* 299 */     if (!this.indexNames.equals(localTabularType.indexNames)) {
/* 300 */       return false;
/*     */     }
/*     */ 
/* 305 */     return true;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 328 */     if (this.myHashCode == null) {
/* 329 */       int i = 0;
/* 330 */       i += getTypeName().hashCode();
/* 331 */       i += this.rowType.hashCode();
/* 332 */       for (String str : this.indexNames)
/* 333 */         i += str.hashCode();
/* 334 */       this.myHashCode = Integer.valueOf(i);
/*     */     }
/*     */ 
/* 339 */     return this.myHashCode.intValue();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 358 */     if (this.myToString == null) {
/* 359 */       StringBuilder localStringBuilder = new StringBuilder().append(getClass().getName()).append("(name=").append(getTypeName()).append(",rowType=").append(this.rowType.toString()).append(",indexNames=(");
/*     */ 
/* 366 */       String str1 = "";
/* 367 */       for (String str2 : this.indexNames) {
/* 368 */         localStringBuilder.append(str1).append(str2);
/* 369 */         str1 = ",";
/*     */       }
/* 371 */       localStringBuilder.append("))");
/* 372 */       this.myToString = localStringBuilder.toString();
/*     */     }
/*     */ 
/* 377 */     return this.myToString;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.openmbean.TabularType
 * JD-Core Version:    0.6.2
 */