/*    */ package javax.print.event;
/*    */ 
/*    */ import javax.print.DocPrintJob;
/*    */ import javax.print.attribute.AttributeSetUtilities;
/*    */ import javax.print.attribute.PrintJobAttributeSet;
/*    */ 
/*    */ public class PrintJobAttributeEvent extends PrintEvent
/*    */ {
/*    */   private static final long serialVersionUID = -6534469883874742101L;
/*    */   private PrintJobAttributeSet attributes;
/*    */ 
/*    */   public PrintJobAttributeEvent(DocPrintJob paramDocPrintJob, PrintJobAttributeSet paramPrintJobAttributeSet)
/*    */   {
/* 53 */     super(paramDocPrintJob);
/*    */ 
/* 55 */     this.attributes = AttributeSetUtilities.unmodifiableView(paramPrintJobAttributeSet);
/*    */   }
/*    */ 
/*    */   public DocPrintJob getPrintJob()
/*    */   {
/* 66 */     return (DocPrintJob)getSource();
/*    */   }
/*    */ 
/*    */   public PrintJobAttributeSet getAttributes()
/*    */   {
/* 78 */     return this.attributes;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.event.PrintJobAttributeEvent
 * JD-Core Version:    0.6.2
 */