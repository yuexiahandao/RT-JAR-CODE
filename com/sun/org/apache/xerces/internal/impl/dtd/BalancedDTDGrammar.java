/*     */ package com.sun.org.apache.xerces.internal.impl.dtd;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*     */ import com.sun.org.apache.xerces.internal.xni.Augmentations;
/*     */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*     */ 
/*     */ final class BalancedDTDGrammar extends DTDGrammar
/*     */ {
/*     */   private boolean fMixed;
/*  47 */   private int fDepth = 0;
/*     */ 
/*  50 */   private short[] fOpStack = null;
/*     */   private int[][] fGroupIndexStack;
/*     */   private int[] fGroupIndexStackSizes;
/*     */ 
/*     */   public BalancedDTDGrammar(SymbolTable symbolTable, XMLDTDDescription desc)
/*     */   {
/*  64 */     super(symbolTable, desc);
/*     */   }
/*     */ 
/*     */   public final void startContentModel(String elementName, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/*  83 */     this.fDepth = 0;
/*  84 */     initializeContentModelStacks();
/*  85 */     super.startContentModel(elementName, augs);
/*     */   }
/*     */ 
/*     */   public final void startGroup(Augmentations augs)
/*     */     throws XNIException
/*     */   {
/* 102 */     this.fDepth += 1;
/* 103 */     initializeContentModelStacks();
/* 104 */     this.fMixed = false;
/*     */   }
/*     */ 
/*     */   public final void pcdata(Augmentations augs)
/*     */     throws XNIException
/*     */   {
/* 120 */     this.fMixed = true;
/*     */   }
/*     */ 
/*     */   public final void element(String elementName, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/* 133 */     addToCurrentGroup(addUniqueLeafNode(elementName));
/*     */   }
/*     */ 
/*     */   public final void separator(short separator, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/* 149 */     if (separator == 0) {
/* 150 */       this.fOpStack[this.fDepth] = 4;
/*     */     }
/* 152 */     else if (separator == 1)
/* 153 */       this.fOpStack[this.fDepth] = 5;
/*     */   }
/*     */ 
/*     */   public final void occurrence(short occurrence, Augmentations augs)
/*     */     throws XNIException
/*     */   {
/* 172 */     if (!this.fMixed) {
/* 173 */       int currentIndex = this.fGroupIndexStackSizes[this.fDepth] - 1;
/* 174 */       if (occurrence == 2) {
/* 175 */         this.fGroupIndexStack[this.fDepth][currentIndex] = addContentSpecNode(1, this.fGroupIndexStack[this.fDepth][currentIndex], -1);
/*     */       }
/* 177 */       else if (occurrence == 3) {
/* 178 */         this.fGroupIndexStack[this.fDepth][currentIndex] = addContentSpecNode(2, this.fGroupIndexStack[this.fDepth][currentIndex], -1);
/*     */       }
/* 180 */       else if (occurrence == 4)
/* 181 */         this.fGroupIndexStack[this.fDepth][currentIndex] = addContentSpecNode(3, this.fGroupIndexStack[this.fDepth][currentIndex], -1);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void endGroup(Augmentations augs)
/*     */     throws XNIException
/*     */   {
/* 194 */     int length = this.fGroupIndexStackSizes[this.fDepth];
/* 195 */     int group = length > 0 ? addContentSpecNodes(0, length - 1) : addUniqueLeafNode(null);
/* 196 */     this.fDepth -= 1;
/* 197 */     addToCurrentGroup(group);
/*     */   }
/*     */ 
/*     */   public final void endDTD(Augmentations augs)
/*     */     throws XNIException
/*     */   {
/* 208 */     super.endDTD(augs);
/* 209 */     this.fOpStack = null;
/* 210 */     this.fGroupIndexStack = ((int[][])null);
/* 211 */     this.fGroupIndexStackSizes = null;
/*     */   }
/*     */ 
/*     */   protected final void addContentSpecToElement(XMLElementDecl elementDecl)
/*     */   {
/* 222 */     int contentSpec = this.fGroupIndexStackSizes[0] > 0 ? this.fGroupIndexStack[0][0] : -1;
/* 223 */     setContentSpecIndex(this.fCurrentElementIndex, contentSpec);
/*     */   }
/*     */ 
/*     */   private int addContentSpecNodes(int begin, int end)
/*     */   {
/* 234 */     if (begin == end) {
/* 235 */       return this.fGroupIndexStack[this.fDepth][begin];
/*     */     }
/* 237 */     int middle = begin + end >>> 1;
/* 238 */     return addContentSpecNode(this.fOpStack[this.fDepth], addContentSpecNodes(begin, middle), addContentSpecNodes(middle + 1, end));
/*     */   }
/*     */ 
/*     */   private void initializeContentModelStacks()
/*     */   {
/* 247 */     if (this.fOpStack == null) {
/* 248 */       this.fOpStack = new short[8];
/* 249 */       this.fGroupIndexStack = new int[8][];
/* 250 */       this.fGroupIndexStackSizes = new int[8];
/*     */     }
/* 252 */     else if (this.fDepth == this.fOpStack.length) {
/* 253 */       short[] newOpStack = new short[this.fDepth * 2];
/* 254 */       System.arraycopy(this.fOpStack, 0, newOpStack, 0, this.fDepth);
/* 255 */       this.fOpStack = newOpStack;
/* 256 */       int[][] newGroupIndexStack = new int[this.fDepth * 2][];
/* 257 */       System.arraycopy(this.fGroupIndexStack, 0, newGroupIndexStack, 0, this.fDepth);
/* 258 */       this.fGroupIndexStack = newGroupIndexStack;
/* 259 */       int[] newGroupIndexStackLengths = new int[this.fDepth * 2];
/* 260 */       System.arraycopy(this.fGroupIndexStackSizes, 0, newGroupIndexStackLengths, 0, this.fDepth);
/* 261 */       this.fGroupIndexStackSizes = newGroupIndexStackLengths;
/*     */     }
/* 263 */     this.fOpStack[this.fDepth] = -1;
/* 264 */     this.fGroupIndexStackSizes[this.fDepth] = 0;
/*     */   }
/*     */ 
/*     */   private void addToCurrentGroup(int contentSpec)
/*     */   {
/* 273 */     int[] currentGroup = this.fGroupIndexStack[this.fDepth];
/*     */     int tmp18_15 = this.fDepth;
/*     */     int[] tmp18_11 = this.fGroupIndexStackSizes;
/*     */     int tmp20_19 = tmp18_11[tmp18_15]; tmp18_11[tmp18_15] = (tmp20_19 + 1); int length = tmp20_19;
/* 275 */     if (currentGroup == null) {
/* 276 */       currentGroup = new int[8];
/* 277 */       this.fGroupIndexStack[this.fDepth] = currentGroup;
/*     */     }
/* 279 */     else if (length == currentGroup.length) {
/* 280 */       int[] newGroup = new int[currentGroup.length * 2];
/* 281 */       System.arraycopy(currentGroup, 0, newGroup, 0, currentGroup.length);
/* 282 */       currentGroup = newGroup;
/* 283 */       this.fGroupIndexStack[this.fDepth] = currentGroup;
/*     */     }
/* 285 */     currentGroup[length] = contentSpec;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dtd.BalancedDTDGrammar
 * JD-Core Version:    0.6.2
 */