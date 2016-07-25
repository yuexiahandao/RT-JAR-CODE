/*     */ package java.awt.event;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.awt.Toolkit;
/*     */ import java.util.Arrays;
/*     */ import sun.awt.AWTAccessor;
/*     */ import sun.awt.AWTAccessor.InputEventAccessor;
/*     */ import sun.util.logging.PlatformLogger;
/*     */ 
/*     */ public abstract class InputEvent extends ComponentEvent
/*     */ {
/*  60 */   private static final PlatformLogger logger = PlatformLogger.getLogger("java.awt.event.InputEvent");
/*     */   public static final int SHIFT_MASK = 1;
/*     */   public static final int CTRL_MASK = 2;
/*     */   public static final int META_MASK = 4;
/*     */   public static final int ALT_MASK = 8;
/*     */   public static final int ALT_GRAPH_MASK = 32;
/*     */   public static final int BUTTON1_MASK = 16;
/*     */   public static final int BUTTON2_MASK = 8;
/*     */   public static final int BUTTON3_MASK = 4;
/*     */   public static final int SHIFT_DOWN_MASK = 64;
/*     */   public static final int CTRL_DOWN_MASK = 128;
/*     */   public static final int META_DOWN_MASK = 256;
/*     */   public static final int ALT_DOWN_MASK = 512;
/*     */   public static final int BUTTON1_DOWN_MASK = 1024;
/*     */   public static final int BUTTON2_DOWN_MASK = 2048;
/*     */   public static final int BUTTON3_DOWN_MASK = 4096;
/*     */   public static final int ALT_GRAPH_DOWN_MASK = 8192;
/* 166 */   private static final int[] BUTTON_DOWN_MASK = { 1024, 2048, 4096, 16384, 32768, 65536, 131072, 262144, 524288, 1048576, 2097152, 4194304, 8388608, 16777216, 33554432, 67108864, 134217728, 268435456, 536870912, 1073741824 };
/*     */   static final int FIRST_HIGH_BIT = -2147483648;
/*     */   static final int JDK_1_3_MODIFIERS = 63;
/*     */   static final int HIGH_MODIFIERS = -2147483648;
/*     */   long when;
/*     */   int modifiers;
/*     */   private transient boolean canAccessSystemClipboard;
/*     */   static final long serialVersionUID = -2482525981698309786L;
/*     */ 
/*     */   private static int[] getButtonDownMasks()
/*     */   {
/* 192 */     return Arrays.copyOf(BUTTON_DOWN_MASK, BUTTON_DOWN_MASK.length);
/*     */   }
/*     */ 
/*     */   public static int getMaskForButton(int paramInt)
/*     */   {
/* 245 */     if ((paramInt <= 0) || (paramInt > BUTTON_DOWN_MASK.length)) {
/* 246 */       throw new IllegalArgumentException("button doesn't exist " + paramInt);
/*     */     }
/* 248 */     return BUTTON_DOWN_MASK[(paramInt - 1)];
/*     */   }
/*     */ 
/*     */   private static native void initIDs();
/*     */ 
/*     */   InputEvent(Component paramComponent, int paramInt1, long paramLong, int paramInt2)
/*     */   {
/* 339 */     super(paramComponent, paramInt1);
/* 340 */     this.when = paramLong;
/* 341 */     this.modifiers = paramInt2;
/* 342 */     this.canAccessSystemClipboard = canAccessSystemClipboard();
/*     */   }
/*     */ 
/*     */   private boolean canAccessSystemClipboard() {
/* 346 */     boolean bool = false;
/*     */ 
/* 348 */     if (!GraphicsEnvironment.isHeadless()) {
/* 349 */       SecurityManager localSecurityManager = System.getSecurityManager();
/* 350 */       if (localSecurityManager != null) {
/*     */         try {
/* 352 */           localSecurityManager.checkSystemClipboardAccess();
/* 353 */           bool = true;
/*     */         } catch (SecurityException localSecurityException) {
/* 355 */           if (logger.isLoggable(500))
/* 356 */             logger.fine("InputEvent.canAccessSystemClipboard() got SecurityException ", localSecurityException);
/*     */         }
/*     */       }
/*     */       else {
/* 360 */         bool = true;
/*     */       }
/*     */     }
/*     */ 
/* 364 */     return bool;
/*     */   }
/*     */ 
/*     */   public boolean isShiftDown()
/*     */   {
/* 371 */     return (this.modifiers & 0x1) != 0;
/*     */   }
/*     */ 
/*     */   public boolean isControlDown()
/*     */   {
/* 378 */     return (this.modifiers & 0x2) != 0;
/*     */   }
/*     */ 
/*     */   public boolean isMetaDown()
/*     */   {
/* 385 */     return (this.modifiers & 0x4) != 0;
/*     */   }
/*     */ 
/*     */   public boolean isAltDown()
/*     */   {
/* 392 */     return (this.modifiers & 0x8) != 0;
/*     */   }
/*     */ 
/*     */   public boolean isAltGraphDown()
/*     */   {
/* 399 */     return (this.modifiers & 0x20) != 0;
/*     */   }
/*     */ 
/*     */   public long getWhen()
/*     */   {
/* 407 */     return this.when;
/*     */   }
/*     */ 
/*     */   public int getModifiers()
/*     */   {
/* 414 */     return this.modifiers & 0x8000003F;
/*     */   }
/*     */ 
/*     */   public int getModifiersEx()
/*     */   {
/* 451 */     return this.modifiers & 0xFFFFFFC0;
/*     */   }
/*     */ 
/*     */   public void consume()
/*     */   {
/* 459 */     this.consumed = true;
/*     */   }
/*     */ 
/*     */   public boolean isConsumed()
/*     */   {
/* 467 */     return this.consumed;
/*     */   }
/*     */ 
/*     */   public static String getModifiersExText(int paramInt)
/*     */   {
/* 492 */     StringBuilder localStringBuilder = new StringBuilder();
/* 493 */     if ((paramInt & 0x100) != 0) {
/* 494 */       localStringBuilder.append(Toolkit.getProperty("AWT.meta", "Meta"));
/* 495 */       localStringBuilder.append("+");
/*     */     }
/* 497 */     if ((paramInt & 0x80) != 0) {
/* 498 */       localStringBuilder.append(Toolkit.getProperty("AWT.control", "Ctrl"));
/* 499 */       localStringBuilder.append("+");
/*     */     }
/* 501 */     if ((paramInt & 0x200) != 0) {
/* 502 */       localStringBuilder.append(Toolkit.getProperty("AWT.alt", "Alt"));
/* 503 */       localStringBuilder.append("+");
/*     */     }
/* 505 */     if ((paramInt & 0x40) != 0) {
/* 506 */       localStringBuilder.append(Toolkit.getProperty("AWT.shift", "Shift"));
/* 507 */       localStringBuilder.append("+");
/*     */     }
/* 509 */     if ((paramInt & 0x2000) != 0) {
/* 510 */       localStringBuilder.append(Toolkit.getProperty("AWT.altGraph", "Alt Graph"));
/* 511 */       localStringBuilder.append("+");
/*     */     }
/*     */ 
/* 514 */     int i = 1;
/* 515 */     for (int m : BUTTON_DOWN_MASK) {
/* 516 */       if ((paramInt & m) != 0) {
/* 517 */         localStringBuilder.append(Toolkit.getProperty("AWT.button" + i, "Button" + i));
/* 518 */         localStringBuilder.append("+");
/*     */       }
/* 520 */       i++;
/*     */     }
/* 522 */     if (localStringBuilder.length() > 0) {
/* 523 */       localStringBuilder.setLength(localStringBuilder.length() - 1);
/*     */     }
/* 525 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 290 */     NativeLibLoader.loadLibraries();
/* 291 */     if (!GraphicsEnvironment.isHeadless()) {
/* 292 */       initIDs();
/*     */     }
/* 294 */     AWTAccessor.setInputEventAccessor(new AWTAccessor.InputEventAccessor()
/*     */     {
/*     */       public int[] getButtonDownMasks() {
/* 297 */         return InputEvent.access$000();
/*     */       }
/*     */     });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.event.InputEvent
 * JD-Core Version:    0.6.2
 */