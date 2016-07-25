/*     */ package com.sun.xml.internal.ws.api.pipe;
/*     */ 
/*     */ import com.sun.xml.internal.ws.api.message.Packet;
/*     */ 
/*     */ public final class NextAction
/*     */ {
/*     */   int kind;
/*     */   Tube next;
/*     */   Packet packet;
/*     */   Throwable throwable;
/*     */   static final int INVOKE = 0;
/*     */   static final int INVOKE_AND_FORGET = 1;
/*     */   static final int RETURN = 2;
/*     */   static final int THROW = 3;
/*     */   static final int SUSPEND = 4;
/*     */ 
/*     */   private void set(int k, Tube v, Packet p, Throwable t)
/*     */   {
/*  57 */     this.kind = k;
/*  58 */     this.next = v;
/*  59 */     this.packet = p;
/*  60 */     this.throwable = t;
/*     */   }
/*     */ 
/*     */   public void invoke(Tube next, Packet p)
/*     */   {
/*  70 */     set(0, next, p, null);
/*     */   }
/*     */ 
/*     */   public void invokeAndForget(Tube next, Packet p)
/*     */   {
/*  80 */     set(1, next, p, null);
/*     */   }
/*     */ 
/*     */   public void returnWith(Packet response)
/*     */   {
/*  88 */     set(2, null, response, null);
/*     */   }
/*     */ 
/*     */   public void throwException(Throwable t)
/*     */   {
/* 101 */     assert (((t instanceof RuntimeException)) || ((t instanceof Error)));
/* 102 */     set(3, null, null, t);
/*     */   }
/*     */ 
/*     */   public void suspend()
/*     */   {
/* 110 */     set(4, null, null, null);
/*     */   }
/*     */ 
/*     */   public void suspend(Tube next)
/*     */   {
/* 119 */     set(4, next, null, null);
/*     */   }
/*     */ 
/*     */   public Tube getNext() {
/* 123 */     return this.next;
/*     */   }
/*     */ 
/*     */   public Packet getPacket() {
/* 127 */     return this.packet;
/*     */   }
/*     */ 
/*     */   public Throwable getThrowable() {
/* 131 */     return this.throwable;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 139 */     StringBuilder buf = new StringBuilder();
/* 140 */     buf.append(super.toString()).append(" [");
/* 141 */     buf.append("kind=").append(getKindString()).append(',');
/* 142 */     buf.append("next=").append(this.next).append(',');
/* 143 */     buf.append("packet=").append(this.packet).append(',');
/* 144 */     buf.append("throwable=").append(this.throwable).append(']');
/* 145 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   public String getKindString()
/*     */   {
/* 152 */     switch (this.kind) { case 0:
/* 153 */       return "INVOKE";
/*     */     case 1:
/* 154 */       return "INVOKE_AND_FORGET";
/*     */     case 2:
/* 155 */       return "RETURN";
/*     */     case 3:
/* 156 */       return "THROW";
/*     */     case 4:
/* 157 */       return "SUSPEND"; }
/* 158 */     throw new AssertionError(this.kind);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.pipe.NextAction
 * JD-Core Version:    0.6.2
 */