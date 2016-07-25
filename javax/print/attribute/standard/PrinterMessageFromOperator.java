/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.PrintServiceAttribute;
/*     */ import javax.print.attribute.TextSyntax;
/*     */ 
/*     */ public final class PrinterMessageFromOperator extends TextSyntax
/*     */   implements PrintServiceAttribute
/*     */ {
/*     */   static final long serialVersionUID = -4486871203218629318L;
/*     */ 
/*     */   public PrinterMessageFromOperator(String paramString, Locale paramLocale)
/*     */   {
/*  76 */     super(paramString, paramLocale);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 103 */     return (super.equals(paramObject)) && ((paramObject instanceof PrinterMessageFromOperator));
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/* 118 */     return PrinterMessageFromOperator.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 131 */     return "printer-message-from-operator";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.PrinterMessageFromOperator
 * JD-Core Version:    0.6.2
 */