/*     */ package java.nio.file.attribute;
/*     */ 
/*     */ import java.util.Date;
/*     */ import java.util.Formatter;
/*     */ import java.util.GregorianCalendar;
/*     */ import java.util.Locale;
/*     */ import java.util.TimeZone;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ 
/*     */ public final class FileTime
/*     */   implements Comparable<FileTime>
/*     */ {
/*     */   private final long value;
/*     */   private final TimeUnit unit;
/*     */   private String valueAsString;
/*     */   private DaysAndNanos daysAndNanos;
/*     */ 
/*     */   private DaysAndNanos asDaysAndNanos()
/*     */   {
/*  77 */     if (this.daysAndNanos == null)
/*  78 */       this.daysAndNanos = new DaysAndNanos(this.value, this.unit);
/*  79 */     return this.daysAndNanos;
/*     */   }
/*     */ 
/*     */   private FileTime(long paramLong, TimeUnit paramTimeUnit)
/*     */   {
/*  86 */     if (paramTimeUnit == null)
/*  87 */       throw new NullPointerException();
/*  88 */     this.value = paramLong;
/*  89 */     this.unit = paramTimeUnit;
/*     */   }
/*     */ 
/*     */   public static FileTime from(long paramLong, TimeUnit paramTimeUnit)
/*     */   {
/* 105 */     return new FileTime(paramLong, paramTimeUnit);
/*     */   }
/*     */ 
/*     */   public static FileTime fromMillis(long paramLong)
/*     */   {
/* 118 */     return new FileTime(paramLong, TimeUnit.MILLISECONDS);
/*     */   }
/*     */ 
/*     */   public long to(TimeUnit paramTimeUnit)
/*     */   {
/* 135 */     return paramTimeUnit.convert(this.value, this.unit);
/*     */   }
/*     */ 
/*     */   public long toMillis()
/*     */   {
/* 148 */     return this.unit.toMillis(this.value);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 166 */     return compareTo((FileTime)paramObject) == 0;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 180 */     return asDaysAndNanos().hashCode();
/*     */   }
/*     */ 
/*     */   public int compareTo(FileTime paramFileTime)
/*     */   {
/* 197 */     if (this.unit == paramFileTime.unit) {
/* 198 */       return this.value == paramFileTime.value ? 0 : this.value < paramFileTime.value ? -1 : 1;
/*     */     }
/*     */ 
/* 201 */     return asDaysAndNanos().compareTo(paramFileTime.asDaysAndNanos());
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 232 */     String str1 = this.valueAsString;
/* 233 */     if (str1 == null)
/*     */     {
/* 237 */       long l1 = toMillis();
/*     */ 
/* 240 */       String str2 = "";
/* 241 */       if (this.unit.compareTo(TimeUnit.SECONDS) < 0) {
/* 242 */         long l2 = asDaysAndNanos().fractionOfSecondInNanos();
/* 243 */         if (l2 != 0L)
/*     */         {
/* 245 */           if (l2 < 0L)
/*     */           {
/* 247 */             l2 += 1000000000L;
/* 248 */             if (l1 != -9223372036854775808L) l1 -= 1L;
/*     */ 
/*     */           }
/*     */ 
/* 253 */           String str4 = Long.toString(l2);
/* 254 */           int i = str4.length();
/* 255 */           int j = 9 - i;
/* 256 */           StringBuilder localStringBuilder = new StringBuilder(".");
/* 257 */           while (j-- > 0) {
/* 258 */             localStringBuilder.append('0');
/*     */           }
/* 260 */           if (str4.charAt(i - 1) == '0')
/*     */           {
/* 262 */             i--;
/* 263 */             while (str4.charAt(i - 1) == '0')
/* 264 */               i--;
/* 265 */             localStringBuilder.append(str4.substring(0, i));
/*     */           } else {
/* 267 */             localStringBuilder.append(str4);
/*     */           }
/* 269 */           str2 = localStringBuilder.toString();
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 274 */       GregorianCalendar localGregorianCalendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"), Locale.ROOT);
/*     */ 
/* 276 */       if (this.value < 0L)
/* 277 */         localGregorianCalendar.setGregorianChange(new Date(-9223372036854775808L));
/* 278 */       localGregorianCalendar.setTimeInMillis(l1);
/*     */ 
/* 281 */       String str3 = localGregorianCalendar.get(0) == 0 ? "-" : "";
/*     */ 
/* 284 */       str1 = new Formatter(Locale.ROOT).format("%s%tFT%tR:%tS%sZ", new Object[] { str3, localGregorianCalendar, localGregorianCalendar, localGregorianCalendar, str2 }).toString();
/*     */ 
/* 287 */       this.valueAsString = str1;
/*     */     }
/* 289 */     return str1;
/*     */   }
/*     */ 
/*     */   private static class DaysAndNanos
/*     */     implements Comparable<DaysAndNanos>
/*     */   {
/*     */     private static final long C0 = 1L;
/*     */     private static final long C1 = 24L;
/*     */     private static final long C2 = 1440L;
/*     */     private static final long C3 = 86400L;
/*     */     private static final long C4 = 86400000L;
/*     */     private static final long C5 = 86400000000L;
/*     */     private static final long C6 = 86400000000000L;
/*     */     private final long days;
/*     */     private final long excessNanos;
/*     */ 
/*     */     DaysAndNanos(long paramLong, TimeUnit paramTimeUnit)
/*     */     {
/*     */       long l;
/* 322 */       switch (FileTime.1.$SwitchMap$java$util$concurrent$TimeUnit[paramTimeUnit.ordinal()]) { case 1:
/* 323 */         l = 1L; break;
/*     */       case 2:
/* 324 */         l = 24L; break;
/*     */       case 3:
/* 325 */         l = 1440L; break;
/*     */       case 4:
/* 326 */         l = 86400L; break;
/*     */       case 5:
/* 327 */         l = 86400000L; break;
/*     */       case 6:
/* 328 */         l = 86400000000L; break;
/*     */       case 7:
/* 329 */         l = 86400000000000L; break;
/*     */       default:
/* 330 */         throw new AssertionError("Unit not handled");
/*     */       }
/* 332 */       this.days = paramTimeUnit.toDays(paramLong);
/* 333 */       this.excessNanos = paramTimeUnit.toNanos(paramLong - this.days * l);
/*     */     }
/*     */ 
/*     */     long fractionOfSecondInNanos()
/*     */     {
/* 340 */       return this.excessNanos % 1000000000L;
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject)
/*     */     {
/* 345 */       return compareTo((DaysAndNanos)paramObject) == 0;
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 351 */       return (int)(this.days ^ this.days >>> 32 ^ this.excessNanos ^ this.excessNanos >>> 32);
/*     */     }
/*     */ 
/*     */     public int compareTo(DaysAndNanos paramDaysAndNanos)
/*     */     {
/* 357 */       if (this.days != paramDaysAndNanos.days)
/* 358 */         return this.days < paramDaysAndNanos.days ? -1 : 1;
/* 359 */       return this.excessNanos == paramDaysAndNanos.excessNanos ? 0 : this.excessNanos < paramDaysAndNanos.excessNanos ? -1 : 1;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.file.attribute.FileTime
 * JD-Core Version:    0.6.2
 */