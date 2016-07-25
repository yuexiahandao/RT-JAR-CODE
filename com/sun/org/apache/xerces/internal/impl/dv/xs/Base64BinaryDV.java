/*    */ package com.sun.org.apache.xerces.internal.impl.dv.xs;
/*    */ 
/*    */ import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
/*    */ import com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;
/*    */ import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
/*    */ import com.sun.org.apache.xerces.internal.impl.dv.util.ByteListImpl;
/*    */ 
/*    */ public class Base64BinaryDV extends TypeValidator
/*    */ {
/*    */   public short getAllowedFacets()
/*    */   {
/* 41 */     return 2079;
/*    */   }
/*    */ 
/*    */   public Object getActualValue(String content, ValidationContext context) throws InvalidDatatypeValueException {
/* 45 */     byte[] decoded = Base64.decode(content);
/* 46 */     if (decoded == null) {
/* 47 */       throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[] { content, "base64Binary" });
/*    */     }
/* 49 */     return new XBase64(decoded);
/*    */   }
/*    */ 
/*    */   public int getDataLength(Object value)
/*    */   {
/* 54 */     return ((XBase64)value).getLength();
/*    */   }
/*    */ 
/*    */   private static final class XBase64 extends ByteListImpl
/*    */   {
/*    */     public XBase64(byte[] data)
/*    */     {
/* 63 */       super();
/*    */     }
/*    */     public synchronized String toString() {
/* 66 */       if (this.canonical == null) {
/* 67 */         this.canonical = Base64.encode(this.data);
/*    */       }
/* 69 */       return this.canonical;
/*    */     }
/*    */ 
/*    */     public boolean equals(Object obj) {
/* 73 */       if (!(obj instanceof XBase64))
/* 74 */         return false;
/* 75 */       byte[] odata = ((XBase64)obj).data;
/* 76 */       int len = this.data.length;
/* 77 */       if (len != odata.length)
/* 78 */         return false;
/* 79 */       for (int i = 0; i < len; i++) {
/* 80 */         if (this.data[i] != odata[i])
/* 81 */           return false;
/*    */       }
/* 83 */       return true;
/*    */     }
/*    */ 
/*    */     public int hashCode() {
/* 87 */       int hash = 0;
/* 88 */       for (int i = 0; i < this.data.length; i++) {
/* 89 */         hash = hash * 37 + (this.data[i] & 0xFF);
/*    */       }
/* 91 */       return hash;
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dv.xs.Base64BinaryDV
 * JD-Core Version:    0.6.2
 */