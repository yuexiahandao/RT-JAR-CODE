/*    */ package com.sun.corba.se.impl.transport;
/*    */ 
/*    */ import com.sun.corba.se.spi.ior.IOR;
/*    */ import com.sun.corba.se.spi.ior.iiop.AlternateIIOPAddressComponent;
/*    */ import com.sun.corba.se.spi.ior.iiop.IIOPAddress;
/*    */ import com.sun.corba.se.spi.ior.iiop.IIOPProfile;
/*    */ import com.sun.corba.se.spi.ior.iiop.IIOPProfileTemplate;
/*    */ import com.sun.corba.se.spi.transport.IORToSocketInfo;
/*    */ import com.sun.corba.se.spi.transport.SocketInfo;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ 
/*    */ public class DefaultIORToSocketInfoImpl
/*    */   implements IORToSocketInfo
/*    */ {
/*    */   public List getSocketInfo(IOR paramIOR)
/*    */   {
/* 49 */     ArrayList localArrayList = new ArrayList();
/*    */ 
/* 51 */     IIOPProfileTemplate localIIOPProfileTemplate = (IIOPProfileTemplate)paramIOR.getProfile().getTaggedProfileTemplate();
/*    */ 
/* 53 */     IIOPAddress localIIOPAddress = localIIOPProfileTemplate.getPrimaryAddress();
/* 54 */     String str = localIIOPAddress.getHost().toLowerCase();
/* 55 */     int i = localIIOPAddress.getPort();
/*    */ 
/* 61 */     SocketInfo localSocketInfo = createSocketInfo(str, i);
/* 62 */     localArrayList.add(localSocketInfo);
/*    */ 
/* 64 */     Iterator localIterator = localIIOPProfileTemplate.iteratorById(3);
/*    */ 
/* 67 */     while (localIterator.hasNext()) {
/* 68 */       AlternateIIOPAddressComponent localAlternateIIOPAddressComponent = (AlternateIIOPAddressComponent)localIterator.next();
/*    */ 
/* 70 */       str = localAlternateIIOPAddressComponent.getAddress().getHost().toLowerCase();
/* 71 */       i = localAlternateIIOPAddressComponent.getAddress().getPort();
/* 72 */       localSocketInfo = createSocketInfo(str, i);
/* 73 */       localArrayList.add(localSocketInfo);
/*    */     }
/* 75 */     return localArrayList;
/*    */   }
/*    */ 
/*    */   private SocketInfo createSocketInfo(final String paramString, final int paramInt)
/*    */   {
/* 80 */     return new SocketInfo() {
/* 81 */       public String getType() { return "IIOP_CLEAR_TEXT"; } 
/* 82 */       public String getHost() { return paramString; } 
/* 83 */       public int getPort() { return paramInt; }
/*    */ 
/*    */     };
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.transport.DefaultIORToSocketInfoImpl
 * JD-Core Version:    0.6.2
 */