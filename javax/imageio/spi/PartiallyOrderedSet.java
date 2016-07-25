/*     */ package javax.imageio.spi;
/*     */ 
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ class PartiallyOrderedSet extends AbstractSet
/*     */ {
/*  61 */   private Map poNodes = new HashMap();
/*     */ 
/*  64 */   private Set nodes = this.poNodes.keySet();
/*     */ 
/*     */   public int size()
/*     */   {
/*  72 */     return this.nodes.size();
/*     */   }
/*     */ 
/*     */   public boolean contains(Object paramObject) {
/*  76 */     return this.nodes.contains(paramObject);
/*     */   }
/*     */ 
/*     */   public Iterator iterator()
/*     */   {
/*  85 */     return new PartialOrderIterator(this.poNodes.values().iterator());
/*     */   }
/*     */ 
/*     */   public boolean add(Object paramObject)
/*     */   {
/*  93 */     if (this.nodes.contains(paramObject)) {
/*  94 */       return false;
/*     */     }
/*     */ 
/*  97 */     DigraphNode localDigraphNode = new DigraphNode(paramObject);
/*  98 */     this.poNodes.put(paramObject, localDigraphNode);
/*  99 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean remove(Object paramObject)
/*     */   {
/* 107 */     DigraphNode localDigraphNode = (DigraphNode)this.poNodes.get(paramObject);
/* 108 */     if (localDigraphNode == null) {
/* 109 */       return false;
/*     */     }
/*     */ 
/* 112 */     this.poNodes.remove(paramObject);
/* 113 */     localDigraphNode.dispose();
/* 114 */     return true;
/*     */   }
/*     */ 
/*     */   public void clear() {
/* 118 */     this.poNodes.clear();
/*     */   }
/*     */ 
/*     */   public boolean setOrdering(Object paramObject1, Object paramObject2)
/*     */   {
/* 131 */     DigraphNode localDigraphNode1 = (DigraphNode)this.poNodes.get(paramObject1);
/*     */ 
/* 133 */     DigraphNode localDigraphNode2 = (DigraphNode)this.poNodes.get(paramObject2);
/*     */ 
/* 136 */     localDigraphNode2.removeEdge(localDigraphNode1);
/* 137 */     return localDigraphNode1.addEdge(localDigraphNode2);
/*     */   }
/*     */ 
/*     */   public boolean unsetOrdering(Object paramObject1, Object paramObject2)
/*     */   {
/* 146 */     DigraphNode localDigraphNode1 = (DigraphNode)this.poNodes.get(paramObject1);
/*     */ 
/* 148 */     DigraphNode localDigraphNode2 = (DigraphNode)this.poNodes.get(paramObject2);
/*     */ 
/* 151 */     return (localDigraphNode1.removeEdge(localDigraphNode2)) || (localDigraphNode2.removeEdge(localDigraphNode1));
/*     */   }
/*     */ 
/*     */   public boolean hasOrdering(Object paramObject1, Object paramObject2)
/*     */   {
/* 160 */     DigraphNode localDigraphNode1 = (DigraphNode)this.poNodes.get(paramObject1);
/*     */ 
/* 162 */     DigraphNode localDigraphNode2 = (DigraphNode)this.poNodes.get(paramObject2);
/*     */ 
/* 165 */     return localDigraphNode1.hasEdge(localDigraphNode2);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.imageio.spi.PartiallyOrderedSet
 * JD-Core Version:    0.6.2
 */