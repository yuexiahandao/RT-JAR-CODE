/*     */ package com.sun.media.sound;
/*     */ 
/*     */ public final class ModelStandardIndexedDirector
/*     */   implements ModelDirector
/*     */ {
/*     */   ModelPerformer[] performers;
/*     */   ModelDirectedPlayer player;
/*  37 */   boolean noteOnUsed = false;
/*  38 */   boolean noteOffUsed = false;
/*     */   byte[][] trantables;
/*     */   int[] counters;
/*     */   int[][] mat;
/*     */ 
/*     */   public ModelStandardIndexedDirector(ModelPerformer[] paramArrayOfModelPerformer, ModelDirectedPlayer paramModelDirectedPlayer)
/*     */   {
/*  47 */     this.performers = paramArrayOfModelPerformer;
/*  48 */     this.player = paramModelDirectedPlayer;
/*  49 */     for (int i = 0; i < paramArrayOfModelPerformer.length; i++) {
/*  50 */       ModelPerformer localModelPerformer = paramArrayOfModelPerformer[i];
/*  51 */       if (localModelPerformer.isReleaseTriggered())
/*  52 */         this.noteOffUsed = true;
/*     */       else {
/*  54 */         this.noteOnUsed = true;
/*     */       }
/*     */     }
/*  57 */     buildindex();
/*     */   }
/*     */ 
/*     */   private int[] lookupIndex(int paramInt1, int paramInt2) {
/*  61 */     if ((paramInt1 >= 0) && (paramInt1 < 128) && (paramInt2 >= 0) && (paramInt2 < 128)) {
/*  62 */       int i = this.trantables[0][paramInt1];
/*  63 */       int j = this.trantables[1][paramInt2];
/*  64 */       if ((i != -1) && (j != -1)) {
/*  65 */         return this.mat[(i + j * this.counters[0])];
/*     */       }
/*     */     }
/*  68 */     return null;
/*     */   }
/*     */ 
/*     */   private int restrict(int paramInt) {
/*  72 */     if (paramInt < 0) return 0;
/*  73 */     if (paramInt > 127) return 127;
/*  74 */     return paramInt;
/*     */   }
/*     */ 
/*     */   private void buildindex() {
/*  78 */     this.trantables = new byte[2][''];
/*  79 */     this.counters = new int[this.trantables.length];
/*     */     int n;
/*     */     int i1;
/*     */     int i2;
/*     */     int i3;
/*  80 */     for (ModelPerformer localModelPerformer : this.performers) {
/*  81 */       n = localModelPerformer.getKeyFrom();
/*  82 */       i1 = localModelPerformer.getKeyTo();
/*  83 */       i2 = localModelPerformer.getVelFrom();
/*  84 */       i3 = localModelPerformer.getVelTo();
/*  85 */       if ((n <= i1) && 
/*  86 */         (i2 <= i3)) {
/*  87 */         n = restrict(n);
/*  88 */         i1 = restrict(i1);
/*  89 */         i2 = restrict(i2);
/*  90 */         i3 = restrict(i3);
/*  91 */         this.trantables[0][n] = 1;
/*  92 */         this.trantables[0][(i1 + 1)] = 1;
/*  93 */         this.trantables[1][i2] = 1;
/*  94 */         this.trantables[1][(i3 + 1)] = 1;
/*     */       }
/*     */     }
/*     */     Object localObject1;
/*     */     int m;
/*  96 */     for (int i = 0; i < this.trantables.length; i++) {
/*  97 */       localObject1 = this.trantables[i];
/*  98 */       ??? = localObject1.length;
/*  99 */       for (m = ??? - 1; m >= 0; m--) {
/* 100 */         if (localObject1[m] == 1) {
/* 101 */           localObject1[m] = -1;
/* 102 */           break;
/*     */         }
/* 104 */         localObject1[m] = -1;
/*     */       }
/* 106 */       m = -1;
/* 107 */       for (n = 0; n < ???; n++) {
/* 108 */         if (localObject1[n] != 0) {
/* 109 */           m++;
/* 110 */           if (localObject1[n] == -1)
/*     */             break;
/*     */         }
/* 113 */         localObject1[n] = ((byte)m);
/*     */       }
/* 115 */       this.counters[i] = m;
/*     */     }
/* 117 */     this.mat = new int[this.counters[0] * this.counters[1]][];
/* 118 */     i = 0;
/* 119 */     for (Object localObject2 : this.performers) {
/* 120 */       i1 = localObject2.getKeyFrom();
/* 121 */       i2 = localObject2.getKeyTo();
/* 122 */       i3 = localObject2.getVelFrom();
/* 123 */       int i4 = localObject2.getVelTo();
/* 124 */       if ((i1 <= i2) && 
/* 125 */         (i3 <= i4)) {
/* 126 */         i1 = restrict(i1);
/* 127 */         i2 = restrict(i2);
/* 128 */         i3 = restrict(i3);
/* 129 */         i4 = restrict(i4);
/* 130 */         int i5 = this.trantables[0][i1];
/* 131 */         int i6 = this.trantables[0][(i2 + 1)];
/* 132 */         int i7 = this.trantables[1][i3];
/* 133 */         int i8 = this.trantables[1][(i4 + 1)];
/* 134 */         if (i6 == -1)
/* 135 */           i6 = this.counters[0];
/* 136 */         if (i8 == -1)
/* 137 */           i8 = this.counters[1];
/* 138 */         for (int i9 = i7; i9 < i8; i9++) {
/* 139 */           int i10 = i5 + i9 * this.counters[0];
/* 140 */           for (int i11 = i5; i11 < i6; i11++) {
/* 141 */             int[] arrayOfInt1 = this.mat[i10];
/* 142 */             if (arrayOfInt1 == null) {
/* 143 */               this.mat[i10] = { i };
/*     */             } else {
/* 145 */               int[] arrayOfInt2 = new int[arrayOfInt1.length + 1];
/* 146 */               arrayOfInt2[(arrayOfInt2.length - 1)] = i;
/* 147 */               for (int i12 = 0; i12 < arrayOfInt1.length; i12++)
/* 148 */                 arrayOfInt2[i12] = arrayOfInt1[i12];
/* 149 */               this.mat[i10] = arrayOfInt2;
/*     */             }
/* 151 */             i10++;
/*     */           }
/*     */         }
/* 154 */         i++;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void close() {
/*     */   }
/*     */ 
/* 162 */   public void noteOff(int paramInt1, int paramInt2) { if (!this.noteOffUsed)
/* 163 */       return;
/* 164 */     int[] arrayOfInt1 = lookupIndex(paramInt1, paramInt2);
/* 165 */     if (arrayOfInt1 == null) return;
/* 166 */     for (int k : arrayOfInt1) {
/* 167 */       ModelPerformer localModelPerformer = this.performers[k];
/* 168 */       if (localModelPerformer.isReleaseTriggered())
/* 169 */         this.player.play(k, null);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void noteOn(int paramInt1, int paramInt2)
/*     */   {
/* 175 */     if (!this.noteOnUsed)
/* 176 */       return;
/* 177 */     int[] arrayOfInt1 = lookupIndex(paramInt1, paramInt2);
/* 178 */     if (arrayOfInt1 == null) return;
/* 179 */     for (int k : arrayOfInt1) {
/* 180 */       ModelPerformer localModelPerformer = this.performers[k];
/* 181 */       if (!localModelPerformer.isReleaseTriggered())
/* 182 */         this.player.play(k, null);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.ModelStandardIndexedDirector
 * JD-Core Version:    0.6.2
 */