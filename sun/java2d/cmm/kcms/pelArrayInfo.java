/*     */ package sun.java2d.cmm.kcms;
/*     */ 
/*     */ class pelArrayInfo
/*     */ {
/*     */   int nPels;
/*     */   int nSrc;
/*     */   int srcSize;
/*     */   int nDest;
/*     */   int destSize;
/*     */ 
/*     */   pelArrayInfo(ICC_Transform paramICC_Transform, int paramInt, float[] paramArrayOfFloat1, float[] paramArrayOfFloat2)
/*     */   {
/* 926 */     this.nSrc = paramICC_Transform.getNumInComponents();
/* 927 */     this.nDest = paramICC_Transform.getNumOutComponents();
/*     */ 
/* 929 */     this.nPels = paramInt;
/* 930 */     this.srcSize = (this.nPels * this.nSrc);
/* 931 */     this.destSize = (this.nPels * this.nDest);
/*     */ 
/* 933 */     if (this.srcSize > paramArrayOfFloat1.length) {
/* 934 */       throw new IllegalArgumentException("Inconsistent pel structure");
/*     */     }
/*     */ 
/* 937 */     if (paramArrayOfFloat2 != null)
/* 938 */       checkDest(paramArrayOfFloat2.length);
/*     */   }
/*     */ 
/*     */   pelArrayInfo(ICC_Transform paramICC_Transform, short[] paramArrayOfShort1, short[] paramArrayOfShort2)
/*     */   {
/* 947 */     this.srcSize = paramArrayOfShort1.length;
/*     */ 
/* 949 */     initInfo(paramICC_Transform);
/* 950 */     this.destSize = (this.nPels * this.nDest);
/*     */ 
/* 952 */     if (paramArrayOfShort2 != null)
/* 953 */       checkDest(paramArrayOfShort2.length);
/*     */   }
/*     */ 
/*     */   pelArrayInfo(ICC_Transform paramICC_Transform, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
/*     */   {
/* 962 */     this.srcSize = paramArrayOfByte1.length;
/*     */ 
/* 964 */     initInfo(paramICC_Transform);
/* 965 */     this.destSize = (this.nPels * this.nDest);
/*     */ 
/* 967 */     if (paramArrayOfByte2 != null)
/* 968 */       checkDest(paramArrayOfByte2.length);
/*     */   }
/*     */ 
/*     */   void initInfo(ICC_Transform paramICC_Transform)
/*     */   {
/* 975 */     this.nSrc = paramICC_Transform.getNumInComponents();
/* 976 */     this.nDest = paramICC_Transform.getNumOutComponents();
/*     */ 
/* 978 */     this.nPels = (this.srcSize / this.nSrc);
/*     */ 
/* 980 */     if (this.nPels * this.nSrc != this.srcSize)
/* 981 */       throw new IllegalArgumentException("Inconsistent pel structure");
/*     */   }
/*     */ 
/*     */   void checkDest(int paramInt)
/*     */   {
/* 988 */     if (this.destSize > paramInt)
/* 989 */       throw new IllegalArgumentException("Inconsistent pel structure");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.cmm.kcms.pelArrayInfo
 * JD-Core Version:    0.6.2
 */