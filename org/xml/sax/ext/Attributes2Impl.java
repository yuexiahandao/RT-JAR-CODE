/*     */ package org.xml.sax.ext;
/*     */ 
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.helpers.AttributesImpl;
/*     */ 
/*     */ public class Attributes2Impl extends AttributesImpl
/*     */   implements Attributes2
/*     */ {
/*     */   private boolean[] declared;
/*     */   private boolean[] specified;
/*     */ 
/*     */   public Attributes2Impl()
/*     */   {
/*  72 */     this.specified = null;
/*  73 */     this.declared = null;
/*     */   }
/*     */ 
/*     */   public Attributes2Impl(Attributes atts)
/*     */   {
/*  93 */     super(atts);
/*     */   }
/*     */ 
/*     */   public boolean isDeclared(int index)
/*     */   {
/* 108 */     if ((index < 0) || (index >= getLength())) {
/* 109 */       throw new ArrayIndexOutOfBoundsException("No attribute at index: " + index);
/*     */     }
/* 111 */     return this.declared[index];
/*     */   }
/*     */ 
/*     */   public boolean isDeclared(String uri, String localName)
/*     */   {
/* 121 */     int index = getIndex(uri, localName);
/*     */ 
/* 123 */     if (index < 0) {
/* 124 */       throw new IllegalArgumentException("No such attribute: local=" + localName + ", namespace=" + uri);
/*     */     }
/*     */ 
/* 127 */     return this.declared[index];
/*     */   }
/*     */ 
/*     */   public boolean isDeclared(String qName)
/*     */   {
/* 137 */     int index = getIndex(qName);
/*     */ 
/* 139 */     if (index < 0) {
/* 140 */       throw new IllegalArgumentException("No such attribute: " + qName);
/*     */     }
/* 142 */     return this.declared[index];
/*     */   }
/*     */ 
/*     */   public boolean isSpecified(int index)
/*     */   {
/* 156 */     if ((index < 0) || (index >= getLength())) {
/* 157 */       throw new ArrayIndexOutOfBoundsException("No attribute at index: " + index);
/*     */     }
/* 159 */     return this.specified[index];
/*     */   }
/*     */ 
/*     */   public boolean isSpecified(String uri, String localName)
/*     */   {
/* 175 */     int index = getIndex(uri, localName);
/*     */ 
/* 177 */     if (index < 0) {
/* 178 */       throw new IllegalArgumentException("No such attribute: local=" + localName + ", namespace=" + uri);
/*     */     }
/*     */ 
/* 181 */     return this.specified[index];
/*     */   }
/*     */ 
/*     */   public boolean isSpecified(String qName)
/*     */   {
/* 195 */     int index = getIndex(qName);
/*     */ 
/* 197 */     if (index < 0) {
/* 198 */       throw new IllegalArgumentException("No such attribute: " + qName);
/*     */     }
/* 200 */     return this.specified[index];
/*     */   }
/*     */ 
/*     */   public void setAttributes(Attributes atts)
/*     */   {
/* 220 */     int length = atts.getLength();
/*     */ 
/* 222 */     super.setAttributes(atts);
/* 223 */     this.declared = new boolean[length];
/* 224 */     this.specified = new boolean[length];
/*     */ 
/* 226 */     if ((atts instanceof Attributes2)) {
/* 227 */       Attributes2 a2 = (Attributes2)atts;
/* 228 */       for (int i = 0; i < length; i++) {
/* 229 */         this.declared[i] = a2.isDeclared(i);
/* 230 */         this.specified[i] = a2.isSpecified(i);
/*     */       }
/*     */     } else {
/* 233 */       for (int i = 0; i < length; i++) {
/* 234 */         this.declared[i] = (!"CDATA".equals(atts.getType(i)) ? 1 : false);
/* 235 */         this.specified[i] = true;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addAttribute(String uri, String localName, String qName, String type, String value)
/*     */   {
/* 255 */     super.addAttribute(uri, localName, qName, type, value);
/*     */ 
/* 258 */     int length = getLength();
/* 259 */     if (this.specified == null)
/*     */     {
/* 261 */       this.specified = new boolean[length];
/* 262 */       this.declared = new boolean[length];
/* 263 */     } else if (length > this.specified.length)
/*     */     {
/* 266 */       boolean[] newFlags = new boolean[length];
/* 267 */       System.arraycopy(this.declared, 0, newFlags, 0, this.declared.length);
/* 268 */       this.declared = newFlags;
/*     */ 
/* 270 */       newFlags = new boolean[length];
/* 271 */       System.arraycopy(this.specified, 0, newFlags, 0, this.specified.length);
/* 272 */       this.specified = newFlags;
/*     */     }
/*     */ 
/* 275 */     this.specified[(length - 1)] = true;
/* 276 */     this.declared[(length - 1)] = (!"CDATA".equals(type) ? 1 : false);
/*     */   }
/*     */ 
/*     */   public void removeAttribute(int index)
/*     */   {
/* 283 */     int origMax = getLength() - 1;
/*     */ 
/* 285 */     super.removeAttribute(index);
/* 286 */     if (index != origMax) {
/* 287 */       System.arraycopy(this.declared, index + 1, this.declared, index, origMax - index);
/*     */ 
/* 289 */       System.arraycopy(this.specified, index + 1, this.specified, index, origMax - index);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setDeclared(int index, boolean value)
/*     */   {
/* 308 */     if ((index < 0) || (index >= getLength())) {
/* 309 */       throw new ArrayIndexOutOfBoundsException("No attribute at index: " + index);
/*     */     }
/* 311 */     this.declared[index] = value;
/*     */   }
/*     */ 
/*     */   public void setSpecified(int index, boolean value)
/*     */   {
/* 327 */     if ((index < 0) || (index >= getLength())) {
/* 328 */       throw new ArrayIndexOutOfBoundsException("No attribute at index: " + index);
/*     */     }
/* 330 */     this.specified[index] = value;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.xml.sax.ext.Attributes2Impl
 * JD-Core Version:    0.6.2
 */