/*    */ package sun.misc.resources;
/*    */ 
/*    */ import java.util.ListResourceBundle;
/*    */ 
/*    */ public class Messages extends ListResourceBundle
/*    */ {
/* 46 */   private static final Object[][] contents = { { "optpkg.versionerror", "ERROR: Invalid version format used in {0} JAR file. Check the documentation for the supported version format." }, { "optpkg.attributeerror", "ERROR: The required {0} JAR manifest attribute is not set in {1} JAR file." }, { "optpkg.attributeserror", "ERROR: Some required JAR manifest attributes are not set in {0} JAR file." } };
/*    */ 
/*    */   public Object[][] getContents()
/*    */   {
/* 43 */     return contents;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.resources.Messages
 * JD-Core Version:    0.6.2
 */