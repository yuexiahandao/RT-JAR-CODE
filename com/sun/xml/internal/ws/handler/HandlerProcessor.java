/*     */ package com.sun.xml.internal.ws.handler;
/*     */ 
/*     */ import com.sun.xml.internal.ws.api.WSBinding;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.ws.ProtocolException;
/*     */ import javax.xml.ws.handler.Handler;
/*     */ import javax.xml.ws.handler.MessageContext;
/*     */ 
/*     */ abstract class HandlerProcessor<C extends MessageUpdatableContext>
/*     */ {
/*     */   boolean isClient;
/*  44 */   static final Logger logger = Logger.getLogger("com.sun.xml.internal.ws.handler");
/*     */   private List<? extends Handler> handlers;
/*     */   WSBinding binding;
/*  57 */   private int index = -1;
/*     */   private HandlerTube owner;
/*     */ 
/*     */   protected HandlerProcessor(HandlerTube owner, WSBinding binding, List<? extends Handler> chain)
/*     */   {
/*  70 */     this.owner = owner;
/*  71 */     if (chain == null) {
/*  72 */       chain = new ArrayList();
/*     */     }
/*  74 */     this.handlers = chain;
/*  75 */     this.binding = binding;
/*     */   }
/*     */ 
/*     */   int getIndex()
/*     */   {
/*  83 */     return this.index;
/*     */   }
/*     */ 
/*     */   void setIndex(int i)
/*     */   {
/*  90 */     this.index = i;
/*     */   }
/*     */ 
/*     */   public boolean callHandlersRequest(Direction direction, C context, boolean responseExpected)
/*     */   {
/* 118 */     setDirection(direction, context);
/*     */     boolean result;
/*     */     try
/*     */     {
/*     */       boolean result;
/* 122 */       if (direction == Direction.OUTBOUND)
/* 123 */         result = callHandleMessage(context, 0, this.handlers.size() - 1);
/*     */       else
/* 125 */         result = callHandleMessage(context, this.handlers.size() - 1, 0);
/*     */     }
/*     */     catch (ProtocolException pe) {
/* 128 */       logger.log(Level.FINER, "exception in handler chain", pe);
/* 129 */       if (responseExpected)
/*     */       {
/* 131 */         insertFaultMessage(context, pe);
/*     */ 
/* 133 */         reverseDirection(direction, context);
/*     */ 
/* 135 */         setHandleFaultProperty();
/*     */ 
/* 137 */         if (direction == Direction.OUTBOUND)
/* 138 */           callHandleFault(context, getIndex() - 1, 0);
/*     */         else {
/* 140 */           callHandleFault(context, getIndex() + 1, this.handlers.size() - 1);
/*     */         }
/* 142 */         return false;
/*     */       }
/* 144 */       throw pe;
/*     */     } catch (RuntimeException re) {
/* 146 */       logger.log(Level.FINER, "exception in handler chain", re);
/* 147 */       throw re;
/*     */     }
/*     */ 
/* 150 */     if (!result) {
/* 151 */       if (responseExpected)
/*     */       {
/* 153 */         reverseDirection(direction, context);
/*     */ 
/* 155 */         if (direction == Direction.OUTBOUND)
/* 156 */           callHandleMessageReverse(context, getIndex() - 1, 0);
/*     */         else {
/* 158 */           callHandleMessageReverse(context, getIndex() + 1, this.handlers.size() - 1);
/*     */         }
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 164 */         setHandleFalseProperty();
/*     */       }
/* 166 */       return false;
/*     */     }
/*     */ 
/* 169 */     return result;
/*     */   }
/*     */ 
/*     */   public void callHandlersResponse(Direction direction, C context, boolean isFault)
/*     */   {
/* 185 */     setDirection(direction, context);
/*     */     try {
/* 187 */       if (isFault)
/*     */       {
/* 189 */         if (direction == Direction.OUTBOUND)
/* 190 */           callHandleFault(context, 0, this.handlers.size() - 1);
/*     */         else {
/* 192 */           callHandleFault(context, this.handlers.size() - 1, 0);
/*     */         }
/*     */ 
/*     */       }
/* 196 */       else if (direction == Direction.OUTBOUND)
/* 197 */         callHandleMessageReverse(context, 0, this.handlers.size() - 1);
/*     */       else
/* 199 */         callHandleMessageReverse(context, this.handlers.size() - 1, 0);
/*     */     }
/*     */     catch (RuntimeException re)
/*     */     {
/* 203 */       logger.log(Level.FINER, "exception in handler chain", re);
/* 204 */       throw re;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void reverseDirection(Direction origDirection, C context)
/*     */   {
/* 214 */     if (origDirection == Direction.OUTBOUND)
/* 215 */       context.put("javax.xml.ws.handler.message.outbound", Boolean.valueOf(false));
/*     */     else
/* 217 */       context.put("javax.xml.ws.handler.message.outbound", Boolean.valueOf(true));
/*     */   }
/*     */ 
/*     */   private void setDirection(Direction direction, C context)
/*     */   {
/* 226 */     if (direction == Direction.OUTBOUND)
/* 227 */       context.put("javax.xml.ws.handler.message.outbound", Boolean.valueOf(true));
/*     */     else
/* 229 */       context.put("javax.xml.ws.handler.message.outbound", Boolean.valueOf(false));
/*     */   }
/*     */ 
/*     */   private void setHandleFaultProperty()
/*     */   {
/* 238 */     this.owner.setHandleFault();
/*     */   }
/*     */ 
/*     */   private void setHandleFalseProperty()
/*     */   {
/* 246 */     this.owner.setHandleFalse();
/*     */   }
/*     */ 
/*     */   abstract void insertFaultMessage(C paramC, ProtocolException paramProtocolException);
/*     */ 
/*     */   private boolean callHandleMessage(C context, int start, int end)
/*     */   {
/* 270 */     int i = start;
/*     */     try {
/* 272 */       if (start > end) {
/* 273 */         while (i >= end) {
/* 274 */           if (!((Handler)this.handlers.get(i)).handleMessage(context)) {
/* 275 */             setIndex(i);
/* 276 */             return false;
/*     */           }
/* 278 */           i--;
/*     */         }
/*     */       }
/* 281 */       while (i <= end) {
/* 282 */         if (!((Handler)this.handlers.get(i)).handleMessage(context)) {
/* 283 */           setIndex(i);
/* 284 */           return false;
/*     */         }
/* 286 */         i++;
/*     */       }
/*     */     }
/*     */     catch (RuntimeException e) {
/* 290 */       setIndex(i);
/* 291 */       throw e;
/*     */     }
/* 293 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean callHandleMessageReverse(C context, int start, int end)
/*     */   {
/* 307 */     if ((this.handlers.isEmpty()) || (start == -1) || (start == this.handlers.size()))
/*     */     {
/* 310 */       return false;
/*     */     }
/*     */ 
/* 313 */     int i = start;
/*     */ 
/* 315 */     if (start > end) {
/* 316 */       while (i >= end) {
/* 317 */         if (!((Handler)this.handlers.get(i)).handleMessage(context))
/*     */         {
/* 319 */           setHandleFalseProperty();
/* 320 */           return false;
/*     */         }
/* 322 */         i--;
/*     */       }
/*     */     }
/* 325 */     while (i <= end) {
/* 326 */       if (!((Handler)this.handlers.get(i)).handleMessage(context))
/*     */       {
/* 328 */         setHandleFalseProperty();
/* 329 */         return false;
/*     */       }
/* 331 */       i++;
/*     */     }
/*     */ 
/* 334 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean callHandleFault(C context, int start, int end)
/*     */   {
/* 345 */     if ((this.handlers.isEmpty()) || (start == -1) || (start == this.handlers.size()))
/*     */     {
/* 348 */       return false;
/*     */     }
/*     */ 
/* 351 */     int i = start;
/* 352 */     if (start > end)
/*     */       try {
/* 354 */         while (i >= end) {
/* 355 */           if (!((Handler)this.handlers.get(i)).handleFault(context)) {
/* 356 */             return false;
/*     */           }
/* 358 */           i--;
/*     */         }
/*     */       } catch (RuntimeException re) {
/* 361 */         logger.log(Level.FINER, "exception in handler chain", re);
/*     */ 
/* 363 */         throw re;
/*     */       }
/*     */     try
/*     */     {
/* 367 */       while (i <= end) {
/* 368 */         if (!((Handler)this.handlers.get(i)).handleFault(context)) {
/* 369 */           return false;
/*     */         }
/* 371 */         i++;
/*     */       }
/*     */     } catch (RuntimeException re) {
/* 374 */       logger.log(Level.FINER, "exception in handler chain", re);
/*     */ 
/* 376 */       throw re;
/*     */     }
/*     */ 
/* 379 */     return true;
/*     */   }
/*     */ 
/*     */   void closeHandlers(MessageContext context, int start, int end)
/*     */   {
/* 388 */     if ((this.handlers.isEmpty()) || (start == -1))
/*     */     {
/* 390 */       return;
/*     */     }
/* 392 */     if (start > end) {
/* 393 */       for (int i = start; i >= end; i--) {
/*     */         try {
/* 395 */           ((Handler)this.handlers.get(i)).close(context);
/*     */         } catch (RuntimeException re) {
/* 397 */           logger.log(Level.INFO, "Exception ignored during close", re);
/*     */         }
/*     */       }
/*     */     }
/*     */     else
/* 402 */       for (int i = start; i <= end; i++)
/*     */         try {
/* 404 */           ((Handler)this.handlers.get(i)).close(context);
/*     */         } catch (RuntimeException re) {
/* 406 */           logger.log(Level.INFO, "Exception ignored during close", re);
/*     */         }
/*     */   }
/*     */ 
/*     */   public static enum Direction
/*     */   {
/*  52 */     OUTBOUND, INBOUND;
/*     */   }
/*     */ 
/*     */   public static enum RequestOrResponse
/*     */   {
/*  49 */     REQUEST, RESPONSE;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.handler.HandlerProcessor
 * JD-Core Version:    0.6.2
 */