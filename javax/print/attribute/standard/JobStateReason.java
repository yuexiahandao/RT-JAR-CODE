/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.EnumSyntax;
/*     */ 
/*     */ public class JobStateReason extends EnumSyntax
/*     */   implements Attribute
/*     */ {
/*     */   private static final long serialVersionUID = -8765894420449009168L;
/*  67 */   public static final JobStateReason JOB_INCOMING = new JobStateReason(0);
/*     */ 
/*  79 */   public static final JobStateReason JOB_DATA_INSUFFICIENT = new JobStateReason(1);
/*     */ 
/*  95 */   public static final JobStateReason DOCUMENT_ACCESS_ERROR = new JobStateReason(2);
/*     */ 
/* 106 */   public static final JobStateReason SUBMISSION_INTERRUPTED = new JobStateReason(3);
/*     */ 
/* 112 */   public static final JobStateReason JOB_OUTGOING = new JobStateReason(4);
/*     */ 
/* 123 */   public static final JobStateReason JOB_HOLD_UNTIL_SPECIFIED = new JobStateReason(5);
/*     */ 
/* 136 */   public static final JobStateReason RESOURCES_ARE_NOT_READY = new JobStateReason(6);
/*     */ 
/* 144 */   public static final JobStateReason PRINTER_STOPPED_PARTLY = new JobStateReason(7);
/*     */ 
/* 151 */   public static final JobStateReason PRINTER_STOPPED = new JobStateReason(8);
/*     */ 
/* 158 */   public static final JobStateReason JOB_INTERPRETING = new JobStateReason(9);
/*     */ 
/* 164 */   public static final JobStateReason JOB_QUEUED = new JobStateReason(10);
/*     */ 
/* 172 */   public static final JobStateReason JOB_TRANSFORMING = new JobStateReason(11);
/*     */ 
/* 186 */   public static final JobStateReason JOB_QUEUED_FOR_MARKER = new JobStateReason(12);
/*     */ 
/* 197 */   public static final JobStateReason JOB_PRINTING = new JobStateReason(13);
/*     */ 
/* 207 */   public static final JobStateReason JOB_CANCELED_BY_USER = new JobStateReason(14);
/*     */ 
/* 220 */   public static final JobStateReason JOB_CANCELED_BY_OPERATOR = new JobStateReason(15);
/*     */ 
/* 228 */   public static final JobStateReason JOB_CANCELED_AT_DEVICE = new JobStateReason(16);
/*     */ 
/* 238 */   public static final JobStateReason ABORTED_BY_SYSTEM = new JobStateReason(17);
/*     */ 
/* 248 */   public static final JobStateReason UNSUPPORTED_COMPRESSION = new JobStateReason(18);
/*     */ 
/* 257 */   public static final JobStateReason COMPRESSION_ERROR = new JobStateReason(19);
/*     */ 
/* 270 */   public static final JobStateReason UNSUPPORTED_DOCUMENT_FORMAT = new JobStateReason(20);
/*     */ 
/* 279 */   public static final JobStateReason DOCUMENT_FORMAT_ERROR = new JobStateReason(21);
/*     */ 
/* 295 */   public static final JobStateReason PROCESSING_TO_STOP_POINT = new JobStateReason(22);
/*     */ 
/* 303 */   public static final JobStateReason SERVICE_OFF_LINE = new JobStateReason(23);
/*     */ 
/* 309 */   public static final JobStateReason JOB_COMPLETED_SUCCESSFULLY = new JobStateReason(24);
/*     */ 
/* 316 */   public static final JobStateReason JOB_COMPLETED_WITH_WARNINGS = new JobStateReason(25);
/*     */ 
/* 323 */   public static final JobStateReason JOB_COMPLETED_WITH_ERRORS = new JobStateReason(26);
/*     */ 
/* 334 */   public static final JobStateReason JOB_RESTARTABLE = new JobStateReason(27);
/*     */ 
/* 345 */   public static final JobStateReason QUEUED_IN_DEVICE = new JobStateReason(28);
/*     */ 
/* 357 */   private static final String[] myStringTable = { "job-incoming", "job-data-insufficient", "document-access-error", "submission-interrupted", "job-outgoing", "job-hold-until-specified", "resources-are-not-ready", "printer-stopped-partly", "printer-stopped", "job-interpreting", "job-queued", "job-transforming", "job-queued-for-marker", "job-printing", "job-canceled-by-user", "job-canceled-by-operator", "job-canceled-at-device", "aborted-by-system", "unsupported-compression", "compression-error", "unsupported-document-format", "document-format-error", "processing-to-stop-point", "service-off-line", "job-completed-successfully", "job-completed-with-warnings", "job-completed-with-errors", "job-restartable", "queued-in-device" };
/*     */ 
/* 388 */   private static final JobStateReason[] myEnumValueTable = { JOB_INCOMING, JOB_DATA_INSUFFICIENT, DOCUMENT_ACCESS_ERROR, SUBMISSION_INTERRUPTED, JOB_OUTGOING, JOB_HOLD_UNTIL_SPECIFIED, RESOURCES_ARE_NOT_READY, PRINTER_STOPPED_PARTLY, PRINTER_STOPPED, JOB_INTERPRETING, JOB_QUEUED, JOB_TRANSFORMING, JOB_QUEUED_FOR_MARKER, JOB_PRINTING, JOB_CANCELED_BY_USER, JOB_CANCELED_BY_OPERATOR, JOB_CANCELED_AT_DEVICE, ABORTED_BY_SYSTEM, UNSUPPORTED_COMPRESSION, COMPRESSION_ERROR, UNSUPPORTED_DOCUMENT_FORMAT, DOCUMENT_FORMAT_ERROR, PROCESSING_TO_STOP_POINT, SERVICE_OFF_LINE, JOB_COMPLETED_SUCCESSFULLY, JOB_COMPLETED_WITH_WARNINGS, JOB_COMPLETED_WITH_ERRORS, JOB_RESTARTABLE, QUEUED_IN_DEVICE };
/*     */ 
/*     */   protected JobStateReason(int paramInt)
/*     */   {
/* 354 */     super(paramInt);
/*     */   }
/*     */ 
/*     */   protected String[] getStringTable()
/*     */   {
/* 423 */     return (String[])myStringTable.clone();
/*     */   }
/*     */ 
/*     */   protected EnumSyntax[] getEnumValueTable()
/*     */   {
/* 430 */     return (EnumSyntax[])myEnumValueTable.clone();
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/* 445 */     return JobStateReason.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 458 */     return "job-state-reason";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.JobStateReason
 * JD-Core Version:    0.6.2
 */