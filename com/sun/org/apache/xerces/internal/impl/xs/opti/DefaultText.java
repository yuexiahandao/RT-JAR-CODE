/*     */ package com.sun.org.apache.xerces.internal.impl.xs.opti;
/*     */ 
/*     */ import org.w3c.dom.DOMException;
/*     */ import org.w3c.dom.Text;
/*     */ 
/*     */ public class DefaultText extends NodeImpl
/*     */   implements Text
/*     */ {
/*     */   public String getData()
/*     */     throws DOMException
/*     */   {
/*  74 */     return null;
/*     */   }
/*     */ 
/*     */   public void setData(String data)
/*     */     throws DOMException
/*     */   {
/*  94 */     throw new DOMException((short)9, "Method not supported");
/*     */   }
/*     */ 
/*     */   public int getLength()
/*     */   {
/* 103 */     return 0;
/*     */   }
/*     */ 
/*     */   public String substringData(int offset, int count)
/*     */     throws DOMException
/*     */   {
/* 124 */     throw new DOMException((short)9, "Method not supported");
/*     */   }
/*     */ 
/*     */   public void appendData(String arg)
/*     */     throws DOMException
/*     */   {
/* 137 */     throw new DOMException((short)9, "Method not supported");
/*     */   }
/*     */ 
/*     */   public void insertData(int offset, String arg)
/*     */     throws DOMException
/*     */   {
/* 153 */     throw new DOMException((short)9, "Method not supported");
/*     */   }
/*     */ 
/*     */   public void deleteData(int offset, int count)
/*     */     throws DOMException
/*     */   {
/* 174 */     throw new DOMException((short)9, "Method not supported");
/*     */   }
/*     */ 
/*     */   public void replaceData(int offset, int count, String arg)
/*     */     throws DOMException
/*     */   {
/* 200 */     throw new DOMException((short)9, "Method not supported");
/*     */   }
/*     */ 
/*     */   public Text splitText(int offset)
/*     */     throws DOMException
/*     */   {
/* 223 */     throw new DOMException((short)9, "Method not supported");
/*     */   }
/*     */ 
/*     */   public boolean isElementContentWhitespace()
/*     */   {
/* 228 */     throw new DOMException((short)9, "Method not supported");
/*     */   }
/*     */ 
/*     */   public String getWholeText() {
/* 232 */     throw new DOMException((short)9, "Method not supported");
/*     */   }
/*     */ 
/*     */   public Text replaceWholeText(String content) throws DOMException {
/* 236 */     throw new DOMException((short)9, "Method not supported");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultText
 * JD-Core Version:    0.6.2
 */