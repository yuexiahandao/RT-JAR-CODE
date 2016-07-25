/*     */ package javax.swing.text;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.io.Serializable;
/*     */ import javax.swing.Action;
/*     */ import javax.swing.JEditorPane;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.event.CaretEvent;
/*     */ import javax.swing.event.CaretListener;
/*     */ 
/*     */ public class StyledEditorKit extends DefaultEditorKit
/*     */ {
/* 194 */   private static final ViewFactory defaultFactory = new StyledViewFactory();
/*     */   Element currentRun;
/*     */   Element currentParagraph;
/*     */   MutableAttributeSet inputAttributes;
/*     */   private AttributeTracker inputAttributeUpdater;
/* 331 */   private static final Action[] defaultActions = { new FontFamilyAction("font-family-SansSerif", "SansSerif"), new FontFamilyAction("font-family-Monospaced", "Monospaced"), new FontFamilyAction("font-family-Serif", "Serif"), new FontSizeAction("font-size-8", 8), new FontSizeAction("font-size-10", 10), new FontSizeAction("font-size-12", 12), new FontSizeAction("font-size-14", 14), new FontSizeAction("font-size-16", 16), new FontSizeAction("font-size-18", 18), new FontSizeAction("font-size-24", 24), new FontSizeAction("font-size-36", 36), new FontSizeAction("font-size-48", 48), new AlignmentAction("left-justify", 0), new AlignmentAction("center-justify", 1), new AlignmentAction("right-justify", 2), new BoldAction(), new ItalicAction(), new StyledInsertBreakAction(), new UnderlineAction() };
/*     */ 
/*     */   public StyledEditorKit()
/*     */   {
/*  53 */     createInputAttributeUpdated();
/*  54 */     createInputAttributes();
/*     */   }
/*     */ 
/*     */   public MutableAttributeSet getInputAttributes()
/*     */   {
/*  69 */     return this.inputAttributes;
/*     */   }
/*     */ 
/*     */   public Element getCharacterAttributeRun()
/*     */   {
/*  79 */     return this.currentRun;
/*     */   }
/*     */ 
/*     */   public Action[] getActions()
/*     */   {
/*  93 */     return TextAction.augmentList(super.getActions(), defaultActions);
/*     */   }
/*     */ 
/*     */   public Document createDefaultDocument()
/*     */   {
/* 103 */     return new DefaultStyledDocument();
/*     */   }
/*     */ 
/*     */   public void install(JEditorPane paramJEditorPane)
/*     */   {
/* 113 */     paramJEditorPane.addCaretListener(this.inputAttributeUpdater);
/* 114 */     paramJEditorPane.addPropertyChangeListener(this.inputAttributeUpdater);
/* 115 */     Caret localCaret = paramJEditorPane.getCaret();
/* 116 */     if (localCaret != null)
/* 117 */       this.inputAttributeUpdater.updateInputAttributes(localCaret.getDot(), localCaret.getMark(), paramJEditorPane);
/*     */   }
/*     */ 
/*     */   public void deinstall(JEditorPane paramJEditorPane)
/*     */   {
/* 130 */     paramJEditorPane.removeCaretListener(this.inputAttributeUpdater);
/* 131 */     paramJEditorPane.removePropertyChangeListener(this.inputAttributeUpdater);
/*     */ 
/* 134 */     this.currentRun = null;
/* 135 */     this.currentParagraph = null;
/*     */   }
/*     */ 
/*     */   public ViewFactory getViewFactory()
/*     */   {
/* 154 */     return defaultFactory;
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/* 163 */     StyledEditorKit localStyledEditorKit = (StyledEditorKit)super.clone();
/* 164 */     localStyledEditorKit.currentRun = (localStyledEditorKit.currentParagraph = null);
/* 165 */     localStyledEditorKit.createInputAttributeUpdated();
/* 166 */     localStyledEditorKit.createInputAttributes();
/* 167 */     return localStyledEditorKit;
/*     */   }
/*     */ 
/*     */   private void createInputAttributes()
/*     */   {
/* 174 */     this.inputAttributes = new SimpleAttributeSet() {
/*     */       public AttributeSet getResolveParent() {
/* 176 */         return StyledEditorKit.this.currentParagraph != null ? StyledEditorKit.this.currentParagraph.getAttributes() : null;
/*     */       }
/*     */ 
/*     */       public Object clone()
/*     */       {
/* 181 */         return new SimpleAttributeSet(this);
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   private void createInputAttributeUpdated()
/*     */   {
/* 190 */     this.inputAttributeUpdater = new AttributeTracker();
/*     */   }
/*     */ 
/*     */   protected void createInputAttributes(Element paramElement, MutableAttributeSet paramMutableAttributeSet)
/*     */   {
/* 291 */     if ((paramElement.getAttributes().getAttributeCount() > 0) || (paramElement.getEndOffset() - paramElement.getStartOffset() > 1) || (paramElement.getEndOffset() < paramElement.getDocument().getLength()))
/*     */     {
/* 294 */       paramMutableAttributeSet.removeAttributes(paramMutableAttributeSet);
/* 295 */       paramMutableAttributeSet.addAttributes(paramElement.getAttributes());
/* 296 */       paramMutableAttributeSet.removeAttribute(StyleConstants.ComponentAttribute);
/* 297 */       paramMutableAttributeSet.removeAttribute(StyleConstants.IconAttribute);
/* 298 */       paramMutableAttributeSet.removeAttribute("$ename");
/* 299 */       paramMutableAttributeSet.removeAttribute(StyleConstants.ComposedTextAttribute);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class AlignmentAction extends StyledEditorKit.StyledTextAction
/*     */   {
/*     */     private int a;
/*     */ 
/*     */     public AlignmentAction(String paramString, int paramInt)
/*     */     {
/* 695 */       super();
/* 696 */       this.a = paramInt;
/*     */     }
/*     */ 
/*     */     public void actionPerformed(ActionEvent paramActionEvent)
/*     */     {
/* 705 */       JEditorPane localJEditorPane = getEditor(paramActionEvent);
/* 706 */       if (localJEditorPane != null) {
/* 707 */         int i = this.a;
/* 708 */         if ((paramActionEvent != null) && (paramActionEvent.getSource() == localJEditorPane)) {
/* 709 */           localObject = paramActionEvent.getActionCommand();
/*     */           try {
/* 711 */             i = Integer.parseInt((String)localObject, 10);
/*     */           } catch (NumberFormatException localNumberFormatException) {
/*     */           }
/*     */         }
/* 715 */         Object localObject = new SimpleAttributeSet();
/* 716 */         StyleConstants.setAlignment((MutableAttributeSet)localObject, i);
/* 717 */         setParagraphAttributes(localJEditorPane, (AttributeSet)localObject, false);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   class AttributeTracker
/*     */     implements CaretListener, PropertyChangeListener, Serializable
/*     */   {
/*     */     AttributeTracker()
/*     */     {
/*     */     }
/*     */ 
/*     */     void updateInputAttributes(int paramInt1, int paramInt2, JTextComponent paramJTextComponent)
/*     */     {
/* 229 */       Document localDocument = paramJTextComponent.getDocument();
/* 230 */       if (!(localDocument instanceof StyledDocument)) {
/* 231 */         return;
/*     */       }
/* 233 */       int i = Math.min(paramInt1, paramInt2);
/*     */ 
/* 235 */       StyledDocument localStyledDocument = (StyledDocument)localDocument;
/*     */ 
/* 240 */       StyledEditorKit.this.currentParagraph = localStyledDocument.getParagraphElement(i);
/*     */       Element localElement;
/* 241 */       if ((StyledEditorKit.this.currentParagraph.getStartOffset() == i) || (paramInt1 != paramInt2))
/*     */       {
/* 244 */         localElement = localStyledDocument.getCharacterElement(i);
/*     */       }
/*     */       else {
/* 247 */         localElement = localStyledDocument.getCharacterElement(Math.max(i - 1, 0));
/*     */       }
/* 249 */       if (localElement != StyledEditorKit.this.currentRun)
/*     */       {
/* 258 */         StyledEditorKit.this.currentRun = localElement;
/* 259 */         StyledEditorKit.this.createInputAttributes(StyledEditorKit.this.currentRun, StyledEditorKit.this.getInputAttributes());
/*     */       }
/*     */     }
/*     */ 
/*     */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent) {
/* 264 */       Object localObject1 = paramPropertyChangeEvent.getNewValue();
/* 265 */       Object localObject2 = paramPropertyChangeEvent.getSource();
/*     */ 
/* 267 */       if (((localObject2 instanceof JTextComponent)) && ((localObject1 instanceof Document)))
/*     */       {
/* 270 */         updateInputAttributes(0, 0, (JTextComponent)localObject2);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void caretUpdate(CaretEvent paramCaretEvent) {
/* 275 */       updateInputAttributes(paramCaretEvent.getDot(), paramCaretEvent.getMark(), (JTextComponent)paramCaretEvent.getSource());
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class BoldAction extends StyledEditorKit.StyledTextAction
/*     */   {
/*     */     public BoldAction()
/*     */     {
/* 742 */       super();
/*     */     }
/*     */ 
/*     */     public void actionPerformed(ActionEvent paramActionEvent)
/*     */     {
/* 751 */       JEditorPane localJEditorPane = getEditor(paramActionEvent);
/* 752 */       if (localJEditorPane != null) {
/* 753 */         StyledEditorKit localStyledEditorKit = getStyledEditorKit(localJEditorPane);
/* 754 */         MutableAttributeSet localMutableAttributeSet = localStyledEditorKit.getInputAttributes();
/* 755 */         boolean bool = !StyleConstants.isBold(localMutableAttributeSet);
/* 756 */         SimpleAttributeSet localSimpleAttributeSet = new SimpleAttributeSet();
/* 757 */         StyleConstants.setBold(localSimpleAttributeSet, bool);
/* 758 */         setCharacterAttributes(localJEditorPane, localSimpleAttributeSet, false);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class FontFamilyAction extends StyledEditorKit.StyledTextAction
/*     */   {
/*     */     private String family;
/*     */ 
/*     */     public FontFamilyAction(String paramString1, String paramString2)
/*     */     {
/* 506 */       super();
/* 507 */       this.family = paramString2;
/*     */     }
/*     */ 
/*     */     public void actionPerformed(ActionEvent paramActionEvent)
/*     */     {
/* 516 */       JEditorPane localJEditorPane = getEditor(paramActionEvent);
/* 517 */       if (localJEditorPane != null) {
/* 518 */         Object localObject1 = this.family;
/*     */         Object localObject2;
/* 519 */         if ((paramActionEvent != null) && (paramActionEvent.getSource() == localJEditorPane)) {
/* 520 */           localObject2 = paramActionEvent.getActionCommand();
/* 521 */           if (localObject2 != null) {
/* 522 */             localObject1 = localObject2;
/*     */           }
/*     */         }
/* 525 */         if (localObject1 != null) {
/* 526 */           localObject2 = new SimpleAttributeSet();
/* 527 */           StyleConstants.setFontFamily((MutableAttributeSet)localObject2, (String)localObject1);
/* 528 */           setCharacterAttributes(localJEditorPane, (AttributeSet)localObject2, false);
/*     */         } else {
/* 530 */           UIManager.getLookAndFeel().provideErrorFeedback(localJEditorPane);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class FontSizeAction extends StyledEditorKit.StyledTextAction
/*     */   {
/*     */     private int size;
/*     */ 
/*     */     public FontSizeAction(String paramString, int paramInt)
/*     */     {
/* 562 */       super();
/* 563 */       this.size = paramInt;
/*     */     }
/*     */ 
/*     */     public void actionPerformed(ActionEvent paramActionEvent)
/*     */     {
/* 572 */       JEditorPane localJEditorPane = getEditor(paramActionEvent);
/* 573 */       if (localJEditorPane != null) {
/* 574 */         int i = this.size;
/*     */         Object localObject;
/* 575 */         if ((paramActionEvent != null) && (paramActionEvent.getSource() == localJEditorPane)) {
/* 576 */           localObject = paramActionEvent.getActionCommand();
/*     */           try {
/* 578 */             i = Integer.parseInt((String)localObject, 10);
/*     */           } catch (NumberFormatException localNumberFormatException) {
/*     */           }
/*     */         }
/* 582 */         if (i != 0) {
/* 583 */           localObject = new SimpleAttributeSet();
/* 584 */           StyleConstants.setFontSize((MutableAttributeSet)localObject, i);
/* 585 */           setCharacterAttributes(localJEditorPane, (AttributeSet)localObject, false);
/*     */         } else {
/* 587 */           UIManager.getLookAndFeel().provideErrorFeedback(localJEditorPane);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class ForegroundAction extends StyledEditorKit.StyledTextAction
/*     */   {
/*     */     private Color fg;
/*     */ 
/*     */     public ForegroundAction(String paramString, Color paramColor)
/*     */     {
/* 629 */       super();
/* 630 */       this.fg = paramColor;
/*     */     }
/*     */ 
/*     */     public void actionPerformed(ActionEvent paramActionEvent)
/*     */     {
/* 639 */       JEditorPane localJEditorPane = getEditor(paramActionEvent);
/* 640 */       if (localJEditorPane != null) {
/* 641 */         Color localColor = this.fg;
/*     */         Object localObject;
/* 642 */         if ((paramActionEvent != null) && (paramActionEvent.getSource() == localJEditorPane)) {
/* 643 */           localObject = paramActionEvent.getActionCommand();
/*     */           try {
/* 645 */             localColor = Color.decode((String)localObject);
/*     */           } catch (NumberFormatException localNumberFormatException) {
/*     */           }
/*     */         }
/* 649 */         if (localColor != null) {
/* 650 */           localObject = new SimpleAttributeSet();
/* 651 */           StyleConstants.setForeground((MutableAttributeSet)localObject, localColor);
/* 652 */           setCharacterAttributes(localJEditorPane, (AttributeSet)localObject, false);
/*     */         } else {
/* 654 */           UIManager.getLookAndFeel().provideErrorFeedback(localJEditorPane);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class ItalicAction extends StyledEditorKit.StyledTextAction
/*     */   {
/*     */     public ItalicAction()
/*     */     {
/* 781 */       super();
/*     */     }
/*     */ 
/*     */     public void actionPerformed(ActionEvent paramActionEvent)
/*     */     {
/* 790 */       JEditorPane localJEditorPane = getEditor(paramActionEvent);
/* 791 */       if (localJEditorPane != null) {
/* 792 */         StyledEditorKit localStyledEditorKit = getStyledEditorKit(localJEditorPane);
/* 793 */         MutableAttributeSet localMutableAttributeSet = localStyledEditorKit.getInputAttributes();
/* 794 */         boolean bool = !StyleConstants.isItalic(localMutableAttributeSet);
/* 795 */         SimpleAttributeSet localSimpleAttributeSet = new SimpleAttributeSet();
/* 796 */         StyleConstants.setItalic(localSimpleAttributeSet, bool);
/* 797 */         setCharacterAttributes(localJEditorPane, localSimpleAttributeSet, false);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static class StyledInsertBreakAction extends StyledEditorKit.StyledTextAction
/*     */   {
/*     */     private SimpleAttributeSet tempSet;
/*     */ 
/*     */     StyledInsertBreakAction()
/*     */     {
/* 853 */       super();
/*     */     }
/*     */ 
/*     */     public void actionPerformed(ActionEvent paramActionEvent) {
/* 857 */       JEditorPane localJEditorPane = getEditor(paramActionEvent);
/*     */       Object localObject;
/* 859 */       if (localJEditorPane != null) {
/* 860 */         if ((!localJEditorPane.isEditable()) || (!localJEditorPane.isEnabled())) {
/* 861 */           UIManager.getLookAndFeel().provideErrorFeedback(localJEditorPane);
/* 862 */           return;
/*     */         }
/* 864 */         localObject = getStyledEditorKit(localJEditorPane);
/*     */ 
/* 866 */         if (this.tempSet != null) {
/* 867 */           this.tempSet.removeAttributes(this.tempSet);
/*     */         }
/*     */         else {
/* 870 */           this.tempSet = new SimpleAttributeSet();
/*     */         }
/* 872 */         this.tempSet.addAttributes(((StyledEditorKit)localObject).getInputAttributes());
/* 873 */         localJEditorPane.replaceSelection("\n");
/*     */ 
/* 875 */         MutableAttributeSet localMutableAttributeSet = ((StyledEditorKit)localObject).getInputAttributes();
/*     */ 
/* 877 */         localMutableAttributeSet.removeAttributes(localMutableAttributeSet);
/* 878 */         localMutableAttributeSet.addAttributes(this.tempSet);
/* 879 */         this.tempSet.removeAttributes(this.tempSet);
/*     */       }
/*     */       else
/*     */       {
/* 883 */         localObject = getTextComponent(paramActionEvent);
/*     */ 
/* 885 */         if (localObject != null) {
/* 886 */           if ((!((JTextComponent)localObject).isEditable()) || (!((JTextComponent)localObject).isEnabled())) {
/* 887 */             UIManager.getLookAndFeel().provideErrorFeedback(localJEditorPane);
/* 888 */             return;
/*     */           }
/* 890 */           ((JTextComponent)localObject).replaceSelection("\n");
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static abstract class StyledTextAction extends TextAction
/*     */   {
/*     */     public StyledTextAction(String paramString)
/*     */     {
/* 386 */       super();
/*     */     }
/*     */ 
/*     */     protected final JEditorPane getEditor(ActionEvent paramActionEvent)
/*     */     {
/* 396 */       JTextComponent localJTextComponent = getTextComponent(paramActionEvent);
/* 397 */       if ((localJTextComponent instanceof JEditorPane)) {
/* 398 */         return (JEditorPane)localJTextComponent;
/*     */       }
/* 400 */       return null;
/*     */     }
/*     */ 
/*     */     protected final StyledDocument getStyledDocument(JEditorPane paramJEditorPane)
/*     */     {
/* 411 */       Document localDocument = paramJEditorPane.getDocument();
/* 412 */       if ((localDocument instanceof StyledDocument)) {
/* 413 */         return (StyledDocument)localDocument;
/*     */       }
/* 415 */       throw new IllegalArgumentException("document must be StyledDocument");
/*     */     }
/*     */ 
/*     */     protected final StyledEditorKit getStyledEditorKit(JEditorPane paramJEditorPane)
/*     */     {
/* 426 */       EditorKit localEditorKit = paramJEditorPane.getEditorKit();
/* 427 */       if ((localEditorKit instanceof StyledEditorKit)) {
/* 428 */         return (StyledEditorKit)localEditorKit;
/*     */       }
/* 430 */       throw new IllegalArgumentException("EditorKit must be StyledEditorKit");
/*     */     }
/*     */ 
/*     */     protected final void setCharacterAttributes(JEditorPane paramJEditorPane, AttributeSet paramAttributeSet, boolean paramBoolean)
/*     */     {
/* 447 */       int i = paramJEditorPane.getSelectionStart();
/* 448 */       int j = paramJEditorPane.getSelectionEnd();
/* 449 */       if (i != j) {
/* 450 */         localObject = getStyledDocument(paramJEditorPane);
/* 451 */         ((StyledDocument)localObject).setCharacterAttributes(i, j - i, paramAttributeSet, paramBoolean);
/*     */       }
/* 453 */       Object localObject = getStyledEditorKit(paramJEditorPane);
/* 454 */       MutableAttributeSet localMutableAttributeSet = ((StyledEditorKit)localObject).getInputAttributes();
/* 455 */       if (paramBoolean) {
/* 456 */         localMutableAttributeSet.removeAttributes(localMutableAttributeSet);
/*     */       }
/* 458 */       localMutableAttributeSet.addAttributes(paramAttributeSet);
/*     */     }
/*     */ 
/*     */     protected final void setParagraphAttributes(JEditorPane paramJEditorPane, AttributeSet paramAttributeSet, boolean paramBoolean)
/*     */     {
/* 474 */       int i = paramJEditorPane.getSelectionStart();
/* 475 */       int j = paramJEditorPane.getSelectionEnd();
/* 476 */       StyledDocument localStyledDocument = getStyledDocument(paramJEditorPane);
/* 477 */       localStyledDocument.setParagraphAttributes(i, j - i, paramAttributeSet, paramBoolean);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class StyledViewFactory
/*     */     implements ViewFactory
/*     */   {
/*     */     public View create(Element paramElement)
/*     */     {
/* 308 */       String str = paramElement.getName();
/* 309 */       if (str != null) {
/* 310 */         if (str.equals("content"))
/* 311 */           return new LabelView(paramElement);
/* 312 */         if (str.equals("paragraph"))
/* 313 */           return new ParagraphView(paramElement);
/* 314 */         if (str.equals("section"))
/* 315 */           return new BoxView(paramElement, 1);
/* 316 */         if (str.equals("component"))
/* 317 */           return new ComponentView(paramElement);
/* 318 */         if (str.equals("icon")) {
/* 319 */           return new IconView(paramElement);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 324 */       return new LabelView(paramElement);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class UnderlineAction extends StyledEditorKit.StyledTextAction
/*     */   {
/*     */     public UnderlineAction()
/*     */     {
/* 820 */       super();
/*     */     }
/*     */ 
/*     */     public void actionPerformed(ActionEvent paramActionEvent)
/*     */     {
/* 829 */       JEditorPane localJEditorPane = getEditor(paramActionEvent);
/* 830 */       if (localJEditorPane != null) {
/* 831 */         StyledEditorKit localStyledEditorKit = getStyledEditorKit(localJEditorPane);
/* 832 */         MutableAttributeSet localMutableAttributeSet = localStyledEditorKit.getInputAttributes();
/* 833 */         boolean bool = !StyleConstants.isUnderline(localMutableAttributeSet);
/* 834 */         SimpleAttributeSet localSimpleAttributeSet = new SimpleAttributeSet();
/* 835 */         StyleConstants.setUnderline(localSimpleAttributeSet, bool);
/* 836 */         setCharacterAttributes(localJEditorPane, localSimpleAttributeSet, false);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.StyledEditorKit
 * JD-Core Version:    0.6.2
 */