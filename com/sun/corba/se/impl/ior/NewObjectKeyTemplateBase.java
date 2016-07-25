/*    */ package com.sun.corba.se.impl.ior;
/*    */ 
/*    */ import com.sun.corba.se.impl.logging.IORSystemException;
/*    */ import com.sun.corba.se.spi.ior.ObjectAdapterId;
/*    */ import com.sun.corba.se.spi.ior.ObjectId;
/*    */ import com.sun.corba.se.spi.orb.ORB;
/*    */ import com.sun.corba.se.spi.orb.ORBVersion;
/*    */ import com.sun.corba.se.spi.orb.ORBVersionFactory;
/*    */ import org.omg.CORBA_2_3.portable.InputStream;
/*    */ import org.omg.CORBA_2_3.portable.OutputStream;
/*    */ 
/*    */ public abstract class NewObjectKeyTemplateBase extends ObjectKeyTemplateBase
/*    */ {
/*    */   public NewObjectKeyTemplateBase(ORB paramORB, int paramInt1, int paramInt2, int paramInt3, String paramString, ObjectAdapterId paramObjectAdapterId)
/*    */   {
/* 50 */     super(paramORB, paramInt1, paramInt2, paramInt3, paramString, paramObjectAdapterId);
/*    */ 
/* 53 */     if (paramInt1 != -1347695872)
/* 54 */       throw this.wrapper.badMagic(new Integer(paramInt1));
/*    */   }
/*    */ 
/*    */   public void write(ObjectId paramObjectId, OutputStream paramOutputStream)
/*    */   {
/* 59 */     super.write(paramObjectId, paramOutputStream);
/* 60 */     getORBVersion().write(paramOutputStream);
/*    */   }
/*    */ 
/*    */   public void write(OutputStream paramOutputStream)
/*    */   {
/* 65 */     super.write(paramOutputStream);
/* 66 */     getORBVersion().write(paramOutputStream);
/*    */   }
/*    */ 
/*    */   protected void setORBVersion(InputStream paramInputStream)
/*    */   {
/* 71 */     ORBVersion localORBVersion = ORBVersionFactory.create(paramInputStream);
/* 72 */     setORBVersion(localORBVersion);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.ior.NewObjectKeyTemplateBase
 * JD-Core Version:    0.6.2
 */