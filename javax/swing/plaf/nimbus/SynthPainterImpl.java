/*      */ package javax.swing.plaf.nimbus;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.ComponentOrientation;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Graphics2D;
/*      */ import java.awt.Image;
/*      */ import java.awt.geom.AffineTransform;
/*      */ import java.awt.geom.NoninvertibleTransformException;
/*      */ import java.awt.image.BufferedImage;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.JSlider;
/*      */ import javax.swing.Painter;
/*      */ import javax.swing.plaf.synth.SynthContext;
/*      */ import javax.swing.plaf.synth.SynthPainter;
/*      */ 
/*      */ class SynthPainterImpl extends SynthPainter
/*      */ {
/*      */   private NimbusStyle style;
/*      */ 
/*      */   SynthPainterImpl(NimbusStyle paramNimbusStyle)
/*      */   {
/*   44 */     this.style = paramNimbusStyle;
/*      */   }
/*      */ 
/*      */   private void paint(Painter paramPainter, SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, AffineTransform paramAffineTransform)
/*      */   {
/*   54 */     if (paramPainter != null)
/*      */     {
/*      */       Object localObject;
/*   55 */       if ((paramGraphics instanceof Graphics2D)) {
/*   56 */         localObject = (Graphics2D)paramGraphics;
/*   57 */         if (paramAffineTransform != null) {
/*   58 */           ((Graphics2D)localObject).transform(paramAffineTransform);
/*      */         }
/*   60 */         ((Graphics2D)localObject).translate(paramInt1, paramInt2);
/*   61 */         paramPainter.paint((Graphics2D)localObject, paramSynthContext.getComponent(), paramInt3, paramInt4);
/*   62 */         ((Graphics2D)localObject).translate(-paramInt1, -paramInt2);
/*   63 */         if (paramAffineTransform != null) {
/*      */           try {
/*   65 */             ((Graphics2D)localObject).transform(paramAffineTransform.createInverse());
/*      */           }
/*      */           catch (NoninvertibleTransformException localNoninvertibleTransformException)
/*      */           {
/*   70 */             localNoninvertibleTransformException.printStackTrace();
/*      */           }
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*   76 */         localObject = new BufferedImage(paramInt3, paramInt4, 2);
/*      */ 
/*   78 */         Graphics2D localGraphics2D = ((BufferedImage)localObject).createGraphics();
/*   79 */         if (paramAffineTransform != null) {
/*   80 */           localGraphics2D.transform(paramAffineTransform);
/*      */         }
/*   82 */         paramPainter.paint(localGraphics2D, paramSynthContext.getComponent(), paramInt3, paramInt4);
/*   83 */         localGraphics2D.dispose();
/*   84 */         paramGraphics.drawImage((Image)localObject, paramInt1, paramInt2, null);
/*   85 */         localObject = null;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void paintBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, AffineTransform paramAffineTransform)
/*      */   {
/*   96 */     JComponent localJComponent = paramSynthContext.getComponent();
/*   97 */     Object localObject = localJComponent != null ? localJComponent.getBackground() : null;
/*   98 */     if ((localObject == null) || (localObject.getAlpha() > 0)) {
/*   99 */       Painter localPainter = this.style.getBackgroundPainter(paramSynthContext);
/*  100 */       if (localPainter != null)
/*  101 */         paint(localPainter, paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramAffineTransform);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void paintForeground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, AffineTransform paramAffineTransform)
/*      */   {
/*  108 */     Painter localPainter = this.style.getForegroundPainter(paramSynthContext);
/*  109 */     if (localPainter != null)
/*  110 */       paint(localPainter, paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramAffineTransform);
/*      */   }
/*      */ 
/*      */   private void paintBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, AffineTransform paramAffineTransform)
/*      */   {
/*  116 */     Painter localPainter = this.style.getBorderPainter(paramSynthContext);
/*  117 */     if (localPainter != null)
/*  118 */       paint(localPainter, paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramAffineTransform);
/*      */   }
/*      */ 
/*      */   private void paintBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */   {
/*  123 */     JComponent localJComponent = paramSynthContext.getComponent();
/*  124 */     boolean bool = localJComponent.getComponentOrientation().isLeftToRight();
/*      */ 
/*  126 */     if ((paramSynthContext.getComponent() instanceof JSlider)) bool = true;
/*      */     AffineTransform localAffineTransform;
/*  128 */     if ((paramInt5 == 1) && (bool)) {
/*  129 */       localAffineTransform = new AffineTransform();
/*  130 */       localAffineTransform.scale(-1.0D, 1.0D);
/*  131 */       localAffineTransform.rotate(Math.toRadians(90.0D));
/*  132 */       paintBackground(paramSynthContext, paramGraphics, paramInt2, paramInt1, paramInt4, paramInt3, localAffineTransform);
/*  133 */     } else if (paramInt5 == 1) {
/*  134 */       localAffineTransform = new AffineTransform();
/*  135 */       localAffineTransform.rotate(Math.toRadians(90.0D));
/*  136 */       localAffineTransform.translate(0.0D, -(paramInt1 + paramInt3));
/*  137 */       paintBackground(paramSynthContext, paramGraphics, paramInt2, paramInt1, paramInt4, paramInt3, localAffineTransform);
/*  138 */     } else if ((paramInt5 == 0) && (bool)) {
/*  139 */       paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */     }
/*      */     else {
/*  142 */       localAffineTransform = new AffineTransform();
/*  143 */       localAffineTransform.translate(paramInt1, paramInt2);
/*  144 */       localAffineTransform.scale(-1.0D, 1.0D);
/*  145 */       localAffineTransform.translate(-paramInt3, 0.0D);
/*  146 */       paintBackground(paramSynthContext, paramGraphics, 0, 0, paramInt3, paramInt4, localAffineTransform);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void paintBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
/*  151 */     JComponent localJComponent = paramSynthContext.getComponent();
/*  152 */     boolean bool = localJComponent.getComponentOrientation().isLeftToRight();
/*      */     AffineTransform localAffineTransform;
/*  153 */     if ((paramInt5 == 1) && (bool)) {
/*  154 */       localAffineTransform = new AffineTransform();
/*  155 */       localAffineTransform.scale(-1.0D, 1.0D);
/*  156 */       localAffineTransform.rotate(Math.toRadians(90.0D));
/*  157 */       paintBorder(paramSynthContext, paramGraphics, paramInt2, paramInt1, paramInt4, paramInt3, localAffineTransform);
/*  158 */     } else if (paramInt5 == 1) {
/*  159 */       localAffineTransform = new AffineTransform();
/*  160 */       localAffineTransform.rotate(Math.toRadians(90.0D));
/*  161 */       localAffineTransform.translate(0.0D, -(paramInt1 + paramInt3));
/*  162 */       paintBorder(paramSynthContext, paramGraphics, paramInt2, 0, paramInt4, paramInt3, localAffineTransform);
/*  163 */     } else if ((paramInt5 == 0) && (bool)) {
/*  164 */       paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */     }
/*      */     else {
/*  167 */       paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void paintForeground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
/*  172 */     JComponent localJComponent = paramSynthContext.getComponent();
/*  173 */     boolean bool = localJComponent.getComponentOrientation().isLeftToRight();
/*      */     AffineTransform localAffineTransform;
/*  174 */     if ((paramInt5 == 1) && (bool)) {
/*  175 */       localAffineTransform = new AffineTransform();
/*  176 */       localAffineTransform.scale(-1.0D, 1.0D);
/*  177 */       localAffineTransform.rotate(Math.toRadians(90.0D));
/*  178 */       paintForeground(paramSynthContext, paramGraphics, paramInt2, paramInt1, paramInt4, paramInt3, localAffineTransform);
/*  179 */     } else if (paramInt5 == 1) {
/*  180 */       localAffineTransform = new AffineTransform();
/*  181 */       localAffineTransform.rotate(Math.toRadians(90.0D));
/*  182 */       localAffineTransform.translate(0.0D, -(paramInt1 + paramInt3));
/*  183 */       paintForeground(paramSynthContext, paramGraphics, paramInt2, 0, paramInt4, paramInt3, localAffineTransform);
/*  184 */     } else if ((paramInt5 == 0) && (bool)) {
/*  185 */       paintForeground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */     }
/*      */     else {
/*  188 */       paintForeground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void paintArrowButtonBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  207 */     if (paramSynthContext.getComponent().getComponentOrientation().isLeftToRight()) {
/*  208 */       paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */     } else {
/*  210 */       AffineTransform localAffineTransform = new AffineTransform();
/*  211 */       localAffineTransform.translate(paramInt1, paramInt2);
/*  212 */       localAffineTransform.scale(-1.0D, 1.0D);
/*  213 */       localAffineTransform.translate(-paramInt3, 0.0D);
/*  214 */       paintBackground(paramSynthContext, paramGraphics, 0, 0, paramInt3, paramInt4, localAffineTransform);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void paintArrowButtonBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  233 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintArrowButtonForeground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */   {
/*  257 */     String str = paramSynthContext.getComponent().getName();
/*  258 */     boolean bool = paramSynthContext.getComponent().getComponentOrientation().isLeftToRight();
/*      */     AffineTransform localAffineTransform;
/*  262 */     if (("Spinner.nextButton".equals(str)) || ("Spinner.previousButton".equals(str)))
/*      */     {
/*  264 */       if (bool) {
/*  265 */         paintForeground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */       } else {
/*  267 */         localAffineTransform = new AffineTransform();
/*  268 */         localAffineTransform.translate(paramInt3, 0.0D);
/*  269 */         localAffineTransform.scale(-1.0D, 1.0D);
/*  270 */         paintForeground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, localAffineTransform);
/*      */       }
/*  272 */     } else if (paramInt5 == 7) {
/*  273 */       paintForeground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*  274 */     } else if (paramInt5 == 1) {
/*  275 */       if (bool) {
/*  276 */         localAffineTransform = new AffineTransform();
/*  277 */         localAffineTransform.scale(-1.0D, 1.0D);
/*  278 */         localAffineTransform.rotate(Math.toRadians(90.0D));
/*  279 */         paintForeground(paramSynthContext, paramGraphics, paramInt2, 0, paramInt4, paramInt3, localAffineTransform);
/*      */       } else {
/*  281 */         localAffineTransform = new AffineTransform();
/*  282 */         localAffineTransform.rotate(Math.toRadians(90.0D));
/*  283 */         localAffineTransform.translate(0.0D, -(paramInt1 + paramInt3));
/*  284 */         paintForeground(paramSynthContext, paramGraphics, paramInt2, 0, paramInt4, paramInt3, localAffineTransform);
/*      */       }
/*  286 */     } else if (paramInt5 == 3) {
/*  287 */       localAffineTransform = new AffineTransform();
/*  288 */       localAffineTransform.translate(paramInt3, 0.0D);
/*  289 */       localAffineTransform.scale(-1.0D, 1.0D);
/*  290 */       paintForeground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, localAffineTransform);
/*  291 */     } else if (paramInt5 == 5) {
/*  292 */       if (bool) {
/*  293 */         localAffineTransform = new AffineTransform();
/*  294 */         localAffineTransform.rotate(Math.toRadians(-90.0D));
/*  295 */         localAffineTransform.translate(-paramInt4, 0.0D);
/*  296 */         paintForeground(paramSynthContext, paramGraphics, paramInt2, paramInt1, paramInt4, paramInt3, localAffineTransform);
/*      */       } else {
/*  298 */         localAffineTransform = new AffineTransform();
/*  299 */         localAffineTransform.scale(-1.0D, 1.0D);
/*  300 */         localAffineTransform.rotate(Math.toRadians(-90.0D));
/*  301 */         localAffineTransform.translate(-(paramInt4 + paramInt2), -(paramInt3 + paramInt1));
/*  302 */         paintForeground(paramSynthContext, paramGraphics, paramInt2, paramInt1, paramInt4, paramInt3, localAffineTransform);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void paintButtonBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  321 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintButtonBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  338 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintCheckBoxMenuItemBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  355 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintCheckBoxMenuItemBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  372 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintCheckBoxBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  389 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintCheckBoxBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  406 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintColorChooserBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  423 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintColorChooserBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  440 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintComboBoxBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  457 */     if (paramSynthContext.getComponent().getComponentOrientation().isLeftToRight()) {
/*  458 */       paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */     } else {
/*  460 */       AffineTransform localAffineTransform = new AffineTransform();
/*  461 */       localAffineTransform.translate(paramInt1, paramInt2);
/*  462 */       localAffineTransform.scale(-1.0D, 1.0D);
/*  463 */       localAffineTransform.translate(-paramInt3, 0.0D);
/*  464 */       paintBackground(paramSynthContext, paramGraphics, 0, 0, paramInt3, paramInt4, localAffineTransform);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void paintComboBoxBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  482 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintDesktopIconBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  499 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintDesktopIconBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  516 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintDesktopPaneBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  533 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintDesktopPaneBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  550 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintEditorPaneBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  567 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintEditorPaneBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  584 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintFileChooserBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  601 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintFileChooserBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  618 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintFormattedTextFieldBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  635 */     if (paramSynthContext.getComponent().getComponentOrientation().isLeftToRight()) {
/*  636 */       paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */     } else {
/*  638 */       AffineTransform localAffineTransform = new AffineTransform();
/*  639 */       localAffineTransform.translate(paramInt1, paramInt2);
/*  640 */       localAffineTransform.scale(-1.0D, 1.0D);
/*  641 */       localAffineTransform.translate(-paramInt3, 0.0D);
/*  642 */       paintBackground(paramSynthContext, paramGraphics, 0, 0, paramInt3, paramInt4, localAffineTransform);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void paintFormattedTextFieldBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  660 */     if (paramSynthContext.getComponent().getComponentOrientation().isLeftToRight()) {
/*  661 */       paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */     } else {
/*  663 */       AffineTransform localAffineTransform = new AffineTransform();
/*  664 */       localAffineTransform.translate(paramInt1, paramInt2);
/*  665 */       localAffineTransform.scale(-1.0D, 1.0D);
/*  666 */       localAffineTransform.translate(-paramInt3, 0.0D);
/*  667 */       paintBorder(paramSynthContext, paramGraphics, 0, 0, paramInt3, paramInt4, localAffineTransform);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void paintInternalFrameTitlePaneBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  685 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintInternalFrameTitlePaneBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  702 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintInternalFrameBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  719 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintInternalFrameBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  736 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintLabelBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  753 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintLabelBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  770 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintListBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  787 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintListBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  804 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintMenuBarBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  821 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintMenuBarBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  838 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintMenuItemBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  855 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintMenuItemBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  872 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintMenuBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  889 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintMenuBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  906 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintOptionPaneBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  923 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintOptionPaneBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  940 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintPanelBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  957 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintPanelBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  974 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintPasswordFieldBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  991 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintPasswordFieldBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 1008 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintPopupMenuBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 1025 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintPopupMenuBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 1042 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintProgressBarBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 1059 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintProgressBarBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */   {
/* 1080 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */   }
/*      */ 
/*      */   public void paintProgressBarBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 1097 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintProgressBarBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */   {
/* 1118 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */   }
/*      */ 
/*      */   public void paintProgressBarForeground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */   {
/* 1138 */     paintForeground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */   }
/*      */ 
/*      */   public void paintRadioButtonMenuItemBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 1155 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintRadioButtonMenuItemBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 1172 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintRadioButtonBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 1189 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintRadioButtonBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 1206 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintRootPaneBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 1223 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintRootPaneBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 1240 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintScrollBarBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 1257 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintScrollBarBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */   {
/* 1279 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */   }
/*      */ 
/*      */   public void paintScrollBarBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 1296 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintScrollBarBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */   {
/* 1318 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */   }
/*      */ 
/*      */   public void paintScrollBarThumbBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */   {
/* 1340 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */   }
/*      */ 
/*      */   public void paintScrollBarThumbBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */   {
/* 1362 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */   }
/*      */ 
/*      */   public void paintScrollBarTrackBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 1380 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintScrollBarTrackBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */   {
/* 1403 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */   }
/*      */ 
/*      */   public void paintScrollBarTrackBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 1421 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintScrollBarTrackBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */   {
/* 1444 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */   }
/*      */ 
/*      */   public void paintScrollPaneBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 1461 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintScrollPaneBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 1478 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintSeparatorBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 1495 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintSeparatorBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */   {
/* 1516 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */   }
/*      */ 
/*      */   public void paintSeparatorBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 1533 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintSeparatorBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */   {
/* 1554 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */   }
/*      */ 
/*      */   public void paintSeparatorForeground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */   {
/* 1573 */     paintForeground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */   }
/*      */ 
/*      */   public void paintSliderBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 1590 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintSliderBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */   {
/* 1611 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */   }
/*      */ 
/*      */   public void paintSliderBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 1628 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintSliderBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */   {
/* 1649 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */   }
/*      */ 
/*      */   public void paintSliderThumbBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */   {
/* 1668 */     if (paramSynthContext.getComponent().getClientProperty("Slider.paintThumbArrowShape") == Boolean.TRUE)
/*      */     {
/* 1670 */       if (paramInt5 == 0)
/* 1671 */         paramInt5 = 1;
/*      */       else {
/* 1673 */         paramInt5 = 0;
/*      */       }
/* 1675 */       paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     } else {
/* 1677 */       paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void paintSliderThumbBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */   {
/* 1697 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */   }
/*      */ 
/*      */   public void paintSliderTrackBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 1714 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintSliderTrackBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */   {
/* 1735 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */   }
/*      */ 
/*      */   public void paintSliderTrackBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 1752 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintSliderTrackBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */   {
/* 1773 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */   }
/*      */ 
/*      */   public void paintSpinnerBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 1790 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintSpinnerBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 1807 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintSplitPaneDividerBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 1824 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintSplitPaneDividerBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */   {
/* 1845 */     if (paramInt5 == 1) {
/* 1846 */       AffineTransform localAffineTransform = new AffineTransform();
/* 1847 */       localAffineTransform.scale(-1.0D, 1.0D);
/* 1848 */       localAffineTransform.rotate(Math.toRadians(90.0D));
/* 1849 */       paintBackground(paramSynthContext, paramGraphics, paramInt2, paramInt1, paramInt4, paramInt3, localAffineTransform);
/*      */     } else {
/* 1851 */       paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void paintSplitPaneDividerForeground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */   {
/* 1871 */     paintForeground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintSplitPaneDragDivider(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */   {
/* 1891 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintSplitPaneBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 1908 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintSplitPaneBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 1925 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintTabbedPaneBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 1942 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintTabbedPaneBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 1959 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintTabbedPaneTabAreaBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 1976 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintTabbedPaneTabAreaBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */   {
/*      */     AffineTransform localAffineTransform;
/* 2000 */     if (paramInt5 == 2) {
/* 2001 */       localAffineTransform = new AffineTransform();
/* 2002 */       localAffineTransform.scale(-1.0D, 1.0D);
/* 2003 */       localAffineTransform.rotate(Math.toRadians(90.0D));
/* 2004 */       paintBackground(paramSynthContext, paramGraphics, paramInt2, paramInt1, paramInt4, paramInt3, localAffineTransform);
/* 2005 */     } else if (paramInt5 == 4) {
/* 2006 */       localAffineTransform = new AffineTransform();
/* 2007 */       localAffineTransform.rotate(Math.toRadians(90.0D));
/* 2008 */       localAffineTransform.translate(0.0D, -(paramInt1 + paramInt3));
/* 2009 */       paintBackground(paramSynthContext, paramGraphics, paramInt2, 0, paramInt4, paramInt3, localAffineTransform);
/* 2010 */     } else if (paramInt5 == 3) {
/* 2011 */       localAffineTransform = new AffineTransform();
/* 2012 */       localAffineTransform.translate(paramInt1, paramInt2);
/* 2013 */       localAffineTransform.scale(1.0D, -1.0D);
/* 2014 */       localAffineTransform.translate(0.0D, -paramInt4);
/* 2015 */       paintBackground(paramSynthContext, paramGraphics, 0, 0, paramInt3, paramInt4, localAffineTransform);
/*      */     } else {
/* 2017 */       paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void paintTabbedPaneTabAreaBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 2035 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintTabbedPaneTabAreaBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */   {
/* 2058 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintTabbedPaneTabBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */   {
/* 2076 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintTabbedPaneTabBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*      */   {
/*      */     AffineTransform localAffineTransform;
/* 2100 */     if (paramInt6 == 2) {
/* 2101 */       localAffineTransform = new AffineTransform();
/* 2102 */       localAffineTransform.scale(-1.0D, 1.0D);
/* 2103 */       localAffineTransform.rotate(Math.toRadians(90.0D));
/* 2104 */       paintBackground(paramSynthContext, paramGraphics, paramInt2, paramInt1, paramInt4, paramInt3, localAffineTransform);
/* 2105 */     } else if (paramInt6 == 4) {
/* 2106 */       localAffineTransform = new AffineTransform();
/* 2107 */       localAffineTransform.rotate(Math.toRadians(90.0D));
/* 2108 */       localAffineTransform.translate(0.0D, -(paramInt1 + paramInt3));
/* 2109 */       paintBackground(paramSynthContext, paramGraphics, paramInt2, 0, paramInt4, paramInt3, localAffineTransform);
/* 2110 */     } else if (paramInt6 == 3) {
/* 2111 */       localAffineTransform = new AffineTransform();
/* 2112 */       localAffineTransform.translate(paramInt1, paramInt2);
/* 2113 */       localAffineTransform.scale(1.0D, -1.0D);
/* 2114 */       localAffineTransform.translate(0.0D, -paramInt4);
/* 2115 */       paintBackground(paramSynthContext, paramGraphics, 0, 0, paramInt3, paramInt4, localAffineTransform);
/*      */     } else {
/* 2117 */       paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void paintTabbedPaneTabBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */   {
/* 2136 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintTabbedPaneTabBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*      */   {
/* 2160 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintTabbedPaneContentBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 2178 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintTabbedPaneContentBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 2195 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintTableHeaderBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 2212 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintTableHeaderBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 2229 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintTableBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 2246 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintTableBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 2263 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintTextAreaBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 2280 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintTextAreaBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 2297 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintTextPaneBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 2314 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintTextPaneBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 2331 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintTextFieldBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 2348 */     if (paramSynthContext.getComponent().getComponentOrientation().isLeftToRight()) {
/* 2349 */       paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */     } else {
/* 2351 */       AffineTransform localAffineTransform = new AffineTransform();
/* 2352 */       localAffineTransform.translate(paramInt1, paramInt2);
/* 2353 */       localAffineTransform.scale(-1.0D, 1.0D);
/* 2354 */       localAffineTransform.translate(-paramInt3, 0.0D);
/* 2355 */       paintBackground(paramSynthContext, paramGraphics, 0, 0, paramInt3, paramInt4, localAffineTransform);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void paintTextFieldBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 2373 */     if (paramSynthContext.getComponent().getComponentOrientation().isLeftToRight()) {
/* 2374 */       paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */     } else {
/* 2376 */       AffineTransform localAffineTransform = new AffineTransform();
/* 2377 */       localAffineTransform.translate(paramInt1, paramInt2);
/* 2378 */       localAffineTransform.scale(-1.0D, 1.0D);
/* 2379 */       localAffineTransform.translate(-paramInt3, 0.0D);
/* 2380 */       paintBorder(paramSynthContext, paramGraphics, 0, 0, paramInt3, paramInt4, localAffineTransform);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void paintToggleButtonBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 2398 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintToggleButtonBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 2415 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintToolBarBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 2432 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintToolBarBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */   {
/* 2453 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */   }
/*      */ 
/*      */   public void paintToolBarBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 2470 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintToolBarBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */   {
/* 2491 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */   }
/*      */ 
/*      */   public void paintToolBarContentBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 2508 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintToolBarContentBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */   {
/* 2529 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */   }
/*      */ 
/*      */   public void paintToolBarContentBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 2546 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintToolBarContentBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */   {
/* 2567 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */   }
/*      */ 
/*      */   public void paintToolBarDragWindowBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 2585 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintToolBarDragWindowBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */   {
/* 2607 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */   }
/*      */ 
/*      */   public void paintToolBarDragWindowBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 2625 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintToolBarDragWindowBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */   {
/* 2647 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */   }
/*      */ 
/*      */   public void paintToolTipBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 2664 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintToolTipBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 2681 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintTreeBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 2698 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintTreeBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 2715 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintTreeCellBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 2732 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintTreeCellBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 2749 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintTreeCellFocus(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void paintViewportBackground(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 2783 */     paintBackground(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   public void paintViewportBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 2800 */     paintBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.nimbus.SynthPainterImpl
 * JD-Core Version:    0.6.2
 */