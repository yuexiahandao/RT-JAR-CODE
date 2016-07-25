/*     */ package javax.swing.text;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.util.Vector;
/*     */ import javax.swing.undo.AbstractUndoableEdit;
/*     */ import javax.swing.undo.CannotRedoException;
/*     */ import javax.swing.undo.CannotUndoException;
/*     */ import javax.swing.undo.UndoableEdit;
/*     */ 
/*     */ public class GapContent extends GapVector
/*     */   implements AbstractDocument.Content, Serializable
/*     */ {
/* 317 */   private static final char[] empty = new char[0];
/*     */   private transient MarkVector marks;
/*     */   private transient MarkData search;
/* 330 */   private transient int unusedMarks = 0;
/*     */   private transient ReferenceQueue<StickyPosition> queue;
/*     */   static final int GROWTH_SIZE = 524288;
/*     */ 
/*     */   public GapContent()
/*     */   {
/*  67 */     this(10);
/*     */   }
/*     */ 
/*     */   public GapContent(int paramInt)
/*     */   {
/*  79 */     super(Math.max(paramInt, 2));
/*  80 */     char[] arrayOfChar = new char[1];
/*  81 */     arrayOfChar[0] = '\n';
/*  82 */     replace(0, 0, arrayOfChar, arrayOfChar.length);
/*     */ 
/*  84 */     this.marks = new MarkVector();
/*  85 */     this.search = new MarkData(0);
/*  86 */     this.queue = new ReferenceQueue();
/*     */   }
/*     */ 
/*     */   protected Object allocateArray(int paramInt)
/*     */   {
/*  94 */     return new char[paramInt];
/*     */   }
/*     */ 
/*     */   protected int getArrayLength()
/*     */   {
/* 101 */     char[] arrayOfChar = (char[])getArray();
/* 102 */     return arrayOfChar.length;
/*     */   }
/*     */ 
/*     */   public int length()
/*     */   {
/* 114 */     int i = getArrayLength() - (getGapEnd() - getGapStart());
/* 115 */     return i;
/*     */   }
/*     */ 
/*     */   public UndoableEdit insertString(int paramInt, String paramString)
/*     */     throws BadLocationException
/*     */   {
/* 128 */     if ((paramInt > length()) || (paramInt < 0)) {
/* 129 */       throw new BadLocationException("Invalid insert", length());
/*     */     }
/* 131 */     char[] arrayOfChar = paramString.toCharArray();
/* 132 */     replace(paramInt, 0, arrayOfChar, arrayOfChar.length);
/* 133 */     return new InsertUndo(paramInt, paramString.length());
/*     */   }
/*     */ 
/*     */   public UndoableEdit remove(int paramInt1, int paramInt2)
/*     */     throws BadLocationException
/*     */   {
/* 146 */     if (paramInt1 + paramInt2 >= length()) {
/* 147 */       throw new BadLocationException("Invalid remove", length() + 1);
/*     */     }
/* 149 */     String str = getString(paramInt1, paramInt2);
/* 150 */     RemoveUndo localRemoveUndo = new RemoveUndo(paramInt1, str);
/* 151 */     replace(paramInt1, paramInt2, empty, 0);
/* 152 */     return localRemoveUndo;
/*     */   }
/*     */ 
/*     */   public String getString(int paramInt1, int paramInt2)
/*     */     throws BadLocationException
/*     */   {
/* 166 */     Segment localSegment = new Segment();
/* 167 */     getChars(paramInt1, paramInt2, localSegment);
/* 168 */     return new String(localSegment.array, localSegment.offset, localSegment.count);
/*     */   }
/*     */ 
/*     */   public void getChars(int paramInt1, int paramInt2, Segment paramSegment)
/*     */     throws BadLocationException
/*     */   {
/* 184 */     int i = paramInt1 + paramInt2;
/* 185 */     if ((paramInt1 < 0) || (i < 0)) {
/* 186 */       throw new BadLocationException("Invalid location", -1);
/*     */     }
/* 188 */     if ((i > length()) || (paramInt1 > length())) {
/* 189 */       throw new BadLocationException("Invalid location", length() + 1);
/*     */     }
/* 191 */     int j = getGapStart();
/* 192 */     int k = getGapEnd();
/* 193 */     char[] arrayOfChar = (char[])getArray();
/* 194 */     if (paramInt1 + paramInt2 <= j)
/*     */     {
/* 196 */       paramSegment.array = arrayOfChar;
/* 197 */       paramSegment.offset = paramInt1;
/* 198 */     } else if (paramInt1 >= j)
/*     */     {
/* 200 */       paramSegment.array = arrayOfChar;
/* 201 */       paramSegment.offset = (k + paramInt1 - j);
/*     */     }
/*     */     else {
/* 204 */       int m = j - paramInt1;
/* 205 */       if (paramSegment.isPartialReturn())
/*     */       {
/* 207 */         paramSegment.array = arrayOfChar;
/* 208 */         paramSegment.offset = paramInt1;
/* 209 */         paramSegment.count = m;
/* 210 */         return;
/*     */       }
/*     */ 
/* 213 */       paramSegment.array = new char[paramInt2];
/* 214 */       paramSegment.offset = 0;
/* 215 */       System.arraycopy(arrayOfChar, paramInt1, paramSegment.array, 0, m);
/* 216 */       System.arraycopy(arrayOfChar, k, paramSegment.array, m, paramInt2 - m);
/*     */     }
/* 218 */     paramSegment.count = paramInt2;
/*     */   }
/*     */ 
/*     */   public Position createPosition(int paramInt)
/*     */     throws BadLocationException
/*     */   {
/* 230 */     while (this.queue.poll() != null) {
/* 231 */       this.unusedMarks += 1;
/*     */     }
/* 233 */     if (this.unusedMarks > Math.max(5, this.marks.size() / 10)) {
/* 234 */       removeUnusedMarks();
/*     */     }
/* 236 */     int i = getGapStart();
/* 237 */     int j = getGapEnd();
/* 238 */     int k = paramInt < i ? paramInt : paramInt + (j - i);
/* 239 */     this.search.index = k;
/* 240 */     int m = findSortIndex(this.search);
/*     */     MarkData localMarkData;
/*     */     StickyPosition localStickyPosition;
/* 243 */     if ((m >= this.marks.size()) || ((localMarkData = this.marks.elementAt(m)).index != k) || ((localStickyPosition = localMarkData.getPosition()) == null))
/*     */     {
/* 248 */       localStickyPosition = new StickyPosition();
/* 249 */       localMarkData = new MarkData(k, localStickyPosition, this.queue);
/* 250 */       localStickyPosition.setMark(localMarkData);
/* 251 */       this.marks.insertElementAt(localMarkData, m);
/*     */     }
/*     */ 
/* 254 */     return localStickyPosition;
/*     */   }
/*     */ 
/*     */   protected void shiftEnd(int paramInt)
/*     */   {
/* 343 */     int i = getGapEnd();
/*     */ 
/* 345 */     super.shiftEnd(paramInt);
/*     */ 
/* 348 */     int j = getGapEnd() - i;
/* 349 */     int k = findMarkAdjustIndex(i);
/* 350 */     int m = this.marks.size();
/* 351 */     for (int n = k; n < m; n++) {
/* 352 */       MarkData localMarkData = this.marks.elementAt(n);
/* 353 */       localMarkData.index += j;
/*     */     }
/*     */   }
/*     */ 
/*     */   int getNewArraySize(int paramInt)
/*     */   {
/* 362 */     if (paramInt < 524288) {
/* 363 */       return super.getNewArraySize(paramInt);
/*     */     }
/* 365 */     return paramInt + 524288;
/*     */   }
/*     */ 
/*     */   protected void shiftGap(int paramInt)
/*     */   {
/* 376 */     int i = getGapStart();
/* 377 */     int j = paramInt - i;
/* 378 */     int k = getGapEnd();
/* 379 */     int m = k + j;
/* 380 */     int n = k - i;
/*     */ 
/* 383 */     super.shiftGap(paramInt);
/*     */     int i1;
/*     */     int i2;
/*     */     int i3;
/*     */     MarkData localMarkData;
/* 386 */     if (j > 0)
/*     */     {
/* 388 */       i1 = findMarkAdjustIndex(i);
/* 389 */       i2 = this.marks.size();
/* 390 */       for (i3 = i1; i3 < i2; i3++) {
/* 391 */         localMarkData = this.marks.elementAt(i3);
/* 392 */         if (localMarkData.index >= m) {
/*     */           break;
/*     */         }
/* 395 */         localMarkData.index -= n;
/*     */       }
/* 397 */     } else if (j < 0)
/*     */     {
/* 399 */       i1 = findMarkAdjustIndex(paramInt);
/* 400 */       i2 = this.marks.size();
/* 401 */       for (i3 = i1; i3 < i2; i3++) {
/* 402 */         localMarkData = this.marks.elementAt(i3);
/* 403 */         if (localMarkData.index >= k) {
/*     */           break;
/*     */         }
/* 406 */         localMarkData.index += n;
/*     */       }
/*     */     }
/* 409 */     resetMarksAtZero();
/*     */   }
/*     */ 
/*     */   protected void resetMarksAtZero()
/*     */   {
/* 417 */     if ((this.marks != null) && (getGapStart() == 0)) {
/* 418 */       int i = getGapEnd();
/* 419 */       int j = 0; int k = this.marks.size();
/* 420 */       for (; j < k; j++) {
/* 421 */         MarkData localMarkData = this.marks.elementAt(j);
/* 422 */         if (localMarkData.index > i) break;
/* 423 */         localMarkData.index = 0;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void shiftGapStartDown(int paramInt)
/*     */   {
/* 442 */     int i = findMarkAdjustIndex(paramInt);
/* 443 */     int j = this.marks.size();
/* 444 */     int k = getGapStart();
/* 445 */     int m = getGapEnd();
/* 446 */     for (int n = i; n < j; n++) {
/* 447 */       MarkData localMarkData = this.marks.elementAt(n);
/* 448 */       if (localMarkData.index > k)
/*     */       {
/*     */         break;
/*     */       }
/* 452 */       localMarkData.index = m;
/*     */     }
/*     */ 
/* 456 */     super.shiftGapStartDown(paramInt);
/*     */ 
/* 458 */     resetMarksAtZero();
/*     */   }
/*     */ 
/*     */   protected void shiftGapEndUp(int paramInt)
/*     */   {
/* 470 */     int i = findMarkAdjustIndex(getGapEnd());
/* 471 */     int j = this.marks.size();
/* 472 */     for (int k = i; k < j; k++) {
/* 473 */       MarkData localMarkData = this.marks.elementAt(k);
/* 474 */       if (localMarkData.index >= paramInt) {
/*     */         break;
/*     */       }
/* 477 */       localMarkData.index = paramInt;
/*     */     }
/*     */ 
/* 481 */     super.shiftGapEndUp(paramInt);
/*     */ 
/* 483 */     resetMarksAtZero();
/*     */   }
/*     */ 
/*     */   final int compare(MarkData paramMarkData1, MarkData paramMarkData2)
/*     */   {
/* 494 */     if (paramMarkData1.index < paramMarkData2.index)
/* 495 */       return -1;
/* 496 */     if (paramMarkData1.index > paramMarkData2.index) {
/* 497 */       return 1;
/*     */     }
/* 499 */     return 0;
/*     */   }
/*     */ 
/*     */   final int findMarkAdjustIndex(int paramInt)
/*     */   {
/* 508 */     this.search.index = Math.max(paramInt, 1);
/* 509 */     int i = findSortIndex(this.search);
/*     */ 
/* 513 */     for (int j = i - 1; j >= 0; j--) {
/* 514 */       MarkData localMarkData = this.marks.elementAt(j);
/* 515 */       if (localMarkData.index != this.search.index) {
/*     */         break;
/*     */       }
/* 518 */       i--;
/*     */     }
/* 520 */     return i;
/*     */   }
/*     */ 
/*     */   final int findSortIndex(MarkData paramMarkData)
/*     */   {
/* 530 */     int i = 0;
/* 531 */     int j = this.marks.size() - 1;
/* 532 */     int k = 0;
/*     */ 
/* 534 */     if (j == -1) {
/* 535 */       return 0;
/*     */     }
/*     */ 
/* 539 */     MarkData localMarkData1 = this.marks.elementAt(j);
/* 540 */     int m = compare(paramMarkData, localMarkData1);
/* 541 */     if (m > 0) {
/* 542 */       return j + 1;
/*     */     }
/* 544 */     while (i <= j) {
/* 545 */       k = i + (j - i) / 2;
/* 546 */       MarkData localMarkData2 = this.marks.elementAt(k);
/* 547 */       m = compare(paramMarkData, localMarkData2);
/*     */ 
/* 549 */       if (m == 0)
/*     */       {
/* 551 */         return k;
/* 552 */       }if (m < 0)
/* 553 */         j = k - 1;
/*     */       else {
/* 555 */         i = k + 1;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 560 */     return m < 0 ? k : k + 1;
/*     */   }
/*     */ 
/*     */   final void removeUnusedMarks()
/*     */   {
/* 568 */     int i = this.marks.size();
/* 569 */     MarkVector localMarkVector = new MarkVector(i);
/* 570 */     for (int j = 0; j < i; j++) {
/* 571 */       MarkData localMarkData = this.marks.elementAt(j);
/* 572 */       if (localMarkData.get() != null) {
/* 573 */         localMarkVector.addElement(localMarkData);
/*     */       }
/*     */     }
/* 576 */     this.marks = localMarkVector;
/* 577 */     this.unusedMarks = 0;
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws ClassNotFoundException, IOException
/*     */   {
/* 691 */     paramObjectInputStream.defaultReadObject();
/* 692 */     this.marks = new MarkVector();
/* 693 */     this.search = new MarkData(0);
/* 694 */     this.queue = new ReferenceQueue();
/*     */   }
/*     */ 
/*     */   protected Vector getPositionsInRange(Vector paramVector, int paramInt1, int paramInt2)
/*     */   {
/* 713 */     int i = paramInt1 + paramInt2;
/*     */ 
/* 716 */     int m = getGapStart();
/* 717 */     int n = getGapEnd();
/*     */     int j;
/*     */     int k;
/* 720 */     if (paramInt1 < m) {
/* 721 */       if (paramInt1 == 0)
/*     */       {
/* 723 */         j = 0;
/*     */       }
/*     */       else {
/* 726 */         j = findMarkAdjustIndex(paramInt1);
/*     */       }
/* 728 */       if (i >= m) {
/* 729 */         k = findMarkAdjustIndex(i + (n - m) + 1);
/*     */       }
/*     */       else
/* 732 */         k = findMarkAdjustIndex(i + 1);
/*     */     }
/*     */     else
/*     */     {
/* 736 */       j = findMarkAdjustIndex(paramInt1 + (n - m));
/* 737 */       k = findMarkAdjustIndex(i + (n - m) + 1);
/*     */     }
/*     */ 
/* 740 */     Vector localVector = paramVector == null ? new Vector(Math.max(1, k - j)) : paramVector;
/*     */ 
/* 743 */     for (int i1 = j; i1 < k; i1++) {
/* 744 */       localVector.addElement(new UndoPosRef(this.marks.elementAt(i1)));
/*     */     }
/* 746 */     return localVector;
/*     */   }
/*     */ 
/*     */   protected void updateUndoPositions(Vector paramVector, int paramInt1, int paramInt2)
/*     */   {
/* 761 */     int i = paramInt1 + paramInt2;
/* 762 */     int j = getGapEnd();
/*     */ 
/* 764 */     int m = findMarkAdjustIndex(j + 1);
/*     */     int k;
/* 766 */     if (paramInt1 != 0) {
/* 767 */       k = findMarkAdjustIndex(j);
/*     */     }
/*     */     else {
/* 770 */       k = 0;
/*     */     }
/*     */ 
/* 774 */     for (int n = paramVector.size() - 1; n >= 0; n--) {
/* 775 */       UndoPosRef localUndoPosRef = (UndoPosRef)paramVector.elementAt(n);
/* 776 */       localUndoPosRef.resetLocation(i, j);
/*     */     }
/*     */ 
/* 782 */     if (k < m) {
/* 783 */       Object[] arrayOfObject = new Object[m - k];
/* 784 */       int i1 = 0;
/*     */       MarkData localMarkData;
/* 786 */       if (paramInt1 == 0)
/*     */       {
/* 790 */         for (i2 = k; i2 < m; i2++) {
/* 791 */           localMarkData = this.marks.elementAt(i2);
/* 792 */           if (localMarkData.index == 0) {
/* 793 */             arrayOfObject[(i1++)] = localMarkData;
/*     */           }
/*     */         }
/* 796 */         for (i2 = k; i2 < m; i2++) {
/* 797 */           localMarkData = this.marks.elementAt(i2);
/* 798 */           if (localMarkData.index != 0) {
/* 799 */             arrayOfObject[(i1++)] = localMarkData;
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 804 */       for (int i2 = k; i2 < m; i2++) {
/* 805 */         localMarkData = this.marks.elementAt(i2);
/* 806 */         if (localMarkData.index != j) {
/* 807 */           arrayOfObject[(i1++)] = localMarkData;
/*     */         }
/*     */       }
/* 810 */       for (i2 = k; i2 < m; i2++) {
/* 811 */         localMarkData = this.marks.elementAt(i2);
/* 812 */         if (localMarkData.index == j) {
/* 813 */           arrayOfObject[(i1++)] = localMarkData;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 818 */       this.marks.replaceRange(k, m, arrayOfObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   class InsertUndo extends AbstractUndoableEdit
/*     */   {
/*     */     protected int offset;
/*     */     protected int length;
/*     */     protected String string;
/*     */     protected Vector posRefs;
/*     */ 
/*     */     protected InsertUndo(int paramInt1, int arg3)
/*     */     {
/* 861 */       this.offset = paramInt1;
/*     */       int i;
/* 862 */       this.length = i;
/*     */     }
/*     */ 
/*     */     public void undo() throws CannotUndoException {
/* 866 */       super.undo();
/*     */       try
/*     */       {
/* 869 */         this.posRefs = GapContent.this.getPositionsInRange(null, this.offset, this.length);
/* 870 */         this.string = GapContent.this.getString(this.offset, this.length);
/* 871 */         GapContent.this.remove(this.offset, this.length);
/*     */       } catch (BadLocationException localBadLocationException) {
/* 873 */         throw new CannotUndoException();
/*     */       }
/*     */     }
/*     */ 
/*     */     public void redo() throws CannotRedoException {
/* 878 */       super.redo();
/*     */       try {
/* 880 */         GapContent.this.insertString(this.offset, this.string);
/* 881 */         this.string = null;
/*     */ 
/* 883 */         if (this.posRefs != null) {
/* 884 */           GapContent.this.updateUndoPositions(this.posRefs, this.offset, this.length);
/* 885 */           this.posRefs = null;
/*     */         }
/*     */       } catch (BadLocationException localBadLocationException) {
/* 888 */         throw new CannotRedoException();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   final class MarkData extends WeakReference<GapContent.StickyPosition>
/*     */   {
/*     */     int index;
/*     */ 
/*     */     MarkData(int arg2)
/*     */     {
/* 268 */       super();
/*     */       int i;
/* 269 */       this.index = i;
/*     */     }
/*     */     MarkData(GapContent.StickyPosition paramReferenceQueue, ReferenceQueue<? super GapContent.StickyPosition> arg3) {
/* 272 */       super(localReferenceQueue);
/* 273 */       this.index = paramReferenceQueue;
/*     */     }
/*     */ 
/*     */     public final int getOffset()
/*     */     {
/* 283 */       int i = GapContent.this.getGapStart();
/* 284 */       int j = GapContent.this.getGapEnd();
/* 285 */       int k = this.index < i ? this.index : this.index - (j - i);
/* 286 */       return Math.max(k, 0);
/*     */     }
/*     */ 
/*     */     GapContent.StickyPosition getPosition() {
/* 290 */       return (GapContent.StickyPosition)get();
/*     */     }
/*     */   }
/*     */ 
/*     */   static class MarkVector extends GapVector
/*     */   {
/* 683 */     GapContent.MarkData[] oneMark = new GapContent.MarkData[1];
/*     */ 
/*     */     MarkVector()
/*     */     {
/*     */     }
/*     */ 
/*     */     MarkVector(int paramInt)
/*     */     {
/* 588 */       super();
/*     */     }
/*     */ 
/*     */     protected Object allocateArray(int paramInt)
/*     */     {
/* 596 */       return new GapContent.MarkData[paramInt];
/*     */     }
/*     */ 
/*     */     protected int getArrayLength()
/*     */     {
/* 603 */       GapContent.MarkData[] arrayOfMarkData = (GapContent.MarkData[])getArray();
/* 604 */       return arrayOfMarkData.length;
/*     */     }
/*     */ 
/*     */     public int size()
/*     */     {
/* 611 */       int i = getArrayLength() - (getGapEnd() - getGapStart());
/* 612 */       return i;
/*     */     }
/*     */ 
/*     */     public void insertElementAt(GapContent.MarkData paramMarkData, int paramInt)
/*     */     {
/* 619 */       this.oneMark[0] = paramMarkData;
/* 620 */       replace(paramInt, 0, this.oneMark, 1);
/*     */     }
/*     */ 
/*     */     public void addElement(GapContent.MarkData paramMarkData)
/*     */     {
/* 627 */       insertElementAt(paramMarkData, size());
/*     */     }
/*     */ 
/*     */     public GapContent.MarkData elementAt(int paramInt)
/*     */     {
/* 634 */       int i = getGapStart();
/* 635 */       int j = getGapEnd();
/* 636 */       GapContent.MarkData[] arrayOfMarkData = (GapContent.MarkData[])getArray();
/* 637 */       if (paramInt < i)
/*     */       {
/* 639 */         return arrayOfMarkData[paramInt];
/*     */       }
/*     */ 
/* 642 */       paramInt += j - i;
/* 643 */       return arrayOfMarkData[paramInt];
/*     */     }
/*     */ 
/*     */     protected void replaceRange(int paramInt1, int paramInt2, Object[] paramArrayOfObject)
/*     */     {
/* 654 */       int i = getGapStart();
/* 655 */       int j = getGapEnd();
/* 656 */       int k = paramInt1;
/* 657 */       int m = 0;
/* 658 */       Object[] arrayOfObject = (Object[])getArray();
/* 659 */       if (paramInt1 >= i)
/*     */       {
/* 661 */         k += j - i;
/* 662 */         paramInt2 += j - i;
/*     */       }
/* 664 */       else if (paramInt2 >= i)
/*     */       {
/* 666 */         paramInt2 += j - i;
/* 667 */         while (k < i) {
/* 668 */           arrayOfObject[(k++)] = paramArrayOfObject[(m++)];
/*     */         }
/* 670 */         k = j;
/*     */       }
/*     */       else
/*     */       {
/* 674 */         while (k < paramInt2) {
/* 675 */           arrayOfObject[(k++)] = paramArrayOfObject[(m++)];
/*     */         }
/*     */       }
/* 678 */       while (k < paramInt2)
/* 679 */         arrayOfObject[(k++)] = paramArrayOfObject[(m++)];
/*     */     }
/*     */   }
/*     */ 
/*     */   class RemoveUndo extends AbstractUndoableEdit
/*     */   {
/*     */     protected int offset;
/*     */     protected int length;
/*     */     protected String string;
/*     */     protected Vector posRefs;
/*     */ 
/*     */     protected RemoveUndo(int paramString, String arg3)
/*     */     {
/* 911 */       this.offset = paramString;
/*     */       Object localObject;
/* 912 */       this.string = localObject;
/* 913 */       this.length = localObject.length();
/* 914 */       this.posRefs = GapContent.this.getPositionsInRange(null, paramString, this.length);
/*     */     }
/*     */ 
/*     */     public void undo() throws CannotUndoException {
/* 918 */       super.undo();
/*     */       try {
/* 920 */         GapContent.this.insertString(this.offset, this.string);
/*     */ 
/* 922 */         if (this.posRefs != null) {
/* 923 */           GapContent.this.updateUndoPositions(this.posRefs, this.offset, this.length);
/* 924 */           this.posRefs = null;
/*     */         }
/* 926 */         this.string = null;
/*     */       } catch (BadLocationException localBadLocationException) {
/* 928 */         throw new CannotUndoException();
/*     */       }
/*     */     }
/*     */ 
/*     */     public void redo() throws CannotRedoException {
/* 933 */       super.redo();
/*     */       try {
/* 935 */         this.string = GapContent.this.getString(this.offset, this.length);
/*     */ 
/* 937 */         this.posRefs = GapContent.this.getPositionsInRange(null, this.offset, this.length);
/* 938 */         GapContent.this.remove(this.offset, this.length);
/*     */       } catch (BadLocationException localBadLocationException) {
/* 940 */         throw new CannotRedoException();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   final class StickyPosition
/*     */     implements Position
/*     */   {
/*     */     GapContent.MarkData mark;
/*     */ 
/*     */     StickyPosition()
/*     */     {
/*     */     }
/*     */ 
/*     */     void setMark(GapContent.MarkData paramMarkData)
/*     */     {
/* 301 */       this.mark = paramMarkData;
/*     */     }
/*     */ 
/*     */     public final int getOffset() {
/* 305 */       return this.mark.getOffset();
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 309 */       return Integer.toString(getOffset());
/*     */     }
/*     */   }
/*     */ 
/*     */   final class UndoPosRef
/*     */   {
/*     */     protected int undoLocation;
/*     */     protected GapContent.MarkData rec;
/*     */ 
/*     */     UndoPosRef(GapContent.MarkData arg2)
/*     */     {
/*     */       Object localObject;
/* 828 */       this.rec = localObject;
/* 829 */       this.undoLocation = localObject.getOffset();
/*     */     }
/*     */ 
/*     */     protected void resetLocation(int paramInt1, int paramInt2)
/*     */     {
/* 840 */       if (this.undoLocation != paramInt1) {
/* 841 */         this.rec.index = this.undoLocation;
/*     */       }
/*     */       else
/* 844 */         this.rec.index = paramInt2;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.GapContent
 * JD-Core Version:    0.6.2
 */