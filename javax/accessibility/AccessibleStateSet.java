/*     */ package javax.accessibility;
/*     */ 
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class AccessibleStateSet
/*     */ {
/*  54 */   protected Vector<AccessibleState> states = null;
/*     */ 
/*     */   public AccessibleStateSet()
/*     */   {
/*  60 */     this.states = null;
/*     */   }
/*     */ 
/*     */   public AccessibleStateSet(AccessibleState[] paramArrayOfAccessibleState)
/*     */   {
/*  70 */     if (paramArrayOfAccessibleState.length != 0) {
/*  71 */       this.states = new Vector(paramArrayOfAccessibleState.length);
/*  72 */       for (int i = 0; i < paramArrayOfAccessibleState.length; i++)
/*  73 */         if (!this.states.contains(paramArrayOfAccessibleState[i]))
/*  74 */           this.states.addElement(paramArrayOfAccessibleState[i]);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean add(AccessibleState paramAccessibleState)
/*     */   {
/*  94 */     if (this.states == null) {
/*  95 */       this.states = new Vector();
/*     */     }
/*     */ 
/*  98 */     if (!this.states.contains(paramAccessibleState)) {
/*  99 */       this.states.addElement(paramAccessibleState);
/* 100 */       return true;
/*     */     }
/* 102 */     return false;
/*     */   }
/*     */ 
/*     */   public void addAll(AccessibleState[] paramArrayOfAccessibleState)
/*     */   {
/* 112 */     if (paramArrayOfAccessibleState.length != 0) {
/* 113 */       if (this.states == null) {
/* 114 */         this.states = new Vector(paramArrayOfAccessibleState.length);
/*     */       }
/* 116 */       for (int i = 0; i < paramArrayOfAccessibleState.length; i++)
/* 117 */         if (!this.states.contains(paramArrayOfAccessibleState[i]))
/* 118 */           this.states.addElement(paramArrayOfAccessibleState[i]);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean remove(AccessibleState paramAccessibleState)
/*     */   {
/* 135 */     if (this.states == null) {
/* 136 */       return false;
/*     */     }
/* 138 */     return this.states.removeElement(paramAccessibleState);
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 146 */     if (this.states != null)
/* 147 */       this.states.removeAllElements();
/*     */   }
/*     */ 
/*     */   public boolean contains(AccessibleState paramAccessibleState)
/*     */   {
/* 157 */     if (this.states == null) {
/* 158 */       return false;
/*     */     }
/* 160 */     return this.states.contains(paramAccessibleState);
/*     */   }
/*     */ 
/*     */   public AccessibleState[] toArray()
/*     */   {
/* 169 */     if (this.states == null) {
/* 170 */       return new AccessibleState[0];
/*     */     }
/* 172 */     AccessibleState[] arrayOfAccessibleState = new AccessibleState[this.states.size()];
/* 173 */     for (int i = 0; i < arrayOfAccessibleState.length; i++) {
/* 174 */       arrayOfAccessibleState[i] = ((AccessibleState)this.states.elementAt(i));
/*     */     }
/* 176 */     return arrayOfAccessibleState;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 188 */     String str = null;
/* 189 */     if ((this.states != null) && (this.states.size() > 0)) {
/* 190 */       str = ((AccessibleState)this.states.elementAt(0)).toDisplayString();
/* 191 */       for (int i = 1; i < this.states.size(); i++) {
/* 192 */         str = str + "," + ((AccessibleState)this.states.elementAt(i)).toDisplayString();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 197 */     return str;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.accessibility.AccessibleStateSet
 * JD-Core Version:    0.6.2
 */