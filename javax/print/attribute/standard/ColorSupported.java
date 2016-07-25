/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.EnumSyntax;
/*     */ import javax.print.attribute.PrintServiceAttribute;
/*     */ 
/*     */ public final class ColorSupported extends EnumSyntax
/*     */   implements PrintServiceAttribute
/*     */ {
/*     */   private static final long serialVersionUID = -2700555589688535545L;
/*  67 */   public static final ColorSupported NOT_SUPPORTED = new ColorSupported(0);
/*     */ 
/*  73 */   public static final ColorSupported SUPPORTED = new ColorSupported(1);
/*     */ 
/*  85 */   private static final String[] myStringTable = { "not-supported", "supported" };
/*     */ 
/*  88 */   private static final ColorSupported[] myEnumValueTable = { NOT_SUPPORTED, SUPPORTED };
/*     */ 
/*     */   protected ColorSupported(int paramInt)
/*     */   {
/*  82 */     super(paramInt);
/*     */   }
/*     */ 
/*     */   protected String[] getStringTable()
/*     */   {
/*  95 */     return myStringTable;
/*     */   }
/*     */ 
/*     */   protected EnumSyntax[] getEnumValueTable()
/*     */   {
/* 102 */     return myEnumValueTable;
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/* 115 */     return ColorSupported.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 127 */     return "color-supported";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.ColorSupported
 * JD-Core Version:    0.6.2
 */