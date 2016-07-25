/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.PrintRequestAttribute;
/*     */ import javax.print.attribute.TextSyntax;
/*     */ 
/*     */ public final class RequestingUserName extends TextSyntax
/*     */   implements PrintRequestAttribute
/*     */ {
/*     */   private static final long serialVersionUID = -2683049894310331454L;
/*     */ 
/*     */   public RequestingUserName(String paramString, Locale paramLocale)
/*     */   {
/*  73 */     super(paramString, paramLocale);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  99 */     return (super.equals(paramObject)) && ((paramObject instanceof RequestingUserName));
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/* 114 */     return RequestingUserName.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 127 */     return "requesting-user-name";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.RequestingUserName
 * JD-Core Version:    0.6.2
 */