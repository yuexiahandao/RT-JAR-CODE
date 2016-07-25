/*    */ package org.jcp.xml.dsig.internal.dom;
/*    */ 
/*    */ import com.sun.org.apache.xml.internal.security.utils.Base64;
/*    */ import java.math.BigInteger;
/*    */ import javax.xml.crypto.MarshalException;
/*    */ import javax.xml.crypto.dom.DOMCryptoContext;
/*    */ import org.w3c.dom.Document;
/*    */ import org.w3c.dom.Node;
/*    */ import org.w3c.dom.Text;
/*    */ 
/*    */ public final class DOMCryptoBinary extends DOMStructure
/*    */ {
/*    */   private final BigInteger bigNum;
/*    */   private final String value;
/*    */ 
/*    */   public DOMCryptoBinary(BigInteger paramBigInteger)
/*    */   {
/* 65 */     if (paramBigInteger == null) {
/* 66 */       throw new NullPointerException("bigNum is null");
/*    */     }
/* 68 */     this.bigNum = paramBigInteger;
/*    */ 
/* 70 */     this.value = Base64.encode(paramBigInteger);
/*    */   }
/*    */ 
/*    */   public DOMCryptoBinary(Node paramNode)
/*    */     throws MarshalException
/*    */   {
/* 80 */     this.value = paramNode.getNodeValue();
/*    */     try {
/* 82 */       this.bigNum = Base64.decodeBigIntegerFromText((Text)paramNode);
/*    */     } catch (Exception localException) {
/* 84 */       throw new MarshalException(localException);
/*    */     }
/*    */   }
/*    */ 
/*    */   public BigInteger getBigNum()
/*    */   {
/* 94 */     return this.bigNum;
/*    */   }
/*    */ 
/*    */   public void marshal(Node paramNode, String paramString, DOMCryptoContext paramDOMCryptoContext) throws MarshalException
/*    */   {
/* 99 */     paramNode.appendChild(DOMUtils.getOwnerDocument(paramNode).createTextNode(this.value));
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.jcp.xml.dsig.internal.dom.DOMCryptoBinary
 * JD-Core Version:    0.6.2
 */