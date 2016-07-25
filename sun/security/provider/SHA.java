/*     */ package sun.security.provider;
/*     */ 
/*     */ public final class SHA extends DigestBase
/*     */ {
/*     */   private final int[] W;
/*     */   private final int[] state;
/*     */   private static final int round1_kt = 1518500249;
/*     */   private static final int round2_kt = 1859775393;
/*     */   private static final int round3_kt = -1894007588;
/*     */   private static final int round4_kt = -899497514;
/*     */ 
/*     */   public SHA()
/*     */   {
/*  59 */     super("SHA-1", 20, 64);
/*  60 */     this.state = new int[5];
/*  61 */     this.W = new int[80];
/*  62 */     implReset();
/*     */   }
/*     */ 
/*     */   private SHA(SHA paramSHA)
/*     */   {
/*  68 */     super(paramSHA);
/*  69 */     this.state = ((int[])paramSHA.state.clone());
/*  70 */     this.W = new int[80];
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*  77 */     return new SHA(this);
/*     */   }
/*     */ 
/*     */   void implReset()
/*     */   {
/*  84 */     this.state[0] = 1732584193;
/*  85 */     this.state[1] = -271733879;
/*  86 */     this.state[2] = -1732584194;
/*  87 */     this.state[3] = 271733878;
/*  88 */     this.state[4] = -1009589776;
/*     */   }
/*     */ 
/*     */   void implDigest(byte[] paramArrayOfByte, int paramInt)
/*     */   {
/*  95 */     long l = this.bytesProcessed << 3;
/*     */ 
/*  97 */     int i = (int)this.bytesProcessed & 0x3F;
/*  98 */     int j = i < 56 ? 56 - i : 120 - i;
/*  99 */     engineUpdate(padding, 0, j);
/*     */ 
/* 101 */     ByteArrayAccess.i2bBig4((int)(l >>> 32), this.buffer, 56);
/* 102 */     ByteArrayAccess.i2bBig4((int)l, this.buffer, 60);
/* 103 */     implCompress(this.buffer, 0);
/*     */ 
/* 105 */     ByteArrayAccess.i2bBig(this.state, 0, paramArrayOfByte, paramInt, 20);
/*     */   }
/*     */ 
/*     */   void implCompress(byte[] paramArrayOfByte, int paramInt)
/*     */   {
/* 122 */     ByteArrayAccess.b2iBig64(paramArrayOfByte, paramInt, this.W);
/*     */ 
/* 126 */     for (int i = 16; i <= 79; i++) {
/* 127 */       j = this.W[(i - 3)] ^ this.W[(i - 8)] ^ this.W[(i - 14)] ^ this.W[(i - 16)];
/* 128 */       this.W[i] = (j << 1 | j >>> 31);
/*     */     }
/*     */ 
/* 131 */     i = this.state[0];
/* 132 */     int j = this.state[1];
/* 133 */     int k = this.state[2];
/* 134 */     int m = this.state[3];
/* 135 */     int n = this.state[4];
/*     */     int i2;
/* 138 */     for (int i1 = 0; i1 < 20; i1++) {
/* 139 */       i2 = (i << 5 | i >>> 27) + (j & k | (j ^ 0xFFFFFFFF) & m) + n + this.W[i1] + 1518500249;
/*     */ 
/* 141 */       n = m;
/* 142 */       m = k;
/* 143 */       k = j << 30 | j >>> 2;
/* 144 */       j = i;
/* 145 */       i = i2;
/*     */     }
/*     */ 
/* 149 */     for (i1 = 20; i1 < 40; i1++) {
/* 150 */       i2 = (i << 5 | i >>> 27) + (j ^ k ^ m) + n + this.W[i1] + 1859775393;
/*     */ 
/* 152 */       n = m;
/* 153 */       m = k;
/* 154 */       k = j << 30 | j >>> 2;
/* 155 */       j = i;
/* 156 */       i = i2;
/*     */     }
/*     */ 
/* 160 */     for (i1 = 40; i1 < 60; i1++) {
/* 161 */       i2 = (i << 5 | i >>> 27) + (j & k | j & m | k & m) + n + this.W[i1] + -1894007588;
/*     */ 
/* 163 */       n = m;
/* 164 */       m = k;
/* 165 */       k = j << 30 | j >>> 2;
/* 166 */       j = i;
/* 167 */       i = i2;
/*     */     }
/*     */ 
/* 171 */     for (i1 = 60; i1 < 80; i1++) {
/* 172 */       i2 = (i << 5 | i >>> 27) + (j ^ k ^ m) + n + this.W[i1] + -899497514;
/*     */ 
/* 174 */       n = m;
/* 175 */       m = k;
/* 176 */       k = j << 30 | j >>> 2;
/* 177 */       j = i;
/* 178 */       i = i2;
/*     */     }
/* 180 */     this.state[0] += i;
/* 181 */     this.state[1] += j;
/* 182 */     this.state[2] += k;
/* 183 */     this.state[3] += m;
/* 184 */     this.state[4] += n;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.SHA
 * JD-Core Version:    0.6.2
 */