/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.EnumSyntax;
/*     */ import javax.print.attribute.PrintServiceAttribute;
/*     */ 
/*     */ public class PDLOverrideSupported extends EnumSyntax
/*     */   implements PrintServiceAttribute
/*     */ {
/*     */   private static final long serialVersionUID = -4393264467928463934L;
/*  56 */   public static final PDLOverrideSupported NOT_ATTEMPTED = new PDLOverrideSupported(0);
/*     */ 
/*  64 */   public static final PDLOverrideSupported ATTEMPTED = new PDLOverrideSupported(1);
/*     */ 
/*  77 */   private static final String[] myStringTable = { "not-attempted", "attempted" };
/*     */ 
/*  82 */   private static final PDLOverrideSupported[] myEnumValueTable = { NOT_ATTEMPTED, ATTEMPTED };
/*     */ 
/*     */   protected PDLOverrideSupported(int paramInt)
/*     */   {
/*  74 */     super(paramInt);
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
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/* 112 */     return PDLOverrideSupported.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 125 */     return "pdl-override-supported";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.PDLOverrideSupported
 * JD-Core Version:    0.6.2
 */