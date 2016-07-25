/*     */ package java.util.zip;
/*     */ 
/*     */ import java.util.Date;
/*     */ 
/*     */ public class ZipEntry
/*     */   implements ZipConstants, Cloneable
/*     */ {
/*     */   String name;
/*  38 */   long time = -1L;
/*  39 */   long crc = -1L;
/*  40 */   long size = -1L;
/*  41 */   long csize = -1L;
/*  42 */   int method = -1;
/*  43 */   int flag = 0;
/*     */   byte[] extra;
/*     */   String comment;
/*     */   public static final int STORED = 0;
/*     */   public static final int DEFLATED = 8;
/*     */ 
/*     */   public ZipEntry(String paramString)
/*     */   {
/*  66 */     if (paramString == null) {
/*  67 */       throw new NullPointerException();
/*     */     }
/*  69 */     if (paramString.length() > 65535) {
/*  70 */       throw new IllegalArgumentException("entry name too long");
/*     */     }
/*  72 */     this.name = paramString;
/*     */   }
/*     */ 
/*     */   public ZipEntry(ZipEntry paramZipEntry)
/*     */   {
/*  81 */     this.name = paramZipEntry.name;
/*  82 */     this.time = paramZipEntry.time;
/*  83 */     this.crc = paramZipEntry.crc;
/*  84 */     this.size = paramZipEntry.size;
/*  85 */     this.csize = paramZipEntry.csize;
/*  86 */     this.method = paramZipEntry.method;
/*  87 */     this.flag = paramZipEntry.flag;
/*  88 */     this.extra = paramZipEntry.extra;
/*  89 */     this.comment = paramZipEntry.comment;
/*     */   }
/*     */ 
/*     */   ZipEntry()
/*     */   {
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 102 */     return this.name;
/*     */   }
/*     */ 
/*     */   public void setTime(long paramLong)
/*     */   {
/* 112 */     this.time = javaToDosTime(paramLong);
/*     */   }
/*     */ 
/*     */   public long getTime()
/*     */   {
/* 121 */     return this.time != -1L ? dosToJavaTime(this.time) : -1L;
/*     */   }
/*     */ 
/*     */   public void setSize(long paramLong)
/*     */   {
/* 134 */     if (paramLong < 0L) {
/* 135 */       throw new IllegalArgumentException("invalid entry size");
/*     */     }
/* 137 */     this.size = paramLong;
/*     */   }
/*     */ 
/*     */   public long getSize()
/*     */   {
/* 146 */     return this.size;
/*     */   }
/*     */ 
/*     */   public long getCompressedSize()
/*     */   {
/* 157 */     return this.csize;
/*     */   }
/*     */ 
/*     */   public void setCompressedSize(long paramLong)
/*     */   {
/* 166 */     this.csize = paramLong;
/*     */   }
/*     */ 
/*     */   public void setCrc(long paramLong)
/*     */   {
/* 177 */     if ((paramLong < 0L) || (paramLong > 4294967295L)) {
/* 178 */       throw new IllegalArgumentException("invalid entry crc-32");
/*     */     }
/* 180 */     this.crc = paramLong;
/*     */   }
/*     */ 
/*     */   public long getCrc()
/*     */   {
/* 191 */     return this.crc;
/*     */   }
/*     */ 
/*     */   public void setMethod(int paramInt)
/*     */   {
/* 202 */     if ((paramInt != 0) && (paramInt != 8)) {
/* 203 */       throw new IllegalArgumentException("invalid compression method");
/*     */     }
/* 205 */     this.method = paramInt;
/*     */   }
/*     */ 
/*     */   public int getMethod()
/*     */   {
/* 214 */     return this.method;
/*     */   }
/*     */ 
/*     */   public void setExtra(byte[] paramArrayOfByte)
/*     */   {
/* 225 */     if ((paramArrayOfByte != null) && (paramArrayOfByte.length > 65535)) {
/* 226 */       throw new IllegalArgumentException("invalid extra field length");
/*     */     }
/* 228 */     this.extra = paramArrayOfByte;
/*     */   }
/*     */ 
/*     */   public byte[] getExtra()
/*     */   {
/* 237 */     return this.extra;
/*     */   }
/*     */ 
/*     */   public void setComment(String paramString)
/*     */   {
/* 252 */     this.comment = paramString;
/*     */   }
/*     */ 
/*     */   public String getComment()
/*     */   {
/* 261 */     return this.comment;
/*     */   }
/*     */ 
/*     */   public boolean isDirectory()
/*     */   {
/* 270 */     return this.name.endsWith("/");
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 277 */     return getName();
/*     */   }
/*     */ 
/*     */   private static long dosToJavaTime(long paramLong)
/*     */   {
/* 284 */     Date localDate = new Date((int)((paramLong >> 25 & 0x7F) + 80L), (int)((paramLong >> 21 & 0xF) - 1L), (int)(paramLong >> 16 & 0x1F), (int)(paramLong >> 11 & 0x1F), (int)(paramLong >> 5 & 0x3F), (int)(paramLong << 1 & 0x3E));
/*     */ 
/* 290 */     return localDate.getTime();
/*     */   }
/*     */ 
/*     */   private static long javaToDosTime(long paramLong)
/*     */   {
/* 297 */     Date localDate = new Date(paramLong);
/* 298 */     int i = localDate.getYear() + 1900;
/* 299 */     if (i < 1980) {
/* 300 */       return 2162688L;
/*     */     }
/* 302 */     return i - 1980 << 25 | localDate.getMonth() + 1 << 21 | localDate.getDate() << 16 | localDate.getHours() << 11 | localDate.getMinutes() << 5 | localDate.getSeconds() >> 1;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 311 */     return this.name.hashCode();
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*     */     try
/*     */     {
/* 319 */       ZipEntry localZipEntry = (ZipEntry)super.clone();
/* 320 */       localZipEntry.extra = (this.extra == null ? null : (byte[])this.extra.clone());
/* 321 */       return localZipEntry;
/*     */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*     */     }
/* 324 */     throw new InternalError();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.zip.ZipEntry
 * JD-Core Version:    0.6.2
 */