/*     */ package sun.util.calendar;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Properties;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.TimeZone;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ public class LocalGregorianCalendar extends BaseCalendar
/*     */ {
/*     */   private String name;
/*     */   private Era[] eras;
/*     */ 
/*     */   static LocalGregorianCalendar getLocalGregorianCalendar(String paramString)
/*     */   {
/* 121 */     Properties localProperties = null;
/*     */     try {
/* 123 */       String str1 = (String)AccessController.doPrivileged(new GetPropertyAction("java.home"));
/*     */ 
/* 125 */       localObject1 = str1 + File.separator + "lib" + File.separator + "calendars.properties";
/*     */ 
/* 127 */       localProperties = (Properties)AccessController.doPrivileged(new PrivilegedExceptionAction() {
/*     */         public Object run() throws IOException {
/* 129 */           Properties localProperties = new Properties();
/* 130 */           FileInputStream localFileInputStream = new FileInputStream(this.val$fname); Object localObject1 = null;
/*     */           try { localProperties.load(localFileInputStream); }
/*     */           catch (Throwable localThrowable2)
/*     */           {
/* 130 */             localObject1 = localThrowable2; throw localThrowable2;
/*     */           } finally {
/* 132 */             if (localFileInputStream != null) if (localObject1 != null) try { localFileInputStream.close(); } catch (Throwable localThrowable3) { localObject1.addSuppressed(localThrowable3); } else localFileInputStream.close(); 
/*     */           }
/* 133 */           return localProperties;
/*     */         } } );
/*     */     }
/*     */     catch (PrivilegedActionException localPrivilegedActionException) {
/* 137 */       throw new RuntimeException(localPrivilegedActionException.getException());
/*     */     }
/*     */ 
/* 141 */     String str2 = localProperties.getProperty("calendar." + paramString + ".eras");
/* 142 */     if (str2 == null) {
/* 143 */       return null;
/*     */     }
/* 145 */     Object localObject1 = new ArrayList();
/* 146 */     StringTokenizer localStringTokenizer1 = new StringTokenizer(str2, ";");
/* 147 */     while (localStringTokenizer1.hasMoreTokens()) {
/* 148 */       localObject2 = localStringTokenizer1.nextToken().trim();
/* 149 */       StringTokenizer localStringTokenizer2 = new StringTokenizer((String)localObject2, ",");
/* 150 */       Object localObject3 = null;
/* 151 */       boolean bool = true;
/* 152 */       long l = 0L;
/* 153 */       Object localObject4 = null;
/*     */ 
/* 155 */       while (localStringTokenizer2.hasMoreTokens()) {
/* 156 */         localObject5 = localStringTokenizer2.nextToken();
/* 157 */         int i = ((String)localObject5).indexOf('=');
/*     */ 
/* 159 */         if (i == -1) {
/* 160 */           return null;
/*     */         }
/* 162 */         String str3 = ((String)localObject5).substring(0, i);
/* 163 */         String str4 = ((String)localObject5).substring(i + 1);
/* 164 */         if ("name".equals(str3))
/* 165 */           localObject3 = str4;
/* 166 */         else if ("since".equals(str3)) {
/* 167 */           if (str4.endsWith("u")) {
/* 168 */             bool = false;
/* 169 */             l = Long.parseLong(str4.substring(0, str4.length() - 1));
/*     */           } else {
/* 171 */             l = Long.parseLong(str4);
/*     */           }
/* 173 */         } else if ("abbr".equals(str3))
/* 174 */           localObject4 = str4;
/*     */         else {
/* 176 */           throw new RuntimeException("Unknown key word: " + str3);
/*     */         }
/*     */       }
/* 179 */       Object localObject5 = new Era(localObject3, localObject4, l, bool);
/* 180 */       ((List)localObject1).add(localObject5);
/*     */     }
/* 182 */     Object localObject2 = new Era[((List)localObject1).size()];
/* 183 */     ((List)localObject1).toArray((Object[])localObject2);
/*     */ 
/* 185 */     return new LocalGregorianCalendar(paramString, (Era[])localObject2);
/*     */   }
/*     */ 
/*     */   private LocalGregorianCalendar(String paramString, Era[] paramArrayOfEra) {
/* 189 */     this.name = paramString;
/* 190 */     this.eras = paramArrayOfEra;
/* 191 */     setEras(paramArrayOfEra);
/*     */   }
/*     */ 
/*     */   public String getName() {
/* 195 */     return this.name;
/*     */   }
/*     */ 
/*     */   public Date getCalendarDate() {
/* 199 */     return getCalendarDate(System.currentTimeMillis(), newCalendarDate());
/*     */   }
/*     */ 
/*     */   public Date getCalendarDate(long paramLong) {
/* 203 */     return getCalendarDate(paramLong, newCalendarDate());
/*     */   }
/*     */ 
/*     */   public Date getCalendarDate(long paramLong, TimeZone paramTimeZone) {
/* 207 */     return getCalendarDate(paramLong, newCalendarDate(paramTimeZone));
/*     */   }
/*     */ 
/*     */   public Date getCalendarDate(long paramLong, CalendarDate paramCalendarDate) {
/* 211 */     Date localDate = (Date)super.getCalendarDate(paramLong, paramCalendarDate);
/* 212 */     return adjustYear(localDate, paramLong, localDate.getZoneOffset());
/*     */   }
/*     */ 
/*     */   private Date adjustYear(Date paramDate, long paramLong, int paramInt)
/*     */   {
/* 217 */     for (int i = this.eras.length - 1; i >= 0; i--) {
/* 218 */       Era localEra = this.eras[i];
/* 219 */       long l = localEra.getSince(null);
/* 220 */       if (localEra.isLocalTime()) {
/* 221 */         l -= paramInt;
/*     */       }
/* 223 */       if (paramLong >= l) {
/* 224 */         paramDate.setLocalEra(localEra);
/* 225 */         int j = paramDate.getNormalizedYear() - localEra.getSinceDate().getYear() + 1;
/* 226 */         paramDate.setLocalYear(j);
/* 227 */         break;
/*     */       }
/*     */     }
/* 230 */     if (i < 0) {
/* 231 */       paramDate.setLocalEra(null);
/* 232 */       paramDate.setLocalYear(paramDate.getNormalizedYear());
/*     */     }
/* 234 */     paramDate.setNormalized(true);
/* 235 */     return paramDate;
/*     */   }
/*     */ 
/*     */   public Date newCalendarDate() {
/* 239 */     return new Date();
/*     */   }
/*     */ 
/*     */   public Date newCalendarDate(TimeZone paramTimeZone) {
/* 243 */     return new Date(paramTimeZone);
/*     */   }
/*     */ 
/*     */   public boolean validate(CalendarDate paramCalendarDate) {
/* 247 */     Date localDate = (Date)paramCalendarDate;
/* 248 */     Era localEra = localDate.getEra();
/* 249 */     if (localEra != null) {
/* 250 */       if (!validateEra(localEra)) {
/* 251 */         return false;
/*     */       }
/* 253 */       localDate.setNormalizedYear(localEra.getSinceDate().getYear() + localDate.getYear());
/*     */     } else {
/* 255 */       localDate.setNormalizedYear(localDate.getYear());
/*     */     }
/* 257 */     return super.validate(localDate);
/*     */   }
/*     */ 
/*     */   private boolean validateEra(Era paramEra)
/*     */   {
/* 262 */     for (int i = 0; i < this.eras.length; i++) {
/* 263 */       if (paramEra == this.eras[i]) {
/* 264 */         return true;
/*     */       }
/*     */     }
/* 267 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean normalize(CalendarDate paramCalendarDate) {
/* 271 */     if (paramCalendarDate.isNormalized()) {
/* 272 */       return true;
/*     */     }
/*     */ 
/* 275 */     normalizeYear(paramCalendarDate);
/* 276 */     Date localDate = (Date)paramCalendarDate;
/*     */ 
/* 279 */     super.normalize(localDate);
/*     */ 
/* 281 */     int i = 0;
/* 282 */     long l1 = 0L;
/* 283 */     int j = localDate.getNormalizedYear();
/*     */ 
/* 285 */     Era localEra = null;
/* 286 */     for (int k = this.eras.length - 1; k >= 0; k--) {
/* 287 */       localEra = this.eras[k];
/*     */       CalendarDate localCalendarDate;
/* 288 */       if (localEra.isLocalTime()) {
/* 289 */         localCalendarDate = localEra.getSinceDate();
/* 290 */         int n = localCalendarDate.getYear();
/* 291 */         if (j > n) {
/*     */           break;
/*     */         }
/* 294 */         if (j == n) {
/* 295 */           int i1 = localDate.getMonth();
/* 296 */           int i2 = localCalendarDate.getMonth();
/* 297 */           if (i1 > i2) {
/*     */             break;
/*     */           }
/* 300 */           if (i1 == i2) {
/* 301 */             int i3 = localDate.getDayOfMonth();
/* 302 */             int i4 = localCalendarDate.getDayOfMonth();
/* 303 */             if (i3 > i4) {
/*     */               break;
/*     */             }
/* 306 */             if (i3 == i4) {
/* 307 */               long l3 = localDate.getTimeOfDay();
/* 308 */               long l4 = localCalendarDate.getTimeOfDay();
/* 309 */               if (l3 >= l4) {
/*     */                 break;
/*     */               }
/* 312 */               k--;
/* 313 */               break;
/*     */             }
/*     */           }
/*     */         }
/*     */       } else {
/* 318 */         if (i == 0) {
/* 319 */           l1 = super.getTime(paramCalendarDate);
/* 320 */           i = 1;
/*     */         }
/*     */ 
/* 323 */         long l2 = localEra.getSince(paramCalendarDate.getZone());
/* 324 */         if (l1 >= l2) {
/*     */           break;
/*     */         }
/*     */       }
/*     */     }
/* 329 */     if (k >= 0) {
/* 330 */       localDate.setLocalEra(localEra);
/* 331 */       int m = localDate.getNormalizedYear() - localEra.getSinceDate().getYear() + 1;
/* 332 */       localDate.setLocalYear(m);
/*     */     }
/*     */     else {
/* 335 */       localDate.setEra(null);
/* 336 */       localDate.setLocalYear(j);
/* 337 */       localDate.setNormalizedYear(j);
/*     */     }
/* 339 */     localDate.setNormalized(true);
/* 340 */     return true;
/*     */   }
/*     */ 
/*     */   void normalizeMonth(CalendarDate paramCalendarDate) {
/* 344 */     normalizeYear(paramCalendarDate);
/* 345 */     super.normalizeMonth(paramCalendarDate);
/*     */   }
/*     */ 
/*     */   void normalizeYear(CalendarDate paramCalendarDate) {
/* 349 */     Date localDate = (Date)paramCalendarDate;
/*     */ 
/* 352 */     Era localEra = localDate.getEra();
/* 353 */     if ((localEra == null) || (!validateEra(localEra)))
/* 354 */       localDate.setNormalizedYear(localDate.getYear());
/*     */     else
/* 356 */       localDate.setNormalizedYear(localEra.getSinceDate().getYear() + localDate.getYear() - 1);
/*     */   }
/*     */ 
/*     */   public boolean isLeapYear(int paramInt)
/*     */   {
/* 365 */     return CalendarUtils.isGregorianLeapYear(paramInt);
/*     */   }
/*     */ 
/*     */   public boolean isLeapYear(Era paramEra, int paramInt) {
/* 369 */     if (paramEra == null) {
/* 370 */       return isLeapYear(paramInt);
/*     */     }
/* 372 */     int i = paramEra.getSinceDate().getYear() + paramInt - 1;
/* 373 */     return isLeapYear(i);
/*     */   }
/*     */ 
/*     */   public void getCalendarDateFromFixedDate(CalendarDate paramCalendarDate, long paramLong) {
/* 377 */     Date localDate = (Date)paramCalendarDate;
/* 378 */     super.getCalendarDateFromFixedDate(localDate, paramLong);
/* 379 */     adjustYear(localDate, (paramLong - 719163L) * 86400000L, 0);
/*     */   }
/*     */ 
/*     */   public static class Date extends BaseCalendar.Date
/*     */   {
/*  61 */     private int gregorianYear = -2147483648;
/*     */ 
/*     */     protected Date()
/*     */     {
/*     */     }
/*     */ 
/*     */     protected Date(TimeZone paramTimeZone)
/*     */     {
/*  58 */       super();
/*     */     }
/*     */ 
/*     */     public Date setEra(Era paramEra)
/*     */     {
/*  64 */       if (getEra() != paramEra) {
/*  65 */         super.setEra(paramEra);
/*  66 */         this.gregorianYear = -2147483648;
/*     */       }
/*  68 */       return this;
/*     */     }
/*     */ 
/*     */     public Date addYear(int paramInt) {
/*  72 */       super.addYear(paramInt);
/*  73 */       this.gregorianYear += paramInt;
/*  74 */       return this;
/*     */     }
/*     */ 
/*     */     public Date setYear(int paramInt) {
/*  78 */       if (getYear() != paramInt) {
/*  79 */         super.setYear(paramInt);
/*  80 */         this.gregorianYear = -2147483648;
/*     */       }
/*  82 */       return this;
/*     */     }
/*     */ 
/*     */     public int getNormalizedYear() {
/*  86 */       return this.gregorianYear;
/*     */     }
/*     */ 
/*     */     public void setNormalizedYear(int paramInt) {
/*  90 */       this.gregorianYear = paramInt;
/*     */     }
/*     */ 
/*     */     void setLocalEra(Era paramEra) {
/*  94 */       super.setEra(paramEra);
/*     */     }
/*     */ 
/*     */     void setLocalYear(int paramInt) {
/*  98 */       super.setYear(paramInt);
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 102 */       String str1 = super.toString();
/* 103 */       str1 = str1.substring(str1.indexOf('T'));
/* 104 */       StringBuffer localStringBuffer = new StringBuffer();
/* 105 */       Era localEra = getEra();
/* 106 */       if (localEra != null) {
/* 107 */         String str2 = localEra.getAbbreviation();
/* 108 */         if (str2 != null) {
/* 109 */           localStringBuffer.append(str2);
/*     */         }
/*     */       }
/* 112 */       localStringBuffer.append(getYear()).append('.');
/* 113 */       CalendarUtils.sprintf0d(localStringBuffer, getMonth(), 2).append('.');
/* 114 */       CalendarUtils.sprintf0d(localStringBuffer, getDayOfMonth(), 2);
/* 115 */       localStringBuffer.append(str1);
/* 116 */       return localStringBuffer.toString();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.util.calendar.LocalGregorianCalendar
 * JD-Core Version:    0.6.2
 */