/*     */ package javax.swing.text;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Stack;
/*     */ 
/*     */ public class ElementIterator
/*     */   implements Cloneable
/*     */ {
/*     */   private Element root;
/*  75 */   private Stack<StackItem> elementStack = null;
/*     */ 
/*     */   public ElementIterator(Document paramDocument)
/*     */   {
/* 127 */     this.root = paramDocument.getDefaultRootElement();
/*     */   }
/*     */ 
/*     */   public ElementIterator(Element paramElement)
/*     */   {
/* 137 */     this.root = paramElement;
/*     */   }
/*     */ 
/*     */   public synchronized Object clone()
/*     */   {
/*     */     try
/*     */     {
/* 149 */       ElementIterator localElementIterator = new ElementIterator(this.root);
/* 150 */       if (this.elementStack != null) {
/* 151 */         localElementIterator.elementStack = new Stack();
/* 152 */         for (int i = 0; i < this.elementStack.size(); i++) {
/* 153 */           StackItem localStackItem1 = (StackItem)this.elementStack.elementAt(i);
/* 154 */           StackItem localStackItem2 = (StackItem)localStackItem1.clone();
/* 155 */           localElementIterator.elementStack.push(localStackItem2);
/*     */         }
/*     */       }
/* 158 */       return localElementIterator; } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*     */     }
/* 160 */     throw new InternalError();
/*     */   }
/*     */ 
/*     */   public Element first()
/*     */   {
/* 172 */     if (this.root == null) {
/* 173 */       return null;
/*     */     }
/*     */ 
/* 176 */     this.elementStack = new Stack();
/* 177 */     if (this.root.getElementCount() != 0) {
/* 178 */       this.elementStack.push(new StackItem(this.root, null));
/*     */     }
/* 180 */     return this.root;
/*     */   }
/*     */ 
/*     */   public int depth()
/*     */   {
/* 189 */     if (this.elementStack == null) {
/* 190 */       return 0;
/*     */     }
/* 192 */     return this.elementStack.size();
/*     */   }
/*     */ 
/*     */   public Element current()
/*     */   {
/* 204 */     if (this.elementStack == null) {
/* 205 */       return first();
/*     */     }
/*     */ 
/* 211 */     if (!this.elementStack.empty()) {
/* 212 */       StackItem localStackItem = (StackItem)this.elementStack.peek();
/* 213 */       Element localElement = localStackItem.getElement();
/* 214 */       int i = localStackItem.getIndex();
/*     */ 
/* 216 */       if (i == -1) {
/* 217 */         return localElement;
/*     */       }
/*     */ 
/* 220 */       return localElement.getElement(i);
/*     */     }
/* 222 */     return null;
/*     */   }
/*     */ 
/*     */   public Element next()
/*     */   {
/* 239 */     if (this.elementStack == null) {
/* 240 */       return first();
/*     */     }
/*     */ 
/* 244 */     if (this.elementStack.isEmpty()) {
/* 245 */       return null;
/*     */     }
/*     */ 
/* 250 */     StackItem localStackItem = (StackItem)this.elementStack.peek();
/* 251 */     Element localElement = localStackItem.getElement();
/* 252 */     int i = localStackItem.getIndex();
/*     */     Object localObject;
/* 254 */     if (i + 1 < localElement.getElementCount()) {
/* 255 */       localObject = localElement.getElement(i + 1);
/* 256 */       if (((Element)localObject).isLeaf())
/*     */       {
/* 260 */         localStackItem.incrementIndex();
/*     */       }
/*     */       else
/*     */       {
/* 265 */         this.elementStack.push(new StackItem((Element)localObject, null));
/*     */       }
/* 267 */       return localObject;
/*     */     }
/*     */ 
/* 271 */     this.elementStack.pop();
/* 272 */     if (!this.elementStack.isEmpty())
/*     */     {
/* 275 */       localObject = (StackItem)this.elementStack.peek();
/* 276 */       ((StackItem)localObject).incrementIndex();
/*     */ 
/* 279 */       return next();
/*     */     }
/*     */ 
/* 282 */     return null;
/*     */   }
/*     */ 
/*     */   public Element previous()
/*     */   {
/*     */     int i;
/* 297 */     if ((this.elementStack == null) || ((i = this.elementStack.size()) == 0)) {
/* 298 */       return null;
/*     */     }
/*     */ 
/* 303 */     StackItem localStackItem1 = (StackItem)this.elementStack.peek();
/* 304 */     Element localElement = localStackItem1.getElement();
/* 305 */     int j = localStackItem1.getIndex();
/*     */ 
/* 307 */     if (j > 0)
/*     */     {
/* 309 */       return getDeepestLeaf(localElement.getElement(--j));
/* 310 */     }if (j == 0)
/*     */     {
/* 314 */       return localElement;
/* 315 */     }if (j == -1) {
/* 316 */       if (i == 1)
/*     */       {
/* 318 */         return null;
/*     */       }
/*     */ 
/* 323 */       StackItem localStackItem2 = (StackItem)this.elementStack.pop();
/* 324 */       localStackItem1 = (StackItem)this.elementStack.peek();
/*     */ 
/* 327 */       this.elementStack.push(localStackItem2);
/* 328 */       localElement = localStackItem1.getElement();
/* 329 */       j = localStackItem1.getIndex();
/* 330 */       return j == -1 ? localElement : getDeepestLeaf(localElement.getElement(j));
/*     */     }
/*     */ 
/* 334 */     return null;
/*     */   }
/*     */ 
/*     */   private Element getDeepestLeaf(Element paramElement)
/*     */   {
/* 342 */     if (paramElement.isLeaf()) {
/* 343 */       return paramElement;
/*     */     }
/* 345 */     int i = paramElement.getElementCount();
/* 346 */     if (i == 0) {
/* 347 */       return paramElement;
/*     */     }
/* 349 */     return getDeepestLeaf(paramElement.getElement(i - 1));
/*     */   }
/*     */ 
/*     */   private void dumpTree()
/*     */   {
/*     */     Element localElement;
/* 360 */     while ((localElement = next()) != null) {
/* 361 */       System.out.println("elem: " + localElement.getName());
/* 362 */       AttributeSet localAttributeSet = localElement.getAttributes();
/* 363 */       String str = "";
/* 364 */       Enumeration localEnumeration = localAttributeSet.getAttributeNames();
/* 365 */       while (localEnumeration.hasMoreElements()) {
/* 366 */         Object localObject1 = localEnumeration.nextElement();
/* 367 */         Object localObject2 = localAttributeSet.getAttribute(localObject1);
/* 368 */         if ((localObject2 instanceof AttributeSet))
/*     */         {
/* 370 */           str = str + localObject1 + "=**AttributeSet** ";
/*     */         }
/* 372 */         else str = str + localObject1 + "=" + localObject2 + " ";
/*     */       }
/*     */ 
/* 375 */       System.out.println("attributes: " + str);
/*     */     }
/*     */   }
/*     */ 
/*     */   private class StackItem
/*     */     implements Cloneable
/*     */   {
/*     */     Element item;
/*     */     int childIndex;
/*     */ 
/*     */     private StackItem(Element arg2)
/*     */     {
/*     */       Object localObject;
/*  98 */       this.item = localObject;
/*  99 */       this.childIndex = -1;
/*     */     }
/*     */ 
/*     */     private void incrementIndex() {
/* 103 */       this.childIndex += 1;
/*     */     }
/*     */ 
/*     */     private Element getElement() {
/* 107 */       return this.item;
/*     */     }
/*     */ 
/*     */     private int getIndex() {
/* 111 */       return this.childIndex;
/*     */     }
/*     */ 
/*     */     protected Object clone() throws CloneNotSupportedException {
/* 115 */       return super.clone();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.ElementIterator
 * JD-Core Version:    0.6.2
 */