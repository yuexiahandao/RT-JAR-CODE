/*     */ package sun.security.smartcardio;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import javax.smartcardio.ATR;
/*     */ import javax.smartcardio.Card;
/*     */ import javax.smartcardio.CardChannel;
/*     */ import javax.smartcardio.CardException;
/*     */ import javax.smartcardio.CardPermission;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ final class CardImpl extends Card
/*     */ {
/*     */   private final TerminalImpl terminal;
/*     */   final long cardId;
/*     */   private final ATR atr;
/*     */   final int protocol;
/*     */   private final ChannelImpl basicChannel;
/*     */   private volatile State state;
/*     */   private volatile Thread exclusiveThread;
/*  75 */   private static final boolean isWindows = str.startsWith("Windows");
/*     */ 
/* 177 */   private static byte[] commandOpenChannel = { 0, 112, 0, 0, 1 };
/*     */ 
/* 258 */   private static final boolean invertReset = Boolean.parseBoolean((String)AccessController.doPrivileged(new GetPropertyAction("sun.security.smartcardio.invertCardReset", "true")));
/*     */ 
/*     */   CardImpl(TerminalImpl paramTerminalImpl, String paramString)
/*     */     throws PCSCException
/*     */   {
/*  79 */     this.terminal = paramTerminalImpl;
/*  80 */     int i = 2;
/*     */     int j;
/*  82 */     if (paramString.equals("*")) {
/*  83 */       j = 3;
/*  84 */     } else if (paramString.equalsIgnoreCase("T=0")) {
/*  85 */       j = 1;
/*  86 */     } else if (paramString.equalsIgnoreCase("T=1")) {
/*  87 */       j = 2;
/*  88 */     } else if (paramString.equalsIgnoreCase("direct"))
/*     */     {
/*  94 */       j = isWindows ? 0 : 65536;
/*     */ 
/*  96 */       i = 3;
/*     */     } else {
/*  98 */       throw new IllegalArgumentException("Unsupported protocol " + paramString);
/*     */     }
/* 100 */     this.cardId = PCSC.SCardConnect(paramTerminalImpl.contextId, paramTerminalImpl.name, i, j);
/*     */ 
/* 102 */     byte[] arrayOfByte1 = new byte[2];
/* 103 */     byte[] arrayOfByte2 = PCSC.SCardStatus(this.cardId, arrayOfByte1);
/* 104 */     this.atr = new ATR(arrayOfByte2);
/* 105 */     this.protocol = (arrayOfByte1[1] & 0xFF);
/* 106 */     this.basicChannel = new ChannelImpl(this, 0);
/* 107 */     this.state = State.OK;
/*     */   }
/*     */ 
/*     */   void checkState() {
/* 111 */     State localState = this.state;
/* 112 */     if (localState == State.DISCONNECTED)
/* 113 */       throw new IllegalStateException("Card has been disconnected");
/* 114 */     if (localState == State.REMOVED)
/* 115 */       throw new IllegalStateException("Card has been removed");
/*     */   }
/*     */ 
/*     */   boolean isValid()
/*     */   {
/* 120 */     if (this.state != State.OK) {
/* 121 */       return false;
/*     */     }
/*     */     try
/*     */     {
/* 125 */       PCSC.SCardStatus(this.cardId, new byte[2]);
/* 126 */       return true;
/*     */     } catch (PCSCException localPCSCException) {
/* 128 */       this.state = State.REMOVED;
/* 129 */     }return false;
/*     */   }
/*     */ 
/*     */   private void checkSecurity(String paramString)
/*     */   {
/* 134 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 135 */     if (localSecurityManager != null)
/* 136 */       localSecurityManager.checkPermission(new CardPermission(this.terminal.name, paramString));
/*     */   }
/*     */ 
/*     */   void handleError(PCSCException paramPCSCException)
/*     */   {
/* 141 */     if (paramPCSCException.code == -2146434967)
/* 142 */       this.state = State.REMOVED;
/*     */   }
/*     */ 
/*     */   public ATR getATR()
/*     */   {
/* 147 */     return this.atr;
/*     */   }
/*     */ 
/*     */   public String getProtocol() {
/* 151 */     switch (this.protocol) {
/*     */     case 1:
/* 153 */       return "T=0";
/*     */     case 2:
/* 155 */       return "T=1";
/*     */     }
/*     */ 
/* 158 */     return "Unknown protocol " + this.protocol;
/*     */   }
/*     */ 
/*     */   public CardChannel getBasicChannel()
/*     */   {
/* 163 */     checkSecurity("getBasicChannel");
/* 164 */     checkState();
/* 165 */     return this.basicChannel;
/*     */   }
/*     */ 
/*     */   private static int getSW(byte[] paramArrayOfByte) {
/* 169 */     if (paramArrayOfByte.length < 2) {
/* 170 */       return -1;
/*     */     }
/* 172 */     int i = paramArrayOfByte[(paramArrayOfByte.length - 2)] & 0xFF;
/* 173 */     int j = paramArrayOfByte[(paramArrayOfByte.length - 1)] & 0xFF;
/* 174 */     return i << 8 | j;
/*     */   }
/*     */ 
/*     */   public CardChannel openLogicalChannel()
/*     */     throws CardException
/*     */   {
/* 180 */     checkSecurity("openLogicalChannel");
/* 181 */     checkState();
/* 182 */     checkExclusive();
/*     */     try {
/* 184 */       byte[] arrayOfByte = PCSC.SCardTransmit(this.cardId, this.protocol, commandOpenChannel, 0, commandOpenChannel.length);
/*     */ 
/* 186 */       if ((arrayOfByte.length != 3) || (getSW(arrayOfByte) != 36864)) {
/* 187 */         throw new CardException("openLogicalChannel() failed, card response: " + PCSC.toString(arrayOfByte));
/*     */       }
/*     */ 
/* 191 */       return new ChannelImpl(this, arrayOfByte[0]);
/*     */     } catch (PCSCException localPCSCException) {
/* 193 */       handleError(localPCSCException);
/* 194 */       throw new CardException("openLogicalChannel() failed", localPCSCException);
/*     */     }
/*     */   }
/*     */ 
/*     */   void checkExclusive() throws CardException {
/* 199 */     Thread localThread = this.exclusiveThread;
/* 200 */     if (localThread == null) {
/* 201 */       return;
/*     */     }
/* 203 */     if (localThread != Thread.currentThread())
/* 204 */       throw new CardException("Exclusive access established by another Thread");
/*     */   }
/*     */ 
/*     */   public synchronized void beginExclusive() throws CardException
/*     */   {
/* 209 */     checkSecurity("exclusive");
/* 210 */     checkState();
/* 211 */     if (this.exclusiveThread != null) {
/* 212 */       throw new CardException("Exclusive access has already been assigned to Thread " + this.exclusiveThread.getName());
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 217 */       PCSC.SCardBeginTransaction(this.cardId);
/*     */     } catch (PCSCException localPCSCException) {
/* 219 */       handleError(localPCSCException);
/* 220 */       throw new CardException("beginExclusive() failed", localPCSCException);
/*     */     }
/* 222 */     this.exclusiveThread = Thread.currentThread();
/*     */   }
/*     */ 
/*     */   public synchronized void endExclusive() throws CardException {
/* 226 */     checkState();
/* 227 */     if (this.exclusiveThread != Thread.currentThread()) {
/* 228 */       throw new IllegalStateException("Exclusive access not assigned to current Thread");
/*     */     }
/*     */     try
/*     */     {
/* 232 */       PCSC.SCardEndTransaction(this.cardId, 0);
/*     */     } catch (PCSCException localPCSCException) {
/* 234 */       handleError(localPCSCException);
/* 235 */       throw new CardException("endExclusive() failed", localPCSCException);
/*     */     } finally {
/* 237 */       this.exclusiveThread = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public byte[] transmitControlCommand(int paramInt, byte[] paramArrayOfByte) throws CardException
/*     */   {
/* 243 */     checkSecurity("transmitControl");
/* 244 */     checkState();
/* 245 */     checkExclusive();
/* 246 */     if (paramArrayOfByte == null)
/* 247 */       throw new NullPointerException();
/*     */     try
/*     */     {
/* 250 */       return PCSC.SCardControl(this.cardId, paramInt, paramArrayOfByte);
/*     */     }
/*     */     catch (PCSCException localPCSCException) {
/* 253 */       handleError(localPCSCException);
/* 254 */       throw new CardException("transmitControlCommand() failed", localPCSCException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void disconnect(boolean paramBoolean)
/*     */     throws CardException
/*     */   {
/* 265 */     if (paramBoolean) {
/* 266 */       checkSecurity("reset");
/*     */     }
/* 268 */     if (this.state != State.OK) {
/* 269 */       return;
/*     */     }
/* 271 */     checkExclusive();
/*     */ 
/* 273 */     if (invertReset)
/* 274 */       paramBoolean = !paramBoolean;
/*     */     try
/*     */     {
/* 277 */       PCSC.SCardDisconnect(this.cardId, paramBoolean ? 1 : 0);
/*     */     } catch (PCSCException localPCSCException) {
/* 279 */       throw new CardException("disconnect() failed", localPCSCException);
/*     */     } finally {
/* 281 */       this.state = State.DISCONNECTED;
/* 282 */       this.exclusiveThread = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 287 */     return "PC/SC card in " + this.terminal.getName() + ", protocol " + getProtocol() + ", state " + this.state;
/*     */   }
/*     */ 
/*     */   protected void finalize() throws Throwable
/*     */   {
/*     */     try {
/* 293 */       if (this.state == State.OK)
/* 294 */         PCSC.SCardDisconnect(this.cardId, 0);
/*     */     }
/*     */     finally {
/* 297 */       super.finalize();
/*     */     }
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  69 */     String str = (String)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public String run() {
/*  72 */         return System.getProperty("os.name");
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private static enum State
/*     */   {
/*  42 */     OK, REMOVED, DISCONNECTED;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.smartcardio.CardImpl
 * JD-Core Version:    0.6.2
 */