/*     */ package com.sun.org.apache.xpath.internal.patterns;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.dtm.DTM;
/*     */ import com.sun.org.apache.xpath.internal.Expression;
/*     */ import com.sun.org.apache.xpath.internal.ExpressionOwner;
/*     */ import com.sun.org.apache.xpath.internal.XPathContext;
/*     */ import com.sun.org.apache.xpath.internal.XPathVisitor;
/*     */ import com.sun.org.apache.xpath.internal.objects.XNumber;
/*     */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Vector;
/*     */ import javax.xml.transform.TransformerException;
/*     */ 
/*     */ public class NodeTest extends Expression
/*     */ {
/*     */   static final long serialVersionUID = -5736721866747906182L;
/*     */   public static final String WILD = "*";
/*     */   public static final String SUPPORTS_PRE_STRIPPING = "http://xml.apache.org/xpath/features/whitespace-pre-stripping";
/*     */   protected int m_whatToShow;
/*     */   public static final int SHOW_BYFUNCTION = 65536;
/*     */   String m_namespace;
/*     */   protected String m_name;
/*     */   XNumber m_score;
/* 160 */   public static final XNumber SCORE_NODETEST = new XNumber(-0.5D);
/*     */ 
/* 167 */   public static final XNumber SCORE_NSWILD = new XNumber(-0.25D);
/*     */ 
/* 175 */   public static final XNumber SCORE_QNAME = new XNumber(0.0D);
/*     */ 
/* 183 */   public static final XNumber SCORE_OTHER = new XNumber(0.5D);
/*     */ 
/* 190 */   public static final XNumber SCORE_NONE = new XNumber((-1.0D / 0.0D));
/*     */   private boolean m_isTotallyWild;
/*     */ 
/*     */   public int getWhatToShow()
/*     */   {
/*  78 */     return this.m_whatToShow;
/*     */   }
/*     */ 
/*     */   public void setWhatToShow(int what)
/*     */   {
/*  90 */     this.m_whatToShow = what;
/*     */   }
/*     */ 
/*     */   public String getNamespace()
/*     */   {
/* 106 */     return this.m_namespace;
/*     */   }
/*     */ 
/*     */   public void setNamespace(String ns)
/*     */   {
/* 116 */     this.m_namespace = ns;
/*     */   }
/*     */ 
/*     */   public String getLocalName()
/*     */   {
/* 132 */     return null == this.m_name ? "" : this.m_name;
/*     */   }
/*     */ 
/*     */   public void setLocalName(String name)
/*     */   {
/* 142 */     this.m_name = name;
/*     */   }
/*     */ 
/*     */   public NodeTest(int whatToShow, String namespace, String name)
/*     */   {
/* 203 */     initNodeTest(whatToShow, namespace, name);
/*     */   }
/*     */ 
/*     */   public NodeTest(int whatToShow)
/*     */   {
/* 214 */     initNodeTest(whatToShow);
/*     */   }
/*     */ 
/*     */   public boolean deepEquals(Expression expr)
/*     */   {
/* 222 */     if (!isSameClass(expr)) {
/* 223 */       return false;
/*     */     }
/* 225 */     NodeTest nt = (NodeTest)expr;
/*     */ 
/* 227 */     if (null != nt.m_name)
/*     */     {
/* 229 */       if (null == this.m_name)
/* 230 */         return false;
/* 231 */       if (!nt.m_name.equals(this.m_name))
/* 232 */         return false;
/*     */     }
/* 234 */     else if (null != this.m_name) {
/* 235 */       return false;
/*     */     }
/* 237 */     if (null != nt.m_namespace)
/*     */     {
/* 239 */       if (null == this.m_namespace)
/* 240 */         return false;
/* 241 */       if (!nt.m_namespace.equals(this.m_namespace))
/* 242 */         return false;
/*     */     }
/* 244 */     else if (null != this.m_namespace) {
/* 245 */       return false;
/*     */     }
/* 247 */     if (this.m_whatToShow != nt.m_whatToShow) {
/* 248 */       return false;
/*     */     }
/* 250 */     if (this.m_isTotallyWild != nt.m_isTotallyWild) {
/* 251 */       return false;
/*     */     }
/* 253 */     return true;
/*     */   }
/*     */ 
/*     */   public NodeTest()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void initNodeTest(int whatToShow)
/*     */   {
/* 271 */     this.m_whatToShow = whatToShow;
/*     */ 
/* 273 */     calcScore();
/*     */   }
/*     */ 
/*     */   public void initNodeTest(int whatToShow, String namespace, String name)
/*     */   {
/* 289 */     this.m_whatToShow = whatToShow;
/* 290 */     this.m_namespace = namespace;
/* 291 */     this.m_name = name;
/*     */ 
/* 293 */     calcScore();
/*     */   }
/*     */ 
/*     */   public XNumber getStaticScore()
/*     */   {
/* 308 */     return this.m_score;
/*     */   }
/*     */ 
/*     */   public void setStaticScore(XNumber score)
/*     */   {
/* 317 */     this.m_score = score;
/*     */   }
/*     */ 
/*     */   protected void calcScore()
/*     */   {
/* 326 */     if ((this.m_namespace == null) && (this.m_name == null))
/* 327 */       this.m_score = SCORE_NODETEST;
/* 328 */     else if (((this.m_namespace == "*") || (this.m_namespace == null)) && (this.m_name == "*"))
/*     */     {
/* 330 */       this.m_score = SCORE_NODETEST;
/* 331 */     } else if ((this.m_namespace != "*") && (this.m_name == "*"))
/* 332 */       this.m_score = SCORE_NSWILD;
/*     */     else {
/* 334 */       this.m_score = SCORE_QNAME;
/*     */     }
/* 336 */     this.m_isTotallyWild = ((this.m_namespace == null) && (this.m_name == "*"));
/*     */   }
/*     */ 
/*     */   public double getDefaultScore()
/*     */   {
/* 347 */     return this.m_score.num();
/*     */   }
/*     */ 
/*     */   public static int getNodeTypeTest(int whatToShow)
/*     */   {
/* 364 */     if (0 != (whatToShow & 0x1)) {
/* 365 */       return 1;
/*     */     }
/* 367 */     if (0 != (whatToShow & 0x2)) {
/* 368 */       return 2;
/*     */     }
/* 370 */     if (0 != (whatToShow & 0x4)) {
/* 371 */       return 3;
/*     */     }
/* 373 */     if (0 != (whatToShow & 0x100)) {
/* 374 */       return 9;
/*     */     }
/* 376 */     if (0 != (whatToShow & 0x400)) {
/* 377 */       return 11;
/*     */     }
/* 379 */     if (0 != (whatToShow & 0x1000)) {
/* 380 */       return 13;
/*     */     }
/* 382 */     if (0 != (whatToShow & 0x80)) {
/* 383 */       return 8;
/*     */     }
/* 385 */     if (0 != (whatToShow & 0x40)) {
/* 386 */       return 7;
/*     */     }
/* 388 */     if (0 != (whatToShow & 0x200)) {
/* 389 */       return 10;
/*     */     }
/* 391 */     if (0 != (whatToShow & 0x20)) {
/* 392 */       return 6;
/*     */     }
/* 394 */     if (0 != (whatToShow & 0x10)) {
/* 395 */       return 5;
/*     */     }
/* 397 */     if (0 != (whatToShow & 0x800)) {
/* 398 */       return 12;
/*     */     }
/* 400 */     if (0 != (whatToShow & 0x8)) {
/* 401 */       return 4;
/*     */     }
/*     */ 
/* 404 */     return 0;
/*     */   }
/*     */ 
/*     */   public static void debugWhatToShow(int whatToShow)
/*     */   {
/* 418 */     Vector v = new Vector();
/*     */ 
/* 420 */     if (0 != (whatToShow & 0x2)) {
/* 421 */       v.addElement("SHOW_ATTRIBUTE");
/*     */     }
/* 423 */     if (0 != (whatToShow & 0x1000)) {
/* 424 */       v.addElement("SHOW_NAMESPACE");
/*     */     }
/* 426 */     if (0 != (whatToShow & 0x8)) {
/* 427 */       v.addElement("SHOW_CDATA_SECTION");
/*     */     }
/* 429 */     if (0 != (whatToShow & 0x80)) {
/* 430 */       v.addElement("SHOW_COMMENT");
/*     */     }
/* 432 */     if (0 != (whatToShow & 0x100)) {
/* 433 */       v.addElement("SHOW_DOCUMENT");
/*     */     }
/* 435 */     if (0 != (whatToShow & 0x400)) {
/* 436 */       v.addElement("SHOW_DOCUMENT_FRAGMENT");
/*     */     }
/* 438 */     if (0 != (whatToShow & 0x200)) {
/* 439 */       v.addElement("SHOW_DOCUMENT_TYPE");
/*     */     }
/* 441 */     if (0 != (whatToShow & 0x1)) {
/* 442 */       v.addElement("SHOW_ELEMENT");
/*     */     }
/* 444 */     if (0 != (whatToShow & 0x20)) {
/* 445 */       v.addElement("SHOW_ENTITY");
/*     */     }
/* 447 */     if (0 != (whatToShow & 0x10)) {
/* 448 */       v.addElement("SHOW_ENTITY_REFERENCE");
/*     */     }
/* 450 */     if (0 != (whatToShow & 0x800)) {
/* 451 */       v.addElement("SHOW_NOTATION");
/*     */     }
/* 453 */     if (0 != (whatToShow & 0x40)) {
/* 454 */       v.addElement("SHOW_PROCESSING_INSTRUCTION");
/*     */     }
/* 456 */     if (0 != (whatToShow & 0x4)) {
/* 457 */       v.addElement("SHOW_TEXT");
/*     */     }
/* 459 */     int n = v.size();
/*     */ 
/* 461 */     for (int i = 0; i < n; i++)
/*     */     {
/* 463 */       if (i > 0) {
/* 464 */         System.out.print(" | ");
/*     */       }
/* 466 */       System.out.print(v.elementAt(i));
/*     */     }
/*     */ 
/* 469 */     if (0 == n) {
/* 470 */       System.out.print("empty whatToShow: " + whatToShow);
/*     */     }
/* 472 */     System.out.println();
/*     */   }
/*     */ 
/*     */   private static final boolean subPartMatch(String p, String t)
/*     */   {
/* 490 */     return (p == t) || ((null != p) && ((t == "*") || (p.equals(t))));
/*     */   }
/*     */ 
/*     */   private static final boolean subPartMatchNS(String p, String t)
/*     */   {
/* 506 */     return (p == t) || ((null != p) && (p.length() > 0 ? (t == "*") || (p.equals(t)) : null == t));
/*     */   }
/*     */ 
/*     */   public XObject execute(XPathContext xctxt, int context)
/*     */     throws TransformerException
/*     */   {
/* 531 */     DTM dtm = xctxt.getDTM(context);
/* 532 */     short nodeType = dtm.getNodeType(context);
/*     */ 
/* 534 */     if (this.m_whatToShow == -1) {
/* 535 */       return this.m_score;
/*     */     }
/* 537 */     int nodeBit = this.m_whatToShow & 1 << nodeType - 1;
/*     */ 
/* 539 */     switch (nodeBit)
/*     */     {
/*     */     case 256:
/*     */     case 1024:
/* 543 */       return SCORE_OTHER;
/*     */     case 128:
/* 545 */       return this.m_score;
/*     */     case 4:
/*     */     case 8:
/* 552 */       return this.m_score;
/*     */     case 64:
/* 554 */       return subPartMatch(dtm.getNodeName(context), this.m_name) ? this.m_score : SCORE_NONE;
/*     */     case 4096:
/* 571 */       String ns = dtm.getLocalName(context);
/*     */ 
/* 573 */       return subPartMatch(ns, this.m_name) ? this.m_score : SCORE_NONE;
/*     */     case 1:
/*     */     case 2:
/* 578 */       return (this.m_isTotallyWild) || ((subPartMatchNS(dtm.getNamespaceURI(context), this.m_namespace)) && (subPartMatch(dtm.getLocalName(context), this.m_name))) ? this.m_score : SCORE_NONE;
/*     */     }
/*     */ 
/* 582 */     return SCORE_NONE;
/*     */   }
/*     */ 
/*     */   public XObject execute(XPathContext xctxt, int context, DTM dtm, int expType)
/*     */     throws TransformerException
/*     */   {
/* 606 */     if (this.m_whatToShow == -1) {
/* 607 */       return this.m_score;
/*     */     }
/* 609 */     int nodeBit = this.m_whatToShow & 1 << dtm.getNodeType(context) - 1;
/*     */ 
/* 612 */     switch (nodeBit)
/*     */     {
/*     */     case 256:
/*     */     case 1024:
/* 616 */       return SCORE_OTHER;
/*     */     case 128:
/* 618 */       return this.m_score;
/*     */     case 4:
/*     */     case 8:
/* 625 */       return this.m_score;
/*     */     case 64:
/* 627 */       return subPartMatch(dtm.getNodeName(context), this.m_name) ? this.m_score : SCORE_NONE;
/*     */     case 4096:
/* 644 */       String ns = dtm.getLocalName(context);
/*     */ 
/* 646 */       return subPartMatch(ns, this.m_name) ? this.m_score : SCORE_NONE;
/*     */     case 1:
/*     */     case 2:
/* 651 */       return (this.m_isTotallyWild) || ((subPartMatchNS(dtm.getNamespaceURI(context), this.m_namespace)) && (subPartMatch(dtm.getLocalName(context), this.m_name))) ? this.m_score : SCORE_NONE;
/*     */     }
/*     */ 
/* 655 */     return SCORE_NONE;
/*     */   }
/*     */ 
/*     */   public XObject execute(XPathContext xctxt)
/*     */     throws TransformerException
/*     */   {
/* 675 */     return execute(xctxt, xctxt.getCurrentNode());
/*     */   }
/*     */ 
/*     */   public void fixupVariables(Vector vars, int globalsSize)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void callVisitors(ExpressionOwner owner, XPathVisitor visitor)
/*     */   {
/* 691 */     assertion(false, "callVisitors should not be called for this object!!!");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.patterns.NodeTest
 * JD-Core Version:    0.6.2
 */