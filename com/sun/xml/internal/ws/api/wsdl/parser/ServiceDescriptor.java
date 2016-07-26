package com.sun.xml.internal.ws.api.wsdl.parser;

import com.sun.istack.internal.NotNull;

import java.util.List;
import javax.xml.transform.Source;

/**
 * 服务的描述类，可以通过下面的两个方法获得Service的描述信息
 */
public abstract class ServiceDescriptor {
    @NotNull
    // WSDL(网络服务描述语言)是Web Service的描述语言，它包含一系列描述某个web service的定义。
    public abstract List<? extends Source> getWSDLs();

    @NotNull
    public abstract List<? extends Source> getSchemas();
}