/*     */ package java.awt.image;
/*     */ 
/*     */ import sun.awt.image.SunWritableRaster;
/*     */ import sun.awt.image.SunWritableRaster.DataStealer;
/*     */ import sun.java2d.StateTrackable.State;
/*     */ import sun.java2d.StateTrackableDelegate;
/*     */ 
/*     */ public abstract class DataBuffer
/*     */ {
/*     */   public static final int TYPE_BYTE = 0;
/*     */   public static final int TYPE_USHORT = 1;
/*     */   public static final int TYPE_SHORT = 2;
/*     */   public static final int TYPE_INT = 3;
/*     */   public static final int TYPE_FLOAT = 4;
/*     */   public static final int TYPE_DOUBLE = 5;
/*     */   public static final int TYPE_UNDEFINED = 32;
/*     */   protected int dataType;
/*     */   protected int banks;
/*     */   protected int offset;
/*     */   protected int size;
/*     */   protected int[] offsets;
/*     */   StateTrackableDelegate theTrackable;
/* 110 */   private static final int[] dataTypeSize = { 8, 16, 16, 32, 32, 64 };
/*     */ 
/*     */   public static int getDataTypeSize(int paramInt)
/*     */   {
/* 119 */     if ((paramInt < 0) || (paramInt > 5)) {
/* 120 */       throw new IllegalArgumentException("Unknown data type " + paramInt);
/*     */     }
/* 122 */     return dataTypeSize[paramInt];
/*     */   }
/*     */ 
/*     */   protected DataBuffer(int paramInt1, int paramInt2)
/*     */   {
/* 133 */     this(StateTrackable.State.UNTRACKABLE, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   DataBuffer(StateTrackable.State paramState, int paramInt1, int paramInt2)
/*     */   {
/* 148 */     this.theTrackable = StateTrackableDelegate.createInstance(paramState);
/* 149 */     this.dataType = paramInt1;
/* 150 */     this.banks = 1;
/* 151 */     this.size = paramInt2;
/* 152 */     this.offset = 0;
/* 153 */     this.offsets = new int[1];
/*     */   }
/*     */ 
/*     */   protected DataBuffer(int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 166 */     this(StateTrackable.State.UNTRACKABLE, paramInt1, paramInt2, paramInt3);
/*     */   }
/*     */ 
/*     */   DataBuffer(StateTrackable.State paramState, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 184 */     this.theTrackable = StateTrackableDelegate.createInstance(paramState);
/* 185 */     this.dataType = paramInt1;
/* 186 */     this.banks = paramInt3;
/* 187 */     this.size = paramInt2;
/* 188 */     this.offset = 0;
/* 189 */     this.offsets = new int[this.banks];
/*     */   }
/*     */ 
/*     */   protected DataBuffer(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 203 */     this(StateTrackable.State.UNTRACKABLE, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */   }
/*     */ 
/*     */   DataBuffer(StateTrackable.State paramState, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 222 */     this.theTrackable = StateTrackableDelegate.createInstance(paramState);
/* 223 */     this.dataType = paramInt1;
/* 224 */     this.banks = paramInt3;
/* 225 */     this.size = paramInt2;
/* 226 */     this.offset = paramInt4;
/* 227 */     this.offsets = new int[paramInt3];
/* 228 */     for (int i = 0; i < paramInt3; i++)
/* 229 */       this.offsets[i] = paramInt4;
/*     */   }
/*     */ 
/*     */   protected DataBuffer(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt)
/*     */   {
/* 248 */     this(StateTrackable.State.UNTRACKABLE, paramInt1, paramInt2, paramInt3, paramArrayOfInt);
/*     */   }
/*     */ 
/*     */   DataBuffer(StateTrackable.State paramState, int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt)
/*     */   {
/* 271 */     if (paramInt3 != paramArrayOfInt.length) {
/* 272 */       throw new ArrayIndexOutOfBoundsException("Number of banks does not match number of bank offsets");
/*     */     }
/*     */ 
/* 275 */     this.theTrackable = StateTrackableDelegate.createInstance(paramState);
/* 276 */     this.dataType = paramInt1;
/* 277 */     this.banks = paramInt3;
/* 278 */     this.size = paramInt2;
/* 279 */     this.offset = paramArrayOfInt[0];
/* 280 */     this.offsets = ((int[])paramArrayOfInt.clone());
/*     */   }
/*     */ 
/*     */   public int getDataType()
/*     */   {
/* 287 */     return this.dataType;
/*     */   }
/*     */ 
/*     */   public int getSize()
/*     */   {
/* 294 */     return this.size;
/*     */   }
/*     */ 
/*     */   public int getOffset()
/*     */   {
/* 301 */     return this.offset;
/*     */   }
/*     */ 
/*     */   public int[] getOffsets()
/*     */   {
/* 308 */     return (int[])this.offsets.clone();
/*     */   }
/*     */ 
/*     */   public int getNumBanks()
/*     */   {
/* 315 */     return this.banks;
/*     */   }
/*     */ 
/*     */   public int getElem(int paramInt)
/*     */   {
/* 327 */     return getElem(0, paramInt);
/*     */   }
/*     */ 
/*     */   public abstract int getElem(int paramInt1, int paramInt2);
/*     */ 
/*     */   public void setElem(int paramInt1, int paramInt2)
/*     */   {
/* 352 */     setElem(0, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public abstract void setElem(int paramInt1, int paramInt2, int paramInt3);
/*     */ 
/*     */   public float getElemFloat(int paramInt)
/*     */   {
/* 379 */     return getElem(paramInt);
/*     */   }
/*     */ 
/*     */   public float getElemFloat(int paramInt1, int paramInt2)
/*     */   {
/* 396 */     return getElem(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public void setElemFloat(int paramInt, float paramFloat)
/*     */   {
/* 411 */     setElem(paramInt, (int)paramFloat);
/*     */   }
/*     */ 
/*     */   public void setElemFloat(int paramInt1, int paramInt2, float paramFloat)
/*     */   {
/* 427 */     setElem(paramInt1, paramInt2, (int)paramFloat);
/*     */   }
/*     */ 
/*     */   public double getElemDouble(int paramInt)
/*     */   {
/* 443 */     return getElem(paramInt);
/*     */   }
/*     */ 
/*     */   public double getElemDouble(int paramInt1, int paramInt2)
/*     */   {
/* 459 */     return getElem(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public void setElemDouble(int paramInt, double paramDouble)
/*     */   {
/* 474 */     setElem(paramInt, (int)paramDouble);
/*     */   }
/*     */ 
/*     */   public void setElemDouble(int paramInt1, int paramInt2, double paramDouble)
/*     */   {
/* 490 */     setElem(paramInt1, paramInt2, (int)paramDouble);
/*     */   }
/*     */ 
/*     */   static int[] toIntArray(Object paramObject) {
/* 494 */     if ((paramObject instanceof int[]))
/* 495 */       return (int[])paramObject;
/* 496 */     if (paramObject == null)
/* 497 */       return null;
/*     */     Object localObject;
/*     */     int[] arrayOfInt;
/*     */     int i;
/* 498 */     if ((paramObject instanceof short[])) {
/* 499 */       localObject = (short[])paramObject;
/* 500 */       arrayOfInt = new int[localObject.length];
/* 501 */       for (i = 0; i < localObject.length; i++) {
/* 502 */         localObject[i] &= 65535;
/*     */       }
/* 504 */       return arrayOfInt;
/* 505 */     }if ((paramObject instanceof byte[])) {
/* 506 */       localObject = (byte[])paramObject;
/* 507 */       arrayOfInt = new int[localObject.length];
/* 508 */       for (i = 0; i < localObject.length; i++) {
/* 509 */         arrayOfInt[i] = (0xFF & localObject[i]);
/*     */       }
/* 511 */       return arrayOfInt;
/*     */     }
/* 513 */     return null;
/*     */   }
/*     */ 
/*     */   static {
/* 517 */     SunWritableRaster.setDataStealer(new SunWritableRaster.DataStealer() {
/*     */       public byte[] getData(DataBufferByte paramAnonymousDataBufferByte, int paramAnonymousInt) {
/* 519 */         return paramAnonymousDataBufferByte.bankdata[paramAnonymousInt];
/*     */       }
/*     */ 
/*     */       public short[] getData(DataBufferUShort paramAnonymousDataBufferUShort, int paramAnonymousInt) {
/* 523 */         return paramAnonymousDataBufferUShort.bankdata[paramAnonymousInt];
/*     */       }
/*     */ 
/*     */       public int[] getData(DataBufferInt paramAnonymousDataBufferInt, int paramAnonymousInt) {
/* 527 */         return paramAnonymousDataBufferInt.bankdata[paramAnonymousInt];
/*     */       }
/*     */ 
/*     */       public StateTrackableDelegate getTrackable(DataBuffer paramAnonymousDataBuffer) {
/* 531 */         return paramAnonymousDataBuffer.theTrackable;
/*     */       }
/*     */ 
/*     */       public void setTrackable(DataBuffer paramAnonymousDataBuffer, StateTrackableDelegate paramAnonymousStateTrackableDelegate)
/*     */       {
/* 537 */         paramAnonymousDataBuffer.theTrackable = paramAnonymousStateTrackableDelegate;
/*     */       }
/*     */     });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.image.DataBuffer
 * JD-Core Version:    0.6.2
 */