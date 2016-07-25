/*      */ package com.sun.org.apache.xerces.internal.impl.xs.models;
/*      */ 
/*      */ import com.sun.org.apache.xerces.internal.impl.dtd.models.CMNode;
/*      */ import com.sun.org.apache.xerces.internal.impl.dtd.models.CMStateSet;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.SubstitutionGroupHandler;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaException;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.XSConstraints;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.XSElementDecl;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.XSWildcardDecl;
/*      */ import com.sun.org.apache.xerces.internal.xni.QName;
/*      */ import java.io.PrintStream;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.Vector;
/*      */ 
/*      */ public class XSDFACM
/*      */   implements XSCMValidator
/*      */ {
/*      */   private static final boolean DEBUG = false;
/*      */   private static final boolean DEBUG_VALIDATE_CONTENT = false;
/*   76 */   private Object[] fElemMap = null;
/*      */ 
/*   82 */   private int[] fElemMapType = null;
/*      */ 
/*   87 */   private int[] fElemMapId = null;
/*      */ 
/*   90 */   private int fElemMapSize = 0;
/*      */ 
/*   97 */   private boolean[] fFinalStateFlags = null;
/*      */ 
/*  104 */   private CMStateSet[] fFollowList = null;
/*      */ 
/*  112 */   private CMNode fHeadNode = null;
/*      */ 
/*  118 */   private int fLeafCount = 0;
/*      */ 
/*  124 */   private XSCMLeaf[] fLeafList = null;
/*      */ 
/*  127 */   private int[] fLeafListType = null;
/*      */ 
/*  141 */   private int[][] fTransTable = (int[][])null;
/*      */ 
/*  146 */   private Occurence[] fCountingStates = null;
/*      */ 
/*  168 */   private int fTransTableSize = 0;
/*      */   private int[] fElemMapCounter;
/*      */   private int[] fElemMapCounterLowerBound;
/*      */   private int[] fElemMapCounterUpperBound;
/*  244 */   private static long time = 0L;
/*      */ 
/*      */   public XSDFACM(CMNode syntaxTree, int leafCount)
/*      */   {
/*  214 */     this.fLeafCount = leafCount;
/*      */ 
/*  236 */     buildDFA(syntaxTree);
/*      */   }
/*      */ 
/*      */   public boolean isFinalState(int state)
/*      */   {
/*  258 */     return state < 0 ? 0 : this.fFinalStateFlags[state];
/*      */   }
/*      */ 
/*      */   public Object oneTransition(QName curElem, int[] state, SubstitutionGroupHandler subGroupHandler)
/*      */   {
/*  277 */     int curState = state[0];
/*      */ 
/*  279 */     if ((curState == -1) || (curState == -2))
/*      */     {
/*  282 */       if (curState == -1) {
/*  283 */         state[0] = -2;
/*      */       }
/*  285 */       return findMatchingDecl(curElem, subGroupHandler);
/*      */     }
/*      */ 
/*  288 */     int nextState = 0;
/*  289 */     int elemIndex = 0;
/*  290 */     Object matchingDecl = null;
/*      */ 
/*  292 */     for (; elemIndex < this.fElemMapSize; elemIndex++) {
/*  293 */       nextState = this.fTransTable[curState][elemIndex];
/*  294 */       if (nextState != -1)
/*      */       {
/*  296 */         int type = this.fElemMapType[elemIndex];
/*  297 */         if (type == 1) {
/*  298 */           matchingDecl = subGroupHandler.getMatchingElemDecl(curElem, (XSElementDecl)this.fElemMap[elemIndex]);
/*  299 */           if (matchingDecl != null)
/*      */           {
/*  301 */             if (this.fElemMapCounter[elemIndex] < 0) break;
/*  302 */             this.fElemMapCounter[elemIndex] += 1; break;
/*      */           }
/*      */ 
/*      */         }
/*  307 */         else if ((type == 2) && 
/*  308 */           (((XSWildcardDecl)this.fElemMap[elemIndex]).allowNamespace(curElem.uri))) {
/*  309 */           matchingDecl = this.fElemMap[elemIndex];
/*      */ 
/*  311 */           if (this.fElemMapCounter[elemIndex] < 0) break;
/*  312 */           this.fElemMapCounter[elemIndex] += 1; break;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  321 */     if (elemIndex == this.fElemMapSize) {
/*  322 */       state[1] = state[0];
/*  323 */       state[0] = -1;
/*  324 */       return findMatchingDecl(curElem, subGroupHandler);
/*      */     }
/*      */ 
/*  327 */     if (this.fCountingStates != null) {
/*  328 */       Occurence o = this.fCountingStates[curState];
/*  329 */       if (o != null) {
/*  330 */         if (curState == nextState) {
/*  331 */           if ((state[2] += 1 > o.maxOccurs) && (o.maxOccurs != -1))
/*      */           {
/*  357 */             return findMatchingDecl(curElem, state, subGroupHandler, elemIndex);
/*      */           }
/*      */         } else {
/*  360 */           if (state[2] < o.minOccurs)
/*      */           {
/*  362 */             state[1] = state[0];
/*  363 */             state[0] = -1;
/*  364 */             return findMatchingDecl(curElem, subGroupHandler);
/*      */           }
/*      */ 
/*  369 */           o = this.fCountingStates[nextState];
/*  370 */           if (o != null)
/*  371 */             state[2] = (elemIndex == o.elemIndex ? 1 : 0);
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*  376 */         o = this.fCountingStates[nextState];
/*  377 */         if (o != null)
/*      */         {
/*  382 */           state[2] = (elemIndex == o.elemIndex ? 1 : 0);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  387 */     state[0] = nextState;
/*  388 */     return matchingDecl;
/*      */   }
/*      */ 
/*      */   Object findMatchingDecl(QName curElem, SubstitutionGroupHandler subGroupHandler) {
/*  392 */     Object matchingDecl = null;
/*      */ 
/*  394 */     for (int elemIndex = 0; elemIndex < this.fElemMapSize; elemIndex++) {
/*  395 */       int type = this.fElemMapType[elemIndex];
/*  396 */       if (type == 1) {
/*  397 */         matchingDecl = subGroupHandler.getMatchingElemDecl(curElem, (XSElementDecl)this.fElemMap[elemIndex]);
/*  398 */         if (matchingDecl != null) {
/*  399 */           return matchingDecl;
/*      */         }
/*      */       }
/*  402 */       else if ((type == 2) && 
/*  403 */         (((XSWildcardDecl)this.fElemMap[elemIndex]).allowNamespace(curElem.uri))) {
/*  404 */         return this.fElemMap[elemIndex];
/*      */       }
/*      */     }
/*      */ 
/*  408 */     return null;
/*      */   }
/*      */ 
/*      */   Object findMatchingDecl(QName curElem, int[] state, SubstitutionGroupHandler subGroupHandler, int elemIndex)
/*      */   {
/*  413 */     int curState = state[0];
/*  414 */     int nextState = 0;
/*  415 */     Object matchingDecl = null;
/*      */     while (true) {
/*  417 */       elemIndex++; if (elemIndex >= this.fElemMapSize) break;
/*  418 */       nextState = this.fTransTable[curState][elemIndex];
/*  419 */       if (nextState != -1)
/*      */       {
/*  421 */         int type = this.fElemMapType[elemIndex];
/*  422 */         if (type == 1) {
/*  423 */           matchingDecl = subGroupHandler.getMatchingElemDecl(curElem, (XSElementDecl)this.fElemMap[elemIndex]);
/*  424 */           if (matchingDecl != null) {
/*  425 */             break;
/*      */           }
/*      */         }
/*  428 */         else if ((type == 2) && 
/*  429 */           (((XSWildcardDecl)this.fElemMap[elemIndex]).allowNamespace(curElem.uri))) {
/*  430 */           matchingDecl = this.fElemMap[elemIndex];
/*  431 */           break;
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  437 */     if (elemIndex == this.fElemMapSize) {
/*  438 */       state[1] = state[0];
/*  439 */       state[0] = -1;
/*  440 */       return findMatchingDecl(curElem, subGroupHandler);
/*      */     }
/*      */ 
/*  445 */     state[0] = nextState;
/*  446 */     Occurence o = this.fCountingStates[nextState];
/*  447 */     if (o != null) {
/*  448 */       state[2] = (elemIndex == o.elemIndex ? 1 : 0);
/*      */     }
/*  450 */     return matchingDecl;
/*      */   }
/*      */ 
/*      */   public int[] startContentModel()
/*      */   {
/*  456 */     for (int elemIndex = 0; elemIndex < this.fElemMapSize; elemIndex++) {
/*  457 */       if (this.fElemMapCounter[elemIndex] != -1) {
/*  458 */         this.fElemMapCounter[elemIndex] = 0;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  465 */     return new int[3];
/*      */   }
/*      */ 
/*      */   public boolean endContentModel(int[] state)
/*      */   {
/*  470 */     int curState = state[0];
/*  471 */     if (this.fFinalStateFlags[curState] != 0) {
/*  472 */       if (this.fCountingStates != null) {
/*  473 */         Occurence o = this.fCountingStates[curState];
/*  474 */         if ((o != null) && (state[2] < o.minOccurs))
/*      */         {
/*  476 */           return false;
/*      */         }
/*      */       }
/*  479 */       return true;
/*      */     }
/*  481 */     return false;
/*      */   }
/*      */ 
/*      */   private void buildDFA(CMNode syntaxTree)
/*      */   {
/*  547 */     int EOCPos = this.fLeafCount;
/*  548 */     XSCMLeaf nodeEOC = new XSCMLeaf(1, null, -1, this.fLeafCount++);
/*  549 */     this.fHeadNode = new XSCMBinOp(102, syntaxTree, nodeEOC);
/*      */ 
/*  569 */     this.fLeafList = new XSCMLeaf[this.fLeafCount];
/*  570 */     this.fLeafListType = new int[this.fLeafCount];
/*  571 */     postTreeBuildInit(this.fHeadNode);
/*      */ 
/*  578 */     this.fFollowList = new CMStateSet[this.fLeafCount];
/*  579 */     for (int index = 0; index < this.fLeafCount; index++)
/*  580 */       this.fFollowList[index] = new CMStateSet(this.fLeafCount);
/*  581 */     calcFollowList(this.fHeadNode);
/*      */ 
/*  593 */     this.fElemMap = new Object[this.fLeafCount];
/*  594 */     this.fElemMapType = new int[this.fLeafCount];
/*  595 */     this.fElemMapId = new int[this.fLeafCount];
/*      */ 
/*  597 */     this.fElemMapCounter = new int[this.fLeafCount];
/*  598 */     this.fElemMapCounterLowerBound = new int[this.fLeafCount];
/*  599 */     this.fElemMapCounterUpperBound = new int[this.fLeafCount];
/*      */ 
/*  601 */     this.fElemMapSize = 0;
/*  602 */     Occurence[] elemOccurenceMap = null;
/*      */ 
/*  604 */     for (int outIndex = 0; outIndex < this.fLeafCount; outIndex++)
/*      */     {
/*  607 */       this.fElemMap[outIndex] = null;
/*      */ 
/*  609 */       int inIndex = 0;
/*  610 */       int id = this.fLeafList[outIndex].getParticleId();
/*  611 */       while ((inIndex < this.fElemMapSize) && 
/*  612 */         (id != this.fElemMapId[inIndex])) {
/*  611 */         inIndex++;
/*      */       }
/*      */ 
/*  617 */       if (inIndex == this.fElemMapSize) {
/*  618 */         XSCMLeaf leaf = this.fLeafList[outIndex];
/*  619 */         this.fElemMap[this.fElemMapSize] = leaf.getLeaf();
/*  620 */         if ((leaf instanceof XSCMRepeatingLeaf)) {
/*  621 */           if (elemOccurenceMap == null) {
/*  622 */             elemOccurenceMap = new Occurence[this.fLeafCount];
/*      */           }
/*  624 */           elemOccurenceMap[this.fElemMapSize] = new Occurence((XSCMRepeatingLeaf)leaf, this.fElemMapSize);
/*      */         }
/*      */ 
/*  627 */         this.fElemMapType[this.fElemMapSize] = this.fLeafListType[outIndex];
/*  628 */         this.fElemMapId[this.fElemMapSize] = id;
/*      */ 
/*  631 */         int[] bounds = (int[])leaf.getUserData();
/*  632 */         if (bounds != null) {
/*  633 */           this.fElemMapCounter[this.fElemMapSize] = 0;
/*  634 */           this.fElemMapCounterLowerBound[this.fElemMapSize] = bounds[0];
/*  635 */           this.fElemMapCounterUpperBound[this.fElemMapSize] = bounds[1];
/*      */         } else {
/*  637 */           this.fElemMapCounter[this.fElemMapSize] = -1;
/*  638 */           this.fElemMapCounterLowerBound[this.fElemMapSize] = -1;
/*  639 */           this.fElemMapCounterUpperBound[this.fElemMapSize] = -1;
/*      */         }
/*      */ 
/*  642 */         this.fElemMapSize += 1;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  652 */     this.fElemMapSize -= 1;
/*      */ 
/*  660 */     int[] fLeafSorter = new int[this.fLeafCount + this.fElemMapSize];
/*  661 */     int fSortCount = 0;
/*      */ 
/*  663 */     for (int elemIndex = 0; elemIndex < this.fElemMapSize; elemIndex++) {
/*  664 */       int id = this.fElemMapId[elemIndex];
/*  665 */       for (int leafIndex = 0; leafIndex < this.fLeafCount; leafIndex++) {
/*  666 */         if (id == this.fLeafList[leafIndex].getParticleId())
/*  667 */           fLeafSorter[(fSortCount++)] = leafIndex;
/*      */       }
/*  669 */       fLeafSorter[(fSortCount++)] = -1;
/*      */     }
/*      */ 
/*  687 */     int curArraySize = this.fLeafCount * 4;
/*  688 */     CMStateSet[] statesToDo = new CMStateSet[curArraySize];
/*  689 */     this.fFinalStateFlags = new boolean[curArraySize];
/*  690 */     this.fTransTable = new int[curArraySize][];
/*      */ 
/*  697 */     CMStateSet setT = this.fHeadNode.firstPos();
/*      */ 
/*  707 */     int unmarkedState = 0;
/*  708 */     int curState = 0;
/*      */ 
/*  714 */     this.fTransTable[curState] = makeDefStateList();
/*  715 */     statesToDo[curState] = setT;
/*  716 */     curState++;
/*      */ 
/*  722 */     HashMap stateTable = new HashMap();
/*      */ 
/*  731 */     while (unmarkedState < curState)
/*      */     {
/*  736 */       setT = statesToDo[unmarkedState];
/*  737 */       int[] transEntry = this.fTransTable[unmarkedState];
/*      */ 
/*  740 */       this.fFinalStateFlags[unmarkedState] = setT.getBit(EOCPos);
/*      */ 
/*  743 */       unmarkedState++;
/*      */ 
/*  746 */       CMStateSet newSet = null;
/*      */ 
/*  748 */       int sorterIndex = 0;
/*      */ 
/*  750 */       for (int elemIndex = 0; elemIndex < this.fElemMapSize; elemIndex++)
/*      */       {
/*  757 */         if (newSet == null)
/*  758 */           newSet = new CMStateSet(this.fLeafCount);
/*      */         else {
/*  760 */           newSet.zeroBits();
/*      */         }
/*      */ 
/*  763 */         int leafIndex = fLeafSorter[(sorterIndex++)];
/*      */ 
/*  765 */         while (leafIndex != -1)
/*      */         {
/*  767 */           if (setT.getBit(leafIndex))
/*      */           {
/*  773 */             newSet.union(this.fFollowList[leafIndex]);
/*      */           }
/*      */ 
/*  776 */           leafIndex = fLeafSorter[(sorterIndex++)];
/*      */         }
/*      */ 
/*  784 */         if (!newSet.isEmpty())
/*      */         {
/*  791 */           Integer stateObj = (Integer)stateTable.get(newSet);
/*  792 */           int stateIndex = stateObj == null ? curState : stateObj.intValue();
/*      */ 
/*  796 */           if (stateIndex == curState)
/*      */           {
/*  802 */             statesToDo[curState] = newSet;
/*  803 */             this.fTransTable[curState] = makeDefStateList();
/*      */ 
/*  806 */             stateTable.put(newSet, new Integer(curState));
/*      */ 
/*  810 */             curState++;
/*      */ 
/*  817 */             newSet = null;
/*      */           }
/*      */ 
/*  826 */           transEntry[elemIndex] = stateIndex;
/*      */ 
/*  829 */           if (curState == curArraySize)
/*      */           {
/*  835 */             int newSize = (int)(curArraySize * 1.5D);
/*  836 */             CMStateSet[] newToDo = new CMStateSet[newSize];
/*  837 */             boolean[] newFinalFlags = new boolean[newSize];
/*  838 */             int[][] newTransTable = new int[newSize][];
/*      */ 
/*  841 */             System.arraycopy(statesToDo, 0, newToDo, 0, curArraySize);
/*  842 */             System.arraycopy(this.fFinalStateFlags, 0, newFinalFlags, 0, curArraySize);
/*  843 */             System.arraycopy(this.fTransTable, 0, newTransTable, 0, curArraySize);
/*      */ 
/*  846 */             curArraySize = newSize;
/*  847 */             statesToDo = newToDo;
/*  848 */             this.fFinalStateFlags = newFinalFlags;
/*  849 */             this.fTransTable = newTransTable;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  859 */     if (elemOccurenceMap != null) {
/*  860 */       this.fCountingStates = new Occurence[curState];
/*  861 */       for (int i = 0; i < curState; i++) {
/*  862 */         int[] transitions = this.fTransTable[i];
/*  863 */         for (int j = 0; j < transitions.length; j++) {
/*  864 */           if (i == transitions[j]) {
/*  865 */             this.fCountingStates[i] = elemOccurenceMap[j];
/*  866 */             break;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  878 */     this.fHeadNode = null;
/*  879 */     this.fLeafList = null;
/*  880 */     this.fFollowList = null;
/*  881 */     this.fLeafListType = null;
/*  882 */     this.fElemMapId = null;
/*      */   }
/*      */ 
/*      */   private void calcFollowList(CMNode nodeCur)
/*      */   {
/*  894 */     if (nodeCur.type() == 101)
/*      */     {
/*  896 */       calcFollowList(((XSCMBinOp)nodeCur).getLeft());
/*  897 */       calcFollowList(((XSCMBinOp)nodeCur).getRight());
/*      */     }
/*  899 */     else if (nodeCur.type() == 102)
/*      */     {
/*  901 */       calcFollowList(((XSCMBinOp)nodeCur).getLeft());
/*  902 */       calcFollowList(((XSCMBinOp)nodeCur).getRight());
/*      */ 
/*  909 */       CMStateSet last = ((XSCMBinOp)nodeCur).getLeft().lastPos();
/*  910 */       CMStateSet first = ((XSCMBinOp)nodeCur).getRight().firstPos();
/*      */ 
/*  917 */       for (int index = 0; index < this.fLeafCount; index++) {
/*  918 */         if (last.getBit(index))
/*  919 */           this.fFollowList[index].union(first);
/*      */       }
/*      */     }
/*  922 */     else if ((nodeCur.type() == 4) || (nodeCur.type() == 6))
/*      */     {
/*  925 */       calcFollowList(((XSCMUniOp)nodeCur).getChild());
/*      */ 
/*  931 */       CMStateSet first = nodeCur.firstPos();
/*  932 */       CMStateSet last = nodeCur.lastPos();
/*      */ 
/*  939 */       for (int index = 0; index < this.fLeafCount; index++) {
/*  940 */         if (last.getBit(index)) {
/*  941 */           this.fFollowList[index].union(first);
/*      */         }
/*      */       }
/*      */     }
/*  945 */     else if (nodeCur.type() == 5)
/*      */     {
/*  947 */       calcFollowList(((XSCMUniOp)nodeCur).getChild());
/*      */     }
/*      */   }
/*      */ 
/*      */   private void dumpTree(CMNode nodeCur, int level)
/*      */   {
/*  961 */     for (int index = 0; index < level; index++) {
/*  962 */       System.out.print("   ");
/*      */     }
/*  964 */     int type = nodeCur.type();
/*      */ 
/*  966 */     switch (type)
/*      */     {
/*      */     case 101:
/*      */     case 102:
/*  970 */       if (type == 101)
/*  971 */         System.out.print("Choice Node ");
/*      */       else {
/*  973 */         System.out.print("Seq Node ");
/*      */       }
/*  975 */       if (nodeCur.isNullable()) {
/*  976 */         System.out.print("Nullable ");
/*      */       }
/*  978 */       System.out.print("firstPos=");
/*  979 */       System.out.print(nodeCur.firstPos().toString());
/*  980 */       System.out.print(" lastPos=");
/*  981 */       System.out.println(nodeCur.lastPos().toString());
/*      */ 
/*  983 */       dumpTree(((XSCMBinOp)nodeCur).getLeft(), level + 1);
/*  984 */       dumpTree(((XSCMBinOp)nodeCur).getRight(), level + 1);
/*  985 */       break;
/*      */     case 4:
/*      */     case 5:
/*      */     case 6:
/*  990 */       System.out.print("Rep Node ");
/*      */ 
/*  992 */       if (nodeCur.isNullable()) {
/*  993 */         System.out.print("Nullable ");
/*      */       }
/*  995 */       System.out.print("firstPos=");
/*  996 */       System.out.print(nodeCur.firstPos().toString());
/*  997 */       System.out.print(" lastPos=");
/*  998 */       System.out.println(nodeCur.lastPos().toString());
/*      */ 
/* 1000 */       dumpTree(((XSCMUniOp)nodeCur).getChild(), level + 1);
/* 1001 */       break;
/*      */     case 1:
/* 1004 */       System.out.print("Leaf: (pos=" + ((XSCMLeaf)nodeCur).getPosition() + "), " + "(elemIndex=" + ((XSCMLeaf)nodeCur).getLeaf() + ") ");
/*      */ 
/* 1014 */       if (nodeCur.isNullable()) {
/* 1015 */         System.out.print(" Nullable ");
/*      */       }
/* 1017 */       System.out.print("firstPos=");
/* 1018 */       System.out.print(nodeCur.firstPos().toString());
/* 1019 */       System.out.print(" lastPos=");
/* 1020 */       System.out.println(nodeCur.lastPos().toString());
/* 1021 */       break;
/*      */     case 2:
/* 1024 */       System.out.print("Any Node: ");
/*      */ 
/* 1026 */       System.out.print("firstPos=");
/* 1027 */       System.out.print(nodeCur.firstPos().toString());
/* 1028 */       System.out.print(" lastPos=");
/* 1029 */       System.out.println(nodeCur.lastPos().toString());
/* 1030 */       break;
/*      */     default:
/* 1032 */       throw new RuntimeException("ImplementationMessages.VAL_NIICM");
/*      */     }
/*      */   }
/*      */ 
/*      */   private int[] makeDefStateList()
/*      */   {
/* 1046 */     int[] retArray = new int[this.fElemMapSize];
/* 1047 */     for (int index = 0; index < this.fElemMapSize; index++)
/* 1048 */       retArray[index] = -1;
/* 1049 */     return retArray;
/*      */   }
/*      */ 
/*      */   private void postTreeBuildInit(CMNode nodeCur)
/*      */     throws RuntimeException
/*      */   {
/* 1055 */     nodeCur.setMaxStates(this.fLeafCount);
/*      */ 
/* 1057 */     XSCMLeaf leaf = null;
/* 1058 */     int pos = 0;
/*      */ 
/* 1060 */     if (nodeCur.type() == 2) {
/* 1061 */       leaf = (XSCMLeaf)nodeCur;
/* 1062 */       pos = leaf.getPosition();
/* 1063 */       this.fLeafList[pos] = leaf;
/* 1064 */       this.fLeafListType[pos] = 2;
/*      */     }
/* 1066 */     else if ((nodeCur.type() == 101) || (nodeCur.type() == 102))
/*      */     {
/* 1068 */       postTreeBuildInit(((XSCMBinOp)nodeCur).getLeft());
/* 1069 */       postTreeBuildInit(((XSCMBinOp)nodeCur).getRight());
/*      */     }
/* 1071 */     else if ((nodeCur.type() == 4) || (nodeCur.type() == 6) || (nodeCur.type() == 5))
/*      */     {
/* 1074 */       postTreeBuildInit(((XSCMUniOp)nodeCur).getChild());
/*      */     }
/* 1076 */     else if (nodeCur.type() == 1)
/*      */     {
/* 1079 */       leaf = (XSCMLeaf)nodeCur;
/* 1080 */       pos = leaf.getPosition();
/* 1081 */       this.fLeafList[pos] = leaf;
/* 1082 */       this.fLeafListType[pos] = 1;
/*      */     }
/*      */     else {
/* 1085 */       throw new RuntimeException("ImplementationMessages.VAL_NIICM");
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean checkUniqueParticleAttribution(SubstitutionGroupHandler subGroupHandler)
/*      */     throws XMLSchemaException
/*      */   {
/* 1100 */     byte[][] conflictTable = new byte[this.fElemMapSize][this.fElemMapSize];
/*      */ 
/* 1103 */     for (int i = 0; (i < this.fTransTable.length) && (this.fTransTable[i] != null); i++) {
/* 1104 */       for (int j = 0; j < this.fElemMapSize; j++) {
/* 1105 */         for (int k = j + 1; k < this.fElemMapSize; k++) {
/* 1106 */           if ((this.fTransTable[i][j] != -1) && (this.fTransTable[i][k] != -1))
/*      */           {
/* 1108 */             if (conflictTable[j][k] == 0) {
/* 1109 */               if (XSConstraints.overlapUPA(this.fElemMap[j], this.fElemMap[k], subGroupHandler))
/*      */               {
/* 1112 */                 if (this.fCountingStates != null) {
/* 1113 */                   Occurence o = this.fCountingStates[i];
/*      */ 
/* 1117 */                   if (o != null) if ((((this.fTransTable[i][j] == i ? 1 : 0) ^ (this.fTransTable[i][k] == i ? 1 : 0)) != 0) && (o.minOccurs == o.maxOccurs))
/*      */                     {
/* 1120 */                       conflictTable[j][k] = -1;
/* 1121 */                       continue;
/*      */                     }
/*      */                 }
/* 1124 */                 conflictTable[j][k] = 1;
/*      */               }
/*      */               else {
/* 1127 */                 conflictTable[j][k] = -1;
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1136 */     for (int i = 0; i < this.fElemMapSize; i++) {
/* 1137 */       for (int j = 0; j < this.fElemMapSize; j++) {
/* 1138 */         if (conflictTable[i][j] == 1)
/*      */         {
/* 1142 */           throw new XMLSchemaException("cos-nonambig", new Object[] { this.fElemMap[i].toString(), this.fElemMap[j].toString() });
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1150 */     for (int i = 0; i < this.fElemMapSize; i++) {
/* 1151 */       if (this.fElemMapType[i] == 2) {
/* 1152 */         XSWildcardDecl wildcard = (XSWildcardDecl)this.fElemMap[i];
/* 1153 */         if ((wildcard.fType == 3) || (wildcard.fType == 2))
/*      */         {
/* 1155 */           return true;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1160 */     return false;
/*      */   }
/*      */ 
/*      */   public Vector whatCanGoHere(int[] state)
/*      */   {
/* 1173 */     int curState = state[0];
/* 1174 */     if (curState < 0)
/* 1175 */       curState = state[1];
/* 1176 */     Occurence o = this.fCountingStates != null ? this.fCountingStates[curState] : null;
/*      */ 
/* 1178 */     int count = state[2];
/*      */ 
/* 1180 */     Vector ret = new Vector();
/* 1181 */     for (int elemIndex = 0; elemIndex < this.fElemMapSize; elemIndex++) {
/* 1182 */       int nextState = this.fTransTable[curState][elemIndex];
/* 1183 */       if ((nextState != -1) && (
/* 1184 */         (o == null) || 
/* 1185 */         (curState == nextState ? 
/* 1189 */         (count < o.maxOccurs) && (o.maxOccurs == -1) : 
/* 1196 */         count >= o.minOccurs)))
/*      */       {
/* 1200 */         ret.addElement(this.fElemMap[elemIndex]);
/*      */       }
/*      */     }
/* 1203 */     return ret;
/*      */   }
/*      */ 
/*      */   public ArrayList checkMinMaxBounds()
/*      */   {
/* 1220 */     ArrayList result = null;
/* 1221 */     for (int elemIndex = 0; elemIndex < this.fElemMapSize; elemIndex++) {
/* 1222 */       int count = this.fElemMapCounter[elemIndex];
/* 1223 */       if (count != -1)
/*      */       {
/* 1226 */         int minOccurs = this.fElemMapCounterLowerBound[elemIndex];
/* 1227 */         int maxOccurs = this.fElemMapCounterUpperBound[elemIndex];
/* 1228 */         if (count < minOccurs) {
/* 1229 */           if (result == null) result = new ArrayList();
/* 1230 */           result.add("cvc-complex-type.2.4.b");
/* 1231 */           result.add("{" + this.fElemMap[elemIndex] + "}");
/*      */         }
/* 1233 */         if ((maxOccurs != -1) && (count > maxOccurs)) {
/* 1234 */           if (result == null) result = new ArrayList();
/* 1235 */           result.add("cvc-complex-type.2.4.e");
/* 1236 */           result.add("{" + this.fElemMap[elemIndex] + "}");
/*      */         }
/*      */       }
/*      */     }
/* 1239 */     return result;
/*      */   }
/*      */ 
/*      */   static final class Occurence
/*      */   {
/*      */     final int minOccurs;
/*      */     final int maxOccurs;
/*      */     final int elemIndex;
/*      */ 
/*      */     public Occurence(XSCMRepeatingLeaf leaf, int elemIndex)
/*      */     {
/*  152 */       this.minOccurs = leaf.getMinOccurs();
/*  153 */       this.maxOccurs = leaf.getMaxOccurs();
/*  154 */       this.elemIndex = elemIndex;
/*      */     }
/*      */     public String toString() {
/*  157 */       return "minOccurs=" + this.minOccurs + ";maxOccurs=" + (this.maxOccurs != -1 ? Integer.toString(this.maxOccurs) : "unbounded");
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.models.XSDFACM
 * JD-Core Version:    0.6.2
 */