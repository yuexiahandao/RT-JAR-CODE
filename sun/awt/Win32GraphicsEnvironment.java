/*     */ package sun.awt;
/*     */ 
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.GraphicsDevice;
/*     */ import java.awt.peer.ComponentPeer;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.util.ArrayList;
/*     */ import java.util.ListIterator;
/*     */ import sun.awt.windows.WToolkit;
/*     */ import sun.java2d.SunGraphicsEnvironment;
/*     */ import sun.java2d.SurfaceManagerFactory;
/*     */ import sun.java2d.WindowsSurfaceManagerFactory;
/*     */ import sun.java2d.d3d.D3DGraphicsDevice;
/*     */ import sun.java2d.windows.WindowsFlags;
/*     */ 
/*     */ public class Win32GraphicsEnvironment extends SunGraphicsEnvironment
/*     */ {
/*     */   private static boolean displayInitialized;
/*     */   private ArrayList<WeakReference<Win32GraphicsDevice>> oldDevices;
/*     */   private static volatile boolean isDWMCompositionEnabled;
/*     */ 
/*     */   private static native void initDisplay();
/*     */ 
/*     */   public static void initDisplayWrapper()
/*     */   {
/*  83 */     if (!displayInitialized) {
/*  84 */       displayInitialized = true;
/*  85 */       initDisplay();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected native int getNumScreens();
/*     */ 
/*     */   protected native int getDefaultScreen();
/*     */ 
/*     */   public GraphicsDevice getDefaultScreenDevice()
/*     */   {
/*  96 */     return getScreenDevices()[getDefaultScreen()];
/*     */   }
/*     */ 
/*     */   public native int getXResolution();
/*     */ 
/*     */   public native int getYResolution();
/*     */ 
/*     */   public void displayChanged()
/*     */   {
/* 129 */     GraphicsDevice[] arrayOfGraphicsDevice1 = new GraphicsDevice[getNumScreens()];
/* 130 */     GraphicsDevice[] arrayOfGraphicsDevice2 = this.screens;
/*     */ 
/* 133 */     if (arrayOfGraphicsDevice2 != null) {
/* 134 */       for (i = 0; i < arrayOfGraphicsDevice2.length; i++)
/* 135 */         if (!(this.screens[i] instanceof Win32GraphicsDevice))
/*     */         {
/* 137 */           if (!$assertionsDisabled) throw new AssertionError(arrayOfGraphicsDevice2[i]); 
/*     */         }
/*     */         else
/*     */         {
/* 140 */           Win32GraphicsDevice localWin32GraphicsDevice1 = (Win32GraphicsDevice)arrayOfGraphicsDevice2[i];
/*     */ 
/* 144 */           if (!localWin32GraphicsDevice1.isValid()) {
/* 145 */             if (this.oldDevices == null) {
/* 146 */               this.oldDevices = new ArrayList();
/*     */             }
/*     */ 
/* 149 */             this.oldDevices.add(new WeakReference(localWin32GraphicsDevice1));
/* 150 */           } else if (i < arrayOfGraphicsDevice1.length)
/*     */           {
/* 152 */             arrayOfGraphicsDevice1[i] = localWin32GraphicsDevice1;
/*     */           }
/*     */         }
/* 155 */       arrayOfGraphicsDevice2 = null;
/*     */     }
/*     */ 
/* 158 */     for (int i = 0; i < arrayOfGraphicsDevice1.length; i++) {
/* 159 */       if (arrayOfGraphicsDevice1[i] == null) {
/* 160 */         arrayOfGraphicsDevice1[i] = makeScreenDevice(i);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 166 */     this.screens = arrayOfGraphicsDevice1;
/* 167 */     for (GraphicsDevice localGraphicsDevice : this.screens) {
/* 168 */       if ((localGraphicsDevice instanceof DisplayChangedListener)) {
/* 169 */         ((DisplayChangedListener)localGraphicsDevice).displayChanged();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 176 */     if (this.oldDevices != null) {
/* 177 */       int j = getDefaultScreen();
/* 178 */       ListIterator localListIterator = this.oldDevices.listIterator();
/* 179 */       while (localListIterator.hasNext())
/*     */       {
/* 181 */         Win32GraphicsDevice localWin32GraphicsDevice2 = (Win32GraphicsDevice)((WeakReference)localListIterator.next()).get();
/* 182 */         if (localWin32GraphicsDevice2 != null) {
/* 183 */           localWin32GraphicsDevice2.invalidate(j);
/* 184 */           localWin32GraphicsDevice2.displayChanged();
/*     */         }
/*     */         else {
/* 187 */           localListIterator.remove();
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 192 */     WToolkit.resetGC();
/*     */ 
/* 196 */     this.displayChanger.notifyListeners();
/*     */   }
/*     */ 
/*     */   protected GraphicsDevice makeScreenDevice(int paramInt)
/*     */   {
/* 206 */     Object localObject = null;
/* 207 */     if (WindowsFlags.isD3DEnabled()) {
/* 208 */       localObject = D3DGraphicsDevice.createDevice(paramInt);
/*     */     }
/* 210 */     if (localObject == null) {
/* 211 */       localObject = new Win32GraphicsDevice(paramInt);
/*     */     }
/* 213 */     return localObject;
/*     */   }
/*     */ 
/*     */   public boolean isDisplayLocal() {
/* 217 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean isFlipStrategyPreferred(ComponentPeer paramComponentPeer)
/*     */   {
/*     */     GraphicsConfiguration localGraphicsConfiguration;
/* 223 */     if ((paramComponentPeer != null) && ((localGraphicsConfiguration = paramComponentPeer.getGraphicsConfiguration()) != null)) {
/* 224 */       GraphicsDevice localGraphicsDevice = localGraphicsConfiguration.getDevice();
/* 225 */       if ((localGraphicsDevice instanceof D3DGraphicsDevice)) {
/* 226 */         return ((D3DGraphicsDevice)localGraphicsDevice).isD3DEnabledOnDevice();
/*     */       }
/*     */     }
/* 229 */     return false;
/*     */   }
/*     */ 
/*     */   public static boolean isDWMCompositionEnabled()
/*     */   {
/* 239 */     return isDWMCompositionEnabled;
/*     */   }
/*     */ 
/*     */   private static void dwmCompositionChanged(boolean paramBoolean)
/*     */   {
/* 252 */     isDWMCompositionEnabled = paramBoolean;
/*     */   }
/*     */ 
/*     */   public static native boolean isVistaOS();
/*     */ 
/*     */   static
/*     */   {
/*  65 */     WToolkit.loadLibraries();
/*     */ 
/*  67 */     WindowsFlags.initFlags();
/*  68 */     initDisplayWrapper();
/*     */ 
/*  71 */     SurfaceManagerFactory.setInstance(new WindowsSurfaceManagerFactory());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.Win32GraphicsEnvironment
 * JD-Core Version:    0.6.2
 */