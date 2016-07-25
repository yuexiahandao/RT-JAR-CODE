/*     */ package com.sun.org.apache.xml.internal.serializer.utils;
/*     */ 
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.Attributes;
/*     */ 
/*     */ public final class AttList
/*     */   implements Attributes
/*     */ {
/*     */   NamedNodeMap m_attrs;
/*     */   int m_lastIndex;
/*     */   DOM2Helper m_dh;
/*     */ 
/*     */   public AttList(NamedNodeMap attrs, DOM2Helper dh)
/*     */   {
/*  83 */     this.m_attrs = attrs;
/*  84 */     this.m_lastIndex = (this.m_attrs.getLength() - 1);
/*  85 */     this.m_dh = dh;
/*     */   }
/*     */ 
/*     */   public int getLength()
/*     */   {
/*  96 */     return this.m_attrs.getLength();
/*     */   }
/*     */ 
/*     */   public String getURI(int index)
/*     */   {
/* 109 */     String ns = this.m_dh.getNamespaceOfNode((Attr)this.m_attrs.item(index));
/* 110 */     if (null == ns)
/* 111 */       ns = "";
/* 112 */     return ns;
/*     */   }
/*     */ 
/*     */   public String getLocalName(int index)
/*     */   {
/* 125 */     return this.m_dh.getLocalNameOfNode((Attr)this.m_attrs.item(index));
/*     */   }
/*     */ 
/*     */   public String getQName(int i)
/*     */   {
/* 138 */     return ((Attr)this.m_attrs.item(i)).getName();
/*     */   }
/*     */ 
/*     */   public String getType(int i)
/*     */   {
/* 151 */     return "CDATA";
/*     */   }
/*     */ 
/*     */   public String getValue(int i)
/*     */   {
/* 164 */     return ((Attr)this.m_attrs.item(i)).getValue();
/*     */   }
/*     */ 
/*     */   public String getType(String name)
/*     */   {
/* 177 */     return "CDATA";
/*     */   }
/*     */ 
/*     */   public String getType(String uri, String localName)
/*     */   {
/* 192 */     return "CDATA";
/*     */   }
/*     */ 
/*     */   public String getValue(String name)
/*     */   {
/* 205 */     Attr attr = (Attr)this.m_attrs.getNamedItem(name);
/* 206 */     return null != attr ? attr.getValue() : null;
/*     */   }
/*     */ 
/*     */   public String getValue(String uri, String localName)
/*     */   {
/* 221 */     Node a = this.m_attrs.getNamedItemNS(uri, localName);
/* 222 */     return a == null ? null : a.getNodeValue();
/*     */   }
/*     */ 
/*     */   public int getIndex(String uri, String localPart)
/*     */   {
/* 236 */     for (int i = this.m_attrs.getLength() - 1; i >= 0; i--)
/*     */     {
/* 238 */       Node a = this.m_attrs.item(i);
/* 239 */       String u = a.getNamespaceURI();
/* 240 */       if ((u == null ? uri == null : u.equals(uri)) && (a.getLocalName().equals(localPart)))
/*     */       {
/* 243 */         return i;
/*     */       }
/*     */     }
/* 245 */     return -1;
/*     */   }
/*     */ 
/*     */   public int getIndex(String qName)
/*     */   {
/* 257 */     for (int i = this.m_attrs.getLength() - 1; i >= 0; i--)
/*     */     {
/* 259 */       Node a = this.m_attrs.item(i);
/* 260 */       if (a.getNodeName().equals(qName))
/* 261 */         return i;
/*     */     }
/* 263 */     return -1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.serializer.utils.AttList
 * JD-Core Version:    0.6.2
 */