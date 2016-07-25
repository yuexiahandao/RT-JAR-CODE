/*     */ package javax.swing;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class SpinnerNumberModel extends AbstractSpinnerModel
/*     */   implements Serializable
/*     */ {
/*     */   private Number stepSize;
/*     */   private Number value;
/*     */   private Comparable minimum;
/*     */   private Comparable maximum;
/*     */ 
/*     */   public SpinnerNumberModel(Number paramNumber1, Comparable paramComparable1, Comparable paramComparable2, Number paramNumber2)
/*     */   {
/* 120 */     if ((paramNumber1 == null) || (paramNumber2 == null)) {
/* 121 */       throw new IllegalArgumentException("value and stepSize must be non-null");
/*     */     }
/* 123 */     if (((paramComparable1 != null) && (paramComparable1.compareTo(paramNumber1) > 0)) || ((paramComparable2 != null) && (paramComparable2.compareTo(paramNumber1) < 0)))
/*     */     {
/* 125 */       throw new IllegalArgumentException("(minimum <= value <= maximum) is false");
/*     */     }
/* 127 */     this.value = paramNumber1;
/* 128 */     this.minimum = paramComparable1;
/* 129 */     this.maximum = paramComparable2;
/* 130 */     this.stepSize = paramNumber2;
/*     */   }
/*     */ 
/*     */   public SpinnerNumberModel(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 147 */     this(Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), Integer.valueOf(paramInt3), Integer.valueOf(paramInt4));
/*     */   }
/*     */ 
/*     */   public SpinnerNumberModel(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
/*     */   {
/* 164 */     this(new Double(paramDouble1), new Double(paramDouble2), new Double(paramDouble3), new Double(paramDouble4));
/*     */   }
/*     */ 
/*     */   public SpinnerNumberModel()
/*     */   {
/* 174 */     this(Integer.valueOf(0), null, null, Integer.valueOf(1));
/*     */   }
/*     */ 
/*     */   public void setMinimum(Comparable paramComparable)
/*     */   {
/* 215 */     if (paramComparable == null ? this.minimum != null : !paramComparable.equals(this.minimum)) {
/* 216 */       this.minimum = paramComparable;
/* 217 */       fireStateChanged();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Comparable getMinimum()
/*     */   {
/* 229 */     return this.minimum;
/*     */   }
/*     */ 
/*     */   public void setMaximum(Comparable paramComparable)
/*     */   {
/* 262 */     if (paramComparable == null ? this.maximum != null : !paramComparable.equals(this.maximum)) {
/* 263 */       this.maximum = paramComparable;
/* 264 */       fireStateChanged();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Comparable getMaximum()
/*     */   {
/* 276 */     return this.maximum;
/*     */   }
/*     */ 
/*     */   public void setStepSize(Number paramNumber)
/*     */   {
/* 297 */     if (paramNumber == null) {
/* 298 */       throw new IllegalArgumentException("null stepSize");
/*     */     }
/* 300 */     if (!paramNumber.equals(this.stepSize)) {
/* 301 */       this.stepSize = paramNumber;
/* 302 */       fireStateChanged();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Number getStepSize()
/*     */   {
/* 316 */     return this.stepSize;
/*     */   }
/*     */ 
/*     */   private Number incrValue(int paramInt)
/*     */   {
/*     */     Object localObject;
/* 323 */     if (((this.value instanceof Float)) || ((this.value instanceof Double))) {
/* 324 */       double d = this.value.doubleValue() + this.stepSize.doubleValue() * paramInt;
/* 325 */       if ((this.value instanceof Double)) {
/* 326 */         localObject = new Double(d);
/*     */       }
/*     */       else
/* 329 */         localObject = new Float(d);
/*     */     }
/*     */     else
/*     */     {
/* 333 */       long l = this.value.longValue() + this.stepSize.longValue() * paramInt;
/*     */ 
/* 335 */       if ((this.value instanceof Long)) {
/* 336 */         localObject = Long.valueOf(l);
/*     */       }
/* 338 */       else if ((this.value instanceof Integer)) {
/* 339 */         localObject = Integer.valueOf((int)l);
/*     */       }
/* 341 */       else if ((this.value instanceof Short)) {
/* 342 */         localObject = Short.valueOf((short)(int)l);
/*     */       }
/*     */       else {
/* 345 */         localObject = Byte.valueOf((byte)(int)l);
/*     */       }
/*     */     }
/*     */ 
/* 349 */     if ((this.maximum != null) && (this.maximum.compareTo(localObject) < 0)) {
/* 350 */       return null;
/*     */     }
/* 352 */     if ((this.minimum != null) && (this.minimum.compareTo(localObject) > 0)) {
/* 353 */       return null;
/*     */     }
/*     */ 
/* 356 */     return localObject;
/*     */   }
/*     */ 
/*     */   public Object getNextValue()
/*     */   {
/* 372 */     return incrValue(1);
/*     */   }
/*     */ 
/*     */   public Object getPreviousValue()
/*     */   {
/* 388 */     return incrValue(-1);
/*     */   }
/*     */ 
/*     */   public Number getNumber()
/*     */   {
/* 399 */     return this.value;
/*     */   }
/*     */ 
/*     */   public Object getValue()
/*     */   {
/* 411 */     return this.value;
/*     */   }
/*     */ 
/*     */   public void setValue(Object paramObject)
/*     */   {
/* 442 */     if ((paramObject == null) || (!(paramObject instanceof Number))) {
/* 443 */       throw new IllegalArgumentException("illegal value");
/*     */     }
/* 445 */     if (!paramObject.equals(this.value)) {
/* 446 */       this.value = ((Number)paramObject);
/* 447 */       fireStateChanged();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.SpinnerNumberModel
 * JD-Core Version:    0.6.2
 */