/*      */ package com.sun.org.apache.xml.internal.dtm.ref;
/*      */ 
/*      */ import com.sun.org.apache.xml.internal.dtm.DTM;
/*      */ import com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser;
/*      */ import com.sun.org.apache.xml.internal.dtm.DTMException;
/*      */ import com.sun.org.apache.xml.internal.dtm.DTMManager;
/*      */ import com.sun.org.apache.xml.internal.dtm.DTMWSFilter;
/*      */ import com.sun.org.apache.xml.internal.res.XMLMessages;
/*      */ import com.sun.org.apache.xml.internal.utils.BoolStack;
/*      */ import com.sun.org.apache.xml.internal.utils.SuballocatedIntVector;
/*      */ import com.sun.org.apache.xml.internal.utils.XMLString;
/*      */ import com.sun.org.apache.xml.internal.utils.XMLStringFactory;
/*      */ import java.io.File;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.util.Vector;
/*      */ import javax.xml.transform.Source;
/*      */ import org.w3c.dom.Node;
/*      */ import org.xml.sax.ContentHandler;
/*      */ import org.xml.sax.SAXException;
/*      */ 
/*      */ public abstract class DTMDefaultBase
/*      */   implements DTM
/*      */ {
/*      */   static final boolean JJK_DEBUG = false;
/*      */   public static final int ROOTNODE = 0;
/*   60 */   protected int m_size = 0;
/*      */   protected SuballocatedIntVector m_exptype;
/*      */   protected SuballocatedIntVector m_firstch;
/*      */   protected SuballocatedIntVector m_nextsib;
/*      */   protected SuballocatedIntVector m_prevsib;
/*      */   protected SuballocatedIntVector m_parent;
/*   78 */   protected Vector m_namespaceDeclSets = null;
/*      */ 
/*   82 */   protected SuballocatedIntVector m_namespaceDeclSetElements = null;
/*      */   protected int[][][] m_elemIndexes;
/*      */   public static final int DEFAULT_BLOCKSIZE = 512;
/*      */   public static final int DEFAULT_NUMBLOCKS = 32;
/*      */   public static final int DEFAULT_NUMBLOCKS_SMALL = 4;
/*      */   protected static final int NOTPROCESSED = -2;
/*      */   public DTMManager m_mgr;
/*  119 */   protected DTMManagerDefault m_mgrDefault = null;
/*      */   protected SuballocatedIntVector m_dtmIdent;
/*      */   protected String m_documentBaseURI;
/*      */   protected DTMWSFilter m_wsfilter;
/*  140 */   protected boolean m_shouldStripWS = false;
/*      */   protected BoolStack m_shouldStripWhitespaceStack;
/*      */   protected XMLStringFactory m_xstrf;
/*      */   protected ExpandedNameTable m_expandedNameTable;
/*      */   protected boolean m_indexing;
/*      */   protected DTMAxisTraverser[] m_traversers;
/* 1246 */   private Vector m_namespaceLists = null;
/*      */ 
/*      */   public DTMDefaultBase(DTMManager mgr, Source source, int dtmIdentity, DTMWSFilter whiteSpaceFilter, XMLStringFactory xstringfactory, boolean doIndexing)
/*      */   {
/*  173 */     this(mgr, source, dtmIdentity, whiteSpaceFilter, xstringfactory, doIndexing, 512, true, false);
/*      */   }
/*      */ 
/*      */   public DTMDefaultBase(DTMManager mgr, Source source, int dtmIdentity, DTMWSFilter whiteSpaceFilter, XMLStringFactory xstringfactory, boolean doIndexing, int blocksize, boolean usePrevsib, boolean newNameTable)
/*      */   {
/*      */     int numblocks;
/*  201 */     if (blocksize <= 64)
/*      */     {
/*  203 */       int numblocks = 4;
/*  204 */       this.m_dtmIdent = new SuballocatedIntVector(4, 1);
/*      */     }
/*      */     else
/*      */     {
/*  208 */       numblocks = 32;
/*  209 */       this.m_dtmIdent = new SuballocatedIntVector(32);
/*      */     }
/*      */ 
/*  212 */     this.m_exptype = new SuballocatedIntVector(blocksize, numblocks);
/*  213 */     this.m_firstch = new SuballocatedIntVector(blocksize, numblocks);
/*  214 */     this.m_nextsib = new SuballocatedIntVector(blocksize, numblocks);
/*  215 */     this.m_parent = new SuballocatedIntVector(blocksize, numblocks);
/*      */ 
/*  220 */     if (usePrevsib) {
/*  221 */       this.m_prevsib = new SuballocatedIntVector(blocksize, numblocks);
/*      */     }
/*  223 */     this.m_mgr = mgr;
/*  224 */     if ((mgr instanceof DTMManagerDefault)) {
/*  225 */       this.m_mgrDefault = ((DTMManagerDefault)mgr);
/*      */     }
/*  227 */     this.m_documentBaseURI = (null != source ? source.getSystemId() : null);
/*  228 */     this.m_dtmIdent.setElementAt(dtmIdentity, 0);
/*  229 */     this.m_wsfilter = whiteSpaceFilter;
/*  230 */     this.m_xstrf = xstringfactory;
/*  231 */     this.m_indexing = doIndexing;
/*      */ 
/*  233 */     if (doIndexing)
/*      */     {
/*  235 */       this.m_expandedNameTable = new ExpandedNameTable();
/*      */     }
/*      */     else
/*      */     {
/*  241 */       this.m_expandedNameTable = this.m_mgrDefault.getExpandedNameTable(this);
/*      */     }
/*      */ 
/*  244 */     if (null != whiteSpaceFilter)
/*      */     {
/*  246 */       this.m_shouldStripWhitespaceStack = new BoolStack();
/*      */ 
/*  248 */       pushShouldStripWhitespace(false);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void ensureSizeOfIndex(int namespaceID, int LocalNameID)
/*      */   {
/*  261 */     if (null == this.m_elemIndexes)
/*      */     {
/*  263 */       this.m_elemIndexes = new int[namespaceID + 20][][];
/*      */     }
/*  265 */     else if (this.m_elemIndexes.length <= namespaceID)
/*      */     {
/*  267 */       int[][][] indexes = this.m_elemIndexes;
/*      */ 
/*  269 */       this.m_elemIndexes = new int[namespaceID + 20][][];
/*      */ 
/*  271 */       System.arraycopy(indexes, 0, this.m_elemIndexes, 0, indexes.length);
/*      */     }
/*      */ 
/*  274 */     int[][] localNameIndex = this.m_elemIndexes[namespaceID];
/*      */ 
/*  276 */     if (null == localNameIndex)
/*      */     {
/*  278 */       localNameIndex = new int[LocalNameID + 100][];
/*  279 */       this.m_elemIndexes[namespaceID] = localNameIndex;
/*      */     }
/*  281 */     else if (localNameIndex.length <= LocalNameID)
/*      */     {
/*  283 */       int[][] indexes = localNameIndex;
/*      */ 
/*  285 */       localNameIndex = new int[LocalNameID + 100][];
/*      */ 
/*  287 */       System.arraycopy(indexes, 0, localNameIndex, 0, indexes.length);
/*      */ 
/*  289 */       this.m_elemIndexes[namespaceID] = localNameIndex;
/*      */     }
/*      */ 
/*  292 */     int[] elemHandles = localNameIndex[LocalNameID];
/*      */ 
/*  294 */     if (null == elemHandles)
/*      */     {
/*  296 */       elemHandles = new int[''];
/*  297 */       localNameIndex[LocalNameID] = elemHandles;
/*  298 */       elemHandles[0] = 1;
/*      */     }
/*  300 */     else if (elemHandles.length <= elemHandles[0] + 1)
/*      */     {
/*  302 */       int[] indexes = elemHandles;
/*      */ 
/*  304 */       elemHandles = new int[elemHandles[0] + 1024];
/*      */ 
/*  306 */       System.arraycopy(indexes, 0, elemHandles, 0, indexes.length);
/*      */ 
/*  308 */       localNameIndex[LocalNameID] = elemHandles;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void indexNode(int expandedTypeID, int identity)
/*      */   {
/*  322 */     ExpandedNameTable ent = this.m_expandedNameTable;
/*  323 */     short type = ent.getType(expandedTypeID);
/*      */ 
/*  325 */     if (1 == type)
/*      */     {
/*  327 */       int namespaceID = ent.getNamespaceID(expandedTypeID);
/*  328 */       int localNameID = ent.getLocalNameID(expandedTypeID);
/*      */ 
/*  330 */       ensureSizeOfIndex(namespaceID, localNameID);
/*      */ 
/*  332 */       int[] index = this.m_elemIndexes[namespaceID][localNameID];
/*      */ 
/*  334 */       index[index[0]] = identity;
/*      */ 
/*  336 */       index[0] += 1;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected int findGTE(int[] list, int start, int len, int value)
/*      */   {
/*  356 */     int low = start;
/*  357 */     int high = start + (len - 1);
/*  358 */     int end = high;
/*      */ 
/*  360 */     while (low <= high)
/*      */     {
/*  362 */       int mid = (low + high) / 2;
/*  363 */       int c = list[mid];
/*      */ 
/*  365 */       if (c > value)
/*  366 */         high = mid - 1;
/*  367 */       else if (c < value)
/*  368 */         low = mid + 1;
/*      */       else {
/*  370 */         return mid;
/*      */       }
/*      */     }
/*  373 */     return (low <= end) && (list[low] > value) ? low : -1;
/*      */   }
/*      */ 
/*      */   int findElementFromIndex(int nsIndex, int lnIndex, int firstPotential)
/*      */   {
/*  390 */     int[][][] indexes = this.m_elemIndexes;
/*      */ 
/*  392 */     if ((null != indexes) && (nsIndex < indexes.length))
/*      */     {
/*  394 */       int[][] lnIndexs = indexes[nsIndex];
/*      */ 
/*  396 */       if ((null != lnIndexs) && (lnIndex < lnIndexs.length))
/*      */       {
/*  398 */         int[] elems = lnIndexs[lnIndex];
/*      */ 
/*  400 */         if (null != elems)
/*      */         {
/*  402 */           int pos = findGTE(elems, 1, elems[0], firstPotential);
/*      */ 
/*  404 */           if (pos > -1)
/*      */           {
/*  406 */             return elems[pos];
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  412 */     return -2;
/*      */   }
/*      */ 
/*      */   protected abstract int getNextNodeIdentity(int paramInt);
/*      */ 
/*      */   protected abstract boolean nextNode();
/*      */ 
/*      */   protected abstract int getNumberOfNodes();
/*      */ 
/*      */   protected short _type(int identity)
/*      */   {
/*  464 */     int info = _exptype(identity);
/*      */ 
/*  466 */     if (-1 != info) {
/*  467 */       return this.m_expandedNameTable.getType(info);
/*      */     }
/*  469 */     return -1;
/*      */   }
/*      */ 
/*      */   protected int _exptype(int identity)
/*      */   {
/*  481 */     if (identity == -1) {
/*  482 */       return -1;
/*      */     }
/*      */ 
/*  486 */     while (identity >= this.m_size)
/*      */     {
/*  488 */       if ((!nextNode()) && (identity >= this.m_size))
/*  489 */         return -1;
/*      */     }
/*  491 */     return this.m_exptype.elementAt(identity);
/*      */   }
/*      */ 
/*      */   protected int _level(int identity)
/*      */   {
/*  504 */     while (identity >= this.m_size)
/*      */     {
/*  506 */       boolean isMore = nextNode();
/*  507 */       if ((!isMore) && (identity >= this.m_size)) {
/*  508 */         return -1;
/*      */       }
/*      */     }
/*  511 */     int i = 0;
/*  512 */     while (-1 != (identity = _parent(identity)))
/*  513 */       i++;
/*  514 */     return i;
/*      */   }
/*      */ 
/*      */   protected int _firstch(int identity)
/*      */   {
/*  528 */     int info = identity >= this.m_size ? -2 : this.m_firstch.elementAt(identity);
/*      */ 
/*  533 */     while (info == -2)
/*      */     {
/*  535 */       boolean isMore = nextNode();
/*      */ 
/*  537 */       if ((identity >= this.m_size) && (!isMore)) {
/*  538 */         return -1;
/*      */       }
/*      */ 
/*  541 */       info = this.m_firstch.elementAt(identity);
/*  542 */       if ((info == -2) && (!isMore)) {
/*  543 */         return -1;
/*      */       }
/*      */     }
/*      */ 
/*  547 */     return info;
/*      */   }
/*      */ 
/*      */   protected int _nextsib(int identity)
/*      */   {
/*  560 */     int info = identity >= this.m_size ? -2 : this.m_nextsib.elementAt(identity);
/*      */ 
/*  565 */     while (info == -2)
/*      */     {
/*  567 */       boolean isMore = nextNode();
/*      */ 
/*  569 */       if ((identity >= this.m_size) && (!isMore)) {
/*  570 */         return -1;
/*      */       }
/*      */ 
/*  573 */       info = this.m_nextsib.elementAt(identity);
/*  574 */       if ((info == -2) && (!isMore)) {
/*  575 */         return -1;
/*      */       }
/*      */     }
/*      */ 
/*  579 */     return info;
/*      */   }
/*      */ 
/*      */   protected int _prevsib(int identity)
/*      */   {
/*  592 */     if (identity < this.m_size) {
/*  593 */       return this.m_prevsib.elementAt(identity);
/*      */     }
/*      */ 
/*      */     while (true)
/*      */     {
/*  600 */       boolean isMore = nextNode();
/*      */ 
/*  602 */       if ((identity >= this.m_size) && (!isMore))
/*  603 */         return -1;
/*  604 */       if (identity < this.m_size)
/*  605 */         return this.m_prevsib.elementAt(identity);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected int _parent(int identity)
/*      */   {
/*  619 */     if (identity < this.m_size) {
/*  620 */       return this.m_parent.elementAt(identity);
/*      */     }
/*      */ 
/*      */     while (true)
/*      */     {
/*  627 */       boolean isMore = nextNode();
/*      */ 
/*  629 */       if ((identity >= this.m_size) && (!isMore))
/*  630 */         return -1;
/*  631 */       if (identity < this.m_size)
/*  632 */         return this.m_parent.elementAt(identity);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void dumpDTM(OutputStream os)
/*      */   {
/*      */     try
/*      */     {
/*  643 */       if (os == null)
/*      */       {
/*  645 */         File f = new File("DTMDump" + hashCode() + ".txt");
/*  646 */         System.err.println("Dumping... " + f.getAbsolutePath());
/*  647 */         os = new FileOutputStream(f);
/*      */       }
/*  649 */       PrintStream ps = new PrintStream(os);
/*      */ 
/*  651 */       while (nextNode());
/*  653 */       int nRecords = this.m_size;
/*      */ 
/*  655 */       ps.println("Total nodes: " + nRecords);
/*      */ 
/*  657 */       for (int index = 0; index < nRecords; index++)
/*      */       {
/*  659 */         int i = makeNodeHandle(index);
/*  660 */         ps.println("=========== index=" + index + " handle=" + i + " ===========");
/*  661 */         ps.println("NodeName: " + getNodeName(i));
/*  662 */         ps.println("NodeNameX: " + getNodeNameX(i));
/*  663 */         ps.println("LocalName: " + getLocalName(i));
/*  664 */         ps.println("NamespaceURI: " + getNamespaceURI(i));
/*  665 */         ps.println("Prefix: " + getPrefix(i));
/*      */ 
/*  667 */         int exTypeID = _exptype(index);
/*      */ 
/*  669 */         ps.println("Expanded Type ID: " + Integer.toHexString(exTypeID));
/*      */ 
/*  672 */         int type = _type(index);
/*      */         String typestring;
/*  675 */         switch (type)
/*      */         {
/*      */         case 2:
/*  678 */           typestring = "ATTRIBUTE_NODE";
/*  679 */           break;
/*      */         case 4:
/*  681 */           typestring = "CDATA_SECTION_NODE";
/*  682 */           break;
/*      */         case 8:
/*  684 */           typestring = "COMMENT_NODE";
/*  685 */           break;
/*      */         case 11:
/*  687 */           typestring = "DOCUMENT_FRAGMENT_NODE";
/*  688 */           break;
/*      */         case 9:
/*  690 */           typestring = "DOCUMENT_NODE";
/*  691 */           break;
/*      */         case 10:
/*  693 */           typestring = "DOCUMENT_NODE";
/*  694 */           break;
/*      */         case 1:
/*  696 */           typestring = "ELEMENT_NODE";
/*  697 */           break;
/*      */         case 6:
/*  699 */           typestring = "ENTITY_NODE";
/*  700 */           break;
/*      */         case 5:
/*  702 */           typestring = "ENTITY_REFERENCE_NODE";
/*  703 */           break;
/*      */         case 13:
/*  705 */           typestring = "NAMESPACE_NODE";
/*  706 */           break;
/*      */         case 12:
/*  708 */           typestring = "NOTATION_NODE";
/*  709 */           break;
/*      */         case -1:
/*  711 */           typestring = "NULL";
/*  712 */           break;
/*      */         case 7:
/*  714 */           typestring = "PROCESSING_INSTRUCTION_NODE";
/*  715 */           break;
/*      */         case 3:
/*  717 */           typestring = "TEXT_NODE";
/*  718 */           break;
/*      */         case 0:
/*      */         default:
/*  720 */           typestring = "Unknown!";
/*      */         }
/*      */ 
/*  724 */         ps.println("Type: " + typestring);
/*      */ 
/*  726 */         int firstChild = _firstch(index);
/*      */ 
/*  728 */         if (-1 == firstChild)
/*  729 */           ps.println("First child: DTM.NULL");
/*  730 */         else if (-2 == firstChild)
/*  731 */           ps.println("First child: NOTPROCESSED");
/*      */         else {
/*  733 */           ps.println("First child: " + firstChild);
/*      */         }
/*  735 */         if (this.m_prevsib != null)
/*      */         {
/*  737 */           int prevSibling = _prevsib(index);
/*      */ 
/*  739 */           if (-1 == prevSibling)
/*  740 */             ps.println("Prev sibling: DTM.NULL");
/*  741 */           else if (-2 == prevSibling)
/*  742 */             ps.println("Prev sibling: NOTPROCESSED");
/*      */           else {
/*  744 */             ps.println("Prev sibling: " + prevSibling);
/*      */           }
/*      */         }
/*  747 */         int nextSibling = _nextsib(index);
/*      */ 
/*  749 */         if (-1 == nextSibling)
/*  750 */           ps.println("Next sibling: DTM.NULL");
/*  751 */         else if (-2 == nextSibling)
/*  752 */           ps.println("Next sibling: NOTPROCESSED");
/*      */         else {
/*  754 */           ps.println("Next sibling: " + nextSibling);
/*      */         }
/*  756 */         int parent = _parent(index);
/*      */ 
/*  758 */         if (-1 == parent)
/*  759 */           ps.println("Parent: DTM.NULL");
/*  760 */         else if (-2 == parent)
/*  761 */           ps.println("Parent: NOTPROCESSED");
/*      */         else {
/*  763 */           ps.println("Parent: " + parent);
/*      */         }
/*  765 */         int level = _level(index);
/*      */ 
/*  767 */         ps.println("Level: " + level);
/*  768 */         ps.println("Node Value: " + getNodeValue(i));
/*  769 */         ps.println("String Value: " + getStringValue(i));
/*      */       }
/*      */     }
/*      */     catch (IOException ioe)
/*      */     {
/*  774 */       ioe.printStackTrace(System.err);
/*  775 */       throw new RuntimeException(ioe.getMessage());
/*      */     }
/*      */   }
/*      */ 
/*      */   public String dumpNode(int nodeHandle)
/*      */   {
/*  794 */     if (nodeHandle == -1)
/*  795 */       return "[null]";
/*      */     String typestring;
/*  798 */     switch (getNodeType(nodeHandle))
/*      */     {
/*      */     case 2:
/*  801 */       typestring = "ATTR";
/*  802 */       break;
/*      */     case 4:
/*  804 */       typestring = "CDATA";
/*  805 */       break;
/*      */     case 8:
/*  807 */       typestring = "COMMENT";
/*  808 */       break;
/*      */     case 11:
/*  810 */       typestring = "DOC_FRAG";
/*  811 */       break;
/*      */     case 9:
/*  813 */       typestring = "DOC";
/*  814 */       break;
/*      */     case 10:
/*  816 */       typestring = "DOC_TYPE";
/*  817 */       break;
/*      */     case 1:
/*  819 */       typestring = "ELEMENT";
/*  820 */       break;
/*      */     case 6:
/*  822 */       typestring = "ENTITY";
/*  823 */       break;
/*      */     case 5:
/*  825 */       typestring = "ENT_REF";
/*  826 */       break;
/*      */     case 13:
/*  828 */       typestring = "NAMESPACE";
/*  829 */       break;
/*      */     case 12:
/*  831 */       typestring = "NOTATION";
/*  832 */       break;
/*      */     case -1:
/*  834 */       typestring = "null";
/*  835 */       break;
/*      */     case 7:
/*  837 */       typestring = "PI";
/*  838 */       break;
/*      */     case 3:
/*  840 */       typestring = "TEXT";
/*  841 */       break;
/*      */     case 0:
/*      */     default:
/*  843 */       typestring = "Unknown!";
/*      */     }
/*      */ 
/*  847 */     return "[" + nodeHandle + ": " + typestring + "(0x" + Integer.toHexString(getExpandedTypeID(nodeHandle)) + ") " + getNodeNameX(nodeHandle) + " {" + getNamespaceURI(nodeHandle) + "}" + "=\"" + getNodeValue(nodeHandle) + "\"]";
/*      */   }
/*      */ 
/*      */   public void setFeature(String featureId, boolean state)
/*      */   {
/*      */   }
/*      */ 
/*      */   public boolean hasChildNodes(int nodeHandle)
/*      */   {
/*  882 */     int identity = makeNodeIdentity(nodeHandle);
/*  883 */     int firstChild = _firstch(identity);
/*      */ 
/*  885 */     return firstChild != -1;
/*      */   }
/*      */ 
/*      */   public final int makeNodeHandle(int nodeIdentity)
/*      */   {
/*  904 */     if (-1 == nodeIdentity) return -1;
/*      */ 
/*  909 */     return this.m_dtmIdent.elementAt(nodeIdentity >>> 16) + (nodeIdentity & 0xFFFF);
/*      */   }
/*      */ 
/*      */   public final int makeNodeIdentity(int nodeHandle)
/*      */   {
/*  931 */     if (-1 == nodeHandle) return -1;
/*      */ 
/*  933 */     if (this.m_mgrDefault != null)
/*      */     {
/*  939 */       int whichDTMindex = nodeHandle >>> 16;
/*      */ 
/*  945 */       if (this.m_mgrDefault.m_dtms[whichDTMindex] != this) {
/*  946 */         return -1;
/*      */       }
/*  948 */       return this.m_mgrDefault.m_dtm_offsets[whichDTMindex] | nodeHandle & 0xFFFF;
/*      */     }
/*      */ 
/*  953 */     int whichDTMid = this.m_dtmIdent.indexOf(nodeHandle & 0xFFFF0000);
/*  954 */     return whichDTMid == -1 ? -1 : (whichDTMid << 16) + (nodeHandle & 0xFFFF);
/*      */   }
/*      */ 
/*      */   public int getFirstChild(int nodeHandle)
/*      */   {
/*  972 */     int identity = makeNodeIdentity(nodeHandle);
/*  973 */     int firstChild = _firstch(identity);
/*      */ 
/*  975 */     return makeNodeHandle(firstChild);
/*      */   }
/*      */ 
/*      */   public int getTypedFirstChild(int nodeHandle, int nodeType)
/*      */   {
/*  990 */     if (nodeType < 14) {
/*  991 */       for (int firstChild = _firstch(makeNodeIdentity(nodeHandle)); 
/*  992 */         firstChild != -1; 
/*  993 */         firstChild = _nextsib(firstChild)) {
/*  994 */         int eType = _exptype(firstChild);
/*  995 */         if ((eType == nodeType) || ((eType >= 14) && (this.m_expandedNameTable.getType(eType) == nodeType)))
/*      */         {
/*  998 */           return makeNodeHandle(firstChild);
/*      */         }
/*      */       }
/*      */     }
/* 1002 */     for (int firstChild = _firstch(makeNodeIdentity(nodeHandle)); 
/* 1003 */       firstChild != -1; 
/* 1004 */       firstChild = _nextsib(firstChild)) {
/* 1005 */       if (_exptype(firstChild) == nodeType) {
/* 1006 */         return makeNodeHandle(firstChild);
/*      */       }
/*      */     }
/*      */ 
/* 1010 */     return -1;
/*      */   }
/*      */ 
/*      */   public int getLastChild(int nodeHandle)
/*      */   {
/* 1025 */     int identity = makeNodeIdentity(nodeHandle);
/* 1026 */     int child = _firstch(identity);
/* 1027 */     int lastChild = -1;
/*      */ 
/* 1029 */     while (child != -1)
/*      */     {
/* 1031 */       lastChild = child;
/* 1032 */       child = _nextsib(child);
/*      */     }
/*      */ 
/* 1035 */     return makeNodeHandle(lastChild);
/*      */   }
/*      */ 
/*      */   public abstract int getAttributeNode(int paramInt, String paramString1, String paramString2);
/*      */ 
/*      */   public int getFirstAttribute(int nodeHandle)
/*      */   {
/* 1061 */     int nodeID = makeNodeIdentity(nodeHandle);
/*      */ 
/* 1063 */     return makeNodeHandle(getFirstAttributeIdentity(nodeID));
/*      */   }
/*      */ 
/*      */   protected int getFirstAttributeIdentity(int identity)
/*      */   {
/* 1073 */     int type = _type(identity);
/*      */ 
/* 1075 */     if (1 == type)
/*      */     {
/* 1078 */       while (-1 != (identity = getNextNodeIdentity(identity)))
/*      */       {
/* 1082 */         type = _type(identity);
/*      */ 
/* 1084 */         if (type == 2)
/*      */         {
/* 1086 */           return identity;
/*      */         }
/* 1088 */         if (13 != type)
/*      */         {
/* 1090 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1095 */     return -1;
/*      */   }
/*      */ 
/*      */   protected int getTypedAttribute(int nodeHandle, int attType)
/*      */   {
/* 1108 */     int type = getNodeType(nodeHandle);
/* 1109 */     if (1 == type) {
/* 1110 */       int identity = makeNodeIdentity(nodeHandle);
/*      */ 
/* 1112 */       while (-1 != (identity = getNextNodeIdentity(identity)))
/*      */       {
/* 1114 */         type = _type(identity);
/*      */ 
/* 1116 */         if (type == 2)
/*      */         {
/* 1118 */           if (_exptype(identity) == attType) return makeNodeHandle(identity);
/*      */         }
/* 1120 */         else if (13 != type)
/*      */         {
/* 1122 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1127 */     return -1;
/*      */   }
/*      */ 
/*      */   public int getNextSibling(int nodeHandle)
/*      */   {
/* 1140 */     if (nodeHandle == -1)
/* 1141 */       return -1;
/* 1142 */     return makeNodeHandle(_nextsib(makeNodeIdentity(nodeHandle)));
/*      */   }
/*      */ 
/*      */   public int getTypedNextSibling(int nodeHandle, int nodeType)
/*      */   {
/* 1155 */     if (nodeHandle == -1)
/* 1156 */       return -1;
/* 1157 */     int node = makeNodeIdentity(nodeHandle);
/*      */     int eType;
/* 1159 */     while (((node = _nextsib(node)) != -1) && ((eType = _exptype(node)) != nodeType) && (this.m_expandedNameTable.getType(eType) != nodeType));
/* 1164 */     return node == -1 ? -1 : makeNodeHandle(node);
/*      */   }
/*      */ 
/*      */   public int getPreviousSibling(int nodeHandle)
/*      */   {
/* 1178 */     if (nodeHandle == -1) {
/* 1179 */       return -1;
/*      */     }
/* 1181 */     if (this.m_prevsib != null) {
/* 1182 */       return makeNodeHandle(_prevsib(makeNodeIdentity(nodeHandle)));
/*      */     }
/*      */ 
/* 1188 */     int nodeID = makeNodeIdentity(nodeHandle);
/* 1189 */     int parent = _parent(nodeID);
/* 1190 */     int node = _firstch(parent);
/* 1191 */     int result = -1;
/* 1192 */     while (node != nodeID)
/*      */     {
/* 1194 */       result = node;
/* 1195 */       node = _nextsib(node);
/*      */     }
/* 1197 */     return makeNodeHandle(result);
/*      */   }
/*      */ 
/*      */   public int getNextAttribute(int nodeHandle)
/*      */   {
/* 1211 */     int nodeID = makeNodeIdentity(nodeHandle);
/*      */ 
/* 1213 */     if (_type(nodeID) == 2) {
/* 1214 */       return makeNodeHandle(getNextAttributeIdentity(nodeID));
/*      */     }
/*      */ 
/* 1217 */     return -1;
/*      */   }
/*      */ 
/*      */   protected int getNextAttributeIdentity(int identity)
/*      */   {
/* 1232 */     while (-1 != (identity = getNextNodeIdentity(identity))) {
/* 1233 */       int type = _type(identity);
/*      */ 
/* 1235 */       if (type == 2)
/* 1236 */         return identity;
/* 1237 */       if (type != 13)
/*      */       {
/*      */         break;
/*      */       }
/*      */     }
/* 1242 */     return -1;
/*      */   }
/*      */ 
/*      */   protected void declareNamespaceInContext(int elementNodeIndex, int namespaceNodeIndex)
/*      */   {
/* 1264 */     SuballocatedIntVector nsList = null;
/* 1265 */     if (this.m_namespaceDeclSets == null)
/*      */     {
/* 1269 */       this.m_namespaceDeclSetElements = new SuballocatedIntVector(32);
/* 1270 */       this.m_namespaceDeclSetElements.addElement(elementNodeIndex);
/* 1271 */       this.m_namespaceDeclSets = new Vector();
/* 1272 */       nsList = new SuballocatedIntVector(32);
/* 1273 */       this.m_namespaceDeclSets.addElement(nsList);
/*      */     }
/*      */     else
/*      */     {
/* 1279 */       int last = this.m_namespaceDeclSetElements.size() - 1;
/*      */ 
/* 1281 */       if ((last >= 0) && (elementNodeIndex == this.m_namespaceDeclSetElements.elementAt(last)))
/*      */       {
/* 1283 */         nsList = (SuballocatedIntVector)this.m_namespaceDeclSets.elementAt(last);
/*      */       }
/*      */     }
/* 1286 */     if (nsList == null)
/*      */     {
/* 1288 */       this.m_namespaceDeclSetElements.addElement(elementNodeIndex);
/*      */ 
/* 1290 */       SuballocatedIntVector inherited = findNamespaceContext(_parent(elementNodeIndex));
/*      */ 
/* 1293 */       if (inherited != null)
/*      */       {
/* 1297 */         int isize = inherited.size();
/*      */ 
/* 1301 */         nsList = new SuballocatedIntVector(Math.max(Math.min(isize + 16, 2048), 32));
/*      */ 
/* 1304 */         for (int i = 0; i < isize; i++)
/*      */         {
/* 1306 */           nsList.addElement(inherited.elementAt(i));
/*      */         }
/*      */       } else {
/* 1309 */         nsList = new SuballocatedIntVector(32);
/*      */       }
/*      */ 
/* 1312 */       this.m_namespaceDeclSets.addElement(nsList);
/*      */     }
/*      */ 
/* 1319 */     int newEType = _exptype(namespaceNodeIndex);
/*      */ 
/* 1321 */     for (int i = nsList.size() - 1; i >= 0; i--)
/*      */     {
/* 1323 */       if (newEType == getExpandedTypeID(nsList.elementAt(i)))
/*      */       {
/* 1325 */         nsList.setElementAt(makeNodeHandle(namespaceNodeIndex), i);
/* 1326 */         return;
/*      */       }
/*      */     }
/* 1329 */     nsList.addElement(makeNodeHandle(namespaceNodeIndex));
/*      */   }
/*      */ 
/*      */   protected SuballocatedIntVector findNamespaceContext(int elementNodeIndex)
/*      */   {
/* 1341 */     if (null != this.m_namespaceDeclSetElements)
/*      */     {
/* 1345 */       int wouldBeAt = findInSortedSuballocatedIntVector(this.m_namespaceDeclSetElements, elementNodeIndex);
/*      */ 
/* 1347 */       if (wouldBeAt >= 0)
/* 1348 */         return (SuballocatedIntVector)this.m_namespaceDeclSets.elementAt(wouldBeAt);
/* 1349 */       if (wouldBeAt == -1) {
/* 1350 */         return null;
/*      */       }
/*      */ 
/* 1354 */       wouldBeAt = -1 - wouldBeAt;
/*      */ 
/* 1357 */       int candidate = this.m_namespaceDeclSetElements.elementAt(--wouldBeAt);
/* 1358 */       int ancestor = _parent(elementNodeIndex);
/*      */ 
/* 1363 */       if ((wouldBeAt == 0) && (candidate < ancestor)) {
/* 1364 */         int rootHandle = getDocumentRoot(makeNodeHandle(elementNodeIndex));
/* 1365 */         int rootID = makeNodeIdentity(rootHandle);
/*      */         int uppermostNSCandidateID;
/*      */         int uppermostNSCandidateID;
/* 1368 */         if (getNodeType(rootHandle) == 9) {
/* 1369 */           int ch = _firstch(rootID);
/* 1370 */           uppermostNSCandidateID = ch != -1 ? ch : rootID;
/*      */         } else {
/* 1372 */           uppermostNSCandidateID = rootID;
/*      */         }
/*      */ 
/* 1375 */         if (candidate == uppermostNSCandidateID) {
/* 1376 */           return (SuballocatedIntVector)this.m_namespaceDeclSets.elementAt(wouldBeAt);
/*      */         }
/*      */       }
/*      */ 
/* 1380 */       while ((wouldBeAt >= 0) && (ancestor > 0))
/*      */       {
/* 1382 */         if (candidate == ancestor)
/*      */         {
/* 1384 */           return (SuballocatedIntVector)this.m_namespaceDeclSets.elementAt(wouldBeAt);
/* 1385 */         }if (candidate < ancestor)
/*      */         {
/*      */           do
/* 1388 */             ancestor = _parent(ancestor);
/* 1389 */           while (candidate < ancestor); } else {
/* 1390 */           if (wouldBeAt <= 0)
/*      */             break;
/* 1392 */           candidate = this.m_namespaceDeclSetElements.elementAt(--wouldBeAt);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1399 */     return null;
/*      */   }
/*      */ 
/*      */   protected int findInSortedSuballocatedIntVector(SuballocatedIntVector vector, int lookfor)
/*      */   {
/* 1418 */     int i = 0;
/* 1419 */     if (vector != null) {
/* 1420 */       int first = 0;
/* 1421 */       int last = vector.size() - 1;
/*      */ 
/* 1423 */       while (first <= last) {
/* 1424 */         i = (first + last) / 2;
/* 1425 */         int test = lookfor - vector.elementAt(i);
/* 1426 */         if (test == 0) {
/* 1427 */           return i;
/*      */         }
/* 1429 */         if (test < 0) {
/* 1430 */           last = i - 1;
/*      */         }
/*      */         else {
/* 1433 */           first = i + 1;
/*      */         }
/*      */       }
/*      */ 
/* 1437 */       if (first > i) {
/* 1438 */         i = first;
/*      */       }
/*      */     }
/*      */ 
/* 1442 */     return -1 - i;
/*      */   }
/*      */ 
/*      */   public int getFirstNamespaceNode(int nodeHandle, boolean inScope)
/*      */   {
/* 1461 */     if (inScope)
/*      */     {
/* 1463 */       int identity = makeNodeIdentity(nodeHandle);
/* 1464 */       if (_type(identity) == 1)
/*      */       {
/* 1466 */         SuballocatedIntVector nsContext = findNamespaceContext(identity);
/* 1467 */         if ((nsContext == null) || (nsContext.size() < 1)) {
/* 1468 */           return -1;
/*      */         }
/* 1470 */         return nsContext.elementAt(0);
/*      */       }
/*      */ 
/* 1473 */       return -1;
/*      */     }
/*      */ 
/* 1483 */     int identity = makeNodeIdentity(nodeHandle);
/* 1484 */     if (_type(identity) == 1)
/*      */     {
/* 1486 */       while (-1 != (identity = getNextNodeIdentity(identity)))
/*      */       {
/* 1488 */         int type = _type(identity);
/* 1489 */         if (type == 13)
/* 1490 */           return makeNodeHandle(identity);
/* 1491 */         if (2 != type)
/*      */           break;
/*      */       }
/* 1494 */       return -1;
/*      */     }
/*      */ 
/* 1497 */     return -1;
/*      */   }
/*      */ 
/*      */   public int getNextNamespaceNode(int baseHandle, int nodeHandle, boolean inScope)
/*      */   {
/* 1514 */     if (inScope)
/*      */     {
/* 1521 */       SuballocatedIntVector nsContext = findNamespaceContext(makeNodeIdentity(baseHandle));
/*      */ 
/* 1523 */       if (nsContext == null)
/* 1524 */         return -1;
/* 1525 */       int i = 1 + nsContext.indexOf(nodeHandle);
/* 1526 */       if ((i <= 0) || (i == nsContext.size())) {
/* 1527 */         return -1;
/*      */       }
/* 1529 */       return nsContext.elementAt(i);
/*      */     }
/*      */ 
/* 1534 */     int identity = makeNodeIdentity(nodeHandle);
/* 1535 */     while (-1 != (identity = getNextNodeIdentity(identity)))
/*      */     {
/* 1537 */       int type = _type(identity);
/* 1538 */       if (type == 13)
/*      */       {
/* 1540 */         return makeNodeHandle(identity);
/*      */       }
/* 1542 */       if (type != 2)
/*      */       {
/*      */         break;
/*      */       }
/*      */     }
/*      */ 
/* 1548 */     return -1;
/*      */   }
/*      */ 
/*      */   public int getParent(int nodeHandle)
/*      */   {
/* 1561 */     int identity = makeNodeIdentity(nodeHandle);
/*      */ 
/* 1563 */     if (identity > 0) {
/* 1564 */       return makeNodeHandle(_parent(identity));
/*      */     }
/* 1566 */     return -1;
/*      */   }
/*      */ 
/*      */   public int getDocument()
/*      */   {
/* 1579 */     return this.m_dtmIdent.elementAt(0);
/*      */   }
/*      */ 
/*      */   public int getOwnerDocument(int nodeHandle)
/*      */   {
/* 1597 */     if (9 == getNodeType(nodeHandle)) {
/* 1598 */       return -1;
/*      */     }
/* 1600 */     return getDocumentRoot(nodeHandle);
/*      */   }
/*      */ 
/*      */   public int getDocumentRoot(int nodeHandle)
/*      */   {
/* 1613 */     return getManager().getDTM(nodeHandle).getDocument();
/*      */   }
/*      */ 
/*      */   public abstract XMLString getStringValue(int paramInt);
/*      */ 
/*      */   public int getStringValueChunkCount(int nodeHandle)
/*      */   {
/* 1643 */     error(XMLMessages.createXMLMessage("ER_METHOD_NOT_SUPPORTED", null));
/*      */ 
/* 1645 */     return 0;
/*      */   }
/*      */ 
/*      */   public char[] getStringValueChunk(int nodeHandle, int chunkIndex, int[] startAndLen)
/*      */   {
/* 1666 */     error(XMLMessages.createXMLMessage("ER_METHOD_NOT_SUPPORTED", null));
/*      */ 
/* 1668 */     return null;
/*      */   }
/*      */ 
/*      */   public int getExpandedTypeID(int nodeHandle)
/*      */   {
/* 1682 */     int id = makeNodeIdentity(nodeHandle);
/* 1683 */     if (id == -1)
/* 1684 */       return -1;
/* 1685 */     return _exptype(id);
/*      */   }
/*      */ 
/*      */   public int getExpandedTypeID(String namespace, String localName, int type)
/*      */   {
/* 1707 */     ExpandedNameTable ent = this.m_expandedNameTable;
/*      */ 
/* 1709 */     return ent.getExpandedTypeID(namespace, localName, type);
/*      */   }
/*      */ 
/*      */   public String getLocalNameFromExpandedNameID(int expandedNameID)
/*      */   {
/* 1720 */     return this.m_expandedNameTable.getLocalName(expandedNameID);
/*      */   }
/*      */ 
/*      */   public String getNamespaceFromExpandedNameID(int expandedNameID)
/*      */   {
/* 1732 */     return this.m_expandedNameTable.getNamespace(expandedNameID);
/*      */   }
/*      */ 
/*      */   public int getNamespaceType(int nodeHandle)
/*      */   {
/* 1743 */     int identity = makeNodeIdentity(nodeHandle);
/* 1744 */     int expandedNameID = _exptype(identity);
/*      */ 
/* 1746 */     return this.m_expandedNameTable.getNamespaceID(expandedNameID);
/*      */   }
/*      */ 
/*      */   public abstract String getNodeName(int paramInt);
/*      */ 
/*      */   public String getNodeNameX(int nodeHandle)
/*      */   {
/* 1772 */     error(XMLMessages.createXMLMessage("ER_METHOD_NOT_SUPPORTED", null));
/*      */ 
/* 1774 */     return null;
/*      */   }
/*      */ 
/*      */   public abstract String getLocalName(int paramInt);
/*      */ 
/*      */   public abstract String getPrefix(int paramInt);
/*      */ 
/*      */   public abstract String getNamespaceURI(int paramInt);
/*      */ 
/*      */   public abstract String getNodeValue(int paramInt);
/*      */ 
/*      */   public short getNodeType(int nodeHandle)
/*      */   {
/* 1836 */     if (nodeHandle == -1)
/* 1837 */       return -1;
/* 1838 */     return this.m_expandedNameTable.getType(_exptype(makeNodeIdentity(nodeHandle)));
/*      */   }
/*      */ 
/*      */   public short getLevel(int nodeHandle)
/*      */   {
/* 1852 */     int identity = makeNodeIdentity(nodeHandle);
/* 1853 */     return (short)(_level(identity) + 1);
/*      */   }
/*      */ 
/*      */   public int getNodeIdent(int nodeHandle)
/*      */   {
/* 1870 */     return makeNodeIdentity(nodeHandle);
/*      */   }
/*      */ 
/*      */   public int getNodeHandle(int nodeId)
/*      */   {
/* 1887 */     return makeNodeHandle(nodeId);
/*      */   }
/*      */ 
/*      */   public boolean isSupported(String feature, String version)
/*      */   {
/* 1908 */     return false;
/*      */   }
/*      */ 
/*      */   public String getDocumentBaseURI()
/*      */   {
/* 1920 */     return this.m_documentBaseURI;
/*      */   }
/*      */ 
/*      */   public void setDocumentBaseURI(String baseURI)
/*      */   {
/* 1930 */     this.m_documentBaseURI = baseURI;
/*      */   }
/*      */ 
/*      */   public String getDocumentSystemIdentifier(int nodeHandle)
/*      */   {
/* 1944 */     return this.m_documentBaseURI;
/*      */   }
/*      */ 
/*      */   public String getDocumentEncoding(int nodeHandle)
/*      */   {
/* 1959 */     return "UTF-8";
/*      */   }
/*      */ 
/*      */   public String getDocumentStandalone(int nodeHandle)
/*      */   {
/* 1974 */     return null;
/*      */   }
/*      */ 
/*      */   public String getDocumentVersion(int documentHandle)
/*      */   {
/* 1989 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean getDocumentAllDeclarationsProcessed()
/*      */   {
/* 2006 */     return true;
/*      */   }
/*      */ 
/*      */   public abstract String getDocumentTypeDeclarationSystemIdentifier();
/*      */ 
/*      */   public abstract String getDocumentTypeDeclarationPublicIdentifier();
/*      */ 
/*      */   public abstract int getElementById(String paramString);
/*      */ 
/*      */   public abstract String getUnparsedEntityURI(String paramString);
/*      */ 
/*      */   public boolean supportsPreStripping()
/*      */   {
/* 2094 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean isNodeAfter(int nodeHandle1, int nodeHandle2)
/*      */   {
/* 2116 */     int index1 = makeNodeIdentity(nodeHandle1);
/* 2117 */     int index2 = makeNodeIdentity(nodeHandle2);
/*      */ 
/* 2119 */     return (index1 != -1) && (index2 != -1) && (index1 <= index2);
/*      */   }
/*      */ 
/*      */   public boolean isCharacterElementContentWhitespace(int nodeHandle)
/*      */   {
/* 2142 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean isDocumentAllDeclarationsProcessed(int documentHandle)
/*      */   {
/* 2159 */     return true;
/*      */   }
/*      */ 
/*      */   public abstract boolean isAttributeSpecified(int paramInt);
/*      */ 
/*      */   public abstract void dispatchCharactersEvents(int paramInt, ContentHandler paramContentHandler, boolean paramBoolean)
/*      */     throws SAXException;
/*      */ 
/*      */   public abstract void dispatchToEvents(int paramInt, ContentHandler paramContentHandler)
/*      */     throws SAXException;
/*      */ 
/*      */   public Node getNode(int nodeHandle)
/*      */   {
/* 2218 */     return new DTMNodeProxy(this, nodeHandle);
/*      */   }
/*      */ 
/*      */   public void appendChild(int newChild, boolean clone, boolean cloneDepth)
/*      */   {
/* 2237 */     error(XMLMessages.createXMLMessage("ER_METHOD_NOT_SUPPORTED", null));
/*      */   }
/*      */ 
/*      */   public void appendTextChild(String str)
/*      */   {
/* 2251 */     error(XMLMessages.createXMLMessage("ER_METHOD_NOT_SUPPORTED", null));
/*      */   }
/*      */ 
/*      */   protected void error(String msg)
/*      */   {
/* 2261 */     throw new DTMException(msg);
/*      */   }
/*      */ 
/*      */   protected boolean getShouldStripWhitespace()
/*      */   {
/* 2272 */     return this.m_shouldStripWS;
/*      */   }
/*      */ 
/*      */   protected void pushShouldStripWhitespace(boolean shouldStrip)
/*      */   {
/* 2284 */     this.m_shouldStripWS = shouldStrip;
/*      */ 
/* 2286 */     if (null != this.m_shouldStripWhitespaceStack)
/* 2287 */       this.m_shouldStripWhitespaceStack.push(shouldStrip);
/*      */   }
/*      */ 
/*      */   protected void popShouldStripWhitespace()
/*      */   {
/* 2297 */     if (null != this.m_shouldStripWhitespaceStack)
/* 2298 */       this.m_shouldStripWS = this.m_shouldStripWhitespaceStack.popAndTop();
/*      */   }
/*      */ 
/*      */   protected void setShouldStripWhitespace(boolean shouldStrip)
/*      */   {
/* 2311 */     this.m_shouldStripWS = shouldStrip;
/*      */ 
/* 2313 */     if (null != this.m_shouldStripWhitespaceStack)
/* 2314 */       this.m_shouldStripWhitespaceStack.setTop(shouldStrip);
/*      */   }
/*      */ 
/*      */   public void documentRegistration()
/*      */   {
/*      */   }
/*      */ 
/*      */   public void documentRelease()
/*      */   {
/*      */   }
/*      */ 
/*      */   public void migrateTo(DTMManager mgr)
/*      */   {
/* 2344 */     this.m_mgr = mgr;
/* 2345 */     if ((mgr instanceof DTMManagerDefault))
/* 2346 */       this.m_mgrDefault = ((DTMManagerDefault)mgr);
/*      */   }
/*      */ 
/*      */   public DTMManager getManager()
/*      */   {
/* 2357 */     return this.m_mgr;
/*      */   }
/*      */ 
/*      */   public SuballocatedIntVector getDTMIDs()
/*      */   {
/* 2368 */     if (this.m_mgr == null) return null;
/* 2369 */     return this.m_dtmIdent;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase
 * JD-Core Version:    0.6.2
 */