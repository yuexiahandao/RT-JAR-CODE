/*     */ package com.sun.org.apache.xml.internal.security.c14n;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.c14n.implementations.Canonicalizer11_OmitComments;
/*     */ import com.sun.org.apache.xml.internal.security.c14n.implementations.Canonicalizer11_WithComments;
/*     */ import com.sun.org.apache.xml.internal.security.c14n.implementations.Canonicalizer20010315ExclOmitComments;
/*     */ import com.sun.org.apache.xml.internal.security.c14n.implementations.Canonicalizer20010315ExclWithComments;
/*     */ import com.sun.org.apache.xml.internal.security.c14n.implementations.Canonicalizer20010315OmitComments;
/*     */ import com.sun.org.apache.xml.internal.security.c14n.implementations.Canonicalizer20010315WithComments;
/*     */ import com.sun.org.apache.xml.internal.security.exceptions.AlgorithmAlreadyRegisteredException;
/*     */ import com.sun.org.apache.xml.internal.security.utils.IgnoreAllErrorHandler;
/*     */ import com.sun.org.apache.xml.internal.security.utils.JavaUtils;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class Canonicalizer
/*     */ {
/*     */   public static final String ENCODING = "UTF8";
/*     */   public static final String XPATH_C14N_WITH_COMMENTS_SINGLE_NODE = "(.//. | .//@* | .//namespace::*)";
/*     */   public static final String ALGO_ID_C14N_OMIT_COMMENTS = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315";
/*     */   public static final String ALGO_ID_C14N_WITH_COMMENTS = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments";
/*     */   public static final String ALGO_ID_C14N_EXCL_OMIT_COMMENTS = "http://www.w3.org/2001/10/xml-exc-c14n#";
/*     */   public static final String ALGO_ID_C14N_EXCL_WITH_COMMENTS = "http://www.w3.org/2001/10/xml-exc-c14n#WithComments";
/*     */   public static final String ALGO_ID_C14N11_OMIT_COMMENTS = "http://www.w3.org/2006/12/xml-c14n11";
/*     */   public static final String ALGO_ID_C14N11_WITH_COMMENTS = "http://www.w3.org/2006/12/xml-c14n11#WithComments";
/*  96 */   private static Map<String, Class<? extends CanonicalizerSpi>> canonicalizerHash = new ConcurrentHashMap();
/*     */   private final CanonicalizerSpi canonicalizerSpi;
/*     */ 
/*     */   private Canonicalizer(String paramString)
/*     */     throws InvalidCanonicalizerException
/*     */   {
/*     */     try
/*     */     {
/* 109 */       Class localClass = (Class)canonicalizerHash.get(paramString);
/*     */ 
/* 112 */       this.canonicalizerSpi = ((CanonicalizerSpi)localClass.newInstance());
/* 113 */       this.canonicalizerSpi.reset = true;
/*     */     } catch (Exception localException) {
/* 115 */       Object[] arrayOfObject = { paramString };
/* 116 */       throw new InvalidCanonicalizerException("signature.Canonicalizer.UnknownCanonicalizer", arrayOfObject, localException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final Canonicalizer getInstance(String paramString)
/*     */     throws InvalidCanonicalizerException
/*     */   {
/* 131 */     return new Canonicalizer(paramString);
/*     */   }
/*     */ 
/*     */   public static void register(String paramString1, String paramString2)
/*     */     throws AlgorithmAlreadyRegisteredException, ClassNotFoundException
/*     */   {
/* 146 */     JavaUtils.checkRegisterPermission();
/*     */ 
/* 148 */     Class localClass = (Class)canonicalizerHash.get(paramString1);
/*     */ 
/* 151 */     if (localClass != null) {
/* 152 */       Object[] arrayOfObject = { paramString1, localClass };
/* 153 */       throw new AlgorithmAlreadyRegisteredException("algorithm.alreadyRegistered", arrayOfObject);
/*     */     }
/*     */ 
/* 156 */     canonicalizerHash.put(paramString1, Class.forName(paramString2));
/*     */   }
/*     */ 
/*     */   public static void register(String paramString, Class<? extends CanonicalizerSpi> paramClass)
/*     */     throws AlgorithmAlreadyRegisteredException, ClassNotFoundException
/*     */   {
/* 172 */     JavaUtils.checkRegisterPermission();
/*     */ 
/* 174 */     Class localClass = (Class)canonicalizerHash.get(paramString);
/*     */ 
/* 176 */     if (localClass != null) {
/* 177 */       Object[] arrayOfObject = { paramString, localClass };
/* 178 */       throw new AlgorithmAlreadyRegisteredException("algorithm.alreadyRegistered", arrayOfObject);
/*     */     }
/*     */ 
/* 181 */     canonicalizerHash.put(paramString, paramClass);
/*     */   }
/*     */ 
/*     */   public static void registerDefaultAlgorithms()
/*     */   {
/* 188 */     canonicalizerHash.put("http://www.w3.org/TR/2001/REC-xml-c14n-20010315", Canonicalizer20010315OmitComments.class);
/*     */ 
/* 192 */     canonicalizerHash.put("http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments", Canonicalizer20010315WithComments.class);
/*     */ 
/* 196 */     canonicalizerHash.put("http://www.w3.org/2001/10/xml-exc-c14n#", Canonicalizer20010315ExclOmitComments.class);
/*     */ 
/* 200 */     canonicalizerHash.put("http://www.w3.org/2001/10/xml-exc-c14n#WithComments", Canonicalizer20010315ExclWithComments.class);
/*     */ 
/* 204 */     canonicalizerHash.put("http://www.w3.org/2006/12/xml-c14n11", Canonicalizer11_OmitComments.class);
/*     */ 
/* 208 */     canonicalizerHash.put("http://www.w3.org/2006/12/xml-c14n11#WithComments", Canonicalizer11_WithComments.class);
/*     */   }
/*     */ 
/*     */   public final String getURI()
/*     */   {
/* 220 */     return this.canonicalizerSpi.engineGetURI();
/*     */   }
/*     */ 
/*     */   public boolean getIncludeComments()
/*     */   {
/* 229 */     return this.canonicalizerSpi.engineGetIncludeComments();
/*     */   }
/*     */ 
/*     */   public byte[] canonicalize(byte[] paramArrayOfByte)
/*     */     throws ParserConfigurationException, IOException, SAXException, CanonicalizationException
/*     */   {
/* 247 */     ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(paramArrayOfByte);
/* 248 */     InputSource localInputSource = new InputSource(localByteArrayInputStream);
/* 249 */     DocumentBuilderFactory localDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
/* 250 */     localDocumentBuilderFactory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", Boolean.TRUE.booleanValue());
/*     */ 
/* 252 */     localDocumentBuilderFactory.setNamespaceAware(true);
/*     */ 
/* 255 */     localDocumentBuilderFactory.setValidating(true);
/*     */ 
/* 257 */     DocumentBuilder localDocumentBuilder = localDocumentBuilderFactory.newDocumentBuilder();
/*     */ 
/* 280 */     localDocumentBuilder.setErrorHandler(new IgnoreAllErrorHandler());
/*     */ 
/* 282 */     Document localDocument = localDocumentBuilder.parse(localInputSource);
/* 283 */     return canonicalizeSubtree(localDocument);
/*     */   }
/*     */ 
/*     */   public byte[] canonicalizeSubtree(Node paramNode)
/*     */     throws CanonicalizationException
/*     */   {
/* 295 */     return this.canonicalizerSpi.engineCanonicalizeSubTree(paramNode);
/*     */   }
/*     */ 
/*     */   public byte[] canonicalizeSubtree(Node paramNode, String paramString)
/*     */     throws CanonicalizationException
/*     */   {
/* 308 */     return this.canonicalizerSpi.engineCanonicalizeSubTree(paramNode, paramString);
/*     */   }
/*     */ 
/*     */   public byte[] canonicalizeXPathNodeSet(NodeList paramNodeList)
/*     */     throws CanonicalizationException
/*     */   {
/* 321 */     return this.canonicalizerSpi.engineCanonicalizeXPathNodeSet(paramNodeList);
/*     */   }
/*     */ 
/*     */   public byte[] canonicalizeXPathNodeSet(NodeList paramNodeList, String paramString)
/*     */     throws CanonicalizationException
/*     */   {
/* 336 */     return this.canonicalizerSpi.engineCanonicalizeXPathNodeSet(paramNodeList, paramString);
/*     */   }
/*     */ 
/*     */   public byte[] canonicalizeXPathNodeSet(Set<Node> paramSet)
/*     */     throws CanonicalizationException
/*     */   {
/* 349 */     return this.canonicalizerSpi.engineCanonicalizeXPathNodeSet(paramSet);
/*     */   }
/*     */ 
/*     */   public byte[] canonicalizeXPathNodeSet(Set<Node> paramSet, String paramString)
/*     */     throws CanonicalizationException
/*     */   {
/* 363 */     return this.canonicalizerSpi.engineCanonicalizeXPathNodeSet(paramSet, paramString);
/*     */   }
/*     */ 
/*     */   public void setWriter(OutputStream paramOutputStream)
/*     */   {
/* 373 */     this.canonicalizerSpi.setWriter(paramOutputStream);
/*     */   }
/*     */ 
/*     */   public String getImplementingCanonicalizerClass()
/*     */   {
/* 382 */     return this.canonicalizerSpi.getClass().getName();
/*     */   }
/*     */ 
/*     */   public void notReset()
/*     */   {
/* 389 */     this.canonicalizerSpi.reset = false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.c14n.Canonicalizer
 * JD-Core Version:    0.6.2
 */