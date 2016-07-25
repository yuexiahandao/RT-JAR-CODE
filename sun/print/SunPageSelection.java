/*    */ package sun.print;
/*    */ 
/*    */ import javax.print.attribute.PrintRequestAttribute;
/*    */ 
/*    */ public final class SunPageSelection
/*    */   implements PrintRequestAttribute
/*    */ {
/* 35 */   public static final SunPageSelection ALL = new SunPageSelection(0);
/* 36 */   public static final SunPageSelection RANGE = new SunPageSelection(1);
/* 37 */   public static final SunPageSelection SELECTION = new SunPageSelection(2);
/*    */   private int pages;
/*    */ 
/*    */   public SunPageSelection(int paramInt)
/*    */   {
/* 42 */     this.pages = paramInt;
/*    */   }
/*    */ 
/*    */   public final Class getCategory() {
/* 46 */     return SunPageSelection.class;
/*    */   }
/*    */ 
/*    */   public final String getName() {
/* 50 */     return "sun-page-selection";
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 54 */     return "page-selection: " + this.pages;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.print.SunPageSelection
 * JD-Core Version:    0.6.2
 */