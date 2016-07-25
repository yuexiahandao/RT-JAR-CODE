/*      */ package java.text;
/*      */ 
/*      */ import java.util.AbstractMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ import java.util.Vector;
/*      */ 
/*      */ public class AttributedString
/*      */ {
/*      */   private static final int ARRAY_SIZE_INCREMENT = 10;
/*      */   String text;
/*      */   int runArraySize;
/*      */   int runCount;
/*      */   int[] runStarts;
/*      */   Vector[] runAttributes;
/*      */   Vector[] runAttributeValues;
/*      */ 
/*      */   AttributedString(AttributedCharacterIterator[] paramArrayOfAttributedCharacterIterator)
/*      */   {
/*   76 */     if (paramArrayOfAttributedCharacterIterator == null) {
/*   77 */       throw new NullPointerException("Iterators must not be null");
/*      */     }
/*   79 */     if (paramArrayOfAttributedCharacterIterator.length == 0) {
/*   80 */       this.text = "";
/*      */     }
/*      */     else
/*      */     {
/*   84 */       StringBuffer localStringBuffer = new StringBuffer();
/*   85 */       for (int i = 0; i < paramArrayOfAttributedCharacterIterator.length; i++) {
/*   86 */         appendContents(localStringBuffer, paramArrayOfAttributedCharacterIterator[i]);
/*      */       }
/*      */ 
/*   89 */       this.text = localStringBuffer.toString();
/*      */ 
/*   91 */       if (this.text.length() > 0)
/*      */       {
/*   94 */         i = 0;
/*   95 */         Object localObject = null;
/*      */ 
/*   97 */         for (int j = 0; j < paramArrayOfAttributedCharacterIterator.length; j++) {
/*   98 */           AttributedCharacterIterator localAttributedCharacterIterator = paramArrayOfAttributedCharacterIterator[j];
/*   99 */           int k = localAttributedCharacterIterator.getBeginIndex();
/*  100 */           int m = localAttributedCharacterIterator.getEndIndex();
/*  101 */           int n = k;
/*      */ 
/*  103 */           while (n < m) {
/*  104 */             localAttributedCharacterIterator.setIndex(n);
/*      */ 
/*  106 */             Map localMap = localAttributedCharacterIterator.getAttributes();
/*      */ 
/*  108 */             if (mapsDiffer((Map)localObject, localMap)) {
/*  109 */               setAttributes(localMap, n - k + i);
/*      */             }
/*  111 */             localObject = localMap;
/*  112 */             n = localAttributedCharacterIterator.getRunLimit();
/*      */           }
/*  114 */           i += m - k;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public AttributedString(String paramString)
/*      */   {
/*  126 */     if (paramString == null) {
/*  127 */       throw new NullPointerException();
/*      */     }
/*  129 */     this.text = paramString;
/*      */   }
/*      */ 
/*      */   public AttributedString(String paramString, Map<? extends AttributedCharacterIterator.Attribute, ?> paramMap)
/*      */   {
/*  145 */     if ((paramString == null) || (paramMap == null)) {
/*  146 */       throw new NullPointerException();
/*      */     }
/*  148 */     this.text = paramString;
/*      */ 
/*  150 */     if (paramString.length() == 0) {
/*  151 */       if (paramMap.isEmpty())
/*  152 */         return;
/*  153 */       throw new IllegalArgumentException("Can't add attribute to 0-length text");
/*      */     }
/*      */ 
/*  156 */     int i = paramMap.size();
/*  157 */     if (i > 0) {
/*  158 */       createRunAttributeDataVectors();
/*  159 */       Vector localVector1 = new Vector(i);
/*  160 */       Vector localVector2 = new Vector(i);
/*  161 */       this.runAttributes[0] = localVector1;
/*  162 */       this.runAttributeValues[0] = localVector2;
/*  163 */       Iterator localIterator = paramMap.entrySet().iterator();
/*  164 */       while (localIterator.hasNext()) {
/*  165 */         Map.Entry localEntry = (Map.Entry)localIterator.next();
/*  166 */         localVector1.addElement(localEntry.getKey());
/*  167 */         localVector2.addElement(localEntry.getValue());
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public AttributedString(AttributedCharacterIterator paramAttributedCharacterIterator)
/*      */   {
/*  182 */     this(paramAttributedCharacterIterator, paramAttributedCharacterIterator.getBeginIndex(), paramAttributedCharacterIterator.getEndIndex(), null);
/*      */   }
/*      */ 
/*      */   public AttributedString(AttributedCharacterIterator paramAttributedCharacterIterator, int paramInt1, int paramInt2)
/*      */   {
/*  205 */     this(paramAttributedCharacterIterator, paramInt1, paramInt2, null);
/*      */   }
/*      */ 
/*      */   public AttributedString(AttributedCharacterIterator paramAttributedCharacterIterator, int paramInt1, int paramInt2, AttributedCharacterIterator.Attribute[] paramArrayOfAttribute)
/*      */   {
/*  234 */     if (paramAttributedCharacterIterator == null) {
/*  235 */       throw new NullPointerException();
/*      */     }
/*      */ 
/*  239 */     int i = paramAttributedCharacterIterator.getBeginIndex();
/*  240 */     int j = paramAttributedCharacterIterator.getEndIndex();
/*  241 */     if ((paramInt1 < i) || (paramInt2 > j) || (paramInt1 > paramInt2)) {
/*  242 */       throw new IllegalArgumentException("Invalid substring range");
/*      */     }
/*      */ 
/*  245 */     StringBuffer localStringBuffer = new StringBuffer();
/*  246 */     paramAttributedCharacterIterator.setIndex(paramInt1);
/*  247 */     for (char c = paramAttributedCharacterIterator.current(); paramAttributedCharacterIterator.getIndex() < paramInt2; c = paramAttributedCharacterIterator.next())
/*  248 */       localStringBuffer.append(c);
/*  249 */     this.text = localStringBuffer.toString();
/*      */ 
/*  251 */     if (paramInt1 == paramInt2) {
/*  252 */       return;
/*      */     }
/*      */ 
/*  255 */     HashSet localHashSet = new HashSet();
/*  256 */     if (paramArrayOfAttribute == null) {
/*  257 */       localHashSet.addAll(paramAttributedCharacterIterator.getAllAttributeKeys());
/*      */     } else {
/*  259 */       for (int k = 0; k < paramArrayOfAttribute.length; k++)
/*  260 */         localHashSet.add(paramArrayOfAttribute[k]);
/*  261 */       localHashSet.retainAll(paramAttributedCharacterIterator.getAllAttributeKeys());
/*      */     }
/*  263 */     if (localHashSet.isEmpty()) {
/*  264 */       return;
/*      */     }
/*      */ 
/*  269 */     Iterator localIterator = localHashSet.iterator();
/*  270 */     while (localIterator.hasNext()) {
/*  271 */       AttributedCharacterIterator.Attribute localAttribute = (AttributedCharacterIterator.Attribute)localIterator.next();
/*  272 */       paramAttributedCharacterIterator.setIndex(i);
/*  273 */       while (paramAttributedCharacterIterator.getIndex() < paramInt2) {
/*  274 */         int m = paramAttributedCharacterIterator.getRunStart(localAttribute);
/*  275 */         int n = paramAttributedCharacterIterator.getRunLimit(localAttribute);
/*  276 */         Object localObject = paramAttributedCharacterIterator.getAttribute(localAttribute);
/*      */ 
/*  278 */         if (localObject != null) {
/*  279 */           if ((localObject instanceof Annotation)) {
/*  280 */             if ((m >= paramInt1) && (n <= paramInt2)) {
/*  281 */               addAttribute(localAttribute, localObject, m - paramInt1, n - paramInt1);
/*      */             }
/*  283 */             else if (n > paramInt2) {
/*  284 */               break;
/*      */             }
/*      */           }
/*      */           else
/*      */           {
/*  289 */             if (m >= paramInt2)
/*      */               break;
/*  291 */             if (n > paramInt1)
/*      */             {
/*  293 */               if (m < paramInt1)
/*  294 */                 m = paramInt1;
/*  295 */               if (n > paramInt2)
/*  296 */                 n = paramInt2;
/*  297 */               if (m != n) {
/*  298 */                 addAttribute(localAttribute, localObject, m - paramInt1, n - paramInt1);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*  303 */         paramAttributedCharacterIterator.setIndex(n);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void addAttribute(AttributedCharacterIterator.Attribute paramAttribute, Object paramObject)
/*      */   {
/*  318 */     if (paramAttribute == null) {
/*  319 */       throw new NullPointerException();
/*      */     }
/*      */ 
/*  322 */     int i = length();
/*  323 */     if (i == 0) {
/*  324 */       throw new IllegalArgumentException("Can't add attribute to 0-length text");
/*      */     }
/*      */ 
/*  327 */     addAttributeImpl(paramAttribute, paramObject, 0, i);
/*      */   }
/*      */ 
/*      */   public void addAttribute(AttributedCharacterIterator.Attribute paramAttribute, Object paramObject, int paramInt1, int paramInt2)
/*      */   {
/*  344 */     if (paramAttribute == null) {
/*  345 */       throw new NullPointerException();
/*      */     }
/*      */ 
/*  348 */     if ((paramInt1 < 0) || (paramInt2 > length()) || (paramInt1 >= paramInt2)) {
/*  349 */       throw new IllegalArgumentException("Invalid substring range");
/*      */     }
/*      */ 
/*  352 */     addAttributeImpl(paramAttribute, paramObject, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void addAttributes(Map<? extends AttributedCharacterIterator.Attribute, ?> paramMap, int paramInt1, int paramInt2)
/*      */   {
/*  371 */     if (paramMap == null) {
/*  372 */       throw new NullPointerException();
/*      */     }
/*      */ 
/*  375 */     if ((paramInt1 < 0) || (paramInt2 > length()) || (paramInt1 > paramInt2)) {
/*  376 */       throw new IllegalArgumentException("Invalid substring range");
/*      */     }
/*  378 */     if (paramInt1 == paramInt2) {
/*  379 */       if (paramMap.isEmpty())
/*  380 */         return;
/*  381 */       throw new IllegalArgumentException("Can't add attribute to 0-length text");
/*      */     }
/*      */ 
/*  385 */     if (this.runCount == 0) {
/*  386 */       createRunAttributeDataVectors();
/*      */     }
/*      */ 
/*  390 */     int i = ensureRunBreak(paramInt1);
/*  391 */     int j = ensureRunBreak(paramInt2);
/*      */ 
/*  393 */     Iterator localIterator = paramMap.entrySet().iterator();
/*  394 */     while (localIterator.hasNext()) {
/*  395 */       Map.Entry localEntry = (Map.Entry)localIterator.next();
/*  396 */       addAttributeRunData((AttributedCharacterIterator.Attribute)localEntry.getKey(), localEntry.getValue(), i, j);
/*      */     }
/*      */   }
/*      */ 
/*      */   private synchronized void addAttributeImpl(AttributedCharacterIterator.Attribute paramAttribute, Object paramObject, int paramInt1, int paramInt2)
/*      */   {
/*  404 */     if (this.runCount == 0) {
/*  405 */       createRunAttributeDataVectors();
/*      */     }
/*      */ 
/*  409 */     int i = ensureRunBreak(paramInt1);
/*  410 */     int j = ensureRunBreak(paramInt2);
/*      */ 
/*  412 */     addAttributeRunData(paramAttribute, paramObject, i, j);
/*      */   }
/*      */ 
/*      */   private final void createRunAttributeDataVectors()
/*      */   {
/*  417 */     int[] arrayOfInt = new int[10];
/*  418 */     Vector[] arrayOfVector1 = new Vector[10];
/*  419 */     Vector[] arrayOfVector2 = new Vector[10];
/*  420 */     this.runStarts = arrayOfInt;
/*  421 */     this.runAttributes = arrayOfVector1;
/*  422 */     this.runAttributeValues = arrayOfVector2;
/*  423 */     this.runArraySize = 10;
/*  424 */     this.runCount = 1;
/*      */   }
/*      */ 
/*      */   private final int ensureRunBreak(int paramInt)
/*      */   {
/*  429 */     return ensureRunBreak(paramInt, true);
/*      */   }
/*      */ 
/*      */   private final int ensureRunBreak(int paramInt, boolean paramBoolean)
/*      */   {
/*  444 */     if (paramInt == length()) {
/*  445 */       return this.runCount;
/*      */     }
/*      */ 
/*  449 */     int i = 0;
/*  450 */     while ((i < this.runCount) && (this.runStarts[i] < paramInt)) {
/*  451 */       i++;
/*      */     }
/*      */ 
/*  455 */     if ((i < this.runCount) && (this.runStarts[i] == paramInt))
/*  456 */       return i;
/*      */     Object localObject2;
/*      */     Object localObject3;
/*  461 */     if (this.runCount == this.runArraySize) {
/*  462 */       int j = this.runArraySize + 10;
/*  463 */       localObject1 = new int[j];
/*  464 */       localObject2 = new Vector[j];
/*  465 */       localObject3 = new Vector[j];
/*  466 */       for (int m = 0; m < this.runArraySize; m++) {
/*  467 */         localObject1[m] = this.runStarts[m];
/*  468 */         localObject2[m] = this.runAttributes[m];
/*  469 */         localObject3[m] = this.runAttributeValues[m];
/*      */       }
/*  471 */       this.runStarts = ((int[])localObject1);
/*  472 */       this.runAttributes = ((Vector[])localObject2);
/*  473 */       this.runAttributeValues = ((Vector[])localObject3);
/*  474 */       this.runArraySize = j;
/*      */     }
/*      */ 
/*  479 */     Vector localVector = null;
/*  480 */     Object localObject1 = null;
/*      */ 
/*  482 */     if (paramBoolean) {
/*  483 */       localObject2 = this.runAttributes[(i - 1)];
/*  484 */       localObject3 = this.runAttributeValues[(i - 1)];
/*  485 */       if (localObject2 != null) {
/*  486 */         localVector = (Vector)((Vector)localObject2).clone();
/*      */       }
/*  488 */       if (localObject3 != null) {
/*  489 */         localObject1 = (Vector)((Vector)localObject3).clone();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  494 */     this.runCount += 1;
/*  495 */     for (int k = this.runCount - 1; k > i; k--) {
/*  496 */       this.runStarts[k] = this.runStarts[(k - 1)];
/*  497 */       this.runAttributes[k] = this.runAttributes[(k - 1)];
/*  498 */       this.runAttributeValues[k] = this.runAttributeValues[(k - 1)];
/*      */     }
/*  500 */     this.runStarts[i] = paramInt;
/*  501 */     this.runAttributes[i] = localVector;
/*  502 */     this.runAttributeValues[i] = localObject1;
/*      */ 
/*  504 */     return i;
/*      */   }
/*      */ 
/*      */   private void addAttributeRunData(AttributedCharacterIterator.Attribute paramAttribute, Object paramObject, int paramInt1, int paramInt2)
/*      */   {
/*  511 */     for (int i = paramInt1; i < paramInt2; i++) {
/*  512 */       int j = -1;
/*  513 */       if (this.runAttributes[i] == null) {
/*  514 */         Vector localVector1 = new Vector();
/*  515 */         Vector localVector2 = new Vector();
/*  516 */         this.runAttributes[i] = localVector1;
/*  517 */         this.runAttributeValues[i] = localVector2;
/*      */       }
/*      */       else {
/*  520 */         j = this.runAttributes[i].indexOf(paramAttribute);
/*      */       }
/*      */ 
/*  523 */       if (j == -1)
/*      */       {
/*  525 */         int k = this.runAttributes[i].size();
/*  526 */         this.runAttributes[i].addElement(paramAttribute);
/*      */         try {
/*  528 */           this.runAttributeValues[i].addElement(paramObject);
/*      */         }
/*      */         catch (Exception localException) {
/*  531 */           this.runAttributes[i].setSize(k);
/*  532 */           this.runAttributeValues[i].setSize(k);
/*      */         }
/*      */       }
/*      */       else {
/*  536 */         this.runAttributeValues[i].set(j, paramObject);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public AttributedCharacterIterator getIterator()
/*      */   {
/*  548 */     return getIterator(null, 0, length());
/*      */   }
/*      */ 
/*      */   public AttributedCharacterIterator getIterator(AttributedCharacterIterator.Attribute[] paramArrayOfAttribute)
/*      */   {
/*  563 */     return getIterator(paramArrayOfAttribute, 0, length());
/*      */   }
/*      */ 
/*      */   public AttributedCharacterIterator getIterator(AttributedCharacterIterator.Attribute[] paramArrayOfAttribute, int paramInt1, int paramInt2)
/*      */   {
/*  583 */     return new AttributedStringIterator(paramArrayOfAttribute, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   int length()
/*      */   {
/*  592 */     return this.text.length();
/*      */   }
/*      */ 
/*      */   private char charAt(int paramInt) {
/*  596 */     return this.text.charAt(paramInt);
/*      */   }
/*      */ 
/*      */   private synchronized Object getAttribute(AttributedCharacterIterator.Attribute paramAttribute, int paramInt) {
/*  600 */     Vector localVector1 = this.runAttributes[paramInt];
/*  601 */     Vector localVector2 = this.runAttributeValues[paramInt];
/*  602 */     if (localVector1 == null) {
/*  603 */       return null;
/*      */     }
/*  605 */     int i = localVector1.indexOf(paramAttribute);
/*  606 */     if (i != -1) {
/*  607 */       return localVector2.elementAt(i);
/*      */     }
/*      */ 
/*  610 */     return null;
/*      */   }
/*      */ 
/*      */   private Object getAttributeCheckRange(AttributedCharacterIterator.Attribute paramAttribute, int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/*  616 */     Object localObject = getAttribute(paramAttribute, paramInt1);
/*  617 */     if ((localObject instanceof Annotation))
/*      */     {
/*      */       int j;
/*  619 */       if (paramInt2 > 0) {
/*  620 */         i = paramInt1;
/*  621 */         j = this.runStarts[i];
/*  622 */         while ((j >= paramInt2) && (valuesMatch(localObject, getAttribute(paramAttribute, i - 1))))
/*      */         {
/*  624 */           i--;
/*  625 */           j = this.runStarts[i];
/*      */         }
/*  627 */         if (j < paramInt2)
/*      */         {
/*  629 */           return null;
/*      */         }
/*      */       }
/*  632 */       int i = length();
/*  633 */       if (paramInt3 < i) {
/*  634 */         j = paramInt1;
/*  635 */         int k = j < this.runCount - 1 ? this.runStarts[(j + 1)] : i;
/*  636 */         while ((k <= paramInt3) && (valuesMatch(localObject, getAttribute(paramAttribute, j + 1))))
/*      */         {
/*  638 */           j++;
/*  639 */           k = j < this.runCount - 1 ? this.runStarts[(j + 1)] : i;
/*      */         }
/*  641 */         if (k > paramInt3)
/*      */         {
/*  643 */           return null;
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  649 */     return localObject;
/*      */   }
/*      */ 
/*      */   private boolean attributeValuesMatch(Set paramSet, int paramInt1, int paramInt2)
/*      */   {
/*  654 */     Iterator localIterator = paramSet.iterator();
/*  655 */     while (localIterator.hasNext()) {
/*  656 */       AttributedCharacterIterator.Attribute localAttribute = (AttributedCharacterIterator.Attribute)localIterator.next();
/*  657 */       if (!valuesMatch(getAttribute(localAttribute, paramInt1), getAttribute(localAttribute, paramInt2))) {
/*  658 */         return false;
/*      */       }
/*      */     }
/*  661 */     return true;
/*      */   }
/*      */ 
/*      */   private static final boolean valuesMatch(Object paramObject1, Object paramObject2)
/*      */   {
/*  666 */     if (paramObject1 == null) {
/*  667 */       return paramObject2 == null;
/*      */     }
/*  669 */     return paramObject1.equals(paramObject2);
/*      */   }
/*      */ 
/*      */   private final void appendContents(StringBuffer paramStringBuffer, CharacterIterator paramCharacterIterator)
/*      */   {
/*  679 */     int i = paramCharacterIterator.getBeginIndex();
/*  680 */     int j = paramCharacterIterator.getEndIndex();
/*      */ 
/*  682 */     while (i < j) {
/*  683 */       paramCharacterIterator.setIndex(i++);
/*  684 */       paramStringBuffer.append(paramCharacterIterator.current());
/*      */     }
/*      */   }
/*      */ 
/*      */   private void setAttributes(Map paramMap, int paramInt)
/*      */   {
/*  694 */     if (this.runCount == 0) {
/*  695 */       createRunAttributeDataVectors();
/*      */     }
/*      */ 
/*  698 */     int i = ensureRunBreak(paramInt, false);
/*      */     int j;
/*  701 */     if ((paramMap != null) && ((j = paramMap.size()) > 0)) {
/*  702 */       Vector localVector1 = new Vector(j);
/*  703 */       Vector localVector2 = new Vector(j);
/*  704 */       Iterator localIterator = paramMap.entrySet().iterator();
/*      */ 
/*  706 */       while (localIterator.hasNext()) {
/*  707 */         Map.Entry localEntry = (Map.Entry)localIterator.next();
/*      */ 
/*  709 */         localVector1.add(localEntry.getKey());
/*  710 */         localVector2.add(localEntry.getValue());
/*      */       }
/*  712 */       this.runAttributes[i] = localVector1;
/*  713 */       this.runAttributeValues[i] = localVector2;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static boolean mapsDiffer(Map paramMap1, Map paramMap2)
/*      */   {
/*  721 */     if (paramMap1 == null) {
/*  722 */       return (paramMap2 != null) && (paramMap2.size() > 0);
/*      */     }
/*  724 */     return !paramMap1.equals(paramMap2);
/*      */   }
/*      */ 
/*      */   private final class AttributeMap extends AbstractMap<AttributedCharacterIterator.Attribute, Object>
/*      */   {
/*      */     int runIndex;
/*      */     int beginIndex;
/*      */     int endIndex;
/*      */ 
/*      */     AttributeMap(int paramInt1, int paramInt2, int arg4)
/*      */     {
/* 1050 */       this.runIndex = paramInt1;
/* 1051 */       this.beginIndex = paramInt2;
/*      */       int i;
/* 1052 */       this.endIndex = i;
/*      */     }
/*      */ 
/*      */     public Set entrySet() {
/* 1056 */       HashSet localHashSet = new HashSet();
/* 1057 */       synchronized (AttributedString.this) {
/* 1058 */         int i = AttributedString.this.runAttributes[this.runIndex].size();
/* 1059 */         for (int j = 0; j < i; j++) {
/* 1060 */           AttributedCharacterIterator.Attribute localAttribute = (AttributedCharacterIterator.Attribute)AttributedString.this.runAttributes[this.runIndex].get(j);
/* 1061 */           Object localObject1 = AttributedString.this.runAttributeValues[this.runIndex].get(j);
/* 1062 */           if ((localObject1 instanceof Annotation)) {
/* 1063 */             localObject1 = AttributedString.this.getAttributeCheckRange(localAttribute, this.runIndex, this.beginIndex, this.endIndex);
/*      */ 
/* 1065 */             if (localObject1 == null);
/*      */           }
/*      */           else {
/* 1069 */             AttributeEntry localAttributeEntry = new AttributeEntry(localAttribute, localObject1);
/* 1070 */             localHashSet.add(localAttributeEntry);
/*      */           }
/*      */         }
/*      */       }
/* 1073 */       return localHashSet;
/*      */     }
/*      */ 
/*      */     public Object get(Object paramObject) {
/* 1077 */       return AttributedString.this.getAttributeCheckRange((AttributedCharacterIterator.Attribute)paramObject, this.runIndex, this.beginIndex, this.endIndex);
/*      */     }
/*      */   }
/*      */ 
/*      */   private final class AttributedStringIterator
/*      */     implements AttributedCharacterIterator
/*      */   {
/*      */     private int beginIndex;
/*      */     private int endIndex;
/*      */     private AttributedCharacterIterator.Attribute[] relevantAttributes;
/*      */     private int currentIndex;
/*      */     private int currentRunIndex;
/*      */     private int currentRunStart;
/*      */     private int currentRunLimit;
/*      */ 
/*      */     AttributedStringIterator(AttributedCharacterIterator.Attribute[] paramInt1, int paramInt2, int arg4)
/*      */     {
/*      */       int i;
/*  755 */       if ((paramInt2 < 0) || (paramInt2 > i) || (i > AttributedString.this.length())) {
/*  756 */         throw new IllegalArgumentException("Invalid substring range");
/*      */       }
/*      */ 
/*  759 */       this.beginIndex = paramInt2;
/*  760 */       this.endIndex = i;
/*  761 */       this.currentIndex = paramInt2;
/*  762 */       updateRunInfo();
/*  763 */       if (paramInt1 != null)
/*  764 */         this.relevantAttributes = ((AttributedCharacterIterator.Attribute[])paramInt1.clone());
/*      */     }
/*      */ 
/*      */     public boolean equals(Object paramObject)
/*      */     {
/*  771 */       if (this == paramObject) {
/*  772 */         return true;
/*      */       }
/*  774 */       if (!(paramObject instanceof AttributedStringIterator)) {
/*  775 */         return false;
/*      */       }
/*      */ 
/*  778 */       AttributedStringIterator localAttributedStringIterator = (AttributedStringIterator)paramObject;
/*      */ 
/*  780 */       if (AttributedString.this != localAttributedStringIterator.getString())
/*  781 */         return false;
/*  782 */       if ((this.currentIndex != localAttributedStringIterator.currentIndex) || (this.beginIndex != localAttributedStringIterator.beginIndex) || (this.endIndex != localAttributedStringIterator.endIndex))
/*  783 */         return false;
/*  784 */       return true;
/*      */     }
/*      */ 
/*      */     public int hashCode() {
/*  788 */       return AttributedString.this.text.hashCode() ^ this.currentIndex ^ this.beginIndex ^ this.endIndex;
/*      */     }
/*      */ 
/*      */     public Object clone() {
/*      */       try {
/*  793 */         return (AttributedStringIterator)super.clone();
/*      */       }
/*      */       catch (CloneNotSupportedException localCloneNotSupportedException) {
/*      */       }
/*  797 */       throw new InternalError();
/*      */     }
/*      */ 
/*      */     public char first()
/*      */     {
/*  804 */       return internalSetIndex(this.beginIndex);
/*      */     }
/*      */ 
/*      */     public char last() {
/*  808 */       if (this.endIndex == this.beginIndex) {
/*  809 */         return internalSetIndex(this.endIndex);
/*      */       }
/*  811 */       return internalSetIndex(this.endIndex - 1);
/*      */     }
/*      */ 
/*      */     public char current()
/*      */     {
/*  816 */       if (this.currentIndex == this.endIndex) {
/*  817 */         return 65535;
/*      */       }
/*  819 */       return AttributedString.this.charAt(this.currentIndex);
/*      */     }
/*      */ 
/*      */     public char next()
/*      */     {
/*  824 */       if (this.currentIndex < this.endIndex) {
/*  825 */         return internalSetIndex(this.currentIndex + 1);
/*      */       }
/*      */ 
/*  828 */       return 65535;
/*      */     }
/*      */ 
/*      */     public char previous()
/*      */     {
/*  833 */       if (this.currentIndex > this.beginIndex) {
/*  834 */         return internalSetIndex(this.currentIndex - 1);
/*      */       }
/*      */ 
/*  837 */       return 65535;
/*      */     }
/*      */ 
/*      */     public char setIndex(int paramInt)
/*      */     {
/*  842 */       if ((paramInt < this.beginIndex) || (paramInt > this.endIndex))
/*  843 */         throw new IllegalArgumentException("Invalid index");
/*  844 */       return internalSetIndex(paramInt);
/*      */     }
/*      */ 
/*      */     public int getBeginIndex() {
/*  848 */       return this.beginIndex;
/*      */     }
/*      */ 
/*      */     public int getEndIndex() {
/*  852 */       return this.endIndex;
/*      */     }
/*      */ 
/*      */     public int getIndex() {
/*  856 */       return this.currentIndex;
/*      */     }
/*      */ 
/*      */     public int getRunStart()
/*      */     {
/*  862 */       return this.currentRunStart;
/*      */     }
/*      */ 
/*      */     public int getRunStart(AttributedCharacterIterator.Attribute paramAttribute) {
/*  866 */       if ((this.currentRunStart == this.beginIndex) || (this.currentRunIndex == -1)) {
/*  867 */         return this.currentRunStart;
/*      */       }
/*  869 */       Object localObject = getAttribute(paramAttribute);
/*  870 */       int i = this.currentRunStart;
/*  871 */       int j = this.currentRunIndex;
/*  872 */       while ((i > this.beginIndex) && (AttributedString.valuesMatch(localObject, AttributedString.access$100(AttributedString.this, paramAttribute, j - 1))))
/*      */       {
/*  874 */         j--;
/*  875 */         i = AttributedString.this.runStarts[j];
/*      */       }
/*  877 */       if (i < this.beginIndex) {
/*  878 */         i = this.beginIndex;
/*      */       }
/*  880 */       return i;
/*      */     }
/*      */ 
/*      */     public int getRunStart(Set<? extends AttributedCharacterIterator.Attribute> paramSet)
/*      */     {
/*  885 */       if ((this.currentRunStart == this.beginIndex) || (this.currentRunIndex == -1)) {
/*  886 */         return this.currentRunStart;
/*      */       }
/*  888 */       int i = this.currentRunStart;
/*  889 */       int j = this.currentRunIndex;
/*  890 */       while ((i > this.beginIndex) && (AttributedString.this.attributeValuesMatch(paramSet, this.currentRunIndex, j - 1)))
/*      */       {
/*  892 */         j--;
/*  893 */         i = AttributedString.this.runStarts[j];
/*      */       }
/*  895 */       if (i < this.beginIndex) {
/*  896 */         i = this.beginIndex;
/*      */       }
/*  898 */       return i;
/*      */     }
/*      */ 
/*      */     public int getRunLimit()
/*      */     {
/*  903 */       return this.currentRunLimit;
/*      */     }
/*      */ 
/*      */     public int getRunLimit(AttributedCharacterIterator.Attribute paramAttribute) {
/*  907 */       if ((this.currentRunLimit == this.endIndex) || (this.currentRunIndex == -1)) {
/*  908 */         return this.currentRunLimit;
/*      */       }
/*  910 */       Object localObject = getAttribute(paramAttribute);
/*  911 */       int i = this.currentRunLimit;
/*  912 */       int j = this.currentRunIndex;
/*  913 */       while ((i < this.endIndex) && (AttributedString.valuesMatch(localObject, AttributedString.access$100(AttributedString.this, paramAttribute, j + 1))))
/*      */       {
/*  915 */         j++;
/*  916 */         i = j < AttributedString.this.runCount - 1 ? AttributedString.this.runStarts[(j + 1)] : this.endIndex;
/*      */       }
/*  918 */       if (i > this.endIndex) {
/*  919 */         i = this.endIndex;
/*      */       }
/*  921 */       return i;
/*      */     }
/*      */ 
/*      */     public int getRunLimit(Set<? extends AttributedCharacterIterator.Attribute> paramSet)
/*      */     {
/*  926 */       if ((this.currentRunLimit == this.endIndex) || (this.currentRunIndex == -1)) {
/*  927 */         return this.currentRunLimit;
/*      */       }
/*  929 */       int i = this.currentRunLimit;
/*  930 */       int j = this.currentRunIndex;
/*  931 */       while ((i < this.endIndex) && (AttributedString.this.attributeValuesMatch(paramSet, this.currentRunIndex, j + 1)))
/*      */       {
/*  933 */         j++;
/*  934 */         i = j < AttributedString.this.runCount - 1 ? AttributedString.this.runStarts[(j + 1)] : this.endIndex;
/*      */       }
/*  936 */       if (i > this.endIndex) {
/*  937 */         i = this.endIndex;
/*      */       }
/*  939 */       return i;
/*      */     }
/*      */ 
/*      */     public Map<AttributedCharacterIterator.Attribute, Object> getAttributes()
/*      */     {
/*  944 */       if ((AttributedString.this.runAttributes == null) || (this.currentRunIndex == -1) || (AttributedString.this.runAttributes[this.currentRunIndex] == null))
/*      */       {
/*  947 */         return new Hashtable();
/*      */       }
/*  949 */       return new AttributedString.AttributeMap(AttributedString.this, this.currentRunIndex, this.beginIndex, this.endIndex);
/*      */     }
/*      */ 
/*      */     public Set<AttributedCharacterIterator.Attribute> getAllAttributeKeys()
/*      */     {
/*  954 */       if (AttributedString.this.runAttributes == null)
/*      */       {
/*  957 */         return new HashSet();
/*      */       }
/*  959 */       synchronized (AttributedString.this)
/*      */       {
/*  962 */         HashSet localHashSet = new HashSet();
/*  963 */         int i = 0;
/*  964 */         while (i < AttributedString.this.runCount) {
/*  965 */           if ((AttributedString.this.runStarts[i] < this.endIndex) && ((i == AttributedString.this.runCount - 1) || (AttributedString.this.runStarts[(i + 1)] > this.beginIndex))) {
/*  966 */             Vector localVector = AttributedString.this.runAttributes[i];
/*  967 */             if (localVector != null) {
/*  968 */               int j = localVector.size();
/*  969 */               while (j-- > 0) {
/*  970 */                 localHashSet.add(localVector.get(j));
/*      */               }
/*      */             }
/*      */           }
/*  974 */           i++;
/*      */         }
/*  976 */         return localHashSet;
/*      */       }
/*      */     }
/*      */ 
/*      */     public Object getAttribute(AttributedCharacterIterator.Attribute paramAttribute) {
/*  981 */       int i = this.currentRunIndex;
/*  982 */       if (i < 0) {
/*  983 */         return null;
/*      */       }
/*  985 */       return AttributedString.this.getAttributeCheckRange(paramAttribute, i, this.beginIndex, this.endIndex);
/*      */     }
/*      */ 
/*      */     private AttributedString getString()
/*      */     {
/*  991 */       return AttributedString.this;
/*      */     }
/*      */ 
/*      */     private char internalSetIndex(int paramInt)
/*      */     {
/*  997 */       this.currentIndex = paramInt;
/*  998 */       if ((paramInt < this.currentRunStart) || (paramInt >= this.currentRunLimit)) {
/*  999 */         updateRunInfo();
/*      */       }
/* 1001 */       if (this.currentIndex == this.endIndex) {
/* 1002 */         return 65535;
/*      */       }
/* 1004 */       return AttributedString.this.charAt(paramInt);
/*      */     }
/*      */ 
/*      */     private void updateRunInfo()
/*      */     {
/* 1010 */       if (this.currentIndex == this.endIndex) {
/* 1011 */         this.currentRunStart = (this.currentRunLimit = this.endIndex);
/* 1012 */         this.currentRunIndex = -1;
/*      */       } else {
/* 1014 */         synchronized (AttributedString.this) {
/* 1015 */           int i = -1;
/* 1016 */           while ((i < AttributedString.this.runCount - 1) && (AttributedString.this.runStarts[(i + 1)] <= this.currentIndex))
/* 1017 */             i++;
/* 1018 */           this.currentRunIndex = i;
/* 1019 */           if (i >= 0) {
/* 1020 */             this.currentRunStart = AttributedString.this.runStarts[i];
/* 1021 */             if (this.currentRunStart < this.beginIndex)
/* 1022 */               this.currentRunStart = this.beginIndex;
/*      */           }
/*      */           else {
/* 1025 */             this.currentRunStart = this.beginIndex;
/*      */           }
/* 1027 */           if (i < AttributedString.this.runCount - 1) {
/* 1028 */             this.currentRunLimit = AttributedString.this.runStarts[(i + 1)];
/* 1029 */             if (this.currentRunLimit > this.endIndex)
/* 1030 */               this.currentRunLimit = this.endIndex;
/*      */           }
/*      */           else {
/* 1033 */             this.currentRunLimit = this.endIndex;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.text.AttributedString
 * JD-Core Version:    0.6.2
 */