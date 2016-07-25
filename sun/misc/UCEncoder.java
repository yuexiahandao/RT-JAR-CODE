/*     */ package sun.misc;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public class UCEncoder extends CharacterEncoder
/*     */ {
/*  89 */   private static final byte[] map_array = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 40, 41 };
/*     */   private int sequence;
/* 102 */   private byte[] tmp = new byte[2];
/* 103 */   private CRC16 crc = new CRC16();
/*     */ 
/*     */   protected int bytesPerAtom()
/*     */   {
/*  80 */     return 2;
/*     */   }
/*     */ 
/*     */   protected int bytesPerLine()
/*     */   {
/*  85 */     return 48;
/*     */   }
/*     */ 
/*     */   protected void encodeAtom(OutputStream paramOutputStream, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 116 */     int n = paramArrayOfByte[paramInt1];
/*     */     int i1;
/* 117 */     if (paramInt2 == 2)
/* 118 */       i1 = paramArrayOfByte[(paramInt1 + 1)];
/*     */     else {
/* 120 */       i1 = 0;
/*     */     }
/* 122 */     this.crc.update(n);
/* 123 */     if (paramInt2 == 2) {
/* 124 */       this.crc.update(i1);
/*     */     }
/* 126 */     paramOutputStream.write(map_array[((n >>> 2 & 0x38) + (i1 >>> 5 & 0x7))]);
/* 127 */     int k = 0; int m = 0;
/*     */     int j;
/* 128 */     for (int i = 1; i < 256; i *= 2) {
/* 129 */       if ((n & i) != 0) {
/* 130 */         k++;
/*     */       }
/* 132 */       if ((i1 & i) != 0) {
/* 133 */         m++;
/*     */       }
/*     */     }
/* 136 */     k = (k & 0x1) * 32;
/* 137 */     m = (m & 0x1) * 32;
/* 138 */     paramOutputStream.write(map_array[((n & 0x1F) + k)]);
/* 139 */     paramOutputStream.write(map_array[((i1 & 0x1F) + m)]);
/*     */   }
/*     */ 
/*     */   protected void encodeLinePrefix(OutputStream paramOutputStream, int paramInt)
/*     */     throws IOException
/*     */   {
/* 149 */     paramOutputStream.write(42);
/* 150 */     this.crc.value = 0;
/* 151 */     this.tmp[0] = ((byte)paramInt);
/* 152 */     this.tmp[1] = ((byte)this.sequence);
/* 153 */     this.sequence = (this.sequence + 1 & 0xFF);
/* 154 */     encodeAtom(paramOutputStream, this.tmp, 0, 2);
/*     */   }
/*     */ 
/*     */   protected void encodeLineSuffix(OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 164 */     this.tmp[0] = ((byte)(this.crc.value >>> 8 & 0xFF));
/* 165 */     this.tmp[1] = ((byte)(this.crc.value & 0xFF));
/* 166 */     encodeAtom(paramOutputStream, this.tmp, 0, 2);
/* 167 */     this.pStream.println();
/*     */   }
/*     */ 
/*     */   protected void encodeBufferPrefix(OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 175 */     this.sequence = 0;
/* 176 */     super.encodeBufferPrefix(paramOutputStream);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.UCEncoder
 * JD-Core Version:    0.6.2
 */