/*      */ package com.sun.xml.internal.fastinfoset.dom;
/*      */ 
/*      */ import com.sun.xml.internal.fastinfoset.CommonResourceBundle;
/*      */ import com.sun.xml.internal.fastinfoset.Decoder;
/*      */ import com.sun.xml.internal.fastinfoset.DecoderStateTables;
/*      */ import com.sun.xml.internal.fastinfoset.QualifiedName;
/*      */ import com.sun.xml.internal.fastinfoset.algorithm.BuiltInEncodingAlgorithm;
/*      */ import com.sun.xml.internal.fastinfoset.algorithm.BuiltInEncodingAlgorithmFactory;
/*      */ import com.sun.xml.internal.fastinfoset.util.CharArray;
/*      */ import com.sun.xml.internal.fastinfoset.util.CharArrayArray;
/*      */ import com.sun.xml.internal.fastinfoset.util.CharArrayString;
/*      */ import com.sun.xml.internal.fastinfoset.util.ContiguousCharArrayArray;
/*      */ import com.sun.xml.internal.fastinfoset.util.DuplicateAttributeVerifier;
/*      */ import com.sun.xml.internal.fastinfoset.util.PrefixArray;
/*      */ import com.sun.xml.internal.fastinfoset.util.QualifiedNameArray;
/*      */ import com.sun.xml.internal.fastinfoset.util.StringArray;
/*      */ import com.sun.xml.internal.fastinfoset.vocab.ParserVocabulary;
/*      */ import com.sun.xml.internal.org.jvnet.fastinfoset.EncodingAlgorithm;
/*      */ import com.sun.xml.internal.org.jvnet.fastinfoset.EncodingAlgorithmException;
/*      */ import com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetException;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import org.w3c.dom.Attr;
/*      */ import org.w3c.dom.Document;
/*      */ import org.w3c.dom.Element;
/*      */ import org.w3c.dom.Node;
/*      */ import org.w3c.dom.Text;
/*      */ 
/*      */ public class DOMDocumentParser extends Decoder
/*      */ {
/*      */   protected Document _document;
/*      */   protected Node _currentNode;
/*      */   protected Element _currentElement;
/*   65 */   protected Attr[] _namespaceAttributes = new Attr[16];
/*      */   protected int _namespaceAttributesIndex;
/*   69 */   protected int[] _namespacePrefixes = new int[16];
/*      */   protected int _namespacePrefixesIndex;
/*      */ 
/*      */   public void parse(Document d, InputStream s)
/*      */     throws FastInfosetException, IOException
/*      */   {
/*   83 */     this._currentNode = (this._document = d);
/*   84 */     this._namespaceAttributesIndex = 0;
/*      */ 
/*   86 */     parse(s);
/*      */   }
/*      */ 
/*      */   protected final void parse(InputStream s) throws FastInfosetException, IOException {
/*   90 */     setInputStream(s);
/*   91 */     parse();
/*      */   }
/*      */ 
/*      */   protected void resetOnError() {
/*   95 */     this._namespacePrefixesIndex = 0;
/*      */ 
/*   97 */     if (this._v == null) {
/*   98 */       this._prefixTable.clearCompletely();
/*      */     }
/*  100 */     this._duplicateAttributeVerifier.clear();
/*      */   }
/*      */ 
/*      */   protected final void parse() throws FastInfosetException, IOException {
/*      */     try {
/*  105 */       reset();
/*  106 */       decodeHeader();
/*  107 */       processDII();
/*      */     } catch (RuntimeException e) {
/*  109 */       resetOnError();
/*      */ 
/*  111 */       throw new FastInfosetException(e);
/*      */     } catch (FastInfosetException e) {
/*  113 */       resetOnError();
/*  114 */       throw e;
/*      */     } catch (IOException e) {
/*  116 */       resetOnError();
/*  117 */       throw e;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void processDII() throws FastInfosetException, IOException {
/*  122 */     this._b = read();
/*  123 */     if (this._b > 0) {
/*  124 */       processDIIOptionalProperties();
/*      */     }
/*      */ 
/*  128 */     boolean firstElementHasOccured = false;
/*  129 */     boolean documentTypeDeclarationOccured = false;
/*  130 */     while ((!this._terminate) || (!firstElementHasOccured)) {
/*  131 */       this._b = read();
/*  132 */       switch (DecoderStateTables.DII(this._b)) {
/*      */       case 0:
/*  134 */         processEII(this._elementNameTable._array[this._b], false);
/*  135 */         firstElementHasOccured = true;
/*  136 */         break;
/*      */       case 1:
/*  138 */         processEII(this._elementNameTable._array[(this._b & 0x1F)], true);
/*  139 */         firstElementHasOccured = true;
/*  140 */         break;
/*      */       case 2:
/*  142 */         processEII(decodeEIIIndexMedium(), (this._b & 0x40) > 0);
/*  143 */         firstElementHasOccured = true;
/*  144 */         break;
/*      */       case 3:
/*  146 */         processEII(decodeEIIIndexLarge(), (this._b & 0x40) > 0);
/*  147 */         firstElementHasOccured = true;
/*  148 */         break;
/*      */       case 5:
/*  151 */         QualifiedName qn = processLiteralQualifiedName(this._b & 0x3, this._elementNameTable.getNext());
/*      */ 
/*  154 */         this._elementNameTable.add(qn);
/*  155 */         processEII(qn, (this._b & 0x40) > 0);
/*  156 */         firstElementHasOccured = true;
/*  157 */         break;
/*      */       case 4:
/*  160 */         processEIIWithNamespaces();
/*  161 */         firstElementHasOccured = true;
/*  162 */         break;
/*      */       case 20:
/*  165 */         if (documentTypeDeclarationOccured) {
/*  166 */           throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.secondOccurenceOfDTDII"));
/*      */         }
/*  168 */         documentTypeDeclarationOccured = true;
/*      */ 
/*  170 */         String system_identifier = (this._b & 0x2) > 0 ? decodeIdentifyingNonEmptyStringOnFirstBit(this._v.otherURI) : null;
/*      */ 
/*  172 */         String public_identifier = (this._b & 0x1) > 0 ? decodeIdentifyingNonEmptyStringOnFirstBit(this._v.otherURI) : null;
/*      */ 
/*  175 */         this._b = read();
/*  176 */         while (this._b == 225) {
/*  177 */           switch (decodeNonIdentifyingStringOnFirstBit()) {
/*      */           case 0:
/*  179 */             if (this._addToTable)
/*  180 */               this._v.otherString.add(new CharArray(this._charBuffer, 0, this._charBufferLength, true)); break;
/*      */           case 2:
/*  184 */             throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.processingIIWithEncodingAlgorithm"));
/*      */           case 1:
/*  186 */             break;
/*      */           case 3:
/*      */           }
/*      */ 
/*  190 */           this._b = read();
/*      */         }
/*  192 */         if ((this._b & 0xF0) != 240) {
/*  193 */           throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.processingInstructionIIsNotTerminatedCorrectly"));
/*      */         }
/*  195 */         if (this._b == 255) {
/*  196 */           this._terminate = true;
/*      */         }
/*      */ 
/*  199 */         this._notations.clear();
/*  200 */         this._unparsedEntities.clear();
/*      */ 
/*  205 */         break;
/*      */       case 18:
/*  208 */         processCommentII();
/*  209 */         break;
/*      */       case 19:
/*  211 */         processProcessingII();
/*  212 */         break;
/*      */       case 23:
/*  214 */         this._doubleTerminate = true;
/*      */       case 22:
/*  216 */         this._terminate = true;
/*  217 */         break;
/*      */       case 6:
/*      */       case 7:
/*      */       case 8:
/*      */       case 9:
/*      */       case 10:
/*      */       case 11:
/*      */       case 12:
/*      */       case 13:
/*      */       case 14:
/*      */       case 15:
/*      */       case 16:
/*      */       case 17:
/*      */       case 21:
/*      */       default:
/*  219 */         throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.IllegalStateDecodingDII"));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  224 */     while (!this._terminate) {
/*  225 */       this._b = read();
/*  226 */       switch (DecoderStateTables.DII(this._b)) {
/*      */       case 18:
/*  228 */         processCommentII();
/*  229 */         break;
/*      */       case 19:
/*  231 */         processProcessingII();
/*  232 */         break;
/*      */       case 23:
/*  234 */         this._doubleTerminate = true;
/*      */       case 22:
/*  236 */         this._terminate = true;
/*  237 */         break;
/*      */       case 20:
/*      */       case 21:
/*      */       default:
/*  239 */         throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.IllegalStateDecodingDII"));
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void processDIIOptionalProperties()
/*      */     throws FastInfosetException, IOException
/*      */   {
/*  247 */     if (this._b == 32) {
/*  248 */       decodeInitialVocabulary();
/*  249 */       return;
/*      */     }
/*      */ 
/*  252 */     if ((this._b & 0x40) > 0) {
/*  253 */       decodeAdditionalData();
/*      */     }
/*      */ 
/*  260 */     if ((this._b & 0x20) > 0) {
/*  261 */       decodeInitialVocabulary();
/*      */     }
/*      */ 
/*  264 */     if ((this._b & 0x10) > 0) {
/*  265 */       decodeNotations();
/*      */     }
/*      */ 
/*  269 */     if ((this._b & 0x8) > 0)
/*  270 */       decodeUnparsedEntities();
/*      */     String version;
/*  274 */     if ((this._b & 0x4) > 0)
/*  275 */       version = decodeCharacterEncodingScheme();
/*      */     boolean standalone;
/*  282 */     if ((this._b & 0x2) > 0) {
/*  283 */       standalone = read() > 0;
/*      */     }
/*      */ 
/*  290 */     if ((this._b & 0x1) > 0)
/*  291 */       decodeVersion();
/*      */   }
/*      */ 
/*      */   protected final void processEII(QualifiedName name, boolean hasAttributes)
/*      */     throws FastInfosetException, IOException
/*      */   {
/*  300 */     if (this._prefixTable._currentInScope[name.prefixIndex] != name.namespaceNameIndex) {
/*  301 */       throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.qnameOfEIINotInScope"));
/*      */     }
/*      */ 
/*  304 */     Node parentCurrentNode = this._currentNode;
/*      */ 
/*  306 */     this._currentNode = (this._currentElement = createElement(name.namespaceName, name.qName, name.localName));
/*      */ 
/*  308 */     if (this._namespaceAttributesIndex > 0) {
/*  309 */       for (int i = 0; i < this._namespaceAttributesIndex; i++) {
/*  310 */         this._currentElement.setAttributeNode(this._namespaceAttributes[i]);
/*  311 */         this._namespaceAttributes[i] = null;
/*      */       }
/*  313 */       this._namespaceAttributesIndex = 0;
/*      */     }
/*      */ 
/*  316 */     if (hasAttributes) {
/*  317 */       processAIIs();
/*      */     }
/*      */ 
/*  320 */     parentCurrentNode.appendChild(this._currentElement);
/*      */ 
/*  322 */     while (!this._terminate) {
/*  323 */       this._b = read();
/*      */       String public_identifier;
/*  324 */       switch (DecoderStateTables.EII(this._b)) {
/*      */       case 0:
/*  326 */         processEII(this._elementNameTable._array[this._b], false);
/*  327 */         break;
/*      */       case 1:
/*  329 */         processEII(this._elementNameTable._array[(this._b & 0x1F)], true);
/*  330 */         break;
/*      */       case 2:
/*  332 */         processEII(decodeEIIIndexMedium(), (this._b & 0x40) > 0);
/*  333 */         break;
/*      */       case 3:
/*  335 */         processEII(decodeEIIIndexLarge(), (this._b & 0x40) > 0);
/*  336 */         break;
/*      */       case 5:
/*  339 */         QualifiedName qn = processLiteralQualifiedName(this._b & 0x3, this._elementNameTable.getNext());
/*      */ 
/*  342 */         this._elementNameTable.add(qn);
/*  343 */         processEII(qn, (this._b & 0x40) > 0);
/*  344 */         break;
/*      */       case 4:
/*  347 */         processEIIWithNamespaces();
/*  348 */         break;
/*      */       case 6:
/*  351 */         this._octetBufferLength = ((this._b & 0x1) + 1);
/*      */ 
/*  353 */         appendOrCreateTextData(processUtf8CharacterString());
/*  354 */         break;
/*      */       case 7:
/*  358 */         this._octetBufferLength = (read() + 3);
/*  359 */         appendOrCreateTextData(processUtf8CharacterString());
/*  360 */         break;
/*      */       case 8:
/*  364 */         this._octetBufferLength = (read() << 24 | read() << 16 | read() << 8 | read());
/*      */ 
/*  368 */         this._octetBufferLength += 259;
/*  369 */         appendOrCreateTextData(processUtf8CharacterString());
/*  370 */         break;
/*      */       case 9:
/*  374 */         this._octetBufferLength = ((this._b & 0x1) + 1);
/*      */ 
/*  376 */         String v = decodeUtf16StringAsString();
/*  377 */         if ((this._b & 0x10) > 0) {
/*  378 */           this._characterContentChunkTable.add(this._charBuffer, this._charBufferLength);
/*      */         }
/*      */ 
/*  381 */         appendOrCreateTextData(v);
/*  382 */         break;
/*      */       case 10:
/*  386 */         this._octetBufferLength = (read() + 3);
/*  387 */         String v = decodeUtf16StringAsString();
/*  388 */         if ((this._b & 0x10) > 0) {
/*  389 */           this._characterContentChunkTable.add(this._charBuffer, this._charBufferLength);
/*      */         }
/*      */ 
/*  392 */         appendOrCreateTextData(v);
/*  393 */         break;
/*      */       case 11:
/*  397 */         this._octetBufferLength = (read() << 24 | read() << 16 | read() << 8 | read());
/*      */ 
/*  401 */         this._octetBufferLength += 259;
/*  402 */         String v = decodeUtf16StringAsString();
/*  403 */         if ((this._b & 0x10) > 0) {
/*  404 */           this._characterContentChunkTable.add(this._charBuffer, this._charBufferLength);
/*      */         }
/*      */ 
/*  407 */         appendOrCreateTextData(v);
/*  408 */         break;
/*      */       case 12:
/*  412 */         boolean addToTable = (this._b & 0x10) > 0;
/*      */ 
/*  415 */         this._identifier = ((this._b & 0x2) << 6);
/*  416 */         this._b = read();
/*  417 */         this._identifier |= (this._b & 0xFC) >> 2;
/*      */ 
/*  419 */         decodeOctetsOnSeventhBitOfNonIdentifyingStringOnThirdBit(this._b);
/*      */ 
/*  421 */         String v = decodeRestrictedAlphabetAsString();
/*  422 */         if (addToTable) {
/*  423 */           this._characterContentChunkTable.add(this._charBuffer, this._charBufferLength);
/*      */         }
/*      */ 
/*  426 */         appendOrCreateTextData(v);
/*  427 */         break;
/*      */       case 13:
/*  431 */         boolean addToTable = (this._b & 0x10) > 0;
/*      */ 
/*  433 */         this._identifier = ((this._b & 0x2) << 6);
/*  434 */         this._b = read();
/*  435 */         this._identifier |= (this._b & 0xFC) >> 2;
/*      */ 
/*  437 */         decodeOctetsOnSeventhBitOfNonIdentifyingStringOnThirdBit(this._b);
/*  438 */         String s = convertEncodingAlgorithmDataToCharacters(false);
/*  439 */         if (addToTable) {
/*  440 */           this._characterContentChunkTable.add(s.toCharArray(), s.length());
/*      */         }
/*  442 */         appendOrCreateTextData(s);
/*  443 */         break;
/*      */       case 14:
/*  447 */         String s = this._characterContentChunkTable.getString(this._b & 0xF);
/*      */ 
/*  449 */         appendOrCreateTextData(s);
/*  450 */         break;
/*      */       case 15:
/*  454 */         int index = ((this._b & 0x3) << 8 | read()) + 16;
/*      */ 
/*  456 */         String s = this._characterContentChunkTable.getString(index);
/*      */ 
/*  458 */         appendOrCreateTextData(s);
/*  459 */         break;
/*      */       case 16:
/*  463 */         int index = (this._b & 0x3) << 16 | read() << 8 | read();
/*      */ 
/*  466 */         index += 1040;
/*  467 */         String s = this._characterContentChunkTable.getString(index);
/*      */ 
/*  469 */         appendOrCreateTextData(s);
/*  470 */         break;
/*      */       case 17:
/*  474 */         int index = read() << 16 | read() << 8 | read();
/*      */ 
/*  477 */         index += 263184;
/*  478 */         String s = this._characterContentChunkTable.getString(index);
/*      */ 
/*  480 */         appendOrCreateTextData(s);
/*  481 */         break;
/*      */       case 18:
/*  484 */         processCommentII();
/*  485 */         break;
/*      */       case 19:
/*  487 */         processProcessingII();
/*  488 */         break;
/*      */       case 21:
/*  491 */         String entity_reference_name = decodeIdentifyingNonEmptyStringOnFirstBit(this._v.otherNCName);
/*      */ 
/*  493 */         String system_identifier = (this._b & 0x2) > 0 ? decodeIdentifyingNonEmptyStringOnFirstBit(this._v.otherURI) : null;
/*      */ 
/*  495 */         public_identifier = (this._b & 0x1) > 0 ? decodeIdentifyingNonEmptyStringOnFirstBit(this._v.otherURI) : null;
/*      */ 
/*  499 */         break;
/*      */       case 23:
/*  502 */         this._doubleTerminate = true;
/*      */       case 22:
/*  504 */         this._terminate = true;
/*  505 */         break;
/*      */       case 20:
/*      */       default:
/*  507 */         throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.IllegalStateDecodingEII"));
/*      */       }
/*      */     }
/*      */ 
/*  511 */     this._terminate = this._doubleTerminate;
/*  512 */     this._doubleTerminate = false;
/*      */ 
/*  514 */     this._currentNode = parentCurrentNode;
/*      */   }
/*      */ 
/*      */   private void appendOrCreateTextData(String textData) {
/*  518 */     Node lastChild = this._currentNode.getLastChild();
/*  519 */     if ((lastChild instanceof Text))
/*  520 */       ((Text)lastChild).appendData(textData);
/*      */     else
/*  522 */       this._currentNode.appendChild(this._document.createTextNode(textData));
/*      */   }
/*      */ 
/*      */   private final String processUtf8CharacterString()
/*      */     throws FastInfosetException, IOException
/*      */   {
/*  528 */     if ((this._b & 0x10) > 0) {
/*  529 */       this._characterContentChunkTable.ensureSize(this._octetBufferLength);
/*  530 */       int charactersOffset = this._characterContentChunkTable._arrayIndex;
/*  531 */       decodeUtf8StringAsCharBuffer(this._characterContentChunkTable._array, charactersOffset);
/*  532 */       this._characterContentChunkTable.add(this._charBufferLength);
/*  533 */       return this._characterContentChunkTable.getString(this._characterContentChunkTable._cachedIndex);
/*      */     }
/*  535 */     decodeUtf8StringAsCharBuffer();
/*  536 */     return new String(this._charBuffer, 0, this._charBufferLength);
/*      */   }
/*      */ 
/*      */   protected final void processEIIWithNamespaces() throws FastInfosetException, IOException
/*      */   {
/*  541 */     boolean hasAttributes = (this._b & 0x40) > 0;
/*      */ 
/*  543 */     if (++this._prefixTable._declarationId == 2147483647) {
/*  544 */       this._prefixTable.clearDeclarationIds();
/*      */     }
/*      */ 
/*  548 */     Attr a = null;
/*  549 */     int start = this._namespacePrefixesIndex;
/*  550 */     int b = read();
/*  551 */     while ((b & 0xFC) == 204) {
/*  552 */       if (this._namespaceAttributesIndex == this._namespaceAttributes.length) {
/*  553 */         Attr[] newNamespaceAttributes = new Attr[this._namespaceAttributesIndex * 3 / 2 + 1];
/*  554 */         System.arraycopy(this._namespaceAttributes, 0, newNamespaceAttributes, 0, this._namespaceAttributesIndex);
/*  555 */         this._namespaceAttributes = newNamespaceAttributes;
/*      */       }
/*      */ 
/*  558 */       if (this._namespacePrefixesIndex == this._namespacePrefixes.length) {
/*  559 */         int[] namespaceAIIs = new int[this._namespacePrefixesIndex * 3 / 2 + 1];
/*  560 */         System.arraycopy(this._namespacePrefixes, 0, namespaceAIIs, 0, this._namespacePrefixesIndex);
/*  561 */         this._namespacePrefixes = namespaceAIIs;
/*      */       }
/*      */       String prefix;
/*  565 */       switch (b & 0x3)
/*      */       {
/*      */       case 0:
/*  569 */         a = createAttribute("http://www.w3.org/2000/xmlns/", "xmlns", "xmlns");
/*      */ 
/*  573 */         a.setValue("");
/*      */ 
/*  575 */         this._prefixIndex = (this._namespaceNameIndex = this._namespacePrefixes[(this._namespacePrefixesIndex++)] = -1);
/*  576 */         break;
/*      */       case 1:
/*  580 */         a = createAttribute("http://www.w3.org/2000/xmlns/", "xmlns", "xmlns");
/*      */ 
/*  584 */         a.setValue(decodeIdentifyingNonEmptyStringOnFirstBitAsNamespaceName(false));
/*      */ 
/*  586 */         this._prefixIndex = (this._namespacePrefixes[(this._namespacePrefixesIndex++)] = -1);
/*  587 */         break;
/*      */       case 2:
/*  591 */         prefix = decodeIdentifyingNonEmptyStringOnFirstBitAsPrefix(false);
/*  592 */         a = createAttribute("http://www.w3.org/2000/xmlns/", createQualifiedNameString(prefix), prefix);
/*      */ 
/*  596 */         a.setValue("");
/*      */ 
/*  598 */         this._namespaceNameIndex = -1;
/*  599 */         this._namespacePrefixes[(this._namespacePrefixesIndex++)] = this._prefixIndex;
/*  600 */         break;
/*      */       case 3:
/*  604 */         prefix = decodeIdentifyingNonEmptyStringOnFirstBitAsPrefix(true);
/*  605 */         a = createAttribute("http://www.w3.org/2000/xmlns/", createQualifiedNameString(prefix), prefix);
/*      */ 
/*  609 */         a.setValue(decodeIdentifyingNonEmptyStringOnFirstBitAsNamespaceName(true));
/*      */ 
/*  611 */         this._namespacePrefixes[(this._namespacePrefixesIndex++)] = this._prefixIndex;
/*      */       }
/*      */ 
/*  615 */       this._prefixTable.pushScope(this._prefixIndex, this._namespaceNameIndex);
/*      */ 
/*  617 */       this._namespaceAttributes[(this._namespaceAttributesIndex++)] = a;
/*      */ 
/*  619 */       b = read();
/*      */     }
/*  621 */     if (b != 240) {
/*  622 */       throw new IOException(CommonResourceBundle.getInstance().getString("message.EIInamespaceNameNotTerminatedCorrectly"));
/*      */     }
/*  624 */     int end = this._namespacePrefixesIndex;
/*      */ 
/*  626 */     this._b = read();
/*  627 */     switch (DecoderStateTables.EII(this._b)) {
/*      */     case 0:
/*  629 */       processEII(this._elementNameTable._array[this._b], hasAttributes);
/*  630 */       break;
/*      */     case 2:
/*  632 */       processEII(decodeEIIIndexMedium(), hasAttributes);
/*  633 */       break;
/*      */     case 3:
/*  635 */       processEII(decodeEIIIndexLarge(), hasAttributes);
/*  636 */       break;
/*      */     case 5:
/*  639 */       QualifiedName qn = processLiteralQualifiedName(this._b & 0x3, this._elementNameTable.getNext());
/*      */ 
/*  642 */       this._elementNameTable.add(qn);
/*  643 */       processEII(qn, hasAttributes);
/*  644 */       break;
/*      */     case 1:
/*      */     case 4:
/*      */     default:
/*  647 */       throw new IOException(CommonResourceBundle.getInstance().getString("message.IllegalStateDecodingEIIAfterAIIs"));
/*      */     }
/*      */ 
/*  650 */     for (int i = start; i < end; i++) {
/*  651 */       this._prefixTable.popScope(this._namespacePrefixes[i]);
/*      */     }
/*  653 */     this._namespacePrefixesIndex = start;
/*      */   }
/*      */ 
/*      */   protected final QualifiedName processLiteralQualifiedName(int state, QualifiedName q)
/*      */     throws FastInfosetException, IOException
/*      */   {
/*  659 */     if (q == null) q = new QualifiedName();
/*      */ 
/*  661 */     switch (state)
/*      */     {
/*      */     case 0:
/*  664 */       return q.set(null, null, decodeIdentifyingNonEmptyStringOnFirstBit(this._v.localName), -1, -1, this._identifier, null);
/*      */     case 1:
/*  674 */       return q.set(null, decodeIdentifyingNonEmptyStringIndexOnFirstBitAsNamespaceName(false), decodeIdentifyingNonEmptyStringOnFirstBit(this._v.localName), -1, this._namespaceNameIndex, this._identifier, null);
/*      */     case 2:
/*  684 */       throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.qNameMissingNamespaceName"));
/*      */     case 3:
/*  687 */       return q.set(decodeIdentifyingNonEmptyStringIndexOnFirstBitAsPrefix(true), decodeIdentifyingNonEmptyStringIndexOnFirstBitAsNamespaceName(true), decodeIdentifyingNonEmptyStringOnFirstBit(this._v.localName), this._prefixIndex, this._namespaceNameIndex, this._identifier, this._charBuffer);
/*      */     }
/*      */ 
/*  696 */     throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.decodingEII"));
/*      */   }
/*      */ 
/*      */   protected final QualifiedName processLiteralQualifiedName(int state)
/*      */     throws FastInfosetException, IOException
/*      */   {
/*  702 */     switch (state)
/*      */     {
/*      */     case 0:
/*  705 */       return new QualifiedName(null, null, decodeIdentifyingNonEmptyStringOnFirstBit(this._v.localName), -1, -1, this._identifier, null);
/*      */     case 1:
/*  715 */       return new QualifiedName(null, decodeIdentifyingNonEmptyStringIndexOnFirstBitAsNamespaceName(false), decodeIdentifyingNonEmptyStringOnFirstBit(this._v.localName), -1, this._namespaceNameIndex, this._identifier, null);
/*      */     case 2:
/*  725 */       throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.qNameMissingNamespaceName"));
/*      */     case 3:
/*  728 */       return new QualifiedName(decodeIdentifyingNonEmptyStringIndexOnFirstBitAsPrefix(true), decodeIdentifyingNonEmptyStringIndexOnFirstBitAsNamespaceName(true), decodeIdentifyingNonEmptyStringOnFirstBit(this._v.localName), this._prefixIndex, this._namespaceNameIndex, this._identifier, this._charBuffer);
/*      */     }
/*      */ 
/*  737 */     throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.decodingEII"));
/*      */   }
/*      */ 
/*      */   protected final void processAIIs()
/*      */     throws FastInfosetException, IOException
/*      */   {
/*  746 */     if (++this._duplicateAttributeVerifier._currentIteration == 2147483647) {
/*  747 */       this._duplicateAttributeVerifier.clear();
/*      */     }
/*      */ 
/*      */     do
/*      */     {
/*  752 */       int b = read();
/*      */       QualifiedName name;
/*  753 */       switch (DecoderStateTables.AII(b)) {
/*      */       case 0:
/*  755 */         name = this._attributeNameTable._array[b];
/*  756 */         break;
/*      */       case 1:
/*  759 */         int i = ((b & 0x1F) << 8 | read()) + 64;
/*      */ 
/*  761 */         name = this._attributeNameTable._array[i];
/*  762 */         break;
/*      */       case 2:
/*  766 */         int i = ((b & 0xF) << 16 | read() << 8 | read()) + 8256;
/*      */ 
/*  768 */         name = this._attributeNameTable._array[i];
/*  769 */         break;
/*      */       case 3:
/*  772 */         name = processLiteralQualifiedName(b & 0x3, this._attributeNameTable.getNext());
/*      */ 
/*  775 */         name.createAttributeValues(256);
/*  776 */         this._attributeNameTable.add(name);
/*  777 */         break;
/*      */       case 5:
/*  779 */         this._doubleTerminate = true;
/*      */       case 4:
/*  781 */         this._terminate = true;
/*      */ 
/*  783 */         break;
/*      */       default:
/*  785 */         throw new IOException(CommonResourceBundle.getInstance().getString("message.decodingAIIs"));
/*      */       }
/*      */ 
/*  788 */       if ((name.prefixIndex > 0) && (this._prefixTable._currentInScope[name.prefixIndex] != name.namespaceNameIndex)) {
/*  789 */         throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.AIIqNameNotInScope"));
/*      */       }
/*      */ 
/*  792 */       this._duplicateAttributeVerifier.checkForDuplicateAttribute(name.attributeHash, name.attributeId);
/*      */ 
/*  794 */       Attr a = createAttribute(name.namespaceName, name.qName, name.localName);
/*      */ 
/*  801 */       b = read();
/*      */       String value;
/*  802 */       switch (DecoderStateTables.NISTRING(b))
/*      */       {
/*      */       case 0:
/*  805 */         boolean addToTable = (b & 0x40) > 0;
/*  806 */         this._octetBufferLength = ((b & 0x7) + 1);
/*  807 */         value = decodeUtf8StringAsString();
/*  808 */         if (addToTable) {
/*  809 */           this._attributeValueTable.add(value);
/*      */         }
/*      */ 
/*  812 */         a.setValue(value);
/*  813 */         this._currentElement.setAttributeNode(a);
/*  814 */         break;
/*      */       case 1:
/*  818 */         boolean addToTable = (b & 0x40) > 0;
/*  819 */         this._octetBufferLength = (read() + 9);
/*  820 */         value = decodeUtf8StringAsString();
/*  821 */         if (addToTable) {
/*  822 */           this._attributeValueTable.add(value);
/*      */         }
/*      */ 
/*  825 */         a.setValue(value);
/*  826 */         this._currentElement.setAttributeNode(a);
/*  827 */         break;
/*      */       case 2:
/*  831 */         boolean addToTable = (b & 0x40) > 0;
/*  832 */         int length = read() << 24 | read() << 16 | read() << 8 | read();
/*      */ 
/*  836 */         this._octetBufferLength = (length + 265);
/*  837 */         value = decodeUtf8StringAsString();
/*  838 */         if (addToTable) {
/*  839 */           this._attributeValueTable.add(value);
/*      */         }
/*      */ 
/*  842 */         a.setValue(value);
/*  843 */         this._currentElement.setAttributeNode(a);
/*  844 */         break;
/*      */       case 3:
/*  848 */         boolean addToTable = (b & 0x40) > 0;
/*  849 */         this._octetBufferLength = ((b & 0x7) + 1);
/*  850 */         value = decodeUtf16StringAsString();
/*  851 */         if (addToTable) {
/*  852 */           this._attributeValueTable.add(value);
/*      */         }
/*      */ 
/*  855 */         a.setValue(value);
/*  856 */         this._currentElement.setAttributeNode(a);
/*  857 */         break;
/*      */       case 4:
/*  861 */         boolean addToTable = (b & 0x40) > 0;
/*  862 */         this._octetBufferLength = (read() + 9);
/*  863 */         value = decodeUtf16StringAsString();
/*  864 */         if (addToTable) {
/*  865 */           this._attributeValueTable.add(value);
/*      */         }
/*      */ 
/*  868 */         a.setValue(value);
/*  869 */         this._currentElement.setAttributeNode(a);
/*  870 */         break;
/*      */       case 5:
/*  874 */         boolean addToTable = (b & 0x40) > 0;
/*  875 */         int length = read() << 24 | read() << 16 | read() << 8 | read();
/*      */ 
/*  879 */         this._octetBufferLength = (length + 265);
/*  880 */         value = decodeUtf16StringAsString();
/*  881 */         if (addToTable) {
/*  882 */           this._attributeValueTable.add(value);
/*      */         }
/*      */ 
/*  885 */         a.setValue(value);
/*  886 */         this._currentElement.setAttributeNode(a);
/*  887 */         break;
/*      */       case 6:
/*  891 */         boolean addToTable = (b & 0x40) > 0;
/*      */ 
/*  893 */         this._identifier = ((b & 0xF) << 4);
/*  894 */         b = read();
/*  895 */         this._identifier |= (b & 0xF0) >> 4;
/*      */ 
/*  897 */         decodeOctetsOnFifthBitOfNonIdentifyingStringOnFirstBit(b);
/*      */ 
/*  899 */         value = decodeRestrictedAlphabetAsString();
/*  900 */         if (addToTable) {
/*  901 */           this._attributeValueTable.add(value);
/*      */         }
/*      */ 
/*  904 */         a.setValue(value);
/*  905 */         this._currentElement.setAttributeNode(a);
/*  906 */         break;
/*      */       case 7:
/*  910 */         boolean addToTable = (b & 0x40) > 0;
/*  911 */         this._identifier = ((b & 0xF) << 4);
/*  912 */         b = read();
/*  913 */         this._identifier |= (b & 0xF0) >> 4;
/*      */ 
/*  915 */         decodeOctetsOnFifthBitOfNonIdentifyingStringOnFirstBit(b);
/*  916 */         value = convertEncodingAlgorithmDataToCharacters(true);
/*  917 */         if (addToTable) {
/*  918 */           this._attributeValueTable.add(value);
/*      */         }
/*  920 */         a.setValue(value);
/*  921 */         this._currentElement.setAttributeNode(a);
/*  922 */         break;
/*      */       case 8:
/*  925 */         value = this._attributeValueTable._array[(b & 0x3F)];
/*      */ 
/*  927 */         a.setValue(value);
/*  928 */         this._currentElement.setAttributeNode(a);
/*  929 */         break;
/*      */       case 9:
/*  932 */         int index = ((b & 0x1F) << 8 | read()) + 64;
/*      */ 
/*  934 */         value = this._attributeValueTable._array[index];
/*      */ 
/*  936 */         a.setValue(value);
/*  937 */         this._currentElement.setAttributeNode(a);
/*  938 */         break;
/*      */       case 10:
/*  942 */         int index = ((b & 0xF) << 16 | read() << 8 | read()) + 8256;
/*      */ 
/*  944 */         value = this._attributeValueTable._array[index];
/*      */ 
/*  946 */         a.setValue(value);
/*  947 */         this._currentElement.setAttributeNode(a);
/*  948 */         break;
/*      */       case 11:
/*  951 */         a.setValue("");
/*  952 */         this._currentElement.setAttributeNode(a);
/*  953 */         break;
/*      */       default:
/*  955 */         throw new IOException(CommonResourceBundle.getInstance().getString("message.decodingAIIValue"));
/*      */       }
/*      */     }
/*  958 */     while (!this._terminate);
/*      */ 
/*  961 */     this._duplicateAttributeVerifier._poolCurrent = this._duplicateAttributeVerifier._poolHead;
/*      */ 
/*  963 */     this._terminate = this._doubleTerminate;
/*  964 */     this._doubleTerminate = false;
/*      */   }
/*      */ 
/*      */   protected final void processCommentII() throws FastInfosetException, IOException {
/*  968 */     switch (decodeNonIdentifyingStringOnFirstBit())
/*      */     {
/*      */     case 0:
/*  971 */       String s = new String(this._charBuffer, 0, this._charBufferLength);
/*  972 */       if (this._addToTable) {
/*  973 */         this._v.otherString.add(new CharArrayString(s, false));
/*      */       }
/*      */ 
/*  976 */       this._currentNode.appendChild(this._document.createComment(s));
/*  977 */       break;
/*      */     case 2:
/*  980 */       throw new IOException(CommonResourceBundle.getInstance().getString("message.commentIIAlgorithmNotSupported"));
/*      */     case 1:
/*  983 */       String s = this._v.otherString.get(this._integer).toString();
/*      */ 
/*  985 */       this._currentNode.appendChild(this._document.createComment(s));
/*  986 */       break;
/*      */     case 3:
/*  989 */       this._currentNode.appendChild(this._document.createComment(""));
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void processProcessingII() throws FastInfosetException, IOException
/*      */   {
/*  995 */     String target = decodeIdentifyingNonEmptyStringOnFirstBit(this._v.otherNCName);
/*      */ 
/*  997 */     switch (decodeNonIdentifyingStringOnFirstBit())
/*      */     {
/*      */     case 0:
/* 1000 */       String data = new String(this._charBuffer, 0, this._charBufferLength);
/* 1001 */       if (this._addToTable) {
/* 1002 */         this._v.otherString.add(new CharArrayString(data, false));
/*      */       }
/*      */ 
/* 1005 */       this._currentNode.appendChild(this._document.createProcessingInstruction(target, data));
/* 1006 */       break;
/*      */     case 2:
/* 1009 */       throw new IOException(CommonResourceBundle.getInstance().getString("message.processingIIWithEncodingAlgorithm"));
/*      */     case 1:
/* 1012 */       String data = this._v.otherString.get(this._integer).toString();
/*      */ 
/* 1014 */       this._currentNode.appendChild(this._document.createProcessingInstruction(target, data));
/* 1015 */       break;
/*      */     case 3:
/* 1018 */       this._currentNode.appendChild(this._document.createProcessingInstruction(target, ""));
/*      */     }
/*      */   }
/*      */ 
/*      */   protected Element createElement(String namespaceName, String qName, String localName)
/*      */   {
/* 1024 */     return this._document.createElementNS(namespaceName, qName);
/*      */   }
/*      */ 
/*      */   protected Attr createAttribute(String namespaceName, String qName, String localName) {
/* 1028 */     return this._document.createAttributeNS(namespaceName, qName);
/*      */   }
/*      */ 
/*      */   protected String convertEncodingAlgorithmDataToCharacters(boolean isAttributeValue) throws FastInfosetException, IOException {
/* 1032 */     StringBuffer buffer = new StringBuffer();
/* 1033 */     if (this._identifier < 9) {
/* 1034 */       Object array = BuiltInEncodingAlgorithmFactory.getAlgorithm(this._identifier).decodeFromBytes(this._octetBuffer, this._octetBufferStart, this._octetBufferLength);
/*      */ 
/* 1036 */       BuiltInEncodingAlgorithmFactory.getAlgorithm(this._identifier).convertToCharacters(array, buffer); } else {
/* 1037 */       if (this._identifier == 9) {
/* 1038 */         if (!isAttributeValue)
/*      */         {
/* 1040 */           this._octetBufferOffset -= this._octetBufferLength;
/* 1041 */           return decodeUtf8StringAsString();
/*      */         }
/* 1043 */         throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.CDATAAlgorithmNotSupported"));
/* 1044 */       }if (this._identifier >= 32) {
/* 1045 */         String URI = this._v.encodingAlgorithm.get(this._identifier - 32);
/* 1046 */         EncodingAlgorithm ea = (EncodingAlgorithm)this._registeredEncodingAlgorithms.get(URI);
/* 1047 */         if (ea != null) {
/* 1048 */           Object data = ea.decodeFromBytes(this._octetBuffer, this._octetBufferStart, this._octetBufferLength);
/* 1049 */           ea.convertToCharacters(data, buffer);
/*      */         } else {
/* 1051 */           throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.algorithmDataCannotBeReported"));
/*      */         }
/*      */       }
/*      */     }
/* 1055 */     return buffer.toString();
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.dom.DOMDocumentParser
 * JD-Core Version:    0.6.2
 */