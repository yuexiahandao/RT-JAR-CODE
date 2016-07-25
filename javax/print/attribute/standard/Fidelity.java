/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.EnumSyntax;
/*     */ import javax.print.attribute.PrintJobAttribute;
/*     */ import javax.print.attribute.PrintRequestAttribute;
/*     */ 
/*     */ public final class Fidelity extends EnumSyntax
/*     */   implements PrintJobAttribute, PrintRequestAttribute
/*     */ {
/*     */   private static final long serialVersionUID = 6320827847329172308L;
/*  61 */   public static final Fidelity FIDELITY_TRUE = new Fidelity(0);
/*     */ 
/*  68 */   public static final Fidelity FIDELITY_FALSE = new Fidelity(1);
/*     */ 
/*  80 */   private static final String[] myStringTable = { "true", "false" };
/*     */ 
/*  86 */   private static final Fidelity[] myEnumValueTable = { FIDELITY_TRUE, FIDELITY_FALSE };
/*     */ 
/*     */   protected Fidelity(int paramInt)
/*     */   {
/*  77 */     super(paramInt);
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
/* 113 */     return Fidelity.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 126 */     return "ipp-attribute-fidelity";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.Fidelity
 * JD-Core Version:    0.6.2
 */