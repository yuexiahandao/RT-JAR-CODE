/*     */ package java.lang;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public abstract class Number
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -8742448824652078965L;
/*     */ 
/*     */   public abstract int intValue();
/*     */ 
/*     */   public abstract long longValue();
/*     */ 
/*     */   public abstract float floatValue();
/*     */ 
/*     */   public abstract double doubleValue();
/*     */ 
/*     */   public byte byteValue()
/*     */   {
/*  95 */     return (byte)intValue();
/*     */   }
/*     */ 
/*     */   public short shortValue()
/*     */   {
/* 107 */     return (short)intValue();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.Number
 * JD-Core Version:    0.6.2
 */