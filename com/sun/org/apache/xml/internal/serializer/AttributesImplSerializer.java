/*     */ package com.sun.org.apache.xml.internal.serializer;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.helpers.AttributesImpl;
/*     */ 
/*     */ public final class AttributesImplSerializer extends AttributesImpl
/*     */ {
/*  51 */   private final Hashtable m_indexFromQName = new Hashtable();
/*     */ 
/*  53 */   private final StringBuffer m_buff = new StringBuffer();
/*     */   private static final int MAX = 12;
/*     */   private static final int MAXMinus1 = 11;
/*     */ 
/*     */   public final int getIndex(String qname)
/*     */   {
/*  77 */     if (super.getLength() < 12)
/*     */     {
/*  81 */       int index = super.getIndex(qname);
/*  82 */       return index;
/*     */     }
/*     */ 
/*  86 */     Integer i = (Integer)this.m_indexFromQName.get(qname);
/*     */     int index;
/*     */     int index;
/*  87 */     if (i == null)
/*  88 */       index = -1;
/*     */     else
/*  90 */       index = i.intValue();
/*  91 */     return index;
/*     */   }
/*     */ 
/*     */   public final void addAttribute(String uri, String local, String qname, String type, String val)
/*     */   {
/* 112 */     int index = super.getLength();
/* 113 */     super.addAttribute(uri, local, qname, type, val);
/*     */ 
/* 117 */     if (index < 11)
/*     */     {
/* 119 */       return;
/*     */     }
/* 121 */     if (index == 11)
/*     */     {
/* 123 */       switchOverToHash(12);
/*     */     }
/*     */     else
/*     */     {
/* 129 */       Integer i = new Integer(index);
/* 130 */       this.m_indexFromQName.put(qname, i);
/*     */ 
/* 133 */       this.m_buff.setLength(0);
/* 134 */       this.m_buff.append('{').append(uri).append('}').append(local);
/* 135 */       String key = this.m_buff.toString();
/* 136 */       this.m_indexFromQName.put(key, i);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void switchOverToHash(int numAtts)
/*     */   {
/* 151 */     for (int index = 0; index < numAtts; index++)
/*     */     {
/* 153 */       String qName = super.getQName(index);
/* 154 */       Integer i = new Integer(index);
/* 155 */       this.m_indexFromQName.put(qName, i);
/*     */ 
/* 158 */       String uri = super.getURI(index);
/* 159 */       String local = super.getLocalName(index);
/* 160 */       this.m_buff.setLength(0);
/* 161 */       this.m_buff.append('{').append(uri).append('}').append(local);
/* 162 */       String key = this.m_buff.toString();
/* 163 */       this.m_indexFromQName.put(key, i);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void clear()
/*     */   {
/* 175 */     int len = super.getLength();
/* 176 */     super.clear();
/* 177 */     if (12 <= len)
/*     */     {
/* 181 */       this.m_indexFromQName.clear();
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void setAttributes(Attributes atts)
/*     */   {
/* 197 */     super.setAttributes(atts);
/*     */ 
/* 202 */     int numAtts = atts.getLength();
/* 203 */     if (12 <= numAtts)
/* 204 */       switchOverToHash(numAtts);
/*     */   }
/*     */ 
/*     */   public final int getIndex(String uri, String localName)
/*     */   {
/* 219 */     if (super.getLength() < 12)
/*     */     {
/* 223 */       int index = super.getIndex(uri, localName);
/* 224 */       return index;
/*     */     }
/*     */ 
/* 229 */     this.m_buff.setLength(0);
/* 230 */     this.m_buff.append('{').append(uri).append('}').append(localName);
/* 231 */     String key = this.m_buff.toString();
/* 232 */     Integer i = (Integer)this.m_indexFromQName.get(key);
/*     */     int index;
/*     */     int index;
/* 233 */     if (i == null)
/* 234 */       index = -1;
/*     */     else
/* 236 */       index = i.intValue();
/* 237 */     return index;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.serializer.AttributesImplSerializer
 * JD-Core Version:    0.6.2
 */