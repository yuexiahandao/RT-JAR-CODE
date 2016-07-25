/*     */ package com.sun.corba.se.impl.encoding;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.OMGSystemException;
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.impl.orbutil.ORBUtility;
/*     */ import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;
/*     */ import com.sun.corba.se.pept.protocol.MessageMediator;
/*     */ import com.sun.corba.se.spi.encoding.CorbaOutputObject;
/*     */ import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.orb.ORBData;
/*     */ import com.sun.corba.se.spi.protocol.CorbaMessageMediator;
/*     */ import com.sun.corba.se.spi.transport.CorbaConnection;
/*     */ import java.io.IOException;
/*     */ import java.nio.Buffer;
/*     */ import java.nio.ByteBuffer;
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ 
/*     */ public class CDROutputObject extends CorbaOutputObject
/*     */ {
/*     */   private Message header;
/*     */   private ORB orb;
/*     */   private ORBUtilSystemException wrapper;
/*     */   private OMGSystemException omgWrapper;
/*     */   private CorbaConnection connection;
/*     */ 
/*     */   private CDROutputObject(ORB paramORB, GIOPVersion paramGIOPVersion, Message paramMessage, BufferManagerWrite paramBufferManagerWrite, byte paramByte, CorbaMessageMediator paramCorbaMessageMediator)
/*     */   {
/*  79 */     super(paramORB, paramGIOPVersion, paramMessage.getEncodingVersion(), false, paramBufferManagerWrite, paramByte, (paramCorbaMessageMediator != null) && (paramCorbaMessageMediator.getConnection() != null) ? ((CorbaConnection)paramCorbaMessageMediator.getConnection()).shouldUseDirectByteBuffers() : false);
/*     */ 
/*  85 */     this.header = paramMessage;
/*  86 */     this.orb = paramORB;
/*  87 */     this.wrapper = ORBUtilSystemException.get(paramORB, "rpc.encoding");
/*  88 */     this.omgWrapper = OMGSystemException.get(paramORB, "rpc.encoding");
/*     */ 
/*  90 */     getBufferManager().setOutputObject(this);
/*  91 */     this.corbaMessageMediator = paramCorbaMessageMediator;
/*     */   }
/*     */ 
/*     */   public CDROutputObject(ORB paramORB, MessageMediator paramMessageMediator, Message paramMessage, byte paramByte)
/*     */   {
/*  99 */     this(paramORB, ((CorbaMessageMediator)paramMessageMediator).getGIOPVersion(), paramMessage, BufferManagerFactory.newBufferManagerWrite(((CorbaMessageMediator)paramMessageMediator).getGIOPVersion(), paramMessage.getEncodingVersion(), paramORB), paramByte, (CorbaMessageMediator)paramMessageMediator);
/*     */   }
/*     */ 
/*     */   public CDROutputObject(ORB paramORB, MessageMediator paramMessageMediator, Message paramMessage, byte paramByte, int paramInt)
/*     */   {
/* 120 */     this(paramORB, ((CorbaMessageMediator)paramMessageMediator).getGIOPVersion(), paramMessage, BufferManagerFactory.newBufferManagerWrite(paramInt, paramMessage.getEncodingVersion(), paramORB), paramByte, (CorbaMessageMediator)paramMessageMediator);
/*     */   }
/*     */ 
/*     */   public CDROutputObject(ORB paramORB, CorbaMessageMediator paramCorbaMessageMediator, GIOPVersion paramGIOPVersion, CorbaConnection paramCorbaConnection, Message paramMessage, byte paramByte)
/*     */   {
/* 140 */     this(paramORB, paramGIOPVersion, paramMessage, BufferManagerFactory.newBufferManagerWrite(paramGIOPVersion, paramMessage.getEncodingVersion(), paramORB), paramByte, paramCorbaMessageMediator);
/*     */ 
/* 150 */     this.connection = paramCorbaConnection;
/*     */   }
/*     */ 
/*     */   public Message getMessageHeader()
/*     */   {
/* 158 */     return this.header;
/*     */   }
/*     */ 
/*     */   public final void finishSendingMessage() {
/* 162 */     getBufferManager().sendMessage();
/*     */   }
/*     */ 
/*     */   public void writeTo(CorbaConnection paramCorbaConnection)
/*     */     throws IOException
/*     */   {
/* 179 */     ByteBufferWithInfo localByteBufferWithInfo = getByteBufferWithInfo();
/*     */ 
/* 181 */     getMessageHeader().setSize(localByteBufferWithInfo.byteBuffer, localByteBufferWithInfo.getSize());
/*     */ 
/* 183 */     if (orb() != null) {
/* 184 */       if (((ORB)orb()).transportDebugFlag) {
/* 185 */         dprint(".writeTo: " + paramCorbaConnection);
/*     */       }
/* 187 */       if (((ORB)orb()).giopDebugFlag) {
/* 188 */         CDROutputStream_1_0.printBuffer(localByteBufferWithInfo);
/*     */       }
/*     */     }
/* 191 */     localByteBufferWithInfo.byteBuffer.position(0).limit(localByteBufferWithInfo.getSize());
/* 192 */     paramCorbaConnection.write(localByteBufferWithInfo.byteBuffer);
/*     */   }
/*     */ 
/*     */   public InputStream create_input_stream()
/*     */   {
/* 199 */     return null;
/*     */   }
/*     */ 
/*     */   public CorbaConnection getConnection()
/*     */   {
/* 207 */     if (this.connection != null) {
/* 208 */       return this.connection;
/*     */     }
/* 210 */     return (CorbaConnection)this.corbaMessageMediator.getConnection();
/*     */   }
/*     */ 
/*     */   public final ByteBufferWithInfo getByteBufferWithInfo()
/*     */   {
/* 218 */     return super.getByteBufferWithInfo();
/*     */   }
/*     */ 
/*     */   public final void setByteBufferWithInfo(ByteBufferWithInfo paramByteBufferWithInfo)
/*     */   {
/* 223 */     super.setByteBufferWithInfo(paramByteBufferWithInfo);
/*     */   }
/*     */ 
/*     */   protected CodeSetConversion.CTBConverter createCharCTBConverter()
/*     */   {
/* 236 */     CodeSetComponentInfo.CodeSetContext localCodeSetContext = getCodeSets();
/*     */ 
/* 241 */     if (localCodeSetContext == null) {
/* 242 */       return super.createCharCTBConverter();
/*     */     }
/* 244 */     OSFCodeSetRegistry.Entry localEntry = OSFCodeSetRegistry.lookupEntry(localCodeSetContext.getCharCodeSet());
/*     */ 
/* 247 */     if (localEntry == null) {
/* 248 */       throw this.wrapper.unknownCodeset(localEntry);
/*     */     }
/* 250 */     return CodeSetConversion.impl().getCTBConverter(localEntry, isLittleEndian(), false);
/*     */   }
/*     */ 
/*     */   protected CodeSetConversion.CTBConverter createWCharCTBConverter()
/*     */   {
/* 257 */     CodeSetComponentInfo.CodeSetContext localCodeSetContext = getCodeSets();
/*     */ 
/* 262 */     if (localCodeSetContext == null) {
/* 263 */       if (getConnection().isServer()) {
/* 264 */         throw this.omgWrapper.noClientWcharCodesetCtx();
/*     */       }
/* 266 */       throw this.omgWrapper.noServerWcharCodesetCmp();
/*     */     }
/*     */ 
/* 269 */     OSFCodeSetRegistry.Entry localEntry = OSFCodeSetRegistry.lookupEntry(localCodeSetContext.getWCharCodeSet());
/*     */ 
/* 272 */     if (localEntry == null) {
/* 273 */       throw this.wrapper.unknownCodeset(localEntry);
/*     */     }
/* 275 */     boolean bool = ((ORB)orb()).getORBData().useByteOrderMarkers();
/*     */ 
/* 285 */     if (localEntry == OSFCodeSetRegistry.UTF_16) {
/* 286 */       if (getGIOPVersion().equals(GIOPVersion.V1_2)) {
/* 287 */         return CodeSetConversion.impl().getCTBConverter(localEntry, false, bool);
/*     */       }
/*     */ 
/* 292 */       if (getGIOPVersion().equals(GIOPVersion.V1_1)) {
/* 293 */         return CodeSetConversion.impl().getCTBConverter(localEntry, isLittleEndian(), false);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 300 */     return CodeSetConversion.impl().getCTBConverter(localEntry, isLittleEndian(), bool);
/*     */   }
/*     */ 
/*     */   private CodeSetComponentInfo.CodeSetContext getCodeSets()
/*     */   {
/* 311 */     if (getConnection() == null) {
/* 312 */       return CodeSetComponentInfo.LOCAL_CODE_SETS;
/*     */     }
/* 314 */     return getConnection().getCodeSetContext();
/*     */   }
/*     */ 
/*     */   protected void dprint(String paramString)
/*     */   {
/* 319 */     ORBUtility.dprint("CDROutputObject", paramString);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.encoding.CDROutputObject
 * JD-Core Version:    0.6.2
 */