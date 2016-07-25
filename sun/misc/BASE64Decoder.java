/*     */ package sun.misc;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PushbackInputStream;
/*     */ 
/*     */ public class BASE64Decoder extends CharacterDecoder
/*     */ {
/*  77 */   private static final char[] pem_array = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };
/*     */ 
/*  89 */   private static final byte[] pem_convert_array = new byte[256];
/*     */ 
/* 100 */   byte[] decode_buffer = new byte[4];
/*     */ 
/*     */   protected int bytesPerAtom()
/*     */   {
/*  65 */     return 4;
/*     */   }
/*     */ 
/*     */   protected int bytesPerLine()
/*     */   {
/*  70 */     return 72;
/*     */   }
/*     */ 
/*     */   protected void decodeAtom(PushbackInputStream paramPushbackInputStream, OutputStream paramOutputStream, int paramInt)
/*     */     throws IOException
/*     */   {
/* 109 */     int j = -1; int k = -1; int m = -1; int n = -1;
/*     */ 
/* 111 */     if (paramInt < 2)
/* 112 */       throw new CEFormatException("BASE64Decoder: Not enough bytes for an atom.");
/*     */     do
/*     */     {
/* 115 */       i = paramPushbackInputStream.read();
/* 116 */       if (i == -1)
/* 117 */         throw new CEStreamExhausted();
/*     */     }
/* 119 */     while ((i == 10) || (i == 13));
/* 120 */     this.decode_buffer[0] = ((byte)i);
/*     */ 
/* 122 */     int i = readFully(paramPushbackInputStream, this.decode_buffer, 1, paramInt - 1);
/* 123 */     if (i == -1) {
/* 124 */       throw new CEStreamExhausted();
/*     */     }
/*     */ 
/* 127 */     if ((paramInt > 3) && (this.decode_buffer[3] == 61)) {
/* 128 */       paramInt = 3;
/*     */     }
/* 130 */     if ((paramInt > 2) && (this.decode_buffer[2] == 61)) {
/* 131 */       paramInt = 2;
/*     */     }
/* 133 */     switch (paramInt) {
/*     */     case 4:
/* 135 */       n = pem_convert_array[(this.decode_buffer[3] & 0xFF)];
/*     */     case 3:
/* 138 */       m = pem_convert_array[(this.decode_buffer[2] & 0xFF)];
/*     */     case 2:
/* 141 */       k = pem_convert_array[(this.decode_buffer[1] & 0xFF)];
/* 142 */       j = pem_convert_array[(this.decode_buffer[0] & 0xFF)];
/*     */     }
/*     */ 
/* 146 */     switch (paramInt) {
/*     */     case 2:
/* 148 */       paramOutputStream.write((byte)(j << 2 & 0xFC | k >>> 4 & 0x3));
/* 149 */       break;
/*     */     case 3:
/* 151 */       paramOutputStream.write((byte)(j << 2 & 0xFC | k >>> 4 & 0x3));
/* 152 */       paramOutputStream.write((byte)(k << 4 & 0xF0 | m >>> 2 & 0xF));
/* 153 */       break;
/*     */     case 4:
/* 155 */       paramOutputStream.write((byte)(j << 2 & 0xFC | k >>> 4 & 0x3));
/* 156 */       paramOutputStream.write((byte)(k << 4 & 0xF0 | m >>> 2 & 0xF));
/* 157 */       paramOutputStream.write((byte)(m << 6 & 0xC0 | n & 0x3F));
/*     */     }
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  92 */     for (int i = 0; i < 255; i++) {
/*  93 */       pem_convert_array[i] = -1;
/*     */     }
/*  95 */     for (i = 0; i < pem_array.length; i++)
/*  96 */       pem_convert_array[pem_array[i]] = ((byte)i);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.BASE64Decoder
 * JD-Core Version:    0.6.2
 */