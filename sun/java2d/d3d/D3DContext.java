/*     */ package sun.java2d.d3d;
/*     */ 
/*     */ import sun.java2d.pipe.BufferedContext;
/*     */ import sun.java2d.pipe.RenderBuffer;
/*     */ import sun.java2d.pipe.RenderQueue;
/*     */ import sun.java2d.pipe.hw.ContextCapabilities;
/*     */ 
/*     */ class D3DContext extends BufferedContext
/*     */ {
/*     */   private final D3DGraphicsDevice device;
/*     */ 
/*     */   D3DContext(RenderQueue paramRenderQueue, D3DGraphicsDevice paramD3DGraphicsDevice)
/*     */   {
/*  45 */     super(paramRenderQueue);
/*  46 */     this.device = paramD3DGraphicsDevice;
/*     */   }
/*     */ 
/*     */   static void invalidateCurrentContext()
/*     */   {
/*  61 */     if (currentContext != null) {
/*  62 */       currentContext.invalidateContext();
/*  63 */       currentContext = null;
/*     */     }
/*     */ 
/*  69 */     D3DRenderQueue localD3DRenderQueue = D3DRenderQueue.getInstance();
/*  70 */     localD3DRenderQueue.ensureCapacity(4);
/*  71 */     localD3DRenderQueue.getBuffer().putInt(75);
/*  72 */     localD3DRenderQueue.flushNow();
/*     */   }
/*     */ 
/*     */   static void setScratchSurface(D3DContext paramD3DContext)
/*     */   {
/*  91 */     if (paramD3DContext != currentContext) {
/*  92 */       currentContext = null;
/*     */     }
/*     */ 
/*  96 */     D3DRenderQueue localD3DRenderQueue = D3DRenderQueue.getInstance();
/*  97 */     RenderBuffer localRenderBuffer = localD3DRenderQueue.getBuffer();
/*  98 */     localD3DRenderQueue.ensureCapacity(8);
/*  99 */     localRenderBuffer.putInt(71);
/* 100 */     localRenderBuffer.putInt(paramD3DContext.getDevice().getScreen());
/*     */   }
/*     */ 
/*     */   public RenderQueue getRenderQueue() {
/* 104 */     return D3DRenderQueue.getInstance();
/*     */   }
/*     */ 
/*     */   public void saveState()
/*     */   {
/* 112 */     invalidateContext();
/* 113 */     invalidateCurrentContext();
/*     */ 
/* 115 */     setScratchSurface(this);
/*     */ 
/* 118 */     this.rq.ensureCapacity(4);
/* 119 */     this.buf.putInt(78);
/* 120 */     this.rq.flushNow();
/*     */   }
/*     */ 
/*     */   public void restoreState()
/*     */   {
/* 128 */     invalidateContext();
/* 129 */     invalidateCurrentContext();
/*     */ 
/* 131 */     setScratchSurface(this);
/*     */ 
/* 134 */     this.rq.ensureCapacity(4);
/* 135 */     this.buf.putInt(79);
/* 136 */     this.rq.flushNow();
/*     */   }
/*     */ 
/*     */   D3DGraphicsDevice getDevice() {
/* 140 */     return this.device;
/*     */   }
/*     */ 
/*     */   static class D3DContextCaps extends ContextCapabilities
/*     */   {
/*     */     static final int CAPS_LCD_SHADER = 65536;
/*     */     static final int CAPS_BIOP_SHADER = 131072;
/*     */     static final int CAPS_DEVICE_OK = 262144;
/*     */     static final int CAPS_AA_SHADER = 524288;
/*     */ 
/*     */     D3DContextCaps(int paramInt, String paramString)
/*     */     {
/* 168 */       super(paramString);
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 173 */       StringBuffer localStringBuffer = new StringBuffer(super.toString());
/* 174 */       if ((this.caps & 0x10000) != 0) {
/* 175 */         localStringBuffer.append("CAPS_LCD_SHADER|");
/*     */       }
/* 177 */       if ((this.caps & 0x20000) != 0) {
/* 178 */         localStringBuffer.append("CAPS_BIOP_SHADER|");
/*     */       }
/* 180 */       if ((this.caps & 0x80000) != 0) {
/* 181 */         localStringBuffer.append("CAPS_AA_SHADER|");
/*     */       }
/* 183 */       if ((this.caps & 0x40000) != 0) {
/* 184 */         localStringBuffer.append("CAPS_DEVICE_OK|");
/*     */       }
/* 186 */       return localStringBuffer.toString();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.d3d.D3DContext
 * JD-Core Version:    0.6.2
 */