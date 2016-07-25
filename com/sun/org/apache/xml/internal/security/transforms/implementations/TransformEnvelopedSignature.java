/*     */ package com.sun.org.apache.xml.internal.security.transforms.implementations;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.signature.NodeFilter;
/*     */ import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
/*     */ import com.sun.org.apache.xml.internal.security.transforms.Transform;
/*     */ import com.sun.org.apache.xml.internal.security.transforms.TransformSpi;
/*     */ import com.sun.org.apache.xml.internal.security.transforms.TransformationException;
/*     */ import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public class TransformEnvelopedSignature extends TransformSpi
/*     */ {
/*     */   public static final String implementedTransformURI = "http://www.w3.org/2000/09/xmldsig#enveloped-signature";
/*     */ 
/*     */   protected String engineGetURI()
/*     */   {
/*  52 */     return "http://www.w3.org/2000/09/xmldsig#enveloped-signature";
/*     */   }
/*     */ 
/*     */   protected XMLSignatureInput enginePerformTransform(XMLSignatureInput paramXMLSignatureInput, Transform paramTransform)
/*     */     throws TransformationException
/*     */   {
/*  75 */     Object localObject = paramTransform.getElement();
/*     */ 
/*  78 */     localObject = searchSignatureElement((Node)localObject);
/*  79 */     paramXMLSignatureInput.setExcludeNode((Node)localObject);
/*  80 */     paramXMLSignatureInput.addNodeFilter(new EnvelopedNodeFilter((Node)localObject));
/*  81 */     return paramXMLSignatureInput;
/*     */   }
/*     */ 
/*     */   private static Node searchSignatureElement(Node paramNode)
/*     */     throws TransformationException
/*     */   {
/*  94 */     int i = 0;
/*     */ 
/*  97 */     while ((paramNode != null) && (paramNode.getNodeType() != 9))
/*     */     {
/* 101 */       Element localElement = (Element)paramNode;
/* 102 */       if ((localElement.getNamespaceURI().equals("http://www.w3.org/2000/09/xmldsig#")) && (localElement.getLocalName().equals("Signature")))
/*     */       {
/* 105 */         i = 1;
/* 106 */         break;
/*     */       }
/*     */ 
/* 109 */       paramNode = paramNode.getParentNode();
/*     */     }
/*     */ 
/* 112 */     if (i == 0) {
/* 113 */       throw new TransformationException("envelopedSignatureTransformNotInSignatureElement");
/*     */     }
/*     */ 
/* 116 */     return paramNode;
/*     */   }
/*     */   static class EnvelopedNodeFilter implements NodeFilter {
/*     */     Node exclude;
/*     */ 
/* 121 */     EnvelopedNodeFilter(Node paramNode) { this.exclude = paramNode; }
/*     */ 
/*     */     public int isNodeIncludeDO(Node paramNode, int paramInt) {
/* 124 */       if (paramNode == this.exclude)
/* 125 */         return -1;
/* 126 */       return 1;
/*     */     }
/*     */ 
/*     */     public int isNodeInclude(Node paramNode)
/*     */     {
/* 132 */       if ((paramNode == this.exclude) || (XMLUtils.isDescendantOrSelf(this.exclude, paramNode)))
/* 133 */         return -1;
/* 134 */       return 1;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.transforms.implementations.TransformEnvelopedSignature
 * JD-Core Version:    0.6.2
 */