/*     */ package javax.imageio.spi;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ 
/*     */ class DigraphNode
/*     */   implements Cloneable, Serializable
/*     */ {
/*     */   protected Object data;
/*  51 */   protected Set outNodes = new HashSet();
/*     */ 
/*  54 */   protected int inDegree = 0;
/*     */ 
/*  60 */   private Set inNodes = new HashSet();
/*     */ 
/*     */   public DigraphNode(Object paramObject) {
/*  63 */     this.data = paramObject;
/*     */   }
/*     */ 
/*     */   public Object getData()
/*     */   {
/*  68 */     return this.data;
/*     */   }
/*     */ 
/*     */   public Iterator getOutNodes()
/*     */   {
/*  76 */     return this.outNodes.iterator();
/*     */   }
/*     */ 
/*     */   public boolean addEdge(DigraphNode paramDigraphNode)
/*     */   {
/*  89 */     if (this.outNodes.contains(paramDigraphNode)) {
/*  90 */       return false;
/*     */     }
/*     */ 
/*  93 */     this.outNodes.add(paramDigraphNode);
/*  94 */     paramDigraphNode.inNodes.add(this);
/*  95 */     paramDigraphNode.incrementInDegree();
/*  96 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean hasEdge(DigraphNode paramDigraphNode)
/*     */   {
/* 108 */     return this.outNodes.contains(paramDigraphNode);
/*     */   }
/*     */ 
/*     */   public boolean removeEdge(DigraphNode paramDigraphNode)
/*     */   {
/* 119 */     if (!this.outNodes.contains(paramDigraphNode)) {
/* 120 */       return false;
/*     */     }
/*     */ 
/* 123 */     this.outNodes.remove(paramDigraphNode);
/* 124 */     paramDigraphNode.inNodes.remove(this);
/* 125 */     paramDigraphNode.decrementInDegree();
/* 126 */     return true;
/*     */   }
/*     */ 
/*     */   public void dispose()
/*     */   {
/* 134 */     Object[] arrayOfObject1 = this.inNodes.toArray();
/* 135 */     for (int i = 0; i < arrayOfObject1.length; i++) {
/* 136 */       DigraphNode localDigraphNode1 = (DigraphNode)arrayOfObject1[i];
/* 137 */       localDigraphNode1.removeEdge(this);
/*     */     }
/*     */ 
/* 140 */     Object[] arrayOfObject2 = this.outNodes.toArray();
/* 141 */     for (int j = 0; j < arrayOfObject2.length; j++) {
/* 142 */       DigraphNode localDigraphNode2 = (DigraphNode)arrayOfObject2[j];
/* 143 */       removeEdge(localDigraphNode2);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getInDegree()
/*     */   {
/* 149 */     return this.inDegree;
/*     */   }
/*     */ 
/*     */   private void incrementInDegree()
/*     */   {
/* 154 */     this.inDegree += 1;
/*     */   }
/*     */ 
/*     */   private void decrementInDegree()
/*     */   {
/* 159 */     this.inDegree -= 1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.imageio.spi.DigraphNode
 * JD-Core Version:    0.6.2
 */