/*     */ package com.sun.xml.internal.ws.api.pipe.helper;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.xml.internal.ws.api.message.Packet;
/*     */ import com.sun.xml.internal.ws.api.pipe.Fiber;
/*     */ import com.sun.xml.internal.ws.api.pipe.NextAction;
/*     */ import com.sun.xml.internal.ws.api.pipe.Pipe;
/*     */ import com.sun.xml.internal.ws.api.pipe.PipeCloner;
/*     */ import com.sun.xml.internal.ws.api.pipe.Tube;
/*     */ import com.sun.xml.internal.ws.api.pipe.TubeCloner;
/*     */ 
/*     */ public class PipeAdapter extends AbstractTubeImpl
/*     */ {
/*     */   private final Pipe next;
/*     */ 
/*     */   public static Tube adapt(Pipe p)
/*     */   {
/*  50 */     if ((p instanceof Tube)) {
/*  51 */       return (Tube)p;
/*     */     }
/*  53 */     return new PipeAdapter(p);
/*     */   }
/*     */ 
/*     */   public static Pipe adapt(Tube p)
/*     */   {
/*  58 */     if ((p instanceof Pipe)) {
/*  59 */       return (Pipe)p;
/*     */     }
/*     */ 
/*  82 */     return new AbstractPipeImpl()
/*     */     {
/*     */       private final Tube t;
/*     */ 
/*     */       public Packet process(Packet request)
/*     */       {
/*  74 */         return Fiber.current().runSync(this.t, request);
/*     */       }
/*     */ 
/*     */       public Pipe copy(PipeCloner cloner) {
/*  78 */         return new 1TubeAdapter(this, cloner);
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   private PipeAdapter(Pipe next)
/*     */   {
/*  88 */     this.next = next;
/*     */   }
/*     */ 
/*     */   private PipeAdapter(PipeAdapter that, TubeCloner cloner)
/*     */   {
/*  95 */     super(that, cloner);
/*  96 */     this.next = ((PipeCloner)cloner).copy(that.next);
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public NextAction processRequest(@NotNull Packet p)
/*     */   {
/* 104 */     return doReturnWith(this.next.process(p));
/*     */   }
/*     */   @NotNull
/*     */   public NextAction processResponse(@NotNull Packet p) {
/* 108 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public NextAction processException(@NotNull Throwable t) {
/* 113 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */   public void preDestroy() {
/* 117 */     this.next.preDestroy();
/*     */   }
/*     */ 
/*     */   public PipeAdapter copy(TubeCloner cloner) {
/* 121 */     return new PipeAdapter(this, cloner);
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 125 */     return super.toString() + "[" + this.next.toString() + "]";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.pipe.helper.PipeAdapter
 * JD-Core Version:    0.6.2
 */