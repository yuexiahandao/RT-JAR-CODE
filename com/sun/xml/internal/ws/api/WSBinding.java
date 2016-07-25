package com.sun.xml.internal.ws.api;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
import java.util.List;
import javax.xml.ws.Binding;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.handler.Handler;

public abstract interface WSBinding extends Binding
{
  public abstract SOAPVersion getSOAPVersion();

  public abstract AddressingVersion getAddressingVersion();

  @NotNull
  public abstract BindingID getBindingId();

  @NotNull
  public abstract List<Handler> getHandlerChain();

  public abstract boolean isFeatureEnabled(@NotNull Class<? extends WebServiceFeature> paramClass);

  @Nullable
  public abstract <F extends WebServiceFeature> F getFeature(@NotNull Class<F> paramClass);

  @NotNull
  public abstract WSFeatureList getFeatures();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.WSBinding
 * JD-Core Version:    0.6.2
 */