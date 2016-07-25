/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import java.net.URI;
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.PrintServiceAttribute;
/*     */ import javax.print.attribute.URISyntax;
/*     */ 
/*     */ public final class PrinterMoreInfoManufacturer extends URISyntax
/*     */   implements PrintServiceAttribute
/*     */ {
/*     */   private static final long serialVersionUID = 3323271346485076608L;
/*     */ 
/*     */   public PrinterMoreInfoManufacturer(URI paramURI)
/*     */   {
/*  72 */     super(paramURI);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  96 */     return (super.equals(paramObject)) && ((paramObject instanceof PrinterMoreInfoManufacturer));
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/* 111 */     return PrinterMoreInfoManufacturer.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 124 */     return "printer-more-info-manufacturer";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.PrinterMoreInfoManufacturer
 * JD-Core Version:    0.6.2
 */