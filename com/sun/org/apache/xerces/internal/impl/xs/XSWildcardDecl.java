/*     */ package com.sun.org.apache.xerces.internal.impl.xs;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.util.StringListImpl;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.util.XSObjectListImpl;
/*     */ import com.sun.org.apache.xerces.internal.xs.StringList;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSAnnotation;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSNamespaceItem;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSObjectList;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSWildcard;
/*     */ 
/*     */ public class XSWildcardDecl
/*     */   implements XSWildcard
/*     */ {
/*  45 */   public static final String ABSENT = null;
/*     */ 
/*  48 */   public short fType = 1;
/*     */ 
/*  50 */   public short fProcessContents = 1;
/*     */   public String[] fNamespaceList;
/*  57 */   public XSObjectList fAnnotations = null;
/*     */ 
/* 479 */   private String fDescription = null;
/*     */ 
/*     */   public boolean allowNamespace(String namespace)
/*     */   {
/*  83 */     if (this.fType == 1) {
/*  84 */       return true;
/*     */     }
/*     */ 
/*  92 */     if (this.fType == 2) {
/*  93 */       boolean found = false;
/*  94 */       int listNum = this.fNamespaceList.length;
/*  95 */       for (int i = 0; (i < listNum) && (!found); i++) {
/*  96 */         if (namespace == this.fNamespaceList[i]) {
/*  97 */           found = true;
/*     */         }
/*     */       }
/* 100 */       if (!found) {
/* 101 */         return true;
/*     */       }
/*     */     }
/*     */ 
/* 105 */     if (this.fType == 3) {
/* 106 */       int listNum = this.fNamespaceList.length;
/* 107 */       for (int i = 0; i < listNum; i++) {
/* 108 */         if (namespace == this.fNamespaceList[i]) {
/* 109 */           return true;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 114 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isSubsetOf(XSWildcardDecl superWildcard)
/*     */   {
/* 122 */     if (superWildcard == null) {
/* 123 */       return false;
/*     */     }
/*     */ 
/* 129 */     if (superWildcard.fType == 1) {
/* 130 */       return true;
/*     */     }
/*     */ 
/* 138 */     if ((this.fType == 2) && 
/* 139 */       (superWildcard.fType == 2) && (this.fNamespaceList[0] == superWildcard.fNamespaceList[0]))
/*     */     {
/* 141 */       return true;
/*     */     }
/*     */ 
/* 155 */     if (this.fType == 3) {
/* 156 */       if ((superWildcard.fType == 3) && (subset2sets(this.fNamespaceList, superWildcard.fNamespaceList)))
/*     */       {
/* 158 */         return true;
/*     */       }
/*     */ 
/* 161 */       if ((superWildcard.fType == 2) && (!elementInSet(superWildcard.fNamespaceList[0], this.fNamespaceList)) && (!elementInSet(ABSENT, this.fNamespaceList)))
/*     */       {
/* 164 */         return true;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 169 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean weakerProcessContents(XSWildcardDecl superWildcard)
/*     */   {
/* 177 */     return ((this.fProcessContents == 3) && (superWildcard.fProcessContents == 1)) || ((this.fProcessContents == 2) && (superWildcard.fProcessContents != 2));
/*     */   }
/*     */ 
/*     */   public XSWildcardDecl performUnionWith(XSWildcardDecl wildcard, short processContents)
/*     */   {
/* 189 */     if (wildcard == null) {
/* 190 */       return null;
/*     */     }
/*     */ 
/* 196 */     XSWildcardDecl unionWildcard = new XSWildcardDecl();
/* 197 */     unionWildcard.fProcessContents = processContents;
/*     */ 
/* 200 */     if (areSame(wildcard)) {
/* 201 */       unionWildcard.fType = this.fType;
/* 202 */       unionWildcard.fNamespaceList = this.fNamespaceList;
/*     */     }
/* 206 */     else if ((this.fType == 1) || (wildcard.fType == 1)) {
/* 207 */       unionWildcard.fType = 1;
/*     */     }
/* 212 */     else if ((this.fType == 3) && (wildcard.fType == 3)) {
/* 213 */       unionWildcard.fType = 3;
/* 214 */       unionWildcard.fNamespaceList = union2sets(this.fNamespaceList, wildcard.fNamespaceList);
/*     */     }
/* 223 */     else if ((this.fType == 2) && (wildcard.fType == 2)) {
/* 224 */       unionWildcard.fType = 2;
/* 225 */       unionWildcard.fNamespaceList = new String[2];
/* 226 */       unionWildcard.fNamespaceList[0] = ABSENT;
/* 227 */       unionWildcard.fNamespaceList[1] = ABSENT;
/*     */     }
/* 252 */     else if (((this.fType == 2) && (wildcard.fType == 3)) || ((this.fType == 3) && (wildcard.fType == 2)))
/*     */     {
/* 254 */       String[] other = null;
/* 255 */       String[] list = null;
/*     */ 
/* 257 */       if (this.fType == 2) {
/* 258 */         other = this.fNamespaceList;
/* 259 */         list = wildcard.fNamespaceList;
/*     */       }
/*     */       else {
/* 262 */         other = wildcard.fNamespaceList;
/* 263 */         list = this.fNamespaceList;
/*     */       }
/*     */ 
/* 266 */       boolean foundAbsent = elementInSet(ABSENT, list);
/*     */ 
/* 268 */       if (other[0] != ABSENT) {
/* 269 */         boolean foundNS = elementInSet(other[0], list);
/* 270 */         if ((foundNS) && (foundAbsent)) {
/* 271 */           unionWildcard.fType = 1;
/* 272 */         } else if ((foundNS) && (!foundAbsent)) {
/* 273 */           unionWildcard.fType = 2;
/* 274 */           unionWildcard.fNamespaceList = new String[2];
/* 275 */           unionWildcard.fNamespaceList[0] = ABSENT;
/* 276 */           unionWildcard.fNamespaceList[1] = ABSENT; } else {
/* 277 */           if ((!foundNS) && (foundAbsent)) {
/* 278 */             return null;
/*     */           }
/* 280 */           unionWildcard.fType = 2;
/* 281 */           unionWildcard.fNamespaceList = other;
/*     */         }
/*     */       }
/* 284 */       else if (foundAbsent) {
/* 285 */         unionWildcard.fType = 1;
/*     */       } else {
/* 287 */         unionWildcard.fType = 2;
/* 288 */         unionWildcard.fNamespaceList = other;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 293 */     return unionWildcard;
/*     */   }
/*     */ 
/*     */   public XSWildcardDecl performIntersectionWith(XSWildcardDecl wildcard, short processContents)
/*     */   {
/* 303 */     if (wildcard == null) {
/* 304 */       return null;
/*     */     }
/*     */ 
/* 310 */     XSWildcardDecl intersectWildcard = new XSWildcardDecl();
/* 311 */     intersectWildcard.fProcessContents = processContents;
/*     */ 
/* 314 */     if (areSame(wildcard)) {
/* 315 */       intersectWildcard.fType = this.fType;
/* 316 */       intersectWildcard.fNamespaceList = this.fNamespaceList;
/*     */     }
/* 320 */     else if ((this.fType == 1) || (wildcard.fType == 1))
/*     */     {
/* 322 */       XSWildcardDecl other = this;
/*     */ 
/* 324 */       if (this.fType == 1) {
/* 325 */         other = wildcard;
/*     */       }
/* 327 */       intersectWildcard.fType = other.fType;
/* 328 */       intersectWildcard.fNamespaceList = other.fNamespaceList;
/*     */     }
/* 339 */     else if (((this.fType == 2) && (wildcard.fType == 3)) || ((this.fType == 3) && (wildcard.fType == 2)))
/*     */     {
/* 341 */       String[] list = null;
/* 342 */       String[] other = null;
/*     */ 
/* 344 */       if (this.fType == 2) {
/* 345 */         other = this.fNamespaceList;
/* 346 */         list = wildcard.fNamespaceList;
/*     */       }
/*     */       else {
/* 349 */         other = wildcard.fNamespaceList;
/* 350 */         list = this.fNamespaceList;
/*     */       }
/*     */ 
/* 353 */       int listSize = list.length;
/* 354 */       String[] intersect = new String[listSize];
/* 355 */       int newSize = 0;
/* 356 */       for (int i = 0; i < listSize; i++) {
/* 357 */         if ((list[i] != other[0]) && (list[i] != ABSENT)) {
/* 358 */           intersect[(newSize++)] = list[i];
/*     */         }
/*     */       }
/* 361 */       intersectWildcard.fType = 3;
/* 362 */       intersectWildcard.fNamespaceList = new String[newSize];
/* 363 */       System.arraycopy(intersect, 0, intersectWildcard.fNamespaceList, 0, newSize);
/*     */     }
/* 368 */     else if ((this.fType == 3) && (wildcard.fType == 3)) {
/* 369 */       intersectWildcard.fType = 3;
/* 370 */       intersectWildcard.fNamespaceList = intersect2sets(this.fNamespaceList, wildcard.fNamespaceList);
/*     */     }
/* 381 */     else if ((this.fType == 2) && (wildcard.fType == 2)) {
/* 382 */       if ((this.fNamespaceList[0] != ABSENT) && (wildcard.fNamespaceList[0] != ABSENT)) {
/* 383 */         return null;
/*     */       }
/* 385 */       XSWildcardDecl other = this;
/* 386 */       if (this.fNamespaceList[0] == ABSENT) {
/* 387 */         other = wildcard;
/*     */       }
/* 389 */       intersectWildcard.fType = other.fType;
/* 390 */       intersectWildcard.fNamespaceList = other.fNamespaceList;
/*     */     }
/*     */ 
/* 393 */     return intersectWildcard;
/*     */   }
/*     */ 
/*     */   private boolean areSame(XSWildcardDecl wildcard)
/*     */   {
/* 398 */     if (this.fType == wildcard.fType)
/*     */     {
/* 400 */       if (this.fType == 1) {
/* 401 */         return true;
/*     */       }
/*     */ 
/* 406 */       if (this.fType == 2) {
/* 407 */         return this.fNamespaceList[0] == wildcard.fNamespaceList[0];
/*     */       }
/*     */ 
/* 412 */       if (this.fNamespaceList.length == wildcard.fNamespaceList.length) {
/* 413 */         for (int i = 0; i < this.fNamespaceList.length; i++) {
/* 414 */           if (!elementInSet(this.fNamespaceList[i], wildcard.fNamespaceList))
/* 415 */             return false;
/*     */         }
/* 417 */         return true;
/*     */       }
/*     */     }
/*     */ 
/* 421 */     return false;
/*     */   }
/*     */ 
/*     */   String[] intersect2sets(String[] one, String[] theOther) {
/* 425 */     String[] result = new String[Math.min(one.length, theOther.length)];
/*     */ 
/* 428 */     int count = 0;
/* 429 */     for (int i = 0; i < one.length; i++) {
/* 430 */       if (elementInSet(one[i], theOther)) {
/* 431 */         result[(count++)] = one[i];
/*     */       }
/*     */     }
/* 434 */     String[] result2 = new String[count];
/* 435 */     System.arraycopy(result, 0, result2, 0, count);
/*     */ 
/* 437 */     return result2;
/*     */   }
/*     */ 
/*     */   String[] union2sets(String[] one, String[] theOther) {
/* 441 */     String[] result1 = new String[one.length];
/*     */ 
/* 444 */     int count = 0;
/* 445 */     for (int i = 0; i < one.length; i++) {
/* 446 */       if (!elementInSet(one[i], theOther)) {
/* 447 */         result1[(count++)] = one[i];
/*     */       }
/*     */     }
/* 450 */     String[] result2 = new String[count + theOther.length];
/* 451 */     System.arraycopy(result1, 0, result2, 0, count);
/* 452 */     System.arraycopy(theOther, 0, result2, count, theOther.length);
/*     */ 
/* 454 */     return result2;
/*     */   }
/*     */ 
/*     */   boolean subset2sets(String[] subSet, String[] superSet) {
/* 458 */     for (int i = 0; i < subSet.length; i++) {
/* 459 */       if (!elementInSet(subSet[i], superSet)) {
/* 460 */         return false;
/*     */       }
/*     */     }
/* 463 */     return true;
/*     */   }
/*     */ 
/*     */   boolean elementInSet(String ele, String[] set) {
/* 467 */     boolean found = false;
/* 468 */     for (int i = 0; (i < set.length) && (!found); i++) {
/* 469 */       if (ele == set[i]) {
/* 470 */         found = true;
/*     */       }
/*     */     }
/* 473 */     return found;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 481 */     if (this.fDescription == null) {
/* 482 */       StringBuffer buffer = new StringBuffer();
/* 483 */       buffer.append("WC[");
/* 484 */       switch (this.fType) {
/*     */       case 1:
/* 486 */         buffer.append("##any");
/* 487 */         break;
/*     */       case 2:
/* 489 */         buffer.append("##other");
/* 490 */         buffer.append(":\"");
/* 491 */         if (this.fNamespaceList[0] != null)
/* 492 */           buffer.append(this.fNamespaceList[0]);
/* 493 */         buffer.append("\"");
/* 494 */         break;
/*     */       case 3:
/* 496 */         if (this.fNamespaceList.length != 0)
/*     */         {
/* 498 */           buffer.append("\"");
/* 499 */           if (this.fNamespaceList[0] != null)
/* 500 */             buffer.append(this.fNamespaceList[0]);
/* 501 */           buffer.append("\"");
/* 502 */           for (int i = 1; i < this.fNamespaceList.length; i++) {
/* 503 */             buffer.append(",\"");
/* 504 */             if (this.fNamespaceList[i] != null)
/* 505 */               buffer.append(this.fNamespaceList[i]);
/* 506 */             buffer.append("\"");
/*     */           }
/*     */         }
/*     */         break;
/*     */       }
/* 510 */       buffer.append(']');
/* 511 */       this.fDescription = buffer.toString();
/*     */     }
/*     */ 
/* 514 */     return this.fDescription;
/*     */   }
/*     */ 
/*     */   public short getType()
/*     */   {
/* 521 */     return 9;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 529 */     return null;
/*     */   }
/*     */ 
/*     */   public String getNamespace()
/*     */   {
/* 538 */     return null;
/*     */   }
/*     */ 
/*     */   public short getConstraintType()
/*     */   {
/* 545 */     return this.fType;
/*     */   }
/*     */ 
/*     */   public StringList getNsConstraintList()
/*     */   {
/* 555 */     return new StringListImpl(this.fNamespaceList, this.fNamespaceList == null ? 0 : this.fNamespaceList.length);
/*     */   }
/*     */ 
/*     */   public short getProcessContents()
/*     */   {
/* 563 */     return this.fProcessContents;
/*     */   }
/*     */ 
/*     */   public String getProcessContentsAsString()
/*     */   {
/* 570 */     switch (this.fProcessContents) { case 2:
/* 571 */       return "skip";
/*     */     case 3:
/* 572 */       return "lax";
/*     */     case 1:
/* 573 */       return "strict"; }
/* 574 */     return "invalid value";
/*     */   }
/*     */ 
/*     */   public XSAnnotation getAnnotation()
/*     */   {
/* 582 */     return this.fAnnotations != null ? (XSAnnotation)this.fAnnotations.item(0) : null;
/*     */   }
/*     */ 
/*     */   public XSObjectList getAnnotations()
/*     */   {
/* 589 */     return this.fAnnotations != null ? this.fAnnotations : XSObjectListImpl.EMPTY_LIST;
/*     */   }
/*     */ 
/*     */   public XSNamespaceItem getNamespaceItem()
/*     */   {
/* 596 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.XSWildcardDecl
 * JD-Core Version:    0.6.2
 */