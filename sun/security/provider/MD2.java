/*     */ package sun.security.provider;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ 
/*     */ public final class MD2 extends DigestBase
/*     */ {
/*     */   private final int[] X;
/*     */   private final int[] C;
/*     */   private final byte[] cBytes;
/* 115 */   private static final int[] S = { 41, 46, 67, 201, 162, 216, 124, 1, 61, 54, 84, 161, 236, 240, 6, 19, 98, 167, 5, 243, 192, 199, 115, 140, 152, 147, 43, 217, 188, 76, 130, 202, 30, 155, 87, 60, 253, 212, 224, 22, 103, 66, 111, 24, 138, 23, 229, 18, 190, 78, 196, 214, 218, 158, 222, 73, 160, 251, 245, 142, 187, 47, 238, 122, 169, 104, 121, 145, 21, 178, 7, 63, 148, 194, 16, 137, 11, 34, 95, 33, 128, 127, 93, 154, 90, 144, 50, 39, 53, 62, 204, 231, 191, 247, 151, 3, 255, 25, 48, 179, 72, 165, 181, 209, 215, 94, 146, 42, 172, 86, 170, 198, 79, 184, 56, 210, 150, 164, 125, 182, 118, 252, 107, 226, 156, 116, 4, 241, 69, 157, 112, 89, 100, 113, 135, 32, 134, 91, 207, 101, 230, 45, 168, 2, 27, 96, 37, 173, 174, 176, 185, 246, 28, 70, 97, 105, 52, 64, 126, 15, 85, 71, 163, 35, 221, 81, 175, 58, 195, 92, 249, 206, 186, 197, 234, 38, 44, 83, 13, 110, 133, 40, 132, 9, 211, 223, 205, 244, 65, 129, 77, 82, 106, 220, 55, 200, 108, 193, 171, 250, 36, 225, 123, 8, 12, 189, 177, 74, 120, 136, 149, 139, 227, 99, 232, 109, 233, 203, 213, 254, 59, 0, 29, 57, 242, 239, 183, 14, 102, 88, 208, 228, 166, 119, 114, 248, 235, 117, 75, 10, 49, 68, 80, 180, 143, 237, 31, 26, 219, 153, 141, 51, 159, 17, 131, 20 };
/*     */ 
/* 142 */   private static final byte[][] PADDING = new byte[17][];
/*     */ 
/*     */   public MD2()
/*     */   {
/*  55 */     super("MD2", 16, 16);
/*  56 */     this.X = new int[48];
/*  57 */     this.C = new int[16];
/*  58 */     this.cBytes = new byte[16];
/*     */   }
/*     */ 
/*     */   private MD2(MD2 paramMD2) {
/*  62 */     super(paramMD2);
/*  63 */     this.X = ((int[])paramMD2.X.clone());
/*  64 */     this.C = ((int[])paramMD2.C.clone());
/*  65 */     this.cBytes = new byte[16];
/*     */   }
/*     */ 
/*     */   public Object clone() {
/*  69 */     return new MD2(this);
/*     */   }
/*     */ 
/*     */   void implReset()
/*     */   {
/*  74 */     Arrays.fill(this.X, 0);
/*  75 */     Arrays.fill(this.C, 0);
/*     */   }
/*     */ 
/*     */   void implDigest(byte[] paramArrayOfByte, int paramInt)
/*     */   {
/*  80 */     int i = 16 - ((int)this.bytesProcessed & 0xF);
/*  81 */     engineUpdate(PADDING[i], 0, i);
/*  82 */     for (int j = 0; j < 16; j++) {
/*  83 */       this.cBytes[j] = ((byte)this.C[j]);
/*     */     }
/*  85 */     implCompress(this.cBytes, 0);
/*  86 */     for (j = 0; j < 16; j++)
/*  87 */       paramArrayOfByte[(paramInt + j)] = ((byte)this.X[j]);
/*     */   }
/*     */ 
/*     */   void implCompress(byte[] paramArrayOfByte, int paramInt)
/*     */   {
/*  93 */     for (int i = 0; i < 16; i++) {
/*  94 */       j = paramArrayOfByte[(paramInt + i)] & 0xFF;
/*  95 */       this.X[(16 + i)] = j;
/*  96 */       this.X[(32 + i)] = (j ^ this.X[i]);
/*     */     }
/*     */ 
/* 100 */     i = this.C[15];
/* 101 */     for (int j = 0; j < 16; j++) {
/* 102 */       i = this.C[j] ^= S[(this.X[(16 + j)] ^ i)];
/*     */     }
/*     */ 
/* 105 */     i = 0;
/* 106 */     for (j = 0; j < 18; j++) {
/* 107 */       for (int k = 0; k < 48; k++) {
/* 108 */         i = this.X[k] ^= S[i];
/*     */       }
/* 110 */       i = i + j & 0xFF;
/*     */     }
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 143 */     for (int i = 1; i < 17; i++) {
/* 144 */       byte[] arrayOfByte = new byte[i];
/* 145 */       Arrays.fill(arrayOfByte, (byte)i);
/* 146 */       PADDING[i] = arrayOfByte;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.MD2
 * JD-Core Version:    0.6.2
 */