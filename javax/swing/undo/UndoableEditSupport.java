/*     */ package javax.swing.undo;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.Vector;
/*     */ import javax.swing.event.UndoableEditEvent;
/*     */ import javax.swing.event.UndoableEditListener;
/*     */ 
/*     */ public class UndoableEditSupport
/*     */ {
/*     */   protected int updateLevel;
/*     */   protected CompoundEdit compoundEdit;
/*     */   protected Vector<UndoableEditListener> listeners;
/*     */   protected Object realSource;
/*     */ 
/*     */   public UndoableEditSupport()
/*     */   {
/*  46 */     this(null);
/*     */   }
/*     */ 
/*     */   public UndoableEditSupport(Object paramObject)
/*     */   {
/*  55 */     this.realSource = (paramObject == null ? this : paramObject);
/*  56 */     this.updateLevel = 0;
/*  57 */     this.compoundEdit = null;
/*  58 */     this.listeners = new Vector();
/*     */   }
/*     */ 
/*     */   public synchronized void addUndoableEditListener(UndoableEditListener paramUndoableEditListener)
/*     */   {
/*  69 */     this.listeners.addElement(paramUndoableEditListener);
/*     */   }
/*     */ 
/*     */   public synchronized void removeUndoableEditListener(UndoableEditListener paramUndoableEditListener)
/*     */   {
/*  80 */     this.listeners.removeElement(paramUndoableEditListener);
/*     */   }
/*     */ 
/*     */   public synchronized UndoableEditListener[] getUndoableEditListeners()
/*     */   {
/*  92 */     return (UndoableEditListener[])this.listeners.toArray(new UndoableEditListener[0]);
/*     */   }
/*     */ 
/*     */   protected void _postEdit(UndoableEdit paramUndoableEdit)
/*     */   {
/* 101 */     UndoableEditEvent localUndoableEditEvent = new UndoableEditEvent(this.realSource, paramUndoableEdit);
/* 102 */     Enumeration localEnumeration = ((Vector)this.listeners.clone()).elements();
/* 103 */     while (localEnumeration.hasMoreElements())
/* 104 */       ((UndoableEditListener)localEnumeration.nextElement()).undoableEditHappened(localUndoableEditEvent);
/*     */   }
/*     */ 
/*     */   public synchronized void postEdit(UndoableEdit paramUndoableEdit)
/*     */   {
/* 115 */     if (this.updateLevel == 0) {
/* 116 */       _postEdit(paramUndoableEdit);
/*     */     }
/*     */     else
/* 119 */       this.compoundEdit.addEdit(paramUndoableEdit);
/*     */   }
/*     */ 
/*     */   public int getUpdateLevel()
/*     */   {
/* 129 */     return this.updateLevel;
/*     */   }
/*     */ 
/*     */   public synchronized void beginUpdate()
/*     */   {
/* 136 */     if (this.updateLevel == 0) {
/* 137 */       this.compoundEdit = createCompoundEdit();
/*     */     }
/* 139 */     this.updateLevel += 1;
/*     */   }
/*     */ 
/*     */   protected CompoundEdit createCompoundEdit()
/*     */   {
/* 147 */     return new CompoundEdit();
/*     */   }
/*     */ 
/*     */   public synchronized void endUpdate()
/*     */   {
/* 156 */     this.updateLevel -= 1;
/* 157 */     if (this.updateLevel == 0) {
/* 158 */       this.compoundEdit.end();
/* 159 */       _postEdit(this.compoundEdit);
/* 160 */       this.compoundEdit = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 171 */     return super.toString() + " updateLevel: " + this.updateLevel + " listeners: " + this.listeners + " compoundEdit: " + this.compoundEdit;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.undo.UndoableEditSupport
 * JD-Core Version:    0.6.2
 */