/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.DocAttribute;
/*     */ import javax.print.attribute.EnumSyntax;
/*     */ import javax.print.attribute.PrintJobAttribute;
/*     */ import javax.print.attribute.PrintRequestAttribute;
/*     */ 
/*     */ public class Finishings extends EnumSyntax
/*     */   implements DocAttribute, PrintRequestAttribute, PrintJobAttribute
/*     */ {
/*     */   private static final long serialVersionUID = -627840419548391754L;
/* 231 */   public static final Finishings NONE = new Finishings(3);
/*     */ 
/* 237 */   public static final Finishings STAPLE = new Finishings(4);
/*     */ 
/* 245 */   public static final Finishings COVER = new Finishings(6);
/*     */ 
/* 251 */   public static final Finishings BIND = new Finishings(7);
/*     */ 
/* 258 */   public static final Finishings SADDLE_STITCH = new Finishings(8);
/*     */ 
/* 266 */   public static final Finishings EDGE_STITCH = new Finishings(9);
/*     */ 
/* 272 */   public static final Finishings STAPLE_TOP_LEFT = new Finishings(20);
/*     */ 
/* 279 */   public static final Finishings STAPLE_BOTTOM_LEFT = new Finishings(21);
/*     */ 
/* 285 */   public static final Finishings STAPLE_TOP_RIGHT = new Finishings(22);
/*     */ 
/* 292 */   public static final Finishings STAPLE_BOTTOM_RIGHT = new Finishings(23);
/*     */ 
/* 300 */   public static final Finishings EDGE_STITCH_LEFT = new Finishings(24);
/*     */ 
/* 308 */   public static final Finishings EDGE_STITCH_TOP = new Finishings(25);
/*     */ 
/* 316 */   public static final Finishings EDGE_STITCH_RIGHT = new Finishings(26);
/*     */ 
/* 324 */   public static final Finishings EDGE_STITCH_BOTTOM = new Finishings(27);
/*     */ 
/* 331 */   public static final Finishings STAPLE_DUAL_LEFT = new Finishings(28);
/*     */ 
/* 338 */   public static final Finishings STAPLE_DUAL_TOP = new Finishings(29);
/*     */ 
/* 345 */   public static final Finishings STAPLE_DUAL_RIGHT = new Finishings(30);
/*     */ 
/* 352 */   public static final Finishings STAPLE_DUAL_BOTTOM = new Finishings(31);
/*     */ 
/* 365 */   private static final String[] myStringTable = { "none", "staple", null, "cover", "bind", "saddle-stitch", "edge-stitch", null, null, null, null, null, null, null, null, null, null, "staple-top-left", "staple-bottom-left", "staple-top-right", "staple-bottom-right", "edge-stitch-left", "edge-stitch-top", "edge-stitch-right", "edge-stitch-bottom", "staple-dual-left", "staple-dual-top", "staple-dual-right", "staple-dual-bottom" };
/*     */ 
/* 397 */   private static final Finishings[] myEnumValueTable = { NONE, STAPLE, null, COVER, BIND, SADDLE_STITCH, EDGE_STITCH, null, null, null, null, null, null, null, null, null, null, STAPLE_TOP_LEFT, STAPLE_BOTTOM_LEFT, STAPLE_TOP_RIGHT, STAPLE_BOTTOM_RIGHT, EDGE_STITCH_LEFT, EDGE_STITCH_TOP, EDGE_STITCH_RIGHT, EDGE_STITCH_BOTTOM, STAPLE_DUAL_LEFT, STAPLE_DUAL_TOP, STAPLE_DUAL_RIGHT, STAPLE_DUAL_BOTTOM };
/*     */ 
/*     */   protected Finishings(int paramInt)
/*     */   {
/* 362 */     super(paramInt);
/*     */   }
/*     */ 
/*     */   protected String[] getStringTable()
/*     */   {
/* 433 */     return (String[])myStringTable.clone();
/*     */   }
/*     */ 
/*     */   protected EnumSyntax[] getEnumValueTable()
/*     */   {
/* 440 */     return (EnumSyntax[])myEnumValueTable.clone();
/*     */   }
/*     */ 
/*     */   protected int getOffset()
/*     */   {
/* 447 */     return 3;
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/* 461 */     return Finishings.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 474 */     return "finishings";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.Finishings
 * JD-Core Version:    0.6.2
 */