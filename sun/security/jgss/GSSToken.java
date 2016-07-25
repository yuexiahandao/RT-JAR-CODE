/*     */ package sun.security.jgss;
/*     */ 
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public abstract class GSSToken
/*     */ {
/*     */   public static final void writeLittleEndian(int paramInt, byte[] paramArrayOfByte)
/*     */   {
/*  49 */     writeLittleEndian(paramInt, paramArrayOfByte, 0);
/*     */   }
/*     */ 
/*     */   public static final void writeLittleEndian(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
/*     */   {
/*  62 */     paramArrayOfByte[(paramInt2++)] = ((byte)paramInt1);
/*  63 */     paramArrayOfByte[(paramInt2++)] = ((byte)(paramInt1 >>> 8));
/*  64 */     paramArrayOfByte[(paramInt2++)] = ((byte)(paramInt1 >>> 16));
/*  65 */     paramArrayOfByte[(paramInt2++)] = ((byte)(paramInt1 >>> 24));
/*     */   }
/*     */ 
/*     */   public static final void writeBigEndian(int paramInt, byte[] paramArrayOfByte) {
/*  69 */     writeBigEndian(paramInt, paramArrayOfByte, 0);
/*     */   }
/*     */ 
/*     */   public static final void writeBigEndian(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
/*     */   {
/*  74 */     paramArrayOfByte[(paramInt2++)] = ((byte)(paramInt1 >>> 24));
/*  75 */     paramArrayOfByte[(paramInt2++)] = ((byte)(paramInt1 >>> 16));
/*  76 */     paramArrayOfByte[(paramInt2++)] = ((byte)(paramInt1 >>> 8));
/*  77 */     paramArrayOfByte[(paramInt2++)] = ((byte)paramInt1);
/*     */   }
/*     */ 
/*     */   public static final int readLittleEndian(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/*  91 */     int i = 0;
/*  92 */     int j = 0;
/*  93 */     while (paramInt2 > 0) {
/*  94 */       i += ((paramArrayOfByte[paramInt1] & 0xFF) << j);
/*  95 */       j += 8;
/*  96 */       paramInt1++;
/*  97 */       paramInt2--;
/*     */     }
/*  99 */     return i;
/*     */   }
/*     */ 
/*     */   public static final int readBigEndian(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
/* 103 */     int i = 0;
/* 104 */     int j = (paramInt2 - 1) * 8;
/* 105 */     while (paramInt2 > 0) {
/* 106 */       i += ((paramArrayOfByte[paramInt1] & 0xFF) << j);
/* 107 */       j -= 8;
/* 108 */       paramInt1++;
/* 109 */       paramInt2--;
/*     */     }
/* 111 */     return i;
/*     */   }
/*     */ 
/*     */   public static final void writeInt(int paramInt, OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 123 */     paramOutputStream.write(paramInt >>> 8);
/* 124 */     paramOutputStream.write(paramInt);
/*     */   }
/*     */ 
/*     */   public static final int writeInt(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
/*     */   {
/* 135 */     paramArrayOfByte[(paramInt2++)] = ((byte)(paramInt1 >>> 8));
/* 136 */     paramArrayOfByte[(paramInt2++)] = ((byte)paramInt1);
/* 137 */     return paramInt2;
/*     */   }
/*     */ 
/*     */   public static final int readInt(InputStream paramInputStream)
/*     */     throws IOException
/*     */   {
/* 149 */     return (0xFF & paramInputStream.read()) << 8 | 0xFF & paramInputStream.read();
/*     */   }
/*     */ 
/*     */   public static final int readInt(byte[] paramArrayOfByte, int paramInt)
/*     */   {
/* 161 */     return (0xFF & paramArrayOfByte[paramInt]) << 8 | 0xFF & paramArrayOfByte[(paramInt + 1)];
/*     */   }
/*     */ 
/*     */   public static final void readFully(InputStream paramInputStream, byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/* 176 */     readFully(paramInputStream, paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */   }
/*     */ 
/*     */   public static final void readFully(InputStream paramInputStream, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 195 */     while (paramInt2 > 0) {
/* 196 */       int i = paramInputStream.read(paramArrayOfByte, paramInt1, paramInt2);
/* 197 */       if (i == -1) {
/* 198 */         throw new EOFException("Cannot read all " + paramInt2 + " bytes needed to form this token!");
/*     */       }
/*     */ 
/* 201 */       paramInt1 += i;
/* 202 */       paramInt2 -= i;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final void debug(String paramString) {
/* 207 */     System.err.print(paramString);
/*     */   }
/*     */ 
/*     */   public static final String getHexBytes(byte[] paramArrayOfByte) {
/* 211 */     return getHexBytes(paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */   }
/*     */ 
/*     */   public static final String getHexBytes(byte[] paramArrayOfByte, int paramInt) {
/* 215 */     return getHexBytes(paramArrayOfByte, 0, paramInt);
/*     */   }
/*     */ 
/*     */   public static final String getHexBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
/* 219 */     StringBuffer localStringBuffer = new StringBuffer();
/* 220 */     for (int i = paramInt1; i < paramInt1 + paramInt2; i++) {
/* 221 */       int j = paramArrayOfByte[i] >> 4 & 0xF;
/* 222 */       int k = paramArrayOfByte[i] & 0xF;
/*     */ 
/* 224 */       localStringBuffer.append(Integer.toHexString(j));
/* 225 */       localStringBuffer.append(Integer.toHexString(k));
/* 226 */       localStringBuffer.append(' ');
/*     */     }
/* 228 */     return localStringBuffer.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.jgss.GSSToken
 * JD-Core Version:    0.6.2
 */