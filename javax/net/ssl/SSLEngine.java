/*      */ package javax.net.ssl;
/*      */ 
/*      */ import java.nio.ByteBuffer;
/*      */ 
/*      */ public abstract class SSLEngine
/*      */ {
/*  373 */   private String peerHost = null;
/*  374 */   private int peerPort = -1;
/*      */ 
/*      */   protected SSLEngine()
/*      */   {
/*      */   }
/*      */ 
/*      */   protected SSLEngine(String paramString, int paramInt)
/*      */   {
/*  406 */     this.peerHost = paramString;
/*  407 */     this.peerPort = paramInt;
/*      */   }
/*      */ 
/*      */   public String getPeerHost()
/*      */   {
/*  420 */     return this.peerHost;
/*      */   }
/*      */ 
/*      */   public int getPeerPort()
/*      */   {
/*  433 */     return this.peerPort;
/*      */   }
/*      */ 
/*      */   public SSLEngineResult wrap(ByteBuffer paramByteBuffer1, ByteBuffer paramByteBuffer2)
/*      */     throws SSLException
/*      */   {
/*  469 */     return wrap(new ByteBuffer[] { paramByteBuffer1 }, 0, 1, paramByteBuffer2);
/*      */   }
/*      */ 
/*      */   public SSLEngineResult wrap(ByteBuffer[] paramArrayOfByteBuffer, ByteBuffer paramByteBuffer)
/*      */     throws SSLException
/*      */   {
/*  506 */     if (paramArrayOfByteBuffer == null) {
/*  507 */       throw new IllegalArgumentException("src == null");
/*      */     }
/*  509 */     return wrap(paramArrayOfByteBuffer, 0, paramArrayOfByteBuffer.length, paramByteBuffer);
/*      */   }
/*      */ 
/*      */   public abstract SSLEngineResult wrap(ByteBuffer[] paramArrayOfByteBuffer, int paramInt1, int paramInt2, ByteBuffer paramByteBuffer)
/*      */     throws SSLException;
/*      */ 
/*      */   public SSLEngineResult unwrap(ByteBuffer paramByteBuffer1, ByteBuffer paramByteBuffer2)
/*      */     throws SSLException
/*      */   {
/*  624 */     return unwrap(paramByteBuffer1, new ByteBuffer[] { paramByteBuffer2 }, 0, 1);
/*      */   }
/*      */ 
/*      */   public SSLEngineResult unwrap(ByteBuffer paramByteBuffer, ByteBuffer[] paramArrayOfByteBuffer)
/*      */     throws SSLException
/*      */   {
/*  661 */     if (paramArrayOfByteBuffer == null) {
/*  662 */       throw new IllegalArgumentException("dsts == null");
/*      */     }
/*  664 */     return unwrap(paramByteBuffer, paramArrayOfByteBuffer, 0, paramArrayOfByteBuffer.length);
/*      */   }
/*      */ 
/*      */   public abstract SSLEngineResult unwrap(ByteBuffer paramByteBuffer, ByteBuffer[] paramArrayOfByteBuffer, int paramInt1, int paramInt2)
/*      */     throws SSLException;
/*      */ 
/*      */   public abstract Runnable getDelegatedTask();
/*      */ 
/*      */   public abstract void closeInbound()
/*      */     throws SSLException;
/*      */ 
/*      */   public abstract boolean isInboundDone();
/*      */ 
/*      */   public abstract void closeOutbound();
/*      */ 
/*      */   public abstract boolean isOutboundDone();
/*      */ 
/*      */   public abstract String[] getSupportedCipherSuites();
/*      */ 
/*      */   public abstract String[] getEnabledCipherSuites();
/*      */ 
/*      */   public abstract void setEnabledCipherSuites(String[] paramArrayOfString);
/*      */ 
/*      */   public abstract String[] getSupportedProtocols();
/*      */ 
/*      */   public abstract String[] getEnabledProtocols();
/*      */ 
/*      */   public abstract void setEnabledProtocols(String[] paramArrayOfString);
/*      */ 
/*      */   public abstract SSLSession getSession();
/*      */ 
/*      */   public SSLSession getHandshakeSession()
/*      */   {
/* 1007 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public abstract void beginHandshake()
/*      */     throws SSLException;
/*      */ 
/*      */   public abstract SSLEngineResult.HandshakeStatus getHandshakeStatus();
/*      */ 
/*      */   public abstract void setUseClientMode(boolean paramBoolean);
/*      */ 
/*      */   public abstract boolean getUseClientMode();
/*      */ 
/*      */   public abstract void setNeedClientAuth(boolean paramBoolean);
/*      */ 
/*      */   public abstract boolean getNeedClientAuth();
/*      */ 
/*      */   public abstract void setWantClientAuth(boolean paramBoolean);
/*      */ 
/*      */   public abstract boolean getWantClientAuth();
/*      */ 
/*      */   public abstract void setEnableSessionCreation(boolean paramBoolean);
/*      */ 
/*      */   public abstract boolean getEnableSessionCreation();
/*      */ 
/*      */   public SSLParameters getSSLParameters()
/*      */   {
/* 1201 */     SSLParameters localSSLParameters = new SSLParameters();
/* 1202 */     localSSLParameters.setCipherSuites(getEnabledCipherSuites());
/* 1203 */     localSSLParameters.setProtocols(getEnabledProtocols());
/* 1204 */     if (getNeedClientAuth())
/* 1205 */       localSSLParameters.setNeedClientAuth(true);
/* 1206 */     else if (getWantClientAuth()) {
/* 1207 */       localSSLParameters.setWantClientAuth(true);
/*      */     }
/* 1209 */     return localSSLParameters;
/*      */   }
/*      */ 
/*      */   public void setSSLParameters(SSLParameters paramSSLParameters)
/*      */   {
/* 1235 */     String[] arrayOfString = paramSSLParameters.getCipherSuites();
/* 1236 */     if (arrayOfString != null) {
/* 1237 */       setEnabledCipherSuites(arrayOfString);
/*      */     }
/* 1239 */     arrayOfString = paramSSLParameters.getProtocols();
/* 1240 */     if (arrayOfString != null) {
/* 1241 */       setEnabledProtocols(arrayOfString);
/*      */     }
/* 1243 */     if (paramSSLParameters.getNeedClientAuth())
/* 1244 */       setNeedClientAuth(true);
/* 1245 */     else if (paramSSLParameters.getWantClientAuth())
/* 1246 */       setWantClientAuth(true);
/*      */     else
/* 1248 */       setWantClientAuth(false);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.net.ssl.SSLEngine
 * JD-Core Version:    0.6.2
 */