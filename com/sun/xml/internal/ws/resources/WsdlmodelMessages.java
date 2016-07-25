/*    */ package com.sun.xml.internal.ws.resources;
/*    */ 
/*    */ import com.sun.xml.internal.ws.util.localization.Localizable;
/*    */ import com.sun.xml.internal.ws.util.localization.LocalizableMessageFactory;
/*    */ import com.sun.xml.internal.ws.util.localization.Localizer;
/*    */ 
/*    */ public final class WsdlmodelMessages
/*    */ {
/* 40 */   private static final LocalizableMessageFactory messageFactory = new LocalizableMessageFactory("com.sun.xml.internal.ws.resources.wsdlmodel");
/* 41 */   private static final Localizer localizer = new Localizer();
/*    */ 
/*    */   public static Localizable localizableWSDL_PORTADDRESS_EPRADDRESS_NOT_MATCH(Object arg0, Object arg1, Object arg2) {
/* 44 */     return messageFactory.getMessage("wsdl.portaddress.epraddress.not.match", new Object[] { arg0, arg1, arg2 });
/*    */   }
/*    */ 
/*    */   public static String WSDL_PORTADDRESS_EPRADDRESS_NOT_MATCH(Object arg0, Object arg1, Object arg2)
/*    */   {
/* 52 */     return localizer.localize(localizableWSDL_PORTADDRESS_EPRADDRESS_NOT_MATCH(arg0, arg1, arg2));
/*    */   }
/*    */ 
/*    */   public static Localizable localizableWSDL_IMPORT_SHOULD_BE_WSDL(Object arg0) {
/* 56 */     return messageFactory.getMessage("wsdl.import.should.be.wsdl", new Object[] { arg0 });
/*    */   }
/*    */ 
/*    */   public static String WSDL_IMPORT_SHOULD_BE_WSDL(Object arg0)
/*    */   {
/* 65 */     return localizer.localize(localizableWSDL_IMPORT_SHOULD_BE_WSDL(arg0));
/*    */   }
/*    */ 
/*    */   public static Localizable localizableMEX_METADATA_SYSTEMID_NULL() {
/* 69 */     return messageFactory.getMessage("Mex.metadata.systemid.null", new Object[0]);
/*    */   }
/*    */ 
/*    */   public static String MEX_METADATA_SYSTEMID_NULL()
/*    */   {
/* 77 */     return localizer.localize(localizableMEX_METADATA_SYSTEMID_NULL());
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.resources.WsdlmodelMessages
 * JD-Core Version:    0.6.2
 */