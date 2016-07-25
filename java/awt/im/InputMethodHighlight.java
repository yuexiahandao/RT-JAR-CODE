/*     */ package java.awt.im;
/*     */ 
/*     */ import java.awt.font.TextAttribute;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class InputMethodHighlight
/*     */ {
/*     */   public static final int RAW_TEXT = 0;
/*     */   public static final int CONVERTED_TEXT = 1;
/*  85 */   public static final InputMethodHighlight UNSELECTED_RAW_TEXT_HIGHLIGHT = new InputMethodHighlight(false, 0);
/*     */ 
/*  91 */   public static final InputMethodHighlight SELECTED_RAW_TEXT_HIGHLIGHT = new InputMethodHighlight(true, 0);
/*     */ 
/*  97 */   public static final InputMethodHighlight UNSELECTED_CONVERTED_TEXT_HIGHLIGHT = new InputMethodHighlight(false, 1);
/*     */ 
/* 103 */   public static final InputMethodHighlight SELECTED_CONVERTED_TEXT_HIGHLIGHT = new InputMethodHighlight(true, 1);
/*     */   private boolean selected;
/*     */   private int state;
/*     */   private int variation;
/*     */   private Map style;
/*     */ 
/*     */   public InputMethodHighlight(boolean paramBoolean, int paramInt)
/*     */   {
/* 117 */     this(paramBoolean, paramInt, 0, null);
/*     */   }
/*     */ 
/*     */   public InputMethodHighlight(boolean paramBoolean, int paramInt1, int paramInt2)
/*     */   {
/* 131 */     this(paramBoolean, paramInt1, paramInt2, null);
/*     */   }
/*     */ 
/*     */   public InputMethodHighlight(boolean paramBoolean, int paramInt1, int paramInt2, Map<TextAttribute, ?> paramMap)
/*     */   {
/* 149 */     this.selected = paramBoolean;
/* 150 */     if ((paramInt1 != 0) && (paramInt1 != 1)) {
/* 151 */       throw new IllegalArgumentException("unknown input method highlight state");
/*     */     }
/* 153 */     this.state = paramInt1;
/* 154 */     this.variation = paramInt2;
/* 155 */     this.style = paramMap;
/*     */   }
/*     */ 
/*     */   public boolean isSelected()
/*     */   {
/* 162 */     return this.selected;
/*     */   }
/*     */ 
/*     */   public int getState()
/*     */   {
/* 172 */     return this.state;
/*     */   }
/*     */ 
/*     */   public int getVariation()
/*     */   {
/* 179 */     return this.variation;
/*     */   }
/*     */ 
/*     */   public Map<TextAttribute, ?> getStyle()
/*     */   {
/* 187 */     return this.style;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.im.InputMethodHighlight
 * JD-Core Version:    0.6.2
 */