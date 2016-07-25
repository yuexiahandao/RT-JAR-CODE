/*     */ package sun.java2d.opengl;
/*     */ 
/*     */ import sun.java2d.pipe.BufferedContext;
/*     */ import sun.java2d.pipe.RenderBuffer;
/*     */ import sun.java2d.pipe.RenderQueue;
/*     */ import sun.java2d.pipe.hw.ContextCapabilities;
/*     */ 
/*     */ public class OGLContext extends BufferedContext
/*     */ {
/*     */   private final OGLGraphicsConfig config;
/*     */ 
/*     */   OGLContext(RenderQueue paramRenderQueue, OGLGraphicsConfig paramOGLGraphicsConfig)
/*     */   {
/*  44 */     super(paramRenderQueue);
/*  45 */     this.config = paramOGLGraphicsConfig;
/*     */   }
/*     */ 
/*     */   static void setScratchSurface(OGLGraphicsConfig paramOGLGraphicsConfig)
/*     */   {
/*  52 */     setScratchSurface(paramOGLGraphicsConfig.getNativeConfigInfo());
/*     */   }
/*     */ 
/*     */   static void setScratchSurface(long paramLong)
/*     */   {
/*  76 */     currentContext = null;
/*     */ 
/*  79 */     OGLRenderQueue localOGLRenderQueue = OGLRenderQueue.getInstance();
/*  80 */     RenderBuffer localRenderBuffer = localOGLRenderQueue.getBuffer();
/*  81 */     localOGLRenderQueue.ensureCapacityAndAlignment(12, 4);
/*  82 */     localRenderBuffer.putInt(71);
/*  83 */     localRenderBuffer.putLong(paramLong);
/*     */   }
/*     */ 
/*     */   static void invalidateCurrentContext()
/*     */   {
/*  98 */     if (currentContext != null) {
/*  99 */       currentContext.invalidateContext();
/* 100 */       currentContext = null;
/*     */     }
/*     */ 
/* 106 */     OGLRenderQueue localOGLRenderQueue = OGLRenderQueue.getInstance();
/* 107 */     localOGLRenderQueue.ensureCapacity(4);
/* 108 */     localOGLRenderQueue.getBuffer().putInt(75);
/* 109 */     localOGLRenderQueue.flushNow();
/*     */   }
/*     */ 
/*     */   public RenderQueue getRenderQueue() {
/* 113 */     return OGLRenderQueue.getInstance();
/*     */   }
/*     */ 
/*     */   static final native String getOGLIdString();
/*     */ 
/*     */   public void saveState()
/*     */   {
/* 129 */     invalidateContext();
/* 130 */     invalidateCurrentContext();
/*     */ 
/* 132 */     setScratchSurface(this.config);
/*     */ 
/* 135 */     this.rq.ensureCapacity(4);
/* 136 */     this.buf.putInt(78);
/* 137 */     this.rq.flushNow();
/*     */   }
/*     */ 
/*     */   public void restoreState()
/*     */   {
/* 145 */     invalidateContext();
/* 146 */     invalidateCurrentContext();
/*     */ 
/* 148 */     setScratchSurface(this.config);
/*     */ 
/* 151 */     this.rq.ensureCapacity(4);
/* 152 */     this.buf.putInt(79);
/* 153 */     this.rq.flushNow();
/*     */   }
/*     */ 
/*     */   static class OGLContextCaps extends ContextCapabilities
/*     */   {
/*     */     static final int CAPS_EXT_FBOBJECT = 12;
/*     */     static final int CAPS_STORED_ALPHA = 2;
/*     */     static final int CAPS_DOUBLEBUFFERED = 65536;
/*     */     static final int CAPS_EXT_LCD_SHADER = 131072;
/*     */     static final int CAPS_EXT_BIOP_SHADER = 262144;
/*     */     static final int CAPS_EXT_GRAD_SHADER = 524288;
/*     */     static final int CAPS_EXT_TEXRECT = 1048576;
/*     */ 
/*     */     OGLContextCaps(int paramInt, String paramString)
/*     */     {
/* 190 */       super(paramString);
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 195 */       StringBuffer localStringBuffer = new StringBuffer(super.toString());
/* 196 */       if ((this.caps & 0xC) != 0) {
/* 197 */         localStringBuffer.append("CAPS_EXT_FBOBJECT|");
/*     */       }
/* 199 */       if ((this.caps & 0x2) != 0) {
/* 200 */         localStringBuffer.append("CAPS_STORED_ALPHA|");
/*     */       }
/* 202 */       if ((this.caps & 0x10000) != 0) {
/* 203 */         localStringBuffer.append("CAPS_DOUBLEBUFFERED|");
/*     */       }
/* 205 */       if ((this.caps & 0x20000) != 0) {
/* 206 */         localStringBuffer.append("CAPS_EXT_LCD_SHADER|");
/*     */       }
/* 208 */       if ((this.caps & 0x40000) != 0) {
/* 209 */         localStringBuffer.append("CAPS_BIOP_SHADER|");
/*     */       }
/* 211 */       if ((this.caps & 0x80000) != 0) {
/* 212 */         localStringBuffer.append("CAPS_EXT_GRAD_SHADER|");
/*     */       }
/* 214 */       if ((this.caps & 0x100000) != 0) {
/* 215 */         localStringBuffer.append("CAPS_EXT_TEXRECT|");
/*     */       }
/* 217 */       return localStringBuffer.toString();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.opengl.OGLContext
 * JD-Core Version:    0.6.2
 */