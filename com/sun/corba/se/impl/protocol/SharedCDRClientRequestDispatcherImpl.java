/*     */ package com.sun.corba.se.impl.protocol;
/*     */ 
/*     */ import com.sun.corba.se.impl.encoding.ByteBufferWithInfo;
/*     */ import com.sun.corba.se.impl.encoding.CDRInputObject;
/*     */ import com.sun.corba.se.impl.encoding.CDROutputObject;
/*     */ import com.sun.corba.se.impl.orbutil.ORBUtility;
/*     */ import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;
/*     */ import com.sun.corba.se.pept.encoding.InputObject;
/*     */ import com.sun.corba.se.pept.encoding.OutputObject;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.protocol.CorbaMessageMediator;
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import org.omg.CORBA.portable.ApplicationException;
/*     */ import org.omg.CORBA.portable.RemarshalException;
/*     */ 
/*     */ public class SharedCDRClientRequestDispatcherImpl extends CorbaClientRequestDispatcherImpl
/*     */ {
/*     */   public InputObject marshalingComplete(Object paramObject, OutputObject paramOutputObject)
/*     */     throws ApplicationException, RemarshalException
/*     */   {
/* 141 */     ORB localORB1 = null;
/* 142 */     CorbaMessageMediator localCorbaMessageMediator = null;
/*     */     try {
/* 144 */       localCorbaMessageMediator = (CorbaMessageMediator)paramOutputObject.getMessageMediator();
/*     */ 
/* 147 */       localORB1 = (ORB)localCorbaMessageMediator.getBroker();
/*     */ 
/* 149 */       if (localORB1.subcontractDebugFlag) {
/* 150 */         dprint(".marshalingComplete->: " + opAndId(localCorbaMessageMediator));
/*     */       }
/*     */ 
/* 153 */       CDROutputObject localCDROutputObject = (CDROutputObject)paramOutputObject;
/*     */ 
/* 159 */       ByteBufferWithInfo localByteBufferWithInfo = localCDROutputObject.getByteBufferWithInfo();
/* 160 */       localCDROutputObject.getMessageHeader().setSize(localByteBufferWithInfo.byteBuffer, localByteBufferWithInfo.getSize());
/* 161 */       final ORB localORB2 = localORB1;
/* 162 */       final ByteBuffer localByteBuffer1 = localByteBufferWithInfo.byteBuffer;
/* 163 */       final Message localMessage1 = localCDROutputObject.getMessageHeader();
/* 164 */       CDRInputObject localCDRInputObject1 = (CDRInputObject)AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public CDRInputObject run()
/*     */         {
/* 168 */           return new CDRInputObject(localORB2, null, localByteBuffer1, localMessage1);
/*     */         }
/*     */       });
/* 172 */       localCorbaMessageMediator.setInputObject(localCDRInputObject1);
/* 173 */       localCDRInputObject1.setMessageMediator(localCorbaMessageMediator);
/*     */ 
/* 180 */       ((CorbaMessageMediatorImpl)localCorbaMessageMediator).handleRequestRequest(localCorbaMessageMediator);
/*     */       try
/*     */       {
/* 186 */         localCDRInputObject1.close();
/*     */       }
/*     */       catch (IOException localIOException)
/*     */       {
/* 192 */         if (localORB1.transportDebugFlag) {
/* 193 */           dprint(".marshalingComplete: ignoring IOException - " + localIOException.toString());
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 201 */       localCDROutputObject = (CDROutputObject)localCorbaMessageMediator.getOutputObject();
/* 202 */       localByteBufferWithInfo = localCDROutputObject.getByteBufferWithInfo();
/* 203 */       localCDROutputObject.getMessageHeader().setSize(localByteBufferWithInfo.byteBuffer, localByteBufferWithInfo.getSize());
/* 204 */       final ORB localORB3 = localORB1;
/* 205 */       final ByteBuffer localByteBuffer2 = localByteBufferWithInfo.byteBuffer;
/* 206 */       final Message localMessage2 = localCDROutputObject.getMessageHeader();
/* 207 */       localCDRInputObject1 = (CDRInputObject)AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public CDRInputObject run()
/*     */         {
/* 211 */           return new CDRInputObject(localORB3, null, localByteBuffer2, localMessage2);
/*     */         }
/*     */       });
/* 215 */       localCorbaMessageMediator.setInputObject(localCDRInputObject1);
/* 216 */       localCDRInputObject1.setMessageMediator(localCorbaMessageMediator);
/*     */ 
/* 218 */       localCDRInputObject1.unmarshalHeader();
/*     */ 
/* 220 */       CDRInputObject localCDRInputObject2 = localCDRInputObject1;
/*     */ 
/* 222 */       return processResponse(localORB1, localCorbaMessageMediator, localCDRInputObject2);
/*     */     }
/*     */     finally {
/* 225 */       if (localORB1.subcontractDebugFlag)
/* 226 */         dprint(".marshalingComplete<-: " + opAndId(localCorbaMessageMediator));
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void dprint(String paramString)
/*     */   {
/* 233 */     ORBUtility.dprint("SharedCDRClientRequestDispatcherImpl", paramString);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.protocol.SharedCDRClientRequestDispatcherImpl
 * JD-Core Version:    0.6.2
 */