/*     */ package javax.swing.text.html;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Point;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.net.URLEncoder;
/*     */ import java.util.BitSet;
/*     */ import javax.swing.AbstractListModel;
/*     */ import javax.swing.Box;
/*     */ import javax.swing.ButtonModel;
/*     */ import javax.swing.ComboBoxModel;
/*     */ import javax.swing.DefaultButtonModel;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JEditorPane;
/*     */ import javax.swing.JFileChooser;
/*     */ import javax.swing.JList;
/*     */ import javax.swing.JPasswordField;
/*     */ import javax.swing.JRadioButton;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTextArea;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.JToggleButton.ToggleButtonModel;
/*     */ import javax.swing.ListModel;
/*     */ import javax.swing.ListSelectionModel;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.event.HyperlinkEvent.EventType;
/*     */ import javax.swing.event.ListDataListener;
/*     */ import javax.swing.text.AbstractDocument;
/*     */ import javax.swing.text.AttributeSet;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.ComponentView;
/*     */ import javax.swing.text.Document;
/*     */ import javax.swing.text.Element;
/*     */ import javax.swing.text.ElementIterator;
/*     */ import javax.swing.text.PlainDocument;
/*     */ import javax.swing.text.StyleConstants;
/*     */ 
/*     */ public class FormView extends ComponentView
/*     */   implements ActionListener
/*     */ {
/*     */ 
/*     */   @Deprecated
/* 117 */   public static final String SUBMIT = new String("Submit Query");
/*     */ 
/*     */   @Deprecated
/* 126 */   public static final String RESET = new String("Reset");
/*     */   static final String PostDataProperty = "javax.swing.JEditorPane.postdata";
/*     */   private short maxIsPreferred;
/*     */ 
/*     */   public FormView(Element paramElement)
/*     */   {
/* 148 */     super(paramElement);
/*     */   }
/*     */ 
/*     */   protected Component createComponent()
/*     */   {
/* 157 */     AttributeSet localAttributeSet = getElement().getAttributes();
/* 158 */     HTML.Tag localTag = (HTML.Tag)localAttributeSet.getAttribute(StyleConstants.NameAttribute);
/*     */ 
/* 160 */     Object localObject1 = null;
/* 161 */     Object localObject2 = localAttributeSet.getAttribute(StyleConstants.ModelAttribute);
/*     */ 
/* 165 */     removeStaleListenerForModel(localObject2);
/* 166 */     if (localTag == HTML.Tag.INPUT) {
/* 167 */       localObject1 = createInputComponent(localAttributeSet, localObject2);
/*     */     }
/*     */     else
/*     */     {
/*     */       Object localObject3;
/*     */       int i;
/* 168 */       if (localTag == HTML.Tag.SELECT)
/*     */       {
/* 170 */         if ((localObject2 instanceof OptionListModel))
/*     */         {
/* 172 */           localObject3 = new JList((ListModel)localObject2);
/* 173 */           i = HTML.getIntegerAttributeValue(localAttributeSet, HTML.Attribute.SIZE, 1);
/*     */ 
/* 176 */           ((JList)localObject3).setVisibleRowCount(i);
/* 177 */           ((JList)localObject3).setSelectionModel((ListSelectionModel)localObject2);
/* 178 */           localObject1 = new JScrollPane((Component)localObject3);
/*     */         } else {
/* 180 */           localObject1 = new JComboBox((ComboBoxModel)localObject2);
/* 181 */           this.maxIsPreferred = 3;
/*     */         }
/* 183 */       } else if (localTag == HTML.Tag.TEXTAREA) {
/* 184 */         localObject3 = new JTextArea((Document)localObject2);
/* 185 */         i = HTML.getIntegerAttributeValue(localAttributeSet, HTML.Attribute.ROWS, 1);
/*     */ 
/* 188 */         ((JTextArea)localObject3).setRows(i);
/* 189 */         int j = HTML.getIntegerAttributeValue(localAttributeSet, HTML.Attribute.COLS, 20);
/*     */ 
/* 192 */         this.maxIsPreferred = 3;
/* 193 */         ((JTextArea)localObject3).setColumns(j);
/* 194 */         localObject1 = new JScrollPane((Component)localObject3, 22, 32);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 199 */     if (localObject1 != null) {
/* 200 */       ((JComponent)localObject1).setAlignmentY(1.0F);
/*     */     }
/* 202 */     return localObject1;
/*     */   }
/*     */ 
/*     */   private JComponent createInputComponent(AttributeSet paramAttributeSet, Object paramObject)
/*     */   {
/* 215 */     Object localObject1 = null;
/* 216 */     String str1 = (String)paramAttributeSet.getAttribute(HTML.Attribute.TYPE);
/*     */     String str2;
/*     */     Object localObject3;
/* 218 */     if ((str1.equals("submit")) || (str1.equals("reset"))) {
/* 219 */       str2 = (String)paramAttributeSet.getAttribute(HTML.Attribute.VALUE);
/*     */ 
/* 221 */       if (str2 == null) {
/* 222 */         if (str1.equals("submit"))
/* 223 */           str2 = UIManager.getString("FormView.submitButtonText");
/*     */         else {
/* 225 */           str2 = UIManager.getString("FormView.resetButtonText");
/*     */         }
/*     */       }
/* 228 */       localObject3 = new JButton(str2);
/* 229 */       if (paramObject != null) {
/* 230 */         ((JButton)localObject3).setModel((ButtonModel)paramObject);
/* 231 */         ((JButton)localObject3).addActionListener(this);
/*     */       }
/* 233 */       localObject1 = localObject3;
/* 234 */       this.maxIsPreferred = 3;
/*     */     }
/*     */     else
/*     */     {
/*     */       Object localObject4;
/* 235 */       if (str1.equals("image")) {
/* 236 */         str2 = (String)paramAttributeSet.getAttribute(HTML.Attribute.SRC);
/*     */         try
/*     */         {
/* 239 */           URL localURL = ((HTMLDocument)getElement().getDocument()).getBase();
/* 240 */           localObject4 = new URL(localURL, str2);
/* 241 */           ImageIcon localImageIcon = new ImageIcon((URL)localObject4);
/* 242 */           localObject3 = new JButton(localImageIcon);
/*     */         } catch (MalformedURLException localMalformedURLException) {
/* 244 */           localObject3 = new JButton(str2);
/*     */         }
/* 246 */         if (paramObject != null) {
/* 247 */           ((JButton)localObject3).setModel((ButtonModel)paramObject);
/* 248 */           ((JButton)localObject3).addMouseListener(new MouseEventListener());
/*     */         }
/* 250 */         localObject1 = localObject3;
/* 251 */         this.maxIsPreferred = 3;
/* 252 */       } else if (str1.equals("checkbox")) {
/* 253 */         localObject1 = new JCheckBox();
/* 254 */         if (paramObject != null) {
/* 255 */           ((JCheckBox)localObject1).setModel((JToggleButton.ToggleButtonModel)paramObject);
/*     */         }
/* 257 */         this.maxIsPreferred = 3;
/* 258 */       } else if (str1.equals("radio")) {
/* 259 */         localObject1 = new JRadioButton();
/* 260 */         if (paramObject != null) {
/* 261 */           ((JRadioButton)localObject1).setModel((JToggleButton.ToggleButtonModel)paramObject);
/*     */         }
/* 263 */         this.maxIsPreferred = 3;
/* 264 */       } else if (str1.equals("text")) {
/* 265 */         int i = HTML.getIntegerAttributeValue(paramAttributeSet, HTML.Attribute.SIZE, -1);
/*     */ 
/* 269 */         if (i > 0) {
/* 270 */           localObject3 = new JTextField();
/* 271 */           ((JTextField)localObject3).setColumns(i);
/*     */         }
/*     */         else {
/* 274 */           localObject3 = new JTextField();
/* 275 */           ((JTextField)localObject3).setColumns(20);
/*     */         }
/* 277 */         localObject1 = localObject3;
/* 278 */         if (paramObject != null) {
/* 279 */           ((JTextField)localObject3).setDocument((Document)paramObject);
/*     */         }
/* 281 */         ((JTextField)localObject3).addActionListener(this);
/* 282 */         this.maxIsPreferred = 3;
/*     */       }
/*     */       else
/*     */       {
/*     */         Object localObject2;
/*     */         int j;
/* 283 */         if (str1.equals("password")) {
/* 284 */           localObject2 = new JPasswordField();
/* 285 */           localObject1 = localObject2;
/* 286 */           if (paramObject != null) {
/* 287 */             ((JPasswordField)localObject2).setDocument((Document)paramObject);
/*     */           }
/* 289 */           j = HTML.getIntegerAttributeValue(paramAttributeSet, HTML.Attribute.SIZE, -1);
/*     */ 
/* 292 */           ((JPasswordField)localObject2).setColumns(j > 0 ? j : 20);
/* 293 */           ((JPasswordField)localObject2).addActionListener(this);
/* 294 */           this.maxIsPreferred = 3;
/* 295 */         } else if (str1.equals("file")) {
/* 296 */           localObject2 = new JTextField();
/* 297 */           if (paramObject != null) {
/* 298 */             ((JTextField)localObject2).setDocument((Document)paramObject);
/*     */           }
/* 300 */           j = HTML.getIntegerAttributeValue(paramAttributeSet, HTML.Attribute.SIZE, -1);
/*     */ 
/* 302 */           ((JTextField)localObject2).setColumns(j > 0 ? j : 20);
/* 303 */           JButton localJButton = new JButton(UIManager.getString("FormView.browseFileButtonText"));
/*     */ 
/* 305 */           localObject4 = Box.createHorizontalBox();
/* 306 */           ((Box)localObject4).add((Component)localObject2);
/* 307 */           ((Box)localObject4).add(Box.createHorizontalStrut(5));
/* 308 */           ((Box)localObject4).add(localJButton);
/* 309 */           localJButton.addActionListener(new BrowseFileAction(paramAttributeSet, (Document)paramObject));
/*     */ 
/* 311 */           localObject1 = localObject4;
/* 312 */           this.maxIsPreferred = 3;
/*     */         }
/*     */       }
/*     */     }
/* 314 */     return localObject1;
/*     */   }
/*     */ 
/*     */   private void removeStaleListenerForModel(Object paramObject)
/*     */   {
/*     */     Object localObject1;
/*     */     String str;
/* 318 */     if ((paramObject instanceof DefaultButtonModel))
/*     */     {
/* 322 */       localObject1 = (DefaultButtonModel)paramObject;
/* 323 */       str = "javax.swing.AbstractButton$Handler";
/*     */       ActionListener localActionListener;
/* 324 */       for (localActionListener : ((DefaultButtonModel)localObject1).getActionListeners()) {
/* 325 */         if (str.equals(localActionListener.getClass().getName())) {
/* 326 */           ((DefaultButtonModel)localObject1).removeActionListener(localActionListener);
/*     */         }
/*     */       }
/* 329 */       for (localActionListener : ((DefaultButtonModel)localObject1).getChangeListeners()) {
/* 330 */         if (str.equals(localActionListener.getClass().getName())) {
/* 331 */           ((DefaultButtonModel)localObject1).removeChangeListener(localActionListener);
/*     */         }
/*     */       }
/* 334 */       for (localActionListener : ((DefaultButtonModel)localObject1).getItemListeners())
/* 335 */         if (str.equals(localActionListener.getClass().getName()))
/* 336 */           ((DefaultButtonModel)localObject1).removeItemListener(localActionListener);
/*     */     }
/*     */     else
/*     */     {
/*     */       ListDataListener localListDataListener;
/* 339 */       if ((paramObject instanceof AbstractListModel))
/*     */       {
/* 345 */         localObject1 = (AbstractListModel)paramObject;
/* 346 */         str = "javax.swing.plaf.basic.BasicListUI$Handler";
/*     */ 
/* 348 */         ??? = "javax.swing.plaf.basic.BasicComboBoxUI$Handler";
/*     */ 
/* 350 */         for (localListDataListener : ((AbstractListModel)localObject1).getListDataListeners())
/* 351 */           if ((str.equals(localListDataListener.getClass().getName())) || (((String)???).equals(localListDataListener.getClass().getName())))
/*     */           {
/* 354 */             ((AbstractListModel)localObject1).removeListDataListener(localListDataListener);
/*     */           }
/*     */       }
/* 357 */       else if ((paramObject instanceof AbstractDocument))
/*     */       {
/* 360 */         localObject1 = "javax.swing.plaf.basic.BasicTextUI$UpdateHandler";
/*     */ 
/* 362 */         str = "javax.swing.text.DefaultCaret$Handler";
/*     */ 
/* 364 */         ??? = (AbstractDocument)paramObject;
/* 365 */         for (localListDataListener : ((AbstractDocument)???).getDocumentListeners())
/* 366 */           if ((((String)localObject1).equals(localListDataListener.getClass().getName())) || (str.equals(localListDataListener.getClass().getName())))
/*     */           {
/* 369 */             ((AbstractDocument)???).removeDocumentListener(localListDataListener);
/*     */           }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public float getMaximumSpan(int paramInt)
/*     */   {
/* 390 */     switch (paramInt) {
/*     */     case 0:
/* 392 */       if ((this.maxIsPreferred & 0x1) == 1) {
/* 393 */         super.getMaximumSpan(paramInt);
/* 394 */         return getPreferredSpan(paramInt);
/*     */       }
/* 396 */       return super.getMaximumSpan(paramInt);
/*     */     case 1:
/* 398 */       if ((this.maxIsPreferred & 0x2) == 2) {
/* 399 */         super.getMaximumSpan(paramInt);
/* 400 */         return getPreferredSpan(paramInt);
/*     */       }
/* 402 */       return super.getMaximumSpan(paramInt);
/*     */     }
/*     */ 
/* 406 */     return super.getMaximumSpan(paramInt);
/*     */   }
/*     */ 
/*     */   public void actionPerformed(ActionEvent paramActionEvent)
/*     */   {
/* 425 */     Element localElement = getElement();
/* 426 */     StringBuilder localStringBuilder = new StringBuilder();
/* 427 */     HTMLDocument localHTMLDocument = (HTMLDocument)getDocument();
/* 428 */     AttributeSet localAttributeSet = localElement.getAttributes();
/*     */ 
/* 430 */     String str = (String)localAttributeSet.getAttribute(HTML.Attribute.TYPE);
/*     */ 
/* 432 */     if (str.equals("submit")) {
/* 433 */       getFormData(localStringBuilder);
/* 434 */       submitData(localStringBuilder.toString());
/* 435 */     } else if (str.equals("reset")) {
/* 436 */       resetForm();
/* 437 */     } else if ((str.equals("text")) || (str.equals("password"))) {
/* 438 */       if (isLastTextOrPasswordField()) {
/* 439 */         getFormData(localStringBuilder);
/* 440 */         submitData(localStringBuilder.toString());
/*     */       } else {
/* 442 */         getComponent().transferFocus();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void submitData(String paramString)
/*     */   {
/* 453 */     Element localElement = getFormElement();
/* 454 */     AttributeSet localAttributeSet = localElement.getAttributes();
/* 455 */     HTMLDocument localHTMLDocument = (HTMLDocument)localElement.getDocument();
/* 456 */     URL localURL1 = localHTMLDocument.getBase();
/*     */ 
/* 458 */     String str1 = (String)localAttributeSet.getAttribute(HTML.Attribute.TARGET);
/* 459 */     if (str1 == null) {
/* 460 */       str1 = "_self";
/*     */     }
/*     */ 
/* 463 */     String str2 = (String)localAttributeSet.getAttribute(HTML.Attribute.METHOD);
/* 464 */     if (str2 == null) {
/* 465 */       str2 = "GET";
/*     */     }
/* 467 */     str2 = str2.toLowerCase();
/* 468 */     boolean bool = str2.equals("post");
/* 469 */     if (bool) {
/* 470 */       storePostData(localHTMLDocument, str1, paramString);
/* 473 */     }
/*     */ String str3 = (String)localAttributeSet.getAttribute(HTML.Attribute.ACTION);
/*     */     URL localURL2;
/*     */     try {
/* 476 */       localURL2 = str3 == null ? new URL(localURL1.getProtocol(), localURL1.getHost(), localURL1.getPort(), localURL1.getFile()) : new URL(localURL1, str3);
/*     */ 
/* 480 */       if (!bool) {
/* 481 */         String str4 = paramString.toString();
/* 482 */         localURL2 = new URL(localURL2 + "?" + str4);
/*     */       }
/*     */     } catch (MalformedURLException localMalformedURLException) {
/* 485 */       localURL2 = null;
/*     */     }
/* 487 */     final JEditorPane localJEditorPane = (JEditorPane)getContainer();
/* 488 */     HTMLEditorKit localHTMLEditorKit = (HTMLEditorKit)localJEditorPane.getEditorKit();
/*     */ 
/* 490 */     FormSubmitEvent localFormSubmitEvent = null;
/* 491 */     if ((!localHTMLEditorKit.isAutoFormSubmission()) || (localHTMLDocument.isFrameDocument())) {
/* 492 */       localObject = bool ? FormSubmitEvent.MethodType.POST : FormSubmitEvent.MethodType.GET;
/*     */ 
/* 495 */       localFormSubmitEvent = new FormSubmitEvent(this, HyperlinkEvent.EventType.ACTIVATED, localURL2, localElement, str1, (FormSubmitEvent.MethodType)localObject, paramString);
/*     */     }
/*     */ 
/* 501 */     Object localObject = localFormSubmitEvent;
/* 502 */     final URL localURL3 = localURL2;
/* 503 */     SwingUtilities.invokeLater(new Runnable() {
/*     */       public void run() {
/* 505 */         if (this.val$fse != null)
/* 506 */           localJEditorPane.fireHyperlinkUpdate(this.val$fse);
/*     */         else
/*     */           try {
/* 509 */             localJEditorPane.setPage(localURL3);
/*     */           } catch (IOException localIOException) {
/* 511 */             UIManager.getLookAndFeel().provideErrorFeedback(localJEditorPane);
/*     */           }
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private void storePostData(HTMLDocument paramHTMLDocument, String paramString1, String paramString2)
/*     */   {
/* 530 */     Object localObject = paramHTMLDocument;
/* 531 */     String str = "javax.swing.JEditorPane.postdata";
/*     */ 
/* 533 */     if (paramHTMLDocument.isFrameDocument())
/*     */     {
/* 535 */       FrameView.FrameEditorPane localFrameEditorPane = (FrameView.FrameEditorPane)getContainer();
/*     */ 
/* 537 */       FrameView localFrameView = localFrameEditorPane.getFrameView();
/* 538 */       JEditorPane localJEditorPane = localFrameView.getOutermostJEditorPane();
/* 539 */       if (localJEditorPane != null) {
/* 540 */         localObject = localJEditorPane.getDocument();
/* 541 */         str = str + "." + paramString1;
/*     */       }
/*     */     }
/*     */ 
/* 545 */     ((Document)localObject).putProperty(str, paramString2);
/*     */   }
/*     */ 
/*     */   protected void imageSubmit(String paramString)
/*     */   {
/* 572 */     StringBuilder localStringBuilder = new StringBuilder();
/* 573 */     Element localElement = getElement();
/* 574 */     HTMLDocument localHTMLDocument = (HTMLDocument)localElement.getDocument();
/* 575 */     getFormData(localStringBuilder);
/* 576 */     if (localStringBuilder.length() > 0) {
/* 577 */       localStringBuilder.append('&');
/*     */     }
/* 579 */     localStringBuilder.append(paramString);
/* 580 */     submitData(localStringBuilder.toString());
/*     */   }
/*     */ 
/*     */   private String getImageData(Point paramPoint)
/*     */   {
/* 599 */     String str1 = paramPoint.x + ":" + paramPoint.y;
/* 600 */     int i = str1.indexOf(':');
/* 601 */     String str2 = str1.substring(0, i);
/* 602 */     String str3 = str1.substring(++i);
/* 603 */     String str4 = (String)getElement().getAttributes().getAttribute(HTML.Attribute.NAME);
/*     */     String str5;
/* 606 */     if ((str4 == null) || (str4.equals(""))) {
/* 607 */       str5 = "x=" + str2 + "&y=" + str3;
/*     */     } else {
/* 609 */       str4 = URLEncoder.encode(str4);
/* 610 */       str5 = str4 + ".x" + "=" + str2 + "&" + str4 + ".y" + "=" + str3;
/*     */     }
/* 612 */     return str5;
/*     */   }
/*     */ 
/*     */   private Element getFormElement()
/*     */   {
/* 630 */     Element localElement = getElement();
/* 631 */     while (localElement != null) {
/* 632 */       if (localElement.getAttributes().getAttribute(StyleConstants.NameAttribute) == HTML.Tag.FORM)
/*     */       {
/* 634 */         return localElement;
/*     */       }
/* 636 */       localElement = localElement.getParentElement();
/*     */     }
/* 638 */     return null;
/*     */   }
/*     */ 
/*     */   private void getFormData(StringBuilder paramStringBuilder)
/*     */   {
/* 654 */     Element localElement1 = getFormElement();
/* 655 */     if (localElement1 != null) {
/* 656 */       ElementIterator localElementIterator = new ElementIterator(localElement1);
/*     */       Element localElement2;
/* 659 */       while ((localElement2 = localElementIterator.next()) != null)
/* 660 */         if (isControl(localElement2)) {
/* 661 */           String str = (String)localElement2.getAttributes().getAttribute(HTML.Attribute.TYPE);
/*     */ 
/* 664 */           if ((str == null) || (!str.equals("submit")) || (localElement2 == getElement()))
/*     */           {
/* 667 */             if ((str == null) || (!str.equals("image")))
/*     */             {
/* 672 */               loadElementDataIntoBuffer(localElement2, paramStringBuilder);
/*     */             }
/*     */           }
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void loadElementDataIntoBuffer(Element paramElement, StringBuilder paramStringBuilder)
/*     */   {
/* 689 */     AttributeSet localAttributeSet = paramElement.getAttributes();
/* 690 */     String str1 = (String)localAttributeSet.getAttribute(HTML.Attribute.NAME);
/* 691 */     if (str1 == null) {
/* 692 */       return;
/*     */     }
/* 694 */     String str2 = null;
/* 695 */     HTML.Tag localTag = (HTML.Tag)paramElement.getAttributes().getAttribute(StyleConstants.NameAttribute);
/*     */ 
/* 698 */     if (localTag == HTML.Tag.INPUT)
/* 699 */       str2 = getInputElementData(localAttributeSet);
/* 700 */     else if (localTag == HTML.Tag.TEXTAREA)
/* 701 */       str2 = getTextAreaData(localAttributeSet);
/* 702 */     else if (localTag == HTML.Tag.SELECT) {
/* 703 */       loadSelectData(localAttributeSet, paramStringBuilder);
/*     */     }
/*     */ 
/* 706 */     if ((str1 != null) && (str2 != null))
/* 707 */       appendBuffer(paramStringBuilder, str1, str2);
/*     */   }
/*     */ 
/*     */   private String getInputElementData(AttributeSet paramAttributeSet)
/*     */   {
/* 721 */     Object localObject1 = paramAttributeSet.getAttribute(StyleConstants.ModelAttribute);
/* 722 */     String str1 = (String)paramAttributeSet.getAttribute(HTML.Attribute.TYPE);
/* 723 */     Object localObject2 = null;
/*     */     Object localObject3;
/* 725 */     if ((str1.equals("text")) || (str1.equals("password"))) {
/* 726 */       localObject3 = (Document)localObject1;
/*     */       try {
/* 728 */         localObject2 = ((Document)localObject3).getText(0, ((Document)localObject3).getLength());
/*     */       } catch (BadLocationException localBadLocationException1) {
/* 730 */         localObject2 = null;
/*     */       }
/* 732 */     } else if ((str1.equals("submit")) || (str1.equals("hidden"))) {
/* 733 */       localObject2 = (String)paramAttributeSet.getAttribute(HTML.Attribute.VALUE);
/* 734 */       if (localObject2 == null)
/* 735 */         localObject2 = "";
/*     */     }
/* 737 */     else if ((str1.equals("radio")) || (str1.equals("checkbox"))) {
/* 738 */       localObject3 = (ButtonModel)localObject1;
/* 739 */       if (((ButtonModel)localObject3).isSelected()) {
/* 740 */         localObject2 = (String)paramAttributeSet.getAttribute(HTML.Attribute.VALUE);
/* 741 */         if (localObject2 == null)
/* 742 */           localObject2 = "on";
/*     */       }
/*     */     }
/* 745 */     else if (str1.equals("file")) {
/* 746 */       localObject3 = (Document)localObject1;
/*     */       String str2;
/*     */       try {
/* 750 */         str2 = ((Document)localObject3).getText(0, ((Document)localObject3).getLength());
/*     */       } catch (BadLocationException localBadLocationException2) {
/* 752 */         str2 = null;
/*     */       }
/* 754 */       if ((str2 != null) && (str2.length() > 0)) {
/* 755 */         localObject2 = str2;
/*     */       }
/*     */     }
/* 758 */     return localObject2;
/*     */   }
/*     */ 
/*     */   private String getTextAreaData(AttributeSet paramAttributeSet)
/*     */   {
/* 767 */     Document localDocument = (Document)paramAttributeSet.getAttribute(StyleConstants.ModelAttribute);
/*     */     try {
/* 769 */       return localDocument.getText(0, localDocument.getLength()); } catch (BadLocationException localBadLocationException) {
/*     */     }
/* 771 */     return null;
/*     */   }
/*     */ 
/*     */   private void loadSelectData(AttributeSet paramAttributeSet, StringBuilder paramStringBuilder)
/*     */   {
/* 783 */     String str = (String)paramAttributeSet.getAttribute(HTML.Attribute.NAME);
/* 784 */     if (str == null) {
/* 785 */       return;
/*     */     }
/* 787 */     Object localObject1 = paramAttributeSet.getAttribute(StyleConstants.ModelAttribute);
/*     */     Object localObject2;
/* 788 */     if ((localObject1 instanceof OptionListModel)) {
/* 789 */       localObject2 = (OptionListModel)localObject1;
/*     */ 
/* 791 */       for (int i = 0; i < ((OptionListModel)localObject2).getSize(); i++)
/* 792 */         if (((OptionListModel)localObject2).isSelectedIndex(i)) {
/* 793 */           Option localOption2 = (Option)((OptionListModel)localObject2).getElementAt(i);
/* 794 */           appendBuffer(paramStringBuilder, str, localOption2.getValue());
/*     */         }
/*     */     }
/* 797 */     else if ((localObject1 instanceof ComboBoxModel)) {
/* 798 */       localObject2 = (ComboBoxModel)localObject1;
/* 799 */       Option localOption1 = (Option)((ComboBoxModel)localObject2).getSelectedItem();
/* 800 */       if (localOption1 != null)
/* 801 */         appendBuffer(paramStringBuilder, str, localOption1.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   private void appendBuffer(StringBuilder paramStringBuilder, String paramString1, String paramString2)
/*     */   {
/* 813 */     if (paramStringBuilder.length() > 0) {
/* 814 */       paramStringBuilder.append('&');
/*     */     }
/* 816 */     String str1 = URLEncoder.encode(paramString1);
/* 817 */     paramStringBuilder.append(str1);
/* 818 */     paramStringBuilder.append('=');
/* 819 */     String str2 = URLEncoder.encode(paramString2);
/* 820 */     paramStringBuilder.append(str2);
/*     */   }
/*     */ 
/*     */   private boolean isControl(Element paramElement)
/*     */   {
/* 827 */     return paramElement.isLeaf();
/*     */   }
/*     */ 
/*     */   boolean isLastTextOrPasswordField()
/*     */   {
/* 837 */     Element localElement1 = getFormElement();
/* 838 */     Element localElement2 = getElement();
/*     */ 
/* 840 */     if (localElement1 != null) {
/* 841 */       ElementIterator localElementIterator = new ElementIterator(localElement1);
/*     */ 
/* 843 */       int i = 0;
/*     */       Element localElement3;
/* 845 */       while ((localElement3 = localElementIterator.next()) != null) {
/* 846 */         if (localElement3 == localElement2) {
/* 847 */           i = 1;
/*     */         }
/* 849 */         else if ((i != 0) && (isControl(localElement3))) {
/* 850 */           AttributeSet localAttributeSet = localElement3.getAttributes();
/*     */ 
/* 852 */           if (HTMLDocument.matchNameAttribute(localAttributeSet, HTML.Tag.INPUT))
/*     */           {
/* 854 */             String str = (String)localAttributeSet.getAttribute(HTML.Attribute.TYPE);
/*     */ 
/* 857 */             if (("text".equals(str)) || ("password".equals(str))) {
/* 858 */               return false;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 864 */     return true;
/*     */   }
/*     */ 
/*     */   void resetForm()
/*     */   {
/* 876 */     Element localElement1 = getFormElement();
/*     */ 
/* 878 */     if (localElement1 != null) {
/* 879 */       ElementIterator localElementIterator = new ElementIterator(localElement1);
/*     */       Element localElement2;
/* 882 */       while ((localElement2 = localElementIterator.next()) != null)
/* 883 */         if (isControl(localElement2)) {
/* 884 */           AttributeSet localAttributeSet = localElement2.getAttributes();
/* 885 */           Object localObject1 = localAttributeSet.getAttribute(StyleConstants.ModelAttribute);
/*     */           Object localObject2;
/* 887 */           if ((localObject1 instanceof TextAreaDocument)) {
/* 888 */             localObject2 = (TextAreaDocument)localObject1;
/* 889 */             ((TextAreaDocument)localObject2).reset();
/* 890 */           } else if ((localObject1 instanceof PlainDocument)) {
/*     */             try {
/* 892 */               localObject2 = (PlainDocument)localObject1;
/* 893 */               ((PlainDocument)localObject2).remove(0, ((PlainDocument)localObject2).getLength());
/* 894 */               if (HTMLDocument.matchNameAttribute(localAttributeSet, HTML.Tag.INPUT))
/*     */               {
/* 896 */                 String str = (String)localAttributeSet.getAttribute(HTML.Attribute.VALUE);
/*     */ 
/* 898 */                 if (str != null)
/* 899 */                   ((PlainDocument)localObject2).insertString(0, str, null);
/*     */               }
/*     */             }
/*     */             catch (BadLocationException localBadLocationException)
/*     */             {
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/*     */             Object localObject3;
/* 904 */             if ((localObject1 instanceof OptionListModel)) {
/* 905 */               localObject3 = (OptionListModel)localObject1;
/* 906 */               int i = ((OptionListModel)localObject3).getSize();
/* 907 */               for (int j = 0; j < i; j++) {
/* 908 */                 ((OptionListModel)localObject3).removeIndexInterval(j, j);
/*     */               }
/* 910 */               BitSet localBitSet = ((OptionListModel)localObject3).getInitialSelection();
/* 911 */               for (int k = 0; k < localBitSet.size(); k++)
/* 912 */                 if (localBitSet.get(k))
/* 913 */                   ((OptionListModel)localObject3).addSelectionInterval(k, k);
/*     */             }
/*     */             else
/*     */             {
/*     */               Object localObject4;
/* 916 */               if ((localObject1 instanceof OptionComboBoxModel)) {
/* 917 */                 localObject3 = (OptionComboBoxModel)localObject1;
/* 918 */                 localObject4 = ((OptionComboBoxModel)localObject3).getInitialSelection();
/* 919 */                 if (localObject4 != null)
/* 920 */                   ((OptionComboBoxModel)localObject3).setSelectedItem(localObject4);
/*     */               }
/* 922 */               else if ((localObject1 instanceof JToggleButton.ToggleButtonModel)) {
/* 923 */                 boolean bool = (String)localAttributeSet.getAttribute(HTML.Attribute.CHECKED) != null;
/*     */ 
/* 925 */                 localObject4 = (JToggleButton.ToggleButtonModel)localObject1;
/*     */ 
/* 927 */                 ((JToggleButton.ToggleButtonModel)localObject4).setSelected(bool);
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   private class BrowseFileAction
/*     */     implements ActionListener
/*     */   {
/*     */     private AttributeSet attrs;
/*     */     private Document model;
/*     */ 
/*     */     BrowseFileAction(AttributeSet paramDocument, Document arg3)
/*     */     {
/* 946 */       this.attrs = paramDocument;
/*     */       Object localObject;
/* 947 */       this.model = localObject;
/*     */     }
/*     */ 
/*     */     public void actionPerformed(ActionEvent paramActionEvent)
/*     */     {
/* 953 */       JFileChooser localJFileChooser = new JFileChooser();
/* 954 */       localJFileChooser.setMultiSelectionEnabled(false);
/* 955 */       if (localJFileChooser.showOpenDialog(FormView.this.getContainer()) == 0)
/*     */       {
/* 957 */         File localFile = localJFileChooser.getSelectedFile();
/*     */ 
/* 959 */         if (localFile != null)
/*     */           try {
/* 961 */             if (this.model.getLength() > 0) {
/* 962 */               this.model.remove(0, this.model.getLength());
/*     */             }
/* 964 */             this.model.insertString(0, localFile.getPath(), null);
/*     */           }
/*     */           catch (BadLocationException localBadLocationException)
/*     */           {
/*     */           }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected class MouseEventListener extends MouseAdapter
/*     */   {
/*     */     protected MouseEventListener()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void mouseReleased(MouseEvent paramMouseEvent)
/*     */     {
/* 558 */       String str = FormView.this.getImageData(paramMouseEvent.getPoint());
/* 559 */       FormView.this.imageSubmit(str);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.html.FormView
 * JD-Core Version:    0.6.2
 */