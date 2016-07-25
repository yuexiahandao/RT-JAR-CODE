/*      */ package javax.swing.text;
/*      */ 
/*      */ import java.awt.ComponentOrientation;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Toolkit;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.OutputStream;
/*      */ import java.io.OutputStreamWriter;
/*      */ import java.io.Reader;
/*      */ import java.io.Writer;
/*      */ import javax.swing.Action;
/*      */ import javax.swing.LookAndFeel;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.plaf.TextUI;
/*      */ import sun.awt.SunToolkit;
/*      */ 
/*      */ public class DefaultEditorKit extends EditorKit
/*      */ {
/*      */   public static final String EndOfLineStringProperty = "__EndOfLine__";
/*      */   public static final String insertContentAction = "insert-content";
/*      */   public static final String insertBreakAction = "insert-break";
/*      */   public static final String insertTabAction = "insert-tab";
/*      */   public static final String deletePrevCharAction = "delete-previous";
/*      */   public static final String deleteNextCharAction = "delete-next";
/*      */   public static final String deleteNextWordAction = "delete-next-word";
/*      */   public static final String deletePrevWordAction = "delete-previous-word";
/*      */   public static final String readOnlyAction = "set-read-only";
/*      */   public static final String writableAction = "set-writable";
/*      */   public static final String cutAction = "cut-to-clipboard";
/*      */   public static final String copyAction = "copy-to-clipboard";
/*      */   public static final String pasteAction = "paste-from-clipboard";
/*      */   public static final String beepAction = "beep";
/*      */   public static final String pageUpAction = "page-up";
/*      */   public static final String pageDownAction = "page-down";
/*      */   static final String selectionPageUpAction = "selection-page-up";
/*      */   static final String selectionPageDownAction = "selection-page-down";
/*      */   static final String selectionPageLeftAction = "selection-page-left";
/*      */   static final String selectionPageRightAction = "selection-page-right";
/*      */   public static final String forwardAction = "caret-forward";
/*      */   public static final String backwardAction = "caret-backward";
/*      */   public static final String selectionForwardAction = "selection-forward";
/*      */   public static final String selectionBackwardAction = "selection-backward";
/*      */   public static final String upAction = "caret-up";
/*      */   public static final String downAction = "caret-down";
/*      */   public static final String selectionUpAction = "selection-up";
/*      */   public static final String selectionDownAction = "selection-down";
/*      */   public static final String beginWordAction = "caret-begin-word";
/*      */   public static final String endWordAction = "caret-end-word";
/*      */   public static final String selectionBeginWordAction = "selection-begin-word";
/*      */   public static final String selectionEndWordAction = "selection-end-word";
/*      */   public static final String previousWordAction = "caret-previous-word";
/*      */   public static final String nextWordAction = "caret-next-word";
/*      */   public static final String selectionPreviousWordAction = "selection-previous-word";
/*      */   public static final String selectionNextWordAction = "selection-next-word";
/*      */   public static final String beginLineAction = "caret-begin-line";
/*      */   public static final String endLineAction = "caret-end-line";
/*      */   public static final String selectionBeginLineAction = "selection-begin-line";
/*      */   public static final String selectionEndLineAction = "selection-end-line";
/*      */   public static final String beginParagraphAction = "caret-begin-paragraph";
/*      */   public static final String endParagraphAction = "caret-end-paragraph";
/*      */   public static final String selectionBeginParagraphAction = "selection-begin-paragraph";
/*      */   public static final String selectionEndParagraphAction = "selection-end-paragraph";
/*      */   public static final String beginAction = "caret-begin";
/*      */   public static final String endAction = "caret-end";
/*      */   public static final String selectionBeginAction = "selection-begin";
/*      */   public static final String selectionEndAction = "selection-end";
/*      */   public static final String selectWordAction = "select-word";
/*      */   public static final String selectLineAction = "select-line";
/*      */   public static final String selectParagraphAction = "select-paragraph";
/*      */   public static final String selectAllAction = "select-all";
/*      */   static final String unselectAction = "unselect";
/*      */   static final String toggleComponentOrientationAction = "toggle-componentOrientation";
/*      */   public static final String defaultKeyTypedAction = "default-typed";
/*  763 */   private static final Action[] defaultActions = { new InsertContentAction(), new DeletePrevCharAction(), new DeleteNextCharAction(), new ReadOnlyAction(), new DeleteWordAction("delete-previous-word"), new DeleteWordAction("delete-next-word"), new WritableAction(), new CutAction(), new CopyAction(), new PasteAction(), new VerticalPageAction("page-up", -1, false), new VerticalPageAction("page-down", 1, false), new VerticalPageAction("selection-page-up", -1, true), new VerticalPageAction("selection-page-down", 1, true), new PageAction("selection-page-left", true, true), new PageAction("selection-page-right", false, true), new InsertBreakAction(), new BeepAction(), new NextVisualPositionAction("caret-forward", false, 3), new NextVisualPositionAction("caret-backward", false, 7), new NextVisualPositionAction("selection-forward", true, 3), new NextVisualPositionAction("selection-backward", true, 7), new NextVisualPositionAction("caret-up", false, 1), new NextVisualPositionAction("caret-down", false, 5), new NextVisualPositionAction("selection-up", true, 1), new NextVisualPositionAction("selection-down", true, 5), new BeginWordAction("caret-begin-word", false), new EndWordAction("caret-end-word", false), new BeginWordAction("selection-begin-word", true), new EndWordAction("selection-end-word", true), new PreviousWordAction("caret-previous-word", false), new NextWordAction("caret-next-word", false), new PreviousWordAction("selection-previous-word", true), new NextWordAction("selection-next-word", true), new BeginLineAction("caret-begin-line", false), new EndLineAction("caret-end-line", false), new BeginLineAction("selection-begin-line", true), new EndLineAction("selection-end-line", true), new BeginParagraphAction("caret-begin-paragraph", false), new EndParagraphAction("caret-end-paragraph", false), new BeginParagraphAction("selection-begin-paragraph", true), new EndParagraphAction("selection-end-paragraph", true), new BeginAction("caret-begin", false), new EndAction("caret-end", false), new BeginAction("selection-begin", true), new EndAction("selection-end", true), new DefaultKeyTypedAction(), new InsertTabAction(), new SelectWordAction(), new SelectLineAction(), new SelectParagraphAction(), new SelectAllAction(), new UnselectAction(), new ToggleComponentOrientationAction(), new DumpModelAction() };
/*      */ 
/*      */   public String getContentType()
/*      */   {
/*   89 */     return "text/plain";
/*      */   }
/*      */ 
/*      */   public ViewFactory getViewFactory()
/*      */   {
/*  101 */     return null;
/*      */   }
/*      */ 
/*      */   public Action[] getActions()
/*      */   {
/*  112 */     return defaultActions;
/*      */   }
/*      */ 
/*      */   public Caret createCaret()
/*      */   {
/*  122 */     return null;
/*      */   }
/*      */ 
/*      */   public Document createDefaultDocument()
/*      */   {
/*  132 */     return new PlainDocument();
/*      */   }
/*      */ 
/*      */   public void read(InputStream paramInputStream, Document paramDocument, int paramInt)
/*      */     throws IOException, BadLocationException
/*      */   {
/*  151 */     read(new InputStreamReader(paramInputStream), paramDocument, paramInt);
/*      */   }
/*      */ 
/*      */   public void write(OutputStream paramOutputStream, Document paramDocument, int paramInt1, int paramInt2)
/*      */     throws IOException, BadLocationException
/*      */   {
/*  169 */     OutputStreamWriter localOutputStreamWriter = new OutputStreamWriter(paramOutputStream);
/*      */ 
/*  171 */     write(localOutputStreamWriter, paramDocument, paramInt1, paramInt2);
/*  172 */     localOutputStreamWriter.flush();
/*      */   }
/*      */ 
/*      */   MutableAttributeSet getInputAttributes()
/*      */   {
/*  184 */     return null;
/*      */   }
/*      */ 
/*      */   public void read(Reader paramReader, Document paramDocument, int paramInt)
/*      */     throws IOException, BadLocationException
/*      */   {
/*  202 */     char[] arrayOfChar = new char[4096];
/*      */ 
/*  204 */     int j = 0;
/*  205 */     int k = 0;
/*  206 */     int m = 0;
/*      */ 
/*  208 */     int i1 = paramDocument.getLength() == 0 ? 1 : 0;
/*  209 */     MutableAttributeSet localMutableAttributeSet = getInputAttributes();
/*      */     int i;
/*  216 */     while ((i = paramReader.read(arrayOfChar, 0, arrayOfChar.length)) != -1) {
/*  217 */       int n = 0;
/*  218 */       for (int i2 = 0; i2 < i; i2++) {
/*  219 */         switch (arrayOfChar[i2]) {
/*      */         case '\r':
/*  221 */           if (j != 0) {
/*  222 */             m = 1;
/*  223 */             if (i2 == 0) {
/*  224 */               paramDocument.insertString(paramInt, "\n", localMutableAttributeSet);
/*  225 */               paramInt++;
/*      */             }
/*      */             else {
/*  228 */               arrayOfChar[(i2 - 1)] = '\n';
/*      */             }
/*      */           }
/*      */           else {
/*  232 */             j = 1;
/*      */           }
/*  234 */           break;
/*      */         case '\n':
/*  236 */           if (j != 0) {
/*  237 */             if (i2 > n + 1) {
/*  238 */               paramDocument.insertString(paramInt, new String(arrayOfChar, n, i2 - n - 1), localMutableAttributeSet);
/*      */ 
/*  240 */               paramInt += i2 - n - 1;
/*      */             }
/*      */ 
/*  244 */             j = 0;
/*  245 */             n = i2;
/*  246 */             k = 1; } break;
/*      */         default:
/*  250 */           if (j != 0) {
/*  251 */             m = 1;
/*  252 */             if (i2 == 0) {
/*  253 */               paramDocument.insertString(paramInt, "\n", localMutableAttributeSet);
/*  254 */               paramInt++;
/*      */             }
/*      */             else {
/*  257 */               arrayOfChar[(i2 - 1)] = '\n';
/*      */             }
/*  259 */             j = 0;
/*      */           }
/*      */           break;
/*      */         }
/*      */       }
/*  264 */       if (n < i) {
/*  265 */         if (j != 0) {
/*  266 */           if (n < i - 1) {
/*  267 */             paramDocument.insertString(paramInt, new String(arrayOfChar, n, i - n - 1), localMutableAttributeSet);
/*      */ 
/*  269 */             paramInt += i - n - 1;
/*      */           }
/*      */         }
/*      */         else {
/*  273 */           paramDocument.insertString(paramInt, new String(arrayOfChar, n, i - n), localMutableAttributeSet);
/*      */ 
/*  275 */           paramInt += i - n;
/*      */         }
/*      */       }
/*      */     }
/*  279 */     if (j != 0) {
/*  280 */       paramDocument.insertString(paramInt, "\n", localMutableAttributeSet);
/*  281 */       m = 1;
/*      */     }
/*  283 */     if (i1 != 0)
/*  284 */       if (k != 0) {
/*  285 */         paramDocument.putProperty("__EndOfLine__", "\r\n");
/*      */       }
/*  287 */       else if (m != 0) {
/*  288 */         paramDocument.putProperty("__EndOfLine__", "\r");
/*      */       }
/*      */       else
/*  291 */         paramDocument.putProperty("__EndOfLine__", "\n");
/*      */   }
/*      */ 
/*      */   public void write(Writer paramWriter, Document paramDocument, int paramInt1, int paramInt2)
/*      */     throws IOException, BadLocationException
/*      */   {
/*  312 */     if ((paramInt1 < 0) || (paramInt1 + paramInt2 > paramDocument.getLength())) {
/*  313 */       throw new BadLocationException("DefaultEditorKit.write", paramInt1);
/*      */     }
/*  315 */     Segment localSegment = new Segment();
/*  316 */     int i = paramInt2;
/*  317 */     int j = paramInt1;
/*  318 */     Object localObject = paramDocument.getProperty("__EndOfLine__");
/*  319 */     if (localObject == null)
/*      */       try {
/*  321 */         localObject = System.getProperty("line.separator");
/*      */       }
/*      */       catch (SecurityException localSecurityException)
/*      */       {
/*      */       }
/*      */     String str;
/*  325 */     if ((localObject instanceof String)) {
/*  326 */       str = (String)localObject;
/*      */     }
/*      */     else {
/*  329 */       str = null;
/*      */     }
/*  331 */     if ((localObject != null) && (!str.equals("\n")));
/*  334 */     while (i > 0) {
/*  335 */       int k = Math.min(i, 4096);
/*  336 */       paramDocument.getText(j, k, localSegment);
/*  337 */       int m = localSegment.offset;
/*  338 */       char[] arrayOfChar = localSegment.array;
/*  339 */       int n = m + localSegment.count;
/*  340 */       for (int i1 = m; i1 < n; i1++) {
/*  341 */         if (arrayOfChar[i1] == '\n') {
/*  342 */           if (i1 > m) {
/*  343 */             paramWriter.write(arrayOfChar, m, i1 - m);
/*      */           }
/*  345 */           paramWriter.write(str);
/*  346 */           m = i1 + 1;
/*      */         }
/*      */       }
/*  349 */       if (n > m) {
/*  350 */         paramWriter.write(arrayOfChar, m, n - m);
/*      */       }
/*  352 */       j += k;
/*  353 */       i -= k;
/*  354 */       continue;
/*      */ 
/*  359 */       while (i > 0) {
/*  360 */         k = Math.min(i, 4096);
/*  361 */         paramDocument.getText(j, k, localSegment);
/*  362 */         paramWriter.write(localSegment.array, localSegment.offset, localSegment.count);
/*  363 */         j += k;
/*  364 */         i -= k;
/*      */       }
/*      */     }
/*  367 */     paramWriter.flush();
/*      */   }
/*      */ 
/*      */   public static class BeepAction extends TextAction
/*      */   {
/*      */     public BeepAction()
/*      */     {
/* 1387 */       super();
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/* 1396 */       JTextComponent localJTextComponent = getTextComponent(paramActionEvent);
/* 1397 */       UIManager.getLookAndFeel().provideErrorFeedback(localJTextComponent);
/*      */     }
/*      */   }
/*      */ 
/*      */   static class BeginAction extends TextAction
/*      */   {
/*      */     private boolean select;
/*      */ 
/*      */     BeginAction(String paramString, boolean paramBoolean)
/*      */     {
/* 2093 */       super();
/* 2094 */       this.select = paramBoolean;
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/* 2099 */       JTextComponent localJTextComponent = getTextComponent(paramActionEvent);
/* 2100 */       if (localJTextComponent != null)
/* 2101 */         if (this.select)
/* 2102 */           localJTextComponent.moveCaretPosition(0);
/*      */         else
/* 2104 */           localJTextComponent.setCaretPosition(0);
/*      */     }
/*      */   }
/*      */ 
/*      */   static class BeginLineAction extends TextAction
/*      */   {
/*      */     private boolean select;
/*      */ 
/*      */     BeginLineAction(String paramString, boolean paramBoolean)
/*      */     {
/* 1944 */       super();
/* 1945 */       this.select = paramBoolean;
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/* 1950 */       JTextComponent localJTextComponent = getTextComponent(paramActionEvent);
/* 1951 */       if (localJTextComponent != null)
/*      */         try {
/* 1953 */           int i = localJTextComponent.getCaretPosition();
/* 1954 */           int j = Utilities.getRowStart(localJTextComponent, i);
/* 1955 */           if (this.select)
/* 1956 */             localJTextComponent.moveCaretPosition(j);
/*      */           else
/* 1958 */             localJTextComponent.setCaretPosition(j);
/*      */         }
/*      */         catch (BadLocationException localBadLocationException) {
/* 1961 */           UIManager.getLookAndFeel().provideErrorFeedback(localJTextComponent);
/*      */         }
/*      */     }
/*      */   }
/*      */ 
/*      */   static class BeginParagraphAction extends TextAction
/*      */   {
/*      */     private boolean select;
/*      */ 
/*      */     BeginParagraphAction(String paramString, boolean paramBoolean)
/*      */     {
/* 2024 */       super();
/* 2025 */       this.select = paramBoolean;
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/* 2030 */       JTextComponent localJTextComponent = getTextComponent(paramActionEvent);
/* 2031 */       if (localJTextComponent != null) {
/* 2032 */         int i = localJTextComponent.getCaretPosition();
/* 2033 */         Element localElement = Utilities.getParagraphElement(localJTextComponent, i);
/* 2034 */         i = localElement.getStartOffset();
/* 2035 */         if (this.select)
/* 2036 */           localJTextComponent.moveCaretPosition(i);
/*      */         else
/* 2038 */           localJTextComponent.setCaretPosition(i);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static class BeginWordAction extends TextAction
/*      */   {
/*      */     private boolean select;
/*      */ 
/*      */     BeginWordAction(String paramString, boolean paramBoolean)
/*      */     {
/* 1740 */       super();
/* 1741 */       this.select = paramBoolean;
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/* 1746 */       JTextComponent localJTextComponent = getTextComponent(paramActionEvent);
/* 1747 */       if (localJTextComponent != null)
/*      */         try {
/* 1749 */           int i = localJTextComponent.getCaretPosition();
/* 1750 */           int j = Utilities.getWordStart(localJTextComponent, i);
/* 1751 */           if (this.select)
/* 1752 */             localJTextComponent.moveCaretPosition(j);
/*      */           else
/* 1754 */             localJTextComponent.setCaretPosition(j);
/*      */         }
/*      */         catch (BadLocationException localBadLocationException) {
/* 1757 */           UIManager.getLookAndFeel().provideErrorFeedback(localJTextComponent);
/*      */         }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class CopyAction extends TextAction
/*      */   {
/*      */     public CopyAction()
/*      */     {
/* 1315 */       super();
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/* 1324 */       JTextComponent localJTextComponent = getTextComponent(paramActionEvent);
/* 1325 */       if (localJTextComponent != null)
/* 1326 */         localJTextComponent.copy();
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class CutAction extends TextAction
/*      */   {
/*      */     public CutAction()
/*      */     {
/* 1279 */       super();
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/* 1288 */       JTextComponent localJTextComponent = getTextComponent(paramActionEvent);
/* 1289 */       if (localJTextComponent != null)
/* 1290 */         localJTextComponent.cut();
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class DefaultKeyTypedAction extends TextAction
/*      */   {
/*      */     public DefaultKeyTypedAction()
/*      */     {
/*  858 */       super();
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/*  867 */       JTextComponent localJTextComponent = getTextComponent(paramActionEvent);
/*  868 */       if ((localJTextComponent != null) && (paramActionEvent != null)) {
/*  869 */         if ((!localJTextComponent.isEditable()) || (!localJTextComponent.isEnabled())) {
/*  870 */           return;
/*      */         }
/*  872 */         String str = paramActionEvent.getActionCommand();
/*  873 */         int i = paramActionEvent.getModifiers();
/*  874 */         if ((str != null) && (str.length() > 0)) {
/*  875 */           boolean bool = true;
/*  876 */           Toolkit localToolkit = Toolkit.getDefaultToolkit();
/*  877 */           if ((localToolkit instanceof SunToolkit)) {
/*  878 */             bool = ((SunToolkit)localToolkit).isPrintableCharacterModifiersMask(i);
/*      */           }
/*      */ 
/*  881 */           if (bool) {
/*  882 */             int j = str.charAt(0);
/*  883 */             if ((j >= 32) && (j != 127))
/*  884 */               localJTextComponent.replaceSelection(str);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static class DeleteNextCharAction extends TextAction
/*      */   {
/*      */     DeleteNextCharAction()
/*      */     {
/* 1093 */       super();
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/* 1098 */       JTextComponent localJTextComponent = getTextComponent(paramActionEvent);
/* 1099 */       int i = 1;
/* 1100 */       if ((localJTextComponent != null) && (localJTextComponent.isEditable()))
/*      */         try {
/* 1102 */           Document localDocument = localJTextComponent.getDocument();
/* 1103 */           Caret localCaret = localJTextComponent.getCaret();
/* 1104 */           int j = localCaret.getDot();
/* 1105 */           int k = localCaret.getMark();
/* 1106 */           if (j != k) {
/* 1107 */             localDocument.remove(Math.min(j, k), Math.abs(j - k));
/* 1108 */             i = 0;
/* 1109 */           } else if (j < localDocument.getLength()) {
/* 1110 */             int m = 1;
/*      */ 
/* 1112 */             if (j < localDocument.getLength() - 1) {
/* 1113 */               String str = localDocument.getText(j, 2);
/* 1114 */               int n = str.charAt(0);
/* 1115 */               int i1 = str.charAt(1);
/*      */ 
/* 1117 */               if ((n >= 55296) && (n <= 56319) && (i1 >= 56320) && (i1 <= 57343))
/*      */               {
/* 1119 */                 m = 2;
/*      */               }
/*      */             }
/*      */ 
/* 1123 */             localDocument.remove(j, m);
/* 1124 */             i = 0;
/*      */           }
/*      */         }
/*      */         catch (BadLocationException localBadLocationException) {
/*      */         }
/* 1129 */       if (i != 0)
/* 1130 */         UIManager.getLookAndFeel().provideErrorFeedback(localJTextComponent);
/*      */     }
/*      */   }
/*      */ 
/*      */   static class DeletePrevCharAction extends TextAction
/*      */   {
/*      */     DeletePrevCharAction()
/*      */     {
/* 1037 */       super();
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/* 1046 */       JTextComponent localJTextComponent = getTextComponent(paramActionEvent);
/* 1047 */       int i = 1;
/* 1048 */       if ((localJTextComponent != null) && (localJTextComponent.isEditable()))
/*      */         try {
/* 1050 */           Document localDocument = localJTextComponent.getDocument();
/* 1051 */           Caret localCaret = localJTextComponent.getCaret();
/* 1052 */           int j = localCaret.getDot();
/* 1053 */           int k = localCaret.getMark();
/* 1054 */           if (j != k) {
/* 1055 */             localDocument.remove(Math.min(j, k), Math.abs(j - k));
/* 1056 */             i = 0;
/* 1057 */           } else if (j > 0) {
/* 1058 */             int m = 1;
/*      */ 
/* 1060 */             if (j > 1) {
/* 1061 */               String str = localDocument.getText(j - 2, 2);
/* 1062 */               int n = str.charAt(0);
/* 1063 */               int i1 = str.charAt(1);
/*      */ 
/* 1065 */               if ((n >= 55296) && (n <= 56319) && (i1 >= 56320) && (i1 <= 57343))
/*      */               {
/* 1067 */                 m = 2;
/*      */               }
/*      */             }
/*      */ 
/* 1071 */             localDocument.remove(j - m, m);
/* 1072 */             i = 0;
/*      */           }
/*      */         }
/*      */         catch (BadLocationException localBadLocationException) {
/*      */         }
/* 1077 */       if (i != 0)
/* 1078 */         UIManager.getLookAndFeel().provideErrorFeedback(localJTextComponent);
/*      */     }
/*      */   }
/*      */ 
/*      */   static class DeleteWordAction extends TextAction
/*      */   {
/*      */     DeleteWordAction(String paramString)
/*      */     {
/* 1142 */       super();
/* 1143 */       assert ((paramString == "delete-previous-word") || (paramString == "delete-next-word"));
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/* 1152 */       JTextComponent localJTextComponent = getTextComponent(paramActionEvent);
/* 1153 */       if ((localJTextComponent != null) && (paramActionEvent != null)) {
/* 1154 */         if ((!localJTextComponent.isEditable()) || (!localJTextComponent.isEnabled())) {
/* 1155 */           UIManager.getLookAndFeel().provideErrorFeedback(localJTextComponent);
/* 1156 */           return;
/*      */         }
/* 1158 */         int i = 1;
/*      */         try {
/* 1160 */           int j = localJTextComponent.getSelectionStart();
/* 1161 */           Element localElement = Utilities.getParagraphElement(localJTextComponent, j);
/*      */           int k;
/* 1164 */           if ("delete-next-word" == getValue("Name")) {
/* 1165 */             k = Utilities.getNextWordInParagraph(localJTextComponent, localElement, j, false);
/*      */ 
/* 1167 */             if (k == -1)
/*      */             {
/* 1169 */               m = localElement.getEndOffset();
/* 1170 */               if (j == m - 1)
/*      */               {
/* 1172 */                 k = m;
/*      */               }
/*      */               else
/* 1175 */                 k = m - 1;
/*      */             }
/*      */           }
/*      */           else {
/* 1179 */             k = Utilities.getPrevWordInParagraph(localJTextComponent, localElement, j);
/*      */ 
/* 1181 */             if (k == -1)
/*      */             {
/* 1183 */               m = localElement.getStartOffset();
/* 1184 */               if (j == m)
/*      */               {
/* 1186 */                 k = m - 1;
/*      */               }
/*      */               else {
/* 1189 */                 k = m;
/*      */               }
/*      */             }
/*      */           }
/* 1193 */           int m = Math.min(j, k);
/* 1194 */           int n = Math.abs(k - j);
/* 1195 */           if (m >= 0) {
/* 1196 */             localJTextComponent.getDocument().remove(m, n);
/* 1197 */             i = 0;
/*      */           }
/*      */         } catch (BadLocationException localBadLocationException) {
/*      */         }
/* 1201 */         if (i != 0)
/* 1202 */           UIManager.getLookAndFeel().provideErrorFeedback(localJTextComponent);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static class DumpModelAction extends TextAction
/*      */   {
/*      */     DumpModelAction()
/*      */     {
/* 1625 */       super();
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent) {
/* 1629 */       JTextComponent localJTextComponent = getTextComponent(paramActionEvent);
/* 1630 */       if (localJTextComponent != null) {
/* 1631 */         Document localDocument = localJTextComponent.getDocument();
/* 1632 */         if ((localDocument instanceof AbstractDocument))
/* 1633 */           ((AbstractDocument)localDocument).dump(System.err);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static class EndAction extends TextAction
/*      */   {
/*      */     private boolean select;
/*      */ 
/*      */     EndAction(String paramString, boolean paramBoolean)
/*      */     {
/* 2121 */       super();
/* 2122 */       this.select = paramBoolean;
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/* 2127 */       JTextComponent localJTextComponent = getTextComponent(paramActionEvent);
/* 2128 */       if (localJTextComponent != null) {
/* 2129 */         Document localDocument = localJTextComponent.getDocument();
/* 2130 */         int i = localDocument.getLength();
/* 2131 */         if (this.select)
/* 2132 */           localJTextComponent.moveCaretPosition(i);
/*      */         else
/* 2134 */           localJTextComponent.setCaretPosition(i);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static class EndLineAction extends TextAction
/*      */   {
/*      */     private boolean select;
/*      */ 
/*      */     EndLineAction(String paramString, boolean paramBoolean)
/*      */     {
/* 1984 */       super();
/* 1985 */       this.select = paramBoolean;
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/* 1990 */       JTextComponent localJTextComponent = getTextComponent(paramActionEvent);
/* 1991 */       if (localJTextComponent != null)
/*      */         try {
/* 1993 */           int i = localJTextComponent.getCaretPosition();
/* 1994 */           int j = Utilities.getRowEnd(localJTextComponent, i);
/* 1995 */           if (this.select)
/* 1996 */             localJTextComponent.moveCaretPosition(j);
/*      */           else
/* 1998 */             localJTextComponent.setCaretPosition(j);
/*      */         }
/*      */         catch (BadLocationException localBadLocationException) {
/* 2001 */           UIManager.getLookAndFeel().provideErrorFeedback(localJTextComponent);
/*      */         }
/*      */     }
/*      */   }
/*      */ 
/*      */   static class EndParagraphAction extends TextAction
/*      */   {
/*      */     private boolean select;
/*      */ 
/*      */     EndParagraphAction(String paramString, boolean paramBoolean)
/*      */     {
/* 2061 */       super();
/* 2062 */       this.select = paramBoolean;
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/* 2067 */       JTextComponent localJTextComponent = getTextComponent(paramActionEvent);
/* 2068 */       if (localJTextComponent != null) {
/* 2069 */         int i = localJTextComponent.getCaretPosition();
/* 2070 */         Element localElement = Utilities.getParagraphElement(localJTextComponent, i);
/* 2071 */         i = Math.min(localJTextComponent.getDocument().getLength(), localElement.getEndOffset());
/*      */ 
/* 2073 */         if (this.select)
/* 2074 */           localJTextComponent.moveCaretPosition(i);
/*      */         else
/* 2076 */           localJTextComponent.setCaretPosition(i);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static class EndWordAction extends TextAction
/*      */   {
/*      */     private boolean select;
/*      */ 
/*      */     EndWordAction(String paramString, boolean paramBoolean)
/*      */     {
/* 1780 */       super();
/* 1781 */       this.select = paramBoolean;
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/* 1786 */       JTextComponent localJTextComponent = getTextComponent(paramActionEvent);
/* 1787 */       if (localJTextComponent != null)
/*      */         try {
/* 1789 */           int i = localJTextComponent.getCaretPosition();
/* 1790 */           int j = Utilities.getWordEnd(localJTextComponent, i);
/* 1791 */           if (this.select)
/* 1792 */             localJTextComponent.moveCaretPosition(j);
/*      */           else
/* 1794 */             localJTextComponent.setCaretPosition(j);
/*      */         }
/*      */         catch (BadLocationException localBadLocationException) {
/* 1797 */           UIManager.getLookAndFeel().provideErrorFeedback(localJTextComponent);
/*      */         }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class InsertBreakAction extends TextAction
/*      */   {
/*      */     public InsertBreakAction()
/*      */     {
/*  963 */       super();
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/*  972 */       JTextComponent localJTextComponent = getTextComponent(paramActionEvent);
/*  973 */       if (localJTextComponent != null) {
/*  974 */         if ((!localJTextComponent.isEditable()) || (!localJTextComponent.isEnabled())) {
/*  975 */           UIManager.getLookAndFeel().provideErrorFeedback(localJTextComponent);
/*  976 */           return;
/*      */         }
/*  978 */         localJTextComponent.replaceSelection("\n");
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class InsertContentAction extends TextAction
/*      */   {
/*      */     public InsertContentAction()
/*      */     {
/*  915 */       super();
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/*  924 */       JTextComponent localJTextComponent = getTextComponent(paramActionEvent);
/*  925 */       if ((localJTextComponent != null) && (paramActionEvent != null)) {
/*  926 */         if ((!localJTextComponent.isEditable()) || (!localJTextComponent.isEnabled())) {
/*  927 */           UIManager.getLookAndFeel().provideErrorFeedback(localJTextComponent);
/*  928 */           return;
/*      */         }
/*  930 */         String str = paramActionEvent.getActionCommand();
/*  931 */         if (str != null)
/*  932 */           localJTextComponent.replaceSelection(str);
/*      */         else
/*  934 */           UIManager.getLookAndFeel().provideErrorFeedback(localJTextComponent);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class InsertTabAction extends TextAction
/*      */   {
/*      */     public InsertTabAction()
/*      */     {
/* 1005 */       super();
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/* 1014 */       JTextComponent localJTextComponent = getTextComponent(paramActionEvent);
/* 1015 */       if (localJTextComponent != null) {
/* 1016 */         if ((!localJTextComponent.isEditable()) || (!localJTextComponent.isEnabled())) {
/* 1017 */           UIManager.getLookAndFeel().provideErrorFeedback(localJTextComponent);
/* 1018 */           return;
/*      */         }
/* 1020 */         localJTextComponent.replaceSelection("\t");
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static class NextVisualPositionAction extends TextAction
/*      */   {
/*      */     private boolean select;
/*      */     private int direction;
/*      */ 
/*      */     NextVisualPositionAction(String paramString, boolean paramBoolean, int paramInt)
/*      */     {
/* 1653 */       super();
/* 1654 */       this.select = paramBoolean;
/* 1655 */       this.direction = paramInt;
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/* 1660 */       JTextComponent localJTextComponent = getTextComponent(paramActionEvent);
/* 1661 */       if (localJTextComponent != null) {
/* 1662 */         Caret localCaret = localJTextComponent.getCaret();
/* 1663 */         Object localObject1 = (localCaret instanceof DefaultCaret) ? (DefaultCaret)localCaret : null;
/*      */ 
/* 1665 */         int i = localCaret.getDot();
/* 1666 */         Position.Bias[] arrayOfBias = new Position.Bias[1];
/* 1667 */         Point localPoint = localCaret.getMagicCaretPosition();
/*      */         try
/*      */         {
/* 1670 */           if ((localPoint == null) && ((this.direction == 1) || (this.direction == 5)))
/*      */           {
/* 1673 */             localObject2 = localObject1 != null ? localJTextComponent.getUI().modelToView(localJTextComponent, i, localObject1.getDotBias()) : localJTextComponent.modelToView(i);
/*      */ 
/* 1677 */             localPoint = new Point(((Rectangle)localObject2).x, ((Rectangle)localObject2).y);
/*      */           }
/*      */ 
/* 1680 */           Object localObject2 = localJTextComponent.getNavigationFilter();
/*      */ 
/* 1682 */           if (localObject2 != null) {
/* 1683 */             i = ((NavigationFilter)localObject2).getNextVisualPositionFrom(localJTextComponent, i, localObject1 != null ? localObject1.getDotBias() : Position.Bias.Forward, this.direction, arrayOfBias);
/*      */           }
/*      */           else
/*      */           {
/* 1689 */             i = localJTextComponent.getUI().getNextVisualPositionFrom(localJTextComponent, i, localObject1 != null ? localObject1.getDotBias() : Position.Bias.Forward, this.direction, arrayOfBias);
/*      */           }
/*      */ 
/* 1694 */           if (arrayOfBias[0] == null) {
/* 1695 */             arrayOfBias[0] = Position.Bias.Forward;
/*      */           }
/* 1697 */           if (localObject1 != null) {
/* 1698 */             if (this.select)
/* 1699 */               localObject1.moveDot(i, arrayOfBias[0]);
/*      */             else {
/* 1701 */               localObject1.setDot(i, arrayOfBias[0]);
/*      */             }
/*      */ 
/*      */           }
/* 1705 */           else if (this.select)
/* 1706 */             localCaret.moveDot(i);
/*      */           else {
/* 1708 */             localCaret.setDot(i);
/*      */           }
/*      */ 
/* 1711 */           if ((localPoint != null) && ((this.direction == 1) || (this.direction == 5)))
/*      */           {
/* 1714 */             localJTextComponent.getCaret().setMagicCaretPosition(localPoint);
/*      */           }
/*      */         }
/*      */         catch (BadLocationException localBadLocationException)
/*      */         {
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static class NextWordAction extends TextAction
/*      */   {
/*      */     private boolean select;
/*      */ 
/*      */     NextWordAction(String paramString, boolean paramBoolean)
/*      */     {
/* 1879 */       super();
/* 1880 */       this.select = paramBoolean;
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/* 1885 */       JTextComponent localJTextComponent = getTextComponent(paramActionEvent);
/* 1886 */       if (localJTextComponent != null) {
/* 1887 */         int i = localJTextComponent.getCaretPosition();
/* 1888 */         int j = 0;
/* 1889 */         int k = i;
/* 1890 */         Element localElement = Utilities.getParagraphElement(localJTextComponent, i);
/*      */         try
/*      */         {
/* 1893 */           i = Utilities.getNextWord(localJTextComponent, i);
/* 1894 */           if ((i >= localElement.getEndOffset()) && (k != localElement.getEndOffset() - 1))
/*      */           {
/* 1898 */             i = localElement.getEndOffset() - 1;
/*      */           }
/*      */         } catch (BadLocationException localBadLocationException) {
/* 1901 */           int m = localJTextComponent.getDocument().getLength();
/* 1902 */           if (i != m) {
/* 1903 */             if (k != localElement.getEndOffset() - 1)
/* 1904 */               i = localElement.getEndOffset() - 1;
/*      */             else {
/* 1906 */               i = m;
/*      */             }
/*      */           }
/*      */           else {
/* 1910 */             j = 1;
/*      */           }
/*      */         }
/* 1913 */         if (j == 0) {
/* 1914 */           if (this.select)
/* 1915 */             localJTextComponent.moveCaretPosition(i);
/*      */           else {
/* 1917 */             localJTextComponent.setCaretPosition(i);
/*      */           }
/*      */         }
/*      */         else
/* 1921 */           UIManager.getLookAndFeel().provideErrorFeedback(localJTextComponent);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static class PageAction extends TextAction
/*      */   {
/*      */     private boolean select;
/*      */     private boolean left;
/*      */ 
/*      */     public PageAction(String paramString, boolean paramBoolean1, boolean paramBoolean2)
/*      */     {
/* 1572 */       super();
/* 1573 */       this.select = paramBoolean2;
/* 1574 */       this.left = paramBoolean1;
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/* 1579 */       JTextComponent localJTextComponent = getTextComponent(paramActionEvent);
/* 1580 */       if (localJTextComponent != null)
/*      */       {
/* 1582 */         Rectangle localRectangle = new Rectangle();
/* 1583 */         localJTextComponent.computeVisibleRect(localRectangle);
/* 1584 */         if (this.left) {
/* 1585 */           localRectangle.x = Math.max(0, localRectangle.x - localRectangle.width);
/*      */         }
/*      */         else {
/* 1588 */           localRectangle.x += localRectangle.width;
/*      */         }
/*      */ 
/* 1591 */         int i = localJTextComponent.getCaretPosition();
/* 1592 */         if (i != -1) {
/* 1593 */           if (this.left) {
/* 1594 */             i = localJTextComponent.viewToModel(new Point(localRectangle.x, localRectangle.y));
/*      */           }
/*      */           else
/*      */           {
/* 1598 */             i = localJTextComponent.viewToModel(new Point(localRectangle.x + localRectangle.width - 1, localRectangle.y + localRectangle.height - 1));
/*      */           }
/*      */ 
/* 1602 */           Document localDocument = localJTextComponent.getDocument();
/* 1603 */           if ((i != 0) && (i > localDocument.getLength() - 1))
/*      */           {
/* 1605 */             i = localDocument.getLength() - 1;
/*      */           }
/* 1607 */           else if (i < 0) {
/* 1608 */             i = 0;
/*      */           }
/* 1610 */           if (this.select)
/* 1611 */             localJTextComponent.moveCaretPosition(i);
/*      */           else
/* 1613 */             localJTextComponent.setCaretPosition(i);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class PasteAction extends TextAction
/*      */   {
/*      */     public PasteAction()
/*      */     {
/* 1352 */       super();
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/* 1361 */       JTextComponent localJTextComponent = getTextComponent(paramActionEvent);
/* 1362 */       if (localJTextComponent != null)
/* 1363 */         localJTextComponent.paste();
/*      */     }
/*      */   }
/*      */ 
/*      */   static class PreviousWordAction extends TextAction
/*      */   {
/*      */     private boolean select;
/*      */ 
/*      */     PreviousWordAction(String paramString, boolean paramBoolean)
/*      */     {
/* 1820 */       super();
/* 1821 */       this.select = paramBoolean;
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/* 1826 */       JTextComponent localJTextComponent = getTextComponent(paramActionEvent);
/* 1827 */       if (localJTextComponent != null) {
/* 1828 */         int i = localJTextComponent.getCaretPosition();
/* 1829 */         int j = 0;
/*      */         try {
/* 1831 */           Element localElement = Utilities.getParagraphElement(localJTextComponent, i);
/*      */ 
/* 1833 */           i = Utilities.getPreviousWord(localJTextComponent, i);
/* 1834 */           if (i < localElement.getStartOffset())
/*      */           {
/* 1837 */             i = Utilities.getParagraphElement(localJTextComponent, i).getEndOffset() - 1;
/*      */           }
/*      */         }
/*      */         catch (BadLocationException localBadLocationException) {
/* 1841 */           if (i != 0) {
/* 1842 */             i = 0;
/*      */           }
/*      */           else {
/* 1845 */             j = 1;
/*      */           }
/*      */         }
/* 1848 */         if (j == 0) {
/* 1849 */           if (this.select)
/* 1850 */             localJTextComponent.moveCaretPosition(i);
/*      */           else {
/* 1852 */             localJTextComponent.setCaretPosition(i);
/*      */           }
/*      */         }
/*      */         else
/* 1856 */           UIManager.getLookAndFeel().provideErrorFeedback(localJTextComponent);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static class ReadOnlyAction extends TextAction
/*      */   {
/*      */     ReadOnlyAction()
/*      */     {
/* 1218 */       super();
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/* 1227 */       JTextComponent localJTextComponent = getTextComponent(paramActionEvent);
/* 1228 */       if (localJTextComponent != null)
/* 1229 */         localJTextComponent.setEditable(false);
/*      */     }
/*      */   }
/*      */ 
/*      */   static class SelectAllAction extends TextAction
/*      */   {
/*      */     SelectAllAction()
/*      */     {
/* 2243 */       super();
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/* 2248 */       JTextComponent localJTextComponent = getTextComponent(paramActionEvent);
/* 2249 */       if (localJTextComponent != null) {
/* 2250 */         Document localDocument = localJTextComponent.getDocument();
/* 2251 */         localJTextComponent.setCaretPosition(0);
/* 2252 */         localJTextComponent.moveCaretPosition(localDocument.getLength());
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static class SelectLineAction extends TextAction
/*      */   {
/*      */     private Action start;
/*      */     private Action end;
/*      */ 
/*      */     SelectLineAction()
/*      */     {
/* 2185 */       super();
/* 2186 */       this.start = new DefaultEditorKit.BeginLineAction("pigdog", false);
/* 2187 */       this.end = new DefaultEditorKit.EndLineAction("pigdog", true);
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/* 2192 */       this.start.actionPerformed(paramActionEvent);
/* 2193 */       this.end.actionPerformed(paramActionEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   static class SelectParagraphAction extends TextAction
/*      */   {
/*      */     private Action start;
/*      */     private Action end;
/*      */ 
/*      */     SelectParagraphAction()
/*      */     {
/* 2214 */       super();
/* 2215 */       this.start = new DefaultEditorKit.BeginParagraphAction("pigdog", false);
/* 2216 */       this.end = new DefaultEditorKit.EndParagraphAction("pigdog", true);
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/* 2221 */       this.start.actionPerformed(paramActionEvent);
/* 2222 */       this.end.actionPerformed(paramActionEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   static class SelectWordAction extends TextAction
/*      */   {
/*      */     private Action start;
/*      */     private Action end;
/*      */ 
/*      */     SelectWordAction()
/*      */     {
/* 2156 */       super();
/* 2157 */       this.start = new DefaultEditorKit.BeginWordAction("pigdog", false);
/* 2158 */       this.end = new DefaultEditorKit.EndWordAction("pigdog", true);
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/* 2163 */       this.start.actionPerformed(paramActionEvent);
/* 2164 */       this.end.actionPerformed(paramActionEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   static class ToggleComponentOrientationAction extends TextAction
/*      */   {
/*      */     ToggleComponentOrientationAction()
/*      */     {
/* 2293 */       super();
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/* 2298 */       JTextComponent localJTextComponent = getTextComponent(paramActionEvent);
/* 2299 */       if (localJTextComponent != null) {
/* 2300 */         ComponentOrientation localComponentOrientation1 = localJTextComponent.getComponentOrientation();
/*      */         ComponentOrientation localComponentOrientation2;
/* 2302 */         if (localComponentOrientation1 == ComponentOrientation.RIGHT_TO_LEFT)
/* 2303 */           localComponentOrientation2 = ComponentOrientation.LEFT_TO_RIGHT;
/*      */         else
/* 2305 */           localComponentOrientation2 = ComponentOrientation.RIGHT_TO_LEFT;
/* 2306 */         localJTextComponent.setComponentOrientation(localComponentOrientation2);
/* 2307 */         localJTextComponent.repaint();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static class UnselectAction extends TextAction
/*      */   {
/*      */     UnselectAction()
/*      */     {
/* 2269 */       super();
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/* 2274 */       JTextComponent localJTextComponent = getTextComponent(paramActionEvent);
/* 2275 */       if (localJTextComponent != null)
/* 2276 */         localJTextComponent.setCaretPosition(localJTextComponent.getCaretPosition());
/*      */     }
/*      */   }
/*      */ 
/*      */   static class VerticalPageAction extends TextAction
/*      */   {
/*      */     private boolean select;
/*      */     private int direction;
/*      */ 
/*      */     public VerticalPageAction(String paramString, int paramInt, boolean paramBoolean)
/*      */     {
/* 1413 */       super();
/* 1414 */       this.select = paramBoolean;
/* 1415 */       this.direction = paramInt;
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/* 1420 */       JTextComponent localJTextComponent = getTextComponent(paramActionEvent);
/* 1421 */       if (localJTextComponent != null) {
/* 1422 */         Rectangle localRectangle1 = localJTextComponent.getVisibleRect();
/* 1423 */         Rectangle localRectangle2 = new Rectangle(localRectangle1);
/* 1424 */         int i = localJTextComponent.getCaretPosition();
/* 1425 */         int j = this.direction * localJTextComponent.getScrollableBlockIncrement(localRectangle1, 1, this.direction);
/*      */ 
/* 1428 */         int k = localRectangle1.y;
/* 1429 */         Caret localCaret = localJTextComponent.getCaret();
/* 1430 */         Point localPoint = localCaret.getMagicCaretPosition();
/*      */ 
/* 1432 */         if (i != -1)
/*      */           try {
/* 1434 */             Rectangle localRectangle3 = localJTextComponent.modelToView(i);
/*      */ 
/* 1436 */             int m = localPoint != null ? localPoint.x : localRectangle3.x;
/*      */ 
/* 1438 */             int n = localRectangle3.height;
/* 1439 */             if (n > 0)
/*      */             {
/* 1442 */               j = j / n * n;
/*      */             }
/* 1444 */             localRectangle2.y = constrainY(localJTextComponent, k + j, localRectangle1.height);
/*      */ 
/* 1449 */             if (localRectangle1.contains(localRectangle3.x, localRectangle3.y))
/*      */             {
/* 1452 */               i1 = localJTextComponent.viewToModel(new Point(m, constrainY(localJTextComponent, localRectangle3.y + j, 0)));
/*      */             }
/* 1459 */             else if (this.direction == -1) {
/* 1460 */               i1 = localJTextComponent.viewToModel(new Point(m, localRectangle2.y));
/*      */             }
/*      */             else
/*      */             {
/* 1464 */               i1 = localJTextComponent.viewToModel(new Point(m, localRectangle2.y + localRectangle1.height));
/*      */             }
/*      */ 
/* 1468 */             int i1 = constrainOffset(localJTextComponent, i1);
/* 1469 */             if (i1 != i)
/*      */             {
/* 1473 */               int i2 = getAdjustedY(localJTextComponent, localRectangle2, i1);
/*      */ 
/* 1475 */               if (((this.direction == -1) && (i2 <= k)) || ((this.direction == 1) && (i2 >= k)))
/*      */               {
/* 1477 */                 localRectangle2.y = i2;
/*      */ 
/* 1479 */                 if (this.select)
/* 1480 */                   localJTextComponent.moveCaretPosition(i1);
/*      */                 else
/* 1482 */                   localJTextComponent.setCaretPosition(i1);
/*      */               }
/*      */             }
/*      */           }
/*      */           catch (BadLocationException localBadLocationException) {
/*      */           }
/* 1488 */         else localRectangle2.y = constrainY(localJTextComponent, k + j, localRectangle1.height);
/*      */ 
/* 1491 */         if (localPoint != null) {
/* 1492 */           localCaret.setMagicCaretPosition(localPoint);
/*      */         }
/* 1494 */         localJTextComponent.scrollRectToVisible(localRectangle2);
/*      */       }
/*      */     }
/*      */ 
/*      */     private int constrainY(JTextComponent paramJTextComponent, int paramInt1, int paramInt2)
/*      */     {
/* 1503 */       if (paramInt1 < 0) {
/* 1504 */         paramInt1 = 0;
/*      */       }
/* 1506 */       else if (paramInt1 + paramInt2 > paramJTextComponent.getHeight()) {
/* 1507 */         paramInt1 = Math.max(0, paramJTextComponent.getHeight() - paramInt2);
/*      */       }
/* 1509 */       return paramInt1;
/*      */     }
/*      */ 
/*      */     private int constrainOffset(JTextComponent paramJTextComponent, int paramInt)
/*      */     {
/* 1517 */       Document localDocument = paramJTextComponent.getDocument();
/*      */ 
/* 1519 */       if ((paramInt != 0) && (paramInt > localDocument.getLength())) {
/* 1520 */         paramInt = localDocument.getLength();
/*      */       }
/* 1522 */       if (paramInt < 0) {
/* 1523 */         paramInt = 0;
/*      */       }
/* 1525 */       return paramInt;
/*      */     }
/*      */ 
/*      */     private int getAdjustedY(JTextComponent paramJTextComponent, Rectangle paramRectangle, int paramInt)
/*      */     {
/* 1533 */       int i = paramRectangle.y;
/*      */       try
/*      */       {
/* 1536 */         Rectangle localRectangle = paramJTextComponent.modelToView(paramInt);
/*      */ 
/* 1538 */         if (localRectangle.y < paramRectangle.y) {
/* 1539 */           i = localRectangle.y;
/*      */         }
/* 1541 */         else if ((localRectangle.y > paramRectangle.y + paramRectangle.height) || (localRectangle.y + localRectangle.height > paramRectangle.y + paramRectangle.height))
/*      */         {
/* 1543 */           i = localRectangle.y + localRectangle.height - paramRectangle.height;
/*      */         }
/*      */       }
/*      */       catch (BadLocationException localBadLocationException)
/*      */       {
/*      */       }
/* 1549 */       return i;
/*      */     }
/*      */   }
/*      */ 
/*      */   static class WritableAction extends TextAction
/*      */   {
/*      */     WritableAction()
/*      */     {
/* 1243 */       super();
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/* 1252 */       JTextComponent localJTextComponent = getTextComponent(paramActionEvent);
/* 1253 */       if (localJTextComponent != null)
/* 1254 */         localJTextComponent.setEditable(true);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.DefaultEditorKit
 * JD-Core Version:    0.6.2
 */