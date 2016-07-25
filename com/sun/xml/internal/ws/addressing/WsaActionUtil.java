/*    */ package com.sun.xml.internal.ws.addressing;
/*    */ 
/*    */ import com.sun.xml.internal.ws.api.model.CheckedException;
/*    */ import com.sun.xml.internal.ws.api.model.JavaMethod;
/*    */ import com.sun.xml.internal.ws.api.model.SEIModel;
/*    */ import java.net.URI;
/*    */ import java.net.URISyntaxException;
/*    */ import java.util.logging.Logger;
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ public class WsaActionUtil
/*    */ {
/* 65 */   private static final Logger LOGGER = Logger.getLogger(WsaActionUtil.class.getName());
/*    */ 
/*    */   public static final String getDefaultFaultAction(JavaMethod method, CheckedException ce)
/*    */   {
/* 40 */     String tns = method.getOwner().getTargetNamespace();
/* 41 */     String delim = getDelimiter(tns);
/* 42 */     if (tns.endsWith(delim)) {
/* 43 */       tns = tns.substring(0, tns.length() - 1);
/*    */     }
/*    */ 
/* 46 */     String name = method.getOperationName() + delim + "Fault" + delim + ce.getExceptionClass();
/*    */ 
/* 48 */     return tns + delim + method.getOwner().getPortTypeName().getLocalPart() + delim + method.getOperationName() + delim + "Fault" + delim + ce.getExceptionClass().getSimpleName();
/*    */   }
/*    */ 
/*    */   private static final String getDelimiter(String tns)
/*    */   {
/* 54 */     String delim = "/";
/*    */     try
/*    */     {
/* 57 */       URI uri = new URI(tns);
/* 58 */       if ((uri.getScheme() != null) && (uri.getScheme().equalsIgnoreCase("urn")))
/* 59 */         delim = ":";
/*    */     } catch (URISyntaxException e) {
/* 61 */       LOGGER.warning("TargetNamespace of WebService is not a valid URI");
/*    */     }
/* 63 */     return delim;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.addressing.WsaActionUtil
 * JD-Core Version:    0.6.2
 */