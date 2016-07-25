/*    */ package com.sun.corba.se.impl.corba;
/*    */ 
/*    */ import java.util.Vector;
/*    */ import org.omg.CORBA.Bounds;
/*    */ import org.omg.CORBA.ExceptionList;
/*    */ import org.omg.CORBA.TypeCode;
/*    */ 
/*    */ public class ExceptionListImpl extends ExceptionList
/*    */ {
/* 44 */   private final int INITIAL_CAPACITY = 2;
/* 45 */   private final int CAPACITY_INCREMENT = 2;
/*    */   private Vector _exceptions;
/*    */ 
/*    */   public ExceptionListImpl()
/*    */   {
/* 50 */     this._exceptions = new Vector(2, 2);
/*    */   }
/*    */ 
/*    */   public int count()
/*    */   {
/* 55 */     return this._exceptions.size();
/*    */   }
/*    */ 
/*    */   public void add(TypeCode paramTypeCode)
/*    */   {
/* 60 */     this._exceptions.addElement(paramTypeCode);
/*    */   }
/*    */ 
/*    */   public TypeCode item(int paramInt) throws Bounds
/*    */   {
/*    */     try
/*    */     {
/* 67 */       return (TypeCode)this._exceptions.elementAt(paramInt); } catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
/*    */     }
/* 69 */     throw new Bounds();
/*    */   }
/*    */ 
/*    */   public void remove(int paramInt)
/*    */     throws Bounds
/*    */   {
/*    */     try
/*    */     {
/* 77 */       this._exceptions.removeElementAt(paramInt);
/*    */     } catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
/* 79 */       throw new Bounds();
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.corba.ExceptionListImpl
 * JD-Core Version:    0.6.2
 */