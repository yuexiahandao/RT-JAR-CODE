/*     */ package sun.awt.im;
/*     */ 
/*     */ import java.awt.AWTEvent;
/*     */ import java.awt.Component;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.awt.HeadlessException;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.Window;
/*     */ import java.awt.event.InputMethodEvent;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.font.TextHitInfo;
/*     */ import java.awt.im.InputMethodRequests;
/*     */ import java.awt.im.spi.InputMethod;
/*     */ import java.security.AccessController;
/*     */ import java.text.AttributedCharacterIterator;
/*     */ import java.text.AttributedCharacterIterator.Attribute;
/*     */ import java.text.AttributedString;
/*     */ import javax.swing.JFrame;
/*     */ import sun.awt.InputMethodSupport;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ public class InputMethodContext extends InputContext
/*     */   implements java.awt.im.spi.InputMethodContext
/*     */ {
/*     */   private boolean dispatchingCommittedText;
/*     */   private CompositionAreaHandler compositionAreaHandler;
/*  67 */   private Object compositionAreaHandlerLock = new Object();
/*     */ 
/*  82 */   private static boolean belowTheSpotInputRequested = "below-the-spot".equals(str);
/*     */   private boolean inputMethodSupportsBelowTheSpot;
/*     */ 
/*     */   void setInputMethodSupportsBelowTheSpot(boolean paramBoolean)
/*     */   {
/*  93 */     this.inputMethodSupportsBelowTheSpot = paramBoolean;
/*     */   }
/*     */ 
/*     */   boolean useBelowTheSpotInput() {
/*  97 */     return (belowTheSpotInputRequested) && (this.inputMethodSupportsBelowTheSpot);
/*     */   }
/*     */ 
/*     */   private boolean haveActiveClient() {
/* 101 */     Component localComponent = getClientComponent();
/* 102 */     return (localComponent != null) && (localComponent.getInputMethodRequests() != null);
/*     */   }
/*     */ 
/*     */   public void dispatchInputMethodEvent(int paramInt1, AttributedCharacterIterator paramAttributedCharacterIterator, int paramInt2, TextHitInfo paramTextHitInfo1, TextHitInfo paramTextHitInfo2)
/*     */   {
/* 115 */     Component localComponent = getClientComponent();
/* 116 */     if (localComponent != null) {
/* 117 */       InputMethodEvent localInputMethodEvent = new InputMethodEvent(localComponent, paramInt1, paramAttributedCharacterIterator, paramInt2, paramTextHitInfo1, paramTextHitInfo2);
/*     */ 
/* 120 */       if ((haveActiveClient()) && (!useBelowTheSpotInput()))
/* 121 */         localComponent.dispatchEvent(localInputMethodEvent);
/*     */       else
/* 123 */         getCompositionAreaHandler(true).processInputMethodEvent(localInputMethodEvent);
/*     */     }
/*     */   }
/*     */ 
/*     */   synchronized void dispatchCommittedText(Component paramComponent, AttributedCharacterIterator paramAttributedCharacterIterator, int paramInt)
/*     */   {
/* 144 */     if ((paramInt == 0) || (paramAttributedCharacterIterator.getEndIndex() <= paramAttributedCharacterIterator.getBeginIndex()))
/*     */     {
/* 146 */       return;
/*     */     }
/* 148 */     long l = System.currentTimeMillis();
/* 149 */     this.dispatchingCommittedText = true;
/*     */     try {
/* 151 */       InputMethodRequests localInputMethodRequests = paramComponent.getInputMethodRequests();
/*     */       int i;
/*     */       Object localObject1;
/* 152 */       if (localInputMethodRequests != null)
/*     */       {
/* 154 */         i = paramAttributedCharacterIterator.getBeginIndex();
/* 155 */         localObject1 = new AttributedString(paramAttributedCharacterIterator, i, i + paramInt).getIterator();
/*     */ 
/* 158 */         InputMethodEvent localInputMethodEvent = new InputMethodEvent(paramComponent, 1100, (AttributedCharacterIterator)localObject1, paramInt, null, null);
/*     */ 
/* 165 */         paramComponent.dispatchEvent(localInputMethodEvent);
/*     */       }
/*     */       else {
/* 168 */         i = paramAttributedCharacterIterator.first();
/* 169 */         while ((paramInt-- > 0) && (i != 65535)) {
/* 170 */           localObject1 = new KeyEvent(paramComponent, 400, l, 0, 0, i);
/*     */ 
/* 172 */           paramComponent.dispatchEvent((AWTEvent)localObject1);
/* 173 */           i = paramAttributedCharacterIterator.next();
/*     */         }
/*     */       }
/*     */     } finally {
/* 177 */       this.dispatchingCommittedText = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void dispatchEvent(AWTEvent paramAWTEvent)
/*     */   {
/* 189 */     if ((paramAWTEvent instanceof InputMethodEvent)) {
/* 190 */       if ((((Component)paramAWTEvent.getSource()).getInputMethodRequests() == null) || ((useBelowTheSpotInput()) && (!this.dispatchingCommittedText)))
/*     */       {
/* 192 */         getCompositionAreaHandler(true).processInputMethodEvent((InputMethodEvent)paramAWTEvent);
/*     */       }
/*     */ 
/*     */     }
/* 196 */     else if (!this.dispatchingCommittedText)
/* 197 */       super.dispatchEvent(paramAWTEvent);
/*     */   }
/*     */ 
/*     */   private CompositionAreaHandler getCompositionAreaHandler(boolean paramBoolean)
/*     */   {
/* 208 */     synchronized (this.compositionAreaHandlerLock) {
/* 209 */       if (this.compositionAreaHandler == null) {
/* 210 */         this.compositionAreaHandler = new CompositionAreaHandler(this);
/*     */       }
/* 212 */       this.compositionAreaHandler.setClientComponent(getClientComponent());
/* 213 */       if (paramBoolean) {
/* 214 */         this.compositionAreaHandler.grabCompositionArea(false);
/*     */       }
/*     */ 
/* 217 */       return this.compositionAreaHandler;
/*     */     }
/*     */   }
/*     */ 
/*     */   void grabCompositionArea(boolean paramBoolean)
/*     */   {
/* 227 */     synchronized (this.compositionAreaHandlerLock) {
/* 228 */       if (this.compositionAreaHandler != null) {
/* 229 */         this.compositionAreaHandler.grabCompositionArea(paramBoolean);
/*     */       }
/*     */       else
/*     */       {
/* 233 */         CompositionAreaHandler.closeCompositionArea();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   void releaseCompositionArea()
/*     */   {
/* 243 */     synchronized (this.compositionAreaHandlerLock) {
/* 244 */       if (this.compositionAreaHandler != null)
/* 245 */         this.compositionAreaHandler.releaseCompositionArea();
/*     */     }
/*     */   }
/*     */ 
/*     */   boolean isCompositionAreaVisible()
/*     */   {
/* 257 */     if (this.compositionAreaHandler != null) {
/* 258 */       return this.compositionAreaHandler.isCompositionAreaVisible();
/*     */     }
/*     */ 
/* 261 */     return false;
/*     */   }
/*     */ 
/*     */   void setCompositionAreaVisible(boolean paramBoolean)
/*     */   {
/* 270 */     if (this.compositionAreaHandler != null)
/* 271 */       this.compositionAreaHandler.setCompositionAreaVisible(paramBoolean);
/*     */   }
/*     */ 
/*     */   public Rectangle getTextLocation(TextHitInfo paramTextHitInfo)
/*     */   {
/* 279 */     return getReq().getTextLocation(paramTextHitInfo);
/*     */   }
/*     */ 
/*     */   public TextHitInfo getLocationOffset(int paramInt1, int paramInt2)
/*     */   {
/* 286 */     return getReq().getLocationOffset(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public int getInsertPositionOffset()
/*     */   {
/* 293 */     return getReq().getInsertPositionOffset();
/*     */   }
/*     */ 
/*     */   public AttributedCharacterIterator getCommittedText(int paramInt1, int paramInt2, AttributedCharacterIterator.Attribute[] paramArrayOfAttribute)
/*     */   {
/* 302 */     return getReq().getCommittedText(paramInt1, paramInt2, paramArrayOfAttribute);
/*     */   }
/*     */ 
/*     */   public int getCommittedTextLength()
/*     */   {
/* 309 */     return getReq().getCommittedTextLength();
/*     */   }
/*     */ 
/*     */   public AttributedCharacterIterator cancelLatestCommittedText(AttributedCharacterIterator.Attribute[] paramArrayOfAttribute)
/*     */   {
/* 317 */     return getReq().cancelLatestCommittedText(paramArrayOfAttribute);
/*     */   }
/*     */ 
/*     */   public AttributedCharacterIterator getSelectedText(AttributedCharacterIterator.Attribute[] paramArrayOfAttribute)
/*     */   {
/* 324 */     return getReq().getSelectedText(paramArrayOfAttribute);
/*     */   }
/*     */ 
/*     */   private InputMethodRequests getReq() {
/* 328 */     if ((haveActiveClient()) && (!useBelowTheSpotInput())) {
/* 329 */       return getClientComponent().getInputMethodRequests();
/*     */     }
/* 331 */     return getCompositionAreaHandler(false);
/*     */   }
/*     */ 
/*     */   public Window createInputMethodWindow(String paramString, boolean paramBoolean)
/*     */   {
/* 337 */     InputContext localInputContext = paramBoolean ? this : null;
/* 338 */     return createInputMethodWindow(paramString, localInputContext, false);
/*     */   }
/*     */ 
/*     */   public JFrame createInputMethodJFrame(String paramString, boolean paramBoolean)
/*     */   {
/* 343 */     InputContext localInputContext = paramBoolean ? this : null;
/* 344 */     return (JFrame)createInputMethodWindow(paramString, localInputContext, true);
/*     */   }
/*     */ 
/*     */   static Window createInputMethodWindow(String paramString, InputContext paramInputContext, boolean paramBoolean) {
/* 348 */     if (GraphicsEnvironment.isHeadless()) {
/* 349 */       throw new HeadlessException();
/*     */     }
/* 351 */     if (paramBoolean) {
/* 352 */       return new InputMethodJFrame(paramString, paramInputContext);
/*     */     }
/* 354 */     Toolkit localToolkit = Toolkit.getDefaultToolkit();
/* 355 */     if ((localToolkit instanceof InputMethodSupport)) {
/* 356 */       return ((InputMethodSupport)localToolkit).createInputMethodWindow(paramString, paramInputContext);
/*     */     }
/*     */ 
/* 360 */     throw new InternalError("Input methods must be supported");
/*     */   }
/*     */ 
/*     */   public void enableClientWindowNotification(InputMethod paramInputMethod, boolean paramBoolean)
/*     */   {
/* 367 */     super.enableClientWindowNotification(paramInputMethod, paramBoolean);
/*     */   }
/*     */ 
/*     */   void setCompositionAreaUndecorated(boolean paramBoolean)
/*     */   {
/* 374 */     if (this.compositionAreaHandler != null)
/* 375 */       this.compositionAreaHandler.setCompositionAreaUndecorated(paramBoolean);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  75 */     String str = (String)AccessController.doPrivileged(new GetPropertyAction("java.awt.im.style", null));
/*     */ 
/*  78 */     if (str == null) {
/*  79 */       Toolkit.getDefaultToolkit(); str = Toolkit.getProperty("java.awt.im.style", null);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.im.InputMethodContext
 * JD-Core Version:    0.6.2
 */