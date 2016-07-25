/*    */ package javax.imageio.metadata;
/*    */ 
/*    */ import javax.imageio.IIOException;
/*    */ import org.w3c.dom.Node;
/*    */ 
/*    */ public class IIOInvalidTreeException extends IIOException
/*    */ {
/* 52 */   protected Node offendingNode = null;
/*    */ 
/*    */   public IIOInvalidTreeException(String paramString, Node paramNode)
/*    */   {
/* 65 */     super(paramString);
/* 66 */     this.offendingNode = paramNode;
/*    */   }
/*    */ 
/*    */   public IIOInvalidTreeException(String paramString, Throwable paramThrowable, Node paramNode)
/*    */   {
/* 85 */     super(paramString, paramThrowable);
/* 86 */     this.offendingNode = paramNode;
/*    */   }
/*    */ 
/*    */   public Node getOffendingNode()
/*    */   {
/* 95 */     return this.offendingNode;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.imageio.metadata.IIOInvalidTreeException
 * JD-Core Version:    0.6.2
 */