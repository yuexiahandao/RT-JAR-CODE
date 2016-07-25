/*     */ package sun.security.provider;
/*     */ 
/*     */ public final class SHA2 extends DigestBase
/*     */ {
/*     */   private static final int ITERATION = 64;
/*  47 */   private static final int[] ROUND_CONSTS = { 1116352408, 1899447441, -1245643825, -373957723, 961987163, 1508970993, -1841331548, -1424204075, -670586216, 310598401, 607225278, 1426881987, 1925078388, -2132889090, -1680079193, -1046744716, -459576895, -272742522, 264347078, 604807628, 770255983, 1249150122, 1555081692, 1996064986, -1740746414, -1473132947, -1341970488, -1084653625, -958395405, -710438585, 113926993, 338241895, 666307205, 773529912, 1294757372, 1396182291, 1695183700, 1986661051, -2117940946, -1838011259, -1564481375, -1474664885, -1035236496, -949202525, -778901479, -694614492, -200395387, 275423344, 430227734, 506948616, 659060556, 883997877, 958139571, 1322822218, 1537002063, 1747873779, 1955562222, 2024104815, -2067236844, -1933114872, -1866530822, -1538233109, -1090935817, -965641998 };
/*     */   private final int[] W;
/*     */   private final int[] state;
/*     */ 
/*     */   public SHA2()
/*     */   {
/*  76 */     super("SHA-256", 32, 64);
/*  77 */     this.state = new int[8];
/*  78 */     this.W = new int[64];
/*  79 */     implReset();
/*     */   }
/*     */ 
/*     */   private SHA2(SHA2 paramSHA2)
/*     */   {
/*  86 */     super(paramSHA2);
/*  87 */     this.state = ((int[])paramSHA2.state.clone());
/*  88 */     this.W = new int[64];
/*     */   }
/*     */ 
/*     */   public Object clone() {
/*  92 */     return new SHA2(this);
/*     */   }
/*     */ 
/*     */   void implReset()
/*     */   {
/*  99 */     this.state[0] = 1779033703;
/* 100 */     this.state[1] = -1150833019;
/* 101 */     this.state[2] = 1013904242;
/* 102 */     this.state[3] = -1521486534;
/* 103 */     this.state[4] = 1359893119;
/* 104 */     this.state[5] = -1694144372;
/* 105 */     this.state[6] = 528734635;
/* 106 */     this.state[7] = 1541459225;
/*     */   }
/*     */ 
/*     */   void implDigest(byte[] paramArrayOfByte, int paramInt) {
/* 110 */     long l = this.bytesProcessed << 3;
/*     */ 
/* 112 */     int i = (int)this.bytesProcessed & 0x3F;
/* 113 */     int j = i < 56 ? 56 - i : 120 - i;
/* 114 */     engineUpdate(padding, 0, j);
/*     */ 
/* 116 */     ByteArrayAccess.i2bBig4((int)(l >>> 32), this.buffer, 56);
/* 117 */     ByteArrayAccess.i2bBig4((int)l, this.buffer, 60);
/* 118 */     implCompress(this.buffer, 0);
/*     */ 
/* 120 */     ByteArrayAccess.i2bBig(this.state, 0, paramArrayOfByte, paramInt, 32);
/*     */   }
/*     */ 
/*     */   private static int lf_ch(int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 131 */     return paramInt1 & paramInt2 ^ (paramInt1 ^ 0xFFFFFFFF) & paramInt3;
/*     */   }
/*     */ 
/*     */   private static int lf_maj(int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 142 */     return paramInt1 & paramInt2 ^ paramInt1 & paramInt3 ^ paramInt2 & paramInt3;
/*     */   }
/*     */ 
/*     */   private static int lf_R(int paramInt1, int paramInt2)
/*     */   {
/* 152 */     return paramInt1 >>> paramInt2;
/*     */   }
/*     */ 
/*     */   private static int lf_S(int paramInt1, int paramInt2)
/*     */   {
/* 162 */     return paramInt1 >>> paramInt2 | paramInt1 << 32 - paramInt2;
/*     */   }
/*     */ 
/*     */   private static int lf_sigma0(int paramInt)
/*     */   {
/* 171 */     return lf_S(paramInt, 2) ^ lf_S(paramInt, 13) ^ lf_S(paramInt, 22);
/*     */   }
/*     */ 
/*     */   private static int lf_sigma1(int paramInt)
/*     */   {
/* 180 */     return lf_S(paramInt, 6) ^ lf_S(paramInt, 11) ^ lf_S(paramInt, 25);
/*     */   }
/*     */ 
/*     */   private static int lf_delta0(int paramInt)
/*     */   {
/* 189 */     return lf_S(paramInt, 7) ^ lf_S(paramInt, 18) ^ lf_R(paramInt, 3);
/*     */   }
/*     */ 
/*     */   private static int lf_delta1(int paramInt)
/*     */   {
/* 198 */     return lf_S(paramInt, 17) ^ lf_S(paramInt, 19) ^ lf_R(paramInt, 10);
/*     */   }
/*     */ 
/*     */   void implCompress(byte[] paramArrayOfByte, int paramInt)
/*     */   {
/* 205 */     ByteArrayAccess.b2iBig64(paramArrayOfByte, paramInt, this.W);
/*     */ 
/* 209 */     for (int i = 16; i < 64; i++) {
/* 210 */       this.W[i] = (lf_delta1(this.W[(i - 2)]) + this.W[(i - 7)] + lf_delta0(this.W[(i - 15)]) + this.W[(i - 16)]);
/*     */     }
/*     */ 
/* 214 */     i = this.state[0];
/* 215 */     int j = this.state[1];
/* 216 */     int k = this.state[2];
/* 217 */     int m = this.state[3];
/* 218 */     int n = this.state[4];
/* 219 */     int i1 = this.state[5];
/* 220 */     int i2 = this.state[6];
/* 221 */     int i3 = this.state[7];
/*     */ 
/* 223 */     for (int i4 = 0; i4 < 64; i4++) {
/* 224 */       int i5 = i3 + lf_sigma1(n) + lf_ch(n, i1, i2) + ROUND_CONSTS[i4] + this.W[i4];
/* 225 */       int i6 = lf_sigma0(i) + lf_maj(i, j, k);
/* 226 */       i3 = i2;
/* 227 */       i2 = i1;
/* 228 */       i1 = n;
/* 229 */       n = m + i5;
/* 230 */       m = k;
/* 231 */       k = j;
/* 232 */       j = i;
/* 233 */       i = i5 + i6;
/*     */     }
/* 235 */     this.state[0] += i;
/* 236 */     this.state[1] += j;
/* 237 */     this.state[2] += k;
/* 238 */     this.state[3] += m;
/* 239 */     this.state[4] += n;
/* 240 */     this.state[5] += i1;
/* 241 */     this.state[6] += i2;
/* 242 */     this.state[7] += i3;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.SHA2
 * JD-Core Version:    0.6.2
 */