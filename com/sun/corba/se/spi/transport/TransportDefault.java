/*    */ package com.sun.corba.se.spi.transport;
/*    */ 
/*    */ import com.sun.corba.se.impl.protocol.CorbaClientDelegateImpl;
/*    */ import com.sun.corba.se.impl.transport.CorbaContactInfoListImpl;
/*    */ import com.sun.corba.se.impl.transport.ReadTCPTimeoutsImpl;
/*    */ import com.sun.corba.se.spi.ior.IOR;
/*    */ import com.sun.corba.se.spi.orb.ORB;
/*    */ import com.sun.corba.se.spi.protocol.ClientDelegateFactory;
/*    */ import com.sun.corba.se.spi.protocol.CorbaClientDelegate;
/*    */ 
/*    */ public abstract class TransportDefault
/*    */ {
/*    */   public static CorbaContactInfoListFactory makeCorbaContactInfoListFactory(ORB paramORB)
/*    */   {
/* 51 */     return new CorbaContactInfoListFactory() {
/*    */       public void setORB(ORB paramAnonymousORB) {
/*    */       }
/* 54 */       public CorbaContactInfoList create(IOR paramAnonymousIOR) { return new CorbaContactInfoListImpl(this.val$broker, paramAnonymousIOR); }
/*    */ 
/*    */     };
/*    */   }
/*    */ 
/*    */   public static ClientDelegateFactory makeClientDelegateFactory(ORB paramORB)
/*    */   {
/* 63 */     return new ClientDelegateFactory() {
/*    */       public CorbaClientDelegate create(CorbaContactInfoList paramAnonymousCorbaContactInfoList) {
/* 65 */         return new CorbaClientDelegateImpl(this.val$broker, paramAnonymousCorbaContactInfoList);
/*    */       }
/*    */     };
/*    */   }
/*    */ 
/*    */   public static IORTransformer makeIORTransformer(ORB paramORB)
/*    */   {
/* 74 */     return null;
/*    */   }
/*    */ 
/*    */   public static ReadTimeoutsFactory makeReadTimeoutsFactory()
/*    */   {
/* 79 */     return new ReadTimeoutsFactory()
/*    */     {
/*    */       public ReadTimeouts create(int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3, int paramAnonymousInt4)
/*    */       {
/* 85 */         return new ReadTCPTimeoutsImpl(paramAnonymousInt1, paramAnonymousInt2, paramAnonymousInt3, paramAnonymousInt4);
/*    */       }
/*    */     };
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.transport.TransportDefault
 * JD-Core Version:    0.6.2
 */