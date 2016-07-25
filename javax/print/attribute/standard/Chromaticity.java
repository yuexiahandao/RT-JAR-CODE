/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.DocAttribute;
/*     */ import javax.print.attribute.EnumSyntax;
/*     */ import javax.print.attribute.PrintJobAttribute;
/*     */ import javax.print.attribute.PrintRequestAttribute;
/*     */ 
/*     */ public final class Chromaticity extends EnumSyntax
/*     */   implements DocAttribute, PrintRequestAttribute, PrintJobAttribute
/*     */ {
/*     */   private static final long serialVersionUID = 4660543931355214012L;
/*  96 */   public static final Chromaticity MONOCHROME = new Chromaticity(0);
/*     */ 
/* 101 */   public static final Chromaticity COLOR = new Chromaticity(1);
/*     */ 
/* 114 */   private static final String[] myStringTable = { "monochrome", "color" };
/*     */ 
/* 117 */   private static final Chromaticity[] myEnumValueTable = { MONOCHROME, COLOR };
/*     */ 
/*     */   protected Chromaticity(int paramInt)
/*     */   {
/* 111 */     super(paramInt);
/*     */   }
/*     */ 
/*     */   protected String[] getStringTable()
/*     */   {
/* 124 */     return myStringTable;
/*     */   }
/*     */ 
/*     */   protected EnumSyntax[] getEnumValueTable()
/*     */   {
/* 131 */     return myEnumValueTable;
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/* 144 */     return Chromaticity.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 156 */     return "chromaticity";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.Chromaticity
 * JD-Core Version:    0.6.2
 */