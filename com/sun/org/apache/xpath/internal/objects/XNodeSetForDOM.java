/*     */ package com.sun.org.apache.xpath.internal.objects;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMManager;
/*     */ import com.sun.org.apache.xpath.internal.NodeSetDTM;
/*     */ import com.sun.org.apache.xpath.internal.XPathContext;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.w3c.dom.traversal.NodeIterator;
/*     */ 
/*     */ public class XNodeSetForDOM extends XNodeSet
/*     */ {
/*     */   static final long serialVersionUID = -8396190713754624640L;
/*     */   Object m_origObj;
/*     */ 
/*     */   public XNodeSetForDOM(Node node, DTMManager dtmMgr)
/*     */   {
/*  44 */     this.m_dtmMgr = dtmMgr;
/*  45 */     this.m_origObj = node;
/*  46 */     int dtmHandle = dtmMgr.getDTMHandleFromNode(node);
/*  47 */     setObject(new NodeSetDTM(dtmMgr));
/*  48 */     ((NodeSetDTM)this.m_obj).addNode(dtmHandle);
/*     */   }
/*     */ 
/*     */   public XNodeSetForDOM(XNodeSet val)
/*     */   {
/*  58 */     super(val);
/*  59 */     if ((val instanceof XNodeSetForDOM))
/*  60 */       this.m_origObj = ((XNodeSetForDOM)val).m_origObj;
/*     */   }
/*     */ 
/*     */   public XNodeSetForDOM(NodeList nodeList, XPathContext xctxt)
/*     */   {
/*  65 */     this.m_dtmMgr = xctxt.getDTMManager();
/*  66 */     this.m_origObj = nodeList;
/*     */ 
/*  72 */     NodeSetDTM nsdtm = new NodeSetDTM(nodeList, xctxt);
/*  73 */     this.m_last = nsdtm.getLength();
/*  74 */     setObject(nsdtm);
/*     */   }
/*     */ 
/*     */   public XNodeSetForDOM(NodeIterator nodeIter, XPathContext xctxt)
/*     */   {
/*  79 */     this.m_dtmMgr = xctxt.getDTMManager();
/*  80 */     this.m_origObj = nodeIter;
/*     */ 
/*  86 */     NodeSetDTM nsdtm = new NodeSetDTM(nodeIter, xctxt);
/*  87 */     this.m_last = nsdtm.getLength();
/*  88 */     setObject(nsdtm);
/*     */   }
/*     */ 
/*     */   public Object object()
/*     */   {
/*  99 */     return this.m_origObj;
/*     */   }
/*     */ 
/*     */   public NodeIterator nodeset()
/*     */     throws TransformerException
/*     */   {
/* 111 */     return (this.m_origObj instanceof NodeIterator) ? (NodeIterator)this.m_origObj : super.nodeset();
/*     */   }
/*     */ 
/*     */   public NodeList nodelist()
/*     */     throws TransformerException
/*     */   {
/* 124 */     return (this.m_origObj instanceof NodeList) ? (NodeList)this.m_origObj : super.nodelist();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.objects.XNodeSetForDOM
 * JD-Core Version:    0.6.2
 */