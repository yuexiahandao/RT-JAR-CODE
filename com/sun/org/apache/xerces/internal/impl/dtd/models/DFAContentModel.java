/*      */ package com.sun.org.apache.xerces.internal.impl.dtd.models;
/*      */ 
/*      */ import com.sun.org.apache.xerces.internal.xni.QName;
/*      */ import java.io.PrintStream;
/*      */ import java.util.HashMap;
/*      */ 
/*      */ public class DFAContentModel
/*      */   implements ContentModelValidator
/*      */ {
/*  103 */   private static String fEpsilonString = fEpsilonString.intern();
/*  104 */   private static String fEOCString = fEOCString.intern();
/*      */   private static final boolean DEBUG_VALIDATE_CONTENT = false;
/*  125 */   private QName[] fElemMap = null;
/*      */ 
/*  131 */   private int[] fElemMapType = null;
/*      */ 
/*  134 */   private int fElemMapSize = 0;
/*      */   private boolean fMixed;
/*  143 */   private int fEOCPos = 0;
/*      */ 
/*  151 */   private boolean[] fFinalStateFlags = null;
/*      */ 
/*  158 */   private CMStateSet[] fFollowList = null;
/*      */ 
/*  166 */   private CMNode fHeadNode = null;
/*      */ 
/*  172 */   private int fLeafCount = 0;
/*      */ 
/*  178 */   private CMLeaf[] fLeafList = null;
/*      */ 
/*  181 */   private int[] fLeafListType = null;
/*      */ 
/*  203 */   private int[][] fTransTable = (int[][])null;
/*      */ 
/*  209 */   private int fTransTableSize = 0;
/*      */ 
/*  219 */   private boolean fEmptyContentIsValid = false;
/*      */ 
/*  224 */   private final QName fQName = new QName();
/*      */ 
/*      */   public DFAContentModel(CMNode syntaxTree, int leafCount, boolean mixed)
/*      */   {
/*  246 */     this.fLeafCount = leafCount;
/*      */ 
/*  250 */     this.fMixed = mixed;
/*      */ 
/*  261 */     buildDFA(syntaxTree);
/*      */   }
/*      */ 
/*      */   public int validate(QName[] children, int offset, int length)
/*      */   {
/*  309 */     if (length == 0)
/*      */     {
/*  327 */       return this.fEmptyContentIsValid ? -1 : 0;
/*      */     }
/*      */ 
/*  336 */     int curState = 0;
/*  337 */     for (int childIndex = 0; childIndex < length; childIndex++)
/*      */     {
/*  340 */       QName curElem = children[(offset + childIndex)];
/*      */ 
/*  342 */       if ((!this.fMixed) || (curElem.localpart != null))
/*      */       {
/*  347 */         for (int elemIndex = 0; 
/*  348 */           elemIndex < this.fElemMapSize; elemIndex++)
/*      */         {
/*  350 */           int type = this.fElemMapType[elemIndex] & 0xF;
/*  351 */           if (type == 0)
/*      */           {
/*  353 */             if (this.fElemMap[elemIndex].rawname == curElem.rawname) {
/*  354 */               break;
/*      */             }
/*      */           }
/*  357 */           else if (type == 6) {
/*  358 */             String uri = this.fElemMap[elemIndex].uri;
/*  359 */             if ((uri == null) || (uri == curElem.uri))
/*      */               break;
/*      */           }
/*      */           else {
/*  363 */             if (type == 8 ? 
/*  364 */               curElem.uri != null : 
/*  368 */               (type == 7) && 
/*  369 */               (this.fElemMap[elemIndex].uri != curElem.uri))
/*      */             {
/*      */               break;
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/*  376 */         if (elemIndex == this.fElemMapSize)
/*      */         {
/*  387 */           return childIndex;
/*      */         }
/*      */ 
/*  394 */         curState = this.fTransTable[curState][elemIndex];
/*      */ 
/*  397 */         if (curState == -1)
/*      */         {
/*  400 */           return childIndex;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  411 */     if (this.fFinalStateFlags[curState] == 0) {
/*  412 */       return length;
/*      */     }
/*      */ 
/*  415 */     return -1;
/*      */   }
/*      */ 
/*      */   private void buildDFA(CMNode syntaxTree)
/*      */   {
/*  473 */     this.fQName.setValues(null, fEOCString, fEOCString, null);
/*  474 */     CMLeaf nodeEOC = new CMLeaf(this.fQName);
/*  475 */     this.fHeadNode = new CMBinOp(5, syntaxTree, nodeEOC);
/*      */ 
/*  489 */     this.fEOCPos = this.fLeafCount;
/*  490 */     nodeEOC.setPosition(this.fLeafCount++);
/*      */ 
/*  506 */     this.fLeafList = new CMLeaf[this.fLeafCount];
/*  507 */     this.fLeafListType = new int[this.fLeafCount];
/*  508 */     postTreeBuildInit(this.fHeadNode, 0);
/*      */ 
/*  515 */     this.fFollowList = new CMStateSet[this.fLeafCount];
/*  516 */     for (int index = 0; index < this.fLeafCount; index++)
/*  517 */       this.fFollowList[index] = new CMStateSet(this.fLeafCount);
/*  518 */     calcFollowList(this.fHeadNode);
/*      */ 
/*  530 */     this.fElemMap = new QName[this.fLeafCount];
/*  531 */     this.fElemMapType = new int[this.fLeafCount];
/*  532 */     this.fElemMapSize = 0;
/*  533 */     for (int outIndex = 0; outIndex < this.fLeafCount; outIndex++)
/*      */     {
/*  535 */       this.fElemMap[outIndex] = new QName();
/*      */ 
/*  546 */       QName element = this.fLeafList[outIndex].getElement();
/*      */ 
/*  549 */       for (int inIndex = 0; 
/*  550 */         inIndex < this.fElemMapSize; inIndex++)
/*      */       {
/*  552 */         if (this.fElemMap[inIndex].rawname == element.rawname)
/*      */         {
/*      */           break;
/*      */         }
/*      */       }
/*      */ 
/*  558 */       if (inIndex == this.fElemMapSize) {
/*  559 */         this.fElemMap[this.fElemMapSize].setValues(element);
/*  560 */         this.fElemMapType[this.fElemMapSize] = this.fLeafListType[outIndex];
/*  561 */         this.fElemMapSize += 1;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  576 */     int[] fLeafSorter = new int[this.fLeafCount + this.fElemMapSize];
/*  577 */     int fSortCount = 0;
/*      */ 
/*  579 */     for (int elemIndex = 0; elemIndex < this.fElemMapSize; elemIndex++) {
/*  580 */       for (int leafIndex = 0; leafIndex < this.fLeafCount; leafIndex++) {
/*  581 */         QName leaf = this.fLeafList[leafIndex].getElement();
/*  582 */         QName element = this.fElemMap[elemIndex];
/*  583 */         if (leaf.rawname == element.rawname) {
/*  584 */           fLeafSorter[(fSortCount++)] = leafIndex;
/*      */         }
/*      */       }
/*  587 */       fLeafSorter[(fSortCount++)] = -1;
/*      */     }
/*      */ 
/*  605 */     int curArraySize = this.fLeafCount * 4;
/*  606 */     CMStateSet[] statesToDo = new CMStateSet[curArraySize];
/*  607 */     this.fFinalStateFlags = new boolean[curArraySize];
/*  608 */     this.fTransTable = new int[curArraySize][];
/*      */ 
/*  615 */     CMStateSet setT = this.fHeadNode.firstPos();
/*      */ 
/*  625 */     int unmarkedState = 0;
/*  626 */     int curState = 0;
/*      */ 
/*  632 */     this.fTransTable[curState] = makeDefStateList();
/*  633 */     statesToDo[curState] = setT;
/*  634 */     curState++;
/*      */ 
/*  640 */     HashMap stateTable = new HashMap();
/*      */ 
/*  649 */     while (unmarkedState < curState)
/*      */     {
/*  655 */       setT = statesToDo[unmarkedState];
/*  656 */       int[] transEntry = this.fTransTable[unmarkedState];
/*      */ 
/*  659 */       this.fFinalStateFlags[unmarkedState] = setT.getBit(this.fEOCPos);
/*      */ 
/*  662 */       unmarkedState++;
/*      */ 
/*  665 */       CMStateSet newSet = null;
/*      */ 
/*  667 */       int sorterIndex = 0;
/*      */ 
/*  669 */       for (int elemIndex = 0; elemIndex < this.fElemMapSize; elemIndex++)
/*      */       {
/*  677 */         if (newSet == null)
/*  678 */           newSet = new CMStateSet(this.fLeafCount);
/*      */         else {
/*  680 */           newSet.zeroBits();
/*      */         }
/*      */ 
/*  683 */         int leafIndex = fLeafSorter[(sorterIndex++)];
/*      */ 
/*  685 */         while (leafIndex != -1)
/*      */         {
/*  687 */           if (setT.getBit(leafIndex))
/*      */           {
/*  694 */             newSet.union(this.fFollowList[leafIndex]);
/*      */           }
/*      */ 
/*  697 */           leafIndex = fLeafSorter[(sorterIndex++)];
/*      */         }
/*      */ 
/*  705 */         if (!newSet.isEmpty())
/*      */         {
/*  713 */           Integer stateObj = (Integer)stateTable.get(newSet);
/*  714 */           int stateIndex = stateObj == null ? curState : stateObj.intValue();
/*      */ 
/*  718 */           if (stateIndex == curState)
/*      */           {
/*  725 */             statesToDo[curState] = newSet;
/*  726 */             this.fTransTable[curState] = makeDefStateList();
/*      */ 
/*  729 */             stateTable.put(newSet, new Integer(curState));
/*      */ 
/*  733 */             curState++;
/*      */ 
/*  740 */             newSet = null;
/*      */           }
/*      */ 
/*  749 */           transEntry[elemIndex] = stateIndex;
/*      */ 
/*  752 */           if (curState == curArraySize)
/*      */           {
/*  759 */             int newSize = (int)(curArraySize * 1.5D);
/*  760 */             CMStateSet[] newToDo = new CMStateSet[newSize];
/*  761 */             boolean[] newFinalFlags = new boolean[newSize];
/*  762 */             int[][] newTransTable = new int[newSize][];
/*      */ 
/*  765 */             System.arraycopy(statesToDo, 0, newToDo, 0, curArraySize);
/*  766 */             System.arraycopy(this.fFinalStateFlags, 0, newFinalFlags, 0, curArraySize);
/*  767 */             System.arraycopy(this.fTransTable, 0, newTransTable, 0, curArraySize);
/*      */ 
/*  770 */             curArraySize = newSize;
/*  771 */             statesToDo = newToDo;
/*  772 */             this.fFinalStateFlags = newFinalFlags;
/*  773 */             this.fTransTable = newTransTable;
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  780 */     this.fEmptyContentIsValid = ((CMBinOp)this.fHeadNode).getLeft().isNullable();
/*      */ 
/*  788 */     this.fHeadNode = null;
/*  789 */     this.fLeafList = null;
/*  790 */     this.fFollowList = null;
/*      */   }
/*      */ 
/*      */   private void calcFollowList(CMNode nodeCur)
/*      */   {
/*  804 */     if (nodeCur.type() == 4)
/*      */     {
/*  807 */       calcFollowList(((CMBinOp)nodeCur).getLeft());
/*  808 */       calcFollowList(((CMBinOp)nodeCur).getRight());
/*      */     }
/*  810 */     else if (nodeCur.type() == 5)
/*      */     {
/*  813 */       calcFollowList(((CMBinOp)nodeCur).getLeft());
/*  814 */       calcFollowList(((CMBinOp)nodeCur).getRight());
/*      */ 
/*  821 */       CMStateSet last = ((CMBinOp)nodeCur).getLeft().lastPos();
/*  822 */       CMStateSet first = ((CMBinOp)nodeCur).getRight().firstPos();
/*      */ 
/*  829 */       for (int index = 0; index < this.fLeafCount; index++)
/*      */       {
/*  831 */         if (last.getBit(index)) {
/*  832 */           this.fFollowList[index].union(first);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*  865 */     else if ((nodeCur.type() == 2) || (nodeCur.type() == 3))
/*      */     {
/*  869 */       calcFollowList(((CMUniOp)nodeCur).getChild());
/*      */ 
/*  875 */       CMStateSet first = nodeCur.firstPos();
/*  876 */       CMStateSet last = nodeCur.lastPos();
/*      */ 
/*  883 */       for (int index = 0; index < this.fLeafCount; index++)
/*      */       {
/*  885 */         if (last.getBit(index)) {
/*  886 */           this.fFollowList[index].union(first);
/*      */         }
/*      */       }
/*      */     }
/*  890 */     else if (nodeCur.type() == 1)
/*      */     {
/*  892 */       calcFollowList(((CMUniOp)nodeCur).getChild());
/*      */     }
/*      */   }
/*      */ 
/*      */   private void dumpTree(CMNode nodeCur, int level)
/*      */   {
/*  907 */     for (int index = 0; index < level; index++) {
/*  908 */       System.out.print("   ");
/*      */     }
/*  910 */     int type = nodeCur.type();
/*  911 */     if ((type == 4) || (type == 5))
/*      */     {
/*  914 */       if (type == 4)
/*  915 */         System.out.print("Choice Node ");
/*      */       else {
/*  917 */         System.out.print("Seq Node ");
/*      */       }
/*  919 */       if (nodeCur.isNullable()) {
/*  920 */         System.out.print("Nullable ");
/*      */       }
/*  922 */       System.out.print("firstPos=");
/*  923 */       System.out.print(nodeCur.firstPos().toString());
/*  924 */       System.out.print(" lastPos=");
/*  925 */       System.out.println(nodeCur.lastPos().toString());
/*      */ 
/*  927 */       dumpTree(((CMBinOp)nodeCur).getLeft(), level + 1);
/*  928 */       dumpTree(((CMBinOp)nodeCur).getRight(), level + 1);
/*      */     }
/*  930 */     else if (nodeCur.type() == 2)
/*      */     {
/*  932 */       System.out.print("Rep Node ");
/*      */ 
/*  934 */       if (nodeCur.isNullable()) {
/*  935 */         System.out.print("Nullable ");
/*      */       }
/*  937 */       System.out.print("firstPos=");
/*  938 */       System.out.print(nodeCur.firstPos().toString());
/*  939 */       System.out.print(" lastPos=");
/*  940 */       System.out.println(nodeCur.lastPos().toString());
/*      */ 
/*  942 */       dumpTree(((CMUniOp)nodeCur).getChild(), level + 1);
/*      */     }
/*  944 */     else if (nodeCur.type() == 0)
/*      */     {
/*  946 */       System.out.print("Leaf: (pos=" + ((CMLeaf)nodeCur).getPosition() + "), " + ((CMLeaf)nodeCur).getElement() + "(elemIndex=" + ((CMLeaf)nodeCur).getElement() + ") ");
/*      */ 
/*  957 */       if (nodeCur.isNullable()) {
/*  958 */         System.out.print(" Nullable ");
/*      */       }
/*  960 */       System.out.print("firstPos=");
/*  961 */       System.out.print(nodeCur.firstPos().toString());
/*  962 */       System.out.print(" lastPos=");
/*  963 */       System.out.println(nodeCur.lastPos().toString());
/*      */     }
/*      */     else
/*      */     {
/*  967 */       throw new RuntimeException("ImplementationMessages.VAL_NIICM");
/*      */     }
/*      */   }
/*      */ 
/*      */   private int[] makeDefStateList()
/*      */   {
/*  979 */     int[] retArray = new int[this.fElemMapSize];
/*  980 */     for (int index = 0; index < this.fElemMapSize; index++)
/*  981 */       retArray[index] = -1;
/*  982 */     return retArray;
/*      */   }
/*      */ 
/*      */   private int postTreeBuildInit(CMNode nodeCur, int curIndex)
/*      */   {
/*  989 */     nodeCur.setMaxStates(this.fLeafCount);
/*      */ 
/*  992 */     if (((nodeCur.type() & 0xF) == 6) || ((nodeCur.type() & 0xF) == 8) || ((nodeCur.type() & 0xF) == 7))
/*      */     {
/*  996 */       QName qname = new QName(null, null, null, ((CMAny)nodeCur).getURI());
/*  997 */       this.fLeafList[curIndex] = new CMLeaf(qname, ((CMAny)nodeCur).getPosition());
/*  998 */       this.fLeafListType[curIndex] = nodeCur.type();
/*  999 */       curIndex++;
/*      */     }
/* 1001 */     else if ((nodeCur.type() == 4) || (nodeCur.type() == 5))
/*      */     {
/* 1004 */       curIndex = postTreeBuildInit(((CMBinOp)nodeCur).getLeft(), curIndex);
/* 1005 */       curIndex = postTreeBuildInit(((CMBinOp)nodeCur).getRight(), curIndex);
/*      */     }
/* 1007 */     else if ((nodeCur.type() == 2) || (nodeCur.type() == 3) || (nodeCur.type() == 1))
/*      */     {
/* 1011 */       curIndex = postTreeBuildInit(((CMUniOp)nodeCur).getChild(), curIndex);
/*      */     }
/* 1013 */     else if (nodeCur.type() == 0)
/*      */     {
/* 1019 */       QName node = ((CMLeaf)nodeCur).getElement();
/* 1020 */       if (node.localpart != fEpsilonString) {
/* 1021 */         this.fLeafList[curIndex] = ((CMLeaf)nodeCur);
/* 1022 */         this.fLeafListType[curIndex] = 0;
/* 1023 */         curIndex++;
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 1028 */       throw new RuntimeException("ImplementationMessages.VAL_NIICM: type=" + nodeCur.type());
/*      */     }
/* 1030 */     return curIndex;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dtd.models.DFAContentModel
 * JD-Core Version:    0.6.2
 */