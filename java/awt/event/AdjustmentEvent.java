/*     */ package java.awt.event;
/*     */ 
/*     */ import java.awt.AWTEvent;
/*     */ import java.awt.Adjustable;
/*     */ 
/*     */ public class AdjustmentEvent extends AWTEvent
/*     */ {
/*     */   public static final int ADJUSTMENT_FIRST = 601;
/*     */   public static final int ADJUSTMENT_LAST = 601;
/*     */   public static final int ADJUSTMENT_VALUE_CHANGED = 601;
/*     */   public static final int UNIT_INCREMENT = 1;
/*     */   public static final int UNIT_DECREMENT = 2;
/*     */   public static final int BLOCK_DECREMENT = 3;
/*     */   public static final int BLOCK_INCREMENT = 4;
/*     */   public static final int TRACK = 5;
/*     */   Adjustable adjustable;
/*     */   int value;
/*     */   int adjustmentType;
/*     */   boolean isAdjusting;
/*     */   private static final long serialVersionUID = 5700290645205279921L;
/*     */ 
/*     */   public AdjustmentEvent(Adjustable paramAdjustable, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 171 */     this(paramAdjustable, paramInt1, paramInt2, paramInt3, false);
/*     */   }
/*     */ 
/*     */   public AdjustmentEvent(Adjustable paramAdjustable, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean)
/*     */   {
/* 202 */     super(paramAdjustable, paramInt1);
/* 203 */     this.adjustable = paramAdjustable;
/* 204 */     this.adjustmentType = paramInt2;
/* 205 */     this.value = paramInt3;
/* 206 */     this.isAdjusting = paramBoolean;
/*     */   }
/*     */ 
/*     */   public Adjustable getAdjustable()
/*     */   {
/* 215 */     return this.adjustable;
/*     */   }
/*     */ 
/*     */   public int getValue()
/*     */   {
/* 224 */     return this.value;
/*     */   }
/*     */ 
/*     */   public int getAdjustmentType()
/*     */   {
/* 240 */     return this.adjustmentType;
/*     */   }
/*     */ 
/*     */   public boolean getValueIsAdjusting()
/*     */   {
/* 252 */     return this.isAdjusting;
/*     */   }
/*     */ 
/*     */   public String paramString()
/*     */   {
/*     */     String str1;
/* 257 */     switch (this.id) {
/*     */     case 601:
/* 259 */       str1 = "ADJUSTMENT_VALUE_CHANGED";
/* 260 */       break;
/*     */     default:
/* 262 */       str1 = "unknown type";
/*     */     }
/*     */     String str2;
/* 265 */     switch (this.adjustmentType) {
/*     */     case 1:
/* 267 */       str2 = "UNIT_INCREMENT";
/* 268 */       break;
/*     */     case 2:
/* 270 */       str2 = "UNIT_DECREMENT";
/* 271 */       break;
/*     */     case 4:
/* 273 */       str2 = "BLOCK_INCREMENT";
/* 274 */       break;
/*     */     case 3:
/* 276 */       str2 = "BLOCK_DECREMENT";
/* 277 */       break;
/*     */     case 5:
/* 279 */       str2 = "TRACK";
/* 280 */       break;
/*     */     default:
/* 282 */       str2 = "unknown type";
/*     */     }
/* 284 */     return str1 + ",adjType=" + str2 + ",value=" + this.value + ",isAdjusting=" + this.isAdjusting;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.event.AdjustmentEvent
 * JD-Core Version:    0.6.2
 */