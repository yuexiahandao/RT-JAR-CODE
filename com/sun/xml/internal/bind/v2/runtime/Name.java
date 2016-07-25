/*    */ package com.sun.xml.internal.bind.v2.runtime;
/*    */ 
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ public final class Name
/*    */   implements Comparable<Name>
/*    */ {
/*    */   public final String nsUri;
/*    */   public final String localName;
/*    */   public final short nsUriIndex;
/*    */   public final short localNameIndex;
/*    */   public final short qNameIndex;
/*    */   public final boolean isAttribute;
/*    */ 
/*    */   Name(int qNameIndex, int nsUriIndex, String nsUri, int localIndex, String localName, boolean isAttribute)
/*    */   {
/* 70 */     this.qNameIndex = ((short)qNameIndex);
/* 71 */     this.nsUri = nsUri;
/* 72 */     this.localName = localName;
/* 73 */     this.nsUriIndex = ((short)nsUriIndex);
/* 74 */     this.localNameIndex = ((short)localIndex);
/* 75 */     this.isAttribute = isAttribute;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 79 */     return '{' + this.nsUri + '}' + this.localName;
/*    */   }
/*    */ 
/*    */   public QName toQName()
/*    */   {
/* 86 */     return new QName(this.nsUri, this.localName);
/*    */   }
/*    */ 
/*    */   public boolean equals(String nsUri, String localName) {
/* 90 */     return (localName.equals(this.localName)) && (nsUri.equals(this.nsUri));
/*    */   }
/*    */ 
/*    */   public int compareTo(Name that) {
/* 94 */     int r = this.nsUri.compareTo(that.nsUri);
/* 95 */     if (r != 0) return r;
/* 96 */     return this.localName.compareTo(that.localName);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.Name
 * JD-Core Version:    0.6.2
 */