/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.IntegerSyntax;
/*     */ import javax.print.attribute.PrintJobAttribute;
/*     */ import javax.print.attribute.PrintRequestAttribute;
/*     */ 
/*     */ public class JobMediaSheets extends IntegerSyntax
/*     */   implements PrintRequestAttribute, PrintJobAttribute
/*     */ {
/*     */   private static final long serialVersionUID = 408871131531979741L;
/*     */ 
/*     */   public JobMediaSheets(int paramInt)
/*     */   {
/*  85 */     super(paramInt, 0, 2147483647);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 108 */     return (super.equals(paramObject)) && ((paramObject instanceof JobMediaSheets));
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/* 122 */     return JobMediaSheets.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 135 */     return "job-media-sheets";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.JobMediaSheets
 * JD-Core Version:    0.6.2
 */