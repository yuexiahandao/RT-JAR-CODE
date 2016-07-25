/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.Arrays;
/*     */ import javax.sound.midi.Patch;
/*     */ 
/*     */ public final class SoftTuning
/*     */ {
/*  40 */   private String name = null;
/*  41 */   private final double[] tuning = new double[''];
/*  42 */   private Patch patch = null;
/*     */ 
/*     */   public SoftTuning() {
/*  45 */     this.name = "12-TET";
/*  46 */     for (int i = 0; i < this.tuning.length; i++)
/*  47 */       this.tuning[i] = (i * 100);
/*     */   }
/*     */ 
/*     */   public SoftTuning(byte[] paramArrayOfByte) {
/*  51 */     for (int i = 0; i < this.tuning.length; i++)
/*  52 */       this.tuning[i] = (i * 100);
/*  53 */     load(paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   public SoftTuning(Patch paramPatch) {
/*  57 */     this.patch = paramPatch;
/*  58 */     this.name = "12-TET";
/*  59 */     for (int i = 0; i < this.tuning.length; i++)
/*  60 */       this.tuning[i] = (i * 100);
/*     */   }
/*     */ 
/*     */   public SoftTuning(Patch paramPatch, byte[] paramArrayOfByte) {
/*  64 */     this.patch = paramPatch;
/*  65 */     for (int i = 0; i < this.tuning.length; i++)
/*  66 */       this.tuning[i] = (i * 100);
/*  67 */     load(paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   private boolean checksumOK(byte[] paramArrayOfByte) {
/*  71 */     int i = paramArrayOfByte[1] & 0xFF;
/*  72 */     for (int j = 2; j < paramArrayOfByte.length - 2; j++)
/*  73 */       i ^= paramArrayOfByte[j] & 0xFF;
/*  74 */     return (paramArrayOfByte[(paramArrayOfByte.length - 2)] & 0xFF) == (i & 0x7F);
/*     */   }
/*     */ 
/*     */   public void load(byte[] paramArrayOfByte)
/*     */   {
/*  90 */     if (((paramArrayOfByte[1] & 0xFF) == 126) || ((paramArrayOfByte[1] & 0xFF) == 127)) {
/*  91 */       int i = paramArrayOfByte[3] & 0xFF;
/*  92 */       switch (i) {
/*     */       case 8:
/*  94 */         int j = paramArrayOfByte[4] & 0xFF;
/*     */         int k;
/*     */         int i1;
/*     */         int i2;
/*     */         int i3;
/*     */         int i4;
/*     */         int i5;
/*     */         int i6;
/*     */         Object localObject;
/*  95 */         switch (j)
/*     */         {
/*     */         case 1:
/*     */           try
/*     */           {
/* 102 */             this.name = new String(paramArrayOfByte, 6, 16, "ascii");
/*     */           } catch (UnsupportedEncodingException localUnsupportedEncodingException1) {
/* 104 */             this.name = null;
/*     */           }
/* 106 */           k = 22;
/* 107 */           for (i1 = 0; i1 < 128; i1++) {
/* 108 */             i2 = paramArrayOfByte[(k++)] & 0xFF;
/* 109 */             i3 = paramArrayOfByte[(k++)] & 0xFF;
/* 110 */             i4 = paramArrayOfByte[(k++)] & 0xFF;
/* 111 */             if ((i2 != 127) || (i3 != 127) || (i4 != 127)) {
/* 112 */               this.tuning[i1] = (100.0D * ((i2 * 16384 + i3 * 128 + i4) / 16384.0D));
/*     */             }
/*     */           }
/* 115 */           break;
/*     */         case 2:
/* 120 */           k = paramArrayOfByte[6] & 0xFF;
/* 121 */           i1 = 7;
/* 122 */           for (i2 = 0; i2 < k; i2++) {
/* 123 */             i3 = paramArrayOfByte[(i1++)] & 0xFF;
/* 124 */             i4 = paramArrayOfByte[(i1++)] & 0xFF;
/* 125 */             i5 = paramArrayOfByte[(i1++)] & 0xFF;
/* 126 */             i6 = paramArrayOfByte[(i1++)] & 0xFF;
/* 127 */             if ((i4 != 127) || (i5 != 127) || (i6 != 127))
/* 128 */               this.tuning[i3] = (100.0D * ((i4 * 16384 + i5 * 128 + i6) / 16384.0D));
/*     */           }
/* 130 */           break;
/*     */         case 4:
/* 135 */           if (checksumOK(paramArrayOfByte))
/*     */           {
/*     */             try {
/* 138 */               this.name = new String(paramArrayOfByte, 7, 16, "ascii");
/*     */             } catch (UnsupportedEncodingException localUnsupportedEncodingException2) {
/* 140 */               this.name = null;
/*     */             }
/* 142 */             int m = 23;
/* 143 */             for (i1 = 0; i1 < 128; i1++) {
/* 144 */               i2 = paramArrayOfByte[(m++)] & 0xFF;
/* 145 */               i3 = paramArrayOfByte[(m++)] & 0xFF;
/* 146 */               i4 = paramArrayOfByte[(m++)] & 0xFF;
/* 147 */               if ((i2 != 127) || (i3 != 127) || (i4 != 127))
/* 148 */                 this.tuning[i1] = (100.0D * ((i2 * 16384 + i3 * 128 + i4) / 16384.0D)); 
/*     */             }
/*     */           }
/* 150 */           break;
/*     */         case 5:
/* 156 */           if (checksumOK(paramArrayOfByte))
/*     */           {
/*     */             try {
/* 159 */               this.name = new String(paramArrayOfByte, 7, 16, "ascii");
/*     */             } catch (UnsupportedEncodingException localUnsupportedEncodingException3) {
/* 161 */               this.name = null;
/*     */             }
/* 163 */             int[] arrayOfInt = new int[12];
/* 164 */             for (i1 = 0; i1 < 12; i1++)
/* 165 */               arrayOfInt[i1] = ((paramArrayOfByte[(i1 + 23)] & 0xFF) - 64);
/* 166 */             for (i1 = 0; i1 < this.tuning.length; i1++)
/* 167 */               this.tuning[i1] = (i1 * 100 + arrayOfInt[(i1 % 12)]); 
/*     */           }
/* 168 */           break;
/*     */         case 6:
/* 174 */           if (checksumOK(paramArrayOfByte))
/*     */           {
/*     */             try {
/* 177 */               this.name = new String(paramArrayOfByte, 7, 16, "ascii");
/*     */             } catch (UnsupportedEncodingException localUnsupportedEncodingException4) {
/* 179 */               this.name = null;
/*     */             }
/* 181 */             double[] arrayOfDouble = new double[12];
/* 182 */             for (i1 = 0; i1 < 12; i1++) {
/* 183 */               i2 = (paramArrayOfByte[(i1 * 2 + 23)] & 0xFF) * 128 + (paramArrayOfByte[(i1 * 2 + 24)] & 0xFF);
/*     */ 
/* 185 */               arrayOfDouble[i1] = ((i2 / 8192.0D - 1.0D) * 100.0D);
/*     */             }
/* 187 */             for (i1 = 0; i1 < this.tuning.length; i1++)
/* 188 */               this.tuning[i1] = (i1 * 100 + arrayOfDouble[(i1 % 12)]); 
/*     */           }
/* 189 */           break;
/*     */         case 7:
/* 194 */           int n = paramArrayOfByte[7] & 0xFF;
/* 195 */           i1 = 8;
/* 196 */           for (i2 = 0; i2 < n; i2++) {
/* 197 */             i3 = paramArrayOfByte[(i1++)] & 0xFF;
/* 198 */             i4 = paramArrayOfByte[(i1++)] & 0xFF;
/* 199 */             i5 = paramArrayOfByte[(i1++)] & 0xFF;
/* 200 */             i6 = paramArrayOfByte[(i1++)] & 0xFF;
/* 201 */             if ((i4 != 127) || (i5 != 127) || (i6 != 127)) {
/* 202 */               this.tuning[i3] = (100.0D * ((i4 * 16384 + i5 * 128 + i6) / 16384.0D));
/*     */             }
/*     */           }
/* 205 */           break;
/*     */         case 8:
/* 210 */           localObject = new int[12];
/* 211 */           for (i3 = 0; i3 < 12; i3++)
/* 212 */             localObject[i3] = ((paramArrayOfByte[(i3 + 8)] & 0xFF) - 64);
/* 213 */           for (i3 = 0; i3 < this.tuning.length; i3++)
/* 214 */             this.tuning[i3] = (i3 * 100 + localObject[(i3 % 12)]);
/* 215 */           break;
/*     */         case 9:
/* 221 */           localObject = new double[12];
/* 222 */           for (i3 = 0; i3 < 12; i3++) {
/* 223 */             i4 = (paramArrayOfByte[(i3 * 2 + 8)] & 0xFF) * 128 + (paramArrayOfByte[(i3 * 2 + 9)] & 0xFF);
/*     */ 
/* 225 */             localObject[i3] = ((i4 / 8192.0D - 1.0D) * 100.0D);
/*     */           }
/* 227 */           for (i3 = 0; i3 < this.tuning.length; i3++)
/* 228 */             this.tuning[i3] = (i3 * 100 + localObject[(i3 % 12)]); case 3:
/* 229 */         }break;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public double[] getTuning()
/*     */   {
/* 241 */     return Arrays.copyOf(this.tuning, this.tuning.length);
/*     */   }
/*     */ 
/*     */   public double getTuning(int paramInt) {
/* 245 */     return this.tuning[paramInt];
/*     */   }
/*     */ 
/*     */   public Patch getPatch() {
/* 249 */     return this.patch;
/*     */   }
/*     */ 
/*     */   public String getName() {
/* 253 */     return this.name;
/*     */   }
/*     */ 
/*     */   public void setName(String paramString) {
/* 257 */     this.name = paramString;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.SoftTuning
 * JD-Core Version:    0.6.2
 */