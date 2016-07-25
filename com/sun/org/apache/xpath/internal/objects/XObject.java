/*     */ package com.sun.org.apache.xpath.internal.objects;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.res.XSLMessages;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTM;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMIterator;
/*     */ import com.sun.org.apache.xml.internal.utils.FastStringBuffer;
/*     */ import com.sun.org.apache.xml.internal.utils.XMLString;
/*     */ import com.sun.org.apache.xml.internal.utils.XMLStringFactory;
/*     */ import com.sun.org.apache.xpath.internal.Expression;
/*     */ import com.sun.org.apache.xpath.internal.ExpressionOwner;
/*     */ import com.sun.org.apache.xpath.internal.NodeSetDTM;
/*     */ import com.sun.org.apache.xpath.internal.XPathContext;
/*     */ import com.sun.org.apache.xpath.internal.XPathException;
/*     */ import com.sun.org.apache.xpath.internal.XPathVisitor;
/*     */ import java.io.Serializable;
/*     */ import java.util.Vector;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import org.w3c.dom.DocumentFragment;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.w3c.dom.traversal.NodeIterator;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class XObject extends Expression
/*     */   implements Serializable, Cloneable
/*     */ {
/*     */   static final long serialVersionUID = -821887098985662951L;
/*     */   protected Object m_obj;
/*     */   public static final int CLASS_NULL = -1;
/*     */   public static final int CLASS_UNKNOWN = 0;
/*     */   public static final int CLASS_BOOLEAN = 1;
/*     */   public static final int CLASS_NUMBER = 2;
/*     */   public static final int CLASS_STRING = 3;
/*     */   public static final int CLASS_NODESET = 4;
/*     */   public static final int CLASS_RTREEFRAG = 5;
/*     */   public static final int CLASS_UNRESOLVEDVARIABLE = 600;
/*     */ 
/*     */   public XObject()
/*     */   {
/*     */   }
/*     */ 
/*     */   public XObject(Object obj)
/*     */   {
/*  73 */     setObject(obj);
/*     */   }
/*     */ 
/*     */   protected void setObject(Object obj) {
/*  77 */     this.m_obj = obj;
/*     */   }
/*     */ 
/*     */   public XObject execute(XPathContext xctxt)
/*     */     throws TransformerException
/*     */   {
/*  92 */     return this;
/*     */   }
/*     */ 
/*     */   public void allowDetachToRelease(boolean allowRelease)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void detach()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void destruct()
/*     */   {
/* 123 */     if (null != this.m_obj)
/*     */     {
/* 125 */       allowDetachToRelease(true);
/* 126 */       detach();
/*     */ 
/* 128 */       setObject(null);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void dispatchCharactersEvents(ContentHandler ch)
/*     */     throws SAXException
/*     */   {
/* 153 */     xstr().dispatchCharactersEvents(ch);
/*     */   }
/*     */ 
/*     */   public static XObject create(Object val)
/*     */   {
/* 167 */     return XObjectFactory.create(val);
/*     */   }
/*     */ 
/*     */   public static XObject create(Object val, XPathContext xctxt)
/*     */   {
/* 182 */     return XObjectFactory.create(val, xctxt);
/*     */   }
/*     */ 
/*     */   public int getType()
/*     */   {
/* 216 */     return 0;
/*     */   }
/*     */ 
/*     */   public String getTypeString()
/*     */   {
/* 227 */     return "#UNKNOWN (" + object().getClass().getName() + ")";
/*     */   }
/*     */ 
/*     */   public double num()
/*     */     throws TransformerException
/*     */   {
/* 240 */     error("ER_CANT_CONVERT_TO_NUMBER", new Object[] { getTypeString() });
/*     */ 
/* 243 */     return 0.0D;
/*     */   }
/*     */ 
/*     */   public double numWithSideEffects()
/*     */     throws TransformerException
/*     */   {
/* 255 */     return num();
/*     */   }
/*     */ 
/*     */   public boolean bool()
/*     */     throws TransformerException
/*     */   {
/* 268 */     error("ER_CANT_CONVERT_TO_NUMBER", new Object[] { getTypeString() });
/*     */ 
/* 271 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean boolWithSideEffects()
/*     */     throws TransformerException
/*     */   {
/* 282 */     return bool();
/*     */   }
/*     */ 
/*     */   public XMLString xstr()
/*     */   {
/* 293 */     return XMLStringFactoryImpl.getFactory().newstr(str());
/*     */   }
/*     */ 
/*     */   public String str()
/*     */   {
/* 303 */     return this.m_obj != null ? this.m_obj.toString() : "";
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 314 */     return str();
/*     */   }
/*     */ 
/*     */   public int rtf(XPathContext support)
/*     */   {
/* 327 */     int result = rtf();
/*     */ 
/* 329 */     if (-1 == result)
/*     */     {
/* 331 */       DTM frag = support.createDocumentFragment();
/*     */ 
/* 334 */       frag.appendTextChild(str());
/*     */ 
/* 336 */       result = frag.getDocument();
/*     */     }
/*     */ 
/* 339 */     return result;
/*     */   }
/*     */ 
/*     */   public DocumentFragment rtree(XPathContext support)
/*     */   {
/* 351 */     DocumentFragment docFrag = null;
/* 352 */     int result = rtf();
/*     */ 
/* 354 */     if (-1 == result)
/*     */     {
/* 356 */       DTM frag = support.createDocumentFragment();
/*     */ 
/* 359 */       frag.appendTextChild(str());
/*     */ 
/* 361 */       docFrag = (DocumentFragment)frag.getNode(frag.getDocument());
/*     */     }
/*     */     else
/*     */     {
/* 365 */       DTM frag = support.getDTM(result);
/* 366 */       docFrag = (DocumentFragment)frag.getNode(frag.getDocument());
/*     */     }
/*     */ 
/* 369 */     return docFrag;
/*     */   }
/*     */ 
/*     */   public DocumentFragment rtree()
/*     */   {
/* 380 */     return null;
/*     */   }
/*     */ 
/*     */   public int rtf()
/*     */   {
/* 390 */     return -1;
/*     */   }
/*     */ 
/*     */   public Object object()
/*     */   {
/* 401 */     return this.m_obj;
/*     */   }
/*     */ 
/*     */   public DTMIterator iter()
/*     */     throws TransformerException
/*     */   {
/* 414 */     error("ER_CANT_CONVERT_TO_NODELIST", new Object[] { getTypeString() });
/*     */ 
/* 417 */     return null;
/*     */   }
/*     */ 
/*     */   public XObject getFresh()
/*     */   {
/* 427 */     return this;
/*     */   }
/*     */ 
/*     */   public NodeIterator nodeset()
/*     */     throws TransformerException
/*     */   {
/* 441 */     error("ER_CANT_CONVERT_TO_NODELIST", new Object[] { getTypeString() });
/*     */ 
/* 444 */     return null;
/*     */   }
/*     */ 
/*     */   public NodeList nodelist()
/*     */     throws TransformerException
/*     */   {
/* 457 */     error("ER_CANT_CONVERT_TO_NODELIST", new Object[] { getTypeString() });
/*     */ 
/* 460 */     return null;
/*     */   }
/*     */ 
/*     */   public NodeSetDTM mutableNodeset()
/*     */     throws TransformerException
/*     */   {
/* 475 */     error("ER_CANT_CONVERT_TO_MUTABLENODELIST", new Object[] { getTypeString() });
/*     */ 
/* 478 */     return (NodeSetDTM)this.m_obj;
/*     */   }
/*     */ 
/*     */   public Object castToType(int t, XPathContext support)
/*     */     throws TransformerException
/*     */   {
/*     */     Object result;
/* 497 */     switch (t)
/*     */     {
/*     */     case 3:
/* 500 */       result = str();
/* 501 */       break;
/*     */     case 2:
/* 503 */       result = new Double(num());
/* 504 */       break;
/*     */     case 4:
/* 506 */       result = iter();
/* 507 */       break;
/*     */     case 1:
/* 509 */       result = new Boolean(bool());
/* 510 */       break;
/*     */     case 0:
/* 512 */       result = this.m_obj;
/* 513 */       break;
/*     */     default:
/* 520 */       error("ER_CANT_CONVERT_TO_TYPE", new Object[] { getTypeString(), Integer.toString(t) });
/*     */ 
/* 524 */       result = null;
/*     */     }
/*     */ 
/* 527 */     return result;
/*     */   }
/*     */ 
/*     */   public boolean lessThan(XObject obj2)
/*     */     throws TransformerException
/*     */   {
/* 548 */     if (obj2.getType() == 4) {
/* 549 */       return obj2.greaterThan(this);
/*     */     }
/* 551 */     return num() < obj2.num();
/*     */   }
/*     */ 
/*     */   public boolean lessThanOrEqual(XObject obj2)
/*     */     throws TransformerException
/*     */   {
/* 572 */     if (obj2.getType() == 4) {
/* 573 */       return obj2.greaterThanOrEqual(this);
/*     */     }
/* 575 */     return num() <= obj2.num();
/*     */   }
/*     */ 
/*     */   public boolean greaterThan(XObject obj2)
/*     */     throws TransformerException
/*     */   {
/* 596 */     if (obj2.getType() == 4) {
/* 597 */       return obj2.lessThan(this);
/*     */     }
/* 599 */     return num() > obj2.num();
/*     */   }
/*     */ 
/*     */   public boolean greaterThanOrEqual(XObject obj2)
/*     */     throws TransformerException
/*     */   {
/* 620 */     if (obj2.getType() == 4) {
/* 621 */       return obj2.lessThanOrEqual(this);
/*     */     }
/* 623 */     return num() >= obj2.num();
/*     */   }
/*     */ 
/*     */   public boolean equals(XObject obj2)
/*     */   {
/* 641 */     if (obj2.getType() == 4) {
/* 642 */       return obj2.equals(this);
/*     */     }
/* 644 */     if (null != this.m_obj)
/*     */     {
/* 646 */       return this.m_obj.equals(obj2.m_obj);
/*     */     }
/*     */ 
/* 650 */     return obj2.m_obj == null;
/*     */   }
/*     */ 
/*     */   public boolean notEquals(XObject obj2)
/*     */     throws TransformerException
/*     */   {
/* 670 */     if (obj2.getType() == 4) {
/* 671 */       return obj2.notEquals(this);
/*     */     }
/* 673 */     return !equals(obj2);
/*     */   }
/*     */ 
/*     */   protected void error(String msg)
/*     */     throws TransformerException
/*     */   {
/* 687 */     error(msg, null);
/*     */   }
/*     */ 
/*     */   protected void error(String msg, Object[] args)
/*     */     throws TransformerException
/*     */   {
/* 703 */     String fmsg = XSLMessages.createXPATHMessage(msg, args);
/*     */ 
/* 711 */     throw new XPathException(fmsg, this);
/*     */   }
/*     */ 
/*     */   public void fixupVariables(Vector vars, int globalsSize)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void appendToFsb(FastStringBuffer fsb)
/*     */   {
/* 734 */     fsb.append(str());
/*     */   }
/*     */ 
/*     */   public void callVisitors(ExpressionOwner owner, XPathVisitor visitor)
/*     */   {
/* 742 */     assertion(false, "callVisitors should not be called for this object!!!");
/*     */   }
/*     */ 
/*     */   public boolean deepEquals(Expression expr)
/*     */   {
/* 749 */     if (!isSameClass(expr)) {
/* 750 */       return false;
/*     */     }
/*     */ 
/* 755 */     if (!equals((XObject)expr)) {
/* 756 */       return false;
/*     */     }
/* 758 */     return true;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.objects.XObject
 * JD-Core Version:    0.6.2
 */