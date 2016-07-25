/*    */ package com.sun.xml.internal.bind.v2.runtime.reflect;
/*    */ 
/*    */ import com.sun.xml.internal.bind.api.AccessorException;
/*    */ import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
/*    */ import java.util.Map;
/*    */ 
/*    */ final class PrimitiveArrayListerDouble<BeanT> extends Lister<BeanT, double[], Double, DoubleArrayPack>
/*    */ {
/*    */   static void register()
/*    */   {
/* 44 */     Lister.primitiveArrayListers.put(Double.TYPE, new PrimitiveArrayListerDouble());
/*    */   }
/*    */ 
/*    */   public ListIterator<Double> iterator(final double[] objects, XMLSerializer context) {
/* 48 */     return new ListIterator() {
/* 49 */       int idx = 0;
/*    */ 
/* 51 */       public boolean hasNext() { return this.idx < objects.length; }
/*    */ 
/*    */       public Double next()
/*    */       {
/* 55 */         return Double.valueOf(objects[(this.idx++)]);
/*    */       }
/*    */     };
/*    */   }
/*    */ 
/*    */   public DoubleArrayPack startPacking(BeanT current, Accessor<BeanT, double[]> acc) {
/* 61 */     return new DoubleArrayPack();
/*    */   }
/*    */ 
/*    */   public void addToPack(DoubleArrayPack objects, Double o) {
/* 65 */     objects.add(o);
/*    */   }
/*    */ 
/*    */   public void endPacking(DoubleArrayPack pack, BeanT bean, Accessor<BeanT, double[]> acc) throws AccessorException {
/* 69 */     acc.set(bean, pack.build());
/*    */   }
/*    */ 
/*    */   public void reset(BeanT o, Accessor<BeanT, double[]> acc) throws AccessorException {
/* 73 */     acc.set(o, new double[0]);
/*    */   }
/*    */ 
/* 77 */   static final class DoubleArrayPack { double[] buf = new double[16];
/*    */     int size;
/*    */ 
/*    */     void add(Double b) {
/* 81 */       if (this.buf.length == this.size)
/*    */       {
/* 83 */         double[] nb = new double[this.buf.length * 2];
/* 84 */         System.arraycopy(this.buf, 0, nb, 0, this.buf.length);
/* 85 */         this.buf = nb;
/*    */       }
/* 87 */       if (b != null)
/* 88 */         this.buf[(this.size++)] = b.doubleValue();
/*    */     }
/*    */ 
/*    */     double[] build() {
/* 92 */       if (this.buf.length == this.size)
/*    */       {
/* 94 */         return this.buf;
/*    */       }
/* 96 */       double[] r = new double[this.size];
/* 97 */       System.arraycopy(this.buf, 0, r, 0, this.size);
/* 98 */       return r;
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.reflect.PrimitiveArrayListerDouble
 * JD-Core Version:    0.6.2
 */