/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import java.net.URI;
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.PrintServiceAttribute;
/*     */ import javax.print.attribute.URISyntax;
/*     */ 
/*     */ public final class PrinterMoreInfo extends URISyntax
/*     */   implements PrintServiceAttribute
/*     */ {
/*     */   private static final long serialVersionUID = 4555850007675338574L;
/*     */ 
/*     */   public PrinterMoreInfo(URI paramURI)
/*     */   {
/*  70 */     super(paramURI);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  93 */     return (super.equals(paramObject)) && ((paramObject instanceof PrinterMoreInfo));
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/* 107 */     return PrinterMoreInfo.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 120 */     return "printer-more-info";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.PrinterMoreInfo
 * JD-Core Version:    0.6.2
 */