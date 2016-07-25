/*     */ package com.sun.org.apache.xml.internal.security.transforms.implementations;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityRuntimeException;
/*     */ import com.sun.org.apache.xml.internal.security.signature.NodeFilter;
/*     */ import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
/*     */ import com.sun.org.apache.xml.internal.security.transforms.Transform;
/*     */ import com.sun.org.apache.xml.internal.security.transforms.TransformSpi;
/*     */ import com.sun.org.apache.xml.internal.security.transforms.TransformationException;
/*     */ import com.sun.org.apache.xml.internal.security.utils.CachedXPathAPIHolder;
/*     */ import com.sun.org.apache.xml.internal.security.utils.CachedXPathFuncHereAPI;
/*     */ import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
/*     */ import com.sun.org.apache.xml.internal.utils.PrefixResolverDefault;
/*     */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import org.w3c.dom.DOMException;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ 
/*     */ public class TransformXPath extends TransformSpi
/*     */ {
/*     */   public static final String implementedTransformURI = "http://www.w3.org/TR/1999/REC-xpath-19991116";
/*     */ 
/*     */   protected String engineGetURI()
/*     */   {
/*  64 */     return "http://www.w3.org/TR/1999/REC-xpath-19991116";
/*     */   }
/*     */ 
/*     */   protected XMLSignatureInput enginePerformTransform(XMLSignatureInput paramXMLSignatureInput, Transform paramTransform)
/*     */     throws TransformationException
/*     */   {
/*     */     try
/*     */     {
/*  90 */       CachedXPathAPIHolder.setDoc(paramTransform.getElement().getOwnerDocument());
/*     */ 
/*  94 */       Element localElement = XMLUtils.selectDsNode(paramTransform.getElement().getFirstChild(), "XPath", 0);
/*     */ 
/*  98 */       if (localElement == null) {
/*  99 */         localObject = new Object[] { "ds:XPath", "Transform" };
/*     */ 
/* 101 */         throw new TransformationException("xml.WrongContent", (Object[])localObject);
/*     */       }
/* 103 */       Object localObject = localElement.getChildNodes().item(0);
/* 104 */       String str = CachedXPathFuncHereAPI.getStrFromNode((Node)localObject);
/* 105 */       paramXMLSignatureInput.setNeedsToBeExpanded(needsCircunvent(str));
/* 106 */       if (localObject == null) {
/* 107 */         throw new DOMException((short)3, "Text must be in ds:Xpath");
/*     */       }
/*     */ 
/* 112 */       paramXMLSignatureInput.addNodeFilter(new XPathNodeFilter(localElement, (Node)localObject, str));
/* 113 */       paramXMLSignatureInput.setNodeSet(true);
/* 114 */       return paramXMLSignatureInput;
/*     */     } catch (DOMException localDOMException) {
/* 116 */       throw new TransformationException("empty", localDOMException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean needsCircunvent(String paramString)
/*     */   {
/* 127 */     return (paramString.indexOf("namespace") != -1) || (paramString.indexOf("name()") != -1);
/*     */   }
/*     */   static class XPathNodeFilter implements NodeFilter {
/*     */     PrefixResolverDefault prefixResolver;
/* 132 */     CachedXPathFuncHereAPI xPathFuncHereAPI = new CachedXPathFuncHereAPI(CachedXPathAPIHolder.getCachedXPathAPI());
/*     */     Node xpathnode;
/*     */     String str;
/*     */ 
/* 138 */     XPathNodeFilter(Element paramElement, Node paramNode, String paramString) { this.xpathnode = paramNode;
/* 139 */       this.str = paramString;
/* 140 */       this.prefixResolver = new PrefixResolverDefault(paramElement);
/*     */     }
/*     */ 
/*     */     public int isNodeInclude(Node paramNode)
/*     */     {
/*     */       try
/*     */       {
/* 149 */         XObject localXObject = this.xPathFuncHereAPI.eval(paramNode, this.xpathnode, this.str, this.prefixResolver);
/*     */ 
/* 151 */         if (localXObject.bool())
/* 152 */           return 1;
/* 153 */         return 0;
/*     */       } catch (TransformerException localTransformerException) {
/* 155 */         arrayOfObject = new Object[] { paramNode };
/* 156 */         throw new XMLSecurityRuntimeException("signature.Transform.node", arrayOfObject, localTransformerException);
/*     */       }
/*     */       catch (Exception localException) {
/* 159 */         Object[] arrayOfObject = { paramNode, new Short(paramNode.getNodeType()) };
/* 160 */         throw new XMLSecurityRuntimeException("signature.Transform.nodeAndType", arrayOfObject, localException);
/*     */       }
/*     */     }
/*     */ 
/*     */     public int isNodeIncludeDO(Node paramNode, int paramInt) {
/* 165 */       return isNodeInclude(paramNode);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.transforms.implementations.TransformXPath
 * JD-Core Version:    0.6.2
 */