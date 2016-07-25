package com.sun.java.browser.net;

import java.net.URL;

public abstract interface ProxyServiceProvider
{
  public abstract ProxyInfo[] getProxyInfo(URL paramURL);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.browser.net.ProxyServiceProvider
 * JD-Core Version:    0.6.2
 */