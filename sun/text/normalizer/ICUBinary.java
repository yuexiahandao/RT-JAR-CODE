/*     */ package sun.text.normalizer;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ public final class ICUBinary
/*     */ {
/*     */   private static final byte MAGIC1 = -38;
/*     */   private static final byte MAGIC2 = 39;
/*     */   private static final byte BIG_ENDIAN_ = 1;
/*     */   private static final byte CHAR_SET_ = 0;
/*     */   private static final byte CHAR_SIZE_ = 2;
/*     */   private static final String MAGIC_NUMBER_AUTHENTICATION_FAILED_ = "ICU data file error: Not an ICU data file";
/*     */   private static final String HEADER_AUTHENTICATION_FAILED_ = "ICU data file error: Header authentication failed, please check if you have a valid ICU data file";
/*     */ 
/*     */   public static final byte[] readHeader(InputStream paramInputStream, byte[] paramArrayOfByte, Authenticate paramAuthenticate)
/*     */     throws IOException
/*     */   {
/* 118 */     DataInputStream localDataInputStream = new DataInputStream(paramInputStream);
/* 119 */     int i = localDataInputStream.readChar();
/* 120 */     int j = 2;
/*     */ 
/* 122 */     int k = localDataInputStream.readByte();
/* 123 */     j++;
/* 124 */     int m = localDataInputStream.readByte();
/* 125 */     j++;
/* 126 */     if ((k != -38) || (m != 39)) {
/* 127 */       throw new IOException("ICU data file error: Not an ICU data file");
/*     */     }
/*     */ 
/* 130 */     localDataInputStream.readChar();
/* 131 */     j += 2;
/* 132 */     localDataInputStream.readChar();
/* 133 */     j += 2;
/* 134 */     int n = localDataInputStream.readByte();
/* 135 */     j++;
/* 136 */     int i1 = localDataInputStream.readByte();
/* 137 */     j++;
/* 138 */     int i2 = localDataInputStream.readByte();
/* 139 */     j++;
/* 140 */     localDataInputStream.readByte();
/* 141 */     j++;
/*     */ 
/* 143 */     byte[] arrayOfByte1 = new byte[4];
/* 144 */     localDataInputStream.readFully(arrayOfByte1);
/* 145 */     j += 4;
/* 146 */     byte[] arrayOfByte2 = new byte[4];
/* 147 */     localDataInputStream.readFully(arrayOfByte2);
/* 148 */     j += 4;
/* 149 */     byte[] arrayOfByte3 = new byte[4];
/* 150 */     localDataInputStream.readFully(arrayOfByte3);
/* 151 */     j += 4;
/* 152 */     if (i < j) {
/* 153 */       throw new IOException("Internal Error: Header size error");
/*     */     }
/* 155 */     localDataInputStream.skipBytes(i - j);
/*     */ 
/* 157 */     if ((n != 1) || (i1 != 0) || (i2 != 2) || (!Arrays.equals(paramArrayOfByte, arrayOfByte1)) || ((paramAuthenticate != null) && (!paramAuthenticate.isDataVersionAcceptable(arrayOfByte2))))
/*     */     {
/* 162 */       throw new IOException("ICU data file error: Header authentication failed, please check if you have a valid ICU data file");
/*     */     }
/* 164 */     return arrayOfByte3;
/*     */   }
/*     */ 
/*     */   public static abstract interface Authenticate
/*     */   {
/*     */     public abstract boolean isDataVersionAcceptable(byte[] paramArrayOfByte);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.text.normalizer.ICUBinary
 * JD-Core Version:    0.6.2
 */