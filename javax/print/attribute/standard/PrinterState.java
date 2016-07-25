/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.EnumSyntax;
/*     */ import javax.print.attribute.PrintServiceAttribute;
/*     */ 
/*     */ public final class PrinterState extends EnumSyntax
/*     */   implements PrintServiceAttribute
/*     */ {
/*     */   private static final long serialVersionUID = -649578618346507718L;
/*  57 */   public static final PrinterState UNKNOWN = new PrinterState(0);
/*     */ 
/*  62 */   public static final PrinterState IDLE = new PrinterState(3);
/*     */ 
/*  68 */   public static final PrinterState PROCESSING = new PrinterState(4);
/*     */ 
/*  73 */   public static final PrinterState STOPPED = new PrinterState(5);
/*     */ 
/*  85 */   private static final String[] myStringTable = { "unknown", null, null, "idle", "processing", "stopped" };
/*     */ 
/*  94 */   private static final PrinterState[] myEnumValueTable = { UNKNOWN, null, null, IDLE, PROCESSING, STOPPED };
/*     */ 
/*     */   protected PrinterState(int paramInt)
/*     */   {
/*  82 */     super(paramInt);
/*     */   }
/*     */ 
/*     */   protected String[] getStringTable()
/*     */   {
/* 107 */     return myStringTable;
/*     */   }
/*     */ 
/*     */   protected EnumSyntax[] getEnumValueTable()
/*     */   {
/* 114 */     return myEnumValueTable;
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/* 127 */     return PrinterState.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 139 */     return "printer-state";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.PrinterState
 * JD-Core Version:    0.6.2
 */