/*     */ package javax.swing.plaf.metal;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import javax.swing.ButtonModel;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ColorUIResource;
/*     */ import javax.swing.plaf.basic.BasicArrowButton;
/*     */ 
/*     */ public class MetalScrollButton extends BasicArrowButton
/*     */ {
/*     */   private static Color shadowColor;
/*     */   private static Color highlightColor;
/*  57 */   private boolean isFreeStanding = false;
/*     */   private int buttonWidth;
/*     */ 
/*     */   public MetalScrollButton(int paramInt1, int paramInt2, boolean paramBoolean)
/*     */   {
/*  63 */     super(paramInt1);
/*     */ 
/*  65 */     shadowColor = UIManager.getColor("ScrollBar.darkShadow");
/*  66 */     highlightColor = UIManager.getColor("ScrollBar.highlight");
/*     */ 
/*  68 */     this.buttonWidth = paramInt2;
/*  69 */     this.isFreeStanding = paramBoolean;
/*     */   }
/*     */ 
/*     */   public void setFreeStanding(boolean paramBoolean)
/*     */   {
/*  74 */     this.isFreeStanding = paramBoolean;
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics)
/*     */   {
/*  79 */     boolean bool1 = MetalUtils.isLeftToRight(this);
/*  80 */     boolean bool2 = getParent().isEnabled();
/*     */ 
/*  82 */     ColorUIResource localColorUIResource = bool2 ? MetalLookAndFeel.getControlInfo() : MetalLookAndFeel.getControlDisabled();
/*  83 */     boolean bool3 = getModel().isPressed();
/*  84 */     int i = getWidth();
/*  85 */     int j = getHeight();
/*  86 */     int k = i;
/*  87 */     int m = j;
/*  88 */     int n = (j + 1) / 4;
/*  89 */     int i1 = (j + 1) / 2;
/*     */ 
/*  91 */     if (bool3)
/*     */     {
/*  93 */       paramGraphics.setColor(MetalLookAndFeel.getControlShadow());
/*     */     }
/*     */     else
/*     */     {
/*  97 */       paramGraphics.setColor(getBackground());
/*     */     }
/*     */ 
/* 100 */     paramGraphics.fillRect(0, 0, i, j);
/*     */     int i2;
/*     */     int i3;
/*     */     int i4;
/* 102 */     if (getDirection() == 1)
/*     */     {
/* 104 */       if (!this.isFreeStanding) {
/* 105 */         j++;
/* 106 */         paramGraphics.translate(0, -1);
/* 107 */         i += 2;
/* 108 */         if (!bool1) {
/* 109 */           paramGraphics.translate(-1, 0);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 114 */       paramGraphics.setColor(localColorUIResource);
/* 115 */       i2 = (m + 1 - n) / 2;
/* 116 */       i3 = k / 2;
/*     */ 
/* 118 */       for (i4 = 0; i4 < n; i4++) {
/* 119 */         paramGraphics.drawLine(i3 - i4, i2 + i4, i3 + i4 + 1, i2 + i4);
/*     */       }
/*     */ 
/* 126 */       if (bool2) {
/* 127 */         paramGraphics.setColor(highlightColor);
/*     */ 
/* 129 */         if (!bool3)
/*     */         {
/* 131 */           paramGraphics.drawLine(1, 1, i - 3, 1);
/* 132 */           paramGraphics.drawLine(1, 1, 1, j - 1);
/*     */         }
/*     */ 
/* 135 */         paramGraphics.drawLine(i - 1, 1, i - 1, j - 1);
/*     */ 
/* 137 */         paramGraphics.setColor(shadowColor);
/* 138 */         paramGraphics.drawLine(0, 0, i - 2, 0);
/* 139 */         paramGraphics.drawLine(0, 0, 0, j - 1);
/* 140 */         paramGraphics.drawLine(i - 2, 2, i - 2, j - 1);
/*     */       } else {
/* 142 */         MetalUtils.drawDisabledBorder(paramGraphics, 0, 0, i, j + 1);
/*     */       }
/* 144 */       if (!this.isFreeStanding) {
/* 145 */         j--;
/* 146 */         paramGraphics.translate(0, 1);
/* 147 */         i -= 2;
/* 148 */         if (!bool1) {
/* 149 */           paramGraphics.translate(1, 0);
/*     */         }
/*     */       }
/*     */     }
/* 153 */     else if (getDirection() == 5)
/*     */     {
/* 155 */       if (!this.isFreeStanding) {
/* 156 */         j++;
/* 157 */         i += 2;
/* 158 */         if (!bool1) {
/* 159 */           paramGraphics.translate(-1, 0);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 164 */       paramGraphics.setColor(localColorUIResource);
/*     */ 
/* 166 */       i2 = (m + 1 - n) / 2 + n - 1;
/* 167 */       i3 = k / 2;
/*     */ 
/* 171 */       for (i4 = 0; i4 < n; i4++) {
/* 172 */         paramGraphics.drawLine(i3 - i4, i2 - i4, i3 + i4 + 1, i2 - i4);
/*     */       }
/*     */ 
/* 180 */       if (bool2) {
/* 181 */         paramGraphics.setColor(highlightColor);
/*     */ 
/* 183 */         if (!bool3)
/*     */         {
/* 185 */           paramGraphics.drawLine(1, 0, i - 3, 0);
/* 186 */           paramGraphics.drawLine(1, 0, 1, j - 3);
/*     */         }
/*     */ 
/* 189 */         paramGraphics.drawLine(1, j - 1, i - 1, j - 1);
/* 190 */         paramGraphics.drawLine(i - 1, 0, i - 1, j - 1);
/*     */ 
/* 192 */         paramGraphics.setColor(shadowColor);
/* 193 */         paramGraphics.drawLine(0, 0, 0, j - 2);
/* 194 */         paramGraphics.drawLine(i - 2, 0, i - 2, j - 2);
/* 195 */         paramGraphics.drawLine(2, j - 2, i - 2, j - 2);
/*     */       } else {
/* 197 */         MetalUtils.drawDisabledBorder(paramGraphics, 0, -1, i, j + 1);
/*     */       }
/*     */ 
/* 200 */       if (!this.isFreeStanding) {
/* 201 */         j--;
/* 202 */         i -= 2;
/* 203 */         if (!bool1) {
/* 204 */           paramGraphics.translate(1, 0);
/*     */         }
/*     */       }
/*     */     }
/* 208 */     else if (getDirection() == 3)
/*     */     {
/* 210 */       if (!this.isFreeStanding) {
/* 211 */         j += 2;
/* 212 */         i++;
/*     */       }
/*     */ 
/* 216 */       paramGraphics.setColor(localColorUIResource);
/*     */ 
/* 218 */       i2 = (k + 1 - n) / 2 + n - 1;
/* 219 */       i3 = m / 2;
/*     */ 
/* 223 */       for (i4 = 0; i4 < n; i4++) {
/* 224 */         paramGraphics.drawLine(i2 - i4, i3 - i4, i2 - i4, i3 + i4 + 1);
/*     */       }
/*     */ 
/* 233 */       if (bool2) {
/* 234 */         paramGraphics.setColor(highlightColor);
/*     */ 
/* 236 */         if (!bool3)
/*     */         {
/* 238 */           paramGraphics.drawLine(0, 1, i - 3, 1);
/* 239 */           paramGraphics.drawLine(0, 1, 0, j - 3);
/*     */         }
/*     */ 
/* 242 */         paramGraphics.drawLine(i - 1, 1, i - 1, j - 1);
/* 243 */         paramGraphics.drawLine(0, j - 1, i - 1, j - 1);
/*     */ 
/* 245 */         paramGraphics.setColor(shadowColor);
/* 246 */         paramGraphics.drawLine(0, 0, i - 2, 0);
/* 247 */         paramGraphics.drawLine(i - 2, 2, i - 2, j - 2);
/* 248 */         paramGraphics.drawLine(0, j - 2, i - 2, j - 2);
/*     */       } else {
/* 250 */         MetalUtils.drawDisabledBorder(paramGraphics, -1, 0, i + 1, j);
/*     */       }
/* 252 */       if (!this.isFreeStanding) {
/* 253 */         j -= 2;
/* 254 */         i--;
/*     */       }
/*     */     }
/* 257 */     else if (getDirection() == 7)
/*     */     {
/* 259 */       if (!this.isFreeStanding) {
/* 260 */         j += 2;
/* 261 */         i++;
/* 262 */         paramGraphics.translate(-1, 0);
/*     */       }
/*     */ 
/* 266 */       paramGraphics.setColor(localColorUIResource);
/*     */ 
/* 268 */       i2 = (k + 1 - n) / 2;
/* 269 */       i3 = m / 2;
/*     */ 
/* 272 */       for (i4 = 0; i4 < n; i4++) {
/* 273 */         paramGraphics.drawLine(i2 + i4, i3 - i4, i2 + i4, i3 + i4 + 1);
/*     */       }
/*     */ 
/* 281 */       if (bool2) {
/* 282 */         paramGraphics.setColor(highlightColor);
/*     */ 
/* 285 */         if (!bool3)
/*     */         {
/* 287 */           paramGraphics.drawLine(1, 1, i - 1, 1);
/* 288 */           paramGraphics.drawLine(1, 1, 1, j - 3);
/*     */         }
/*     */ 
/* 291 */         paramGraphics.drawLine(1, j - 1, i - 1, j - 1);
/*     */ 
/* 293 */         paramGraphics.setColor(shadowColor);
/* 294 */         paramGraphics.drawLine(0, 0, i - 1, 0);
/* 295 */         paramGraphics.drawLine(0, 0, 0, j - 2);
/* 296 */         paramGraphics.drawLine(2, j - 2, i - 1, j - 2);
/*     */       } else {
/* 298 */         MetalUtils.drawDisabledBorder(paramGraphics, 0, 0, i + 1, j);
/*     */       }
/*     */ 
/* 301 */       if (!this.isFreeStanding) {
/* 302 */         j -= 2;
/* 303 */         i--;
/* 304 */         paramGraphics.translate(1, 0);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize()
/*     */   {
/* 311 */     if (getDirection() == 1)
/*     */     {
/* 313 */       return new Dimension(this.buttonWidth, this.buttonWidth - 2);
/*     */     }
/* 315 */     if (getDirection() == 5)
/*     */     {
/* 317 */       return new Dimension(this.buttonWidth, this.buttonWidth - (this.isFreeStanding ? 1 : 2));
/*     */     }
/* 319 */     if (getDirection() == 3)
/*     */     {
/* 321 */       return new Dimension(this.buttonWidth - (this.isFreeStanding ? 1 : 2), this.buttonWidth);
/*     */     }
/* 323 */     if (getDirection() == 7)
/*     */     {
/* 325 */       return new Dimension(this.buttonWidth - 2, this.buttonWidth);
/*     */     }
/*     */ 
/* 329 */     return new Dimension(0, 0);
/*     */   }
/*     */ 
/*     */   public Dimension getMinimumSize()
/*     */   {
/* 335 */     return getPreferredSize();
/*     */   }
/*     */ 
/*     */   public Dimension getMaximumSize()
/*     */   {
/* 340 */     return new Dimension(2147483647, 2147483647);
/*     */   }
/*     */ 
/*     */   public int getButtonWidth() {
/* 344 */     return this.buttonWidth;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.metal.MetalScrollButton
 * JD-Core Version:    0.6.2
 */