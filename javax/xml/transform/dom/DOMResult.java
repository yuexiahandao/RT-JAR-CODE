/*     */ package javax.xml.transform.dom;
/*     */ 
/*     */ import javax.xml.transform.Result;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public class DOMResult
/*     */   implements Result
/*     */ {
/*     */   public static final String FEATURE = "http://javax.xml.transform.dom.DOMResult/feature";
/* 349 */   private Node node = null;
/*     */ 
/* 356 */   private Node nextSibling = null;
/*     */ 
/* 361 */   private String systemId = null;
/*     */ 
/*     */   public DOMResult()
/*     */   {
/*  56 */     setNode(null);
/*  57 */     setNextSibling(null);
/*  58 */     setSystemId(null);
/*     */   }
/*     */ 
/*     */   public DOMResult(Node node)
/*     */   {
/*  77 */     setNode(node);
/*  78 */     setNextSibling(null);
/*  79 */     setSystemId(null);
/*     */   }
/*     */ 
/*     */   public DOMResult(Node node, String systemId)
/*     */   {
/*  97 */     setNode(node);
/*  98 */     setNextSibling(null);
/*  99 */     setSystemId(systemId);
/*     */   }
/*     */ 
/*     */   public DOMResult(Node node, Node nextSibling)
/*     */   {
/* 135 */     if (nextSibling != null)
/*     */     {
/* 137 */       if (node == null) {
/* 138 */         throw new IllegalArgumentException("Cannot create a DOMResult when the nextSibling is contained by the \"null\" node.");
/*     */       }
/*     */ 
/* 142 */       if ((node.compareDocumentPosition(nextSibling) & 0x10) == 0) {
/* 143 */         throw new IllegalArgumentException("Cannot create a DOMResult when the nextSibling is not contained by the node.");
/*     */       }
/*     */     }
/*     */ 
/* 147 */     setNode(node);
/* 148 */     setNextSibling(nextSibling);
/* 149 */     setSystemId(null);
/*     */   }
/*     */ 
/*     */   public DOMResult(Node node, Node nextSibling, String systemId)
/*     */   {
/* 186 */     if (nextSibling != null)
/*     */     {
/* 188 */       if (node == null) {
/* 189 */         throw new IllegalArgumentException("Cannot create a DOMResult when the nextSibling is contained by the \"null\" node.");
/*     */       }
/*     */ 
/* 193 */       if ((node.compareDocumentPosition(nextSibling) & 0x10) == 0) {
/* 194 */         throw new IllegalArgumentException("Cannot create a DOMResult when the nextSibling is not contained by the node.");
/*     */       }
/*     */     }
/*     */ 
/* 198 */     setNode(node);
/* 199 */     setNextSibling(nextSibling);
/* 200 */     setSystemId(systemId);
/*     */   }
/*     */ 
/*     */   public void setNode(Node node)
/*     */   {
/* 228 */     if (this.nextSibling != null)
/*     */     {
/* 230 */       if (node == null) {
/* 231 */         throw new IllegalStateException("Cannot create a DOMResult when the nextSibling is contained by the \"null\" node.");
/*     */       }
/*     */ 
/* 235 */       if ((node.compareDocumentPosition(this.nextSibling) & 0x10) == 0) {
/* 236 */         throw new IllegalArgumentException("Cannot create a DOMResult when the nextSibling is not contained by the node.");
/*     */       }
/*     */     }
/*     */ 
/* 240 */     this.node = node;
/*     */   }
/*     */ 
/*     */   public Node getNode()
/*     */   {
/* 258 */     return this.node;
/*     */   }
/*     */ 
/*     */   public void setNextSibling(Node nextSibling)
/*     */   {
/* 286 */     if (nextSibling != null)
/*     */     {
/* 288 */       if (this.node == null) {
/* 289 */         throw new IllegalStateException("Cannot create a DOMResult when the nextSibling is contained by the \"null\" node.");
/*     */       }
/*     */ 
/* 293 */       if ((this.node.compareDocumentPosition(nextSibling) & 0x10) == 0) {
/* 294 */         throw new IllegalArgumentException("Cannot create a DOMResult when the nextSibling is not contained by the node.");
/*     */       }
/*     */     }
/*     */ 
/* 298 */     this.nextSibling = nextSibling;
/*     */   }
/*     */ 
/*     */   public Node getNextSibling()
/*     */   {
/* 315 */     return this.nextSibling;
/*     */   }
/*     */ 
/*     */   public void setSystemId(String systemId)
/*     */   {
/* 324 */     this.systemId = systemId;
/*     */   }
/*     */ 
/*     */   public String getSystemId()
/*     */   {
/* 339 */     return this.systemId;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.transform.dom.DOMResult
 * JD-Core Version:    0.6.2
 */