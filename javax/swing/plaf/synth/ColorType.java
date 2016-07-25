/*     */ package javax.swing.plaf.synth;
/*     */ 
/*     */ public class ColorType
/*     */ {
/*  67 */   public static final ColorType FOREGROUND = new ColorType("Foreground");
/*     */ 
/*  72 */   public static final ColorType BACKGROUND = new ColorType("Background");
/*     */ 
/*  77 */   public static final ColorType TEXT_FOREGROUND = new ColorType("TextForeground");
/*     */ 
/*  83 */   public static final ColorType TEXT_BACKGROUND = new ColorType("TextBackground");
/*     */ 
/*  89 */   public static final ColorType FOCUS = new ColorType("Focus");
/*     */ 
/* 102 */   public static final int MAX_COUNT = Math.max(FOREGROUND.getID(), Math.max(BACKGROUND.getID(), FOCUS.getID())) + 1;
/*     */   private static int nextID;
/*     */   private String description;
/*     */   private int index;
/*     */ 
/*     */   protected ColorType(String paramString)
/*     */   {
/* 112 */     if (paramString == null) {
/* 113 */       throw new NullPointerException("ColorType must have a valid description");
/*     */     }
/*     */ 
/* 116 */     this.description = paramString;
/* 117 */     synchronized (ColorType.class) {
/* 118 */       this.index = (nextID++);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final int getID()
/*     */   {
/* 128 */     return this.index;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 139 */     return this.description;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.synth.ColorType
 * JD-Core Version:    0.6.2
 */