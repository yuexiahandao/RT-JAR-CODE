/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import java.net.URI;
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.PrintServiceAttribute;
/*     */ import javax.print.attribute.URISyntax;
/*     */ 
/*     */ public final class PrinterURI extends URISyntax
/*     */   implements PrintServiceAttribute
/*     */ {
/*     */   private static final long serialVersionUID = 7923912792485606497L;
/*     */ 
/*     */   public PrinterURI(URI paramURI)
/*     */   {
/*  63 */     super(paramURI);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  86 */     return (super.equals(paramObject)) && ((paramObject instanceof PrinterURI));
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/* 100 */     return PrinterURI.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 113 */     return "printer-uri";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.PrinterURI
 * JD-Core Version:    0.6.2
 */