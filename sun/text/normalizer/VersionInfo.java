/*     */ package sun.text.normalizer;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ 
/*     */ public final class VersionInfo
/*     */ {
/*     */   private int m_version_;
/* 156 */   private static final HashMap MAP_ = new HashMap();
/*     */   private static final String INVALID_VERSION_NUMBER_ = "Invalid version number: Version number may be negative or greater than 255";
/*     */ 
/*     */   public static VersionInfo getInstance(String paramString)
/*     */   {
/*  66 */     int i = paramString.length();
/*  67 */     int[] arrayOfInt = { 0, 0, 0, 0 };
/*  68 */     int j = 0;
/*  69 */     int k = 0;
/*     */ 
/*  71 */     while ((j < 4) && (k < i)) {
/*  72 */       m = paramString.charAt(k);
/*  73 */       if (m == 46) {
/*  74 */         j++;
/*     */       }
/*     */       else {
/*  77 */         m = (char)(m - 48);
/*  78 */         if ((m < 0) || (m > 9)) {
/*  79 */           throw new IllegalArgumentException("Invalid version number: Version number may be negative or greater than 255");
/*     */         }
/*  81 */         arrayOfInt[j] *= 10;
/*  82 */         arrayOfInt[j] += m;
/*     */       }
/*  84 */       k++;
/*     */     }
/*  86 */     if (k != i) {
/*  87 */       throw new IllegalArgumentException("Invalid version number: String '" + paramString + "' exceeds version format");
/*     */     }
/*     */ 
/*  90 */     for (int m = 0; m < 4; m++) {
/*  91 */       if ((arrayOfInt[m] < 0) || (arrayOfInt[m] > 255)) {
/*  92 */         throw new IllegalArgumentException("Invalid version number: Version number may be negative or greater than 255");
/*     */       }
/*     */     }
/*     */ 
/*  96 */     return getInstance(arrayOfInt[0], arrayOfInt[1], arrayOfInt[2], arrayOfInt[3]);
/*     */   }
/*     */ 
/*     */   public static VersionInfo getInstance(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 114 */     if ((paramInt1 < 0) || (paramInt1 > 255) || (paramInt2 < 0) || (paramInt2 > 255) || (paramInt3 < 0) || (paramInt3 > 255) || (paramInt4 < 0) || (paramInt4 > 255))
/*     */     {
/* 116 */       throw new IllegalArgumentException("Invalid version number: Version number may be negative or greater than 255");
/*     */     }
/* 118 */     int i = getInt(paramInt1, paramInt2, paramInt3, paramInt4);
/* 119 */     Integer localInteger = Integer.valueOf(i);
/* 120 */     Object localObject = MAP_.get(localInteger);
/* 121 */     if (localObject == null) {
/* 122 */       localObject = new VersionInfo(i);
/* 123 */       MAP_.put(localInteger, localObject);
/*     */     }
/* 125 */     return (VersionInfo)localObject;
/*     */   }
/*     */ 
/*     */   public int compareTo(VersionInfo paramVersionInfo)
/*     */   {
/* 141 */     return this.m_version_ - paramVersionInfo.m_version_;
/*     */   }
/*     */ 
/*     */   private VersionInfo(int paramInt)
/*     */   {
/* 171 */     this.m_version_ = paramInt;
/*     */   }
/*     */ 
/*     */   private static int getInt(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 183 */     return paramInt1 << 24 | paramInt2 << 16 | paramInt3 << 8 | paramInt4;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.text.normalizer.VersionInfo
 * JD-Core Version:    0.6.2
 */