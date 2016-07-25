package com.sun.xml.internal.ws.api.model.wsdl;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.WSFeatureList;
import javax.xml.ws.WebServiceFeature;

public abstract interface WSDLFeaturedObject extends WSDLObject
{
  @Nullable
  public abstract <F extends WebServiceFeature> F getFeature(@NotNull Class<F> paramClass);

  @NotNull
  public abstract WSFeatureList getFeatures();

  public abstract void addFeature(@NotNull WebServiceFeature paramWebServiceFeature);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.model.wsdl.WSDLFeaturedObject
 * JD-Core Version:    0.6.2
 */