/*     */ package javax.swing.plaf.nimbus;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Paint;
/*     */ import java.awt.Shape;
/*     */ import java.awt.geom.Ellipse2D;
/*     */ import java.awt.geom.Ellipse2D.Float;
/*     */ import java.awt.geom.Path2D;
/*     */ import java.awt.geom.Path2D.Float;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.awt.geom.Rectangle2D.Float;
/*     */ import java.awt.geom.RoundRectangle2D;
/*     */ import java.awt.geom.RoundRectangle2D.Float;
/*     */ import javax.swing.JComponent;
/*     */ 
/*     */ final class DesktopPanePainter extends AbstractRegionPainter
/*     */ {
/*     */   static final int BACKGROUND_ENABLED = 1;
/*     */   private int state;
/*     */   private AbstractRegionPainter.PaintContext ctx;
/*  46 */   private Path2D path = new Path2D.Float();
/*  47 */   private Rectangle2D rect = new Rectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*  48 */   private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
/*  49 */   private Ellipse2D ellipse = new Ellipse2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*     */ 
/*  54 */   private Color color1 = decodeColor("nimbusBase", -0.004577577F, -0.1286721F, 0.007843137F, 0);
/*  55 */   private Color color2 = decodeColor("nimbusBase", -0.006324589F, -0.08363098F, -0.172549F, 0);
/*  56 */   private Color color3 = decodeColor("nimbusBase", -0.0003688335F, -0.05676693F, -0.1019608F, 0);
/*  57 */   private Color color4 = decodeColor("nimbusBase", -0.008954704F, -0.126455F, -0.1254902F, 0);
/*  58 */   private Color color5 = new Color(255, 200, 0, 6);
/*  59 */   private Color color6 = decodeColor("nimbusBase", -8.028746E-005F, -0.08453322F, -0.05098042F, 0);
/*  60 */   private Color color7 = decodeColor("nimbusBase", -0.005205333F, -0.1226708F, -0.0980392F, 0);
/*  61 */   private Color color8 = decodeColor("nimbusBase", -0.01255971F, -0.1313665F, -0.0980392F, 0);
/*  62 */   private Color color9 = decodeColor("nimbusBase", -0.009207249F, -0.1398465F, -0.07450983F, 0);
/*  63 */   private Color color10 = decodeColor("nimbusBase", -0.01075047F, -0.1357143F, -0.1254902F, 0);
/*  64 */   private Color color11 = decodeColor("nimbusBase", -0.008476257F, -0.126786F, -0.1098039F, 0);
/*  65 */   private Color color12 = decodeColor("nimbusBase", -0.003488302F, -0.04269105F, -0.2117647F, 0);
/*  66 */   private Color color13 = decodeColor("nimbusBase", -0.01261395F, -0.1161065F, -0.1490196F, 0);
/*  67 */   private Color color14 = decodeColor("nimbusBase", -0.00382179F, -0.05238098F, -0.2196079F, 0);
/*     */   private Object[] componentColors;
/*     */ 
/*     */   public DesktopPanePainter(AbstractRegionPainter.PaintContext paramPaintContext, int paramInt)
/*     */   {
/*  75 */     this.state = paramInt;
/*  76 */     this.ctx = paramPaintContext;
/*     */   }
/*     */ 
/*     */   protected void doPaint(Graphics2D paramGraphics2D, JComponent paramJComponent, int paramInt1, int paramInt2, Object[] paramArrayOfObject)
/*     */   {
/*  82 */     this.componentColors = paramArrayOfObject;
/*     */ 
/*  85 */     switch (this.state) { case 1:
/*  86 */       paintBackgroundEnabled(paramGraphics2D);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final AbstractRegionPainter.PaintContext getPaintContext()
/*     */   {
/*  95 */     return this.ctx;
/*     */   }
/*     */ 
/*     */   private void paintBackgroundEnabled(Graphics2D paramGraphics2D) {
/*  99 */     this.path = decodePath1();
/* 100 */     paramGraphics2D.setPaint(decodeGradient1(this.path));
/* 101 */     paramGraphics2D.fill(this.path);
/* 102 */     this.path = decodePath2();
/* 103 */     paramGraphics2D.setPaint(decodeGradient2(this.path));
/* 104 */     paramGraphics2D.fill(this.path);
/* 105 */     this.path = decodePath3();
/* 106 */     paramGraphics2D.setPaint(this.color5);
/* 107 */     paramGraphics2D.fill(this.path);
/* 108 */     this.path = decodePath4();
/* 109 */     paramGraphics2D.setPaint(decodeGradient3(this.path));
/* 110 */     paramGraphics2D.fill(this.path);
/* 111 */     this.path = decodePath5();
/* 112 */     paramGraphics2D.setPaint(decodeGradient4(this.path));
/* 113 */     paramGraphics2D.fill(this.path);
/* 114 */     this.path = decodePath6();
/* 115 */     paramGraphics2D.setPaint(decodeGradient5(this.path));
/* 116 */     paramGraphics2D.fill(this.path);
/* 117 */     this.path = decodePath7();
/* 118 */     paramGraphics2D.setPaint(decodeGradient6(this.path));
/* 119 */     paramGraphics2D.fill(this.path);
/* 120 */     this.path = decodePath8();
/* 121 */     paramGraphics2D.setPaint(decodeGradient7(this.path));
/* 122 */     paramGraphics2D.fill(this.path);
/* 123 */     this.path = decodePath9();
/* 124 */     paramGraphics2D.setPaint(decodeGradient8(this.path));
/* 125 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private Path2D decodePath1()
/*     */   {
/* 132 */     this.path.reset();
/* 133 */     this.path.moveTo(decodeX(1.271667F), decodeY(2.0F));
/* 134 */     this.path.curveTo(decodeAnchorX(1.271667F, 0.0F), decodeAnchorY(2.0F, 0.5F), decodeAnchorX(1.128333F, 0.0F), decodeAnchorY(1.0F, 0.0F), decodeX(1.128333F), decodeY(1.0F));
/* 135 */     this.path.lineTo(decodeX(1.351667F), decodeY(1.0F));
/* 136 */     this.path.lineTo(decodeX(1.586667F), decodeY(1.575431F));
/* 137 */     this.path.lineTo(decodeX(1.541667F), decodeY(2.0F));
/* 138 */     this.path.curveTo(decodeAnchorX(1.541667F, 0.0F), decodeAnchorY(2.0F, 0.0F), decodeAnchorX(1.271667F, 0.0F), decodeAnchorY(2.0F, -0.5F), decodeX(1.271667F), decodeY(2.0F));
/* 139 */     this.path.closePath();
/* 140 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath2() {
/* 144 */     this.path.reset();
/* 145 */     this.path.moveTo(decodeX(1.788333F), decodeY(2.0F));
/* 146 */     this.path.curveTo(decodeAnchorX(1.788333F, 0.0F), decodeAnchorY(2.0F, 0.5F), decodeAnchorX(1.653333F, 0.0F), decodeAnchorY(1.773707F, 0.0F), decodeX(1.653333F), decodeY(1.773707F));
/* 147 */     this.path.lineTo(decodeX(2.0F), decodeY(1.146552F));
/* 148 */     this.path.curveTo(decodeAnchorX(2.0F, 0.0F), decodeAnchorY(1.146552F, 0.0F), decodeAnchorX(2.0F, 0.0F), decodeAnchorY(2.0F, -0.5F), decodeX(2.0F), decodeY(2.0F));
/* 149 */     this.path.curveTo(decodeAnchorX(2.0F, 0.5F), decodeAnchorY(2.0F, 0.5F), decodeAnchorX(1.788333F, 0.0F), decodeAnchorY(2.0F, -0.5F), decodeX(1.788333F), decodeY(2.0F));
/* 150 */     this.path.closePath();
/* 151 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath3() {
/* 155 */     this.path.reset();
/* 156 */     this.path.moveTo(decodeX(1.566667F), decodeY(1.0F));
/* 157 */     this.path.lineTo(decodeX(1.566667F), decodeY(1.568965F));
/* 158 */     this.path.lineTo(decodeX(1.675F), decodeY(1.771552F));
/* 159 */     this.path.curveTo(decodeAnchorX(1.675F, 0.0F), decodeAnchorY(1.771552F, 0.0F), decodeAnchorX(1.811667F, -23.5F), decodeAnchorY(1.497845F, 33.5F), decodeX(1.811667F), decodeY(1.497845F));
/* 160 */     this.path.curveTo(decodeAnchorX(1.811667F, 23.5F), decodeAnchorY(1.497845F, -33.5F), decodeAnchorX(2.0F, 0.0F), decodeAnchorY(1.200431F, 0.0F), decodeX(2.0F), decodeY(1.200431F));
/* 161 */     this.path.lineTo(decodeX(2.0F), decodeY(1.0F));
/* 162 */     this.path.lineTo(decodeX(1.566667F), decodeY(1.0F));
/* 163 */     this.path.closePath();
/* 164 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath4() {
/* 168 */     this.path.reset();
/* 169 */     this.path.moveTo(decodeX(1.338333F), decodeY(1.0F));
/* 170 */     this.path.curveTo(decodeAnchorX(1.338333F, 0.0F), decodeAnchorY(1.0F, 0.0F), decodeAnchorX(1.441667F, -21.0F), decodeAnchorY(1.310345F, -37.5F), decodeX(1.441667F), decodeY(1.310345F));
/* 171 */     this.path.curveTo(decodeAnchorX(1.441667F, 21.0F), decodeAnchorY(1.310345F, 37.5F), decodeAnchorX(1.573333F, 0.0F), decodeAnchorY(1.584052F, 0.0F), decodeX(1.573333F), decodeY(1.584052F));
/* 172 */     this.path.curveTo(decodeAnchorX(1.573333F, 0.0F), decodeAnchorY(1.584052F, 0.0F), decodeAnchorX(1.606667F, 1.5F), decodeAnchorY(1.241379F, 29.5F), decodeX(1.606667F), decodeY(1.241379F));
/* 173 */     this.path.curveTo(decodeAnchorX(1.606667F, -1.5F), decodeAnchorY(1.241379F, -29.5F), decodeAnchorX(1.605F, 0.0F), decodeAnchorY(1.0F, 0.0F), decodeX(1.605F), decodeY(1.0F));
/* 174 */     this.path.lineTo(decodeX(1.338333F), decodeY(1.0F));
/* 175 */     this.path.closePath();
/* 176 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath5() {
/* 180 */     this.path.reset();
/* 181 */     this.path.moveTo(decodeX(1.568333F), decodeY(1.579741F));
/* 182 */     this.path.curveTo(decodeAnchorX(1.568333F, 0.0F), decodeAnchorY(1.579741F, 0.0F), decodeAnchorX(1.575F, 0.0F), decodeAnchorY(1.239224F, 33.0F), decodeX(1.575F), decodeY(1.239224F));
/* 183 */     this.path.curveTo(decodeAnchorX(1.575F, 0.0F), decodeAnchorY(1.239224F, -33.0F), decodeAnchorX(1.561667F, 0.0F), decodeAnchorY(1.0F, 0.0F), decodeX(1.561667F), decodeY(1.0F));
/* 184 */     this.path.lineTo(decodeX(2.0F), decodeY(1.0F));
/* 185 */     this.path.lineTo(decodeX(2.0F), decodeY(1.198276F));
/* 186 */     this.path.curveTo(decodeAnchorX(2.0F, 0.0F), decodeAnchorY(1.198276F, 0.0F), decodeAnchorX(1.806667F, 27.5F), decodeAnchorY(1.50431F, -38.5F), decodeX(1.806667F), decodeY(1.50431F));
/* 187 */     this.path.curveTo(decodeAnchorX(1.806667F, -27.5F), decodeAnchorY(1.50431F, 38.5F), decodeAnchorX(1.676667F, 0.0F), decodeAnchorY(1.778017F, 0.0F), decodeX(1.676667F), decodeY(1.778017F));
/* 188 */     this.path.lineTo(decodeX(1.568333F), decodeY(1.579741F));
/* 189 */     this.path.closePath();
/* 190 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath6() {
/* 194 */     this.path.reset();
/* 195 */     this.path.moveTo(decodeX(1.521667F), decodeY(2.0F));
/* 196 */     this.path.curveTo(decodeAnchorX(1.521667F, 0.0F), decodeAnchorY(2.0F, 0.0F), decodeAnchorX(1.555F, -2.0F), decodeAnchorY(1.778017F, 22.5F), decodeX(1.555F), decodeY(1.778017F));
/* 197 */     this.path.curveTo(decodeAnchorX(1.555F, 2.0F), decodeAnchorY(1.778017F, -22.5F), decodeAnchorX(1.568333F, 0.0F), decodeAnchorY(1.576509F, 0.0F), decodeX(1.568333F), decodeY(1.576509F));
/* 198 */     this.path.lineTo(decodeX(1.6775F), decodeY(1.774785F));
/* 199 */     this.path.curveTo(decodeAnchorX(1.6775F, 0.0F), decodeAnchorY(1.774785F, 0.0F), decodeAnchorX(1.650833F, 6.0F), decodeAnchorY(1.892241F, -14.0F), decodeX(1.650833F), decodeY(1.892241F));
/* 200 */     this.path.curveTo(decodeAnchorX(1.650833F, -6.0F), decodeAnchorY(1.892241F, 14.0F), decodeAnchorX(1.608333F, 0.0F), decodeAnchorY(2.0F, 0.0F), decodeX(1.608333F), decodeY(2.0F));
/* 201 */     this.path.lineTo(decodeX(1.521667F), decodeY(2.0F));
/* 202 */     this.path.closePath();
/* 203 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath7() {
/* 207 */     this.path.reset();
/* 208 */     this.path.moveTo(decodeX(1.606667F), decodeY(2.0F));
/* 209 */     this.path.curveTo(decodeAnchorX(1.606667F, 0.0F), decodeAnchorY(2.0F, 0.0F), decodeAnchorX(1.64F, -7.0F), decodeAnchorY(1.881465F, 17.0F), decodeX(1.64F), decodeY(1.881465F));
/* 210 */     this.path.curveTo(decodeAnchorX(1.64F, 7.0F), decodeAnchorY(1.881465F, -17.0F), decodeAnchorX(1.6775F, 0.0F), decodeAnchorY(1.774785F, 0.0F), decodeX(1.6775F), decodeY(1.774785F));
/* 211 */     this.path.curveTo(decodeAnchorX(1.6775F, 0.0F), decodeAnchorY(1.774785F, 0.0F), decodeAnchorX(1.741667F, -11.0F), decodeAnchorY(1.883621F, -15.0F), decodeX(1.741667F), decodeY(1.883621F));
/* 212 */     this.path.curveTo(decodeAnchorX(1.741667F, 11.0F), decodeAnchorY(1.883621F, 15.0F), decodeAnchorX(1.813333F, 0.0F), decodeAnchorY(2.0F, -0.5F), decodeX(1.813333F), decodeY(2.0F));
/* 213 */     this.path.curveTo(decodeAnchorX(1.813333F, 0.0F), decodeAnchorY(2.0F, 0.5F), decodeAnchorX(1.606667F, 0.0F), decodeAnchorY(2.0F, 0.0F), decodeX(1.606667F), decodeY(2.0F));
/* 214 */     this.path.closePath();
/* 215 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath8() {
/* 219 */     this.path.reset();
/* 220 */     this.path.moveTo(decodeX(1.273333F), decodeY(2.0F));
/* 221 */     this.path.curveTo(decodeAnchorX(1.273333F, 0.0F), decodeAnchorY(2.0F, 0.0F), decodeAnchorX(1.263333F, 5.0F), decodeAnchorY(1.659483F, 37.0F), decodeX(1.263333F), decodeY(1.659483F));
/* 222 */     this.path.curveTo(decodeAnchorX(1.263333F, -5.0F), decodeAnchorY(1.659483F, -37.0F), decodeAnchorX(1.193333F, 9.0F), decodeAnchorY(1.224138F, 33.5F), decodeX(1.193333F), decodeY(1.224138F));
/* 223 */     this.path.curveTo(decodeAnchorX(1.193333F, -9.0F), decodeAnchorY(1.224138F, -33.5F), decodeAnchorX(1.133333F, 0.0F), decodeAnchorY(1.0F, 0.0F), decodeX(1.133333F), decodeY(1.0F));
/* 224 */     this.path.lineTo(decodeX(1.0F), decodeY(1.0F));
/* 225 */     this.path.lineTo(decodeX(1.0F), decodeY(1.612069F));
/* 226 */     this.path.curveTo(decodeAnchorX(1.0F, 0.0F), decodeAnchorY(1.612069F, 0.0F), decodeAnchorX(1.15F, 0.0F), decodeAnchorY(2.0F, -0.5F), decodeX(1.15F), decodeY(2.0F));
/* 227 */     this.path.curveTo(decodeAnchorX(1.15F, 0.0F), decodeAnchorY(2.0F, 0.5F), decodeAnchorX(1.273333F, 0.0F), decodeAnchorY(2.0F, 0.0F), decodeX(1.273333F), decodeY(2.0F));
/* 228 */     this.path.closePath();
/* 229 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath9() {
/* 233 */     this.path.reset();
/* 234 */     this.path.moveTo(decodeX(1.0F), decodeY(2.0F));
/* 235 */     this.path.lineTo(decodeX(1.0F), decodeY(1.596983F));
/* 236 */     this.path.curveTo(decodeAnchorX(1.0F, 0.0F), decodeAnchorY(1.596983F, 0.0F), decodeAnchorX(1.073333F, -10.0F), decodeAnchorY(1.797414F, -19.5F), decodeX(1.073333F), decodeY(1.797414F));
/* 237 */     this.path.curveTo(decodeAnchorX(1.073333F, 10.0F), decodeAnchorY(1.797414F, 19.5F), decodeAnchorX(1.166667F, 0.0F), decodeAnchorY(2.0F, -0.5F), decodeX(1.166667F), decodeY(2.0F));
/* 238 */     this.path.curveTo(decodeAnchorX(1.166667F, 0.0F), decodeAnchorY(2.0F, 0.5F), decodeAnchorX(1.0F, 0.0F), decodeAnchorY(2.0F, 0.0F), decodeX(1.0F), decodeY(2.0F));
/* 239 */     this.path.closePath();
/* 240 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient1(Shape paramShape)
/*     */   {
/* 246 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 247 */     float f1 = (float)localRectangle2D.getX();
/* 248 */     float f2 = (float)localRectangle2D.getY();
/* 249 */     float f3 = (float)localRectangle2D.getWidth();
/* 250 */     float f4 = (float)localRectangle2D.getHeight();
/* 251 */     return decodeGradient(0.25F * f3 + f1, 0.0F * f4 + f2, 0.75F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color1, decodeColor(this.color1, this.color2, 0.5F), this.color2 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient2(Shape paramShape)
/*     */   {
/* 259 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 260 */     float f1 = (float)localRectangle2D.getX();
/* 261 */     float f2 = (float)localRectangle2D.getY();
/* 262 */     float f3 = (float)localRectangle2D.getWidth();
/* 263 */     float f4 = (float)localRectangle2D.getHeight();
/* 264 */     return decodeGradient(0.9567308F * f3 + f1, 0.06835443F * f4 + f2, 0.75F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color3, decodeColor(this.color3, this.color4, 0.5F), this.color4 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient3(Shape paramShape)
/*     */   {
/* 272 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 273 */     float f1 = (float)localRectangle2D.getX();
/* 274 */     float f2 = (float)localRectangle2D.getY();
/* 275 */     float f3 = (float)localRectangle2D.getWidth();
/* 276 */     float f4 = (float)localRectangle2D.getHeight();
/* 277 */     return decodeGradient(0.25F * f3 + f1, 0.0F * f4 + f2, 0.8353658F * f3 + f1, 0.952206F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color6, decodeColor(this.color6, this.color7, 0.5F), this.color7 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient4(Shape paramShape)
/*     */   {
/* 285 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 286 */     float f1 = (float)localRectangle2D.getX();
/* 287 */     float f2 = (float)localRectangle2D.getY();
/* 288 */     float f3 = (float)localRectangle2D.getWidth();
/* 289 */     float f4 = (float)localRectangle2D.getHeight();
/* 290 */     return decodeGradient(0.86597F * f3 + f1, 0.01104972F * f4 + f2, 0.2480989F * f3 + f1, 0.9502763F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color6, decodeColor(this.color6, this.color8, 0.5F), this.color8 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient5(Shape paramShape)
/*     */   {
/* 298 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 299 */     float f1 = (float)localRectangle2D.getX();
/* 300 */     float f2 = (float)localRectangle2D.getY();
/* 301 */     float f3 = (float)localRectangle2D.getWidth();
/* 302 */     float f4 = (float)localRectangle2D.getHeight();
/* 303 */     return decodeGradient(0.351124F * f3 + f1, 0.09326425F * f4 + f2, 0.3342697F * f3 + f1, 0.9846154F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color9, decodeColor(this.color9, this.color10, 0.5F), this.color10 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient6(Shape paramShape)
/*     */   {
/* 311 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 312 */     float f1 = (float)localRectangle2D.getX();
/* 313 */     float f2 = (float)localRectangle2D.getY();
/* 314 */     float f3 = (float)localRectangle2D.getWidth();
/* 315 */     float f4 = (float)localRectangle2D.getHeight();
/* 316 */     return decodeGradient(0.354839F * f3 + f1, 0.1142857F * f4 + f2, 0.483871F * f3 + f1, 0.9809524F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color11, decodeColor(this.color11, this.color4, 0.5F), this.color4 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient7(Shape paramShape)
/*     */   {
/* 324 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 325 */     float f1 = (float)localRectangle2D.getX();
/* 326 */     float f2 = (float)localRectangle2D.getY();
/* 327 */     float f3 = (float)localRectangle2D.getWidth();
/* 328 */     float f4 = (float)localRectangle2D.getHeight();
/* 329 */     return decodeGradient(0.25F * f3 + f1, 0.0F * f4 + f2, 0.75F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color1, decodeColor(this.color1, this.color12, 0.5F), this.color12 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient8(Shape paramShape)
/*     */   {
/* 337 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 338 */     float f1 = (float)localRectangle2D.getX();
/* 339 */     float f2 = (float)localRectangle2D.getY();
/* 340 */     float f3 = (float)localRectangle2D.getWidth();
/* 341 */     float f4 = (float)localRectangle2D.getHeight();
/* 342 */     return decodeGradient(0.25F * f3 + f1, 0.0F * f4 + f2, 0.75F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color13, decodeColor(this.color13, this.color14, 0.5F), this.color14 });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.nimbus.DesktopPanePainter
 * JD-Core Version:    0.6.2
 */