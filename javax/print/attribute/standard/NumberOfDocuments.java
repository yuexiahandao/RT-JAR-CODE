/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.IntegerSyntax;
/*     */ import javax.print.attribute.PrintJobAttribute;
/*     */ 
/*     */ public final class NumberOfDocuments extends IntegerSyntax
/*     */   implements PrintJobAttribute
/*     */ {
/*     */   private static final long serialVersionUID = 7891881310684461097L;
/*     */ 
/*     */   public NumberOfDocuments(int paramInt)
/*     */   {
/*  60 */     super(paramInt, 0, 2147483647);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  83 */     return (super.equals(paramObject)) && ((paramObject instanceof NumberOfDocuments));
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/*  98 */     return NumberOfDocuments.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 111 */     return "number-of-documents";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.NumberOfDocuments
 * JD-Core Version:    0.6.2
 */