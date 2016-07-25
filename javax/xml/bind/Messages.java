/*    */ package javax.xml.bind;
/*    */ 
/*    */ import java.text.MessageFormat;
/*    */ import java.util.ResourceBundle;
/*    */ 
/*    */ class Messages
/*    */ {
/*    */   static final String PROVIDER_NOT_FOUND = "ContextFinder.ProviderNotFound";
/*    */   static final String COULD_NOT_INSTANTIATE = "ContextFinder.CouldNotInstantiate";
/*    */   static final String CANT_FIND_PROPERTIES_FILE = "ContextFinder.CantFindPropertiesFile";
/*    */   static final String CANT_MIX_PROVIDERS = "ContextFinder.CantMixProviders";
/*    */   static final String MISSING_PROPERTY = "ContextFinder.MissingProperty";
/*    */   static final String NO_PACKAGE_IN_CONTEXTPATH = "ContextFinder.NoPackageInContextPath";
/*    */   static final String NAME_VALUE = "PropertyException.NameValue";
/*    */   static final String CONVERTER_MUST_NOT_BE_NULL = "DatatypeConverter.ConverterMustNotBeNull";
/*    */   static final String ILLEGAL_CAST = "JAXBContext.IllegalCast";
/*    */   static final String FAILED_TO_INITIALE_DATATYPE_FACTORY = "FAILED_TO_INITIALE_DATATYPE_FACTORY";
/*    */ 
/*    */   static String format(String property)
/*    */   {
/* 37 */     return format(property, null);
/*    */   }
/*    */ 
/*    */   static String format(String property, Object arg1) {
/* 41 */     return format(property, new Object[] { arg1 });
/*    */   }
/*    */ 
/*    */   static String format(String property, Object arg1, Object arg2) {
/* 45 */     return format(property, new Object[] { arg1, arg2 });
/*    */   }
/*    */ 
/*    */   static String format(String property, Object arg1, Object arg2, Object arg3) {
/* 49 */     return format(property, new Object[] { arg1, arg2, arg3 });
/*    */   }
/*    */ 
/*    */   static String format(String property, Object[] args)
/*    */   {
/* 56 */     String text = ResourceBundle.getBundle(Messages.class.getName()).getString(property);
/* 57 */     return MessageFormat.format(text, args);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.bind.Messages
 * JD-Core Version:    0.6.2
 */