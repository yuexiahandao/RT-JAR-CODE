/*     */ package sun.management.counter;
/*     */ 
/*     */ public abstract class AbstractCounter
/*     */   implements Counter
/*     */ {
/*     */   String name;
/*     */   Units units;
/*     */   Variability variability;
/*     */   int flags;
/*     */   int vectorLength;
/*     */   private static final long serialVersionUID = 6992337162326171013L;
/*     */ 
/*     */   protected AbstractCounter(String paramString, Units paramUnits, Variability paramVariability, int paramInt1, int paramInt2)
/*     */   {
/*  50 */     this.name = paramString;
/*  51 */     this.units = paramUnits;
/*  52 */     this.variability = paramVariability;
/*  53 */     this.flags = paramInt1;
/*  54 */     this.vectorLength = paramInt2;
/*     */   }
/*     */ 
/*     */   protected AbstractCounter(String paramString, Units paramUnits, Variability paramVariability, int paramInt)
/*     */   {
/*  59 */     this(paramString, paramUnits, paramVariability, paramInt, 0);
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/*  66 */     return this.name;
/*     */   }
/*     */ 
/*     */   public Units getUnits()
/*     */   {
/*  73 */     return this.units;
/*     */   }
/*     */ 
/*     */   public Variability getVariability()
/*     */   {
/*  80 */     return this.variability;
/*     */   }
/*     */ 
/*     */   public boolean isVector()
/*     */   {
/*  87 */     return this.vectorLength > 0;
/*     */   }
/*     */ 
/*     */   public int getVectorLength()
/*     */   {
/*  94 */     return this.vectorLength;
/*     */   }
/*     */ 
/*     */   public boolean isInternal() {
/*  98 */     return (this.flags & 0x1) == 0;
/*     */   }
/*     */ 
/*     */   public int getFlags()
/*     */   {
/* 105 */     return this.flags;
/*     */   }
/*     */ 
/*     */   public abstract Object getValue();
/*     */ 
/*     */   public String toString() {
/* 111 */     String str = getName() + ": " + getValue() + " " + getUnits();
/* 112 */     if (isInternal()) {
/* 113 */       return str + " [INTERNAL]";
/*     */     }
/* 115 */     return str;
/*     */   }
/*     */ 
/*     */   class Flags
/*     */   {
/*     */     static final int SUPPORTED = 1;
/*     */ 
/*     */     Flags()
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.counter.AbstractCounter
 * JD-Core Version:    0.6.2
 */