/*     */ package sun.misc;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PushbackInputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ 
/*     */ public abstract class CharacterDecoder
/*     */ {
/*     */   protected abstract int bytesPerAtom();
/*     */ 
/*     */   protected abstract int bytesPerLine();
/*     */ 
/*     */   protected void decodeBufferPrefix(PushbackInputStream paramPushbackInputStream, OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void decodeBufferSuffix(PushbackInputStream paramPushbackInputStream, OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/*     */   }
/*     */ 
/*     */   protected int decodeLinePrefix(PushbackInputStream paramPushbackInputStream, OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 109 */     return bytesPerLine();
/*     */   }
/*     */ 
/*     */   protected void decodeLineSuffix(PushbackInputStream paramPushbackInputStream, OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void decodeAtom(PushbackInputStream paramPushbackInputStream, OutputStream paramOutputStream, int paramInt)
/*     */     throws IOException
/*     */   {
/* 126 */     throw new CEStreamExhausted();
/*     */   }
/*     */ 
/*     */   protected int readFully(InputStream paramInputStream, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 135 */     for (int i = 0; i < paramInt2; i++) {
/* 136 */       int j = paramInputStream.read();
/* 137 */       if (j == -1)
/* 138 */         return i == 0 ? -1 : i;
/* 139 */       paramArrayOfByte[(i + paramInt1)] = ((byte)j);
/*     */     }
/* 141 */     return paramInt2;
/*     */   }
/*     */ 
/*     */   public void decodeBuffer(InputStream paramInputStream, OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 153 */     int j = 0;
/*     */ 
/* 155 */     PushbackInputStream localPushbackInputStream = new PushbackInputStream(paramInputStream);
/* 156 */     decodeBufferPrefix(localPushbackInputStream, paramOutputStream);
/*     */     while (true)
/*     */     {
/*     */       try
/*     */       {
/* 161 */         int k = decodeLinePrefix(localPushbackInputStream, paramOutputStream);
/* 162 */         int i = 0; if (i + bytesPerAtom() < k) {
/* 163 */           decodeAtom(localPushbackInputStream, paramOutputStream, bytesPerAtom());
/* 164 */           j += bytesPerAtom();
/*     */ 
/* 162 */           i += bytesPerAtom();
/*     */         }
/*     */         else
/*     */         {
/* 166 */           if (i + bytesPerAtom() == k) {
/* 167 */             decodeAtom(localPushbackInputStream, paramOutputStream, bytesPerAtom());
/* 168 */             j += bytesPerAtom();
/*     */           } else {
/* 170 */             decodeAtom(localPushbackInputStream, paramOutputStream, k - i);
/* 171 */             j += k - i;
/*     */           }
/* 173 */           decodeLineSuffix(localPushbackInputStream, paramOutputStream);
/*     */         }
/*     */       } catch (CEStreamExhausted localCEStreamExhausted) { break; }
/*     */ 
/*     */     }
/* 178 */     decodeBufferSuffix(localPushbackInputStream, paramOutputStream);
/*     */   }
/*     */ 
/*     */   public byte[] decodeBuffer(String paramString)
/*     */     throws IOException
/*     */   {
/* 187 */     byte[] arrayOfByte = new byte[paramString.length()];
/*     */ 
/* 191 */     paramString.getBytes(0, paramString.length(), arrayOfByte, 0);
/* 192 */     ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(arrayOfByte);
/* 193 */     ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
/* 194 */     decodeBuffer(localByteArrayInputStream, localByteArrayOutputStream);
/* 195 */     return localByteArrayOutputStream.toByteArray();
/*     */   }
/*     */ 
/*     */   public byte[] decodeBuffer(InputStream paramInputStream)
/*     */     throws IOException
/*     */   {
/* 202 */     ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
/* 203 */     decodeBuffer(paramInputStream, localByteArrayOutputStream);
/* 204 */     return localByteArrayOutputStream.toByteArray();
/*     */   }
/*     */ 
/*     */   public ByteBuffer decodeBufferToByteBuffer(String paramString)
/*     */     throws IOException
/*     */   {
/* 212 */     return ByteBuffer.wrap(decodeBuffer(paramString));
/*     */   }
/*     */ 
/*     */   public ByteBuffer decodeBufferToByteBuffer(InputStream paramInputStream)
/*     */     throws IOException
/*     */   {
/* 220 */     return ByteBuffer.wrap(decodeBuffer(paramInputStream));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.CharacterDecoder
 * JD-Core Version:    0.6.2
 */