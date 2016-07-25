/*     */ package com.sun.org.apache.xml.internal.security.c14n;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Set;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public abstract class CanonicalizerSpi
/*     */ {
/* 200 */   protected boolean reset = false;
/*     */ 
/*     */   public byte[] engineCanonicalize(byte[] paramArrayOfByte)
/*     */     throws ParserConfigurationException, IOException, SAXException, CanonicalizationException
/*     */   {
/*  68 */     ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(paramArrayOfByte);
/*  69 */     InputSource localInputSource = new InputSource(localByteArrayInputStream);
/*  70 */     DocumentBuilderFactory localDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
/*  71 */     localDocumentBuilderFactory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", Boolean.TRUE.booleanValue());
/*     */ 
/*  74 */     localDocumentBuilderFactory.setNamespaceAware(true);
/*     */ 
/*  76 */     DocumentBuilder localDocumentBuilder = localDocumentBuilderFactory.newDocumentBuilder();
/*     */ 
/* 103 */     Document localDocument = localDocumentBuilder.parse(localInputSource);
/* 104 */     byte[] arrayOfByte = engineCanonicalizeSubTree(localDocument);
/* 105 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public byte[] engineCanonicalizeXPathNodeSet(NodeList paramNodeList)
/*     */     throws CanonicalizationException
/*     */   {
/* 118 */     return engineCanonicalizeXPathNodeSet(XMLUtils.convertNodelistToSet(paramNodeList));
/*     */   }
/*     */ 
/*     */   public byte[] engineCanonicalizeXPathNodeSet(NodeList paramNodeList, String paramString)
/*     */     throws CanonicalizationException
/*     */   {
/* 134 */     return engineCanonicalizeXPathNodeSet(XMLUtils.convertNodelistToSet(paramNodeList), paramString);
/*     */   }
/*     */ 
/*     */   public abstract String engineGetURI();
/*     */ 
/*     */   public abstract boolean engineGetIncludeComments();
/*     */ 
/*     */   public abstract byte[] engineCanonicalizeXPathNodeSet(Set<Node> paramSet)
/*     */     throws CanonicalizationException;
/*     */ 
/*     */   public abstract byte[] engineCanonicalizeXPathNodeSet(Set<Node> paramSet, String paramString)
/*     */     throws CanonicalizationException;
/*     */ 
/*     */   public abstract byte[] engineCanonicalizeSubTree(Node paramNode)
/*     */     throws CanonicalizationException;
/*     */ 
/*     */   public abstract byte[] engineCanonicalizeSubTree(Node paramNode, String paramString)
/*     */     throws CanonicalizationException;
/*     */ 
/*     */   public abstract void setWriter(OutputStream paramOutputStream);
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.c14n.CanonicalizerSpi
 * JD-Core Version:    0.6.2
 */