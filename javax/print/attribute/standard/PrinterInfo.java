/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.PrintServiceAttribute;
/*     */ import javax.print.attribute.TextSyntax;
/*     */ 
/*     */ public final class PrinterInfo extends TextSyntax
/*     */   implements PrintServiceAttribute
/*     */ {
/*     */   private static final long serialVersionUID = 7765280618777599727L;
/*     */ 
/*     */   public PrinterInfo(String paramString, Locale paramLocale)
/*     */   {
/*  67 */     super(paramString, paramLocale);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  93 */     return (super.equals(paramObject)) && ((paramObject instanceof PrinterInfo));
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/* 106 */     return PrinterInfo.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 118 */     return "printer-info";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.PrinterInfo
 * JD-Core Version:    0.6.2
 */