/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import javax.print.attribute.EnumSyntax;
/*     */ import javax.print.attribute.PrintRequestAttribute;
/*     */ 
/*     */ public final class DialogTypeSelection extends EnumSyntax
/*     */   implements PrintRequestAttribute
/*     */ {
/*     */   private static final long serialVersionUID = 7518682952133256029L;
/*  61 */   public static final DialogTypeSelection NATIVE = new DialogTypeSelection(0);
/*     */ 
/*  67 */   public static final DialogTypeSelection COMMON = new DialogTypeSelection(1);
/*     */ 
/*  79 */   private static final String[] myStringTable = { "native", "common" };
/*     */ 
/*  83 */   private static final DialogTypeSelection[] myEnumValueTable = { NATIVE, COMMON };
/*     */ 
/*     */   protected DialogTypeSelection(int paramInt)
/*     */   {
/*  76 */     super(paramInt);
/*     */   }
/*     */ 
/*     */   protected String[] getStringTable()
/*     */   {
/*  92 */     return myStringTable;
/*     */   }
/*     */ 
/*     */   protected EnumSyntax[] getEnumValueTable()
/*     */   {
/*  99 */     return myEnumValueTable;
/*     */   }
/*     */ 
/*     */   public final Class getCategory()
/*     */   {
/* 114 */     return DialogTypeSelection.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 128 */     return "dialog-type-selection";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.DialogTypeSelection
 * JD-Core Version:    0.6.2
 */