/*    */ package com.sun.xml.internal.bind.v2.runtime.reflect;
/*    */ 
/*    */ import com.sun.xml.internal.bind.api.AccessorException;
/*    */ import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
/*    */ import java.util.Map;
/*    */ 
/*    */ final class PrimitiveArrayListerByte<BeanT> extends Lister<BeanT, byte[], Byte, ByteArrayPack>
/*    */ {
/*    */   static void register()
/*    */   {
/* 44 */     Lister.primitiveArrayListers.put(Byte.TYPE, new PrimitiveArrayListerByte());
/*    */   }
/*    */ 
/*    */   public ListIterator<Byte> iterator(final byte[] objects, XMLSerializer context) {
/* 48 */     return new ListIterator() {
/* 49 */       int idx = 0;
/*    */ 
/* 51 */       public boolean hasNext() { return this.idx < objects.length; }
/*    */ 
/*    */       public Byte next()
/*    */       {
/* 55 */         return Byte.valueOf(objects[(this.idx++)]);
/*    */       }
/*    */     };
/*    */   }
/*    */ 
/*    */   public ByteArrayPack startPacking(BeanT current, Accessor<BeanT, byte[]> acc) {
/* 61 */     return new ByteArrayPack();
/*    */   }
/*    */ 
/*    */   public void addToPack(ByteArrayPack objects, Byte o) {
/* 65 */     objects.add(o);
/*    */   }
/*    */ 
/*    */   public void endPacking(ByteArrayPack pack, BeanT bean, Accessor<BeanT, byte[]> acc) throws AccessorException {
/* 69 */     acc.set(bean, pack.build());
/*    */   }
/*    */ 
/*    */   public void reset(BeanT o, Accessor<BeanT, byte[]> acc) throws AccessorException {
/* 73 */     acc.set(o, new byte[0]);
/*    */   }
/*    */ 
/* 77 */   static final class ByteArrayPack { byte[] buf = new byte[16];
/*    */     int size;
/*    */ 
/*    */     void add(Byte b) {
/* 81 */       if (this.buf.length == this.size)
/*    */       {
/* 83 */         byte[] nb = new byte[this.buf.length * 2];
/* 84 */         System.arraycopy(this.buf, 0, nb, 0, this.buf.length);
/* 85 */         this.buf = nb;
/*    */       }
/* 87 */       if (b != null)
/* 88 */         this.buf[(this.size++)] = b.byteValue();
/*    */     }
/*    */ 
/*    */     byte[] build() {
/* 92 */       if (this.buf.length == this.size)
/*    */       {
/* 94 */         return this.buf;
/*    */       }
/* 96 */       byte[] r = new byte[this.size];
/* 97 */       System.arraycopy(this.buf, 0, r, 0, this.size);
/* 98 */       return r;
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.reflect.PrimitiveArrayListerByte
 * JD-Core Version:    0.6.2
 */