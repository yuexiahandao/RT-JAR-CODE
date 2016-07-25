/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.PrintServiceAttribute;
/*     */ import javax.print.attribute.TextSyntax;
/*     */ 
/*     */ public final class PrinterName extends TextSyntax
/*     */   implements PrintServiceAttribute
/*     */ {
/*     */   private static final long serialVersionUID = 299740639137803127L;
/*     */ 
/*     */   public PrinterName(String paramString, Locale paramLocale)
/*     */   {
/*  65 */     super(paramString, paramLocale);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  91 */     return (super.equals(paramObject)) && ((paramObject instanceof PrinterName));
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/* 105 */     return PrinterName.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 118 */     return "printer-name";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.PrinterName
 * JD-Core Version:    0.6.2
 */