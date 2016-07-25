/*    */ package org.jcp.xml.dsig.internal.dom;
/*    */ 
/*    */ import javax.xml.crypto.MarshalException;
/*    */ import javax.xml.crypto.dom.DOMCryptoContext;
/*    */ import javax.xml.crypto.dsig.keyinfo.KeyName;
/*    */ import org.w3c.dom.Document;
/*    */ import org.w3c.dom.Element;
/*    */ import org.w3c.dom.Node;
/*    */ 
/*    */ public final class DOMKeyName extends DOMStructure
/*    */   implements KeyName
/*    */ {
/*    */   private final String name;
/*    */ 
/*    */   public DOMKeyName(String paramString)
/*    */   {
/* 54 */     if (paramString == null) {
/* 55 */       throw new NullPointerException("name cannot be null");
/*    */     }
/* 57 */     this.name = paramString;
/*    */   }
/*    */ 
/*    */   public DOMKeyName(Element paramElement)
/*    */   {
/* 66 */     this.name = paramElement.getFirstChild().getNodeValue();
/*    */   }
/*    */ 
/*    */   public String getName() {
/* 70 */     return this.name;
/*    */   }
/*    */ 
/*    */   public void marshal(Node paramNode, String paramString, DOMCryptoContext paramDOMCryptoContext) throws MarshalException
/*    */   {
/* 75 */     Document localDocument = DOMUtils.getOwnerDocument(paramNode);
/*    */ 
/* 77 */     Element localElement = DOMUtils.createElement(localDocument, "KeyName", "http://www.w3.org/2000/09/xmldsig#", paramString);
/*    */ 
/* 79 */     localElement.appendChild(localDocument.createTextNode(this.name));
/* 80 */     paramNode.appendChild(localElement);
/*    */   }
/*    */ 
/*    */   public boolean equals(Object paramObject) {
/* 84 */     if (this == paramObject) {
/* 85 */       return true;
/*    */     }
/* 87 */     if (!(paramObject instanceof KeyName)) {
/* 88 */       return false;
/*    */     }
/* 90 */     KeyName localKeyName = (KeyName)paramObject;
/* 91 */     return this.name.equals(localKeyName.getName());
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.jcp.xml.dsig.internal.dom.DOMKeyName
 * JD-Core Version:    0.6.2
 */