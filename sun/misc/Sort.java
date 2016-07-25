/*    */ package sun.misc;
/*    */ 
/*    */ public class Sort
/*    */ {
/*    */   private static void swap(Object[] paramArrayOfObject, int paramInt1, int paramInt2)
/*    */   {
/* 40 */     Object localObject = paramArrayOfObject[paramInt1];
/* 41 */     paramArrayOfObject[paramInt1] = paramArrayOfObject[paramInt2];
/* 42 */     paramArrayOfObject[paramInt2] = localObject;
/*    */   }
/*    */ 
/*    */   public static void quicksort(Object[] paramArrayOfObject, int paramInt1, int paramInt2, Compare paramCompare)
/*    */   {
/* 56 */     if (paramInt1 >= paramInt2) {
/* 57 */       return;
/*    */     }
/* 59 */     swap(paramArrayOfObject, paramInt1, (paramInt1 + paramInt2) / 2);
/* 60 */     int j = paramInt1;
/* 61 */     for (int i = paramInt1 + 1; i <= paramInt2; i++) {
/* 62 */       if (paramCompare.doCompare(paramArrayOfObject[i], paramArrayOfObject[paramInt1]) < 0) {
/* 63 */         swap(paramArrayOfObject, ++j, i);
/*    */       }
/*    */     }
/* 66 */     swap(paramArrayOfObject, paramInt1, j);
/* 67 */     quicksort(paramArrayOfObject, paramInt1, j - 1, paramCompare);
/* 68 */     quicksort(paramArrayOfObject, j + 1, paramInt2, paramCompare);
/*    */   }
/*    */ 
/*    */   public static void quicksort(Object[] paramArrayOfObject, Compare paramCompare) {
/* 72 */     quicksort(paramArrayOfObject, 0, paramArrayOfObject.length - 1, paramCompare);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.Sort
 * JD-Core Version:    0.6.2
 */