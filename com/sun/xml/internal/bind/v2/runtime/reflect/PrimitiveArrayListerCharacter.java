/*    */ package com.sun.xml.internal.bind.v2.runtime.reflect;
/*    */ 
/*    */ import com.sun.xml.internal.bind.api.AccessorException;
/*    */ import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
/*    */ import java.util.Map;
/*    */ 
/*    */ final class PrimitiveArrayListerCharacter<BeanT> extends Lister<BeanT, char[], Character, CharacterArrayPack>
/*    */ {
/*    */   static void register()
/*    */   {
/* 44 */     Lister.primitiveArrayListers.put(Character.TYPE, new PrimitiveArrayListerCharacter());
/*    */   }
/*    */ 
/*    */   public ListIterator<Character> iterator(final char[] objects, XMLSerializer context) {
/* 48 */     return new ListIterator() {
/* 49 */       int idx = 0;
/*    */ 
/* 51 */       public boolean hasNext() { return this.idx < objects.length; }
/*    */ 
/*    */       public Character next()
/*    */       {
/* 55 */         return Character.valueOf(objects[(this.idx++)]);
/*    */       }
/*    */     };
/*    */   }
/*    */ 
/*    */   public CharacterArrayPack startPacking(BeanT current, Accessor<BeanT, char[]> acc) {
/* 61 */     return new CharacterArrayPack();
/*    */   }
/*    */ 
/*    */   public void addToPack(CharacterArrayPack objects, Character o) {
/* 65 */     objects.add(o);
/*    */   }
/*    */ 
/*    */   public void endPacking(CharacterArrayPack pack, BeanT bean, Accessor<BeanT, char[]> acc) throws AccessorException {
/* 69 */     acc.set(bean, pack.build());
/*    */   }
/*    */ 
/*    */   public void reset(BeanT o, Accessor<BeanT, char[]> acc) throws AccessorException {
/* 73 */     acc.set(o, new char[0]);
/*    */   }
/*    */ 
/* 77 */   static final class CharacterArrayPack { char[] buf = new char[16];
/*    */     int size;
/*    */ 
/*    */     void add(Character b) {
/* 81 */       if (this.buf.length == this.size)
/*    */       {
/* 83 */         char[] nb = new char[this.buf.length * 2];
/* 84 */         System.arraycopy(this.buf, 0, nb, 0, this.buf.length);
/* 85 */         this.buf = nb;
/*    */       }
/* 87 */       if (b != null)
/* 88 */         this.buf[(this.size++)] = b.charValue();
/*    */     }
/*    */ 
/*    */     char[] build() {
/* 92 */       if (this.buf.length == this.size)
/*    */       {
/* 94 */         return this.buf;
/*    */       }
/* 96 */       char[] r = new char[this.size];
/* 97 */       System.arraycopy(this.buf, 0, r, 0, this.size);
/* 98 */       return r;
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.reflect.PrimitiveArrayListerCharacter
 * JD-Core Version:    0.6.2
 */