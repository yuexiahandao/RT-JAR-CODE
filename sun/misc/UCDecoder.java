/*     */ package sun.misc;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PushbackInputStream;
/*     */ 
/*     */ public class UCDecoder extends CharacterDecoder
/*     */ {
/*  98 */   private static final byte[] map_array = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 40, 41 };
/*     */   private int sequence;
/* 111 */   private byte[] tmp = new byte[2];
/* 112 */   private CRC16 crc = new CRC16();
/*     */ 
/* 165 */   private ByteArrayOutputStream lineAndSeq = new ByteArrayOutputStream(2);
/*     */ 
/*     */   protected int bytesPerAtom()
/*     */   {
/*  89 */     return 2;
/*     */   }
/*     */ 
/*     */   protected int bytesPerLine()
/*     */   {
/*  94 */     return 48;
/*     */   }
/*     */ 
/*     */   protected void decodeAtom(PushbackInputStream paramPushbackInputStream, OutputStream paramOutputStream, int paramInt)
/*     */     throws IOException
/*     */   {
/* 120 */     int i1 = -1; int i2 = -1; int i3 = -1;
/*     */ 
/* 122 */     byte[] arrayOfByte = new byte[3];
/*     */ 
/* 124 */     int i = paramPushbackInputStream.read(arrayOfByte);
/* 125 */     if (i != 3) {
/* 126 */       throw new CEStreamExhausted();
/*     */     }
/* 128 */     for (i = 0; (i < 64) && ((i1 == -1) || (i2 == -1) || (i3 == -1)); i++) {
/* 129 */       if (arrayOfByte[0] == map_array[i]) {
/* 130 */         i1 = (byte)i;
/*     */       }
/* 132 */       if (arrayOfByte[1] == map_array[i]) {
/* 133 */         i2 = (byte)i;
/*     */       }
/* 135 */       if (arrayOfByte[2] == map_array[i]) {
/* 136 */         i3 = (byte)i;
/*     */       }
/*     */     }
/* 139 */     int i4 = (byte)(((i1 & 0x38) << 2) + (i2 & 0x1F));
/* 140 */     int i5 = (byte)(((i1 & 0x7) << 5) + (i3 & 0x1F));
/* 141 */     int j = 0;
/* 142 */     int k = 0;
/* 143 */     for (i = 1; i < 256; i *= 2) {
/* 144 */       if ((i4 & i) != 0)
/* 145 */         j++;
/* 146 */       if ((i5 & i) != 0)
/* 147 */         k++;
/*     */     }
/* 149 */     int m = (i2 & 0x20) / 32;
/* 150 */     int n = (i3 & 0x20) / 32;
/* 151 */     if ((j & 0x1) != m) {
/* 152 */       throw new CEFormatException("UCDecoder: High byte parity error.");
/*     */     }
/* 154 */     if ((k & 0x1) != n) {
/* 155 */       throw new CEFormatException("UCDecoder: Low byte parity error.");
/*     */     }
/* 157 */     paramOutputStream.write(i4);
/* 158 */     this.crc.update(i4);
/* 159 */     if (paramInt == 2) {
/* 160 */       paramOutputStream.write(i5);
/* 161 */       this.crc.update(i5);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void decodeBufferPrefix(PushbackInputStream paramPushbackInputStream, OutputStream paramOutputStream)
/*     */   {
/* 171 */     this.sequence = 0;
/*     */   }
/*     */ 
/*     */   protected int decodeLinePrefix(PushbackInputStream paramPushbackInputStream, OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 190 */     this.crc.value = 0;
/*     */     while (true) {
/* 192 */       int k = paramPushbackInputStream.read(this.tmp, 0, 1);
/* 193 */       if (k == -1) {
/* 194 */         throw new CEStreamExhausted();
/*     */       }
/* 196 */       if (this.tmp[0] == 42) {
/* 197 */         break;
/*     */       }
/*     */     }
/* 200 */     this.lineAndSeq.reset();
/* 201 */     decodeAtom(paramPushbackInputStream, this.lineAndSeq, 2);
/* 202 */     byte[] arrayOfByte = this.lineAndSeq.toByteArray();
/* 203 */     int i = arrayOfByte[0] & 0xFF;
/* 204 */     int j = arrayOfByte[1] & 0xFF;
/* 205 */     if (j != this.sequence) {
/* 206 */       throw new CEFormatException("UCDecoder: Out of sequence line.");
/*     */     }
/* 208 */     this.sequence = (this.sequence + 1 & 0xFF);
/* 209 */     return i;
/*     */   }
/*     */ 
/*     */   protected void decodeLineSuffix(PushbackInputStream paramPushbackInputStream, OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 221 */     int i = this.crc.value;
/*     */ 
/* 225 */     this.lineAndSeq.reset();
/* 226 */     decodeAtom(paramPushbackInputStream, this.lineAndSeq, 2);
/* 227 */     byte[] arrayOfByte = this.lineAndSeq.toByteArray();
/* 228 */     int j = (arrayOfByte[0] << 8 & 0xFF00) + (arrayOfByte[1] & 0xFF);
/* 229 */     if (j != i)
/* 230 */       throw new CEFormatException("UCDecoder: CRC check failed.");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.UCDecoder
 * JD-Core Version:    0.6.2
 */