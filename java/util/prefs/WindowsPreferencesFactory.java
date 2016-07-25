/*    */ package java.util.prefs;
/*    */ 
/*    */ class WindowsPreferencesFactory
/*    */   implements PreferencesFactory
/*    */ {
/*    */   public Preferences userRoot()
/*    */   {
/* 42 */     return WindowsPreferences.userRoot;
/*    */   }
/*    */ 
/*    */   public Preferences systemRoot()
/*    */   {
/* 49 */     return WindowsPreferences.systemRoot;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.prefs.WindowsPreferencesFactory
 * JD-Core Version:    0.6.2
 */