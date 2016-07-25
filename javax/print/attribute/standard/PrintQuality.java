/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.DocAttribute;
/*     */ import javax.print.attribute.EnumSyntax;
/*     */ import javax.print.attribute.PrintJobAttribute;
/*     */ import javax.print.attribute.PrintRequestAttribute;
/*     */ 
/*     */ public class PrintQuality extends EnumSyntax
/*     */   implements DocAttribute, PrintRequestAttribute, PrintJobAttribute
/*     */ {
/*     */   private static final long serialVersionUID = -3072341285225858365L;
/*  53 */   public static final PrintQuality DRAFT = new PrintQuality(3);
/*     */ 
/*  58 */   public static final PrintQuality NORMAL = new PrintQuality(4);
/*     */ 
/*  63 */   public static final PrintQuality HIGH = new PrintQuality(5);
/*     */ 
/*  75 */   private static final String[] myStringTable = { "draft", "normal", "high" };
/*     */ 
/*  81 */   private static final PrintQuality[] myEnumValueTable = { DRAFT, NORMAL, HIGH };
/*     */ 
/*     */   protected PrintQuality(int paramInt)
/*     */   {
/*  72 */     super(paramInt);
/*     */   }
/*     */ 
/*     */   protected String[] getStringTable()
/*     */   {
/*  91 */     return (String[])myStringTable.clone();
/*     */   }
/*     */ 
/*     */   protected EnumSyntax[] getEnumValueTable()
/*     */   {
/*  98 */     return (EnumSyntax[])myEnumValueTable.clone();
/*     */   }
/*     */ 
/*     */   protected int getOffset()
/*     */   {
/* 105 */     return 3;
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/* 119 */     return PrintQuality.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 132 */     return "print-quality";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.PrintQuality
 * JD-Core Version:    0.6.2
 */