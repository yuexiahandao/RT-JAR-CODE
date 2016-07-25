/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.DocAttribute;
/*     */ import javax.print.attribute.TextSyntax;
/*     */ 
/*     */ public final class DocumentName extends TextSyntax
/*     */   implements DocAttribute
/*     */ {
/*     */   private static final long serialVersionUID = 7883105848533280430L;
/*     */ 
/*     */   public DocumentName(String paramString, Locale paramLocale)
/*     */   {
/*  67 */     super(paramString, paramLocale);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  93 */     return (super.equals(paramObject)) && ((paramObject instanceof DocumentName));
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/* 106 */     return DocumentName.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 118 */     return "document-name";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.DocumentName
 * JD-Core Version:    0.6.2
 */