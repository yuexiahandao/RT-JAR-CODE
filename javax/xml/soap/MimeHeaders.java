/*     */ package javax.xml.soap;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class MimeHeaders
/*     */ {
/*     */   private Vector headers;
/*     */ 
/*     */   public MimeHeaders()
/*     */   {
/*  51 */     this.headers = new Vector();
/*     */   }
/*     */ 
/*     */   public String[] getHeader(String name)
/*     */   {
/*  64 */     Vector values = new Vector();
/*     */ 
/*  66 */     for (int i = 0; i < this.headers.size(); i++) {
/*  67 */       MimeHeader hdr = (MimeHeader)this.headers.elementAt(i);
/*  68 */       if ((hdr.getName().equalsIgnoreCase(name)) && (hdr.getValue() != null))
/*     */       {
/*  70 */         values.addElement(hdr.getValue());
/*     */       }
/*     */     }
/*  73 */     if (values.size() == 0) {
/*  74 */       return null;
/*     */     }
/*  76 */     String[] r = new String[values.size()];
/*  77 */     values.copyInto(r);
/*  78 */     return r;
/*     */   }
/*     */ 
/*     */   public void setHeader(String name, String value)
/*     */   {
/*  99 */     boolean found = false;
/*     */ 
/* 101 */     if ((name == null) || (name.equals(""))) {
/* 102 */       throw new IllegalArgumentException("Illegal MimeHeader name");
/*     */     }
/* 104 */     for (int i = 0; i < this.headers.size(); i++) {
/* 105 */       MimeHeader hdr = (MimeHeader)this.headers.elementAt(i);
/* 106 */       if (hdr.getName().equalsIgnoreCase(name)) {
/* 107 */         if (!found) {
/* 108 */           this.headers.setElementAt(new MimeHeader(hdr.getName(), value), i);
/*     */ 
/* 110 */           found = true;
/*     */         }
/*     */         else {
/* 113 */           this.headers.removeElementAt(i--);
/*     */         }
/*     */       }
/*     */     }
/* 117 */     if (!found)
/* 118 */       addHeader(name, value);
/*     */   }
/*     */ 
/*     */   public void addHeader(String name, String value)
/*     */   {
/* 137 */     if ((name == null) || (name.equals(""))) {
/* 138 */       throw new IllegalArgumentException("Illegal MimeHeader name");
/*     */     }
/* 140 */     int pos = this.headers.size();
/*     */ 
/* 142 */     for (int i = pos - 1; i >= 0; i--) {
/* 143 */       MimeHeader hdr = (MimeHeader)this.headers.elementAt(i);
/* 144 */       if (hdr.getName().equalsIgnoreCase(name)) {
/* 145 */         this.headers.insertElementAt(new MimeHeader(name, value), i + 1);
/*     */ 
/* 147 */         return;
/*     */       }
/*     */     }
/* 150 */     this.headers.addElement(new MimeHeader(name, value));
/*     */   }
/*     */ 
/*     */   public void removeHeader(String name)
/*     */   {
/* 161 */     for (int i = 0; i < this.headers.size(); i++) {
/* 162 */       MimeHeader hdr = (MimeHeader)this.headers.elementAt(i);
/* 163 */       if (hdr.getName().equalsIgnoreCase(name))
/* 164 */         this.headers.removeElementAt(i--);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void removeAllHeaders()
/*     */   {
/* 172 */     this.headers.removeAllElements();
/*     */   }
/*     */ 
/*     */   public Iterator getAllHeaders()
/*     */   {
/* 183 */     return this.headers.iterator();
/*     */   }
/*     */ 
/*     */   public Iterator getMatchingHeaders(String[] names)
/*     */   {
/* 254 */     return new MatchingIterator(names, true);
/*     */   }
/*     */ 
/*     */   public Iterator getNonMatchingHeaders(String[] names)
/*     */   {
/* 267 */     return new MatchingIterator(names, false);
/*     */   }
/*     */ 
/*     */   class MatchingIterator
/*     */     implements Iterator
/*     */   {
/*     */     private boolean match;
/*     */     private Iterator iterator;
/*     */     private String[] names;
/*     */     private Object nextHeader;
/*     */ 
/*     */     MatchingIterator(String[] names, boolean match)
/*     */     {
/* 193 */       this.match = match;
/* 194 */       this.names = names;
/* 195 */       this.iterator = MimeHeaders.this.headers.iterator();
/*     */     }
/*     */ 
/*     */     private Object nextMatch()
/*     */     {
/* 200 */       while (this.iterator.hasNext()) {
/* 201 */         MimeHeader hdr = (MimeHeader)this.iterator.next();
/*     */ 
/* 203 */         if (this.names == null) {
/* 204 */           return this.match ? null : hdr;
/*     */         }
/* 206 */         for (int i = 0; ; i++) { if (i >= this.names.length) break label87;
/* 207 */           if (hdr.getName().equalsIgnoreCase(this.names[i])) {
/* 208 */             if (!this.match) break;
/* 209 */             return hdr;
/*     */           }
/*     */         }
/* 212 */         label87: if (!this.match)
/* 213 */           return hdr;
/*     */       }
/* 215 */       return null;
/*     */     }
/*     */ 
/*     */     public boolean hasNext()
/*     */     {
/* 220 */       if (this.nextHeader == null)
/* 221 */         this.nextHeader = nextMatch();
/* 222 */       return this.nextHeader != null;
/*     */     }
/*     */ 
/*     */     public Object next()
/*     */     {
/* 228 */       if (this.nextHeader != null) {
/* 229 */         Object ret = this.nextHeader;
/* 230 */         this.nextHeader = null;
/* 231 */         return ret;
/*     */       }
/* 233 */       if (hasNext())
/* 234 */         return this.nextHeader;
/* 235 */       return null;
/*     */     }
/*     */ 
/*     */     public void remove() {
/* 239 */       this.iterator.remove();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.soap.MimeHeaders
 * JD-Core Version:    0.6.2
 */