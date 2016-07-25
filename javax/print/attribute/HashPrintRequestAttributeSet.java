/*     */ package javax.print.attribute;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class HashPrintRequestAttributeSet extends HashAttributeSet
/*     */   implements PrintRequestAttributeSet, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 2364756266107751933L;
/*     */ 
/*     */   public HashPrintRequestAttributeSet()
/*     */   {
/*  49 */     super(PrintRequestAttribute.class);
/*     */   }
/*     */ 
/*     */   public HashPrintRequestAttributeSet(PrintRequestAttribute paramPrintRequestAttribute)
/*     */   {
/*  62 */     super(paramPrintRequestAttribute, PrintRequestAttribute.class);
/*     */   }
/*     */ 
/*     */   public HashPrintRequestAttributeSet(PrintRequestAttribute[] paramArrayOfPrintRequestAttribute)
/*     */   {
/*  81 */     super(paramArrayOfPrintRequestAttribute, PrintRequestAttribute.class);
/*     */   }
/*     */ 
/*     */   public HashPrintRequestAttributeSet(PrintRequestAttributeSet paramPrintRequestAttributeSet)
/*     */   {
/* 100 */     super(paramPrintRequestAttributeSet, PrintRequestAttribute.class);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.HashPrintRequestAttributeSet
 * JD-Core Version:    0.6.2
 */