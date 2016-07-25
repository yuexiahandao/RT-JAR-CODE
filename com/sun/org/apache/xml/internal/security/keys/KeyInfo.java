/*      */ package com.sun.org.apache.xml.internal.security.keys;
/*      */ 
/*      */ import com.sun.org.apache.xml.internal.security.encryption.EncryptedKey;
/*      */ import com.sun.org.apache.xml.internal.security.encryption.XMLCipher;
/*      */ import com.sun.org.apache.xml.internal.security.encryption.XMLEncryptionException;
/*      */ import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
/*      */ import com.sun.org.apache.xml.internal.security.keys.content.KeyName;
/*      */ import com.sun.org.apache.xml.internal.security.keys.content.KeyValue;
/*      */ import com.sun.org.apache.xml.internal.security.keys.content.MgmtData;
/*      */ import com.sun.org.apache.xml.internal.security.keys.content.PGPData;
/*      */ import com.sun.org.apache.xml.internal.security.keys.content.RetrievalMethod;
/*      */ import com.sun.org.apache.xml.internal.security.keys.content.SPKIData;
/*      */ import com.sun.org.apache.xml.internal.security.keys.content.X509Data;
/*      */ import com.sun.org.apache.xml.internal.security.keys.content.keyvalues.DSAKeyValue;
/*      */ import com.sun.org.apache.xml.internal.security.keys.content.keyvalues.RSAKeyValue;
/*      */ import com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolver;
/*      */ import com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverException;
/*      */ import com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolverSpi;
/*      */ import com.sun.org.apache.xml.internal.security.keys.storage.StorageResolver;
/*      */ import com.sun.org.apache.xml.internal.security.transforms.Transforms;
/*      */ import com.sun.org.apache.xml.internal.security.utils.SignatureElementProxy;
/*      */ import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
/*      */ import java.security.PublicKey;
/*      */ import java.security.cert.X509Certificate;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import javax.crypto.SecretKey;
/*      */ import org.w3c.dom.Attr;
/*      */ import org.w3c.dom.Document;
/*      */ import org.w3c.dom.Element;
/*      */ import org.w3c.dom.Node;
/*      */ import org.w3c.dom.NodeList;
/*      */ 
/*      */ public class KeyInfo extends SignatureElementProxy
/*      */ {
/*   99 */   static Logger log = Logger.getLogger(KeyInfo.class.getName());
/*      */ 
/*  101 */   List<X509Data> x509Datas = null;
/*  102 */   List<EncryptedKey> encryptedKeys = null;
/*      */ 
/*  108 */   static final List<StorageResolver> nullList = Collections.unmodifiableList(localArrayList);
/*      */ 
/* 1014 */   List<KeyResolverSpi> _internalKeyResolvers = new ArrayList();
/*      */ 
/* 1050 */   private List<StorageResolver> _storageResolvers = nullList;
/*      */ 
/* 1066 */   static boolean _alreadyInitialized = false;
/*      */ 
/*      */   public KeyInfo(Document paramDocument)
/*      */   {
/*  117 */     super(paramDocument);
/*      */ 
/*  119 */     XMLUtils.addReturnToElement(this._constructionElement);
/*      */   }
/*      */ 
/*      */   public KeyInfo(Element paramElement, String paramString)
/*      */     throws XMLSecurityException
/*      */   {
/*  131 */     super(paramElement, paramString);
/*      */ 
/*  133 */     Attr localAttr = paramElement.getAttributeNodeNS(null, "Id");
/*  134 */     if (localAttr != null)
/*  135 */       paramElement.setIdAttributeNode(localAttr, true);
/*      */   }
/*      */ 
/*      */   public void setId(String paramString)
/*      */   {
/*  146 */     if (paramString != null)
/*  147 */       setLocalIdAttribute("Id", paramString);
/*      */   }
/*      */ 
/*      */   public String getId()
/*      */   {
/*  157 */     return this._constructionElement.getAttributeNS(null, "Id");
/*      */   }
/*      */ 
/*      */   public void addKeyName(String paramString)
/*      */   {
/*  166 */     add(new KeyName(this._doc, paramString));
/*      */   }
/*      */ 
/*      */   public void add(KeyName paramKeyName)
/*      */   {
/*  176 */     this._constructionElement.appendChild(paramKeyName.getElement());
/*  177 */     XMLUtils.addReturnToElement(this._constructionElement);
/*      */   }
/*      */ 
/*      */   public void addKeyValue(PublicKey paramPublicKey)
/*      */   {
/*  186 */     add(new KeyValue(this._doc, paramPublicKey));
/*      */   }
/*      */ 
/*      */   public void addKeyValue(Element paramElement)
/*      */   {
/*  195 */     add(new KeyValue(this._doc, paramElement));
/*      */   }
/*      */ 
/*      */   public void add(DSAKeyValue paramDSAKeyValue)
/*      */   {
/*  204 */     add(new KeyValue(this._doc, paramDSAKeyValue));
/*      */   }
/*      */ 
/*      */   public void add(RSAKeyValue paramRSAKeyValue)
/*      */   {
/*  213 */     add(new KeyValue(this._doc, paramRSAKeyValue));
/*      */   }
/*      */ 
/*      */   public void add(PublicKey paramPublicKey)
/*      */   {
/*  222 */     add(new KeyValue(this._doc, paramPublicKey));
/*      */   }
/*      */ 
/*      */   public void add(KeyValue paramKeyValue)
/*      */   {
/*  231 */     this._constructionElement.appendChild(paramKeyValue.getElement());
/*  232 */     XMLUtils.addReturnToElement(this._constructionElement);
/*      */   }
/*      */ 
/*      */   public void addMgmtData(String paramString)
/*      */   {
/*  241 */     add(new MgmtData(this._doc, paramString));
/*      */   }
/*      */ 
/*      */   public void add(MgmtData paramMgmtData)
/*      */   {
/*  250 */     this._constructionElement.appendChild(paramMgmtData.getElement());
/*  251 */     XMLUtils.addReturnToElement(this._constructionElement);
/*      */   }
/*      */ 
/*      */   public void add(PGPData paramPGPData)
/*      */   {
/*  260 */     this._constructionElement.appendChild(paramPGPData.getElement());
/*  261 */     XMLUtils.addReturnToElement(this._constructionElement);
/*      */   }
/*      */ 
/*      */   public void addRetrievalMethod(String paramString1, Transforms paramTransforms, String paramString2)
/*      */   {
/*  273 */     add(new RetrievalMethod(this._doc, paramString1, paramTransforms, paramString2));
/*      */   }
/*      */ 
/*      */   public void add(RetrievalMethod paramRetrievalMethod)
/*      */   {
/*  282 */     this._constructionElement.appendChild(paramRetrievalMethod.getElement());
/*  283 */     XMLUtils.addReturnToElement(this._constructionElement);
/*      */   }
/*      */ 
/*      */   public void add(SPKIData paramSPKIData)
/*      */   {
/*  292 */     this._constructionElement.appendChild(paramSPKIData.getElement());
/*  293 */     XMLUtils.addReturnToElement(this._constructionElement);
/*      */   }
/*      */ 
/*      */   public void add(X509Data paramX509Data)
/*      */   {
/*  302 */     if (this.x509Datas == null)
/*  303 */       this.x509Datas = new ArrayList();
/*  304 */     this.x509Datas.add(paramX509Data);
/*  305 */     this._constructionElement.appendChild(paramX509Data.getElement());
/*  306 */     XMLUtils.addReturnToElement(this._constructionElement);
/*      */   }
/*      */ 
/*      */   public void add(EncryptedKey paramEncryptedKey)
/*      */     throws XMLEncryptionException
/*      */   {
/*  318 */     if (this.encryptedKeys == null)
/*  319 */       this.encryptedKeys = new ArrayList();
/*  320 */     this.encryptedKeys.add(paramEncryptedKey);
/*  321 */     XMLCipher localXMLCipher = XMLCipher.getInstance();
/*  322 */     this._constructionElement.appendChild(localXMLCipher.martial(paramEncryptedKey));
/*      */   }
/*      */ 
/*      */   public void addUnknownElement(Element paramElement)
/*      */   {
/*  331 */     this._constructionElement.appendChild(paramElement);
/*  332 */     XMLUtils.addReturnToElement(this._constructionElement);
/*      */   }
/*      */ 
/*      */   public int lengthKeyName()
/*      */   {
/*  341 */     return length("http://www.w3.org/2000/09/xmldsig#", "KeyName");
/*      */   }
/*      */ 
/*      */   public int lengthKeyValue()
/*      */   {
/*  350 */     return length("http://www.w3.org/2000/09/xmldsig#", "KeyValue");
/*      */   }
/*      */ 
/*      */   public int lengthMgmtData()
/*      */   {
/*  359 */     return length("http://www.w3.org/2000/09/xmldsig#", "MgmtData");
/*      */   }
/*      */ 
/*      */   public int lengthPGPData()
/*      */   {
/*  368 */     return length("http://www.w3.org/2000/09/xmldsig#", "PGPData");
/*      */   }
/*      */ 
/*      */   public int lengthRetrievalMethod()
/*      */   {
/*  377 */     return length("http://www.w3.org/2000/09/xmldsig#", "RetrievalMethod");
/*      */   }
/*      */ 
/*      */   public int lengthSPKIData()
/*      */   {
/*  387 */     return length("http://www.w3.org/2000/09/xmldsig#", "SPKIData");
/*      */   }
/*      */ 
/*      */   public int lengthX509Data()
/*      */   {
/*  396 */     if (this.x509Datas != null) {
/*  397 */       return this.x509Datas.size();
/*      */     }
/*  399 */     return length("http://www.w3.org/2000/09/xmldsig#", "X509Data");
/*      */   }
/*      */ 
/*      */   public int lengthUnknownElement()
/*      */   {
/*  409 */     int i = 0;
/*  410 */     NodeList localNodeList = this._constructionElement.getChildNodes();
/*      */ 
/*  412 */     for (int j = 0; j < localNodeList.getLength(); j++) {
/*  413 */       Node localNode = localNodeList.item(j);
/*      */ 
/*  419 */       if ((localNode.getNodeType() == 1) && (localNode.getNamespaceURI().equals("http://www.w3.org/2000/09/xmldsig#")))
/*      */       {
/*  422 */         i++;
/*      */       }
/*      */     }
/*      */ 
/*  426 */     return i;
/*      */   }
/*      */ 
/*      */   public KeyName itemKeyName(int paramInt)
/*      */     throws XMLSecurityException
/*      */   {
/*  438 */     Element localElement = XMLUtils.selectDsNode(this._constructionElement.getFirstChild(), "KeyName", paramInt);
/*      */ 
/*  441 */     if (localElement != null) {
/*  442 */       return new KeyName(localElement, this._baseURI);
/*      */     }
/*  444 */     return null;
/*      */   }
/*      */ 
/*      */   public KeyValue itemKeyValue(int paramInt)
/*      */     throws XMLSecurityException
/*      */   {
/*  456 */     Element localElement = XMLUtils.selectDsNode(this._constructionElement.getFirstChild(), "KeyValue", paramInt);
/*      */ 
/*  459 */     if (localElement != null) {
/*  460 */       return new KeyValue(localElement, this._baseURI);
/*      */     }
/*  462 */     return null;
/*      */   }
/*      */ 
/*      */   public MgmtData itemMgmtData(int paramInt)
/*      */     throws XMLSecurityException
/*      */   {
/*  474 */     Element localElement = XMLUtils.selectDsNode(this._constructionElement.getFirstChild(), "MgmtData", paramInt);
/*      */ 
/*  477 */     if (localElement != null) {
/*  478 */       return new MgmtData(localElement, this._baseURI);
/*      */     }
/*  480 */     return null;
/*      */   }
/*      */ 
/*      */   public PGPData itemPGPData(int paramInt)
/*      */     throws XMLSecurityException
/*      */   {
/*  492 */     Element localElement = XMLUtils.selectDsNode(this._constructionElement.getFirstChild(), "PGPData", paramInt);
/*      */ 
/*  495 */     if (localElement != null) {
/*  496 */       return new PGPData(localElement, this._baseURI);
/*      */     }
/*  498 */     return null;
/*      */   }
/*      */ 
/*      */   public RetrievalMethod itemRetrievalMethod(int paramInt)
/*      */     throws XMLSecurityException
/*      */   {
/*  511 */     Element localElement = XMLUtils.selectDsNode(this._constructionElement.getFirstChild(), "RetrievalMethod", paramInt);
/*      */ 
/*  514 */     if (localElement != null) {
/*  515 */       return new RetrievalMethod(localElement, this._baseURI);
/*      */     }
/*  517 */     return null;
/*      */   }
/*      */ 
/*      */   public SPKIData itemSPKIData(int paramInt)
/*      */     throws XMLSecurityException
/*      */   {
/*  529 */     Element localElement = XMLUtils.selectDsNode(this._constructionElement.getFirstChild(), "SPKIData", paramInt);
/*      */ 
/*  532 */     if (localElement != null) {
/*  533 */       return new SPKIData(localElement, this._baseURI);
/*      */     }
/*  535 */     return null;
/*      */   }
/*      */ 
/*      */   public X509Data itemX509Data(int paramInt)
/*      */     throws XMLSecurityException
/*      */   {
/*  546 */     if (this.x509Datas != null) {
/*  547 */       return (X509Data)this.x509Datas.get(paramInt);
/*      */     }
/*  549 */     Element localElement = XMLUtils.selectDsNode(this._constructionElement.getFirstChild(), "X509Data", paramInt);
/*      */ 
/*  552 */     if (localElement != null) {
/*  553 */       return new X509Data(localElement, this._baseURI);
/*      */     }
/*  555 */     return null;
/*      */   }
/*      */ 
/*      */   public EncryptedKey itemEncryptedKey(int paramInt)
/*      */     throws XMLSecurityException
/*      */   {
/*  567 */     if (this.encryptedKeys != null) {
/*  568 */       return (EncryptedKey)this.encryptedKeys.get(paramInt);
/*      */     }
/*  570 */     Element localElement = XMLUtils.selectXencNode(this._constructionElement.getFirstChild(), "EncryptedKey", paramInt);
/*      */ 
/*  574 */     if (localElement != null) {
/*  575 */       XMLCipher localXMLCipher = XMLCipher.getInstance();
/*  576 */       localXMLCipher.init(4, null);
/*  577 */       return localXMLCipher.loadEncryptedKey(localElement);
/*      */     }
/*  579 */     return null;
/*      */   }
/*      */ 
/*      */   public Element itemUnknownElement(int paramInt)
/*      */   {
/*  590 */     NodeList localNodeList = this._constructionElement.getChildNodes();
/*  591 */     int i = 0;
/*      */ 
/*  593 */     for (int j = 0; j < localNodeList.getLength(); j++) {
/*  594 */       Node localNode = localNodeList.item(j);
/*      */ 
/*  600 */       if ((localNode.getNodeType() == 1) && (localNode.getNamespaceURI().equals("http://www.w3.org/2000/09/xmldsig#")))
/*      */       {
/*  603 */         i++;
/*      */ 
/*  605 */         if (i == paramInt) {
/*  606 */           return (Element)localNode;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  611 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean isEmpty()
/*      */   {
/*  620 */     return this._constructionElement.getFirstChild() == null;
/*      */   }
/*      */ 
/*      */   public boolean containsKeyName()
/*      */   {
/*  629 */     return lengthKeyName() > 0;
/*      */   }
/*      */ 
/*      */   public boolean containsKeyValue()
/*      */   {
/*  638 */     return lengthKeyValue() > 0;
/*      */   }
/*      */ 
/*      */   public boolean containsMgmtData()
/*      */   {
/*  647 */     return lengthMgmtData() > 0;
/*      */   }
/*      */ 
/*      */   public boolean containsPGPData()
/*      */   {
/*  656 */     return lengthPGPData() > 0;
/*      */   }
/*      */ 
/*      */   public boolean containsRetrievalMethod()
/*      */   {
/*  665 */     return lengthRetrievalMethod() > 0;
/*      */   }
/*      */ 
/*      */   public boolean containsSPKIData()
/*      */   {
/*  674 */     return lengthSPKIData() > 0;
/*      */   }
/*      */ 
/*      */   public boolean containsUnknownElement()
/*      */   {
/*  683 */     return lengthUnknownElement() > 0;
/*      */   }
/*      */ 
/*      */   public boolean containsX509Data()
/*      */   {
/*  692 */     return lengthX509Data() > 0;
/*      */   }
/*      */ 
/*      */   public PublicKey getPublicKey()
/*      */     throws KeyResolverException
/*      */   {
/*  704 */     PublicKey localPublicKey = getPublicKeyFromInternalResolvers();
/*      */ 
/*  706 */     if (localPublicKey != null) {
/*  707 */       log.log(Level.FINE, "I could find a key using the per-KeyInfo key resolvers");
/*      */ 
/*  709 */       return localPublicKey;
/*      */     }
/*  711 */     log.log(Level.FINE, "I couldn't find a key using the per-KeyInfo key resolvers");
/*      */ 
/*  713 */     localPublicKey = getPublicKeyFromStaticResolvers();
/*      */ 
/*  715 */     if (localPublicKey != null) {
/*  716 */       log.log(Level.FINE, "I could find a key using the system-wide key resolvers");
/*      */ 
/*  718 */       return localPublicKey;
/*      */     }
/*  720 */     log.log(Level.FINE, "I couldn't find a key using the system-wide key resolvers");
/*      */ 
/*  722 */     return null;
/*      */   }
/*      */ 
/*      */   PublicKey getPublicKeyFromStaticResolvers()
/*      */     throws KeyResolverException
/*      */   {
/*  732 */     Iterator localIterator1 = KeyResolver.iterator();
/*  733 */     while (localIterator1.hasNext()) {
/*  734 */       KeyResolverSpi localKeyResolverSpi = (KeyResolverSpi)localIterator1.next();
/*  735 */       Node localNode = this._constructionElement.getFirstChild();
/*  736 */       String str = getBaseURI();
/*  737 */       while (localNode != null) {
/*  738 */         if (localNode.getNodeType() == 1) {
/*  739 */           for (StorageResolver localStorageResolver : this._storageResolvers) {
/*  740 */             PublicKey localPublicKey = localKeyResolverSpi.engineLookupAndResolvePublicKey((Element)localNode, str, localStorageResolver);
/*      */ 
/*  745 */             if (localPublicKey != null) {
/*  746 */               return localPublicKey;
/*      */             }
/*      */           }
/*      */         }
/*  750 */         localNode = localNode.getNextSibling();
/*      */       }
/*      */     }
/*  753 */     return null;
/*      */   }
/*      */ 
/*      */   PublicKey getPublicKeyFromInternalResolvers()
/*      */     throws KeyResolverException
/*      */   {
/*  763 */     int i = lengthInternalKeyResolver();
/*  764 */     int j = this._storageResolvers.size();
/*  765 */     for (int k = 0; k < i; k++) {
/*  766 */       KeyResolverSpi localKeyResolverSpi = itemInternalKeyResolver(k);
/*  767 */       if (log.isLoggable(Level.FINE)) {
/*  768 */         log.log(Level.FINE, "Try " + localKeyResolverSpi.getClass().getName());
/*      */       }
/*  770 */       Node localNode = this._constructionElement.getFirstChild();
/*  771 */       String str = getBaseURI();
/*  772 */       while (localNode != null) {
/*  773 */         if (localNode.getNodeType() == 1) {
/*  774 */           for (int m = 0; m < j; m++) {
/*  775 */             StorageResolver localStorageResolver = (StorageResolver)this._storageResolvers.get(m);
/*      */ 
/*  777 */             PublicKey localPublicKey = localKeyResolverSpi.engineLookupAndResolvePublicKey((Element)localNode, str, localStorageResolver);
/*      */ 
/*  780 */             if (localPublicKey != null) {
/*  781 */               return localPublicKey;
/*      */             }
/*      */           }
/*      */         }
/*  785 */         localNode = localNode.getNextSibling();
/*      */       }
/*      */     }
/*      */ 
/*  789 */     return null;
/*      */   }
/*      */ 
/*      */   public X509Certificate getX509Certificate()
/*      */     throws KeyResolverException
/*      */   {
/*  801 */     X509Certificate localX509Certificate = getX509CertificateFromInternalResolvers();
/*      */ 
/*  803 */     if (localX509Certificate != null) {
/*  804 */       log.log(Level.FINE, "I could find a X509Certificate using the per-KeyInfo key resolvers");
/*      */ 
/*  807 */       return localX509Certificate;
/*      */     }
/*  809 */     log.log(Level.FINE, "I couldn't find a X509Certificate using the per-KeyInfo key resolvers");
/*      */ 
/*  814 */     localX509Certificate = getX509CertificateFromStaticResolvers();
/*      */ 
/*  816 */     if (localX509Certificate != null) {
/*  817 */       log.log(Level.FINE, "I could find a X509Certificate using the system-wide key resolvers");
/*      */ 
/*  820 */       return localX509Certificate;
/*      */     }
/*  822 */     log.log(Level.FINE, "I couldn't find a X509Certificate using the system-wide key resolvers");
/*      */ 
/*  826 */     return null;
/*      */   }
/*      */ 
/*      */   X509Certificate getX509CertificateFromStaticResolvers()
/*      */     throws KeyResolverException
/*      */   {
/*  839 */     if (log.isLoggable(Level.FINE)) {
/*  840 */       log.log(Level.FINE, "Start getX509CertificateFromStaticResolvers() with " + KeyResolver.length() + " resolvers");
/*      */     }
/*      */ 
/*  845 */     String str = getBaseURI();
/*  846 */     Iterator localIterator = KeyResolver.iterator();
/*  847 */     while (localIterator.hasNext()) {
/*  848 */       KeyResolverSpi localKeyResolverSpi = (KeyResolverSpi)localIterator.next();
/*  849 */       X509Certificate localX509Certificate = applyCurrentResolver(str, localKeyResolverSpi);
/*  850 */       if (localX509Certificate != null) {
/*  851 */         return localX509Certificate;
/*      */       }
/*      */     }
/*  854 */     return null;
/*      */   }
/*      */ 
/*      */   private X509Certificate applyCurrentResolver(String paramString, KeyResolverSpi paramKeyResolverSpi)
/*      */     throws KeyResolverException
/*      */   {
/*  860 */     Node localNode = this._constructionElement.getFirstChild();
/*  861 */     while (localNode != null) {
/*  862 */       if (localNode.getNodeType() == 1) {
/*  863 */         for (StorageResolver localStorageResolver : this._storageResolvers) {
/*  864 */           X509Certificate localX509Certificate = paramKeyResolverSpi.engineLookupResolveX509Certificate((Element)localNode, paramString, localStorageResolver);
/*      */ 
/*  869 */           if (localX509Certificate != null) {
/*  870 */             return localX509Certificate;
/*      */           }
/*      */         }
/*      */       }
/*  874 */       localNode = localNode.getNextSibling();
/*      */     }
/*  876 */     return null;
/*      */   }
/*      */ 
/*      */   X509Certificate getX509CertificateFromInternalResolvers()
/*      */     throws KeyResolverException
/*      */   {
/*  887 */     if (log.isLoggable(Level.FINE)) {
/*  888 */       log.log(Level.FINE, "Start getX509CertificateFromInternalResolvers() with " + lengthInternalKeyResolver() + " resolvers");
/*      */     }
/*      */ 
/*  893 */     String str = getBaseURI();
/*  894 */     for (KeyResolverSpi localKeyResolverSpi : this._internalKeyResolvers) {
/*  895 */       if (log.isLoggable(Level.FINE)) {
/*  896 */         log.log(Level.FINE, "Try " + localKeyResolverSpi.getClass().getName());
/*      */       }
/*  898 */       X509Certificate localX509Certificate = applyCurrentResolver(str, localKeyResolverSpi);
/*  899 */       if (localX509Certificate != null) {
/*  900 */         return localX509Certificate;
/*      */       }
/*      */     }
/*      */ 
/*  904 */     return null;
/*      */   }
/*      */ 
/*      */   public SecretKey getSecretKey()
/*      */     throws KeyResolverException
/*      */   {
/*  913 */     SecretKey localSecretKey = getSecretKeyFromInternalResolvers();
/*      */ 
/*  915 */     if (localSecretKey != null) {
/*  916 */       log.log(Level.FINE, "I could find a secret key using the per-KeyInfo key resolvers");
/*      */ 
/*  918 */       return localSecretKey;
/*      */     }
/*  920 */     log.log(Level.FINE, "I couldn't find a secret key using the per-KeyInfo key resolvers");
/*      */ 
/*  923 */     localSecretKey = getSecretKeyFromStaticResolvers();
/*      */ 
/*  925 */     if (localSecretKey != null) {
/*  926 */       log.log(Level.FINE, "I could find a secret key using the system-wide key resolvers");
/*      */ 
/*  928 */       return localSecretKey;
/*      */     }
/*  930 */     log.log(Level.FINE, "I couldn't find a secret key using the system-wide key resolvers");
/*      */ 
/*  933 */     return null;
/*      */   }
/*      */ 
/*      */   SecretKey getSecretKeyFromStaticResolvers()
/*      */     throws KeyResolverException
/*      */   {
/*  944 */     int i = KeyResolver.length();
/*  945 */     int j = this._storageResolvers.size();
/*  946 */     Iterator localIterator = KeyResolver.iterator();
/*  947 */     for (int k = 0; k < i; k++) {
/*  948 */       KeyResolverSpi localKeyResolverSpi = (KeyResolverSpi)localIterator.next();
/*      */ 
/*  950 */       Node localNode = this._constructionElement.getFirstChild();
/*  951 */       String str = getBaseURI();
/*  952 */       while (localNode != null) {
/*  953 */         if (localNode.getNodeType() == 1) {
/*  954 */           for (int m = 0; m < j; m++) {
/*  955 */             StorageResolver localStorageResolver = (StorageResolver)this._storageResolvers.get(m);
/*      */ 
/*  958 */             SecretKey localSecretKey = localKeyResolverSpi.engineLookupAndResolveSecretKey((Element)localNode, str, localStorageResolver);
/*      */ 
/*  963 */             if (localSecretKey != null) {
/*  964 */               return localSecretKey;
/*      */             }
/*      */           }
/*      */         }
/*  968 */         localNode = localNode.getNextSibling();
/*      */       }
/*      */     }
/*  971 */     return null;
/*      */   }
/*      */ 
/*      */   SecretKey getSecretKeyFromInternalResolvers()
/*      */     throws KeyResolverException
/*      */   {
/*  982 */     int i = this._storageResolvers.size();
/*  983 */     for (int j = 0; j < lengthInternalKeyResolver(); j++) {
/*  984 */       KeyResolverSpi localKeyResolverSpi = itemInternalKeyResolver(j);
/*  985 */       if (log.isLoggable(Level.FINE)) {
/*  986 */         log.log(Level.FINE, "Try " + localKeyResolverSpi.getClass().getName());
/*      */       }
/*  988 */       Node localNode = this._constructionElement.getFirstChild();
/*  989 */       String str = getBaseURI();
/*  990 */       while (localNode != null) {
/*  991 */         if (localNode.getNodeType() == 1) {
/*  992 */           for (int k = 0; k < i; k++) {
/*  993 */             StorageResolver localStorageResolver = (StorageResolver)this._storageResolvers.get(k);
/*      */ 
/*  996 */             SecretKey localSecretKey = localKeyResolverSpi.engineLookupAndResolveSecretKey((Element)localNode, str, localStorageResolver);
/*      */ 
/*  999 */             if (localSecretKey != null) {
/* 1000 */               return localSecretKey;
/*      */             }
/*      */           }
/*      */         }
/* 1004 */         localNode = localNode.getNextSibling();
/*      */       }
/*      */     }
/*      */ 
/* 1008 */     return null;
/*      */   }
/*      */ 
/*      */   public void registerInternalKeyResolver(KeyResolverSpi paramKeyResolverSpi)
/*      */   {
/* 1023 */     if (this._internalKeyResolvers == null) {
/* 1024 */       this._internalKeyResolvers = new ArrayList();
/*      */     }
/* 1026 */     this._internalKeyResolvers.add(paramKeyResolverSpi);
/*      */   }
/*      */ 
/*      */   int lengthInternalKeyResolver()
/*      */   {
/* 1034 */     if (this._internalKeyResolvers == null)
/* 1035 */       return 0;
/* 1036 */     return this._internalKeyResolvers.size();
/*      */   }
/*      */ 
/*      */   KeyResolverSpi itemInternalKeyResolver(int paramInt)
/*      */   {
/* 1046 */     return (KeyResolverSpi)this._internalKeyResolvers.get(paramInt);
/*      */   }
/*      */ 
/*      */   public void addStorageResolver(StorageResolver paramStorageResolver)
/*      */   {
/* 1058 */     if (this._storageResolvers == nullList) {
/* 1059 */       this._storageResolvers = new ArrayList();
/*      */     }
/* 1061 */     this._storageResolvers.add(paramStorageResolver);
/*      */   }
/*      */ 
/*      */   public static void init()
/*      */   {
/* 1070 */     if (!_alreadyInitialized) {
/* 1071 */       if (log == null)
/*      */       {
/* 1077 */         log = Logger.getLogger(KeyInfo.class.getName());
/*      */ 
/* 1080 */         log.log(Level.SEVERE, "Had to assign log in the init() function");
/*      */       }
/*      */ 
/* 1084 */       _alreadyInitialized = true;
/*      */     }
/*      */   }
/*      */ 
/*      */   public String getBaseLocalName()
/*      */   {
/* 1090 */     return "KeyInfo";
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  106 */     ArrayList localArrayList = new ArrayList(1);
/*  107 */     localArrayList.add(null);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.keys.KeyInfo
 * JD-Core Version:    0.6.2
 */