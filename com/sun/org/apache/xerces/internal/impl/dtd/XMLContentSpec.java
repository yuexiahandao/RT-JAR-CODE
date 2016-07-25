/*     */ package com.sun.org.apache.xerces.internal.impl.dtd;
/*     */ 
/*     */ public class XMLContentSpec
/*     */ {
/*     */   public static final short CONTENTSPECNODE_LEAF = 0;
/*     */   public static final short CONTENTSPECNODE_ZERO_OR_ONE = 1;
/*     */   public static final short CONTENTSPECNODE_ZERO_OR_MORE = 2;
/*     */   public static final short CONTENTSPECNODE_ONE_OR_MORE = 3;
/*     */   public static final short CONTENTSPECNODE_CHOICE = 4;
/*     */   public static final short CONTENTSPECNODE_SEQ = 5;
/*     */   public static final short CONTENTSPECNODE_ANY = 6;
/*     */   public static final short CONTENTSPECNODE_ANY_OTHER = 7;
/*     */   public static final short CONTENTSPECNODE_ANY_LOCAL = 8;
/*     */   public static final short CONTENTSPECNODE_ANY_LAX = 22;
/*     */   public static final short CONTENTSPECNODE_ANY_OTHER_LAX = 23;
/*     */   public static final short CONTENTSPECNODE_ANY_LOCAL_LAX = 24;
/*     */   public static final short CONTENTSPECNODE_ANY_SKIP = 38;
/*     */   public static final short CONTENTSPECNODE_ANY_OTHER_SKIP = 39;
/*     */   public static final short CONTENTSPECNODE_ANY_LOCAL_SKIP = 40;
/*     */   public short type;
/*     */   public Object value;
/*     */   public Object otherValue;
/*     */ 
/*     */   public XMLContentSpec()
/*     */   {
/* 216 */     clear();
/*     */   }
/*     */ 
/*     */   public XMLContentSpec(short type, Object value, Object otherValue)
/*     */   {
/* 221 */     setValues(type, value, otherValue);
/*     */   }
/*     */ 
/*     */   public XMLContentSpec(XMLContentSpec contentSpec)
/*     */   {
/* 228 */     setValues(contentSpec);
/*     */   }
/*     */ 
/*     */   public XMLContentSpec(Provider provider, int contentSpecIndex)
/*     */   {
/* 237 */     setValues(provider, contentSpecIndex);
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 246 */     this.type = -1;
/* 247 */     this.value = null;
/* 248 */     this.otherValue = null;
/*     */   }
/*     */ 
/*     */   public void setValues(short type, Object value, Object otherValue)
/*     */   {
/* 253 */     this.type = type;
/* 254 */     this.value = value;
/* 255 */     this.otherValue = otherValue;
/*     */   }
/*     */ 
/*     */   public void setValues(XMLContentSpec contentSpec)
/*     */   {
/* 260 */     this.type = contentSpec.type;
/* 261 */     this.value = contentSpec.value;
/* 262 */     this.otherValue = contentSpec.otherValue;
/*     */   }
/*     */ 
/*     */   public void setValues(Provider provider, int contentSpecIndex)
/*     */   {
/* 272 */     if (!provider.getContentSpec(contentSpecIndex, this))
/* 273 */       clear();
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 284 */     return this.type << 16 | this.value.hashCode() << 8 | this.otherValue.hashCode();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object object)
/*     */   {
/* 291 */     if ((object != null) && ((object instanceof XMLContentSpec))) {
/* 292 */       XMLContentSpec contentSpec = (XMLContentSpec)object;
/* 293 */       return (this.type == contentSpec.type) && (this.value == contentSpec.value) && (this.otherValue == contentSpec.otherValue);
/*     */     }
/*     */ 
/* 297 */     return false;
/*     */   }
/*     */ 
/*     */   public static abstract interface Provider
/*     */   {
/*     */     public abstract boolean getContentSpec(int paramInt, XMLContentSpec paramXMLContentSpec);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dtd.XMLContentSpec
 * JD-Core Version:    0.6.2
 */