/*     */ package sun.misc;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public class UUEncoder extends CharacterEncoder
/*     */ {
/*     */   private String bufferName;
/*     */   private int mode;
/*     */ 
/*     */   public UUEncoder()
/*     */   {
/*  94 */     this.bufferName = "encoder.buf";
/*  95 */     this.mode = 644;
/*     */   }
/*     */ 
/*     */   public UUEncoder(String paramString)
/*     */   {
/* 105 */     this.bufferName = paramString;
/* 106 */     this.mode = 644;
/*     */   }
/*     */ 
/*     */   public UUEncoder(String paramString, int paramInt)
/*     */   {
/* 116 */     this.bufferName = paramString;
/* 117 */     this.mode = paramInt;
/*     */   }
/*     */ 
/*     */   protected int bytesPerAtom()
/*     */   {
/* 122 */     return 3;
/*     */   }
/*     */ 
/*     */   protected int bytesPerLine()
/*     */   {
/* 127 */     return 45;
/*     */   }
/*     */ 
/*     */   protected void encodeAtom(OutputStream paramOutputStream, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 138 */     int j = 1; int k = 1;
/*     */ 
/* 141 */     int i = paramArrayOfByte[paramInt1];
/* 142 */     if (paramInt2 > 1) {
/* 143 */       j = paramArrayOfByte[(paramInt1 + 1)];
/*     */     }
/* 145 */     if (paramInt2 > 2) {
/* 146 */       k = paramArrayOfByte[(paramInt1 + 2)];
/*     */     }
/*     */ 
/* 149 */     int m = i >>> 2 & 0x3F;
/* 150 */     int n = i << 4 & 0x30 | j >>> 4 & 0xF;
/* 151 */     int i1 = j << 2 & 0x3C | k >>> 6 & 0x3;
/* 152 */     int i2 = k & 0x3F;
/* 153 */     paramOutputStream.write(m + 32);
/* 154 */     paramOutputStream.write(n + 32);
/* 155 */     paramOutputStream.write(i1 + 32);
/* 156 */     paramOutputStream.write(i2 + 32);
/*     */   }
/*     */ 
/*     */   protected void encodeLinePrefix(OutputStream paramOutputStream, int paramInt)
/*     */     throws IOException
/*     */   {
/* 166 */     paramOutputStream.write((paramInt & 0x3F) + 32);
/*     */   }
/*     */ 
/*     */   protected void encodeLineSuffix(OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 174 */     this.pStream.println();
/*     */   }
/*     */ 
/*     */   protected void encodeBufferPrefix(OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 181 */     this.pStream = new PrintStream(paramOutputStream);
/* 182 */     this.pStream.print("begin " + this.mode + " ");
/* 183 */     if (this.bufferName != null)
/* 184 */       this.pStream.println(this.bufferName);
/*     */     else {
/* 186 */       this.pStream.println("encoder.bin");
/*     */     }
/* 188 */     this.pStream.flush();
/*     */   }
/*     */ 
/*     */   protected void encodeBufferSuffix(OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 196 */     this.pStream.println(" \nend");
/* 197 */     this.pStream.flush();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.UUEncoder
 * JD-Core Version:    0.6.2
 */