/*     */ package sun.misc;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.nio.ByteBuffer;
/*     */ 
/*     */ public abstract class CharacterEncoder
/*     */ {
/*     */   protected PrintStream pStream;
/*     */ 
/*     */   protected abstract int bytesPerAtom();
/*     */ 
/*     */   protected abstract int bytesPerLine();
/*     */ 
/*     */   protected void encodeBufferPrefix(OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/*  92 */     this.pStream = new PrintStream(paramOutputStream);
/*     */   }
/*     */ 
/*     */   protected void encodeBufferSuffix(OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void encodeLinePrefix(OutputStream paramOutputStream, int paramInt)
/*     */     throws IOException
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void encodeLineSuffix(OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 113 */     this.pStream.println();
/*     */   }
/*     */ 
/*     */   protected abstract void encodeAtom(OutputStream paramOutputStream, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException;
/*     */ 
/*     */   protected int readFully(InputStream paramInputStream, byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/* 126 */     for (int i = 0; i < paramArrayOfByte.length; i++) {
/* 127 */       int j = paramInputStream.read();
/* 128 */       if (j == -1)
/* 129 */         return i;
/* 130 */       paramArrayOfByte[i] = ((byte)j);
/*     */     }
/* 132 */     return paramArrayOfByte.length;
/*     */   }
/*     */ 
/*     */   public void encode(InputStream paramInputStream, OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 145 */     byte[] arrayOfByte = new byte[bytesPerLine()];
/*     */ 
/* 147 */     encodeBufferPrefix(paramOutputStream);
/*     */     while (true)
/*     */     {
/* 150 */       int j = readFully(paramInputStream, arrayOfByte);
/* 151 */       if (j == 0) {
/*     */         break;
/*     */       }
/* 154 */       encodeLinePrefix(paramOutputStream, j);
/* 155 */       for (int i = 0; i < j; i += bytesPerAtom())
/*     */       {
/* 157 */         if (i + bytesPerAtom() <= j)
/* 158 */           encodeAtom(paramOutputStream, arrayOfByte, i, bytesPerAtom());
/*     */         else {
/* 160 */           encodeAtom(paramOutputStream, arrayOfByte, i, j - i);
/*     */         }
/*     */       }
/* 163 */       if (j < bytesPerLine()) {
/*     */         break;
/*     */       }
/* 166 */       encodeLineSuffix(paramOutputStream);
/*     */     }
/*     */ 
/* 169 */     encodeBufferSuffix(paramOutputStream);
/*     */   }
/*     */ 
/*     */   public void encode(byte[] paramArrayOfByte, OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 178 */     ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(paramArrayOfByte);
/* 179 */     encode(localByteArrayInputStream, paramOutputStream);
/*     */   }
/*     */ 
/*     */   public String encode(byte[] paramArrayOfByte)
/*     */   {
/* 187 */     ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
/* 188 */     ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(paramArrayOfByte);
/* 189 */     String str = null;
/*     */     try {
/* 191 */       encode(localByteArrayInputStream, localByteArrayOutputStream);
/*     */ 
/* 193 */       str = localByteArrayOutputStream.toString("8859_1");
/*     */     }
/*     */     catch (Exception localException) {
/* 196 */       throw new Error("CharacterEncoder.encode internal error");
/*     */     }
/* 198 */     return str;
/*     */   }
/*     */ 
/*     */   private byte[] getBytes(ByteBuffer paramByteBuffer)
/*     */   {
/* 215 */     Object localObject = null;
/*     */ 
/* 221 */     if (paramByteBuffer.hasArray()) {
/* 222 */       byte[] arrayOfByte = paramByteBuffer.array();
/* 223 */       if ((arrayOfByte.length == paramByteBuffer.capacity()) && (arrayOfByte.length == paramByteBuffer.remaining()))
/*     */       {
/* 225 */         localObject = arrayOfByte;
/* 226 */         paramByteBuffer.position(paramByteBuffer.limit());
/*     */       }
/*     */     }
/*     */ 
/* 230 */     if (localObject == null)
/*     */     {
/* 236 */       localObject = new byte[paramByteBuffer.remaining()];
/*     */ 
/* 241 */       paramByteBuffer.get((byte[])localObject);
/*     */     }
/*     */ 
/* 244 */     return localObject;
/*     */   }
/*     */ 
/*     */   public void encode(ByteBuffer paramByteBuffer, OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 255 */     byte[] arrayOfByte = getBytes(paramByteBuffer);
/* 256 */     encode(arrayOfByte, paramOutputStream);
/*     */   }
/*     */ 
/*     */   public String encode(ByteBuffer paramByteBuffer)
/*     */   {
/* 266 */     byte[] arrayOfByte = getBytes(paramByteBuffer);
/* 267 */     return encode(arrayOfByte);
/*     */   }
/*     */ 
/*     */   public void encodeBuffer(InputStream paramInputStream, OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 280 */     byte[] arrayOfByte = new byte[bytesPerLine()];
/*     */ 
/* 282 */     encodeBufferPrefix(paramOutputStream);
/*     */     while (true)
/*     */     {
/* 285 */       int j = readFully(paramInputStream, arrayOfByte);
/* 286 */       if (j != 0)
/*     */       {
/* 289 */         encodeLinePrefix(paramOutputStream, j);
/* 290 */         for (int i = 0; i < j; i += bytesPerAtom()) {
/* 291 */           if (i + bytesPerAtom() <= j)
/* 292 */             encodeAtom(paramOutputStream, arrayOfByte, i, bytesPerAtom());
/*     */           else {
/* 294 */             encodeAtom(paramOutputStream, arrayOfByte, i, j - i);
/*     */           }
/*     */         }
/* 297 */         encodeLineSuffix(paramOutputStream);
/* 298 */         if (j < bytesPerLine())
/* 299 */           break;
/*     */       }
/*     */     }
/* 302 */     encodeBufferSuffix(paramOutputStream);
/*     */   }
/*     */ 
/*     */   public void encodeBuffer(byte[] paramArrayOfByte, OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 311 */     ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(paramArrayOfByte);
/* 312 */     encodeBuffer(localByteArrayInputStream, paramOutputStream);
/*     */   }
/*     */ 
/*     */   public String encodeBuffer(byte[] paramArrayOfByte)
/*     */   {
/* 320 */     ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
/* 321 */     ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(paramArrayOfByte);
/*     */     try {
/* 323 */       encodeBuffer(localByteArrayInputStream, localByteArrayOutputStream);
/*     */     }
/*     */     catch (Exception localException) {
/* 326 */       throw new Error("CharacterEncoder.encodeBuffer internal error");
/*     */     }
/* 328 */     return localByteArrayOutputStream.toString();
/*     */   }
/*     */ 
/*     */   public void encodeBuffer(ByteBuffer paramByteBuffer, OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 339 */     byte[] arrayOfByte = getBytes(paramByteBuffer);
/* 340 */     encodeBuffer(arrayOfByte, paramOutputStream);
/*     */   }
/*     */ 
/*     */   public String encodeBuffer(ByteBuffer paramByteBuffer)
/*     */   {
/* 350 */     byte[] arrayOfByte = getBytes(paramByteBuffer);
/* 351 */     return encodeBuffer(arrayOfByte);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.CharacterEncoder
 * JD-Core Version:    0.6.2
 */