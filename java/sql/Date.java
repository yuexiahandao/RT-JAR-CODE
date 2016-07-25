/*     */ package java.sql;
/*     */ 
/*     */ public class Date extends java.util.Date
/*     */ {
/*     */   static final long serialVersionUID = 1511598038487230103L;
/*     */ 
/*     */   /** @deprecated */
/*     */   public Date(int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/*  55 */     super(paramInt1, paramInt2, paramInt3);
/*     */   }
/*     */ 
/*     */   public Date(long paramLong)
/*     */   {
/*  72 */     super(paramLong);
/*     */   }
/*     */ 
/*     */   public void setTime(long paramLong)
/*     */   {
/*  91 */     super.setTime(paramLong);
/*     */   }
/*     */ 
/*     */   public static Date valueOf(String paramString)
/*     */   {
/* 114 */     Date localDate = null;
/*     */ 
/* 116 */     if (paramString == null) {
/* 117 */       throw new IllegalArgumentException();
/*     */     }
/*     */ 
/* 120 */     int i = paramString.indexOf('-');
/* 121 */     int j = paramString.indexOf('-', i + 1);
/*     */ 
/* 123 */     if ((i > 0) && (j > 0) && (j < paramString.length() - 1)) {
/* 124 */       String str1 = paramString.substring(0, i);
/* 125 */       String str2 = paramString.substring(i + 1, j);
/* 126 */       String str3 = paramString.substring(j + 1);
/* 127 */       if ((str1.length() == 4) && (str2.length() >= 1) && (str2.length() <= 2) && (str3.length() >= 1) && (str3.length() <= 2))
/*     */       {
/* 130 */         int k = Integer.parseInt(str1);
/* 131 */         int m = Integer.parseInt(str2);
/* 132 */         int n = Integer.parseInt(str3);
/*     */ 
/* 134 */         if ((m >= 1) && (m <= 12) && (n >= 1) && (n <= 31)) {
/* 135 */           localDate = new Date(k - 1900, m - 1, n);
/*     */         }
/*     */       }
/*     */     }
/* 139 */     if (localDate == null) {
/* 140 */       throw new IllegalArgumentException();
/*     */     }
/*     */ 
/* 143 */     return localDate;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 154 */     int i = super.getYear() + 1900;
/* 155 */     int j = super.getMonth() + 1;
/* 156 */     int k = super.getDate();
/*     */ 
/* 158 */     char[] arrayOfChar = "2000-00-00".toCharArray();
/* 159 */     arrayOfChar[0] = Character.forDigit(i / 1000, 10);
/* 160 */     arrayOfChar[1] = Character.forDigit(i / 100 % 10, 10);
/* 161 */     arrayOfChar[2] = Character.forDigit(i / 10 % 10, 10);
/* 162 */     arrayOfChar[3] = Character.forDigit(i % 10, 10);
/* 163 */     arrayOfChar[5] = Character.forDigit(j / 10, 10);
/* 164 */     arrayOfChar[6] = Character.forDigit(j % 10, 10);
/* 165 */     arrayOfChar[8] = Character.forDigit(k / 10, 10);
/* 166 */     arrayOfChar[9] = Character.forDigit(k % 10, 10);
/*     */ 
/* 168 */     return new String(arrayOfChar);
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public int getHours()
/*     */   {
/* 182 */     throw new IllegalArgumentException();
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public int getMinutes()
/*     */   {
/* 194 */     throw new IllegalArgumentException();
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public int getSeconds()
/*     */   {
/* 206 */     throw new IllegalArgumentException();
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public void setHours(int paramInt)
/*     */   {
/* 218 */     throw new IllegalArgumentException();
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public void setMinutes(int paramInt)
/*     */   {
/* 230 */     throw new IllegalArgumentException();
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public void setSeconds(int paramInt)
/*     */   {
/* 242 */     throw new IllegalArgumentException();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.sql.Date
 * JD-Core Version:    0.6.2
 */