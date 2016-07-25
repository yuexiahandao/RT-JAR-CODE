/*     */ package javax.imageio.metadata;
/*     */ 
/*     */ import java.util.List;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ 
/*     */ class IIONodeList
/*     */   implements NodeList
/*     */ {
/*     */   List nodes;
/*     */ 
/*     */   public IIONodeList(List paramList)
/*     */   {
/* 116 */     this.nodes = paramList;
/*     */   }
/*     */ 
/*     */   public int getLength() {
/* 120 */     return this.nodes.size();
/*     */   }
/*     */ 
/*     */   public Node item(int paramInt) {
/* 124 */     if ((paramInt < 0) || (paramInt > this.nodes.size())) {
/* 125 */       return null;
/*     */     }
/* 127 */     return (Node)this.nodes.get(paramInt);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.imageio.metadata.IIONodeList
 * JD-Core Version:    0.6.2
 */