/*     */ package sun.awt.windows;
/*     */ 
/*     */ import java.awt.AWTEvent;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.ComponentEvent;
/*     */ import java.awt.event.InputMethodEvent;
/*     */ import java.awt.event.InvocationEvent;
/*     */ import java.awt.font.TextAttribute;
/*     */ import java.awt.font.TextHitInfo;
/*     */ import java.awt.im.InputMethodHighlight;
/*     */ import java.awt.im.InputSubset;
/*     */ import java.awt.im.spi.InputMethodContext;
/*     */ import java.awt.peer.ComponentPeer;
/*     */ import java.awt.peer.LightweightPeer;
/*     */ import java.text.Annotation;
/*     */ import java.text.AttributedCharacterIterator;
/*     */ import java.text.AttributedCharacterIterator.Attribute;
/*     */ import java.text.AttributedString;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import sun.awt.im.InputMethodAdapter;
/*     */ 
/*     */ public class WInputMethod extends InputMethodAdapter
/*     */ {
/*     */   private InputMethodContext inputContext;
/*     */   private Component awtFocussedComponent;
/*  55 */   private WComponentPeer awtFocussedComponentPeer = null;
/*  56 */   private WComponentPeer lastFocussedComponentPeer = null;
/*  57 */   private boolean isLastFocussedActiveClient = false;
/*     */   private boolean isActive;
/*     */   private int context;
/*     */   private boolean open;
/*     */   private int cmode;
/*     */   private Locale currentLocale;
/*  64 */   private boolean statusWindowHidden = false;
/*     */   public static final byte ATTR_INPUT = 0;
/*     */   public static final byte ATTR_TARGET_CONVERTED = 1;
/*     */   public static final byte ATTR_CONVERTED = 2;
/*     */   public static final byte ATTR_TARGET_NOTCONVERTED = 3;
/*     */   public static final byte ATTR_INPUT_ERROR = 4;
/*     */   public static final int IME_CMODE_ALPHANUMERIC = 0;
/*     */   public static final int IME_CMODE_NATIVE = 1;
/*     */   public static final int IME_CMODE_KATAKANA = 2;
/*     */   public static final int IME_CMODE_LANGUAGE = 3;
/*     */   public static final int IME_CMODE_FULLSHAPE = 8;
/*     */   public static final int IME_CMODE_HANJACONVERT = 64;
/*     */   public static final int IME_CMODE_ROMAN = 16;
/*     */   private static final boolean COMMIT_INPUT = true;
/*     */   private static final boolean DISCARD_INPUT = false;
/* 116 */   private static Map[] highlightStyles = arrayOfMap;
/*     */ 
/*     */   public WInputMethod()
/*     */   {
/* 121 */     this.context = createNativeContext();
/* 122 */     this.cmode = getConversionStatus(this.context);
/* 123 */     this.open = getOpenStatus(this.context);
/* 124 */     this.currentLocale = getNativeLocale();
/* 125 */     if (this.currentLocale == null)
/* 126 */       this.currentLocale = Locale.getDefault();
/*     */   }
/*     */ 
/*     */   protected void finalize()
/*     */     throws Throwable
/*     */   {
/* 133 */     if (this.context != 0) {
/* 134 */       destroyNativeContext(this.context);
/* 135 */       this.context = 0;
/*     */     }
/* 137 */     super.finalize();
/*     */   }
/*     */ 
/*     */   public synchronized void setInputMethodContext(InputMethodContext paramInputMethodContext) {
/* 141 */     this.inputContext = paramInputMethodContext;
/*     */   }
/*     */ 
/*     */   public final void dispose()
/*     */   {
/*     */   }
/*     */ 
/*     */   public Object getControlObject()
/*     */   {
/* 156 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean setLocale(Locale paramLocale) {
/* 160 */     return setLocale(paramLocale, false);
/*     */   }
/*     */ 
/*     */   private boolean setLocale(Locale paramLocale, boolean paramBoolean) {
/* 164 */     Locale[] arrayOfLocale = WInputMethodDescriptor.getAvailableLocalesInternal();
/* 165 */     for (int i = 0; i < arrayOfLocale.length; i++) {
/* 166 */       Locale localLocale = arrayOfLocale[i];
/* 167 */       if ((paramLocale.equals(localLocale)) || ((localLocale.equals(Locale.JAPAN)) && (paramLocale.equals(Locale.JAPANESE))) || ((localLocale.equals(Locale.KOREA)) && (paramLocale.equals(Locale.KOREAN))))
/*     */       {
/* 171 */         if (this.isActive) {
/* 172 */           setNativeLocale(localLocale.toLanguageTag(), paramBoolean);
/*     */         }
/* 174 */         this.currentLocale = localLocale;
/* 175 */         return true;
/*     */       }
/*     */     }
/* 178 */     return false;
/*     */   }
/*     */ 
/*     */   public Locale getLocale() {
/* 182 */     if (this.isActive) {
/* 183 */       this.currentLocale = getNativeLocale();
/* 184 */       if (this.currentLocale == null) {
/* 185 */         this.currentLocale = Locale.getDefault();
/*     */       }
/*     */     }
/* 188 */     return this.currentLocale;
/*     */   }
/*     */ 
/*     */   public void setCharacterSubsets(Character.Subset[] paramArrayOfSubset)
/*     */   {
/* 197 */     if (paramArrayOfSubset == null) {
/* 198 */       setConversionStatus(this.context, this.cmode);
/* 199 */       setOpenStatus(this.context, this.open);
/* 200 */       return;
/*     */     }
/*     */ 
/* 205 */     Character.Subset localSubset = paramArrayOfSubset[0];
/*     */ 
/* 207 */     Locale localLocale = getNativeLocale();
/*     */ 
/* 210 */     if (localLocale == null)
/*     */       return;
/*     */     int i;
/* 214 */     if (localLocale.getLanguage().equals(Locale.JAPANESE.getLanguage())) {
/* 215 */       if ((localSubset == Character.UnicodeBlock.BASIC_LATIN) || (localSubset == InputSubset.LATIN_DIGITS)) {
/* 216 */         setOpenStatus(this.context, false);
/*     */       } else {
/* 218 */         if ((localSubset == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS) || (localSubset == InputSubset.KANJI) || (localSubset == Character.UnicodeBlock.HIRAGANA))
/*     */         {
/* 221 */           i = 9;
/* 222 */         } else if (localSubset == Character.UnicodeBlock.KATAKANA)
/* 223 */           i = 11;
/* 224 */         else if (localSubset == InputSubset.HALFWIDTH_KATAKANA)
/* 225 */           i = 3;
/* 226 */         else if (localSubset == InputSubset.FULLWIDTH_LATIN)
/* 227 */           i = 8;
/*     */         else
/* 229 */           return;
/* 230 */         setOpenStatus(this.context, true);
/* 231 */         i |= getConversionStatus(this.context) & 0x10;
/* 232 */         setConversionStatus(this.context, i);
/*     */       }
/* 234 */     } else if (localLocale.getLanguage().equals(Locale.KOREAN.getLanguage())) {
/* 235 */       if ((localSubset == Character.UnicodeBlock.BASIC_LATIN) || (localSubset == InputSubset.LATIN_DIGITS)) {
/* 236 */         setOpenStatus(this.context, false);
/*     */       } else {
/* 238 */         if ((localSubset == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS) || (localSubset == InputSubset.HANJA) || (localSubset == Character.UnicodeBlock.HANGUL_SYLLABLES) || (localSubset == Character.UnicodeBlock.HANGUL_JAMO) || (localSubset == Character.UnicodeBlock.HANGUL_COMPATIBILITY_JAMO))
/*     */         {
/* 243 */           i = 1;
/* 244 */         } else if (localSubset == InputSubset.FULLWIDTH_LATIN)
/* 245 */           i = 8;
/*     */         else
/* 247 */           return;
/* 248 */         setOpenStatus(this.context, true);
/* 249 */         setConversionStatus(this.context, i);
/*     */       }
/* 251 */     } else if (localLocale.getLanguage().equals(Locale.CHINESE.getLanguage()))
/* 252 */       if ((localSubset == Character.UnicodeBlock.BASIC_LATIN) || (localSubset == InputSubset.LATIN_DIGITS)) {
/* 253 */         setOpenStatus(this.context, false);
/*     */       } else {
/* 255 */         if ((localSubset == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS) || (localSubset == InputSubset.TRADITIONAL_HANZI) || (localSubset == InputSubset.SIMPLIFIED_HANZI))
/*     */         {
/* 258 */           i = 1;
/* 259 */         } else if (localSubset == InputSubset.FULLWIDTH_LATIN)
/* 260 */           i = 8;
/*     */         else
/* 262 */           return;
/* 263 */         setOpenStatus(this.context, true);
/* 264 */         setConversionStatus(this.context, i);
/*     */       }
/*     */   }
/*     */ 
/*     */   public void dispatchEvent(AWTEvent paramAWTEvent)
/*     */   {
/* 270 */     if ((paramAWTEvent instanceof ComponentEvent)) {
/* 271 */       Component localComponent = ((ComponentEvent)paramAWTEvent).getComponent();
/* 272 */       if (localComponent == this.awtFocussedComponent) {
/* 273 */         if ((this.awtFocussedComponentPeer == null) || (this.awtFocussedComponentPeer.isDisposed()))
/*     */         {
/* 275 */           this.awtFocussedComponentPeer = getNearestNativePeer(localComponent);
/*     */         }
/* 277 */         if (this.awtFocussedComponentPeer != null)
/* 278 */           handleNativeIMEEvent(this.awtFocussedComponentPeer, paramAWTEvent);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void activate()
/*     */   {
/* 285 */     boolean bool = haveActiveClient();
/*     */ 
/* 291 */     if ((this.lastFocussedComponentPeer != this.awtFocussedComponentPeer) || (this.isLastFocussedActiveClient != bool))
/*     */     {
/* 293 */       if (this.lastFocussedComponentPeer != null) {
/* 294 */         disableNativeIME(this.lastFocussedComponentPeer);
/*     */       }
/* 296 */       if (this.awtFocussedComponentPeer != null) {
/* 297 */         enableNativeIME(this.awtFocussedComponentPeer, this.context, !bool);
/*     */       }
/* 299 */       this.lastFocussedComponentPeer = this.awtFocussedComponentPeer;
/* 300 */       this.isLastFocussedActiveClient = bool;
/*     */     }
/* 302 */     this.isActive = true;
/* 303 */     if (this.currentLocale != null) {
/* 304 */       setLocale(this.currentLocale, true);
/*     */     }
/*     */ 
/* 313 */     if (this.statusWindowHidden) {
/* 314 */       setStatusWindowVisible(this.awtFocussedComponentPeer, true);
/* 315 */       this.statusWindowHidden = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void deactivate(boolean paramBoolean)
/*     */   {
/* 324 */     getLocale();
/*     */ 
/* 328 */     if (this.awtFocussedComponentPeer != null) {
/* 329 */       this.lastFocussedComponentPeer = this.awtFocussedComponentPeer;
/* 330 */       this.isLastFocussedActiveClient = haveActiveClient();
/*     */     }
/* 332 */     this.isActive = false;
/*     */   }
/*     */ 
/*     */   public void disableInputMethod()
/*     */   {
/* 340 */     if (this.lastFocussedComponentPeer != null) {
/* 341 */       disableNativeIME(this.lastFocussedComponentPeer);
/* 342 */       this.lastFocussedComponentPeer = null;
/* 343 */       this.isLastFocussedActiveClient = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getNativeInputMethodInfo()
/*     */   {
/* 352 */     return getNativeIMMDescription();
/*     */   }
/*     */ 
/*     */   protected void stopListening()
/*     */   {
/* 365 */     disableInputMethod();
/*     */   }
/*     */ 
/*     */   protected void setAWTFocussedComponent(Component paramComponent)
/*     */   {
/* 370 */     if (paramComponent == null) {
/* 371 */       return;
/*     */     }
/* 373 */     WComponentPeer localWComponentPeer = getNearestNativePeer(paramComponent);
/* 374 */     if (this.isActive)
/*     */     {
/* 377 */       if (this.awtFocussedComponentPeer != null) {
/* 378 */         disableNativeIME(this.awtFocussedComponentPeer);
/*     */       }
/* 380 */       if (localWComponentPeer != null) {
/* 381 */         enableNativeIME(localWComponentPeer, this.context, !haveActiveClient());
/*     */       }
/*     */     }
/* 384 */     this.awtFocussedComponent = paramComponent;
/* 385 */     this.awtFocussedComponentPeer = localWComponentPeer;
/*     */   }
/*     */ 
/*     */   public void hideWindows()
/*     */   {
/* 390 */     if (this.awtFocussedComponentPeer != null)
/*     */     {
/* 396 */       setStatusWindowVisible(this.awtFocussedComponentPeer, false);
/* 397 */       this.statusWindowHidden = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void removeNotify()
/*     */   {
/* 405 */     endCompositionNative(this.context, false);
/* 406 */     this.awtFocussedComponent = null;
/* 407 */     this.awtFocussedComponentPeer = null;
/*     */   }
/*     */ 
/*     */   static Map mapInputMethodHighlight(InputMethodHighlight paramInputMethodHighlight)
/*     */   {
/* 415 */     int j = paramInputMethodHighlight.getState();
/*     */     int i;
/* 416 */     if (j == 0)
/* 417 */       i = 0;
/* 418 */     else if (j == 1)
/* 419 */       i = 2;
/*     */     else {
/* 421 */       return null;
/*     */     }
/* 423 */     if (paramInputMethodHighlight.isSelected()) {
/* 424 */       i++;
/*     */     }
/* 426 */     return highlightStyles[i];
/*     */   }
/*     */ 
/*     */   protected boolean supportsBelowTheSpot()
/*     */   {
/* 431 */     return true;
/*     */   }
/*     */ 
/*     */   public void endComposition()
/*     */   {
/* 438 */     endCompositionNative(this.context, haveActiveClient());
/*     */   }
/*     */ 
/*     */   public void setCompositionEnabled(boolean paramBoolean)
/*     */   {
/* 446 */     setOpenStatus(this.context, paramBoolean);
/*     */   }
/*     */ 
/*     */   public boolean isCompositionEnabled()
/*     */   {
/* 453 */     return getOpenStatus(this.context);
/*     */   }
/*     */ 
/*     */   public void sendInputMethodEvent(int paramInt1, long paramLong, String paramString, int[] paramArrayOfInt1, String[] paramArrayOfString, int[] paramArrayOfInt2, byte[] paramArrayOfByte, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 462 */     AttributedCharacterIterator localAttributedCharacterIterator = null;
/*     */ 
/* 464 */     if (paramString != null)
/*     */     {
/* 467 */       localObject = new AttributedString(paramString);
/*     */ 
/* 470 */       ((AttributedString)localObject).addAttribute(AttributedCharacterIterator.Attribute.LANGUAGE, Locale.getDefault(), 0, paramString.length());
/*     */       int i;
/* 474 */       if ((paramArrayOfInt1 != null) && (paramArrayOfString != null) && (paramArrayOfString.length != 0) && (paramArrayOfInt1.length == paramArrayOfString.length + 1) && (paramArrayOfInt1[0] == 0) && (paramArrayOfInt1[paramArrayOfString.length] == paramString.length()))
/*     */       {
/* 478 */         for (i = 0; i < paramArrayOfInt1.length - 1; i++) {
/* 479 */           ((AttributedString)localObject).addAttribute(AttributedCharacterIterator.Attribute.INPUT_METHOD_SEGMENT, new Annotation(null), paramArrayOfInt1[i], paramArrayOfInt1[(i + 1)]);
/*     */ 
/* 481 */           ((AttributedString)localObject).addAttribute(AttributedCharacterIterator.Attribute.READING, new Annotation(paramArrayOfString[i]), paramArrayOfInt1[i], paramArrayOfInt1[(i + 1)]);
/*     */         }
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 488 */         ((AttributedString)localObject).addAttribute(AttributedCharacterIterator.Attribute.INPUT_METHOD_SEGMENT, new Annotation(null), 0, paramString.length());
/*     */ 
/* 490 */         ((AttributedString)localObject).addAttribute(AttributedCharacterIterator.Attribute.READING, new Annotation(""), 0, paramString.length());
/*     */       }
/*     */ 
/* 495 */       if ((paramArrayOfInt2 != null) && (paramArrayOfByte != null) && (paramArrayOfByte.length != 0) && (paramArrayOfInt2.length == paramArrayOfByte.length + 1) && (paramArrayOfInt2[0] == 0) && (paramArrayOfInt2[paramArrayOfByte.length] == paramString.length()))
/*     */       {
/* 499 */         for (i = 0; i < paramArrayOfInt2.length - 1; i++)
/*     */         {
/*     */           InputMethodHighlight localInputMethodHighlight;
/* 501 */           switch (paramArrayOfByte[i]) {
/*     */           case 1:
/* 503 */             localInputMethodHighlight = InputMethodHighlight.SELECTED_CONVERTED_TEXT_HIGHLIGHT;
/* 504 */             break;
/*     */           case 2:
/* 506 */             localInputMethodHighlight = InputMethodHighlight.UNSELECTED_CONVERTED_TEXT_HIGHLIGHT;
/* 507 */             break;
/*     */           case 3:
/* 509 */             localInputMethodHighlight = InputMethodHighlight.SELECTED_RAW_TEXT_HIGHLIGHT;
/* 510 */             break;
/*     */           case 0:
/*     */           case 4:
/*     */           default:
/* 514 */             localInputMethodHighlight = InputMethodHighlight.UNSELECTED_RAW_TEXT_HIGHLIGHT;
/*     */           }
/*     */ 
/* 517 */           ((AttributedString)localObject).addAttribute(TextAttribute.INPUT_METHOD_HIGHLIGHT, localInputMethodHighlight, paramArrayOfInt2[i], paramArrayOfInt2[(i + 1)]);
/*     */         }
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 525 */         ((AttributedString)localObject).addAttribute(TextAttribute.INPUT_METHOD_HIGHLIGHT, InputMethodHighlight.UNSELECTED_CONVERTED_TEXT_HIGHLIGHT, 0, paramString.length());
/*     */       }
/*     */ 
/* 531 */       localAttributedCharacterIterator = ((AttributedString)localObject).getIterator();
/*     */     }
/*     */ 
/* 535 */     Object localObject = getClientComponent();
/* 536 */     if (localObject == null) {
/* 537 */       return;
/*     */     }
/* 539 */     InputMethodEvent localInputMethodEvent = new InputMethodEvent((Component)localObject, paramInt1, paramLong, localAttributedCharacterIterator, paramInt2, TextHitInfo.leading(paramInt3), TextHitInfo.leading(paramInt4));
/*     */ 
/* 546 */     WToolkit.postEvent(WToolkit.targetToAppContext(localObject), localInputMethodEvent);
/*     */   }
/*     */ 
/*     */   public void inquireCandidatePosition()
/*     */   {
/* 551 */     Component localComponent = getClientComponent();
/* 552 */     if (localComponent == null) {
/* 553 */       return;
/*     */     }
/*     */ 
/* 559 */     Runnable local1 = new Runnable() {
/*     */       public void run() {
/* 561 */         int i = 0;
/* 562 */         int j = 0;
/* 563 */         Component localComponent = WInputMethod.this.getClientComponent();
/*     */ 
/* 565 */         if (localComponent != null)
/*     */         {
/*     */           Object localObject;
/* 566 */           if (WInputMethod.this.haveActiveClient()) {
/* 567 */             localObject = WInputMethod.this.inputContext.getTextLocation(TextHitInfo.leading(0));
/* 568 */             i = ((Rectangle)localObject).x;
/* 569 */             j = ((Rectangle)localObject).y + ((Rectangle)localObject).height;
/*     */           } else {
/* 571 */             localObject = localComponent.getLocationOnScreen();
/* 572 */             Dimension localDimension = localComponent.getSize();
/* 573 */             i = ((Point)localObject).x;
/* 574 */             j = ((Point)localObject).y + localDimension.height;
/*     */           }
/*     */         }
/*     */ 
/* 578 */         WInputMethod.this.openCandidateWindow(WInputMethod.this.awtFocussedComponentPeer, i, j);
/*     */       }
/*     */     };
/* 581 */     WToolkit.postEvent(WToolkit.targetToAppContext(localComponent), new InvocationEvent(localComponent, local1));
/*     */   }
/*     */ 
/*     */   private WComponentPeer getNearestNativePeer(Component paramComponent)
/*     */   {
/* 589 */     if (paramComponent == null) return null;
/*     */ 
/* 591 */     ComponentPeer localComponentPeer = paramComponent.getPeer();
/* 592 */     if (localComponentPeer == null) return null;
/*     */ 
/* 594 */     while ((localComponentPeer instanceof LightweightPeer)) {
/* 595 */       paramComponent = paramComponent.getParent();
/* 596 */       if (paramComponent == null) return null;
/* 597 */       localComponentPeer = paramComponent.getPeer();
/* 598 */       if (localComponentPeer == null) return null;
/*     */     }
/*     */ 
/* 601 */     if ((localComponentPeer instanceof WComponentPeer)) {
/* 602 */       return (WComponentPeer)localComponentPeer;
/*     */     }
/* 604 */     return null;
/*     */   }
/*     */ 
/*     */   private native int createNativeContext();
/*     */ 
/*     */   private native void destroyNativeContext(int paramInt);
/*     */ 
/*     */   private native void enableNativeIME(WComponentPeer paramWComponentPeer, int paramInt, boolean paramBoolean);
/*     */ 
/*     */   private native void disableNativeIME(WComponentPeer paramWComponentPeer);
/*     */ 
/*     */   private native void handleNativeIMEEvent(WComponentPeer paramWComponentPeer, AWTEvent paramAWTEvent);
/*     */ 
/*     */   private native void endCompositionNative(int paramInt, boolean paramBoolean);
/*     */ 
/*     */   private native void setConversionStatus(int paramInt1, int paramInt2);
/*     */ 
/*     */   private native int getConversionStatus(int paramInt);
/*     */ 
/*     */   private native void setOpenStatus(int paramInt, boolean paramBoolean);
/*     */ 
/*     */   private native boolean getOpenStatus(int paramInt);
/*     */ 
/*     */   private native void setStatusWindowVisible(WComponentPeer paramWComponentPeer, boolean paramBoolean);
/*     */ 
/*     */   private native String getNativeIMMDescription();
/*     */ 
/*     */   static native Locale getNativeLocale();
/*     */ 
/*     */   static native boolean setNativeLocale(String paramString, boolean paramBoolean);
/*     */ 
/*     */   private native void openCandidateWindow(WComponentPeer paramWComponentPeer, int paramInt1, int paramInt2);
/*     */ 
/*     */   static
/*     */   {
/*  89 */     Map[] arrayOfMap = new Map[4];
/*     */ 
/*  93 */     HashMap localHashMap = new HashMap(1);
/*  94 */     localHashMap.put(TextAttribute.INPUT_METHOD_UNDERLINE, TextAttribute.UNDERLINE_LOW_DOTTED);
/*  95 */     arrayOfMap[0] = Collections.unmodifiableMap(localHashMap);
/*     */ 
/*  98 */     localHashMap = new HashMap(1);
/*  99 */     localHashMap.put(TextAttribute.INPUT_METHOD_UNDERLINE, TextAttribute.UNDERLINE_LOW_GRAY);
/* 100 */     arrayOfMap[1] = Collections.unmodifiableMap(localHashMap);
/*     */ 
/* 103 */     localHashMap = new HashMap(1);
/* 104 */     localHashMap.put(TextAttribute.INPUT_METHOD_UNDERLINE, TextAttribute.UNDERLINE_LOW_DOTTED);
/* 105 */     arrayOfMap[2] = Collections.unmodifiableMap(localHashMap);
/*     */ 
/* 108 */     localHashMap = new HashMap(4);
/* 109 */     Color localColor = new Color(0, 0, 128);
/* 110 */     localHashMap.put(TextAttribute.FOREGROUND, localColor);
/* 111 */     localHashMap.put(TextAttribute.BACKGROUND, Color.white);
/* 112 */     localHashMap.put(TextAttribute.SWAP_COLORS, TextAttribute.SWAP_COLORS_ON);
/* 113 */     localHashMap.put(TextAttribute.INPUT_METHOD_UNDERLINE, TextAttribute.UNDERLINE_LOW_ONE_PIXEL);
/* 114 */     arrayOfMap[3] = Collections.unmodifiableMap(localHashMap);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.windows.WInputMethod
 * JD-Core Version:    0.6.2
 */