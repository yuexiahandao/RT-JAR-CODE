/*     */ package java.util.jar;
/*     */ 
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class Manifest
/*     */   implements Cloneable
/*     */ {
/*  51 */   private Attributes attr = new Attributes();
/*     */ 
/*  54 */   private Map entries = new HashMap();
/*     */ 
/*     */   public Manifest()
/*     */   {
/*     */   }
/*     */ 
/*     */   public Manifest(InputStream paramInputStream)
/*     */     throws IOException
/*     */   {
/*  69 */     read(paramInputStream);
/*     */   }
/*     */ 
/*     */   public Manifest(Manifest paramManifest)
/*     */   {
/*  78 */     this.attr.putAll(paramManifest.getMainAttributes());
/*  79 */     this.entries.putAll(paramManifest.getEntries());
/*     */   }
/*     */ 
/*     */   public Attributes getMainAttributes()
/*     */   {
/*  87 */     return this.attr;
/*     */   }
/*     */ 
/*     */   public Map<String, Attributes> getEntries()
/*     */   {
/* 100 */     return this.entries;
/*     */   }
/*     */ 
/*     */   public Attributes getAttributes(String paramString)
/*     */   {
/* 126 */     return (Attributes)getEntries().get(paramString);
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 133 */     this.attr.clear();
/* 134 */     this.entries.clear();
/*     */   }
/*     */ 
/*     */   public void write(OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 147 */     DataOutputStream localDataOutputStream = new DataOutputStream(paramOutputStream);
/*     */ 
/* 149 */     this.attr.writeMain(localDataOutputStream);
/*     */ 
/* 151 */     Iterator localIterator = this.entries.entrySet().iterator();
/* 152 */     while (localIterator.hasNext()) {
/* 153 */       Map.Entry localEntry = (Map.Entry)localIterator.next();
/* 154 */       StringBuffer localStringBuffer = new StringBuffer("Name: ");
/* 155 */       String str = (String)localEntry.getKey();
/* 156 */       if (str != null) {
/* 157 */         byte[] arrayOfByte = str.getBytes("UTF8");
/* 158 */         str = new String(arrayOfByte, 0, 0, arrayOfByte.length);
/*     */       }
/* 160 */       localStringBuffer.append(str);
/* 161 */       localStringBuffer.append("\r\n");
/* 162 */       make72Safe(localStringBuffer);
/* 163 */       localDataOutputStream.writeBytes(localStringBuffer.toString());
/* 164 */       ((Attributes)localEntry.getValue()).write(localDataOutputStream);
/*     */     }
/* 166 */     localDataOutputStream.flush();
/*     */   }
/*     */ 
/*     */   static void make72Safe(StringBuffer paramStringBuffer)
/*     */   {
/* 173 */     int i = paramStringBuffer.length();
/* 174 */     if (i > 72) {
/* 175 */       int j = 70;
/* 176 */       while (j < i - 2) {
/* 177 */         paramStringBuffer.insert(j, "\r\n ");
/* 178 */         j += 72;
/* 179 */         i += 3;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void read(InputStream paramInputStream)
/*     */     throws IOException
/*     */   {
/* 195 */     FastInputStream localFastInputStream = new FastInputStream(paramInputStream);
/*     */ 
/* 197 */     byte[] arrayOfByte = new byte[512];
/*     */ 
/* 199 */     this.attr.read(localFastInputStream, arrayOfByte);
/*     */ 
/* 201 */     int i = 0; int j = 0;
/*     */ 
/* 203 */     int k = 2;
/*     */ 
/* 206 */     String str = null;
/* 207 */     int n = 1;
/* 208 */     Object localObject1 = null;
/*     */     int m;
/* 210 */     while ((m = localFastInputStream.readLine(arrayOfByte)) != -1) {
/* 211 */       if (arrayOfByte[(--m)] != 10) {
/* 212 */         throw new IOException("manifest line too long");
/*     */       }
/* 214 */       if ((m > 0) && (arrayOfByte[(m - 1)] == 13)) {
/* 215 */         m--;
/*     */       }
/* 217 */       if ((m != 0) || (n == 0))
/*     */       {
/* 220 */         n = 0;
/*     */ 
/* 222 */         if (str == null) {
/* 223 */           str = parseName(arrayOfByte, m);
/* 224 */           if (str == null) {
/* 225 */             throw new IOException("invalid manifest format");
/*     */           }
/* 227 */           if (localFastInputStream.peek() == 32)
/*     */           {
/* 229 */             localObject1 = new byte[m - 6];
/* 230 */             System.arraycopy(arrayOfByte, 6, localObject1, 0, m - 6);
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 235 */           Object localObject2 = new byte[localObject1.length + m - 1];
/* 236 */           System.arraycopy(localObject1, 0, localObject2, 0, localObject1.length);
/* 237 */           System.arraycopy(arrayOfByte, 1, localObject2, localObject1.length, m - 1);
/* 238 */           if (localFastInputStream.peek() == 32)
/*     */           {
/* 240 */             localObject1 = localObject2;
/*     */           }
/*     */           else {
/* 243 */             str = new String((byte[])localObject2, 0, localObject2.length, "UTF8");
/* 244 */             localObject1 = null;
/*     */ 
/* 246 */             localObject2 = getAttributes(str);
/* 247 */             if (localObject2 == null) {
/* 248 */               localObject2 = new Attributes(k);
/* 249 */               this.entries.put(str, localObject2);
/*     */             }
/* 251 */             ((Attributes)localObject2).read(localFastInputStream, arrayOfByte);
/* 252 */             i++;
/* 253 */             j += ((Attributes)localObject2).size();
/*     */ 
/* 257 */             k = Math.max(2, j / i);
/*     */ 
/* 259 */             str = null;
/* 260 */             n = 1; }  } 
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/* 265 */   private String parseName(byte[] paramArrayOfByte, int paramInt) { if ((toLower(paramArrayOfByte[0]) == 110) && (toLower(paramArrayOfByte[1]) == 97) && (toLower(paramArrayOfByte[2]) == 109) && (toLower(paramArrayOfByte[3]) == 101) && (paramArrayOfByte[4] == 58) && (paramArrayOfByte[5] == 32))
/*     */     {
/*     */       try
/*     */       {
/* 269 */         return new String(paramArrayOfByte, 6, paramInt - 6, "UTF8");
/*     */       }
/*     */       catch (Exception localException) {
/*     */       }
/*     */     }
/* 274 */     return null; }
/*     */ 
/*     */   private int toLower(int paramInt)
/*     */   {
/* 278 */     return (paramInt >= 65) && (paramInt <= 90) ? 97 + (paramInt - 65) : paramInt;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 290 */     if ((paramObject instanceof Manifest)) {
/* 291 */       Manifest localManifest = (Manifest)paramObject;
/* 292 */       return (this.attr.equals(localManifest.getMainAttributes())) && (this.entries.equals(localManifest.getEntries()));
/*     */     }
/*     */ 
/* 295 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 303 */     return this.attr.hashCode() + this.entries.hashCode();
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/* 315 */     return new Manifest(this);
/*     */   }
/*     */ 
/*     */   static class FastInputStream extends FilterInputStream
/*     */   {
/*     */     private byte[] buf;
/* 323 */     private int count = 0;
/* 324 */     private int pos = 0;
/*     */ 
/*     */     FastInputStream(InputStream paramInputStream) {
/* 327 */       this(paramInputStream, 8192);
/*     */     }
/*     */ 
/*     */     FastInputStream(InputStream paramInputStream, int paramInt) {
/* 331 */       super();
/* 332 */       this.buf = new byte[paramInt];
/*     */     }
/*     */ 
/*     */     public int read() throws IOException {
/* 336 */       if (this.pos >= this.count) {
/* 337 */         fill();
/* 338 */         if (this.pos >= this.count) {
/* 339 */           return -1;
/*     */         }
/*     */       }
/* 342 */       return this.buf[(this.pos++)] & 0xFF;
/*     */     }
/*     */ 
/*     */     public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException {
/* 346 */       int i = this.count - this.pos;
/* 347 */       if (i <= 0) {
/* 348 */         if (paramInt2 >= this.buf.length) {
/* 349 */           return this.in.read(paramArrayOfByte, paramInt1, paramInt2);
/*     */         }
/* 351 */         fill();
/* 352 */         i = this.count - this.pos;
/* 353 */         if (i <= 0) {
/* 354 */           return -1;
/*     */         }
/*     */       }
/* 357 */       if (paramInt2 > i) {
/* 358 */         paramInt2 = i;
/*     */       }
/* 360 */       System.arraycopy(this.buf, this.pos, paramArrayOfByte, paramInt1, paramInt2);
/* 361 */       this.pos += paramInt2;
/* 362 */       return paramInt2;
/*     */     }
/*     */ 
/*     */     public int readLine(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */       throws IOException
/*     */     {
/* 370 */       byte[] arrayOfByte = this.buf;
/* 371 */       int i = 0;
/* 372 */       while (i < paramInt2) {
/* 373 */         int j = this.count - this.pos;
/* 374 */         if (j <= 0) {
/* 375 */           fill();
/* 376 */           j = this.count - this.pos;
/* 377 */           if (j <= 0) {
/* 378 */             return -1;
/*     */           }
/*     */         }
/* 381 */         int k = paramInt2 - i;
/* 382 */         if (k > j) {
/* 383 */           k = j;
/*     */         }
/* 385 */         int m = this.pos;
/* 386 */         int n = m + k;
/* 387 */         while ((m < n) && (arrayOfByte[(m++)] != 10));
/* 388 */         k = m - this.pos;
/* 389 */         System.arraycopy(arrayOfByte, this.pos, paramArrayOfByte, paramInt1, k);
/* 390 */         paramInt1 += k;
/* 391 */         i += k;
/* 392 */         this.pos = m;
/* 393 */         if (arrayOfByte[(m - 1)] == 10) {
/*     */           break;
/*     */         }
/*     */       }
/* 397 */       return i;
/*     */     }
/*     */ 
/*     */     public byte peek() throws IOException {
/* 401 */       if (this.pos == this.count)
/* 402 */         fill();
/* 403 */       if (this.pos == this.count)
/* 404 */         return -1;
/* 405 */       return this.buf[this.pos];
/*     */     }
/*     */ 
/*     */     public int readLine(byte[] paramArrayOfByte) throws IOException {
/* 409 */       return readLine(paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */     }
/*     */ 
/*     */     public long skip(long paramLong) throws IOException {
/* 413 */       if (paramLong <= 0L) {
/* 414 */         return 0L;
/*     */       }
/* 416 */       long l = this.count - this.pos;
/* 417 */       if (l <= 0L) {
/* 418 */         return this.in.skip(paramLong);
/*     */       }
/* 420 */       if (paramLong > l) {
/* 421 */         paramLong = l;
/*     */       }
/* 423 */       this.pos = ((int)(this.pos + paramLong));
/* 424 */       return paramLong;
/*     */     }
/*     */ 
/*     */     public int available() throws IOException {
/* 428 */       return this.count - this.pos + this.in.available();
/*     */     }
/*     */ 
/*     */     public void close() throws IOException {
/* 432 */       if (this.in != null) {
/* 433 */         this.in.close();
/* 434 */         this.in = null;
/* 435 */         this.buf = null;
/*     */       }
/*     */     }
/*     */ 
/*     */     private void fill() throws IOException {
/* 440 */       this.count = (this.pos = 0);
/* 441 */       int i = this.in.read(this.buf, 0, this.buf.length);
/* 442 */       if (i > 0)
/* 443 */         this.count = i;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.jar.Manifest
 * JD-Core Version:    0.6.2
 */