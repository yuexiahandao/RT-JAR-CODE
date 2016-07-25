/*     */ package javax.print.attribute;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public abstract class IntegerSyntax
/*     */   implements Serializable, Cloneable
/*     */ {
/*     */   private static final long serialVersionUID = 3644574816328081943L;
/*     */   private int value;
/*     */ 
/*     */   protected IntegerSyntax(int paramInt)
/*     */   {
/*  61 */     this.value = paramInt;
/*     */   }
/*     */ 
/*     */   protected IntegerSyntax(int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/*  78 */     if ((paramInt2 > paramInt1) || (paramInt1 > paramInt3)) {
/*  79 */       throw new IllegalArgumentException("Value " + paramInt1 + " not in range " + paramInt2 + ".." + paramInt3);
/*     */     }
/*     */ 
/*  83 */     this.value = paramInt1;
/*     */   }
/*     */ 
/*     */   public int getValue()
/*     */   {
/*  91 */     return this.value;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 114 */     return (paramObject != null) && ((paramObject instanceof IntegerSyntax)) && (this.value == ((IntegerSyntax)paramObject).value);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 123 */     return this.value;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 132 */     return "" + this.value;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.IntegerSyntax
 * JD-Core Version:    0.6.2
 */