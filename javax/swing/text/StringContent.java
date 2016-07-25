/*     */ package javax.swing.text;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Vector;
/*     */ import javax.swing.undo.AbstractUndoableEdit;
/*     */ import javax.swing.undo.CannotRedoException;
/*     */ import javax.swing.undo.CannotUndoException;
/*     */ import javax.swing.undo.UndoableEdit;
/*     */ 
/*     */ public final class StringContent
/*     */   implements AbstractDocument.Content, Serializable
/*     */ {
/* 312 */   private static final char[] empty = new char[0];
/*     */   private char[] data;
/*     */   private int count;
/*     */   transient Vector<PosRec> marks;
/*     */ 
/*     */   public StringContent()
/*     */   {
/*  59 */     this(10);
/*     */   }
/*     */ 
/*     */   public StringContent(int paramInt)
/*     */   {
/*  69 */     if (paramInt < 1) {
/*  70 */       paramInt = 1;
/*     */     }
/*  72 */     this.data = new char[paramInt];
/*  73 */     this.data[0] = '\n';
/*  74 */     this.count = 1;
/*     */   }
/*     */ 
/*     */   public int length()
/*     */   {
/*  84 */     return this.count;
/*     */   }
/*     */ 
/*     */   public UndoableEdit insertString(int paramInt, String paramString)
/*     */     throws BadLocationException
/*     */   {
/*  97 */     if ((paramInt >= this.count) || (paramInt < 0)) {
/*  98 */       throw new BadLocationException("Invalid location", this.count);
/*     */     }
/* 100 */     char[] arrayOfChar = paramString.toCharArray();
/* 101 */     replace(paramInt, 0, arrayOfChar, 0, arrayOfChar.length);
/* 102 */     if (this.marks != null) {
/* 103 */       updateMarksForInsert(paramInt, paramString.length());
/*     */     }
/* 105 */     return new InsertUndo(paramInt, paramString.length());
/*     */   }
/*     */ 
/*     */   public UndoableEdit remove(int paramInt1, int paramInt2)
/*     */     throws BadLocationException
/*     */   {
/* 118 */     if (paramInt1 + paramInt2 >= this.count) {
/* 119 */       throw new BadLocationException("Invalid range", this.count);
/*     */     }
/* 121 */     String str = getString(paramInt1, paramInt2);
/* 122 */     RemoveUndo localRemoveUndo = new RemoveUndo(paramInt1, str);
/* 123 */     replace(paramInt1, paramInt2, empty, 0, 0);
/* 124 */     if (this.marks != null) {
/* 125 */       updateMarksForRemove(paramInt1, paramInt2);
/*     */     }
/* 127 */     return localRemoveUndo;
/*     */   }
/*     */ 
/*     */   public String getString(int paramInt1, int paramInt2)
/*     */     throws BadLocationException
/*     */   {
/* 141 */     if (paramInt1 + paramInt2 > this.count) {
/* 142 */       throw new BadLocationException("Invalid range", this.count);
/*     */     }
/* 144 */     return new String(this.data, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public void getChars(int paramInt1, int paramInt2, Segment paramSegment)
/*     */     throws BadLocationException
/*     */   {
/* 157 */     if (paramInt1 + paramInt2 > this.count) {
/* 158 */       throw new BadLocationException("Invalid location", this.count);
/*     */     }
/* 160 */     paramSegment.array = this.data;
/* 161 */     paramSegment.offset = paramInt1;
/* 162 */     paramSegment.count = paramInt2;
/*     */   }
/*     */ 
/*     */   public Position createPosition(int paramInt)
/*     */     throws BadLocationException
/*     */   {
/* 176 */     if (this.marks == null) {
/* 177 */       this.marks = new Vector();
/*     */     }
/* 179 */     return new StickyPosition(paramInt);
/*     */   }
/*     */ 
/*     */   void replace(int paramInt1, int paramInt2, char[] paramArrayOfChar, int paramInt3, int paramInt4)
/*     */   {
/* 195 */     int i = paramInt4 - paramInt2;
/* 196 */     int j = paramInt1 + paramInt2;
/* 197 */     int k = this.count - j;
/* 198 */     int m = j + i;
/* 199 */     if (this.count + i >= this.data.length)
/*     */     {
/* 201 */       int n = Math.max(2 * this.data.length, this.count + i);
/* 202 */       char[] arrayOfChar = new char[n];
/* 203 */       System.arraycopy(this.data, 0, arrayOfChar, 0, paramInt1);
/* 204 */       System.arraycopy(paramArrayOfChar, paramInt3, arrayOfChar, paramInt1, paramInt4);
/* 205 */       System.arraycopy(this.data, j, arrayOfChar, m, k);
/* 206 */       this.data = arrayOfChar;
/*     */     }
/*     */     else {
/* 209 */       System.arraycopy(this.data, j, this.data, m, k);
/* 210 */       System.arraycopy(paramArrayOfChar, paramInt3, this.data, paramInt1, paramInt4);
/*     */     }
/* 212 */     this.count += i;
/*     */   }
/*     */ 
/*     */   void resize(int paramInt) {
/* 216 */     char[] arrayOfChar = new char[paramInt];
/* 217 */     System.arraycopy(this.data, 0, arrayOfChar, 0, Math.min(paramInt, this.count));
/* 218 */     this.data = arrayOfChar;
/*     */   }
/*     */ 
/*     */   synchronized void updateMarksForInsert(int paramInt1, int paramInt2) {
/* 222 */     if (paramInt1 == 0)
/*     */     {
/* 225 */       paramInt1 = 1;
/*     */     }
/* 227 */     int i = this.marks.size();
/* 228 */     for (int j = 0; j < i; j++) {
/* 229 */       PosRec localPosRec = (PosRec)this.marks.elementAt(j);
/* 230 */       if (localPosRec.unused)
/*     */       {
/* 232 */         this.marks.removeElementAt(j);
/* 233 */         j--;
/* 234 */         i--;
/* 235 */       } else if (localPosRec.offset >= paramInt1) {
/* 236 */         localPosRec.offset += paramInt2;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   synchronized void updateMarksForRemove(int paramInt1, int paramInt2) {
/* 242 */     int i = this.marks.size();
/* 243 */     for (int j = 0; j < i; j++) {
/* 244 */       PosRec localPosRec = (PosRec)this.marks.elementAt(j);
/* 245 */       if (localPosRec.unused)
/*     */       {
/* 247 */         this.marks.removeElementAt(j);
/* 248 */         j--;
/* 249 */         i--;
/* 250 */       } else if (localPosRec.offset >= paramInt1 + paramInt2) {
/* 251 */         localPosRec.offset -= paramInt2;
/* 252 */       } else if (localPosRec.offset >= paramInt1) {
/* 253 */         localPosRec.offset = paramInt1;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected Vector getPositionsInRange(Vector paramVector, int paramInt1, int paramInt2)
/*     */   {
/* 275 */     int i = this.marks.size();
/* 276 */     int j = paramInt1 + paramInt2;
/* 277 */     Vector localVector = paramVector == null ? new Vector() : paramVector;
/* 278 */     for (int k = 0; k < i; k++) {
/* 279 */       PosRec localPosRec = (PosRec)this.marks.elementAt(k);
/* 280 */       if (localPosRec.unused)
/*     */       {
/* 282 */         this.marks.removeElementAt(k);
/* 283 */         k--;
/* 284 */         i--;
/* 285 */       } else if ((localPosRec.offset >= paramInt1) && (localPosRec.offset <= j)) {
/* 286 */         localVector.addElement(new UndoPosRef(localPosRec));
/*     */       }
/*     */     }
/* 288 */     return localVector;
/*     */   }
/*     */ 
/*     */   protected void updateUndoPositions(Vector paramVector)
/*     */   {
/* 301 */     for (int i = paramVector.size() - 1; i >= 0; i--) {
/* 302 */       UndoPosRef localUndoPosRef = (UndoPosRef)paramVector.elementAt(i);
/*     */ 
/* 304 */       if (localUndoPosRef.rec.unused) {
/* 305 */         paramVector.removeElementAt(i);
/*     */       }
/*     */       else
/* 308 */         localUndoPosRef.resetLocation();
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
/* 394 */       this.offset = paramInt1;
/*     */       int i;
/* 395 */       this.length = i;
/*     */     }
/*     */ 
/*     */     public void undo() throws CannotUndoException {
/* 399 */       super.undo();
/*     */       try {
/* 401 */         synchronized (StringContent.this)
/*     */         {
/* 403 */           if (StringContent.this.marks != null)
/* 404 */             this.posRefs = StringContent.this.getPositionsInRange(null, this.offset, this.length);
/* 405 */           this.string = StringContent.this.getString(this.offset, this.length);
/* 406 */           StringContent.this.remove(this.offset, this.length);
/*     */         }
/*     */       } catch (BadLocationException localBadLocationException) {
/* 409 */         throw new CannotUndoException();
/*     */       }
/*     */     }
/*     */ 
/*     */     public void redo() throws CannotRedoException {
/* 414 */       super.redo();
/*     */       try {
/* 416 */         synchronized (StringContent.this) {
/* 417 */           StringContent.this.insertString(this.offset, this.string);
/* 418 */           this.string = null;
/*     */ 
/* 420 */           if (this.posRefs != null) {
/* 421 */             StringContent.this.updateUndoPositions(this.posRefs);
/* 422 */             this.posRefs = null;
/*     */           }
/*     */         }
/*     */       } catch (BadLocationException localBadLocationException) {
/* 426 */         throw new CannotRedoException();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   final class PosRec
/*     */   {
/*     */     int offset;
/*     */     boolean unused;
/*     */ 
/*     */     PosRec(int arg2)
/*     */     {
/*     */       int i;
/* 327 */       this.offset = i;
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
/* 449 */       this.offset = paramString;
/*     */       Object localObject;
/* 450 */       this.string = localObject;
/* 451 */       this.length = localObject.length();
/* 452 */       if (StringContent.this.marks != null)
/* 453 */         this.posRefs = StringContent.this.getPositionsInRange(null, paramString, this.length);
/*     */     }
/*     */ 
/*     */     public void undo() throws CannotUndoException {
/* 457 */       super.undo();
/*     */       try {
/* 459 */         synchronized (StringContent.this) {
/* 460 */           StringContent.this.insertString(this.offset, this.string);
/*     */ 
/* 462 */           if (this.posRefs != null) {
/* 463 */             StringContent.this.updateUndoPositions(this.posRefs);
/* 464 */             this.posRefs = null;
/*     */           }
/* 466 */           this.string = null;
/*     */         }
/*     */       } catch (BadLocationException localBadLocationException) {
/* 469 */         throw new CannotUndoException();
/*     */       }
/*     */     }
/*     */ 
/*     */     public void redo() throws CannotRedoException {
/* 474 */       super.redo();
/*     */       try {
/* 476 */         synchronized (StringContent.this) {
/* 477 */           this.string = StringContent.this.getString(this.offset, this.length);
/*     */ 
/* 479 */           if (StringContent.this.marks != null)
/* 480 */             this.posRefs = StringContent.this.getPositionsInRange(null, this.offset, this.length);
/* 481 */           StringContent.this.remove(this.offset, this.length);
/*     */         }
/*     */       } catch (BadLocationException localBadLocationException) {
/* 484 */         throw new CannotRedoException();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   final class StickyPosition
/*     */     implements Position
/*     */   {
/*     */     StringContent.PosRec rec;
/*     */ 
/*     */     StickyPosition(int arg2)
/*     */     {
/*     */       int i;
/* 343 */       this.rec = new StringContent.PosRec(StringContent.this, i);
/* 344 */       StringContent.this.marks.addElement(this.rec);
/*     */     }
/*     */ 
/*     */     public int getOffset() {
/* 348 */       return this.rec.offset;
/*     */     }
/*     */ 
/*     */     protected void finalize()
/*     */       throws Throwable
/*     */     {
/* 354 */       this.rec.unused = true;
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 358 */       return Integer.toString(getOffset());
/*     */     }
/*     */   }
/*     */ 
/*     */   final class UndoPosRef
/*     */   {
/*     */     protected int undoLocation;
/*     */     protected StringContent.PosRec rec;
/*     */ 
/*     */     UndoPosRef(StringContent.PosRec arg2)
/*     */     {
/*     */       Object localObject;
/* 370 */       this.rec = localObject;
/* 371 */       this.undoLocation = localObject.offset;
/*     */     }
/*     */ 
/*     */     protected void resetLocation()
/*     */     {
/* 379 */       this.rec.offset = this.undoLocation;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.StringContent
 * JD-Core Version:    0.6.2
 */