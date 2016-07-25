/*      */ package javax.swing.text.html;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.Writer;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Stack;
/*      */ import java.util.Vector;
/*      */ import javax.swing.text.AbstractWriter;
/*      */ import javax.swing.text.AttributeSet;
/*      */ import javax.swing.text.BadLocationException;
/*      */ import javax.swing.text.Document;
/*      */ import javax.swing.text.Element;
/*      */ import javax.swing.text.ElementIterator;
/*      */ import javax.swing.text.MutableAttributeSet;
/*      */ import javax.swing.text.Segment;
/*      */ import javax.swing.text.SimpleAttributeSet;
/*      */ import javax.swing.text.Style;
/*      */ import javax.swing.text.StyleConstants;
/*      */ 
/*      */ public class HTMLWriter extends AbstractWriter
/*      */ {
/*   49 */   private Stack<Element> blockElementStack = new Stack();
/*   50 */   private boolean inContent = false;
/*   51 */   private boolean inPre = false;
/*      */   private int preEndOffset;
/*   55 */   private boolean inTextArea = false;
/*   56 */   private boolean newlineOutputed = false;
/*      */   private boolean completeDoc;
/*   65 */   private Vector<HTML.Tag> tags = new Vector(10);
/*      */ 
/*   70 */   private Vector<Object> tagValues = new Vector(10);
/*      */   private Segment segment;
/*   80 */   private Vector<HTML.Tag> tagsToRemove = new Vector(10);
/*      */   private boolean wroteHead;
/*      */   private boolean replaceEntities;
/*      */   private char[] tempChars;
/*  832 */   private boolean indentNext = false;
/*      */ 
/* 1012 */   private boolean writeCSS = false;
/*      */ 
/* 1017 */   private MutableAttributeSet convAttr = new SimpleAttributeSet();
/*      */ 
/* 1023 */   private MutableAttributeSet oConvAttr = new SimpleAttributeSet();
/*      */ 
/* 1262 */   private boolean indented = false;
/*      */ 
/*      */   public HTMLWriter(Writer paramWriter, HTMLDocument paramHTMLDocument)
/*      */   {
/*  106 */     this(paramWriter, paramHTMLDocument, 0, paramHTMLDocument.getLength());
/*      */   }
/*      */ 
/*      */   public HTMLWriter(Writer paramWriter, HTMLDocument paramHTMLDocument, int paramInt1, int paramInt2)
/*      */   {
/*  118 */     super(paramWriter, paramHTMLDocument, paramInt1, paramInt2);
/*  119 */     this.completeDoc = ((paramInt1 == 0) && (paramInt2 == paramHTMLDocument.getLength()));
/*  120 */     setLineLength(80);
/*      */   }
/*      */ 
/*      */   public void write()
/*      */     throws IOException, BadLocationException
/*      */   {
/*  134 */     ElementIterator localElementIterator = getElementIterator();
/*  135 */     Object localObject1 = null;
/*      */ 
/*  138 */     this.wroteHead = false;
/*  139 */     setCurrentLineLength(0);
/*  140 */     this.replaceEntities = false;
/*  141 */     setCanWrapLines(false);
/*  142 */     if (this.segment == null) {
/*  143 */       this.segment = new Segment();
/*      */     }
/*  145 */     this.inPre = false;
/*  146 */     int i = 0;
/*      */     Element localElement;
/*      */     Object localObject2;
/*  147 */     while ((localElement = localElementIterator.next()) != null) {
/*  148 */       if (!inRange(localElement)) {
/*  149 */         if ((this.completeDoc) && (localElement.getAttributes().getAttribute(StyleConstants.NameAttribute) == HTML.Tag.BODY))
/*      */         {
/*  151 */           i = 1;
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*  157 */         if (localObject1 != null)
/*      */         {
/*  163 */           if (indentNeedsIncrementing((Element)localObject1, localElement)) {
/*  164 */             incrIndent();
/*  165 */           } else if (((Element)localObject1).getParentElement() != localElement.getParentElement())
/*      */           {
/*  172 */             localObject2 = (Element)this.blockElementStack.peek();
/*  173 */             while (localObject2 != localElement.getParentElement())
/*      */             {
/*  177 */               this.blockElementStack.pop();
/*  178 */               if (!synthesizedElement((Element)localObject2)) {
/*  179 */                 AttributeSet localAttributeSet = ((Element)localObject2).getAttributes();
/*  180 */                 if ((!matchNameAttribute(localAttributeSet, HTML.Tag.PRE)) && (!isFormElementWithContent(localAttributeSet)))
/*      */                 {
/*  182 */                   decrIndent();
/*      */                 }
/*  184 */                 endTag((Element)localObject2);
/*      */               }
/*  186 */               localObject2 = (Element)this.blockElementStack.peek();
/*      */             }
/*  188 */           } else if (((Element)localObject1).getParentElement() == localElement.getParentElement())
/*      */           {
/*  194 */             localObject2 = (Element)this.blockElementStack.peek();
/*  195 */             if (localObject2 == localObject1) {
/*  196 */               this.blockElementStack.pop();
/*  197 */               endTag((Element)localObject2);
/*      */             }
/*      */           }
/*      */         }
/*  201 */         if ((!localElement.isLeaf()) || (isFormElementWithContent(localElement.getAttributes()))) {
/*  202 */           this.blockElementStack.push(localElement);
/*  203 */           startTag(localElement);
/*      */         } else {
/*  205 */           emptyTag(localElement);
/*      */         }
/*  207 */         localObject1 = localElement;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  215 */     closeOutUnwantedEmbeddedTags(null);
/*      */ 
/*  217 */     if (i != 0) {
/*  218 */       this.blockElementStack.pop();
/*  219 */       endTag((Element)localObject1);
/*      */     }
/*  221 */     while (!this.blockElementStack.empty()) {
/*  222 */       localObject1 = (Element)this.blockElementStack.pop();
/*  223 */       if (!synthesizedElement((Element)localObject1)) {
/*  224 */         localObject2 = ((Element)localObject1).getAttributes();
/*  225 */         if ((!matchNameAttribute((AttributeSet)localObject2, HTML.Tag.PRE)) && (!isFormElementWithContent((AttributeSet)localObject2)))
/*      */         {
/*  227 */           decrIndent();
/*      */         }
/*  229 */         endTag((Element)localObject1);
/*      */       }
/*      */     }
/*      */ 
/*  233 */     if (this.completeDoc) {
/*  234 */       writeAdditionalComments();
/*      */     }
/*      */ 
/*  237 */     this.segment.array = null;
/*      */   }
/*      */ 
/*      */   protected void writeAttributes(AttributeSet paramAttributeSet)
/*      */     throws IOException
/*      */   {
/*  254 */     this.convAttr.removeAttributes(this.convAttr);
/*  255 */     convertToHTML32(paramAttributeSet, this.convAttr);
/*      */ 
/*  257 */     Enumeration localEnumeration = this.convAttr.getAttributeNames();
/*  258 */     while (localEnumeration.hasMoreElements()) {
/*  259 */       Object localObject = localEnumeration.nextElement();
/*  260 */       if ((!(localObject instanceof HTML.Tag)) && (!(localObject instanceof StyleConstants)) && (localObject != HTML.Attribute.ENDTAG))
/*      */       {
/*  265 */         write(" " + localObject + "=\"" + this.convAttr.getAttribute(localObject) + "\"");
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void emptyTag(Element paramElement)
/*      */     throws BadLocationException, IOException
/*      */   {
/*  280 */     if ((!this.inContent) && (!this.inPre)) {
/*  281 */       indentSmart();
/*      */     }
/*      */ 
/*  284 */     AttributeSet localAttributeSet = paramElement.getAttributes();
/*  285 */     closeOutUnwantedEmbeddedTags(localAttributeSet);
/*  286 */     writeEmbeddedTags(localAttributeSet);
/*      */ 
/*  288 */     if (matchNameAttribute(localAttributeSet, HTML.Tag.CONTENT)) {
/*  289 */       this.inContent = true;
/*  290 */       text(paramElement);
/*  291 */     } else if (matchNameAttribute(localAttributeSet, HTML.Tag.COMMENT)) {
/*  292 */       comment(paramElement);
/*      */     } else {
/*  294 */       boolean bool = isBlockTag(paramElement.getAttributes());
/*  295 */       if ((this.inContent) && (bool)) {
/*  296 */         writeLineSeparator();
/*  297 */         indentSmart();
/*      */       }
/*      */ 
/*  300 */       Object localObject1 = localAttributeSet != null ? localAttributeSet.getAttribute(StyleConstants.NameAttribute) : null;
/*      */ 
/*  302 */       Object localObject2 = localAttributeSet != null ? localAttributeSet.getAttribute(HTML.Attribute.ENDTAG) : null;
/*      */ 
/*  305 */       int i = 0;
/*      */ 
/*  309 */       if ((localObject1 != null) && (localObject2 != null) && ((localObject2 instanceof String)) && (localObject2.equals("true")))
/*      */       {
/*  312 */         i = 1;
/*      */       }
/*      */ 
/*  315 */       if ((this.completeDoc) && (matchNameAttribute(localAttributeSet, HTML.Tag.HEAD))) {
/*  316 */         if (i != 0)
/*      */         {
/*  318 */           writeStyles(((HTMLDocument)getDocument()).getStyleSheet());
/*      */         }
/*  320 */         this.wroteHead = true;
/*      */       }
/*      */ 
/*  323 */       write('<');
/*  324 */       if (i != 0) {
/*  325 */         write('/');
/*      */       }
/*  327 */       write(paramElement.getName());
/*  328 */       writeAttributes(localAttributeSet);
/*  329 */       write('>');
/*  330 */       if ((matchNameAttribute(localAttributeSet, HTML.Tag.TITLE)) && (i == 0)) {
/*  331 */         Document localDocument = paramElement.getDocument();
/*  332 */         String str = (String)localDocument.getProperty("title");
/*  333 */         write(str);
/*  334 */       } else if ((!this.inContent) || (bool)) {
/*  335 */         writeLineSeparator();
/*  336 */         if ((bool) && (this.inContent))
/*  337 */           indentSmart();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected boolean isBlockTag(AttributeSet paramAttributeSet)
/*      */   {
/*  351 */     Object localObject = paramAttributeSet.getAttribute(StyleConstants.NameAttribute);
/*  352 */     if ((localObject instanceof HTML.Tag)) {
/*  353 */       HTML.Tag localTag = (HTML.Tag)localObject;
/*  354 */       return localTag.isBlock();
/*      */     }
/*  356 */     return false;
/*      */   }
/*      */ 
/*      */   protected void startTag(Element paramElement)
/*      */     throws IOException, BadLocationException
/*      */   {
/*  369 */     if (synthesizedElement(paramElement)) {
/*  370 */       return;
/*      */     }
/*      */ 
/*  374 */     AttributeSet localAttributeSet = paramElement.getAttributes();
/*  375 */     Object localObject = localAttributeSet.getAttribute(StyleConstants.NameAttribute);
/*      */     HTML.Tag localTag;
/*  377 */     if ((localObject instanceof HTML.Tag)) {
/*  378 */       localTag = (HTML.Tag)localObject;
/*      */     }
/*      */     else {
/*  381 */       localTag = null;
/*      */     }
/*      */ 
/*  384 */     if (localTag == HTML.Tag.PRE) {
/*  385 */       this.inPre = true;
/*  386 */       this.preEndOffset = paramElement.getEndOffset();
/*      */     }
/*      */ 
/*  390 */     closeOutUnwantedEmbeddedTags(localAttributeSet);
/*      */ 
/*  392 */     if (this.inContent) {
/*  393 */       writeLineSeparator();
/*  394 */       this.inContent = false;
/*  395 */       this.newlineOutputed = false;
/*      */     }
/*      */ 
/*  398 */     if ((this.completeDoc) && (localTag == HTML.Tag.BODY) && (!this.wroteHead))
/*      */     {
/*  400 */       this.wroteHead = true;
/*  401 */       indentSmart();
/*  402 */       write("<head>");
/*  403 */       writeLineSeparator();
/*  404 */       incrIndent();
/*  405 */       writeStyles(((HTMLDocument)getDocument()).getStyleSheet());
/*  406 */       decrIndent();
/*  407 */       writeLineSeparator();
/*  408 */       indentSmart();
/*  409 */       write("</head>");
/*  410 */       writeLineSeparator();
/*      */     }
/*      */ 
/*  413 */     indentSmart();
/*  414 */     write('<');
/*  415 */     write(paramElement.getName());
/*  416 */     writeAttributes(localAttributeSet);
/*  417 */     write('>');
/*  418 */     if (localTag != HTML.Tag.PRE) {
/*  419 */       writeLineSeparator();
/*      */     }
/*      */ 
/*  422 */     if (localTag == HTML.Tag.TEXTAREA) {
/*  423 */       textAreaContent(paramElement.getAttributes());
/*  424 */     } else if (localTag == HTML.Tag.SELECT) {
/*  425 */       selectContent(paramElement.getAttributes());
/*  426 */     } else if ((this.completeDoc) && (localTag == HTML.Tag.BODY))
/*      */     {
/*  429 */       writeMaps(((HTMLDocument)getDocument()).getMaps());
/*      */     }
/*  431 */     else if (localTag == HTML.Tag.HEAD) {
/*  432 */       HTMLDocument localHTMLDocument = (HTMLDocument)getDocument();
/*  433 */       this.wroteHead = true;
/*  434 */       incrIndent();
/*  435 */       writeStyles(localHTMLDocument.getStyleSheet());
/*  436 */       if (localHTMLDocument.hasBaseTag()) {
/*  437 */         indentSmart();
/*  438 */         write("<base href=\"" + localHTMLDocument.getBase() + "\">");
/*  439 */         writeLineSeparator();
/*      */       }
/*  441 */       decrIndent();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void textAreaContent(AttributeSet paramAttributeSet)
/*      */     throws BadLocationException, IOException
/*      */   {
/*  457 */     Document localDocument = (Document)paramAttributeSet.getAttribute(StyleConstants.ModelAttribute);
/*  458 */     if ((localDocument != null) && (localDocument.getLength() > 0)) {
/*  459 */       if (this.segment == null) {
/*  460 */         this.segment = new Segment();
/*      */       }
/*  462 */       localDocument.getText(0, localDocument.getLength(), this.segment);
/*  463 */       if (this.segment.count > 0) {
/*  464 */         this.inTextArea = true;
/*  465 */         incrIndent();
/*  466 */         indentSmart();
/*  467 */         setCanWrapLines(true);
/*  468 */         this.replaceEntities = true;
/*  469 */         write(this.segment.array, this.segment.offset, this.segment.count);
/*  470 */         this.replaceEntities = false;
/*  471 */         setCanWrapLines(false);
/*  472 */         writeLineSeparator();
/*  473 */         this.inTextArea = false;
/*  474 */         decrIndent();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void text(Element paramElement)
/*      */     throws BadLocationException, IOException
/*      */   {
/*  491 */     int i = Math.max(getStartOffset(), paramElement.getStartOffset());
/*  492 */     int j = Math.min(getEndOffset(), paramElement.getEndOffset());
/*  493 */     if (i < j) {
/*  494 */       if (this.segment == null) {
/*  495 */         this.segment = new Segment();
/*      */       }
/*  497 */       getDocument().getText(i, j - i, this.segment);
/*  498 */       this.newlineOutputed = false;
/*  499 */       if (this.segment.count > 0) {
/*  500 */         if (this.segment.array[(this.segment.offset + this.segment.count - 1)] == '\n') {
/*  501 */           this.newlineOutputed = true;
/*      */         }
/*  503 */         if ((this.inPre) && (j == this.preEndOffset)) {
/*  504 */           if (this.segment.count > 1) {
/*  505 */             this.segment.count -= 1;
/*      */           }
/*      */           else {
/*  508 */             return;
/*      */           }
/*      */         }
/*  511 */         this.replaceEntities = true;
/*  512 */         setCanWrapLines(!this.inPre);
/*  513 */         write(this.segment.array, this.segment.offset, this.segment.count);
/*  514 */         setCanWrapLines(false);
/*  515 */         this.replaceEntities = false;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void selectContent(AttributeSet paramAttributeSet)
/*      */     throws IOException
/*      */   {
/*  527 */     Object localObject1 = paramAttributeSet.getAttribute(StyleConstants.ModelAttribute);
/*  528 */     incrIndent();
/*      */     Object localObject2;
/*      */     int i;
/*      */     int j;
/*      */     Option localOption;
/*  529 */     if ((localObject1 instanceof OptionListModel)) {
/*  530 */       localObject2 = (OptionListModel)localObject1;
/*  531 */       i = ((OptionListModel)localObject2).getSize();
/*  532 */       for (j = 0; j < i; j++) {
/*  533 */         localOption = (Option)((OptionListModel)localObject2).getElementAt(j);
/*  534 */         writeOption(localOption);
/*      */       }
/*  536 */     } else if ((localObject1 instanceof OptionComboBoxModel)) {
/*  537 */       localObject2 = (OptionComboBoxModel)localObject1;
/*  538 */       i = ((OptionComboBoxModel)localObject2).getSize();
/*  539 */       for (j = 0; j < i; j++) {
/*  540 */         localOption = (Option)((OptionComboBoxModel)localObject2).getElementAt(j);
/*  541 */         writeOption(localOption);
/*      */       }
/*      */     }
/*  544 */     decrIndent();
/*      */   }
/*      */ 
/*      */   protected void writeOption(Option paramOption)
/*      */     throws IOException
/*      */   {
/*  556 */     indentSmart();
/*  557 */     write('<');
/*  558 */     write("option");
/*      */ 
/*  560 */     Object localObject = paramOption.getAttributes().getAttribute(HTML.Attribute.VALUE);
/*      */ 
/*  562 */     if (localObject != null) {
/*  563 */       write(" value=" + localObject);
/*      */     }
/*  565 */     if (paramOption.isSelected()) {
/*  566 */       write(" selected");
/*      */     }
/*  568 */     write('>');
/*  569 */     if (paramOption.getLabel() != null) {
/*  570 */       write(paramOption.getLabel());
/*      */     }
/*  572 */     writeLineSeparator();
/*      */   }
/*      */ 
/*      */   protected void endTag(Element paramElement)
/*      */     throws IOException
/*      */   {
/*  582 */     if (synthesizedElement(paramElement)) {
/*  583 */       return;
/*      */     }
/*      */ 
/*  587 */     closeOutUnwantedEmbeddedTags(paramElement.getAttributes());
/*  588 */     if (this.inContent) {
/*  589 */       if ((!this.newlineOutputed) && (!this.inPre)) {
/*  590 */         writeLineSeparator();
/*      */       }
/*  592 */       this.newlineOutputed = false;
/*  593 */       this.inContent = false;
/*      */     }
/*  595 */     if (!this.inPre) {
/*  596 */       indentSmart();
/*      */     }
/*  598 */     if (matchNameAttribute(paramElement.getAttributes(), HTML.Tag.PRE)) {
/*  599 */       this.inPre = false;
/*      */     }
/*  601 */     write('<');
/*  602 */     write('/');
/*  603 */     write(paramElement.getName());
/*  604 */     write('>');
/*  605 */     writeLineSeparator();
/*      */   }
/*      */ 
/*      */   protected void comment(Element paramElement)
/*      */     throws BadLocationException, IOException
/*      */   {
/*  619 */     AttributeSet localAttributeSet = paramElement.getAttributes();
/*  620 */     if (matchNameAttribute(localAttributeSet, HTML.Tag.COMMENT)) {
/*  621 */       Object localObject = localAttributeSet.getAttribute(HTML.Attribute.COMMENT);
/*  622 */       if ((localObject instanceof String)) {
/*  623 */         writeComment((String)localObject);
/*      */       }
/*      */       else
/*  626 */         writeComment(null);
/*      */     }
/*      */   }
/*      */ 
/*      */   void writeComment(String paramString)
/*      */     throws IOException
/*      */   {
/*  641 */     write("<!--");
/*  642 */     if (paramString != null) {
/*  643 */       write(paramString);
/*      */     }
/*  645 */     write("-->");
/*  646 */     writeLineSeparator();
/*  647 */     indentSmart();
/*      */   }
/*      */ 
/*      */   void writeAdditionalComments()
/*      */     throws IOException
/*      */   {
/*  656 */     Object localObject = getDocument().getProperty("AdditionalComments");
/*      */ 
/*  659 */     if ((localObject instanceof Vector)) {
/*  660 */       Vector localVector = (Vector)localObject;
/*  661 */       int i = 0; for (int j = localVector.size(); i < j; 
/*  662 */         i++)
/*  663 */         writeComment(localVector.elementAt(i).toString());
/*      */     }
/*      */   }
/*      */ 
/*      */   protected boolean synthesizedElement(Element paramElement)
/*      */   {
/*  675 */     if (matchNameAttribute(paramElement.getAttributes(), HTML.Tag.IMPLIED)) {
/*  676 */       return true;
/*      */     }
/*  678 */     return false;
/*      */   }
/*      */ 
/*      */   protected boolean matchNameAttribute(AttributeSet paramAttributeSet, HTML.Tag paramTag)
/*      */   {
/*  687 */     Object localObject = paramAttributeSet.getAttribute(StyleConstants.NameAttribute);
/*  688 */     if ((localObject instanceof HTML.Tag)) {
/*  689 */       HTML.Tag localTag = (HTML.Tag)localObject;
/*  690 */       if (localTag == paramTag) {
/*  691 */         return true;
/*      */       }
/*      */     }
/*  694 */     return false;
/*      */   }
/*      */ 
/*      */   protected void writeEmbeddedTags(AttributeSet paramAttributeSet)
/*      */     throws IOException
/*      */   {
/*  708 */     paramAttributeSet = convertToHTML(paramAttributeSet, this.oConvAttr);
/*      */ 
/*  710 */     Enumeration localEnumeration = paramAttributeSet.getAttributeNames();
/*  711 */     while (localEnumeration.hasMoreElements()) {
/*  712 */       Object localObject1 = localEnumeration.nextElement();
/*  713 */       if ((localObject1 instanceof HTML.Tag)) {
/*  714 */         HTML.Tag localTag = (HTML.Tag)localObject1;
/*  715 */         if ((localTag != HTML.Tag.FORM) && (!this.tags.contains(localTag)))
/*      */         {
/*  718 */           write('<');
/*  719 */           write(localTag.toString());
/*  720 */           Object localObject2 = paramAttributeSet.getAttribute(localTag);
/*  721 */           if ((localObject2 != null) && ((localObject2 instanceof AttributeSet))) {
/*  722 */             writeAttributes((AttributeSet)localObject2);
/*      */           }
/*  724 */           write('>');
/*  725 */           this.tags.addElement(localTag);
/*  726 */           this.tagValues.addElement(localObject2);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean noMatchForTagInAttributes(AttributeSet paramAttributeSet, HTML.Tag paramTag, Object paramObject)
/*      */   {
/*  739 */     if ((paramAttributeSet != null) && (paramAttributeSet.isDefined(paramTag))) {
/*  740 */       Object localObject = paramAttributeSet.getAttribute(paramTag);
/*      */ 
/*  742 */       if (paramObject == null ? localObject == null : (localObject != null) && (paramObject.equals(localObject)))
/*      */       {
/*  744 */         return false;
/*      */       }
/*      */     }
/*  747 */     return true;
/*      */   }
/*      */ 
/*      */   protected void closeOutUnwantedEmbeddedTags(AttributeSet paramAttributeSet)
/*      */     throws IOException
/*      */   {
/*  761 */     this.tagsToRemove.removeAllElements();
/*      */ 
/*  764 */     paramAttributeSet = convertToHTML(paramAttributeSet, null);
/*      */ 
/*  768 */     int i = -1;
/*  769 */     int j = this.tags.size();
/*      */     HTML.Tag localTag;
/*  771 */     for (int k = j - 1; k >= 0; k--) {
/*  772 */       localTag = (HTML.Tag)this.tags.elementAt(k);
/*  773 */       Object localObject1 = this.tagValues.elementAt(k);
/*  774 */       if ((paramAttributeSet == null) || (noMatchForTagInAttributes(paramAttributeSet, localTag, localObject1))) {
/*  775 */         i = k;
/*  776 */         this.tagsToRemove.addElement(localTag);
/*      */       }
/*      */     }
/*  779 */     if (i != -1)
/*      */     {
/*  781 */       k = j - i == this.tagsToRemove.size() ? 1 : 0;
/*  782 */       for (int m = j - 1; m >= i; m--) {
/*  783 */         localTag = (HTML.Tag)this.tags.elementAt(m);
/*  784 */         if ((k != 0) || (this.tagsToRemove.contains(localTag))) {
/*  785 */           this.tags.removeElementAt(m);
/*  786 */           this.tagValues.removeElementAt(m);
/*      */         }
/*  788 */         write('<');
/*  789 */         write('/');
/*  790 */         write(localTag.toString());
/*  791 */         write('>');
/*      */       }
/*      */ 
/*  795 */       j = this.tags.size();
/*  796 */       for (m = i; m < j; m++) {
/*  797 */         localTag = (HTML.Tag)this.tags.elementAt(m);
/*  798 */         write('<');
/*  799 */         write(localTag.toString());
/*  800 */         Object localObject2 = this.tagValues.elementAt(m);
/*  801 */         if ((localObject2 != null) && ((localObject2 instanceof AttributeSet))) {
/*  802 */           writeAttributes((AttributeSet)localObject2);
/*      */         }
/*  804 */         write('>');
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean isFormElementWithContent(AttributeSet paramAttributeSet)
/*      */   {
/*  816 */     return (matchNameAttribute(paramAttributeSet, HTML.Tag.TEXTAREA)) || (matchNameAttribute(paramAttributeSet, HTML.Tag.SELECT));
/*      */   }
/*      */ 
/*      */   private boolean indentNeedsIncrementing(Element paramElement1, Element paramElement2)
/*      */   {
/*  834 */     if ((paramElement2.getParentElement() == paramElement1) && (!this.inPre)) {
/*  835 */       if (this.indentNext) {
/*  836 */         this.indentNext = false;
/*  837 */         return true;
/*  838 */       }if (synthesizedElement(paramElement2))
/*  839 */         this.indentNext = true;
/*  840 */       else if (!synthesizedElement(paramElement1)) {
/*  841 */         return true;
/*      */       }
/*      */     }
/*  844 */     return false;
/*      */   }
/*      */ 
/*      */   void writeMaps(Enumeration paramEnumeration)
/*      */     throws IOException
/*      */   {
/*  852 */     if (paramEnumeration != null)
/*  853 */       while (paramEnumeration.hasMoreElements()) {
/*  854 */         Map localMap = (Map)paramEnumeration.nextElement();
/*  855 */         String str = localMap.getName();
/*      */ 
/*  857 */         incrIndent();
/*  858 */         indentSmart();
/*  859 */         write("<map");
/*  860 */         if (str != null) {
/*  861 */           write(" name=\"");
/*  862 */           write(str);
/*  863 */           write("\">");
/*      */         }
/*      */         else {
/*  866 */           write('>');
/*      */         }
/*  868 */         writeLineSeparator();
/*  869 */         incrIndent();
/*      */ 
/*  872 */         AttributeSet[] arrayOfAttributeSet = localMap.getAreas();
/*  873 */         if (arrayOfAttributeSet != null) {
/*  874 */           int i = 0; int j = arrayOfAttributeSet.length;
/*  875 */           for (; i < j; i++) {
/*  876 */             indentSmart();
/*  877 */             write("<area");
/*  878 */             writeAttributes(arrayOfAttributeSet[i]);
/*  879 */             write("></area>");
/*  880 */             writeLineSeparator();
/*      */           }
/*      */         }
/*  883 */         decrIndent();
/*  884 */         indentSmart();
/*  885 */         write("</map>");
/*  886 */         writeLineSeparator();
/*  887 */         decrIndent();
/*      */       }
/*      */   }
/*      */ 
/*      */   void writeStyles(StyleSheet paramStyleSheet)
/*      */     throws IOException
/*      */   {
/*  898 */     if (paramStyleSheet != null) {
/*  899 */       Enumeration localEnumeration = paramStyleSheet.getStyleNames();
/*  900 */       if (localEnumeration != null) {
/*  901 */         boolean bool = false;
/*  902 */         while (localEnumeration.hasMoreElements()) {
/*  903 */           String str = (String)localEnumeration.nextElement();
/*      */ 
/*  905 */           if ((!"default".equals(str)) && (writeStyle(str, paramStyleSheet.getStyle(str), bool)))
/*      */           {
/*  907 */             bool = true;
/*      */           }
/*      */         }
/*  910 */         if (bool)
/*  911 */           writeStyleEndTag();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   boolean writeStyle(String paramString, Style paramStyle, boolean paramBoolean)
/*      */     throws IOException
/*      */   {
/*  924 */     boolean bool = false;
/*  925 */     Enumeration localEnumeration = paramStyle.getAttributeNames();
/*  926 */     if (localEnumeration != null) {
/*  927 */       while (localEnumeration.hasMoreElements()) {
/*  928 */         Object localObject = localEnumeration.nextElement();
/*  929 */         if ((localObject instanceof CSS.Attribute)) {
/*  930 */           String str = paramStyle.getAttribute(localObject).toString();
/*  931 */           if (str != null) {
/*  932 */             if (!paramBoolean) {
/*  933 */               writeStyleStartTag();
/*  934 */               paramBoolean = true;
/*      */             }
/*  936 */             if (!bool) {
/*  937 */               bool = true;
/*  938 */               indentSmart();
/*  939 */               write(paramString);
/*  940 */               write(" {");
/*      */             }
/*      */             else {
/*  943 */               write(";");
/*      */             }
/*  945 */             write(' ');
/*  946 */             write(localObject.toString());
/*  947 */             write(": ");
/*  948 */             write(str);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  953 */     if (bool) {
/*  954 */       write(" }");
/*  955 */       writeLineSeparator();
/*      */     }
/*  957 */     return bool;
/*      */   }
/*      */ 
/*      */   void writeStyleStartTag() throws IOException {
/*  961 */     indentSmart();
/*  962 */     write("<style type=\"text/css\">");
/*  963 */     incrIndent();
/*  964 */     writeLineSeparator();
/*  965 */     indentSmart();
/*  966 */     write("<!--");
/*  967 */     incrIndent();
/*  968 */     writeLineSeparator();
/*      */   }
/*      */ 
/*      */   void writeStyleEndTag() throws IOException {
/*  972 */     decrIndent();
/*  973 */     indentSmart();
/*  974 */     write("-->");
/*  975 */     writeLineSeparator();
/*  976 */     decrIndent();
/*  977 */     indentSmart();
/*  978 */     write("</style>");
/*  979 */     writeLineSeparator();
/*  980 */     indentSmart();
/*      */   }
/*      */ 
/*      */   AttributeSet convertToHTML(AttributeSet paramAttributeSet, MutableAttributeSet paramMutableAttributeSet)
/*      */   {
/*  995 */     if (paramMutableAttributeSet == null) {
/*  996 */       paramMutableAttributeSet = this.convAttr;
/*      */     }
/*  998 */     paramMutableAttributeSet.removeAttributes(paramMutableAttributeSet);
/*  999 */     if (this.writeCSS)
/* 1000 */       convertToHTML40(paramAttributeSet, paramMutableAttributeSet);
/*      */     else {
/* 1002 */       convertToHTML32(paramAttributeSet, paramMutableAttributeSet);
/*      */     }
/* 1004 */     return paramMutableAttributeSet;
/*      */   }
/*      */ 
/*      */   private static void convertToHTML32(AttributeSet paramAttributeSet, MutableAttributeSet paramMutableAttributeSet)
/*      */   {
/* 1032 */     if (paramAttributeSet == null) {
/* 1033 */       return;
/*      */     }
/* 1035 */     Enumeration localEnumeration = paramAttributeSet.getAttributeNames();
/* 1036 */     String str = "";
/* 1037 */     while (localEnumeration.hasMoreElements()) {
/* 1038 */       Object localObject1 = localEnumeration.nextElement();
/*      */       Object localObject2;
/* 1039 */       if ((localObject1 instanceof CSS.Attribute)) {
/* 1040 */         if ((localObject1 == CSS.Attribute.FONT_FAMILY) || (localObject1 == CSS.Attribute.FONT_SIZE) || (localObject1 == CSS.Attribute.COLOR))
/*      */         {
/* 1044 */           createFontAttribute((CSS.Attribute)localObject1, paramAttributeSet, paramMutableAttributeSet);
/* 1045 */         } else if (localObject1 == CSS.Attribute.FONT_WEIGHT)
/*      */         {
/* 1047 */           localObject2 = (CSS.FontWeight)paramAttributeSet.getAttribute(CSS.Attribute.FONT_WEIGHT);
/*      */ 
/* 1049 */           if ((localObject2 != null) && (((CSS.FontWeight)localObject2).getValue() > 400))
/* 1050 */             addAttribute(paramMutableAttributeSet, HTML.Tag.B, SimpleAttributeSet.EMPTY);
/*      */         }
/* 1052 */         else if (localObject1 == CSS.Attribute.FONT_STYLE) {
/* 1053 */           localObject2 = paramAttributeSet.getAttribute(localObject1).toString();
/* 1054 */           if (((String)localObject2).indexOf("italic") >= 0)
/* 1055 */             addAttribute(paramMutableAttributeSet, HTML.Tag.I, SimpleAttributeSet.EMPTY);
/*      */         }
/* 1057 */         else if (localObject1 == CSS.Attribute.TEXT_DECORATION) {
/* 1058 */           localObject2 = paramAttributeSet.getAttribute(localObject1).toString();
/* 1059 */           if (((String)localObject2).indexOf("underline") >= 0) {
/* 1060 */             addAttribute(paramMutableAttributeSet, HTML.Tag.U, SimpleAttributeSet.EMPTY);
/*      */           }
/* 1062 */           if (((String)localObject2).indexOf("line-through") >= 0)
/* 1063 */             addAttribute(paramMutableAttributeSet, HTML.Tag.STRIKE, SimpleAttributeSet.EMPTY);
/*      */         }
/* 1065 */         else if (localObject1 == CSS.Attribute.VERTICAL_ALIGN) {
/* 1066 */           localObject2 = paramAttributeSet.getAttribute(localObject1).toString();
/* 1067 */           if (((String)localObject2).indexOf("sup") >= 0) {
/* 1068 */             addAttribute(paramMutableAttributeSet, HTML.Tag.SUP, SimpleAttributeSet.EMPTY);
/*      */           }
/* 1070 */           if (((String)localObject2).indexOf("sub") >= 0)
/* 1071 */             addAttribute(paramMutableAttributeSet, HTML.Tag.SUB, SimpleAttributeSet.EMPTY);
/*      */         }
/* 1073 */         else if (localObject1 == CSS.Attribute.TEXT_ALIGN) {
/* 1074 */           addAttribute(paramMutableAttributeSet, HTML.Attribute.ALIGN, paramAttributeSet.getAttribute(localObject1).toString());
/*      */         }
/*      */         else
/*      */         {
/* 1078 */           if (str.length() > 0) {
/* 1079 */             str = str + "; ";
/*      */           }
/* 1081 */           str = str + localObject1 + ": " + paramAttributeSet.getAttribute(localObject1);
/*      */         }
/*      */       } else {
/* 1084 */         localObject2 = paramAttributeSet.getAttribute(localObject1);
/* 1085 */         if ((localObject2 instanceof AttributeSet)) {
/* 1086 */           localObject2 = ((AttributeSet)localObject2).copyAttributes();
/*      */         }
/* 1088 */         addAttribute(paramMutableAttributeSet, localObject1, localObject2);
/*      */       }
/*      */     }
/* 1091 */     if (str.length() > 0)
/* 1092 */       paramMutableAttributeSet.addAttribute(HTML.Attribute.STYLE, str);
/*      */   }
/*      */ 
/*      */   private static void addAttribute(MutableAttributeSet paramMutableAttributeSet, Object paramObject1, Object paramObject2)
/*      */   {
/* 1101 */     Object localObject = paramMutableAttributeSet.getAttribute(paramObject1);
/* 1102 */     if ((localObject == null) || (localObject == SimpleAttributeSet.EMPTY)) {
/* 1103 */       paramMutableAttributeSet.addAttribute(paramObject1, paramObject2);
/*      */     }
/* 1105 */     else if (((localObject instanceof MutableAttributeSet)) && ((paramObject2 instanceof AttributeSet)))
/*      */     {
/* 1107 */       ((MutableAttributeSet)localObject).addAttributes((AttributeSet)paramObject2);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void createFontAttribute(CSS.Attribute paramAttribute, AttributeSet paramAttributeSet, MutableAttributeSet paramMutableAttributeSet)
/*      */   {
/* 1119 */     Object localObject = (MutableAttributeSet)paramMutableAttributeSet.getAttribute(HTML.Tag.FONT);
/*      */ 
/* 1121 */     if (localObject == null) {
/* 1122 */       localObject = new SimpleAttributeSet();
/* 1123 */       paramMutableAttributeSet.addAttribute(HTML.Tag.FONT, localObject);
/*      */     }
/*      */ 
/* 1126 */     String str = paramAttributeSet.getAttribute(paramAttribute).toString();
/* 1127 */     if (paramAttribute == CSS.Attribute.FONT_FAMILY)
/* 1128 */       ((MutableAttributeSet)localObject).addAttribute(HTML.Attribute.FACE, str);
/* 1129 */     else if (paramAttribute == CSS.Attribute.FONT_SIZE)
/* 1130 */       ((MutableAttributeSet)localObject).addAttribute(HTML.Attribute.SIZE, str);
/* 1131 */     else if (paramAttribute == CSS.Attribute.COLOR)
/* 1132 */       ((MutableAttributeSet)localObject).addAttribute(HTML.Attribute.COLOR, str);
/*      */   }
/*      */ 
/*      */   private static void convertToHTML40(AttributeSet paramAttributeSet, MutableAttributeSet paramMutableAttributeSet)
/*      */   {
/* 1142 */     Enumeration localEnumeration = paramAttributeSet.getAttributeNames();
/* 1143 */     String str = "";
/* 1144 */     while (localEnumeration.hasMoreElements()) {
/* 1145 */       Object localObject = localEnumeration.nextElement();
/* 1146 */       if ((localObject instanceof CSS.Attribute))
/* 1147 */         str = str + " " + localObject + "=" + paramAttributeSet.getAttribute(localObject) + ";";
/*      */       else {
/* 1149 */         paramMutableAttributeSet.addAttribute(localObject, paramAttributeSet.getAttribute(localObject));
/*      */       }
/*      */     }
/* 1152 */     if (str.length() > 0)
/* 1153 */       paramMutableAttributeSet.addAttribute(HTML.Attribute.STYLE, str);
/*      */   }
/*      */ 
/*      */   protected void writeLineSeparator()
/*      */     throws IOException
/*      */   {
/* 1170 */     boolean bool = this.replaceEntities;
/* 1171 */     this.replaceEntities = false;
/* 1172 */     super.writeLineSeparator();
/* 1173 */     this.replaceEntities = bool;
/* 1174 */     this.indented = false;
/*      */   }
/*      */ 
/*      */   protected void output(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*      */     throws IOException
/*      */   {
/* 1185 */     if (!this.replaceEntities) {
/* 1186 */       super.output(paramArrayOfChar, paramInt1, paramInt2);
/* 1187 */       return;
/*      */     }
/* 1189 */     int i = paramInt1;
/* 1190 */     paramInt2 += paramInt1;
/* 1191 */     for (int j = paramInt1; j < paramInt2; j++)
/*      */     {
/* 1194 */       switch (paramArrayOfChar[j])
/*      */       {
/*      */       case '<':
/* 1197 */         if (j > i) {
/* 1198 */           super.output(paramArrayOfChar, i, j - i);
/*      */         }
/* 1200 */         i = j + 1;
/* 1201 */         output("&lt;");
/* 1202 */         break;
/*      */       case '>':
/* 1204 */         if (j > i) {
/* 1205 */           super.output(paramArrayOfChar, i, j - i);
/*      */         }
/* 1207 */         i = j + 1;
/* 1208 */         output("&gt;");
/* 1209 */         break;
/*      */       case '&':
/* 1211 */         if (j > i) {
/* 1212 */           super.output(paramArrayOfChar, i, j - i);
/*      */         }
/* 1214 */         i = j + 1;
/* 1215 */         output("&amp;");
/* 1216 */         break;
/*      */       case '"':
/* 1218 */         if (j > i) {
/* 1219 */           super.output(paramArrayOfChar, i, j - i);
/*      */         }
/* 1221 */         i = j + 1;
/* 1222 */         output("&quot;");
/* 1223 */         break;
/*      */       case '\t':
/*      */       case '\n':
/*      */       case '\r':
/* 1228 */         break;
/*      */       default:
/* 1230 */         if ((paramArrayOfChar[j] < ' ') || (paramArrayOfChar[j] > '')) {
/* 1231 */           if (j > i) {
/* 1232 */             super.output(paramArrayOfChar, i, j - i);
/*      */           }
/* 1234 */           i = j + 1;
/*      */ 
/* 1237 */           output("&#");
/* 1238 */           output(String.valueOf(paramArrayOfChar[j]));
/* 1239 */           output(";");
/*      */         }
/*      */         break;
/*      */       }
/*      */     }
/* 1244 */     if (i < paramInt2)
/* 1245 */       super.output(paramArrayOfChar, i, paramInt2 - i);
/*      */   }
/*      */ 
/*      */   private void output(String paramString)
/*      */     throws IOException
/*      */   {
/* 1254 */     int i = paramString.length();
/* 1255 */     if ((this.tempChars == null) || (this.tempChars.length < i)) {
/* 1256 */       this.tempChars = new char[i];
/*      */     }
/* 1258 */     paramString.getChars(0, i, this.tempChars, 0);
/* 1259 */     super.output(this.tempChars, 0, i);
/*      */   }
/*      */ 
/*      */   private void indentSmart()
/*      */     throws IOException
/*      */   {
/* 1268 */     if (!this.indented) {
/* 1269 */       indent();
/* 1270 */       this.indented = true;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.html.HTMLWriter
 * JD-Core Version:    0.6.2
 */