/*     */ package sun.awt.windows;
/*     */ 
/*     */ import java.awt.AWTEvent;
/*     */ import java.awt.Frame;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Image;
/*     */ import java.awt.Point;
/*     */ import java.awt.PopupMenu;
/*     */ import java.awt.TrayIcon;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.DataBufferInt;
/*     */ import java.awt.image.ImageObserver;
/*     */ import java.awt.image.Raster;
/*     */ import java.awt.image.WritableRaster;
/*     */ import java.awt.peer.TrayIconPeer;
/*     */ import sun.awt.SunToolkit;
/*     */ import sun.awt.image.IntegerComponentRaster;
/*     */ 
/*     */ public class WTrayIconPeer extends WObjectPeer
/*     */   implements TrayIconPeer
/*     */ {
/*     */   static final int TRAY_ICON_WIDTH = 16;
/*     */   static final int TRAY_ICON_HEIGHT = 16;
/*     */   static final int TRAY_ICON_MASK_SIZE = 32;
/*  46 */   IconObserver observer = new IconObserver();
/*  47 */   boolean firstUpdate = true;
/*  48 */   Frame popupParent = new Frame("PopupMessageWindow");
/*     */   PopupMenu popup;
/*     */ 
/*     */   protected void disposeImpl()
/*     */   {
/*  52 */     if (this.popupParent != null) {
/*  53 */       this.popupParent.dispose();
/*     */     }
/*  55 */     this.popupParent.dispose();
/*  56 */     _dispose();
/*  57 */     WToolkit.targetDisposedPeer(this.target, this);
/*     */   }
/*     */ 
/*     */   WTrayIconPeer(TrayIcon paramTrayIcon) {
/*  61 */     this.target = paramTrayIcon;
/*  62 */     this.popupParent.addNotify();
/*  63 */     create();
/*  64 */     updateImage();
/*     */   }
/*     */ 
/*     */   public void updateImage() {
/*  68 */     Image localImage = ((TrayIcon)this.target).getImage();
/*  69 */     if (localImage != null)
/*  70 */       updateNativeImage(localImage);
/*     */   }
/*     */ 
/*     */   public native void setToolTip(String paramString);
/*     */ 
/*     */   public synchronized void showPopupMenu(final int paramInt1, final int paramInt2)
/*     */   {
/*  77 */     if (isDisposed()) {
/*  78 */       return;
/*     */     }
/*  80 */     SunToolkit.executeOnEventHandlerThread(this.target, new Runnable() {
/*     */       public void run() {
/*  82 */         PopupMenu localPopupMenu = ((TrayIcon)WTrayIconPeer.this.target).getPopupMenu();
/*  83 */         if (WTrayIconPeer.this.popup != localPopupMenu) {
/*  84 */           if (WTrayIconPeer.this.popup != null) {
/*  85 */             WTrayIconPeer.this.popupParent.remove(WTrayIconPeer.this.popup);
/*     */           }
/*  87 */           if (localPopupMenu != null) {
/*  88 */             WTrayIconPeer.this.popupParent.add(localPopupMenu);
/*     */           }
/*  90 */           WTrayIconPeer.this.popup = localPopupMenu;
/*     */         }
/*  92 */         if (WTrayIconPeer.this.popup != null)
/*  93 */           ((WPopupMenuPeer)WTrayIconPeer.this.popup.getPeer()).show(WTrayIconPeer.this.popupParent, new Point(paramInt1, paramInt2));
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public void displayMessage(String paramString1, String paramString2, String paramString3)
/*     */   {
/* 101 */     if (paramString1 == null) {
/* 102 */       paramString1 = "";
/*     */     }
/* 104 */     if (paramString2 == null) {
/* 105 */       paramString2 = "";
/*     */     }
/* 107 */     _displayMessage(paramString1, paramString2, paramString3);
/*     */   }
/*     */ 
/*     */   synchronized void updateNativeImage(Image paramImage)
/*     */   {
/* 116 */     if (isDisposed()) {
/* 117 */       return;
/*     */     }
/* 119 */     boolean bool = ((TrayIcon)this.target).isImageAutoSize();
/*     */ 
/* 121 */     BufferedImage localBufferedImage = new BufferedImage(16, 16, 2);
/*     */ 
/* 123 */     Graphics2D localGraphics2D = localBufferedImage.createGraphics();
/* 124 */     if (localGraphics2D != null)
/*     */       try {
/* 126 */         localGraphics2D.setPaintMode();
/*     */ 
/* 128 */         localGraphics2D.drawImage(paramImage, 0, 0, bool ? 16 : paramImage.getWidth(this.observer), bool ? 16 : paramImage.getHeight(this.observer), this.observer);
/*     */ 
/* 131 */         createNativeImage(localBufferedImage);
/*     */ 
/* 133 */         updateNativeIcon(!this.firstUpdate);
/* 134 */         if (this.firstUpdate) this.firstUpdate = false; 
/*     */       }
/*     */       finally
/*     */       {
/* 137 */         localGraphics2D.dispose();
/*     */       }
/*     */   }
/*     */ 
/*     */   void createNativeImage(BufferedImage paramBufferedImage)
/*     */   {
/* 143 */     WritableRaster localWritableRaster = paramBufferedImage.getRaster();
/* 144 */     byte[] arrayOfByte = new byte[32];
/* 145 */     int[] arrayOfInt = ((DataBufferInt)localWritableRaster.getDataBuffer()).getData();
/* 146 */     int i = arrayOfInt.length;
/* 147 */     int j = localWritableRaster.getWidth();
/*     */ 
/* 149 */     for (int k = 0; k < i; k++) {
/* 150 */       int m = k / 8;
/* 151 */       int n = 1 << 7 - k % 8;
/*     */ 
/* 153 */       if ((arrayOfInt[k] & 0xFF000000) == 0)
/*     */       {
/* 155 */         if (m < arrayOfByte.length)
/*     */         {
/*     */           int tmp83_81 = m;
/*     */           byte[] tmp83_80 = arrayOfByte; tmp83_80[tmp83_81] = ((byte)(tmp83_80[tmp83_81] | n));
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 161 */     if ((localWritableRaster instanceof IntegerComponentRaster)) {
/* 162 */       j = ((IntegerComponentRaster)localWritableRaster).getScanlineStride();
/*     */     }
/* 164 */     setNativeIcon(((DataBufferInt)paramBufferedImage.getRaster().getDataBuffer()).getData(), arrayOfByte, j, localWritableRaster.getWidth(), localWritableRaster.getHeight());
/*     */   }
/*     */ 
/*     */   void postEvent(AWTEvent paramAWTEvent)
/*     */   {
/* 169 */     WToolkit.postEvent(WToolkit.targetToAppContext(this.target), paramAWTEvent);
/*     */   }
/*     */ 
/*     */   native void create();
/*     */ 
/*     */   synchronized native void _dispose();
/*     */ 
/*     */   native void updateNativeIcon(boolean paramBoolean);
/*     */ 
/*     */   native void setNativeIcon(int[] paramArrayOfInt, byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3);
/*     */ 
/*     */   native void _displayMessage(String paramString1, String paramString2, String paramString3);
/*     */ 
/*     */   class IconObserver implements ImageObserver
/*     */   {
/*     */     IconObserver()
/*     */     {
/*     */     }
/*     */ 
/*     */     public boolean imageUpdate(Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
/* 189 */       if ((paramImage != ((TrayIcon)WTrayIconPeer.this.target).getImage()) || (WTrayIconPeer.this.isDisposed()))
/*     */       {
/* 192 */         return false;
/*     */       }
/* 194 */       if ((paramInt1 & 0x33) != 0)
/*     */       {
/* 197 */         WTrayIconPeer.this.updateNativeImage(paramImage);
/*     */       }
/* 199 */       return (paramInt1 & 0x20) == 0;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.windows.WTrayIconPeer
 * JD-Core Version:    0.6.2
 */