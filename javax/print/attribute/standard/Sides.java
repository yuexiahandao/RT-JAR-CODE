/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.DocAttribute;
/*     */ import javax.print.attribute.EnumSyntax;
/*     */ import javax.print.attribute.PrintJobAttribute;
/*     */ import javax.print.attribute.PrintRequestAttribute;
/*     */ 
/*     */ public final class Sides extends EnumSyntax
/*     */   implements DocAttribute, PrintRequestAttribute, PrintJobAttribute
/*     */ {
/*     */   private static final long serialVersionUID = -6890309414893262822L;
/* 130 */   public static final Sides ONE_SIDED = new Sides(0);
/*     */ 
/* 139 */   public static final Sides TWO_SIDED_LONG_EDGE = new Sides(1);
/*     */ 
/* 148 */   public static final Sides TWO_SIDED_SHORT_EDGE = new Sides(2);
/*     */ 
/* 154 */   public static final Sides DUPLEX = TWO_SIDED_LONG_EDGE;
/*     */ 
/* 160 */   public static final Sides TUMBLE = TWO_SIDED_SHORT_EDGE;
/*     */ 
/* 171 */   private static final String[] myStringTable = { "one-sided", "two-sided-long-edge", "two-sided-short-edge" };
/*     */ 
/* 177 */   private static final Sides[] myEnumValueTable = { ONE_SIDED, TWO_SIDED_LONG_EDGE, TWO_SIDED_SHORT_EDGE };
/*     */ 
/*     */   protected Sides(int paramInt)
/*     */   {
/* 168 */     super(paramInt);
/*     */   }
/*     */ 
/*     */   protected String[] getStringTable()
/*     */   {
/* 187 */     return myStringTable;
/*     */   }
/*     */ 
/*     */   protected EnumSyntax[] getEnumValueTable()
/*     */   {
/* 194 */     return myEnumValueTable;
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/* 207 */     return Sides.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 219 */     return "sides";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.Sides
 * JD-Core Version:    0.6.2
 */