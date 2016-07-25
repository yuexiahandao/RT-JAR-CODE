/*     */ package com.sun.org.apache.xml.internal.utils;
/*     */ 
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.Attributes;
/*     */ 
/*     */ public class AttList
/*     */   implements Attributes
/*     */ {
/*     */   NamedNodeMap m_attrs;
/*     */   int m_lastIndex;
/*     */   DOMHelper m_dh;
/*     */ 
/*     */   public AttList(NamedNodeMap attrs, DOMHelper dh)
/*     */   {
/*  74 */     this.m_attrs = attrs;
/*  75 */     this.m_lastIndex = (this.m_attrs.getLength() - 1);
/*  76 */     this.m_dh = dh;
/*     */   }
/*     */ 
/*     */   public int getLength()
/*     */   {
/*  87 */     return this.m_attrs.getLength();
/*     */   }
/*     */ 
/*     */   public String getURI(int index)
/*     */   {
/* 100 */     String ns = this.m_dh.getNamespaceOfNode((Attr)this.m_attrs.item(index));
/* 101 */     if (null == ns)
/* 102 */       ns = "";
/* 103 */     return ns;
/*     */   }
/*     */ 
/*     */   public String getLocalName(int index)
/*     */   {
/* 116 */     return this.m_dh.getLocalNameOfNode((Attr)this.m_attrs.item(index));
/*     */   }
/*     */ 
/*     */   public String getQName(int i)
/*     */   {
/* 129 */     return ((Attr)this.m_attrs.item(i)).getName();
/*     */   }
/*     */ 
/*     */   public String getType(int i)
/*     */   {
/* 142 */     return "CDATA";
/*     */   }
/*     */ 
/*     */   public String getValue(int i)
/*     */   {
/* 155 */     return ((Attr)this.m_attrs.item(i)).getValue();
/*     */   }
/*     */ 
/*     */   public String getType(String name)
/*     */   {
/* 168 */     return "CDATA";
/*     */   }
/*     */ 
/*     */   public String getType(String uri, String localName)
/*     */   {
/* 183 */     return "CDATA";
/*     */   }
/*     */ 
/*     */   public String getValue(String name)
/*     */   {
/* 196 */     Attr attr = (Attr)this.m_attrs.getNamedItem(name);
/* 197 */     return null != attr ? attr.getValue() : null;
/*     */   }
/*     */ 
/*     */   public String getValue(String uri, String localName)
/*     */   {
/* 212 */     Node a = this.m_attrs.getNamedItemNS(uri, localName);
/* 213 */     return a == null ? null : a.getNodeValue();
/*     */   }
/*     */ 
/*     */   public int getIndex(String uri, String localPart)
/*     */   {
/* 227 */     for (int i = this.m_attrs.getLength() - 1; i >= 0; i--)
/*     */     {
/* 229 */       Node a = this.m_attrs.item(i);
/* 230 */       String u = a.getNamespaceURI();
/* 231 */       if ((u == null ? uri == null : u.equals(uri)) && (a.getLocalName().equals(localPart)))
/*     */       {
/* 234 */         return i;
/*     */       }
/*     */     }
/* 236 */     return -1;
/*     */   }
/*     */ 
/*     */   public int getIndex(String qName)
/*     */   {
/* 248 */     for (int i = this.m_attrs.getLength() - 1; i >= 0; i--)
/*     */     {
/* 250 */       Node a = this.m_attrs.item(i);
/* 251 */       if (a.getNodeName().equals(qName))
/* 252 */         return i;
/*     */     }
/* 254 */     return -1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.utils.AttList
 * JD-Core Version:    0.6.2
 */