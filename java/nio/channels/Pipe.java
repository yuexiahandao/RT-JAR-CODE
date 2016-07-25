/*     */ package java.nio.channels;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.channels.spi.AbstractSelectableChannel;
/*     */ import java.nio.channels.spi.SelectorProvider;
/*     */ 
/*     */ public abstract class Pipe
/*     */ {
/*     */   public abstract SourceChannel source();
/*     */ 
/*     */   public abstract SinkChannel sink();
/*     */ 
/*     */   public static Pipe open()
/*     */     throws IOException
/*     */   {
/* 150 */     return SelectorProvider.provider().openPipe();
/*     */   }
/*     */ 
/*     */   public static abstract class SinkChannel extends AbstractSelectableChannel
/*     */     implements WritableByteChannel, GatheringByteChannel
/*     */   {
/*     */     protected SinkChannel(SelectorProvider paramSelectorProvider)
/*     */     {
/*  99 */       super();
/*     */     }
/*     */ 
/*     */     public final int validOps()
/*     */     {
/* 112 */       return 4;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static abstract class SourceChannel extends AbstractSelectableChannel
/*     */     implements ReadableByteChannel, ScatteringByteChannel
/*     */   {
/*     */     protected SourceChannel(SelectorProvider paramSelectorProvider)
/*     */     {
/*  68 */       super();
/*     */     }
/*     */ 
/*     */     public final int validOps()
/*     */     {
/*  81 */       return 1;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.channels.Pipe
 * JD-Core Version:    0.6.2
 */