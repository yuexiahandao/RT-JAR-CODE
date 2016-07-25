/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.DocAttribute;
/*     */ import javax.print.attribute.PrintJobAttribute;
/*     */ import javax.print.attribute.PrintRequestAttribute;
/*     */ import javax.print.attribute.SetOfIntegerSyntax;
/*     */ 
/*     */ public final class PageRanges extends SetOfIntegerSyntax
/*     */   implements DocAttribute, PrintRequestAttribute, PrintJobAttribute
/*     */ {
/*     */   private static final long serialVersionUID = 8639895197656148392L;
/*     */ 
/*     */   public PageRanges(int[][] paramArrayOfInt)
/*     */   {
/* 132 */     super(paramArrayOfInt);
/* 133 */     if (paramArrayOfInt == null) {
/* 134 */       throw new NullPointerException("members is null");
/*     */     }
/* 136 */     myPageRanges();
/*     */   }
/*     */ 
/*     */   public PageRanges(String paramString)
/*     */   {
/* 158 */     super(paramString);
/* 159 */     if (paramString == null) {
/* 160 */       throw new NullPointerException("members is null");
/*     */     }
/* 162 */     myPageRanges();
/*     */   }
/*     */ 
/*     */   private void myPageRanges() {
/* 166 */     int[][] arrayOfInt = getMembers();
/* 167 */     int i = arrayOfInt.length;
/* 168 */     if (i == 0) {
/* 169 */       throw new IllegalArgumentException("members is zero-length");
/*     */     }
/*     */ 
/* 172 */     for (int j = 0; j < i; j++)
/* 173 */       if (arrayOfInt[j][0] < 1)
/* 174 */         throw new IllegalArgumentException("Page value < 1 specified");
/*     */   }
/*     */ 
/*     */   public PageRanges(int paramInt)
/*     */   {
/* 190 */     super(paramInt);
/* 191 */     if (paramInt < 1)
/* 192 */       throw new IllegalArgumentException("Page value < 1 specified");
/*     */   }
/*     */ 
/*     */   public PageRanges(int paramInt1, int paramInt2)
/*     */   {
/* 209 */     super(paramInt1, paramInt2);
/* 210 */     if (paramInt1 > paramInt2)
/* 211 */       throw new IllegalArgumentException("Null range specified");
/* 212 */     if (paramInt1 < 1)
/* 213 */       throw new IllegalArgumentException("Page value < 1 specified");
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 237 */     return (super.equals(paramObject)) && ((paramObject instanceof PageRanges));
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/* 250 */     return PageRanges.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 262 */     return "page-ranges";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.PageRanges
 * JD-Core Version:    0.6.2
 */