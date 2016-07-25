/*     */ package com.sun.xml.internal.ws.api.pipe.helper;
/*     */ 
/*     */ import com.sun.xml.internal.ws.api.message.Packet;
/*     */ import com.sun.xml.internal.ws.api.pipe.Fiber;
/*     */ import com.sun.xml.internal.ws.api.pipe.NextAction;
/*     */ import com.sun.xml.internal.ws.api.pipe.Pipe;
/*     */ import com.sun.xml.internal.ws.api.pipe.PipeCloner;
/*     */ import com.sun.xml.internal.ws.api.pipe.Tube;
/*     */ import com.sun.xml.internal.ws.api.pipe.TubeCloner;
/*     */ 
/*     */ public abstract class AbstractTubeImpl
/*     */   implements Tube, Pipe
/*     */ {
/*     */   protected AbstractTubeImpl()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected AbstractTubeImpl(AbstractTubeImpl that, TubeCloner cloner)
/*     */   {
/*  57 */     cloner.add(that, this);
/*     */   }
/*     */ 
/*     */   protected final NextAction doInvoke(Tube next, Packet packet) {
/*  61 */     NextAction na = new NextAction();
/*  62 */     na.invoke(next, packet);
/*  63 */     return na;
/*     */   }
/*     */ 
/*     */   protected final NextAction doInvokeAndForget(Tube next, Packet packet) {
/*  67 */     NextAction na = new NextAction();
/*  68 */     na.invokeAndForget(next, packet);
/*  69 */     return na;
/*     */   }
/*     */ 
/*     */   protected final NextAction doReturnWith(Packet response) {
/*  73 */     NextAction na = new NextAction();
/*  74 */     na.returnWith(response);
/*  75 */     return na;
/*     */   }
/*     */ 
/*     */   protected final NextAction doSuspend() {
/*  79 */     NextAction na = new NextAction();
/*  80 */     na.suspend();
/*  81 */     return na;
/*     */   }
/*     */ 
/*     */   protected final NextAction doSuspend(Tube next) {
/*  85 */     NextAction na = new NextAction();
/*  86 */     na.suspend(next);
/*  87 */     return na;
/*     */   }
/*     */ 
/*     */   protected final NextAction doThrow(Throwable t) {
/*  91 */     NextAction na = new NextAction();
/*  92 */     na.throwException(t);
/*  93 */     return na;
/*     */   }
/*     */ 
/*     */   public Packet process(Packet p)
/*     */   {
/* 101 */     return Fiber.current().runSync(this, p);
/*     */   }
/*     */ 
/*     */   public final AbstractTubeImpl copy(PipeCloner cloner)
/*     */   {
/* 109 */     return copy(cloner);
/*     */   }
/*     */ 
/*     */   public abstract AbstractTubeImpl copy(TubeCloner paramTubeCloner);
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.pipe.helper.AbstractTubeImpl
 * JD-Core Version:    0.6.2
 */