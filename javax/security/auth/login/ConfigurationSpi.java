package javax.security.auth.login;

public abstract class ConfigurationSpi
{
  protected abstract AppConfigurationEntry[] engineGetAppConfigurationEntry(String paramString);

  protected void engineRefresh()
  {
  }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.security.auth.login.ConfigurationSpi
 * JD-Core Version:    0.6.2
 */