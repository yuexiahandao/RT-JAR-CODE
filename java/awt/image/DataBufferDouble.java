/*     */ package java.awt.image;
/*     */ 
/*     */ import sun.java2d.StateTrackable.State;
/*     */ import sun.java2d.StateTrackableDelegate;
/*     */ 
/*     */ public final class DataBufferDouble extends DataBuffer
/*     */ {
/*     */   double[][] bankdata;
/*     */   double[] data;
/*     */ 
/*     */   public DataBufferDouble(int paramInt)
/*     */   {
/*  67 */     super(StateTrackable.State.STABLE, 5, paramInt);
/*  68 */     this.data = new double[paramInt];
/*  69 */     this.bankdata = new double[1][];
/*  70 */     this.bankdata[0] = this.data;
/*     */   }
/*     */ 
/*     */   public DataBufferDouble(int paramInt1, int paramInt2)
/*     */   {
/*  83 */     super(StateTrackable.State.STABLE, 5, paramInt1, paramInt2);
/*  84 */     this.bankdata = new double[paramInt2][];
/*  85 */     for (int i = 0; i < paramInt2; i++) {
/*  86 */       this.bankdata[i] = new double[paramInt1];
/*     */     }
/*  88 */     this.data = this.bankdata[0];
/*     */   }
/*     */ 
/*     */   public DataBufferDouble(double[] paramArrayOfDouble, int paramInt)
/*     */   {
/* 108 */     super(StateTrackable.State.UNTRACKABLE, 5, paramInt);
/* 109 */     this.data = paramArrayOfDouble;
/* 110 */     this.bankdata = new double[1][];
/* 111 */     this.bankdata[0] = this.data;
/*     */   }
/*     */ 
/*     */   public DataBufferDouble(double[] paramArrayOfDouble, int paramInt1, int paramInt2)
/*     */   {
/* 133 */     super(StateTrackable.State.UNTRACKABLE, 5, paramInt1, 1, paramInt2);
/* 134 */     this.data = paramArrayOfDouble;
/* 135 */     this.bankdata = new double[1][];
/* 136 */     this.bankdata[0] = this.data;
/*     */   }
/*     */ 
/*     */   public DataBufferDouble(double[][] paramArrayOfDouble, int paramInt)
/*     */   {
/* 156 */     super(StateTrackable.State.UNTRACKABLE, 5, paramInt, paramArrayOfDouble.length);
/* 157 */     this.bankdata = ((double[][])paramArrayOfDouble.clone());
/* 158 */     this.data = this.bankdata[0];
/*     */   }
/*     */ 
/*     */   public DataBufferDouble(double[][] paramArrayOfDouble, int paramInt, int[] paramArrayOfInt)
/*     */   {
/* 180 */     super(StateTrackable.State.UNTRACKABLE, 5, paramInt, paramArrayOfDouble.length, paramArrayOfInt);
/* 181 */     this.bankdata = ((double[][])paramArrayOfDouble.clone());
/* 182 */     this.data = this.bankdata[0];
/*     */   }
/*     */ 
/*     */   public double[] getData()
/*     */   {
/* 196 */     this.theTrackable.setUntrackable();
/* 197 */     return this.data;
/*     */   }
/*     */ 
/*     */   public double[] getData(int paramInt)
/*     */   {
/* 212 */     this.theTrackable.setUntrackable();
/* 213 */     return this.bankdata[paramInt];
/*     */   }
/*     */ 
/*     */   public double[][] getBankData()
/*     */   {
/* 227 */     this.theTrackable.setUntrackable();
/* 228 */     return (double[][])this.bankdata.clone();
/*     */   }
/*     */ 
/*     */   public int getElem(int paramInt)
/*     */   {
/* 241 */     return (int)this.data[(paramInt + this.offset)];
/*     */   }
/*     */ 
/*     */   public int getElem(int paramInt1, int paramInt2)
/*     */   {
/* 256 */     return (int)this.bankdata[paramInt1][(paramInt2 + this.offsets[paramInt1])];
/*     */   }
/*     */ 
/*     */   public void setElem(int paramInt1, int paramInt2)
/*     */   {
/* 269 */     this.data[(paramInt1 + this.offset)] = paramInt2;
/* 270 */     this.theTrackable.markDirty();
/*     */   }
/*     */ 
/*     */   public void setElem(int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 284 */     this.bankdata[paramInt1][(paramInt2 + this.offsets[paramInt1])] = paramInt3;
/* 285 */     this.theTrackable.markDirty();
/*     */   }
/*     */ 
/*     */   public float getElemFloat(int paramInt)
/*     */   {
/* 299 */     return (float)this.data[(paramInt + this.offset)];
/*     */   }
/*     */ 
/*     */   public float getElemFloat(int paramInt1, int paramInt2)
/*     */   {
/* 314 */     return (float)this.bankdata[paramInt1][(paramInt2 + this.offsets[paramInt1])];
/*     */   }
/*     */ 
/*     */   public void setElemFloat(int paramInt, float paramFloat)
/*     */   {
/* 327 */     this.data[(paramInt + this.offset)] = paramFloat;
/* 328 */     this.theTrackable.markDirty();
/*     */   }
/*     */ 
/*     */   public void setElemFloat(int paramInt1, int paramInt2, float paramFloat)
/*     */   {
/* 342 */     this.bankdata[paramInt1][(paramInt2 + this.offsets[paramInt1])] = paramFloat;
/* 343 */     this.theTrackable.markDirty();
/*     */   }
/*     */ 
/*     */   public double getElemDouble(int paramInt)
/*     */   {
/* 357 */     return this.data[(paramInt + this.offset)];
/*     */   }
/*     */ 
/*     */   public double getElemDouble(int paramInt1, int paramInt2)
/*     */   {
/* 372 */     return this.bankdata[paramInt1][(paramInt2 + this.offsets[paramInt1])];
/*     */   }
/*     */ 
/*     */   public void setElemDouble(int paramInt, double paramDouble)
/*     */   {
/* 385 */     this.data[(paramInt + this.offset)] = paramDouble;
/* 386 */     this.theTrackable.markDirty();
/*     */   }
/*     */ 
/*     */   public void setElemDouble(int paramInt1, int paramInt2, double paramDouble)
/*     */   {
/* 400 */     this.bankdata[paramInt1][(paramInt2 + this.offsets[paramInt1])] = paramDouble;
/* 401 */     this.theTrackable.markDirty();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.image.DataBufferDouble
 * JD-Core Version:    0.6.2
 */