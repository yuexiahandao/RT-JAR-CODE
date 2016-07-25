/*    */ package com.sun.xml.internal.bind.v2.runtime.reflect;
/*    */ 
/*    */ import com.sun.xml.internal.bind.api.AccessorException;
/*    */ import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
/*    */ import java.util.Map;
/*    */ 
/*    */ final class PrimitiveArrayListerShort<BeanT> extends Lister<BeanT, short[], Short, ShortArrayPack>
/*    */ {
/*    */   static void register()
/*    */   {
/* 44 */     Lister.primitiveArrayListers.put(Short.TYPE, new PrimitiveArrayListerShort());
/*    */   }
/*    */ 
/*    */   public ListIterator<Short> iterator(final short[] objects, XMLSerializer context) {
/* 48 */     return new ListIterator() {
/* 49 */       int idx = 0;
/*    */ 
/* 51 */       public boolean hasNext() { return this.idx < objects.length; }
/*    */ 
/*    */       public Short next()
/*    */       {
/* 55 */         return Short.valueOf(objects[(this.idx++)]);
/*    */       }
/*    */     };
/*    */   }
/*    */ 
/*    */   public ShortArrayPack startPacking(BeanT current, Accessor<BeanT, short[]> acc) {
/* 61 */     return new ShortArrayPack();
/*    */   }
/*    */ 
/*    */   public void addToPack(ShortArrayPack objects, Short o) {
/* 65 */     objects.add(o);
/*    */   }
/*    */ 
/*    */   public void endPacking(ShortArrayPack pack, BeanT bean, Accessor<BeanT, short[]> acc) throws AccessorException {
/* 69 */     acc.set(bean, pack.build());
/*    */   }
/*    */ 
/*    */   public void reset(BeanT o, Accessor<BeanT, short[]> acc) throws AccessorException {
/* 73 */     acc.set(o, new short[0]);
/*    */   }
/*    */ 
/* 77 */   static final class ShortArrayPack { short[] buf = new short[16];
/*    */     int size;
/*    */ 
/*    */     void add(Short b) {
/* 81 */       if (this.buf.length == this.size)
/*    */       {
/* 83 */         short[] nb = new short[this.buf.length * 2];
/* 84 */         System.arraycopy(this.buf, 0, nb, 0, this.buf.length);
/* 85 */         this.buf = nb;
/*    */       }
/* 87 */       if (b != null)
/* 88 */         this.buf[(this.size++)] = b.shortValue();
/*    */     }
/*    */ 
/*    */     short[] build() {
/* 92 */       if (this.buf.length == this.size)
/*    */       {
/* 94 */         return this.buf;
/*    */       }
/* 96 */       short[] r = new short[this.size];
/* 97 */       System.arraycopy(this.buf, 0, r, 0, this.size);
/* 98 */       return r;
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.reflect.PrimitiveArrayListerShort
 * JD-Core Version:    0.6.2
 */