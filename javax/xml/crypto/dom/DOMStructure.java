/*    */ package javax.xml.crypto.dom;
/*    */ 
/*    */ import javax.xml.crypto.XMLStructure;
/*    */ import org.w3c.dom.Node;
/*    */ 
/*    */ public class DOMStructure
/*    */   implements XMLStructure
/*    */ {
/*    */   private final Node node;
/*    */ 
/*    */   public DOMStructure(Node paramNode)
/*    */   {
/* 63 */     if (paramNode == null) {
/* 64 */       throw new NullPointerException("node cannot be null");
/*    */     }
/* 66 */     this.node = paramNode;
/*    */   }
/*    */ 
/*    */   public Node getNode()
/*    */   {
/* 75 */     return this.node;
/*    */   }
/*    */ 
/*    */   public boolean isFeatureSupported(String paramString)
/*    */   {
/* 82 */     if (paramString == null) {
/* 83 */       throw new NullPointerException();
/*    */     }
/* 85 */     return false;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.crypto.dom.DOMStructure
 * JD-Core Version:    0.6.2
 */