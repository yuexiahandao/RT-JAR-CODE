/*     */ package sun.text.normalizer;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ final class NormalizerDataReader
/*     */   implements ICUBinary.Authenticate
/*     */ {
/*     */   private DataInputStream dataInputStream;
/*     */   private byte[] unicodeVersion;
/* 384 */   private static final byte[] DATA_FORMAT_ID = { 78, 111, 114, 109 };
/*     */ 
/* 386 */   private static final byte[] DATA_FORMAT_VERSION = { 2, 2, 5, 2 };
/*     */ 
/*     */   protected NormalizerDataReader(InputStream paramInputStream)
/*     */     throws IOException
/*     */   {
/* 303 */     this.unicodeVersion = ICUBinary.readHeader(paramInputStream, DATA_FORMAT_ID, this);
/* 304 */     this.dataInputStream = new DataInputStream(paramInputStream);
/*     */   }
/*     */ 
/*     */   protected int[] readIndexes(int paramInt)
/*     */     throws IOException
/*     */   {
/* 310 */     int[] arrayOfInt = new int[paramInt];
/*     */ 
/* 312 */     for (int i = 0; i < paramInt; i++) {
/* 313 */       arrayOfInt[i] = this.dataInputStream.readInt();
/*     */     }
/* 315 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   protected void read(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, char[] paramArrayOfChar1, char[] paramArrayOfChar2)
/*     */     throws IOException
/*     */   {
/* 333 */     this.dataInputStream.readFully(paramArrayOfByte1);
/*     */ 
/* 338 */     for (int i = 0; i < paramArrayOfChar1.length; i++) {
/* 339 */       paramArrayOfChar1[i] = this.dataInputStream.readChar();
/*     */     }
/*     */ 
/* 343 */     for (i = 0; i < paramArrayOfChar2.length; i++) {
/* 344 */       paramArrayOfChar2[i] = this.dataInputStream.readChar();
/*     */     }
/*     */ 
/* 348 */     this.dataInputStream.readFully(paramArrayOfByte2);
/*     */ 
/* 352 */     this.dataInputStream.readFully(paramArrayOfByte3);
/*     */   }
/*     */ 
/*     */   public byte[] getDataFormatVersion() {
/* 356 */     return DATA_FORMAT_VERSION;
/*     */   }
/*     */ 
/*     */   public boolean isDataVersionAcceptable(byte[] paramArrayOfByte)
/*     */   {
/* 361 */     return (paramArrayOfByte[0] == DATA_FORMAT_VERSION[0]) && (paramArrayOfByte[2] == DATA_FORMAT_VERSION[2]) && (paramArrayOfByte[3] == DATA_FORMAT_VERSION[3]);
/*     */   }
/*     */ 
/*     */   public byte[] getUnicodeVersion()
/*     */   {
/* 367 */     return this.unicodeVersion;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.text.normalizer.NormalizerDataReader
 * JD-Core Version:    0.6.2
 */