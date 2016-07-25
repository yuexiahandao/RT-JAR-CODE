/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.PrintServiceAttribute;
/*     */ import javax.print.attribute.TextSyntax;
/*     */ 
/*     */ public final class PrinterMakeAndModel extends TextSyntax
/*     */   implements PrintServiceAttribute
/*     */ {
/*     */   private static final long serialVersionUID = 4580461489499351411L;
/*     */ 
/*     */   public PrinterMakeAndModel(String paramString, Locale paramLocale)
/*     */   {
/*  61 */     super(paramString, paramLocale);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  87 */     return (super.equals(paramObject)) && ((paramObject instanceof PrinterMakeAndModel));
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/* 102 */     return PrinterMakeAndModel.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 115 */     return "printer-make-and-model";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.PrinterMakeAndModel
 * JD-Core Version:    0.6.2
 */