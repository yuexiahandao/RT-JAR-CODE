/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.SetOfIntegerSyntax;
/*     */ import javax.print.attribute.SupportedValuesAttribute;
/*     */ 
/*     */ public final class CopiesSupported extends SetOfIntegerSyntax
/*     */   implements SupportedValuesAttribute
/*     */ {
/*     */   private static final long serialVersionUID = 6927711687034846001L;
/*     */ 
/*     */   public CopiesSupported(int paramInt)
/*     */   {
/*  62 */     super(paramInt);
/*  63 */     if (paramInt < 1)
/*  64 */       throw new IllegalArgumentException("Copies value < 1 specified");
/*     */   }
/*     */ 
/*     */   public CopiesSupported(int paramInt1, int paramInt2)
/*     */   {
/*  82 */     super(paramInt1, paramInt2);
/*     */ 
/*  84 */     if (paramInt1 > paramInt2)
/*  85 */       throw new IllegalArgumentException("Null range specified");
/*  86 */     if (paramInt1 < 1)
/*  87 */       throw new IllegalArgumentException("Copies value < 1 specified");
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 111 */     return (super.equals(paramObject)) && ((paramObject instanceof CopiesSupported));
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/* 125 */     return CopiesSupported.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 138 */     return "copies-supported";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.CopiesSupported
 * JD-Core Version:    0.6.2
 */