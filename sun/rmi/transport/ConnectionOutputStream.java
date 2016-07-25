/*     */ package sun.rmi.transport;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.rmi.server.UID;
/*     */ import sun.rmi.server.MarshalOutputStream;
/*     */ 
/*     */ class ConnectionOutputStream extends MarshalOutputStream
/*     */ {
/*     */   private final Connection conn;
/*     */   private final boolean resultStream;
/*     */   private final UID ackID;
/*  49 */   private DGCAckHandler dgcAckHandler = null;
/*     */ 
/*     */   ConnectionOutputStream(Connection paramConnection, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/*  62 */     super(paramConnection.getOutputStream());
/*  63 */     this.conn = paramConnection;
/*  64 */     this.resultStream = paramBoolean;
/*  65 */     this.ackID = (paramBoolean ? new UID() : null);
/*     */   }
/*     */ 
/*     */   void writeID() throws IOException {
/*  69 */     assert (this.resultStream);
/*  70 */     this.ackID.write(this);
/*     */   }
/*     */ 
/*     */   boolean isResultStream()
/*     */   {
/*  78 */     return this.resultStream;
/*     */   }
/*     */ 
/*     */   void saveObject(Object paramObject)
/*     */   {
/*  87 */     if (this.dgcAckHandler == null) {
/*  88 */       this.dgcAckHandler = new DGCAckHandler(this.ackID);
/*     */     }
/*  90 */     this.dgcAckHandler.add(paramObject);
/*     */   }
/*     */ 
/*     */   DGCAckHandler getDGCAckHandler()
/*     */   {
/* 101 */     return this.dgcAckHandler;
/*     */   }
/*     */ 
/*     */   void done() {
/* 105 */     if (this.dgcAckHandler != null)
/* 106 */       this.dgcAckHandler.startTimer();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.transport.ConnectionOutputStream
 * JD-Core Version:    0.6.2
 */