/*     */ package com.sun.org.apache.xerces.internal.impl.xs.identity;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.xpath.XPath;
/*     */ import com.sun.org.apache.xerces.internal.impl.xpath.XPath.Axis;
/*     */ import com.sun.org.apache.xerces.internal.impl.xpath.XPath.LocationPath;
/*     */ import com.sun.org.apache.xerces.internal.impl.xpath.XPath.NodeTest;
/*     */ import com.sun.org.apache.xerces.internal.impl.xpath.XPath.Step;
/*     */ import com.sun.org.apache.xerces.internal.util.IntStack;
/*     */ import com.sun.org.apache.xerces.internal.xni.Augmentations;
/*     */ import com.sun.org.apache.xerces.internal.xni.QName;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLAttributes;
/*     */ import com.sun.org.apache.xerces.internal.xs.AttributePSVI;
/*     */ import com.sun.org.apache.xerces.internal.xs.ShortList;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;
/*     */ 
/*     */ public class XPathMatcher
/*     */ {
/*     */   protected static final boolean DEBUG_ALL = false;
/*     */   protected static final boolean DEBUG_METHODS = false;
/*     */   protected static final boolean DEBUG_METHODS2 = false;
/*     */   protected static final boolean DEBUG_METHODS3 = false;
/*     */   protected static final boolean DEBUG_MATCH = false;
/*     */   protected static final boolean DEBUG_STACK = false;
/*     */   protected static final boolean DEBUG_ANY = false;
/*     */   protected static final int MATCHED = 1;
/*     */   protected static final int MATCHED_ATTRIBUTE = 3;
/*     */   protected static final int MATCHED_DESCENDANT = 5;
/*     */   protected static final int MATCHED_DESCENDANT_PREVIOUS = 13;
/*     */   private XPath.LocationPath[] fLocationPaths;
/*     */   private int[] fMatched;
/*     */   protected Object fMatchedString;
/*     */   private IntStack[] fStepIndexes;
/*     */   private int[] fCurrentStep;
/*     */   private int[] fNoMatchDepth;
/* 111 */   final QName fQName = new QName();
/*     */ 
/*     */   public XPathMatcher(XPath xpath)
/*     */   {
/* 125 */     this.fLocationPaths = xpath.getLocationPaths();
/* 126 */     this.fStepIndexes = new IntStack[this.fLocationPaths.length];
/* 127 */     for (int i = 0; i < this.fStepIndexes.length; i++) this.fStepIndexes[i] = new IntStack();
/* 128 */     this.fCurrentStep = new int[this.fLocationPaths.length];
/* 129 */     this.fNoMatchDepth = new int[this.fLocationPaths.length];
/* 130 */     this.fMatched = new int[this.fLocationPaths.length];
/*     */   }
/*     */ 
/*     */   public boolean isMatched()
/*     */   {
/* 143 */     for (int i = 0; i < this.fLocationPaths.length; i++)
/* 144 */       if (((this.fMatched[i] & 0x1) == 1) && ((this.fMatched[i] & 0xD) != 13) && ((this.fNoMatchDepth[i] == 0) || ((this.fMatched[i] & 0x5) == 5)))
/*     */       {
/* 148 */         return true;
/*     */       }
/* 150 */     return false;
/*     */   }
/*     */ 
/*     */   protected void handleContent(XSTypeDefinition type, boolean nillable, Object value, short valueType, ShortList itemValueType)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void matched(Object actualValue, short valueType, ShortList itemValueType, boolean isNil)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void startDocumentFragment()
/*     */   {
/* 187 */     this.fMatchedString = null;
/* 188 */     for (int i = 0; i < this.fLocationPaths.length; i++) {
/* 189 */       this.fStepIndexes[i].clear();
/* 190 */       this.fCurrentStep[i] = 0;
/* 191 */       this.fNoMatchDepth[i] = 0;
/* 192 */       this.fMatched[i] = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void startElement(QName element, XMLAttributes attributes)
/*     */   {
/* 216 */     for (int i = 0; i < this.fLocationPaths.length; i++)
/*     */     {
/* 218 */       int startStep = this.fCurrentStep[i];
/* 219 */       this.fStepIndexes[i].push(startStep);
/*     */ 
/* 222 */       if (((this.fMatched[i] & 0x5) == 1) || (this.fNoMatchDepth[i] > 0)) {
/* 223 */         this.fNoMatchDepth[i] += 1;
/*     */       }
/*     */       else {
/* 226 */         if ((this.fMatched[i] & 0x5) == 5) {
/* 227 */           this.fMatched[i] = 13;
/*     */         }
/*     */ 
/* 235 */         XPath.Step[] steps = this.fLocationPaths[i].steps;
/* 236 */         while ((this.fCurrentStep[i] < steps.length) && (steps[this.fCurrentStep[i]].axis.type == 3))
/*     */         {
/* 242 */           this.fCurrentStep[i] += 1;
/*     */         }
/* 244 */         if (this.fCurrentStep[i] == steps.length)
/*     */         {
/* 248 */           this.fMatched[i] = 1;
/*     */         }
/*     */         else
/*     */         {
/* 256 */           int descendantStep = this.fCurrentStep[i];
/* 257 */           while ((this.fCurrentStep[i] < steps.length) && (steps[this.fCurrentStep[i]].axis.type == 4))
/*     */           {
/* 262 */             this.fCurrentStep[i] += 1;
/*     */           }
/* 264 */           boolean sawDescendant = this.fCurrentStep[i] > descendantStep;
/* 265 */           if (this.fCurrentStep[i] == steps.length)
/*     */           {
/* 269 */             this.fNoMatchDepth[i] += 1;
/*     */           }
/* 277 */           else if (((this.fCurrentStep[i] == startStep) || (this.fCurrentStep[i] > descendantStep)) && (steps[this.fCurrentStep[i]].axis.type == 1))
/*     */           {
/* 279 */             XPath.Step step = steps[this.fCurrentStep[i]];
/* 280 */             XPath.NodeTest nodeTest = step.nodeTest;
/*     */ 
/* 284 */             if ((nodeTest.type == 1) && 
/* 285 */               (!nodeTest.name.equals(element))) {
/* 286 */               if (this.fCurrentStep[i] > descendantStep) {
/* 287 */                 this.fCurrentStep[i] = descendantStep;
/*     */               }
/*     */               else {
/* 290 */                 this.fNoMatchDepth[i] += 1;
/*     */               }
/*     */ 
/*     */             }
/*     */             else
/*     */             {
/* 297 */               this.fCurrentStep[i] += 1;
/*     */             }
/*     */ 
/*     */           }
/* 302 */           else if (this.fCurrentStep[i] == steps.length) {
/* 303 */             if (sawDescendant) {
/* 304 */               this.fCurrentStep[i] = descendantStep;
/* 305 */               this.fMatched[i] = 5;
/*     */             } else {
/* 307 */               this.fMatched[i] = 1;
/*     */             }
/*     */ 
/*     */           }
/* 313 */           else if ((this.fCurrentStep[i] < steps.length) && (steps[this.fCurrentStep[i]].axis.type == 2))
/*     */           {
/* 318 */             int attrCount = attributes.getLength();
/* 319 */             if (attrCount > 0) {
/* 320 */               XPath.NodeTest nodeTest = steps[this.fCurrentStep[i]].nodeTest;
/*     */ 
/* 322 */               for (int aIndex = 0; aIndex < attrCount; aIndex++) {
/* 323 */                 attributes.getName(aIndex, this.fQName);
/* 324 */                 if ((nodeTest.type != 1) || (nodeTest.name.equals(this.fQName)))
/*     */                 {
/* 326 */                   this.fCurrentStep[i] += 1;
/* 327 */                   if (this.fCurrentStep[i] != steps.length) break;
/* 328 */                   this.fMatched[i] = 3;
/* 329 */                   int j = 0;
/* 330 */                   while ((j < i) && ((this.fMatched[j] & 0x1) != 1)) j++;
/* 331 */                   if (j == i) {
/* 332 */                     AttributePSVI attrPSVI = (AttributePSVI)attributes.getAugmentations(aIndex).getItem("ATTRIBUTE_PSVI");
/* 333 */                     this.fMatchedString = attrPSVI.getActualNormalizedValue();
/* 334 */                     matched(this.fMatchedString, attrPSVI.getActualNormalizedValueType(), attrPSVI.getItemValueTypes(), false);
/*     */                   }
/* 336 */                   break;
/*     */                 }
/*     */               }
/*     */             }
/*     */ 
/* 341 */             if ((this.fMatched[i] & 0x1) != 1)
/* 342 */               if (this.fCurrentStep[i] > descendantStep) {
/* 343 */                 this.fCurrentStep[i] = descendantStep;
/*     */               }
/*     */               else
/* 346 */                 this.fNoMatchDepth[i] += 1;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void endElement(QName element, XSTypeDefinition type, boolean nillable, Object value, short valueType, ShortList itemValueType)
/*     */   {
/* 381 */     for (int i = 0; i < this.fLocationPaths.length; i++)
/*     */     {
/* 383 */       this.fCurrentStep[i] = this.fStepIndexes[i].pop();
/*     */ 
/* 386 */       if (this.fNoMatchDepth[i] > 0) {
/* 387 */         this.fNoMatchDepth[i] -= 1;
/*     */       }
/*     */       else
/*     */       {
/* 392 */         int j = 0;
/* 393 */         while ((j < i) && ((this.fMatched[j] & 0x1) != 1)) j++;
/* 394 */         if ((j >= i) && (this.fMatched[j] != 0) && ((this.fMatched[j] & 0x3) != 3))
/*     */         {
/* 402 */           handleContent(type, nillable, value, valueType, itemValueType);
/* 403 */           this.fMatched[i] = 0;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 422 */     StringBuffer str = new StringBuffer();
/* 423 */     String s = super.toString();
/* 424 */     int index2 = s.lastIndexOf('.');
/* 425 */     if (index2 != -1) {
/* 426 */       s = s.substring(index2 + 1);
/*     */     }
/* 428 */     str.append(s);
/* 429 */     for (int i = 0; i < this.fLocationPaths.length; i++) {
/* 430 */       str.append('[');
/* 431 */       XPath.Step[] steps = this.fLocationPaths[i].steps;
/* 432 */       for (int j = 0; j < steps.length; j++) {
/* 433 */         if (j == this.fCurrentStep[i]) {
/* 434 */           str.append('^');
/*     */         }
/* 436 */         str.append(steps[j].toString());
/* 437 */         if (j < steps.length - 1) {
/* 438 */           str.append('/');
/*     */         }
/*     */       }
/* 441 */       if (this.fCurrentStep[i] == steps.length) {
/* 442 */         str.append('^');
/*     */       }
/* 444 */       str.append(']');
/* 445 */       str.append(',');
/*     */     }
/* 447 */     return str.toString();
/*     */   }
/*     */ 
/*     */   private String normalize(String s)
/*     */   {
/* 456 */     StringBuffer str = new StringBuffer();
/* 457 */     int length = s.length();
/* 458 */     for (int i = 0; i < length; i++) {
/* 459 */       char c = s.charAt(i);
/* 460 */       switch (c) {
/*     */       case '\n':
/* 462 */         str.append("\\n");
/* 463 */         break;
/*     */       default:
/* 466 */         str.append(c);
/*     */       }
/*     */     }
/*     */ 
/* 470 */     return str.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.identity.XPathMatcher
 * JD-Core Version:    0.6.2
 */