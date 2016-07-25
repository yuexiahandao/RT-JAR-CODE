/*    */ package com.sun.xml.internal.ws.api.pipe.helper;
/*    */ 
/*    */ import com.sun.istack.internal.NotNull;
/*    */ import com.sun.xml.internal.ws.api.message.Packet;
/*    */ import com.sun.xml.internal.ws.api.pipe.NextAction;
/*    */ import com.sun.xml.internal.ws.api.pipe.Tube;
/*    */ import com.sun.xml.internal.ws.api.pipe.TubeCloner;
/*    */ 
/*    */ public abstract class AbstractFilterTubeImpl extends AbstractTubeImpl
/*    */ {
/*    */   protected final Tube next;
/*    */ 
/*    */   protected AbstractFilterTubeImpl(Tube next)
/*    */   {
/* 48 */     this.next = next;
/*    */   }
/*    */ 
/*    */   protected AbstractFilterTubeImpl(AbstractFilterTubeImpl that, TubeCloner cloner) {
/* 52 */     super(that, cloner);
/* 53 */     if (that.next != null)
/* 54 */       this.next = cloner.copy(that.next);
/*    */     else
/* 56 */       this.next = null;
/*    */   }
/*    */ 
/*    */   @NotNull
/*    */   public NextAction processRequest(Packet request)
/*    */   {
/* 64 */     return doInvoke(this.next, request);
/*    */   }
/*    */ 
/*    */   @NotNull
/*    */   public NextAction processResponse(Packet response)
/*    */   {
/* 71 */     return doReturnWith(response);
/*    */   }
/*    */ 
/*    */   @NotNull
/*    */   public NextAction processException(Throwable t)
/*    */   {
/* 78 */     return doThrow(t);
/*    */   }
/*    */ 
/*    */   public void preDestroy() {
/* 82 */     this.next.preDestroy();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.pipe.helper.AbstractFilterTubeImpl
 * JD-Core Version:    0.6.2
 */