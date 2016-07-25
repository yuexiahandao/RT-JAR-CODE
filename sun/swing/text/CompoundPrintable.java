/*    */ package sun.swing.text;
/*    */ 
/*    */ import java.awt.Graphics;
/*    */ import java.awt.print.PageFormat;
/*    */ import java.awt.print.PrinterException;
/*    */ import java.util.LinkedList;
/*    */ import java.util.List;
/*    */ import java.util.Queue;
/*    */ 
/*    */ class CompoundPrintable
/*    */   implements CountingPrintable
/*    */ {
/*    */   private final Queue<CountingPrintable> printables;
/* 41 */   private int offset = 0;
/*    */ 
/*    */   public CompoundPrintable(List<CountingPrintable> paramList) {
/* 44 */     this.printables = new LinkedList(paramList);
/*    */   }
/*    */ 
/*    */   public int print(Graphics paramGraphics, PageFormat paramPageFormat, int paramInt)
/*    */     throws PrinterException
/*    */   {
/* 50 */     int i = 1;
/* 51 */     while (this.printables.peek() != null) {
/* 52 */       i = ((CountingPrintable)this.printables.peek()).print(paramGraphics, paramPageFormat, paramInt - this.offset);
/* 53 */       if (i == 0) {
/*    */         break;
/*    */       }
/* 56 */       this.offset += ((CountingPrintable)this.printables.poll()).getNumberOfPages();
/*    */     }
/*    */ 
/* 59 */     return i;
/*    */   }
/*    */ 
/*    */   public int getNumberOfPages()
/*    */   {
/* 70 */     return this.offset;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.swing.text.CompoundPrintable
 * JD-Core Version:    0.6.2
 */