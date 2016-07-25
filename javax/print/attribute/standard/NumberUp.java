/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.DocAttribute;
/*     */ import javax.print.attribute.IntegerSyntax;
/*     */ import javax.print.attribute.PrintJobAttribute;
/*     */ import javax.print.attribute.PrintRequestAttribute;
/*     */ 
/*     */ public final class NumberUp extends IntegerSyntax
/*     */   implements DocAttribute, PrintRequestAttribute, PrintJobAttribute
/*     */ {
/*     */   private static final long serialVersionUID = -3040436486786527811L;
/*     */ 
/*     */   public NumberUp(int paramInt)
/*     */   {
/* 142 */     super(paramInt, 1, 2147483647);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 164 */     return (super.equals(paramObject)) && ((paramObject instanceof NumberUp));
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/* 177 */     return NumberUp.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 189 */     return "number-up";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.NumberUp
 * JD-Core Version:    0.6.2
 */