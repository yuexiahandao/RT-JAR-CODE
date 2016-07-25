/*     */ package javax.swing.undo;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class CompoundEdit extends AbstractUndoableEdit
/*     */ {
/*     */   boolean inProgress;
/*     */   protected Vector<UndoableEdit> edits;
/*     */ 
/*     */   public CompoundEdit()
/*     */   {
/*  49 */     this.inProgress = true;
/*  50 */     this.edits = new Vector();
/*     */   }
/*     */ 
/*     */   public void undo()
/*     */     throws CannotUndoException
/*     */   {
/*  59 */     super.undo();
/*  60 */     int i = this.edits.size();
/*  61 */     while (i-- > 0) {
/*  62 */       UndoableEdit localUndoableEdit = (UndoableEdit)this.edits.elementAt(i);
/*  63 */       localUndoableEdit.undo();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void redo()
/*     */     throws CannotRedoException
/*     */   {
/*  73 */     super.redo();
/*  74 */     Enumeration localEnumeration = this.edits.elements();
/*  75 */     while (localEnumeration.hasMoreElements())
/*  76 */       ((UndoableEdit)localEnumeration.nextElement()).redo();
/*     */   }
/*     */ 
/*     */   protected UndoableEdit lastEdit()
/*     */   {
/*  86 */     int i = this.edits.size();
/*  87 */     if (i > 0) {
/*  88 */       return (UndoableEdit)this.edits.elementAt(i - 1);
/*     */     }
/*  90 */     return null;
/*     */   }
/*     */ 
/*     */   public void die()
/*     */   {
/*  98 */     int i = this.edits.size();
/*  99 */     for (int j = i - 1; j >= 0; j--)
/*     */     {
/* 101 */       UndoableEdit localUndoableEdit = (UndoableEdit)this.edits.elementAt(j);
/*     */ 
/* 104 */       localUndoableEdit.die();
/*     */     }
/* 106 */     super.die();
/*     */   }
/*     */ 
/*     */   public boolean addEdit(UndoableEdit paramUndoableEdit)
/*     */   {
/* 125 */     if (!this.inProgress) {
/* 126 */       return false;
/*     */     }
/* 128 */     UndoableEdit localUndoableEdit = lastEdit();
/*     */ 
/* 135 */     if (localUndoableEdit == null) {
/* 136 */       this.edits.addElement(paramUndoableEdit);
/*     */     }
/* 138 */     else if (!localUndoableEdit.addEdit(paramUndoableEdit)) {
/* 139 */       if (paramUndoableEdit.replaceEdit(localUndoableEdit)) {
/* 140 */         this.edits.removeElementAt(this.edits.size() - 1);
/*     */       }
/* 142 */       this.edits.addElement(paramUndoableEdit);
/*     */     }
/*     */ 
/* 145 */     return true;
/*     */   }
/*     */ 
/*     */   public void end()
/*     */   {
/* 156 */     this.inProgress = false;
/*     */   }
/*     */ 
/*     */   public boolean canUndo()
/*     */   {
/* 166 */     return (!isInProgress()) && (super.canUndo());
/*     */   }
/*     */ 
/*     */   public boolean canRedo()
/*     */   {
/* 176 */     return (!isInProgress()) && (super.canRedo());
/*     */   }
/*     */ 
/*     */   public boolean isInProgress()
/*     */   {
/* 187 */     return this.inProgress;
/*     */   }
/*     */ 
/*     */   public boolean isSignificant()
/*     */   {
/* 196 */     Enumeration localEnumeration = this.edits.elements();
/* 197 */     while (localEnumeration.hasMoreElements()) {
/* 198 */       if (((UndoableEdit)localEnumeration.nextElement()).isSignificant()) {
/* 199 */         return true;
/*     */       }
/*     */     }
/* 202 */     return false;
/*     */   }
/*     */ 
/*     */   public String getPresentationName()
/*     */   {
/* 212 */     UndoableEdit localUndoableEdit = lastEdit();
/* 213 */     if (localUndoableEdit != null) {
/* 214 */       return localUndoableEdit.getPresentationName();
/*     */     }
/* 216 */     return super.getPresentationName();
/*     */   }
/*     */ 
/*     */   public String getUndoPresentationName()
/*     */   {
/* 227 */     UndoableEdit localUndoableEdit = lastEdit();
/* 228 */     if (localUndoableEdit != null) {
/* 229 */       return localUndoableEdit.getUndoPresentationName();
/*     */     }
/* 231 */     return super.getUndoPresentationName();
/*     */   }
/*     */ 
/*     */   public String getRedoPresentationName()
/*     */   {
/* 242 */     UndoableEdit localUndoableEdit = lastEdit();
/* 243 */     if (localUndoableEdit != null) {
/* 244 */       return localUndoableEdit.getRedoPresentationName();
/*     */     }
/* 246 */     return super.getRedoPresentationName();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 258 */     return super.toString() + " inProgress: " + this.inProgress + " edits: " + this.edits;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.undo.CompoundEdit
 * JD-Core Version:    0.6.2
 */