/*      */ package com.sun.org.apache.xerces.internal.impl;
/*      */ 
/*      */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*      */ import com.sun.org.apache.xerces.internal.util.XML11Char;
/*      */ import com.sun.org.apache.xerces.internal.util.XMLChar;
/*      */ import com.sun.org.apache.xerces.internal.util.XMLStringBuffer;
/*      */ import com.sun.org.apache.xerces.internal.xni.QName;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLString;
/*      */ import com.sun.xml.internal.stream.Entity.ScannedEntity;
/*      */ import java.io.IOException;
/*      */ 
/*      */ public class XML11EntityScanner extends XMLEntityScanner
/*      */ {
/*      */   public int peekChar()
/*      */     throws IOException
/*      */   {
/*  110 */     if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
/*  111 */       load(0, true, true);
/*      */     }
/*      */ 
/*  115 */     int c = this.fCurrentEntity.ch[this.fCurrentEntity.position];
/*      */ 
/*  118 */     if (this.fCurrentEntity.isExternal()) {
/*  119 */       return (c != 13) && (c != 133) && (c != 8232) ? c : 10;
/*      */     }
/*      */ 
/*  122 */     return c;
/*      */   }
/*      */ 
/*      */   public int scanChar()
/*      */     throws IOException
/*      */   {
/*  138 */     if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
/*  139 */       load(0, true, true);
/*      */     }
/*      */ 
/*  143 */     int c = this.fCurrentEntity.ch[(this.fCurrentEntity.position++)];
/*  144 */     boolean external = false;
/*  145 */     if ((c == 10) || (((c == 13) || (c == 133) || (c == 8232)) && ((external = this.fCurrentEntity.isExternal()))))
/*      */     {
/*  147 */       this.fCurrentEntity.lineNumber += 1;
/*  148 */       this.fCurrentEntity.columnNumber = 1;
/*  149 */       if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
/*  150 */         invokeListeners(1);
/*  151 */         this.fCurrentEntity.ch[0] = ((char)c);
/*  152 */         load(1, false, false);
/*      */       }
/*  154 */       if ((c == 13) && (external)) {
/*  155 */         int cc = this.fCurrentEntity.ch[(this.fCurrentEntity.position++)];
/*  156 */         if ((cc != 10) && (cc != 133)) {
/*  157 */           this.fCurrentEntity.position -= 1;
/*      */         }
/*      */       }
/*  160 */       c = 10;
/*      */     }
/*      */ 
/*  164 */     this.fCurrentEntity.columnNumber += 1;
/*  165 */     return c;
/*      */   }
/*      */ 
/*      */   public String scanNmtoken()
/*      */     throws IOException
/*      */   {
/*  186 */     if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
/*  187 */       load(0, true, true);
/*      */     }
/*      */ 
/*  191 */     int offset = this.fCurrentEntity.position;
/*      */     while (true)
/*      */     {
/*  194 */       char ch = this.fCurrentEntity.ch[this.fCurrentEntity.position];
/*  195 */       if (XML11Char.isXML11Name(ch)) {
/*  196 */         if (++this.fCurrentEntity.position == this.fCurrentEntity.count) {
/*  197 */           int length = this.fCurrentEntity.position - offset;
/*  198 */           invokeListeners(length);
/*  199 */           if (length == this.fCurrentEntity.ch.length)
/*      */           {
/*  201 */             char[] tmp = new char[this.fCurrentEntity.ch.length << 1];
/*  202 */             System.arraycopy(this.fCurrentEntity.ch, offset, tmp, 0, length);
/*      */ 
/*  204 */             this.fCurrentEntity.ch = tmp;
/*      */           }
/*      */           else {
/*  207 */             System.arraycopy(this.fCurrentEntity.ch, offset, this.fCurrentEntity.ch, 0, length);
/*      */           }
/*      */ 
/*  210 */           offset = 0;
/*  211 */           if (load(length, false, false))
/*      */             break;
/*      */         }
/*      */       }
/*      */       else {
/*  216 */         if (!XML11Char.isXML11NameHighSurrogate(ch)) break;
/*  217 */         if (++this.fCurrentEntity.position == this.fCurrentEntity.count) {
/*  218 */           int length = this.fCurrentEntity.position - offset;
/*  219 */           invokeListeners(length);
/*  220 */           if (length == this.fCurrentEntity.ch.length)
/*      */           {
/*  222 */             char[] tmp = new char[this.fCurrentEntity.ch.length << 1];
/*  223 */             System.arraycopy(this.fCurrentEntity.ch, offset, tmp, 0, length);
/*      */ 
/*  225 */             this.fCurrentEntity.ch = tmp;
/*      */           }
/*      */           else {
/*  228 */             System.arraycopy(this.fCurrentEntity.ch, offset, this.fCurrentEntity.ch, 0, length);
/*      */           }
/*      */ 
/*  231 */           offset = 0;
/*  232 */           if (load(length, false, false)) {
/*  233 */             this.fCurrentEntity.startPosition -= 1;
/*  234 */             this.fCurrentEntity.position -= 1;
/*  235 */             break;
/*      */           }
/*      */         }
/*  238 */         char ch2 = this.fCurrentEntity.ch[this.fCurrentEntity.position];
/*  239 */         if ((!XMLChar.isLowSurrogate(ch2)) || (!XML11Char.isXML11Name(XMLChar.supplemental(ch, ch2))))
/*      */         {
/*  241 */           this.fCurrentEntity.position -= 1;
/*      */         }
/*  244 */         else if (++this.fCurrentEntity.position == this.fCurrentEntity.count) {
/*  245 */           int length = this.fCurrentEntity.position - offset;
/*  246 */           invokeListeners(length);
/*  247 */           if (length == this.fCurrentEntity.ch.length)
/*      */           {
/*  249 */             char[] tmp = new char[this.fCurrentEntity.ch.length << 1];
/*  250 */             System.arraycopy(this.fCurrentEntity.ch, offset, tmp, 0, length);
/*      */ 
/*  252 */             this.fCurrentEntity.ch = tmp;
/*      */           }
/*      */           else {
/*  255 */             System.arraycopy(this.fCurrentEntity.ch, offset, this.fCurrentEntity.ch, 0, length);
/*      */           }
/*      */ 
/*  258 */           offset = 0;
/*  259 */           if (load(length, false, false))
/*      */           {
/*      */             break;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  270 */     int length = this.fCurrentEntity.position - offset;
/*  271 */     this.fCurrentEntity.columnNumber += length;
/*      */ 
/*  274 */     String symbol = null;
/*  275 */     if (length > 0) {
/*  276 */       symbol = this.fSymbolTable.addSymbol(this.fCurrentEntity.ch, offset, length);
/*      */     }
/*  278 */     return symbol;
/*      */   }
/*      */ 
/*      */   public String scanName()
/*      */     throws IOException
/*      */   {
/*  300 */     if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
/*  301 */       load(0, true, true);
/*      */     }
/*      */ 
/*  305 */     int offset = this.fCurrentEntity.position;
/*  306 */     char ch = this.fCurrentEntity.ch[offset];
/*      */ 
/*  308 */     if (XML11Char.isXML11NameStart(ch)) {
/*  309 */       if (++this.fCurrentEntity.position == this.fCurrentEntity.count) {
/*  310 */         invokeListeners(1);
/*  311 */         this.fCurrentEntity.ch[0] = ch;
/*  312 */         offset = 0;
/*  313 */         if (load(1, false, false)) {
/*  314 */           this.fCurrentEntity.columnNumber += 1;
/*  315 */           String symbol = this.fSymbolTable.addSymbol(this.fCurrentEntity.ch, 0, 1);
/*  316 */           return symbol;
/*      */         }
/*      */       }
/*      */     }
/*  320 */     else if (XML11Char.isXML11NameHighSurrogate(ch)) {
/*  321 */       if (++this.fCurrentEntity.position == this.fCurrentEntity.count) {
/*  322 */         invokeListeners(1);
/*  323 */         this.fCurrentEntity.ch[0] = ch;
/*  324 */         offset = 0;
/*  325 */         if (load(1, false, false)) {
/*  326 */           this.fCurrentEntity.position -= 1;
/*  327 */           this.fCurrentEntity.startPosition -= 1;
/*  328 */           return null;
/*      */         }
/*      */       }
/*  331 */       char ch2 = this.fCurrentEntity.ch[this.fCurrentEntity.position];
/*  332 */       if ((!XMLChar.isLowSurrogate(ch2)) || (!XML11Char.isXML11NameStart(XMLChar.supplemental(ch, ch2))))
/*      */       {
/*  334 */         this.fCurrentEntity.position -= 1;
/*  335 */         return null;
/*      */       }
/*  337 */       if (++this.fCurrentEntity.position == this.fCurrentEntity.count) {
/*  338 */         invokeListeners(2);
/*  339 */         this.fCurrentEntity.ch[0] = ch;
/*  340 */         this.fCurrentEntity.ch[1] = ch2;
/*  341 */         offset = 0;
/*  342 */         if (load(2, false, false)) {
/*  343 */           this.fCurrentEntity.columnNumber += 2;
/*  344 */           String symbol = this.fSymbolTable.addSymbol(this.fCurrentEntity.ch, 0, 2);
/*  345 */           return symbol;
/*      */         }
/*      */       }
/*      */     }
/*      */     else {
/*  350 */       return null;
/*      */     }
/*      */     while (true)
/*      */     {
/*  354 */       ch = this.fCurrentEntity.ch[this.fCurrentEntity.position];
/*  355 */       if (XML11Char.isXML11Name(ch)) {
/*  356 */         if (++this.fCurrentEntity.position == this.fCurrentEntity.count) {
/*  357 */           int length = this.fCurrentEntity.position - offset;
/*  358 */           invokeListeners(length);
/*  359 */           if (length == this.fCurrentEntity.ch.length)
/*      */           {
/*  361 */             char[] tmp = new char[this.fCurrentEntity.ch.length << 1];
/*  362 */             System.arraycopy(this.fCurrentEntity.ch, offset, tmp, 0, length);
/*      */ 
/*  364 */             this.fCurrentEntity.ch = tmp;
/*      */           }
/*      */           else {
/*  367 */             System.arraycopy(this.fCurrentEntity.ch, offset, this.fCurrentEntity.ch, 0, length);
/*      */           }
/*      */ 
/*  370 */           offset = 0;
/*  371 */           if (load(length, false, false))
/*      */             break;
/*      */         }
/*      */       }
/*      */       else {
/*  376 */         if (!XML11Char.isXML11NameHighSurrogate(ch)) break;
/*  377 */         if (++this.fCurrentEntity.position == this.fCurrentEntity.count) {
/*  378 */           int length = this.fCurrentEntity.position - offset;
/*  379 */           invokeListeners(length);
/*  380 */           if (length == this.fCurrentEntity.ch.length)
/*      */           {
/*  382 */             char[] tmp = new char[this.fCurrentEntity.ch.length << 1];
/*  383 */             System.arraycopy(this.fCurrentEntity.ch, offset, tmp, 0, length);
/*      */ 
/*  385 */             this.fCurrentEntity.ch = tmp;
/*      */           }
/*      */           else {
/*  388 */             System.arraycopy(this.fCurrentEntity.ch, offset, this.fCurrentEntity.ch, 0, length);
/*      */           }
/*      */ 
/*  391 */           offset = 0;
/*  392 */           if (load(length, false, false)) {
/*  393 */             this.fCurrentEntity.position -= 1;
/*  394 */             this.fCurrentEntity.startPosition -= 1;
/*  395 */             break;
/*      */           }
/*      */         }
/*  398 */         char ch2 = this.fCurrentEntity.ch[this.fCurrentEntity.position];
/*  399 */         if ((!XMLChar.isLowSurrogate(ch2)) || (!XML11Char.isXML11Name(XMLChar.supplemental(ch, ch2))))
/*      */         {
/*  401 */           this.fCurrentEntity.position -= 1;
/*      */         }
/*  404 */         else if (++this.fCurrentEntity.position == this.fCurrentEntity.count) {
/*  405 */           int length = this.fCurrentEntity.position - offset;
/*  406 */           invokeListeners(length);
/*  407 */           if (length == this.fCurrentEntity.ch.length)
/*      */           {
/*  409 */             char[] tmp = new char[this.fCurrentEntity.ch.length << 1];
/*  410 */             System.arraycopy(this.fCurrentEntity.ch, offset, tmp, 0, length);
/*      */ 
/*  412 */             this.fCurrentEntity.ch = tmp;
/*      */           }
/*      */           else {
/*  415 */             System.arraycopy(this.fCurrentEntity.ch, offset, this.fCurrentEntity.ch, 0, length);
/*      */           }
/*      */ 
/*  418 */           offset = 0;
/*  419 */           if (load(length, false, false))
/*      */           {
/*      */             break;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  430 */     int length = this.fCurrentEntity.position - offset;
/*  431 */     this.fCurrentEntity.columnNumber += length;
/*      */ 
/*  434 */     String symbol = null;
/*  435 */     if (length > 0) {
/*  436 */       symbol = this.fSymbolTable.addSymbol(this.fCurrentEntity.ch, offset, length);
/*      */     }
/*  438 */     return symbol;
/*      */   }
/*      */ 
/*      */   public String scanNCName()
/*      */     throws IOException
/*      */   {
/*  461 */     if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
/*  462 */       load(0, true, true);
/*      */     }
/*      */ 
/*  466 */     int offset = this.fCurrentEntity.position;
/*  467 */     char ch = this.fCurrentEntity.ch[offset];
/*      */ 
/*  469 */     if (XML11Char.isXML11NCNameStart(ch)) {
/*  470 */       if (++this.fCurrentEntity.position == this.fCurrentEntity.count) {
/*  471 */         invokeListeners(1);
/*  472 */         this.fCurrentEntity.ch[0] = ch;
/*  473 */         offset = 0;
/*  474 */         if (load(1, false, false)) {
/*  475 */           this.fCurrentEntity.columnNumber += 1;
/*  476 */           String symbol = this.fSymbolTable.addSymbol(this.fCurrentEntity.ch, 0, 1);
/*  477 */           return symbol;
/*      */         }
/*      */       }
/*      */     }
/*  481 */     else if (XML11Char.isXML11NameHighSurrogate(ch)) {
/*  482 */       if (++this.fCurrentEntity.position == this.fCurrentEntity.count) {
/*  483 */         invokeListeners(1);
/*  484 */         this.fCurrentEntity.ch[0] = ch;
/*  485 */         offset = 0;
/*  486 */         if (load(1, false, false)) {
/*  487 */           this.fCurrentEntity.position -= 1;
/*  488 */           this.fCurrentEntity.startPosition -= 1;
/*  489 */           return null;
/*      */         }
/*      */       }
/*  492 */       char ch2 = this.fCurrentEntity.ch[this.fCurrentEntity.position];
/*  493 */       if ((!XMLChar.isLowSurrogate(ch2)) || (!XML11Char.isXML11NCNameStart(XMLChar.supplemental(ch, ch2))))
/*      */       {
/*  495 */         this.fCurrentEntity.position -= 1;
/*  496 */         return null;
/*      */       }
/*  498 */       if (++this.fCurrentEntity.position == this.fCurrentEntity.count) {
/*  499 */         invokeListeners(2);
/*  500 */         this.fCurrentEntity.ch[0] = ch;
/*  501 */         this.fCurrentEntity.ch[1] = ch2;
/*  502 */         offset = 0;
/*  503 */         if (load(2, false, false)) {
/*  504 */           this.fCurrentEntity.columnNumber += 2;
/*  505 */           String symbol = this.fSymbolTable.addSymbol(this.fCurrentEntity.ch, 0, 2);
/*  506 */           return symbol;
/*      */         }
/*      */       }
/*      */     }
/*      */     else {
/*  511 */       return null;
/*      */     }
/*      */     while (true)
/*      */     {
/*  515 */       ch = this.fCurrentEntity.ch[this.fCurrentEntity.position];
/*  516 */       if (XML11Char.isXML11NCName(ch)) {
/*  517 */         if (++this.fCurrentEntity.position == this.fCurrentEntity.count) {
/*  518 */           int length = this.fCurrentEntity.position - offset;
/*  519 */           invokeListeners(length);
/*  520 */           if (length == this.fCurrentEntity.ch.length)
/*      */           {
/*  522 */             char[] tmp = new char[this.fCurrentEntity.ch.length << 1];
/*  523 */             System.arraycopy(this.fCurrentEntity.ch, offset, tmp, 0, length);
/*      */ 
/*  525 */             this.fCurrentEntity.ch = tmp;
/*      */           }
/*      */           else {
/*  528 */             System.arraycopy(this.fCurrentEntity.ch, offset, this.fCurrentEntity.ch, 0, length);
/*      */           }
/*      */ 
/*  531 */           offset = 0;
/*  532 */           if (load(length, false, false))
/*      */             break;
/*      */         }
/*      */       }
/*      */       else {
/*  537 */         if (!XML11Char.isXML11NameHighSurrogate(ch)) break;
/*  538 */         if (++this.fCurrentEntity.position == this.fCurrentEntity.count) {
/*  539 */           int length = this.fCurrentEntity.position - offset;
/*  540 */           invokeListeners(length);
/*  541 */           if (length == this.fCurrentEntity.ch.length)
/*      */           {
/*  543 */             char[] tmp = new char[this.fCurrentEntity.ch.length << 1];
/*  544 */             System.arraycopy(this.fCurrentEntity.ch, offset, tmp, 0, length);
/*      */ 
/*  546 */             this.fCurrentEntity.ch = tmp;
/*      */           }
/*      */           else {
/*  549 */             System.arraycopy(this.fCurrentEntity.ch, offset, this.fCurrentEntity.ch, 0, length);
/*      */           }
/*      */ 
/*  552 */           offset = 0;
/*  553 */           if (load(length, false, false)) {
/*  554 */             this.fCurrentEntity.startPosition -= 1;
/*  555 */             this.fCurrentEntity.position -= 1;
/*  556 */             break;
/*      */           }
/*      */         }
/*  559 */         char ch2 = this.fCurrentEntity.ch[this.fCurrentEntity.position];
/*  560 */         if ((!XMLChar.isLowSurrogate(ch2)) || (!XML11Char.isXML11NCName(XMLChar.supplemental(ch, ch2))))
/*      */         {
/*  562 */           this.fCurrentEntity.position -= 1;
/*      */         }
/*  565 */         else if (++this.fCurrentEntity.position == this.fCurrentEntity.count) {
/*  566 */           int length = this.fCurrentEntity.position - offset;
/*  567 */           invokeListeners(length);
/*  568 */           if (length == this.fCurrentEntity.ch.length)
/*      */           {
/*  570 */             char[] tmp = new char[this.fCurrentEntity.ch.length << 1];
/*  571 */             System.arraycopy(this.fCurrentEntity.ch, offset, tmp, 0, length);
/*      */ 
/*  573 */             this.fCurrentEntity.ch = tmp;
/*      */           }
/*      */           else {
/*  576 */             System.arraycopy(this.fCurrentEntity.ch, offset, this.fCurrentEntity.ch, 0, length);
/*      */           }
/*      */ 
/*  579 */           offset = 0;
/*  580 */           if (load(length, false, false))
/*      */           {
/*      */             break;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  591 */     int length = this.fCurrentEntity.position - offset;
/*  592 */     this.fCurrentEntity.columnNumber += length;
/*      */ 
/*  595 */     String symbol = null;
/*  596 */     if (length > 0) {
/*  597 */       symbol = this.fSymbolTable.addSymbol(this.fCurrentEntity.ch, offset, length);
/*      */     }
/*  599 */     return symbol;
/*      */   }
/*      */ 
/*      */   public boolean scanQName(QName qname)
/*      */     throws IOException
/*      */   {
/*  628 */     if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
/*  629 */       load(0, true, true);
/*      */     }
/*      */ 
/*  633 */     int offset = this.fCurrentEntity.position;
/*  634 */     char ch = this.fCurrentEntity.ch[offset];
/*      */ 
/*  636 */     if (XML11Char.isXML11NCNameStart(ch)) {
/*  637 */       if (++this.fCurrentEntity.position == this.fCurrentEntity.count) {
/*  638 */         invokeListeners(1);
/*  639 */         this.fCurrentEntity.ch[0] = ch;
/*  640 */         offset = 0;
/*  641 */         if (load(1, false, false)) {
/*  642 */           this.fCurrentEntity.columnNumber += 1;
/*  643 */           String name = this.fSymbolTable.addSymbol(this.fCurrentEntity.ch, 0, 1);
/*  644 */           qname.setValues(null, name, name, null);
/*  645 */           return true;
/*      */         }
/*      */       }
/*      */     }
/*  649 */     else if (XML11Char.isXML11NameHighSurrogate(ch)) {
/*  650 */       if (++this.fCurrentEntity.position == this.fCurrentEntity.count) {
/*  651 */         invokeListeners(1);
/*  652 */         this.fCurrentEntity.ch[0] = ch;
/*  653 */         offset = 0;
/*  654 */         if (load(1, false, false)) {
/*  655 */           this.fCurrentEntity.startPosition -= 1;
/*  656 */           this.fCurrentEntity.position -= 1;
/*  657 */           return false;
/*      */         }
/*      */       }
/*  660 */       char ch2 = this.fCurrentEntity.ch[this.fCurrentEntity.position];
/*  661 */       if ((!XMLChar.isLowSurrogate(ch2)) || (!XML11Char.isXML11NCNameStart(XMLChar.supplemental(ch, ch2))))
/*      */       {
/*  663 */         this.fCurrentEntity.position -= 1;
/*  664 */         return false;
/*      */       }
/*  666 */       if (++this.fCurrentEntity.position == this.fCurrentEntity.count) {
/*  667 */         invokeListeners(2);
/*  668 */         this.fCurrentEntity.ch[0] = ch;
/*  669 */         this.fCurrentEntity.ch[1] = ch2;
/*  670 */         offset = 0;
/*  671 */         if (load(2, false, false)) {
/*  672 */           this.fCurrentEntity.columnNumber += 2;
/*  673 */           String name = this.fSymbolTable.addSymbol(this.fCurrentEntity.ch, 0, 2);
/*  674 */           qname.setValues(null, name, name, null);
/*  675 */           return true;
/*      */         }
/*      */       }
/*      */     }
/*      */     else {
/*  680 */       return false;
/*      */     }
/*      */ 
/*  683 */     int index = -1;
/*  684 */     boolean sawIncompleteSurrogatePair = false;
/*      */     while (true) {
/*  686 */       ch = this.fCurrentEntity.ch[this.fCurrentEntity.position];
/*  687 */       if (XML11Char.isXML11Name(ch)) {
/*  688 */         if (ch == ':') {
/*  689 */           if (index == -1)
/*      */           {
/*  692 */             index = this.fCurrentEntity.position;
/*      */           }
/*      */         } else { if (++this.fCurrentEntity.position != this.fCurrentEntity.count) continue;
/*  695 */           int length = this.fCurrentEntity.position - offset;
/*  696 */           invokeListeners(length);
/*  697 */           if (length == this.fCurrentEntity.ch.length)
/*      */           {
/*  699 */             char[] tmp = new char[this.fCurrentEntity.ch.length << 1];
/*  700 */             System.arraycopy(this.fCurrentEntity.ch, offset, tmp, 0, length);
/*      */ 
/*  702 */             this.fCurrentEntity.ch = tmp;
/*      */           }
/*      */           else {
/*  705 */             System.arraycopy(this.fCurrentEntity.ch, offset, this.fCurrentEntity.ch, 0, length);
/*      */           }
/*      */ 
/*  708 */           if (index != -1) {
/*  709 */             index -= offset;
/*      */           }
/*  711 */           offset = 0;
/*  712 */           if (load(length, false, false))
/*      */             break; }
/*      */       }
/*      */       else
/*      */       {
/*  717 */         if (!XML11Char.isXML11NameHighSurrogate(ch)) break;
/*  718 */         if (++this.fCurrentEntity.position == this.fCurrentEntity.count) {
/*  719 */           int length = this.fCurrentEntity.position - offset;
/*  720 */           invokeListeners(length);
/*  721 */           if (length == this.fCurrentEntity.ch.length)
/*      */           {
/*  723 */             char[] tmp = new char[this.fCurrentEntity.ch.length << 1];
/*  724 */             System.arraycopy(this.fCurrentEntity.ch, offset, tmp, 0, length);
/*      */ 
/*  726 */             this.fCurrentEntity.ch = tmp;
/*      */           }
/*      */           else {
/*  729 */             System.arraycopy(this.fCurrentEntity.ch, offset, this.fCurrentEntity.ch, 0, length);
/*      */           }
/*      */ 
/*  732 */           if (index != -1) {
/*  733 */             index -= offset;
/*      */           }
/*  735 */           offset = 0;
/*  736 */           if (load(length, false, false)) {
/*  737 */             sawIncompleteSurrogatePair = true;
/*  738 */             this.fCurrentEntity.startPosition -= 1;
/*  739 */             this.fCurrentEntity.position -= 1;
/*  740 */             break;
/*      */           }
/*      */         }
/*  743 */         char ch2 = this.fCurrentEntity.ch[this.fCurrentEntity.position];
/*  744 */         if ((!XMLChar.isLowSurrogate(ch2)) || (!XML11Char.isXML11Name(XMLChar.supplemental(ch, ch2))))
/*      */         {
/*  746 */           sawIncompleteSurrogatePair = true;
/*  747 */           this.fCurrentEntity.position -= 1;
/*      */         }
/*  750 */         else if (++this.fCurrentEntity.position == this.fCurrentEntity.count) {
/*  751 */           int length = this.fCurrentEntity.position - offset;
/*  752 */           invokeListeners(length);
/*  753 */           if (length == this.fCurrentEntity.ch.length)
/*      */           {
/*  755 */             char[] tmp = new char[this.fCurrentEntity.ch.length << 1];
/*  756 */             System.arraycopy(this.fCurrentEntity.ch, offset, tmp, 0, length);
/*      */ 
/*  758 */             this.fCurrentEntity.ch = tmp;
/*      */           }
/*      */           else {
/*  761 */             System.arraycopy(this.fCurrentEntity.ch, offset, this.fCurrentEntity.ch, 0, length);
/*      */           }
/*      */ 
/*  764 */           if (index != -1) {
/*  765 */             index -= offset;
/*      */           }
/*  767 */           offset = 0;
/*  768 */           if (load(length, false, false))
/*      */           {
/*      */             break;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  779 */     int length = this.fCurrentEntity.position - offset;
/*  780 */     this.fCurrentEntity.columnNumber += length;
/*      */ 
/*  782 */     if (length > 0) {
/*  783 */       String prefix = null;
/*  784 */       String localpart = null;
/*  785 */       String rawname = this.fSymbolTable.addSymbol(this.fCurrentEntity.ch, offset, length);
/*      */ 
/*  787 */       if (index != -1) {
/*  788 */         int prefixLength = index - offset;
/*  789 */         prefix = this.fSymbolTable.addSymbol(this.fCurrentEntity.ch, offset, prefixLength);
/*      */ 
/*  791 */         int len = length - prefixLength - 1;
/*  792 */         int startLocal = index + 1;
/*  793 */         if ((!XML11Char.isXML11NCNameStart(this.fCurrentEntity.ch[startLocal])) && ((!XML11Char.isXML11NameHighSurrogate(this.fCurrentEntity.ch[startLocal])) || (sawIncompleteSurrogatePair)))
/*      */         {
/*  796 */           this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "IllegalQName", null, (short)2);
/*      */         }
/*      */ 
/*  801 */         localpart = this.fSymbolTable.addSymbol(this.fCurrentEntity.ch, index + 1, len);
/*      */       }
/*      */       else
/*      */       {
/*  806 */         localpart = rawname;
/*      */       }
/*  808 */       qname.setValues(prefix, localpart, rawname, null);
/*  809 */       return true;
/*      */     }
/*  811 */     return false;
/*      */   }
/*      */ 
/*      */   public int scanContent(XMLString content)
/*      */     throws IOException
/*      */   {
/*  844 */     if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
/*  845 */       load(0, true, true);
/*      */     }
/*  847 */     else if (this.fCurrentEntity.position == this.fCurrentEntity.count - 1) {
/*  848 */       invokeListeners(0);
/*  849 */       this.fCurrentEntity.ch[0] = this.fCurrentEntity.ch[(this.fCurrentEntity.count - 1)];
/*  850 */       load(1, false, false);
/*  851 */       this.fCurrentEntity.position = 0;
/*  852 */       this.fCurrentEntity.startPosition = 0;
/*      */     }
/*      */ 
/*  856 */     int offset = this.fCurrentEntity.position;
/*  857 */     int c = this.fCurrentEntity.ch[offset];
/*  858 */     int newlines = 0;
/*  859 */     boolean external = this.fCurrentEntity.isExternal();
/*  860 */     if ((c == 10) || (((c == 13) || (c == 133) || (c == 8232)) && (external))) {
/*      */       do {
/*  862 */         c = this.fCurrentEntity.ch[(this.fCurrentEntity.position++)];
/*  863 */         if ((c == 13) && (external)) {
/*  864 */           newlines++;
/*  865 */           this.fCurrentEntity.lineNumber += 1;
/*  866 */           this.fCurrentEntity.columnNumber = 1;
/*  867 */           if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
/*  868 */             offset = 0;
/*  869 */             this.fCurrentEntity.baseCharOffset += this.fCurrentEntity.position - this.fCurrentEntity.startPosition;
/*  870 */             this.fCurrentEntity.position = newlines;
/*  871 */             this.fCurrentEntity.startPosition = newlines;
/*  872 */             if (load(newlines, false, true)) {
/*      */               break;
/*      */             }
/*      */           }
/*  876 */           int cc = this.fCurrentEntity.ch[this.fCurrentEntity.position];
/*  877 */           if ((cc == 10) || (cc == 133)) {
/*  878 */             this.fCurrentEntity.position += 1;
/*  879 */             offset++;
/*      */           }
/*      */           else
/*      */           {
/*  883 */             newlines++;
/*      */           }
/*      */         }
/*  886 */         else if ((c == 10) || (((c == 133) || (c == 8232)) && (external))) {
/*  887 */           newlines++;
/*  888 */           this.fCurrentEntity.lineNumber += 1;
/*  889 */           this.fCurrentEntity.columnNumber = 1;
/*  890 */           if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
/*  891 */             offset = 0;
/*  892 */             this.fCurrentEntity.baseCharOffset += this.fCurrentEntity.position - this.fCurrentEntity.startPosition;
/*  893 */             this.fCurrentEntity.position = newlines;
/*  894 */             this.fCurrentEntity.startPosition = newlines;
/*  895 */             if (load(newlines, false, true))
/*  896 */               break;
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/*  901 */           this.fCurrentEntity.position -= 1;
/*  902 */           break;
/*      */         }
/*      */       }
/*  904 */       while (this.fCurrentEntity.position < this.fCurrentEntity.count - 1);
/*  905 */       for (int i = offset; i < this.fCurrentEntity.position; i++) {
/*  906 */         this.fCurrentEntity.ch[i] = '\n';
/*      */       }
/*  908 */       int length = this.fCurrentEntity.position - offset;
/*  909 */       if (this.fCurrentEntity.position == this.fCurrentEntity.count - 1) {
/*  910 */         content.setValues(this.fCurrentEntity.ch, offset, length);
/*  911 */         return -1;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  916 */     if (external) {
/*      */       do { if (this.fCurrentEntity.position >= this.fCurrentEntity.count) break;
/*  918 */         c = this.fCurrentEntity.ch[(this.fCurrentEntity.position++)]; }
/*  919 */       while ((XML11Char.isXML11Content(c)) && (c != 133) && (c != 8232));
/*  920 */       this.fCurrentEntity.position -= 1;
/*      */     }
/*      */     else
/*      */     {
/*  926 */       while (this.fCurrentEntity.position < this.fCurrentEntity.count) {
/*  927 */         c = this.fCurrentEntity.ch[(this.fCurrentEntity.position++)];
/*      */ 
/*  929 */         if (!XML11Char.isXML11InternalEntityContent(c)) {
/*  930 */           this.fCurrentEntity.position -= 1;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  935 */     int length = this.fCurrentEntity.position - offset;
/*  936 */     this.fCurrentEntity.columnNumber += length - newlines;
/*  937 */     content.setValues(this.fCurrentEntity.ch, offset, length);
/*      */ 
/*  940 */     if (this.fCurrentEntity.position != this.fCurrentEntity.count) {
/*  941 */       c = this.fCurrentEntity.ch[this.fCurrentEntity.position];
/*      */ 
/*  944 */       if (((c == 13) || (c == 133) || (c == 8232)) && (external))
/*  945 */         c = 10;
/*      */     }
/*      */     else
/*      */     {
/*  949 */       c = -1;
/*      */     }
/*  951 */     return c;
/*      */   }
/*      */ 
/*      */   public int scanLiteral(int quote, XMLString content)
/*      */     throws IOException
/*      */   {
/*  986 */     if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
/*  987 */       load(0, true, true);
/*      */     }
/*  989 */     else if (this.fCurrentEntity.position == this.fCurrentEntity.count - 1) {
/*  990 */       invokeListeners(0);
/*  991 */       this.fCurrentEntity.ch[0] = this.fCurrentEntity.ch[(this.fCurrentEntity.count - 1)];
/*  992 */       load(1, false, false);
/*  993 */       this.fCurrentEntity.startPosition = 0;
/*  994 */       this.fCurrentEntity.position = 0;
/*      */     }
/*      */ 
/*  998 */     int offset = this.fCurrentEntity.position;
/*  999 */     int c = this.fCurrentEntity.ch[offset];
/* 1000 */     int newlines = 0;
/* 1001 */     boolean external = this.fCurrentEntity.isExternal();
/* 1002 */     if ((c == 10) || (((c == 13) || (c == 133) || (c == 8232)) && (external))) {
/*      */       do {
/* 1004 */         c = this.fCurrentEntity.ch[(this.fCurrentEntity.position++)];
/* 1005 */         if ((c == 13) && (external)) {
/* 1006 */           newlines++;
/* 1007 */           this.fCurrentEntity.lineNumber += 1;
/* 1008 */           this.fCurrentEntity.columnNumber = 1;
/* 1009 */           if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
/* 1010 */             offset = 0;
/* 1011 */             this.fCurrentEntity.baseCharOffset += this.fCurrentEntity.position - this.fCurrentEntity.startPosition;
/* 1012 */             this.fCurrentEntity.position = newlines;
/* 1013 */             this.fCurrentEntity.startPosition = newlines;
/* 1014 */             if (load(newlines, false, true)) {
/*      */               break;
/*      */             }
/*      */           }
/* 1018 */           int cc = this.fCurrentEntity.ch[this.fCurrentEntity.position];
/* 1019 */           if ((cc == 10) || (cc == 133)) {
/* 1020 */             this.fCurrentEntity.position += 1;
/* 1021 */             offset++;
/*      */           }
/*      */           else
/*      */           {
/* 1025 */             newlines++;
/*      */           }
/*      */         }
/* 1028 */         else if ((c == 10) || (((c == 133) || (c == 8232)) && (external))) {
/* 1029 */           newlines++;
/* 1030 */           this.fCurrentEntity.lineNumber += 1;
/* 1031 */           this.fCurrentEntity.columnNumber = 1;
/* 1032 */           if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
/* 1033 */             offset = 0;
/* 1034 */             this.fCurrentEntity.baseCharOffset += this.fCurrentEntity.position - this.fCurrentEntity.startPosition;
/* 1035 */             this.fCurrentEntity.position = newlines;
/* 1036 */             this.fCurrentEntity.startPosition = newlines;
/* 1037 */             if (load(newlines, false, true))
/* 1038 */               break;
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/* 1043 */           this.fCurrentEntity.position -= 1;
/* 1044 */           break;
/*      */         }
/*      */       }
/* 1046 */       while (this.fCurrentEntity.position < this.fCurrentEntity.count - 1);
/* 1047 */       for (int i = offset; i < this.fCurrentEntity.position; i++) {
/* 1048 */         this.fCurrentEntity.ch[i] = '\n';
/*      */       }
/* 1050 */       int length = this.fCurrentEntity.position - offset;
/* 1051 */       if (this.fCurrentEntity.position == this.fCurrentEntity.count - 1) {
/* 1052 */         content.setValues(this.fCurrentEntity.ch, offset, length);
/* 1053 */         return -1;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1058 */     if (external) {
/*      */       do { if (this.fCurrentEntity.position >= this.fCurrentEntity.count) break;
/* 1060 */         c = this.fCurrentEntity.ch[(this.fCurrentEntity.position++)]; }
/* 1061 */       while ((c != quote) && (c != 37) && (XML11Char.isXML11Content(c)) && (c != 133) && (c != 8232));
/*      */ 
/* 1063 */       this.fCurrentEntity.position -= 1;
/*      */     }
/*      */     else
/*      */     {
/* 1069 */       while (this.fCurrentEntity.position < this.fCurrentEntity.count) {
/* 1070 */         c = this.fCurrentEntity.ch[(this.fCurrentEntity.position++)];
/*      */ 
/* 1072 */         if (((c == quote) && (!this.fCurrentEntity.literal)) || (c == 37) || (!XML11Char.isXML11InternalEntityContent(c)))
/*      */         {
/* 1074 */           this.fCurrentEntity.position -= 1;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1079 */     int length = this.fCurrentEntity.position - offset;
/* 1080 */     this.fCurrentEntity.columnNumber += length - newlines;
/* 1081 */     content.setValues(this.fCurrentEntity.ch, offset, length);
/*      */ 
/* 1084 */     if (this.fCurrentEntity.position != this.fCurrentEntity.count) {
/* 1085 */       c = this.fCurrentEntity.ch[this.fCurrentEntity.position];
/*      */ 
/* 1089 */       if ((c == quote) && (this.fCurrentEntity.literal))
/* 1090 */         c = -1;
/*      */     }
/*      */     else
/*      */     {
/* 1094 */       c = -1;
/*      */     }
/* 1096 */     return c;
/*      */   }
/*      */ 
/*      */   public boolean scanData(String delimiter, XMLStringBuffer buffer)
/*      */     throws IOException
/*      */   {
/* 1133 */     boolean done = false;
/* 1134 */     int delimLen = delimiter.length();
/* 1135 */     char charAt0 = delimiter.charAt(0);
/* 1136 */     boolean external = this.fCurrentEntity.isExternal();
/*      */     label1386: 
/*      */     do
/*      */     {
/* 1139 */       if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
/* 1140 */         load(0, true, false);
/*      */       }
/*      */ 
/* 1143 */       boolean bNextEntity = false;
/*      */ 
/* 1146 */       while ((this.fCurrentEntity.position >= this.fCurrentEntity.count - delimLen) && (!bNextEntity))
/*      */       {
/* 1148 */         System.arraycopy(this.fCurrentEntity.ch, this.fCurrentEntity.position, this.fCurrentEntity.ch, 0, this.fCurrentEntity.count - this.fCurrentEntity.position);
/*      */ 
/* 1154 */         bNextEntity = load(this.fCurrentEntity.count - this.fCurrentEntity.position, false, false);
/* 1155 */         this.fCurrentEntity.position = 0;
/* 1156 */         this.fCurrentEntity.startPosition = 0;
/*      */       }
/*      */ 
/* 1159 */       if (this.fCurrentEntity.position >= this.fCurrentEntity.count - delimLen)
/*      */       {
/* 1161 */         int length = this.fCurrentEntity.count - this.fCurrentEntity.position;
/* 1162 */         buffer.append(this.fCurrentEntity.ch, this.fCurrentEntity.position, length);
/* 1163 */         this.fCurrentEntity.columnNumber += this.fCurrentEntity.count;
/* 1164 */         this.fCurrentEntity.baseCharOffset += this.fCurrentEntity.position - this.fCurrentEntity.startPosition;
/* 1165 */         this.fCurrentEntity.position = this.fCurrentEntity.count;
/* 1166 */         this.fCurrentEntity.startPosition = this.fCurrentEntity.count;
/* 1167 */         load(0, true, false);
/* 1168 */         return false;
/*      */       }
/*      */ 
/* 1172 */       int offset = this.fCurrentEntity.position;
/* 1173 */       int c = this.fCurrentEntity.ch[offset];
/* 1174 */       int newlines = 0;
/* 1175 */       if ((c == 10) || (((c == 13) || (c == 133) || (c == 8232)) && (external))) {
/*      */         do {
/* 1177 */           c = this.fCurrentEntity.ch[(this.fCurrentEntity.position++)];
/* 1178 */           if ((c == 13) && (external)) {
/* 1179 */             newlines++;
/* 1180 */             this.fCurrentEntity.lineNumber += 1;
/* 1181 */             this.fCurrentEntity.columnNumber = 1;
/* 1182 */             if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
/* 1183 */               offset = 0;
/* 1184 */               this.fCurrentEntity.baseCharOffset += this.fCurrentEntity.position - this.fCurrentEntity.startPosition;
/* 1185 */               this.fCurrentEntity.position = newlines;
/* 1186 */               this.fCurrentEntity.startPosition = newlines;
/* 1187 */               if (load(newlines, false, true)) {
/*      */                 break;
/*      */               }
/*      */             }
/* 1191 */             int cc = this.fCurrentEntity.ch[this.fCurrentEntity.position];
/* 1192 */             if ((cc == 10) || (cc == 133)) {
/* 1193 */               this.fCurrentEntity.position += 1;
/* 1194 */               offset++;
/*      */             }
/*      */             else
/*      */             {
/* 1198 */               newlines++;
/*      */             }
/*      */           }
/* 1201 */           else if ((c == 10) || (((c == 133) || (c == 8232)) && (external))) {
/* 1202 */             newlines++;
/* 1203 */             this.fCurrentEntity.lineNumber += 1;
/* 1204 */             this.fCurrentEntity.columnNumber = 1;
/* 1205 */             if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
/* 1206 */               offset = 0;
/* 1207 */               this.fCurrentEntity.baseCharOffset += this.fCurrentEntity.position - this.fCurrentEntity.startPosition;
/* 1208 */               this.fCurrentEntity.position = newlines;
/* 1209 */               this.fCurrentEntity.startPosition = newlines;
/* 1210 */               this.fCurrentEntity.count = newlines;
/* 1211 */               if (load(newlines, false, true))
/* 1212 */                 break;
/*      */             }
/*      */           }
/*      */           else
/*      */           {
/* 1217 */             this.fCurrentEntity.position -= 1;
/* 1218 */             break;
/*      */           }
/*      */         }
/* 1220 */         while (this.fCurrentEntity.position < this.fCurrentEntity.count - 1);
/* 1221 */         for (int i = offset; i < this.fCurrentEntity.position; i++) {
/* 1222 */           this.fCurrentEntity.ch[i] = '\n';
/*      */         }
/* 1224 */         int length = this.fCurrentEntity.position - offset;
/* 1225 */         if (this.fCurrentEntity.position == this.fCurrentEntity.count - 1) {
/* 1226 */           buffer.append(this.fCurrentEntity.ch, offset, length);
/* 1227 */           return true;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1232 */       if (external) {
/*      */         do { while (true) { if (this.fCurrentEntity.position >= this.fCurrentEntity.count) break label1386;
/* 1234 */             c = this.fCurrentEntity.ch[(this.fCurrentEntity.position++)];
/* 1235 */             if (c != charAt0)
/*      */               break;
/* 1237 */             int delimOffset = this.fCurrentEntity.position - 1;
/* 1238 */             for (int i = 1; i < delimLen; i++) {
/* 1239 */               if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
/* 1240 */                 this.fCurrentEntity.position -= i;
/* 1241 */                 break label1386;
/*      */               }
/* 1243 */               c = this.fCurrentEntity.ch[(this.fCurrentEntity.position++)];
/* 1244 */               if (delimiter.charAt(i) != c) {
/* 1245 */                 this.fCurrentEntity.position -= 1;
/* 1246 */                 break;
/*      */               }
/*      */             }
/* 1249 */             if (this.fCurrentEntity.position == delimOffset + delimLen) {
/* 1250 */               done = true;
/* 1251 */               break label1386;
/*      */             }
/*      */           }
/* 1254 */           if ((c == 10) || (c == 13) || (c == 133) || (c == 8232)) {
/* 1255 */             this.fCurrentEntity.position -= 1;
/* 1256 */             break;
/*      */           }
/*      */         }
/*      */ 
/* 1260 */         while (XML11Char.isXML11ValidLiteral(c));
/* 1261 */         this.fCurrentEntity.position -= 1;
/* 1262 */         int length = this.fCurrentEntity.position - offset;
/* 1263 */         this.fCurrentEntity.columnNumber += length - newlines;
/* 1264 */         buffer.append(this.fCurrentEntity.ch, offset, length);
/* 1265 */         return true;
/*      */       }
/*      */ 
/* 1270 */       while (this.fCurrentEntity.position < this.fCurrentEntity.count) {
/* 1271 */         c = this.fCurrentEntity.ch[(this.fCurrentEntity.position++)];
/* 1272 */         if (c == charAt0)
/*      */         {
/* 1274 */           int delimOffset = this.fCurrentEntity.position - 1;
/* 1275 */           for (int i = 1; i < delimLen; i++) {
/* 1276 */             if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
/* 1277 */               this.fCurrentEntity.position -= i;
/* 1278 */               break label1386;
/*      */             }
/* 1280 */             c = this.fCurrentEntity.ch[(this.fCurrentEntity.position++)];
/* 1281 */             if (delimiter.charAt(i) != c) {
/* 1282 */               this.fCurrentEntity.position -= 1;
/* 1283 */               break;
/*      */             }
/*      */           }
/* 1286 */           if (this.fCurrentEntity.position == delimOffset + delimLen) {
/* 1287 */             done = true;
/*      */           }
/*      */ 
/*      */         }
/* 1291 */         else if (c == 10) {
/* 1292 */           this.fCurrentEntity.position -= 1;
/*      */         }
/* 1297 */         else if (!XML11Char.isXML11Valid(c)) {
/* 1298 */           this.fCurrentEntity.position -= 1;
/* 1299 */           int length = this.fCurrentEntity.position - offset;
/* 1300 */           this.fCurrentEntity.columnNumber += length - newlines;
/* 1301 */           buffer.append(this.fCurrentEntity.ch, offset, length);
/* 1302 */           return true;
/*      */         }
/*      */       }
/*      */ 
/* 1306 */       int length = this.fCurrentEntity.position - offset;
/* 1307 */       this.fCurrentEntity.columnNumber += length - newlines;
/* 1308 */       if (done) {
/* 1309 */         length -= delimLen;
/*      */       }
/* 1311 */       buffer.append(this.fCurrentEntity.ch, offset, length);
/*      */     }
/*      */ 
/* 1314 */     while (!done);
/* 1315 */     return !done;
/*      */   }
/*      */ 
/*      */   public boolean skipChar(int c)
/*      */     throws IOException
/*      */   {
/* 1335 */     if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
/* 1336 */       load(0, true, true);
/*      */     }
/*      */ 
/* 1340 */     int cc = this.fCurrentEntity.ch[this.fCurrentEntity.position];
/* 1341 */     if (cc == c) {
/* 1342 */       this.fCurrentEntity.position += 1;
/* 1343 */       if (c == 10) {
/* 1344 */         this.fCurrentEntity.lineNumber += 1;
/* 1345 */         this.fCurrentEntity.columnNumber = 1;
/*      */       }
/*      */       else {
/* 1348 */         this.fCurrentEntity.columnNumber += 1;
/*      */       }
/* 1350 */       return true;
/*      */     }
/* 1352 */     if ((c == 10) && ((cc == 8232) || (cc == 133)) && (this.fCurrentEntity.isExternal())) {
/* 1353 */       this.fCurrentEntity.position += 1;
/* 1354 */       this.fCurrentEntity.lineNumber += 1;
/* 1355 */       this.fCurrentEntity.columnNumber = 1;
/* 1356 */       return true;
/*      */     }
/* 1358 */     if ((c == 10) && (cc == 13) && (this.fCurrentEntity.isExternal()))
/*      */     {
/* 1360 */       if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
/* 1361 */         invokeListeners(1);
/* 1362 */         this.fCurrentEntity.ch[0] = ((char)cc);
/* 1363 */         load(1, false, false);
/*      */       }
/* 1365 */       int ccc = this.fCurrentEntity.ch[(++this.fCurrentEntity.position)];
/* 1366 */       if ((ccc == 10) || (ccc == 133)) {
/* 1367 */         this.fCurrentEntity.position += 1;
/*      */       }
/* 1369 */       this.fCurrentEntity.lineNumber += 1;
/* 1370 */       this.fCurrentEntity.columnNumber = 1;
/* 1371 */       return true;
/*      */     }
/*      */ 
/* 1375 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean skipSpaces()
/*      */     throws IOException
/*      */   {
/* 1396 */     if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
/* 1397 */       load(0, true, true);
/*      */     }
/*      */ 
/* 1407 */     if (this.fCurrentEntity == null) {
/* 1408 */       return false;
/*      */     }
/*      */ 
/* 1412 */     int c = this.fCurrentEntity.ch[this.fCurrentEntity.position];
/*      */ 
/* 1415 */     if (this.fCurrentEntity.isExternal()) {
/* 1416 */       if (XML11Char.isXML11Space(c)) {
/*      */         do {
/* 1418 */           boolean entityChanged = false;
/*      */ 
/* 1420 */           if ((c == 10) || (c == 13) || (c == 133) || (c == 8232)) {
/* 1421 */             this.fCurrentEntity.lineNumber += 1;
/* 1422 */             this.fCurrentEntity.columnNumber = 1;
/* 1423 */             if (this.fCurrentEntity.position == this.fCurrentEntity.count - 1) {
/* 1424 */               invokeListeners(0);
/* 1425 */               this.fCurrentEntity.ch[0] = ((char)c);
/* 1426 */               entityChanged = load(1, true, false);
/* 1427 */               if (!entityChanged)
/*      */               {
/* 1430 */                 this.fCurrentEntity.startPosition = 0;
/* 1431 */                 this.fCurrentEntity.position = 0;
/* 1432 */               } else if (this.fCurrentEntity == null) {
/* 1433 */                 return true;
/*      */               }
/*      */             }
/*      */ 
/* 1437 */             if (c == 13)
/*      */             {
/* 1440 */               int cc = this.fCurrentEntity.ch[(++this.fCurrentEntity.position)];
/* 1441 */               if ((cc != 10) && (cc != 133))
/* 1442 */                 this.fCurrentEntity.position -= 1;
/*      */             }
/*      */           }
/*      */           else
/*      */           {
/* 1447 */             this.fCurrentEntity.columnNumber += 1;
/*      */           }
/*      */ 
/* 1450 */           if (!entityChanged)
/* 1451 */             this.fCurrentEntity.position += 1;
/* 1452 */           if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
/* 1453 */             load(0, true, true);
/*      */ 
/* 1455 */             if (this.fCurrentEntity == null) {
/* 1456 */               return true;
/*      */             }
/*      */           }
/*      */         }
/* 1460 */         while (XML11Char.isXML11Space(c = this.fCurrentEntity.ch[this.fCurrentEntity.position]));
/* 1461 */         return true;
/*      */       }
/*      */ 
/*      */     }
/* 1465 */     else if (XMLChar.isSpace(c)) {
/*      */       do {
/* 1467 */         boolean entityChanged = false;
/*      */ 
/* 1469 */         if (c == 10) {
/* 1470 */           this.fCurrentEntity.lineNumber += 1;
/* 1471 */           this.fCurrentEntity.columnNumber = 1;
/* 1472 */           if (this.fCurrentEntity.position == this.fCurrentEntity.count - 1) {
/* 1473 */             this.fCurrentEntity.ch[0] = ((char)c);
/* 1474 */             entityChanged = load(1, true, true);
/* 1475 */             if (!entityChanged)
/*      */             {
/* 1478 */               this.fCurrentEntity.startPosition = 0;
/* 1479 */               this.fCurrentEntity.position = 0;
/* 1480 */             } else if (this.fCurrentEntity == null) {
/* 1481 */               return true;
/*      */             }
/*      */           }
/*      */         }
/*      */         else {
/* 1486 */           this.fCurrentEntity.columnNumber += 1;
/*      */         }
/*      */ 
/* 1489 */         if (!entityChanged)
/* 1490 */           this.fCurrentEntity.position += 1;
/* 1491 */         if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
/* 1492 */           load(0, true, true);
/*      */ 
/* 1494 */           if (this.fCurrentEntity == null) {
/* 1495 */             return true;
/*      */           }
/*      */         }
/*      */       }
/* 1499 */       while (XMLChar.isSpace(c = this.fCurrentEntity.ch[this.fCurrentEntity.position]));
/* 1500 */       return true;
/*      */     }
/*      */ 
/* 1504 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean skipString(String s)
/*      */     throws IOException
/*      */   {
/* 1524 */     if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
/* 1525 */       load(0, true, true);
/*      */     }
/*      */ 
/* 1529 */     int length = s.length();
/* 1530 */     for (int i = 0; i < length; i++) {
/* 1531 */       char c = this.fCurrentEntity.ch[(this.fCurrentEntity.position++)];
/* 1532 */       if (c != s.charAt(i)) {
/* 1533 */         this.fCurrentEntity.position -= i + 1;
/* 1534 */         return false;
/*      */       }
/* 1536 */       if ((i < length - 1) && (this.fCurrentEntity.position == this.fCurrentEntity.count)) {
/* 1537 */         invokeListeners(0);
/* 1538 */         System.arraycopy(this.fCurrentEntity.ch, this.fCurrentEntity.count - i - 1, this.fCurrentEntity.ch, 0, i + 1);
/*      */ 
/* 1541 */         if (load(i + 1, false, false)) {
/* 1542 */           this.fCurrentEntity.startPosition -= i + 1;
/* 1543 */           this.fCurrentEntity.position -= i + 1;
/* 1544 */           return false;
/*      */         }
/*      */       }
/*      */     }
/* 1548 */     this.fCurrentEntity.columnNumber += length;
/* 1549 */     return true;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.XML11EntityScanner
 * JD-Core Version:    0.6.2
 */