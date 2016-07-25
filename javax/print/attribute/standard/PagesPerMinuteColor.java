/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.IntegerSyntax;
/*     */ import javax.print.attribute.PrintServiceAttribute;
/*     */ 
/*     */ public final class PagesPerMinuteColor extends IntegerSyntax
/*     */   implements PrintServiceAttribute
/*     */ {
/*     */   static final long serialVersionUID = 1684993151687470944L;
/*     */ 
/*     */   public PagesPerMinuteColor(int paramInt)
/*     */   {
/*  72 */     super(paramInt, 0, 2147483647);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  95 */     return (super.equals(paramObject)) && ((paramObject instanceof PagesPerMinuteColor));
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/* 110 */     return PagesPerMinuteColor.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 123 */     return "pages-per-minute-color";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.PagesPerMinuteColor
 * JD-Core Version:    0.6.2
 */