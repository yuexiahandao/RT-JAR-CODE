/*     */ package java.nio.channels;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.nio.channels.spi.SelectorProvider;
/*     */ import java.util.Set;
/*     */ 
/*     */ public abstract class Selector
/*     */   implements Closeable
/*     */ {
/*     */   public static Selector open()
/*     */     throws IOException
/*     */   {
/* 227 */     return SelectorProvider.provider().openSelector();
/*     */   }
/*     */ 
/*     */   public abstract boolean isOpen();
/*     */ 
/*     */   public abstract SelectorProvider provider();
/*     */ 
/*     */   public abstract Set<SelectionKey> keys();
/*     */ 
/*     */   public abstract Set<SelectionKey> selectedKeys();
/*     */ 
/*     */   public abstract int selectNow()
/*     */     throws IOException;
/*     */ 
/*     */   public abstract int select(long paramLong)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract int select()
/*     */     throws IOException;
/*     */ 
/*     */   public abstract Selector wakeup();
/*     */ 
/*     */   public abstract void close()
/*     */     throws IOException;
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.channels.Selector
 * JD-Core Version:    0.6.2
 */