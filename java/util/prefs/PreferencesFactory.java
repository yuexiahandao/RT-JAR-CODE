package java.util.prefs;

public abstract interface PreferencesFactory
{
  public abstract Preferences systemRoot();

  public abstract Preferences userRoot();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.prefs.PreferencesFactory
 * JD-Core Version:    0.6.2
 */