/*     */ package sun.security.krb5.internal;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.GregorianCalendar;
/*     */ import java.util.TimeZone;
/*     */ import sun.security.krb5.Asn1Exception;
/*     */ import sun.security.krb5.Config;
/*     */ import sun.security.krb5.KrbException;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class KerberosTime
/*     */   implements Cloneable
/*     */ {
/*     */   private long kerberosTime;
/*     */   private int microSeconds;
/*  71 */   private static long initMilli = System.currentTimeMillis();
/*  72 */   private static long initMicro = System.nanoTime() / 1000L;
/*     */   private static long syncTime;
/*  75 */   private static boolean DEBUG = Krb5.DEBUG;
/*     */   public static final boolean NOW = true;
/*     */   public static final boolean UNADJUSTED_NOW = false;
/*     */ 
/*     */   public KerberosTime(long paramLong)
/*     */   {
/*  81 */     this.kerberosTime = paramLong;
/*     */   }
/*     */ 
/*     */   private KerberosTime(long paramLong, int paramInt) {
/*  85 */     this.kerberosTime = paramLong;
/*  86 */     this.microSeconds = paramInt;
/*     */   }
/*     */ 
/*     */   public Object clone() {
/*  90 */     return new KerberosTime(this.kerberosTime, this.microSeconds);
/*     */   }
/*     */ 
/*     */   public KerberosTime(String paramString)
/*     */     throws Asn1Exception
/*     */   {
/*  96 */     this.kerberosTime = toKerberosTime(paramString);
/*     */   }
/*     */ 
/*     */   public KerberosTime(DerValue paramDerValue)
/*     */     throws Asn1Exception, IOException
/*     */   {
/* 106 */     GregorianCalendar localGregorianCalendar = new GregorianCalendar();
/* 107 */     Date localDate = paramDerValue.getGeneralizedTime();
/* 108 */     this.kerberosTime = localDate.getTime();
/*     */   }
/*     */ 
/*     */   private static long toKerberosTime(String paramString)
/*     */     throws Asn1Exception
/*     */   {
/* 123 */     if (paramString.length() != 15)
/* 124 */       throw new Asn1Exception(900);
/* 125 */     if (paramString.charAt(14) != 'Z')
/* 126 */       throw new Asn1Exception(900);
/* 127 */     int i = Integer.parseInt(paramString.substring(0, 4));
/* 128 */     Calendar localCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
/* 129 */     localCalendar.clear();
/* 130 */     localCalendar.set(i, Integer.parseInt(paramString.substring(4, 6)) - 1, Integer.parseInt(paramString.substring(6, 8)), Integer.parseInt(paramString.substring(8, 10)), Integer.parseInt(paramString.substring(10, 12)), Integer.parseInt(paramString.substring(12, 14)));
/*     */ 
/* 141 */     return localCalendar.getTime().getTime();
/*     */   }
/*     */ 
/*     */   public static String zeroPad(String paramString, int paramInt)
/*     */   {
/* 147 */     StringBuffer localStringBuffer = new StringBuffer(paramString);
/* 148 */     while (localStringBuffer.length() < paramInt)
/* 149 */       localStringBuffer.insert(0, '0');
/* 150 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   public KerberosTime(Date paramDate) {
/* 154 */     this.kerberosTime = paramDate.getTime();
/*     */   }
/*     */ 
/*     */   public KerberosTime(boolean paramBoolean) {
/* 158 */     if (paramBoolean)
/* 159 */       setNow();
/*     */   }
/*     */ 
/*     */   public String toGeneralizedTimeString()
/*     */   {
/* 168 */     Calendar localCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
/* 169 */     localCalendar.clear();
/*     */ 
/* 171 */     localCalendar.setTimeInMillis(this.kerberosTime);
/* 172 */     return zeroPad(Integer.toString(localCalendar.get(1)), 4) + zeroPad(Integer.toString(localCalendar.get(2) + 1), 2) + zeroPad(Integer.toString(localCalendar.get(5)), 2) + zeroPad(Integer.toString(localCalendar.get(11)), 2) + zeroPad(Integer.toString(localCalendar.get(12)), 2) + zeroPad(Integer.toString(localCalendar.get(13)), 2) + 'Z';
/*     */   }
/*     */ 
/*     */   public byte[] asn1Encode()
/*     */     throws Asn1Exception, IOException
/*     */   {
/* 188 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/* 189 */     localDerOutputStream.putGeneralizedTime(toDate());
/* 190 */     return localDerOutputStream.toByteArray();
/*     */   }
/*     */ 
/*     */   public long getTime() {
/* 194 */     return this.kerberosTime;
/*     */   }
/*     */ 
/*     */   public void setTime(Date paramDate)
/*     */   {
/* 199 */     this.kerberosTime = paramDate.getTime();
/* 200 */     this.microSeconds = 0;
/*     */   }
/*     */ 
/*     */   public void setTime(long paramLong) {
/* 204 */     this.kerberosTime = paramLong;
/* 205 */     this.microSeconds = 0;
/*     */   }
/*     */ 
/*     */   public Date toDate() {
/* 209 */     Date localDate = new Date(this.kerberosTime);
/* 210 */     localDate.setTime(localDate.getTime());
/* 211 */     return localDate;
/*     */   }
/*     */ 
/*     */   public void setNow() {
/* 215 */     long l1 = System.currentTimeMillis();
/* 216 */     long l2 = System.nanoTime() / 1000L;
/* 217 */     long l3 = l2 - initMicro;
/* 218 */     long l4 = initMilli + l3 / 1000L;
/* 219 */     if ((l4 - l1 > 100L) || (l1 - l4 > 100L)) {
/* 220 */       if (DEBUG) {
/* 221 */         System.out.println("System time adjusted");
/*     */       }
/* 223 */       initMilli = l1;
/* 224 */       initMicro = l2;
/* 225 */       setTime(l1);
/* 226 */       this.microSeconds = 0;
/*     */     } else {
/* 228 */       setTime(l4);
/* 229 */       this.microSeconds = ((int)(l3 % 1000L));
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getMicroSeconds() {
/* 234 */     Long localLong = new Long(this.kerberosTime % 1000L * 1000L);
/* 235 */     return localLong.intValue() + this.microSeconds;
/*     */   }
/*     */ 
/*     */   public void setMicroSeconds(int paramInt) {
/* 239 */     this.microSeconds = (paramInt % 1000);
/* 240 */     Integer localInteger = new Integer(paramInt);
/* 241 */     long l = localInteger.longValue() / 1000L;
/* 242 */     this.kerberosTime = (this.kerberosTime - this.kerberosTime % 1000L + l);
/*     */   }
/*     */ 
/*     */   public void setMicroSeconds(Integer paramInteger) {
/* 246 */     if (paramInteger != null) {
/* 247 */       this.microSeconds = (paramInteger.intValue() % 1000);
/* 248 */       long l = paramInteger.longValue() / 1000L;
/* 249 */       this.kerberosTime = (this.kerberosTime - this.kerberosTime % 1000L + l);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean inClockSkew(int paramInt) {
/* 254 */     KerberosTime localKerberosTime = new KerberosTime(true);
/*     */ 
/* 256 */     if (Math.abs(this.kerberosTime - localKerberosTime.kerberosTime) > paramInt * 1000L)
/*     */     {
/* 258 */       return false;
/* 259 */     }return true;
/*     */   }
/*     */ 
/*     */   public boolean inClockSkew() {
/* 263 */     return inClockSkew(getDefaultSkew());
/*     */   }
/*     */ 
/*     */   public boolean inClockSkew(int paramInt, KerberosTime paramKerberosTime) {
/* 267 */     if (Math.abs(this.kerberosTime - paramKerberosTime.kerberosTime) > paramInt * 1000L)
/*     */     {
/* 269 */       return false;
/* 270 */     }return true;
/*     */   }
/*     */ 
/*     */   public boolean inClockSkew(KerberosTime paramKerberosTime) {
/* 274 */     return inClockSkew(getDefaultSkew(), paramKerberosTime);
/*     */   }
/*     */ 
/*     */   public boolean greaterThanWRTClockSkew(KerberosTime paramKerberosTime, int paramInt) {
/* 278 */     if (this.kerberosTime - paramKerberosTime.kerberosTime > paramInt * 1000L)
/* 279 */       return true;
/* 280 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean greaterThanWRTClockSkew(KerberosTime paramKerberosTime) {
/* 284 */     return greaterThanWRTClockSkew(paramKerberosTime, getDefaultSkew());
/*     */   }
/*     */ 
/*     */   public boolean greaterThan(KerberosTime paramKerberosTime) {
/* 288 */     return (this.kerberosTime > paramKerberosTime.kerberosTime) || ((this.kerberosTime == paramKerberosTime.kerberosTime) && (this.microSeconds > paramKerberosTime.microSeconds));
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 294 */     if (this == paramObject) {
/* 295 */       return true;
/*     */     }
/*     */ 
/* 298 */     if (!(paramObject instanceof KerberosTime)) {
/* 299 */       return false;
/*     */     }
/*     */ 
/* 302 */     return (this.kerberosTime == ((KerberosTime)paramObject).kerberosTime) && (this.microSeconds == ((KerberosTime)paramObject).microSeconds);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 307 */     int i = 629 + (int)(this.kerberosTime ^ this.kerberosTime >>> 32);
/* 308 */     return i * 17 + this.microSeconds;
/*     */   }
/*     */ 
/*     */   public boolean isZero() {
/* 312 */     return (this.kerberosTime == 0L) && (this.microSeconds == 0);
/*     */   }
/*     */ 
/*     */   public int getSeconds() {
/* 316 */     Long localLong = new Long(this.kerberosTime / 1000L);
/* 317 */     return localLong.intValue();
/*     */   }
/*     */ 
/*     */   public void setSeconds(int paramInt) {
/* 321 */     Integer localInteger = new Integer(paramInt);
/* 322 */     this.kerberosTime = (localInteger.longValue() * 1000L);
/*     */   }
/*     */ 
/*     */   public static KerberosTime parse(DerInputStream paramDerInputStream, byte paramByte, boolean paramBoolean)
/*     */     throws Asn1Exception, IOException
/*     */   {
/* 338 */     if ((paramBoolean) && (((byte)paramDerInputStream.peekByte() & 0x1F) != paramByte))
/* 339 */       return null;
/* 340 */     DerValue localDerValue1 = paramDerInputStream.getDerValue();
/* 341 */     if (paramByte != (localDerValue1.getTag() & 0x1F)) {
/* 342 */       throw new Asn1Exception(906);
/*     */     }
/*     */ 
/* 345 */     DerValue localDerValue2 = localDerValue1.getData().getDerValue();
/* 346 */     return new KerberosTime(localDerValue2);
/*     */   }
/*     */ 
/*     */   public static int getDefaultSkew()
/*     */   {
/* 351 */     int i = 300;
/*     */     try {
/* 353 */       Config localConfig = Config.getInstance();
/* 354 */       if ((i = localConfig.getDefaultIntValue("clockskew", "libdefaults")) == -2147483648)
/*     */       {
/* 356 */         i = 300;
/*     */       }
/*     */     } catch (KrbException localKrbException) {
/* 359 */       if (DEBUG) {
/* 360 */         System.out.println("Exception in getting clockskew from Configuration using default value " + localKrbException.getMessage());
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 366 */     return i;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 370 */     return toGeneralizedTimeString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.KerberosTime
 * JD-Core Version:    0.6.2
 */