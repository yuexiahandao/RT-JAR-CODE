/*    */ package sun.java2d.pipe;
/*    */ 
/*    */ public class RegionIterator
/*    */ {
/*    */   Region region;
/*    */   int curIndex;
/*    */   int numXbands;
/*    */ 
/*    */   RegionIterator(Region paramRegion)
/*    */   {
/* 40 */     this.region = paramRegion;
/*    */   }
/*    */ 
/*    */   public RegionIterator createCopy()
/*    */   {
/* 49 */     RegionIterator localRegionIterator = new RegionIterator(this.region);
/* 50 */     localRegionIterator.curIndex = this.curIndex;
/* 51 */     localRegionIterator.numXbands = this.numXbands;
/* 52 */     return localRegionIterator;
/*    */   }
/*    */ 
/*    */   public void copyStateFrom(RegionIterator paramRegionIterator)
/*    */   {
/* 61 */     if (this.region != paramRegionIterator.region) {
/* 62 */       throw new InternalError("region mismatch");
/*    */     }
/* 64 */     this.curIndex = paramRegionIterator.curIndex;
/* 65 */     this.numXbands = paramRegionIterator.numXbands;
/*    */   }
/*    */ 
/*    */   public boolean nextYRange(int[] paramArrayOfInt)
/*    */   {
/* 75 */     this.curIndex += this.numXbands * 2;
/* 76 */     this.numXbands = 0;
/* 77 */     if (this.curIndex >= this.region.endIndex) {
/* 78 */       return false;
/*    */     }
/* 80 */     paramArrayOfInt[1] = this.region.bands[(this.curIndex++)];
/* 81 */     paramArrayOfInt[3] = this.region.bands[(this.curIndex++)];
/* 82 */     this.numXbands = this.region.bands[(this.curIndex++)];
/* 83 */     return true;
/*    */   }
/*    */ 
/*    */   public boolean nextXBand(int[] paramArrayOfInt)
/*    */   {
/* 93 */     if (this.numXbands <= 0) {
/* 94 */       return false;
/*    */     }
/* 96 */     this.numXbands -= 1;
/* 97 */     paramArrayOfInt[0] = this.region.bands[(this.curIndex++)];
/* 98 */     paramArrayOfInt[2] = this.region.bands[(this.curIndex++)];
/* 99 */     return true;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.pipe.RegionIterator
 * JD-Core Version:    0.6.2
 */