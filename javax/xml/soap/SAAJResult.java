/*     */ package javax.xml.soap;
/*     */ 
/*     */ import javax.xml.transform.dom.DOMResult;
/*     */ 
/*     */ public class SAAJResult extends DOMResult
/*     */ {
/*     */   public SAAJResult()
/*     */     throws SOAPException
/*     */   {
/*  59 */     this(MessageFactory.newInstance().createMessage());
/*     */   }
/*     */ 
/*     */   public SAAJResult(String protocol)
/*     */     throws SOAPException
/*     */   {
/*  85 */     this(MessageFactory.newInstance(protocol).createMessage());
/*     */   }
/*     */ 
/*     */   public SAAJResult(SOAPMessage message)
/*     */   {
/* 105 */     super(message.getSOAPPart());
/*     */   }
/*     */ 
/*     */   public SAAJResult(SOAPElement rootNode)
/*     */   {
/* 122 */     super(rootNode);
/*     */   }
/*     */ 
/*     */   public Node getResult()
/*     */   {
/* 131 */     return (Node)super.getNode().getFirstChild();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.soap.SAAJResult
 * JD-Core Version:    0.6.2
 */