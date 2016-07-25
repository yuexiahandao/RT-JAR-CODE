/*     */ package com.sun.org.apache.xml.internal.serialize;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.AttributeList;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class TextSerializer extends BaseMarkupSerializer
/*     */ {
/*     */   public TextSerializer()
/*     */   {
/*  74 */     super(new OutputFormat("text", null, false));
/*     */   }
/*     */ 
/*     */   public void setOutputFormat(OutputFormat format)
/*     */   {
/*  80 */     super.setOutputFormat(format != null ? format : new OutputFormat("text", null, false));
/*     */   }
/*     */ 
/*     */   public void startElement(String namespaceURI, String localName, String rawName, Attributes attrs)
/*     */     throws SAXException
/*     */   {
/*  93 */     startElement(rawName == null ? localName : rawName, null);
/*     */   }
/*     */ 
/*     */   public void endElement(String namespaceURI, String localName, String rawName)
/*     */     throws SAXException
/*     */   {
/* 101 */     endElement(rawName == null ? localName : rawName);
/*     */   }
/*     */ 
/*     */   public void startElement(String tagName, AttributeList attrs)
/*     */     throws SAXException
/*     */   {
/*     */     try
/*     */     {
/* 117 */       ElementState state = getElementState();
/* 118 */       if (isDocumentState())
/*     */       {
/* 123 */         if (!this._started) {
/* 124 */           startDocument(tagName);
/*     */         }
/*     */       }
/*     */ 
/* 128 */       boolean preserveSpace = state.preserveSpace;
/*     */ 
/* 139 */       state = enterElementState(null, null, tagName, preserveSpace);
/*     */     } catch (IOException except) {
/* 141 */       throw new SAXException(except);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void endElement(String tagName)
/*     */     throws SAXException
/*     */   {
/*     */     try
/*     */     {
/* 150 */       endElementIO(tagName);
/*     */     } catch (IOException except) {
/* 152 */       throw new SAXException(except);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void endElementIO(String tagName)
/*     */     throws IOException
/*     */   {
/* 165 */     ElementState state = getElementState();
/*     */ 
/* 168 */     state = leaveElementState();
/* 169 */     state.afterElement = true;
/* 170 */     state.empty = false;
/* 171 */     if (isDocumentState())
/* 172 */       this._printer.flush();
/*     */   }
/*     */ 
/*     */   public void processingInstructionIO(String target, String code)
/*     */     throws IOException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void comment(String text)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void comment(char[] chars, int start, int length)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void characters(char[] chars, int start, int length)
/*     */     throws SAXException
/*     */   {
/*     */     try
/*     */     {
/* 197 */       ElementState state = content();
/* 198 */       state.doCData = (state.inCData = 0);
/* 199 */       printText(chars, start, length, true, true);
/*     */     } catch (IOException except) {
/* 201 */       throw new SAXException(except);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void characters(String text, boolean unescaped)
/*     */     throws IOException
/*     */   {
/* 211 */     ElementState state = content();
/* 212 */     state.doCData = (state.inCData = 0);
/* 213 */     printText(text, true, true);
/*     */   }
/*     */ 
/*     */   protected void startDocument(String rootTagName)
/*     */     throws IOException
/*     */   {
/* 236 */     this._printer.leaveDTD();
/*     */ 
/* 238 */     this._started = true;
/*     */ 
/* 240 */     serializePreRoot();
/*     */   }
/*     */ 
/*     */   protected void serializeElement(Element elem)
/*     */     throws IOException
/*     */   {
/* 257 */     String tagName = elem.getTagName();
/* 258 */     ElementState state = getElementState();
/* 259 */     if (isDocumentState())
/*     */     {
/* 264 */       if (!this._started) {
/* 265 */         startDocument(tagName);
/*     */       }
/*     */     }
/*     */ 
/* 269 */     boolean preserveSpace = state.preserveSpace;
/*     */ 
/* 279 */     if (elem.hasChildNodes())
/*     */     {
/* 282 */       state = enterElementState(null, null, tagName, preserveSpace);
/* 283 */       Node child = elem.getFirstChild();
/* 284 */       while (child != null) {
/* 285 */         serializeNode(child);
/* 286 */         child = child.getNextSibling();
/*     */       }
/* 288 */       endElementIO(tagName);
/*     */     }
/* 290 */     else if (!isDocumentState())
/*     */     {
/* 292 */       state.afterElement = true;
/* 293 */       state.empty = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void serializeNode(Node node)
/*     */     throws IOException
/*     */   {
/* 310 */     switch (node.getNodeType())
/*     */     {
/*     */     case 3:
/* 314 */       String text = node.getNodeValue();
/* 315 */       if (text != null)
/* 316 */         characters(node.getNodeValue(), true); break;
/*     */     case 4:
/* 323 */       String text = node.getNodeValue();
/* 324 */       if (text != null)
/* 325 */         characters(node.getNodeValue(), true); break;
/*     */     case 8:
/* 330 */       break;
/*     */     case 5:
/* 334 */       break;
/*     */     case 7:
/* 337 */       break;
/*     */     case 1:
/* 340 */       serializeElement((Element)node);
/* 341 */       break;
/*     */     case 9:
/*     */     case 11:
/* 351 */       Node child = node.getFirstChild();
/* 352 */       while (child != null) {
/* 353 */         serializeNode(child);
/* 354 */         child = child.getNextSibling();
/*     */       }
/*     */     case 2:
/*     */     case 6:
/*     */     case 10:
/*     */     }
/*     */   }
/*     */ 
/*     */   protected ElementState content()
/*     */   {
/* 369 */     ElementState state = getElementState();
/* 370 */     if (!isDocumentState())
/*     */     {
/* 373 */       if (state.empty) {
/* 374 */         state.empty = false;
/*     */       }
/*     */ 
/* 378 */       state.afterElement = false;
/*     */     }
/* 380 */     return state;
/*     */   }
/*     */ 
/*     */   protected String getEntityRef(int ch)
/*     */   {
/* 386 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.serialize.TextSerializer
 * JD-Core Version:    0.6.2
 */