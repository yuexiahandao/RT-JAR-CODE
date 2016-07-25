/*      */ package com.sun.org.apache.xerces.internal.impl;
/*      */ 
/*      */ import com.sun.org.apache.xerces.internal.impl.io.ASCIIReader;
/*      */ import com.sun.org.apache.xerces.internal.impl.io.UCSReader;
/*      */ import com.sun.org.apache.xerces.internal.impl.io.UTF8Reader;
/*      */ import com.sun.org.apache.xerces.internal.util.EncodingMap;
/*      */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*      */ import com.sun.org.apache.xerces.internal.util.XMLChar;
/*      */ import com.sun.org.apache.xerces.internal.util.XMLStringBuffer;
/*      */ import com.sun.org.apache.xerces.internal.xni.QName;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLLocator;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLString;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
/*      */ import com.sun.xml.internal.stream.Entity.ScannedEntity;
/*      */ import com.sun.xml.internal.stream.XMLBufferListener;
/*      */ import java.io.EOFException;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.Reader;
/*      */ import java.util.Locale;
/*      */ import java.util.Vector;
/*      */ 
/*      */ public class XMLEntityScanner
/*      */   implements XMLLocator
/*      */ {
/*   58 */   protected Entity.ScannedEntity fCurrentEntity = null;
/*   59 */   protected int fBufferSize = 8192;
/*      */   protected XMLEntityManager fEntityManager;
/*      */   private static final boolean DEBUG_ENCODINGS = false;
/*   66 */   private Vector listeners = new Vector();
/*      */ 
/*   68 */   private static final boolean[] VALID_NAMES = new boolean[127];
/*      */   private static final boolean DEBUG_BUFFER = false;
/*      */   private static final boolean DEBUG_SKIP_STRING = false;
/*   80 */   private static final EOFException END_OF_DOCUMENT_ENTITY = new EOFException() {
/*      */     private static final long serialVersionUID = 980337771224675268L;
/*      */ 
/*   83 */     public Throwable fillInStackTrace() { return this; }
/*      */ 
/*   80 */   };
/*      */ 
/*   87 */   protected SymbolTable fSymbolTable = null;
/*   88 */   protected XMLErrorReporter fErrorReporter = null;
/*   89 */   int[] whiteSpaceLookup = new int[100];
/*   90 */   int whiteSpaceLen = 0;
/*   91 */   boolean whiteSpaceInfoNeeded = true;
/*      */   protected boolean fAllowJavaEncodings;
/*      */   protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
/*      */   protected static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
/*      */   protected static final String ALLOW_JAVA_ENCODINGS = "http://apache.org/xml/features/allow-java-encodings";
/*  114 */   protected PropertyManager fPropertyManager = null;
/*      */ 
/*  116 */   boolean isExternal = false;
/*      */ 
/*  135 */   boolean xmlVersionSetExplicitly = false;
/*      */ 
/*      */   public XMLEntityScanner()
/*      */   {
/*      */   }
/*      */ 
/*      */   public XMLEntityScanner(PropertyManager propertyManager, XMLEntityManager entityManager)
/*      */   {
/*  151 */     this.fEntityManager = entityManager;
/*  152 */     reset(propertyManager);
/*      */   }
/*      */ 
/*      */   public final void setBufferSize(int size)
/*      */   {
/*  168 */     this.fBufferSize = size;
/*      */   }
/*      */ 
/*      */   public void reset(PropertyManager propertyManager)
/*      */   {
/*  175 */     this.fSymbolTable = ((SymbolTable)propertyManager.getProperty("http://apache.org/xml/properties/internal/symbol-table"));
/*  176 */     this.fErrorReporter = ((XMLErrorReporter)propertyManager.getProperty("http://apache.org/xml/properties/internal/error-reporter"));
/*  177 */     this.fCurrentEntity = null;
/*  178 */     this.whiteSpaceLen = 0;
/*  179 */     this.whiteSpaceInfoNeeded = true;
/*  180 */     this.listeners.clear();
/*      */   }
/*      */ 
/*      */   public void reset(XMLComponentManager componentManager)
/*      */     throws XMLConfigurationException
/*      */   {
/*  202 */     this.fAllowJavaEncodings = componentManager.getFeature("http://apache.org/xml/features/allow-java-encodings", false);
/*      */ 
/*  205 */     this.fSymbolTable = ((SymbolTable)componentManager.getProperty("http://apache.org/xml/properties/internal/symbol-table"));
/*  206 */     this.fErrorReporter = ((XMLErrorReporter)componentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter"));
/*  207 */     this.fCurrentEntity = null;
/*  208 */     this.whiteSpaceLen = 0;
/*  209 */     this.whiteSpaceInfoNeeded = true;
/*  210 */     this.listeners.clear();
/*      */   }
/*      */ 
/*      */   public final void reset(SymbolTable symbolTable, XMLEntityManager entityManager, XMLErrorReporter reporter)
/*      */   {
/*  216 */     this.fCurrentEntity = null;
/*  217 */     this.fSymbolTable = symbolTable;
/*  218 */     this.fEntityManager = entityManager;
/*  219 */     this.fErrorReporter = reporter;
/*      */   }
/*      */ 
/*      */   public final String getXMLVersion()
/*      */   {
/*  233 */     if (this.fCurrentEntity != null) {
/*  234 */       return this.fCurrentEntity.xmlVersion;
/*      */     }
/*  236 */     return null;
/*      */   }
/*      */ 
/*      */   public final void setXMLVersion(String xmlVersion)
/*      */   {
/*  247 */     this.xmlVersionSetExplicitly = true;
/*  248 */     this.fCurrentEntity.xmlVersion = xmlVersion;
/*      */   }
/*      */ 
/*      */   public final void setCurrentEntity(Entity.ScannedEntity scannedEntity)
/*      */   {
/*  257 */     this.fCurrentEntity = scannedEntity;
/*  258 */     if (this.fCurrentEntity != null)
/*  259 */       this.isExternal = this.fCurrentEntity.isExternal();
/*      */   }
/*      */ 
/*      */   public Entity.ScannedEntity getCurrentEntity()
/*      */   {
/*  266 */     return this.fCurrentEntity;
/*      */   }
/*      */ 
/*      */   public final String getBaseSystemId()
/*      */   {
/*  277 */     return (this.fCurrentEntity != null) && (this.fCurrentEntity.entityLocation != null) ? this.fCurrentEntity.entityLocation.getExpandedSystemId() : null;
/*      */   }
/*      */ 
/*      */   public void setBaseSystemId(String systemId)
/*      */   {
/*      */   }
/*      */ 
/*      */   public final int getLineNumber()
/*      */   {
/*  291 */     return this.fCurrentEntity != null ? this.fCurrentEntity.lineNumber : -1;
/*      */   }
/*      */ 
/*      */   public void setLineNumber(int line)
/*      */   {
/*      */   }
/*      */ 
/*      */   public final int getColumnNumber()
/*      */   {
/*  305 */     return this.fCurrentEntity != null ? this.fCurrentEntity.columnNumber : -1;
/*      */   }
/*      */ 
/*      */   public void setColumnNumber(int col)
/*      */   {
/*      */   }
/*      */ 
/*      */   public final int getCharacterOffset()
/*      */   {
/*  317 */     return this.fCurrentEntity != null ? this.fCurrentEntity.fTotalCountTillLastLoad + this.fCurrentEntity.position : -1;
/*      */   }
/*      */ 
/*      */   public final String getExpandedSystemId()
/*      */   {
/*  322 */     return (this.fCurrentEntity != null) && (this.fCurrentEntity.entityLocation != null) ? this.fCurrentEntity.entityLocation.getExpandedSystemId() : null;
/*      */   }
/*      */ 
/*      */   public void setExpandedSystemId(String systemId)
/*      */   {
/*      */   }
/*      */ 
/*      */   public final String getLiteralSystemId()
/*      */   {
/*  334 */     return (this.fCurrentEntity != null) && (this.fCurrentEntity.entityLocation != null) ? this.fCurrentEntity.entityLocation.getLiteralSystemId() : null;
/*      */   }
/*      */ 
/*      */   public void setLiteralSystemId(String systemId)
/*      */   {
/*      */   }
/*      */ 
/*      */   public final String getPublicId()
/*      */   {
/*  346 */     return (this.fCurrentEntity != null) && (this.fCurrentEntity.entityLocation != null) ? this.fCurrentEntity.entityLocation.getPublicId() : null;
/*      */   }
/*      */ 
/*      */   public void setPublicId(String publicId)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void setVersion(String version)
/*      */   {
/*  360 */     this.fCurrentEntity.version = version;
/*      */   }
/*      */ 
/*      */   public String getVersion() {
/*  364 */     if (this.fCurrentEntity != null)
/*  365 */       return this.fCurrentEntity.version;
/*  366 */     return null;
/*      */   }
/*      */ 
/*      */   public final String getEncoding()
/*      */   {
/*  378 */     if (this.fCurrentEntity != null) {
/*  379 */       return this.fCurrentEntity.encoding;
/*      */     }
/*  381 */     return null;
/*      */   }
/*      */ 
/*      */   public final void setEncoding(String encoding)
/*      */     throws IOException
/*      */   {
/*  408 */     if (this.fCurrentEntity.stream != null)
/*      */     {
/*  416 */       if ((this.fCurrentEntity.encoding == null) || (!this.fCurrentEntity.encoding.equals(encoding)))
/*      */       {
/*  422 */         if ((this.fCurrentEntity.encoding != null) && (this.fCurrentEntity.encoding.startsWith("UTF-16"))) {
/*  423 */           String ENCODING = encoding.toUpperCase(Locale.ENGLISH);
/*  424 */           if (ENCODING.equals("UTF-16")) return;
/*  425 */           if (ENCODING.equals("ISO-10646-UCS-4")) {
/*  426 */             if (this.fCurrentEntity.encoding.equals("UTF-16BE"))
/*  427 */               this.fCurrentEntity.reader = new UCSReader(this.fCurrentEntity.stream, (short)8);
/*      */             else {
/*  429 */               this.fCurrentEntity.reader = new UCSReader(this.fCurrentEntity.stream, (short)4);
/*      */             }
/*  431 */             return;
/*      */           }
/*  433 */           if (ENCODING.equals("ISO-10646-UCS-2")) {
/*  434 */             if (this.fCurrentEntity.encoding.equals("UTF-16BE"))
/*  435 */               this.fCurrentEntity.reader = new UCSReader(this.fCurrentEntity.stream, (short)2);
/*      */             else {
/*  437 */               this.fCurrentEntity.reader = new UCSReader(this.fCurrentEntity.stream, (short)1);
/*      */             }
/*  439 */             return;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  449 */         this.fCurrentEntity.reader = createReader(this.fCurrentEntity.stream, encoding, null);
/*  450 */         this.fCurrentEntity.encoding = encoding;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public final boolean isExternal()
/*      */   {
/*  462 */     return this.fCurrentEntity.isExternal();
/*      */   }
/*      */ 
/*      */   public int getChar(int relative) throws IOException {
/*  466 */     if (arrangeCapacity(relative + 1, false)) {
/*  467 */       return this.fCurrentEntity.ch[(this.fCurrentEntity.position + relative)];
/*      */     }
/*  469 */     return -1;
/*      */   }
/*      */ 
/*      */   public int peekChar()
/*      */     throws IOException
/*      */   {
/*  489 */     if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
/*  490 */       load(0, true, true);
/*      */     }
/*      */ 
/*  494 */     int c = this.fCurrentEntity.ch[this.fCurrentEntity.position];
/*      */ 
/*  506 */     if (this.isExternal) {
/*  507 */       return c != 13 ? c : 10;
/*      */     }
/*  509 */     return c;
/*      */   }
/*      */ 
/*      */   public int scanChar()
/*      */     throws IOException
/*      */   {
/*  530 */     if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
/*  531 */       load(0, true, true);
/*      */     }
/*      */ 
/*  535 */     int c = this.fCurrentEntity.ch[(this.fCurrentEntity.position++)];
/*  536 */     if ((c == 10) || ((c == 13) && (this.isExternal)))
/*      */     {
/*  538 */       this.fCurrentEntity.lineNumber += 1;
/*  539 */       this.fCurrentEntity.columnNumber = 1;
/*  540 */       if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
/*  541 */         invokeListeners(1);
/*  542 */         this.fCurrentEntity.ch[0] = ((char)c);
/*  543 */         load(1, false, false);
/*      */       }
/*  545 */       if ((c == 13) && (this.isExternal)) {
/*  546 */         if (this.fCurrentEntity.ch[(this.fCurrentEntity.position++)] != '\n') {
/*  547 */           this.fCurrentEntity.position -= 1;
/*      */         }
/*  549 */         c = 10;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  559 */     this.fCurrentEntity.columnNumber += 1;
/*  560 */     return c;
/*      */   }
/*      */ 
/*      */   public String scanNmtoken()
/*      */     throws IOException
/*      */   {
/*  587 */     if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
/*  588 */       load(0, true, true);
/*      */     }
/*      */ 
/*  592 */     int offset = this.fCurrentEntity.position;
/*  593 */     boolean vc = false;
/*      */     while (true)
/*      */     {
/*  597 */       char c = this.fCurrentEntity.ch[this.fCurrentEntity.position];
/*  598 */       if (c < '')
/*  599 */         vc = VALID_NAMES[c];
/*      */       else {
/*  601 */         vc = XMLChar.isName(c);
/*      */       }
/*  603 */       if (!vc)
/*      */         break;
/*  605 */       if (++this.fCurrentEntity.position == this.fCurrentEntity.count) {
/*  606 */         int length = this.fCurrentEntity.position - offset;
/*  607 */         invokeListeners(length);
/*  608 */         if (length == this.fCurrentEntity.fBufferSize)
/*      */         {
/*  610 */           char[] tmp = new char[this.fCurrentEntity.fBufferSize * 2];
/*  611 */           System.arraycopy(this.fCurrentEntity.ch, offset, tmp, 0, length);
/*      */ 
/*  613 */           this.fCurrentEntity.ch = tmp;
/*  614 */           this.fCurrentEntity.fBufferSize *= 2;
/*      */         } else {
/*  616 */           System.arraycopy(this.fCurrentEntity.ch, offset, this.fCurrentEntity.ch, 0, length);
/*      */         }
/*      */ 
/*  619 */         offset = 0;
/*  620 */         if (load(length, false, false)) {
/*      */           break;
/*      */         }
/*      */       }
/*      */     }
/*  625 */     int length = this.fCurrentEntity.position - offset;
/*  626 */     this.fCurrentEntity.columnNumber += length;
/*      */ 
/*  629 */     String symbol = null;
/*  630 */     if (length > 0) {
/*  631 */       symbol = this.fSymbolTable.addSymbol(this.fCurrentEntity.ch, offset, length);
/*      */     }
/*      */ 
/*  638 */     return symbol;
/*      */   }
/*      */ 
/*      */   public String scanName()
/*      */     throws IOException
/*      */   {
/*  666 */     if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
/*  667 */       load(0, true, true);
/*      */     }
/*      */ 
/*  671 */     int offset = this.fCurrentEntity.position;
/*  672 */     if (XMLChar.isNameStart(this.fCurrentEntity.ch[offset])) {
/*  673 */       if (++this.fCurrentEntity.position == this.fCurrentEntity.count) {
/*  674 */         invokeListeners(1);
/*  675 */         this.fCurrentEntity.ch[0] = this.fCurrentEntity.ch[offset];
/*  676 */         offset = 0;
/*  677 */         if (load(1, false, false)) {
/*  678 */           this.fCurrentEntity.columnNumber += 1;
/*  679 */           String symbol = this.fSymbolTable.addSymbol(this.fCurrentEntity.ch, 0, 1);
/*      */ 
/*  686 */           return symbol;
/*      */         }
/*      */       }
/*  689 */       boolean vc = false;
/*      */       while (true)
/*      */       {
/*  692 */         char c = this.fCurrentEntity.ch[this.fCurrentEntity.position];
/*  693 */         if (c < '')
/*  694 */           vc = VALID_NAMES[c];
/*      */         else {
/*  696 */           vc = XMLChar.isName(c);
/*      */         }
/*  698 */         if (!vc) break;
/*  699 */         if (++this.fCurrentEntity.position == this.fCurrentEntity.count) {
/*  700 */           int length = this.fCurrentEntity.position - offset;
/*  701 */           invokeListeners(length);
/*  702 */           if (length == this.fCurrentEntity.fBufferSize)
/*      */           {
/*  704 */             char[] tmp = new char[this.fCurrentEntity.fBufferSize * 2];
/*  705 */             System.arraycopy(this.fCurrentEntity.ch, offset, tmp, 0, length);
/*      */ 
/*  707 */             this.fCurrentEntity.ch = tmp;
/*  708 */             this.fCurrentEntity.fBufferSize *= 2;
/*      */           } else {
/*  710 */             System.arraycopy(this.fCurrentEntity.ch, offset, this.fCurrentEntity.ch, 0, length);
/*      */           }
/*      */ 
/*  713 */           offset = 0;
/*  714 */           if (load(length, false, false)) {
/*      */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  720 */     int length = this.fCurrentEntity.position - offset;
/*  721 */     this.fCurrentEntity.columnNumber += length;
/*      */     String symbol;
/*      */     String symbol;
/*  725 */     if (length > 0)
/*  726 */       symbol = this.fSymbolTable.addSymbol(this.fCurrentEntity.ch, offset, length);
/*      */     else {
/*  728 */       symbol = null;
/*      */     }
/*      */ 
/*  734 */     return symbol;
/*      */   }
/*      */ 
/*      */   public boolean scanQName(QName qname)
/*      */     throws IOException
/*      */   {
/*  768 */     if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
/*  769 */       load(0, true, true);
/*      */     }
/*      */ 
/*  773 */     int offset = this.fCurrentEntity.position;
/*      */ 
/*  779 */     if (XMLChar.isNameStart(this.fCurrentEntity.ch[offset])) {
/*  780 */       if (++this.fCurrentEntity.position == this.fCurrentEntity.count) {
/*  781 */         invokeListeners(1);
/*  782 */         this.fCurrentEntity.ch[0] = this.fCurrentEntity.ch[offset];
/*  783 */         offset = 0;
/*      */ 
/*  785 */         if (load(1, false, false)) {
/*  786 */           this.fCurrentEntity.columnNumber += 1;
/*      */ 
/*  789 */           String name = this.fSymbolTable.addSymbol(this.fCurrentEntity.ch, 0, 1);
/*  790 */           qname.setValues(null, name, name, null);
/*      */ 
/*  796 */           return true;
/*      */         }
/*      */       }
/*  799 */       int index = -1;
/*  800 */       boolean vc = false;
/*      */       while (true)
/*      */       {
/*  804 */         char c = this.fCurrentEntity.ch[this.fCurrentEntity.position];
/*  805 */         if (c < '')
/*  806 */           vc = VALID_NAMES[c];
/*      */         else {
/*  808 */           vc = XMLChar.isName(c);
/*      */         }
/*  810 */         if (!vc) break;
/*  811 */         if (c == ':') {
/*  812 */           if (index == -1)
/*      */           {
/*  815 */             index = this.fCurrentEntity.position;
/*      */           }
/*  817 */         } else if (++this.fCurrentEntity.position == this.fCurrentEntity.count) {
/*  818 */           int length = this.fCurrentEntity.position - offset;
/*  819 */           invokeListeners(length);
/*  820 */           if (length == this.fCurrentEntity.fBufferSize)
/*      */           {
/*  822 */             char[] tmp = new char[this.fCurrentEntity.fBufferSize * 2];
/*  823 */             System.arraycopy(this.fCurrentEntity.ch, offset, tmp, 0, length);
/*      */ 
/*  825 */             this.fCurrentEntity.ch = tmp;
/*  826 */             this.fCurrentEntity.fBufferSize *= 2;
/*      */           } else {
/*  828 */             System.arraycopy(this.fCurrentEntity.ch, offset, this.fCurrentEntity.ch, 0, length);
/*      */           }
/*      */ 
/*  831 */           if (index != -1) {
/*  832 */             index -= offset;
/*      */           }
/*  834 */           offset = 0;
/*  835 */           if (load(length, false, false)) {
/*      */             break;
/*      */           }
/*      */         }
/*      */       }
/*  840 */       int length = this.fCurrentEntity.position - offset;
/*  841 */       this.fCurrentEntity.columnNumber += length;
/*  842 */       if (length > 0) {
/*  843 */         String prefix = null;
/*  844 */         String localpart = null;
/*  845 */         String rawname = this.fSymbolTable.addSymbol(this.fCurrentEntity.ch, offset, length);
/*      */ 
/*  848 */         if (index != -1) {
/*  849 */           int prefixLength = index - offset;
/*  850 */           prefix = this.fSymbolTable.addSymbol(this.fCurrentEntity.ch, offset, prefixLength);
/*      */ 
/*  852 */           int len = length - prefixLength - 1;
/*  853 */           localpart = this.fSymbolTable.addSymbol(this.fCurrentEntity.ch, index + 1, len);
/*      */         }
/*      */         else
/*      */         {
/*  857 */           localpart = rawname;
/*      */         }
/*  859 */         qname.setValues(prefix, localpart, rawname, null);
/*      */ 
/*  865 */         return true;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  875 */     return false;
/*      */   }
/*      */ 
/*      */   public int scanContent(XMLString content)
/*      */     throws IOException
/*      */   {
/*  909 */     if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
/*  910 */       load(0, true, true);
/*  911 */     } else if (this.fCurrentEntity.position == this.fCurrentEntity.count - 1) {
/*  912 */       invokeListeners(0);
/*  913 */       this.fCurrentEntity.ch[0] = this.fCurrentEntity.ch[(this.fCurrentEntity.count - 1)];
/*  914 */       load(1, false, false);
/*  915 */       this.fCurrentEntity.position = 0;
/*      */     }
/*      */ 
/*  919 */     int offset = this.fCurrentEntity.position;
/*  920 */     int c = this.fCurrentEntity.ch[offset];
/*  921 */     int newlines = 0;
/*  922 */     if ((c == 10) || ((c == 13) && (this.isExternal)))
/*      */     {
/*      */       do
/*      */       {
/*  929 */         c = this.fCurrentEntity.ch[(this.fCurrentEntity.position++)];
/*  930 */         if ((c == 13) && (this.isExternal)) {
/*  931 */           newlines++;
/*  932 */           this.fCurrentEntity.lineNumber += 1;
/*  933 */           this.fCurrentEntity.columnNumber = 1;
/*  934 */           if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
/*  935 */             offset = 0;
/*  936 */             this.fCurrentEntity.position = newlines;
/*  937 */             if (load(newlines, false, true)) {
/*      */               break;
/*      */             }
/*      */           }
/*  941 */           if (this.fCurrentEntity.ch[this.fCurrentEntity.position] == '\n') {
/*  942 */             this.fCurrentEntity.position += 1;
/*  943 */             offset++;
/*      */           }
/*      */           else
/*      */           {
/*  947 */             newlines++;
/*      */           }
/*  949 */         } else if (c == 10) {
/*  950 */           newlines++;
/*  951 */           this.fCurrentEntity.lineNumber += 1;
/*  952 */           this.fCurrentEntity.columnNumber = 1;
/*  953 */           if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
/*  954 */             offset = 0;
/*  955 */             this.fCurrentEntity.position = newlines;
/*  956 */             if (load(newlines, false, true))
/*  957 */               break;
/*      */           }
/*      */         }
/*      */         else {
/*  961 */           this.fCurrentEntity.position -= 1;
/*  962 */           break;
/*      */         }
/*      */       }
/*  964 */       while (this.fCurrentEntity.position < this.fCurrentEntity.count - 1);
/*  965 */       for (int i = offset; i < this.fCurrentEntity.position; i++) {
/*  966 */         this.fCurrentEntity.ch[i] = '\n';
/*      */       }
/*  968 */       int length = this.fCurrentEntity.position - offset;
/*  969 */       if (this.fCurrentEntity.position == this.fCurrentEntity.count - 1)
/*      */       {
/*  972 */         content.setValues(this.fCurrentEntity.ch, offset, length);
/*      */ 
/*  979 */         return -1;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  988 */     while (this.fCurrentEntity.position < this.fCurrentEntity.count) {
/*  989 */       c = this.fCurrentEntity.ch[(this.fCurrentEntity.position++)];
/*  990 */       if (!XMLChar.isContent(c)) {
/*  991 */         this.fCurrentEntity.position -= 1;
/*      */       }
/*      */     }
/*      */ 
/*  995 */     int length = this.fCurrentEntity.position - offset;
/*  996 */     this.fCurrentEntity.columnNumber += length - newlines;
/*      */ 
/* 1000 */     content.setValues(this.fCurrentEntity.ch, offset, length);
/*      */ 
/* 1003 */     if (this.fCurrentEntity.position != this.fCurrentEntity.count) {
/* 1004 */       c = this.fCurrentEntity.ch[this.fCurrentEntity.position];
/*      */ 
/* 1007 */       if ((c == 13) && (this.isExternal))
/* 1008 */         c = 10;
/*      */     }
/*      */     else {
/* 1011 */       c = -1;
/*      */     }
/*      */ 
/* 1018 */     return c;
/*      */   }
/*      */ 
/*      */   public int scanLiteral(int quote, XMLString content)
/*      */     throws IOException
/*      */   {
/* 1058 */     if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
/* 1059 */       load(0, true, true);
/* 1060 */     } else if (this.fCurrentEntity.position == this.fCurrentEntity.count - 1) {
/* 1061 */       invokeListeners(0);
/* 1062 */       this.fCurrentEntity.ch[0] = this.fCurrentEntity.ch[(this.fCurrentEntity.count - 1)];
/* 1063 */       load(1, false, false);
/* 1064 */       this.fCurrentEntity.position = 0;
/*      */     }
/*      */ 
/* 1068 */     int offset = this.fCurrentEntity.position;
/* 1069 */     int c = this.fCurrentEntity.ch[offset];
/* 1070 */     int newlines = 0;
/* 1071 */     if (this.whiteSpaceInfoNeeded)
/* 1072 */       this.whiteSpaceLen = 0;
/* 1073 */     if ((c == 10) || ((c == 13) && (this.isExternal)))
/*      */     {
/*      */       do
/*      */       {
/* 1080 */         c = this.fCurrentEntity.ch[(this.fCurrentEntity.position++)];
/* 1081 */         if ((c == 13) && (this.isExternal)) {
/* 1082 */           newlines++;
/* 1083 */           this.fCurrentEntity.lineNumber += 1;
/* 1084 */           this.fCurrentEntity.columnNumber = 1;
/* 1085 */           if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
/* 1086 */             offset = 0;
/* 1087 */             this.fCurrentEntity.position = newlines;
/* 1088 */             if (load(newlines, false, true)) {
/*      */               break;
/*      */             }
/*      */           }
/* 1092 */           if (this.fCurrentEntity.ch[this.fCurrentEntity.position] == '\n') {
/* 1093 */             this.fCurrentEntity.position += 1;
/* 1094 */             offset++;
/*      */           }
/*      */           else
/*      */           {
/* 1098 */             newlines++;
/*      */           }
/*      */         }
/* 1101 */         else if (c == 10) {
/* 1102 */           newlines++;
/* 1103 */           this.fCurrentEntity.lineNumber += 1;
/* 1104 */           this.fCurrentEntity.columnNumber = 1;
/* 1105 */           if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
/* 1106 */             offset = 0;
/* 1107 */             this.fCurrentEntity.position = newlines;
/* 1108 */             if (load(newlines, false, true)) {
/* 1109 */               break;
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/* 1120 */           this.fCurrentEntity.position -= 1;
/* 1121 */           break;
/*      */         }
/*      */       }
/* 1123 */       while (this.fCurrentEntity.position < this.fCurrentEntity.count - 1);
/* 1124 */       int i = 0;
/* 1125 */       for (i = offset; i < this.fCurrentEntity.position; i++) {
/* 1126 */         this.fCurrentEntity.ch[i] = '\n';
/* 1127 */         this.whiteSpaceLookup[(this.whiteSpaceLen++)] = i;
/*      */       }
/*      */ 
/* 1130 */       int length = this.fCurrentEntity.position - offset;
/* 1131 */       if (this.fCurrentEntity.position == this.fCurrentEntity.count - 1) {
/* 1132 */         content.setValues(this.fCurrentEntity.ch, offset, length);
/*      */ 
/* 1138 */         return -1;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1148 */     while (this.fCurrentEntity.position < this.fCurrentEntity.count) {
/* 1149 */       c = this.fCurrentEntity.ch[(this.fCurrentEntity.position++)];
/* 1150 */       if (((c == quote) && ((!this.fCurrentEntity.literal) || (this.isExternal))) || (c == 37) || (!XMLChar.isContent(c)))
/*      */       {
/* 1153 */         this.fCurrentEntity.position -= 1;
/* 1154 */         break;
/*      */       }
/* 1156 */       if ((this.whiteSpaceInfoNeeded) && (
/* 1157 */         (c == 32) || (c == 9))) {
/* 1158 */         if (this.whiteSpaceLen < this.whiteSpaceLookup.length) {
/* 1159 */           this.whiteSpaceLookup[(this.whiteSpaceLen++)] = (this.fCurrentEntity.position - 1);
/*      */         } else {
/* 1161 */           int[] tmp = new int[this.whiteSpaceLookup.length * 2];
/* 1162 */           System.arraycopy(this.whiteSpaceLookup, 0, tmp, 0, this.whiteSpaceLookup.length);
/* 1163 */           this.whiteSpaceLookup = tmp;
/* 1164 */           this.whiteSpaceLookup[(this.whiteSpaceLen++)] = (this.fCurrentEntity.position - 1);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1169 */     int length = this.fCurrentEntity.position - offset;
/* 1170 */     this.fCurrentEntity.columnNumber += length - newlines;
/* 1171 */     content.setValues(this.fCurrentEntity.ch, offset, length);
/*      */ 
/* 1174 */     if (this.fCurrentEntity.position != this.fCurrentEntity.count) {
/* 1175 */       c = this.fCurrentEntity.ch[this.fCurrentEntity.position];
/*      */ 
/* 1179 */       if ((c == quote) && (this.fCurrentEntity.literal))
/* 1180 */         c = -1;
/*      */     }
/*      */     else {
/* 1183 */       c = -1;
/*      */     }
/*      */ 
/* 1190 */     return c;
/*      */   }
/*      */ 
/*      */   public boolean scanData(String delimiter, XMLStringBuffer buffer)
/*      */     throws IOException
/*      */   {
/* 1221 */     boolean done = false;
/* 1222 */     int delimLen = delimiter.length();
/* 1223 */     char charAt0 = delimiter.charAt(0);
/*      */     label974: 
/*      */     do
/*      */     {
/* 1233 */       if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
/* 1234 */         load(0, true, false);
/*      */       }
/*      */ 
/* 1237 */       boolean bNextEntity = false;
/*      */ 
/* 1240 */       while ((this.fCurrentEntity.position > this.fCurrentEntity.count - delimLen) && (!bNextEntity))
/*      */       {
/* 1242 */         System.arraycopy(this.fCurrentEntity.ch, this.fCurrentEntity.position, this.fCurrentEntity.ch, 0, this.fCurrentEntity.count - this.fCurrentEntity.position);
/*      */ 
/* 1248 */         bNextEntity = load(this.fCurrentEntity.count - this.fCurrentEntity.position, false, false);
/* 1249 */         this.fCurrentEntity.position = 0;
/* 1250 */         this.fCurrentEntity.startPosition = 0;
/*      */       }
/*      */ 
/* 1253 */       if (this.fCurrentEntity.position > this.fCurrentEntity.count - delimLen)
/*      */       {
/* 1255 */         int length = this.fCurrentEntity.count - this.fCurrentEntity.position;
/* 1256 */         buffer.append(this.fCurrentEntity.ch, this.fCurrentEntity.position, length);
/* 1257 */         this.fCurrentEntity.columnNumber += this.fCurrentEntity.count;
/* 1258 */         this.fCurrentEntity.baseCharOffset += this.fCurrentEntity.position - this.fCurrentEntity.startPosition;
/* 1259 */         this.fCurrentEntity.position = this.fCurrentEntity.count;
/* 1260 */         this.fCurrentEntity.startPosition = this.fCurrentEntity.count;
/* 1261 */         load(0, true, false);
/* 1262 */         return false;
/*      */       }
/*      */ 
/* 1266 */       int offset = this.fCurrentEntity.position;
/* 1267 */       int c = this.fCurrentEntity.ch[offset];
/* 1268 */       int newlines = 0;
/* 1269 */       if ((c == 10) || ((c == 13) && (this.isExternal)))
/*      */       {
/*      */         do
/*      */         {
/* 1276 */           c = this.fCurrentEntity.ch[(this.fCurrentEntity.position++)];
/* 1277 */           if ((c == 13) && (this.isExternal)) {
/* 1278 */             newlines++;
/* 1279 */             this.fCurrentEntity.lineNumber += 1;
/* 1280 */             this.fCurrentEntity.columnNumber = 1;
/* 1281 */             if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
/* 1282 */               offset = 0;
/* 1283 */               this.fCurrentEntity.position = newlines;
/* 1284 */               if (load(newlines, false, true)) {
/*      */                 break;
/*      */               }
/*      */             }
/* 1288 */             if (this.fCurrentEntity.ch[this.fCurrentEntity.position] == '\n') {
/* 1289 */               this.fCurrentEntity.position += 1;
/* 1290 */               offset++;
/*      */             }
/*      */             else
/*      */             {
/* 1294 */               newlines++;
/*      */             }
/* 1296 */           } else if (c == 10) {
/* 1297 */             newlines++;
/* 1298 */             this.fCurrentEntity.lineNumber += 1;
/* 1299 */             this.fCurrentEntity.columnNumber = 1;
/* 1300 */             if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
/* 1301 */               offset = 0;
/* 1302 */               this.fCurrentEntity.position = newlines;
/* 1303 */               this.fCurrentEntity.count = newlines;
/* 1304 */               if (load(newlines, false, true))
/* 1305 */                 break;
/*      */             }
/*      */           }
/*      */           else {
/* 1309 */             this.fCurrentEntity.position -= 1;
/* 1310 */             break;
/*      */           }
/*      */         }
/* 1312 */         while (this.fCurrentEntity.position < this.fCurrentEntity.count - 1);
/* 1313 */         for (int i = offset; i < this.fCurrentEntity.position; i++) {
/* 1314 */           this.fCurrentEntity.ch[i] = '\n';
/*      */         }
/* 1316 */         int length = this.fCurrentEntity.position - offset;
/* 1317 */         if (this.fCurrentEntity.position == this.fCurrentEntity.count - 1) {
/* 1318 */           buffer.append(this.fCurrentEntity.ch, offset, length);
/*      */ 
/* 1324 */           return true;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1334 */       while (this.fCurrentEntity.position < this.fCurrentEntity.count) {
/* 1335 */         c = this.fCurrentEntity.ch[(this.fCurrentEntity.position++)];
/* 1336 */         if (c == charAt0)
/*      */         {
/* 1338 */           int delimOffset = this.fCurrentEntity.position - 1;
/* 1339 */           for (int i = 1; i < delimLen; i++) {
/* 1340 */             if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
/* 1341 */               this.fCurrentEntity.position -= i;
/* 1342 */               break label974;
/*      */             }
/* 1344 */             c = this.fCurrentEntity.ch[(this.fCurrentEntity.position++)];
/* 1345 */             if (delimiter.charAt(i) != c) {
/* 1346 */               this.fCurrentEntity.position -= i;
/* 1347 */               break;
/*      */             }
/*      */           }
/* 1350 */           if (this.fCurrentEntity.position == delimOffset + delimLen) {
/* 1351 */             done = true;
/*      */           }
/*      */         }
/* 1354 */         else if ((c == 10) || ((this.isExternal) && (c == 13))) {
/* 1355 */           this.fCurrentEntity.position -= 1;
/*      */         }
/* 1357 */         else if (XMLChar.isInvalid(c)) {
/* 1358 */           this.fCurrentEntity.position -= 1;
/* 1359 */           int length = this.fCurrentEntity.position - offset;
/* 1360 */           this.fCurrentEntity.columnNumber += length - newlines;
/* 1361 */           buffer.append(this.fCurrentEntity.ch, offset, length);
/* 1362 */           return true;
/*      */         }
/*      */       }
/* 1365 */       int length = this.fCurrentEntity.position - offset;
/* 1366 */       this.fCurrentEntity.columnNumber += length - newlines;
/* 1367 */       if (done) {
/* 1368 */         length -= delimLen;
/*      */       }
/* 1370 */       buffer.append(this.fCurrentEntity.ch, offset, length);
/*      */     }
/*      */ 
/* 1378 */     while (!done);
/* 1379 */     return !done;
/*      */   }
/*      */ 
/*      */   public boolean skipChar(int c)
/*      */     throws IOException
/*      */   {
/* 1404 */     if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
/* 1405 */       load(0, true, true);
/*      */     }
/*      */ 
/* 1409 */     int cc = this.fCurrentEntity.ch[this.fCurrentEntity.position];
/* 1410 */     if (cc == c) {
/* 1411 */       this.fCurrentEntity.position += 1;
/* 1412 */       if (c == 10) {
/* 1413 */         this.fCurrentEntity.lineNumber += 1;
/* 1414 */         this.fCurrentEntity.columnNumber = 1;
/*      */       } else {
/* 1416 */         this.fCurrentEntity.columnNumber += 1;
/*      */       }
/*      */ 
/* 1423 */       return true;
/* 1424 */     }if ((c == 10) && (cc == 13) && (this.isExternal))
/*      */     {
/* 1426 */       if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
/* 1427 */         invokeListeners(1);
/* 1428 */         this.fCurrentEntity.ch[0] = ((char)cc);
/* 1429 */         load(1, false, false);
/*      */       }
/* 1431 */       this.fCurrentEntity.position += 1;
/* 1432 */       if (this.fCurrentEntity.ch[this.fCurrentEntity.position] == '\n') {
/* 1433 */         this.fCurrentEntity.position += 1;
/*      */       }
/* 1435 */       this.fCurrentEntity.lineNumber += 1;
/* 1436 */       this.fCurrentEntity.columnNumber = 1;
/*      */ 
/* 1442 */       return true;
/*      */     }
/*      */ 
/* 1451 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean isSpace(char ch)
/*      */   {
/* 1456 */     return (ch == ' ') || (ch == '\n') || (ch == '\t') || (ch == '\r');
/*      */   }
/*      */ 
/*      */   public boolean skipSpaces()
/*      */     throws IOException
/*      */   {
/* 1479 */     if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
/* 1480 */       load(0, true, true);
/*      */     }
/*      */ 
/* 1489 */     if (this.fCurrentEntity == null) {
/* 1490 */       return false;
/*      */     }
/*      */ 
/* 1494 */     int c = this.fCurrentEntity.ch[this.fCurrentEntity.position];
/* 1495 */     if (XMLChar.isSpace(c)) {
/*      */       do {
/* 1497 */         boolean entityChanged = false;
/*      */ 
/* 1499 */         if ((c == 10) || ((this.isExternal) && (c == 13))) {
/* 1500 */           this.fCurrentEntity.lineNumber += 1;
/* 1501 */           this.fCurrentEntity.columnNumber = 1;
/* 1502 */           if (this.fCurrentEntity.position == this.fCurrentEntity.count - 1) {
/* 1503 */             invokeListeners(0);
/* 1504 */             this.fCurrentEntity.ch[0] = ((char)c);
/* 1505 */             entityChanged = load(1, true, false);
/* 1506 */             if (!entityChanged)
/*      */             {
/* 1509 */               this.fCurrentEntity.position = 0;
/* 1510 */             } else if (this.fCurrentEntity == null) {
/* 1511 */               return true;
/*      */             }
/*      */           }
/* 1514 */           if ((c == 13) && (this.isExternal))
/*      */           {
/* 1517 */             if (this.fCurrentEntity.ch[(++this.fCurrentEntity.position)] != '\n')
/* 1518 */               this.fCurrentEntity.position -= 1;
/*      */           }
/*      */         }
/*      */         else {
/* 1522 */           this.fCurrentEntity.columnNumber += 1;
/*      */         }
/*      */ 
/* 1525 */         if (!entityChanged) {
/* 1526 */           this.fCurrentEntity.position += 1;
/*      */         }
/*      */ 
/* 1529 */         if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
/* 1530 */           load(0, true, true);
/*      */ 
/* 1539 */           if (this.fCurrentEntity == null) {
/* 1540 */             return true;
/*      */           }
/*      */         }
/*      */       }
/* 1544 */       while (XMLChar.isSpace(c = this.fCurrentEntity.ch[this.fCurrentEntity.position]));
/*      */ 
/* 1550 */       return true;
/*      */     }
/*      */ 
/* 1559 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean arrangeCapacity(int length)
/*      */     throws IOException
/*      */   {
/* 1570 */     return arrangeCapacity(length, false);
/*      */   }
/*      */ 
/*      */   public boolean arrangeCapacity(int length, boolean changeEntity)
/*      */     throws IOException
/*      */   {
/* 1585 */     if (this.fCurrentEntity.count - this.fCurrentEntity.position >= length) {
/* 1586 */       return true;
/*      */     }
/*      */ 
/* 1593 */     boolean entityChanged = false;
/*      */ 
/* 1595 */     while (this.fCurrentEntity.count - this.fCurrentEntity.position < length) {
/* 1596 */       if (this.fCurrentEntity.ch.length - this.fCurrentEntity.position < length) {
/* 1597 */         invokeListeners(0);
/* 1598 */         System.arraycopy(this.fCurrentEntity.ch, this.fCurrentEntity.position, this.fCurrentEntity.ch, 0, this.fCurrentEntity.count - this.fCurrentEntity.position);
/* 1599 */         this.fCurrentEntity.count -= this.fCurrentEntity.position;
/* 1600 */         this.fCurrentEntity.position = 0;
/*      */       }
/*      */ 
/* 1603 */       if (this.fCurrentEntity.count - this.fCurrentEntity.position < length) {
/* 1604 */         int pos = this.fCurrentEntity.position;
/* 1605 */         invokeListeners(pos);
/* 1606 */         entityChanged = load(this.fCurrentEntity.count, changeEntity, false);
/* 1607 */         this.fCurrentEntity.position = pos;
/* 1608 */         if (entityChanged)
/*      */         {
/*      */           break;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1619 */     if (this.fCurrentEntity.count - this.fCurrentEntity.position >= length) {
/* 1620 */       return true;
/*      */     }
/* 1622 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean skipString(String s)
/*      */     throws IOException
/*      */   {
/* 1641 */     int length = s.length();
/*      */ 
/* 1644 */     if (arrangeCapacity(length, false)) {
/* 1645 */       int beforeSkip = this.fCurrentEntity.position;
/* 1646 */       int afterSkip = this.fCurrentEntity.position + length - 1;
/*      */ 
/* 1653 */       int i = length - 1;
/*      */ 
/* 1655 */       while (s.charAt(i--) == this.fCurrentEntity.ch[afterSkip]) {
/* 1656 */         if (afterSkip-- == beforeSkip) {
/* 1657 */           this.fCurrentEntity.position += length;
/* 1658 */           this.fCurrentEntity.columnNumber += length;
/* 1659 */           return true;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1664 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean skipString(char[] s) throws IOException
/*      */   {
/* 1669 */     int length = s.length;
/*      */ 
/* 1671 */     if (arrangeCapacity(length, false)) {
/* 1672 */       int beforeSkip = this.fCurrentEntity.position;
/* 1673 */       int afterSkip = this.fCurrentEntity.position + length;
/*      */ 
/* 1680 */       for (int i = 0; i < length; i++) {
/* 1681 */         if (this.fCurrentEntity.ch[(beforeSkip++)] != s[i]) {
/* 1682 */           return false;
/*      */         }
/*      */       }
/* 1685 */       this.fCurrentEntity.position += length;
/* 1686 */       this.fCurrentEntity.columnNumber += length;
/* 1687 */       return true;
/*      */     }
/*      */ 
/* 1691 */     return false;
/*      */   }
/*      */ 
/*      */   final boolean load(int offset, boolean changeEntity, boolean notify)
/*      */     throws IOException
/*      */   {
/* 1724 */     if (notify) {
/* 1725 */       invokeListeners(offset);
/*      */     }
/*      */ 
/* 1728 */     this.fCurrentEntity.fTotalCountTillLastLoad += this.fCurrentEntity.fLastCount;
/*      */ 
/* 1730 */     int length = this.fCurrentEntity.ch.length - offset;
/* 1731 */     if ((!this.fCurrentEntity.mayReadChunks) && (length > 64)) {
/* 1732 */       length = 64;
/*      */     }
/*      */ 
/* 1735 */     int count = this.fCurrentEntity.reader.read(this.fCurrentEntity.ch, offset, length);
/*      */ 
/* 1739 */     boolean entityChanged = false;
/* 1740 */     if (count != -1) {
/* 1741 */       if (count != 0)
/*      */       {
/* 1743 */         this.fCurrentEntity.fLastCount = count;
/* 1744 */         this.fCurrentEntity.count = (count + offset);
/* 1745 */         this.fCurrentEntity.position = offset;
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 1750 */       this.fCurrentEntity.count = offset;
/* 1751 */       this.fCurrentEntity.position = offset;
/* 1752 */       entityChanged = true;
/*      */ 
/* 1754 */       if (changeEntity)
/*      */       {
/* 1756 */         this.fEntityManager.endEntity();
/*      */ 
/* 1758 */         if (this.fCurrentEntity == null) {
/* 1759 */           throw END_OF_DOCUMENT_ENTITY;
/*      */         }
/*      */ 
/* 1762 */         if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
/* 1763 */           load(0, true, false);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1774 */     return entityChanged;
/*      */   }
/*      */ 
/*      */   protected Reader createReader(InputStream inputStream, String encoding, Boolean isBigEndian)
/*      */     throws IOException
/*      */   {
/* 1798 */     if (encoding == null) {
/* 1799 */       encoding = "UTF-8";
/*      */     }
/*      */ 
/* 1803 */     String ENCODING = encoding.toUpperCase(Locale.ENGLISH);
/* 1804 */     if (ENCODING.equals("UTF-8"))
/*      */     {
/* 1808 */       return new UTF8Reader(inputStream, this.fCurrentEntity.fBufferSize, this.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210"), this.fErrorReporter.getLocale());
/*      */     }
/* 1810 */     if (ENCODING.equals("US-ASCII"))
/*      */     {
/* 1814 */       return new ASCIIReader(inputStream, this.fCurrentEntity.fBufferSize, this.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210"), this.fErrorReporter.getLocale());
/*      */     }
/* 1816 */     if (ENCODING.equals("ISO-10646-UCS-4")) {
/* 1817 */       if (isBigEndian != null) {
/* 1818 */         boolean isBE = isBigEndian.booleanValue();
/* 1819 */         if (isBE) {
/* 1820 */           return new UCSReader(inputStream, (short)8);
/*      */         }
/* 1822 */         return new UCSReader(inputStream, (short)4);
/*      */       }
/*      */ 
/* 1825 */       this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "EncodingByteOrderUnsupported", new Object[] { encoding }, (short)2);
/*      */     }
/*      */ 
/* 1831 */     if (ENCODING.equals("ISO-10646-UCS-2")) {
/* 1832 */       if (isBigEndian != null) {
/* 1833 */         boolean isBE = isBigEndian.booleanValue();
/* 1834 */         if (isBE) {
/* 1835 */           return new UCSReader(inputStream, (short)2);
/*      */         }
/* 1837 */         return new UCSReader(inputStream, (short)1);
/*      */       }
/*      */ 
/* 1840 */       this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "EncodingByteOrderUnsupported", new Object[] { encoding }, (short)2);
/*      */     }
/*      */ 
/* 1848 */     boolean validIANA = XMLChar.isValidIANAEncoding(encoding);
/* 1849 */     boolean validJava = XMLChar.isValidJavaEncoding(encoding);
/* 1850 */     if ((!validIANA) || ((this.fAllowJavaEncodings) && (!validJava))) {
/* 1851 */       this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "EncodingDeclInvalid", new Object[] { encoding }, (short)2);
/*      */ 
/* 1863 */       encoding = "ISO-8859-1";
/*      */     }
/*      */ 
/* 1867 */     String javaEncoding = EncodingMap.getIANA2JavaMapping(ENCODING);
/* 1868 */     if (javaEncoding == null) {
/* 1869 */       if (this.fAllowJavaEncodings) {
/* 1870 */         javaEncoding = encoding;
/*      */       } else {
/* 1872 */         this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "EncodingDeclInvalid", new Object[] { encoding }, (short)2);
/*      */ 
/* 1877 */         javaEncoding = "ISO8859_1";
/*      */       }
/*      */     }
/* 1880 */     else if (javaEncoding.equals("ASCII"))
/*      */     {
/* 1884 */       return new ASCIIReader(inputStream, this.fBufferSize, this.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210"), this.fErrorReporter.getLocale());
/*      */     }
/*      */ 
/* 1894 */     return new InputStreamReader(inputStream, javaEncoding);
/*      */   }
/*      */ 
/*      */   protected Object[] getEncodingName(byte[] b4, int count)
/*      */   {
/* 1910 */     if (count < 2) {
/* 1911 */       return new Object[] { "UTF-8", null };
/*      */     }
/*      */ 
/* 1915 */     int b0 = b4[0] & 0xFF;
/* 1916 */     int b1 = b4[1] & 0xFF;
/* 1917 */     if ((b0 == 254) && (b1 == 255))
/*      */     {
/* 1919 */       return new Object[] { "UTF-16BE", new Boolean(true) };
/*      */     }
/* 1921 */     if ((b0 == 255) && (b1 == 254))
/*      */     {
/* 1923 */       return new Object[] { "UTF-16LE", new Boolean(false) };
/*      */     }
/*      */ 
/* 1928 */     if (count < 3) {
/* 1929 */       return new Object[] { "UTF-8", null };
/*      */     }
/*      */ 
/* 1933 */     int b2 = b4[2] & 0xFF;
/* 1934 */     if ((b0 == 239) && (b1 == 187) && (b2 == 191)) {
/* 1935 */       return new Object[] { "UTF-8", null };
/*      */     }
/*      */ 
/* 1940 */     if (count < 4) {
/* 1941 */       return new Object[] { "UTF-8", null };
/*      */     }
/*      */ 
/* 1945 */     int b3 = b4[3] & 0xFF;
/* 1946 */     if ((b0 == 0) && (b1 == 0) && (b2 == 0) && (b3 == 60))
/*      */     {
/* 1948 */       return new Object[] { "ISO-10646-UCS-4", new Boolean(true) };
/*      */     }
/* 1950 */     if ((b0 == 60) && (b1 == 0) && (b2 == 0) && (b3 == 0))
/*      */     {
/* 1952 */       return new Object[] { "ISO-10646-UCS-4", new Boolean(false) };
/*      */     }
/* 1954 */     if ((b0 == 0) && (b1 == 0) && (b2 == 60) && (b3 == 0))
/*      */     {
/* 1957 */       return new Object[] { "ISO-10646-UCS-4", null };
/*      */     }
/* 1959 */     if ((b0 == 0) && (b1 == 60) && (b2 == 0) && (b3 == 0))
/*      */     {
/* 1962 */       return new Object[] { "ISO-10646-UCS-4", null };
/*      */     }
/* 1964 */     if ((b0 == 0) && (b1 == 60) && (b2 == 0) && (b3 == 63))
/*      */     {
/* 1968 */       return new Object[] { "UTF-16BE", new Boolean(true) };
/*      */     }
/* 1970 */     if ((b0 == 60) && (b1 == 0) && (b2 == 63) && (b3 == 0))
/*      */     {
/* 1973 */       return new Object[] { "UTF-16LE", new Boolean(false) };
/*      */     }
/* 1975 */     if ((b0 == 76) && (b1 == 111) && (b2 == 167) && (b3 == 148))
/*      */     {
/* 1978 */       return new Object[] { "CP037", null };
/*      */     }
/*      */ 
/* 1982 */     return new Object[] { "UTF-8", null };
/*      */   }
/*      */ 
/*      */   final void print()
/*      */   {
/*      */   }
/*      */ 
/*      */   public void registerListener(XMLBufferListener listener)
/*      */   {
/* 2052 */     if (!this.listeners.contains(listener))
/* 2053 */       this.listeners.add(listener);
/*      */   }
/*      */ 
/*      */   public void invokeListeners(int loadPos)
/*      */   {
/* 2061 */     for (int i = 0; i < this.listeners.size(); i++) {
/* 2062 */       XMLBufferListener listener = (XMLBufferListener)this.listeners.get(i);
/* 2063 */       listener.refresh(loadPos);
/*      */     }
/*      */   }
/*      */ 
/*      */   public final boolean skipDeclSpaces()
/*      */     throws IOException
/*      */   {
/* 2092 */     if (this.fCurrentEntity.position == this.fCurrentEntity.count) {
/* 2093 */       load(0, true, false);
/*      */     }
/*      */ 
/* 2097 */     int c = this.fCurrentEntity.ch[this.fCurrentEntity.position];
/* 2098 */     if (XMLChar.isSpace(c)) {
/* 2099 */       boolean external = this.fCurrentEntity.isExternal();
/*      */       do {
/* 2101 */         boolean entityChanged = false;
/*      */ 
/* 2103 */         if ((c == 10) || ((external) && (c == 13))) {
/* 2104 */           this.fCurrentEntity.lineNumber += 1;
/* 2105 */           this.fCurrentEntity.columnNumber = 1;
/* 2106 */           if (this.fCurrentEntity.position == this.fCurrentEntity.count - 1) {
/* 2107 */             this.fCurrentEntity.ch[0] = ((char)c);
/* 2108 */             entityChanged = load(1, true, false);
/* 2109 */             if (!entityChanged)
/*      */             {
/* 2112 */               this.fCurrentEntity.position = 0;
/*      */             }
/*      */           }
/* 2114 */           if ((c == 13) && (external))
/*      */           {
/* 2117 */             if (this.fCurrentEntity.ch[(++this.fCurrentEntity.position)] != '\n') {
/* 2118 */               this.fCurrentEntity.position -= 1;
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/* 2130 */           this.fCurrentEntity.columnNumber += 1;
/*      */         }
/*      */ 
/* 2133 */         if (!entityChanged)
/* 2134 */           this.fCurrentEntity.position += 1;
/* 2135 */         if (this.fCurrentEntity.position == this.fCurrentEntity.count)
/* 2136 */           load(0, true, false);
/*      */       }
/* 2138 */       while (XMLChar.isSpace(c = this.fCurrentEntity.ch[this.fCurrentEntity.position]));
/*      */ 
/* 2144 */       return true;
/*      */     }
/*      */ 
/* 2153 */     return false;
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  119 */     for (int i = 65; i <= 90; i++) {
/*  120 */       VALID_NAMES[i] = true;
/*      */     }
/*  122 */     for (int i = 97; i <= 122; i++) {
/*  123 */       VALID_NAMES[i] = true;
/*      */     }
/*  125 */     for (int i = 48; i <= 57; i++) {
/*  126 */       VALID_NAMES[i] = true;
/*      */     }
/*  128 */     VALID_NAMES[45] = true;
/*  129 */     VALID_NAMES[46] = true;
/*  130 */     VALID_NAMES[58] = true;
/*  131 */     VALID_NAMES[95] = true;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.XMLEntityScanner
 * JD-Core Version:    0.6.2
 */