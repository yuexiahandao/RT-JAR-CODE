/*     */ package com.sun.org.apache.xml.internal.security.signature;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.c14n.CanonicalizationException;
/*     */ import com.sun.org.apache.xml.internal.security.c14n.implementations.Canonicalizer11_OmitComments;
/*     */ import com.sun.org.apache.xml.internal.security.c14n.implementations.Canonicalizer20010315OmitComments;
/*     */ import com.sun.org.apache.xml.internal.security.c14n.implementations.CanonicalizerBase;
/*     */ import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityRuntimeException;
/*     */ import com.sun.org.apache.xml.internal.security.utils.IgnoreAllErrorHandler;
/*     */ import com.sun.org.apache.xml.internal.security.utils.JavaUtils;
/*     */ import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class XMLSignatureInput
/*     */   implements Cloneable
/*     */ {
/*  57 */   static Logger log = Logger.getLogger(XMLSignatureInput.class.getName());
/*     */ 
/*  74 */   InputStream _inputOctetStreamProxy = null;
/*     */ 
/*  78 */   Set _inputNodeSet = null;
/*     */ 
/*  82 */   Node _subNode = null;
/*     */ 
/*  86 */   Node excludeNode = null;
/*     */ 
/*  90 */   boolean excludeComments = false;
/*     */ 
/*  92 */   boolean isNodeSet = false;
/*     */ 
/*  96 */   byte[] bytes = null;
/*     */ 
/* 101 */   private String _MIMEType = null;
/*     */ 
/* 106 */   private String _SourceURI = null;
/*     */ 
/* 111 */   List nodeFilters = new ArrayList();
/*     */ 
/* 113 */   boolean needsToBeExpanded = false;
/* 114 */   OutputStream outputStream = null;
/*     */ 
/*     */   public boolean isNeedsToBeExpanded()
/*     */   {
/* 121 */     return this.needsToBeExpanded;
/*     */   }
/*     */ 
/*     */   public void setNeedsToBeExpanded(boolean paramBoolean)
/*     */   {
/* 129 */     this.needsToBeExpanded = paramBoolean;
/*     */   }
/*     */ 
/*     */   public XMLSignatureInput(byte[] paramArrayOfByte)
/*     */   {
/* 145 */     this.bytes = paramArrayOfByte;
/*     */   }
/*     */ 
/*     */   public XMLSignatureInput(InputStream paramInputStream)
/*     */   {
/* 155 */     this._inputOctetStreamProxy = paramInputStream;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public XMLSignatureInput(String paramString)
/*     */   {
/* 169 */     this(paramString.getBytes());
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public XMLSignatureInput(String paramString1, String paramString2)
/*     */     throws UnsupportedEncodingException
/*     */   {
/* 185 */     this(paramString1.getBytes(paramString2));
/*     */   }
/*     */ 
/*     */   public XMLSignatureInput(Node paramNode)
/*     */   {
/* 196 */     this._subNode = paramNode;
/*     */   }
/*     */ 
/*     */   public XMLSignatureInput(Set paramSet)
/*     */   {
/* 206 */     this._inputNodeSet = paramSet;
/*     */   }
/*     */ 
/*     */   public Set getNodeSet()
/*     */     throws CanonicalizationException, ParserConfigurationException, IOException, SAXException
/*     */   {
/* 221 */     return getNodeSet(false);
/*     */   }
/*     */ 
/*     */   public Set getNodeSet(boolean paramBoolean)
/*     */     throws ParserConfigurationException, IOException, SAXException, CanonicalizationException
/*     */   {
/* 238 */     if (this._inputNodeSet != null) {
/* 239 */       return this._inputNodeSet;
/*     */     }
/* 241 */     if ((this._inputOctetStreamProxy == null) && (this._subNode != null))
/*     */     {
/* 243 */       if (paramBoolean) {
/* 244 */         XMLUtils.circumventBug2650(XMLUtils.getOwnerDocument(this._subNode));
/*     */       }
/* 246 */       this._inputNodeSet = new LinkedHashSet();
/* 247 */       XMLUtils.getSet(this._subNode, this._inputNodeSet, this.excludeNode, this.excludeComments);
/*     */ 
/* 249 */       return this._inputNodeSet;
/* 250 */     }if (isOctetStream()) {
/* 251 */       convertToNodes();
/* 252 */       LinkedHashSet localLinkedHashSet = new LinkedHashSet();
/* 253 */       XMLUtils.getSet(this._subNode, localLinkedHashSet, null, false);
/*     */ 
/* 255 */       return localLinkedHashSet;
/*     */     }
/*     */ 
/* 258 */     throw new RuntimeException("getNodeSet() called but no input data present");
/*     */   }
/*     */ 
/*     */   public InputStream getOctetStream()
/*     */     throws IOException
/*     */   {
/* 272 */     return getResetableInputStream();
/*     */   }
/*     */ 
/*     */   public InputStream getOctetStreamReal()
/*     */   {
/* 279 */     return this._inputOctetStreamProxy;
/*     */   }
/*     */ 
/*     */   public byte[] getBytes()
/*     */     throws IOException, CanonicalizationException
/*     */   {
/* 293 */     if (this.bytes != null) {
/* 294 */       return this.bytes;
/*     */     }
/* 296 */     InputStream localInputStream = getResetableInputStream();
/* 297 */     if (localInputStream != null)
/*     */     {
/* 299 */       if (this.bytes == null) {
/* 300 */         localInputStream.reset();
/* 301 */         this.bytes = JavaUtils.getBytesFromStream(localInputStream);
/*     */       }
/* 303 */       return this.bytes;
/*     */     }
/* 305 */     Canonicalizer20010315OmitComments localCanonicalizer20010315OmitComments = new Canonicalizer20010315OmitComments();
/*     */ 
/* 307 */     this.bytes = localCanonicalizer20010315OmitComments.engineCanonicalize(this);
/* 308 */     return this.bytes;
/*     */   }
/*     */ 
/*     */   public boolean isNodeSet()
/*     */   {
/* 317 */     return ((this._inputOctetStreamProxy == null) && (this._inputNodeSet != null)) || (this.isNodeSet);
/*     */   }
/*     */ 
/*     */   public boolean isElement()
/*     */   {
/* 327 */     return (this._inputOctetStreamProxy == null) && (this._subNode != null) && (this._inputNodeSet == null) && (!this.isNodeSet);
/*     */   }
/*     */ 
/*     */   public boolean isOctetStream()
/*     */   {
/* 337 */     return ((this._inputOctetStreamProxy != null) || (this.bytes != null)) && (this._inputNodeSet == null) && (this._subNode == null);
/*     */   }
/*     */ 
/*     */   public boolean isOutputStreamSet()
/*     */   {
/* 349 */     return this.outputStream != null;
/*     */   }
/*     */ 
/*     */   public boolean isByteArray()
/*     */   {
/* 358 */     return (this.bytes != null) && (this._inputNodeSet == null) && (this._subNode == null);
/*     */   }
/*     */ 
/*     */   public boolean isInitialized()
/*     */   {
/* 368 */     return (isOctetStream()) || (isNodeSet());
/*     */   }
/*     */ 
/*     */   public String getMIMEType()
/*     */   {
/* 377 */     return this._MIMEType;
/*     */   }
/*     */ 
/*     */   public void setMIMEType(String paramString)
/*     */   {
/* 386 */     this._MIMEType = paramString;
/*     */   }
/*     */ 
/*     */   public String getSourceURI()
/*     */   {
/* 395 */     return this._SourceURI;
/*     */   }
/*     */ 
/*     */   public void setSourceURI(String paramString)
/*     */   {
/* 404 */     this._SourceURI = paramString;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 412 */     if (isNodeSet()) {
/* 413 */       return "XMLSignatureInput/NodeSet/" + this._inputNodeSet.size() + " nodes/" + getSourceURI();
/*     */     }
/*     */ 
/* 416 */     if (isElement()) {
/* 417 */       return "XMLSignatureInput/Element/" + this._subNode + " exclude " + this.excludeNode + " comments:" + this.excludeComments + "/" + getSourceURI();
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 422 */       return "XMLSignatureInput/OctetStream/" + getBytes().length + " octets/" + getSourceURI();
/*     */     }
/*     */     catch (IOException localIOException) {
/* 425 */       return "XMLSignatureInput/OctetStream//" + getSourceURI(); } catch (CanonicalizationException localCanonicalizationException) {
/*     */     }
/* 427 */     return "XMLSignatureInput/OctetStream//" + getSourceURI();
/*     */   }
/*     */ 
/*     */   public String getHTMLRepresentation()
/*     */     throws XMLSignatureException
/*     */   {
/* 439 */     XMLSignatureInputDebugger localXMLSignatureInputDebugger = new XMLSignatureInputDebugger(this);
/*     */ 
/* 441 */     return localXMLSignatureInputDebugger.getHTMLRepresentation();
/*     */   }
/*     */ 
/*     */   public String getHTMLRepresentation(Set paramSet)
/*     */     throws XMLSignatureException
/*     */   {
/* 454 */     XMLSignatureInputDebugger localXMLSignatureInputDebugger = new XMLSignatureInputDebugger(this, paramSet);
/*     */ 
/* 457 */     return localXMLSignatureInputDebugger.getHTMLRepresentation();
/*     */   }
/*     */ 
/*     */   public Node getExcludeNode()
/*     */   {
/* 465 */     return this.excludeNode;
/*     */   }
/*     */ 
/*     */   public void setExcludeNode(Node paramNode)
/*     */   {
/* 473 */     this.excludeNode = paramNode;
/*     */   }
/*     */ 
/*     */   public Node getSubNode()
/*     */   {
/* 481 */     return this._subNode;
/*     */   }
/*     */ 
/*     */   public boolean isExcludeComments()
/*     */   {
/* 488 */     return this.excludeComments;
/*     */   }
/*     */ 
/*     */   public void setExcludeComments(boolean paramBoolean)
/*     */   {
/* 495 */     this.excludeComments = paramBoolean;
/*     */   }
/*     */ 
/*     */   public void updateOutputStream(OutputStream paramOutputStream)
/*     */     throws CanonicalizationException, IOException
/*     */   {
/* 505 */     updateOutputStream(paramOutputStream, false);
/*     */   }
/*     */ 
/*     */   public void updateOutputStream(OutputStream paramOutputStream, boolean paramBoolean) throws CanonicalizationException, IOException
/*     */   {
/* 510 */     if (paramOutputStream == this.outputStream) {
/* 511 */       return;
/*     */     }
/* 513 */     if (this.bytes != null) {
/* 514 */       paramOutputStream.write(this.bytes);
/* 515 */       return;
/* 516 */     }if (this._inputOctetStreamProxy == null) {
/* 517 */       localObject = null;
/* 518 */       if (paramBoolean)
/* 519 */         localObject = new Canonicalizer11_OmitComments();
/*     */       else {
/* 521 */         localObject = new Canonicalizer20010315OmitComments();
/*     */       }
/* 523 */       ((CanonicalizerBase)localObject).setWriter(paramOutputStream);
/* 524 */       ((CanonicalizerBase)localObject).engineCanonicalize(this);
/* 525 */       return;
/*     */     }
/* 527 */     Object localObject = getResetableInputStream();
/* 528 */     if (this.bytes != null)
/*     */     {
/* 530 */       paramOutputStream.write(this.bytes, 0, this.bytes.length);
/* 531 */       return;
/*     */     }
/* 533 */     ((InputStream)localObject).reset();
/*     */ 
/* 535 */     byte[] arrayOfByte = new byte[1024];
/*     */     int i;
/* 536 */     while ((i = ((InputStream)localObject).read(arrayOfByte)) > 0)
/* 537 */       paramOutputStream.write(arrayOfByte, 0, i);
/*     */   }
/*     */ 
/*     */   public void setOutputStream(OutputStream paramOutputStream)
/*     */   {
/* 546 */     this.outputStream = paramOutputStream;
/*     */   }
/*     */ 
/*     */   protected InputStream getResetableInputStream() throws IOException {
/* 550 */     if ((this._inputOctetStreamProxy instanceof ByteArrayInputStream)) {
/* 551 */       if (!this._inputOctetStreamProxy.markSupported()) {
/* 552 */         throw new RuntimeException("Accepted as Markable but not truly been" + this._inputOctetStreamProxy);
/*     */       }
/* 554 */       return this._inputOctetStreamProxy;
/*     */     }
/* 556 */     if (this.bytes != null) {
/* 557 */       this._inputOctetStreamProxy = new ByteArrayInputStream(this.bytes);
/* 558 */       return this._inputOctetStreamProxy;
/*     */     }
/* 560 */     if (this._inputOctetStreamProxy == null)
/* 561 */       return null;
/* 562 */     if (this._inputOctetStreamProxy.markSupported()) {
/* 563 */       log.log(Level.INFO, "Mark Suported but not used as reset");
/*     */     }
/* 565 */     this.bytes = JavaUtils.getBytesFromStream(this._inputOctetStreamProxy);
/* 566 */     this._inputOctetStreamProxy.close();
/* 567 */     this._inputOctetStreamProxy = new ByteArrayInputStream(this.bytes);
/* 568 */     return this._inputOctetStreamProxy;
/*     */   }
/*     */ 
/*     */   public void addNodeFilter(NodeFilter paramNodeFilter)
/*     */   {
/* 575 */     if (isOctetStream()) {
/*     */       try {
/* 577 */         convertToNodes();
/*     */       } catch (Exception localException) {
/* 579 */         throw new XMLSecurityRuntimeException("signature.XMLSignatureInput.nodesetReference", localException);
/*     */       }
/*     */     }
/* 582 */     this.nodeFilters.add(paramNodeFilter);
/*     */   }
/*     */ 
/*     */   public List getNodeFilters()
/*     */   {
/* 590 */     return this.nodeFilters;
/*     */   }
/*     */ 
/*     */   public void setNodeSet(boolean paramBoolean)
/*     */   {
/* 597 */     this.isNodeSet = paramBoolean;
/*     */   }
/*     */ 
/*     */   void convertToNodes() throws CanonicalizationException, ParserConfigurationException, IOException, SAXException
/*     */   {
/* 602 */     DocumentBuilderFactory localDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
/* 603 */     localDocumentBuilderFactory.setValidating(false);
/* 604 */     localDocumentBuilderFactory.setNamespaceAware(true);
/* 605 */     localDocumentBuilderFactory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", Boolean.TRUE.booleanValue());
/*     */ 
/* 607 */     DocumentBuilder localDocumentBuilder = localDocumentBuilderFactory.newDocumentBuilder();
/*     */     try
/*     */     {
/* 610 */       localDocumentBuilder.setErrorHandler(new IgnoreAllErrorHandler());
/*     */ 
/* 613 */       Document localDocument1 = localDocumentBuilder.parse(getOctetStream());
/*     */ 
/* 615 */       this._subNode = localDocument1.getDocumentElement();
/*     */     }
/*     */     catch (SAXException localSAXException)
/*     */     {
/* 619 */       ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
/*     */ 
/* 621 */       localByteArrayOutputStream.write("<container>".getBytes());
/* 622 */       localByteArrayOutputStream.write(getBytes());
/* 623 */       localByteArrayOutputStream.write("</container>".getBytes());
/*     */ 
/* 625 */       byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
/* 626 */       Document localDocument2 = localDocumentBuilder.parse(new ByteArrayInputStream(arrayOfByte));
/* 627 */       this._subNode = localDocument2.getDocumentElement().getFirstChild().getFirstChild();
/*     */     }
/* 629 */     this._inputOctetStreamProxy = null;
/* 630 */     this.bytes = null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput
 * JD-Core Version:    0.6.2
 */