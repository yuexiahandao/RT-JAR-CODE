/*     */ package com.sun.java.swing.plaf.windows;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.MouseEvent;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JSlider;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicSliderUI;
/*     */ import javax.swing.plaf.basic.BasicSliderUI.TrackListener;
/*     */ 
/*     */ public class WindowsSliderUI extends BasicSliderUI
/*     */ {
/*  51 */   private boolean rollover = false;
/*  52 */   private boolean pressed = false;
/*     */ 
/*     */   public WindowsSliderUI(JSlider paramJSlider) {
/*  55 */     super(paramJSlider);
/*     */   }
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent) {
/*  59 */     return new WindowsSliderUI((JSlider)paramJComponent);
/*     */   }
/*     */ 
/*     */   protected BasicSliderUI.TrackListener createTrackListener(JSlider paramJSlider)
/*     */   {
/*  69 */     return new WindowsTrackListener(null);
/*     */   }
/*     */ 
/*     */   public void paintTrack(Graphics paramGraphics)
/*     */   {
/* 125 */     XPStyle localXPStyle = XPStyle.getXP();
/* 126 */     if (localXPStyle != null) {
/* 127 */       int i = this.slider.getOrientation() == 1 ? 1 : 0;
/* 128 */       TMSchema.Part localPart = i != 0 ? TMSchema.Part.TKP_TRACKVERT : TMSchema.Part.TKP_TRACK;
/* 129 */       XPStyle.Skin localSkin = localXPStyle.getSkin(this.slider, localPart);
/*     */       int j;
/* 131 */       if (i != 0) {
/* 132 */         j = (this.trackRect.width - localSkin.getWidth()) / 2;
/* 133 */         localSkin.paintSkin(paramGraphics, this.trackRect.x + j, this.trackRect.y, localSkin.getWidth(), this.trackRect.height, null);
/*     */       }
/*     */       else {
/* 136 */         j = (this.trackRect.height - localSkin.getHeight()) / 2;
/* 137 */         localSkin.paintSkin(paramGraphics, this.trackRect.x, this.trackRect.y + j, this.trackRect.width, localSkin.getHeight(), null);
/*     */       }
/*     */     }
/*     */     else {
/* 141 */       super.paintTrack(paramGraphics);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void paintMinorTickForHorizSlider(Graphics paramGraphics, Rectangle paramRectangle, int paramInt)
/*     */   {
/* 147 */     XPStyle localXPStyle = XPStyle.getXP();
/* 148 */     if (localXPStyle != null) {
/* 149 */       paramGraphics.setColor(localXPStyle.getColor(this.slider, TMSchema.Part.TKP_TICS, null, TMSchema.Prop.COLOR, Color.black));
/*     */     }
/* 151 */     super.paintMinorTickForHorizSlider(paramGraphics, paramRectangle, paramInt);
/*     */   }
/*     */ 
/*     */   protected void paintMajorTickForHorizSlider(Graphics paramGraphics, Rectangle paramRectangle, int paramInt) {
/* 155 */     XPStyle localXPStyle = XPStyle.getXP();
/* 156 */     if (localXPStyle != null) {
/* 157 */       paramGraphics.setColor(localXPStyle.getColor(this.slider, TMSchema.Part.TKP_TICS, null, TMSchema.Prop.COLOR, Color.black));
/*     */     }
/* 159 */     super.paintMajorTickForHorizSlider(paramGraphics, paramRectangle, paramInt);
/*     */   }
/*     */ 
/*     */   protected void paintMinorTickForVertSlider(Graphics paramGraphics, Rectangle paramRectangle, int paramInt) {
/* 163 */     XPStyle localXPStyle = XPStyle.getXP();
/* 164 */     if (localXPStyle != null) {
/* 165 */       paramGraphics.setColor(localXPStyle.getColor(this.slider, TMSchema.Part.TKP_TICSVERT, null, TMSchema.Prop.COLOR, Color.black));
/*     */     }
/* 167 */     super.paintMinorTickForVertSlider(paramGraphics, paramRectangle, paramInt);
/*     */   }
/*     */ 
/*     */   protected void paintMajorTickForVertSlider(Graphics paramGraphics, Rectangle paramRectangle, int paramInt) {
/* 171 */     XPStyle localXPStyle = XPStyle.getXP();
/* 172 */     if (localXPStyle != null) {
/* 173 */       paramGraphics.setColor(localXPStyle.getColor(this.slider, TMSchema.Part.TKP_TICSVERT, null, TMSchema.Prop.COLOR, Color.black));
/*     */     }
/* 175 */     super.paintMajorTickForVertSlider(paramGraphics, paramRectangle, paramInt);
/*     */   }
/*     */ 
/*     */   public void paintThumb(Graphics paramGraphics)
/*     */   {
/* 180 */     XPStyle localXPStyle = XPStyle.getXP();
/* 181 */     if (localXPStyle != null) {
/* 182 */       TMSchema.Part localPart = getXPThumbPart();
/* 183 */       TMSchema.State localState = TMSchema.State.NORMAL;
/*     */ 
/* 185 */       if (this.slider.hasFocus()) {
/* 186 */         localState = TMSchema.State.FOCUSED;
/*     */       }
/* 188 */       if (this.rollover) {
/* 189 */         localState = TMSchema.State.HOT;
/*     */       }
/* 191 */       if (this.pressed) {
/* 192 */         localState = TMSchema.State.PRESSED;
/*     */       }
/* 194 */       if (!this.slider.isEnabled()) {
/* 195 */         localState = TMSchema.State.DISABLED;
/*     */       }
/*     */ 
/* 198 */       localXPStyle.getSkin(this.slider, localPart).paintSkin(paramGraphics, this.thumbRect.x, this.thumbRect.y, localState);
/*     */     } else {
/* 200 */       super.paintThumb(paramGraphics);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected Dimension getThumbSize() {
/* 205 */     XPStyle localXPStyle = XPStyle.getXP();
/* 206 */     if (localXPStyle != null) {
/* 207 */       Dimension localDimension = new Dimension();
/* 208 */       XPStyle.Skin localSkin = localXPStyle.getSkin(this.slider, getXPThumbPart());
/* 209 */       localDimension.width = localSkin.getWidth();
/* 210 */       localDimension.height = localSkin.getHeight();
/* 211 */       return localDimension;
/*     */     }
/* 213 */     return super.getThumbSize();
/*     */   }
/*     */ 
/*     */   private TMSchema.Part getXPThumbPart()
/*     */   {
/* 219 */     int i = this.slider.getOrientation() == 1 ? 1 : 0;
/* 220 */     boolean bool = this.slider.getComponentOrientation().isLeftToRight();
/* 221 */     Boolean localBoolean = (Boolean)this.slider.getClientProperty("Slider.paintThumbArrowShape");
/*     */     TMSchema.Part localPart;
/* 223 */     if (((!this.slider.getPaintTicks()) && (localBoolean == null)) || (localBoolean == Boolean.FALSE))
/*     */     {
/* 225 */       localPart = i != 0 ? TMSchema.Part.TKP_THUMBVERT : TMSchema.Part.TKP_THUMB;
/*     */     }
/*     */     else {
/* 228 */       localPart = i != 0 ? TMSchema.Part.TKP_THUMBLEFT : bool ? TMSchema.Part.TKP_THUMBRIGHT : TMSchema.Part.TKP_THUMBBOTTOM;
/*     */     }
/*     */ 
/* 231 */     return localPart;
/*     */   }
/*     */ 
/*     */   private class WindowsTrackListener extends BasicSliderUI.TrackListener
/*     */   {
/*     */     private WindowsTrackListener()
/*     */     {
/*  72 */       super();
/*     */     }
/*     */     public void mouseMoved(MouseEvent paramMouseEvent) {
/*  75 */       updateRollover(WindowsSliderUI.this.thumbRect.contains(paramMouseEvent.getX(), paramMouseEvent.getY()));
/*  76 */       super.mouseMoved(paramMouseEvent);
/*     */     }
/*     */ 
/*     */     public void mouseEntered(MouseEvent paramMouseEvent) {
/*  80 */       updateRollover(WindowsSliderUI.this.thumbRect.contains(paramMouseEvent.getX(), paramMouseEvent.getY()));
/*  81 */       super.mouseEntered(paramMouseEvent);
/*     */     }
/*     */ 
/*     */     public void mouseExited(MouseEvent paramMouseEvent) {
/*  85 */       updateRollover(false);
/*  86 */       super.mouseExited(paramMouseEvent);
/*     */     }
/*     */ 
/*     */     public void mousePressed(MouseEvent paramMouseEvent) {
/*  90 */       updatePressed(WindowsSliderUI.this.thumbRect.contains(paramMouseEvent.getX(), paramMouseEvent.getY()));
/*  91 */       super.mousePressed(paramMouseEvent);
/*     */     }
/*     */ 
/*     */     public void mouseReleased(MouseEvent paramMouseEvent) {
/*  95 */       updatePressed(false);
/*  96 */       super.mouseReleased(paramMouseEvent);
/*     */     }
/*     */ 
/*     */     public void updatePressed(boolean paramBoolean)
/*     */     {
/* 101 */       if (!WindowsSliderUI.this.slider.isEnabled()) {
/* 102 */         return;
/*     */       }
/* 104 */       if (WindowsSliderUI.this.pressed != paramBoolean) {
/* 105 */         WindowsSliderUI.this.pressed = paramBoolean;
/* 106 */         WindowsSliderUI.this.slider.repaint(WindowsSliderUI.this.thumbRect);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void updateRollover(boolean paramBoolean)
/*     */     {
/* 112 */       if (!WindowsSliderUI.this.slider.isEnabled()) {
/* 113 */         return;
/*     */       }
/* 115 */       if (WindowsSliderUI.this.rollover != paramBoolean) {
/* 116 */         WindowsSliderUI.this.rollover = paramBoolean;
/* 117 */         WindowsSliderUI.this.slider.repaint(WindowsSliderUI.this.thumbRect);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.windows.WindowsSliderUI
 * JD-Core Version:    0.6.2
 */