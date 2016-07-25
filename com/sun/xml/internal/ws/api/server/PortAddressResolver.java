/*    */ package com.sun.xml.internal.ws.api.server;
/*    */ 
/*    */ import com.sun.istack.internal.NotNull;
/*    */ import com.sun.istack.internal.Nullable;
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ public abstract class PortAddressResolver
/*    */ {
/*    */   @Nullable
/*    */   public abstract String getAddressFor(@NotNull QName paramQName, @NotNull String paramString);
/*    */ 
/*    */   @Nullable
/*    */   public String getAddressFor(@NotNull QName serviceName, @NotNull String portName, String currentAddress)
/*    */   {
/* 78 */     return getAddressFor(serviceName, portName);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.server.PortAddressResolver
 * JD-Core Version:    0.6.2
 */