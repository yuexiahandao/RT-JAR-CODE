/*     */ package javax.xml.transform.dom;
/*     */ 
/*     */ import javax.xml.transform.Source;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public class DOMSource
/*     */   implements Source
/*     */ {
/*     */   private Node node;
/*     */   private String systemID;
/*     */   public static final String FEATURE = "http://javax.xml.transform.dom.DOMSource/feature";
/*     */ 
/*     */   public DOMSource()
/*     */   {
/*     */   }
/*     */ 
/*     */   public DOMSource(Node n)
/*     */   {
/*  85 */     setNode(n);
/*     */   }
/*     */ 
/*     */   public DOMSource(Node node, String systemID)
/*     */   {
/*  96 */     setNode(node);
/*  97 */     setSystemId(systemID);
/*     */   }
/*     */ 
/*     */   public void setNode(Node node)
/*     */   {
/* 106 */     this.node = node;
/*     */   }
/*     */ 
/*     */   public Node getNode()
/*     */   {
/* 115 */     return this.node;
/*     */   }
/*     */ 
/*     */   public void setSystemId(String systemID)
/*     */   {
/* 125 */     this.systemID = systemID;
/*     */   }
/*     */ 
/*     */   public String getSystemId()
/*     */   {
/* 135 */     return this.systemID;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.transform.dom.DOMSource
 * JD-Core Version:    0.6.2
 */