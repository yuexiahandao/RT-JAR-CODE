/*    */ package java.awt.image;
/*    */ 
/*    */ public abstract class LookupTable
/*    */ {
/*    */   int numComponents;
/*    */   int offset;
/*    */   int numEntries;
/*    */ 
/*    */   protected LookupTable(int paramInt1, int paramInt2)
/*    */   {
/* 67 */     if (paramInt1 < 0) {
/* 68 */       throw new IllegalArgumentException("Offset must be greater than 0");
/*    */     }
/*    */ 
/* 71 */     if (paramInt2 < 1) {
/* 72 */       throw new IllegalArgumentException("Number of components must  be at least 1");
/*    */     }
/*    */ 
/* 75 */     this.numComponents = paramInt2;
/* 76 */     this.offset = paramInt1;
/*    */   }
/*    */ 
/*    */   public int getNumComponents()
/*    */   {
/* 84 */     return this.numComponents;
/*    */   }
/*    */ 
/*    */   public int getOffset()
/*    */   {
/* 92 */     return this.offset;
/*    */   }
/*    */ 
/*    */   public abstract int[] lookupPixel(int[] paramArrayOfInt1, int[] paramArrayOfInt2);
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.image.LookupTable
 * JD-Core Version:    0.6.2
 */