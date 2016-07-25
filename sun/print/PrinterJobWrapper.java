/*    */ package sun.print;
/*    */ 
/*    */ import java.awt.print.PrinterJob;
/*    */ import javax.print.attribute.PrintRequestAttribute;
/*    */ 
/*    */ public class PrinterJobWrapper
/*    */   implements PrintRequestAttribute
/*    */ {
/*    */   private static final long serialVersionUID = -8792124426995707237L;
/*    */   private PrinterJob job;
/*    */ 
/*    */   public PrinterJobWrapper(PrinterJob paramPrinterJob)
/*    */   {
/* 38 */     this.job = paramPrinterJob;
/*    */   }
/*    */ 
/*    */   public PrinterJob getPrinterJob() {
/* 42 */     return this.job;
/*    */   }
/*    */ 
/*    */   public final Class getCategory() {
/* 46 */     return PrinterJobWrapper.class;
/*    */   }
/*    */ 
/*    */   public final String getName() {
/* 50 */     return "printerjob-wrapper";
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 54 */     return "printerjob-wrapper: " + this.job.toString();
/*    */   }
/*    */ 
/*    */   public int hashCode() {
/* 58 */     return this.job.hashCode();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.print.PrinterJobWrapper
 * JD-Core Version:    0.6.2
 */