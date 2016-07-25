/*     */ package java.beans;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ 
/*     */ class NameGenerator
/*     */ {
/*     */   private Map valueToName;
/*     */   private Map nameToCount;
/*     */ 
/*     */   public NameGenerator()
/*     */   {
/*  50 */     this.valueToName = new IdentityHashMap();
/*  51 */     this.nameToCount = new HashMap();
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/*  59 */     this.valueToName.clear();
/*  60 */     this.nameToCount.clear();
/*     */   }
/*     */ 
/*     */   public static String unqualifiedClassName(Class paramClass)
/*     */   {
/*  67 */     if (paramClass.isArray()) {
/*  68 */       return unqualifiedClassName(paramClass.getComponentType()) + "Array";
/*     */     }
/*  70 */     String str = paramClass.getName();
/*  71 */     return str.substring(str.lastIndexOf('.') + 1);
/*     */   }
/*     */ 
/*     */   public static String capitalize(String paramString)
/*     */   {
/*  78 */     if ((paramString == null) || (paramString.length() == 0)) {
/*  79 */       return paramString;
/*     */     }
/*  81 */     return paramString.substring(0, 1).toUpperCase(Locale.ENGLISH) + paramString.substring(1);
/*     */   }
/*     */ 
/*     */   public String instanceName(Object paramObject)
/*     */   {
/*  93 */     if (paramObject == null) {
/*  94 */       return "null";
/*     */     }
/*  96 */     if ((paramObject instanceof Class)) {
/*  97 */       return unqualifiedClassName((Class)paramObject);
/*     */     }
/*     */ 
/* 100 */     String str1 = (String)this.valueToName.get(paramObject);
/* 101 */     if (str1 != null) {
/* 102 */       return str1;
/*     */     }
/* 104 */     Class localClass = paramObject.getClass();
/* 105 */     String str2 = unqualifiedClassName(localClass);
/*     */ 
/* 107 */     Object localObject = this.nameToCount.get(str2);
/* 108 */     int i = localObject == null ? 0 : ((Integer)localObject).intValue() + 1;
/* 109 */     this.nameToCount.put(str2, new Integer(i));
/*     */ 
/* 111 */     str1 = str2 + i;
/* 112 */     this.valueToName.put(paramObject, str1);
/* 113 */     return str1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.beans.NameGenerator
 * JD-Core Version:    0.6.2
 */