/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.EnumSyntax;
/*     */ import javax.print.attribute.PrintJobAttribute;
/*     */ import javax.print.attribute.PrintRequestAttribute;
/*     */ 
/*     */ public class JobSheets extends EnumSyntax
/*     */   implements PrintRequestAttribute, PrintJobAttribute
/*     */ {
/*     */   private static final long serialVersionUID = -4735258056132519759L;
/*  64 */   public static final JobSheets NONE = new JobSheets(0);
/*     */ 
/*  71 */   public static final JobSheets STANDARD = new JobSheets(1);
/*     */ 
/*  83 */   private static final String[] myStringTable = { "none", "standard" };
/*     */ 
/*  88 */   private static final JobSheets[] myEnumValueTable = { NONE, STANDARD };
/*     */ 
/*     */   protected JobSheets(int paramInt)
/*     */   {
/*  80 */     super(paramInt);
/*     */   }
/*     */ 
/*     */   protected String[] getStringTable()
/*     */   {
/*  97 */     return (String[])myStringTable.clone();
/*     */   }
/*     */ 
/*     */   protected EnumSyntax[] getEnumValueTable()
/*     */   {
/* 104 */     return (EnumSyntax[])myEnumValueTable.clone();
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/* 118 */     return JobSheets.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 131 */     return "job-sheets";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.JobSheets
 * JD-Core Version:    0.6.2
 */