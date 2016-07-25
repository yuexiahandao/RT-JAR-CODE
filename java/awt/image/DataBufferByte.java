/*     */ package java.awt.image;
/*     */ 
/*     */ import sun.java2d.StateTrackable.State;
/*     */ import sun.java2d.StateTrackableDelegate;
/*     */ 
/*     */ public final class DataBufferByte extends DataBuffer
/*     */ {
/*     */   byte[] data;
/*     */   byte[][] bankdata;
/*     */ 
/*     */   public DataBufferByte(int paramInt)
/*     */   {
/*  75 */     super(StateTrackable.State.STABLE, 0, paramInt);
/*  76 */     this.data = new byte[paramInt];
/*  77 */     this.bankdata = new byte[1][];
/*  78 */     this.bankdata[0] = this.data;
/*     */   }
/*     */ 
/*     */   public DataBufferByte(int paramInt1, int paramInt2)
/*     */   {
/*  89 */     super(StateTrackable.State.STABLE, 0, paramInt1, paramInt2);
/*  90 */     this.bankdata = new byte[paramInt2][];
/*  91 */     for (int i = 0; i < paramInt2; i++) {
/*  92 */       this.bankdata[i] = new byte[paramInt1];
/*     */     }
/*  94 */     this.data = this.bankdata[0];
/*     */   }
/*     */ 
/*     */   public DataBufferByte(byte[] paramArrayOfByte, int paramInt)
/*     */   {
/* 113 */     super(StateTrackable.State.UNTRACKABLE, 0, paramInt);
/* 114 */     this.data = paramArrayOfByte;
/* 115 */     this.bankdata = new byte[1][];
/* 116 */     this.bankdata[0] = this.data;
/*     */   }
/*     */ 
/*     */   public DataBufferByte(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/* 137 */     super(StateTrackable.State.UNTRACKABLE, 0, paramInt1, 1, paramInt2);
/* 138 */     this.data = paramArrayOfByte;
/* 139 */     this.bankdata = new byte[1][];
/* 140 */     this.bankdata[0] = this.data;
/*     */   }
/*     */ 
/*     */   public DataBufferByte(byte[][] paramArrayOfByte, int paramInt)
/*     */   {
/* 158 */     super(StateTrackable.State.UNTRACKABLE, 0, paramInt, paramArrayOfByte.length);
/* 159 */     this.bankdata = ((byte[][])paramArrayOfByte.clone());
/* 160 */     this.data = this.bankdata[0];
/*     */   }
/*     */ 
/*     */   public DataBufferByte(byte[][] paramArrayOfByte, int paramInt, int[] paramArrayOfInt)
/*     */   {
/* 183 */     super(StateTrackable.State.UNTRACKABLE, 0, paramInt, paramArrayOfByte.length, paramArrayOfInt);
/* 184 */     this.bankdata = ((byte[][])paramArrayOfByte.clone());
/* 185 */     this.data = this.bankdata[0];
/*     */   }
/*     */ 
/*     */   public byte[] getData()
/*     */   {
/* 199 */     this.theTrackable.setUntrackable();
/* 200 */     return this.data;
/*     */   }
/*     */ 
/*     */   public byte[] getData(int paramInt)
/*     */   {
/* 215 */     this.theTrackable.setUntrackable();
/* 216 */     return this.bankdata[paramInt];
/*     */   }
/*     */ 
/*     */   public byte[][] getBankData()
/*     */   {
/* 230 */     this.theTrackable.setUntrackable();
/* 231 */     return (byte[][])this.bankdata.clone();
/*     */   }
/*     */ 
/*     */   public int getElem(int paramInt)
/*     */   {
/* 243 */     return this.data[(paramInt + this.offset)] & 0xFF;
/*     */   }
/*     */ 
/*     */   public int getElem(int paramInt1, int paramInt2)
/*     */   {
/* 256 */     return this.bankdata[paramInt1][(paramInt2 + this.offsets[paramInt1])] & 0xFF;
/*     */   }
/*     */ 
/*     */   public void setElem(int paramInt1, int paramInt2)
/*     */   {
/* 269 */     this.data[(paramInt1 + this.offset)] = ((byte)paramInt2);
/* 270 */     this.theTrackable.markDirty();
/*     */   }
/*     */ 
/*     */   public void setElem(int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 283 */     this.bankdata[paramInt1][(paramInt2 + this.offsets[paramInt1])] = ((byte)paramInt3);
/* 284 */     this.theTrackable.markDirty();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.image.DataBufferByte
 * JD-Core Version:    0.6.2
 */