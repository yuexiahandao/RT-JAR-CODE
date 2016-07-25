/*    */ package sun.print;
/*    */ 
/*    */ import java.io.OutputStream;
/*    */ import javax.print.DocFlavor;
/*    */ import javax.print.DocFlavor.BYTE_ARRAY;
/*    */ import javax.print.DocFlavor.INPUT_STREAM;
/*    */ import javax.print.DocFlavor.SERVICE_FORMATTED;
/*    */ import javax.print.DocFlavor.URL;
/*    */ import javax.print.StreamPrintService;
/*    */ import javax.print.StreamPrintServiceFactory;
/*    */ 
/*    */ public class PSStreamPrinterFactory extends StreamPrintServiceFactory
/*    */ {
/*    */   static final String psMimeType = "application/postscript";
/* 38 */   static final DocFlavor[] supportedDocFlavors = { DocFlavor.SERVICE_FORMATTED.PAGEABLE, DocFlavor.SERVICE_FORMATTED.PRINTABLE, DocFlavor.BYTE_ARRAY.GIF, DocFlavor.INPUT_STREAM.GIF, DocFlavor.URL.GIF, DocFlavor.BYTE_ARRAY.JPEG, DocFlavor.INPUT_STREAM.JPEG, DocFlavor.URL.JPEG, DocFlavor.BYTE_ARRAY.PNG, DocFlavor.INPUT_STREAM.PNG, DocFlavor.URL.PNG };
/*    */ 
/*    */   public String getOutputFormat()
/*    */   {
/* 53 */     return "application/postscript";
/*    */   }
/*    */ 
/*    */   public DocFlavor[] getSupportedDocFlavors() {
/* 57 */     return getFlavors();
/*    */   }
/*    */ 
/*    */   static DocFlavor[] getFlavors() {
/* 61 */     DocFlavor[] arrayOfDocFlavor = new DocFlavor[supportedDocFlavors.length];
/* 62 */     System.arraycopy(supportedDocFlavors, 0, arrayOfDocFlavor, 0, arrayOfDocFlavor.length);
/* 63 */     return arrayOfDocFlavor;
/*    */   }
/*    */ 
/*    */   public StreamPrintService getPrintService(OutputStream paramOutputStream) {
/* 67 */     return new PSStreamPrintService(paramOutputStream);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.print.PSStreamPrinterFactory
 * JD-Core Version:    0.6.2
 */