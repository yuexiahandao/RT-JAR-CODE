/*    */ package com.sun.org.apache.xerces.internal.impl.dv.xs;
/*    */ 
/*    */ import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
/*    */ import com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;
/*    */ import com.sun.org.apache.xerces.internal.impl.dv.util.ByteListImpl;
/*    */ import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;
/*    */ 
/*    */ public class HexBinaryDV extends TypeValidator
/*    */ {
/*    */   public short getAllowedFacets()
/*    */   {
/* 41 */     return 2079;
/*    */   }
/*    */ 
/*    */   public Object getActualValue(String content, ValidationContext context) throws InvalidDatatypeValueException {
/* 45 */     byte[] decoded = HexBin.decode(content);
/* 46 */     if (decoded == null) {
/* 47 */       throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[] { content, "hexBinary" });
/*    */     }
/* 49 */     return new XHex(decoded);
/*    */   }
/*    */ 
/*    */   public int getDataLength(Object value)
/*    */   {
/* 54 */     return ((XHex)value).getLength();
/*    */   }
/*    */ 
/*    */   private static final class XHex extends ByteListImpl
/*    */   {
/*    */     public XHex(byte[] data) {
/* 60 */       super();
/*    */     }
/*    */     public synchronized String toString() {
/* 63 */       if (this.canonical == null) {
/* 64 */         this.canonical = HexBin.encode(this.data);
/*    */       }
/* 66 */       return this.canonical;
/*    */     }
/*    */ 
/*    */     public boolean equals(Object obj) {
/* 70 */       if (!(obj instanceof XHex))
/* 71 */         return false;
/* 72 */       byte[] odata = ((XHex)obj).data;
/* 73 */       int len = this.data.length;
/* 74 */       if (len != odata.length)
/* 75 */         return false;
/* 76 */       for (int i = 0; i < len; i++) {
/* 77 */         if (this.data[i] != odata[i])
/* 78 */           return false;
/*    */       }
/* 80 */       return true;
/*    */     }
/*    */ 
/*    */     public int hashCode() {
/* 84 */       int hash = 0;
/* 85 */       for (int i = 0; i < this.data.length; i++) {
/* 86 */         hash = hash * 37 + (this.data[i] & 0xFF);
/*    */       }
/* 88 */       return hash;
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dv.xs.HexBinaryDV
 * JD-Core Version:    0.6.2
 */