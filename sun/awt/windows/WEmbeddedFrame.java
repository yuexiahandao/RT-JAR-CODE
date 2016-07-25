/*     */ package sun.awt.windows;
/*     */ 
/*     */ import java.awt.AWTKeyStroke;
/*     */ import java.awt.Color;
/*     */ import java.awt.Dialog;
/*     */ import java.awt.EventQueue;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.event.InvocationEvent;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.peer.ComponentPeer;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import sun.awt.EmbeddedFrame;
/*     */ import sun.awt.image.ByteInterleavedRaster;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ public class WEmbeddedFrame extends EmbeddedFrame
/*     */ {
/*     */   private long handle;
/*  44 */   private int bandWidth = 0;
/*  45 */   private int bandHeight = 0;
/*  46 */   private int imgWid = 0;
/*  47 */   private int imgHgt = 0;
/*     */ 
/*  49 */   private static int pScale = 0;
/*     */   private static final int MAX_BAND_SIZE = 30720;
/*  52 */   private static String printScale = (String)AccessController.doPrivileged(new GetPropertyAction("sun.java2d.print.pluginscalefactor"));
/*     */ 
/*     */   public WEmbeddedFrame()
/*     */   {
/*  56 */     this(0L);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public WEmbeddedFrame(int paramInt)
/*     */   {
/*  64 */     this(paramInt);
/*     */   }
/*     */ 
/*     */   public WEmbeddedFrame(long paramLong) {
/*  68 */     this.handle = paramLong;
/*  69 */     if (paramLong != 0L) {
/*  70 */       addNotify();
/*  71 */       show();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addNotify() {
/*  76 */     if (getPeer() == null) {
/*  77 */       WToolkit localWToolkit = (WToolkit)Toolkit.getDefaultToolkit();
/*  78 */       setPeer(localWToolkit.createEmbeddedFrame(this));
/*     */     }
/*  80 */     super.addNotify();
/*     */   }
/*     */ 
/*     */   public long getEmbedderHandle()
/*     */   {
/*  87 */     return this.handle;
/*     */   }
/*     */ 
/*     */   void print(long paramLong)
/*     */   {
/*  95 */     BufferedImage localBufferedImage = null;
/*     */ 
/*  97 */     int i = 1;
/*  98 */     int j = 1;
/*     */ 
/* 118 */     if (isPrinterDC(paramLong)) {
/* 119 */       i = j = getPrintScaleFactor();
/*     */     }
/*     */ 
/* 122 */     int k = getHeight();
/* 123 */     if (localBufferedImage == null) {
/* 124 */       this.bandWidth = getWidth();
/* 125 */       if (this.bandWidth % 4 != 0) {
/* 126 */         this.bandWidth += 4 - this.bandWidth % 4;
/*     */       }
/* 128 */       if (this.bandWidth <= 0) {
/* 129 */         return;
/*     */       }
/*     */ 
/* 132 */       this.bandHeight = Math.min(30720 / this.bandWidth, k);
/*     */ 
/* 134 */       this.imgWid = (this.bandWidth * i);
/* 135 */       this.imgHgt = (this.bandHeight * j);
/* 136 */       localBufferedImage = new BufferedImage(this.imgWid, this.imgHgt, 5);
/*     */     }
/*     */ 
/* 140 */     Graphics localGraphics = localBufferedImage.getGraphics();
/* 141 */     localGraphics.setColor(Color.white);
/* 142 */     Graphics2D localGraphics2D = (Graphics2D)localBufferedImage.getGraphics();
/* 143 */     localGraphics2D.translate(0, this.imgHgt);
/* 144 */     localGraphics2D.scale(i, -j);
/*     */ 
/* 146 */     ByteInterleavedRaster localByteInterleavedRaster = (ByteInterleavedRaster)localBufferedImage.getRaster();
/* 147 */     byte[] arrayOfByte = localByteInterleavedRaster.getDataStorage();
/*     */ 
/* 149 */     for (int m = 0; m < k; m += this.bandHeight) {
/* 150 */       localGraphics.fillRect(0, 0, this.bandWidth, this.bandHeight);
/*     */ 
/* 152 */       printComponents(localGraphics2D);
/* 153 */       int n = 0;
/* 154 */       int i1 = this.bandHeight;
/* 155 */       int i2 = this.imgHgt;
/* 156 */       if (m + this.bandHeight > k)
/*     */       {
/* 158 */         i1 = k - m;
/* 159 */         i2 = i1 * j;
/*     */ 
/* 162 */         n = this.imgWid * (this.imgHgt - i2) * 3;
/*     */       }
/*     */ 
/* 165 */       printBand(paramLong, arrayOfByte, n, 0, 0, this.imgWid, i2, 0, m, this.bandWidth, i1);
/*     */ 
/* 168 */       localGraphics2D.translate(0, -this.bandHeight);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected static int getPrintScaleFactor()
/*     */   {
/* 174 */     if (pScale != 0)
/* 175 */       return pScale;
/* 176 */     if (printScale == null)
/*     */     {
/* 179 */       printScale = (String)AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public Object run() {
/* 182 */           return System.getenv("JAVA2D_PLUGIN_PRINT_SCALE");
/*     */         }
/*     */       });
/*     */     }
/*     */ 
/* 187 */     int i = 4;
/* 188 */     int j = i;
/* 189 */     if (printScale != null)
/*     */       try {
/* 191 */         j = Integer.parseInt(printScale);
/* 192 */         if ((j > 8) || (j < 1))
/* 193 */           j = i;
/*     */       }
/*     */       catch (NumberFormatException localNumberFormatException)
/*     */       {
/*     */       }
/* 198 */     pScale = j;
/* 199 */     return pScale;
/*     */   }
/*     */ 
/*     */   protected native boolean isPrinterDC(long paramLong);
/*     */ 
/*     */   protected native void printBand(long paramLong, byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9);
/*     */ 
/*     */   private static native void initIDs();
/*     */ 
/*     */   public void activateEmbeddingTopLevel()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void synthesizeWindowActivation(boolean paramBoolean)
/*     */   {
/* 227 */     if ((!paramBoolean) || (EventQueue.isDispatchThread())) {
/* 228 */       ((WEmbeddedFramePeer)getPeer()).synthesizeWmActivate(paramBoolean);
/*     */     }
/*     */     else
/*     */     {
/* 232 */       Runnable local2 = new Runnable() {
/*     */         public void run() {
/* 234 */           ((WEmbeddedFramePeer)WEmbeddedFrame.this.getPeer()).synthesizeWmActivate(true);
/*     */         }
/*     */       };
/* 237 */       WToolkit.postEvent(WToolkit.targetToAppContext(this), new InvocationEvent(this, local2));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void registerAccelerator(AWTKeyStroke paramAWTKeyStroke)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void unregisterAccelerator(AWTKeyStroke paramAWTKeyStroke)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void notifyModalBlocked(Dialog paramDialog, boolean paramBoolean)
/*     */   {
/*     */     try
/*     */     {
/* 259 */       ComponentPeer localComponentPeer1 = (ComponentPeer)WToolkit.targetToPeer(this);
/* 260 */       ComponentPeer localComponentPeer2 = (ComponentPeer)WToolkit.targetToPeer(paramDialog);
/* 261 */       notifyModalBlockedImpl((WEmbeddedFramePeer)localComponentPeer1, (WWindowPeer)localComponentPeer2, paramBoolean);
/*     */     }
/*     */     catch (Exception localException) {
/* 264 */       localException.printStackTrace(System.err);
/*     */     }
/*     */   }
/*     */ 
/*     */   native void notifyModalBlockedImpl(WEmbeddedFramePeer paramWEmbeddedFramePeer, WWindowPeer paramWWindowPeer, boolean paramBoolean);
/*     */ 
/*     */   static
/*     */   {
/*  39 */     initIDs();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.windows.WEmbeddedFrame
 * JD-Core Version:    0.6.2
 */