/*     */ package sun.font;
/*     */ 
/*     */ public final class ScriptRun
/*     */ {
/*     */   private char[] text;
/*     */   private int textStart;
/*     */   private int textLimit;
/*     */   private int scriptStart;
/*     */   private int scriptLimit;
/*     */   private int scriptCode;
/*     */   private int[] stack;
/*     */   private int parenSP;
/*     */   static final int SURROGATE_START = 65536;
/*     */   static final int LEAD_START = 55296;
/*     */   static final int LEAD_LIMIT = 56320;
/*     */   static final int TAIL_START = 56320;
/*     */   static final int TAIL_LIMIT = 57344;
/*     */   static final int LEAD_SURROGATE_SHIFT = 10;
/*     */   static final int SURROGATE_OFFSET = -56613888;
/*     */   static final int DONE = -1;
/* 357 */   private static int[] pairedChars = { 40, 41, 60, 62, 91, 93, 123, 125, 171, 187, 8216, 8217, 8220, 8221, 8249, 8250, 12296, 12297, 12298, 12299, 12300, 12301, 12302, 12303, 12304, 12305, 12308, 12309, 12310, 12311, 12312, 12313, 12314, 12315 };
/*     */ 
/* 377 */   private static final int pairedCharPower = 1 << highBit(pairedChars.length);
/* 378 */   private static final int pairedCharExtra = pairedChars.length - pairedCharPower;
/*     */ 
/*     */   public ScriptRun()
/*     */   {
/*     */   }
/*     */ 
/*     */   public ScriptRun(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */   {
/* 100 */     init(paramArrayOfChar, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public void init(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */   {
/* 105 */     if ((paramArrayOfChar == null) || (paramInt1 < 0) || (paramInt2 < 0) || (paramInt2 > paramArrayOfChar.length - paramInt1)) {
/* 106 */       throw new IllegalArgumentException();
/*     */     }
/*     */ 
/* 109 */     this.text = paramArrayOfChar;
/* 110 */     this.textStart = paramInt1;
/* 111 */     this.textLimit = (paramInt1 + paramInt2);
/*     */ 
/* 113 */     this.scriptStart = this.textStart;
/* 114 */     this.scriptLimit = this.textStart;
/* 115 */     this.scriptCode = -1;
/* 116 */     this.parenSP = 0;
/*     */   }
/*     */ 
/*     */   public final int getScriptStart()
/*     */   {
/* 125 */     return this.scriptStart;
/*     */   }
/*     */ 
/*     */   public final int getScriptLimit()
/*     */   {
/* 134 */     return this.scriptLimit;
/*     */   }
/*     */ 
/*     */   public final int getScriptCode()
/*     */   {
/* 144 */     return this.scriptCode;
/*     */   }
/*     */ 
/*     */   public final boolean next()
/*     */   {
/* 154 */     int i = this.parenSP;
/*     */ 
/* 157 */     if (this.scriptLimit >= this.textLimit) {
/* 158 */       return false;
/*     */     }
/*     */ 
/* 161 */     this.scriptCode = 0;
/* 162 */     this.scriptStart = this.scriptLimit;
/*     */     int j;
/* 166 */     while ((j = nextCodePoint()) != -1) {
/* 167 */       int k = ScriptRunData.getScript(j);
/* 168 */       int m = k == 0 ? getPairIndex(j) : -1;
/*     */ 
/* 176 */       if (m >= 0) {
/* 177 */         if ((m & 0x1) == 0) {
/* 178 */           if (this.stack == null) {
/* 179 */             this.stack = new int[32];
/* 180 */           } else if (this.parenSP == this.stack.length) {
/* 181 */             int[] arrayOfInt = new int[this.stack.length + 32];
/* 182 */             System.arraycopy(this.stack, 0, arrayOfInt, 0, this.stack.length);
/* 183 */             this.stack = arrayOfInt;
/*     */           }
/*     */ 
/* 186 */           this.stack[(this.parenSP++)] = m;
/* 187 */           this.stack[(this.parenSP++)] = this.scriptCode;
/* 188 */         } else if (this.parenSP > 0) {
/* 189 */           int n = m & 0xFFFFFFFE;
/*     */ 
/* 191 */           while ((this.parenSP -= 2 >= 0) && (this.stack[this.parenSP] != n));
/* 193 */           if (this.parenSP >= 0)
/* 194 */             k = this.stack[(this.parenSP + 1)];
/*     */           else {
/* 196 */             this.parenSP = 0;
/*     */           }
/* 198 */           if (this.parenSP < i) {
/* 199 */             i = this.parenSP;
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 204 */       if (sameScript(this.scriptCode, k)) {
/* 205 */         if ((this.scriptCode <= 1) && (k > 1)) {
/* 206 */           this.scriptCode = k;
/*     */ 
/* 210 */           while (i < this.parenSP) {
/* 211 */             this.stack[(i + 1)] = this.scriptCode;
/* 212 */             i += 2;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 218 */         if ((m > 0) && ((m & 0x1) != 0) && (this.parenSP > 0)) {
/* 219 */           this.parenSP -= 2;
/*     */         }
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 225 */         pushback(j);
/*     */ 
/* 228 */         break;
/*     */       }
/*     */     }
/*     */ 
/* 232 */     return true;
/*     */   }
/*     */ 
/*     */   private final int nextCodePoint()
/*     */   {
/* 246 */     if (this.scriptLimit >= this.textLimit) {
/* 247 */       return -1;
/*     */     }
/* 249 */     int i = this.text[(this.scriptLimit++)];
/* 250 */     if ((i >= 55296) && (i < 56320) && (this.scriptLimit < this.textLimit)) {
/* 251 */       int j = this.text[this.scriptLimit];
/* 252 */       if ((j >= 56320) && (j < 57344)) {
/* 253 */         this.scriptLimit += 1;
/* 254 */         i = (i << 10) + j + -56613888;
/*     */       }
/*     */     }
/* 257 */     return i;
/*     */   }
/*     */ 
/*     */   private final void pushback(int paramInt) {
/* 261 */     if (paramInt >= 0)
/* 262 */       if (paramInt >= 65536)
/* 263 */         this.scriptLimit -= 2;
/*     */       else
/* 265 */         this.scriptLimit -= 1;
/*     */   }
/*     */ 
/*     */   private static boolean sameScript(int paramInt1, int paramInt2)
/*     */   {
/* 280 */     return (paramInt1 == paramInt2) || (paramInt1 <= 1) || (paramInt2 <= 1);
/*     */   }
/*     */ 
/*     */   private static final byte highBit(int paramInt)
/*     */   {
/* 292 */     if (paramInt <= 0) {
/* 293 */       return -32;
/*     */     }
/*     */ 
/* 296 */     byte b = 0;
/*     */ 
/* 298 */     if (paramInt >= 65536) {
/* 299 */       paramInt >>= 16;
/* 300 */       b = (byte)(b + 16);
/*     */     }
/*     */ 
/* 303 */     if (paramInt >= 256) {
/* 304 */       paramInt >>= 8;
/* 305 */       b = (byte)(b + 8);
/*     */     }
/*     */ 
/* 308 */     if (paramInt >= 16) {
/* 309 */       paramInt >>= 4;
/* 310 */       b = (byte)(b + 4);
/*     */     }
/*     */ 
/* 313 */     if (paramInt >= 4) {
/* 314 */       paramInt >>= 2;
/* 315 */       b = (byte)(b + 2);
/*     */     }
/*     */ 
/* 318 */     if (paramInt >= 2) {
/* 319 */       paramInt >>= 1;
/* 320 */       b = (byte)(b + 1);
/*     */     }
/*     */ 
/* 323 */     return b;
/*     */   }
/*     */ 
/*     */   private static int getPairIndex(int paramInt)
/*     */   {
/* 334 */     int i = pairedCharPower;
/* 335 */     int j = 0;
/*     */ 
/* 337 */     if (paramInt >= pairedChars[pairedCharExtra]) {
/* 338 */       j = pairedCharExtra;
/*     */     }
/*     */ 
/* 341 */     while (i > 1) {
/* 342 */       i >>= 1;
/*     */ 
/* 344 */       if (paramInt >= pairedChars[(j + i)]) {
/* 345 */         j += i;
/*     */       }
/*     */     }
/*     */ 
/* 349 */     if (pairedChars[j] != paramInt) {
/* 350 */       j = -1;
/*     */     }
/*     */ 
/* 353 */     return j;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.font.ScriptRun
 * JD-Core Version:    0.6.2
 */