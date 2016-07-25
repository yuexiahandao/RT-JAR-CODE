/*     */ package com.sun.corba.se.impl.encoding;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.OMGSystemException;
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.impl.orbutil.ORBUtility;
/*     */ import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;
/*     */ import com.sun.corba.se.pept.encoding.InputObject;
/*     */ import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.transport.CorbaConnection;
/*     */ import com.sun.org.omg.SendingContext.CodeBase;
/*     */ import java.nio.ByteBuffer;
/*     */ 
/*     */ public class CDRInputObject extends CDRInputStream
/*     */   implements InputObject
/*     */ {
/*     */   private CorbaConnection corbaConnection;
/*     */   private Message header;
/*     */   private boolean unmarshaledHeader;
/*     */   private ORB orb;
/*     */   private ORBUtilSystemException wrapper;
/*     */   private OMGSystemException omgWrapper;
/*     */ 
/*     */   public CDRInputObject(ORB paramORB, CorbaConnection paramCorbaConnection, ByteBuffer paramByteBuffer, Message paramMessage)
/*     */   {
/*  74 */     super(paramORB, paramByteBuffer, paramMessage.getSize(), paramMessage.isLittleEndian(), paramMessage.getGIOPVersion(), paramMessage.getEncodingVersion(), BufferManagerFactory.newBufferManagerRead(paramMessage.getGIOPVersion(), paramMessage.getEncodingVersion(), paramORB));
/*     */ 
/*  81 */     this.corbaConnection = paramCorbaConnection;
/*  82 */     this.orb = paramORB;
/*  83 */     this.wrapper = ORBUtilSystemException.get(paramORB, "rpc.encoding");
/*     */ 
/*  85 */     this.omgWrapper = OMGSystemException.get(paramORB, "rpc.encoding");
/*     */ 
/*  88 */     if (paramORB.transportDebugFlag) {
/*  89 */       dprint(".CDRInputObject constructor:");
/*     */     }
/*     */ 
/*  92 */     getBufferManager().init(paramMessage);
/*     */ 
/*  94 */     this.header = paramMessage;
/*     */ 
/*  96 */     this.unmarshaledHeader = false;
/*     */ 
/*  98 */     setIndex(12);
/*     */ 
/* 100 */     setBufferLength(paramMessage.getSize());
/*     */   }
/*     */ 
/*     */   public final CorbaConnection getConnection()
/*     */   {
/* 109 */     return this.corbaConnection;
/*     */   }
/*     */ 
/*     */   public Message getMessageHeader()
/*     */   {
/* 118 */     return this.header;
/*     */   }
/*     */ 
/*     */   public void unmarshalHeader()
/*     */   {
/* 131 */     if (!this.unmarshaledHeader)
/*     */       try {
/* 133 */         if (((ORB)orb()).transportDebugFlag) {
/* 134 */           dprint(".unmarshalHeader->: " + getMessageHeader());
/*     */         }
/* 136 */         getMessageHeader().read(this);
/* 137 */         this.unmarshaledHeader = true;
/*     */       } catch (RuntimeException localRuntimeException) {
/* 139 */         if (((ORB)orb()).transportDebugFlag) {
/* 140 */           dprint(".unmarshalHeader: !!ERROR!!: " + getMessageHeader() + ": " + localRuntimeException);
/*     */         }
/*     */ 
/* 144 */         throw localRuntimeException;
/*     */       } finally {
/* 146 */         if (((ORB)orb()).transportDebugFlag)
/* 147 */           dprint(".unmarshalHeader<-: " + getMessageHeader());
/*     */       }
/*     */   }
/*     */ 
/*     */   public final boolean unmarshaledHeader()
/*     */   {
/* 155 */     return this.unmarshaledHeader;
/*     */   }
/*     */ 
/*     */   protected CodeSetConversion.BTCConverter createCharBTCConverter()
/*     */   {
/* 168 */     CodeSetComponentInfo.CodeSetContext localCodeSetContext = getCodeSets();
/*     */ 
/* 173 */     if (localCodeSetContext == null) {
/* 174 */       return super.createCharBTCConverter();
/*     */     }
/* 176 */     OSFCodeSetRegistry.Entry localEntry = OSFCodeSetRegistry.lookupEntry(localCodeSetContext.getCharCodeSet());
/*     */ 
/* 179 */     if (localEntry == null) {
/* 180 */       throw this.wrapper.unknownCodeset(localEntry);
/*     */     }
/* 182 */     return CodeSetConversion.impl().getBTCConverter(localEntry, isLittleEndian());
/*     */   }
/*     */ 
/*     */   protected CodeSetConversion.BTCConverter createWCharBTCConverter()
/*     */   {
/* 187 */     CodeSetComponentInfo.CodeSetContext localCodeSetContext = getCodeSets();
/*     */ 
/* 192 */     if (localCodeSetContext == null) {
/* 193 */       if (getConnection().isServer()) {
/* 194 */         throw this.omgWrapper.noClientWcharCodesetCtx();
/*     */       }
/* 196 */       throw this.omgWrapper.noServerWcharCodesetCmp();
/*     */     }
/*     */ 
/* 199 */     OSFCodeSetRegistry.Entry localEntry = OSFCodeSetRegistry.lookupEntry(localCodeSetContext.getWCharCodeSet());
/*     */ 
/* 202 */     if (localEntry == null) {
/* 203 */       throw this.wrapper.unknownCodeset(localEntry);
/*     */     }
/*     */ 
/* 213 */     if ((localEntry == OSFCodeSetRegistry.UTF_16) && 
/* 214 */       (getGIOPVersion().equals(GIOPVersion.V1_2))) {
/* 215 */       return CodeSetConversion.impl().getBTCConverter(localEntry, false);
/*     */     }
/*     */ 
/* 218 */     return CodeSetConversion.impl().getBTCConverter(localEntry, isLittleEndian());
/*     */   }
/*     */ 
/*     */   private CodeSetComponentInfo.CodeSetContext getCodeSets()
/*     */   {
/* 227 */     if (getConnection() == null) {
/* 228 */       return CodeSetComponentInfo.LOCAL_CODE_SETS;
/*     */     }
/* 230 */     return getConnection().getCodeSetContext();
/*     */   }
/*     */ 
/*     */   public final CodeBase getCodeBase() {
/* 234 */     if (getConnection() == null) {
/* 235 */       return null;
/*     */     }
/* 237 */     return getConnection().getCodeBase();
/*     */   }
/*     */ 
/*     */   public CDRInputStream dup()
/*     */   {
/* 257 */     return null;
/*     */   }
/*     */ 
/*     */   protected void dprint(String paramString)
/*     */   {
/* 263 */     ORBUtility.dprint("CDRInputObject", paramString);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.encoding.CDRInputObject
 * JD-Core Version:    0.6.2
 */