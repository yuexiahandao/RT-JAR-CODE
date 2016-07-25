/*     */ package javax.swing.plaf.synth;
/*     */ 
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Rectangle;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.text.View;
/*     */ import sun.swing.MenuItemLayoutHelper;
/*     */ import sun.swing.MenuItemLayoutHelper.ColumnAlignment;
/*     */ import sun.swing.MenuItemLayoutHelper.LayoutResult;
/*     */ import sun.swing.MenuItemLayoutHelper.RectSize;
/*     */ import sun.swing.StringUIClientPropertyKey;
/*     */ import sun.swing.plaf.synth.SynthIcon;
/*     */ 
/*     */ class SynthMenuItemLayoutHelper extends MenuItemLayoutHelper
/*     */ {
/*  62 */   public static final StringUIClientPropertyKey MAX_ACC_OR_ARROW_WIDTH = new StringUIClientPropertyKey("maxAccOrArrowWidth");
/*     */ 
/*  65 */   public static final MenuItemLayoutHelper.ColumnAlignment LTR_ALIGNMENT_1 = new MenuItemLayoutHelper.ColumnAlignment(2, 2, 2, 4, 4);
/*     */ 
/*  73 */   public static final MenuItemLayoutHelper.ColumnAlignment LTR_ALIGNMENT_2 = new MenuItemLayoutHelper.ColumnAlignment(2, 2, 2, 2, 4);
/*     */ 
/*  81 */   public static final MenuItemLayoutHelper.ColumnAlignment RTL_ALIGNMENT_1 = new MenuItemLayoutHelper.ColumnAlignment(4, 4, 4, 2, 2);
/*     */ 
/*  89 */   public static final MenuItemLayoutHelper.ColumnAlignment RTL_ALIGNMENT_2 = new MenuItemLayoutHelper.ColumnAlignment(4, 4, 4, 4, 2);
/*     */   private SynthContext context;
/*     */   private SynthContext accContext;
/*     */   private SynthStyle style;
/*     */   private SynthStyle accStyle;
/*     */   private SynthGraphicsUtils gu;
/*     */   private SynthGraphicsUtils accGu;
/*     */   private boolean alignAcceleratorText;
/*     */   private int maxAccOrArrowWidth;
/*     */ 
/*     */   public SynthMenuItemLayoutHelper(SynthContext paramSynthContext1, SynthContext paramSynthContext2, JMenuItem paramJMenuItem, Icon paramIcon1, Icon paramIcon2, Rectangle paramRectangle, int paramInt, String paramString1, boolean paramBoolean1, boolean paramBoolean2, String paramString2)
/*     */   {
/* 112 */     this.context = paramSynthContext1;
/* 113 */     this.accContext = paramSynthContext2;
/* 114 */     this.style = paramSynthContext1.getStyle();
/* 115 */     this.accStyle = paramSynthContext2.getStyle();
/* 116 */     this.gu = this.style.getGraphicsUtils(paramSynthContext1);
/* 117 */     this.accGu = this.accStyle.getGraphicsUtils(paramSynthContext2);
/* 118 */     this.alignAcceleratorText = getAlignAcceleratorText(paramString2);
/* 119 */     reset(paramJMenuItem, paramIcon1, paramIcon2, paramRectangle, paramInt, paramString1, paramBoolean1, this.style.getFont(paramSynthContext1), this.accStyle.getFont(paramSynthContext2), paramBoolean2, paramString2);
/*     */ 
/* 122 */     setLeadingGap(0);
/*     */   }
/*     */ 
/*     */   private boolean getAlignAcceleratorText(String paramString) {
/* 126 */     return this.style.getBoolean(this.context, paramString + ".alignAcceleratorText", true);
/*     */   }
/*     */ 
/*     */   protected void calcWidthsAndHeights()
/*     */   {
/* 132 */     if (getIcon() != null) {
/* 133 */       getIconSize().setWidth(SynthIcon.getIconWidth(getIcon(), this.context));
/* 134 */       getIconSize().setHeight(SynthIcon.getIconHeight(getIcon(), this.context));
/*     */     }
/*     */ 
/* 138 */     if (!getAccText().equals("")) {
/* 139 */       getAccSize().setWidth(this.accGu.computeStringWidth(getAccContext(), getAccFontMetrics().getFont(), getAccFontMetrics(), getAccText()));
/*     */ 
/* 142 */       getAccSize().setHeight(getAccFontMetrics().getHeight());
/*     */     }
/*     */ 
/* 146 */     if (getText() == null)
/* 147 */       setText("");
/* 148 */     else if (!getText().equals("")) {
/* 149 */       if (getHtmlView() != null)
/*     */       {
/* 151 */         getTextSize().setWidth((int)getHtmlView().getPreferredSpan(0));
/*     */ 
/* 153 */         getTextSize().setHeight((int)getHtmlView().getPreferredSpan(1));
/*     */       }
/*     */       else
/*     */       {
/* 157 */         getTextSize().setWidth(this.gu.computeStringWidth(this.context, getFontMetrics().getFont(), getFontMetrics(), getText()));
/*     */ 
/* 160 */         getTextSize().setHeight(getFontMetrics().getHeight());
/*     */       }
/*     */     }
/*     */ 
/* 164 */     if (useCheckAndArrow())
/*     */     {
/* 166 */       if (getCheckIcon() != null) {
/* 167 */         getCheckSize().setWidth(SynthIcon.getIconWidth(getCheckIcon(), this.context));
/*     */ 
/* 169 */         getCheckSize().setHeight(SynthIcon.getIconHeight(getCheckIcon(), this.context));
/*     */       }
/*     */ 
/* 173 */       if (getArrowIcon() != null) {
/* 174 */         getArrowSize().setWidth(SynthIcon.getIconWidth(getArrowIcon(), this.context));
/*     */ 
/* 176 */         getArrowSize().setHeight(SynthIcon.getIconHeight(getArrowIcon(), this.context));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 182 */     if (isColumnLayout()) {
/* 183 */       getLabelSize().setWidth(getIconSize().getWidth() + getTextSize().getWidth() + getGap());
/*     */ 
/* 185 */       getLabelSize().setHeight(MenuItemLayoutHelper.max(new int[] { getCheckSize().getHeight(), getIconSize().getHeight(), getTextSize().getHeight(), getAccSize().getHeight(), getArrowSize().getHeight() }));
/*     */     }
/*     */     else
/*     */     {
/* 192 */       Rectangle localRectangle1 = new Rectangle();
/* 193 */       Rectangle localRectangle2 = new Rectangle();
/* 194 */       this.gu.layoutText(this.context, getFontMetrics(), getText(), getIcon(), getHorizontalAlignment(), getVerticalAlignment(), getHorizontalTextPosition(), getVerticalTextPosition(), getViewRect(), localRectangle2, localRectangle1, getGap());
/*     */ 
/* 198 */       localRectangle1.width += getLeftTextExtraWidth();
/* 199 */       Rectangle localRectangle3 = localRectangle2.union(localRectangle1);
/* 200 */       getLabelSize().setHeight(localRectangle3.height);
/* 201 */       getLabelSize().setWidth(localRectangle3.width);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void calcMaxWidths() {
/* 206 */     calcMaxWidth(getCheckSize(), MAX_CHECK_WIDTH);
/* 207 */     this.maxAccOrArrowWidth = calcMaxValue(MAX_ACC_OR_ARROW_WIDTH, getArrowSize().getWidth());
/*     */ 
/* 209 */     this.maxAccOrArrowWidth = calcMaxValue(MAX_ACC_OR_ARROW_WIDTH, getAccSize().getWidth());
/*     */     int i;
/* 212 */     if (isColumnLayout()) {
/* 213 */       calcMaxWidth(getIconSize(), MAX_ICON_WIDTH);
/* 214 */       calcMaxWidth(getTextSize(), MAX_TEXT_WIDTH);
/* 215 */       i = getGap();
/* 216 */       if ((getIconSize().getMaxWidth() == 0) || (getTextSize().getMaxWidth() == 0))
/*     */       {
/* 218 */         i = 0;
/*     */       }
/* 220 */       getLabelSize().setMaxWidth(calcMaxValue(MAX_LABEL_WIDTH, getIconSize().getMaxWidth() + getTextSize().getMaxWidth() + i));
/*     */     }
/*     */     else
/*     */     {
/* 226 */       getIconSize().setMaxWidth(getParentIntProperty(MAX_ICON_WIDTH));
/*     */ 
/* 228 */       calcMaxWidth(getLabelSize(), MAX_LABEL_WIDTH);
/*     */ 
/* 232 */       i = getLabelSize().getMaxWidth() - getIconSize().getMaxWidth();
/*     */ 
/* 234 */       if (getIconSize().getMaxWidth() > 0) {
/* 235 */         i -= getGap();
/*     */       }
/* 237 */       getTextSize().setMaxWidth(calcMaxValue(MAX_TEXT_WIDTH, i));
/*     */     }
/*     */   }
/*     */ 
/*     */   public SynthContext getContext()
/*     */   {
/* 243 */     return this.context;
/*     */   }
/*     */ 
/*     */   public SynthContext getAccContext() {
/* 247 */     return this.accContext;
/*     */   }
/*     */ 
/*     */   public SynthStyle getStyle() {
/* 251 */     return this.style;
/*     */   }
/*     */ 
/*     */   public SynthStyle getAccStyle() {
/* 255 */     return this.accStyle;
/*     */   }
/*     */ 
/*     */   public SynthGraphicsUtils getGraphicsUtils() {
/* 259 */     return this.gu;
/*     */   }
/*     */ 
/*     */   public SynthGraphicsUtils getAccGraphicsUtils() {
/* 263 */     return this.accGu;
/*     */   }
/*     */ 
/*     */   public boolean alignAcceleratorText() {
/* 267 */     return this.alignAcceleratorText;
/*     */   }
/*     */ 
/*     */   public int getMaxAccOrArrowWidth() {
/* 271 */     return this.maxAccOrArrowWidth;
/*     */   }
/*     */ 
/*     */   protected void prepareForLayout(MenuItemLayoutHelper.LayoutResult paramLayoutResult) {
/* 275 */     paramLayoutResult.getCheckRect().width = getCheckSize().getMaxWidth();
/*     */ 
/* 277 */     if ((useCheckAndArrow()) && (!"".equals(getAccText())))
/* 278 */       paramLayoutResult.getAccRect().width = this.maxAccOrArrowWidth;
/*     */     else
/* 280 */       paramLayoutResult.getArrowRect().width = this.maxAccOrArrowWidth;
/*     */   }
/*     */ 
/*     */   public MenuItemLayoutHelper.ColumnAlignment getLTRColumnAlignment()
/*     */   {
/* 285 */     if (alignAcceleratorText()) {
/* 286 */       return LTR_ALIGNMENT_2;
/*     */     }
/* 288 */     return LTR_ALIGNMENT_1;
/*     */   }
/*     */ 
/*     */   public MenuItemLayoutHelper.ColumnAlignment getRTLColumnAlignment()
/*     */   {
/* 293 */     if (alignAcceleratorText()) {
/* 294 */       return RTL_ALIGNMENT_2;
/*     */     }
/* 296 */     return RTL_ALIGNMENT_1;
/*     */   }
/*     */ 
/*     */   protected void layoutIconAndTextInLabelRect(MenuItemLayoutHelper.LayoutResult paramLayoutResult)
/*     */   {
/* 301 */     paramLayoutResult.setTextRect(new Rectangle());
/* 302 */     paramLayoutResult.setIconRect(new Rectangle());
/* 303 */     this.gu.layoutText(this.context, getFontMetrics(), getText(), getIcon(), getHorizontalAlignment(), getVerticalAlignment(), getHorizontalTextPosition(), getVerticalTextPosition(), paramLayoutResult.getLabelRect(), paramLayoutResult.getIconRect(), paramLayoutResult.getTextRect(), getGap());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.synth.SynthMenuItemLayoutHelper
 * JD-Core Version:    0.6.2
 */