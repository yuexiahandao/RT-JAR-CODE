/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.IntegerSyntax;
/*     */ import javax.print.attribute.PrintServiceAttribute;
/*     */ 
/*     */ public final class PagesPerMinute extends IntegerSyntax
/*     */   implements PrintServiceAttribute
/*     */ {
/*     */   private static final long serialVersionUID = -6366403993072862015L;
/*     */ 
/*     */   public PagesPerMinute(int paramInt)
/*     */   {
/*  61 */     super(paramInt, 0, 2147483647);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  84 */     return (super.equals(paramObject)) && ((paramObject instanceof PagesPerMinute));
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/*  98 */     return PagesPerMinute.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 111 */     return "pages-per-minute";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.PagesPerMinute
 * JD-Core Version:    0.6.2
 */