/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.PrintJobAttribute;
/*     */ import javax.print.attribute.TextSyntax;
/*     */ 
/*     */ public final class JobMessageFromOperator extends TextSyntax
/*     */   implements PrintJobAttribute
/*     */ {
/*     */   private static final long serialVersionUID = -4620751846003142047L;
/*     */ 
/*     */   public JobMessageFromOperator(String paramString, Locale paramLocale)
/*     */   {
/*  72 */     super(paramString, paramLocale);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  98 */     return (super.equals(paramObject)) && ((paramObject instanceof JobMessageFromOperator));
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/* 113 */     return JobMessageFromOperator.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 126 */     return "job-message-from-operator";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.JobMessageFromOperator
 * JD-Core Version:    0.6.2
 */