/*     */ package com.sun.org.apache.xpath.internal.objects;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.dtm.DTM;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMIterator;
/*     */ import com.sun.org.apache.xml.internal.dtm.ref.DTMNodeIterator;
/*     */ import com.sun.org.apache.xml.internal.dtm.ref.DTMNodeList;
/*     */ import com.sun.org.apache.xml.internal.utils.FastStringBuffer;
/*     */ import com.sun.org.apache.xml.internal.utils.WrappedRuntimeException;
/*     */ import com.sun.org.apache.xml.internal.utils.XMLString;
/*     */ import com.sun.org.apache.xpath.internal.Expression;
/*     */ import com.sun.org.apache.xpath.internal.ExpressionNode;
/*     */ import com.sun.org.apache.xpath.internal.NodeSetDTM;
/*     */ import com.sun.org.apache.xpath.internal.XPathContext;
/*     */ import com.sun.org.apache.xpath.internal.axes.RTFIterator;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import org.w3c.dom.NodeList;
/*     */ 
/*     */ public class XRTreeFrag extends XObject
/*     */   implements Cloneable
/*     */ {
/*     */   static final long serialVersionUID = -3201553822254911567L;
/*     */   private DTMXRTreeFrag m_DTMXRTreeFrag;
/*  44 */   private int m_dtmRoot = -1;
/*  45 */   protected boolean m_allowRelease = false;
/*     */ 
/* 173 */   private XMLString m_xmlStr = null;
/*     */ 
/*     */   public XRTreeFrag(int root, XPathContext xctxt, ExpressionNode parent)
/*     */   {
/*  54 */     super(null);
/*  55 */     exprSetParent(parent);
/*  56 */     initDTM(root, xctxt);
/*     */   }
/*     */ 
/*     */   public XRTreeFrag(int root, XPathContext xctxt)
/*     */   {
/*  65 */     super(null);
/*  66 */     initDTM(root, xctxt);
/*     */   }
/*     */ 
/*     */   private final void initDTM(int root, XPathContext xctxt) {
/*  70 */     this.m_dtmRoot = root;
/*  71 */     DTM dtm = xctxt.getDTM(root);
/*  72 */     if (dtm != null)
/*  73 */       this.m_DTMXRTreeFrag = xctxt.getDTMXRTreeFrag(xctxt.getDTMIdentity(dtm));
/*     */   }
/*     */ 
/*     */   public Object object()
/*     */   {
/*  85 */     if (this.m_DTMXRTreeFrag.getXPathContext() != null) {
/*  86 */       return new DTMNodeIterator(new NodeSetDTM(this.m_dtmRoot, this.m_DTMXRTreeFrag.getXPathContext().getDTMManager()));
/*     */     }
/*  88 */     return super.object();
/*     */   }
/*     */ 
/*     */   public XRTreeFrag(Expression expr)
/*     */   {
/*  97 */     super(expr);
/*     */   }
/*     */ 
/*     */   public void allowDetachToRelease(boolean allowRelease)
/*     */   {
/* 108 */     this.m_allowRelease = allowRelease;
/*     */   }
/*     */ 
/*     */   public void detach()
/*     */   {
/* 121 */     if (this.m_allowRelease) {
/* 122 */       this.m_DTMXRTreeFrag.destruct();
/* 123 */       setObject(null);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getType()
/*     */   {
/* 134 */     return 5;
/*     */   }
/*     */ 
/*     */   public String getTypeString()
/*     */   {
/* 145 */     return "#RTREEFRAG";
/*     */   }
/*     */ 
/*     */   public double num()
/*     */     throws TransformerException
/*     */   {
/* 157 */     XMLString s = xstr();
/*     */ 
/* 159 */     return s.toDouble();
/*     */   }
/*     */ 
/*     */   public boolean bool()
/*     */   {
/* 170 */     return true;
/*     */   }
/*     */ 
/*     */   public XMLString xstr()
/*     */   {
/* 182 */     if (null == this.m_xmlStr) {
/* 183 */       this.m_xmlStr = this.m_DTMXRTreeFrag.getDTM().getStringValue(this.m_dtmRoot);
/*     */     }
/* 185 */     return this.m_xmlStr;
/*     */   }
/*     */ 
/*     */   public void appendToFsb(FastStringBuffer fsb)
/*     */   {
/* 195 */     XString xstring = (XString)xstr();
/* 196 */     xstring.appendToFsb(fsb);
/*     */   }
/*     */ 
/*     */   public String str()
/*     */   {
/* 207 */     String str = this.m_DTMXRTreeFrag.getDTM().getStringValue(this.m_dtmRoot).toString();
/*     */ 
/* 209 */     return null == str ? "" : str;
/*     */   }
/*     */ 
/*     */   public int rtf()
/*     */   {
/* 219 */     return this.m_dtmRoot;
/*     */   }
/*     */ 
/*     */   public DTMIterator asNodeIterator()
/*     */   {
/* 231 */     return new RTFIterator(this.m_dtmRoot, this.m_DTMXRTreeFrag.getXPathContext().getDTMManager());
/*     */   }
/*     */ 
/*     */   public NodeList convertToNodeset()
/*     */   {
/* 242 */     if ((this.m_obj instanceof NodeList)) {
/* 243 */       return (NodeList)this.m_obj;
/*     */     }
/* 245 */     return new DTMNodeList(asNodeIterator());
/*     */   }
/*     */ 
/*     */   public boolean equals(XObject obj2)
/*     */   {
/*     */     try
/*     */     {
/* 262 */       if (4 == obj2.getType())
/*     */       {
/* 268 */         return obj2.equals(this);
/*     */       }
/* 270 */       if (1 == obj2.getType())
/*     */       {
/* 272 */         return bool() == obj2.bool();
/*     */       }
/* 274 */       if (2 == obj2.getType())
/*     */       {
/* 276 */         return num() == obj2.num();
/*     */       }
/* 278 */       if (4 == obj2.getType())
/*     */       {
/* 280 */         return xstr().equals(obj2.xstr());
/*     */       }
/* 282 */       if (3 == obj2.getType())
/*     */       {
/* 284 */         return xstr().equals(obj2.xstr());
/*     */       }
/* 286 */       if (5 == obj2.getType())
/*     */       {
/* 290 */         return xstr().equals(obj2.xstr());
/*     */       }
/*     */ 
/* 294 */       return super.equals(obj2);
/*     */     }
/*     */     catch (TransformerException te)
/*     */     {
/* 299 */       throw new WrappedRuntimeException(te);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.objects.XRTreeFrag
 * JD-Core Version:    0.6.2
 */