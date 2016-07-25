/*    */ package com.sun.org.apache.xerces.internal.dom;
/*    */ 
/*    */ import java.util.Vector;
/*    */ import org.w3c.dom.DOMStringList;
/*    */ 
/*    */ public class DOMStringListImpl
/*    */   implements DOMStringList
/*    */ {
/*    */   private Vector fStrings;
/*    */ 
/*    */   public DOMStringListImpl()
/*    */   {
/* 45 */     this.fStrings = new Vector();
/*    */   }
/*    */ 
/*    */   public DOMStringListImpl(Vector params)
/*    */   {
/* 52 */     this.fStrings = params;
/*    */   }
/*    */ 
/*    */   public String item(int index)
/*    */   {
/*    */     try
/*    */     {
/* 60 */       return (String)this.fStrings.elementAt(index); } catch (ArrayIndexOutOfBoundsException e) {
/*    */     }
/* 62 */     return null;
/*    */   }
/*    */ 
/*    */   public int getLength()
/*    */   {
/* 70 */     return this.fStrings.size();
/*    */   }
/*    */ 
/*    */   public boolean contains(String param)
/*    */   {
/* 77 */     return this.fStrings.contains(param);
/*    */   }
/*    */ 
/*    */   public void add(String param)
/*    */   {
/* 87 */     this.fStrings.add(param);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.dom.DOMStringListImpl
 * JD-Core Version:    0.6.2
 */