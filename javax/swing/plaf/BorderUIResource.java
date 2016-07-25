/*     */ package javax.swing.plaf;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Font;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.beans.ConstructorProperties;
/*     */ import java.io.Serializable;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.border.BevelBorder;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.border.CompoundBorder;
/*     */ import javax.swing.border.EmptyBorder;
/*     */ import javax.swing.border.EtchedBorder;
/*     */ import javax.swing.border.LineBorder;
/*     */ import javax.swing.border.MatteBorder;
/*     */ import javax.swing.border.TitledBorder;
/*     */ 
/*     */ public class BorderUIResource
/*     */   implements Border, UIResource, Serializable
/*     */ {
/*     */   static Border etched;
/*     */   static Border loweredBevel;
/*     */   static Border raisedBevel;
/*     */   static Border blackLine;
/*     */   private Border delegate;
/*     */ 
/*     */   public static Border getEtchedBorderUIResource()
/*     */   {
/*  70 */     if (etched == null) {
/*  71 */       etched = new EtchedBorderUIResource();
/*     */     }
/*  73 */     return etched;
/*     */   }
/*     */ 
/*     */   public static Border getLoweredBevelBorderUIResource() {
/*  77 */     if (loweredBevel == null) {
/*  78 */       loweredBevel = new BevelBorderUIResource(1);
/*     */     }
/*  80 */     return loweredBevel;
/*     */   }
/*     */ 
/*     */   public static Border getRaisedBevelBorderUIResource() {
/*  84 */     if (raisedBevel == null) {
/*  85 */       raisedBevel = new BevelBorderUIResource(0);
/*     */     }
/*  87 */     return raisedBevel;
/*     */   }
/*     */ 
/*     */   public static Border getBlackLineBorderUIResource() {
/*  91 */     if (blackLine == null) {
/*  92 */       blackLine = new LineBorderUIResource(Color.black);
/*     */     }
/*  94 */     return blackLine;
/*     */   }
/*     */ 
/*     */   public BorderUIResource(Border paramBorder)
/*     */   {
/* 105 */     if (paramBorder == null) {
/* 106 */       throw new IllegalArgumentException("null border delegate argument");
/*     */     }
/* 108 */     this.delegate = paramBorder;
/*     */   }
/*     */ 
/*     */   public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 113 */     this.delegate.paintBorder(paramComponent, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */   }
/*     */ 
/*     */   public Insets getBorderInsets(Component paramComponent) {
/* 117 */     return this.delegate.getBorderInsets(paramComponent);
/*     */   }
/*     */ 
/*     */   public boolean isBorderOpaque() {
/* 121 */     return this.delegate.isBorderOpaque();
/*     */   }
/*     */ 
/*     */   public static class BevelBorderUIResource extends BevelBorder
/*     */     implements UIResource
/*     */   {
/*     */     public BevelBorderUIResource(int paramInt)
/*     */     {
/* 159 */       super();
/*     */     }
/*     */ 
/*     */     public BevelBorderUIResource(int paramInt, Color paramColor1, Color paramColor2) {
/* 163 */       super(paramColor1, paramColor2);
/*     */     }
/*     */ 
/*     */     @ConstructorProperties({"bevelType", "highlightOuterColor", "highlightInnerColor", "shadowOuterColor", "shadowInnerColor"})
/*     */     public BevelBorderUIResource(int paramInt, Color paramColor1, Color paramColor2, Color paramColor3, Color paramColor4)
/*     */     {
/* 170 */       super(paramColor1, paramColor2, paramColor3, paramColor4);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class CompoundBorderUIResource extends CompoundBorder
/*     */     implements UIResource
/*     */   {
/*     */     @ConstructorProperties({"outsideBorder", "insideBorder"})
/*     */     public CompoundBorderUIResource(Border paramBorder1, Border paramBorder2)
/*     */     {
/* 127 */       super(paramBorder2);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class EmptyBorderUIResource extends EmptyBorder implements UIResource
/*     */   {
/*     */     public EmptyBorderUIResource(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     {
/* 135 */       super(paramInt2, paramInt3, paramInt4);
/*     */     }
/*     */     @ConstructorProperties({"borderInsets"})
/*     */     public EmptyBorderUIResource(Insets paramInsets) {
/* 139 */       super();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class EtchedBorderUIResource extends EtchedBorder
/*     */     implements UIResource
/*     */   {
/*     */     public EtchedBorderUIResource()
/*     */     {
/*     */     }
/*     */ 
/*     */     public EtchedBorderUIResource(int paramInt)
/*     */     {
/* 181 */       super();
/*     */     }
/*     */ 
/*     */     public EtchedBorderUIResource(Color paramColor1, Color paramColor2) {
/* 185 */       super(paramColor2);
/*     */     }
/*     */ 
/*     */     @ConstructorProperties({"etchType", "highlightColor", "shadowColor"})
/*     */     public EtchedBorderUIResource(int paramInt, Color paramColor1, Color paramColor2) {
/* 190 */       super(paramColor1, paramColor2);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class LineBorderUIResource extends LineBorder
/*     */     implements UIResource
/*     */   {
/*     */     public LineBorderUIResource(Color paramColor)
/*     */     {
/* 146 */       super();
/*     */     }
/*     */ 
/*     */     @ConstructorProperties({"lineColor", "thickness"})
/*     */     public LineBorderUIResource(Color paramColor, int paramInt) {
/* 151 */       super(paramInt);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class MatteBorderUIResource extends MatteBorder
/*     */     implements UIResource
/*     */   {
/*     */     public MatteBorderUIResource(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Color paramColor)
/*     */     {
/* 198 */       super(paramInt2, paramInt3, paramInt4, paramColor);
/*     */     }
/*     */ 
/*     */     public MatteBorderUIResource(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Icon paramIcon)
/*     */     {
/* 203 */       super(paramInt2, paramInt3, paramInt4, paramIcon);
/*     */     }
/*     */ 
/*     */     public MatteBorderUIResource(Icon paramIcon) {
/* 207 */       super();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class TitledBorderUIResource extends TitledBorder implements UIResource
/*     */   {
/*     */     public TitledBorderUIResource(String paramString) {
/* 214 */       super();
/*     */     }
/*     */ 
/*     */     public TitledBorderUIResource(Border paramBorder) {
/* 218 */       super();
/*     */     }
/*     */ 
/*     */     public TitledBorderUIResource(Border paramBorder, String paramString) {
/* 222 */       super(paramString);
/*     */     }
/*     */ 
/*     */     public TitledBorderUIResource(Border paramBorder, String paramString, int paramInt1, int paramInt2)
/*     */     {
/* 229 */       super(paramString, paramInt1, paramInt2);
/*     */     }
/*     */ 
/*     */     public TitledBorderUIResource(Border paramBorder, String paramString, int paramInt1, int paramInt2, Font paramFont)
/*     */     {
/* 237 */       super(paramString, paramInt1, paramInt2, paramFont);
/*     */     }
/*     */ 
/*     */     @ConstructorProperties({"border", "title", "titleJustification", "titlePosition", "titleFont", "titleColor"})
/*     */     public TitledBorderUIResource(Border paramBorder, String paramString, int paramInt1, int paramInt2, Font paramFont, Color paramColor)
/*     */     {
/* 247 */       super(paramString, paramInt1, paramInt2, paramFont, paramColor);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.BorderUIResource
 * JD-Core Version:    0.6.2
 */