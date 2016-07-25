/*    */ package org.jcp.xml.dsig.internal.dom;
/*    */ 
/*    */ import javax.xml.crypto.MarshalException;
/*    */ import javax.xml.crypto.XMLStructure;
/*    */ import javax.xml.crypto.dom.DOMCryptoContext;
/*    */ import org.w3c.dom.Node;
/*    */ 
/*    */ public abstract class DOMStructure
/*    */   implements XMLStructure
/*    */ {
/*    */   public final boolean isFeatureSupported(String paramString)
/*    */   {
/* 42 */     if (paramString == null) {
/* 43 */       throw new NullPointerException();
/*    */     }
/* 45 */     return false;
/*    */   }
/*    */ 
/*    */   public abstract void marshal(Node paramNode, String paramString, DOMCryptoContext paramDOMCryptoContext)
/*    */     throws MarshalException;
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.jcp.xml.dsig.internal.dom.DOMStructure
 * JD-Core Version:    0.6.2
 */