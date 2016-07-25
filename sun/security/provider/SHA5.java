/*     */ package sun.security.provider;
/*     */ 
/*     */ abstract class SHA5 extends DigestBase
/*     */ {
/*     */   private static final int ITERATION = 80;
/*  54 */   private static final long[] ROUND_CONSTS = { 4794697086780616226L, 8158064640168781261L, -5349999486874862801L, -1606136188198331460L, 4131703408338449720L, 6480981068601479193L, -7908458776815382629L, -6116909921290321640L, -2880145864133508542L, 1334009975649890238L, 2608012711638119052L, 6128411473006802146L, 8268148722764581231L, -9160688886553864527L, -7215885187991268811L, -4495734319001033068L, -1973867731355612462L, -1171420211273849373L, 1135362057144423861L, 2597628984639134821L, 3308224258029322869L, 5365058923640841347L, 6679025012923562964L, 8573033837759648693L, -7476448914759557205L, -6327057829258317296L, -5763719355590565569L, -4658551843659510044L, -4116276920077217854L, -3051310485924567259L, 489312712824947311L, 1452737877330783856L, 2861767655752347644L, 3322285676063803686L, 5560940570517711597L, 5996557281743188959L, 7280758554555802590L, 8532644243296465576L, -9096487096722542874L, -7894198246740708037L, -6719396339535248540L, -6333637450476146687L, -4446306890439682159L, -4076793802049405392L, -3345356375505022440L, -2983346525034927856L, -860691631967231958L, 1182934255886127544L, 1847814050463011016L, 2177327727835720531L, 2830643537854262169L, 3796741975233480872L, 4115178125766777443L, 5681478168544905931L, 6601373596472566643L, 7507060721942968483L, 8399075790359081724L, 8693463985226723168L, -8878714635349349518L, -8302665154208450068L, -8016688836872298968L, -6606660893046293015L, -4685533653050689259L, -4147400797238176981L, -3880063495543823972L, -3348786107499101689L, -1523767162380948706L, -757361751448694408L, 500013540394364858L, 748580250866718886L, 1242879168328830382L, 1977374033974150939L, 2944078676154940804L, 3659926193048069267L, 4368137639120453308L, 4836135668995329356L, 5532061633213252278L, 6448918945643986474L, 6902733635092675308L, 7801388544844847127L };
/*     */   private final long[] W;
/*     */   private final long[] state;
/*     */   private final long[] initialHashes;
/*     */ 
/*     */   SHA5(String paramString, int paramInt, long[] paramArrayOfLong)
/*     */   {
/*  97 */     super(paramString, paramInt, 128);
/*  98 */     this.initialHashes = paramArrayOfLong;
/*  99 */     this.state = new long[8];
/* 100 */     this.W = new long[80];
/* 101 */     implReset();
/*     */   }
/*     */ 
/*     */   SHA5(SHA5 paramSHA5)
/*     */   {
/* 108 */     super(paramSHA5);
/* 109 */     this.initialHashes = paramSHA5.initialHashes;
/* 110 */     this.state = ((long[])paramSHA5.state.clone());
/* 111 */     this.W = new long[80];
/*     */   }
/*     */ 
/*     */   final void implReset() {
/* 115 */     System.arraycopy(this.initialHashes, 0, this.state, 0, this.state.length);
/*     */   }
/*     */ 
/*     */   final void implDigest(byte[] paramArrayOfByte, int paramInt) {
/* 119 */     long l = this.bytesProcessed << 3;
/*     */ 
/* 121 */     int i = (int)this.bytesProcessed & 0x7F;
/* 122 */     int j = i < 112 ? 112 - i : 240 - i;
/* 123 */     engineUpdate(padding, 0, j + 8);
/*     */ 
/* 125 */     ByteArrayAccess.i2bBig4((int)(l >>> 32), this.buffer, 120);
/* 126 */     ByteArrayAccess.i2bBig4((int)l, this.buffer, 124);
/* 127 */     implCompress(this.buffer, 0);
/*     */ 
/* 129 */     ByteArrayAccess.l2bBig(this.state, 0, paramArrayOfByte, paramInt, engineGetDigestLength());
/*     */   }
/*     */ 
/*     */   private static long lf_ch(long paramLong1, long paramLong2, long paramLong3)
/*     */   {
/* 140 */     return paramLong1 & paramLong2 ^ (paramLong1 ^ 0xFFFFFFFF) & paramLong3;
/*     */   }
/*     */ 
/*     */   private static long lf_maj(long paramLong1, long paramLong2, long paramLong3)
/*     */   {
/* 151 */     return paramLong1 & paramLong2 ^ paramLong1 & paramLong3 ^ paramLong2 & paramLong3;
/*     */   }
/*     */ 
/*     */   private static long lf_R(long paramLong, int paramInt)
/*     */   {
/* 161 */     return paramLong >>> paramInt;
/*     */   }
/*     */ 
/*     */   private static long lf_S(long paramLong, int paramInt)
/*     */   {
/* 171 */     return paramLong >>> paramInt | paramLong << 64 - paramInt;
/*     */   }
/*     */ 
/*     */   private static long lf_sigma0(long paramLong)
/*     */   {
/* 180 */     return lf_S(paramLong, 28) ^ lf_S(paramLong, 34) ^ lf_S(paramLong, 39);
/*     */   }
/*     */ 
/*     */   private static long lf_sigma1(long paramLong)
/*     */   {
/* 189 */     return lf_S(paramLong, 14) ^ lf_S(paramLong, 18) ^ lf_S(paramLong, 41);
/*     */   }
/*     */ 
/*     */   private static long lf_delta0(long paramLong)
/*     */   {
/* 198 */     return lf_S(paramLong, 1) ^ lf_S(paramLong, 8) ^ lf_R(paramLong, 7);
/*     */   }
/*     */ 
/*     */   private static long lf_delta1(long paramLong)
/*     */   {
/* 207 */     return lf_S(paramLong, 19) ^ lf_S(paramLong, 61) ^ lf_R(paramLong, 6);
/*     */   }
/*     */ 
/*     */   final void implCompress(byte[] paramArrayOfByte, int paramInt)
/*     */   {
/* 218 */     ByteArrayAccess.b2lBig128(paramArrayOfByte, paramInt, this.W);
/*     */ 
/* 222 */     for (int i = 16; i < 80; i++) {
/* 223 */       this.W[i] = (lf_delta1(this.W[(i - 2)]) + this.W[(i - 7)] + lf_delta0(this.W[(i - 15)]) + this.W[(i - 16)]);
/*     */     }
/*     */ 
/* 227 */     long l1 = this.state[0];
/* 228 */     long l2 = this.state[1];
/* 229 */     long l3 = this.state[2];
/* 230 */     long l4 = this.state[3];
/* 231 */     long l5 = this.state[4];
/* 232 */     long l6 = this.state[5];
/* 233 */     long l7 = this.state[6];
/* 234 */     long l8 = this.state[7];
/*     */ 
/* 236 */     for (int j = 0; j < 80; j++) {
/* 237 */       long l9 = l8 + lf_sigma1(l5) + lf_ch(l5, l6, l7) + ROUND_CONSTS[j] + this.W[j];
/* 238 */       long l10 = lf_sigma0(l1) + lf_maj(l1, l2, l3);
/* 239 */       l8 = l7;
/* 240 */       l7 = l6;
/* 241 */       l6 = l5;
/* 242 */       l5 = l4 + l9;
/* 243 */       l4 = l3;
/* 244 */       l3 = l2;
/* 245 */       l2 = l1;
/* 246 */       l1 = l9 + l10;
/*     */     }
/* 248 */     this.state[0] += l1;
/* 249 */     this.state[1] += l2;
/* 250 */     this.state[2] += l3;
/* 251 */     this.state[3] += l4;
/* 252 */     this.state[4] += l5;
/* 253 */     this.state[5] += l6;
/* 254 */     this.state[6] += l7;
/* 255 */     this.state[7] += l8;
/*     */   }
/*     */ 
/*     */   public static final class SHA384 extends SHA5
/*     */   {
/* 288 */     private static final long[] INITIAL_HASHES = { -3766243637369397544L, 7105036623409894663L, -7973340178411365097L, 1526699215303891257L, 7436329637833083697L, -8163818279084223215L, -2662702644619276377L, 5167115440072839076L };
/*     */ 
/*     */     public SHA384()
/*     */     {
/* 296 */       super(48, INITIAL_HASHES);
/*     */     }
/*     */ 
/*     */     private SHA384(SHA384 paramSHA384) {
/* 300 */       super();
/*     */     }
/*     */ 
/*     */     public Object clone() {
/* 304 */       return new SHA384(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class SHA512 extends SHA5
/*     */   {
/* 263 */     private static final long[] INITIAL_HASHES = { 7640891576956012808L, -4942790177534073029L, 4354685564936845355L, -6534734903238641935L, 5840696475078001361L, -7276294671716946913L, 2270897969802886507L, 6620516959819538809L };
/*     */ 
/*     */     public SHA512()
/*     */     {
/* 271 */       super(64, INITIAL_HASHES);
/*     */     }
/*     */ 
/*     */     private SHA512(SHA512 paramSHA512) {
/* 275 */       super();
/*     */     }
/*     */ 
/*     */     public Object clone() {
/* 279 */       return new SHA512(this);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.SHA5
 * JD-Core Version:    0.6.2
 */