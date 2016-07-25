/*    */ package sun.print;
/*    */ 
/*    */ import java.awt.print.Pageable;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.Reader;
/*    */ import java.io.UnsupportedEncodingException;
/*    */ import javax.print.Doc;
/*    */ import javax.print.DocFlavor;
/*    */ import javax.print.DocFlavor.SERVICE_FORMATTED;
/*    */ import javax.print.attribute.DocAttributeSet;
/*    */ import javax.print.attribute.HashDocAttributeSet;
/*    */ 
/*    */ public class PageableDoc
/*    */   implements Doc
/*    */ {
/*    */   private Pageable pageable;
/*    */ 
/*    */   public PageableDoc(Pageable paramPageable)
/*    */   {
/* 45 */     this.pageable = paramPageable;
/*    */   }
/*    */ 
/*    */   public DocFlavor getDocFlavor() {
/* 49 */     return DocFlavor.SERVICE_FORMATTED.PAGEABLE;
/*    */   }
/*    */ 
/*    */   public DocAttributeSet getAttributes() {
/* 53 */     return new HashDocAttributeSet();
/*    */   }
/*    */ 
/*    */   public Object getPrintData() throws IOException {
/* 57 */     return this.pageable;
/*    */   }
/*    */ 
/*    */   public Reader getReaderForText()
/*    */     throws UnsupportedEncodingException, IOException
/*    */   {
/* 63 */     return null;
/*    */   }
/*    */ 
/*    */   public InputStream getStreamForBytes() throws IOException {
/* 67 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.print.PageableDoc
 * JD-Core Version:    0.6.2
 */