/*     */ package com.sun.xml.internal.bind.v2.runtime.output;
/*     */ 
/*     */ import com.sun.xml.internal.bind.v2.runtime.Name;
/*     */ import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
/*     */ import java.io.IOException;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public abstract class XmlOutputAbstractImpl
/*     */   implements XmlOutput
/*     */ {
/*     */   protected int[] nsUriIndex2prefixIndex;
/*     */   protected NamespaceContextImpl nsContext;
/*     */   protected XMLSerializer serializer;
/*     */ 
/*     */   public void startDocument(XMLSerializer serializer, boolean fragment, int[] nsUriIndex2prefixIndex, NamespaceContextImpl nsContext)
/*     */     throws IOException, SAXException, XMLStreamException
/*     */   {
/*  61 */     this.nsUriIndex2prefixIndex = nsUriIndex2prefixIndex;
/*  62 */     this.nsContext = nsContext;
/*  63 */     this.serializer = serializer;
/*     */   }
/*     */ 
/*     */   public void endDocument(boolean fragment)
/*     */     throws IOException, SAXException, XMLStreamException
/*     */   {
/*  73 */     this.serializer = null;
/*     */   }
/*     */ 
/*     */   public void beginStartTag(Name name)
/*     */     throws IOException, XMLStreamException
/*     */   {
/*  87 */     beginStartTag(this.nsUriIndex2prefixIndex[name.nsUriIndex], name.localName);
/*     */   }
/*     */ 
/*     */   public abstract void beginStartTag(int paramInt, String paramString) throws IOException, XMLStreamException;
/*     */ 
/*     */   public void attribute(Name name, String value) throws IOException, XMLStreamException {
/*  93 */     short idx = name.nsUriIndex;
/*  94 */     if (idx == -1)
/*  95 */       attribute(-1, name.localName, value);
/*     */     else
/*  97 */       attribute(this.nsUriIndex2prefixIndex[idx], name.localName, value);
/*     */   }
/*     */ 
/*     */   public abstract void attribute(int paramInt, String paramString1, String paramString2)
/*     */     throws IOException, XMLStreamException;
/*     */ 
/*     */   public abstract void endStartTag()
/*     */     throws IOException, SAXException;
/*     */ 
/*     */   public void endTag(Name name)
/*     */     throws IOException, SAXException, XMLStreamException
/*     */   {
/* 109 */     endTag(this.nsUriIndex2prefixIndex[name.nsUriIndex], name.localName);
/*     */   }
/*     */ 
/*     */   public abstract void endTag(int paramInt, String paramString)
/*     */     throws IOException, SAXException, XMLStreamException;
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.output.XmlOutputAbstractImpl
 * JD-Core Version:    0.6.2
 */