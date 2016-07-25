/*     */ package sun.corba;
/*     */ 
/*     */ import com.sun.corba.se.impl.encoding.EncapsInputStream;
/*     */ import com.sun.corba.se.impl.encoding.TypeCodeInputStream;
/*     */ import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
/*     */ import com.sun.org.omg.SendingContext.CodeBase;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import org.omg.CORBA.ORB;
/*     */ 
/*     */ public class EncapsInputStreamFactory
/*     */ {
/*     */   public static EncapsInputStream newEncapsInputStream(ORB paramORB, final byte[] paramArrayOfByte, final int paramInt, final boolean paramBoolean, final GIOPVersion paramGIOPVersion)
/*     */   {
/*  45 */     return (EncapsInputStream)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public EncapsInputStream run()
/*     */       {
/*  49 */         return new EncapsInputStream(this.val$orb, paramArrayOfByte, paramInt, paramBoolean, paramGIOPVersion);
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public static EncapsInputStream newEncapsInputStream(ORB paramORB, final ByteBuffer paramByteBuffer, final int paramInt, final boolean paramBoolean, final GIOPVersion paramGIOPVersion)
/*     */   {
/*  59 */     return (EncapsInputStream)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public EncapsInputStream run()
/*     */       {
/*  63 */         return new EncapsInputStream(this.val$orb, paramByteBuffer, paramInt, paramBoolean, paramGIOPVersion);
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public static EncapsInputStream newEncapsInputStream(ORB paramORB, final byte[] paramArrayOfByte, final int paramInt)
/*     */   {
/*  71 */     return (EncapsInputStream)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public EncapsInputStream run()
/*     */       {
/*  75 */         return new EncapsInputStream(this.val$orb, paramArrayOfByte, paramInt);
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public static EncapsInputStream newEncapsInputStream(EncapsInputStream paramEncapsInputStream)
/*     */   {
/*  82 */     return (EncapsInputStream)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public EncapsInputStream run()
/*     */       {
/*  86 */         return new EncapsInputStream(this.val$eis);
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public static EncapsInputStream newEncapsInputStream(ORB paramORB, final byte[] paramArrayOfByte, final int paramInt, final GIOPVersion paramGIOPVersion)
/*     */   {
/*  94 */     return (EncapsInputStream)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public EncapsInputStream run()
/*     */       {
/*  98 */         return new EncapsInputStream(this.val$orb, paramArrayOfByte, paramInt, paramGIOPVersion);
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public static EncapsInputStream newEncapsInputStream(ORB paramORB, final byte[] paramArrayOfByte, final int paramInt, final GIOPVersion paramGIOPVersion, final CodeBase paramCodeBase)
/*     */   {
/* 106 */     return (EncapsInputStream)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public EncapsInputStream run()
/*     */       {
/* 110 */         return new EncapsInputStream(this.val$orb, paramArrayOfByte, paramInt, paramGIOPVersion, paramCodeBase);
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public static TypeCodeInputStream newTypeCodeInputStream(ORB paramORB, final byte[] paramArrayOfByte, final int paramInt, final boolean paramBoolean, final GIOPVersion paramGIOPVersion)
/*     */   {
/* 119 */     return (TypeCodeInputStream)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public TypeCodeInputStream run()
/*     */       {
/* 123 */         return new TypeCodeInputStream(this.val$orb, paramArrayOfByte, paramInt, paramBoolean, paramGIOPVersion);
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public static TypeCodeInputStream newTypeCodeInputStream(ORB paramORB, final ByteBuffer paramByteBuffer, final int paramInt, final boolean paramBoolean, final GIOPVersion paramGIOPVersion)
/*     */   {
/* 133 */     return (TypeCodeInputStream)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public TypeCodeInputStream run()
/*     */       {
/* 137 */         return new TypeCodeInputStream(this.val$orb, paramByteBuffer, paramInt, paramBoolean, paramGIOPVersion);
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public static TypeCodeInputStream newTypeCodeInputStream(ORB paramORB, final byte[] paramArrayOfByte, final int paramInt)
/*     */   {
/* 145 */     return (TypeCodeInputStream)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public TypeCodeInputStream run()
/*     */       {
/* 149 */         return new TypeCodeInputStream(this.val$orb, paramArrayOfByte, paramInt);
/*     */       }
/*     */     });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.corba.EncapsInputStreamFactory
 * JD-Core Version:    0.6.2
 */