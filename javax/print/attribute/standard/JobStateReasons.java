/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.PrintJobAttribute;
/*     */ 
/*     */ public final class JobStateReasons extends HashSet<JobStateReason>
/*     */   implements PrintJobAttribute
/*     */ {
/*     */   private static final long serialVersionUID = 8849088261264331812L;
/*     */ 
/*     */   public JobStateReasons()
/*     */   {
/*     */   }
/*     */ 
/*     */   public JobStateReasons(int paramInt)
/*     */   {
/*  91 */     super(paramInt);
/*     */   }
/*     */ 
/*     */   public JobStateReasons(int paramInt, float paramFloat)
/*     */   {
/* 104 */     super(paramInt, paramFloat);
/*     */   }
/*     */ 
/*     */   public JobStateReasons(Collection<JobStateReason> paramCollection)
/*     */   {
/* 126 */     super(paramCollection);
/*     */   }
/*     */ 
/*     */   public boolean add(JobStateReason paramJobStateReason)
/*     */   {
/* 149 */     if (paramJobStateReason == null) {
/* 150 */       throw new NullPointerException();
/*     */     }
/* 152 */     return super.add(paramJobStateReason);
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/* 165 */     return JobStateReasons.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 178 */     return "job-state-reasons";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.JobStateReasons
 * JD-Core Version:    0.6.2
 */