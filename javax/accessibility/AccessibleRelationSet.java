/*     */ package javax.accessibility;
/*     */ 
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class AccessibleRelationSet
/*     */ {
/*  56 */   protected Vector<AccessibleRelation> relations = null;
/*     */ 
/*     */   public AccessibleRelationSet()
/*     */   {
/*  62 */     this.relations = null;
/*     */   }
/*     */ 
/*     */   public AccessibleRelationSet(AccessibleRelation[] paramArrayOfAccessibleRelation)
/*     */   {
/*  73 */     if (paramArrayOfAccessibleRelation.length != 0) {
/*  74 */       this.relations = new Vector(paramArrayOfAccessibleRelation.length);
/*  75 */       for (int i = 0; i < paramArrayOfAccessibleRelation.length; i++)
/*  76 */         add(paramArrayOfAccessibleRelation[i]);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean add(AccessibleRelation paramAccessibleRelation)
/*     */   {
/*  92 */     if (this.relations == null) {
/*  93 */       this.relations = new Vector();
/*     */     }
/*     */ 
/*  97 */     AccessibleRelation localAccessibleRelation = get(paramAccessibleRelation.getKey());
/*  98 */     if (localAccessibleRelation == null) {
/*  99 */       this.relations.addElement(paramAccessibleRelation);
/* 100 */       return true;
/*     */     }
/* 102 */     Object[] arrayOfObject1 = localAccessibleRelation.getTarget();
/* 103 */     Object[] arrayOfObject2 = paramAccessibleRelation.getTarget();
/* 104 */     int i = arrayOfObject1.length + arrayOfObject2.length;
/* 105 */     Object[] arrayOfObject3 = new Object[i];
/* 106 */     for (int j = 0; j < arrayOfObject1.length; j++) {
/* 107 */       arrayOfObject3[j] = arrayOfObject1[j];
/*     */     }
/* 109 */     j = arrayOfObject1.length; for (int k = 0; 
/* 110 */       j < i; 
/* 111 */       k++) {
/* 112 */       arrayOfObject3[j] = arrayOfObject2[k];
/*     */ 
/* 111 */       j++;
/*     */     }
/*     */ 
/* 114 */     localAccessibleRelation.setTarget(arrayOfObject3);
/*     */ 
/* 116 */     return true;
/*     */   }
/*     */ 
/*     */   public void addAll(AccessibleRelation[] paramArrayOfAccessibleRelation)
/*     */   {
/* 126 */     if (paramArrayOfAccessibleRelation.length != 0) {
/* 127 */       if (this.relations == null) {
/* 128 */         this.relations = new Vector(paramArrayOfAccessibleRelation.length);
/*     */       }
/* 130 */       for (int i = 0; i < paramArrayOfAccessibleRelation.length; i++)
/* 131 */         add(paramArrayOfAccessibleRelation[i]);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean remove(AccessibleRelation paramAccessibleRelation)
/*     */   {
/* 148 */     if (this.relations == null) {
/* 149 */       return false;
/*     */     }
/* 151 */     return this.relations.removeElement(paramAccessibleRelation);
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 159 */     if (this.relations != null)
/* 160 */       this.relations.removeAllElements();
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 168 */     if (this.relations == null) {
/* 169 */       return 0;
/*     */     }
/* 171 */     return this.relations.size();
/*     */   }
/*     */ 
/*     */   public boolean contains(String paramString)
/*     */   {
/* 182 */     return get(paramString) != null;
/*     */   }
/*     */ 
/*     */   public AccessibleRelation get(String paramString)
/*     */   {
/* 192 */     if (this.relations == null) {
/* 193 */       return null;
/*     */     }
/* 195 */     int i = this.relations.size();
/* 196 */     for (int j = 0; j < i; j++) {
/* 197 */       AccessibleRelation localAccessibleRelation = (AccessibleRelation)this.relations.elementAt(j);
/*     */ 
/* 199 */       if ((localAccessibleRelation != null) && (localAccessibleRelation.getKey().equals(paramString))) {
/* 200 */         return localAccessibleRelation;
/*     */       }
/*     */     }
/* 203 */     return null;
/*     */   }
/*     */ 
/*     */   public AccessibleRelation[] toArray()
/*     */   {
/* 212 */     if (this.relations == null) {
/* 213 */       return new AccessibleRelation[0];
/*     */     }
/* 215 */     AccessibleRelation[] arrayOfAccessibleRelation = new AccessibleRelation[this.relations.size()];
/*     */ 
/* 217 */     for (int i = 0; i < arrayOfAccessibleRelation.length; i++) {
/* 218 */       arrayOfAccessibleRelation[i] = ((AccessibleRelation)this.relations.elementAt(i));
/*     */     }
/* 220 */     return arrayOfAccessibleRelation;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 232 */     String str = "";
/* 233 */     if ((this.relations != null) && (this.relations.size() > 0)) {
/* 234 */       str = ((AccessibleRelation)this.relations.elementAt(0)).toDisplayString();
/* 235 */       for (int i = 1; i < this.relations.size(); i++) {
/* 236 */         str = str + "," + ((AccessibleRelation)this.relations.elementAt(i)).toDisplayString();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 241 */     return str;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.accessibility.AccessibleRelationSet
 * JD-Core Version:    0.6.2
 */