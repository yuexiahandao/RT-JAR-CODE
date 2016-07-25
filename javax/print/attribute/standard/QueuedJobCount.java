/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.IntegerSyntax;
/*     */ import javax.print.attribute.PrintServiceAttribute;
/*     */ 
/*     */ public final class QueuedJobCount extends IntegerSyntax
/*     */   implements PrintServiceAttribute
/*     */ {
/*     */   private static final long serialVersionUID = 7499723077864047742L;
/*     */ 
/*     */   public QueuedJobCount(int paramInt)
/*     */   {
/*  58 */     super(paramInt, 0, 2147483647);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  81 */     return (super.equals(paramObject)) && ((paramObject instanceof QueuedJobCount));
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/*  95 */     return QueuedJobCount.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 108 */     return "queued-job-count";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.QueuedJobCount
 * JD-Core Version:    0.6.2
 */