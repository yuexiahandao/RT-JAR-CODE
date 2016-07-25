/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.EnumSyntax;
/*     */ 
/*     */ public class PrinterStateReason extends EnumSyntax
/*     */   implements Attribute
/*     */ {
/*     */   private static final long serialVersionUID = -1623720656201472593L;
/*  75 */   public static final PrinterStateReason OTHER = new PrinterStateReason(0);
/*     */ 
/*  81 */   public static final PrinterStateReason MEDIA_NEEDED = new PrinterStateReason(1);
/*     */ 
/*  87 */   public static final PrinterStateReason MEDIA_JAM = new PrinterStateReason(2);
/*     */ 
/* 100 */   public static final PrinterStateReason MOVING_TO_PAUSED = new PrinterStateReason(3);
/*     */ 
/* 113 */   public static final PrinterStateReason PAUSED = new PrinterStateReason(4);
/*     */ 
/* 130 */   public static final PrinterStateReason SHUTDOWN = new PrinterStateReason(5);
/*     */ 
/* 140 */   public static final PrinterStateReason CONNECTING_TO_DEVICE = new PrinterStateReason(6);
/*     */ 
/* 147 */   public static final PrinterStateReason TIMED_OUT = new PrinterStateReason(7);
/*     */ 
/* 160 */   public static final PrinterStateReason STOPPING = new PrinterStateReason(8);
/*     */ 
/* 171 */   public static final PrinterStateReason STOPPED_PARTLY = new PrinterStateReason(9);
/*     */ 
/* 177 */   public static final PrinterStateReason TONER_LOW = new PrinterStateReason(10);
/*     */ 
/* 183 */   public static final PrinterStateReason TONER_EMPTY = new PrinterStateReason(11);
/*     */ 
/* 195 */   public static final PrinterStateReason SPOOL_AREA_FULL = new PrinterStateReason(12);
/*     */ 
/* 201 */   public static final PrinterStateReason COVER_OPEN = new PrinterStateReason(13);
/*     */ 
/* 207 */   public static final PrinterStateReason INTERLOCK_OPEN = new PrinterStateReason(14);
/*     */ 
/* 213 */   public static final PrinterStateReason DOOR_OPEN = new PrinterStateReason(15);
/*     */ 
/* 219 */   public static final PrinterStateReason INPUT_TRAY_MISSING = new PrinterStateReason(16);
/*     */ 
/* 225 */   public static final PrinterStateReason MEDIA_LOW = new PrinterStateReason(17);
/*     */ 
/* 231 */   public static final PrinterStateReason MEDIA_EMPTY = new PrinterStateReason(18);
/*     */ 
/* 237 */   public static final PrinterStateReason OUTPUT_TRAY_MISSING = new PrinterStateReason(19);
/*     */ 
/* 244 */   public static final PrinterStateReason OUTPUT_AREA_ALMOST_FULL = new PrinterStateReason(20);
/*     */ 
/* 250 */   public static final PrinterStateReason OUTPUT_AREA_FULL = new PrinterStateReason(21);
/*     */ 
/* 257 */   public static final PrinterStateReason MARKER_SUPPLY_LOW = new PrinterStateReason(22);
/*     */ 
/* 264 */   public static final PrinterStateReason MARKER_SUPPLY_EMPTY = new PrinterStateReason(23);
/*     */ 
/* 270 */   public static final PrinterStateReason MARKER_WASTE_ALMOST_FULL = new PrinterStateReason(24);
/*     */ 
/* 276 */   public static final PrinterStateReason MARKER_WASTE_FULL = new PrinterStateReason(25);
/*     */ 
/* 282 */   public static final PrinterStateReason FUSER_OVER_TEMP = new PrinterStateReason(26);
/*     */ 
/* 288 */   public static final PrinterStateReason FUSER_UNDER_TEMP = new PrinterStateReason(27);
/*     */ 
/* 294 */   public static final PrinterStateReason OPC_NEAR_EOL = new PrinterStateReason(28);
/*     */ 
/* 300 */   public static final PrinterStateReason OPC_LIFE_OVER = new PrinterStateReason(29);
/*     */ 
/* 306 */   public static final PrinterStateReason DEVELOPER_LOW = new PrinterStateReason(30);
/*     */ 
/* 312 */   public static final PrinterStateReason DEVELOPER_EMPTY = new PrinterStateReason(31);
/*     */ 
/* 318 */   public static final PrinterStateReason INTERPRETER_RESOURCE_UNAVAILABLE = new PrinterStateReason(32);
/*     */ 
/* 330 */   private static final String[] myStringTable = { "other", "media-needed", "media-jam", "moving-to-paused", "paused", "shutdown", "connecting-to-device", "timed-out", "stopping", "stopped-partly", "toner-low", "toner-empty", "spool-area-full", "cover-open", "interlock-open", "door-open", "input-tray-missing", "media-low", "media-empty", "output-tray-missing", "output-area-almost-full", "output-area-full", "marker-supply-low", "marker-supply-empty", "marker-waste-almost-full", "marker-waste-full", "fuser-over-temp", "fuser-under-temp", "opc-near-eol", "opc-life-over", "developer-low", "developer-empty", "interpreter-resource-unavailable" };
/*     */ 
/* 366 */   private static final PrinterStateReason[] myEnumValueTable = { OTHER, MEDIA_NEEDED, MEDIA_JAM, MOVING_TO_PAUSED, PAUSED, SHUTDOWN, CONNECTING_TO_DEVICE, TIMED_OUT, STOPPING, STOPPED_PARTLY, TONER_LOW, TONER_EMPTY, SPOOL_AREA_FULL, COVER_OPEN, INTERLOCK_OPEN, DOOR_OPEN, INPUT_TRAY_MISSING, MEDIA_LOW, MEDIA_EMPTY, OUTPUT_TRAY_MISSING, OUTPUT_AREA_ALMOST_FULL, OUTPUT_AREA_FULL, MARKER_SUPPLY_LOW, MARKER_SUPPLY_EMPTY, MARKER_WASTE_ALMOST_FULL, MARKER_WASTE_FULL, FUSER_OVER_TEMP, FUSER_UNDER_TEMP, OPC_NEAR_EOL, OPC_LIFE_OVER, DEVELOPER_LOW, DEVELOPER_EMPTY, INTERPRETER_RESOURCE_UNAVAILABLE };
/*     */ 
/*     */   protected PrinterStateReason(int paramInt)
/*     */   {
/* 327 */     super(paramInt);
/*     */   }
/*     */ 
/*     */   protected String[] getStringTable()
/*     */   {
/* 406 */     return (String[])myStringTable.clone();
/*     */   }
/*     */ 
/*     */   protected EnumSyntax[] getEnumValueTable()
/*     */   {
/* 413 */     return (EnumSyntax[])myEnumValueTable.clone();
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/* 428 */     return PrinterStateReason.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 441 */     return "printer-state-reason";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.PrinterStateReason
 * JD-Core Version:    0.6.2
 */