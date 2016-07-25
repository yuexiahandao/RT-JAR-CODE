/*     */ package javax.swing;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Map;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.text.AbstractDocument;
/*     */ import javax.swing.text.AttributeSet;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.Caret;
/*     */ import javax.swing.text.Document;
/*     */ import javax.swing.text.EditorKit;
/*     */ import javax.swing.text.Element;
/*     */ import javax.swing.text.MutableAttributeSet;
/*     */ import javax.swing.text.Style;
/*     */ import javax.swing.text.StyleConstants;
/*     */ import javax.swing.text.StyledDocument;
/*     */ import javax.swing.text.StyledEditorKit;
/*     */ 
/*     */ public class JTextPane extends JEditorPane
/*     */ {
/*     */   private static final String uiClassID = "TextPaneUI";
/*     */ 
/*     */   public JTextPane()
/*     */   {
/*  91 */     EditorKit localEditorKit = createDefaultEditorKit();
/*  92 */     String str = localEditorKit.getContentType();
/*  93 */     if ((str != null) && (getEditorKitClassNameForContentType(str) == defaultEditorKitMap.get(str)))
/*     */     {
/*  96 */       setEditorKitForContentType(str, localEditorKit);
/*     */     }
/*  98 */     setEditorKit(localEditorKit);
/*     */   }
/*     */ 
/*     */   public JTextPane(StyledDocument paramStyledDocument)
/*     */   {
/* 109 */     this();
/* 110 */     setStyledDocument(paramStyledDocument);
/*     */   }
/*     */ 
/*     */   public String getUIClassID()
/*     */   {
/* 122 */     return "TextPaneUI";
/*     */   }
/*     */ 
/*     */   public void setDocument(Document paramDocument)
/*     */   {
/* 135 */     if ((paramDocument instanceof StyledDocument))
/* 136 */       super.setDocument(paramDocument);
/*     */     else
/* 138 */       throw new IllegalArgumentException("Model must be StyledDocument");
/*     */   }
/*     */ 
/*     */   public void setStyledDocument(StyledDocument paramStyledDocument)
/*     */   {
/* 150 */     super.setDocument(paramStyledDocument);
/*     */   }
/*     */ 
/*     */   public StyledDocument getStyledDocument()
/*     */   {
/* 159 */     return (StyledDocument)getDocument();
/*     */   }
/*     */ 
/*     */   public void replaceSelection(String paramString)
/*     */   {
/* 175 */     replaceSelection(paramString, true);
/*     */   }
/*     */ 
/*     */   private void replaceSelection(String paramString, boolean paramBoolean) {
/* 179 */     if ((paramBoolean) && (!isEditable())) {
/* 180 */       UIManager.getLookAndFeel().provideErrorFeedback(this);
/* 181 */       return;
/*     */     }
/* 183 */     StyledDocument localStyledDocument = getStyledDocument();
/* 184 */     if (localStyledDocument != null)
/*     */       try {
/* 186 */         Caret localCaret = getCaret();
/* 187 */         boolean bool = saveComposedText(localCaret.getDot());
/* 188 */         int i = Math.min(localCaret.getDot(), localCaret.getMark());
/* 189 */         int j = Math.max(localCaret.getDot(), localCaret.getMark());
/* 190 */         AttributeSet localAttributeSet = getInputAttributes().copyAttributes();
/* 191 */         if ((localStyledDocument instanceof AbstractDocument)) {
/* 192 */           ((AbstractDocument)localStyledDocument).replace(i, j - i, paramString, localAttributeSet);
/*     */         }
/*     */         else {
/* 195 */           if (i != j) {
/* 196 */             localStyledDocument.remove(i, j - i);
/*     */           }
/* 198 */           if ((paramString != null) && (paramString.length() > 0)) {
/* 199 */             localStyledDocument.insertString(i, paramString, localAttributeSet);
/*     */           }
/*     */         }
/* 202 */         if (bool)
/* 203 */           restoreComposedText();
/*     */       }
/*     */       catch (BadLocationException localBadLocationException) {
/* 206 */         UIManager.getLookAndFeel().provideErrorFeedback(this);
/*     */       }
/*     */   }
/*     */ 
/*     */   public void insertComponent(Component paramComponent)
/*     */   {
/* 236 */     MutableAttributeSet localMutableAttributeSet = getInputAttributes();
/* 237 */     localMutableAttributeSet.removeAttributes(localMutableAttributeSet);
/* 238 */     StyleConstants.setComponent(localMutableAttributeSet, paramComponent);
/* 239 */     replaceSelection(" ", false);
/* 240 */     localMutableAttributeSet.removeAttributes(localMutableAttributeSet);
/*     */   }
/*     */ 
/*     */   public void insertIcon(Icon paramIcon)
/*     */   {
/* 255 */     MutableAttributeSet localMutableAttributeSet = getInputAttributes();
/* 256 */     localMutableAttributeSet.removeAttributes(localMutableAttributeSet);
/* 257 */     StyleConstants.setIcon(localMutableAttributeSet, paramIcon);
/* 258 */     replaceSelection(" ", false);
/* 259 */     localMutableAttributeSet.removeAttributes(localMutableAttributeSet);
/*     */   }
/*     */ 
/*     */   public Style addStyle(String paramString, Style paramStyle)
/*     */   {
/* 280 */     StyledDocument localStyledDocument = getStyledDocument();
/* 281 */     return localStyledDocument.addStyle(paramString, paramStyle);
/*     */   }
/*     */ 
/*     */   public void removeStyle(String paramString)
/*     */   {
/* 291 */     StyledDocument localStyledDocument = getStyledDocument();
/* 292 */     localStyledDocument.removeStyle(paramString);
/*     */   }
/*     */ 
/*     */   public Style getStyle(String paramString)
/*     */   {
/* 302 */     StyledDocument localStyledDocument = getStyledDocument();
/* 303 */     return localStyledDocument.getStyle(paramString);
/*     */   }
/*     */ 
/*     */   public void setLogicalStyle(Style paramStyle)
/*     */   {
/* 318 */     StyledDocument localStyledDocument = getStyledDocument();
/* 319 */     localStyledDocument.setLogicalStyle(getCaretPosition(), paramStyle);
/*     */   }
/*     */ 
/*     */   public Style getLogicalStyle()
/*     */   {
/* 329 */     StyledDocument localStyledDocument = getStyledDocument();
/* 330 */     return localStyledDocument.getLogicalStyle(getCaretPosition());
/*     */   }
/*     */ 
/*     */   public AttributeSet getCharacterAttributes()
/*     */   {
/* 340 */     StyledDocument localStyledDocument = getStyledDocument();
/* 341 */     Element localElement = localStyledDocument.getCharacterElement(getCaretPosition());
/* 342 */     if (localElement != null) {
/* 343 */       return localElement.getAttributes();
/*     */     }
/* 345 */     return null;
/*     */   }
/*     */ 
/*     */   public void setCharacterAttributes(AttributeSet paramAttributeSet, boolean paramBoolean)
/*     */   {
/* 360 */     int i = getSelectionStart();
/* 361 */     int j = getSelectionEnd();
/*     */     Object localObject;
/* 362 */     if (i != j) {
/* 363 */       localObject = getStyledDocument();
/* 364 */       ((StyledDocument)localObject).setCharacterAttributes(i, j - i, paramAttributeSet, paramBoolean);
/*     */     } else {
/* 366 */       localObject = getInputAttributes();
/* 367 */       if (paramBoolean) {
/* 368 */         ((MutableAttributeSet)localObject).removeAttributes((AttributeSet)localObject);
/*     */       }
/* 370 */       ((MutableAttributeSet)localObject).addAttributes(paramAttributeSet);
/*     */     }
/*     */   }
/*     */ 
/*     */   public AttributeSet getParagraphAttributes()
/*     */   {
/* 381 */     StyledDocument localStyledDocument = getStyledDocument();
/* 382 */     Element localElement = localStyledDocument.getParagraphElement(getCaretPosition());
/* 383 */     if (localElement != null) {
/* 384 */       return localElement.getAttributes();
/*     */     }
/* 386 */     return null;
/*     */   }
/*     */ 
/*     */   public void setParagraphAttributes(AttributeSet paramAttributeSet, boolean paramBoolean)
/*     */   {
/* 400 */     int i = getSelectionStart();
/* 401 */     int j = getSelectionEnd();
/* 402 */     StyledDocument localStyledDocument = getStyledDocument();
/* 403 */     localStyledDocument.setParagraphAttributes(i, j - i, paramAttributeSet, paramBoolean);
/*     */   }
/*     */ 
/*     */   public MutableAttributeSet getInputAttributes()
/*     */   {
/* 412 */     return getStyledEditorKit().getInputAttributes();
/*     */   }
/*     */ 
/*     */   protected final StyledEditorKit getStyledEditorKit()
/*     */   {
/* 421 */     return (StyledEditorKit)getEditorKit();
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 439 */     paramObjectOutputStream.defaultWriteObject();
/* 440 */     if (getUIClassID().equals("TextPaneUI")) {
/* 441 */       byte b = JComponent.getWriteObjCounter(this);
/* 442 */       b = (byte)(b - 1); JComponent.setWriteObjCounter(this, b);
/* 443 */       if ((b == 0) && (this.ui != null))
/* 444 */         this.ui.installUI(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected EditorKit createDefaultEditorKit()
/*     */   {
/* 459 */     return new StyledEditorKit();
/*     */   }
/*     */ 
/*     */   public final void setEditorKit(EditorKit paramEditorKit)
/*     */   {
/* 472 */     if ((paramEditorKit instanceof StyledEditorKit))
/* 473 */       super.setEditorKit(paramEditorKit);
/*     */     else
/* 475 */       throw new IllegalArgumentException("Must be StyledEditorKit");
/*     */   }
/*     */ 
/*     */   protected String paramString()
/*     */   {
/* 490 */     return super.paramString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.JTextPane
 * JD-Core Version:    0.6.2
 */