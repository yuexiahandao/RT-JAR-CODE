/*     */ package com.sun.java.swing.plaf.windows;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.JInternalFrame;
/*     */ import javax.swing.JInternalFrame.JDesktopIcon;
/*     */ import javax.swing.JToolBar;
/*     */ import javax.swing.SwingConstants;
/*     */ import javax.swing.UIDefaults;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.AbstractBorder;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.border.EmptyBorder;
/*     */ import javax.swing.border.LineBorder;
/*     */ import javax.swing.plaf.BorderUIResource.CompoundBorderUIResource;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.plaf.basic.BasicBorders.ButtonBorder;
/*     */ import javax.swing.plaf.basic.BasicBorders.MarginBorder;
/*     */ import javax.swing.plaf.basic.BasicGraphicsUtils;
/*     */ 
/*     */ public class WindowsBorders
/*     */ {
/*     */   public static Border getProgressBarBorder()
/*     */   {
/*  53 */     UIDefaults localUIDefaults = UIManager.getLookAndFeelDefaults();
/*  54 */     BorderUIResource.CompoundBorderUIResource localCompoundBorderUIResource = new BorderUIResource.CompoundBorderUIResource(new ProgressBarBorder(localUIDefaults.getColor("ProgressBar.shadow"), localUIDefaults.getColor("ProgressBar.highlight")), new EmptyBorder(1, 1, 1, 1));
/*     */ 
/*  60 */     return localCompoundBorderUIResource;
/*     */   }
/*     */ 
/*     */   public static Border getToolBarBorder()
/*     */   {
/*  70 */     UIDefaults localUIDefaults = UIManager.getLookAndFeelDefaults();
/*  71 */     ToolBarBorder localToolBarBorder = new ToolBarBorder(localUIDefaults.getColor("ToolBar.shadow"), localUIDefaults.getColor("ToolBar.highlight"));
/*     */ 
/*  74 */     return localToolBarBorder;
/*     */   }
/*     */ 
/*     */   public static Border getFocusCellHighlightBorder()
/*     */   {
/*  85 */     return new ComplementDashedBorder();
/*     */   }
/*     */ 
/*     */   public static Border getTableHeaderBorder() {
/*  89 */     UIDefaults localUIDefaults = UIManager.getLookAndFeelDefaults();
/*  90 */     BorderUIResource.CompoundBorderUIResource localCompoundBorderUIResource = new BorderUIResource.CompoundBorderUIResource(new BasicBorders.ButtonBorder(localUIDefaults.getColor("Table.shadow"), localUIDefaults.getColor("Table.darkShadow"), localUIDefaults.getColor("Table.light"), localUIDefaults.getColor("Table.highlight")), new BasicBorders.MarginBorder());
/*     */ 
/*  97 */     return localCompoundBorderUIResource;
/*     */   }
/*     */ 
/*     */   public static Border getInternalFrameBorder() {
/* 101 */     UIDefaults localUIDefaults = UIManager.getLookAndFeelDefaults();
/* 102 */     BorderUIResource.CompoundBorderUIResource localCompoundBorderUIResource = new BorderUIResource.CompoundBorderUIResource(BorderFactory.createBevelBorder(0, localUIDefaults.getColor("InternalFrame.borderColor"), localUIDefaults.getColor("InternalFrame.borderHighlight"), localUIDefaults.getColor("InternalFrame.borderDarkShadow"), localUIDefaults.getColor("InternalFrame.borderShadow")), new InternalFrameLineBorder(localUIDefaults.getColor("InternalFrame.activeBorderColor"), localUIDefaults.getColor("InternalFrame.inactiveBorderColor"), localUIDefaults.getInt("InternalFrame.borderWidth")));
/*     */ 
/* 114 */     return localCompoundBorderUIResource;
/*     */   }
/*     */ 
/*     */   static class ComplementDashedBorder extends LineBorder
/*     */     implements UIResource
/*     */   {
/*     */     private Color origColor;
/*     */     private Color paintColor;
/*     */ 
/*     */     public ComplementDashedBorder()
/*     */     {
/* 280 */       super();
/*     */     }
/*     */ 
/*     */     public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 284 */       Color localColor = paramComponent.getBackground();
/*     */ 
/* 286 */       if (this.origColor != localColor) {
/* 287 */         this.origColor = localColor;
/* 288 */         this.paintColor = new Color(this.origColor.getRGB() ^ 0xFFFFFFFF);
/*     */       }
/*     */ 
/* 291 */       paramGraphics.setColor(this.paintColor);
/* 292 */       BasicGraphicsUtils.drawDashedRect(paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class DashedBorder extends LineBorder
/*     */     implements UIResource
/*     */   {
/*     */     public DashedBorder(Color paramColor)
/*     */     {
/* 252 */       super();
/*     */     }
/*     */ 
/*     */     public DashedBorder(Color paramColor, int paramInt) {
/* 256 */       super(paramInt);
/*     */     }
/*     */ 
/*     */     public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 260 */       Color localColor = paramGraphics.getColor();
/*     */ 
/* 263 */       paramGraphics.setColor(this.lineColor);
/* 264 */       for (int i = 0; i < this.thickness; i++) {
/* 265 */         BasicGraphicsUtils.drawDashedRect(paramGraphics, paramInt1 + i, paramInt2 + i, paramInt3 - i - i, paramInt4 - i - i);
/*     */       }
/* 267 */       paramGraphics.setColor(localColor);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class InternalFrameLineBorder extends LineBorder
/*     */     implements UIResource
/*     */   {
/*     */     protected Color activeColor;
/*     */     protected Color inactiveColor;
/*     */ 
/*     */     public InternalFrameLineBorder(Color paramColor1, Color paramColor2, int paramInt)
/*     */     {
/* 308 */       super(paramInt);
/* 309 */       this.activeColor = paramColor1;
/* 310 */       this.inactiveColor = paramColor2;
/*     */     }
/*     */ 
/*     */     public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     {
/* 316 */       JInternalFrame localJInternalFrame = null;
/* 317 */       if ((paramComponent instanceof JInternalFrame))
/* 318 */         localJInternalFrame = (JInternalFrame)paramComponent;
/* 319 */       else if ((paramComponent instanceof JInternalFrame.JDesktopIcon))
/* 320 */         localJInternalFrame = ((JInternalFrame.JDesktopIcon)paramComponent).getInternalFrame();
/*     */       else {
/* 322 */         return;
/*     */       }
/*     */ 
/* 325 */       if (localJInternalFrame.isSelected())
/*     */       {
/* 328 */         this.lineColor = this.activeColor;
/* 329 */         super.paintBorder(paramComponent, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */       } else {
/* 331 */         this.lineColor = this.inactiveColor;
/* 332 */         super.paintBorder(paramComponent, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class ProgressBarBorder extends AbstractBorder
/*     */     implements UIResource
/*     */   {
/*     */     protected Color shadow;
/*     */     protected Color highlight;
/*     */ 
/*     */     public ProgressBarBorder(Color paramColor1, Color paramColor2)
/*     */     {
/* 122 */       this.highlight = paramColor2;
/* 123 */       this.shadow = paramColor1;
/*     */     }
/*     */ 
/*     */     public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     {
/* 128 */       paramGraphics.setColor(this.shadow);
/* 129 */       paramGraphics.drawLine(paramInt1, paramInt2, paramInt3 - 1, paramInt2);
/* 130 */       paramGraphics.drawLine(paramInt1, paramInt2, paramInt1, paramInt4 - 1);
/* 131 */       paramGraphics.setColor(this.highlight);
/* 132 */       paramGraphics.drawLine(paramInt1, paramInt4 - 1, paramInt3 - 1, paramInt4 - 1);
/* 133 */       paramGraphics.drawLine(paramInt3 - 1, paramInt2, paramInt3 - 1, paramInt4 - 1);
/*     */     }
/*     */ 
/*     */     public Insets getBorderInsets(Component paramComponent, Insets paramInsets) {
/* 137 */       paramInsets.set(1, 1, 1, 1);
/* 138 */       return paramInsets;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class ToolBarBorder extends AbstractBorder
/*     */     implements UIResource, SwingConstants
/*     */   {
/*     */     protected Color shadow;
/*     */     protected Color highlight;
/*     */ 
/*     */     public ToolBarBorder(Color paramColor1, Color paramColor2)
/*     */     {
/* 152 */       this.highlight = paramColor2;
/* 153 */       this.shadow = paramColor1;
/*     */     }
/*     */ 
/*     */     public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     {
/* 158 */       if (!(paramComponent instanceof JToolBar)) {
/* 159 */         return;
/*     */       }
/* 161 */       paramGraphics.translate(paramInt1, paramInt2);
/*     */ 
/* 163 */       XPStyle localXPStyle = XPStyle.getXP();
/* 164 */       if (localXPStyle != null) {
/* 165 */         Border localBorder = localXPStyle.getBorder(paramComponent, TMSchema.Part.TP_TOOLBAR);
/* 166 */         if (localBorder != null) {
/* 167 */           localBorder.paintBorder(paramComponent, paramGraphics, 0, 0, paramInt3, paramInt4);
/*     */         }
/*     */       }
/* 170 */       if (((JToolBar)paramComponent).isFloatable()) {
/* 171 */         int i = ((JToolBar)paramComponent).getOrientation() == 1 ? 1 : 0;
/*     */ 
/* 173 */         if (localXPStyle != null) {
/* 174 */           TMSchema.Part localPart = i != 0 ? TMSchema.Part.RP_GRIPPERVERT : TMSchema.Part.RP_GRIPPER;
/* 175 */           XPStyle.Skin localSkin = localXPStyle.getSkin(paramComponent, localPart);
/*     */           int j;
/*     */           int k;
/*     */           int m;
/*     */           int n;
/* 177 */           if (i != 0) {
/* 178 */             j = 0;
/* 179 */             k = 2;
/* 180 */             m = paramInt3 - 1;
/* 181 */             n = localSkin.getHeight();
/*     */           } else {
/* 183 */             m = localSkin.getWidth();
/* 184 */             n = paramInt4 - 1;
/* 185 */             j = paramComponent.getComponentOrientation().isLeftToRight() ? 2 : paramInt3 - m - 2;
/* 186 */             k = 0;
/*     */           }
/* 188 */           localSkin.paintSkin(paramGraphics, j, k, m, n, TMSchema.State.NORMAL);
/*     */         }
/* 192 */         else if (i == 0) {
/* 193 */           if (paramComponent.getComponentOrientation().isLeftToRight()) {
/* 194 */             paramGraphics.setColor(this.shadow);
/* 195 */             paramGraphics.drawLine(4, 3, 4, paramInt4 - 4);
/* 196 */             paramGraphics.drawLine(4, paramInt4 - 4, 2, paramInt4 - 4);
/*     */ 
/* 198 */             paramGraphics.setColor(this.highlight);
/* 199 */             paramGraphics.drawLine(2, 3, 3, 3);
/* 200 */             paramGraphics.drawLine(2, 3, 2, paramInt4 - 5);
/*     */           } else {
/* 202 */             paramGraphics.setColor(this.shadow);
/* 203 */             paramGraphics.drawLine(paramInt3 - 3, 3, paramInt3 - 3, paramInt4 - 4);
/* 204 */             paramGraphics.drawLine(paramInt3 - 4, paramInt4 - 4, paramInt3 - 4, paramInt4 - 4);
/*     */ 
/* 206 */             paramGraphics.setColor(this.highlight);
/* 207 */             paramGraphics.drawLine(paramInt3 - 5, 3, paramInt3 - 4, 3);
/* 208 */             paramGraphics.drawLine(paramInt3 - 5, 3, paramInt3 - 5, paramInt4 - 5);
/*     */           }
/*     */         } else {
/* 211 */           paramGraphics.setColor(this.shadow);
/* 212 */           paramGraphics.drawLine(3, 4, paramInt3 - 4, 4);
/* 213 */           paramGraphics.drawLine(paramInt3 - 4, 2, paramInt3 - 4, 4);
/*     */ 
/* 215 */           paramGraphics.setColor(this.highlight);
/* 216 */           paramGraphics.drawLine(3, 2, paramInt3 - 4, 2);
/* 217 */           paramGraphics.drawLine(3, 2, 3, 3);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 222 */       paramGraphics.translate(-paramInt1, -paramInt2);
/*     */     }
/*     */ 
/*     */     public Insets getBorderInsets(Component paramComponent, Insets paramInsets) {
/* 226 */       paramInsets.set(1, 1, 1, 1);
/* 227 */       if (!(paramComponent instanceof JToolBar)) {
/* 228 */         return paramInsets;
/*     */       }
/* 230 */       if (((JToolBar)paramComponent).isFloatable()) {
/* 231 */         int i = XPStyle.getXP() != null ? 12 : 9;
/* 232 */         if (((JToolBar)paramComponent).getOrientation() == 0) {
/* 233 */           if (paramComponent.getComponentOrientation().isLeftToRight())
/* 234 */             paramInsets.left = i;
/*     */           else
/* 236 */             paramInsets.right = i;
/*     */         }
/*     */         else {
/* 239 */           paramInsets.top = i;
/*     */         }
/*     */       }
/* 242 */       return paramInsets;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.windows.WindowsBorders
 * JD-Core Version:    0.6.2
 */