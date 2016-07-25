/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.PrintJobAttribute;
/*     */ import javax.print.attribute.PrintRequestAttribute;
/*     */ import javax.print.attribute.TextSyntax;
/*     */ 
/*     */ public final class JobName extends TextSyntax
/*     */   implements PrintRequestAttribute, PrintJobAttribute
/*     */ {
/*     */   private static final long serialVersionUID = 4660359192078689545L;
/*     */ 
/*     */   public JobName(String paramString, Locale paramLocale)
/*     */   {
/*  72 */     super(paramString, paramLocale);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  97 */     return (super.equals(paramObject)) && ((paramObject instanceof JobName));
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/* 110 */     return JobName.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 122 */     return "job-name";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.JobName
 * JD-Core Version:    0.6.2
 */