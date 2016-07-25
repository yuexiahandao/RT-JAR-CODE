/*    */ package sun.print;
/*    */ 
/*    */ import java.awt.Window;
/*    */ import java.awt.print.PrinterJob;
/*    */ import javax.print.PrintService;
/*    */ import javax.print.attribute.PrintRequestAttributeSet;
/*    */ 
/*    */ public abstract class DocumentPropertiesUI
/*    */ {
/*    */   public static final int DOCUMENTPROPERTIES_ROLE = 199;
/* 46 */   public static final String DOCPROPERTIESCLASSNAME = DocumentPropertiesUI.class.getName();
/*    */ 
/*    */   public abstract PrintRequestAttributeSet showDocumentProperties(PrinterJob paramPrinterJob, Window paramWindow, PrintService paramPrintService, PrintRequestAttributeSet paramPrintRequestAttributeSet);
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.print.DocumentPropertiesUI
 * JD-Core Version:    0.6.2
 */