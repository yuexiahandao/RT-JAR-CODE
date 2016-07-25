package com.sun.xml.internal.ws.server;

import com.sun.org.glassfish.gmbal.Description;
import com.sun.org.glassfish.gmbal.InheritedAttributes;
import com.sun.org.glassfish.gmbal.ManagedData;

@ManagedData
@Description("WebServiceFeature")
@InheritedAttributes({@com.sun.org.glassfish.gmbal.InheritedAttribute(methodName="getID", description="unique id for this feature"), @com.sun.org.glassfish.gmbal.InheritedAttribute(methodName="isEnabled", description="true if this feature is enabled")})
abstract interface DummyWebServiceFeature
{
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.server.DummyWebServiceFeature
 * JD-Core Version:    0.6.2
 */