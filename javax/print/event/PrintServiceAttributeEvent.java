/*    */ package javax.print.event;
/*    */ 
/*    */ import javax.print.PrintService;
/*    */ import javax.print.attribute.AttributeSetUtilities;
/*    */ import javax.print.attribute.PrintServiceAttributeSet;
/*    */ 
/*    */ public class PrintServiceAttributeEvent extends PrintEvent
/*    */ {
/*    */   private static final long serialVersionUID = -7565987018140326600L;
/*    */   private PrintServiceAttributeSet attributes;
/*    */ 
/*    */   public PrintServiceAttributeEvent(PrintService paramPrintService, PrintServiceAttributeSet paramPrintServiceAttributeSet)
/*    */   {
/* 56 */     super(paramPrintService);
/* 57 */     this.attributes = AttributeSetUtilities.unmodifiableView(paramPrintServiceAttributeSet);
/*    */   }
/*    */ 
/*    */   public PrintService getPrintService()
/*    */   {
/* 68 */     return (PrintService)getSource();
/*    */   }
/*    */ 
/*    */   public PrintServiceAttributeSet getAttributes()
/*    */   {
/* 81 */     return this.attributes;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.event.PrintServiceAttributeEvent
 * JD-Core Version:    0.6.2
 */