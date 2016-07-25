/*     */ package javax.imageio.metadata;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.w3c.dom.DOMException;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ class IIONamedNodeMap
/*     */   implements NamedNodeMap
/*     */ {
/*     */   List nodes;
/*     */ 
/*     */   public IIONamedNodeMap(List paramList)
/*     */   {
/*  55 */     this.nodes = paramList;
/*     */   }
/*     */ 
/*     */   public int getLength() {
/*  59 */     return this.nodes.size();
/*     */   }
/*     */ 
/*     */   public Node getNamedItem(String paramString) {
/*  63 */     Iterator localIterator = this.nodes.iterator();
/*  64 */     while (localIterator.hasNext()) {
/*  65 */       Node localNode = (Node)localIterator.next();
/*  66 */       if (paramString.equals(localNode.getNodeName())) {
/*  67 */         return localNode;
/*     */       }
/*     */     }
/*     */ 
/*  71 */     return null;
/*     */   }
/*     */ 
/*     */   public Node item(int paramInt) {
/*  75 */     Node localNode = (Node)this.nodes.get(paramInt);
/*  76 */     return localNode;
/*     */   }
/*     */ 
/*     */   public Node removeNamedItem(String paramString) {
/*  80 */     throw new DOMException((short)7, "This NamedNodeMap is read-only!");
/*     */   }
/*     */ 
/*     */   public Node setNamedItem(Node paramNode)
/*     */   {
/*  85 */     throw new DOMException((short)7, "This NamedNodeMap is read-only!");
/*     */   }
/*     */ 
/*     */   public Node getNamedItemNS(String paramString1, String paramString2)
/*     */   {
/*  93 */     return getNamedItem(paramString2);
/*     */   }
/*     */ 
/*     */   public Node setNamedItemNS(Node paramNode)
/*     */   {
/* 100 */     return setNamedItem(paramNode);
/*     */   }
/*     */ 
/*     */   public Node removeNamedItemNS(String paramString1, String paramString2)
/*     */   {
/* 107 */     return removeNamedItem(paramString2);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.imageio.metadata.IIONamedNodeMap
 * JD-Core Version:    0.6.2
 */