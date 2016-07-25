/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.EnumSyntax;
/*     */ 
/*     */ public final class Severity extends EnumSyntax
/*     */   implements Attribute
/*     */ {
/*     */   private static final long serialVersionUID = 8781881462717925380L;
/*  77 */   public static final Severity REPORT = new Severity(0);
/*     */ 
/*  86 */   public static final Severity WARNING = new Severity(1);
/*     */ 
/*  94 */   public static final Severity ERROR = new Severity(2);
/*     */ 
/* 106 */   private static final String[] myStringTable = { "report", "warning", "error" };
/*     */ 
/* 112 */   private static final Severity[] myEnumValueTable = { REPORT, WARNING, ERROR };
/*     */ 
/*     */   protected Severity(int paramInt)
/*     */   {
/* 103 */     super(paramInt);
/*     */   }
/*     */ 
/*     */   protected String[] getStringTable()
/*     */   {
/* 122 */     return myStringTable;
/*     */   }
/*     */ 
/*     */   protected EnumSyntax[] getEnumValueTable()
/*     */   {
/* 129 */     return myEnumValueTable;
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/* 143 */     return Severity.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 155 */     return "severity";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.Severity
 * JD-Core Version:    0.6.2
 */