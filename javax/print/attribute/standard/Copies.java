/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.IntegerSyntax;
/*     */ import javax.print.attribute.PrintJobAttribute;
/*     */ import javax.print.attribute.PrintRequestAttribute;
/*     */ 
/*     */ public final class Copies extends IntegerSyntax
/*     */   implements PrintRequestAttribute, PrintJobAttribute
/*     */ {
/*     */   private static final long serialVersionUID = -6426631521680023833L;
/*     */ 
/*     */   public Copies(int paramInt)
/*     */   {
/*  86 */     super(paramInt, 1, 2147483647);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 108 */     return (super.equals(paramObject)) && ((paramObject instanceof Copies));
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/* 121 */     return Copies.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 133 */     return "copies";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.Copies
 * JD-Core Version:    0.6.2
 */