/*     */ package java.sql;
/*     */ 
/*     */ import java.util.Date;
/*     */ 
/*     */ public class Timestamp extends Date
/*     */ {
/*     */   private int nanos;
/*     */   static final long serialVersionUID = 2745179027874758501L;
/*     */ 
/*     */   @Deprecated
/*     */   public Timestamp(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7)
/*     */   {
/*  89 */     super(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*  90 */     if ((paramInt7 > 999999999) || (paramInt7 < 0)) {
/*  91 */       throw new IllegalArgumentException("nanos > 999999999 or < 0");
/*     */     }
/*  93 */     this.nanos = paramInt7;
/*     */   }
/*     */ 
/*     */   public Timestamp(long paramLong)
/*     */   {
/* 109 */     super(paramLong / 1000L * 1000L);
/* 110 */     this.nanos = ((int)(paramLong % 1000L * 1000000L));
/* 111 */     if (this.nanos < 0) {
/* 112 */       this.nanos = (1000000000 + this.nanos);
/* 113 */       super.setTime((paramLong / 1000L - 1L) * 1000L);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setTime(long paramLong)
/*     */   {
/* 127 */     super.setTime(paramLong / 1000L * 1000L);
/* 128 */     this.nanos = ((int)(paramLong % 1000L * 1000000L));
/* 129 */     if (this.nanos < 0) {
/* 130 */       this.nanos = (1000000000 + this.nanos);
/* 131 */       super.setTime((paramLong / 1000L - 1L) * 1000L);
/*     */     }
/*     */   }
/*     */ 
/*     */   public long getTime()
/*     */   {
/* 144 */     long l = super.getTime();
/* 145 */     return l + this.nanos / 1000000;
/*     */   }
/*     */ 
/*     */   public static Timestamp valueOf(String paramString)
/*     */   {
/* 175 */     int i = 0;
/* 176 */     int j = 0;
/* 177 */     int k = 0;
/*     */ 
/* 181 */     int i2 = 0;
/*     */ 
/* 185 */     int i6 = 0;
/* 186 */     int i7 = 0;
/* 187 */     int i8 = 0;
/* 188 */     String str4 = "Timestamp format must be yyyy-mm-dd hh:mm:ss[.fffffffff]";
/* 189 */     String str5 = "000000000";
/* 190 */     String str6 = "-";
/* 191 */     String str7 = ":";
/*     */ 
/* 193 */     if (paramString == null) throw new IllegalArgumentException("null string");
/*     */ 
/* 196 */     paramString = paramString.trim();
/* 197 */     int i5 = paramString.indexOf(' ');
/*     */     String str1;
/*     */     String str2;
/* 198 */     if (i5 > 0) {
/* 199 */       str1 = paramString.substring(0, i5);
/* 200 */       str2 = paramString.substring(i5 + 1);
/*     */     } else {
/* 202 */       throw new IllegalArgumentException(str4);
/*     */     }
/*     */ 
/* 206 */     int i3 = str1.indexOf('-');
/* 207 */     int i4 = str1.indexOf('-', i3 + 1);
/*     */ 
/* 210 */     if (str2 == null)
/* 211 */       throw new IllegalArgumentException(str4);
/* 212 */     i6 = str2.indexOf(':');
/* 213 */     i7 = str2.indexOf(':', i6 + 1);
/* 214 */     i8 = str2.indexOf('.', i7 + 1);
/*     */ 
/* 217 */     int i9 = 0;
/* 218 */     if ((i3 > 0) && (i4 > 0) && (i4 < str1.length() - 1)) {
/* 219 */       String str8 = str1.substring(0, i3);
/* 220 */       String str9 = str1.substring(i3 + 1, i4);
/* 221 */       String str10 = str1.substring(i4 + 1);
/* 222 */       if ((str8.length() == 4) && (str9.length() >= 1) && (str9.length() <= 2) && (str10.length() >= 1) && (str10.length() <= 2))
/*     */       {
/* 225 */         i = Integer.parseInt(str8);
/* 226 */         j = Integer.parseInt(str9);
/* 227 */         k = Integer.parseInt(str10);
/*     */ 
/* 229 */         if ((j >= 1) && (j <= 12) && (k >= 1) && (k <= 31)) {
/* 230 */           i9 = 1;
/*     */         }
/*     */       }
/*     */     }
/* 234 */     if (i9 == 0)
/* 235 */       throw new IllegalArgumentException(str4);
/*     */     int m;
/*     */     int n;
/*     */     int i1;
/* 239 */     if (((i6 > 0 ? 1 : 0) & (i7 > 0 ? 1 : 0) & (i7 < str2.length() - 1 ? 1 : 0)) != 0)
/*     */     {
/* 241 */       m = Integer.parseInt(str2.substring(0, i6));
/* 242 */       n = Integer.parseInt(str2.substring(i6 + 1, i7));
/*     */ 
/* 244 */       if (((i8 > 0 ? 1 : 0) & (i8 < str2.length() - 1 ? 1 : 0)) != 0) {
/* 245 */         i1 = Integer.parseInt(str2.substring(i7 + 1, i8));
/*     */ 
/* 247 */         String str3 = str2.substring(i8 + 1);
/* 248 */         if (str3.length() > 9)
/* 249 */           throw new IllegalArgumentException(str4);
/* 250 */         if (!Character.isDigit(str3.charAt(0)))
/* 251 */           throw new IllegalArgumentException(str4);
/* 252 */         str3 = str3 + str5.substring(0, 9 - str3.length());
/* 253 */         i2 = Integer.parseInt(str3); } else {
/* 254 */         if (i8 > 0) {
/* 255 */           throw new IllegalArgumentException(str4);
/*     */         }
/* 257 */         i1 = Integer.parseInt(str2.substring(i7 + 1));
/*     */       }
/*     */     } else {
/* 260 */       throw new IllegalArgumentException(str4);
/*     */     }
/*     */ 
/* 263 */     return new Timestamp(i - 1900, j - 1, k, m, n, i1, i2);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 276 */     int i = super.getYear() + 1900;
/* 277 */     int j = super.getMonth() + 1;
/* 278 */     int k = super.getDate();
/* 279 */     int m = super.getHours();
/* 280 */     int n = super.getMinutes();
/* 281 */     int i1 = super.getSeconds();
/*     */ 
/* 289 */     String str8 = "000000000";
/* 290 */     String str9 = "0000";
/*     */     String str1;
/* 293 */     if (i < 1000)
/*     */     {
/* 295 */       str1 = "" + i;
/* 296 */       str1 = str9.substring(0, 4 - str1.length()) + str1;
/*     */     }
/*     */     else {
/* 299 */       str1 = "" + i;
/*     */     }
/*     */     String str2;
/* 301 */     if (j < 10)
/* 302 */       str2 = "0" + j;
/*     */     else
/* 304 */       str2 = Integer.toString(j);
/*     */     String str3;
/* 306 */     if (k < 10)
/* 307 */       str3 = "0" + k;
/*     */     else
/* 309 */       str3 = Integer.toString(k);
/*     */     String str4;
/* 311 */     if (m < 10)
/* 312 */       str4 = "0" + m;
/*     */     else
/* 314 */       str4 = Integer.toString(m);
/*     */     String str5;
/* 316 */     if (n < 10)
/* 317 */       str5 = "0" + n;
/*     */     else
/* 319 */       str5 = Integer.toString(n);
/*     */     String str6;
/* 321 */     if (i1 < 10)
/* 322 */       str6 = "0" + i1;
/*     */     else
/* 324 */       str6 = Integer.toString(i1);
/*     */     String str7;
/* 326 */     if (this.nanos == 0) {
/* 327 */       str7 = "0";
/*     */     } else {
/* 329 */       str7 = Integer.toString(this.nanos);
/*     */ 
/* 332 */       str7 = str8.substring(0, 9 - str7.length()) + str7;
/*     */ 
/* 336 */       char[] arrayOfChar = new char[str7.length()];
/* 337 */       str7.getChars(0, str7.length(), arrayOfChar, 0);
/* 338 */       int i2 = 8;
/* 339 */       while (arrayOfChar[i2] == '0') {
/* 340 */         i2--;
/*     */       }
/*     */ 
/* 343 */       str7 = new String(arrayOfChar, 0, i2 + 1);
/*     */     }
/*     */ 
/* 347 */     StringBuffer localStringBuffer = new StringBuffer(20 + str7.length());
/* 348 */     localStringBuffer.append(str1);
/* 349 */     localStringBuffer.append("-");
/* 350 */     localStringBuffer.append(str2);
/* 351 */     localStringBuffer.append("-");
/* 352 */     localStringBuffer.append(str3);
/* 353 */     localStringBuffer.append(" ");
/* 354 */     localStringBuffer.append(str4);
/* 355 */     localStringBuffer.append(":");
/* 356 */     localStringBuffer.append(str5);
/* 357 */     localStringBuffer.append(":");
/* 358 */     localStringBuffer.append(str6);
/* 359 */     localStringBuffer.append(".");
/* 360 */     localStringBuffer.append(str7);
/*     */ 
/* 362 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   public int getNanos()
/*     */   {
/* 372 */     return this.nanos;
/*     */   }
/*     */ 
/*     */   public void setNanos(int paramInt)
/*     */   {
/* 385 */     if ((paramInt > 999999999) || (paramInt < 0)) {
/* 386 */       throw new IllegalArgumentException("nanos > 999999999 or < 0");
/*     */     }
/* 388 */     this.nanos = paramInt;
/*     */   }
/*     */ 
/*     */   public boolean equals(Timestamp paramTimestamp)
/*     */   {
/* 401 */     if (super.equals(paramTimestamp)) {
/* 402 */       if (this.nanos == paramTimestamp.nanos) {
/* 403 */         return true;
/*     */       }
/* 405 */       return false;
/*     */     }
/*     */ 
/* 408 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 431 */     if ((paramObject instanceof Timestamp)) {
/* 432 */       return equals((Timestamp)paramObject);
/*     */     }
/* 434 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean before(Timestamp paramTimestamp)
/*     */   {
/* 447 */     return compareTo(paramTimestamp) < 0;
/*     */   }
/*     */ 
/*     */   public boolean after(Timestamp paramTimestamp)
/*     */   {
/* 459 */     return compareTo(paramTimestamp) > 0;
/*     */   }
/*     */ 
/*     */   public int compareTo(Timestamp paramTimestamp)
/*     */   {
/* 476 */     long l1 = getTime();
/* 477 */     long l2 = paramTimestamp.getTime();
/* 478 */     int i = l1 == l2 ? 0 : l1 < l2 ? -1 : 1;
/* 479 */     if (i == 0) {
/* 480 */       if (this.nanos > paramTimestamp.nanos)
/* 481 */         return 1;
/* 482 */       if (this.nanos < paramTimestamp.nanos) {
/* 483 */         return -1;
/*     */       }
/*     */     }
/* 486 */     return i;
/*     */   }
/*     */ 
/*     */   public int compareTo(Date paramDate)
/*     */   {
/* 505 */     if ((paramDate instanceof Timestamp))
/*     */     {
/* 509 */       return compareTo((Timestamp)paramDate);
/*     */     }
/*     */ 
/* 513 */     Timestamp localTimestamp = new Timestamp(paramDate.getTime());
/* 514 */     return compareTo(localTimestamp);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 527 */     return super.hashCode();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.sql.Timestamp
 * JD-Core Version:    0.6.2
 */