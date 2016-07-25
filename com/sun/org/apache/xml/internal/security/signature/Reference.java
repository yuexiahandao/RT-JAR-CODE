/*     */ package com.sun.org.apache.xml.internal.security.signature;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.algorithms.MessageDigestAlgorithm;
/*     */ import com.sun.org.apache.xml.internal.security.c14n.CanonicalizationException;
/*     */ import com.sun.org.apache.xml.internal.security.c14n.InvalidCanonicalizerException;
/*     */ import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
/*     */ import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
/*     */ import com.sun.org.apache.xml.internal.security.transforms.InvalidTransformException;
/*     */ import com.sun.org.apache.xml.internal.security.transforms.Transform;
/*     */ import com.sun.org.apache.xml.internal.security.transforms.TransformationException;
/*     */ import com.sun.org.apache.xml.internal.security.transforms.Transforms;
/*     */ import com.sun.org.apache.xml.internal.security.transforms.params.InclusiveNamespaces;
/*     */ import com.sun.org.apache.xml.internal.security.utils.Base64;
/*     */ import com.sun.org.apache.xml.internal.security.utils.DigesterOutputStream;
/*     */ import com.sun.org.apache.xml.internal.security.utils.SignatureElementProxy;
/*     */ import com.sun.org.apache.xml.internal.security.utils.UnsyncBufferedOutputStream;
/*     */ import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
/*     */ import com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolver;
/*     */ import com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolverException;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.Text;
/*     */ 
/*     */ public class Reference extends SignatureElementProxy
/*     */ {
/* 111 */   private static boolean useC14N11 = ((Boolean)AccessController.doPrivileged(new PrivilegedAction()
/*     */   {
/*     */     public Boolean run() {
/* 114 */       return Boolean.valueOf(Boolean.getBoolean("com.sun.org.apache.xml.internal.security.useC14N11"));
/*     */     }
/*     */   })).booleanValue();
/*     */   public static final boolean CacheSignedNodes = false;
/* 133 */   static Logger log = Logger.getLogger(Reference.class.getName());
/*     */   public static final String OBJECT_URI = "http://www.w3.org/2000/09/xmldsig#Object";
/*     */   public static final String MANIFEST_URI = "http://www.w3.org/2000/09/xmldsig#Manifest";
/* 144 */   Manifest _manifest = null;
/*     */   XMLSignatureInput _transformsOutput;
/*     */   private Transforms transforms;
/*     */   private Element digestMethodElem;
/*     */   private Element digestValueElement;
/*     */ 
/*     */   protected Reference(Document paramDocument, String paramString1, String paramString2, Manifest paramManifest, Transforms paramTransforms, String paramString3)
/*     */     throws XMLSignatureException
/*     */   {
/* 169 */     super(paramDocument);
/*     */ 
/* 171 */     XMLUtils.addReturnToElement(this._constructionElement);
/*     */ 
/* 173 */     this._baseURI = paramString1;
/* 174 */     this._manifest = paramManifest;
/*     */ 
/* 176 */     setURI(paramString2);
/*     */ 
/* 183 */     if (paramTransforms != null) {
/* 184 */       this.transforms = paramTransforms;
/* 185 */       this._constructionElement.appendChild(paramTransforms.getElement());
/* 186 */       XMLUtils.addReturnToElement(this._constructionElement);
/*     */     }
/*     */ 
/* 189 */     MessageDigestAlgorithm localMessageDigestAlgorithm = MessageDigestAlgorithm.getInstance(this._doc, paramString3);
/*     */ 
/* 193 */     this.digestMethodElem = localMessageDigestAlgorithm.getElement();
/* 194 */     this._constructionElement.appendChild(this.digestMethodElem);
/* 195 */     XMLUtils.addReturnToElement(this._constructionElement);
/*     */ 
/* 198 */     this.digestValueElement = XMLUtils.createElementInSignatureSpace(this._doc, "DigestValue");
/*     */ 
/* 202 */     this._constructionElement.appendChild(this.digestValueElement);
/* 203 */     XMLUtils.addReturnToElement(this._constructionElement);
/*     */   }
/*     */ 
/*     */   protected Reference(Element paramElement, String paramString, Manifest paramManifest)
/*     */     throws XMLSecurityException
/*     */   {
/* 219 */     super(paramElement, paramString);
/* 220 */     this._baseURI = paramString;
/* 221 */     Element localElement = XMLUtils.getNextElement(paramElement.getFirstChild());
/* 222 */     if (("Transforms".equals(localElement.getLocalName())) && ("http://www.w3.org/2000/09/xmldsig#".equals(localElement.getNamespaceURI())))
/*     */     {
/* 224 */       this.transforms = new Transforms(localElement, this._baseURI);
/* 225 */       localElement = XMLUtils.getNextElement(localElement.getNextSibling());
/*     */     }
/* 227 */     this.digestMethodElem = localElement;
/* 228 */     this.digestValueElement = XMLUtils.getNextElement(this.digestMethodElem.getNextSibling());
/* 229 */     this._manifest = paramManifest;
/*     */   }
/*     */ 
/*     */   public MessageDigestAlgorithm getMessageDigestAlgorithm()
/*     */     throws XMLSignatureException
/*     */   {
/* 243 */     if (this.digestMethodElem == null) {
/* 244 */       return null;
/*     */     }
/*     */ 
/* 247 */     String str = this.digestMethodElem.getAttributeNS(null, "Algorithm");
/*     */ 
/* 250 */     if (str == null) {
/* 251 */       return null;
/*     */     }
/*     */ 
/* 254 */     return MessageDigestAlgorithm.getInstance(this._doc, str);
/*     */   }
/*     */ 
/*     */   public void setURI(String paramString)
/*     */   {
/* 264 */     if (paramString != null)
/* 265 */       this._constructionElement.setAttributeNS(null, "URI", paramString);
/*     */   }
/*     */ 
/*     */   public String getURI()
/*     */   {
/* 276 */     return this._constructionElement.getAttributeNS(null, "URI");
/*     */   }
/*     */ 
/*     */   public void setId(String paramString)
/*     */   {
/* 286 */     if (paramString != null)
/* 287 */       setLocalIdAttribute("Id", paramString);
/*     */   }
/*     */ 
/*     */   public String getId()
/*     */   {
/* 297 */     return this._constructionElement.getAttributeNS(null, "Id");
/*     */   }
/*     */ 
/*     */   public void setType(String paramString)
/*     */   {
/* 307 */     if (paramString != null)
/* 308 */       this._constructionElement.setAttributeNS(null, "Type", paramString);
/*     */   }
/*     */ 
/*     */   public String getType()
/*     */   {
/* 319 */     return this._constructionElement.getAttributeNS(null, "Type");
/*     */   }
/*     */ 
/*     */   public boolean typeIsReferenceToObject()
/*     */   {
/* 333 */     if ("http://www.w3.org/2000/09/xmldsig#Object".equals(getType())) {
/* 334 */       return true;
/*     */     }
/*     */ 
/* 337 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean typeIsReferenceToManifest()
/*     */   {
/* 350 */     if ("http://www.w3.org/2000/09/xmldsig#Manifest".equals(getType())) {
/* 351 */       return true;
/*     */     }
/*     */ 
/* 354 */     return false;
/*     */   }
/*     */ 
/*     */   private void setDigestValueElement(byte[] paramArrayOfByte)
/*     */   {
/* 364 */     Node localNode = this.digestValueElement.getFirstChild();
/* 365 */     while (localNode != null) {
/* 366 */       this.digestValueElement.removeChild(localNode);
/* 367 */       localNode = localNode.getNextSibling();
/*     */     }
/*     */ 
/* 370 */     String str = Base64.encode(paramArrayOfByte);
/* 371 */     Text localText = this._doc.createTextNode(str);
/*     */ 
/* 373 */     this.digestValueElement.appendChild(localText);
/*     */   }
/*     */ 
/*     */   public void generateDigestValue()
/*     */     throws XMLSignatureException, ReferenceNotInitializedException
/*     */   {
/* 384 */     setDigestValueElement(calculateDigest(false));
/*     */   }
/*     */ 
/*     */   public XMLSignatureInput getContentsBeforeTransformation()
/*     */     throws ReferenceNotInitializedException
/*     */   {
/*     */     try
/*     */     {
/* 397 */       Attr localAttr = this._constructionElement.getAttributeNodeNS(null, "URI");
/*     */       String str;
/* 401 */       if (localAttr == null)
/* 402 */         str = null;
/*     */       else {
/* 404 */         str = localAttr.getNodeValue();
/*     */       }
/*     */ 
/* 407 */       ResourceResolver localResourceResolver = ResourceResolver.getInstance(localAttr, this._baseURI, this._manifest._perManifestResolvers);
/*     */       Object localObject;
/* 410 */       if (localResourceResolver == null) {
/* 411 */         localObject = new Object[] { str };
/*     */ 
/* 413 */         throw new ReferenceNotInitializedException("signature.Verification.Reference.NoInput", (Object[])localObject);
/*     */       }
/*     */ 
/* 417 */       localResourceResolver.addProperties(this._manifest._resolverProperties);
/*     */ 
/* 419 */       return localResourceResolver.resolve(localAttr, this._baseURI);
/*     */     }
/*     */     catch (ResourceResolverException localResourceResolverException)
/*     */     {
/* 424 */       throw new ReferenceNotInitializedException("empty", localResourceResolverException);
/*     */     } catch (XMLSecurityException localXMLSecurityException) {
/* 426 */       throw new ReferenceNotInitializedException("empty", localXMLSecurityException);
/*     */     }
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public XMLSignatureInput getTransformsInput()
/*     */     throws ReferenceNotInitializedException
/*     */   {
/* 440 */     XMLSignatureInput localXMLSignatureInput1 = getContentsBeforeTransformation();
/*     */     XMLSignatureInput localXMLSignatureInput2;
/*     */     try
/*     */     {
/* 443 */       localXMLSignatureInput2 = new XMLSignatureInput(localXMLSignatureInput1.getBytes());
/*     */     } catch (CanonicalizationException localCanonicalizationException) {
/* 445 */       throw new ReferenceNotInitializedException("empty", localCanonicalizationException);
/*     */     } catch (IOException localIOException) {
/* 447 */       throw new ReferenceNotInitializedException("empty", localIOException);
/*     */     }
/* 449 */     localXMLSignatureInput2.setSourceURI(localXMLSignatureInput1.getSourceURI());
/* 450 */     return localXMLSignatureInput2;
/*     */   }
/*     */ 
/*     */   private XMLSignatureInput getContentsAfterTransformation(XMLSignatureInput paramXMLSignatureInput, OutputStream paramOutputStream)
/*     */     throws XMLSignatureException
/*     */   {
/*     */     try
/*     */     {
/* 458 */       Transforms localTransforms = getTransforms();
/* 459 */       XMLSignatureInput localXMLSignatureInput = null;
/*     */ 
/* 461 */       if (localTransforms != null) {
/* 462 */         localXMLSignatureInput = localTransforms.performTransforms(paramXMLSignatureInput, paramOutputStream);
/* 463 */         this._transformsOutput = localXMLSignatureInput;
/*     */       }
/*     */ 
/* 467 */       return paramXMLSignatureInput;
/*     */     }
/*     */     catch (ResourceResolverException localResourceResolverException)
/*     */     {
/* 472 */       throw new XMLSignatureException("empty", localResourceResolverException);
/*     */     } catch (CanonicalizationException localCanonicalizationException) {
/* 474 */       throw new XMLSignatureException("empty", localCanonicalizationException);
/*     */     } catch (InvalidCanonicalizerException localInvalidCanonicalizerException) {
/* 476 */       throw new XMLSignatureException("empty", localInvalidCanonicalizerException);
/*     */     } catch (TransformationException localTransformationException) {
/* 478 */       throw new XMLSignatureException("empty", localTransformationException);
/*     */     } catch (XMLSecurityException localXMLSecurityException) {
/* 480 */       throw new XMLSignatureException("empty", localXMLSecurityException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public XMLSignatureInput getContentsAfterTransformation()
/*     */     throws XMLSignatureException
/*     */   {
/* 492 */     XMLSignatureInput localXMLSignatureInput = getContentsBeforeTransformation();
/*     */ 
/* 494 */     return getContentsAfterTransformation(localXMLSignatureInput, null);
/*     */   }
/*     */ 
/*     */   public XMLSignatureInput getNodesetBeforeFirstCanonicalization()
/*     */     throws XMLSignatureException
/*     */   {
/*     */     try
/*     */     {
/* 508 */       XMLSignatureInput localXMLSignatureInput1 = getContentsBeforeTransformation();
/* 509 */       XMLSignatureInput localXMLSignatureInput2 = localXMLSignatureInput1;
/* 510 */       Transforms localTransforms = getTransforms();
/*     */ 
/* 512 */       if (localTransforms != null) {
/* 513 */         for (int i = 0; i < localTransforms.getLength(); i++) {
/* 514 */           Transform localTransform = localTransforms.item(i);
/* 515 */           String str = localTransform.getURI();
/*     */ 
/* 517 */           if ((str.equals("http://www.w3.org/2001/10/xml-exc-c14n#")) || (str.equals("http://www.w3.org/2001/10/xml-exc-c14n#WithComments")) || (str.equals("http://www.w3.org/TR/2001/REC-xml-c14n-20010315")) || (str.equals("http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments")))
/*     */           {
/*     */             break;
/*     */           }
/*     */ 
/* 529 */           localXMLSignatureInput2 = localTransform.performTransform(localXMLSignatureInput2, null);
/*     */         }
/*     */ 
/* 532 */         localXMLSignatureInput2.setSourceURI(localXMLSignatureInput1.getSourceURI());
/*     */       }
/* 534 */       return localXMLSignatureInput2;
/*     */     } catch (IOException localIOException) {
/* 536 */       throw new XMLSignatureException("empty", localIOException);
/*     */     } catch (ResourceResolverException localResourceResolverException) {
/* 538 */       throw new XMLSignatureException("empty", localResourceResolverException);
/*     */     } catch (CanonicalizationException localCanonicalizationException) {
/* 540 */       throw new XMLSignatureException("empty", localCanonicalizationException);
/*     */     } catch (InvalidCanonicalizerException localInvalidCanonicalizerException) {
/* 542 */       throw new XMLSignatureException("empty", localInvalidCanonicalizerException);
/*     */     } catch (TransformationException localTransformationException) {
/* 544 */       throw new XMLSignatureException("empty", localTransformationException);
/*     */     } catch (XMLSecurityException localXMLSecurityException) {
/* 546 */       throw new XMLSignatureException("empty", localXMLSecurityException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getHTMLRepresentation()
/*     */     throws XMLSignatureException
/*     */   {
/*     */     try
/*     */     {
/* 558 */       XMLSignatureInput localXMLSignatureInput = getNodesetBeforeFirstCanonicalization();
/* 559 */       Object localObject1 = new HashSet();
/*     */ 
/* 562 */       Transforms localTransforms = getTransforms();
/* 563 */       Object localObject2 = null;
/*     */ 
/* 565 */       if (localTransforms != null) {
/* 566 */         for (int i = 0; i < localTransforms.getLength(); i++) {
/* 567 */           Transform localTransform = localTransforms.item(i);
/* 568 */           String str = localTransform.getURI();
/*     */ 
/* 570 */           if ((str.equals("http://www.w3.org/2001/10/xml-exc-c14n#")) || (str.equals("http://www.w3.org/2001/10/xml-exc-c14n#WithComments")))
/*     */           {
/* 573 */             localObject2 = localTransform;
/*     */ 
/* 575 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 580 */       if (localObject2 != null)
/*     */       {
/* 582 */         if (localObject2.length("http://www.w3.org/2001/10/xml-exc-c14n#", "InclusiveNamespaces") == 1)
/*     */         {
/* 588 */           InclusiveNamespaces localInclusiveNamespaces = new InclusiveNamespaces(XMLUtils.selectNode(localObject2.getElement().getFirstChild(), "http://www.w3.org/2001/10/xml-exc-c14n#", "InclusiveNamespaces", 0), getBaseURI());
/*     */ 
/* 594 */           localObject1 = InclusiveNamespaces.prefixStr2Set(localInclusiveNamespaces.getInclusiveNamespaces());
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 600 */       return localXMLSignatureInput.getHTMLRepresentation((Set)localObject1);
/*     */     } catch (TransformationException localTransformationException) {
/* 602 */       throw new XMLSignatureException("empty", localTransformationException);
/*     */     } catch (InvalidTransformException localInvalidTransformException) {
/* 604 */       throw new XMLSignatureException("empty", localInvalidTransformException);
/*     */     } catch (XMLSecurityException localXMLSecurityException) {
/* 606 */       throw new XMLSignatureException("empty", localXMLSecurityException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public XMLSignatureInput getTransformsOutput()
/*     */   {
/* 615 */     return this._transformsOutput;
/*     */   }
/*     */ 
/*     */   protected XMLSignatureInput dereferenceURIandPerformTransforms(OutputStream paramOutputStream)
/*     */     throws XMLSignatureException
/*     */   {
/*     */     try
/*     */     {
/* 631 */       XMLSignatureInput localXMLSignatureInput1 = getContentsBeforeTransformation();
/* 632 */       XMLSignatureInput localXMLSignatureInput2 = getContentsAfterTransformation(localXMLSignatureInput1, paramOutputStream);
/*     */ 
/* 641 */       this._transformsOutput = localXMLSignatureInput2;
/*     */ 
/* 645 */       return localXMLSignatureInput2;
/*     */     } catch (XMLSecurityException localXMLSecurityException) {
/* 647 */       throw new ReferenceNotInitializedException("empty", localXMLSecurityException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Transforms getTransforms()
/*     */     throws XMLSignatureException, InvalidTransformException, TransformationException, XMLSecurityException
/*     */   {
/* 664 */     return this.transforms;
/*     */   }
/*     */ 
/*     */   public byte[] getReferencedBytes()
/*     */     throws ReferenceNotInitializedException, XMLSignatureException
/*     */   {
/*     */     try
/*     */     {
/* 677 */       XMLSignatureInput localXMLSignatureInput = dereferenceURIandPerformTransforms(null);
/*     */ 
/* 679 */       return localXMLSignatureInput.getBytes();
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/* 683 */       throw new ReferenceNotInitializedException("empty", localIOException);
/*     */     } catch (CanonicalizationException localCanonicalizationException) {
/* 685 */       throw new ReferenceNotInitializedException("empty", localCanonicalizationException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private byte[] calculateDigest(boolean paramBoolean)
/*     */     throws ReferenceNotInitializedException, XMLSignatureException
/*     */   {
/*     */     try
/*     */     {
/* 704 */       MessageDigestAlgorithm localMessageDigestAlgorithm = getMessageDigestAlgorithm();
/*     */ 
/* 706 */       localMessageDigestAlgorithm.reset();
/* 707 */       DigesterOutputStream localDigesterOutputStream = new DigesterOutputStream(localMessageDigestAlgorithm);
/* 708 */       UnsyncBufferedOutputStream localUnsyncBufferedOutputStream = new UnsyncBufferedOutputStream(localDigesterOutputStream);
/* 709 */       XMLSignatureInput localXMLSignatureInput = dereferenceURIandPerformTransforms(localUnsyncBufferedOutputStream);
/*     */ 
/* 712 */       if ((useC14N11) && (!paramBoolean) && (!localXMLSignatureInput.isOutputStreamSet()) && (!localXMLSignatureInput.isOctetStream()))
/*     */       {
/* 714 */         if (this.transforms == null) {
/* 715 */           this.transforms = new Transforms(this._doc);
/* 716 */           this._constructionElement.insertBefore(this.transforms.getElement(), this.digestMethodElem);
/*     */         }
/*     */ 
/* 719 */         this.transforms.addTransform("http://www.w3.org/2006/12/xml-c14n11");
/* 720 */         localXMLSignatureInput.updateOutputStream(localUnsyncBufferedOutputStream, true);
/*     */       } else {
/* 722 */         localXMLSignatureInput.updateOutputStream(localUnsyncBufferedOutputStream);
/*     */       }
/* 724 */       localUnsyncBufferedOutputStream.flush();
/*     */ 
/* 728 */       return localDigesterOutputStream.getDigestValue();
/*     */     } catch (XMLSecurityException localXMLSecurityException) {
/* 730 */       throw new ReferenceNotInitializedException("empty", localXMLSecurityException);
/*     */     } catch (IOException localIOException) {
/* 732 */       throw new ReferenceNotInitializedException("empty", localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public byte[] getDigestValue()
/*     */     throws Base64DecodingException, XMLSecurityException
/*     */   {
/* 744 */     if (this.digestValueElement == null)
/*     */     {
/* 746 */       localObject = new Object[] { "DigestValue", "http://www.w3.org/2000/09/xmldsig#" };
/*     */ 
/* 748 */       throw new XMLSecurityException("signature.Verification.NoSignatureElement", (Object[])localObject);
/*     */     }
/*     */ 
/* 752 */     Object localObject = Base64.decode(this.digestValueElement);
/* 753 */     return localObject;
/*     */   }
/*     */ 
/*     */   public boolean verify()
/*     */     throws ReferenceNotInitializedException, XMLSecurityException
/*     */   {
/* 767 */     byte[] arrayOfByte1 = getDigestValue();
/* 768 */     byte[] arrayOfByte2 = calculateDigest(true);
/* 769 */     boolean bool = MessageDigestAlgorithm.isEqual(arrayOfByte1, arrayOfByte2);
/*     */ 
/* 771 */     if (!bool) {
/* 772 */       log.log(Level.WARNING, "Verification failed for URI \"" + getURI() + "\"");
/* 773 */       log.log(Level.WARNING, "Expected Digest: " + Base64.encode(arrayOfByte1));
/* 774 */       log.log(Level.WARNING, "Actual Digest: " + Base64.encode(arrayOfByte2));
/*     */     } else {
/* 776 */       log.log(Level.INFO, "Verification successful for URI \"" + getURI() + "\"");
/*     */     }
/*     */ 
/* 779 */     return bool;
/*     */   }
/*     */ 
/*     */   public String getBaseLocalName()
/*     */   {
/* 788 */     return "Reference";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.signature.Reference
 * JD-Core Version:    0.6.2
 */