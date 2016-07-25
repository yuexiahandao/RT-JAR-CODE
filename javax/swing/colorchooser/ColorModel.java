/*     */ package javax.swing.colorchooser;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import javax.swing.UIManager;
/*     */ 
/*     */ class ColorModel
/*     */ {
/*     */   private final String prefix;
/*     */   private final String[] labels;
/*     */ 
/*     */   ColorModel(String paramString, String[] paramArrayOfString)
/*     */   {
/*  37 */     this.prefix = ("ColorChooser." + paramString);
/*  38 */     this.labels = paramArrayOfString;
/*     */   }
/*     */ 
/*     */   ColorModel() {
/*  42 */     this("rgb", new String[] { "Red", "Green", "Blue", "Alpha" });
/*     */   }
/*     */ 
/*     */   void setColor(int paramInt, float[] paramArrayOfFloat) {
/*  46 */     paramArrayOfFloat[0] = normalize(paramInt >> 16);
/*  47 */     paramArrayOfFloat[1] = normalize(paramInt >> 8);
/*  48 */     paramArrayOfFloat[2] = normalize(paramInt);
/*  49 */     paramArrayOfFloat[3] = normalize(paramInt >> 24);
/*     */   }
/*     */ 
/*     */   int getColor(float[] paramArrayOfFloat) {
/*  53 */     return to8bit(paramArrayOfFloat[2]) | to8bit(paramArrayOfFloat[1]) << 8 | to8bit(paramArrayOfFloat[0]) << 16 | to8bit(paramArrayOfFloat[3]) << 24;
/*     */   }
/*     */ 
/*     */   int getCount() {
/*  57 */     return this.labels.length;
/*     */   }
/*     */ 
/*     */   int getMinimum(int paramInt) {
/*  61 */     return 0;
/*     */   }
/*     */ 
/*     */   int getMaximum(int paramInt) {
/*  65 */     return 255;
/*     */   }
/*     */ 
/*     */   float getDefault(int paramInt) {
/*  69 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   final String getLabel(Component paramComponent, int paramInt) {
/*  73 */     return getText(paramComponent, this.labels[paramInt]);
/*     */   }
/*     */ 
/*     */   private static float normalize(int paramInt) {
/*  77 */     return (paramInt & 0xFF) / 255.0F;
/*     */   }
/*     */ 
/*     */   private static int to8bit(float paramFloat) {
/*  81 */     return (int)(255.0F * paramFloat);
/*     */   }
/*     */ 
/*     */   final String getText(Component paramComponent, String paramString) {
/*  85 */     return UIManager.getString(this.prefix + paramString + "Text", paramComponent.getLocale());
/*     */   }
/*     */ 
/*     */   final int getInteger(Component paramComponent, String paramString) {
/*  89 */     Object localObject = UIManager.get(this.prefix + paramString, paramComponent.getLocale());
/*  90 */     if ((localObject instanceof Integer)) {
/*  91 */       return ((Integer)localObject).intValue();
/*     */     }
/*  93 */     if ((localObject instanceof String))
/*     */       try {
/*  95 */         return Integer.parseInt((String)localObject);
/*     */       }
/*     */       catch (NumberFormatException localNumberFormatException)
/*     */       {
/*     */       }
/* 100 */     return -1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.colorchooser.ColorModel
 * JD-Core Version:    0.6.2
 */