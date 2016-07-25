/*     */ package sun.management.counter;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class Variability
/*     */   implements Serializable
/*     */ {
/*     */   private static final int NATTRIBUTES = 4;
/*  42 */   private static Variability[] map = new Variability[4];
/*     */   private String name;
/*     */   private int value;
/*  50 */   public static final Variability INVALID = new Variability("Invalid", 0);
/*     */ 
/*  55 */   public static final Variability CONSTANT = new Variability("Constant", 1);
/*     */ 
/*  60 */   public static final Variability MONOTONIC = new Variability("Monotonic", 2);
/*     */ 
/*  65 */   public static final Variability VARIABLE = new Variability("Variable", 3);
/*     */   private static final long serialVersionUID = 6992337162326171013L;
/*     */ 
/*     */   public String toString()
/*     */   {
/*  73 */     return this.name;
/*     */   }
/*     */ 
/*     */   public int intValue()
/*     */   {
/*  82 */     return this.value;
/*     */   }
/*     */ 
/*     */   public static Variability toVariability(int paramInt)
/*     */   {
/*  97 */     if ((paramInt < 0) || (paramInt >= map.length) || (map[paramInt] == null)) {
/*  98 */       return INVALID;
/*     */     }
/*     */ 
/* 101 */     return map[paramInt];
/*     */   }
/*     */ 
/*     */   private Variability(String paramString, int paramInt) {
/* 105 */     this.name = paramString;
/* 106 */     this.value = paramInt;
/* 107 */     map[paramInt] = this;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.counter.Variability
 * JD-Core Version:    0.6.2
 */