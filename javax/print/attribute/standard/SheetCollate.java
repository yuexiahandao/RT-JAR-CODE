/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.DocAttribute;
/*     */ import javax.print.attribute.EnumSyntax;
/*     */ import javax.print.attribute.PrintJobAttribute;
/*     */ import javax.print.attribute.PrintRequestAttribute;
/*     */ 
/*     */ public final class SheetCollate extends EnumSyntax
/*     */   implements DocAttribute, PrintRequestAttribute, PrintJobAttribute
/*     */ {
/*     */   private static final long serialVersionUID = 7080587914259873003L;
/* 167 */   public static final SheetCollate UNCOLLATED = new SheetCollate(0);
/*     */ 
/* 173 */   public static final SheetCollate COLLATED = new SheetCollate(1);
/*     */ 
/* 185 */   private static final String[] myStringTable = { "uncollated", "collated" };
/*     */ 
/* 190 */   private static final SheetCollate[] myEnumValueTable = { UNCOLLATED, COLLATED };
/*     */ 
/*     */   protected SheetCollate(int paramInt)
/*     */   {
/* 182 */     super(paramInt);
/*     */   }
/*     */ 
/*     */   protected String[] getStringTable()
/*     */   {
/* 199 */     return myStringTable;
/*     */   }
/*     */ 
/*     */   protected EnumSyntax[] getEnumValueTable()
/*     */   {
/* 206 */     return myEnumValueTable;
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/* 219 */     return SheetCollate.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 231 */     return "sheet-collate";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.SheetCollate
 * JD-Core Version:    0.6.2
 */