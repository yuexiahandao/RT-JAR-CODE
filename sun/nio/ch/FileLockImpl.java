/*    */ package sun.nio.ch;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.channels.AsynchronousFileChannel;
/*    */ import java.nio.channels.Channel;
/*    */ import java.nio.channels.ClosedChannelException;
/*    */ import java.nio.channels.FileChannel;
/*    */ import java.nio.channels.FileLock;
/*    */ 
/*    */ public class FileLockImpl extends FileLock
/*    */ {
/* 34 */   private volatile boolean valid = true;
/*    */ 
/*    */   FileLockImpl(FileChannel paramFileChannel, long paramLong1, long paramLong2, boolean paramBoolean)
/*    */   {
/* 38 */     super(paramFileChannel, paramLong1, paramLong2, paramBoolean);
/*    */   }
/*    */ 
/*    */   FileLockImpl(AsynchronousFileChannel paramAsynchronousFileChannel, long paramLong1, long paramLong2, boolean paramBoolean)
/*    */   {
/* 43 */     super(paramAsynchronousFileChannel, paramLong1, paramLong2, paramBoolean);
/*    */   }
/*    */ 
/*    */   public boolean isValid() {
/* 47 */     return this.valid;
/*    */   }
/*    */ 
/*    */   void invalidate() {
/* 51 */     assert (Thread.holdsLock(this));
/* 52 */     this.valid = false;
/*    */   }
/*    */ 
/*    */   public synchronized void release() throws IOException {
/* 56 */     Channel localChannel = acquiredBy();
/* 57 */     if (!localChannel.isOpen())
/* 58 */       throw new ClosedChannelException();
/* 59 */     if (this.valid) {
/* 60 */       if ((localChannel instanceof FileChannelImpl))
/* 61 */         ((FileChannelImpl)localChannel).release(this);
/* 62 */       else if ((localChannel instanceof AsynchronousFileChannelImpl))
/* 63 */         ((AsynchronousFileChannelImpl)localChannel).release(this);
/* 64 */       else throw new AssertionError();
/* 65 */       this.valid = false;
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.ch.FileLockImpl
 * JD-Core Version:    0.6.2
 */