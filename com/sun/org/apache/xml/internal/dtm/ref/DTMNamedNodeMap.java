/*     */ package com.sun.org.apache.xml.internal.dtm.ref;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.dtm.DTM;
/*     */ import org.w3c.dom.DOMException;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public class DTMNamedNodeMap
/*     */   implements NamedNodeMap
/*     */ {
/*     */   DTM dtm;
/*     */   int element;
/*  56 */   short m_count = -1;
/*     */ 
/*     */   public DTMNamedNodeMap(DTM dtm, int element)
/*     */   {
/*  66 */     this.dtm = dtm;
/*  67 */     this.element = element;
/*     */   }
/*     */ 
/*     */   public int getLength()
/*     */   {
/*  78 */     if (this.m_count == -1)
/*     */     {
/*  80 */       short count = 0;
/*     */ 
/*  82 */       for (int n = this.dtm.getFirstAttribute(this.element); n != -1; 
/*  83 */         n = this.dtm.getNextAttribute(n))
/*     */       {
/*  85 */         count = (short)(count + 1);
/*     */       }
/*     */ 
/*  88 */       this.m_count = count;
/*     */     }
/*     */ 
/*  91 */     return this.m_count;
/*     */   }
/*     */ 
/*     */   public Node getNamedItem(String name)
/*     */   {
/* 104 */     for (int n = this.dtm.getFirstAttribute(this.element); n != -1; 
/* 105 */       n = this.dtm.getNextAttribute(n))
/*     */     {
/* 107 */       if (this.dtm.getNodeName(n).equals(name)) {
/* 108 */         return this.dtm.getNode(n);
/*     */       }
/*     */     }
/* 111 */     return null;
/*     */   }
/*     */ 
/*     */   public Node item(int i)
/*     */   {
/* 125 */     int count = 0;
/*     */ 
/* 127 */     for (int n = this.dtm.getFirstAttribute(this.element); n != -1; 
/* 128 */       n = this.dtm.getNextAttribute(n))
/*     */     {
/* 130 */       if (count == i) {
/* 131 */         return this.dtm.getNode(n);
/*     */       }
/* 133 */       count++;
/*     */     }
/*     */ 
/* 136 */     return null;
/*     */   }
/*     */ 
/*     */   public Node setNamedItem(Node newNode)
/*     */   {
/* 165 */     throw new DTMException((short)7);
/*     */   }
/*     */ 
/*     */   public Node removeNamedItem(String name)
/*     */   {
/* 185 */     throw new DTMException((short)7);
/*     */   }
/*     */ 
/*     */   public Node getNamedItemNS(String namespaceURI, String localName)
/*     */   {
/* 201 */     Node retNode = null;
/* 202 */     for (int n = this.dtm.getFirstAttribute(this.element); n != -1; 
/* 203 */       n = this.dtm.getNextAttribute(n))
/*     */     {
/* 205 */       if (localName.equals(this.dtm.getLocalName(n)))
/*     */       {
/* 207 */         String nsURI = this.dtm.getNamespaceURI(n);
/* 208 */         if (((namespaceURI == null) && (nsURI == null)) || ((namespaceURI != null) && (namespaceURI.equals(nsURI))))
/*     */         {
/* 211 */           retNode = this.dtm.getNode(n);
/* 212 */           break;
/*     */         }
/*     */       }
/*     */     }
/* 216 */     return retNode;
/*     */   }
/*     */ 
/*     */   public Node setNamedItemNS(Node arg)
/*     */     throws DOMException
/*     */   {
/* 244 */     throw new DTMException((short)7);
/*     */   }
/*     */ 
/*     */   public Node removeNamedItemNS(String namespaceURI, String localName)
/*     */     throws DOMException
/*     */   {
/* 270 */     throw new DTMException((short)7);
/*     */   }
/*     */ 
/*     */   public class DTMException extends DOMException
/*     */   {
/*     */     static final long serialVersionUID = -8290238117162437678L;
/*     */ 
/*     */     public DTMException(short code, String message)
/*     */     {
/* 288 */       super(message);
/*     */     }
/*     */ 
/*     */     public DTMException(short code)
/*     */     {
/* 299 */       super("");
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.dtm.ref.DTMNamedNodeMap
 * JD-Core Version:    0.6.2
 */