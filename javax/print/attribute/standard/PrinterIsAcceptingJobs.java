/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.EnumSyntax;
/*     */ import javax.print.attribute.PrintServiceAttribute;
/*     */ 
/*     */ public final class PrinterIsAcceptingJobs extends EnumSyntax
/*     */   implements PrintServiceAttribute
/*     */ {
/*     */   private static final long serialVersionUID = -5052010680537678061L;
/*  59 */   public static final PrinterIsAcceptingJobs NOT_ACCEPTING_JOBS = new PrinterIsAcceptingJobs(0);
/*     */ 
/*  65 */   public static final PrinterIsAcceptingJobs ACCEPTING_JOBS = new PrinterIsAcceptingJobs(1);
/*     */ 
/*  77 */   private static final String[] myStringTable = { "not-accepting-jobs", "accepting-jobs" };
/*     */ 
/*  82 */   private static final PrinterIsAcceptingJobs[] myEnumValueTable = { NOT_ACCEPTING_JOBS, ACCEPTING_JOBS };
/*     */ 
/*     */   protected PrinterIsAcceptingJobs(int paramInt)
/*     */   {
/*  74 */     super(paramInt);
/*     */   }
/*     */ 
/*     */   protected String[] getStringTable()
/*     */   {
/*  91 */     return myStringTable;
/*     */   }
/*     */ 
/*     */   protected EnumSyntax[] getEnumValueTable()
/*     */   {
/*  98 */     return myEnumValueTable;
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/* 112 */     return PrinterIsAcceptingJobs.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 125 */     return "printer-is-accepting-jobs";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.PrinterIsAcceptingJobs
 * JD-Core Version:    0.6.2
 */