/*     */ package java.awt.image.renderable;
/*     */ 
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.awt.image.RenderedImage;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class RenderableImageOp
/*     */   implements RenderableImage
/*     */ {
/*     */   ParameterBlock paramBlock;
/*     */   ContextualRenderedImageFactory myCRIF;
/*     */   Rectangle2D boundingBox;
/*     */ 
/*     */   public RenderableImageOp(ContextualRenderedImageFactory paramContextualRenderedImageFactory, ParameterBlock paramParameterBlock)
/*     */   {
/*  74 */     this.myCRIF = paramContextualRenderedImageFactory;
/*  75 */     this.paramBlock = ((ParameterBlock)paramParameterBlock.clone());
/*     */   }
/*     */ 
/*     */   public Vector<RenderableImage> getSources()
/*     */   {
/*  87 */     return getRenderableSources();
/*     */   }
/*     */ 
/*     */   private Vector getRenderableSources() {
/*  91 */     Vector localVector = null;
/*     */ 
/*  93 */     if (this.paramBlock.getNumSources() > 0) {
/*  94 */       localVector = new Vector();
/*  95 */       int i = 0;
/*  96 */       while (i < this.paramBlock.getNumSources()) {
/*  97 */         Object localObject = this.paramBlock.getSource(i);
/*  98 */         if (!(localObject instanceof RenderableImage)) break;
/*  99 */         localVector.add((RenderableImage)localObject);
/* 100 */         i++;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 106 */     return localVector;
/*     */   }
/*     */ 
/*     */   public Object getProperty(String paramString)
/*     */   {
/* 119 */     return this.myCRIF.getProperty(this.paramBlock, paramString);
/*     */   }
/*     */ 
/*     */   public String[] getPropertyNames()
/*     */   {
/* 127 */     return this.myCRIF.getPropertyNames();
/*     */   }
/*     */ 
/*     */   public boolean isDynamic()
/*     */   {
/* 141 */     return this.myCRIF.isDynamic();
/*     */   }
/*     */ 
/*     */   public float getWidth()
/*     */   {
/* 152 */     if (this.boundingBox == null) {
/* 153 */       this.boundingBox = this.myCRIF.getBounds2D(this.paramBlock);
/*     */     }
/* 155 */     return (float)this.boundingBox.getWidth();
/*     */   }
/*     */ 
/*     */   public float getHeight()
/*     */   {
/* 165 */     if (this.boundingBox == null) {
/* 166 */       this.boundingBox = this.myCRIF.getBounds2D(this.paramBlock);
/*     */     }
/* 168 */     return (float)this.boundingBox.getHeight();
/*     */   }
/*     */ 
/*     */   public float getMinX()
/*     */   {
/* 175 */     if (this.boundingBox == null) {
/* 176 */       this.boundingBox = this.myCRIF.getBounds2D(this.paramBlock);
/*     */     }
/* 178 */     return (float)this.boundingBox.getMinX();
/*     */   }
/*     */ 
/*     */   public float getMinY()
/*     */   {
/* 185 */     if (this.boundingBox == null) {
/* 186 */       this.boundingBox = this.myCRIF.getBounds2D(this.paramBlock);
/*     */     }
/* 188 */     return (float)this.boundingBox.getMinY();
/*     */   }
/*     */ 
/*     */   public ParameterBlock setParameterBlock(ParameterBlock paramParameterBlock)
/*     */   {
/* 202 */     ParameterBlock localParameterBlock = this.paramBlock;
/* 203 */     this.paramBlock = ((ParameterBlock)paramParameterBlock.clone());
/* 204 */     return localParameterBlock;
/*     */   }
/*     */ 
/*     */   public ParameterBlock getParameterBlock()
/*     */   {
/* 214 */     return this.paramBlock;
/*     */   }
/*     */ 
/*     */   public RenderedImage createScaledRendering(int paramInt1, int paramInt2, RenderingHints paramRenderingHints)
/*     */   {
/* 245 */     double d1 = paramInt1 / getWidth();
/* 246 */     double d2 = paramInt2 / getHeight();
/* 247 */     if (Math.abs(d1 / d2 - 1.0D) < 0.01D) {
/* 248 */       d1 = d2;
/*     */     }
/* 250 */     AffineTransform localAffineTransform = AffineTransform.getScaleInstance(d1, d2);
/* 251 */     RenderContext localRenderContext = new RenderContext(localAffineTransform, paramRenderingHints);
/* 252 */     return createRendering(localRenderContext);
/*     */   }
/*     */ 
/*     */   public RenderedImage createDefaultRendering()
/*     */   {
/* 266 */     AffineTransform localAffineTransform = new AffineTransform();
/* 267 */     RenderContext localRenderContext = new RenderContext(localAffineTransform);
/* 268 */     return createRendering(localRenderContext);
/*     */   }
/*     */ 
/*     */   public RenderedImage createRendering(RenderContext paramRenderContext)
/*     */   {
/* 310 */     Object localObject = null;
/* 311 */     RenderContext localRenderContext = null;
/*     */ 
/* 316 */     ParameterBlock localParameterBlock = (ParameterBlock)this.paramBlock.clone();
/* 317 */     Vector localVector1 = getRenderableSources();
/*     */     try
/*     */     {
/* 323 */       if (localVector1 != null) {
/* 324 */         Vector localVector2 = new Vector();
/* 325 */         for (int i = 0; i < localVector1.size(); i++) {
/* 326 */           localRenderContext = this.myCRIF.mapRenderContext(i, paramRenderContext, this.paramBlock, this);
/*     */ 
/* 328 */           RenderedImage localRenderedImage = ((RenderableImage)localVector1.elementAt(i)).createRendering(localRenderContext);
/*     */ 
/* 330 */           if (localRenderedImage == null) {
/* 331 */             return null;
/*     */           }
/*     */ 
/* 336 */           localVector2.addElement(localRenderedImage);
/*     */         }
/*     */ 
/* 339 */         if (localVector2.size() > 0) {
/* 340 */           localParameterBlock.setSources(localVector2);
/*     */         }
/*     */       }
/*     */ 
/* 344 */       return this.myCRIF.create(paramRenderContext, localParameterBlock);
/*     */     } catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
/*     */     }
/* 347 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.image.renderable.RenderableImageOp
 * JD-Core Version:    0.6.2
 */