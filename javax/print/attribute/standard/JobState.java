/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.EnumSyntax;
/*     */ import javax.print.attribute.PrintJobAttribute;
/*     */ 
/*     */ public class JobState extends EnumSyntax
/*     */   implements PrintJobAttribute
/*     */ {
/*     */   private static final long serialVersionUID = 400465010094018920L;
/*  57 */   public static final JobState UNKNOWN = new JobState(0);
/*     */ 
/*  62 */   public static final JobState PENDING = new JobState(3);
/*     */ 
/*  70 */   public static final JobState PENDING_HELD = new JobState(4);
/*     */ 
/* 104 */   public static final JobState PROCESSING = new JobState(5);
/*     */ 
/* 122 */   public static final JobState PROCESSING_STOPPED = new JobState(6);
/*     */ 
/* 135 */   public static final JobState CANCELED = new JobState(7);
/*     */ 
/* 148 */   public static final JobState ABORTED = new JobState(8);
/*     */ 
/* 159 */   public static final JobState COMPLETED = new JobState(9);
/*     */ 
/* 172 */   private static final String[] myStringTable = { "unknown", null, null, "pending", "pending-held", "processing", "processing-stopped", "canceled", "aborted", "completed" };
/*     */ 
/* 184 */   private static final JobState[] myEnumValueTable = { UNKNOWN, null, null, PENDING, PENDING_HELD, PROCESSING, PROCESSING_STOPPED, CANCELED, ABORTED, COMPLETED };
/*     */ 
/*     */   protected JobState(int paramInt)
/*     */   {
/* 169 */     super(paramInt);
/*     */   }
/*     */ 
/*     */   protected String[] getStringTable()
/*     */   {
/* 200 */     return myStringTable;
/*     */   }
/*     */ 
/*     */   protected EnumSyntax[] getEnumValueTable()
/*     */   {
/* 207 */     return myEnumValueTable;
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/* 221 */     return JobState.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 234 */     return "job-state";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.JobState
 * JD-Core Version:    0.6.2
 */