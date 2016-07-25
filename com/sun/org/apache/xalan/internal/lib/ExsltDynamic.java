/*     */ package com.sun.org.apache.xalan.internal.lib;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.extensions.ExpressionContext;
/*     */ import com.sun.org.apache.xalan.internal.res.XSLMessages;
/*     */ import com.sun.org.apache.xpath.internal.NodeSet;
/*     */ import com.sun.org.apache.xpath.internal.NodeSetDTM;
/*     */ import com.sun.org.apache.xpath.internal.XPath;
/*     */ import com.sun.org.apache.xpath.internal.XPathContext;
/*     */ import com.sun.org.apache.xpath.internal.XPathContext.XPathExpressionContext;
/*     */ import com.sun.org.apache.xpath.internal.objects.XBoolean;
/*     */ import com.sun.org.apache.xpath.internal.objects.XNodeSet;
/*     */ import com.sun.org.apache.xpath.internal.objects.XNumber;
/*     */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.w3c.dom.Text;
/*     */ import org.xml.sax.SAXNotSupportedException;
/*     */ 
/*     */ public class ExsltDynamic extends ExsltBase
/*     */ {
/*     */   public static final String EXSL_URI = "http://exslt.org/common";
/*     */ 
/*     */   public static double max(ExpressionContext myContext, NodeList nl, String expr)
/*     */     throws SAXNotSupportedException
/*     */   {
/* 105 */     XPathContext xctxt = null;
/* 106 */     if ((myContext instanceof XPathContext.XPathExpressionContext))
/* 107 */       xctxt = ((XPathContext.XPathExpressionContext)myContext).getXPathContext();
/*     */     else {
/* 109 */       throw new SAXNotSupportedException(XSLMessages.createMessage("ER_INVALID_CONTEXT_PASSED", new Object[] { myContext }));
/*     */     }
/* 111 */     if ((expr == null) || (expr.length() == 0)) {
/* 112 */       return (0.0D / 0.0D);
/*     */     }
/* 114 */     NodeSetDTM contextNodes = new NodeSetDTM(nl, xctxt);
/* 115 */     xctxt.pushContextNodeList(contextNodes);
/*     */ 
/* 117 */     double maxValue = -1.797693134862316E+308D;
/* 118 */     for (int i = 0; i < contextNodes.getLength(); i++)
/*     */     {
/* 120 */       int contextNode = contextNodes.item(i);
/* 121 */       xctxt.pushCurrentNode(contextNode);
/*     */ 
/* 123 */       double result = 0.0D;
/*     */       try
/*     */       {
/* 126 */         XPath dynamicXPath = new XPath(expr, xctxt.getSAXLocator(), xctxt.getNamespaceContext(), 0);
/*     */ 
/* 129 */         result = dynamicXPath.execute(xctxt, contextNode, xctxt.getNamespaceContext()).num();
/*     */       }
/*     */       catch (TransformerException e)
/*     */       {
/* 133 */         xctxt.popCurrentNode();
/* 134 */         xctxt.popContextNodeList();
/* 135 */         return (0.0D / 0.0D);
/*     */       }
/*     */ 
/* 138 */       xctxt.popCurrentNode();
/*     */ 
/* 140 */       if (result > maxValue) {
/* 141 */         maxValue = result;
/*     */       }
/*     */     }
/* 144 */     xctxt.popContextNodeList();
/* 145 */     return maxValue;
/*     */   }
/*     */ 
/*     */   public static double min(ExpressionContext myContext, NodeList nl, String expr)
/*     */     throws SAXNotSupportedException
/*     */   {
/* 186 */     XPathContext xctxt = null;
/* 187 */     if ((myContext instanceof XPathContext.XPathExpressionContext))
/* 188 */       xctxt = ((XPathContext.XPathExpressionContext)myContext).getXPathContext();
/*     */     else {
/* 190 */       throw new SAXNotSupportedException(XSLMessages.createMessage("ER_INVALID_CONTEXT_PASSED", new Object[] { myContext }));
/*     */     }
/* 192 */     if ((expr == null) || (expr.length() == 0)) {
/* 193 */       return (0.0D / 0.0D);
/*     */     }
/* 195 */     NodeSetDTM contextNodes = new NodeSetDTM(nl, xctxt);
/* 196 */     xctxt.pushContextNodeList(contextNodes);
/*     */ 
/* 198 */     double minValue = 1.7976931348623157E+308D;
/* 199 */     for (int i = 0; i < nl.getLength(); i++)
/*     */     {
/* 201 */       int contextNode = contextNodes.item(i);
/* 202 */       xctxt.pushCurrentNode(contextNode);
/*     */ 
/* 204 */       double result = 0.0D;
/*     */       try
/*     */       {
/* 207 */         XPath dynamicXPath = new XPath(expr, xctxt.getSAXLocator(), xctxt.getNamespaceContext(), 0);
/*     */ 
/* 210 */         result = dynamicXPath.execute(xctxt, contextNode, xctxt.getNamespaceContext()).num();
/*     */       }
/*     */       catch (TransformerException e)
/*     */       {
/* 214 */         xctxt.popCurrentNode();
/* 215 */         xctxt.popContextNodeList();
/* 216 */         return (0.0D / 0.0D);
/*     */       }
/*     */ 
/* 219 */       xctxt.popCurrentNode();
/*     */ 
/* 221 */       if (result < minValue) {
/* 222 */         minValue = result;
/*     */       }
/*     */     }
/* 225 */     xctxt.popContextNodeList();
/* 226 */     return minValue;
/*     */   }
/*     */ 
/*     */   public static double sum(ExpressionContext myContext, NodeList nl, String expr)
/*     */     throws SAXNotSupportedException
/*     */   {
/* 266 */     XPathContext xctxt = null;
/* 267 */     if ((myContext instanceof XPathContext.XPathExpressionContext))
/* 268 */       xctxt = ((XPathContext.XPathExpressionContext)myContext).getXPathContext();
/*     */     else {
/* 270 */       throw new SAXNotSupportedException(XSLMessages.createMessage("ER_INVALID_CONTEXT_PASSED", new Object[] { myContext }));
/*     */     }
/* 272 */     if ((expr == null) || (expr.length() == 0)) {
/* 273 */       return (0.0D / 0.0D);
/*     */     }
/* 275 */     NodeSetDTM contextNodes = new NodeSetDTM(nl, xctxt);
/* 276 */     xctxt.pushContextNodeList(contextNodes);
/*     */ 
/* 278 */     double sum = 0.0D;
/* 279 */     for (int i = 0; i < nl.getLength(); i++)
/*     */     {
/* 281 */       int contextNode = contextNodes.item(i);
/* 282 */       xctxt.pushCurrentNode(contextNode);
/*     */ 
/* 284 */       double result = 0.0D;
/*     */       try
/*     */       {
/* 287 */         XPath dynamicXPath = new XPath(expr, xctxt.getSAXLocator(), xctxt.getNamespaceContext(), 0);
/*     */ 
/* 290 */         result = dynamicXPath.execute(xctxt, contextNode, xctxt.getNamespaceContext()).num();
/*     */       }
/*     */       catch (TransformerException e)
/*     */       {
/* 294 */         xctxt.popCurrentNode();
/* 295 */         xctxt.popContextNodeList();
/* 296 */         return (0.0D / 0.0D);
/*     */       }
/*     */ 
/* 299 */       xctxt.popCurrentNode();
/*     */ 
/* 301 */       sum += result;
/*     */     }
/*     */ 
/* 305 */     xctxt.popContextNodeList();
/* 306 */     return sum;
/*     */   }
/*     */ 
/*     */   public static NodeList map(ExpressionContext myContext, NodeList nl, String expr)
/*     */     throws SAXNotSupportedException
/*     */   {
/* 368 */     XPathContext xctxt = null;
/* 369 */     Document lDoc = null;
/*     */ 
/* 371 */     if ((myContext instanceof XPathContext.XPathExpressionContext))
/* 372 */       xctxt = ((XPathContext.XPathExpressionContext)myContext).getXPathContext();
/*     */     else {
/* 374 */       throw new SAXNotSupportedException(XSLMessages.createMessage("ER_INVALID_CONTEXT_PASSED", new Object[] { myContext }));
/*     */     }
/* 376 */     if ((expr == null) || (expr.length() == 0)) {
/* 377 */       return new NodeSet();
/*     */     }
/* 379 */     NodeSetDTM contextNodes = new NodeSetDTM(nl, xctxt);
/* 380 */     xctxt.pushContextNodeList(contextNodes);
/*     */ 
/* 382 */     NodeSet resultSet = new NodeSet();
/* 383 */     resultSet.setShouldCacheNodes(true);
/*     */ 
/* 385 */     for (int i = 0; i < nl.getLength(); i++)
/*     */     {
/* 387 */       int contextNode = contextNodes.item(i);
/* 388 */       xctxt.pushCurrentNode(contextNode);
/*     */ 
/* 390 */       XObject object = null;
/*     */       try
/*     */       {
/* 393 */         XPath dynamicXPath = new XPath(expr, xctxt.getSAXLocator(), xctxt.getNamespaceContext(), 0);
/*     */ 
/* 396 */         object = dynamicXPath.execute(xctxt, contextNode, xctxt.getNamespaceContext());
/*     */ 
/* 398 */         if ((object instanceof XNodeSet))
/*     */         {
/* 400 */           NodeList nodelist = null;
/* 401 */           nodelist = ((XNodeSet)object).nodelist();
/*     */ 
/* 403 */           for (int k = 0; k < nodelist.getLength(); k++)
/*     */           {
/* 405 */             Node n = nodelist.item(k);
/* 406 */             if (!resultSet.contains(n))
/* 407 */               resultSet.addNode(n);
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 412 */           if (lDoc == null)
/*     */           {
/* 414 */             DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
/* 415 */             dbf.setNamespaceAware(true);
/* 416 */             DocumentBuilder db = dbf.newDocumentBuilder();
/* 417 */             lDoc = db.newDocument();
/*     */           }
/*     */ 
/* 420 */           Element element = null;
/* 421 */           if ((object instanceof XNumber))
/* 422 */             element = lDoc.createElementNS("http://exslt.org/common", "exsl:number");
/* 423 */           else if ((object instanceof XBoolean))
/* 424 */             element = lDoc.createElementNS("http://exslt.org/common", "exsl:boolean");
/*     */           else {
/* 426 */             element = lDoc.createElementNS("http://exslt.org/common", "exsl:string");
/*     */           }
/* 428 */           Text textNode = lDoc.createTextNode(object.str());
/* 429 */           element.appendChild(textNode);
/* 430 */           resultSet.addNode(element);
/*     */         }
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/* 435 */         xctxt.popCurrentNode();
/* 436 */         xctxt.popContextNodeList();
/* 437 */         return new NodeSet();
/*     */       }
/*     */ 
/* 440 */       xctxt.popCurrentNode();
/*     */     }
/*     */ 
/* 444 */     xctxt.popContextNodeList();
/* 445 */     return resultSet;
/*     */   }
/*     */ 
/*     */   public static XObject evaluate(ExpressionContext myContext, String xpathExpr)
/*     */     throws SAXNotSupportedException
/*     */   {
/* 467 */     if ((myContext instanceof XPathContext.XPathExpressionContext))
/*     */     {
/* 469 */       XPathContext xctxt = null;
/*     */       try
/*     */       {
/* 472 */         xctxt = ((XPathContext.XPathExpressionContext)myContext).getXPathContext();
/* 473 */         XPath dynamicXPath = new XPath(xpathExpr, xctxt.getSAXLocator(), xctxt.getNamespaceContext(), 0);
/*     */ 
/* 477 */         return dynamicXPath.execute(xctxt, myContext.getContextNode(), xctxt.getNamespaceContext());
/*     */       }
/*     */       catch (TransformerException e)
/*     */       {
/* 482 */         return new XNodeSet(xctxt.getDTMManager());
/*     */       }
/*     */     }
/*     */ 
/* 486 */     throw new SAXNotSupportedException(XSLMessages.createMessage("ER_INVALID_CONTEXT_PASSED", new Object[] { myContext }));
/*     */   }
/*     */ 
/*     */   public static NodeList closure(ExpressionContext myContext, NodeList nl, String expr)
/*     */     throws SAXNotSupportedException
/*     */   {
/* 535 */     XPathContext xctxt = null;
/* 536 */     if ((myContext instanceof XPathContext.XPathExpressionContext))
/* 537 */       xctxt = ((XPathContext.XPathExpressionContext)myContext).getXPathContext();
/*     */     else {
/* 539 */       throw new SAXNotSupportedException(XSLMessages.createMessage("ER_INVALID_CONTEXT_PASSED", new Object[] { myContext }));
/*     */     }
/* 541 */     if ((expr == null) || (expr.length() == 0)) {
/* 542 */       return new NodeSet();
/*     */     }
/* 544 */     NodeSet closureSet = new NodeSet();
/* 545 */     closureSet.setShouldCacheNodes(true);
/*     */ 
/* 547 */     NodeList iterationList = nl;
/*     */     do
/*     */     {
/* 551 */       NodeSet iterationSet = new NodeSet();
/*     */ 
/* 553 */       NodeSetDTM contextNodes = new NodeSetDTM(iterationList, xctxt);
/* 554 */       xctxt.pushContextNodeList(contextNodes);
/*     */ 
/* 556 */       for (int i = 0; i < iterationList.getLength(); i++)
/*     */       {
/* 558 */         int contextNode = contextNodes.item(i);
/* 559 */         xctxt.pushCurrentNode(contextNode);
/*     */ 
/* 561 */         XObject object = null;
/*     */         try
/*     */         {
/* 564 */           XPath dynamicXPath = new XPath(expr, xctxt.getSAXLocator(), xctxt.getNamespaceContext(), 0);
/*     */ 
/* 567 */           object = dynamicXPath.execute(xctxt, contextNode, xctxt.getNamespaceContext());
/*     */ 
/* 569 */           if ((object instanceof XNodeSet))
/*     */           {
/* 571 */             NodeList nodelist = null;
/* 572 */             nodelist = ((XNodeSet)object).nodelist();
/*     */ 
/* 574 */             for (int k = 0; k < nodelist.getLength(); k++)
/*     */             {
/* 576 */               Node n = nodelist.item(k);
/* 577 */               if (!iterationSet.contains(n))
/* 578 */                 iterationSet.addNode(n);
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/* 583 */             xctxt.popCurrentNode();
/* 584 */             xctxt.popContextNodeList();
/* 585 */             return new NodeSet();
/*     */           }
/*     */         }
/*     */         catch (TransformerException e)
/*     */         {
/* 590 */           xctxt.popCurrentNode();
/* 591 */           xctxt.popContextNodeList();
/* 592 */           return new NodeSet();
/*     */         }
/*     */ 
/* 595 */         xctxt.popCurrentNode();
/*     */       }
/*     */ 
/* 599 */       xctxt.popContextNodeList();
/*     */ 
/* 601 */       iterationList = iterationSet;
/*     */ 
/* 603 */       for (int i = 0; i < iterationList.getLength(); i++)
/*     */       {
/* 605 */         Node n = iterationList.item(i);
/* 606 */         if (!closureSet.contains(n))
/* 607 */           closureSet.addNode(n);
/*     */       }
/*     */     }
/* 610 */     while (iterationList.getLength() > 0);
/*     */ 
/* 612 */     return closureSet;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.lib.ExsltDynamic
 * JD-Core Version:    0.6.2
 */