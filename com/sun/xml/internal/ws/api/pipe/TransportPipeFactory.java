/*     */ package com.sun.xml.internal.ws.api.pipe;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.ws.api.pipe.helper.PipeAdapter;
/*     */ 
/*     */ public abstract class TransportPipeFactory
/*     */ {
/*     */   public abstract Pipe doCreate(@NotNull ClientPipeAssemblerContext paramClientPipeAssemblerContext);
/*     */ 
/*     */   /** @deprecated */
/*     */   public static Pipe create(@Nullable ClassLoader classLoader, @NotNull ClientPipeAssemblerContext context)
/*     */   {
/* 114 */     return PipeAdapter.adapt(TransportTubeFactory.create(classLoader, context));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.pipe.TransportPipeFactory
 * JD-Core Version:    0.6.2
 */