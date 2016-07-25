/*    */ package sun.print;
/*    */ 
/*    */ import javax.print.attribute.PrintRequestAttribute;
/*    */ 
/*    */ public final class SunMinMaxPage
/*    */   implements PrintRequestAttribute
/*    */ {
/*    */   private int page_max;
/*    */   private int page_min;
/*    */ 
/*    */   public SunMinMaxPage(int paramInt1, int paramInt2)
/*    */   {
/* 37 */     this.page_min = paramInt1;
/* 38 */     this.page_max = paramInt2;
/*    */   }
/*    */ 
/*    */   public final Class getCategory()
/*    */   {
/* 43 */     return SunMinMaxPage.class;
/*    */   }
/*    */ 
/*    */   public final int getMin()
/*    */   {
/* 48 */     return this.page_min;
/*    */   }
/*    */ 
/*    */   public final int getMax() {
/* 52 */     return this.page_max;
/*    */   }
/*    */ 
/*    */   public final String getName()
/*    */   {
/* 57 */     return "sun-page-minmax";
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.print.SunMinMaxPage
 * JD-Core Version:    0.6.2
 */