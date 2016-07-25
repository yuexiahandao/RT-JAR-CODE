/*     */ package sun.security.provider;
/*     */ 
/*     */ public final class MD5 extends DigestBase
/*     */ {
/*     */   private final int[] state;
/*     */   private final int[] x;
/*     */   private static final int S11 = 7;
/*     */   private static final int S12 = 12;
/*     */   private static final int S13 = 17;
/*     */   private static final int S14 = 22;
/*     */   private static final int S21 = 5;
/*     */   private static final int S22 = 9;
/*     */   private static final int S23 = 14;
/*     */   private static final int S24 = 20;
/*     */   private static final int S31 = 4;
/*     */   private static final int S32 = 11;
/*     */   private static final int S33 = 16;
/*     */   private static final int S34 = 23;
/*     */   private static final int S41 = 6;
/*     */   private static final int S42 = 10;
/*     */   private static final int S43 = 15;
/*     */   private static final int S44 = 21;
/*     */ 
/*     */   public MD5()
/*     */   {
/*  66 */     super("MD5", 16, 64);
/*  67 */     this.state = new int[4];
/*  68 */     this.x = new int[16];
/*  69 */     implReset();
/*     */   }
/*     */ 
/*     */   private MD5(MD5 paramMD5)
/*     */   {
/*  74 */     super(paramMD5);
/*  75 */     this.state = ((int[])paramMD5.state.clone());
/*  76 */     this.x = new int[16];
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*  81 */     return new MD5(this);
/*     */   }
/*     */ 
/*     */   void implReset()
/*     */   {
/*  89 */     this.state[0] = 1732584193;
/*  90 */     this.state[1] = -271733879;
/*  91 */     this.state[2] = -1732584194;
/*  92 */     this.state[3] = 271733878;
/*     */   }
/*     */ 
/*     */   void implDigest(byte[] paramArrayOfByte, int paramInt)
/*     */   {
/* 101 */     long l = this.bytesProcessed << 3;
/*     */ 
/* 103 */     int i = (int)this.bytesProcessed & 0x3F;
/* 104 */     int j = i < 56 ? 56 - i : 120 - i;
/* 105 */     engineUpdate(padding, 0, j);
/*     */ 
/* 107 */     ByteArrayAccess.i2bLittle4((int)l, this.buffer, 56);
/* 108 */     ByteArrayAccess.i2bLittle4((int)(l >>> 32), this.buffer, 60);
/* 109 */     implCompress(this.buffer, 0);
/*     */ 
/* 111 */     ByteArrayAccess.i2bLittle(this.state, 0, paramArrayOfByte, paramInt, 16);
/*     */   }
/*     */ 
/*     */   private static int FF(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7)
/*     */   {
/* 121 */     paramInt1 += (paramInt2 & paramInt3 | (paramInt2 ^ 0xFFFFFFFF) & paramInt4) + paramInt5 + paramInt7;
/* 122 */     return (paramInt1 << paramInt6 | paramInt1 >>> 32 - paramInt6) + paramInt2;
/*     */   }
/*     */ 
/*     */   private static int GG(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7) {
/* 126 */     paramInt1 += (paramInt2 & paramInt4 | paramInt3 & (paramInt4 ^ 0xFFFFFFFF)) + paramInt5 + paramInt7;
/* 127 */     return (paramInt1 << paramInt6 | paramInt1 >>> 32 - paramInt6) + paramInt2;
/*     */   }
/*     */ 
/*     */   private static int HH(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7) {
/* 131 */     paramInt1 += (paramInt2 ^ paramInt3 ^ paramInt4) + paramInt5 + paramInt7;
/* 132 */     return (paramInt1 << paramInt6 | paramInt1 >>> 32 - paramInt6) + paramInt2;
/*     */   }
/*     */ 
/*     */   private static int II(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7) {
/* 136 */     paramInt1 += (paramInt3 ^ (paramInt2 | paramInt4 ^ 0xFFFFFFFF)) + paramInt5 + paramInt7;
/* 137 */     return (paramInt1 << paramInt6 | paramInt1 >>> 32 - paramInt6) + paramInt2;
/*     */   }
/*     */ 
/*     */   void implCompress(byte[] paramArrayOfByte, int paramInt)
/*     */   {
/* 146 */     ByteArrayAccess.b2iLittle64(paramArrayOfByte, paramInt, this.x);
/*     */ 
/* 148 */     int i = this.state[0];
/* 149 */     int j = this.state[1];
/* 150 */     int k = this.state[2];
/* 151 */     int m = this.state[3];
/*     */ 
/* 154 */     i = FF(i, j, k, m, this.x[0], 7, -680876936);
/* 155 */     m = FF(m, i, j, k, this.x[1], 12, -389564586);
/* 156 */     k = FF(k, m, i, j, this.x[2], 17, 606105819);
/* 157 */     j = FF(j, k, m, i, this.x[3], 22, -1044525330);
/* 158 */     i = FF(i, j, k, m, this.x[4], 7, -176418897);
/* 159 */     m = FF(m, i, j, k, this.x[5], 12, 1200080426);
/* 160 */     k = FF(k, m, i, j, this.x[6], 17, -1473231341);
/* 161 */     j = FF(j, k, m, i, this.x[7], 22, -45705983);
/* 162 */     i = FF(i, j, k, m, this.x[8], 7, 1770035416);
/* 163 */     m = FF(m, i, j, k, this.x[9], 12, -1958414417);
/* 164 */     k = FF(k, m, i, j, this.x[10], 17, -42063);
/* 165 */     j = FF(j, k, m, i, this.x[11], 22, -1990404162);
/* 166 */     i = FF(i, j, k, m, this.x[12], 7, 1804603682);
/* 167 */     m = FF(m, i, j, k, this.x[13], 12, -40341101);
/* 168 */     k = FF(k, m, i, j, this.x[14], 17, -1502002290);
/* 169 */     j = FF(j, k, m, i, this.x[15], 22, 1236535329);
/*     */ 
/* 172 */     i = GG(i, j, k, m, this.x[1], 5, -165796510);
/* 173 */     m = GG(m, i, j, k, this.x[6], 9, -1069501632);
/* 174 */     k = GG(k, m, i, j, this.x[11], 14, 643717713);
/* 175 */     j = GG(j, k, m, i, this.x[0], 20, -373897302);
/* 176 */     i = GG(i, j, k, m, this.x[5], 5, -701558691);
/* 177 */     m = GG(m, i, j, k, this.x[10], 9, 38016083);
/* 178 */     k = GG(k, m, i, j, this.x[15], 14, -660478335);
/* 179 */     j = GG(j, k, m, i, this.x[4], 20, -405537848);
/* 180 */     i = GG(i, j, k, m, this.x[9], 5, 568446438);
/* 181 */     m = GG(m, i, j, k, this.x[14], 9, -1019803690);
/* 182 */     k = GG(k, m, i, j, this.x[3], 14, -187363961);
/* 183 */     j = GG(j, k, m, i, this.x[8], 20, 1163531501);
/* 184 */     i = GG(i, j, k, m, this.x[13], 5, -1444681467);
/* 185 */     m = GG(m, i, j, k, this.x[2], 9, -51403784);
/* 186 */     k = GG(k, m, i, j, this.x[7], 14, 1735328473);
/* 187 */     j = GG(j, k, m, i, this.x[12], 20, -1926607734);
/*     */ 
/* 190 */     i = HH(i, j, k, m, this.x[5], 4, -378558);
/* 191 */     m = HH(m, i, j, k, this.x[8], 11, -2022574463);
/* 192 */     k = HH(k, m, i, j, this.x[11], 16, 1839030562);
/* 193 */     j = HH(j, k, m, i, this.x[14], 23, -35309556);
/* 194 */     i = HH(i, j, k, m, this.x[1], 4, -1530992060);
/* 195 */     m = HH(m, i, j, k, this.x[4], 11, 1272893353);
/* 196 */     k = HH(k, m, i, j, this.x[7], 16, -155497632);
/* 197 */     j = HH(j, k, m, i, this.x[10], 23, -1094730640);
/* 198 */     i = HH(i, j, k, m, this.x[13], 4, 681279174);
/* 199 */     m = HH(m, i, j, k, this.x[0], 11, -358537222);
/* 200 */     k = HH(k, m, i, j, this.x[3], 16, -722521979);
/* 201 */     j = HH(j, k, m, i, this.x[6], 23, 76029189);
/* 202 */     i = HH(i, j, k, m, this.x[9], 4, -640364487);
/* 203 */     m = HH(m, i, j, k, this.x[12], 11, -421815835);
/* 204 */     k = HH(k, m, i, j, this.x[15], 16, 530742520);
/* 205 */     j = HH(j, k, m, i, this.x[2], 23, -995338651);
/*     */ 
/* 208 */     i = II(i, j, k, m, this.x[0], 6, -198630844);
/* 209 */     m = II(m, i, j, k, this.x[7], 10, 1126891415);
/* 210 */     k = II(k, m, i, j, this.x[14], 15, -1416354905);
/* 211 */     j = II(j, k, m, i, this.x[5], 21, -57434055);
/* 212 */     i = II(i, j, k, m, this.x[12], 6, 1700485571);
/* 213 */     m = II(m, i, j, k, this.x[3], 10, -1894986606);
/* 214 */     k = II(k, m, i, j, this.x[10], 15, -1051523);
/* 215 */     j = II(j, k, m, i, this.x[1], 21, -2054922799);
/* 216 */     i = II(i, j, k, m, this.x[8], 6, 1873313359);
/* 217 */     m = II(m, i, j, k, this.x[15], 10, -30611744);
/* 218 */     k = II(k, m, i, j, this.x[6], 15, -1560198380);
/* 219 */     j = II(j, k, m, i, this.x[13], 21, 1309151649);
/* 220 */     i = II(i, j, k, m, this.x[4], 6, -145523070);
/* 221 */     m = II(m, i, j, k, this.x[11], 10, -1120210379);
/* 222 */     k = II(k, m, i, j, this.x[2], 15, 718787259);
/* 223 */     j = II(j, k, m, i, this.x[9], 21, -343485551);
/*     */ 
/* 225 */     this.state[0] += i;
/* 226 */     this.state[1] += j;
/* 227 */     this.state[2] += k;
/* 228 */     this.state[3] += m;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.MD5
 * JD-Core Version:    0.6.2
 */