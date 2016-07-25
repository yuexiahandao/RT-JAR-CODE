/*     */ package com.sun.org.apache.xpath.internal.objects;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.dtm.DTM;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMIterator;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMManager;
/*     */ import com.sun.org.apache.xml.internal.dtm.ref.DTMNodeIterator;
/*     */ import com.sun.org.apache.xml.internal.dtm.ref.DTMNodeList;
/*     */ import com.sun.org.apache.xml.internal.utils.FastStringBuffer;
/*     */ import com.sun.org.apache.xml.internal.utils.WrappedRuntimeException;
/*     */ import com.sun.org.apache.xml.internal.utils.XMLString;
/*     */ import com.sun.org.apache.xpath.internal.NodeSetDTM;
/*     */ import com.sun.org.apache.xpath.internal.axes.NodeSequence;
/*     */ import java.util.Vector;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.w3c.dom.traversal.NodeIterator;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class XNodeSet extends NodeSequence
/*     */ {
/*     */   static final long serialVersionUID = 1916026368035639667L;
/* 446 */   static final LessThanComparator S_LT = new LessThanComparator();
/*     */ 
/* 449 */   static final LessThanOrEqualComparator S_LTE = new LessThanOrEqualComparator();
/*     */ 
/* 452 */   static final GreaterThanComparator S_GT = new GreaterThanComparator();
/*     */ 
/* 455 */   static final GreaterThanOrEqualComparator S_GTE = new GreaterThanOrEqualComparator();
/*     */ 
/* 459 */   static final EqualComparator S_EQ = new EqualComparator();
/*     */ 
/* 462 */   static final NotEqualComparator S_NEQ = new NotEqualComparator();
/*     */ 
/*     */   protected XNodeSet()
/*     */   {
/*     */   }
/*     */ 
/*     */   public XNodeSet(DTMIterator val)
/*     */   {
/*  58 */     if ((val instanceof XNodeSet))
/*     */     {
/*  60 */       XNodeSet nodeSet = (XNodeSet)val;
/*  61 */       setIter(nodeSet.m_iter);
/*  62 */       this.m_dtmMgr = nodeSet.m_dtmMgr;
/*  63 */       this.m_last = nodeSet.m_last;
/*     */ 
/*  66 */       if (!nodeSet.hasCache()) {
/*  67 */         nodeSet.setShouldCacheNodes(true);
/*     */       }
/*     */ 
/*  70 */       setObject(nodeSet.getIteratorCache());
/*     */     }
/*     */     else {
/*  73 */       setIter(val);
/*     */     }
/*     */   }
/*     */ 
/*     */   public XNodeSet(XNodeSet val)
/*     */   {
/*  84 */     setIter(val.m_iter);
/*  85 */     this.m_dtmMgr = val.m_dtmMgr;
/*  86 */     this.m_last = val.m_last;
/*  87 */     if (!val.hasCache())
/*  88 */       val.setShouldCacheNodes(true);
/*  89 */     setObject(val.m_obj);
/*     */   }
/*     */ 
/*     */   public XNodeSet(DTMManager dtmMgr)
/*     */   {
/*  99 */     this(-1, dtmMgr);
/*     */   }
/*     */ 
/*     */   public XNodeSet(int n, DTMManager dtmMgr)
/*     */   {
/* 110 */     super(new NodeSetDTM(dtmMgr));
/* 111 */     this.m_dtmMgr = dtmMgr;
/*     */ 
/* 113 */     if (-1 != n)
/*     */     {
/* 115 */       ((NodeSetDTM)this.m_obj).addNode(n);
/* 116 */       this.m_last = 1;
/*     */     }
/*     */     else {
/* 119 */       this.m_last = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getType()
/*     */   {
/* 129 */     return 4;
/*     */   }
/*     */ 
/*     */   public String getTypeString()
/*     */   {
/* 140 */     return "#NODESET";
/*     */   }
/*     */ 
/*     */   public double getNumberFromNode(int n)
/*     */   {
/* 152 */     XMLString xstr = this.m_dtmMgr.getDTM(n).getStringValue(n);
/* 153 */     return xstr.toDouble();
/*     */   }
/*     */ 
/*     */   public double num()
/*     */   {
/* 165 */     int node = item(0);
/* 166 */     return node != -1 ? getNumberFromNode(node) : (0.0D / 0.0D);
/*     */   }
/*     */ 
/*     */   public double numWithSideEffects()
/*     */   {
/* 178 */     int node = nextNode();
/*     */ 
/* 180 */     return node != -1 ? getNumberFromNode(node) : (0.0D / 0.0D);
/*     */   }
/*     */ 
/*     */   public boolean bool()
/*     */   {
/* 191 */     return item(0) != -1;
/*     */   }
/*     */ 
/*     */   public boolean boolWithSideEffects()
/*     */   {
/* 202 */     return nextNode() != -1;
/*     */   }
/*     */ 
/*     */   public XMLString getStringFromNode(int n)
/*     */   {
/* 217 */     if (-1 != n)
/*     */     {
/* 219 */       return this.m_dtmMgr.getDTM(n).getStringValue(n);
/*     */     }
/*     */ 
/* 223 */     return XString.EMPTYSTRING;
/*     */   }
/*     */ 
/*     */   public void dispatchCharactersEvents(ContentHandler ch)
/*     */     throws SAXException
/*     */   {
/* 241 */     int node = item(0);
/*     */ 
/* 243 */     if (node != -1)
/*     */     {
/* 245 */       this.m_dtmMgr.getDTM(node).dispatchCharactersEvents(node, ch, false);
/*     */     }
/*     */   }
/*     */ 
/*     */   public XMLString xstr()
/*     */   {
/* 257 */     int node = item(0);
/* 258 */     return node != -1 ? getStringFromNode(node) : XString.EMPTYSTRING;
/*     */   }
/*     */ 
/*     */   public void appendToFsb(FastStringBuffer fsb)
/*     */   {
/* 268 */     XString xstring = (XString)xstr();
/* 269 */     xstring.appendToFsb(fsb);
/*     */   }
/*     */ 
/*     */   public String str()
/*     */   {
/* 281 */     int node = item(0);
/* 282 */     return node != -1 ? getStringFromNode(node).toString() : "";
/*     */   }
/*     */ 
/*     */   public Object object()
/*     */   {
/* 293 */     if (null == this.m_obj) {
/* 294 */       return this;
/*     */     }
/* 296 */     return this.m_obj;
/*     */   }
/*     */ 
/*     */   public NodeIterator nodeset()
/*     */     throws TransformerException
/*     */   {
/* 336 */     return new DTMNodeIterator(iter());
/*     */   }
/*     */ 
/*     */   public NodeList nodelist()
/*     */     throws TransformerException
/*     */   {
/* 348 */     DTMNodeList nodelist = new DTMNodeList(this);
/*     */ 
/* 353 */     XNodeSet clone = (XNodeSet)nodelist.getDTMIterator();
/* 354 */     SetVector(clone.getVector());
/* 355 */     return nodelist;
/*     */   }
/*     */ 
/*     */   public DTMIterator iterRaw()
/*     */   {
/* 375 */     return this;
/*     */   }
/*     */ 
/*     */   public void release(DTMIterator iter)
/*     */   {
/*     */   }
/*     */ 
/*     */   public DTMIterator iter()
/*     */   {
/*     */     try
/*     */     {
/* 391 */       if (hasCache()) {
/* 392 */         return cloneWithReset();
/*     */       }
/* 394 */       return this;
/*     */     }
/*     */     catch (CloneNotSupportedException cnse)
/*     */     {
/* 398 */       throw new RuntimeException(cnse.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public XObject getFresh()
/*     */   {
/*     */     try
/*     */     {
/* 411 */       if (hasCache()) {
/* 412 */         return (XObject)cloneWithReset();
/*     */       }
/* 414 */       return this;
/*     */     }
/*     */     catch (CloneNotSupportedException cnse)
/*     */     {
/* 418 */       throw new RuntimeException(cnse.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public NodeSetDTM mutableNodeset()
/*     */   {
/*     */     NodeSetDTM mnl;
/*     */     NodeSetDTM mnl;
/* 431 */     if ((this.m_obj instanceof NodeSetDTM))
/*     */     {
/* 433 */       mnl = (NodeSetDTM)this.m_obj;
/*     */     }
/*     */     else
/*     */     {
/* 437 */       mnl = new NodeSetDTM(iter());
/* 438 */       setObject(mnl);
/* 439 */       setCurrentPos(0);
/*     */     }
/*     */ 
/* 442 */     return mnl;
/*     */   }
/*     */ 
/*     */   public boolean compare(XObject obj2, Comparator comparator)
/*     */     throws TransformerException
/*     */   {
/* 478 */     boolean result = false;
/* 479 */     int type = obj2.getType();
/*     */ 
/* 481 */     if (4 == type)
/*     */     {
/* 496 */       DTMIterator list1 = iterRaw();
/* 497 */       DTMIterator list2 = ((XNodeSet)obj2).iterRaw();
/*     */ 
/* 499 */       Vector node2Strings = null;
/*     */       int node1;
/* 501 */       while (-1 != (node1 = list1.nextNode()))
/*     */       {
/* 503 */         XMLString s1 = getStringFromNode(node1);
/*     */ 
/* 505 */         if (null == node2Strings)
/*     */         {
/*     */           int node2;
/* 509 */           while (-1 != (node2 = list2.nextNode()))
/*     */           {
/* 511 */             XMLString s2 = getStringFromNode(node2);
/*     */ 
/* 513 */             if (comparator.compareStrings(s1, s2))
/*     */             {
/* 515 */               result = true;
/*     */ 
/* 517 */               break;
/*     */             }
/*     */ 
/* 520 */             if (null == node2Strings) {
/* 521 */               node2Strings = new Vector();
/*     */             }
/* 523 */             node2Strings.addElement(s2);
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 528 */           int n = node2Strings.size();
/*     */ 
/* 530 */           for (int i = 0; i < n; i++)
/*     */           {
/* 532 */             if (comparator.compareStrings(s1, (XMLString)node2Strings.elementAt(i)))
/*     */             {
/* 534 */               result = true;
/*     */ 
/* 536 */               break;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 541 */       list1.reset();
/* 542 */       list2.reset();
/*     */     }
/* 544 */     else if (1 == type)
/*     */     {
/* 553 */       double num1 = bool() ? 1.0D : 0.0D;
/* 554 */       double num2 = obj2.num();
/*     */ 
/* 556 */       result = comparator.compareNumbers(num1, num2);
/*     */     }
/* 558 */     else if (2 == type)
/*     */     {
/* 568 */       DTMIterator list1 = iterRaw();
/* 569 */       double num2 = obj2.num();
/*     */       int node;
/* 572 */       while (-1 != (node = list1.nextNode()))
/*     */       {
/* 574 */         double num1 = getNumberFromNode(node);
/*     */ 
/* 576 */         if (comparator.compareNumbers(num1, num2))
/*     */         {
/* 578 */           result = true;
/*     */ 
/* 580 */           break;
/*     */         }
/*     */       }
/* 583 */       list1.reset();
/*     */     }
/* 585 */     else if (5 == type)
/*     */     {
/* 587 */       XMLString s2 = obj2.xstr();
/* 588 */       DTMIterator list1 = iterRaw();
/*     */       int node;
/* 591 */       while (-1 != (node = list1.nextNode()))
/*     */       {
/* 593 */         XMLString s1 = getStringFromNode(node);
/*     */ 
/* 595 */         if (comparator.compareStrings(s1, s2))
/*     */         {
/* 597 */           result = true;
/*     */ 
/* 599 */           break;
/*     */         }
/*     */       }
/* 602 */       list1.reset();
/*     */     }
/* 604 */     else if (3 == type)
/*     */     {
/* 613 */       XMLString s2 = obj2.xstr();
/* 614 */       DTMIterator list1 = iterRaw();
/*     */       int node;
/* 617 */       while (-1 != (node = list1.nextNode()))
/*     */       {
/* 619 */         XMLString s1 = getStringFromNode(node);
/* 620 */         if (comparator.compareStrings(s1, s2))
/*     */         {
/* 622 */           result = true;
/*     */ 
/* 624 */           break;
/*     */         }
/*     */       }
/* 627 */       list1.reset();
/*     */     }
/*     */     else
/*     */     {
/* 631 */       result = comparator.compareNumbers(num(), obj2.num());
/*     */     }
/*     */ 
/* 634 */     return result;
/*     */   }
/*     */ 
/*     */   public boolean lessThan(XObject obj2)
/*     */     throws TransformerException
/*     */   {
/* 648 */     return compare(obj2, S_LT);
/*     */   }
/*     */ 
/*     */   public boolean lessThanOrEqual(XObject obj2)
/*     */     throws TransformerException
/*     */   {
/* 662 */     return compare(obj2, S_LTE);
/*     */   }
/*     */ 
/*     */   public boolean greaterThan(XObject obj2)
/*     */     throws TransformerException
/*     */   {
/* 676 */     return compare(obj2, S_GT);
/*     */   }
/*     */ 
/*     */   public boolean greaterThanOrEqual(XObject obj2)
/*     */     throws TransformerException
/*     */   {
/* 691 */     return compare(obj2, S_GTE);
/*     */   }
/*     */ 
/*     */   public boolean equals(XObject obj2)
/*     */   {
/*     */     try
/*     */     {
/* 707 */       return compare(obj2, S_EQ);
/*     */     }
/*     */     catch (TransformerException te)
/*     */     {
/* 711 */       throw new WrappedRuntimeException(te);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean notEquals(XObject obj2)
/*     */     throws TransformerException
/*     */   {
/* 726 */     return compare(obj2, S_NEQ);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.objects.XNodeSet
 * JD-Core Version:    0.6.2
 */