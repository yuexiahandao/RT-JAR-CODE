/*     */ package com.sun.org.apache.xerces.internal.impl.xs;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.util.StringListImpl;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.util.XSNamedMap4Types;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.util.XSNamedMapImpl;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.util.XSObjectListImpl;
/*     */ import com.sun.org.apache.xerces.internal.util.SymbolHash;
/*     */ import com.sun.org.apache.xerces.internal.util.XMLSymbols;
/*     */ import com.sun.org.apache.xerces.internal.xs.StringList;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSAttributeDeclaration;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSAttributeGroupDefinition;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSElementDeclaration;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSModel;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSModelGroupDefinition;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSNamedMap;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSNamespaceItem;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSNamespaceItemList;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSNotationDeclaration;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSObject;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSObjectList;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.AbstractList;
/*     */ import java.util.Iterator;
/*     */ import java.util.ListIterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public final class XSModelImpl extends AbstractList
/*     */   implements XSModel, XSNamespaceItemList
/*     */ {
/*     */   private static final short MAX_COMP_IDX = 16;
/*  66 */   private static final boolean[] GLOBAL_COMP = { false, true, true, true, false, true, true, false, false, false, false, true, false, false, false, true, true };
/*     */   private final int fGrammarCount;
/*     */   private final String[] fNamespaces;
/*     */   private final SchemaGrammar[] fGrammarList;
/*     */   private final SymbolHash fGrammarMap;
/*     */   private final SymbolHash fSubGroupMap;
/*     */   private final XSNamedMap[] fGlobalComponents;
/*     */   private final XSNamedMap[][] fNSComponents;
/*     */   private final StringList fNamespacesList;
/* 104 */   private XSObjectList fAnnotations = null;
/*     */   private final boolean fHasIDC;
/*     */ 
/*     */   public XSModelImpl(SchemaGrammar[] grammars)
/*     */   {
/* 116 */     this(grammars, (short)1);
/*     */   }
/*     */ 
/*     */   public XSModelImpl(SchemaGrammar[] grammars, short s4sVersion)
/*     */   {
/* 121 */     int len = grammars.length;
/* 122 */     int initialSize = Math.max(len + 1, 5);
/* 123 */     String[] namespaces = new String[initialSize];
/* 124 */     SchemaGrammar[] grammarList = new SchemaGrammar[initialSize];
/* 125 */     boolean hasS4S = false;
/* 126 */     for (int i = 0; i < len; i++) {
/* 127 */       SchemaGrammar sg = grammars[i];
/* 128 */       String tns = sg.getTargetNamespace();
/* 129 */       namespaces[i] = tns;
/* 130 */       grammarList[i] = sg;
/* 131 */       if (tns == SchemaSymbols.URI_SCHEMAFORSCHEMA) {
/* 132 */         hasS4S = true;
/*     */       }
/*     */     }
/*     */ 
/* 136 */     if (!hasS4S) {
/* 137 */       namespaces[len] = SchemaSymbols.URI_SCHEMAFORSCHEMA;
/* 138 */       grammarList[(len++)] = SchemaGrammar.getS4SGrammar(s4sVersion);
/*     */     }
/*     */ 
/* 145 */     for (int i = 0; i < len; i++)
/*     */     {
/* 147 */       SchemaGrammar sg1 = grammarList[i];
/* 148 */       Vector gs = sg1.getImportedGrammars();
/*     */ 
/* 150 */       for (int j = gs == null ? -1 : gs.size() - 1; j >= 0; j--) {
/* 151 */         SchemaGrammar sg2 = (SchemaGrammar)gs.elementAt(j);
/*     */ 
/* 153 */         for (int k = 0; (k < len) && 
/* 154 */           (sg2 != grammarList[k]); k++);
/* 159 */         if (k == len)
/*     */         {
/* 161 */           if (len == grammarList.length) {
/* 162 */             String[] newSA = new String[len * 2];
/* 163 */             System.arraycopy(namespaces, 0, newSA, 0, len);
/* 164 */             namespaces = newSA;
/* 165 */             SchemaGrammar[] newGA = new SchemaGrammar[len * 2];
/* 166 */             System.arraycopy(grammarList, 0, newGA, 0, len);
/* 167 */             grammarList = newGA;
/*     */           }
/* 169 */           namespaces[len] = sg2.getTargetNamespace();
/* 170 */           grammarList[len] = sg2;
/* 171 */           len++;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 176 */     this.fNamespaces = namespaces;
/* 177 */     this.fGrammarList = grammarList;
/*     */ 
/* 179 */     boolean hasIDC = false;
/*     */ 
/* 181 */     this.fGrammarMap = new SymbolHash(len * 2);
/* 182 */     for (i = 0; i < len; i++) {
/* 183 */       this.fGrammarMap.put(null2EmptyString(this.fNamespaces[i]), this.fGrammarList[i]);
/*     */ 
/* 185 */       if (this.fGrammarList[i].hasIDConstraints()) {
/* 186 */         hasIDC = true;
/*     */       }
/*     */     }
/*     */ 
/* 190 */     this.fHasIDC = hasIDC;
/* 191 */     this.fGrammarCount = len;
/* 192 */     this.fGlobalComponents = new XSNamedMap[17];
/* 193 */     this.fNSComponents = new XSNamedMap[len][17];
/* 194 */     this.fNamespacesList = new StringListImpl(this.fNamespaces, this.fGrammarCount);
/*     */ 
/* 197 */     this.fSubGroupMap = buildSubGroups();
/*     */   }
/*     */ 
/*     */   private SymbolHash buildSubGroups_Org() {
/* 201 */     SubstitutionGroupHandler sgHandler = new SubstitutionGroupHandler(null);
/* 202 */     for (int i = 0; i < this.fGrammarCount; i++) {
/* 203 */       sgHandler.addSubstitutionGroup(this.fGrammarList[i].getSubstitutionGroups());
/*     */     }
/*     */ 
/* 206 */     XSNamedMap elements = getComponents((short)2);
/* 207 */     int len = elements.getLength();
/* 208 */     SymbolHash subGroupMap = new SymbolHash(len * 2);
/*     */ 
/* 211 */     for (int i = 0; i < len; i++) {
/* 212 */       XSElementDecl head = (XSElementDecl)elements.item(i);
/* 213 */       XSElementDeclaration[] subGroup = sgHandler.getSubstitutionGroup(head);
/* 214 */       subGroupMap.put(head, subGroup.length > 0 ? new XSObjectListImpl(subGroup, subGroup.length) : XSObjectListImpl.EMPTY_LIST);
/*     */     }
/*     */ 
/* 217 */     return subGroupMap;
/*     */   }
/*     */ 
/*     */   private SymbolHash buildSubGroups() {
/* 221 */     SubstitutionGroupHandler sgHandler = new SubstitutionGroupHandler(null);
/* 222 */     for (int i = 0; i < this.fGrammarCount; i++) {
/* 223 */       sgHandler.addSubstitutionGroup(this.fGrammarList[i].getSubstitutionGroups());
/*     */     }
/*     */ 
/* 226 */     XSObjectListImpl elements = getGlobalElements();
/* 227 */     int len = elements.getLength();
/* 228 */     SymbolHash subGroupMap = new SymbolHash(len * 2);
/*     */ 
/* 231 */     for (int i = 0; i < len; i++) {
/* 232 */       XSElementDecl head = (XSElementDecl)elements.item(i);
/* 233 */       XSElementDeclaration[] subGroup = sgHandler.getSubstitutionGroup(head);
/* 234 */       subGroupMap.put(head, subGroup.length > 0 ? new XSObjectListImpl(subGroup, subGroup.length) : XSObjectListImpl.EMPTY_LIST);
/*     */     }
/*     */ 
/* 237 */     return subGroupMap;
/*     */   }
/*     */ 
/*     */   private XSObjectListImpl getGlobalElements() {
/* 241 */     SymbolHash[] tables = new SymbolHash[this.fGrammarCount];
/* 242 */     int length = 0;
/*     */ 
/* 244 */     for (int i = 0; i < this.fGrammarCount; i++) {
/* 245 */       tables[i] = this.fGrammarList[i].fAllGlobalElemDecls;
/* 246 */       length += tables[i].getLength();
/*     */     }
/*     */ 
/* 249 */     if (length == 0) {
/* 250 */       return XSObjectListImpl.EMPTY_LIST;
/*     */     }
/*     */ 
/* 253 */     XSObject[] components = new XSObject[length];
/*     */ 
/* 255 */     int start = 0;
/* 256 */     for (int i = 0; i < this.fGrammarCount; i++) {
/* 257 */       tables[i].getValues(components, start);
/* 258 */       start += tables[i].getLength();
/*     */     }
/*     */ 
/* 261 */     return new XSObjectListImpl(components, length);
/*     */   }
/*     */ 
/*     */   public StringList getNamespaces()
/*     */   {
/* 271 */     return this.fNamespacesList;
/*     */   }
/*     */ 
/*     */   public XSNamespaceItemList getNamespaceItems()
/*     */   {
/* 283 */     return this;
/*     */   }
/*     */ 
/*     */   public synchronized XSNamedMap getComponents(short objectType)
/*     */   {
/* 300 */     if ((objectType <= 0) || (objectType > 16) || (GLOBAL_COMP[objectType] == 0))
/*     */     {
/* 302 */       return XSNamedMapImpl.EMPTY_MAP;
/*     */     }
/*     */ 
/* 305 */     SymbolHash[] tables = new SymbolHash[this.fGrammarCount];
/*     */ 
/* 307 */     if (this.fGlobalComponents[objectType] == null) {
/* 308 */       for (int i = 0; i < this.fGrammarCount; i++)
/* 309 */         switch (objectType) {
/*     */         case 3:
/*     */         case 15:
/*     */         case 16:
/* 313 */           tables[i] = this.fGrammarList[i].fGlobalTypeDecls;
/* 314 */           break;
/*     */         case 1:
/* 316 */           tables[i] = this.fGrammarList[i].fGlobalAttrDecls;
/* 317 */           break;
/*     */         case 2:
/* 319 */           tables[i] = this.fGrammarList[i].fGlobalElemDecls;
/* 320 */           break;
/*     */         case 5:
/* 322 */           tables[i] = this.fGrammarList[i].fGlobalAttrGrpDecls;
/* 323 */           break;
/*     */         case 6:
/* 325 */           tables[i] = this.fGrammarList[i].fGlobalGroupDecls;
/* 326 */           break;
/*     */         case 11:
/* 328 */           tables[i] = this.fGrammarList[i].fGlobalNotationDecls;
/*     */         case 4:
/*     */         case 7:
/*     */         case 8:
/*     */         case 9:
/*     */         case 10:
/*     */         case 12:
/*     */         case 13:
/* 334 */         case 14: }  if ((objectType == 15) || (objectType == 16))
/*     */       {
/* 336 */         this.fGlobalComponents[objectType] = new XSNamedMap4Types(this.fNamespaces, tables, this.fGrammarCount, objectType);
/*     */       }
/*     */       else {
/* 339 */         this.fGlobalComponents[objectType] = new XSNamedMapImpl(this.fNamespaces, tables, this.fGrammarCount);
/*     */       }
/*     */     }
/*     */ 
/* 343 */     return this.fGlobalComponents[objectType];
/*     */   }
/*     */ 
/*     */   public synchronized XSNamedMap getComponentsByNamespace(short objectType, String namespace)
/*     */   {
/* 360 */     if ((objectType <= 0) || (objectType > 16) || (GLOBAL_COMP[objectType] == 0))
/*     */     {
/* 362 */       return XSNamedMapImpl.EMPTY_MAP;
/*     */     }
/*     */ 
/* 366 */     int i = 0;
/* 367 */     if (namespace != null) {
/* 368 */       while ((i < this.fGrammarCount) && 
/* 369 */         (!namespace.equals(this.fNamespaces[i]))) {
/* 368 */         i++;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 375 */     while ((i < this.fGrammarCount) && 
/* 376 */       (this.fNamespaces[i] != null)) {
/* 375 */       i++;
/*     */     }
/*     */ 
/* 381 */     if (i == this.fGrammarCount) {
/* 382 */       return XSNamedMapImpl.EMPTY_MAP;
/*     */     }
/*     */ 
/* 386 */     if (this.fNSComponents[i][objectType] == null) {
/* 387 */       SymbolHash table = null;
/* 388 */       switch (objectType) {
/*     */       case 3:
/*     */       case 15:
/*     */       case 16:
/* 392 */         table = this.fGrammarList[i].fGlobalTypeDecls;
/* 393 */         break;
/*     */       case 1:
/* 395 */         table = this.fGrammarList[i].fGlobalAttrDecls;
/* 396 */         break;
/*     */       case 2:
/* 398 */         table = this.fGrammarList[i].fGlobalElemDecls;
/* 399 */         break;
/*     */       case 5:
/* 401 */         table = this.fGrammarList[i].fGlobalAttrGrpDecls;
/* 402 */         break;
/*     */       case 6:
/* 404 */         table = this.fGrammarList[i].fGlobalGroupDecls;
/* 405 */         break;
/*     */       case 11:
/* 407 */         table = this.fGrammarList[i].fGlobalNotationDecls;
/*     */       case 4:
/*     */       case 7:
/*     */       case 8:
/*     */       case 9:
/*     */       case 10:
/*     */       case 12:
/*     */       case 13:
/* 413 */       case 14: } if ((objectType == 15) || (objectType == 16))
/*     */       {
/* 415 */         this.fNSComponents[i][objectType] = new XSNamedMap4Types(namespace, table, objectType);
/*     */       }
/*     */       else {
/* 418 */         this.fNSComponents[i][objectType] = new XSNamedMapImpl(namespace, table);
/*     */       }
/*     */     }
/*     */ 
/* 422 */     return this.fNSComponents[i][objectType];
/*     */   }
/*     */ 
/*     */   public XSTypeDefinition getTypeDefinition(String name, String namespace)
/*     */   {
/* 435 */     SchemaGrammar sg = (SchemaGrammar)this.fGrammarMap.get(null2EmptyString(namespace));
/* 436 */     if (sg == null) {
/* 437 */       return null;
/*     */     }
/* 439 */     return (XSTypeDefinition)sg.fGlobalTypeDecls.get(name);
/*     */   }
/*     */ 
/*     */   public XSTypeDefinition getTypeDefinition(String name, String namespace, String loc)
/*     */   {
/* 454 */     SchemaGrammar sg = (SchemaGrammar)this.fGrammarMap.get(null2EmptyString(namespace));
/* 455 */     if (sg == null) {
/* 456 */       return null;
/*     */     }
/* 458 */     return sg.getGlobalTypeDecl(name, loc);
/*     */   }
/*     */ 
/*     */   public XSAttributeDeclaration getAttributeDeclaration(String name, String namespace)
/*     */   {
/* 470 */     SchemaGrammar sg = (SchemaGrammar)this.fGrammarMap.get(null2EmptyString(namespace));
/* 471 */     if (sg == null) {
/* 472 */       return null;
/*     */     }
/* 474 */     return (XSAttributeDeclaration)sg.fGlobalAttrDecls.get(name);
/*     */   }
/*     */ 
/*     */   public XSAttributeDeclaration getAttributeDeclaration(String name, String namespace, String loc)
/*     */   {
/* 488 */     SchemaGrammar sg = (SchemaGrammar)this.fGrammarMap.get(null2EmptyString(namespace));
/* 489 */     if (sg == null) {
/* 490 */       return null;
/*     */     }
/* 492 */     return sg.getGlobalAttributeDecl(name, loc);
/*     */   }
/*     */ 
/*     */   public XSElementDeclaration getElementDeclaration(String name, String namespace)
/*     */   {
/* 504 */     SchemaGrammar sg = (SchemaGrammar)this.fGrammarMap.get(null2EmptyString(namespace));
/* 505 */     if (sg == null) {
/* 506 */       return null;
/*     */     }
/* 508 */     return (XSElementDeclaration)sg.fGlobalElemDecls.get(name);
/*     */   }
/*     */ 
/*     */   public XSElementDeclaration getElementDeclaration(String name, String namespace, String loc)
/*     */   {
/* 522 */     SchemaGrammar sg = (SchemaGrammar)this.fGrammarMap.get(null2EmptyString(namespace));
/* 523 */     if (sg == null) {
/* 524 */       return null;
/*     */     }
/* 526 */     return sg.getGlobalElementDecl(name, loc);
/*     */   }
/*     */ 
/*     */   public XSAttributeGroupDefinition getAttributeGroup(String name, String namespace)
/*     */   {
/* 538 */     SchemaGrammar sg = (SchemaGrammar)this.fGrammarMap.get(null2EmptyString(namespace));
/* 539 */     if (sg == null) {
/* 540 */       return null;
/*     */     }
/* 542 */     return (XSAttributeGroupDefinition)sg.fGlobalAttrGrpDecls.get(name);
/*     */   }
/*     */ 
/*     */   public XSAttributeGroupDefinition getAttributeGroup(String name, String namespace, String loc)
/*     */   {
/* 556 */     SchemaGrammar sg = (SchemaGrammar)this.fGrammarMap.get(null2EmptyString(namespace));
/* 557 */     if (sg == null) {
/* 558 */       return null;
/*     */     }
/* 560 */     return sg.getGlobalAttributeGroupDecl(name, loc);
/*     */   }
/*     */ 
/*     */   public XSModelGroupDefinition getModelGroupDefinition(String name, String namespace)
/*     */   {
/* 573 */     SchemaGrammar sg = (SchemaGrammar)this.fGrammarMap.get(null2EmptyString(namespace));
/* 574 */     if (sg == null) {
/* 575 */       return null;
/*     */     }
/* 577 */     return (XSModelGroupDefinition)sg.fGlobalGroupDecls.get(name);
/*     */   }
/*     */ 
/*     */   public XSModelGroupDefinition getModelGroupDefinition(String name, String namespace, String loc)
/*     */   {
/* 592 */     SchemaGrammar sg = (SchemaGrammar)this.fGrammarMap.get(null2EmptyString(namespace));
/* 593 */     if (sg == null) {
/* 594 */       return null;
/*     */     }
/* 596 */     return sg.getGlobalGroupDecl(name, loc);
/*     */   }
/*     */ 
/*     */   public XSNotationDeclaration getNotationDeclaration(String name, String namespace)
/*     */   {
/* 605 */     SchemaGrammar sg = (SchemaGrammar)this.fGrammarMap.get(null2EmptyString(namespace));
/* 606 */     if (sg == null) {
/* 607 */       return null;
/*     */     }
/* 609 */     return (XSNotationDeclaration)sg.fGlobalNotationDecls.get(name);
/*     */   }
/*     */ 
/*     */   public XSNotationDeclaration getNotationDeclaration(String name, String namespace, String loc)
/*     */   {
/* 615 */     SchemaGrammar sg = (SchemaGrammar)this.fGrammarMap.get(null2EmptyString(namespace));
/* 616 */     if (sg == null) {
/* 617 */       return null;
/*     */     }
/* 619 */     return sg.getGlobalNotationDecl(name, loc);
/*     */   }
/*     */ 
/*     */   public synchronized XSObjectList getAnnotations()
/*     */   {
/* 627 */     if (this.fAnnotations != null) {
/* 628 */       return this.fAnnotations;
/*     */     }
/*     */ 
/* 632 */     int totalAnnotations = 0;
/* 633 */     for (int i = 0; i < this.fGrammarCount; i++) {
/* 634 */       totalAnnotations += this.fGrammarList[i].fNumAnnotations;
/*     */     }
/* 636 */     if (totalAnnotations == 0) {
/* 637 */       this.fAnnotations = XSObjectListImpl.EMPTY_LIST;
/* 638 */       return this.fAnnotations;
/*     */     }
/* 640 */     XSAnnotationImpl[] annotations = new XSAnnotationImpl[totalAnnotations];
/* 641 */     int currPos = 0;
/* 642 */     for (int i = 0; i < this.fGrammarCount; i++) {
/* 643 */       SchemaGrammar currGrammar = this.fGrammarList[i];
/* 644 */       if (currGrammar.fNumAnnotations > 0) {
/* 645 */         System.arraycopy(currGrammar.fAnnotations, 0, annotations, currPos, currGrammar.fNumAnnotations);
/* 646 */         currPos += currGrammar.fNumAnnotations;
/*     */       }
/*     */     }
/* 649 */     this.fAnnotations = new XSObjectListImpl(annotations, annotations.length);
/* 650 */     return this.fAnnotations;
/*     */   }
/*     */ 
/*     */   private static final String null2EmptyString(String str) {
/* 654 */     return str == null ? XMLSymbols.EMPTY_STRING : str;
/*     */   }
/*     */ 
/*     */   public boolean hasIDConstraints()
/*     */   {
/* 664 */     return this.fHasIDC;
/*     */   }
/*     */ 
/*     */   public XSObjectList getSubstitutionGroup(XSElementDeclaration head)
/*     */   {
/* 679 */     return (XSObjectList)this.fSubGroupMap.get(head);
/*     */   }
/*     */ 
/*     */   public int getLength()
/*     */   {
/* 691 */     return this.fGrammarCount;
/*     */   }
/*     */ 
/*     */   public XSNamespaceItem item(int index)
/*     */   {
/* 704 */     if ((index < 0) || (index >= this.fGrammarCount)) {
/* 705 */       return null;
/*     */     }
/* 707 */     return this.fGrammarList[index];
/*     */   }
/*     */ 
/*     */   public Object get(int index)
/*     */   {
/* 715 */     if ((index >= 0) && (index < this.fGrammarCount)) {
/* 716 */       return this.fGrammarList[index];
/*     */     }
/* 718 */     throw new IndexOutOfBoundsException("Index: " + index);
/*     */   }
/*     */ 
/*     */   public int size() {
/* 722 */     return getLength();
/*     */   }
/*     */ 
/*     */   public Iterator iterator() {
/* 726 */     return listIterator0(0);
/*     */   }
/*     */ 
/*     */   public ListIterator listIterator() {
/* 730 */     return listIterator0(0);
/*     */   }
/*     */ 
/*     */   public ListIterator listIterator(int index) {
/* 734 */     if ((index >= 0) && (index < this.fGrammarCount)) {
/* 735 */       return listIterator0(index);
/*     */     }
/* 737 */     throw new IndexOutOfBoundsException("Index: " + index);
/*     */   }
/*     */ 
/*     */   private ListIterator listIterator0(int index) {
/* 741 */     return new XSNamespaceItemListIterator(index);
/*     */   }
/*     */ 
/*     */   public Object[] toArray() {
/* 745 */     Object[] a = new Object[this.fGrammarCount];
/* 746 */     toArray0(a);
/* 747 */     return a;
/*     */   }
/*     */ 
/*     */   public Object[] toArray(Object[] a) {
/* 751 */     if (a.length < this.fGrammarCount) {
/* 752 */       Class arrayClass = a.getClass();
/* 753 */       Class componentType = arrayClass.getComponentType();
/* 754 */       a = (Object[])Array.newInstance(componentType, this.fGrammarCount);
/*     */     }
/* 756 */     toArray0(a);
/* 757 */     if (a.length > this.fGrammarCount) {
/* 758 */       a[this.fGrammarCount] = null;
/*     */     }
/* 760 */     return a;
/*     */   }
/*     */ 
/*     */   private void toArray0(Object[] a) {
/* 764 */     if (this.fGrammarCount > 0)
/* 765 */       System.arraycopy(this.fGrammarList, 0, a, 0, this.fGrammarCount);
/*     */   }
/*     */ 
/*     */   private final class XSNamespaceItemListIterator implements ListIterator {
/*     */     private int index;
/*     */ 
/*     */     public XSNamespaceItemListIterator(int index) {
/* 772 */       this.index = index;
/*     */     }
/*     */     public boolean hasNext() {
/* 775 */       return this.index < XSModelImpl.this.fGrammarCount;
/*     */     }
/*     */     public Object next() {
/* 778 */       if (this.index < XSModelImpl.this.fGrammarCount) {
/* 779 */         return XSModelImpl.this.fGrammarList[(this.index++)];
/*     */       }
/* 781 */       throw new NoSuchElementException();
/*     */     }
/*     */     public boolean hasPrevious() {
/* 784 */       return this.index > 0;
/*     */     }
/*     */     public Object previous() {
/* 787 */       if (this.index > 0) {
/* 788 */         return XSModelImpl.this.fGrammarList[(--this.index)];
/*     */       }
/* 790 */       throw new NoSuchElementException();
/*     */     }
/*     */     public int nextIndex() {
/* 793 */       return this.index;
/*     */     }
/*     */     public int previousIndex() {
/* 796 */       return this.index - 1;
/*     */     }
/*     */     public void remove() {
/* 799 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     public void set(Object o) {
/* 802 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     public void add(Object o) {
/* 805 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.XSModelImpl
 * JD-Core Version:    0.6.2
 */