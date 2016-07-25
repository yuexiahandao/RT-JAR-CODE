/*     */ package com.sun.org.apache.xml.internal.security.signature;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.c14n.CanonicalizationException;
/*     */ import com.sun.org.apache.xml.internal.security.c14n.InvalidCanonicalizerException;
/*     */ import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
/*     */ import com.sun.org.apache.xml.internal.security.transforms.Transforms;
/*     */ import com.sun.org.apache.xml.internal.security.utils.I18n;
/*     */ import com.sun.org.apache.xml.internal.security.utils.SignatureElementProxy;
/*     */ import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
/*     */ import com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolver;
/*     */ import com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolverSpi;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.DOMException;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class Manifest extends SignatureElementProxy
/*     */ {
/*  62 */   static Logger log = Logger.getLogger(Manifest.class.getName());
/*     */   List _references;
/*     */   Element[] _referencesEl;
/*  70 */   private boolean[] verificationResults = null;
/*     */ 
/*  73 */   HashMap _resolverProperties = null;
/*     */ 
/*  76 */   List _perManifestResolvers = null;
/*     */ 
/*     */   public Manifest(Document paramDocument)
/*     */   {
/*  85 */     super(paramDocument);
/*     */ 
/*  87 */     XMLUtils.addReturnToElement(this._constructionElement);
/*     */ 
/*  89 */     this._references = new ArrayList();
/*     */   }
/*     */ 
/*     */   public Manifest(Element paramElement, String paramString)
/*     */     throws XMLSecurityException
/*     */   {
/* 102 */     super(paramElement, paramString);
/*     */ 
/* 104 */     Attr localAttr1 = paramElement.getAttributeNodeNS(null, "Id");
/* 105 */     if (localAttr1 != null) {
/* 106 */       paramElement.setIdAttributeNode(localAttr1, true);
/*     */     }
/*     */ 
/* 110 */     this._referencesEl = XMLUtils.selectDsNodes(this._constructionElement.getFirstChild(), "Reference");
/*     */ 
/* 112 */     int i = this._referencesEl.length;
/*     */ 
/* 114 */     if (i == 0)
/*     */     {
/* 117 */       Object[] arrayOfObject = { "Reference", "Manifest" };
/*     */ 
/* 120 */       throw new DOMException((short)4, I18n.translate("xml.WrongContent", arrayOfObject));
/*     */     }
/*     */ 
/* 126 */     this._references = new ArrayList(i);
/*     */ 
/* 128 */     for (int j = 0; j < i; j++) {
/* 129 */       Element localElement = this._referencesEl[j];
/* 130 */       Attr localAttr2 = localElement.getAttributeNodeNS(null, "Id");
/* 131 */       if (localAttr2 != null) {
/* 132 */         localElement.setIdAttributeNode(localAttr2, true);
/*     */       }
/* 134 */       this._references.add(null);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addDocument(String paramString1, String paramString2, Transforms paramTransforms, String paramString3, String paramString4, String paramString5)
/*     */     throws XMLSignatureException
/*     */   {
/* 156 */     Reference localReference = new Reference(this._doc, paramString1, paramString2, this, paramTransforms, paramString3);
/*     */ 
/* 159 */     if (paramString4 != null) {
/* 160 */       localReference.setId(paramString4);
/*     */     }
/*     */ 
/* 163 */     if (paramString5 != null) {
/* 164 */       localReference.setType(paramString5);
/*     */     }
/*     */ 
/* 168 */     this._references.add(localReference);
/*     */ 
/* 171 */     this._constructionElement.appendChild(localReference.getElement());
/* 172 */     XMLUtils.addReturnToElement(this._constructionElement);
/*     */   }
/*     */ 
/*     */   public void generateDigestValues()
/*     */     throws XMLSignatureException, ReferenceNotInitializedException
/*     */   {
/* 186 */     for (int i = 0; i < getLength(); i++)
/*     */     {
/* 189 */       Reference localReference = (Reference)this._references.get(i);
/*     */ 
/* 191 */       localReference.generateDigestValue();
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getLength()
/*     */   {
/* 201 */     return this._references.size();
/*     */   }
/*     */ 
/*     */   public Reference item(int paramInt)
/*     */     throws XMLSecurityException
/*     */   {
/* 214 */     if (this._references.get(paramInt) == null)
/*     */     {
/* 217 */       Reference localReference = new Reference(this._referencesEl[paramInt], this._baseURI, this);
/*     */ 
/* 219 */       this._references.set(paramInt, localReference);
/*     */     }
/*     */ 
/* 222 */     return (Reference)this._references.get(paramInt);
/*     */   }
/*     */ 
/*     */   public void setId(String paramString)
/*     */   {
/* 233 */     if (paramString != null)
/* 234 */       setLocalIdAttribute("Id", paramString);
/*     */   }
/*     */ 
/*     */   public String getId()
/*     */   {
/* 244 */     return this._constructionElement.getAttributeNS(null, "Id");
/*     */   }
/*     */ 
/*     */   public boolean verifyReferences()
/*     */     throws MissingResourceFailureException, XMLSecurityException
/*     */   {
/* 266 */     return verifyReferences(false);
/*     */   }
/*     */ 
/*     */   public boolean verifyReferences(boolean paramBoolean)
/*     */     throws MissingResourceFailureException, XMLSecurityException
/*     */   {
/* 289 */     if (this._referencesEl == null) {
/* 290 */       this._referencesEl = XMLUtils.selectDsNodes(this._constructionElement.getFirstChild(), "Reference");
/*     */     }
/*     */ 
/* 294 */     if (log.isLoggable(Level.FINE)) {
/* 295 */       log.log(Level.FINE, "verify " + this._referencesEl.length + " References");
/* 296 */       log.log(Level.FINE, "I am " + (paramBoolean ? "" : "not") + " requested to follow nested Manifests");
/*     */     }
/*     */ 
/* 300 */     boolean bool1 = true;
/*     */ 
/* 302 */     if (this._referencesEl.length == 0) {
/* 303 */       throw new XMLSecurityException("empty");
/*     */     }
/*     */ 
/* 306 */     this.verificationResults = new boolean[this._referencesEl.length];
/*     */ 
/* 309 */     for (int i = 0; 
/* 310 */       i < this._referencesEl.length; i++) {
/* 311 */       Reference localReference = new Reference(this._referencesEl[i], this._baseURI, this);
/*     */ 
/* 314 */       this._references.set(i, localReference);
/*     */       try
/*     */       {
/* 318 */         boolean bool2 = localReference.verify();
/*     */ 
/* 320 */         setVerificationResult(i, bool2);
/*     */ 
/* 322 */         if (!bool2) {
/* 323 */           bool1 = false;
/*     */         }
/* 325 */         if (log.isLoggable(Level.FINE)) {
/* 326 */           log.log(Level.FINE, "The Reference has Type " + localReference.getType());
/*     */         }
/*     */ 
/* 329 */         if ((bool1) && (paramBoolean) && (localReference.typeIsReferenceToManifest()))
/*     */         {
/* 331 */           log.log(Level.FINE, "We have to follow a nested Manifest");
/*     */           try
/*     */           {
/* 334 */             XMLSignatureInput localXMLSignatureInput = localReference.dereferenceURIandPerformTransforms(null);
/*     */ 
/* 336 */             Set localSet = localXMLSignatureInput.getNodeSet();
/* 337 */             Manifest localManifest = null;
/* 338 */             Iterator localIterator = localSet.iterator();
/*     */ 
/* 340 */             while (localIterator.hasNext()) {
/* 341 */               Node localNode = (Node)localIterator.next();
/*     */ 
/* 343 */               if ((localNode.getNodeType() == 1) && (((Element)localNode).getNamespaceURI().equals("http://www.w3.org/2000/09/xmldsig#")) && (((Element)localNode).getLocalName().equals("Manifest")))
/*     */               {
/*     */                 try
/*     */                 {
/* 348 */                   localManifest = new Manifest((Element)localNode, localXMLSignatureInput.getSourceURI());
/*     */                 }
/*     */                 catch (XMLSecurityException localXMLSecurityException)
/*     */                 {
/*     */                 }
/*     */ 
/*     */               }
/*     */ 
/*     */             }
/*     */ 
/* 360 */             if (localManifest == null)
/*     */             {
/* 364 */               throw new MissingResourceFailureException("empty", localReference);
/*     */             }
/*     */ 
/* 368 */             localManifest._perManifestResolvers = this._perManifestResolvers;
/*     */ 
/* 370 */             localManifest._resolverProperties = this._resolverProperties;
/*     */ 
/* 373 */             boolean bool3 = localManifest.verifyReferences(paramBoolean);
/*     */ 
/* 376 */             if (!bool3) {
/* 377 */               bool1 = false;
/*     */ 
/* 379 */               log.log(Level.WARNING, "The nested Manifest was invalid (bad)");
/*     */             } else {
/* 381 */               log.log(Level.FINE, "The nested Manifest was valid (good)");
/*     */             }
/*     */           } catch (IOException localIOException) {
/* 384 */             throw new ReferenceNotInitializedException("empty", localIOException);
/*     */           } catch (ParserConfigurationException localParserConfigurationException) {
/* 386 */             throw new ReferenceNotInitializedException("empty", localParserConfigurationException);
/*     */           } catch (SAXException localSAXException) {
/* 388 */             throw new ReferenceNotInitializedException("empty", localSAXException);
/*     */           }
/*     */         }
/*     */       } catch (ReferenceNotInitializedException localReferenceNotInitializedException) {
/* 392 */         Object[] arrayOfObject = { localReference.getURI() };
/*     */ 
/* 394 */         throw new MissingResourceFailureException("signature.Verification.Reference.NoInput", arrayOfObject, localReferenceNotInitializedException, localReference);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 400 */     return bool1;
/*     */   }
/*     */ 
/*     */   private void setVerificationResult(int paramInt, boolean paramBoolean)
/*     */   {
/* 412 */     if (this.verificationResults == null) {
/* 413 */       this.verificationResults = new boolean[getLength()];
/*     */     }
/*     */ 
/* 416 */     this.verificationResults[paramInt] = paramBoolean;
/*     */   }
/*     */ 
/*     */   public boolean getVerificationResult(int paramInt)
/*     */     throws XMLSecurityException
/*     */   {
/* 430 */     if ((paramInt < 0) || (paramInt > getLength() - 1)) {
/* 431 */       Object[] arrayOfObject = { Integer.toString(paramInt), Integer.toString(getLength()) };
/*     */ 
/* 433 */       IndexOutOfBoundsException localIndexOutOfBoundsException = new IndexOutOfBoundsException(I18n.translate("signature.Verification.IndexOutOfBounds", arrayOfObject));
/*     */ 
/* 437 */       throw new XMLSecurityException("generic.EmptyMessage", localIndexOutOfBoundsException);
/*     */     }
/*     */ 
/* 440 */     if (this.verificationResults == null) {
/*     */       try {
/* 442 */         verifyReferences();
/*     */       } catch (Exception localException) {
/* 444 */         throw new XMLSecurityException("generic.EmptyMessage", localException);
/*     */       }
/*     */     }
/*     */ 
/* 448 */     return this.verificationResults[paramInt];
/*     */   }
/*     */ 
/*     */   public void addResourceResolver(ResourceResolver paramResourceResolver)
/*     */   {
/* 458 */     if (paramResourceResolver == null) {
/* 459 */       return;
/*     */     }
/* 461 */     if (this._perManifestResolvers == null)
/* 462 */       this._perManifestResolvers = new ArrayList();
/* 463 */     this._perManifestResolvers.add(paramResourceResolver);
/*     */   }
/*     */ 
/*     */   public void addResourceResolver(ResourceResolverSpi paramResourceResolverSpi)
/*     */   {
/* 474 */     if (paramResourceResolverSpi == null) {
/* 475 */       return;
/*     */     }
/* 477 */     if (this._perManifestResolvers == null)
/* 478 */       this._perManifestResolvers = new ArrayList();
/* 479 */     this._perManifestResolvers.add(new ResourceResolver(paramResourceResolverSpi));
/*     */   }
/*     */ 
/*     */   public void setResolverProperty(String paramString1, String paramString2)
/*     */   {
/* 491 */     if (this._resolverProperties == null) {
/* 492 */       this._resolverProperties = new HashMap(10);
/*     */     }
/* 494 */     this._resolverProperties.put(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   public String getResolverProperty(String paramString)
/*     */   {
/* 504 */     return (String)this._resolverProperties.get(paramString);
/*     */   }
/*     */ 
/*     */   public byte[] getSignedContentItem(int paramInt)
/*     */     throws XMLSignatureException
/*     */   {
/*     */     try
/*     */     {
/* 518 */       return getReferencedContentAfterTransformsItem(paramInt).getBytes();
/*     */     } catch (IOException localIOException) {
/* 520 */       throw new XMLSignatureException("empty", localIOException);
/*     */     } catch (CanonicalizationException localCanonicalizationException) {
/* 522 */       throw new XMLSignatureException("empty", localCanonicalizationException);
/*     */     } catch (InvalidCanonicalizerException localInvalidCanonicalizerException) {
/* 524 */       throw new XMLSignatureException("empty", localInvalidCanonicalizerException);
/*     */     } catch (XMLSecurityException localXMLSecurityException) {
/* 526 */       throw new XMLSignatureException("empty", localXMLSecurityException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public XMLSignatureInput getReferencedContentBeforeTransformsItem(int paramInt)
/*     */     throws XMLSecurityException
/*     */   {
/* 539 */     return item(paramInt).getContentsBeforeTransformation();
/*     */   }
/*     */ 
/*     */   public XMLSignatureInput getReferencedContentAfterTransformsItem(int paramInt)
/*     */     throws XMLSecurityException
/*     */   {
/* 551 */     return item(paramInt).getContentsAfterTransformation();
/*     */   }
/*     */ 
/*     */   public int getSignedContentLength()
/*     */   {
/* 560 */     return getLength();
/*     */   }
/*     */ 
/*     */   public String getBaseLocalName()
/*     */   {
/* 569 */     return "Manifest";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.signature.Manifest
 * JD-Core Version:    0.6.2
 */