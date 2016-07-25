/*     */ package javax.swing.undo;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class StateEdit extends AbstractUndoableEdit
/*     */ {
/*     */   protected static final String RCSID = "$Id: StateEdit.java,v 1.6 1997/10/01 20:05:51 sandipc Exp $";
/*     */   protected StateEditable object;
/*     */   protected Hashtable<Object, Object> preState;
/*     */   protected Hashtable<Object, Object> postState;
/*     */   protected String undoRedoName;
/*     */ 
/*     */   public StateEdit(StateEditable paramStateEditable)
/*     */   {
/* 101 */     init(paramStateEditable, null);
/*     */   }
/*     */ 
/*     */   public StateEdit(StateEditable paramStateEditable, String paramString)
/*     */   {
/* 114 */     init(paramStateEditable, paramString);
/*     */   }
/*     */ 
/*     */   protected void init(StateEditable paramStateEditable, String paramString) {
/* 118 */     this.object = paramStateEditable;
/* 119 */     this.preState = new Hashtable(11);
/* 120 */     this.object.storeState(this.preState);
/* 121 */     this.postState = null;
/* 122 */     this.undoRedoName = paramString;
/*     */   }
/*     */ 
/*     */   public void end()
/*     */   {
/* 136 */     this.postState = new Hashtable(11);
/* 137 */     this.object.storeState(this.postState);
/* 138 */     removeRedundantState();
/*     */   }
/*     */ 
/*     */   public void undo()
/*     */   {
/* 145 */     super.undo();
/* 146 */     this.object.restoreState(this.preState);
/*     */   }
/*     */ 
/*     */   public void redo()
/*     */   {
/* 153 */     super.redo();
/* 154 */     this.object.restoreState(this.postState);
/*     */   }
/*     */ 
/*     */   public String getPresentationName()
/*     */   {
/* 161 */     return this.undoRedoName;
/*     */   }
/*     */ 
/*     */   protected void removeRedundantState()
/*     */   {
/* 173 */     Vector localVector = new Vector();
/* 174 */     Enumeration localEnumeration = this.preState.keys();
/*     */ 
/* 177 */     while (localEnumeration.hasMoreElements()) {
/* 178 */       Object localObject1 = localEnumeration.nextElement();
/* 179 */       if ((this.postState.containsKey(localObject1)) && (this.postState.get(localObject1).equals(this.preState.get(localObject1))))
/*     */       {
/* 181 */         localVector.addElement(localObject1);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 186 */     for (int i = localVector.size() - 1; i >= 0; i--) {
/* 187 */       Object localObject2 = localVector.elementAt(i);
/* 188 */       this.preState.remove(localObject2);
/* 189 */       this.postState.remove(localObject2);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.undo.StateEdit
 * JD-Core Version:    0.6.2
 */