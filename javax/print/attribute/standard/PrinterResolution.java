/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.DocAttribute;
/*     */ import javax.print.attribute.PrintJobAttribute;
/*     */ import javax.print.attribute.PrintRequestAttribute;
/*     */ import javax.print.attribute.ResolutionSyntax;
/*     */ 
/*     */ public final class PrinterResolution extends ResolutionSyntax
/*     */   implements DocAttribute, PrintRequestAttribute, PrintJobAttribute
/*     */ {
/*     */   private static final long serialVersionUID = 13090306561090558L;
/*     */ 
/*     */   public PrinterResolution(int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/*  95 */     super(paramInt1, paramInt2, paramInt3);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 121 */     return (super.equals(paramObject)) && ((paramObject instanceof PrinterResolution));
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/* 135 */     return PrinterResolution.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 148 */     return "printer-resolution";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.PrinterResolution
 * JD-Core Version:    0.6.2
 */