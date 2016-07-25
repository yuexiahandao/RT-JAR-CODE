/*     */ package sun.rmi.log;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import sun.rmi.server.MarshalInputStream;
/*     */ import sun.rmi.server.MarshalOutputStream;
/*     */ 
/*     */ public abstract class LogHandler
/*     */ {
/*     */   public abstract Object initialSnapshot()
/*     */     throws Exception;
/*     */ 
/*     */   public void snapshot(OutputStream paramOutputStream, Object paramObject)
/*     */     throws Exception
/*     */   {
/*  71 */     MarshalOutputStream localMarshalOutputStream = new MarshalOutputStream(paramOutputStream);
/*  72 */     localMarshalOutputStream.writeObject(paramObject);
/*  73 */     localMarshalOutputStream.flush();
/*     */   }
/*     */ 
/*     */   public Object recover(InputStream paramInputStream)
/*     */     throws Exception
/*     */   {
/*  87 */     MarshalInputStream localMarshalInputStream = new MarshalInputStream(paramInputStream);
/*  88 */     return localMarshalInputStream.readObject();
/*     */   }
/*     */ 
/*     */   public void writeUpdate(LogOutputStream paramLogOutputStream, Object paramObject)
/*     */     throws Exception
/*     */   {
/* 102 */     MarshalOutputStream localMarshalOutputStream = new MarshalOutputStream(paramLogOutputStream);
/* 103 */     localMarshalOutputStream.writeObject(paramObject);
/* 104 */     localMarshalOutputStream.flush();
/*     */   }
/*     */ 
/*     */   public Object readUpdate(LogInputStream paramLogInputStream, Object paramObject)
/*     */     throws Exception
/*     */   {
/* 121 */     MarshalInputStream localMarshalInputStream = new MarshalInputStream(paramLogInputStream);
/* 122 */     return applyUpdate(localMarshalInputStream.readObject(), paramObject);
/*     */   }
/*     */ 
/*     */   public abstract Object applyUpdate(Object paramObject1, Object paramObject2)
/*     */     throws Exception;
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.log.LogHandler
 * JD-Core Version:    0.6.2
 */