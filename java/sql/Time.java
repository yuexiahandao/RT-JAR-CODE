/*     */ package java.sql;
/*     */ 
/*     */ import java.util.Date;
/*     */ 
/*     */ public class Time extends Date
/*     */ {
/*     */   static final long serialVersionUID = 8397324403548013681L;
/*     */ 
/*     */   @Deprecated
/*     */   public Time(int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/*  58 */     super(70, 0, 1, paramInt1, paramInt2, paramInt3);
/*     */   }
/*     */ 
/*     */   public Time(long paramLong)
/*     */   {
/*  69 */     super(paramLong);
/*     */   }
/*     */ 
/*     */   public void setTime(long paramLong)
/*     */   {
/*  80 */     super.setTime(paramLong);
/*     */   }
/*     */ 
/*     */   public static Time valueOf(String paramString)
/*     */   {
/*  96 */     if (paramString == null) throw new IllegalArgumentException();
/*     */ 
/*  98 */     int m = paramString.indexOf(':');
/*  99 */     int n = paramString.indexOf(':', m + 1);
/*     */     int i;
/*     */     int j;
/*     */     int k;
/* 100 */     if (((m > 0 ? 1 : 0) & (n > 0 ? 1 : 0) & (n < paramString.length() - 1 ? 1 : 0)) != 0)
/*     */     {
/* 102 */       i = Integer.parseInt(paramString.substring(0, m));
/* 103 */       j = Integer.parseInt(paramString.substring(m + 1, n));
/*     */ 
/* 105 */       k = Integer.parseInt(paramString.substring(n + 1));
/*     */     } else {
/* 107 */       throw new IllegalArgumentException();
/*     */     }
/*     */ 
/* 110 */     return new Time(i, j, k);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 119 */     int i = super.getHours();
/* 120 */     int j = super.getMinutes();
/* 121 */     int k = super.getSeconds();
/*     */     String str1;
/* 126 */     if (i < 10)
/* 127 */       str1 = "0" + i;
/*     */     else
/* 129 */       str1 = Integer.toString(i);
/*     */     String str2;
/* 131 */     if (j < 10)
/* 132 */       str2 = "0" + j;
/*     */     else
/* 134 */       str2 = Integer.toString(j);
/*     */     String str3;
/* 136 */     if (k < 10)
/* 137 */       str3 = "0" + k;
/*     */     else {
/* 139 */       str3 = Integer.toString(k);
/*     */     }
/* 141 */     return str1 + ":" + str2 + ":" + str3;
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public int getYear()
/*     */   {
/* 157 */     throw new IllegalArgumentException();
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public int getMonth()
/*     */   {
/* 171 */     throw new IllegalArgumentException();
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public int getDay()
/*     */   {
/* 184 */     throw new IllegalArgumentException();
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public int getDate()
/*     */   {
/* 198 */     throw new IllegalArgumentException();
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public void setYear(int paramInt)
/*     */   {
/* 212 */     throw new IllegalArgumentException();
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public void setMonth(int paramInt)
/*     */   {
/* 226 */     throw new IllegalArgumentException();
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public void setDate(int paramInt)
/*     */   {
/* 240 */     throw new IllegalArgumentException();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.sql.Time
 * JD-Core Version:    0.6.2
 */