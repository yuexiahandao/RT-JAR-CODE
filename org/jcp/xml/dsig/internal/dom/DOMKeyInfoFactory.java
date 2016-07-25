/*     */ package org.jcp.xml.dsig.internal.dom;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.security.KeyException;
/*     */ import java.security.PublicKey;
/*     */ import java.util.List;
/*     */ import javax.xml.crypto.MarshalException;
/*     */ import javax.xml.crypto.URIDereferencer;
/*     */ import javax.xml.crypto.XMLStructure;
/*     */ import javax.xml.crypto.dom.DOMStructure;
/*     */ import javax.xml.crypto.dsig.keyinfo.KeyInfo;
/*     */ import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
/*     */ import javax.xml.crypto.dsig.keyinfo.KeyName;
/*     */ import javax.xml.crypto.dsig.keyinfo.KeyValue;
/*     */ import javax.xml.crypto.dsig.keyinfo.PGPData;
/*     */ import javax.xml.crypto.dsig.keyinfo.RetrievalMethod;
/*     */ import javax.xml.crypto.dsig.keyinfo.X509Data;
/*     */ import javax.xml.crypto.dsig.keyinfo.X509IssuerSerial;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public final class DOMKeyInfoFactory extends KeyInfoFactory
/*     */ {
/*     */   public KeyInfo newKeyInfo(List paramList)
/*     */   {
/*  51 */     return newKeyInfo(paramList, null);
/*     */   }
/*     */ 
/*     */   public KeyInfo newKeyInfo(List paramList, String paramString) {
/*  55 */     return new DOMKeyInfo(paramList, paramString);
/*     */   }
/*     */ 
/*     */   public KeyName newKeyName(String paramString) {
/*  59 */     return new DOMKeyName(paramString);
/*     */   }
/*     */ 
/*     */   public KeyValue newKeyValue(PublicKey paramPublicKey) throws KeyException {
/*  63 */     return new DOMKeyValue(paramPublicKey);
/*     */   }
/*     */ 
/*     */   public PGPData newPGPData(byte[] paramArrayOfByte) {
/*  67 */     return newPGPData(paramArrayOfByte, null, null);
/*     */   }
/*     */ 
/*     */   public PGPData newPGPData(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, List paramList) {
/*  71 */     return new DOMPGPData(paramArrayOfByte1, paramArrayOfByte2, paramList);
/*     */   }
/*     */ 
/*     */   public PGPData newPGPData(byte[] paramArrayOfByte, List paramList) {
/*  75 */     return new DOMPGPData(paramArrayOfByte, paramList);
/*     */   }
/*     */ 
/*     */   public RetrievalMethod newRetrievalMethod(String paramString) {
/*  79 */     return newRetrievalMethod(paramString, null, null);
/*     */   }
/*     */ 
/*     */   public RetrievalMethod newRetrievalMethod(String paramString1, String paramString2, List paramList)
/*     */   {
/*  84 */     if (paramString1 == null) {
/*  85 */       throw new NullPointerException("uri must not be null");
/*     */     }
/*  87 */     return new DOMRetrievalMethod(paramString1, paramString2, paramList);
/*     */   }
/*     */ 
/*     */   public X509Data newX509Data(List paramList) {
/*  91 */     return new DOMX509Data(paramList);
/*     */   }
/*     */ 
/*     */   public X509IssuerSerial newX509IssuerSerial(String paramString, BigInteger paramBigInteger)
/*     */   {
/*  96 */     return new DOMX509IssuerSerial(paramString, paramBigInteger);
/*     */   }
/*     */ 
/*     */   public boolean isFeatureSupported(String paramString) {
/* 100 */     if (paramString == null) {
/* 101 */       throw new NullPointerException();
/*     */     }
/* 103 */     return false;
/*     */   }
/*     */ 
/*     */   public URIDereferencer getURIDereferencer()
/*     */   {
/* 108 */     return DOMURIDereferencer.INSTANCE;
/*     */   }
/*     */ 
/*     */   public KeyInfo unmarshalKeyInfo(XMLStructure paramXMLStructure) throws MarshalException
/*     */   {
/* 113 */     if (paramXMLStructure == null) {
/* 114 */       throw new NullPointerException("xmlStructure cannot be null");
/*     */     }
/* 116 */     Node localNode = ((DOMStructure)paramXMLStructure).getNode();
/*     */ 
/* 118 */     localNode.normalize();
/*     */ 
/* 120 */     Element localElement = null;
/* 121 */     if (localNode.getNodeType() == 9)
/* 122 */       localElement = ((Document)localNode).getDocumentElement();
/* 123 */     else if (localNode.getNodeType() == 1)
/* 124 */       localElement = (Element)localNode;
/*     */     else {
/* 126 */       throw new MarshalException("xmlStructure does not contain a proper Node");
/*     */     }
/*     */ 
/* 131 */     String str = localElement.getLocalName();
/* 132 */     if (str == null) {
/* 133 */       throw new MarshalException("Document implementation must support DOM Level 2 and be namespace aware");
/*     */     }
/*     */ 
/* 136 */     if (str.equals("KeyInfo")) {
/* 137 */       return new DOMKeyInfo(localElement, null, getProvider());
/*     */     }
/* 139 */     throw new MarshalException("invalid KeyInfo tag: " + str);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.jcp.xml.dsig.internal.dom.DOMKeyInfoFactory
 * JD-Core Version:    0.6.2
 */