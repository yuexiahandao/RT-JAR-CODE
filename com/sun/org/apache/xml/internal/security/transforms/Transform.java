/*     */ package com.sun.org.apache.xml.internal.security.transforms;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.c14n.CanonicalizationException;
/*     */ import com.sun.org.apache.xml.internal.security.c14n.InvalidCanonicalizerException;
/*     */ import com.sun.org.apache.xml.internal.security.exceptions.AlgorithmAlreadyRegisteredException;
/*     */ import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
/*     */ import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
/*     */ import com.sun.org.apache.xml.internal.security.transforms.implementations.TransformBase64Decode;
/*     */ import com.sun.org.apache.xml.internal.security.transforms.implementations.TransformC14N;
/*     */ import com.sun.org.apache.xml.internal.security.transforms.implementations.TransformC14N11;
/*     */ import com.sun.org.apache.xml.internal.security.transforms.implementations.TransformC14N11_WithComments;
/*     */ import com.sun.org.apache.xml.internal.security.transforms.implementations.TransformC14NExclusive;
/*     */ import com.sun.org.apache.xml.internal.security.transforms.implementations.TransformC14NExclusiveWithComments;
/*     */ import com.sun.org.apache.xml.internal.security.transforms.implementations.TransformC14NWithComments;
/*     */ import com.sun.org.apache.xml.internal.security.transforms.implementations.TransformEnvelopedSignature;
/*     */ import com.sun.org.apache.xml.internal.security.transforms.implementations.TransformXPath;
/*     */ import com.sun.org.apache.xml.internal.security.transforms.implementations.TransformXPath2Filter;
/*     */ import com.sun.org.apache.xml.internal.security.transforms.implementations.TransformXSLT;
/*     */ import com.sun.org.apache.xml.internal.security.utils.HelperNodeList;
/*     */ import com.sun.org.apache.xml.internal.security.utils.JavaUtils;
/*     */ import com.sun.org.apache.xml.internal.security.utils.SignatureElementProxy;
/*     */ import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public final class Transform extends SignatureElementProxy
/*     */ {
/*  73 */   private static Logger log = Logger.getLogger(Transform.class.getName());
/*     */ 
/*  77 */   private static Map<String, Class<? extends TransformSpi>> transformSpiHash = new ConcurrentHashMap();
/*     */   private final TransformSpi transformSpi;
/*     */ 
/*     */   public Transform(Document paramDocument, String paramString)
/*     */     throws InvalidTransformException
/*     */   {
/*  93 */     this(paramDocument, paramString, (NodeList)null);
/*     */   }
/*     */ 
/*     */   public Transform(Document paramDocument, String paramString, Element paramElement)
/*     */     throws InvalidTransformException
/*     */   {
/* 109 */     super(paramDocument);
/* 110 */     HelperNodeList localHelperNodeList = null;
/*     */ 
/* 112 */     if (paramElement != null) {
/* 113 */       localHelperNodeList = new HelperNodeList();
/*     */ 
/* 115 */       XMLUtils.addReturnToElement(paramDocument, localHelperNodeList);
/* 116 */       localHelperNodeList.appendChild(paramElement);
/* 117 */       XMLUtils.addReturnToElement(paramDocument, localHelperNodeList);
/*     */     }
/*     */ 
/* 120 */     this.transformSpi = initializeTransform(paramString, localHelperNodeList);
/*     */   }
/*     */ 
/*     */   public Transform(Document paramDocument, String paramString, NodeList paramNodeList)
/*     */     throws InvalidTransformException
/*     */   {
/* 134 */     super(paramDocument);
/* 135 */     this.transformSpi = initializeTransform(paramString, paramNodeList);
/*     */   }
/*     */ 
/*     */   public Transform(Element paramElement, String paramString)
/*     */     throws InvalidTransformException, TransformationException, XMLSecurityException
/*     */   {
/* 147 */     super(paramElement, paramString);
/*     */ 
/* 150 */     String str = paramElement.getAttributeNS(null, "Algorithm");
/*     */ 
/* 152 */     if ((str == null) || (str.length() == 0)) {
/* 153 */       localObject = new Object[] { "Algorithm", "Transform" };
/* 154 */       throw new TransformationException("xml.WrongContent", (Object[])localObject);
/*     */     }
/*     */ 
/* 157 */     Object localObject = (Class)transformSpiHash.get(str);
/* 158 */     if (localObject == null) {
/* 159 */       Object[] arrayOfObject1 = { str };
/* 160 */       throw new InvalidTransformException("signature.Transform.UnknownTransform", arrayOfObject1);
/*     */     }
/*     */     try {
/* 163 */       this.transformSpi = ((TransformSpi)((Class)localObject).newInstance());
/*     */     } catch (InstantiationException localInstantiationException) {
/* 165 */       arrayOfObject2 = new Object[] { str };
/* 166 */       throw new InvalidTransformException("signature.Transform.UnknownTransform", arrayOfObject2, localInstantiationException);
/*     */     }
/*     */     catch (IllegalAccessException localIllegalAccessException)
/*     */     {
/* 170 */       Object[] arrayOfObject2 = { str };
/* 171 */       throw new InvalidTransformException("signature.Transform.UnknownTransform", arrayOfObject2, localIllegalAccessException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void register(String paramString1, String paramString2)
/*     */     throws AlgorithmAlreadyRegisteredException, ClassNotFoundException, InvalidTransformException
/*     */   {
/* 192 */     JavaUtils.checkRegisterPermission();
/*     */ 
/* 194 */     Class localClass = (Class)transformSpiHash.get(paramString1);
/* 195 */     if (localClass != null) {
/* 196 */       localObject = new Object[] { paramString1, localClass };
/* 197 */       throw new AlgorithmAlreadyRegisteredException("algorithm.alreadyRegistered", (Object[])localObject);
/*     */     }
/* 199 */     Object localObject = ClassLoaderUtils.loadClass(paramString2, Transform.class);
/*     */ 
/* 202 */     transformSpiHash.put(paramString1, localObject);
/*     */   }
/*     */ 
/*     */   public static void register(String paramString, Class<? extends TransformSpi> paramClass)
/*     */     throws AlgorithmAlreadyRegisteredException
/*     */   {
/* 218 */     JavaUtils.checkRegisterPermission();
/*     */ 
/* 220 */     Class localClass = (Class)transformSpiHash.get(paramString);
/* 221 */     if (localClass != null) {
/* 222 */       Object[] arrayOfObject = { paramString, localClass };
/* 223 */       throw new AlgorithmAlreadyRegisteredException("algorithm.alreadyRegistered", arrayOfObject);
/*     */     }
/* 225 */     transformSpiHash.put(paramString, paramClass);
/*     */   }
/*     */ 
/*     */   public static void registerDefaultAlgorithms()
/*     */   {
/* 232 */     transformSpiHash.put("http://www.w3.org/2000/09/xmldsig#base64", TransformBase64Decode.class);
/*     */ 
/* 235 */     transformSpiHash.put("http://www.w3.org/TR/2001/REC-xml-c14n-20010315", TransformC14N.class);
/*     */ 
/* 238 */     transformSpiHash.put("http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments", TransformC14NWithComments.class);
/*     */ 
/* 241 */     transformSpiHash.put("http://www.w3.org/2006/12/xml-c14n11", TransformC14N11.class);
/*     */ 
/* 244 */     transformSpiHash.put("http://www.w3.org/2006/12/xml-c14n11#WithComments", TransformC14N11_WithComments.class);
/*     */ 
/* 247 */     transformSpiHash.put("http://www.w3.org/2001/10/xml-exc-c14n#", TransformC14NExclusive.class);
/*     */ 
/* 250 */     transformSpiHash.put("http://www.w3.org/2001/10/xml-exc-c14n#WithComments", TransformC14NExclusiveWithComments.class);
/*     */ 
/* 253 */     transformSpiHash.put("http://www.w3.org/TR/1999/REC-xpath-19991116", TransformXPath.class);
/*     */ 
/* 256 */     transformSpiHash.put("http://www.w3.org/2000/09/xmldsig#enveloped-signature", TransformEnvelopedSignature.class);
/*     */ 
/* 259 */     transformSpiHash.put("http://www.w3.org/TR/1999/REC-xslt-19991116", TransformXSLT.class);
/*     */ 
/* 262 */     transformSpiHash.put("http://www.w3.org/2002/06/xmldsig-filter2", TransformXPath2Filter.class);
/*     */   }
/*     */ 
/*     */   public String getURI()
/*     */   {
/* 273 */     return this._constructionElement.getAttributeNS(null, "Algorithm");
/*     */   }
/*     */ 
/*     */   public XMLSignatureInput performTransform(XMLSignatureInput paramXMLSignatureInput)
/*     */     throws IOException, CanonicalizationException, InvalidCanonicalizerException, TransformationException
/*     */   {
/* 291 */     return performTransform(paramXMLSignatureInput, null);
/*     */   }
/*     */ 
/*     */   public XMLSignatureInput performTransform(XMLSignatureInput paramXMLSignatureInput, OutputStream paramOutputStream)
/*     */     throws IOException, CanonicalizationException, InvalidCanonicalizerException, TransformationException
/*     */   {
/* 311 */     XMLSignatureInput localXMLSignatureInput = null;
/*     */     try
/*     */     {
/* 314 */       localXMLSignatureInput = this.transformSpi.enginePerformTransform(paramXMLSignatureInput, paramOutputStream, this);
/*     */     } catch (ParserConfigurationException localParserConfigurationException) {
/* 316 */       arrayOfObject = new Object[] { getURI(), "ParserConfigurationException" };
/* 317 */       throw new CanonicalizationException("signature.Transform.ErrorDuringTransform", arrayOfObject, localParserConfigurationException);
/*     */     }
/*     */     catch (SAXException localSAXException) {
/* 320 */       Object[] arrayOfObject = { getURI(), "SAXException" };
/* 321 */       throw new CanonicalizationException("signature.Transform.ErrorDuringTransform", arrayOfObject, localSAXException);
/*     */     }
/*     */ 
/* 325 */     return localXMLSignatureInput;
/*     */   }
/*     */ 
/*     */   public String getBaseLocalName()
/*     */   {
/* 330 */     return "Transform";
/*     */   }
/*     */ 
/*     */   private TransformSpi initializeTransform(String paramString, NodeList paramNodeList)
/*     */     throws InvalidTransformException
/*     */   {
/* 339 */     this._constructionElement.setAttributeNS(null, "Algorithm", paramString);
/*     */ 
/* 341 */     Class localClass = (Class)transformSpiHash.get(paramString);
/* 342 */     if (localClass == null) {
/* 343 */       localObject = new Object[] { paramString };
/* 344 */       throw new InvalidTransformException("signature.Transform.UnknownTransform", (Object[])localObject);
/*     */     }
/* 346 */     Object localObject = null;
/*     */     try {
/* 348 */       localObject = (TransformSpi)localClass.newInstance();
/*     */     } catch (InstantiationException localInstantiationException) {
/* 350 */       arrayOfObject = new Object[] { paramString };
/* 351 */       throw new InvalidTransformException("signature.Transform.UnknownTransform", arrayOfObject, localInstantiationException);
/*     */     }
/*     */     catch (IllegalAccessException localIllegalAccessException)
/*     */     {
/* 355 */       Object[] arrayOfObject = { paramString };
/* 356 */       throw new InvalidTransformException("signature.Transform.UnknownTransform", arrayOfObject, localIllegalAccessException);
/*     */     }
/*     */ 
/* 361 */     if (log.isLoggable(Level.FINE)) {
/* 362 */       log.log(Level.FINE, "Create URI \"" + paramString + "\" class \"" + localObject.getClass() + "\"");
/*     */ 
/* 364 */       log.log(Level.FINE, "The NodeList is " + paramNodeList);
/*     */     }
/*     */ 
/* 368 */     if (paramNodeList != null) {
/* 369 */       for (int i = 0; i < paramNodeList.getLength(); i++) {
/* 370 */         this._constructionElement.appendChild(paramNodeList.item(i).cloneNode(true));
/*     */       }
/*     */     }
/* 373 */     return localObject;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.transforms.Transform
 * JD-Core Version:    0.6.2
 */