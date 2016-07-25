/*     */ package javax.swing.plaf.nimbus;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Image;
/*     */ import java.awt.image.BufferedImage;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JMenu;
/*     */ import javax.swing.JToolBar;
/*     */ import javax.swing.Painter;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.plaf.synth.SynthContext;
/*     */ import javax.swing.plaf.synth.SynthStyle;
/*     */ import sun.swing.plaf.synth.SynthIcon;
/*     */ 
/*     */ class NimbusIcon extends SynthIcon
/*     */ {
/*     */   private int width;
/*     */   private int height;
/*     */   private String prefix;
/*     */   private String key;
/*     */ 
/*     */   NimbusIcon(String paramString1, String paramString2, int paramInt1, int paramInt2)
/*     */   {
/*  47 */     this.width = paramInt1;
/*  48 */     this.height = paramInt2;
/*  49 */     this.prefix = paramString1;
/*  50 */     this.key = paramString2;
/*     */   }
/*     */ 
/*     */   public void paintIcon(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/*  56 */     Painter localPainter = null;
/*  57 */     if (paramSynthContext != null) {
/*  58 */       localPainter = (Painter)paramSynthContext.getStyle().get(paramSynthContext, this.key);
/*     */     }
/*  60 */     if (localPainter == null) {
/*  61 */       localPainter = (Painter)UIManager.get(this.prefix + "[Enabled]." + this.key);
/*     */     }
/*     */ 
/*  64 */     if ((localPainter != null) && (paramSynthContext != null)) {
/*  65 */       JComponent localJComponent = paramSynthContext.getComponent();
/*  66 */       int i = 0;
/*  67 */       int j = 0;
/*     */ 
/*  71 */       int k = 0;
/*  72 */       int m = 0;
/*     */       Object localObject1;
/*     */       Object localObject2;
/*  73 */       if ((localJComponent instanceof JToolBar)) {
/*  74 */         localObject1 = (JToolBar)localJComponent;
/*  75 */         i = ((JToolBar)localObject1).getOrientation() == 1 ? 1 : 0;
/*  76 */         j = !((JToolBar)localObject1).getComponentOrientation().isLeftToRight() ? 1 : 0;
/*  77 */         localObject2 = NimbusLookAndFeel.resolveToolbarConstraint((JToolBar)localObject1);
/*     */ 
/*  80 */         if ((((JToolBar)localObject1).getBorder() instanceof UIResource)) {
/*  81 */           if (localObject2 == "South")
/*  82 */             m = 1;
/*  83 */           else if (localObject2 == "East")
/*  84 */             k = 1;
/*     */         }
/*     */       }
/*  87 */       else if ((localJComponent instanceof JMenu)) {
/*  88 */         j = !localJComponent.getComponentOrientation().isLeftToRight() ? 1 : 0;
/*     */       }
/*  90 */       if ((paramGraphics instanceof Graphics2D)) {
/*  91 */         localObject1 = (Graphics2D)paramGraphics;
/*  92 */         ((Graphics2D)localObject1).translate(paramInt1, paramInt2);
/*  93 */         ((Graphics2D)localObject1).translate(k, m);
/*  94 */         if (i != 0) {
/*  95 */           ((Graphics2D)localObject1).rotate(Math.toRadians(90.0D));
/*  96 */           ((Graphics2D)localObject1).translate(0, -paramInt3);
/*  97 */           localPainter.paint((Graphics2D)localObject1, paramSynthContext.getComponent(), paramInt4, paramInt3);
/*  98 */           ((Graphics2D)localObject1).translate(0, paramInt3);
/*  99 */           ((Graphics2D)localObject1).rotate(Math.toRadians(-90.0D));
/* 100 */         } else if (j != 0) {
/* 101 */           ((Graphics2D)localObject1).scale(-1.0D, 1.0D);
/* 102 */           ((Graphics2D)localObject1).translate(-paramInt3, 0);
/* 103 */           localPainter.paint((Graphics2D)localObject1, paramSynthContext.getComponent(), paramInt3, paramInt4);
/* 104 */           ((Graphics2D)localObject1).translate(paramInt3, 0);
/* 105 */           ((Graphics2D)localObject1).scale(-1.0D, 1.0D);
/*     */         } else {
/* 107 */           localPainter.paint((Graphics2D)localObject1, paramSynthContext.getComponent(), paramInt3, paramInt4);
/*     */         }
/* 109 */         ((Graphics2D)localObject1).translate(-k, -m);
/* 110 */         ((Graphics2D)localObject1).translate(-paramInt1, -paramInt2);
/*     */       }
/*     */       else
/*     */       {
/* 114 */         localObject1 = new BufferedImage(paramInt3, paramInt4, 2);
/*     */ 
/* 116 */         localObject2 = ((BufferedImage)localObject1).createGraphics();
/* 117 */         if (i != 0) {
/* 118 */           ((Graphics2D)localObject2).rotate(Math.toRadians(90.0D));
/* 119 */           ((Graphics2D)localObject2).translate(0, -paramInt3);
/* 120 */           localPainter.paint((Graphics2D)localObject2, paramSynthContext.getComponent(), paramInt4, paramInt3);
/* 121 */         } else if (j != 0) {
/* 122 */           ((Graphics2D)localObject2).scale(-1.0D, 1.0D);
/* 123 */           ((Graphics2D)localObject2).translate(-paramInt3, 0);
/* 124 */           localPainter.paint((Graphics2D)localObject2, paramSynthContext.getComponent(), paramInt3, paramInt4);
/*     */         } else {
/* 126 */           localPainter.paint((Graphics2D)localObject2, paramSynthContext.getComponent(), paramInt3, paramInt4);
/*     */         }
/* 128 */         ((Graphics2D)localObject2).dispose();
/* 129 */         paramGraphics.drawImage((Image)localObject1, paramInt1, paramInt2, null);
/* 130 */         localObject1 = null;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2)
/*     */   {
/* 143 */     Painter localPainter = (Painter)UIManager.get(this.prefix + "[Enabled]." + this.key);
/* 144 */     if (localPainter != null) {
/* 145 */       Object localObject = (paramComponent instanceof JComponent) ? (JComponent)paramComponent : null;
/* 146 */       Graphics2D localGraphics2D = (Graphics2D)paramGraphics;
/* 147 */       localGraphics2D.translate(paramInt1, paramInt2);
/* 148 */       localPainter.paint(localGraphics2D, localObject, this.width, this.height);
/* 149 */       localGraphics2D.translate(-paramInt1, -paramInt2);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getIconWidth(SynthContext paramSynthContext)
/*     */   {
/* 155 */     if (paramSynthContext == null) {
/* 156 */       return this.width;
/*     */     }
/* 158 */     JComponent localJComponent = paramSynthContext.getComponent();
/* 159 */     if (((localJComponent instanceof JToolBar)) && (((JToolBar)localJComponent).getOrientation() == 1))
/*     */     {
/* 162 */       if ((localJComponent.getBorder() instanceof UIResource)) {
/* 163 */         return localJComponent.getWidth() - 1;
/*     */       }
/* 165 */       return localJComponent.getWidth();
/*     */     }
/*     */ 
/* 168 */     return scale(paramSynthContext, this.width);
/*     */   }
/*     */ 
/*     */   public int getIconHeight(SynthContext paramSynthContext)
/*     */   {
/* 174 */     if (paramSynthContext == null) {
/* 175 */       return this.height;
/*     */     }
/* 177 */     JComponent localJComponent = paramSynthContext.getComponent();
/* 178 */     if ((localJComponent instanceof JToolBar)) {
/* 179 */       JToolBar localJToolBar = (JToolBar)localJComponent;
/* 180 */       if (localJToolBar.getOrientation() == 0)
/*     */       {
/* 183 */         if ((localJToolBar.getBorder() instanceof UIResource)) {
/* 184 */           return localJComponent.getHeight() - 1;
/*     */         }
/* 186 */         return localJComponent.getHeight();
/*     */       }
/*     */ 
/* 189 */       return scale(paramSynthContext, this.width);
/*     */     }
/*     */ 
/* 192 */     return scale(paramSynthContext, this.height);
/*     */   }
/*     */ 
/*     */   private int scale(SynthContext paramSynthContext, int paramInt)
/*     */   {
/* 206 */     if ((paramSynthContext == null) || (paramSynthContext.getComponent() == null)) {
/* 207 */       return paramInt;
/*     */     }
/*     */ 
/* 210 */     String str = (String)paramSynthContext.getComponent().getClientProperty("JComponent.sizeVariant");
/*     */ 
/* 212 */     if (str != null) {
/* 213 */       if ("large".equals(str))
/* 214 */         paramInt = (int)(paramInt * 1.15D);
/* 215 */       else if ("small".equals(str))
/* 216 */         paramInt = (int)(paramInt * 0.857D);
/* 217 */       else if ("mini".equals(str))
/*     */       {
/* 220 */         paramInt = (int)(paramInt * 0.784D);
/*     */       }
/*     */     }
/* 223 */     return paramInt;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.nimbus.NimbusIcon
 * JD-Core Version:    0.6.2
 */