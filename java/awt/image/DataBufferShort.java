/*     */ package java.awt.image;
/*     */ 
/*     */ import sun.java2d.StateTrackable.State;
/*     */ import sun.java2d.StateTrackableDelegate;
/*     */ 
/*     */ public final class DataBufferShort extends DataBuffer
/*     */ {
/*     */   short[] data;
/*     */   short[][] bankdata;
/*     */ 
/*     */   public DataBufferShort(int paramInt)
/*     */   {
/*  73 */     super(StateTrackable.State.STABLE, 2, paramInt);
/*  74 */     this.data = new short[paramInt];
/*  75 */     this.bankdata = new short[1][];
/*  76 */     this.bankdata[0] = this.data;
/*     */   }
/*     */ 
/*     */   public DataBufferShort(int paramInt1, int paramInt2)
/*     */   {
/*  87 */     super(StateTrackable.State.STABLE, 2, paramInt1, paramInt2);
/*  88 */     this.bankdata = new short[paramInt2][];
/*  89 */     for (int i = 0; i < paramInt2; i++) {
/*  90 */       this.bankdata[i] = new short[paramInt1];
/*     */     }
/*  92 */     this.data = this.bankdata[0];
/*     */   }
/*     */ 
/*     */   public DataBufferShort(short[] paramArrayOfShort, int paramInt)
/*     */   {
/* 111 */     super(StateTrackable.State.UNTRACKABLE, 2, paramInt);
/* 112 */     this.data = paramArrayOfShort;
/* 113 */     this.bankdata = new short[1][];
/* 114 */     this.bankdata[0] = this.data;
/*     */   }
/*     */ 
/*     */   public DataBufferShort(short[] paramArrayOfShort, int paramInt1, int paramInt2)
/*     */   {
/* 134 */     super(StateTrackable.State.UNTRACKABLE, 2, paramInt1, 1, paramInt2);
/* 135 */     this.data = paramArrayOfShort;
/* 136 */     this.bankdata = new short[1][];
/* 137 */     this.bankdata[0] = this.data;
/*     */   }
/*     */ 
/*     */   public DataBufferShort(short[][] paramArrayOfShort, int paramInt)
/*     */   {
/* 155 */     super(StateTrackable.State.UNTRACKABLE, 2, paramInt, paramArrayOfShort.length);
/* 156 */     this.bankdata = ((short[][])paramArrayOfShort.clone());
/* 157 */     this.data = this.bankdata[0];
/*     */   }
/*     */ 
/*     */   public DataBufferShort(short[][] paramArrayOfShort, int paramInt, int[] paramArrayOfInt)
/*     */   {
/* 180 */     super(StateTrackable.State.UNTRACKABLE, 2, paramInt, paramArrayOfShort.length, paramArrayOfInt);
/* 181 */     this.bankdata = ((short[][])paramArrayOfShort.clone());
/* 182 */     this.data = this.bankdata[0];
/*     */   }
/*     */ 
/*     */   public short[] getData()
/*     */   {
/* 196 */     this.theTrackable.setUntrackable();
/* 197 */     return this.data;
/*     */   }
/*     */ 
/*     */   public short[] getData(int paramInt)
/*     */   {
/* 212 */     this.theTrackable.setUntrackable();
/* 213 */     return this.bankdata[paramInt];
/*     */   }
/*     */ 
/*     */   public short[][] getBankData()
/*     */   {
/* 227 */     this.theTrackable.setUntrackable();
/* 228 */     return (short[][])this.bankdata.clone();
/*     */   }
/*     */ 
/*     */   public int getElem(int paramInt)
/*     */   {
/* 240 */     return this.data[(paramInt + this.offset)];
/*     */   }
/*     */ 
/*     */   public int getElem(int paramInt1, int paramInt2)
/*     */   {
/* 253 */     return this.bankdata[paramInt1][(paramInt2 + this.offsets[paramInt1])];
/*     */   }
/*     */ 
/*     */   public void setElem(int paramInt1, int paramInt2)
/*     */   {
/* 266 */     this.data[(paramInt1 + this.offset)] = ((short)paramInt2);
/* 267 */     this.theTrackable.markDirty();
/*     */   }
/*     */ 
/*     */   public void setElem(int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 280 */     this.bankdata[paramInt1][(paramInt2 + this.offsets[paramInt1])] = ((short)paramInt3);
/* 281 */     this.theTrackable.markDirty();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.image.DataBufferShort
 * JD-Core Version:    0.6.2
 */