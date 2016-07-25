/*     */ package java.awt.image;
/*     */ 
/*     */ import sun.java2d.StateTrackable.State;
/*     */ import sun.java2d.StateTrackableDelegate;
/*     */ 
/*     */ public final class DataBufferUShort extends DataBuffer
/*     */ {
/*     */   short[] data;
/*     */   short[][] bankdata;
/*     */ 
/*     */   public DataBufferUShort(int paramInt)
/*     */   {
/*  75 */     super(StateTrackable.State.STABLE, 1, paramInt);
/*  76 */     this.data = new short[paramInt];
/*  77 */     this.bankdata = new short[1][];
/*  78 */     this.bankdata[0] = this.data;
/*     */   }
/*     */ 
/*     */   public DataBufferUShort(int paramInt1, int paramInt2)
/*     */   {
/*  89 */     super(StateTrackable.State.STABLE, 1, paramInt1, paramInt2);
/*  90 */     this.bankdata = new short[paramInt2][];
/*  91 */     for (int i = 0; i < paramInt2; i++) {
/*  92 */       this.bankdata[i] = new short[paramInt1];
/*     */     }
/*  94 */     this.data = this.bankdata[0];
/*     */   }
/*     */ 
/*     */   public DataBufferUShort(short[] paramArrayOfShort, int paramInt)
/*     */   {
/* 113 */     super(StateTrackable.State.UNTRACKABLE, 1, paramInt);
/* 114 */     if (paramArrayOfShort == null) {
/* 115 */       throw new NullPointerException("dataArray is null");
/*     */     }
/* 117 */     this.data = paramArrayOfShort;
/* 118 */     this.bankdata = new short[1][];
/* 119 */     this.bankdata[0] = this.data;
/*     */   }
/*     */ 
/*     */   public DataBufferUShort(short[] paramArrayOfShort, int paramInt1, int paramInt2)
/*     */   {
/* 139 */     super(StateTrackable.State.UNTRACKABLE, 1, paramInt1, 1, paramInt2);
/* 140 */     if (paramArrayOfShort == null) {
/* 141 */       throw new NullPointerException("dataArray is null");
/*     */     }
/* 143 */     if (paramInt1 + paramInt2 > paramArrayOfShort.length) {
/* 144 */       throw new IllegalArgumentException("Length of dataArray is less  than size+offset.");
/*     */     }
/*     */ 
/* 147 */     this.data = paramArrayOfShort;
/* 148 */     this.bankdata = new short[1][];
/* 149 */     this.bankdata[0] = this.data;
/*     */   }
/*     */ 
/*     */   public DataBufferUShort(short[][] paramArrayOfShort, int paramInt)
/*     */   {
/* 167 */     super(StateTrackable.State.UNTRACKABLE, 1, paramInt, paramArrayOfShort.length);
/* 168 */     if (paramArrayOfShort == null) {
/* 169 */       throw new NullPointerException("dataArray is null");
/*     */     }
/* 171 */     for (int i = 0; i < paramArrayOfShort.length; i++) {
/* 172 */       if (paramArrayOfShort[i] == null) {
/* 173 */         throw new NullPointerException("dataArray[" + i + "] is null");
/*     */       }
/*     */     }
/*     */ 
/* 177 */     this.bankdata = ((short[][])paramArrayOfShort.clone());
/* 178 */     this.data = this.bankdata[0];
/*     */   }
/*     */ 
/*     */   public DataBufferUShort(short[][] paramArrayOfShort, int paramInt, int[] paramArrayOfInt)
/*     */   {
/* 201 */     super(StateTrackable.State.UNTRACKABLE, 1, paramInt, paramArrayOfShort.length, paramArrayOfInt);
/* 202 */     if (paramArrayOfShort == null) {
/* 203 */       throw new NullPointerException("dataArray is null");
/*     */     }
/* 205 */     for (int i = 0; i < paramArrayOfShort.length; i++) {
/* 206 */       if (paramArrayOfShort[i] == null) {
/* 207 */         throw new NullPointerException("dataArray[" + i + "] is null");
/*     */       }
/* 209 */       if (paramInt + paramArrayOfInt[i] > paramArrayOfShort[i].length) {
/* 210 */         throw new IllegalArgumentException("Length of dataArray[" + i + "] is less than size+" + "offsets[" + i + "].");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 216 */     this.bankdata = ((short[][])paramArrayOfShort.clone());
/* 217 */     this.data = this.bankdata[0];
/*     */   }
/*     */ 
/*     */   public short[] getData()
/*     */   {
/* 231 */     this.theTrackable.setUntrackable();
/* 232 */     return this.data;
/*     */   }
/*     */ 
/*     */   public short[] getData(int paramInt)
/*     */   {
/* 247 */     this.theTrackable.setUntrackable();
/* 248 */     return this.bankdata[paramInt];
/*     */   }
/*     */ 
/*     */   public short[][] getBankData()
/*     */   {
/* 262 */     this.theTrackable.setUntrackable();
/* 263 */     return (short[][])this.bankdata.clone();
/*     */   }
/*     */ 
/*     */   public int getElem(int paramInt)
/*     */   {
/* 275 */     return this.data[(paramInt + this.offset)] & 0xFFFF;
/*     */   }
/*     */ 
/*     */   public int getElem(int paramInt1, int paramInt2)
/*     */   {
/* 288 */     return this.bankdata[paramInt1][(paramInt2 + this.offsets[paramInt1])] & 0xFFFF;
/*     */   }
/*     */ 
/*     */   public void setElem(int paramInt1, int paramInt2)
/*     */   {
/* 301 */     this.data[(paramInt1 + this.offset)] = ((short)(paramInt2 & 0xFFFF));
/* 302 */     this.theTrackable.markDirty();
/*     */   }
/*     */ 
/*     */   public void setElem(int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 315 */     this.bankdata[paramInt1][(paramInt2 + this.offsets[paramInt1])] = ((short)(paramInt3 & 0xFFFF));
/* 316 */     this.theTrackable.markDirty();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.image.DataBufferUShort
 * JD-Core Version:    0.6.2
 */