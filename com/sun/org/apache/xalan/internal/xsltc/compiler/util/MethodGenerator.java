/*      */ package com.sun.org.apache.xalan.internal.xsltc.compiler.util;
/*      */ 
/*      */ import com.sun.org.apache.bcel.internal.classfile.Field;
/*      */ import com.sun.org.apache.bcel.internal.classfile.Method;
/*      */ import com.sun.org.apache.bcel.internal.generic.ALOAD;
/*      */ import com.sun.org.apache.bcel.internal.generic.ASTORE;
/*      */ import com.sun.org.apache.bcel.internal.generic.BranchHandle;
/*      */ import com.sun.org.apache.bcel.internal.generic.BranchInstruction;
/*      */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*      */ import com.sun.org.apache.bcel.internal.generic.DLOAD;
/*      */ import com.sun.org.apache.bcel.internal.generic.DSTORE;
/*      */ import com.sun.org.apache.bcel.internal.generic.FLOAD;
/*      */ import com.sun.org.apache.bcel.internal.generic.FSTORE;
/*      */ import com.sun.org.apache.bcel.internal.generic.GETFIELD;
/*      */ import com.sun.org.apache.bcel.internal.generic.GOTO;
/*      */ import com.sun.org.apache.bcel.internal.generic.ICONST;
/*      */ import com.sun.org.apache.bcel.internal.generic.ILOAD;
/*      */ import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
/*      */ import com.sun.org.apache.bcel.internal.generic.INVOKESPECIAL;
/*      */ import com.sun.org.apache.bcel.internal.generic.INVOKESTATIC;
/*      */ import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
/*      */ import com.sun.org.apache.bcel.internal.generic.ISTORE;
/*      */ import com.sun.org.apache.bcel.internal.generic.IfInstruction;
/*      */ import com.sun.org.apache.bcel.internal.generic.IndexedInstruction;
/*      */ import com.sun.org.apache.bcel.internal.generic.Instruction;
/*      */ import com.sun.org.apache.bcel.internal.generic.InstructionConstants;
/*      */ import com.sun.org.apache.bcel.internal.generic.InstructionHandle;
/*      */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*      */ import com.sun.org.apache.bcel.internal.generic.InstructionTargeter;
/*      */ import com.sun.org.apache.bcel.internal.generic.LLOAD;
/*      */ import com.sun.org.apache.bcel.internal.generic.LSTORE;
/*      */ import com.sun.org.apache.bcel.internal.generic.LocalVariableGen;
/*      */ import com.sun.org.apache.bcel.internal.generic.LocalVariableInstruction;
/*      */ import com.sun.org.apache.bcel.internal.generic.MethodGen;
/*      */ import com.sun.org.apache.bcel.internal.generic.NEW;
/*      */ import com.sun.org.apache.bcel.internal.generic.PUTFIELD;
/*      */ import com.sun.org.apache.bcel.internal.generic.RET;
/*      */ import com.sun.org.apache.bcel.internal.generic.Select;
/*      */ import com.sun.org.apache.bcel.internal.generic.TargetLostException;
/*      */ import com.sun.org.apache.bcel.internal.generic.Type;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.compiler.Parser;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.compiler.Pattern;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.compiler.Stylesheet;
/*      */ import com.sun.org.apache.xalan.internal.xsltc.compiler.XSLTC;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ import java.util.Stack;
/*      */ 
/*      */ public class MethodGenerator extends MethodGen
/*      */   implements Constants
/*      */ {
/*      */   protected static final int INVALID_INDEX = -1;
/*      */   private static final String START_ELEMENT_SIG = "(Ljava/lang/String;)V";
/*      */   private static final String END_ELEMENT_SIG = "(Ljava/lang/String;)V";
/*      */   private InstructionList _mapTypeSub;
/*      */   private static final int DOM_INDEX = 1;
/*      */   private static final int ITERATOR_INDEX = 2;
/*      */   private static final int HANDLER_INDEX = 3;
/*      */   private static final int MAX_METHOD_SIZE = 65535;
/*      */   private static final int MAX_BRANCH_TARGET_OFFSET = 32767;
/*      */   private static final int MIN_BRANCH_TARGET_OFFSET = -32768;
/*      */   private static final int TARGET_METHOD_SIZE = 60000;
/*      */   private static final int MINIMUM_OUTLINEABLE_CHUNK_SIZE = 1000;
/*      */   private Instruction _iloadCurrent;
/*      */   private Instruction _istoreCurrent;
/*      */   private final Instruction _astoreHandler;
/*      */   private final Instruction _aloadHandler;
/*      */   private final Instruction _astoreIterator;
/*      */   private final Instruction _aloadIterator;
/*      */   private final Instruction _aloadDom;
/*      */   private final Instruction _astoreDom;
/*      */   private final Instruction _startElement;
/*      */   private final Instruction _endElement;
/*      */   private final Instruction _startDocument;
/*      */   private final Instruction _endDocument;
/*      */   private final Instruction _attribute;
/*      */   private final Instruction _uniqueAttribute;
/*      */   private final Instruction _namespace;
/*      */   private final Instruction _setStartNode;
/*      */   private final Instruction _reset;
/*      */   private final Instruction _nextNode;
/*      */   private SlotAllocator _slotAllocator;
/*  126 */   private boolean _allocatorInit = false;
/*      */   private LocalVariableRegistry _localVariableRegistry;
/*  134 */   private Hashtable _preCompiled = new Hashtable();
/*      */ 
/* 1825 */   private int m_totalChunks = 0;
/*      */ 
/* 1831 */   private int m_openChunks = 0;
/*      */ 
/*      */   public MethodGenerator(int access_flags, Type return_type, Type[] arg_types, String[] arg_names, String method_name, String class_name, InstructionList il, ConstantPoolGen cpg)
/*      */   {
/*  141 */     super(access_flags, return_type, arg_types, arg_names, method_name, class_name, il, cpg);
/*      */ 
/*  144 */     this._astoreHandler = new ASTORE(3);
/*  145 */     this._aloadHandler = new ALOAD(3);
/*  146 */     this._astoreIterator = new ASTORE(2);
/*  147 */     this._aloadIterator = new ALOAD(2);
/*  148 */     this._aloadDom = new ALOAD(1);
/*  149 */     this._astoreDom = new ASTORE(1);
/*      */ 
/*  151 */     int startElement = cpg.addInterfaceMethodref("com.sun.org.apache.xml.internal.serializer.SerializationHandler", "startElement", "(Ljava/lang/String;)V");
/*      */ 
/*  155 */     this._startElement = new INVOKEINTERFACE(startElement, 2);
/*      */ 
/*  157 */     int endElement = cpg.addInterfaceMethodref("com.sun.org.apache.xml.internal.serializer.SerializationHandler", "endElement", "(Ljava/lang/String;)V");
/*      */ 
/*  161 */     this._endElement = new INVOKEINTERFACE(endElement, 2);
/*      */ 
/*  163 */     int attribute = cpg.addInterfaceMethodref("com.sun.org.apache.xml.internal.serializer.SerializationHandler", "addAttribute", "(Ljava/lang/String;Ljava/lang/String;)V");
/*      */ 
/*  170 */     this._attribute = new INVOKEINTERFACE(attribute, 3);
/*      */ 
/*  172 */     int uniqueAttribute = cpg.addInterfaceMethodref("com.sun.org.apache.xml.internal.serializer.SerializationHandler", "addUniqueAttribute", "(Ljava/lang/String;Ljava/lang/String;I)V");
/*      */ 
/*  179 */     this._uniqueAttribute = new INVOKEINTERFACE(uniqueAttribute, 4);
/*      */ 
/*  181 */     int namespace = cpg.addInterfaceMethodref("com.sun.org.apache.xml.internal.serializer.SerializationHandler", "namespaceAfterStartElement", "(Ljava/lang/String;Ljava/lang/String;)V");
/*      */ 
/*  188 */     this._namespace = new INVOKEINTERFACE(namespace, 3);
/*      */ 
/*  190 */     int index = cpg.addInterfaceMethodref("com.sun.org.apache.xml.internal.serializer.SerializationHandler", "startDocument", "()V");
/*      */ 
/*  193 */     this._startDocument = new INVOKEINTERFACE(index, 1);
/*      */ 
/*  195 */     index = cpg.addInterfaceMethodref("com.sun.org.apache.xml.internal.serializer.SerializationHandler", "endDocument", "()V");
/*      */ 
/*  198 */     this._endDocument = new INVOKEINTERFACE(index, 1);
/*      */ 
/*  201 */     index = cpg.addInterfaceMethodref("com.sun.org.apache.xml.internal.dtm.DTMAxisIterator", "setStartNode", "(I)Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
/*      */ 
/*  204 */     this._setStartNode = new INVOKEINTERFACE(index, 2);
/*      */ 
/*  206 */     index = cpg.addInterfaceMethodref("com.sun.org.apache.xml.internal.dtm.DTMAxisIterator", "reset", "()Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
/*      */ 
/*  208 */     this._reset = new INVOKEINTERFACE(index, 1);
/*      */ 
/*  210 */     index = cpg.addInterfaceMethodref("com.sun.org.apache.xml.internal.dtm.DTMAxisIterator", "next", "()I");
/*  211 */     this._nextNode = new INVOKEINTERFACE(index, 1);
/*      */ 
/*  213 */     this._slotAllocator = new SlotAllocator();
/*  214 */     this._slotAllocator.initialize(getLocalVariableRegistry().getLocals(false));
/*  215 */     this._allocatorInit = true;
/*      */   }
/*      */ 
/*      */   public LocalVariableGen addLocalVariable(String name, Type type, InstructionHandle start, InstructionHandle end)
/*      */   {
/*      */     LocalVariableGen lvg;
/*      */     LocalVariableGen lvg;
/*  230 */     if (this._allocatorInit) {
/*  231 */       lvg = addLocalVariable2(name, type, start);
/*      */     } else {
/*  233 */       lvg = super.addLocalVariable(name, type, start, end);
/*  234 */       getLocalVariableRegistry().registerLocalVariable(lvg);
/*      */     }
/*  236 */     return lvg;
/*      */   }
/*      */ 
/*      */   public LocalVariableGen addLocalVariable2(String name, Type type, InstructionHandle start)
/*      */   {
/*  242 */     LocalVariableGen lvg = super.addLocalVariable(name, type, this._slotAllocator.allocateSlot(type), start, null);
/*      */ 
/*  245 */     getLocalVariableRegistry().registerLocalVariable(lvg);
/*  246 */     return lvg;
/*      */   }
/*      */   private LocalVariableRegistry getLocalVariableRegistry() {
/*  249 */     if (this._localVariableRegistry == null) {
/*  250 */       this._localVariableRegistry = new LocalVariableRegistry();
/*      */     }
/*      */ 
/*  253 */     return this._localVariableRegistry;
/*      */   }
/*      */ 
/*      */   boolean offsetInLocalVariableGenRange(LocalVariableGen lvg, int offset)
/*      */   {
/*  557 */     InstructionHandle lvgStart = lvg.getStart();
/*  558 */     InstructionHandle lvgEnd = lvg.getEnd();
/*      */ 
/*  562 */     if (lvgStart == null) {
/*  563 */       lvgStart = getInstructionList().getStart();
/*      */     }
/*      */ 
/*  568 */     if (lvgEnd == null) {
/*  569 */       lvgEnd = getInstructionList().getEnd();
/*      */     }
/*      */ 
/*  578 */     return (lvgStart.getPosition() <= offset) && (lvgEnd.getPosition() + lvgEnd.getInstruction().getLength() >= offset);
/*      */   }
/*      */ 
/*      */   public void removeLocalVariable(LocalVariableGen lvg)
/*      */   {
/*  584 */     this._slotAllocator.releaseSlot(lvg);
/*  585 */     getLocalVariableRegistry().removeByNameTracking(lvg);
/*  586 */     super.removeLocalVariable(lvg);
/*      */   }
/*      */ 
/*      */   public Instruction loadDOM() {
/*  590 */     return this._aloadDom;
/*      */   }
/*      */ 
/*      */   public Instruction storeDOM() {
/*  594 */     return this._astoreDom;
/*      */   }
/*      */ 
/*      */   public Instruction storeHandler() {
/*  598 */     return this._astoreHandler;
/*      */   }
/*      */ 
/*      */   public Instruction loadHandler() {
/*  602 */     return this._aloadHandler;
/*      */   }
/*      */ 
/*      */   public Instruction storeIterator() {
/*  606 */     return this._astoreIterator;
/*      */   }
/*      */ 
/*      */   public Instruction loadIterator() {
/*  610 */     return this._aloadIterator;
/*      */   }
/*      */ 
/*      */   public final Instruction setStartNode() {
/*  614 */     return this._setStartNode;
/*      */   }
/*      */ 
/*      */   public final Instruction reset() {
/*  618 */     return this._reset;
/*      */   }
/*      */ 
/*      */   public final Instruction nextNode() {
/*  622 */     return this._nextNode;
/*      */   }
/*      */ 
/*      */   public final Instruction startElement() {
/*  626 */     return this._startElement;
/*      */   }
/*      */ 
/*      */   public final Instruction endElement() {
/*  630 */     return this._endElement;
/*      */   }
/*      */ 
/*      */   public final Instruction startDocument() {
/*  634 */     return this._startDocument;
/*      */   }
/*      */ 
/*      */   public final Instruction endDocument() {
/*  638 */     return this._endDocument;
/*      */   }
/*      */ 
/*      */   public final Instruction attribute() {
/*  642 */     return this._attribute;
/*      */   }
/*      */ 
/*      */   public final Instruction uniqueAttribute() {
/*  646 */     return this._uniqueAttribute;
/*      */   }
/*      */ 
/*      */   public final Instruction namespace() {
/*  650 */     return this._namespace;
/*      */   }
/*      */ 
/*      */   public Instruction loadCurrentNode() {
/*  654 */     if (this._iloadCurrent == null) {
/*  655 */       int idx = getLocalIndex("current");
/*  656 */       if (idx > 0)
/*  657 */         this._iloadCurrent = new ILOAD(idx);
/*      */       else
/*  659 */         this._iloadCurrent = new ICONST(0);
/*      */     }
/*  661 */     return this._iloadCurrent;
/*      */   }
/*      */ 
/*      */   public Instruction storeCurrentNode() {
/*  665 */     return this._istoreCurrent = new ISTORE(getLocalIndex("current"));
/*      */   }
/*      */ 
/*      */   public Instruction loadContextNode()
/*      */   {
/*  672 */     return loadCurrentNode();
/*      */   }
/*      */ 
/*      */   public Instruction storeContextNode() {
/*  676 */     return storeCurrentNode();
/*      */   }
/*      */ 
/*      */   public int getLocalIndex(String name) {
/*  680 */     return getLocalVariable(name).getIndex();
/*      */   }
/*      */ 
/*      */   public LocalVariableGen getLocalVariable(String name) {
/*  684 */     return getLocalVariableRegistry().lookUpByName(name);
/*      */   }
/*      */ 
/*      */   public void setMaxLocals()
/*      */   {
/*  690 */     int maxLocals = super.getMaxLocals();
/*  691 */     int prevLocals = maxLocals;
/*      */ 
/*  694 */     LocalVariableGen[] localVars = super.getLocalVariables();
/*  695 */     if ((localVars != null) && 
/*  696 */       (localVars.length > maxLocals)) {
/*  697 */       maxLocals = localVars.length;
/*      */     }
/*      */ 
/*  701 */     if (maxLocals < 5) maxLocals = 5;
/*      */ 
/*  703 */     super.setMaxLocals(maxLocals);
/*      */   }
/*      */ 
/*      */   public void addInstructionList(Pattern pattern, InstructionList ilist)
/*      */   {
/*  710 */     this._preCompiled.put(pattern, ilist);
/*      */   }
/*      */ 
/*      */   public InstructionList getInstructionList(Pattern pattern)
/*      */   {
/*  718 */     return (InstructionList)this._preCompiled.get(pattern);
/*      */   }
/*      */ 
/*      */   private ArrayList getCandidateChunks(ClassGenerator classGen, int totalMethodSize)
/*      */   {
/*  832 */     Iterator instructions = getInstructionList().iterator();
/*  833 */     ArrayList candidateChunks = new ArrayList();
/*  834 */     ArrayList currLevelChunks = new ArrayList();
/*  835 */     Stack subChunkStack = new Stack();
/*  836 */     boolean openChunkAtCurrLevel = false;
/*  837 */     boolean firstInstruction = true;
/*      */ 
/*  841 */     if (this.m_openChunks != 0) {
/*  842 */       String msg = new ErrorMsg("OUTLINE_ERR_UNBALANCED_MARKERS").toString();
/*      */ 
/*  845 */       throw new InternalError(msg);
/*      */     }
/*      */ 
/*      */     InstructionHandle currentHandle;
/*      */     do
/*      */     {
/*  866 */       currentHandle = instructions.hasNext() ? (InstructionHandle)instructions.next() : null;
/*      */ 
/*  869 */       Instruction inst = currentHandle != null ? currentHandle.getInstruction() : null;
/*      */ 
/*  876 */       if (firstInstruction) {
/*  877 */         openChunkAtCurrLevel = true;
/*  878 */         currLevelChunks.add(currentHandle);
/*  879 */         firstInstruction = false;
/*      */       }
/*      */ 
/*  883 */       if ((inst instanceof OutlineableChunkStart))
/*      */       {
/*  888 */         if (openChunkAtCurrLevel) {
/*  889 */           subChunkStack.push(currLevelChunks);
/*  890 */           currLevelChunks = new ArrayList();
/*      */         }
/*      */ 
/*  893 */         openChunkAtCurrLevel = true;
/*  894 */         currLevelChunks.add(currentHandle);
/*      */       }
/*  896 */       else if ((currentHandle == null) || ((inst instanceof OutlineableChunkEnd)))
/*      */       {
/*  898 */         ArrayList nestedSubChunks = null;
/*      */ 
/*  905 */         if (!openChunkAtCurrLevel) {
/*  906 */           nestedSubChunks = currLevelChunks;
/*  907 */           currLevelChunks = (ArrayList)subChunkStack.pop();
/*      */         }
/*      */ 
/*  912 */         InstructionHandle chunkStart = (InstructionHandle)currLevelChunks.get(currLevelChunks.size() - 1);
/*      */ 
/*  916 */         int chunkEndPosition = currentHandle != null ? currentHandle.getPosition() : totalMethodSize;
/*      */ 
/*  919 */         int chunkSize = chunkEndPosition - chunkStart.getPosition();
/*      */ 
/*  933 */         if (chunkSize <= 60000) {
/*  934 */           currLevelChunks.add(currentHandle);
/*      */         } else {
/*  936 */           if (!openChunkAtCurrLevel) {
/*  937 */             int childChunkCount = nestedSubChunks.size() / 2;
/*  938 */             if (childChunkCount > 0) {
/*  939 */               Chunk[] childChunks = new Chunk[childChunkCount];
/*      */ 
/*  942 */               for (int i = 0; i < childChunkCount; i++) {
/*  943 */                 InstructionHandle start = (InstructionHandle)nestedSubChunks.get(i * 2);
/*      */ 
/*  946 */                 InstructionHandle end = (InstructionHandle)nestedSubChunks.get(i * 2 + 1);
/*      */ 
/*  950 */                 childChunks[i] = new Chunk(start, end);
/*      */               }
/*      */ 
/*  954 */               ArrayList mergedChildChunks = mergeAdjacentChunks(childChunks);
/*      */ 
/*  959 */               for (int i = 0; i < mergedChildChunks.size(); i++) {
/*  960 */                 Chunk mergedChunk = (Chunk)mergedChildChunks.get(i);
/*      */ 
/*  962 */                 int mergedSize = mergedChunk.getChunkSize();
/*      */ 
/*  964 */                 if ((mergedSize >= 1000) && (mergedSize <= 60000))
/*      */                 {
/*  966 */                   candidateChunks.add(mergedChunk);
/*      */                 }
/*      */               }
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*  973 */           currLevelChunks.remove(currLevelChunks.size() - 1);
/*      */         }
/*      */ 
/*  979 */         openChunkAtCurrLevel = (currLevelChunks.size() & 0x1) == 1;
/*      */       }
/*      */     }
/*  982 */     while (currentHandle != null);
/*      */ 
/*  984 */     return candidateChunks;
/*      */   }
/*      */ 
/*      */   private ArrayList mergeAdjacentChunks(Chunk[] chunks)
/*      */   {
/*  997 */     int[] adjacencyRunStart = new int[chunks.length];
/*  998 */     int[] adjacencyRunLength = new int[chunks.length];
/*  999 */     boolean[] chunkWasMerged = new boolean[chunks.length];
/*      */ 
/* 1001 */     int maximumRunOfChunks = 0;
/*      */ 
/* 1003 */     int numAdjacentRuns = 0;
/*      */ 
/* 1005 */     ArrayList mergedChunks = new ArrayList();
/*      */ 
/* 1007 */     int startOfCurrentRun = 0;
/*      */ 
/* 1015 */     for (int i = 1; i < chunks.length; i++) {
/* 1016 */       if (!chunks[(i - 1)].isAdjacentTo(chunks[i])) {
/* 1017 */         int lengthOfRun = i - startOfCurrentRun;
/*      */ 
/* 1020 */         if (maximumRunOfChunks < lengthOfRun) {
/* 1021 */           maximumRunOfChunks = lengthOfRun;
/*      */         }
/*      */ 
/* 1024 */         if (lengthOfRun > 1) {
/* 1025 */           adjacencyRunLength[numAdjacentRuns] = lengthOfRun;
/* 1026 */           adjacencyRunStart[numAdjacentRuns] = startOfCurrentRun;
/* 1027 */           numAdjacentRuns++;
/*      */         }
/*      */ 
/* 1030 */         startOfCurrentRun = i;
/*      */       }
/*      */     }
/*      */ 
/* 1034 */     if (chunks.length - startOfCurrentRun > 1) {
/* 1035 */       int lengthOfRun = chunks.length - startOfCurrentRun;
/*      */ 
/* 1038 */       if (maximumRunOfChunks < lengthOfRun) {
/* 1039 */         maximumRunOfChunks = lengthOfRun;
/*      */       }
/*      */ 
/* 1042 */       adjacencyRunLength[numAdjacentRuns] = (chunks.length - startOfCurrentRun);
/*      */ 
/* 1044 */       adjacencyRunStart[numAdjacentRuns] = startOfCurrentRun;
/* 1045 */       numAdjacentRuns++;
/*      */     }
/*      */ 
/* 1059 */     for (int numToMerge = maximumRunOfChunks; numToMerge > 1; numToMerge--)
/*      */     {
/* 1061 */       for (int run = 0; run < numAdjacentRuns; run++) {
/* 1062 */         int runStart = adjacencyRunStart[run];
/* 1063 */         int runEnd = runStart + adjacencyRunLength[run] - 1;
/*      */ 
/* 1065 */         boolean foundChunksToMerge = false;
/*      */ 
/* 1070 */         for (int mergeStart = runStart; 
/* 1071 */           (mergeStart + numToMerge - 1 <= runEnd) && (!foundChunksToMerge); 
/* 1072 */           mergeStart++) {
/* 1073 */           int mergeEnd = mergeStart + numToMerge - 1;
/* 1074 */           int mergeSize = 0;
/*      */ 
/* 1077 */           for (int j = mergeStart; j <= mergeEnd; j++) {
/* 1078 */             mergeSize += chunks[j].getChunkSize();
/*      */           }
/*      */ 
/* 1083 */           if (mergeSize <= 60000) {
/* 1084 */             foundChunksToMerge = true;
/*      */ 
/* 1086 */             for (int j = mergeStart; j <= mergeEnd; j++) {
/* 1087 */               chunkWasMerged[j] = true;
/*      */             }
/*      */ 
/* 1090 */             mergedChunks.add(new Chunk(chunks[mergeStart].getChunkStart(), chunks[mergeEnd].getChunkEnd()));
/*      */ 
/* 1096 */             adjacencyRunStart[run] -= mergeStart;
/*      */ 
/* 1099 */             int trailingRunLength = runEnd - mergeEnd;
/*      */ 
/* 1104 */             if (trailingRunLength >= 2) {
/* 1105 */               adjacencyRunStart[numAdjacentRuns] = (mergeEnd + 1);
/* 1106 */               adjacencyRunLength[numAdjacentRuns] = trailingRunLength;
/*      */ 
/* 1108 */               numAdjacentRuns++;
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1117 */     for (int i = 0; i < chunks.length; i++) {
/* 1118 */       if (chunkWasMerged[i] == 0) {
/* 1119 */         mergedChunks.add(chunks[i]);
/*      */       }
/*      */     }
/*      */ 
/* 1123 */     return mergedChunks;
/*      */   }
/*      */ 
/*      */   public Method[] outlineChunks(ClassGenerator classGen, int originalMethodSize)
/*      */   {
/* 1139 */     ArrayList methodsOutlined = new ArrayList();
/* 1140 */     int currentMethodSize = originalMethodSize;
/*      */ 
/* 1142 */     int outlinedCount = 0;
/*      */ 
/* 1144 */     String originalMethodName = getName();
/*      */ 
/* 1149 */     if (originalMethodName.equals("<init>"))
/* 1150 */       originalMethodName = "$lt$init$gt$";
/* 1151 */     else if (originalMethodName.equals("<clinit>")) {
/* 1152 */       originalMethodName = "$lt$clinit$gt$";
/*      */     }
/*      */ 
/*      */     boolean moreMethodsOutlined;
/*      */     do
/*      */     {
/* 1160 */       ArrayList candidateChunks = getCandidateChunks(classGen, currentMethodSize);
/*      */ 
/* 1162 */       Collections.sort(candidateChunks);
/*      */ 
/* 1164 */       moreMethodsOutlined = false;
/*      */ 
/* 1170 */       for (int i = candidateChunks.size() - 1; 
/* 1171 */         (i >= 0) && (currentMethodSize > 60000); 
/* 1172 */         i--) {
/* 1173 */         Chunk chunkToOutline = (Chunk)candidateChunks.get(i);
/*      */ 
/* 1175 */         methodsOutlined.add(outline(chunkToOutline.getChunkStart(), chunkToOutline.getChunkEnd(), originalMethodName + "$outline$" + outlinedCount, classGen));
/*      */ 
/* 1180 */         outlinedCount++;
/* 1181 */         moreMethodsOutlined = true;
/*      */ 
/* 1183 */         InstructionList il = getInstructionList();
/* 1184 */         InstructionHandle lastInst = il.getEnd();
/* 1185 */         il.setPositions();
/*      */ 
/* 1188 */         currentMethodSize = lastInst.getPosition() + lastInst.getInstruction().getLength();
/*      */       }
/*      */     }
/*      */ 
/* 1192 */     while ((moreMethodsOutlined) && (currentMethodSize > 60000));
/*      */ 
/* 1196 */     if (currentMethodSize > 65535) {
/* 1197 */       String msg = new ErrorMsg("OUTLINE_ERR_METHOD_TOO_BIG").toString();
/*      */ 
/* 1199 */       throw new InternalError(msg);
/*      */     }
/*      */ 
/* 1202 */     Method[] methodsArr = new Method[methodsOutlined.size() + 1];
/* 1203 */     methodsOutlined.toArray(methodsArr);
/*      */ 
/* 1205 */     methodsArr[methodsOutlined.size()] = getThisMethod();
/*      */ 
/* 1207 */     return methodsArr;
/*      */   }
/*      */ 
/*      */   private Method outline(InstructionHandle first, InstructionHandle last, String outlinedMethodName, ClassGenerator classGen)
/*      */   {
/* 1228 */     if (getExceptionHandlers().length != 0) {
/* 1229 */       String msg = new ErrorMsg("OUTLINE_ERR_TRY_CATCH").toString();
/*      */ 
/* 1231 */       throw new InternalError(msg);
/*      */     }
/*      */ 
/* 1234 */     int outlineChunkStartOffset = first.getPosition();
/* 1235 */     int outlineChunkEndOffset = last.getPosition() + last.getInstruction().getLength();
/*      */ 
/* 1238 */     ConstantPoolGen cpg = getConstantPool();
/*      */ 
/* 1253 */     InstructionList newIL = new InstructionList();
/*      */ 
/* 1255 */     XSLTC xsltc = classGen.getParser().getXSLTC();
/* 1256 */     String argTypeName = xsltc.getHelperClassName();
/* 1257 */     Type[] argTypes = { new ObjectType(argTypeName).toJCType() };
/*      */ 
/* 1259 */     String argName = "copyLocals";
/* 1260 */     String[] argNames = { "copyLocals" };
/*      */ 
/* 1262 */     int methodAttributes = 18;
/* 1263 */     boolean isStaticMethod = (getAccessFlags() & 0x8) != 0;
/*      */ 
/* 1265 */     if (isStaticMethod) {
/* 1266 */       methodAttributes |= 8;
/*      */     }
/*      */ 
/* 1269 */     MethodGenerator outlinedMethodGen = new MethodGenerator(methodAttributes, Type.VOID, argTypes, argNames, outlinedMethodName, getClassName(), newIL, cpg);
/*      */ 
/* 1278 */     ClassGenerator copyAreaCG = new ClassGenerator(argTypeName, "java.lang.Object", argTypeName + ".java", 49, null, classGen.getStylesheet())
/*      */     {
/*      */       public boolean isExternal()
/*      */       {
/* 1283 */         return true;
/*      */       }
/*      */     };
/* 1286 */     ConstantPoolGen copyAreaCPG = copyAreaCG.getConstantPool();
/* 1287 */     copyAreaCG.addEmptyConstructor(1);
/*      */ 
/* 1290 */     int copyAreaFieldCount = 0;
/*      */ 
/* 1297 */     InstructionHandle limit = last.getNext();
/*      */ 
/* 1309 */     InstructionList oldMethCopyInIL = new InstructionList();
/* 1310 */     InstructionList oldMethCopyOutIL = new InstructionList();
/* 1311 */     InstructionList newMethCopyInIL = new InstructionList();
/* 1312 */     InstructionList newMethCopyOutIL = new InstructionList();
/*      */ 
/* 1317 */     InstructionHandle outlinedMethodCallSetup = oldMethCopyInIL.append(new NEW(cpg.addClass(argTypeName)));
/*      */ 
/* 1319 */     oldMethCopyInIL.append(InstructionConstants.DUP);
/* 1320 */     oldMethCopyInIL.append(InstructionConstants.DUP);
/* 1321 */     oldMethCopyInIL.append(new INVOKESPECIAL(cpg.addMethodref(argTypeName, "<init>", "()V")));
/*      */     InstructionHandle outlinedMethodRef;
/*      */     InstructionHandle outlinedMethodRef;
/* 1328 */     if (isStaticMethod) {
/* 1329 */       outlinedMethodRef = oldMethCopyOutIL.append(new INVOKESTATIC(cpg.addMethodref(classGen.getClassName(), outlinedMethodName, outlinedMethodGen.getSignature())));
/*      */     }
/*      */     else
/*      */     {
/* 1336 */       oldMethCopyOutIL.append(InstructionConstants.THIS);
/* 1337 */       oldMethCopyOutIL.append(InstructionConstants.SWAP);
/* 1338 */       outlinedMethodRef = oldMethCopyOutIL.append(new INVOKEVIRTUAL(cpg.addMethodref(classGen.getClassName(), outlinedMethodName, outlinedMethodGen.getSignature())));
/*      */     }
/*      */ 
/* 1348 */     boolean chunkStartTargetMappingsPending = false;
/* 1349 */     InstructionHandle pendingTargetMappingHandle = null;
/*      */ 
/* 1352 */     InstructionHandle lastCopyHandle = null;
/*      */ 
/* 1358 */     HashMap targetMap = new HashMap();
/*      */ 
/* 1362 */     HashMap localVarMap = new HashMap();
/*      */ 
/* 1364 */     HashMap revisedLocalVarStart = new HashMap();
/* 1365 */     HashMap revisedLocalVarEnd = new HashMap();
/*      */ 
/* 1377 */     for (InstructionHandle ih = first; ih != limit; ih = ih.getNext()) {
/* 1378 */       Instruction inst = ih.getInstruction();
/*      */ 
/* 1385 */       if ((inst instanceof MarkerInstruction)) {
/* 1386 */         if (ih.hasTargeters()) {
/* 1387 */           if ((inst instanceof OutlineableChunkEnd)) {
/* 1388 */             targetMap.put(ih, lastCopyHandle);
/*      */           }
/* 1390 */           else if (!chunkStartTargetMappingsPending) {
/* 1391 */             chunkStartTargetMappingsPending = true;
/* 1392 */             pendingTargetMappingHandle = ih;
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 1399 */         Instruction c = inst.copy();
/*      */ 
/* 1401 */         if ((c instanceof BranchInstruction))
/* 1402 */           lastCopyHandle = newIL.append((BranchInstruction)c);
/*      */         else {
/* 1404 */           lastCopyHandle = newIL.append(c);
/*      */         }
/*      */ 
/* 1407 */         if (((c instanceof LocalVariableInstruction)) || ((c instanceof RET)))
/*      */         {
/* 1415 */           IndexedInstruction lvi = (IndexedInstruction)c;
/* 1416 */           int oldLocalVarIndex = lvi.getIndex();
/* 1417 */           LocalVariableGen oldLVG = getLocalVariableRegistry().lookupRegisteredLocalVariable(oldLocalVarIndex, ih.getPosition());
/*      */ 
/* 1421 */           LocalVariableGen newLVG = (LocalVariableGen)localVarMap.get(oldLVG);
/*      */ 
/* 1426 */           if (localVarMap.get(oldLVG) == null)
/*      */           {
/* 1438 */             boolean copyInLocalValue = offsetInLocalVariableGenRange(oldLVG, outlineChunkStartOffset != 0 ? outlineChunkStartOffset - 1 : 0);
/*      */ 
/* 1443 */             boolean copyOutLocalValue = offsetInLocalVariableGenRange(oldLVG, outlineChunkEndOffset + 1);
/*      */ 
/* 1451 */             if ((copyInLocalValue) || (copyOutLocalValue)) {
/* 1452 */               String varName = oldLVG.getName();
/* 1453 */               Type varType = oldLVG.getType();
/* 1454 */               newLVG = outlinedMethodGen.addLocalVariable(varName, varType, null, null);
/*      */ 
/* 1458 */               int newLocalVarIndex = newLVG.getIndex();
/* 1459 */               String varSignature = varType.getSignature();
/*      */ 
/* 1462 */               localVarMap.put(oldLVG, newLVG);
/*      */ 
/* 1464 */               copyAreaFieldCount++;
/* 1465 */               String copyAreaFieldName = "field" + copyAreaFieldCount;
/*      */ 
/* 1467 */               copyAreaCG.addField(new Field(1, copyAreaCPG.addUtf8(copyAreaFieldName), copyAreaCPG.addUtf8(varSignature), null, copyAreaCPG.getConstantPool()));
/*      */ 
/* 1473 */               int fieldRef = cpg.addFieldref(argTypeName, copyAreaFieldName, varSignature);
/*      */ 
/* 1477 */               if (copyInLocalValue)
/*      */               {
/* 1482 */                 oldMethCopyInIL.append(InstructionConstants.DUP);
/*      */ 
/* 1484 */                 InstructionHandle copyInLoad = oldMethCopyInIL.append(loadLocal(oldLocalVarIndex, varType));
/*      */ 
/* 1487 */                 oldMethCopyInIL.append(new PUTFIELD(fieldRef));
/*      */ 
/* 1493 */                 if (!copyOutLocalValue) {
/* 1494 */                   revisedLocalVarEnd.put(oldLVG, copyInLoad);
/*      */                 }
/*      */ 
/* 1501 */                 newMethCopyInIL.append(InstructionConstants.ALOAD_1);
/*      */ 
/* 1503 */                 newMethCopyInIL.append(new GETFIELD(fieldRef));
/* 1504 */                 newMethCopyInIL.append(storeLocal(newLocalVarIndex, varType));
/*      */               }
/*      */ 
/* 1508 */               if (copyOutLocalValue)
/*      */               {
/* 1513 */                 newMethCopyOutIL.append(InstructionConstants.ALOAD_1);
/*      */ 
/* 1515 */                 newMethCopyOutIL.append(loadLocal(newLocalVarIndex, varType));
/*      */ 
/* 1517 */                 newMethCopyOutIL.append(new PUTFIELD(fieldRef));
/*      */ 
/* 1523 */                 oldMethCopyOutIL.append(InstructionConstants.DUP);
/*      */ 
/* 1525 */                 oldMethCopyOutIL.append(new GETFIELD(fieldRef));
/* 1526 */                 InstructionHandle copyOutStore = oldMethCopyOutIL.append(storeLocal(oldLocalVarIndex, varType));
/*      */ 
/* 1534 */                 if (!copyInLocalValue) {
/* 1535 */                   revisedLocalVarStart.put(oldLVG, copyOutStore);
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1543 */         if (ih.hasTargeters()) {
/* 1544 */           targetMap.put(ih, lastCopyHandle);
/*      */         }
/*      */ 
/* 1551 */         if (chunkStartTargetMappingsPending) {
/*      */           do {
/* 1553 */             targetMap.put(pendingTargetMappingHandle, lastCopyHandle);
/*      */ 
/* 1555 */             pendingTargetMappingHandle = pendingTargetMappingHandle.getNext();
/*      */           }
/* 1557 */           while (pendingTargetMappingHandle != ih);
/*      */ 
/* 1559 */           chunkStartTargetMappingsPending = false;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1566 */     InstructionHandle ih = first;
/* 1567 */     InstructionHandle ch = newIL.getStart();
/*      */ 
/* 1569 */     while (ch != null)
/*      */     {
/* 1571 */       Instruction i = ih.getInstruction();
/* 1572 */       Instruction c = ch.getInstruction();
/*      */ 
/* 1574 */       if ((i instanceof BranchInstruction)) {
/* 1575 */         BranchInstruction bc = (BranchInstruction)c;
/* 1576 */         BranchInstruction bi = (BranchInstruction)i;
/* 1577 */         InstructionHandle itarget = bi.getTarget();
/*      */ 
/* 1580 */         InstructionHandle newTarget = (InstructionHandle)targetMap.get(itarget);
/*      */ 
/* 1583 */         bc.setTarget(newTarget);
/*      */ 
/* 1587 */         if ((bi instanceof Select)) {
/* 1588 */           InstructionHandle[] itargets = ((Select)bi).getTargets();
/* 1589 */           InstructionHandle[] ctargets = ((Select)bc).getTargets();
/*      */ 
/* 1592 */           for (int j = 0; j < itargets.length; j++) {
/* 1593 */             ctargets[j] = ((InstructionHandle)targetMap.get(itargets[j]));
/*      */           }
/*      */         }
/*      */       }
/* 1597 */       else if (((i instanceof LocalVariableInstruction)) || ((i instanceof RET)))
/*      */       {
/* 1602 */         IndexedInstruction lvi = (IndexedInstruction)c;
/* 1603 */         int oldLocalVarIndex = lvi.getIndex();
/* 1604 */         LocalVariableGen oldLVG = getLocalVariableRegistry().lookupRegisteredLocalVariable(oldLocalVarIndex, ih.getPosition());
/*      */ 
/* 1608 */         LocalVariableGen newLVG = (LocalVariableGen)localVarMap.get(oldLVG);
/*      */         int newLocalVarIndex;
/* 1612 */         if (newLVG == null)
/*      */         {
/* 1617 */           String varName = oldLVG.getName();
/* 1618 */           Type varType = oldLVG.getType();
/* 1619 */           newLVG = outlinedMethodGen.addLocalVariable(varName, varType, null, null);
/*      */ 
/* 1623 */           int newLocalVarIndex = newLVG.getIndex();
/* 1624 */           localVarMap.put(oldLVG, newLVG);
/*      */ 
/* 1631 */           revisedLocalVarStart.put(oldLVG, outlinedMethodRef);
/* 1632 */           revisedLocalVarEnd.put(oldLVG, outlinedMethodRef);
/*      */         } else {
/* 1634 */           newLocalVarIndex = newLVG.getIndex();
/*      */         }
/* 1636 */         lvi.setIndex(newLocalVarIndex);
/*      */       }
/*      */ 
/* 1643 */       if (ih.hasTargeters()) {
/* 1644 */         InstructionTargeter[] targeters = ih.getTargeters();
/*      */ 
/* 1646 */         for (int idx = 0; idx < targeters.length; idx++) {
/* 1647 */           InstructionTargeter targeter = targeters[idx];
/*      */ 
/* 1649 */           if (((targeter instanceof LocalVariableGen)) && (((LocalVariableGen)targeter).getEnd() == ih))
/*      */           {
/* 1651 */             Object newLVG = localVarMap.get(targeter);
/* 1652 */             if (newLVG != null) {
/* 1653 */               outlinedMethodGen.removeLocalVariable((LocalVariableGen)newLVG);
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1663 */       if (!(i instanceof MarkerInstruction)) {
/* 1664 */         ch = ch.getNext();
/*      */       }
/* 1666 */       ih = ih.getNext();
/*      */     }
/*      */ 
/* 1671 */     oldMethCopyOutIL.append(InstructionConstants.POP);
/*      */ 
/* 1675 */     Iterator revisedLocalVarStartPairIter = revisedLocalVarStart.entrySet().iterator();
/*      */ 
/* 1677 */     while (revisedLocalVarStartPairIter.hasNext()) {
/* 1678 */       Map.Entry lvgRangeStartPair = (Map.Entry)revisedLocalVarStartPairIter.next();
/*      */ 
/* 1680 */       LocalVariableGen lvg = (LocalVariableGen)lvgRangeStartPair.getKey();
/* 1681 */       InstructionHandle startInst = (InstructionHandle)lvgRangeStartPair.getValue();
/*      */ 
/* 1684 */       lvg.setStart(startInst);
/*      */     }
/*      */ 
/* 1688 */     Iterator revisedLocalVarEndPairIter = revisedLocalVarEnd.entrySet().iterator();
/*      */ 
/* 1690 */     while (revisedLocalVarEndPairIter.hasNext()) {
/* 1691 */       Map.Entry lvgRangeEndPair = (Map.Entry)revisedLocalVarEndPairIter.next();
/*      */ 
/* 1693 */       LocalVariableGen lvg = (LocalVariableGen)lvgRangeEndPair.getKey();
/* 1694 */       InstructionHandle endInst = (InstructionHandle)lvgRangeEndPair.getValue();
/*      */ 
/* 1697 */       lvg.setEnd(endInst); } 
/*      */ xsltc.dumpClass(copyAreaCG.getJavaClass());
/*      */ 
/* 1704 */     InstructionList oldMethodIL = getInstructionList();
/*      */ 
/* 1706 */     oldMethodIL.insert(first, oldMethCopyInIL);
/* 1707 */     oldMethodIL.insert(first, oldMethCopyOutIL);
/*      */ 
/* 1710 */     newIL.insert(newMethCopyInIL);
/* 1711 */     newIL.append(newMethCopyOutIL);
/* 1712 */     newIL.append(InstructionConstants.RETURN);
/*      */     InstructionHandle[] targets;
/*      */     int i;
/*      */     try { oldMethodIL.delete(first, last);
/*      */     } catch (TargetLostException e) {
/* 1718 */       targets = e.getTargets();
/*      */ 
/* 1726 */       i = 0; } for (; i < targets.length; i++) {
/* 1727 */       InstructionHandle lostTarget = targets[i];
/* 1728 */       InstructionTargeter[] targeters = lostTarget.getTargeters();
/* 1729 */       for (int j = 0; j < targeters.length; j++) {
/* 1730 */         if ((targeters[j] instanceof LocalVariableGen)) {
/* 1731 */           LocalVariableGen lvgTargeter = (LocalVariableGen)targeters[j];
/*      */ 
/* 1737 */           if (lvgTargeter.getStart() == lostTarget) {
/* 1738 */             lvgTargeter.setStart(outlinedMethodRef);
/*      */           }
/* 1740 */           if (lvgTargeter.getEnd() == lostTarget)
/* 1741 */             lvgTargeter.setEnd(outlinedMethodRef);
/*      */         }
/*      */         else {
/* 1744 */           targeters[j].updateTarget(lostTarget, outlinedMethodCallSetup);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1752 */     String[] exceptions = getExceptions();
/* 1753 */     for (int i = 0; i < exceptions.length; i++) {
/* 1754 */       outlinedMethodGen.addException(exceptions[i]);
/*      */     }
/*      */ 
/* 1757 */     return outlinedMethodGen.getThisMethod();
/*      */   }
/*      */ 
/*      */   private static Instruction loadLocal(int index, Type type)
/*      */   {
/* 1770 */     if (type == Type.BOOLEAN)
/* 1771 */       return new ILOAD(index);
/* 1772 */     if (type == Type.INT)
/* 1773 */       return new ILOAD(index);
/* 1774 */     if (type == Type.SHORT)
/* 1775 */       return new ILOAD(index);
/* 1776 */     if (type == Type.LONG)
/* 1777 */       return new LLOAD(index);
/* 1778 */     if (type == Type.BYTE)
/* 1779 */       return new ILOAD(index);
/* 1780 */     if (type == Type.CHAR)
/* 1781 */       return new ILOAD(index);
/* 1782 */     if (type == Type.FLOAT)
/* 1783 */       return new FLOAD(index);
/* 1784 */     if (type == Type.DOUBLE) {
/* 1785 */       return new DLOAD(index);
/*      */     }
/* 1787 */     return new ALOAD(index);
/*      */   }
/*      */ 
/*      */   private static Instruction storeLocal(int index, Type type)
/*      */   {
/* 1801 */     if (type == Type.BOOLEAN)
/* 1802 */       return new ISTORE(index);
/* 1803 */     if (type == Type.INT)
/* 1804 */       return new ISTORE(index);
/* 1805 */     if (type == Type.SHORT)
/* 1806 */       return new ISTORE(index);
/* 1807 */     if (type == Type.LONG)
/* 1808 */       return new LSTORE(index);
/* 1809 */     if (type == Type.BYTE)
/* 1810 */       return new ISTORE(index);
/* 1811 */     if (type == Type.CHAR)
/* 1812 */       return new ISTORE(index);
/* 1813 */     if (type == Type.FLOAT)
/* 1814 */       return new FSTORE(index);
/* 1815 */     if (type == Type.DOUBLE) {
/* 1816 */       return new DSTORE(index);
/*      */     }
/* 1818 */     return new ASTORE(index);
/*      */   }
/*      */ 
/*      */   public void markChunkStart()
/*      */   {
/* 1843 */     getInstructionList().append(OutlineableChunkStart.OUTLINEABLECHUNKSTART);
/*      */ 
/* 1845 */     this.m_totalChunks += 1;
/* 1846 */     this.m_openChunks += 1;
/*      */   }
/*      */ 
/*      */   public void markChunkEnd()
/*      */   {
/* 1855 */     getInstructionList().append(OutlineableChunkEnd.OUTLINEABLECHUNKEND);
/*      */ 
/* 1857 */     this.m_openChunks -= 1;
/* 1858 */     if (this.m_openChunks < 0) {
/* 1859 */       String msg = new ErrorMsg("OUTLINE_ERR_UNBALANCED_MARKERS").toString();
/*      */ 
/* 1861 */       throw new InternalError(msg);
/*      */     }
/*      */   }
/*      */ 
/*      */   Method[] getGeneratedMethods(ClassGenerator classGen)
/*      */   {
/* 1881 */     InstructionList il = getInstructionList();
/* 1882 */     InstructionHandle last = il.getEnd();
/*      */ 
/* 1884 */     il.setPositions();
/*      */ 
/* 1886 */     int instructionListSize = last.getPosition() + last.getInstruction().getLength();
/*      */ 
/* 1891 */     if (instructionListSize > 32767) {
/* 1892 */       boolean ilChanged = widenConditionalBranchTargetOffsets();
/*      */ 
/* 1896 */       if (ilChanged) {
/* 1897 */         il.setPositions();
/* 1898 */         last = il.getEnd();
/* 1899 */         instructionListSize = last.getPosition() + last.getInstruction().getLength();
/*      */       }
/*      */     }
/*      */     Method[] generatedMethods;
/*      */     Method[] generatedMethods;
/* 1904 */     if (instructionListSize > 65535)
/* 1905 */       generatedMethods = outlineChunks(classGen, instructionListSize);
/*      */     else {
/* 1907 */       generatedMethods = new Method[] { getThisMethod() };
/*      */     }
/* 1909 */     return generatedMethods;
/*      */   }
/*      */ 
/*      */   protected Method getThisMethod() {
/* 1913 */     stripAttributes(true);
/* 1914 */     setMaxLocals();
/* 1915 */     setMaxStack();
/* 1916 */     removeNOPs();
/*      */ 
/* 1918 */     return getMethod();
/*      */   }
/*      */ 
/*      */   boolean widenConditionalBranchTargetOffsets()
/*      */   {
/* 1981 */     boolean ilChanged = false;
/* 1982 */     int maxOffsetChange = 0;
/* 1983 */     InstructionList il = getInstructionList();
/*      */ 
/* 1995 */     for (InstructionHandle ih = il.getStart(); 
/* 1996 */       ih != null; 
/* 1997 */       ih = ih.getNext()) {
/* 1998 */       Instruction inst = ih.getInstruction();
/*      */ 
/* 2000 */       switch (inst.getOpcode())
/*      */       {
/*      */       case 167:
/*      */       case 168:
/* 2005 */         maxOffsetChange += 2;
/* 2006 */         break;
/*      */       case 170:
/*      */       case 171:
/* 2014 */         maxOffsetChange += 3;
/* 2015 */         break;
/*      */       case 153:
/*      */       case 154:
/*      */       case 155:
/*      */       case 156:
/*      */       case 157:
/*      */       case 158:
/*      */       case 159:
/*      */       case 160:
/*      */       case 161:
/*      */       case 162:
/*      */       case 163:
/*      */       case 164:
/*      */       case 165:
/*      */       case 166:
/*      */       case 198:
/*      */       case 199:
/* 2035 */         maxOffsetChange += 5;
/*      */       case 169:
/*      */       case 172:
/*      */       case 173:
/*      */       case 174:
/*      */       case 175:
/*      */       case 176:
/*      */       case 177:
/*      */       case 178:
/*      */       case 179:
/*      */       case 180:
/*      */       case 181:
/*      */       case 182:
/*      */       case 183:
/*      */       case 184:
/*      */       case 185:
/*      */       case 186:
/*      */       case 187:
/*      */       case 188:
/*      */       case 189:
/*      */       case 190:
/*      */       case 191:
/*      */       case 192:
/*      */       case 193:
/*      */       case 194:
/*      */       case 195:
/*      */       case 196:
/* 2043 */       case 197: }  } for (InstructionHandle ih = il.getStart(); 
/* 2044 */       ih != null; 
/* 2045 */       ih = ih.getNext()) {
/* 2046 */       Instruction inst = ih.getInstruction();
/*      */ 
/* 2048 */       if ((inst instanceof IfInstruction)) {
/* 2049 */         IfInstruction oldIfInst = (IfInstruction)inst;
/* 2050 */         BranchHandle oldIfHandle = (BranchHandle)ih;
/* 2051 */         InstructionHandle target = oldIfInst.getTarget();
/* 2052 */         int relativeTargetOffset = target.getPosition() - oldIfHandle.getPosition();
/*      */ 
/* 2060 */         if ((relativeTargetOffset - maxOffsetChange < -32768) || (relativeTargetOffset + maxOffsetChange > 32767))
/*      */         {
/* 2067 */           InstructionHandle nextHandle = oldIfHandle.getNext();
/* 2068 */           IfInstruction invertedIfInst = oldIfInst.negate();
/* 2069 */           BranchHandle invertedIfHandle = il.append(oldIfHandle, invertedIfInst);
/*      */ 
/* 2074 */           BranchHandle gotoHandle = il.append(invertedIfHandle, new GOTO(target));
/*      */ 
/* 2080 */           if (nextHandle == null) {
/* 2081 */             nextHandle = il.append(gotoHandle, NOP);
/*      */           }
/*      */ 
/* 2085 */           invertedIfHandle.updateTarget(target, nextHandle);
/*      */ 
/* 2090 */           if (oldIfHandle.hasTargeters()) {
/* 2091 */             InstructionTargeter[] targeters = oldIfHandle.getTargeters();
/*      */ 
/* 2094 */             for (int i = 0; i < targeters.length; i++) {
/* 2095 */               InstructionTargeter targeter = targeters[i];
/*      */ 
/* 2109 */               if ((targeter instanceof LocalVariableGen)) {
/* 2110 */                 LocalVariableGen lvg = (LocalVariableGen)targeter;
/*      */ 
/* 2112 */                 if (lvg.getStart() == oldIfHandle)
/* 2113 */                   lvg.setStart(invertedIfHandle);
/* 2114 */                 else if (lvg.getEnd() == oldIfHandle)
/* 2115 */                   lvg.setEnd(gotoHandle);
/*      */               }
/*      */               else {
/* 2118 */                 targeter.updateTarget(oldIfHandle, invertedIfHandle);
/*      */               }
/*      */             }
/*      */           }
/*      */ 
/*      */           try
/*      */           {
/* 2125 */             il.delete(oldIfHandle);
/*      */           }
/*      */           catch (TargetLostException tle)
/*      */           {
/* 2130 */             String msg = new ErrorMsg("OUTLINE_ERR_DELETED_TARGET", tle.getMessage()).toString();
/*      */ 
/* 2133 */             throw new InternalError(msg);
/*      */           }
/*      */ 
/* 2138 */           ih = gotoHandle;
/*      */ 
/* 2141 */           ilChanged = true;
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2147 */     return ilChanged;
/*      */   }
/*      */ 
/*      */   private class Chunk
/*      */     implements Comparable
/*      */   {
/*      */     private InstructionHandle m_start;
/*      */     private InstructionHandle m_end;
/*      */     private int m_size;
/*      */ 
/*      */     Chunk(InstructionHandle start, InstructionHandle end)
/*      */     {
/*  759 */       this.m_start = start;
/*  760 */       this.m_end = end;
/*  761 */       this.m_size = (end.getPosition() - start.getPosition());
/*      */     }
/*      */ 
/*      */     boolean isAdjacentTo(Chunk neighbour)
/*      */     {
/*  775 */       return getChunkEnd().getNext() == neighbour.getChunkStart();
/*      */     }
/*      */ 
/*      */     InstructionHandle getChunkStart()
/*      */     {
/*  784 */       return this.m_start;
/*      */     }
/*      */ 
/*      */     InstructionHandle getChunkEnd()
/*      */     {
/*  792 */       return this.m_end;
/*      */     }
/*      */ 
/*      */     int getChunkSize()
/*      */     {
/*  801 */       return this.m_size;
/*      */     }
/*      */ 
/*      */     public int compareTo(Object comparand)
/*      */     {
/*  817 */       return getChunkSize() - ((Chunk)comparand).getChunkSize();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class LocalVariableRegistry
/*      */   {
/*  285 */     protected ArrayList _variables = new ArrayList();
/*      */ 
/*  290 */     protected HashMap _nameToLVGMap = new HashMap();
/*      */ 
/*      */     protected LocalVariableRegistry()
/*      */     {
/*      */     }
/*      */ 
/*      */     protected void registerLocalVariable(LocalVariableGen lvg)
/*      */     {
/*  305 */       int slot = lvg.getIndex();
/*      */ 
/*  307 */       int registrySize = this._variables.size();
/*      */ 
/*  312 */       if (slot >= registrySize) {
/*  313 */         for (int i = registrySize; i < slot; i++) {
/*  314 */           this._variables.add(null);
/*      */         }
/*  316 */         this._variables.add(lvg);
/*      */       }
/*      */       else
/*      */       {
/*  323 */         Object localsInSlot = this._variables.get(slot);
/*  324 */         if (localsInSlot != null) {
/*  325 */           if ((localsInSlot instanceof LocalVariableGen)) {
/*  326 */             ArrayList listOfLocalsInSlot = new ArrayList();
/*  327 */             listOfLocalsInSlot.add(localsInSlot);
/*  328 */             listOfLocalsInSlot.add(lvg);
/*  329 */             this._variables.set(slot, listOfLocalsInSlot);
/*      */           } else {
/*  331 */             ((ArrayList)localsInSlot).add(lvg);
/*      */           }
/*      */         }
/*  334 */         else this._variables.set(slot, lvg);
/*      */ 
/*      */       }
/*      */ 
/*  338 */       registerByName(lvg);
/*      */     }
/*      */ 
/*      */     protected LocalVariableGen lookupRegisteredLocalVariable(int slot, int offset)
/*      */     {
/*  359 */       Object localsInSlot = this._variables != null ? this._variables.get(slot) : null;
/*      */ 
/*  368 */       if (localsInSlot != null) {
/*  369 */         if ((localsInSlot instanceof LocalVariableGen)) {
/*  370 */           LocalVariableGen lvg = (LocalVariableGen)localsInSlot;
/*  371 */           if (MethodGenerator.this.offsetInLocalVariableGenRange(lvg, offset))
/*  372 */             return lvg;
/*      */         }
/*      */         else {
/*  375 */           ArrayList listOfLocalsInSlot = (ArrayList)localsInSlot;
/*  376 */           int size = listOfLocalsInSlot.size();
/*      */ 
/*  378 */           for (int i = 0; i < size; i++) {
/*  379 */             LocalVariableGen lvg = (LocalVariableGen)listOfLocalsInSlot.get(i);
/*      */ 
/*  381 */             if (MethodGenerator.this.offsetInLocalVariableGenRange(lvg, offset)) {
/*  382 */               return lvg;
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  389 */       return null;
/*      */     }
/*      */ 
/*      */     protected void registerByName(LocalVariableGen lvg)
/*      */     {
/*  410 */       Object duplicateNameEntry = this._nameToLVGMap.get(lvg.getName());
/*      */ 
/*  412 */       if (duplicateNameEntry == null) {
/*  413 */         this._nameToLVGMap.put(lvg.getName(), lvg);
/*      */       }
/*      */       else
/*      */       {
/*      */         ArrayList sameNameList;
/*  417 */         if ((duplicateNameEntry instanceof ArrayList)) {
/*  418 */           ArrayList sameNameList = (ArrayList)duplicateNameEntry;
/*  419 */           sameNameList.add(lvg);
/*      */         } else {
/*  421 */           sameNameList = new ArrayList();
/*  422 */           sameNameList.add(duplicateNameEntry);
/*  423 */           sameNameList.add(lvg);
/*      */         }
/*      */ 
/*  426 */         this._nameToLVGMap.put(lvg.getName(), sameNameList);
/*      */       }
/*      */     }
/*      */ 
/*      */     protected void removeByNameTracking(LocalVariableGen lvg)
/*      */     {
/*  438 */       Object duplicateNameEntry = this._nameToLVGMap.get(lvg.getName());
/*      */ 
/*  440 */       if ((duplicateNameEntry instanceof ArrayList)) {
/*  441 */         ArrayList sameNameList = (ArrayList)duplicateNameEntry;
/*  442 */         for (int i = 0; i < sameNameList.size(); i++)
/*  443 */           if (sameNameList.get(i) == lvg) {
/*  444 */             sameNameList.remove(i);
/*  445 */             break;
/*      */           }
/*      */       }
/*      */       else {
/*  449 */         this._nameToLVGMap.remove(lvg);
/*      */       }
/*      */     }
/*      */ 
/*      */     protected LocalVariableGen lookUpByName(String name)
/*      */     {
/*  462 */       LocalVariableGen lvg = null;
/*  463 */       Object duplicateNameEntry = this._nameToLVGMap.get(name);
/*      */ 
/*  465 */       if ((duplicateNameEntry instanceof ArrayList)) {
/*  466 */         ArrayList sameNameList = (ArrayList)duplicateNameEntry;
/*      */ 
/*  468 */         for (int i = 0; i < sameNameList.size(); i++) {
/*  469 */           lvg = (LocalVariableGen)sameNameList.get(i);
/*  470 */           if (lvg.getName() == name)
/*      */             break;
/*      */         }
/*      */       }
/*      */       else {
/*  475 */         lvg = (LocalVariableGen)duplicateNameEntry;
/*      */       }
/*      */ 
/*  478 */       return lvg;
/*      */     }
/*      */ 
/*      */     protected LocalVariableGen[] getLocals(boolean includeRemoved)
/*      */     {
/*  496 */       LocalVariableGen[] locals = null;
/*  497 */       ArrayList allVarsEverDeclared = new ArrayList();
/*      */ 
/*  499 */       if (includeRemoved) {
/*  500 */         int slotCount = allVarsEverDeclared.size();
/*      */ 
/*  502 */         for (int i = 0; i < slotCount; i++) {
/*  503 */           Object slotEntries = this._variables.get(i);
/*  504 */           if (slotEntries != null)
/*  505 */             if ((slotEntries instanceof ArrayList)) {
/*  506 */               ArrayList slotList = (ArrayList)slotEntries;
/*      */ 
/*  508 */               for (int j = 0; j < slotList.size(); j++)
/*  509 */                 allVarsEverDeclared.add(slotList.get(i));
/*      */             }
/*      */             else {
/*  512 */               allVarsEverDeclared.add(slotEntries);
/*      */             }
/*      */         }
/*      */       }
/*      */       else {
/*  517 */         Iterator nameVarsPairsIter = this._nameToLVGMap.entrySet().iterator();
/*      */ 
/*  519 */         while (nameVarsPairsIter.hasNext()) {
/*  520 */           Map.Entry nameVarsPair = (Map.Entry)nameVarsPairsIter.next();
/*      */ 
/*  522 */           Object vars = nameVarsPair.getValue();
/*  523 */           if (vars != null) {
/*  524 */             if ((vars instanceof ArrayList)) {
/*  525 */               ArrayList varsList = (ArrayList)vars;
/*  526 */               for (int i = 0; i < varsList.size(); i++)
/*  527 */                 allVarsEverDeclared.add(varsList.get(i));
/*      */             }
/*      */             else {
/*  530 */               allVarsEverDeclared.add(vars);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*  536 */       locals = new LocalVariableGen[allVarsEverDeclared.size()];
/*  537 */       allVarsEverDeclared.toArray(locals);
/*      */ 
/*  539 */       return locals;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator
 * JD-Core Version:    0.6.2
 */