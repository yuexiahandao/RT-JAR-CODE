/*     */ package sun.corba;
/*     */ 
/*     */ import com.sun.corba.se.impl.encoding.CDROutputObject;
/*     */ import com.sun.corba.se.impl.encoding.EncapsOutputStream;
/*     */ import com.sun.corba.se.impl.encoding.TypeCodeOutputStream;
/*     */ import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;
/*     */ import com.sun.corba.se.pept.protocol.MessageMediator;
/*     */ import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.protocol.CorbaMessageMediator;
/*     */ import com.sun.corba.se.spi.transport.CorbaConnection;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ 
/*     */ public final class OutputStreamFactory
/*     */ {
/*     */   public static TypeCodeOutputStream newTypeCodeOutputStream(ORB paramORB)
/*     */   {
/*  52 */     return (TypeCodeOutputStream)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public TypeCodeOutputStream run()
/*     */       {
/*  56 */         return new TypeCodeOutputStream(this.val$orb);
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public static TypeCodeOutputStream newTypeCodeOutputStream(ORB paramORB, final boolean paramBoolean)
/*     */   {
/*  63 */     return (TypeCodeOutputStream)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public TypeCodeOutputStream run()
/*     */       {
/*  67 */         return new TypeCodeOutputStream(this.val$orb, paramBoolean);
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public static EncapsOutputStream newEncapsOutputStream(ORB paramORB)
/*     */   {
/*  74 */     return (EncapsOutputStream)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public EncapsOutputStream run()
/*     */       {
/*  78 */         return new EncapsOutputStream(this.val$orb);
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public static EncapsOutputStream newEncapsOutputStream(ORB paramORB, final GIOPVersion paramGIOPVersion)
/*     */   {
/*  86 */     return (EncapsOutputStream)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public EncapsOutputStream run()
/*     */       {
/*  90 */         return new EncapsOutputStream(this.val$orb, paramGIOPVersion);
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public static EncapsOutputStream newEncapsOutputStream(ORB paramORB, final boolean paramBoolean)
/*     */   {
/*  98 */     return (EncapsOutputStream)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public EncapsOutputStream run()
/*     */       {
/* 102 */         return new EncapsOutputStream(this.val$orb, paramBoolean);
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public static CDROutputObject newCDROutputObject(ORB paramORB, final MessageMediator paramMessageMediator, final Message paramMessage, final byte paramByte)
/*     */   {
/* 111 */     return (CDROutputObject)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public CDROutputObject run()
/*     */       {
/* 115 */         return new CDROutputObject(this.val$orb, paramMessageMediator, paramMessage, paramByte);
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public static CDROutputObject newCDROutputObject(ORB paramORB, final MessageMediator paramMessageMediator, final Message paramMessage, final byte paramByte, final int paramInt)
/*     */   {
/* 125 */     return (CDROutputObject)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public CDROutputObject run()
/*     */       {
/* 129 */         return new CDROutputObject(this.val$orb, paramMessageMediator, paramMessage, paramByte, paramInt);
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public static CDROutputObject newCDROutputObject(ORB paramORB, final CorbaMessageMediator paramCorbaMessageMediator, final GIOPVersion paramGIOPVersion, final CorbaConnection paramCorbaConnection, final Message paramMessage, final byte paramByte)
/*     */   {
/* 139 */     return (CDROutputObject)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public CDROutputObject run()
/*     */       {
/* 143 */         return new CDROutputObject(this.val$orb, paramCorbaMessageMediator, paramGIOPVersion, paramCorbaConnection, paramMessage, paramByte);
/*     */       }
/*     */     });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.corba.OutputStreamFactory
 * JD-Core Version:    0.6.2
 */