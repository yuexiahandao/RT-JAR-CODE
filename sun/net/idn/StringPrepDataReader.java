/*     */ package sun.net.idn;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import sun.text.normalizer.ICUBinary;
/*     */ import sun.text.normalizer.ICUBinary.Authenticate;
/*     */ 
/*     */ final class StringPrepDataReader
/*     */   implements ICUBinary.Authenticate
/*     */ {
/*     */   private DataInputStream dataInputStream;
/*     */   private byte[] unicodeVersion;
/* 122 */   private static final byte[] DATA_FORMAT_ID = { 83, 80, 82, 80 };
/*     */ 
/* 124 */   private static final byte[] DATA_FORMAT_VERSION = { 3, 2, 5, 2 };
/*     */ 
/*     */   public StringPrepDataReader(InputStream paramInputStream)
/*     */     throws IOException
/*     */   {
/*  67 */     this.unicodeVersion = ICUBinary.readHeader(paramInputStream, DATA_FORMAT_ID, this);
/*     */ 
/*  70 */     this.dataInputStream = new DataInputStream(paramInputStream);
/*     */   }
/*     */ 
/*     */   public void read(byte[] paramArrayOfByte, char[] paramArrayOfChar)
/*     */     throws IOException
/*     */   {
/*  79 */     this.dataInputStream.read(paramArrayOfByte);
/*     */ 
/*  82 */     for (int i = 0; i < paramArrayOfChar.length; i++)
/*  83 */       paramArrayOfChar[i] = this.dataInputStream.readChar();
/*     */   }
/*     */ 
/*     */   public byte[] getDataFormatVersion()
/*     */   {
/*  88 */     return DATA_FORMAT_VERSION;
/*     */   }
/*     */ 
/*     */   public boolean isDataVersionAcceptable(byte[] paramArrayOfByte) {
/*  92 */     return (paramArrayOfByte[0] == DATA_FORMAT_VERSION[0]) && (paramArrayOfByte[2] == DATA_FORMAT_VERSION[2]) && (paramArrayOfByte[3] == DATA_FORMAT_VERSION[3]);
/*     */   }
/*     */ 
/*     */   public int[] readIndexes(int paramInt) throws IOException
/*     */   {
/*  97 */     int[] arrayOfInt = new int[paramInt];
/*     */ 
/*  99 */     for (int i = 0; i < paramInt; i++) {
/* 100 */       arrayOfInt[i] = this.dataInputStream.readInt();
/*     */     }
/* 102 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   public byte[] getUnicodeVersion() {
/* 106 */     return this.unicodeVersion;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.idn.StringPrepDataReader
 * JD-Core Version:    0.6.2
 */