/*      */ package com.sun.org.apache.xerces.internal.impl.dtd;
/*      */ 
/*      */ import com.sun.org.apache.xerces.internal.impl.dtd.models.CMAny;
/*      */ import com.sun.org.apache.xerces.internal.impl.dtd.models.CMBinOp;
/*      */ import com.sun.org.apache.xerces.internal.impl.dtd.models.CMLeaf;
/*      */ import com.sun.org.apache.xerces.internal.impl.dtd.models.CMNode;
/*      */ import com.sun.org.apache.xerces.internal.impl.dtd.models.CMUniOp;
/*      */ import com.sun.org.apache.xerces.internal.impl.dtd.models.ContentModelValidator;
/*      */ import com.sun.org.apache.xerces.internal.impl.dtd.models.DFAContentModel;
/*      */ import com.sun.org.apache.xerces.internal.impl.dtd.models.MixedContentModel;
/*      */ import com.sun.org.apache.xerces.internal.impl.dtd.models.SimpleContentModel;
/*      */ import com.sun.org.apache.xerces.internal.impl.dv.DatatypeValidator;
/*      */ import com.sun.org.apache.xerces.internal.impl.validation.EntityState;
/*      */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*      */ import com.sun.org.apache.xerces.internal.xni.Augmentations;
/*      */ import com.sun.org.apache.xerces.internal.xni.QName;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLDTDHandler;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLLocator;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLString;
/*      */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*      */ import com.sun.org.apache.xerces.internal.xni.grammars.Grammar;
/*      */ import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarDescription;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLDTDContentModelSource;
/*      */ import com.sun.org.apache.xerces.internal.xni.parser.XMLDTDSource;
/*      */ import java.io.PrintStream;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Hashtable;
/*      */ 
/*      */ public class DTDGrammar
/*      */   implements XMLDTDHandler, XMLDTDContentModelHandler, EntityState, Grammar
/*      */ {
/*      */   public static final int TOP_LEVEL_SCOPE = -1;
/*      */   private static final int CHUNK_SHIFT = 8;
/*      */   private static final int CHUNK_SIZE = 256;
/*      */   private static final int CHUNK_MASK = 255;
/*      */   private static final int INITIAL_CHUNK_COUNT = 4;
/*      */   private static final short LIST_FLAG = 128;
/*      */   private static final short LIST_MASK = -129;
/*      */   private static final boolean DEBUG = false;
/*  145 */   protected XMLDTDSource fDTDSource = null;
/*  146 */   protected XMLDTDContentModelSource fDTDContentModelSource = null;
/*      */   protected int fCurrentElementIndex;
/*      */   protected int fCurrentAttributeIndex;
/*  155 */   protected boolean fReadingExternalDTD = false;
/*      */   private SymbolTable fSymbolTable;
/*  161 */   protected XMLDTDDescription fGrammarDescription = null;
/*      */ 
/*  166 */   private int fElementDeclCount = 0;
/*      */ 
/*  169 */   private QName[][] fElementDeclName = new QName[4][];
/*      */ 
/*  175 */   private short[][] fElementDeclType = new short[4][];
/*      */ 
/*  181 */   private int[][] fElementDeclContentSpecIndex = new int[4][];
/*      */ 
/*  187 */   private ContentModelValidator[][] fElementDeclContentModelValidator = new ContentModelValidator[4][];
/*      */ 
/*  190 */   private int[][] fElementDeclFirstAttributeDeclIndex = new int[4][];
/*      */ 
/*  193 */   private int[][] fElementDeclLastAttributeDeclIndex = new int[4][];
/*      */ 
/*  198 */   private int fAttributeDeclCount = 0;
/*      */ 
/*  201 */   private QName[][] fAttributeDeclName = new QName[4][];
/*      */ 
/*  204 */   private boolean fIsImmutable = false;
/*      */ 
/*  210 */   private short[][] fAttributeDeclType = new short[4][];
/*      */ 
/*  213 */   private String[][][] fAttributeDeclEnumeration = new String[4][][];
/*  214 */   private short[][] fAttributeDeclDefaultType = new short[4][];
/*  215 */   private DatatypeValidator[][] fAttributeDeclDatatypeValidator = new DatatypeValidator[4][];
/*  216 */   private String[][] fAttributeDeclDefaultValue = new String[4][];
/*  217 */   private String[][] fAttributeDeclNonNormalizedDefaultValue = new String[4][];
/*  218 */   private int[][] fAttributeDeclNextAttributeDeclIndex = new int[4][];
/*      */ 
/*  226 */   private int fContentSpecCount = 0;
/*  227 */   private short[][] fContentSpecType = new short[4][];
/*  228 */   private Object[][] fContentSpecValue = new Object[4][];
/*  229 */   private Object[][] fContentSpecOtherValue = new Object[4][];
/*      */ 
/*  233 */   private int fEntityCount = 0;
/*  234 */   private String[][] fEntityName = new String[4][];
/*  235 */   private String[][] fEntityValue = new String[4][];
/*  236 */   private String[][] fEntityPublicId = new String[4][];
/*  237 */   private String[][] fEntitySystemId = new String[4][];
/*  238 */   private String[][] fEntityBaseSystemId = new String[4][];
/*  239 */   private String[][] fEntityNotation = new String[4][];
/*  240 */   private byte[][] fEntityIsPE = new byte[4][];
/*  241 */   private byte[][] fEntityInExternal = new byte[4][];
/*      */ 
/*  245 */   private int fNotationCount = 0;
/*  246 */   private String[][] fNotationName = new String[4][];
/*  247 */   private String[][] fNotationPublicId = new String[4][];
/*  248 */   private String[][] fNotationSystemId = new String[4][];
/*  249 */   private String[][] fNotationBaseSystemId = new String[4][];
/*      */ 
/*  254 */   private QNameHashtable fElementIndexMap = new QNameHashtable();
/*      */ 
/*  257 */   private QNameHashtable fEntityIndexMap = new QNameHashtable();
/*      */ 
/*  260 */   private QNameHashtable fNotationIndexMap = new QNameHashtable();
/*      */   private boolean fMixed;
/*  268 */   private final QName fQName = new QName();
/*      */ 
/*  271 */   private final QName fQName2 = new QName();
/*      */ 
/*  274 */   protected final XMLAttributeDecl fAttributeDecl = new XMLAttributeDecl();
/*      */ 
/*  278 */   private int fLeafCount = 0;
/*  279 */   private int fEpsilonIndex = -1;
/*      */ 
/*  282 */   private XMLElementDecl fElementDecl = new XMLElementDecl();
/*      */ 
/*  285 */   private XMLEntityDecl fEntityDecl = new XMLEntityDecl();
/*      */ 
/*  288 */   private XMLSimpleType fSimpleType = new XMLSimpleType();
/*      */ 
/*  291 */   private XMLContentSpec fContentSpec = new XMLContentSpec();
/*      */ 
/*  294 */   Hashtable fElementDeclTab = new Hashtable();
/*      */ 
/*  297 */   private short[] fOpStack = null;
/*      */ 
/*  300 */   private int[] fNodeIndexStack = null;
/*      */ 
/*  303 */   private int[] fPrevNodeIndexStack = null;
/*      */ 
/*  306 */   private int fDepth = 0;
/*      */ 
/*  309 */   private boolean[] fPEntityStack = new boolean[4];
/*  310 */   private int fPEDepth = 0;
/*      */ 
/*  315 */   private int[][] fElementDeclIsExternal = new int[4][];
/*      */ 
/*  321 */   private int[][] fAttributeDeclIsExternal = new int[4][];
/*      */ 
/*  325 */   int valueIndex = -1;
/*  326 */   int prevNodeIndex = -1;
/*  327 */   int nodeIndex = -1;
/*      */ 
/*      */   public DTDGrammar(SymbolTable symbolTable, XMLDTDDescription desc)
/*      */   {
/*  335 */     this.fSymbolTable = symbolTable;
/*  336 */     this.fGrammarDescription = desc;
/*      */   }
/*      */ 
/*      */   public XMLGrammarDescription getGrammarDescription()
/*      */   {
/*  343 */     return this.fGrammarDescription;
/*      */   }
/*      */ 
/*      */   public boolean getElementDeclIsExternal(int elementDeclIndex)
/*      */   {
/*  357 */     if (elementDeclIndex < 0) {
/*  358 */       return false;
/*      */     }
/*      */ 
/*  361 */     int chunk = elementDeclIndex >> 8;
/*  362 */     int index = elementDeclIndex & 0xFF;
/*  363 */     return this.fElementDeclIsExternal[chunk][index] != 0;
/*      */   }
/*      */ 
/*      */   public boolean getAttributeDeclIsExternal(int attributeDeclIndex)
/*      */   {
/*  374 */     if (attributeDeclIndex < 0) {
/*  375 */       return false;
/*      */     }
/*      */ 
/*  378 */     int chunk = attributeDeclIndex >> 8;
/*  379 */     int index = attributeDeclIndex & 0xFF;
/*  380 */     return this.fAttributeDeclIsExternal[chunk][index] != 0;
/*      */   }
/*      */ 
/*      */   public int getAttributeDeclIndex(int elementDeclIndex, String attributeDeclName) {
/*  384 */     if (elementDeclIndex == -1) {
/*  385 */       return -1;
/*      */     }
/*  387 */     int attDefIndex = getFirstAttributeDeclIndex(elementDeclIndex);
/*  388 */     while (attDefIndex != -1) {
/*  389 */       getAttributeDecl(attDefIndex, this.fAttributeDecl);
/*      */ 
/*  391 */       if ((this.fAttributeDecl.name.rawname == attributeDeclName) || (attributeDeclName.equals(this.fAttributeDecl.name.rawname)))
/*      */       {
/*  393 */         return attDefIndex;
/*      */       }
/*  395 */       attDefIndex = getNextAttributeDeclIndex(attDefIndex);
/*      */     }
/*  397 */     return -1;
/*      */   }
/*      */ 
/*      */   public void startDTD(XMLLocator locator, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  420 */     this.fOpStack = null;
/*  421 */     this.fNodeIndexStack = null;
/*  422 */     this.fPrevNodeIndexStack = null;
/*      */   }
/*      */ 
/*      */   public void startParameterEntity(String name, XMLResourceIdentifier identifier, String encoding, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  451 */     if (this.fPEDepth == this.fPEntityStack.length) {
/*  452 */       boolean[] entityarray = new boolean[this.fPEntityStack.length * 2];
/*  453 */       System.arraycopy(this.fPEntityStack, 0, entityarray, 0, this.fPEntityStack.length);
/*  454 */       this.fPEntityStack = entityarray;
/*      */     }
/*  456 */     this.fPEntityStack[this.fPEDepth] = this.fReadingExternalDTD;
/*  457 */     this.fPEDepth += 1;
/*      */   }
/*      */ 
/*      */   public void startExternalSubset(XMLResourceIdentifier identifier, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  471 */     this.fReadingExternalDTD = true;
/*      */   }
/*      */ 
/*      */   public void endParameterEntity(String name, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  490 */     this.fPEDepth -= 1;
/*  491 */     this.fReadingExternalDTD = this.fPEntityStack[this.fPEDepth];
/*      */   }
/*      */ 
/*      */   public void endExternalSubset(Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  504 */     this.fReadingExternalDTD = false;
/*      */   }
/*      */ 
/*      */   public void elementDecl(String name, String contentModel, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  519 */     XMLElementDecl tmpElementDecl = (XMLElementDecl)this.fElementDeclTab.get(name);
/*      */ 
/*  522 */     if (tmpElementDecl != null) {
/*  523 */       if (tmpElementDecl.type == -1) {
/*  524 */         this.fCurrentElementIndex = getElementDeclIndex(name);
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*  532 */       this.fCurrentElementIndex = createElementDecl();
/*      */     }
/*      */ 
/*  535 */     XMLElementDecl elementDecl = new XMLElementDecl();
/*      */ 
/*  537 */     this.fQName.setValues(null, name, name, null);
/*      */ 
/*  539 */     elementDecl.name.setValues(this.fQName);
/*      */ 
/*  541 */     elementDecl.contentModelValidator = null;
/*  542 */     elementDecl.scope = -1;
/*  543 */     if (contentModel.equals("EMPTY")) {
/*  544 */       elementDecl.type = 1;
/*      */     }
/*  546 */     else if (contentModel.equals("ANY")) {
/*  547 */       elementDecl.type = 0;
/*      */     }
/*  549 */     else if (contentModel.startsWith("(")) {
/*  550 */       if (contentModel.indexOf("#PCDATA") > 0) {
/*  551 */         elementDecl.type = 2;
/*      */       }
/*      */       else {
/*  554 */         elementDecl.type = 3;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  560 */     this.fElementDeclTab.put(name, elementDecl);
/*      */ 
/*  562 */     this.fElementDecl = elementDecl;
/*  563 */     addContentSpecToElement(elementDecl);
/*      */ 
/*  570 */     setElementDecl(this.fCurrentElementIndex, this.fElementDecl);
/*      */ 
/*  572 */     int chunk = this.fCurrentElementIndex >> 8;
/*  573 */     int index = this.fCurrentElementIndex & 0xFF;
/*  574 */     ensureElementDeclCapacity(chunk);
/*  575 */     this.fElementDeclIsExternal[chunk][index] = ((this.fReadingExternalDTD) || (this.fPEDepth > 0) ? 1 : 0);
/*      */   }
/*      */ 
/*      */   public void attributeDecl(String elementName, String attributeName, String type, String[] enumeration, String defaultType, XMLString defaultValue, XMLString nonNormalizedDefaultValue, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  609 */     if (!this.fElementDeclTab.containsKey(elementName))
/*      */     {
/*  615 */       this.fCurrentElementIndex = createElementDecl();
/*      */ 
/*  617 */       XMLElementDecl elementDecl = new XMLElementDecl();
/*  618 */       elementDecl.name.setValues(null, elementName, elementName, null);
/*      */ 
/*  620 */       elementDecl.scope = -1;
/*      */ 
/*  623 */       this.fElementDeclTab.put(elementName, elementDecl);
/*      */ 
/*  626 */       setElementDecl(this.fCurrentElementIndex, elementDecl);
/*      */     }
/*      */ 
/*  630 */     int elementIndex = getElementDeclIndex(elementName);
/*      */ 
/*  634 */     if (getAttributeDeclIndex(elementIndex, attributeName) != -1) {
/*  635 */       return;
/*      */     }
/*      */ 
/*  638 */     this.fCurrentAttributeIndex = createAttributeDecl();
/*      */ 
/*  640 */     this.fSimpleType.clear();
/*  641 */     if (defaultType != null) {
/*  642 */       if (defaultType.equals("#FIXED"))
/*  643 */         this.fSimpleType.defaultType = 1;
/*  644 */       else if (defaultType.equals("#IMPLIED"))
/*  645 */         this.fSimpleType.defaultType = 0;
/*  646 */       else if (defaultType.equals("#REQUIRED")) {
/*  647 */         this.fSimpleType.defaultType = 2;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  653 */     this.fSimpleType.defaultValue = (defaultValue != null ? defaultValue.toString() : null);
/*  654 */     this.fSimpleType.nonNormalizedDefaultValue = (nonNormalizedDefaultValue != null ? nonNormalizedDefaultValue.toString() : null);
/*  655 */     this.fSimpleType.enumeration = enumeration;
/*      */ 
/*  657 */     if (type.equals("CDATA")) {
/*  658 */       this.fSimpleType.type = 0;
/*      */     }
/*  660 */     else if (type.equals("ID")) {
/*  661 */       this.fSimpleType.type = 3;
/*      */     }
/*  663 */     else if (type.startsWith("IDREF")) {
/*  664 */       this.fSimpleType.type = 4;
/*  665 */       if (type.indexOf("S") > 0) {
/*  666 */         this.fSimpleType.list = true;
/*      */       }
/*      */     }
/*  669 */     else if (type.equals("ENTITIES")) {
/*  670 */       this.fSimpleType.type = 1;
/*  671 */       this.fSimpleType.list = true;
/*      */     }
/*  673 */     else if (type.equals("ENTITY")) {
/*  674 */       this.fSimpleType.type = 1;
/*      */     }
/*  676 */     else if (type.equals("NMTOKENS")) {
/*  677 */       this.fSimpleType.type = 5;
/*  678 */       this.fSimpleType.list = true;
/*      */     }
/*  680 */     else if (type.equals("NMTOKEN")) {
/*  681 */       this.fSimpleType.type = 5;
/*      */     }
/*  683 */     else if (type.startsWith("NOTATION")) {
/*  684 */       this.fSimpleType.type = 6;
/*      */     }
/*  686 */     else if (type.startsWith("ENUMERATION")) {
/*  687 */       this.fSimpleType.type = 2;
/*      */     }
/*      */     else
/*      */     {
/*  691 */       System.err.println("!!! unknown attribute type " + type);
/*      */     }
/*      */ 
/*  697 */     this.fQName.setValues(null, attributeName, attributeName, null);
/*  698 */     this.fAttributeDecl.setValues(this.fQName, this.fSimpleType, false);
/*      */ 
/*  700 */     setAttributeDecl(elementIndex, this.fCurrentAttributeIndex, this.fAttributeDecl);
/*      */ 
/*  702 */     int chunk = this.fCurrentAttributeIndex >> 8;
/*  703 */     int index = this.fCurrentAttributeIndex & 0xFF;
/*  704 */     ensureAttributeDeclCapacity(chunk);
/*  705 */     this.fAttributeDeclIsExternal[chunk][index] = ((this.fReadingExternalDTD) || (this.fPEDepth > 0) ? 1 : 0);
/*      */   }
/*      */ 
/*      */   public void internalEntityDecl(String name, XMLString text, XMLString nonNormalizedText, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  728 */     int entityIndex = getEntityDeclIndex(name);
/*  729 */     if (entityIndex == -1) {
/*  730 */       entityIndex = createEntityDecl();
/*  731 */       boolean isPE = name.startsWith("%");
/*  732 */       boolean inExternal = (this.fReadingExternalDTD) || (this.fPEDepth > 0);
/*  733 */       XMLEntityDecl entityDecl = new XMLEntityDecl();
/*  734 */       entityDecl.setValues(name, null, null, null, null, text.toString(), isPE, inExternal);
/*      */ 
/*  737 */       setEntityDecl(entityIndex, entityDecl);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void externalEntityDecl(String name, XMLResourceIdentifier identifier, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  758 */     int entityIndex = getEntityDeclIndex(name);
/*  759 */     if (entityIndex == -1) {
/*  760 */       entityIndex = createEntityDecl();
/*  761 */       boolean isPE = name.startsWith("%");
/*  762 */       boolean inExternal = (this.fReadingExternalDTD) || (this.fPEDepth > 0);
/*      */ 
/*  764 */       XMLEntityDecl entityDecl = new XMLEntityDecl();
/*  765 */       entityDecl.setValues(name, identifier.getPublicId(), identifier.getLiteralSystemId(), identifier.getBaseSystemId(), null, null, isPE, inExternal);
/*      */ 
/*  769 */       setEntityDecl(entityIndex, entityDecl);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void unparsedEntityDecl(String name, XMLResourceIdentifier identifier, String notation, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  788 */     XMLEntityDecl entityDecl = new XMLEntityDecl();
/*  789 */     boolean isPE = name.startsWith("%");
/*  790 */     boolean inExternal = (this.fReadingExternalDTD) || (this.fPEDepth > 0);
/*      */ 
/*  792 */     entityDecl.setValues(name, identifier.getPublicId(), identifier.getLiteralSystemId(), identifier.getBaseSystemId(), notation, null, isPE, inExternal);
/*      */ 
/*  795 */     int entityIndex = getEntityDeclIndex(name);
/*  796 */     if (entityIndex == -1) {
/*  797 */       entityIndex = createEntityDecl();
/*  798 */       setEntityDecl(entityIndex, entityDecl);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void notationDecl(String name, XMLResourceIdentifier identifier, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  816 */     XMLNotationDecl notationDecl = new XMLNotationDecl();
/*  817 */     notationDecl.setValues(name, identifier.getPublicId(), identifier.getLiteralSystemId(), identifier.getBaseSystemId());
/*      */ 
/*  819 */     int notationIndex = getNotationDeclIndex(name);
/*  820 */     if (notationIndex == -1) {
/*  821 */       notationIndex = createNotationDecl();
/*  822 */       setNotationDecl(notationIndex, notationDecl);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void endDTD(Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  835 */     this.fIsImmutable = true;
/*      */ 
/*  837 */     if (this.fGrammarDescription.getRootName() == null)
/*      */     {
/*  839 */       int index = 0;
/*  840 */       String currName = null;
/*  841 */       int size = this.fElementDeclCount;
/*  842 */       ArrayList elements = new ArrayList(size);
/*  843 */       for (int i = 0; i < size; i++) {
/*  844 */         int chunk = i >> 8;
/*  845 */         index = i & 0xFF;
/*  846 */         currName = this.fElementDeclName[chunk][index].rawname;
/*  847 */         elements.add(currName);
/*      */       }
/*  849 */       this.fGrammarDescription.setPossibleRoots(elements);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setDTDSource(XMLDTDSource source)
/*      */   {
/*  855 */     this.fDTDSource = source;
/*      */   }
/*      */ 
/*      */   public XMLDTDSource getDTDSource()
/*      */   {
/*  860 */     return this.fDTDSource;
/*      */   }
/*      */ 
/*      */   public void textDecl(String version, String encoding, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void comment(XMLString text, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void processingInstruction(String target, XMLString data, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void startAttlist(String elementName, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void endAttlist(Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void startConditional(short type, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void ignoredCharacters(XMLString text, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void endConditional(Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void setDTDContentModelSource(XMLDTDContentModelSource source)
/*      */   {
/*  971 */     this.fDTDContentModelSource = source;
/*      */   }
/*      */ 
/*      */   public XMLDTDContentModelSource getDTDContentModelSource()
/*      */   {
/*  976 */     return this.fDTDContentModelSource;
/*      */   }
/*      */ 
/*      */   public void startContentModel(String elementName, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*  992 */     XMLElementDecl elementDecl = (XMLElementDecl)this.fElementDeclTab.get(elementName);
/*  993 */     if (elementDecl != null) {
/*  994 */       this.fElementDecl = elementDecl;
/*      */     }
/*  996 */     this.fDepth = 0;
/*  997 */     initializeContentModelStack();
/*      */   }
/*      */ 
/*      */   public void startGroup(Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1015 */     this.fDepth += 1;
/* 1016 */     initializeContentModelStack();
/* 1017 */     this.fMixed = false;
/*      */   }
/*      */ 
/*      */   public void pcdata(Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1033 */     this.fMixed = true;
/*      */   }
/*      */ 
/*      */   public void element(String elementName, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1046 */     if (this.fMixed) {
/* 1047 */       if (this.fNodeIndexStack[this.fDepth] == -1) {
/* 1048 */         this.fNodeIndexStack[this.fDepth] = addUniqueLeafNode(elementName);
/*      */       }
/*      */       else {
/* 1051 */         this.fNodeIndexStack[this.fDepth] = addContentSpecNode(4, this.fNodeIndexStack[this.fDepth], addUniqueLeafNode(elementName));
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 1057 */       this.fNodeIndexStack[this.fDepth] = addContentSpecNode(0, elementName);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void separator(short separator, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1075 */     if (!this.fMixed)
/* 1076 */       if ((this.fOpStack[this.fDepth] != 5) && (separator == 0)) {
/* 1077 */         if (this.fPrevNodeIndexStack[this.fDepth] != -1) {
/* 1078 */           this.fNodeIndexStack[this.fDepth] = addContentSpecNode(this.fOpStack[this.fDepth], this.fPrevNodeIndexStack[this.fDepth], this.fNodeIndexStack[this.fDepth]);
/*      */         }
/* 1080 */         this.fPrevNodeIndexStack[this.fDepth] = this.fNodeIndexStack[this.fDepth];
/* 1081 */         this.fOpStack[this.fDepth] = 4;
/* 1082 */       } else if ((this.fOpStack[this.fDepth] != 4) && (separator == 1)) {
/* 1083 */         if (this.fPrevNodeIndexStack[this.fDepth] != -1) {
/* 1084 */           this.fNodeIndexStack[this.fDepth] = addContentSpecNode(this.fOpStack[this.fDepth], this.fPrevNodeIndexStack[this.fDepth], this.fNodeIndexStack[this.fDepth]);
/*      */         }
/* 1086 */         this.fPrevNodeIndexStack[this.fDepth] = this.fNodeIndexStack[this.fDepth];
/* 1087 */         this.fOpStack[this.fDepth] = 5;
/*      */       }
/*      */   }
/*      */ 
/*      */   public void occurrence(short occurrence, Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1109 */     if (!this.fMixed)
/* 1110 */       if (occurrence == 2)
/* 1111 */         this.fNodeIndexStack[this.fDepth] = addContentSpecNode(1, this.fNodeIndexStack[this.fDepth], -1);
/* 1112 */       else if (occurrence == 3)
/* 1113 */         this.fNodeIndexStack[this.fDepth] = addContentSpecNode(2, this.fNodeIndexStack[this.fDepth], -1);
/* 1114 */       else if (occurrence == 4)
/* 1115 */         this.fNodeIndexStack[this.fDepth] = addContentSpecNode(3, this.fNodeIndexStack[this.fDepth], -1);
/*      */   }
/*      */ 
/*      */   public void endGroup(Augmentations augs)
/*      */     throws XNIException
/*      */   {
/* 1130 */     if (!this.fMixed) {
/* 1131 */       if (this.fPrevNodeIndexStack[this.fDepth] != -1) {
/* 1132 */         this.fNodeIndexStack[this.fDepth] = addContentSpecNode(this.fOpStack[this.fDepth], this.fPrevNodeIndexStack[this.fDepth], this.fNodeIndexStack[this.fDepth]);
/*      */       }
/* 1134 */       int nodeIndex = this.fNodeIndexStack[(this.fDepth--)];
/* 1135 */       this.fNodeIndexStack[this.fDepth] = nodeIndex;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void any(Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void empty(Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void endContentModel(Augmentations augs)
/*      */     throws XNIException
/*      */   {
/*      */   }
/*      */ 
/*      */   public boolean isNamespaceAware()
/*      */   {
/* 1181 */     return false;
/*      */   }
/*      */ 
/*      */   public SymbolTable getSymbolTable()
/*      */   {
/* 1186 */     return this.fSymbolTable;
/*      */   }
/*      */ 
/*      */   public int getFirstElementDeclIndex()
/*      */   {
/* 1197 */     return this.fElementDeclCount >= 0 ? 0 : -1;
/*      */   }
/*      */ 
/*      */   public int getNextElementDeclIndex(int elementDeclIndex)
/*      */   {
/* 1207 */     return elementDeclIndex < this.fElementDeclCount - 1 ? elementDeclIndex + 1 : -1;
/*      */   }
/*      */ 
/*      */   public int getElementDeclIndex(String elementDeclName)
/*      */   {
/* 1219 */     int mapping = this.fElementIndexMap.get(elementDeclName);
/*      */ 
/* 1221 */     return mapping;
/*      */   }
/*      */ 
/*      */   public int getElementDeclIndex(QName elementDeclQName)
/*      */   {
/* 1228 */     return getElementDeclIndex(elementDeclQName.rawname);
/*      */   }
/*      */ 
/*      */   public short getContentSpecType(int elementIndex)
/*      */   {
/* 1236 */     if ((elementIndex < 0) || (elementIndex >= this.fElementDeclCount)) {
/* 1237 */       return -1;
/*      */     }
/*      */ 
/* 1240 */     int chunk = elementIndex >> 8;
/* 1241 */     int index = elementIndex & 0xFF;
/*      */ 
/* 1243 */     if (this.fElementDeclType[chunk][index] == -1) {
/* 1244 */       return -1;
/*      */     }
/*      */ 
/* 1247 */     return (short)(this.fElementDeclType[chunk][index] & 0xFFFFFF7F);
/*      */   }
/*      */ 
/*      */   public boolean getElementDecl(int elementDeclIndex, XMLElementDecl elementDecl)
/*      */   {
/* 1263 */     if ((elementDeclIndex < 0) || (elementDeclIndex >= this.fElementDeclCount)) {
/* 1264 */       return false;
/*      */     }
/*      */ 
/* 1267 */     int chunk = elementDeclIndex >> 8;
/* 1268 */     int index = elementDeclIndex & 0xFF;
/*      */ 
/* 1270 */     elementDecl.name.setValues(this.fElementDeclName[chunk][index]);
/*      */ 
/* 1272 */     if (this.fElementDeclType[chunk][index] == -1) {
/* 1273 */       elementDecl.type = -1;
/* 1274 */       elementDecl.simpleType.list = false;
/*      */     } else {
/* 1276 */       elementDecl.type = ((short)(this.fElementDeclType[chunk][index] & 0xFFFFFF7F));
/* 1277 */       elementDecl.simpleType.list = ((this.fElementDeclType[chunk][index] & 0x80) != 0);
/*      */     }
/*      */ 
/* 1281 */     if ((elementDecl.type == 3) || (elementDecl.type == 2)) {
/* 1282 */       elementDecl.contentModelValidator = getElementContentModelValidator(elementDeclIndex);
/*      */     }
/*      */ 
/* 1285 */     elementDecl.simpleType.datatypeValidator = null;
/* 1286 */     elementDecl.simpleType.defaultType = -1;
/* 1287 */     elementDecl.simpleType.defaultValue = null;
/*      */ 
/* 1289 */     return true;
/*      */   }
/*      */ 
/*      */   QName getElementDeclName(int elementDeclIndex)
/*      */   {
/* 1294 */     if ((elementDeclIndex < 0) || (elementDeclIndex >= this.fElementDeclCount)) {
/* 1295 */       return null;
/*      */     }
/* 1297 */     int chunk = elementDeclIndex >> 8;
/* 1298 */     int index = elementDeclIndex & 0xFF;
/* 1299 */     return this.fElementDeclName[chunk][index];
/*      */   }
/*      */ 
/*      */   public int getFirstAttributeDeclIndex(int elementDeclIndex)
/*      */   {
/* 1312 */     int chunk = elementDeclIndex >> 8;
/* 1313 */     int index = elementDeclIndex & 0xFF;
/*      */ 
/* 1315 */     return this.fElementDeclFirstAttributeDeclIndex[chunk][index];
/*      */   }
/*      */ 
/*      */   public int getNextAttributeDeclIndex(int attributeDeclIndex)
/*      */   {
/* 1326 */     int chunk = attributeDeclIndex >> 8;
/* 1327 */     int index = attributeDeclIndex & 0xFF;
/*      */ 
/* 1329 */     return this.fAttributeDeclNextAttributeDeclIndex[chunk][index];
/*      */   }
/*      */ 
/*      */   public boolean getAttributeDecl(int attributeDeclIndex, XMLAttributeDecl attributeDecl)
/*      */   {
/* 1341 */     if ((attributeDeclIndex < 0) || (attributeDeclIndex >= this.fAttributeDeclCount)) {
/* 1342 */       return false;
/*      */     }
/* 1344 */     int chunk = attributeDeclIndex >> 8;
/* 1345 */     int index = attributeDeclIndex & 0xFF;
/*      */ 
/* 1347 */     attributeDecl.name.setValues(this.fAttributeDeclName[chunk][index]);
/*      */     boolean isList;
/*      */     short attributeType;
/*      */     boolean isList;
/* 1352 */     if (this.fAttributeDeclType[chunk][index] == -1)
/*      */     {
/* 1354 */       short attributeType = -1;
/* 1355 */       isList = false;
/*      */     } else {
/* 1357 */       attributeType = (short)(this.fAttributeDeclType[chunk][index] & 0xFFFFFF7F);
/* 1358 */       isList = (this.fAttributeDeclType[chunk][index] & 0x80) != 0;
/*      */     }
/* 1360 */     attributeDecl.simpleType.setValues(attributeType, this.fAttributeDeclName[chunk][index].localpart, this.fAttributeDeclEnumeration[chunk][index], isList, this.fAttributeDeclDefaultType[chunk][index], this.fAttributeDeclDefaultValue[chunk][index], this.fAttributeDeclNonNormalizedDefaultValue[chunk][index], this.fAttributeDeclDatatypeValidator[chunk][index]);
/*      */ 
/* 1366 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean isCDATAAttribute(QName elName, QName atName)
/*      */   {
/* 1380 */     int elDeclIdx = getElementDeclIndex(elName);
/* 1381 */     if ((getAttributeDecl(elDeclIdx, this.fAttributeDecl)) && (this.fAttributeDecl.simpleType.type != 0))
/*      */     {
/* 1383 */       return false;
/*      */     }
/* 1385 */     return true;
/*      */   }
/*      */ 
/*      */   public int getEntityDeclIndex(String entityDeclName)
/*      */   {
/* 1396 */     if (entityDeclName == null) {
/* 1397 */       return -1;
/*      */     }
/*      */ 
/* 1400 */     return this.fEntityIndexMap.get(entityDeclName);
/*      */   }
/*      */ 
/*      */   public boolean getEntityDecl(int entityDeclIndex, XMLEntityDecl entityDecl)
/*      */   {
/* 1413 */     if ((entityDeclIndex < 0) || (entityDeclIndex >= this.fEntityCount)) {
/* 1414 */       return false;
/*      */     }
/* 1416 */     int chunk = entityDeclIndex >> 8;
/* 1417 */     int index = entityDeclIndex & 0xFF;
/*      */ 
/* 1419 */     entityDecl.setValues(this.fEntityName[chunk][index], this.fEntityPublicId[chunk][index], this.fEntitySystemId[chunk][index], this.fEntityBaseSystemId[chunk][index], this.fEntityNotation[chunk][index], this.fEntityValue[chunk][index], this.fEntityIsPE[chunk][index] != 0, this.fEntityInExternal[chunk][index] != 0);
/*      */ 
/* 1428 */     return true;
/*      */   }
/*      */ 
/*      */   public int getNotationDeclIndex(String notationDeclName)
/*      */   {
/* 1439 */     if (notationDeclName == null) {
/* 1440 */       return -1;
/*      */     }
/*      */ 
/* 1443 */     return this.fNotationIndexMap.get(notationDeclName);
/*      */   }
/*      */ 
/*      */   public boolean getNotationDecl(int notationDeclIndex, XMLNotationDecl notationDecl)
/*      */   {
/* 1456 */     if ((notationDeclIndex < 0) || (notationDeclIndex >= this.fNotationCount)) {
/* 1457 */       return false;
/*      */     }
/* 1459 */     int chunk = notationDeclIndex >> 8;
/* 1460 */     int index = notationDeclIndex & 0xFF;
/*      */ 
/* 1462 */     notationDecl.setValues(this.fNotationName[chunk][index], this.fNotationPublicId[chunk][index], this.fNotationSystemId[chunk][index], this.fNotationBaseSystemId[chunk][index]);
/*      */ 
/* 1467 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean getContentSpec(int contentSpecIndex, XMLContentSpec contentSpec)
/*      */   {
/* 1480 */     if ((contentSpecIndex < 0) || (contentSpecIndex >= this.fContentSpecCount)) {
/* 1481 */       return false;
/*      */     }
/* 1483 */     int chunk = contentSpecIndex >> 8;
/* 1484 */     int index = contentSpecIndex & 0xFF;
/*      */ 
/* 1486 */     contentSpec.type = this.fContentSpecType[chunk][index];
/* 1487 */     contentSpec.value = this.fContentSpecValue[chunk][index];
/* 1488 */     contentSpec.otherValue = this.fContentSpecOtherValue[chunk][index];
/* 1489 */     return true;
/*      */   }
/*      */ 
/*      */   public int getContentSpecIndex(int elementDeclIndex)
/*      */   {
/* 1498 */     if ((elementDeclIndex < 0) || (elementDeclIndex >= this.fElementDeclCount)) {
/* 1499 */       return -1;
/*      */     }
/* 1501 */     int chunk = elementDeclIndex >> 8;
/* 1502 */     int index = elementDeclIndex & 0xFF;
/* 1503 */     return this.fElementDeclContentSpecIndex[chunk][index];
/*      */   }
/*      */ 
/*      */   public String getContentSpecAsString(int elementDeclIndex)
/*      */   {
/* 1515 */     if ((elementDeclIndex < 0) || (elementDeclIndex >= this.fElementDeclCount)) {
/* 1516 */       return null;
/*      */     }
/*      */ 
/* 1519 */     int chunk = elementDeclIndex >> 8;
/* 1520 */     int index = elementDeclIndex & 0xFF;
/*      */ 
/* 1522 */     int contentSpecIndex = this.fElementDeclContentSpecIndex[chunk][index];
/*      */ 
/* 1525 */     XMLContentSpec contentSpec = new XMLContentSpec();
/*      */ 
/* 1527 */     if (getContentSpec(contentSpecIndex, contentSpec))
/*      */     {
/* 1530 */       StringBuffer str = new StringBuffer();
/* 1531 */       int parentContentSpecType = contentSpec.type & 0xF;
/*      */       int nextContentSpec;
/* 1533 */       switch (parentContentSpecType) {
/*      */       case 0:
/* 1535 */         str.append('(');
/* 1536 */         if ((contentSpec.value == null) && (contentSpec.otherValue == null)) {
/* 1537 */           str.append("#PCDATA");
/*      */         }
/*      */         else {
/* 1540 */           str.append(contentSpec.value);
/*      */         }
/* 1542 */         str.append(')');
/* 1543 */         break;
/*      */       case 1:
/* 1546 */         getContentSpec(((int[])(int[])contentSpec.value)[0], contentSpec);
/* 1547 */         nextContentSpec = contentSpec.type;
/*      */ 
/* 1549 */         if (nextContentSpec == 0) {
/* 1550 */           str.append('(');
/* 1551 */           str.append(contentSpec.value);
/* 1552 */           str.append(')');
/* 1553 */         } else if ((nextContentSpec == 3) || (nextContentSpec == 2) || (nextContentSpec == 1))
/*      */         {
/* 1556 */           str.append('(');
/* 1557 */           appendContentSpec(contentSpec, str, true, parentContentSpecType);
/*      */ 
/* 1559 */           str.append(')');
/*      */         } else {
/* 1561 */           appendContentSpec(contentSpec, str, true, parentContentSpecType);
/*      */         }
/*      */ 
/* 1564 */         str.append('?');
/* 1565 */         break;
/*      */       case 2:
/* 1568 */         getContentSpec(((int[])(int[])contentSpec.value)[0], contentSpec);
/* 1569 */         nextContentSpec = contentSpec.type;
/*      */ 
/* 1571 */         if (nextContentSpec == 0) {
/* 1572 */           str.append('(');
/* 1573 */           if ((contentSpec.value == null) && (contentSpec.otherValue == null)) {
/* 1574 */             str.append("#PCDATA");
/*      */           }
/* 1576 */           else if (contentSpec.otherValue != null) {
/* 1577 */             str.append("##any:uri=").append(contentSpec.otherValue);
/*      */           }
/* 1579 */           else if (contentSpec.value == null) {
/* 1580 */             str.append("##any");
/*      */           }
/*      */           else {
/* 1583 */             appendContentSpec(contentSpec, str, true, parentContentSpecType);
/*      */           }
/*      */ 
/* 1586 */           str.append(')');
/* 1587 */         } else if ((nextContentSpec == 3) || (nextContentSpec == 2) || (nextContentSpec == 1))
/*      */         {
/* 1590 */           str.append('(');
/* 1591 */           appendContentSpec(contentSpec, str, true, parentContentSpecType);
/*      */ 
/* 1593 */           str.append(')');
/*      */         } else {
/* 1595 */           appendContentSpec(contentSpec, str, true, parentContentSpecType);
/*      */         }
/*      */ 
/* 1598 */         str.append('*');
/* 1599 */         break;
/*      */       case 3:
/* 1602 */         getContentSpec(((int[])(int[])contentSpec.value)[0], contentSpec);
/* 1603 */         nextContentSpec = contentSpec.type;
/*      */ 
/* 1605 */         if (nextContentSpec == 0) {
/* 1606 */           str.append('(');
/* 1607 */           if ((contentSpec.value == null) && (contentSpec.otherValue == null)) {
/* 1608 */             str.append("#PCDATA");
/*      */           }
/* 1610 */           else if (contentSpec.otherValue != null) {
/* 1611 */             str.append("##any:uri=").append(contentSpec.otherValue);
/*      */           }
/* 1613 */           else if (contentSpec.value == null) {
/* 1614 */             str.append("##any");
/*      */           }
/*      */           else {
/* 1617 */             str.append(contentSpec.value);
/*      */           }
/* 1619 */           str.append(')');
/* 1620 */         } else if ((nextContentSpec == 3) || (nextContentSpec == 2) || (nextContentSpec == 1))
/*      */         {
/* 1623 */           str.append('(');
/* 1624 */           appendContentSpec(contentSpec, str, true, parentContentSpecType);
/*      */ 
/* 1626 */           str.append(')');
/*      */         } else {
/* 1628 */           appendContentSpec(contentSpec, str, true, parentContentSpecType);
/*      */         }
/*      */ 
/* 1631 */         str.append('+');
/* 1632 */         break;
/*      */       case 4:
/*      */       case 5:
/* 1636 */         appendContentSpec(contentSpec, str, true, parentContentSpecType);
/*      */ 
/* 1638 */         break;
/*      */       case 6:
/* 1641 */         str.append("##any");
/* 1642 */         if (contentSpec.otherValue != null) {
/* 1643 */           str.append(":uri=");
/* 1644 */           str.append(contentSpec.otherValue); } break;
/*      */       case 7:
/* 1649 */         str.append("##other:uri=");
/* 1650 */         str.append(contentSpec.otherValue);
/* 1651 */         break;
/*      */       case 8:
/* 1654 */         str.append("##local");
/* 1655 */         break;
/*      */       default:
/* 1658 */         str.append("???");
/*      */       }
/*      */ 
/* 1664 */       return str.toString();
/*      */     }
/*      */ 
/* 1668 */     return null;
/*      */   }
/*      */ 
/*      */   public void printElements()
/*      */   {
/* 1675 */     int elementDeclIndex = 0;
/* 1676 */     XMLElementDecl elementDecl = new XMLElementDecl();
/* 1677 */     while (getElementDecl(elementDeclIndex++, elementDecl))
/*      */     {
/* 1679 */       System.out.println("element decl: " + elementDecl.name + ", " + elementDecl.name.rawname);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void printAttributes(int elementDeclIndex)
/*      */   {
/* 1687 */     int attributeDeclIndex = getFirstAttributeDeclIndex(elementDeclIndex);
/* 1688 */     System.out.print(elementDeclIndex);
/* 1689 */     System.out.print(" [");
/* 1690 */     while (attributeDeclIndex != -1) {
/* 1691 */       System.out.print(' ');
/* 1692 */       System.out.print(attributeDeclIndex);
/* 1693 */       printAttribute(attributeDeclIndex);
/* 1694 */       attributeDeclIndex = getNextAttributeDeclIndex(attributeDeclIndex);
/* 1695 */       if (attributeDeclIndex != -1) {
/* 1696 */         System.out.print(",");
/*      */       }
/*      */     }
/* 1699 */     System.out.println(" ]");
/*      */   }
/*      */ 
/*      */   protected void addContentSpecToElement(XMLElementDecl elementDecl)
/*      */   {
/* 1710 */     if (((this.fDepth == 0) || ((this.fDepth == 1) && (elementDecl.type == 2))) && (this.fNodeIndexStack != null))
/*      */     {
/* 1712 */       if (elementDecl.type == 2) {
/* 1713 */         int pcdata = addUniqueLeafNode(null);
/* 1714 */         if (this.fNodeIndexStack[0] == -1) {
/* 1715 */           this.fNodeIndexStack[0] = pcdata;
/*      */         }
/*      */         else {
/* 1718 */           this.fNodeIndexStack[0] = addContentSpecNode(4, pcdata, this.fNodeIndexStack[0]);
/*      */         }
/*      */       }
/*      */ 
/* 1722 */       setContentSpecIndex(this.fCurrentElementIndex, this.fNodeIndexStack[this.fDepth]);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected ContentModelValidator getElementContentModelValidator(int elementDeclIndex)
/*      */   {
/* 1735 */     int chunk = elementDeclIndex >> 8;
/* 1736 */     int index = elementDeclIndex & 0xFF;
/*      */ 
/* 1738 */     ContentModelValidator contentModel = this.fElementDeclContentModelValidator[chunk][index];
/*      */ 
/* 1741 */     if (contentModel != null) {
/* 1742 */       return contentModel;
/*      */     }
/*      */ 
/* 1745 */     int contentType = this.fElementDeclType[chunk][index];
/* 1746 */     if (contentType == 4) {
/* 1747 */       return null;
/*      */     }
/*      */ 
/* 1751 */     int contentSpecIndex = this.fElementDeclContentSpecIndex[chunk][index];
/*      */ 
/* 1758 */     XMLContentSpec contentSpec = new XMLContentSpec();
/* 1759 */     getContentSpec(contentSpecIndex, contentSpec);
/*      */ 
/* 1762 */     if (contentType == 2)
/*      */     {
/* 1767 */       ChildrenList children = new ChildrenList();
/* 1768 */       contentSpecTree(contentSpecIndex, contentSpec, children);
/* 1769 */       contentModel = new MixedContentModel(children.qname, children.type, 0, children.length, false);
/*      */     }
/* 1773 */     else if (contentType == 3)
/*      */     {
/* 1780 */       contentModel = createChildModel(contentSpecIndex);
/*      */     } else {
/* 1782 */       throw new RuntimeException("Unknown content type for a element decl in getElementContentModelValidator() in AbstractDTDGrammar class");
/*      */     }
/*      */ 
/* 1787 */     this.fElementDeclContentModelValidator[chunk][index] = contentModel;
/*      */ 
/* 1789 */     return contentModel;
/*      */   }
/*      */ 
/*      */   protected int createElementDecl()
/*      */   {
/* 1794 */     int chunk = this.fElementDeclCount >> 8;
/* 1795 */     int index = this.fElementDeclCount & 0xFF;
/* 1796 */     ensureElementDeclCapacity(chunk);
/* 1797 */     this.fElementDeclName[chunk][index] = new QName();
/* 1798 */     this.fElementDeclType[chunk][index] = -1;
/* 1799 */     this.fElementDeclContentModelValidator[chunk][index] = null;
/* 1800 */     this.fElementDeclFirstAttributeDeclIndex[chunk][index] = -1;
/* 1801 */     this.fElementDeclLastAttributeDeclIndex[chunk][index] = -1;
/* 1802 */     return this.fElementDeclCount++;
/*      */   }
/*      */ 
/*      */   protected void setElementDecl(int elementDeclIndex, XMLElementDecl elementDecl) {
/* 1806 */     if ((elementDeclIndex < 0) || (elementDeclIndex >= this.fElementDeclCount)) {
/* 1807 */       return;
/*      */     }
/* 1809 */     int chunk = elementDeclIndex >> 8;
/* 1810 */     int index = elementDeclIndex & 0xFF;
/*      */ 
/* 1812 */     this.fElementDeclName[chunk][index].setValues(elementDecl.name);
/* 1813 */     this.fElementDeclType[chunk][index] = elementDecl.type;
/*      */ 
/* 1815 */     this.fElementDeclContentModelValidator[chunk][index] = elementDecl.contentModelValidator;
/*      */ 
/* 1817 */     if (elementDecl.simpleType.list == true)
/*      */     {
/*      */       int tmp86_84 = index;
/*      */       short[] tmp86_83 = this.fElementDeclType[chunk]; tmp86_83[tmp86_84] = ((short)(tmp86_83[tmp86_84] | 0x80));
/*      */     }
/*      */ 
/* 1821 */     this.fElementIndexMap.put(elementDecl.name.rawname, elementDeclIndex);
/*      */   }
/*      */ 
/*      */   protected void putElementNameMapping(QName name, int scope, int elementDeclIndex)
/*      */   {
/*      */   }
/*      */ 
/*      */   protected void setFirstAttributeDeclIndex(int elementDeclIndex, int newFirstAttrIndex)
/*      */   {
/* 1833 */     if ((elementDeclIndex < 0) || (elementDeclIndex >= this.fElementDeclCount)) {
/* 1834 */       return;
/*      */     }
/*      */ 
/* 1837 */     int chunk = elementDeclIndex >> 8;
/* 1838 */     int index = elementDeclIndex & 0xFF;
/*      */ 
/* 1840 */     this.fElementDeclFirstAttributeDeclIndex[chunk][index] = newFirstAttrIndex;
/*      */   }
/*      */ 
/*      */   protected void setContentSpecIndex(int elementDeclIndex, int contentSpecIndex)
/*      */   {
/* 1845 */     if ((elementDeclIndex < 0) || (elementDeclIndex >= this.fElementDeclCount)) {
/* 1846 */       return;
/*      */     }
/*      */ 
/* 1849 */     int chunk = elementDeclIndex >> 8;
/* 1850 */     int index = elementDeclIndex & 0xFF;
/*      */ 
/* 1852 */     this.fElementDeclContentSpecIndex[chunk][index] = contentSpecIndex;
/*      */   }
/*      */ 
/*      */   protected int createAttributeDecl()
/*      */   {
/* 1857 */     int chunk = this.fAttributeDeclCount >> 8;
/* 1858 */     int index = this.fAttributeDeclCount & 0xFF;
/*      */ 
/* 1860 */     ensureAttributeDeclCapacity(chunk);
/* 1861 */     this.fAttributeDeclName[chunk][index] = new QName();
/* 1862 */     this.fAttributeDeclType[chunk][index] = -1;
/* 1863 */     this.fAttributeDeclDatatypeValidator[chunk][index] = null;
/* 1864 */     this.fAttributeDeclEnumeration[chunk][index] = null;
/* 1865 */     this.fAttributeDeclDefaultType[chunk][index] = 0;
/* 1866 */     this.fAttributeDeclDefaultValue[chunk][index] = null;
/* 1867 */     this.fAttributeDeclNonNormalizedDefaultValue[chunk][index] = null;
/* 1868 */     this.fAttributeDeclNextAttributeDeclIndex[chunk][index] = -1;
/* 1869 */     return this.fAttributeDeclCount++;
/*      */   }
/*      */ 
/*      */   protected void setAttributeDecl(int elementDeclIndex, int attributeDeclIndex, XMLAttributeDecl attributeDecl)
/*      */   {
/* 1875 */     int attrChunk = attributeDeclIndex >> 8;
/* 1876 */     int attrIndex = attributeDeclIndex & 0xFF;
/* 1877 */     this.fAttributeDeclName[attrChunk][attrIndex].setValues(attributeDecl.name);
/* 1878 */     this.fAttributeDeclType[attrChunk][attrIndex] = attributeDecl.simpleType.type;
/*      */ 
/* 1880 */     if (attributeDecl.simpleType.list)
/*      */     {
/*      */       int tmp66_64 = attrIndex;
/*      */       short[] tmp66_63 = this.fAttributeDeclType[attrChunk]; tmp66_63[tmp66_64] = ((short)(tmp66_63[tmp66_64] | 0x80));
/*      */     }
/* 1883 */     this.fAttributeDeclEnumeration[attrChunk][attrIndex] = attributeDecl.simpleType.enumeration;
/* 1884 */     this.fAttributeDeclDefaultType[attrChunk][attrIndex] = attributeDecl.simpleType.defaultType;
/* 1885 */     this.fAttributeDeclDatatypeValidator[attrChunk][attrIndex] = attributeDecl.simpleType.datatypeValidator;
/*      */ 
/* 1887 */     this.fAttributeDeclDefaultValue[attrChunk][attrIndex] = attributeDecl.simpleType.defaultValue;
/* 1888 */     this.fAttributeDeclNonNormalizedDefaultValue[attrChunk][attrIndex] = attributeDecl.simpleType.nonNormalizedDefaultValue;
/*      */ 
/* 1890 */     int elemChunk = elementDeclIndex >> 8;
/* 1891 */     int elemIndex = elementDeclIndex & 0xFF;
/* 1892 */     int index = this.fElementDeclFirstAttributeDeclIndex[elemChunk][elemIndex];
/* 1893 */     while ((index != -1) && 
/* 1894 */       (index != attributeDeclIndex))
/*      */     {
/* 1897 */       attrChunk = index >> 8;
/* 1898 */       attrIndex = index & 0xFF;
/* 1899 */       index = this.fAttributeDeclNextAttributeDeclIndex[attrChunk][attrIndex];
/*      */     }
/* 1901 */     if (index == -1) {
/* 1902 */       if (this.fElementDeclFirstAttributeDeclIndex[elemChunk][elemIndex] == -1) {
/* 1903 */         this.fElementDeclFirstAttributeDeclIndex[elemChunk][elemIndex] = attributeDeclIndex;
/*      */       } else {
/* 1905 */         index = this.fElementDeclLastAttributeDeclIndex[elemChunk][elemIndex];
/* 1906 */         attrChunk = index >> 8;
/* 1907 */         attrIndex = index & 0xFF;
/* 1908 */         this.fAttributeDeclNextAttributeDeclIndex[attrChunk][attrIndex] = attributeDeclIndex;
/*      */       }
/* 1910 */       this.fElementDeclLastAttributeDeclIndex[elemChunk][elemIndex] = attributeDeclIndex;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected int createContentSpec() {
/* 1915 */     int chunk = this.fContentSpecCount >> 8;
/* 1916 */     int index = this.fContentSpecCount & 0xFF;
/*      */ 
/* 1918 */     ensureContentSpecCapacity(chunk);
/* 1919 */     this.fContentSpecType[chunk][index] = -1;
/* 1920 */     this.fContentSpecValue[chunk][index] = null;
/* 1921 */     this.fContentSpecOtherValue[chunk][index] = null;
/*      */ 
/* 1923 */     return this.fContentSpecCount++;
/*      */   }
/*      */ 
/*      */   protected void setContentSpec(int contentSpecIndex, XMLContentSpec contentSpec) {
/* 1927 */     int chunk = contentSpecIndex >> 8;
/* 1928 */     int index = contentSpecIndex & 0xFF;
/*      */ 
/* 1930 */     this.fContentSpecType[chunk][index] = contentSpec.type;
/* 1931 */     this.fContentSpecValue[chunk][index] = contentSpec.value;
/* 1932 */     this.fContentSpecOtherValue[chunk][index] = contentSpec.otherValue;
/*      */   }
/*      */ 
/*      */   protected int createEntityDecl()
/*      */   {
/* 1937 */     int chunk = this.fEntityCount >> 8;
/* 1938 */     int index = this.fEntityCount & 0xFF;
/*      */ 
/* 1940 */     ensureEntityDeclCapacity(chunk);
/* 1941 */     this.fEntityIsPE[chunk][index] = 0;
/* 1942 */     this.fEntityInExternal[chunk][index] = 0;
/*      */ 
/* 1944 */     return this.fEntityCount++;
/*      */   }
/*      */ 
/*      */   protected void setEntityDecl(int entityDeclIndex, XMLEntityDecl entityDecl) {
/* 1948 */     int chunk = entityDeclIndex >> 8;
/* 1949 */     int index = entityDeclIndex & 0xFF;
/*      */ 
/* 1951 */     this.fEntityName[chunk][index] = entityDecl.name;
/* 1952 */     this.fEntityValue[chunk][index] = entityDecl.value;
/* 1953 */     this.fEntityPublicId[chunk][index] = entityDecl.publicId;
/* 1954 */     this.fEntitySystemId[chunk][index] = entityDecl.systemId;
/* 1955 */     this.fEntityBaseSystemId[chunk][index] = entityDecl.baseSystemId;
/* 1956 */     this.fEntityNotation[chunk][index] = entityDecl.notation;
/* 1957 */     this.fEntityIsPE[chunk][index] = (entityDecl.isPE ? 1 : 0);
/* 1958 */     this.fEntityInExternal[chunk][index] = (entityDecl.inExternal ? 1 : 0);
/*      */ 
/* 1960 */     this.fEntityIndexMap.put(entityDecl.name, entityDeclIndex);
/*      */   }
/*      */ 
/*      */   protected int createNotationDecl() {
/* 1964 */     int chunk = this.fNotationCount >> 8;
/* 1965 */     ensureNotationDeclCapacity(chunk);
/* 1966 */     return this.fNotationCount++;
/*      */   }
/*      */ 
/*      */   protected void setNotationDecl(int notationDeclIndex, XMLNotationDecl notationDecl) {
/* 1970 */     int chunk = notationDeclIndex >> 8;
/* 1971 */     int index = notationDeclIndex & 0xFF;
/*      */ 
/* 1973 */     this.fNotationName[chunk][index] = notationDecl.name;
/* 1974 */     this.fNotationPublicId[chunk][index] = notationDecl.publicId;
/* 1975 */     this.fNotationSystemId[chunk][index] = notationDecl.systemId;
/* 1976 */     this.fNotationBaseSystemId[chunk][index] = notationDecl.baseSystemId;
/*      */ 
/* 1978 */     this.fNotationIndexMap.put(notationDecl.name, notationDeclIndex);
/*      */   }
/*      */ 
/*      */   protected int addContentSpecNode(short nodeType, String nodeValue)
/*      */   {
/* 1991 */     int contentSpecIndex = createContentSpec();
/*      */ 
/* 1994 */     this.fContentSpec.setValues(nodeType, nodeValue, null);
/* 1995 */     setContentSpec(contentSpecIndex, this.fContentSpec);
/*      */ 
/* 1998 */     return contentSpecIndex;
/*      */   }
/*      */ 
/*      */   protected int addUniqueLeafNode(String elementName)
/*      */   {
/* 2011 */     int contentSpecIndex = createContentSpec();
/*      */ 
/* 2014 */     this.fContentSpec.setValues((short)0, elementName, null);
/*      */ 
/* 2016 */     setContentSpec(contentSpecIndex, this.fContentSpec);
/*      */ 
/* 2019 */     return contentSpecIndex;
/*      */   }
/*      */ 
/*      */   protected int addContentSpecNode(short nodeType, int leftNodeIndex, int rightNodeIndex)
/*      */   {
/* 2035 */     int contentSpecIndex = createContentSpec();
/*      */ 
/* 2038 */     int[] leftIntArray = new int[1];
/* 2039 */     int[] rightIntArray = new int[1];
/*      */ 
/* 2041 */     leftIntArray[0] = leftNodeIndex;
/* 2042 */     rightIntArray[0] = rightNodeIndex;
/* 2043 */     this.fContentSpec.setValues(nodeType, leftIntArray, rightIntArray);
/* 2044 */     setContentSpec(contentSpecIndex, this.fContentSpec);
/*      */ 
/* 2047 */     return contentSpecIndex;
/*      */   }
/*      */ 
/*      */   protected void initializeContentModelStack()
/*      */   {
/* 2054 */     if (this.fOpStack == null) {
/* 2055 */       this.fOpStack = new short[8];
/* 2056 */       this.fNodeIndexStack = new int[8];
/* 2057 */       this.fPrevNodeIndexStack = new int[8];
/* 2058 */     } else if (this.fDepth == this.fOpStack.length) {
/* 2059 */       short[] newStack = new short[this.fDepth * 2];
/* 2060 */       System.arraycopy(this.fOpStack, 0, newStack, 0, this.fDepth);
/* 2061 */       this.fOpStack = newStack;
/* 2062 */       int[] newIntStack = new int[this.fDepth * 2];
/* 2063 */       System.arraycopy(this.fNodeIndexStack, 0, newIntStack, 0, this.fDepth);
/* 2064 */       this.fNodeIndexStack = newIntStack;
/* 2065 */       newIntStack = new int[this.fDepth * 2];
/* 2066 */       System.arraycopy(this.fPrevNodeIndexStack, 0, newIntStack, 0, this.fDepth);
/* 2067 */       this.fPrevNodeIndexStack = newIntStack;
/*      */     }
/* 2069 */     this.fOpStack[this.fDepth] = -1;
/* 2070 */     this.fNodeIndexStack[this.fDepth] = -1;
/* 2071 */     this.fPrevNodeIndexStack[this.fDepth] = -1;
/*      */   }
/*      */ 
/*      */   boolean isImmutable()
/*      */   {
/* 2076 */     return this.fIsImmutable;
/*      */   }
/*      */ 
/*      */   private void appendContentSpec(XMLContentSpec contentSpec, StringBuffer str, boolean parens, int parentContentSpecType)
/*      */   {
/* 2087 */     int thisContentSpec = contentSpec.type & 0xF;
/* 2088 */     switch (thisContentSpec) {
/*      */     case 0:
/* 2090 */       if ((contentSpec.value == null) && (contentSpec.otherValue == null)) {
/* 2091 */         str.append("#PCDATA");
/*      */       }
/* 2093 */       else if ((contentSpec.value == null) && (contentSpec.otherValue != null)) {
/* 2094 */         str.append("##any:uri=").append(contentSpec.otherValue);
/*      */       }
/* 2096 */       else if (contentSpec.value == null) {
/* 2097 */         str.append("##any");
/*      */       }
/*      */       else {
/* 2100 */         str.append(contentSpec.value);
/*      */       }
/* 2102 */       break;
/*      */     case 1:
/* 2105 */       if ((parentContentSpecType == 3) || (parentContentSpecType == 2) || (parentContentSpecType == 1))
/*      */       {
/* 2108 */         getContentSpec(((int[])(int[])contentSpec.value)[0], contentSpec);
/* 2109 */         str.append('(');
/* 2110 */         appendContentSpec(contentSpec, str, true, thisContentSpec);
/* 2111 */         str.append(')');
/*      */       }
/*      */       else {
/* 2114 */         getContentSpec(((int[])(int[])contentSpec.value)[0], contentSpec);
/* 2115 */         appendContentSpec(contentSpec, str, true, thisContentSpec);
/*      */       }
/* 2117 */       str.append('?');
/* 2118 */       break;
/*      */     case 2:
/* 2121 */       if ((parentContentSpecType == 3) || (parentContentSpecType == 2) || (parentContentSpecType == 1))
/*      */       {
/* 2124 */         getContentSpec(((int[])(int[])contentSpec.value)[0], contentSpec);
/* 2125 */         str.append('(');
/* 2126 */         appendContentSpec(contentSpec, str, true, thisContentSpec);
/* 2127 */         str.append(')');
/*      */       }
/*      */       else {
/* 2130 */         getContentSpec(((int[])(int[])contentSpec.value)[0], contentSpec);
/* 2131 */         appendContentSpec(contentSpec, str, true, thisContentSpec);
/*      */       }
/* 2133 */       str.append('*');
/* 2134 */       break;
/*      */     case 3:
/* 2137 */       if ((parentContentSpecType == 3) || (parentContentSpecType == 2) || (parentContentSpecType == 1))
/*      */       {
/* 2141 */         str.append('(');
/* 2142 */         getContentSpec(((int[])(int[])contentSpec.value)[0], contentSpec);
/* 2143 */         appendContentSpec(contentSpec, str, true, thisContentSpec);
/* 2144 */         str.append(')');
/*      */       }
/*      */       else {
/* 2147 */         getContentSpec(((int[])(int[])contentSpec.value)[0], contentSpec);
/* 2148 */         appendContentSpec(contentSpec, str, true, thisContentSpec);
/*      */       }
/* 2150 */       str.append('+');
/* 2151 */       break;
/*      */     case 4:
/*      */     case 5:
/* 2155 */       if (parens) {
/* 2156 */         str.append('(');
/*      */       }
/* 2158 */       int type = contentSpec.type;
/* 2159 */       int otherValue = ((int[])(int[])contentSpec.otherValue)[0];
/* 2160 */       getContentSpec(((int[])(int[])contentSpec.value)[0], contentSpec);
/* 2161 */       appendContentSpec(contentSpec, str, contentSpec.type != type, thisContentSpec);
/* 2162 */       if (type == 4) {
/* 2163 */         str.append('|');
/*      */       }
/*      */       else {
/* 2166 */         str.append(',');
/*      */       }
/* 2168 */       getContentSpec(otherValue, contentSpec);
/* 2169 */       appendContentSpec(contentSpec, str, true, thisContentSpec);
/* 2170 */       if (parens)
/* 2171 */         str.append(')'); break;
/*      */     case 6:
/* 2176 */       str.append("##any");
/* 2177 */       if (contentSpec.otherValue != null) {
/* 2178 */         str.append(":uri=");
/* 2179 */         str.append(contentSpec.otherValue); } break;
/*      */     case 7:
/* 2184 */       str.append("##other:uri=");
/* 2185 */       str.append(contentSpec.otherValue);
/* 2186 */       break;
/*      */     case 8:
/* 2189 */       str.append("##local");
/* 2190 */       break;
/*      */     default:
/* 2193 */       str.append("???");
/*      */     }
/*      */   }
/*      */ 
/*      */   private void printAttribute(int attributeDeclIndex)
/*      */   {
/* 2205 */     XMLAttributeDecl attributeDecl = new XMLAttributeDecl();
/* 2206 */     if (getAttributeDecl(attributeDeclIndex, attributeDecl)) {
/* 2207 */       System.out.print(" { ");
/* 2208 */       System.out.print(attributeDecl.name.localpart);
/* 2209 */       System.out.print(" }");
/*      */     }
/*      */   }
/*      */ 
/*      */   private synchronized ContentModelValidator createChildModel(int contentSpecIndex)
/*      */   {
/* 2229 */     XMLContentSpec contentSpec = new XMLContentSpec();
/* 2230 */     getContentSpec(contentSpecIndex, contentSpec);
/*      */ 
/* 2232 */     if (((contentSpec.type & 0xF) != 6) && ((contentSpec.type & 0xF) != 7) && ((contentSpec.type & 0xF) != 8))
/*      */     {
/* 2238 */       if (contentSpec.type == 0)
/*      */       {
/* 2243 */         if ((contentSpec.value == null) && (contentSpec.otherValue == null)) {
/* 2244 */           throw new RuntimeException("ImplementationMessages.VAL_NPCD");
/*      */         }
/*      */ 
/* 2252 */         this.fQName.setValues(null, (String)contentSpec.value, (String)contentSpec.value, (String)contentSpec.otherValue);
/*      */ 
/* 2254 */         return new SimpleContentModel(contentSpec.type, this.fQName, null);
/* 2255 */       }if ((contentSpec.type == 4) || (contentSpec.type == 5))
/*      */       {
/* 2261 */         XMLContentSpec contentSpecLeft = new XMLContentSpec();
/* 2262 */         XMLContentSpec contentSpecRight = new XMLContentSpec();
/*      */ 
/* 2264 */         getContentSpec(((int[])(int[])contentSpec.value)[0], contentSpecLeft);
/* 2265 */         getContentSpec(((int[])(int[])contentSpec.otherValue)[0], contentSpecRight);
/*      */ 
/* 2267 */         if ((contentSpecLeft.type == 0) && (contentSpecRight.type == 0))
/*      */         {
/* 2273 */           this.fQName.setValues(null, (String)contentSpecLeft.value, (String)contentSpecLeft.value, (String)contentSpecLeft.otherValue);
/*      */ 
/* 2275 */           this.fQName2.setValues(null, (String)contentSpecRight.value, (String)contentSpecRight.value, (String)contentSpecRight.otherValue);
/*      */ 
/* 2277 */           return new SimpleContentModel(contentSpec.type, this.fQName, this.fQName2);
/*      */         }
/* 2279 */       } else if ((contentSpec.type == 1) || (contentSpec.type == 2) || (contentSpec.type == 3))
/*      */       {
/* 2287 */         XMLContentSpec contentSpecLeft = new XMLContentSpec();
/* 2288 */         getContentSpec(((int[])(int[])contentSpec.value)[0], contentSpecLeft);
/*      */ 
/* 2290 */         if (contentSpecLeft.type == 0)
/*      */         {
/* 2296 */           this.fQName.setValues(null, (String)contentSpecLeft.value, (String)contentSpecLeft.value, (String)contentSpecLeft.otherValue);
/*      */ 
/* 2298 */           return new SimpleContentModel(contentSpec.type, this.fQName, null);
/*      */         }
/*      */       } else {
/* 2301 */         throw new RuntimeException("ImplementationMessages.VAL_CST");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2310 */     this.fLeafCount = 0;
/*      */ 
/* 2312 */     this.fLeafCount = 0;
/* 2313 */     CMNode cmn = buildSyntaxTree(contentSpecIndex, contentSpec);
/*      */ 
/* 2316 */     return new DFAContentModel(cmn, this.fLeafCount, false);
/*      */   }
/*      */ 
/*      */   private final CMNode buildSyntaxTree(int startNode, XMLContentSpec contentSpec)
/*      */   {
/* 2324 */     CMNode nodeRet = null;
/* 2325 */     getContentSpec(startNode, contentSpec);
/* 2326 */     if ((contentSpec.type & 0xF) == 6)
/*      */     {
/* 2328 */       nodeRet = new CMAny(contentSpec.type, (String)contentSpec.otherValue, this.fLeafCount++);
/*      */     }
/* 2330 */     else if ((contentSpec.type & 0xF) == 7) {
/* 2331 */       nodeRet = new CMAny(contentSpec.type, (String)contentSpec.otherValue, this.fLeafCount++);
/*      */     }
/* 2333 */     else if ((contentSpec.type & 0xF) == 8) {
/* 2334 */       nodeRet = new CMAny(contentSpec.type, null, this.fLeafCount++);
/*      */     }
/* 2340 */     else if (contentSpec.type == 0)
/*      */     {
/* 2347 */       this.fQName.setValues(null, (String)contentSpec.value, (String)contentSpec.value, (String)contentSpec.otherValue);
/*      */ 
/* 2349 */       nodeRet = new CMLeaf(this.fQName, this.fLeafCount++);
/*      */     }
/*      */     else
/*      */     {
/* 2355 */       int leftNode = ((int[])(int[])contentSpec.value)[0];
/* 2356 */       int rightNode = ((int[])(int[])contentSpec.otherValue)[0];
/*      */ 
/* 2358 */       if ((contentSpec.type == 4) || (contentSpec.type == 5))
/*      */       {
/* 2366 */         nodeRet = new CMBinOp(contentSpec.type, buildSyntaxTree(leftNode, contentSpec), buildSyntaxTree(rightNode, contentSpec));
/*      */       }
/* 2369 */       else if (contentSpec.type == 2) {
/* 2370 */         nodeRet = new CMUniOp(contentSpec.type, buildSyntaxTree(leftNode, contentSpec));
/*      */       }
/* 2372 */       else if ((contentSpec.type == 2) || (contentSpec.type == 1) || (contentSpec.type == 3))
/*      */       {
/* 2375 */         nodeRet = new CMUniOp(contentSpec.type, buildSyntaxTree(leftNode, contentSpec));
/*      */       }
/*      */       else {
/* 2378 */         throw new RuntimeException("ImplementationMessages.VAL_CST");
/*      */       }
/*      */     }
/*      */ 
/* 2382 */     return nodeRet;
/*      */   }
/*      */ 
/*      */   private void contentSpecTree(int contentSpecIndex, XMLContentSpec contentSpec, ChildrenList children)
/*      */   {
/* 2400 */     getContentSpec(contentSpecIndex, contentSpec);
/* 2401 */     if ((contentSpec.type == 0) || ((contentSpec.type & 0xF) == 6) || ((contentSpec.type & 0xF) == 8) || ((contentSpec.type & 0xF) == 7))
/*      */     {
/* 2407 */       if (children.length == children.qname.length) {
/* 2408 */         QName[] newQName = new QName[children.length * 2];
/* 2409 */         System.arraycopy(children.qname, 0, newQName, 0, children.length);
/* 2410 */         children.qname = newQName;
/* 2411 */         int[] newType = new int[children.length * 2];
/* 2412 */         System.arraycopy(children.type, 0, newType, 0, children.length);
/* 2413 */         children.type = newType;
/*      */       }
/*      */ 
/* 2417 */       children.qname[children.length] = new QName(null, (String)contentSpec.value, (String)contentSpec.value, (String)contentSpec.otherValue);
/*      */ 
/* 2420 */       children.type[children.length] = contentSpec.type;
/* 2421 */       children.length += 1;
/* 2422 */       return;
/*      */     }
/*      */ 
/* 2429 */     int leftNode = contentSpec.value != null ? ((int[])(int[])contentSpec.value)[0] : -1;
/*      */ 
/* 2431 */     int rightNode = -1;
/* 2432 */     if (contentSpec.otherValue != null)
/* 2433 */       rightNode = ((int[])(int[])contentSpec.otherValue)[0];
/*      */     else {
/* 2435 */       return;
/*      */     }
/* 2437 */     if ((contentSpec.type == 4) || (contentSpec.type == 5))
/*      */     {
/* 2439 */       contentSpecTree(leftNode, contentSpec, children);
/* 2440 */       contentSpecTree(rightNode, contentSpec, children);
/* 2441 */       return;
/*      */     }
/*      */ 
/* 2444 */     if ((contentSpec.type == 1) || (contentSpec.type == 2) || (contentSpec.type == 3))
/*      */     {
/* 2447 */       contentSpecTree(leftNode, contentSpec, children);
/* 2448 */       return;
/*      */     }
/*      */ 
/* 2452 */     throw new RuntimeException("Invalid content spec type seen in contentSpecTree() method of AbstractDTDGrammar class : " + contentSpec.type);
/*      */   }
/*      */ 
/*      */   private void ensureElementDeclCapacity(int chunk)
/*      */   {
/* 2459 */     if (chunk >= this.fElementDeclName.length) {
/* 2460 */       this.fElementDeclIsExternal = resize(this.fElementDeclIsExternal, this.fElementDeclIsExternal.length * 2);
/*      */ 
/* 2463 */       this.fElementDeclName = resize(this.fElementDeclName, this.fElementDeclName.length * 2);
/* 2464 */       this.fElementDeclType = resize(this.fElementDeclType, this.fElementDeclType.length * 2);
/* 2465 */       this.fElementDeclContentModelValidator = resize(this.fElementDeclContentModelValidator, this.fElementDeclContentModelValidator.length * 2);
/* 2466 */       this.fElementDeclContentSpecIndex = resize(this.fElementDeclContentSpecIndex, this.fElementDeclContentSpecIndex.length * 2);
/* 2467 */       this.fElementDeclFirstAttributeDeclIndex = resize(this.fElementDeclFirstAttributeDeclIndex, this.fElementDeclFirstAttributeDeclIndex.length * 2);
/* 2468 */       this.fElementDeclLastAttributeDeclIndex = resize(this.fElementDeclLastAttributeDeclIndex, this.fElementDeclLastAttributeDeclIndex.length * 2);
/*      */     }
/* 2470 */     else if (this.fElementDeclName[chunk] != null) {
/* 2471 */       return;
/*      */     }
/*      */ 
/* 2474 */     this.fElementDeclIsExternal[chunk] = new int[256];
/* 2475 */     this.fElementDeclName[chunk] = new QName[256];
/* 2476 */     this.fElementDeclType[chunk] = new short[256];
/* 2477 */     this.fElementDeclContentModelValidator[chunk] = new ContentModelValidator[256];
/* 2478 */     this.fElementDeclContentSpecIndex[chunk] = new int[256];
/* 2479 */     this.fElementDeclFirstAttributeDeclIndex[chunk] = new int[256];
/* 2480 */     this.fElementDeclLastAttributeDeclIndex[chunk] = new int[256];
/*      */   }
/*      */ 
/*      */   private void ensureAttributeDeclCapacity(int chunk)
/*      */   {
/* 2486 */     if (chunk >= this.fAttributeDeclName.length) {
/* 2487 */       this.fAttributeDeclIsExternal = resize(this.fAttributeDeclIsExternal, this.fAttributeDeclIsExternal.length * 2);
/*      */ 
/* 2489 */       this.fAttributeDeclName = resize(this.fAttributeDeclName, this.fAttributeDeclName.length * 2);
/* 2490 */       this.fAttributeDeclType = resize(this.fAttributeDeclType, this.fAttributeDeclType.length * 2);
/* 2491 */       this.fAttributeDeclEnumeration = resize(this.fAttributeDeclEnumeration, this.fAttributeDeclEnumeration.length * 2);
/* 2492 */       this.fAttributeDeclDefaultType = resize(this.fAttributeDeclDefaultType, this.fAttributeDeclDefaultType.length * 2);
/* 2493 */       this.fAttributeDeclDatatypeValidator = resize(this.fAttributeDeclDatatypeValidator, this.fAttributeDeclDatatypeValidator.length * 2);
/* 2494 */       this.fAttributeDeclDefaultValue = resize(this.fAttributeDeclDefaultValue, this.fAttributeDeclDefaultValue.length * 2);
/* 2495 */       this.fAttributeDeclNonNormalizedDefaultValue = resize(this.fAttributeDeclNonNormalizedDefaultValue, this.fAttributeDeclNonNormalizedDefaultValue.length * 2);
/* 2496 */       this.fAttributeDeclNextAttributeDeclIndex = resize(this.fAttributeDeclNextAttributeDeclIndex, this.fAttributeDeclNextAttributeDeclIndex.length * 2);
/*      */     }
/* 2498 */     else if (this.fAttributeDeclName[chunk] != null) {
/* 2499 */       return;
/*      */     }
/*      */ 
/* 2502 */     this.fAttributeDeclIsExternal[chunk] = new int[256];
/* 2503 */     this.fAttributeDeclName[chunk] = new QName[256];
/* 2504 */     this.fAttributeDeclType[chunk] = new short[256];
/* 2505 */     this.fAttributeDeclEnumeration[chunk] = new String[256][];
/* 2506 */     this.fAttributeDeclDefaultType[chunk] = new short[256];
/* 2507 */     this.fAttributeDeclDatatypeValidator[chunk] = new DatatypeValidator[256];
/* 2508 */     this.fAttributeDeclDefaultValue[chunk] = new String[256];
/* 2509 */     this.fAttributeDeclNonNormalizedDefaultValue[chunk] = new String[256];
/* 2510 */     this.fAttributeDeclNextAttributeDeclIndex[chunk] = new int[256];
/*      */   }
/*      */ 
/*      */   private void ensureEntityDeclCapacity(int chunk)
/*      */   {
/* 2515 */     if (chunk >= this.fEntityName.length) {
/* 2516 */       this.fEntityName = resize(this.fEntityName, this.fEntityName.length * 2);
/* 2517 */       this.fEntityValue = resize(this.fEntityValue, this.fEntityValue.length * 2);
/* 2518 */       this.fEntityPublicId = resize(this.fEntityPublicId, this.fEntityPublicId.length * 2);
/* 2519 */       this.fEntitySystemId = resize(this.fEntitySystemId, this.fEntitySystemId.length * 2);
/* 2520 */       this.fEntityBaseSystemId = resize(this.fEntityBaseSystemId, this.fEntityBaseSystemId.length * 2);
/* 2521 */       this.fEntityNotation = resize(this.fEntityNotation, this.fEntityNotation.length * 2);
/* 2522 */       this.fEntityIsPE = resize(this.fEntityIsPE, this.fEntityIsPE.length * 2);
/* 2523 */       this.fEntityInExternal = resize(this.fEntityInExternal, this.fEntityInExternal.length * 2);
/*      */     }
/* 2525 */     else if (this.fEntityName[chunk] != null) {
/* 2526 */       return;
/*      */     }
/*      */ 
/* 2529 */     this.fEntityName[chunk] = new String[256];
/* 2530 */     this.fEntityValue[chunk] = new String[256];
/* 2531 */     this.fEntityPublicId[chunk] = new String[256];
/* 2532 */     this.fEntitySystemId[chunk] = new String[256];
/* 2533 */     this.fEntityBaseSystemId[chunk] = new String[256];
/* 2534 */     this.fEntityNotation[chunk] = new String[256];
/* 2535 */     this.fEntityIsPE[chunk] = new byte[256];
/* 2536 */     this.fEntityInExternal[chunk] = new byte[256];
/*      */   }
/*      */ 
/*      */   private void ensureNotationDeclCapacity(int chunk)
/*      */   {
/* 2541 */     if (chunk >= this.fNotationName.length) {
/* 2542 */       this.fNotationName = resize(this.fNotationName, this.fNotationName.length * 2);
/* 2543 */       this.fNotationPublicId = resize(this.fNotationPublicId, this.fNotationPublicId.length * 2);
/* 2544 */       this.fNotationSystemId = resize(this.fNotationSystemId, this.fNotationSystemId.length * 2);
/* 2545 */       this.fNotationBaseSystemId = resize(this.fNotationBaseSystemId, this.fNotationBaseSystemId.length * 2);
/*      */     }
/* 2547 */     else if (this.fNotationName[chunk] != null) {
/* 2548 */       return;
/*      */     }
/*      */ 
/* 2551 */     this.fNotationName[chunk] = new String[256];
/* 2552 */     this.fNotationPublicId[chunk] = new String[256];
/* 2553 */     this.fNotationSystemId[chunk] = new String[256];
/* 2554 */     this.fNotationBaseSystemId[chunk] = new String[256];
/*      */   }
/*      */ 
/*      */   private void ensureContentSpecCapacity(int chunk)
/*      */   {
/* 2559 */     if (chunk >= this.fContentSpecType.length) {
/* 2560 */       this.fContentSpecType = resize(this.fContentSpecType, this.fContentSpecType.length * 2);
/* 2561 */       this.fContentSpecValue = resize(this.fContentSpecValue, this.fContentSpecValue.length * 2);
/* 2562 */       this.fContentSpecOtherValue = resize(this.fContentSpecOtherValue, this.fContentSpecOtherValue.length * 2);
/*      */     }
/* 2564 */     else if (this.fContentSpecType[chunk] != null) {
/* 2565 */       return;
/*      */     }
/*      */ 
/* 2568 */     this.fContentSpecType[chunk] = new short[256];
/* 2569 */     this.fContentSpecValue[chunk] = new Object[256];
/* 2570 */     this.fContentSpecOtherValue[chunk] = new Object[256];
/*      */   }
/*      */ 
/*      */   private static byte[][] resize(byte[][] array, int newsize)
/*      */   {
/* 2581 */     byte[][] newarray = new byte[newsize][];
/* 2582 */     System.arraycopy(array, 0, newarray, 0, array.length);
/* 2583 */     return newarray;
/*      */   }
/*      */ 
/*      */   private static short[][] resize(short[][] array, int newsize) {
/* 2587 */     short[][] newarray = new short[newsize][];
/* 2588 */     System.arraycopy(array, 0, newarray, 0, array.length);
/* 2589 */     return newarray;
/*      */   }
/*      */ 
/*      */   private static int[][] resize(int[][] array, int newsize) {
/* 2593 */     int[][] newarray = new int[newsize][];
/* 2594 */     System.arraycopy(array, 0, newarray, 0, array.length);
/* 2595 */     return newarray;
/*      */   }
/*      */ 
/*      */   private static DatatypeValidator[][] resize(DatatypeValidator[][] array, int newsize) {
/* 2599 */     DatatypeValidator[][] newarray = new DatatypeValidator[newsize][];
/* 2600 */     System.arraycopy(array, 0, newarray, 0, array.length);
/* 2601 */     return newarray;
/*      */   }
/*      */ 
/*      */   private static ContentModelValidator[][] resize(ContentModelValidator[][] array, int newsize) {
/* 2605 */     ContentModelValidator[][] newarray = new ContentModelValidator[newsize][];
/* 2606 */     System.arraycopy(array, 0, newarray, 0, array.length);
/* 2607 */     return newarray;
/*      */   }
/*      */ 
/*      */   private static Object[][] resize(Object[][] array, int newsize) {
/* 2611 */     Object[][] newarray = new Object[newsize][];
/* 2612 */     System.arraycopy(array, 0, newarray, 0, array.length);
/* 2613 */     return newarray;
/*      */   }
/*      */ 
/*      */   private static QName[][] resize(QName[][] array, int newsize) {
/* 2617 */     QName[][] newarray = new QName[newsize][];
/* 2618 */     System.arraycopy(array, 0, newarray, 0, array.length);
/* 2619 */     return newarray;
/*      */   }
/*      */ 
/*      */   private static String[][] resize(String[][] array, int newsize) {
/* 2623 */     String[][] newarray = new String[newsize][];
/* 2624 */     System.arraycopy(array, 0, newarray, 0, array.length);
/* 2625 */     return newarray;
/*      */   }
/*      */ 
/*      */   private static String[][][] resize(String[][][] array, int newsize) {
/* 2629 */     String[][][] newarray = new String[newsize][][];
/* 2630 */     System.arraycopy(array, 0, newarray, 0, array.length);
/* 2631 */     return newarray;
/*      */   }
/*      */ 
/*      */   public boolean isEntityDeclared(String name)
/*      */   {
/* 2778 */     return getEntityDeclIndex(name) != -1;
/*      */   }
/*      */ 
/*      */   public boolean isEntityUnparsed(String name) {
/* 2782 */     int entityIndex = getEntityDeclIndex(name);
/* 2783 */     if (entityIndex > -1) {
/* 2784 */       int chunk = entityIndex >> 8;
/* 2785 */       int index = entityIndex & 0xFF;
/*      */ 
/* 2787 */       return this.fEntityNotation[chunk][index] != null;
/*      */     }
/* 2789 */     return false;
/*      */   }
/*      */ 
/*      */   private static class ChildrenList
/*      */   {
/* 2652 */     public int length = 0;
/*      */ 
/* 2659 */     public QName[] qname = new QName[2];
/*      */ 
/* 2662 */     public int[] type = new int[2];
/*      */   }
/*      */ 
/*      */   protected static final class QNameHashtable
/*      */   {
/*      */     private static final int INITIAL_BUCKET_SIZE = 4;
/*      */     private static final int HASHTABLE_SIZE = 101;
/* 2702 */     private Object[][] fHashTable = new Object[101][];
/*      */ 
/*      */     public void put(String key, int value)
/*      */     {
/* 2710 */       int hash = (key.hashCode() & 0x7FFFFFFF) % 101;
/* 2711 */       Object[] bucket = this.fHashTable[hash];
/*      */ 
/* 2713 */       if (bucket == null) {
/* 2714 */         bucket = new Object[9];
/* 2715 */         bucket[0] = { 1 };
/* 2716 */         bucket[1] = key;
/* 2717 */         bucket[2] = { value };
/* 2718 */         this.fHashTable[hash] = bucket;
/*      */       } else {
/* 2720 */         int count = ((int[])(int[])bucket[0])[0];
/* 2721 */         int offset = 1 + 2 * count;
/* 2722 */         if (offset == bucket.length) {
/* 2723 */           int newSize = count + 4;
/* 2724 */           Object[] newBucket = new Object[1 + 2 * newSize];
/* 2725 */           System.arraycopy(bucket, 0, newBucket, 0, offset);
/* 2726 */           bucket = newBucket;
/* 2727 */           this.fHashTable[hash] = bucket;
/*      */         }
/* 2729 */         boolean found = false;
/* 2730 */         int j = 1;
/* 2731 */         for (int i = 0; i < count; i++) {
/* 2732 */           if ((String)bucket[j] == key) {
/* 2733 */             ((int[])bucket[(j + 1)])[0] = value;
/* 2734 */             found = true;
/* 2735 */             break;
/*      */           }
/* 2737 */           j += 2;
/*      */         }
/* 2739 */         if (!found) {
/* 2740 */           bucket[(offset++)] = key;
/* 2741 */           bucket[offset] = { value };
/* 2742 */           ((int[])bucket[0])[0] = (++count);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public int get(String key)
/*      */     {
/* 2753 */       int hash = (key.hashCode() & 0x7FFFFFFF) % 101;
/* 2754 */       Object[] bucket = this.fHashTable[hash];
/*      */ 
/* 2756 */       if (bucket == null) {
/* 2757 */         return -1;
/*      */       }
/* 2759 */       int count = ((int[])(int[])bucket[0])[0];
/*      */ 
/* 2761 */       int j = 1;
/* 2762 */       for (int i = 0; i < count; i++) {
/* 2763 */         if ((String)bucket[j] == key) {
/* 2764 */           return ((int[])(int[])bucket[(j + 1)])[0];
/*      */         }
/* 2766 */         j += 2;
/*      */       }
/* 2768 */       return -1;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dtd.DTDGrammar
 * JD-Core Version:    0.6.2
 */