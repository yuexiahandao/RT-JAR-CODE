/*    */ package com.sun.xml.internal.ws.util.xml;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import org.w3c.dom.NamedNodeMap;
/*    */ 
/*    */ public class NamedNodeMapIterator
/*    */   implements Iterator
/*    */ {
/*    */   protected NamedNodeMap _map;
/*    */   protected int _index;
/*    */ 
/*    */   public NamedNodeMapIterator(NamedNodeMap map)
/*    */   {
/* 41 */     this._map = map;
/* 42 */     this._index = 0;
/*    */   }
/*    */ 
/*    */   public boolean hasNext() {
/* 46 */     if (this._map == null)
/* 47 */       return false;
/* 48 */     return this._index < this._map.getLength();
/*    */   }
/*    */ 
/*    */   public Object next() {
/* 52 */     Object obj = this._map.item(this._index);
/* 53 */     if (obj != null)
/* 54 */       this._index += 1;
/* 55 */     return obj;
/*    */   }
/*    */ 
/*    */   public void remove() {
/* 59 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.util.xml.NamedNodeMapIterator
 * JD-Core Version:    0.6.2
 */