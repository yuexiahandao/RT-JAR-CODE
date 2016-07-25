/*     */ package javax.imageio.spi;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Map;
/*     */ 
/*     */ class PartialOrderIterator
/*     */   implements Iterator
/*     */ {
/* 171 */   LinkedList zeroList = new LinkedList();
/* 172 */   Map inDegrees = new HashMap();
/*     */ 
/*     */   public PartialOrderIterator(Iterator paramIterator)
/*     */   {
/* 176 */     while (paramIterator.hasNext()) {
/* 177 */       DigraphNode localDigraphNode = (DigraphNode)paramIterator.next();
/* 178 */       int i = localDigraphNode.getInDegree();
/* 179 */       this.inDegrees.put(localDigraphNode, new Integer(i));
/*     */ 
/* 182 */       if (i == 0)
/* 183 */         this.zeroList.add(localDigraphNode);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean hasNext()
/*     */   {
/* 189 */     return !this.zeroList.isEmpty();
/*     */   }
/*     */ 
/*     */   public Object next() {
/* 193 */     DigraphNode localDigraphNode1 = (DigraphNode)this.zeroList.removeFirst();
/*     */ 
/* 196 */     Iterator localIterator = localDigraphNode1.getOutNodes();
/* 197 */     while (localIterator.hasNext()) {
/* 198 */       DigraphNode localDigraphNode2 = (DigraphNode)localIterator.next();
/* 199 */       int i = ((Integer)this.inDegrees.get(localDigraphNode2)).intValue() - 1;
/* 200 */       this.inDegrees.put(localDigraphNode2, new Integer(i));
/*     */ 
/* 203 */       if (i == 0) {
/* 204 */         this.zeroList.add(localDigraphNode2);
/*     */       }
/*     */     }
/*     */ 
/* 208 */     return localDigraphNode1.getData();
/*     */   }
/*     */ 
/*     */   public void remove() {
/* 212 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.imageio.spi.PartialOrderIterator
 * JD-Core Version:    0.6.2
 */