/*    */ package com.sun.org.apache.xml.internal.security.transforms.implementations;
/*    */ 
/*    */ import com.sun.org.apache.xml.internal.security.c14n.CanonicalizationException;
/*    */ import com.sun.org.apache.xml.internal.security.c14n.InvalidCanonicalizerException;
/*    */ import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
/*    */ import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
/*    */ import com.sun.org.apache.xml.internal.security.transforms.Transform;
/*    */ import com.sun.org.apache.xml.internal.security.transforms.TransformSpi;
/*    */ import com.sun.org.apache.xml.internal.security.transforms.TransformationException;
/*    */ import com.sun.org.apache.xml.internal.security.transforms.params.XPath2FilterContainer;
/*    */ import com.sun.org.apache.xml.internal.security.utils.CachedXPathAPIHolder;
/*    */ import com.sun.org.apache.xml.internal.security.utils.CachedXPathFuncHereAPI;
/*    */ import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
/*    */ import java.io.IOException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import javax.xml.parsers.ParserConfigurationException;
/*    */ import javax.xml.transform.TransformerException;
/*    */ import org.w3c.dom.DOMException;
/*    */ import org.w3c.dom.Element;
/*    */ import org.w3c.dom.Node;
/*    */ import org.w3c.dom.NodeList;
/*    */ import org.xml.sax.SAXException;
/*    */ 
/*    */ public class TransformXPath2Filter extends TransformSpi
/*    */ {
/*    */   public static final String implementedTransformURI = "http://www.w3.org/2002/06/xmldsig-filter2";
/*    */ 
/*    */   protected String engineGetURI()
/*    */   {
/* 83 */     return "http://www.w3.org/2002/06/xmldsig-filter2";
/*    */   }
/*    */ 
/*    */   protected XMLSignatureInput enginePerformTransform(XMLSignatureInput paramXMLSignatureInput, Transform paramTransform)
/*    */     throws TransformationException
/*    */   {
/* 97 */     CachedXPathAPIHolder.setDoc(paramTransform.getElement().getOwnerDocument());
/*    */     try {
/* 99 */       ArrayList localArrayList1 = new ArrayList();
/* 100 */       ArrayList localArrayList2 = new ArrayList();
/* 101 */       ArrayList localArrayList3 = new ArrayList();
/*    */ 
/* 103 */       CachedXPathFuncHereAPI localCachedXPathFuncHereAPI = new CachedXPathFuncHereAPI(CachedXPathAPIHolder.getCachedXPathAPI());
/*    */ 
/* 107 */       Element[] arrayOfElement = XMLUtils.selectNodes(paramTransform.getElement().getFirstChild(), "http://www.w3.org/2002/06/xmldsig-filter2", "XPath");
/*    */ 
/* 111 */       int i = arrayOfElement.length;
/*    */ 
/* 114 */       if (i == 0) {
/* 115 */         localObject = new Object[] { "http://www.w3.org/2002/06/xmldsig-filter2", "XPath" };
/*    */ 
/* 117 */         throw new TransformationException("xml.WrongContent", (Object[])localObject);
/*    */       }
/*    */ 
/* 120 */       Object localObject = null;
/* 121 */       if (paramXMLSignatureInput.getSubNode() != null)
/* 122 */         localObject = XMLUtils.getOwnerDocument(paramXMLSignatureInput.getSubNode());
/*    */       else {
/* 124 */         localObject = XMLUtils.getOwnerDocument(paramXMLSignatureInput.getNodeSet());
/*    */       }
/*    */ 
/* 127 */       for (int j = 0; j < i; j++) {
/* 128 */         Element localElement = XMLUtils.selectNode(paramTransform.getElement().getFirstChild(), "http://www.w3.org/2002/06/xmldsig-filter2", "XPath", j);
/*    */ 
/* 132 */         XPath2FilterContainer localXPath2FilterContainer = XPath2FilterContainer.newInstance(localElement, paramXMLSignatureInput.getSourceURI());
/*    */ 
/* 137 */         NodeList localNodeList = localCachedXPathFuncHereAPI.selectNodeList((Node)localObject, localXPath2FilterContainer.getXPathFilterTextNode(), CachedXPathFuncHereAPI.getStrFromNode(localXPath2FilterContainer.getXPathFilterTextNode()), localXPath2FilterContainer.getElement());
/*    */ 
/* 141 */         if (localXPath2FilterContainer.isIntersect())
/* 142 */           localArrayList3.add(localNodeList);
/* 143 */         else if (localXPath2FilterContainer.isSubtract())
/* 144 */           localArrayList2.add(localNodeList);
/* 145 */         else if (localXPath2FilterContainer.isUnion()) {
/* 146 */           localArrayList1.add(localNodeList);
/*    */         }
/*    */ 
/*    */       }
/*    */ 
/* 151 */       paramXMLSignatureInput.addNodeFilter(new XPath2NodeFilter(localArrayList1, localArrayList2, localArrayList3));
/*    */ 
/* 153 */       paramXMLSignatureInput.setNodeSet(true);
/* 154 */       return paramXMLSignatureInput;
/*    */     } catch (TransformerException localTransformerException) {
/* 156 */       throw new TransformationException("empty", localTransformerException);
/*    */     } catch (DOMException localDOMException) {
/* 158 */       throw new TransformationException("empty", localDOMException);
/*    */     } catch (CanonicalizationException localCanonicalizationException) {
/* 160 */       throw new TransformationException("empty", localCanonicalizationException);
/*    */     } catch (InvalidCanonicalizerException localInvalidCanonicalizerException) {
/* 162 */       throw new TransformationException("empty", localInvalidCanonicalizerException);
/*    */     } catch (XMLSecurityException localXMLSecurityException) {
/* 164 */       throw new TransformationException("empty", localXMLSecurityException);
/*    */     } catch (SAXException localSAXException) {
/* 166 */       throw new TransformationException("empty", localSAXException);
/*    */     } catch (IOException localIOException) {
/* 168 */       throw new TransformationException("empty", localIOException);
/*    */     } catch (ParserConfigurationException localParserConfigurationException) {
/* 170 */       throw new TransformationException("empty", localParserConfigurationException);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.transforms.implementations.TransformXPath2Filter
 * JD-Core Version:    0.6.2
 */