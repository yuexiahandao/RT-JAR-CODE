/*     */ package sun.awt.im;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.InputMethodEvent;
/*     */ import java.awt.event.InputMethodListener;
/*     */ import java.awt.font.TextAttribute;
/*     */ import java.awt.font.TextHitInfo;
/*     */ import java.awt.im.InputMethodRequests;
/*     */ import java.text.AttributedCharacterIterator;
/*     */ import java.text.AttributedCharacterIterator.Attribute;
/*     */ import java.text.AttributedString;
/*     */ 
/*     */ class CompositionAreaHandler
/*     */   implements InputMethodListener, InputMethodRequests
/*     */ {
/*     */   private static CompositionArea compositionArea;
/*  53 */   private static Object compositionAreaLock = new Object();
/*     */   private static CompositionAreaHandler compositionAreaOwner;
/*     */   private AttributedCharacterIterator composedText;
/*  57 */   private TextHitInfo caret = null;
/*  58 */   private Component clientComponent = null;
/*     */   private InputMethodContext inputMethodContext;
/* 192 */   private static final AttributedCharacterIterator.Attribute[] IM_ATTRIBUTES = { TextAttribute.INPUT_METHOD_HIGHLIGHT };
/*     */ 
/* 305 */   private static final AttributedCharacterIterator EMPTY_TEXT = new AttributedString("").getIterator();
/*     */ 
/*     */   CompositionAreaHandler(InputMethodContext paramInputMethodContext)
/*     */   {
/*  65 */     this.inputMethodContext = paramInputMethodContext;
/*     */   }
/*     */ 
/*     */   private void createCompositionArea()
/*     */   {
/*  72 */     synchronized (compositionAreaLock) {
/*  73 */       compositionArea = new CompositionArea();
/*  74 */       if (compositionAreaOwner != null) {
/*  75 */         compositionArea.setHandlerInfo(compositionAreaOwner, this.inputMethodContext);
/*     */       }
/*     */ 
/*  79 */       if (this.clientComponent != null) {
/*  80 */         InputMethodRequests localInputMethodRequests = this.clientComponent.getInputMethodRequests();
/*  81 */         if ((localInputMethodRequests != null) && (this.inputMethodContext.useBelowTheSpotInput()))
/*  82 */           setCompositionAreaUndecorated(true);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   void setClientComponent(Component paramComponent)
/*     */   {
/*  89 */     this.clientComponent = paramComponent;
/*     */   }
/*     */ 
/*     */   void grabCompositionArea(boolean paramBoolean)
/*     */   {
/* 101 */     synchronized (compositionAreaLock) {
/* 102 */       if (compositionAreaOwner != this) {
/* 103 */         compositionAreaOwner = this;
/* 104 */         if (compositionArea != null) {
/* 105 */           compositionArea.setHandlerInfo(this, this.inputMethodContext);
/*     */         }
/* 107 */         if (paramBoolean)
/*     */         {
/* 109 */           if ((this.composedText != null) && (compositionArea == null)) {
/* 110 */             createCompositionArea();
/*     */           }
/* 112 */           if (compositionArea != null)
/* 113 */             compositionArea.setText(this.composedText, this.caret);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   void releaseCompositionArea()
/*     */   {
/* 125 */     synchronized (compositionAreaLock) {
/* 126 */       if (compositionAreaOwner == this) {
/* 127 */         compositionAreaOwner = null;
/* 128 */         if (compositionArea != null) {
/* 129 */           compositionArea.setHandlerInfo(null, null);
/* 130 */           compositionArea.setText(null, null);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static void closeCompositionArea()
/*     */   {
/* 141 */     if (compositionArea != null)
/* 142 */       synchronized (compositionAreaLock) {
/* 143 */         compositionAreaOwner = null;
/* 144 */         compositionArea.setHandlerInfo(null, null);
/* 145 */         compositionArea.setText(null, null);
/*     */       }
/*     */   }
/*     */ 
/*     */   boolean isCompositionAreaVisible()
/*     */   {
/* 154 */     if (compositionArea != null) {
/* 155 */       return compositionArea.isCompositionAreaVisible();
/*     */     }
/*     */ 
/* 158 */     return false;
/*     */   }
/*     */ 
/*     */   void setCompositionAreaVisible(boolean paramBoolean)
/*     */   {
/* 166 */     if (compositionArea != null)
/* 167 */       compositionArea.setCompositionAreaVisible(paramBoolean);
/*     */   }
/*     */ 
/*     */   void processInputMethodEvent(InputMethodEvent paramInputMethodEvent)
/*     */   {
/* 172 */     if (paramInputMethodEvent.getID() == 1100)
/* 173 */       inputMethodTextChanged(paramInputMethodEvent);
/*     */     else
/* 175 */       caretPositionChanged(paramInputMethodEvent);
/*     */   }
/*     */ 
/*     */   void setCompositionAreaUndecorated(boolean paramBoolean)
/*     */   {
/* 183 */     if (compositionArea != null)
/* 184 */       compositionArea.setCompositionAreaUndecorated(paramBoolean);
/*     */   }
/*     */ 
/*     */   public void inputMethodTextChanged(InputMethodEvent paramInputMethodEvent)
/*     */   {
/* 196 */     AttributedCharacterIterator localAttributedCharacterIterator = paramInputMethodEvent.getText();
/* 197 */     int i = paramInputMethodEvent.getCommittedCharacterCount();
/*     */ 
/* 200 */     this.composedText = null;
/* 201 */     this.caret = null;
/* 202 */     if ((localAttributedCharacterIterator != null) && (i < localAttributedCharacterIterator.getEndIndex() - localAttributedCharacterIterator.getBeginIndex()))
/*     */     {
/* 206 */       if (compositionArea == null) {
/* 207 */         createCompositionArea();
/*     */       }
/*     */ 
/* 212 */       AttributedString localAttributedString = new AttributedString(localAttributedCharacterIterator, localAttributedCharacterIterator.getBeginIndex() + i, localAttributedCharacterIterator.getEndIndex(), IM_ATTRIBUTES);
/*     */ 
/* 215 */       localAttributedString.addAttribute(TextAttribute.FONT, compositionArea.getFont());
/* 216 */       this.composedText = localAttributedString.getIterator();
/* 217 */       this.caret = paramInputMethodEvent.getCaret();
/*     */     }
/*     */ 
/* 220 */     if (compositionArea != null) {
/* 221 */       compositionArea.setText(this.composedText, this.caret);
/*     */     }
/*     */ 
/* 225 */     if (i > 0) {
/* 226 */       this.inputMethodContext.dispatchCommittedText((Component)paramInputMethodEvent.getSource(), localAttributedCharacterIterator, i);
/*     */ 
/* 230 */       if (isCompositionAreaVisible()) {
/* 231 */         compositionArea.updateWindowLocation();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 236 */     paramInputMethodEvent.consume();
/*     */   }
/*     */ 
/*     */   public void caretPositionChanged(InputMethodEvent paramInputMethodEvent) {
/* 240 */     if (compositionArea != null) {
/* 241 */       compositionArea.setCaret(paramInputMethodEvent.getCaret());
/*     */     }
/*     */ 
/* 245 */     paramInputMethodEvent.consume();
/*     */   }
/*     */ 
/*     */   InputMethodRequests getClientInputMethodRequests()
/*     */   {
/* 259 */     if (this.clientComponent != null) {
/* 260 */       return this.clientComponent.getInputMethodRequests();
/*     */     }
/*     */ 
/* 263 */     return null;
/*     */   }
/*     */ 
/*     */   public Rectangle getTextLocation(TextHitInfo paramTextHitInfo) {
/* 267 */     synchronized (compositionAreaLock) {
/* 268 */       if ((compositionAreaOwner == this) && (isCompositionAreaVisible()))
/* 269 */         return compositionArea.getTextLocation(paramTextHitInfo);
/* 270 */       if (this.composedText != null)
/*     */       {
/* 272 */         return new Rectangle(0, 0, 0, 10);
/*     */       }
/* 274 */       InputMethodRequests localInputMethodRequests = getClientInputMethodRequests();
/* 275 */       if (localInputMethodRequests != null) {
/* 276 */         return localInputMethodRequests.getTextLocation(paramTextHitInfo);
/*     */       }
/*     */ 
/* 279 */       return new Rectangle(0, 0, 0, 10);
/*     */     }
/*     */   }
/*     */ 
/*     */   public TextHitInfo getLocationOffset(int paramInt1, int paramInt2)
/*     */   {
/* 286 */     synchronized (compositionAreaLock) {
/* 287 */       if ((compositionAreaOwner == this) && (isCompositionAreaVisible())) {
/* 288 */         return compositionArea.getLocationOffset(paramInt1, paramInt2);
/*     */       }
/* 290 */       return null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getInsertPositionOffset()
/*     */   {
/* 296 */     InputMethodRequests localInputMethodRequests = getClientInputMethodRequests();
/* 297 */     if (localInputMethodRequests != null) {
/* 298 */       return localInputMethodRequests.getInsertPositionOffset();
/*     */     }
/*     */ 
/* 302 */     return 0;
/*     */   }
/*     */ 
/*     */   public AttributedCharacterIterator getCommittedText(int paramInt1, int paramInt2, AttributedCharacterIterator.Attribute[] paramArrayOfAttribute)
/*     */   {
/* 311 */     InputMethodRequests localInputMethodRequests = getClientInputMethodRequests();
/* 312 */     if (localInputMethodRequests != null) {
/* 313 */       return localInputMethodRequests.getCommittedText(paramInt1, paramInt2, paramArrayOfAttribute);
/*     */     }
/*     */ 
/* 317 */     return EMPTY_TEXT;
/*     */   }
/*     */ 
/*     */   public int getCommittedTextLength() {
/* 321 */     InputMethodRequests localInputMethodRequests = getClientInputMethodRequests();
/* 322 */     if (localInputMethodRequests != null) {
/* 323 */       return localInputMethodRequests.getCommittedTextLength();
/*     */     }
/*     */ 
/* 327 */     return 0;
/*     */   }
/*     */ 
/*     */   public AttributedCharacterIterator cancelLatestCommittedText(AttributedCharacterIterator.Attribute[] paramArrayOfAttribute)
/*     */   {
/* 332 */     InputMethodRequests localInputMethodRequests = getClientInputMethodRequests();
/* 333 */     if (localInputMethodRequests != null) {
/* 334 */       return localInputMethodRequests.cancelLatestCommittedText(paramArrayOfAttribute);
/*     */     }
/*     */ 
/* 338 */     return null;
/*     */   }
/*     */ 
/*     */   public AttributedCharacterIterator getSelectedText(AttributedCharacterIterator.Attribute[] paramArrayOfAttribute) {
/* 342 */     InputMethodRequests localInputMethodRequests = getClientInputMethodRequests();
/* 343 */     if (localInputMethodRequests != null) {
/* 344 */       return localInputMethodRequests.getSelectedText(paramArrayOfAttribute);
/*     */     }
/*     */ 
/* 348 */     return EMPTY_TEXT;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.im.CompositionAreaHandler
 * JD-Core Version:    0.6.2
 */