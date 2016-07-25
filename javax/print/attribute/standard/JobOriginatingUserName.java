/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.PrintJobAttribute;
/*     */ import javax.print.attribute.TextSyntax;
/*     */ 
/*     */ public final class JobOriginatingUserName extends TextSyntax
/*     */   implements PrintJobAttribute
/*     */ {
/*     */   private static final long serialVersionUID = -8052537926362933477L;
/*     */ 
/*     */   public JobOriginatingUserName(String paramString, Locale paramLocale)
/*     */   {
/*  72 */     super(paramString, paramLocale);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  98 */     return (super.equals(paramObject)) && ((paramObject instanceof JobOriginatingUserName));
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/* 113 */     return JobOriginatingUserName.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 126 */     return "job-originating-user-name";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.JobOriginatingUserName
 * JD-Core Version:    0.6.2
 */