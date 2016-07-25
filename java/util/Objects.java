/*     */ package java.util;
/*     */ 
/*     */ public final class Objects
/*     */ {
/*     */   private Objects()
/*     */   {
/*  38 */     throw new AssertionError("No java.util.Objects instances for you!");
/*     */   }
/*     */ 
/*     */   public static boolean equals(Object paramObject1, Object paramObject2)
/*     */   {
/*  57 */     return (paramObject1 == paramObject2) || ((paramObject1 != null) && (paramObject1.equals(paramObject2)));
/*     */   }
/*     */ 
/*     */   public static boolean deepEquals(Object paramObject1, Object paramObject2)
/*     */   {
/*  78 */     if (paramObject1 == paramObject2)
/*  79 */       return true;
/*  80 */     if ((paramObject1 == null) || (paramObject2 == null)) {
/*  81 */       return false;
/*     */     }
/*  83 */     return Arrays.deepEquals0(paramObject1, paramObject2);
/*     */   }
/*     */ 
/*     */   public static int hashCode(Object paramObject)
/*     */   {
/*  96 */     return paramObject != null ? paramObject.hashCode() : 0;
/*     */   }
/*     */ 
/*     */   public static int hash(Object[] paramArrayOfObject)
/*     */   {
/* 126 */     return Arrays.hashCode(paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   public static String toString(Object paramObject)
/*     */   {
/* 140 */     return String.valueOf(paramObject);
/*     */   }
/*     */ 
/*     */   public static String toString(Object paramObject, String paramString)
/*     */   {
/* 157 */     return paramObject != null ? paramObject.toString() : paramString;
/*     */   }
/*     */ 
/*     */   public static <T> int compare(T paramT1, T paramT2, Comparator<? super T> paramComparator)
/*     */   {
/* 181 */     return paramT1 == paramT2 ? 0 : paramComparator.compare(paramT1, paramT2);
/*     */   }
/*     */ 
/*     */   public static <T> T requireNonNull(T paramT)
/*     */   {
/* 200 */     if (paramT == null)
/* 201 */       throw new NullPointerException();
/* 202 */     return paramT;
/*     */   }
/*     */ 
/*     */   public static <T> T requireNonNull(T paramT, String paramString)
/*     */   {
/* 225 */     if (paramT == null)
/* 226 */       throw new NullPointerException(paramString);
/* 227 */     return paramT;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.Objects
 * JD-Core Version:    0.6.2
 */