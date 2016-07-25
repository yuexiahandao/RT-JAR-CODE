/*    */ package sun.text;
/*    */ 
/*    */ public final class SupplementaryCharacterData
/*    */   implements Cloneable
/*    */ {
/*    */   private int[] dataTable;
/*    */ 
/*    */   public SupplementaryCharacterData(int[] paramArrayOfInt)
/*    */   {
/* 54 */     this.dataTable = paramArrayOfInt;
/*    */   }
/*    */ 
/*    */   public int getValue(int paramInt)
/*    */   {
/* 64 */     assert ((paramInt >= 65536) && (paramInt <= 1114111)) : ("Invalid code point:" + Integer.toHexString(paramInt));
/*    */ 
/* 66 */     int i = 0;
/* 67 */     int j = this.dataTable.length - 1;
/*    */     while (true)
/*    */     {
/* 71 */       int k = (i + j) / 2;
/*    */ 
/* 73 */       int m = this.dataTable[k] >> 8;
/* 74 */       int n = this.dataTable[(k + 1)] >> 8;
/*    */ 
/* 76 */       if (paramInt < m)
/* 77 */         j = k;
/* 78 */       else if (paramInt > n - 1)
/* 79 */         i = k;
/*    */       else
/* 81 */         return this.dataTable[k] & 0xFF;
/*    */     }
/*    */   }
/*    */ 
/*    */   public int[] getArray()
/*    */   {
/* 90 */     return this.dataTable;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.text.SupplementaryCharacterData
 * JD-Core Version:    0.6.2
 */