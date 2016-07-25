/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.PrintServiceAttribute;
/*     */ import javax.print.attribute.TextSyntax;
/*     */ 
/*     */ public final class PrinterLocation extends TextSyntax
/*     */   implements PrintServiceAttribute
/*     */ {
/*     */   private static final long serialVersionUID = -1598610039865566337L;
/*     */ 
/*     */   public PrinterLocation(String paramString, Locale paramLocale)
/*     */   {
/*  63 */     super(paramString, paramLocale);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  89 */     return (super.equals(paramObject)) && ((paramObject instanceof PrinterLocation));
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/* 103 */     return PrinterLocation.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 116 */     return "printer-location";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.PrinterLocation
 * JD-Core Version:    0.6.2
 */