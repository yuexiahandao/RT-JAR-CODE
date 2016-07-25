/*      */ package javax.swing.text.html.parser;
/*      */ 
/*      */ import java.io.CharArrayReader;
/*      */ import java.io.IOException;
/*      */ import java.io.InterruptedIOException;
/*      */ import java.io.Reader;
/*      */ import java.util.Vector;
/*      */ import javax.swing.text.ChangedCharSetException;
/*      */ import javax.swing.text.SimpleAttributeSet;
/*      */ import javax.swing.text.html.HTML;
/*      */ import javax.swing.text.html.HTML.Attribute;
/*      */ 
/*      */ public class Parser
/*      */   implements DTDConstants
/*      */ {
/*   83 */   private char[] text = new char[1024];
/*   84 */   private int textpos = 0;
/*      */   private TagElement last;
/*      */   private boolean space;
/*   88 */   private char[] str = new char[''];
/*   89 */   private int strpos = 0;
/*      */ 
/*   91 */   protected DTD dtd = null;
/*      */   private int ch;
/*      */   private int ln;
/*      */   private Reader in;
/*      */   private Element recent;
/*      */   private TagStack stack;
/*   99 */   private boolean skipTag = false;
/*  100 */   private TagElement lastFormSent = null;
/*  101 */   private SimpleAttributeSet attributes = new SimpleAttributeSet();
/*      */ 
/*  108 */   private boolean seenHtml = false;
/*  109 */   private boolean seenHead = false;
/*  110 */   private boolean seenBody = false;
/*      */   private boolean ignoreSpace;
/*  141 */   protected boolean strict = false;
/*      */   private int crlfCount;
/*      */   private int crCount;
/*      */   private int lfCount;
/*      */   private int currentBlockStartPos;
/*      */   private int lastBlockStartPos;
/*  168 */   private static final char[] cp1252Map = { '‚', 'ƒ', '„', '…', '†', '‡', 'ˆ', '‰', 'Š', '‹', 'Œ', '', '', '', '', '‘', '’', '“', '”', '•', '–', '—', '˜', '™', 'š', '›', 'œ', '', '', 'Ÿ' };
/*      */   private static final String START_COMMENT = "<!--";
/*      */   private static final String END_COMMENT = "-->";
/* 1977 */   private static final char[] SCRIPT_END_TAG = "</script>".toCharArray();
/* 1978 */   private static final char[] SCRIPT_END_TAG_UPPER_CASE = "</SCRIPT>".toCharArray();
/*      */ 
/* 2291 */   private char[] buf = new char[1];
/*      */   private int pos;
/*      */   private int len;
/*      */   private int currentPosition;
/*      */ 
/*      */   public Parser(DTD paramDTD)
/*      */   {
/*  202 */     this.dtd = paramDTD;
/*      */   }
/*      */ 
/*      */   protected int getCurrentLine()
/*      */   {
/*  210 */     return this.ln;
/*      */   }
/*      */ 
/*      */   int getBlockStartPosition()
/*      */   {
/*  221 */     return Math.max(0, this.lastBlockStartPos - 1);
/*      */   }
/*      */ 
/*      */   protected TagElement makeTag(Element paramElement, boolean paramBoolean)
/*      */   {
/*  228 */     return new TagElement(paramElement, paramBoolean);
/*      */   }
/*      */ 
/*      */   protected TagElement makeTag(Element paramElement) {
/*  232 */     return makeTag(paramElement, false);
/*      */   }
/*      */ 
/*      */   protected SimpleAttributeSet getAttributes() {
/*  236 */     return this.attributes;
/*      */   }
/*      */ 
/*      */   protected void flushAttributes() {
/*  240 */     this.attributes.removeAttributes(this.attributes);
/*      */   }
/*      */ 
/*      */   protected void handleText(char[] paramArrayOfChar)
/*      */   {
/*      */   }
/*      */ 
/*      */   protected void handleTitle(char[] paramArrayOfChar)
/*      */   {
/*  255 */     handleText(paramArrayOfChar);
/*      */   }
/*      */ 
/*      */   protected void handleComment(char[] paramArrayOfChar)
/*      */   {
/*      */   }
/*      */ 
/*      */   protected void handleEOFInComment()
/*      */   {
/*  271 */     int i = strIndexOf('\n');
/*  272 */     if (i >= 0) {
/*  273 */       handleComment(getChars(0, i));
/*      */       try {
/*  275 */         this.in.close();
/*  276 */         this.in = new CharArrayReader(getChars(i + 1));
/*  277 */         this.ch = 62;
/*      */       } catch (IOException localIOException) {
/*  279 */         error("ioexception");
/*      */       }
/*      */ 
/*  282 */       resetStrBuffer();
/*      */     }
/*      */     else {
/*  285 */       error("eof.comment");
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void handleEmptyTag(TagElement paramTagElement)
/*      */     throws ChangedCharSetException
/*      */   {
/*      */   }
/*      */ 
/*      */   protected void handleStartTag(TagElement paramTagElement)
/*      */   {
/*      */   }
/*      */ 
/*      */   protected void handleEndTag(TagElement paramTagElement)
/*      */   {
/*      */   }
/*      */ 
/*      */   protected void handleError(int paramInt, String paramString)
/*      */   {
/*      */   }
/*      */ 
/*      */   void handleText(TagElement paramTagElement)
/*      */   {
/*  323 */     if (paramTagElement.breaksFlow()) {
/*  324 */       this.space = false;
/*  325 */       if (!this.strict) {
/*  326 */         this.ignoreSpace = true;
/*      */       }
/*      */     }
/*  329 */     if ((this.textpos == 0) && (
/*  330 */       (!this.space) || (this.stack == null) || (this.last.breaksFlow()) || (!this.stack.advance(this.dtd.pcdata))))
/*      */     {
/*  332 */       this.last = paramTagElement;
/*  333 */       this.space = false;
/*  334 */       this.lastBlockStartPos = this.currentBlockStartPos;
/*  335 */       return;
/*      */     }
/*      */ 
/*  338 */     if (this.space) {
/*  339 */       if (!this.ignoreSpace)
/*      */       {
/*  341 */         if (this.textpos + 1 > this.text.length) {
/*  342 */           arrayOfChar = new char[this.text.length + 200];
/*  343 */           System.arraycopy(this.text, 0, arrayOfChar, 0, this.text.length);
/*  344 */           this.text = arrayOfChar;
/*      */         }
/*      */ 
/*  348 */         this.text[(this.textpos++)] = ' ';
/*  349 */         if ((!this.strict) && (!paramTagElement.getElement().isEmpty())) {
/*  350 */           this.ignoreSpace = true;
/*      */         }
/*      */       }
/*  353 */       this.space = false;
/*      */     }
/*  355 */     char[] arrayOfChar = new char[this.textpos];
/*  356 */     System.arraycopy(this.text, 0, arrayOfChar, 0, this.textpos);
/*      */ 
/*  359 */     if (paramTagElement.getElement().getName().equals("title"))
/*  360 */       handleTitle(arrayOfChar);
/*      */     else {
/*  362 */       handleText(arrayOfChar);
/*      */     }
/*  364 */     this.lastBlockStartPos = this.currentBlockStartPos;
/*  365 */     this.textpos = 0;
/*  366 */     this.last = paramTagElement;
/*  367 */     this.space = false;
/*      */   }
/*      */ 
/*      */   protected void error(String paramString1, String paramString2, String paramString3, String paramString4)
/*      */   {
/*  375 */     handleError(this.ln, paramString1 + " " + paramString2 + " " + paramString3 + " " + paramString4);
/*      */   }
/*      */ 
/*      */   protected void error(String paramString1, String paramString2, String paramString3) {
/*  379 */     error(paramString1, paramString2, paramString3, "?");
/*      */   }
/*      */   protected void error(String paramString1, String paramString2) {
/*  382 */     error(paramString1, paramString2, "?", "?");
/*      */   }
/*      */   protected void error(String paramString) {
/*  385 */     error(paramString, "?", "?", "?");
/*      */   }
/*      */ 
/*      */   protected void startTag(TagElement paramTagElement)
/*      */     throws ChangedCharSetException
/*      */   {
/*  395 */     Element localElement = paramTagElement.getElement();
/*      */ 
/*  402 */     if ((!localElement.isEmpty()) || ((this.last != null) && (!this.last.breaksFlow())) || (this.textpos != 0))
/*      */     {
/*  405 */       handleText(paramTagElement);
/*      */     }
/*      */     else
/*      */     {
/*  411 */       this.last = paramTagElement;
/*      */ 
/*  414 */       this.space = false;
/*      */     }
/*  416 */     this.lastBlockStartPos = this.currentBlockStartPos;
/*      */ 
/*  419 */     for (AttributeList localAttributeList = localElement.atts; localAttributeList != null; localAttributeList = localAttributeList.next) {
/*  420 */       if ((localAttributeList.modifier == 2) && ((this.attributes.isEmpty()) || ((!this.attributes.isDefined(localAttributeList.name)) && (!this.attributes.isDefined(HTML.getAttributeKey(localAttributeList.name))))))
/*      */       {
/*  424 */         error("req.att ", localAttributeList.getName(), localElement.getName());
/*      */       }
/*      */     }
/*      */ 
/*  428 */     if (localElement.isEmpty()) {
/*  429 */       handleEmptyTag(paramTagElement);
/*      */     }
/*      */     else
/*      */     {
/*  435 */       this.recent = localElement;
/*  436 */       this.stack = new TagStack(paramTagElement, this.stack);
/*  437 */       handleStartTag(paramTagElement);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void endTag(boolean paramBoolean)
/*      */   {
/*  446 */     handleText(this.stack.tag);
/*      */ 
/*  448 */     if ((paramBoolean) && (!this.stack.elem.omitEnd()))
/*  449 */       error("end.missing", this.stack.elem.getName());
/*  450 */     else if (!this.stack.terminate()) {
/*  451 */       error("end.unexpected", this.stack.elem.getName());
/*      */     }
/*      */ 
/*  455 */     handleEndTag(this.stack.tag);
/*  456 */     this.stack = this.stack.next;
/*  457 */     this.recent = (this.stack != null ? this.stack.elem : null);
/*      */   }
/*      */ 
/*      */   boolean ignoreElement(Element paramElement)
/*      */   {
/*  463 */     String str1 = this.stack.elem.getName();
/*  464 */     String str2 = paramElement.getName();
/*      */ 
/*  471 */     if (((str2.equals("html")) && (this.seenHtml)) || ((str2.equals("head")) && (this.seenHead)) || ((str2.equals("body")) && (this.seenBody)))
/*      */     {
/*  474 */       return true;
/*      */     }
/*  476 */     if ((str2.equals("dt")) || (str2.equals("dd"))) {
/*  477 */       TagStack localTagStack = this.stack;
/*  478 */       while ((localTagStack != null) && (!localTagStack.elem.getName().equals("dl"))) {
/*  479 */         localTagStack = localTagStack.next;
/*      */       }
/*  481 */       if (localTagStack == null) {
/*  482 */         return true;
/*      */       }
/*      */     }
/*      */ 
/*  486 */     if (((str1.equals("table")) && (!str2.equals("#pcdata")) && (!str2.equals("input"))) || ((str2.equals("font")) && ((str1.equals("ul")) || (str1.equals("ol")))) || ((str2.equals("meta")) && (this.stack != null)) || ((str2.equals("style")) && (this.seenBody)) || ((str1.equals("table")) && (str2.equals("a"))))
/*      */     {
/*  493 */       return true;
/*      */     }
/*  495 */     return false;
/*      */   }
/*      */ 
/*      */   protected void markFirstTime(Element paramElement)
/*      */   {
/*  504 */     String str1 = paramElement.getName();
/*  505 */     if (str1.equals("html")) {
/*  506 */       this.seenHtml = true;
/*  507 */     } else if (str1.equals("head")) {
/*  508 */       this.seenHead = true;
/*  509 */     } else if (str1.equals("body")) {
/*  510 */       if (this.buf.length == 1)
/*      */       {
/*  512 */         char[] arrayOfChar = new char[256];
/*      */ 
/*  514 */         arrayOfChar[0] = this.buf[0];
/*  515 */         this.buf = arrayOfChar;
/*      */       }
/*  517 */       this.seenBody = true;
/*      */     }
/*      */   }
/*      */ 
/*      */   boolean legalElementContext(Element paramElement)
/*      */     throws ChangedCharSetException
/*      */   {
/*  529 */     if (this.stack == null)
/*      */     {
/*  531 */       if (paramElement != this.dtd.html)
/*      */       {
/*  533 */         startTag(makeTag(this.dtd.html, true));
/*  534 */         return legalElementContext(paramElement);
/*      */       }
/*  536 */       return true;
/*      */     }
/*      */ 
/*  540 */     if (this.stack.advance(paramElement))
/*      */     {
/*  542 */       markFirstTime(paramElement);
/*  543 */       return true;
/*      */     }
/*  545 */     int i = 0;
/*      */ 
/*  572 */     String str1 = this.stack.elem.getName();
/*  573 */     String str2 = paramElement.getName();
/*      */ 
/*  576 */     if ((!this.strict) && (((str1.equals("table")) && (str2.equals("td"))) || ((str1.equals("table")) && (str2.equals("th"))) || ((str1.equals("tr")) && (!str2.equals("tr")))))
/*      */     {
/*  580 */       i = 1;
/*      */     }
/*      */ 
/*  584 */     if ((!this.strict) && (i == 0) && ((this.stack.elem.getName() != paramElement.getName()) || (paramElement.getName().equals("body"))))
/*      */     {
/*  586 */       if ((this.skipTag = ignoreElement(paramElement))) {
/*  587 */         error("tag.ignore", paramElement.getName());
/*  588 */         return this.skipTag;
/*      */       }
/*      */     }
/*      */     Object localObject2;
/*  595 */     if ((!this.strict) && (str1.equals("table")) && (!str2.equals("tr")) && (!str2.equals("td")) && (!str2.equals("th")) && (!str2.equals("caption")))
/*      */     {
/*  598 */       localObject1 = this.dtd.getElement("tr");
/*  599 */       localObject2 = makeTag((Element)localObject1, true);
/*  600 */       legalTagContext((TagElement)localObject2);
/*  601 */       startTag((TagElement)localObject2);
/*  602 */       error("start.missing", paramElement.getName());
/*  603 */       return legalElementContext(paramElement);
/*      */     }
/*      */ 
/*  614 */     if ((i == 0) && (this.stack.terminate()) && ((!this.strict) || (this.stack.elem.omitEnd()))) {
/*  615 */       for (localObject1 = this.stack.next; localObject1 != null; localObject1 = ((TagStack)localObject1).next) {
/*  616 */         if (((TagStack)localObject1).advance(paramElement)) {
/*  617 */           while (this.stack != localObject1) {
/*  618 */             endTag(true);
/*      */           }
/*  620 */           return true;
/*      */         }
/*  622 */         if ((!((TagStack)localObject1).terminate()) || ((this.strict) && (!((TagStack)localObject1).elem.omitEnd())))
/*      */         {
/*      */           break;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  632 */     Object localObject1 = this.stack.first();
/*  633 */     if ((localObject1 != null) && ((!this.strict) || (((Element)localObject1).omitStart())) && ((localObject1 != this.dtd.head) || (paramElement != this.dtd.pcdata)))
/*      */     {
/*  636 */       localObject2 = makeTag((Element)localObject1, true);
/*  637 */       legalTagContext((TagElement)localObject2);
/*  638 */       startTag((TagElement)localObject2);
/*  639 */       if (!((Element)localObject1).omitStart()) {
/*  640 */         error("start.missing", paramElement.getName());
/*      */       }
/*  642 */       return legalElementContext(paramElement);
/*      */     }
/*      */ 
/*  650 */     if (!this.strict) {
/*  651 */       localObject2 = this.stack.contentModel();
/*  652 */       Vector localVector = new Vector();
/*  653 */       if (localObject2 != null) {
/*  654 */         ((ContentModel)localObject2).getElements(localVector);
/*  655 */         for (Element localElement : localVector)
/*      */         {
/*  659 */           if (!this.stack.excluded(localElement.getIndex()))
/*      */           {
/*  663 */             int j = 0;
/*      */ 
/*  665 */             for (Object localObject3 = localElement.getAttributes(); localObject3 != null; localObject3 = ((AttributeList)localObject3).next) {
/*  666 */               if (((AttributeList)localObject3).modifier == 2) {
/*  667 */                 j = 1;
/*  668 */                 break;
/*      */               }
/*      */ 
/*      */             }
/*      */ 
/*  674 */             if (j == 0)
/*      */             {
/*  678 */               localObject3 = localElement.getContent();
/*  679 */               if ((localObject3 != null) && (((ContentModel)localObject3).first(paramElement)))
/*      */               {
/*  681 */                 TagElement localTagElement = makeTag(localElement, true);
/*  682 */                 legalTagContext(localTagElement);
/*  683 */                 startTag(localTagElement);
/*  684 */                 error("start.missing", localElement.getName());
/*  685 */                 return legalElementContext(paramElement);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  695 */     if ((this.stack.terminate()) && (this.stack.elem != this.dtd.body) && ((!this.strict) || (this.stack.elem.omitEnd())))
/*      */     {
/*  697 */       if (!this.stack.elem.omitEnd()) {
/*  698 */         error("end.missing", paramElement.getName());
/*      */       }
/*      */ 
/*  701 */       endTag(true);
/*  702 */       return legalElementContext(paramElement);
/*      */     }
/*      */ 
/*  706 */     return false;
/*      */   }
/*      */ 
/*      */   void legalTagContext(TagElement paramTagElement)
/*      */     throws ChangedCharSetException
/*      */   {
/*  713 */     if (legalElementContext(paramTagElement.getElement())) {
/*  714 */       markFirstTime(paramTagElement.getElement());
/*  715 */       return;
/*      */     }
/*      */ 
/*  719 */     if ((paramTagElement.breaksFlow()) && (this.stack != null) && (!this.stack.tag.breaksFlow())) {
/*  720 */       endTag(true);
/*  721 */       legalTagContext(paramTagElement);
/*  722 */       return;
/*      */     }
/*      */ 
/*  726 */     for (TagStack localTagStack = this.stack; localTagStack != null; localTagStack = localTagStack.next) {
/*  727 */       if (localTagStack.tag.getElement() == this.dtd.head) {
/*  728 */         while (this.stack != localTagStack) {
/*  729 */           endTag(true);
/*      */         }
/*  731 */         endTag(true);
/*  732 */         legalTagContext(paramTagElement);
/*  733 */         return;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  738 */     error("tag.unexpected", paramTagElement.getElement().getName());
/*      */   }
/*      */ 
/*      */   void errorContext()
/*      */     throws ChangedCharSetException
/*      */   {
/*  746 */     for (; (this.stack != null) && (this.stack.tag.getElement() != this.dtd.body); this.stack = this.stack.next) {
/*  747 */       handleEndTag(this.stack.tag);
/*      */     }
/*  749 */     if (this.stack == null) {
/*  750 */       legalElementContext(this.dtd.body);
/*  751 */       startTag(makeTag(this.dtd.body, true));
/*      */     }
/*      */   }
/*      */ 
/*      */   void addString(int paramInt)
/*      */   {
/*  759 */     if (this.strpos == this.str.length) {
/*  760 */       char[] arrayOfChar = new char[this.str.length + 128];
/*  761 */       System.arraycopy(this.str, 0, arrayOfChar, 0, this.str.length);
/*  762 */       this.str = arrayOfChar;
/*      */     }
/*  764 */     this.str[(this.strpos++)] = ((char)paramInt);
/*      */   }
/*      */ 
/*      */   String getString(int paramInt)
/*      */   {
/*  771 */     char[] arrayOfChar = new char[this.strpos - paramInt];
/*  772 */     System.arraycopy(this.str, paramInt, arrayOfChar, 0, this.strpos - paramInt);
/*  773 */     this.strpos = paramInt;
/*  774 */     return new String(arrayOfChar);
/*      */   }
/*      */ 
/*      */   char[] getChars(int paramInt) {
/*  778 */     char[] arrayOfChar = new char[this.strpos - paramInt];
/*  779 */     System.arraycopy(this.str, paramInt, arrayOfChar, 0, this.strpos - paramInt);
/*  780 */     this.strpos = paramInt;
/*  781 */     return arrayOfChar;
/*      */   }
/*      */ 
/*      */   char[] getChars(int paramInt1, int paramInt2) {
/*  785 */     char[] arrayOfChar = new char[paramInt2 - paramInt1];
/*  786 */     System.arraycopy(this.str, paramInt1, arrayOfChar, 0, paramInt2 - paramInt1);
/*      */ 
/*  789 */     return arrayOfChar;
/*      */   }
/*      */ 
/*      */   void resetStrBuffer() {
/*  793 */     this.strpos = 0;
/*      */   }
/*      */ 
/*      */   int strIndexOf(char paramChar) {
/*  797 */     for (int i = 0; i < this.strpos; i++) {
/*  798 */       if (this.str[i] == paramChar) {
/*  799 */         return i;
/*      */       }
/*      */     }
/*      */ 
/*  803 */     return -1;
/*      */   }
/*      */ 
/*      */   void skipSpace()
/*      */     throws IOException
/*      */   {
/*      */     while (true)
/*  812 */       switch (this.ch) {
/*      */       case 10:
/*  814 */         this.ln += 1;
/*  815 */         this.ch = readCh();
/*  816 */         this.lfCount += 1;
/*  817 */         break;
/*      */       case 13:
/*  820 */         this.ln += 1;
/*  821 */         if ((this.ch = readCh()) == 10) {
/*  822 */           this.ch = readCh();
/*  823 */           this.crlfCount += 1;
/*      */         }
/*      */         else {
/*  826 */           this.crCount += 1;
/*      */         }
/*  828 */         break;
/*      */       case 9:
/*      */       case 32:
/*  831 */         this.ch = readCh();
/*      */       }
/*      */   }
/*      */ 
/*      */   boolean parseIdentifier(boolean paramBoolean)
/*      */     throws IOException
/*      */   {
/*  846 */     switch (this.ch) { case 65:
/*      */     case 66:
/*      */     case 67:
/*      */     case 68:
/*      */     case 69:
/*      */     case 70:
/*      */     case 71:
/*      */     case 72:
/*      */     case 73:
/*      */     case 74:
/*      */     case 75:
/*      */     case 76:
/*      */     case 77:
/*      */     case 78:
/*      */     case 79:
/*      */     case 80:
/*      */     case 81:
/*      */     case 82:
/*      */     case 83:
/*      */     case 84:
/*      */     case 85:
/*      */     case 86:
/*      */     case 87:
/*      */     case 88:
/*      */     case 89:
/*      */     case 90:
/*  852 */       if (paramBoolean)
/*  853 */         this.ch = (97 + (this.ch - 65)); case 97:
/*      */     case 98:
/*      */     case 99:
/*      */     case 100:
/*      */     case 101:
/*      */     case 102:
/*      */     case 103:
/*      */     case 104:
/*      */     case 105:
/*      */     case 106:
/*      */     case 107:
/*      */     case 108:
/*      */     case 109:
/*      */     case 110:
/*      */     case 111:
/*      */     case 112:
/*      */     case 113:
/*      */     case 114:
/*      */     case 115:
/*      */     case 116:
/*      */     case 117:
/*      */     case 118:
/*      */     case 119:
/*      */     case 120:
/*      */     case 121:
/*      */     case 122:
/*  861 */       break;
/*      */     case 91:
/*      */     case 92:
/*      */     case 93:
/*      */     case 94:
/*      */     case 95:
/*  864 */     case 96: } return false;
/*      */     while (true)
/*      */     {
/*  868 */       addString(this.ch);
/*      */ 
/*  870 */       switch (this.ch = readCh()) { case 65:
/*      */       case 66:
/*      */       case 67:
/*      */       case 68:
/*      */       case 69:
/*      */       case 70:
/*      */       case 71:
/*      */       case 72:
/*      */       case 73:
/*      */       case 74:
/*      */       case 75:
/*      */       case 76:
/*      */       case 77:
/*      */       case 78:
/*      */       case 79:
/*      */       case 80:
/*      */       case 81:
/*      */       case 82:
/*      */       case 83:
/*      */       case 84:
/*      */       case 85:
/*      */       case 86:
/*      */       case 87:
/*      */       case 88:
/*      */       case 89:
/*      */       case 90:
/*  876 */         if (paramBoolean)
/*  877 */           this.ch = (97 + (this.ch - 65)); break;
/*      */       case 45:
/*      */       case 46:
/*      */       case 48:
/*      */       case 49:
/*      */       case 50:
/*      */       case 51:
/*      */       case 52:
/*      */       case 53:
/*      */       case 54:
/*      */       case 55:
/*      */       case 56:
/*      */       case 57:
/*      */       case 95:
/*      */       case 97:
/*      */       case 98:
/*      */       case 99:
/*      */       case 100:
/*      */       case 101:
/*      */       case 102:
/*      */       case 103:
/*      */       case 104:
/*      */       case 105:
/*      */       case 106:
/*      */       case 107:
/*      */       case 108:
/*      */       case 109:
/*      */       case 110:
/*      */       case 111:
/*      */       case 112:
/*      */       case 113:
/*      */       case 114:
/*      */       case 115:
/*      */       case 116:
/*      */       case 117:
/*      */       case 118:
/*      */       case 119:
/*      */       case 120:
/*      */       case 121:
/*      */       case 122:
/*      */       case 47:
/*      */       case 58:
/*      */       case 59:
/*      */       case 60:
/*      */       case 61:
/*      */       case 62:
/*      */       case 63:
/*      */       case 64:
/*      */       case 91:
/*      */       case 92:
/*      */       case 93:
/*      */       case 94:
/*  895 */       case 96: }  } return true;
/*      */   }
/*      */ 
/*      */   private char[] parseEntityReference()
/*      */     throws IOException
/*      */   {
/*  904 */     int i = this.strpos;
/*      */ 
/*  906 */     if ((this.ch = readCh()) == 35) {
/*  907 */       int j = 0;
/*  908 */       this.ch = readCh();
/*  909 */       if (((this.ch >= 48) && (this.ch <= 57)) || (this.ch == 120) || (this.ch == 88))
/*      */       {
/*  912 */         if ((this.ch >= 48) && (this.ch <= 57));
/*  914 */         while ((this.ch >= 48) && (this.ch <= 57)) {
/*  915 */           j = j * 10 + this.ch - 48;
/*  916 */           this.ch = readCh(); continue;
/*      */ 
/*  920 */           this.ch = readCh();
/*  921 */           int m = (char)Character.toLowerCase(this.ch);
/*  922 */           while (((m >= 48) && (m <= 57)) || ((m >= 97) && (m <= 102)))
/*      */           {
/*  924 */             if ((m >= 48) && (m <= 57))
/*  925 */               j = j * 16 + m - 48;
/*      */             else {
/*  927 */               j = j * 16 + m - 97 + 10;
/*      */             }
/*  929 */             this.ch = readCh();
/*  930 */             m = (char)Character.toLowerCase(this.ch);
/*      */           }
/*      */         }
/*  933 */         switch (this.ch) {
/*      */         case 10:
/*  935 */           this.ln += 1;
/*  936 */           this.ch = readCh();
/*  937 */           this.lfCount += 1;
/*  938 */           break;
/*      */         case 13:
/*  941 */           this.ln += 1;
/*  942 */           if ((this.ch = readCh()) == 10) {
/*  943 */             this.ch = readCh();
/*  944 */             this.crlfCount += 1;
/*      */           }
/*      */           else {
/*  947 */             this.crCount += 1;
/*      */           }
/*  949 */           break;
/*      */         case 59:
/*  952 */           this.ch = readCh();
/*      */         }
/*      */ 
/*  955 */         localObject = mapNumericReference(j);
/*  956 */         return localObject;
/*      */       }
/*  958 */       addString(35);
/*  959 */       if (!parseIdentifier(false)) {
/*  960 */         error("ident.expected");
/*  961 */         this.strpos = i;
/*  962 */         localObject = new char[] { '&', '#' };
/*  963 */         return localObject;
/*      */       }
/*  965 */     } else if (!parseIdentifier(false)) {
/*  966 */       char[] arrayOfChar1 = { '&' };
/*  967 */       return arrayOfChar1;
/*      */     }
/*      */ 
/*  970 */     int k = 0;
/*      */ 
/*  972 */     switch (this.ch) {
/*      */     case 10:
/*  974 */       this.ln += 1;
/*  975 */       this.ch = readCh();
/*  976 */       this.lfCount += 1;
/*  977 */       break;
/*      */     case 13:
/*  980 */       this.ln += 1;
/*  981 */       if ((this.ch = readCh()) == 10) {
/*  982 */         this.ch = readCh();
/*  983 */         this.crlfCount += 1;
/*      */       }
/*      */       else {
/*  986 */         this.crCount += 1;
/*      */       }
/*  988 */       break;
/*      */     case 59:
/*  991 */       k = 1;
/*      */ 
/*  993 */       this.ch = readCh();
/*      */     }
/*      */ 
/*  997 */     Object localObject = getString(i);
/*  998 */     Entity localEntity = this.dtd.getEntity((String)localObject);
/*      */ 
/* 1004 */     if ((!this.strict) && (localEntity == null)) {
/* 1005 */       localEntity = this.dtd.getEntity(((String)localObject).toLowerCase());
/*      */     }
/* 1007 */     if ((localEntity == null) || (!localEntity.isGeneral()))
/*      */     {
/* 1009 */       if (((String)localObject).length() == 0) {
/* 1010 */         error("invalid.entref", (String)localObject);
/* 1011 */         return new char[0];
/*      */       }
/*      */ 
/* 1014 */       String str1 = "&" + (String)localObject + (k != 0 ? ";" : "");
/*      */ 
/* 1016 */       char[] arrayOfChar2 = new char[str1.length()];
/* 1017 */       str1.getChars(0, arrayOfChar2.length, arrayOfChar2, 0);
/* 1018 */       return arrayOfChar2;
/*      */     }
/* 1020 */     return localEntity.getData();
/*      */   }
/*      */ 
/*      */   private char[] mapNumericReference(int paramInt)
/*      */   {
/*      */     char[] arrayOfChar;
/* 1037 */     if (paramInt >= 65535) {
/*      */       try {
/* 1039 */         arrayOfChar = Character.toChars(paramInt);
/*      */       } catch (IllegalArgumentException localIllegalArgumentException) {
/* 1041 */         arrayOfChar = new char[0];
/*      */       }
/*      */     } else {
/* 1044 */       arrayOfChar = new char[1];
/* 1045 */       arrayOfChar[0] = ((paramInt < 130) || (paramInt > 159) ? (char)paramInt : cp1252Map[(paramInt - 130)]);
/*      */     }
/* 1047 */     return arrayOfChar;
/*      */   }
/*      */ 
/*      */   void parseComment()
/*      */     throws IOException
/*      */   {
/*      */     while (true)
/*      */     {
/* 1056 */       int i = this.ch;
/* 1057 */       switch (i)
/*      */       {
/*      */       case 45:
/* 1069 */         if ((!this.strict) && (this.strpos != 0) && (this.str[(this.strpos - 1)] == '-')) {
/* 1070 */           if ((this.ch = readCh()) == 62) {
/* 1071 */             return;
/*      */           }
/* 1073 */           if (this.ch != 33) break label343;
/* 1074 */           if ((this.ch = readCh()) == 62) {
/* 1075 */             return;
/*      */           }
/*      */ 
/* 1078 */           addString(45);
/* 1079 */           addString(33);
/* 1080 */           continue;
/*      */         }
/*      */ 
/* 1086 */         if ((this.ch = readCh()) != 45) break label343;
/* 1087 */         this.ch = readCh();
/* 1088 */         if ((this.strict) || (this.ch == 62)) {
/* 1089 */           return;
/*      */         }
/* 1091 */         if (this.ch == 33) {
/* 1092 */           if ((this.ch = readCh()) == 62) {
/* 1093 */             return;
/*      */           }
/*      */ 
/* 1096 */           addString(45);
/* 1097 */           addString(33);
/* 1098 */           continue;
/*      */         }
/*      */ 
/* 1102 */         addString(45); break;
/*      */       case -1:
/* 1107 */         handleEOFInComment();
/* 1108 */         return;
/*      */       case 10:
/* 1111 */         this.ln += 1;
/* 1112 */         this.ch = readCh();
/* 1113 */         this.lfCount += 1;
/* 1114 */         break;
/*      */       case 62:
/* 1117 */         this.ch = readCh();
/* 1118 */         break;
/*      */       case 13:
/* 1121 */         this.ln += 1;
/* 1122 */         if ((this.ch = readCh()) == 10) {
/* 1123 */           this.ch = readCh();
/* 1124 */           this.crlfCount += 1;
/*      */         }
/*      */         else {
/* 1127 */           this.crCount += 1;
/*      */         }
/* 1129 */         i = 10;
/* 1130 */         break;
/*      */       }
/* 1132 */       this.ch = readCh();
/*      */ 
/* 1136 */       label343: addString(i);
/*      */     }
/*      */   }
/*      */ 
/*      */   void parseLiteral(boolean paramBoolean)
/*      */     throws IOException
/*      */   {
/*      */     while (true)
/*      */     {
/* 1145 */       int i = this.ch;
/* 1146 */       switch (i) {
/*      */       case -1:
/* 1148 */         error("eof.literal", this.stack.elem.getName());
/* 1149 */         endTag(true);
/* 1150 */         return;
/*      */       case 62:
/* 1153 */         this.ch = readCh();
/* 1154 */         int j = this.textpos - (this.stack.elem.name.length() + 2); int k = 0;
/*      */ 
/* 1157 */         if ((j < 0) || (this.text[(j++)] != '<') || (this.text[j] != '/')) break label456;
/*      */         do j++; while ((j < this.textpos) && (Character.toLowerCase(this.text[j]) == this.stack.elem.name.charAt(k++)));
/*      */ 
/* 1160 */         if (j != this.textpos) break label456;
/* 1161 */         this.textpos -= this.stack.elem.name.length() + 2;
/* 1162 */         if ((this.textpos > 0) && (this.text[(this.textpos - 1)] == '\n')) {
/* 1163 */           this.textpos -= 1;
/*      */         }
/* 1165 */         endTag(false);
/* 1166 */         return;
/*      */       case 38:
/* 1172 */         char[] arrayOfChar2 = parseEntityReference();
/* 1173 */         if (this.textpos + arrayOfChar2.length > this.text.length) {
/* 1174 */           char[] arrayOfChar3 = new char[Math.max(this.textpos + arrayOfChar2.length + 128, this.text.length * 2)];
/* 1175 */           System.arraycopy(this.text, 0, arrayOfChar3, 0, this.text.length);
/* 1176 */           this.text = arrayOfChar3;
/*      */         }
/* 1178 */         System.arraycopy(arrayOfChar2, 0, this.text, this.textpos, arrayOfChar2.length);
/* 1179 */         this.textpos += arrayOfChar2.length;
/* 1180 */         break;
/*      */       case 10:
/* 1183 */         this.ln += 1;
/* 1184 */         this.ch = readCh();
/* 1185 */         this.lfCount += 1;
/* 1186 */         break;
/*      */       case 13:
/* 1189 */         this.ln += 1;
/* 1190 */         if ((this.ch = readCh()) == 10) {
/* 1191 */           this.ch = readCh();
/* 1192 */           this.crlfCount += 1;
/*      */         }
/*      */         else {
/* 1195 */           this.crCount += 1;
/*      */         }
/* 1197 */         i = 10;
/* 1198 */         break;
/*      */       }
/* 1200 */       this.ch = readCh();
/*      */ 
/* 1205 */       label456: if (this.textpos == this.text.length) {
/* 1206 */         char[] arrayOfChar1 = new char[this.text.length + 128];
/* 1207 */         System.arraycopy(this.text, 0, arrayOfChar1, 0, this.text.length);
/* 1208 */         this.text = arrayOfChar1;
/*      */       }
/* 1210 */       this.text[(this.textpos++)] = ((char)i);
/*      */     }
/*      */   }
/*      */ 
/*      */   String parseAttributeValue(boolean paramBoolean)
/*      */     throws IOException
/*      */   {
/* 1218 */     int i = -1;
/*      */ 
/* 1221 */     switch (this.ch) {
/*      */     case 34:
/*      */     case 39:
/* 1224 */       i = this.ch;
/* 1225 */       this.ch = readCh();
/*      */     }
/*      */ 
/*      */     while (true)
/*      */     {
/* 1231 */       int j = this.ch;
/*      */ 
/* 1233 */       switch (j) {
/*      */       case 10:
/* 1235 */         this.ln += 1;
/* 1236 */         this.ch = readCh();
/* 1237 */         this.lfCount += 1;
/* 1238 */         if (i < 0) {
/* 1239 */           return getString(0);
/*      */         }
/*      */ 
/*      */       case 13:
/* 1244 */         this.ln += 1;
/*      */ 
/* 1246 */         if ((this.ch = readCh()) == 10) {
/* 1247 */           this.ch = readCh();
/* 1248 */           this.crlfCount += 1;
/*      */         }
/*      */         else {
/* 1251 */           this.crCount += 1;
/*      */         }
/* 1253 */         if (i < 0) {
/* 1254 */           return getString(0);
/*      */         }
/*      */ 
/*      */       case 9:
/* 1259 */         if (i < 0)
/* 1260 */           j = 32;
/*      */       case 32:
/* 1262 */         this.ch = readCh();
/* 1263 */         if (i < 0) {
/* 1264 */           return getString(0);
/*      */         }
/*      */ 
/*      */       case 60:
/*      */       case 62:
/* 1270 */         if (i < 0) {
/* 1271 */           return getString(0);
/*      */         }
/* 1273 */         this.ch = readCh();
/* 1274 */         break;
/*      */       case 34:
/*      */       case 39:
/* 1278 */         this.ch = readCh();
/* 1279 */         if (j == i)
/* 1280 */           return getString(0);
/* 1281 */         if (i == -1) {
/* 1282 */           error("attvalerr");
/* 1283 */           if ((!this.strict) && (this.ch != 32)) continue;
/* 1284 */           return getString(0);
/*      */         }
/*      */ 
/*      */       case 61:
/* 1292 */         if (i < 0)
/*      */         {
/* 1297 */           error("attvalerr");
/*      */ 
/* 1301 */           if (this.strict) {
/* 1302 */             return getString(0);
/*      */           }
/*      */         }
/* 1305 */         this.ch = readCh();
/* 1306 */         break;
/*      */       case 38:
/* 1309 */         if ((this.strict) && (i < 0)) {
/* 1310 */           this.ch = readCh();
/*      */         }
/*      */         else
/*      */         {
/* 1314 */           char[] arrayOfChar = parseEntityReference();
/* 1315 */           for (int k = 0; k < arrayOfChar.length; k++) {
/* 1316 */             j = arrayOfChar[k];
/* 1317 */             addString((paramBoolean) && (j >= 65) && (j <= 90) ? 97 + j - 65 : j);
/*      */           }
/*      */         }
/* 1319 */         break;
/*      */       case -1:
/* 1322 */         return getString(0);
/*      */       default:
/* 1325 */         if ((paramBoolean) && (j >= 65) && (j <= 90)) {
/* 1326 */           j = 97 + j - 65;
/*      */         }
/* 1328 */         this.ch = readCh();
/*      */ 
/* 1331 */         addString(j);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   void parseAttributeSpecificationList(Element paramElement)
/*      */     throws IOException
/*      */   {
/*      */     while (true)
/*      */     {
/* 1342 */       skipSpace();
/*      */ 
/* 1344 */       switch (this.ch) {
/*      */       case -1:
/*      */       case 47:
/*      */       case 60:
/*      */       case 62:
/* 1349 */         return;
/*      */       case 45:
/* 1352 */         if ((this.ch = readCh()) == 45) {
/* 1353 */           this.ch = readCh();
/* 1354 */           parseComment();
/* 1355 */           this.strpos = 0; continue;
/*      */         }
/* 1357 */         error("invalid.tagchar", "-", paramElement.getName());
/* 1358 */         this.ch = readCh();
/*      */ 
/* 1360 */         break;
/*      */       }
/*      */       String str1;
/*      */       AttributeList localAttributeList;
/*      */       String str2;
/* 1367 */       if (parseIdentifier(true)) {
/* 1368 */         str1 = getString(0);
/* 1369 */         skipSpace();
/* 1370 */         if (this.ch == 61) {
/* 1371 */           this.ch = readCh();
/* 1372 */           skipSpace();
/* 1373 */           localAttributeList = paramElement.getAttribute(str1);
/*      */ 
/* 1378 */           str2 = parseAttributeValue((localAttributeList != null) && (localAttributeList.type != 1) && (localAttributeList.type != 11) && (localAttributeList.type != 7));
/*      */         }
/*      */         else {
/* 1381 */           str2 = str1;
/* 1382 */           localAttributeList = paramElement.getAttributeByValue(str2);
/* 1383 */           if (localAttributeList == null) {
/* 1384 */             localAttributeList = paramElement.getAttribute(str1);
/* 1385 */             if (localAttributeList != null) {
/* 1386 */               str2 = localAttributeList.getValue();
/*      */             }
/*      */             else
/*      */             {
/* 1391 */               str2 = null;
/*      */             }
/*      */           }
/*      */         }
/*      */       } else { if ((!this.strict) && (this.ch == 44)) {
/* 1396 */           this.ch = readCh();
/* 1397 */           continue;
/* 1398 */         }if ((!this.strict) && (this.ch == 34)) {
/* 1399 */           this.ch = readCh();
/* 1400 */           skipSpace();
/* 1401 */           if (parseIdentifier(true)) {
/* 1402 */             str1 = getString(0);
/* 1403 */             if (this.ch == 34) {
/* 1404 */               this.ch = readCh();
/*      */             }
/* 1406 */             skipSpace();
/* 1407 */             if (this.ch == 61) {
/* 1408 */               this.ch = readCh();
/* 1409 */               skipSpace();
/* 1410 */               localAttributeList = paramElement.getAttribute(str1);
/* 1411 */               str2 = parseAttributeValue((localAttributeList != null) && (localAttributeList.type != 1) && (localAttributeList.type != 11)); break label651;
/*      */             }
/*      */ 
/* 1415 */             str2 = str1;
/* 1416 */             localAttributeList = paramElement.getAttributeByValue(str2);
/* 1417 */             if (localAttributeList != null) break label651;
/* 1418 */             localAttributeList = paramElement.getAttribute(str1);
/* 1419 */             if (localAttributeList == null) break label651;
/* 1420 */             str2 = localAttributeList.getValue(); break label651;
/*      */           }
/*      */ 
/* 1425 */           localObject = new char[] { (char)this.ch };
/* 1426 */           error("invalid.tagchar", new String((char[])localObject), paramElement.getName());
/* 1427 */           this.ch = readCh();
/* 1428 */           continue;
/*      */         }
/* 1430 */         if ((!this.strict) && (this.attributes.isEmpty()) && (this.ch == 61)) {
/* 1431 */           this.ch = readCh();
/* 1432 */           skipSpace();
/* 1433 */           str1 = paramElement.getName();
/* 1434 */           localAttributeList = paramElement.getAttribute(str1);
/* 1435 */           str2 = parseAttributeValue((localAttributeList != null) && (localAttributeList.type != 1) && (localAttributeList.type != 11));
/*      */         }
/*      */         else {
/* 1438 */           if ((!this.strict) && (this.ch == 61)) {
/* 1439 */             this.ch = readCh();
/* 1440 */             skipSpace();
/* 1441 */             str2 = parseAttributeValue(true);
/* 1442 */             error("attvalerr");
/* 1443 */             return;
/*      */           }
/* 1445 */           localObject = new char[] { (char)this.ch };
/* 1446 */           error("invalid.tagchar", new String((char[])localObject), paramElement.getName());
/* 1447 */           if (!this.strict) {
/* 1448 */             this.ch = readCh();
/* 1449 */             continue;
/*      */           }
/* 1451 */           return;
/*      */         }
/*      */       }
/*      */ 
/* 1455 */       label651: if (localAttributeList != null)
/* 1456 */         str1 = localAttributeList.getName();
/*      */       else {
/* 1458 */         error("invalid.tagatt", str1, paramElement.getName());
/*      */       }
/*      */ 
/* 1462 */       if (this.attributes.isDefined(str1)) {
/* 1463 */         error("multi.tagatt", str1, paramElement.getName());
/*      */       }
/* 1465 */       if (str2 == null) {
/* 1466 */         str2 = (localAttributeList != null) && (localAttributeList.value != null) ? localAttributeList.value : "#DEFAULT";
/*      */       }
/* 1468 */       else if ((localAttributeList != null) && (localAttributeList.values != null) && (!localAttributeList.values.contains(str2))) {
/* 1469 */         error("invalid.tagattval", str1, paramElement.getName());
/*      */       }
/* 1471 */       Object localObject = HTML.getAttributeKey(str1);
/* 1472 */       if (localObject == null)
/* 1473 */         this.attributes.addAttribute(str1, str2);
/*      */       else
/* 1475 */         this.attributes.addAttribute(localObject, str2);
/*      */     }
/*      */   }
/*      */ 
/*      */   public String parseDTDMarkup()
/*      */     throws IOException
/*      */   {
/* 1486 */     StringBuilder localStringBuilder = new StringBuilder();
/* 1487 */     this.ch = readCh();
/*      */     while (true)
/* 1489 */       switch (this.ch) {
/*      */       case 62:
/* 1491 */         this.ch = readCh();
/* 1492 */         return localStringBuilder.toString();
/*      */       case -1:
/* 1494 */         error("invalid.markup");
/* 1495 */         return localStringBuilder.toString();
/*      */       case 10:
/* 1497 */         this.ln += 1;
/* 1498 */         this.ch = readCh();
/* 1499 */         this.lfCount += 1;
/* 1500 */         break;
/*      */       case 34:
/* 1502 */         this.ch = readCh();
/* 1503 */         break;
/*      */       case 13:
/* 1505 */         this.ln += 1;
/* 1506 */         if ((this.ch = readCh()) == 10) {
/* 1507 */           this.ch = readCh();
/* 1508 */           this.crlfCount += 1;
/*      */         }
/*      */         else {
/* 1511 */           this.crCount += 1;
/*      */         }
/* 1513 */         break;
/*      */       default:
/* 1515 */         localStringBuilder.append((char)(this.ch & 0xFF));
/* 1516 */         this.ch = readCh();
/*      */       }
/*      */   }
/*      */ 
/*      */   protected boolean parseMarkupDeclarations(StringBuffer paramStringBuffer)
/*      */     throws IOException
/*      */   {
/* 1530 */     if ((paramStringBuffer.length() == "DOCTYPE".length()) && (paramStringBuffer.toString().toUpperCase().equals("DOCTYPE")))
/*      */     {
/* 1532 */       parseDTDMarkup();
/* 1533 */       return true;
/*      */     }
/* 1535 */     return false;
/*      */   }
/*      */ 
/*      */   void parseInvalidTag()
/*      */     throws IOException
/*      */   {
/*      */     while (true)
/*      */     {
/* 1544 */       skipSpace();
/* 1545 */       switch (this.ch) {
/*      */       case -1:
/*      */       case 62:
/* 1548 */         this.ch = readCh();
/* 1549 */         return;
/*      */       case 60:
/* 1551 */         return;
/*      */       }
/* 1553 */       this.ch = readCh();
/*      */     }
/*      */   }
/*      */ 
/*      */   void parseTag()
/*      */     throws IOException
/*      */   {
/* 1564 */     boolean bool = false;
/* 1565 */     int i = 0;
/* 1566 */     int j = 0;
/*      */     Element localElement;
/* 1568 */     switch (this.ch = readCh()) {
/*      */     case 33:
/* 1570 */       switch (this.ch = readCh())
/*      */       {
/*      */       case 45:
/*      */         while (true) {
/* 1574 */           if (this.ch == 45) {
/* 1575 */             if ((!this.strict) || ((this.ch = readCh()) == 45)) {
/* 1576 */               this.ch = readCh();
/* 1577 */               if ((!this.strict) && (this.ch == 45)) {
/* 1578 */                 this.ch = readCh();
/*      */               }
/*      */ 
/* 1583 */               if (this.textpos != 0) {
/* 1584 */                 localObject = new char[this.textpos];
/* 1585 */                 System.arraycopy(this.text, 0, localObject, 0, this.textpos);
/* 1586 */                 handleText((char[])localObject);
/* 1587 */                 this.lastBlockStartPos = this.currentBlockStartPos;
/* 1588 */                 this.textpos = 0;
/*      */               }
/* 1590 */               parseComment();
/* 1591 */               this.last = makeTag(this.dtd.getElement("comment"), true);
/* 1592 */               handleComment(getChars(0));
/*      */             }
/* 1594 */             else if (i == 0) {
/* 1595 */               i = 1;
/* 1596 */               error("invalid.commentchar", "-");
/*      */             }
/*      */           } else {
/* 1599 */             skipSpace();
/* 1600 */             switch (this.ch) {
/*      */             case 45:
/* 1602 */               break;
/*      */             case 62:
/* 1604 */               this.ch = readCh();
/*      */             case -1:
/* 1606 */               return;
/*      */             default:
/* 1608 */               this.ch = readCh();
/* 1609 */               if (i == 0) {
/* 1610 */                 i = 1;
/* 1611 */                 error("invalid.commentchar", String.valueOf((char)this.ch));
/*      */               }
/*      */               break;
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1620 */       localObject = new StringBuffer();
/*      */       while (true) {
/* 1622 */         ((StringBuffer)localObject).append((char)this.ch);
/* 1623 */         if (parseMarkupDeclarations((StringBuffer)localObject)) {
/* 1624 */           return;
/*      */         }
/* 1626 */         switch (this.ch) {
/*      */         case 62:
/* 1628 */           this.ch = readCh();
/*      */         case -1:
/* 1630 */           error("invalid.markup");
/* 1631 */           return;
/*      */         case 10:
/* 1633 */           this.ln += 1;
/* 1634 */           this.ch = readCh();
/* 1635 */           this.lfCount += 1;
/* 1636 */           break;
/*      */         case 13:
/* 1638 */           this.ln += 1;
/* 1639 */           if ((this.ch = readCh()) == 10) {
/* 1640 */             this.ch = readCh();
/* 1641 */             this.crlfCount += 1;
/*      */           }
/*      */           else {
/* 1644 */             this.crCount += 1;
/*      */           }
/* 1646 */           break;
/*      */         default:
/* 1649 */           this.ch = readCh();
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     case 47:
/* 1657 */       switch (this.ch = readCh()) {
/*      */       case 62:
/* 1659 */         this.ch = readCh();
/*      */       case 60:
/* 1662 */         if (this.recent == null) {
/* 1663 */           error("invalid.shortend");
/* 1664 */           return;
/*      */         }
/* 1666 */         localElement = this.recent;
/* 1667 */         break;
/*      */       default:
/* 1670 */         if (!parseIdentifier(true)) {
/* 1671 */           error("expected.endtagname");
/* 1672 */           return;
/*      */         }
/* 1674 */         skipSpace();
/* 1675 */         switch (this.ch) {
/*      */         case 62:
/* 1677 */           this.ch = readCh();
/*      */         case 60:
/* 1679 */           break;
/*      */         default:
/* 1682 */           error("expected", "'>'");
/* 1683 */           while ((this.ch != -1) && (this.ch != 10) && (this.ch != 62)) {
/* 1684 */             this.ch = readCh();
/*      */           }
/* 1686 */           if (this.ch == 62) {
/* 1687 */             this.ch = readCh();
/*      */           }
/*      */           break;
/*      */         }
/* 1691 */         localObject = getString(0);
/* 1692 */         if (!this.dtd.elementExists((String)localObject)) {
/* 1693 */           error("end.unrecognized", (String)localObject);
/*      */ 
/* 1695 */           if ((this.textpos > 0) && (this.text[(this.textpos - 1)] == '\n')) {
/* 1696 */             this.textpos -= 1;
/*      */           }
/* 1698 */           localElement = this.dtd.getElement("unknown");
/* 1699 */           localElement.name = ((String)localObject);
/* 1700 */           j = 1;
/*      */         } else {
/* 1702 */           localElement = this.dtd.getElement((String)localObject);
/*      */         }
/*      */ 
/*      */         break;
/*      */       }
/*      */ 
/* 1711 */       if (this.stack == null) {
/* 1712 */         error("end.extra.tag", localElement.getName());
/* 1713 */         return;
/*      */       }
/*      */ 
/* 1717 */       if ((this.textpos > 0) && (this.text[(this.textpos - 1)] == '\n'))
/*      */       {
/* 1722 */         if (this.stack.pre) {
/* 1723 */           if ((this.textpos > 1) && (this.text[(this.textpos - 2)] != '\n'))
/* 1724 */             this.textpos -= 1;
/*      */         }
/*      */         else {
/* 1727 */           this.textpos -= 1;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1748 */       if (j != 0)
/*      */       {
/* 1754 */         localObject = makeTag(localElement);
/* 1755 */         handleText((TagElement)localObject);
/* 1756 */         this.attributes.addAttribute(HTML.Attribute.ENDTAG, "true");
/* 1757 */         handleEmptyTag(makeTag(localElement));
/* 1758 */         j = 0;
/* 1759 */         return;
/*      */       }
/*      */ 
/* 1768 */       if (!this.strict) {
/* 1769 */         localObject = this.stack.elem.getName();
/*      */ 
/* 1771 */         if (((String)localObject).equals("table"))
/*      */         {
/* 1774 */           if (!localElement.getName().equals(localObject)) {
/* 1775 */             error("tag.ignore", localElement.getName());
/* 1776 */             return;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1782 */         if ((((String)localObject).equals("tr")) || (((String)localObject).equals("td")))
/*      */         {
/* 1784 */           if ((!localElement.getName().equals("table")) && (!localElement.getName().equals(localObject)))
/*      */           {
/* 1786 */             error("tag.ignore", localElement.getName());
/* 1787 */             return;
/*      */           }
/*      */         }
/*      */       }
/* 1791 */       localObject = this.stack;
/*      */ 
/* 1793 */       while ((localObject != null) && (localElement != ((TagStack)localObject).elem)) {
/* 1794 */         localObject = ((TagStack)localObject).next;
/*      */       }
/* 1796 */       if (localObject == null) {
/* 1797 */         error("unmatched.endtag", localElement.getName());
/* 1798 */         return;
/*      */       }
/*      */ 
/* 1806 */       String str1 = localElement.getName();
/* 1807 */       if ((this.stack != localObject) && ((str1.equals("font")) || (str1.equals("center"))))
/*      */       {
/* 1816 */         if (str1.equals("center")) {
/* 1817 */           while ((this.stack.elem.omitEnd()) && (this.stack != localObject)) {
/* 1818 */             endTag(true);
/*      */           }
/* 1820 */           if (this.stack.elem == localElement) {
/* 1821 */             endTag(false);
/*      */           }
/*      */         }
/* 1824 */         return;
/*      */       }
/*      */ 
/* 1833 */       while (this.stack != localObject) {
/* 1834 */         endTag(true);
/*      */       }
/*      */ 
/* 1837 */       endTag(false);
/* 1838 */       return;
/*      */     case -1:
/* 1841 */       error("eof");
/* 1842 */       return;
/*      */     }
/*      */ 
/* 1846 */     if (!parseIdentifier(true)) {
/* 1847 */       localElement = this.recent;
/* 1848 */       if ((this.ch != 62) || (localElement == null))
/* 1849 */         error("expected.tagname");
/*      */     }
/*      */     else
/*      */     {
/* 1853 */       localObject = getString(0);
/*      */ 
/* 1855 */       if (((String)localObject).equals("image")) {
/* 1856 */         localObject = "img";
/*      */       }
/*      */ 
/* 1861 */       if (!this.dtd.elementExists((String)localObject))
/*      */       {
/* 1863 */         error("tag.unrecognized ", (String)localObject);
/* 1864 */         localElement = this.dtd.getElement("unknown");
/* 1865 */         localElement.name = ((String)localObject);
/* 1866 */         j = 1;
/*      */       } else {
/* 1868 */         localElement = this.dtd.getElement((String)localObject);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1873 */     parseAttributeSpecificationList(localElement);
/*      */ 
/* 1875 */     switch (this.ch) {
/*      */     case 47:
/* 1877 */       bool = true;
/*      */     case 62:
/* 1879 */       this.ch = readCh();
/* 1880 */       if ((this.ch == 62) && (bool)) {
/* 1881 */         this.ch = readCh();
/*      */       }
/*      */     case 60:
/* 1884 */       break;
/*      */     }
/*      */ 
/* 1887 */     error("expected", "'>'");
/*      */ 
/* 1891 */     if ((!this.strict) && 
/* 1892 */       (localElement.getName().equals("script"))) {
/* 1893 */       error("javascript.unsupported");
/*      */     }
/*      */ 
/* 1899 */     if (!localElement.isEmpty()) {
/* 1900 */       if (this.ch == 10) {
/* 1901 */         this.ln += 1;
/* 1902 */         this.lfCount += 1;
/* 1903 */         this.ch = readCh();
/* 1904 */       } else if (this.ch == 13) {
/* 1905 */         this.ln += 1;
/* 1906 */         if ((this.ch = readCh()) == 10) {
/* 1907 */           this.ch = readCh();
/* 1908 */           this.crlfCount += 1;
/*      */         }
/*      */         else {
/* 1911 */           this.crCount += 1;
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1917 */     Object localObject = makeTag(localElement, false);
/*      */ 
/* 1940 */     if (j == 0) {
/* 1941 */       legalTagContext((TagElement)localObject);
/*      */ 
/* 1947 */       if ((!this.strict) && (this.skipTag)) {
/* 1948 */         this.skipTag = false;
/* 1949 */         return;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1956 */     startTag((TagElement)localObject);
/*      */ 
/* 1958 */     if (!localElement.isEmpty())
/* 1959 */       switch (localElement.getType()) {
/*      */       case 1:
/* 1961 */         parseLiteral(false);
/* 1962 */         break;
/*      */       case 16:
/* 1964 */         parseLiteral(true);
/* 1965 */         break;
/*      */       default:
/* 1967 */         if (this.stack != null)
/* 1968 */           this.stack.net = bool;
/*      */         break;
/*      */       }
/*      */   }
/*      */ 
/*      */   void parseScript()
/*      */     throws IOException
/*      */   {
/* 1982 */     char[] arrayOfChar = new char[SCRIPT_END_TAG.length];
/*      */     while (true)
/*      */     {
/* 1986 */       int i = 0;
/*      */ 
/* 1988 */       while ((i < SCRIPT_END_TAG.length) && ((SCRIPT_END_TAG[i] == this.ch) || (SCRIPT_END_TAG_UPPER_CASE[i] == this.ch)))
/*      */       {
/* 1990 */         arrayOfChar[i] = ((char)this.ch);
/* 1991 */         this.ch = readCh();
/* 1992 */         i++;
/*      */       }
/* 1994 */       if (i == SCRIPT_END_TAG.length)
/*      */       {
/* 1998 */         return;
/*      */       }
/*      */ 
/* 2002 */       for (int j = 0; j < i; j++) {
/* 2003 */         addString(arrayOfChar[j]);
/*      */       }
/*      */ 
/* 2006 */       switch (this.ch) {
/*      */       case -1:
/* 2008 */         error("eof.script");
/* 2009 */         return;
/*      */       case 10:
/* 2011 */         this.ln += 1;
/* 2012 */         this.ch = readCh();
/* 2013 */         this.lfCount += 1;
/* 2014 */         addString(10);
/* 2015 */         break;
/*      */       case 13:
/* 2017 */         this.ln += 1;
/* 2018 */         if ((this.ch = readCh()) == 10) {
/* 2019 */           this.ch = readCh();
/* 2020 */           this.crlfCount += 1;
/*      */         } else {
/* 2022 */           this.crCount += 1;
/*      */         }
/* 2024 */         addString(10);
/* 2025 */         break;
/*      */       default:
/* 2027 */         addString(this.ch);
/* 2028 */         this.ch = readCh();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   void parseContent()
/*      */     throws IOException
/*      */   {
/* 2039 */     Thread localThread = Thread.currentThread();
/*      */     while (true)
/*      */     {
/* 2042 */       if (localThread.isInterrupted()) {
/* 2043 */         localThread.interrupt();
/* 2044 */         break;
/*      */       }
/*      */ 
/* 2047 */       int i = this.ch;
/* 2048 */       this.currentBlockStartPos = this.currentPosition;
/*      */       Object localObject;
/* 2050 */       if (this.recent == this.dtd.script)
/*      */       {
/* 2053 */         parseScript();
/* 2054 */         this.last = makeTag(this.dtd.getElement("comment"), true);
/*      */ 
/* 2057 */         localObject = new String(getChars(0)).trim();
/* 2058 */         int j = "<!--".length() + "-->".length();
/* 2059 */         if ((((String)localObject).startsWith("<!--")) && (((String)localObject).endsWith("-->")) && (((String)localObject).length() >= j))
/*      */         {
/* 2061 */           localObject = ((String)localObject).substring("<!--".length(), ((String)localObject).length() - "-->".length());
/*      */         }
/*      */ 
/* 2066 */         handleComment(((String)localObject).toCharArray());
/* 2067 */         endTag(false);
/* 2068 */         this.lastBlockStartPos = this.currentPosition;
/*      */       }
/*      */       else
/*      */       {
/* 2072 */         switch (i) {
/*      */         case 60:
/* 2074 */           parseTag();
/* 2075 */           this.lastBlockStartPos = this.currentPosition;
/* 2076 */           break;
/*      */         case 47:
/* 2079 */           this.ch = readCh();
/* 2080 */           if ((this.stack != null) && (this.stack.net))
/*      */           {
/* 2082 */             endTag(false);
/*      */           }
/* 2084 */           else if (this.textpos == 0) {
/* 2085 */             if (!legalElementContext(this.dtd.pcdata)) {
/* 2086 */               error("unexpected.pcdata");
/*      */             }
/* 2088 */             if (this.last.breaksFlow())
/* 2089 */               this.space = false;  } break;
/*      */         case -1:
/* 2095 */           return;
/*      */         case 38:
/* 2098 */           if (this.textpos == 0) {
/* 2099 */             if (!legalElementContext(this.dtd.pcdata)) {
/* 2100 */               error("unexpected.pcdata");
/*      */             }
/* 2102 */             if (this.last.breaksFlow()) {
/* 2103 */               this.space = false;
/*      */             }
/*      */           }
/* 2106 */           localObject = parseEntityReference();
/* 2107 */           if (this.textpos + localObject.length + 1 > this.text.length) {
/* 2108 */             char[] arrayOfChar = new char[Math.max(this.textpos + localObject.length + 128, this.text.length * 2)];
/* 2109 */             System.arraycopy(this.text, 0, arrayOfChar, 0, this.text.length);
/* 2110 */             this.text = arrayOfChar;
/*      */           }
/* 2112 */           if (this.space) {
/* 2113 */             this.space = false;
/* 2114 */             this.text[(this.textpos++)] = ' ';
/*      */           }
/* 2116 */           System.arraycopy(localObject, 0, this.text, this.textpos, localObject.length);
/* 2117 */           this.textpos += localObject.length;
/* 2118 */           this.ignoreSpace = false;
/* 2119 */           break;
/*      */         case 10:
/* 2122 */           this.ln += 1;
/* 2123 */           this.lfCount += 1;
/* 2124 */           this.ch = readCh();
/* 2125 */           if ((this.stack == null) || (!this.stack.pre))
/*      */           {
/* 2128 */             if (this.textpos == 0) {
/* 2129 */               this.lastBlockStartPos = this.currentPosition;
/*      */             }
/* 2131 */             if (this.ignoreSpace) continue;
/* 2132 */             this.space = true; } break;
/*      */         case 13:
/* 2137 */           this.ln += 1;
/* 2138 */           i = 10;
/* 2139 */           if ((this.ch = readCh()) == 10) {
/* 2140 */             this.ch = readCh();
/* 2141 */             this.crlfCount += 1;
/*      */           }
/*      */           else {
/* 2144 */             this.crCount += 1;
/*      */           }
/* 2146 */           if ((this.stack == null) || (!this.stack.pre))
/*      */           {
/* 2149 */             if (this.textpos == 0) {
/* 2150 */               this.lastBlockStartPos = this.currentPosition;
/*      */             }
/* 2152 */             if (this.ignoreSpace) continue;
/* 2153 */             this.space = true; } break;
/*      */         case 9:
/*      */         case 32:
/* 2160 */           this.ch = readCh();
/* 2161 */           if ((this.stack == null) || (!this.stack.pre))
/*      */           {
/* 2164 */             if (this.textpos == 0) {
/* 2165 */               this.lastBlockStartPos = this.currentPosition;
/*      */             }
/* 2167 */             if (this.ignoreSpace) continue;
/* 2168 */             this.space = true; } break;
/*      */         default:
/* 2173 */           if (this.textpos == 0) {
/* 2174 */             if (!legalElementContext(this.dtd.pcdata)) {
/* 2175 */               error("unexpected.pcdata");
/*      */             }
/* 2177 */             if (this.last.breaksFlow()) {
/* 2178 */               this.space = false;
/*      */             }
/*      */           }
/* 2181 */           this.ch = readCh();
/*      */ 
/* 2187 */           if (this.textpos + 2 > this.text.length) {
/* 2188 */             localObject = new char[this.text.length + 128];
/* 2189 */             System.arraycopy(this.text, 0, localObject, 0, this.text.length);
/* 2190 */             this.text = ((char[])localObject);
/*      */           }
/*      */ 
/* 2194 */           if (this.space) {
/* 2195 */             if (this.textpos == 0) {
/* 2196 */               this.lastBlockStartPos -= 1;
/*      */             }
/* 2198 */             this.text[(this.textpos++)] = ' ';
/* 2199 */             this.space = false;
/*      */           }
/* 2201 */           this.text[(this.textpos++)] = ((char)i);
/* 2202 */           this.ignoreSpace = false;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   String getEndOfLineString()
/*      */   {
/* 2211 */     if (this.crlfCount >= this.crCount) {
/* 2212 */       if (this.lfCount >= this.crlfCount) {
/* 2213 */         return "\n";
/*      */       }
/*      */ 
/* 2216 */       return "\r\n";
/*      */     }
/*      */ 
/* 2220 */     if (this.crCount > this.lfCount) {
/* 2221 */       return "\r";
/*      */     }
/*      */ 
/* 2224 */     return "\n";
/*      */   }
/*      */ 
/*      */   public synchronized void parse(Reader paramReader)
/*      */     throws IOException
/*      */   {
/* 2233 */     this.in = paramReader;
/*      */ 
/* 2235 */     this.ln = 1;
/*      */ 
/* 2237 */     this.seenHtml = false;
/* 2238 */     this.seenHead = false;
/* 2239 */     this.seenBody = false;
/*      */ 
/* 2241 */     this.crCount = (this.lfCount = this.crlfCount = 0);
/*      */     try
/*      */     {
/* 2244 */       this.ch = readCh();
/* 2245 */       this.text = new char[1024];
/* 2246 */       this.str = new char[''];
/*      */ 
/* 2248 */       parseContent();
/*      */ 
/* 2251 */       while (this.stack != null) {
/* 2252 */         endTag(true);
/*      */       }
/* 2254 */       paramReader.close();
/*      */     } catch (IOException localIOException) {
/* 2256 */       errorContext();
/* 2257 */       error("ioexception");
/* 2258 */       throw localIOException;
/*      */     } catch (Exception localException) {
/* 2260 */       errorContext();
/* 2261 */       error("exception", localException.getClass().getName(), localException.getMessage());
/* 2262 */       localException.printStackTrace();
/*      */     } catch (ThreadDeath localThreadDeath) {
/* 2264 */       errorContext();
/* 2265 */       error("terminated");
/* 2266 */       localThreadDeath.printStackTrace();
/* 2267 */       throw localThreadDeath;
/*      */     } finally {
/* 2269 */       for (; this.stack != null; this.stack = this.stack.next) {
/* 2270 */         handleEndTag(this.stack.tag);
/*      */       }
/*      */ 
/* 2273 */       this.text = null;
/* 2274 */       this.str = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   private final int readCh()
/*      */     throws IOException
/*      */   {
/* 2303 */     if (this.pos >= this.len)
/*      */     {
/*      */       try
/*      */       {
/* 2309 */         this.len = this.in.read(this.buf);
/*      */       }
/*      */       catch (InterruptedIOException localInterruptedIOException) {
/* 2312 */         throw localInterruptedIOException;
/*      */       }
/*      */ 
/* 2316 */       if (this.len <= 0) {
/* 2317 */         return -1;
/*      */       }
/* 2319 */       this.pos = 0;
/*      */     }
/* 2321 */     this.currentPosition += 1;
/*      */ 
/* 2323 */     return this.buf[(this.pos++)];
/*      */   }
/*      */ 
/*      */   protected int getCurrentPos()
/*      */   {
/* 2328 */     return this.currentPosition;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.html.parser.Parser
 * JD-Core Version:    0.6.2
 */