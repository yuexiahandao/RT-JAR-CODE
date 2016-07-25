/*    */ package com.sun.org.apache.xerces.internal.impl.validation;
/*    */ 
/*    */ import java.util.Vector;
/*    */ 
/*    */ public class ValidationManager
/*    */ {
/* 38 */   protected final Vector fVSs = new Vector();
/* 39 */   protected boolean fGrammarFound = false;
/*    */ 
/* 44 */   protected boolean fCachedDTD = false;
/*    */ 
/*    */   public final void addValidationState(ValidationState vs)
/*    */   {
/* 51 */     this.fVSs.addElement(vs);
/*    */   }
/*    */ 
/*    */   public final void setEntityState(EntityState state)
/*    */   {
/* 58 */     for (int i = this.fVSs.size() - 1; i >= 0; i--)
/* 59 */       ((ValidationState)this.fVSs.elementAt(i)).setEntityState(state);
/*    */   }
/*    */ 
/*    */   public final void setGrammarFound(boolean grammar)
/*    */   {
/* 64 */     this.fGrammarFound = grammar;
/*    */   }
/*    */ 
/*    */   public final boolean isGrammarFound() {
/* 68 */     return this.fGrammarFound;
/*    */   }
/*    */ 
/*    */   public final void setCachedDTD(boolean cachedDTD) {
/* 72 */     this.fCachedDTD = cachedDTD;
/*    */   }
/*    */ 
/*    */   public final boolean isCachedDTD() {
/* 76 */     return this.fCachedDTD;
/*    */   }
/*    */ 
/*    */   public final void reset()
/*    */   {
/* 81 */     this.fVSs.removeAllElements();
/* 82 */     this.fGrammarFound = false;
/* 83 */     this.fCachedDTD = false;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.validation.ValidationManager
 * JD-Core Version:    0.6.2
 */