/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.DocAttribute;
/*     */ import javax.print.attribute.EnumSyntax;
/*     */ import javax.print.attribute.PrintJobAttribute;
/*     */ import javax.print.attribute.PrintRequestAttribute;
/*     */ 
/*     */ public abstract class Media extends EnumSyntax
/*     */   implements DocAttribute, PrintRequestAttribute, PrintJobAttribute
/*     */ {
/*     */   private static final long serialVersionUID = -2823970704630722439L;
/*     */ 
/*     */   protected Media(int paramInt)
/*     */   {
/*  70 */     super(paramInt);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  91 */     return (paramObject != null) && ((paramObject instanceof Media)) && (paramObject.getClass() == getClass()) && (((Media)paramObject).getValue() == getValue());
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/* 107 */     return Media.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 120 */     return "media";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.Media
 * JD-Core Version:    0.6.2
 */