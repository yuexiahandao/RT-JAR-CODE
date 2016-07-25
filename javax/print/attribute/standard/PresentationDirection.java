/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.EnumSyntax;
/*     */ import javax.print.attribute.PrintJobAttribute;
/*     */ import javax.print.attribute.PrintRequestAttribute;
/*     */ 
/*     */ public final class PresentationDirection extends EnumSyntax
/*     */   implements PrintJobAttribute, PrintRequestAttribute
/*     */ {
/*     */   private static final long serialVersionUID = 8294728067230931780L;
/*  61 */   public static final PresentationDirection TOBOTTOM_TORIGHT = new PresentationDirection(0);
/*     */ 
/*  68 */   public static final PresentationDirection TOBOTTOM_TOLEFT = new PresentationDirection(1);
/*     */ 
/*  75 */   public static final PresentationDirection TOTOP_TORIGHT = new PresentationDirection(2);
/*     */ 
/*  82 */   public static final PresentationDirection TOTOP_TOLEFT = new PresentationDirection(3);
/*     */ 
/*  89 */   public static final PresentationDirection TORIGHT_TOBOTTOM = new PresentationDirection(4);
/*     */ 
/*  96 */   public static final PresentationDirection TORIGHT_TOTOP = new PresentationDirection(5);
/*     */ 
/* 103 */   public static final PresentationDirection TOLEFT_TOBOTTOM = new PresentationDirection(6);
/*     */ 
/* 110 */   public static final PresentationDirection TOLEFT_TOTOP = new PresentationDirection(7);
/*     */ 
/* 123 */   private static final String[] myStringTable = { "tobottom-toright", "tobottom-toleft", "totop-toright", "totop-toleft", "toright-tobottom", "toright-totop", "toleft-tobottom", "toleft-totop" };
/*     */ 
/* 134 */   private static final PresentationDirection[] myEnumValueTable = { TOBOTTOM_TORIGHT, TOBOTTOM_TOLEFT, TOTOP_TORIGHT, TOTOP_TOLEFT, TORIGHT_TOBOTTOM, TORIGHT_TOTOP, TOLEFT_TOBOTTOM, TOLEFT_TOTOP };
/*     */ 
/*     */   private PresentationDirection(int paramInt)
/*     */   {
/* 120 */     super(paramInt);
/*     */   }
/*     */ 
/*     */   protected String[] getStringTable()
/*     */   {
/* 149 */     return myStringTable;
/*     */   }
/*     */ 
/*     */   protected EnumSyntax[] getEnumValueTable()
/*     */   {
/* 156 */     return myEnumValueTable;
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/* 170 */     return PresentationDirection.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 183 */     return "presentation-direction";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.PresentationDirection
 * JD-Core Version:    0.6.2
 */