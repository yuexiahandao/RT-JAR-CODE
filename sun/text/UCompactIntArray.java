/*     */ package sun.text;
/*     */ 
/*     */ public final class UCompactIntArray
/*     */   implements Cloneable
/*     */ {
/*     */   private static final int PLANEMASK = 196608;
/*     */   private static final int PLANESHIFT = 16;
/*     */   private static final int PLANECOUNT = 16;
/*     */   private static final int CODEPOINTMASK = 65535;
/*     */   private static final int UNICODECOUNT = 65536;
/*     */   private static final int BLOCKSHIFT = 7;
/*     */   private static final int BLOCKCOUNT = 128;
/*     */   private static final int INDEXSHIFT = 9;
/*     */   private static final int INDEXCOUNT = 512;
/*     */   private static final int BLOCKMASK = 127;
/*     */   private int defaultValue;
/*     */   private int[][] values;
/*     */   private short[][] indices;
/*     */   private boolean isCompact;
/*     */   private boolean[][] blockTouched;
/*     */   private boolean[] planeTouched;
/*     */ 
/*     */   public UCompactIntArray()
/*     */   {
/*  34 */     this.values = new int[16][];
/*  35 */     this.indices = new short[16][];
/*  36 */     this.blockTouched = new boolean[16][];
/*  37 */     this.planeTouched = new boolean[16];
/*     */   }
/*     */ 
/*     */   public UCompactIntArray(int paramInt) {
/*  41 */     this();
/*  42 */     this.defaultValue = paramInt;
/*     */   }
/*     */ 
/*     */   public int elementAt(int paramInt)
/*     */   {
/*  51 */     int i = (paramInt & 0x30000) >> 16;
/*  52 */     if (this.planeTouched[i] == 0) {
/*  53 */       return this.defaultValue;
/*     */     }
/*  55 */     paramInt &= 65535;
/*  56 */     return this.values[i][((this.indices[i][(paramInt >> 7)] & 0xFFFF) + (paramInt & 0x7F))];
/*     */   }
/*     */ 
/*     */   public void setElementAt(int paramInt1, int paramInt2)
/*     */   {
/*  68 */     if (this.isCompact) {
/*  69 */       expand();
/*     */     }
/*  71 */     int i = (paramInt1 & 0x30000) >> 16;
/*  72 */     if (this.planeTouched[i] == 0) {
/*  73 */       initPlane(i);
/*     */     }
/*  75 */     paramInt1 &= 65535;
/*  76 */     this.values[i][paramInt1] = paramInt2;
/*  77 */     this.blockTouched[i][(paramInt1 >> 7)] = 1;
/*     */   }
/*     */ 
/*     */   public void compact()
/*     */   {
/*  85 */     if (this.isCompact) {
/*  86 */       return;
/*     */     }
/*  88 */     for (int i = 0; i < 16; i++)
/*  89 */       if (this.planeTouched[i] != 0)
/*     */       {
/*  92 */         int j = 0;
/*  93 */         int k = 0;
/*  94 */         int m = -1;
/*     */ 
/*  96 */         for (int n = 0; n < this.indices[i].length; k += 128) {
/*  97 */           this.indices[i][n] = -1;
/*  98 */           if ((this.blockTouched[i][n] == 0) && (m != -1))
/*     */           {
/* 102 */             this.indices[i][n] = m;
/*     */           } else {
/* 104 */             int i1 = j * 128;
/* 105 */             if (n > j) {
/* 106 */               System.arraycopy(this.values[i], k, this.values[i], i1, 128);
/*     */             }
/*     */ 
/* 109 */             if (this.blockTouched[i][n] == 0)
/*     */             {
/* 111 */               m = (short)i1;
/*     */             }
/* 113 */             this.indices[i][n] = ((short)i1);
/* 114 */             j++;
/*     */           }
/*  96 */           n++;
/*     */         }
/*     */ 
/* 119 */         n = j * 128;
/* 120 */         int[] arrayOfInt = new int[n];
/* 121 */         System.arraycopy(this.values[i], 0, arrayOfInt, 0, n);
/* 122 */         this.values[i] = arrayOfInt;
/* 123 */         this.blockTouched[i] = null;
/*     */       }
/* 125 */     this.isCompact = true;
/*     */   }
/*     */ 
/*     */   private void expand()
/*     */   {
/* 137 */     if (this.isCompact)
/*     */     {
/* 139 */       for (int j = 0; j < 16; j++)
/* 140 */         if (this.planeTouched[j] != 0)
/*     */         {
/* 143 */           this.blockTouched[j] = new boolean[512];
/* 144 */           int[] arrayOfInt = new int[65536];
/* 145 */           for (int i = 0; i < 65536; i++) {
/* 146 */             arrayOfInt[i] = this.values[j][(this.indices[j][(i >> 7)] & 65535 + (i & 0x7F))];
/*     */ 
/* 148 */             this.blockTouched[j][(i >> 7)] = 1;
/*     */           }
/* 150 */           for (i = 0; i < 512; i++) {
/* 151 */             this.indices[j][i] = ((short)(i << 7));
/*     */           }
/* 153 */           this.values[j] = arrayOfInt;
/*     */         }
/* 155 */       this.isCompact = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void initPlane(int paramInt) {
/* 160 */     this.values[paramInt] = new int[65536];
/* 161 */     this.indices[paramInt] = new short[512];
/* 162 */     this.blockTouched[paramInt] = new boolean[512];
/* 163 */     this.planeTouched[paramInt] = true;
/*     */ 
/* 165 */     if ((this.planeTouched[0] != 0) && (paramInt != 0))
/* 166 */       System.arraycopy(this.indices[0], 0, this.indices[paramInt], 0, 512);
/*     */     else {
/* 168 */       for (i = 0; i < 512; i++) {
/* 169 */         this.indices[paramInt][i] = ((short)(i << 7));
/*     */       }
/*     */     }
/* 172 */     for (int i = 0; i < 65536; i++)
/* 173 */       this.values[paramInt][i] = this.defaultValue;
/*     */   }
/*     */ 
/*     */   public int getKSize()
/*     */   {
/* 178 */     int i = 0;
/* 179 */     for (int j = 0; j < 16; j++) {
/* 180 */       if (this.planeTouched[j] != 0) {
/* 181 */         i += this.values[j].length * 4 + this.indices[j].length * 2;
/*     */       }
/*     */     }
/* 184 */     return i / 1024;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.text.UCompactIntArray
 * JD-Core Version:    0.6.2
 */