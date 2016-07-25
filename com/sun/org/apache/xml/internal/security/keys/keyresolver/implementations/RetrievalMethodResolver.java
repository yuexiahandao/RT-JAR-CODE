/*     */ package com.sun.org.apache.xml.internal.security.keys.keyresolver.implementations;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.c14n.CanonicalizationException;
/*     */ import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
/*     */ import com.sun.org.apache.xml.internal.security.keys.content.RetrievalMethod;
/*     */ import com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolver;
/*     */ import com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverException;
/*     */ import com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverSpi;
/*     */ import com.sun.org.apache.xml.internal.security.keys.storage.StorageResolver;
/*     */ import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
/*     */ import com.sun.org.apache.xml.internal.security.transforms.Transforms;
/*     */ import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
/*     */ import com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolver;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.security.PublicKey;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.CertificateFactory;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Set;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.crypto.SecretKey;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class RetrievalMethodResolver extends KeyResolverSpi
/*     */ {
/*  73 */   static Logger log = Logger.getLogger(RetrievalMethodResolver.class.getName());
/*     */ 
/*     */   public PublicKey engineLookupAndResolvePublicKey(Element paramElement, String paramString, StorageResolver paramStorageResolver)
/*     */   {
/*  88 */     if (!XMLUtils.elementIsInSignatureSpace(paramElement, "RetrievalMethod"))
/*     */     {
/*  90 */       return null;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/*  95 */       RetrievalMethod localRetrievalMethod = new RetrievalMethod(paramElement, paramString);
/*  96 */       String str = localRetrievalMethod.getType();
/*  97 */       XMLSignatureInput localXMLSignatureInput = resolveInput(localRetrievalMethod, paramString);
/*  98 */       if ("http://www.w3.org/2000/09/xmldsig#rawX509Certificate".equals(str))
/*     */       {
/* 100 */         localObject = getRawCertificate(localXMLSignatureInput);
/* 101 */         if (localObject != null) {
/* 102 */           return ((X509Certificate)localObject).getPublicKey();
/*     */         }
/* 104 */         return null;
/*     */       }
/* 106 */       Object localObject = obtainRefrenceElement(localXMLSignatureInput);
/* 107 */       return resolveKey((Element)localObject, paramString, paramStorageResolver);
/*     */     } catch (XMLSecurityException localXMLSecurityException) {
/* 109 */       log.log(Level.FINE, "XMLSecurityException", localXMLSecurityException);
/*     */     } catch (CertificateException localCertificateException) {
/* 111 */       log.log(Level.FINE, "CertificateException", localCertificateException);
/*     */     } catch (IOException localIOException) {
/* 113 */       log.log(Level.FINE, "IOException", localIOException);
/*     */     } catch (ParserConfigurationException localParserConfigurationException) {
/* 115 */       log.log(Level.FINE, "ParserConfigurationException", localParserConfigurationException);
/*     */     } catch (SAXException localSAXException) {
/* 117 */       log.log(Level.FINE, "SAXException", localSAXException);
/*     */     }
/* 119 */     return null;
/*     */   }
/*     */ 
/*     */   private static Element obtainRefrenceElement(XMLSignatureInput paramXMLSignatureInput)
/*     */     throws CanonicalizationException, ParserConfigurationException, IOException, SAXException, KeyResolverException
/*     */   {
/*     */     Element localElement;
/* 124 */     if (paramXMLSignatureInput.isElement()) {
/* 125 */       localElement = (Element)paramXMLSignatureInput.getSubNode();
/* 126 */     } else if (paramXMLSignatureInput.isNodeSet())
/*     */     {
/* 128 */       localElement = getDocumentElement(paramXMLSignatureInput.getNodeSet());
/*     */     }
/*     */     else {
/* 131 */       byte[] arrayOfByte = paramXMLSignatureInput.getBytes();
/* 132 */       localElement = getDocFromBytes(arrayOfByte);
/*     */ 
/* 134 */       if (log.isLoggable(Level.FINE))
/* 135 */         log.log(Level.FINE, "we have to parse " + arrayOfByte.length + " bytes");
/*     */     }
/* 137 */     return localElement;
/*     */   }
/*     */ 
/*     */   public X509Certificate engineLookupResolveX509Certificate(Element paramElement, String paramString, StorageResolver paramStorageResolver)
/*     */   {
/* 151 */     if (!XMLUtils.elementIsInSignatureSpace(paramElement, "RetrievalMethod"))
/*     */     {
/* 153 */       return null;
/*     */     }
/*     */     try
/*     */     {
/* 157 */       RetrievalMethod localRetrievalMethod = new RetrievalMethod(paramElement, paramString);
/* 158 */       String str = localRetrievalMethod.getType();
/* 159 */       XMLSignatureInput localXMLSignatureInput = resolveInput(localRetrievalMethod, paramString);
/* 160 */       if ("http://www.w3.org/2000/09/xmldsig#rawX509Certificate".equals(str)) {
/* 161 */         return getRawCertificate(localXMLSignatureInput);
/*     */       }
/*     */ 
/* 164 */       Object localObject = obtainRefrenceElement(localXMLSignatureInput);
/* 165 */       return resolveCertificate((Element)localObject, paramString, paramStorageResolver);
/*     */     } catch (XMLSecurityException localXMLSecurityException) {
/* 167 */       log.log(Level.FINE, "XMLSecurityException", localXMLSecurityException);
/*     */     } catch (CertificateException localCertificateException) {
/* 169 */       log.log(Level.FINE, "CertificateException", localCertificateException);
/*     */     } catch (IOException localIOException) {
/* 171 */       log.log(Level.FINE, "IOException", localIOException);
/*     */     } catch (ParserConfigurationException localParserConfigurationException) {
/* 173 */       log.log(Level.FINE, "ParserConfigurationException", localParserConfigurationException);
/*     */     } catch (SAXException localSAXException) {
/* 175 */       log.log(Level.FINE, "SAXException", localSAXException);
/*     */     }
/* 177 */     return null;
/*     */   }
/*     */ 
/*     */   private static X509Certificate resolveCertificate(Element paramElement, String paramString, StorageResolver paramStorageResolver)
/*     */     throws KeyResolverException
/*     */   {
/* 189 */     if (log.isLoggable(Level.FINE)) {
/* 190 */       log.log(Level.FINE, "Now we have a {" + paramElement.getNamespaceURI() + "}" + paramElement.getLocalName() + " Element");
/*     */     }
/* 192 */     if (paramElement != null) {
/* 193 */       return KeyResolver.getX509Certificate(paramElement, paramString, paramStorageResolver);
/*     */     }
/* 195 */     return null;
/*     */   }
/*     */ 
/*     */   private static PublicKey resolveKey(Element paramElement, String paramString, StorageResolver paramStorageResolver)
/*     */     throws KeyResolverException
/*     */   {
/* 207 */     if (log.isLoggable(Level.FINE)) {
/* 208 */       log.log(Level.FINE, "Now we have a {" + paramElement.getNamespaceURI() + "}" + paramElement.getLocalName() + " Element");
/*     */     }
/* 210 */     if (paramElement != null) {
/* 211 */       return KeyResolver.getPublicKey(paramElement, paramString, paramStorageResolver);
/*     */     }
/* 213 */     return null;
/*     */   }
/*     */ 
/*     */   private static X509Certificate getRawCertificate(XMLSignatureInput paramXMLSignatureInput) throws CanonicalizationException, IOException, CertificateException {
/* 217 */     byte[] arrayOfByte = paramXMLSignatureInput.getBytes();
/*     */ 
/* 219 */     CertificateFactory localCertificateFactory = CertificateFactory.getInstance("X.509");
/* 220 */     X509Certificate localX509Certificate = (X509Certificate)localCertificateFactory.generateCertificate(new ByteArrayInputStream(arrayOfByte));
/* 221 */     return localX509Certificate;
/*     */   }
/*     */ 
/*     */   private static XMLSignatureInput resolveInput(RetrievalMethod paramRetrievalMethod, String paramString)
/*     */     throws XMLSecurityException
/*     */   {
/* 229 */     Attr localAttr = paramRetrievalMethod.getURIAttr();
/*     */ 
/* 231 */     Transforms localTransforms = paramRetrievalMethod.getTransforms();
/* 232 */     ResourceResolver localResourceResolver = ResourceResolver.getInstance(localAttr, paramString);
/* 233 */     if (localResourceResolver != null) {
/* 234 */       XMLSignatureInput localXMLSignatureInput = localResourceResolver.resolve(localAttr, paramString);
/* 235 */       if (localTransforms != null) {
/* 236 */         log.log(Level.FINE, "We have Transforms");
/* 237 */         localXMLSignatureInput = localTransforms.performTransforms(localXMLSignatureInput);
/*     */       }
/* 239 */       return localXMLSignatureInput;
/*     */     }
/* 241 */     return null;
/*     */   }
/*     */ 
/*     */   static Element getDocFromBytes(byte[] paramArrayOfByte)
/*     */     throws KeyResolverException
/*     */   {
/*     */     try
/*     */     {
/* 253 */       DocumentBuilderFactory localDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
/* 254 */       localDocumentBuilderFactory.setNamespaceAware(true);
/* 255 */       localDocumentBuilderFactory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", Boolean.TRUE.booleanValue());
/* 256 */       DocumentBuilder localDocumentBuilder = localDocumentBuilderFactory.newDocumentBuilder();
/* 257 */       Document localDocument = localDocumentBuilder.parse(new ByteArrayInputStream(paramArrayOfByte));
/*     */ 
/* 259 */       return localDocument.getDocumentElement();
/*     */     } catch (SAXException localSAXException) {
/* 261 */       throw new KeyResolverException("empty", localSAXException);
/*     */     } catch (IOException localIOException) {
/* 263 */       throw new KeyResolverException("empty", localIOException);
/*     */     } catch (ParserConfigurationException localParserConfigurationException) {
/* 265 */       throw new KeyResolverException("empty", localParserConfigurationException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public SecretKey engineLookupAndResolveSecretKey(Element paramElement, String paramString, StorageResolver paramStorageResolver)
/*     */   {
/* 280 */     return null;
/*     */   }
/*     */ 
/*     */   static Element getDocumentElement(Set paramSet) {
/* 284 */     Iterator localIterator = paramSet.iterator();
/* 285 */     Element localElement1 = null;
/* 286 */     while (localIterator.hasNext()) {
/* 287 */       localObject1 = (Node)localIterator.next();
/* 288 */       if ((localObject1 != null) && (((Node)localObject1).getNodeType() == 1)) {
/* 289 */         localElement1 = (Element)localObject1;
/* 290 */         break;
/*     */       }
/*     */     }
/*     */ 
/* 294 */     Object localObject1 = new ArrayList(10);
/*     */ 
/* 297 */     while (localElement1 != null) {
/* 298 */       ((List)localObject1).add(localElement1);
/* 299 */       localObject2 = localElement1.getParentNode();
/* 300 */       if ((localObject2 == null) || (((Node)localObject2).getNodeType() != 1)) {
/*     */         break;
/*     */       }
/* 303 */       localElement1 = (Element)localObject2;
/*     */     }
/*     */ 
/* 306 */     Object localObject2 = ((List)localObject1).listIterator(((List)localObject1).size() - 1);
/* 307 */     Element localElement2 = null;
/* 308 */     while (((ListIterator)localObject2).hasPrevious()) {
/* 309 */       localElement2 = (Element)((ListIterator)localObject2).previous();
/* 310 */       if (paramSet.contains(localElement2)) {
/* 311 */         return localElement2;
/*     */       }
/*     */     }
/* 314 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.keys.keyresolver.implementations.RetrievalMethodResolver
 * JD-Core Version:    0.6.2
 */