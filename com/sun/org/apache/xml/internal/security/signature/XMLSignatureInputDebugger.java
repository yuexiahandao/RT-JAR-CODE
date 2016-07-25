/*     */ package com.sun.org.apache.xml.internal.security.signature;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.c14n.helper.AttrCompare;
/*     */ import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
/*     */ import java.io.IOException;
/*     */ import java.io.StringWriter;
/*     */ import java.io.Writer;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Comment;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.ProcessingInstruction;
/*     */ 
/*     */ public class XMLSignatureInputDebugger
/*     */ {
/*     */   private Set _xpathNodeSet;
/*     */   private Set _inclusiveNamespaces;
/*  55 */   private Document _doc = null;
/*     */ 
/*  58 */   private Writer _writer = null;
/*     */   static final String HTMLPrefix = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">\n<html>\n<head>\n<title>Caninical XML node set</title>\n<style type=\"text/css\">\n<!-- \n.INCLUDED { \n   color: #000000; \n   background-color: \n   #FFFFFF; \n   font-weight: bold; } \n.EXCLUDED { \n   color: #666666; \n   background-color: \n   #999999; } \n.INCLUDEDINCLUSIVENAMESPACE { \n   color: #0000FF; \n   background-color: #FFFFFF; \n   font-weight: bold; \n   font-style: italic; } \n.EXCLUDEDINCLUSIVENAMESPACE { \n   color: #0000FF; \n   background-color: #999999; \n   font-style: italic; } \n--> \n</style> \n</head>\n<body bgcolor=\"#999999\">\n<h1>Explanation of the output</h1>\n<p>The following text contains the nodeset of the given Reference before it is canonicalized. There exist four different styles to indicate how a given node is treated.</p>\n<ul>\n<li class=\"INCLUDED\">A node which is in the node set is labeled using the INCLUDED style.</li>\n<li class=\"EXCLUDED\">A node which is <em>NOT</em> in the node set is labeled EXCLUDED style.</li>\n<li class=\"INCLUDEDINCLUSIVENAMESPACE\">A namespace which is in the node set AND in the InclusiveNamespaces PrefixList is labeled using the INCLUDEDINCLUSIVENAMESPACE style.</li>\n<li class=\"EXCLUDEDINCLUSIVENAMESPACE\">A namespace which is in NOT the node set AND in the InclusiveNamespaces PrefixList is labeled using the INCLUDEDINCLUSIVENAMESPACE style.</li>\n</ul>\n<h1>Output</h1>\n<pre>\n";
/*     */   static final String HTMLSuffix = "</pre></body></html>";
/*     */   static final String HTMLExcludePrefix = "<span class=\"EXCLUDED\">";
/*     */   static final String HTMLExcludeSuffix = "</span>";
/*     */   static final String HTMLIncludePrefix = "<span class=\"INCLUDED\">";
/*     */   static final String HTMLIncludeSuffix = "</span>";
/*     */   static final String HTMLIncludedInclusiveNamespacePrefix = "<span class=\"INCLUDEDINCLUSIVENAMESPACE\">";
/*     */   static final String HTMLIncludedInclusiveNamespaceSuffix = "</span>";
/*     */   static final String HTMLExcludedInclusiveNamespacePrefix = "<span class=\"EXCLUDEDINCLUSIVENAMESPACE\">";
/*     */   static final String HTMLExcludedInclusiveNamespaceSuffix = "</span>";
/*     */   private static final int NODE_BEFORE_DOCUMENT_ELEMENT = -1;
/*     */   private static final int NODE_NOT_BEFORE_OR_AFTER_DOCUMENT_ELEMENT = 0;
/*     */   private static final int NODE_AFTER_DOCUMENT_ELEMENT = 1;
/* 133 */   static final AttrCompare ATTR_COMPARE = new AttrCompare();
/*     */ 
/*     */   private XMLSignatureInputDebugger()
/*     */   {
/*     */   }
/*     */ 
/*     */   public XMLSignatureInputDebugger(XMLSignatureInput paramXMLSignatureInput)
/*     */   {
/* 148 */     if (!paramXMLSignatureInput.isNodeSet())
/* 149 */       this._xpathNodeSet = null;
/*     */     else
/* 151 */       this._xpathNodeSet = paramXMLSignatureInput._inputNodeSet;
/*     */   }
/*     */ 
/*     */   public XMLSignatureInputDebugger(XMLSignatureInput paramXMLSignatureInput, Set paramSet)
/*     */   {
/* 164 */     this(paramXMLSignatureInput);
/*     */ 
/* 166 */     this._inclusiveNamespaces = paramSet;
/*     */   }
/*     */ 
/*     */   public String getHTMLRepresentation()
/*     */     throws XMLSignatureException
/*     */   {
/* 177 */     if ((this._xpathNodeSet == null) || (this._xpathNodeSet.size() == 0)) {
/* 178 */       return "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">\n<html>\n<head>\n<title>Caninical XML node set</title>\n<style type=\"text/css\">\n<!-- \n.INCLUDED { \n   color: #000000; \n   background-color: \n   #FFFFFF; \n   font-weight: bold; } \n.EXCLUDED { \n   color: #666666; \n   background-color: \n   #999999; } \n.INCLUDEDINCLUSIVENAMESPACE { \n   color: #0000FF; \n   background-color: #FFFFFF; \n   font-weight: bold; \n   font-style: italic; } \n.EXCLUDEDINCLUSIVENAMESPACE { \n   color: #0000FF; \n   background-color: #999999; \n   font-style: italic; } \n--> \n</style> \n</head>\n<body bgcolor=\"#999999\">\n<h1>Explanation of the output</h1>\n<p>The following text contains the nodeset of the given Reference before it is canonicalized. There exist four different styles to indicate how a given node is treated.</p>\n<ul>\n<li class=\"INCLUDED\">A node which is in the node set is labeled using the INCLUDED style.</li>\n<li class=\"EXCLUDED\">A node which is <em>NOT</em> in the node set is labeled EXCLUDED style.</li>\n<li class=\"INCLUDEDINCLUSIVENAMESPACE\">A namespace which is in the node set AND in the InclusiveNamespaces PrefixList is labeled using the INCLUDEDINCLUSIVENAMESPACE style.</li>\n<li class=\"EXCLUDEDINCLUSIVENAMESPACE\">A namespace which is in NOT the node set AND in the InclusiveNamespaces PrefixList is labeled using the INCLUDEDINCLUSIVENAMESPACE style.</li>\n</ul>\n<h1>Output</h1>\n<pre>\n<blink>no node set, sorry</blink></pre></body></html>";
/*     */     }
/*     */ 
/* 185 */     Object localObject1 = (Node)this._xpathNodeSet.iterator().next();
/*     */ 
/* 187 */     this._doc = XMLUtils.getOwnerDocument((Node)localObject1);
/*     */     try
/*     */     {
/* 191 */       this._writer = new StringWriter();
/*     */ 
/* 193 */       canonicalizeXPathNodeSet(this._doc);
/* 194 */       this._writer.close();
/*     */ 
/* 196 */       return this._writer.toString();
/*     */     } catch (IOException localIOException) {
/* 198 */       throw new XMLSignatureException("empty", localIOException);
/*     */     } finally {
/* 200 */       this._xpathNodeSet = null;
/* 201 */       this._doc = null;
/* 202 */       this._writer = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void canonicalizeXPathNodeSet(Node paramNode)
/*     */     throws XMLSignatureException, IOException
/*     */   {
/* 216 */     int i = paramNode.getNodeType();
/*     */     int j;
/*     */     Object localObject;
/* 217 */     switch (i) {
/*     */     case 5:
/*     */     case 10:
/*     */     default:
/* 221 */       break;
/*     */     case 2:
/*     */     case 6:
/*     */     case 11:
/*     */     case 12:
/* 227 */       throw new XMLSignatureException("empty");
/*     */     case 9:
/* 229 */       this._writer.write("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">\n<html>\n<head>\n<title>Caninical XML node set</title>\n<style type=\"text/css\">\n<!-- \n.INCLUDED { \n   color: #000000; \n   background-color: \n   #FFFFFF; \n   font-weight: bold; } \n.EXCLUDED { \n   color: #666666; \n   background-color: \n   #999999; } \n.INCLUDEDINCLUSIVENAMESPACE { \n   color: #0000FF; \n   background-color: #FFFFFF; \n   font-weight: bold; \n   font-style: italic; } \n.EXCLUDEDINCLUSIVENAMESPACE { \n   color: #0000FF; \n   background-color: #999999; \n   font-style: italic; } \n--> \n</style> \n</head>\n<body bgcolor=\"#999999\">\n<h1>Explanation of the output</h1>\n<p>The following text contains the nodeset of the given Reference before it is canonicalized. There exist four different styles to indicate how a given node is treated.</p>\n<ul>\n<li class=\"INCLUDED\">A node which is in the node set is labeled using the INCLUDED style.</li>\n<li class=\"EXCLUDED\">A node which is <em>NOT</em> in the node set is labeled EXCLUDED style.</li>\n<li class=\"INCLUDEDINCLUSIVENAMESPACE\">A namespace which is in the node set AND in the InclusiveNamespaces PrefixList is labeled using the INCLUDEDINCLUSIVENAMESPACE style.</li>\n<li class=\"EXCLUDEDINCLUSIVENAMESPACE\">A namespace which is in NOT the node set AND in the InclusiveNamespaces PrefixList is labeled using the INCLUDEDINCLUSIVENAMESPACE style.</li>\n</ul>\n<h1>Output</h1>\n<pre>\n");
/*     */ 
/* 231 */       for (Node localNode1 = paramNode.getFirstChild(); localNode1 != null; localNode1 = localNode1.getNextSibling())
/*     */       {
/* 233 */         canonicalizeXPathNodeSet(localNode1);
/*     */       }
/*     */ 
/* 236 */       this._writer.write("</pre></body></html>");
/* 237 */       break;
/*     */     case 8:
/* 240 */       if (this._xpathNodeSet.contains(paramNode))
/* 241 */         this._writer.write("<span class=\"INCLUDED\">");
/*     */       else {
/* 243 */         this._writer.write("<span class=\"EXCLUDED\">");
/*     */       }
/*     */ 
/* 246 */       j = getPositionRelativeToDocumentElement(paramNode);
/*     */ 
/* 248 */       if (j == 1) {
/* 249 */         this._writer.write("\n");
/*     */       }
/*     */ 
/* 252 */       outputCommentToWriter((Comment)paramNode);
/*     */ 
/* 254 */       if (j == -1) {
/* 255 */         this._writer.write("\n");
/*     */       }
/*     */ 
/* 258 */       if (this._xpathNodeSet.contains(paramNode))
/* 259 */         this._writer.write("</span>");
/*     */       else {
/* 261 */         this._writer.write("</span>");
/*     */       }
/* 263 */       break;
/*     */     case 7:
/* 266 */       if (this._xpathNodeSet.contains(paramNode))
/* 267 */         this._writer.write("<span class=\"INCLUDED\">");
/*     */       else {
/* 269 */         this._writer.write("<span class=\"EXCLUDED\">");
/*     */       }
/*     */ 
/* 272 */       j = getPositionRelativeToDocumentElement(paramNode);
/*     */ 
/* 274 */       if (j == 1) {
/* 275 */         this._writer.write("\n");
/*     */       }
/*     */ 
/* 278 */       outputPItoWriter((ProcessingInstruction)paramNode);
/*     */ 
/* 280 */       if (j == -1) {
/* 281 */         this._writer.write("\n");
/*     */       }
/*     */ 
/* 284 */       if (this._xpathNodeSet.contains(paramNode))
/* 285 */         this._writer.write("</span>");
/*     */       else {
/* 287 */         this._writer.write("</span>");
/*     */       }
/* 289 */       break;
/*     */     case 3:
/*     */     case 4:
/* 293 */       if (this._xpathNodeSet.contains(paramNode))
/* 294 */         this._writer.write("<span class=\"INCLUDED\">");
/*     */       else {
/* 296 */         this._writer.write("<span class=\"EXCLUDED\">");
/*     */       }
/*     */ 
/* 299 */       outputTextToWriter(paramNode.getNodeValue());
/*     */ 
/* 301 */       for (localObject = paramNode.getNextSibling(); 
/* 302 */         (localObject != null) && ((((Node)localObject).getNodeType() == 3) || (((Node)localObject).getNodeType() == 4)); 
/* 303 */         localObject = ((Node)localObject).getNextSibling())
/*     */       {
/* 313 */         outputTextToWriter(((Node)localObject).getNodeValue());
/*     */       }
/*     */ 
/* 316 */       if (this._xpathNodeSet.contains(paramNode))
/* 317 */         this._writer.write("</span>");
/*     */       else {
/* 319 */         this._writer.write("</span>");
/*     */       }
/* 321 */       break;
/*     */     case 1:
/* 324 */       localObject = (Element)paramNode;
/*     */ 
/* 326 */       if (this._xpathNodeSet.contains(paramNode))
/* 327 */         this._writer.write("<span class=\"INCLUDED\">");
/*     */       else {
/* 329 */         this._writer.write("<span class=\"EXCLUDED\">");
/*     */       }
/*     */ 
/* 332 */       this._writer.write("&lt;");
/* 333 */       this._writer.write(((Element)localObject).getTagName());
/*     */ 
/* 335 */       if (this._xpathNodeSet.contains(paramNode))
/* 336 */         this._writer.write("</span>");
/*     */       else {
/* 338 */         this._writer.write("</span>");
/*     */       }
/*     */ 
/* 342 */       NamedNodeMap localNamedNodeMap = ((Element)localObject).getAttributes();
/* 343 */       int k = localNamedNodeMap.getLength();
/* 344 */       Object[] arrayOfObject1 = new Object[k];
/*     */ 
/* 346 */       for (int m = 0; m < k; m++) {
/* 347 */         arrayOfObject1[m] = localNamedNodeMap.item(m);
/*     */       }
/*     */ 
/* 350 */       Arrays.sort(arrayOfObject1, ATTR_COMPARE);
/* 351 */       Object[] arrayOfObject2 = arrayOfObject1;
/*     */ 
/* 353 */       for (int n = 0; n < k; n++) {
/* 354 */         Attr localAttr = (Attr)arrayOfObject2[n];
/* 355 */         boolean bool1 = this._xpathNodeSet.contains(localAttr);
/* 356 */         boolean bool2 = this._inclusiveNamespaces.contains(localAttr.getName());
/*     */ 
/* 359 */         if (bool1) {
/* 360 */           if (bool2)
/*     */           {
/* 363 */             this._writer.write("<span class=\"INCLUDEDINCLUSIVENAMESPACE\">");
/*     */           }
/*     */           else
/*     */           {
/* 368 */             this._writer.write("<span class=\"INCLUDED\">");
/*     */           }
/*     */         }
/* 371 */         else if (bool2)
/*     */         {
/* 374 */           this._writer.write("<span class=\"EXCLUDEDINCLUSIVENAMESPACE\">");
/*     */         }
/*     */         else
/*     */         {
/* 379 */           this._writer.write("<span class=\"EXCLUDED\">");
/*     */         }
/*     */ 
/* 383 */         outputAttrToWriter(localAttr.getNodeName(), localAttr.getNodeValue());
/*     */ 
/* 385 */         if (bool1) {
/* 386 */           if (bool2)
/*     */           {
/* 389 */             this._writer.write("</span>");
/*     */           }
/*     */           else
/*     */           {
/* 394 */             this._writer.write("</span>");
/*     */           }
/*     */         }
/* 397 */         else if (bool2)
/*     */         {
/* 400 */           this._writer.write("</span>");
/*     */         }
/*     */         else
/*     */         {
/* 405 */           this._writer.write("</span>");
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 410 */       if (this._xpathNodeSet.contains(paramNode))
/* 411 */         this._writer.write("<span class=\"INCLUDED\">");
/*     */       else {
/* 413 */         this._writer.write("<span class=\"EXCLUDED\">");
/*     */       }
/*     */ 
/* 416 */       this._writer.write("&gt;");
/*     */ 
/* 418 */       if (this._xpathNodeSet.contains(paramNode))
/* 419 */         this._writer.write("</span>");
/*     */       else {
/* 421 */         this._writer.write("</span>");
/*     */       }
/*     */ 
/* 425 */       for (Node localNode2 = paramNode.getFirstChild(); localNode2 != null; localNode2 = localNode2.getNextSibling())
/*     */       {
/* 427 */         canonicalizeXPathNodeSet(localNode2);
/*     */       }
/*     */ 
/* 430 */       if (this._xpathNodeSet.contains(paramNode))
/* 431 */         this._writer.write("<span class=\"INCLUDED\">");
/*     */       else {
/* 433 */         this._writer.write("<span class=\"EXCLUDED\">");
/*     */       }
/*     */ 
/* 436 */       this._writer.write("&lt;/");
/* 437 */       this._writer.write(((Element)localObject).getTagName());
/* 438 */       this._writer.write("&gt;");
/*     */ 
/* 440 */       if (this._xpathNodeSet.contains(paramNode))
/* 441 */         this._writer.write("</span>");
/*     */       else
/* 443 */         this._writer.write("</span>");
/*     */       break;
/*     */     }
/*     */   }
/*     */ 
/*     */   private int getPositionRelativeToDocumentElement(Node paramNode)
/*     */   {
/* 464 */     if (paramNode == null) {
/* 465 */       return 0;
/*     */     }
/*     */ 
/* 468 */     Document localDocument = paramNode.getOwnerDocument();
/*     */ 
/* 470 */     if (paramNode.getParentNode() != localDocument) {
/* 471 */       return 0;
/*     */     }
/*     */ 
/* 474 */     Element localElement = localDocument.getDocumentElement();
/*     */ 
/* 476 */     if (localElement == null) {
/* 477 */       return 0;
/*     */     }
/*     */ 
/* 480 */     if (localElement == paramNode) {
/* 481 */       return 0;
/*     */     }
/*     */ 
/* 484 */     for (Node localNode = paramNode; localNode != null; localNode = localNode.getNextSibling()) {
/* 485 */       if (localNode == localElement) {
/* 486 */         return -1;
/*     */       }
/*     */     }
/*     */ 
/* 490 */     return 1;
/*     */   }
/*     */ 
/*     */   private void outputAttrToWriter(String paramString1, String paramString2)
/*     */     throws IOException
/*     */   {
/* 514 */     this._writer.write(" ");
/* 515 */     this._writer.write(paramString1);
/* 516 */     this._writer.write("=\"");
/*     */ 
/* 518 */     int i = paramString2.length();
/*     */ 
/* 520 */     for (int j = 0; j < i; j++) {
/* 521 */       int k = paramString2.charAt(j);
/*     */ 
/* 523 */       switch (k)
/*     */       {
/*     */       case 38:
/* 526 */         this._writer.write("&amp;amp;");
/* 527 */         break;
/*     */       case 60:
/* 530 */         this._writer.write("&amp;lt;");
/* 531 */         break;
/*     */       case 34:
/* 534 */         this._writer.write("&amp;quot;");
/* 535 */         break;
/*     */       case 9:
/* 538 */         this._writer.write("&amp;#x9;");
/* 539 */         break;
/*     */       case 10:
/* 542 */         this._writer.write("&amp;#xA;");
/* 543 */         break;
/*     */       case 13:
/* 546 */         this._writer.write("&amp;#xD;");
/* 547 */         break;
/*     */       default:
/* 550 */         this._writer.write(k);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 555 */     this._writer.write("\"");
/*     */   }
/*     */ 
/*     */   private void outputPItoWriter(ProcessingInstruction paramProcessingInstruction)
/*     */     throws IOException
/*     */   {
/* 567 */     if (paramProcessingInstruction == null) {
/* 568 */       return;
/*     */     }
/*     */ 
/* 571 */     this._writer.write("&lt;?");
/*     */ 
/* 573 */     String str1 = paramProcessingInstruction.getTarget();
/* 574 */     int i = str1.length();
/*     */     int k;
/* 576 */     for (int j = 0; j < i; j++) {
/* 577 */       k = str1.charAt(j);
/*     */ 
/* 579 */       switch (k)
/*     */       {
/*     */       case 13:
/* 582 */         this._writer.write("&amp;#xD;");
/* 583 */         break;
/*     */       case 32:
/* 586 */         this._writer.write("&middot;");
/* 587 */         break;
/*     */       case 10:
/* 590 */         this._writer.write("&para;\n");
/* 591 */         break;
/*     */       default:
/* 594 */         this._writer.write(k);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 599 */     String str2 = paramProcessingInstruction.getData();
/*     */ 
/* 601 */     i = str2.length();
/*     */ 
/* 603 */     if (i > 0) {
/* 604 */       this._writer.write(" ");
/*     */ 
/* 606 */       for (k = 0; k < i; k++) {
/* 607 */         int m = str2.charAt(k);
/*     */ 
/* 609 */         switch (m)
/*     */         {
/*     */         case 13:
/* 612 */           this._writer.write("&amp;#xD;");
/* 613 */           break;
/*     */         default:
/* 616 */           this._writer.write(m);
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 622 */     this._writer.write("?&gt;");
/*     */   }
/*     */ 
/*     */   private void outputCommentToWriter(Comment paramComment)
/*     */     throws IOException
/*     */   {
/* 634 */     if (paramComment == null) {
/* 635 */       return;
/*     */     }
/*     */ 
/* 638 */     this._writer.write("&lt;!--");
/*     */ 
/* 640 */     String str = paramComment.getData();
/* 641 */     int i = str.length();
/*     */ 
/* 643 */     for (int j = 0; j < i; j++) {
/* 644 */       int k = str.charAt(j);
/*     */ 
/* 646 */       switch (k)
/*     */       {
/*     */       case 13:
/* 649 */         this._writer.write("&amp;#xD;");
/* 650 */         break;
/*     */       case 32:
/* 653 */         this._writer.write("&middot;");
/* 654 */         break;
/*     */       case 10:
/* 657 */         this._writer.write("&para;\n");
/* 658 */         break;
/*     */       default:
/* 661 */         this._writer.write(k);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 666 */     this._writer.write("--&gt;");
/*     */   }
/*     */ 
/*     */   private void outputTextToWriter(String paramString)
/*     */     throws IOException
/*     */   {
/* 677 */     if (paramString == null) {
/* 678 */       return;
/*     */     }
/*     */ 
/* 681 */     int i = paramString.length();
/*     */ 
/* 683 */     for (int j = 0; j < i; j++) {
/* 684 */       int k = paramString.charAt(j);
/*     */ 
/* 686 */       switch (k)
/*     */       {
/*     */       case 38:
/* 689 */         this._writer.write("&amp;amp;");
/* 690 */         break;
/*     */       case 60:
/* 693 */         this._writer.write("&amp;lt;");
/* 694 */         break;
/*     */       case 62:
/* 697 */         this._writer.write("&amp;gt;");
/* 698 */         break;
/*     */       case 13:
/* 701 */         this._writer.write("&amp;#xD;");
/* 702 */         break;
/*     */       case 32:
/* 705 */         this._writer.write("&middot;");
/* 706 */         break;
/*     */       case 10:
/* 709 */         this._writer.write("&para;\n");
/* 710 */         break;
/*     */       default:
/* 713 */         this._writer.write(k);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.signature.XMLSignatureInputDebugger
 * JD-Core Version:    0.6.2
 */