/*     */ package sun.management.counter;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class Units
/*     */   implements Serializable
/*     */ {
/*     */   private static final int NUNITS = 8;
/*  43 */   private static Units[] map = new Units[8];
/*     */   private final String name;
/*     */   private final int value;
/*  51 */   public static final Units INVALID = new Units("Invalid", 0);
/*     */ 
/*  56 */   public static final Units NONE = new Units("None", 1);
/*     */ 
/*  61 */   public static final Units BYTES = new Units("Bytes", 2);
/*     */ 
/*  66 */   public static final Units TICKS = new Units("Ticks", 3);
/*     */ 
/*  71 */   public static final Units EVENTS = new Units("Events", 4);
/*     */ 
/*  78 */   public static final Units STRING = new Units("String", 5);
/*     */ 
/*  83 */   public static final Units HERTZ = new Units("Hertz", 6);
/*     */   private static final long serialVersionUID = 6992337162326171013L;
/*     */ 
/*     */   public String toString()
/*     */   {
/*  91 */     return this.name;
/*     */   }
/*     */ 
/*     */   public int intValue()
/*     */   {
/* 100 */     return this.value;
/*     */   }
/*     */ 
/*     */   public static Units toUnits(int paramInt)
/*     */   {
/* 114 */     if ((paramInt < 0) || (paramInt >= map.length) || (map[paramInt] == null)) {
/* 115 */       return INVALID;
/*     */     }
/*     */ 
/* 118 */     return map[paramInt];
/*     */   }
/*     */ 
/*     */   private Units(String paramString, int paramInt) {
/* 122 */     this.name = paramString;
/* 123 */     this.value = paramInt;
/* 124 */     map[paramInt] = this;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.counter.Units
 * JD-Core Version:    0.6.2
 */