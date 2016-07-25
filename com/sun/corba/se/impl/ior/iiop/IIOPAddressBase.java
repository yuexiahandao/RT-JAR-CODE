/*    */ package com.sun.corba.se.impl.ior.iiop;
/*    */ 
/*    */ import com.sun.corba.se.spi.ior.iiop.IIOPAddress;
/*    */ import org.omg.CORBA_2_3.portable.OutputStream;
/*    */ 
/*    */ abstract class IIOPAddressBase
/*    */   implements IIOPAddress
/*    */ {
/*    */   protected short intToShort(int paramInt)
/*    */   {
/* 48 */     if (paramInt > 32767)
/* 49 */       return (short)(paramInt - 65536);
/* 50 */     return (short)paramInt;
/*    */   }
/*    */ 
/*    */   protected int shortToInt(short paramShort)
/*    */   {
/* 55 */     if (paramShort < 0)
/* 56 */       return paramShort + 65536;
/* 57 */     return paramShort;
/*    */   }
/*    */ 
/*    */   public void write(OutputStream paramOutputStream)
/*    */   {
/* 62 */     paramOutputStream.write_string(getHost());
/* 63 */     int i = getPort();
/* 64 */     paramOutputStream.write_short(intToShort(i));
/*    */   }
/*    */ 
/*    */   public boolean equals(Object paramObject)
/*    */   {
/* 69 */     if (!(paramObject instanceof IIOPAddress)) {
/* 70 */       return false;
/*    */     }
/* 72 */     IIOPAddress localIIOPAddress = (IIOPAddress)paramObject;
/*    */ 
/* 74 */     return (getHost().equals(localIIOPAddress.getHost())) && (getPort() == localIIOPAddress.getPort());
/*    */   }
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 80 */     return getHost().hashCode() ^ getPort();
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 85 */     return "IIOPAddress[" + getHost() + "," + getPort() + "]";
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.ior.iiop.IIOPAddressBase
 * JD-Core Version:    0.6.2
 */