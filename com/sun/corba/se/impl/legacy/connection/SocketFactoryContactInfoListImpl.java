/*    */ package com.sun.corba.se.impl.legacy.connection;
/*    */ 
/*    */ import com.sun.corba.se.impl.transport.CorbaContactInfoListImpl;
/*    */ import com.sun.corba.se.spi.ior.IOR;
/*    */ import com.sun.corba.se.spi.orb.ORB;
/*    */ import java.util.Iterator;
/*    */ 
/*    */ public class SocketFactoryContactInfoListImpl extends CorbaContactInfoListImpl
/*    */ {
/*    */   public SocketFactoryContactInfoListImpl(ORB paramORB)
/*    */   {
/* 45 */     super(paramORB);
/*    */   }
/*    */ 
/*    */   public SocketFactoryContactInfoListImpl(ORB paramORB, IOR paramIOR)
/*    */   {
/* 50 */     super(paramORB, paramIOR);
/*    */   }
/*    */ 
/*    */   public Iterator iterator()
/*    */   {
/* 60 */     return new SocketFactoryContactInfoListIteratorImpl(this.orb, this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.legacy.connection.SocketFactoryContactInfoListImpl
 * JD-Core Version:    0.6.2
 */