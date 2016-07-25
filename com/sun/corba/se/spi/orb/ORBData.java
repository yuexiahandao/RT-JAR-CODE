package com.sun.corba.se.spi.orb;

import com.sun.corba.se.impl.encoding.CodeSetComponentInfo;
import com.sun.corba.se.impl.legacy.connection.USLPort;
import com.sun.corba.se.pept.transport.Acceptor;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import com.sun.corba.se.spi.transport.CorbaContactInfoListFactory;
import com.sun.corba.se.spi.transport.IIOPPrimaryToContactInfo;
import com.sun.corba.se.spi.transport.IORToSocketInfo;
import com.sun.corba.se.spi.transport.ReadTimeouts;
import org.omg.PortableInterceptor.ORBInitializer;

public abstract interface ORBData
{
  public abstract String getORBInitialHost();

  public abstract int getORBInitialPort();

  public abstract String getORBServerHost();

  public abstract int getORBServerPort();

  public abstract String getListenOnAllInterfaces();

  public abstract com.sun.corba.se.spi.legacy.connection.ORBSocketFactory getLegacySocketFactory();

  public abstract com.sun.corba.se.spi.transport.ORBSocketFactory getSocketFactory();

  public abstract USLPort[] getUserSpecifiedListenPorts();

  public abstract IORToSocketInfo getIORToSocketInfo();

  public abstract IIOPPrimaryToContactInfo getIIOPPrimaryToContactInfo();

  public abstract String getORBId();

  public abstract boolean getORBServerIdPropertySpecified();

  public abstract boolean isLocalOptimizationAllowed();

  public abstract GIOPVersion getGIOPVersion();

  public abstract int getHighWaterMark();

  public abstract int getLowWaterMark();

  public abstract int getNumberToReclaim();

  public abstract int getGIOPFragmentSize();

  public abstract int getGIOPBufferSize();

  public abstract int getGIOPBuffMgrStrategy(GIOPVersion paramGIOPVersion);

  public abstract short getGIOPTargetAddressPreference();

  public abstract short getGIOPAddressDisposition();

  public abstract boolean useByteOrderMarkers();

  public abstract boolean useByteOrderMarkersInEncapsulations();

  public abstract boolean alwaysSendCodeSetServiceContext();

  public abstract boolean getPersistentPortInitialized();

  public abstract int getPersistentServerPort();

  public abstract boolean getPersistentServerIdInitialized();

  public abstract int getPersistentServerId();

  public abstract boolean getServerIsORBActivated();

  public abstract Class getBadServerIdHandler();

  public abstract CodeSetComponentInfo getCodeSetComponentInfo();

  public abstract ORBInitializer[] getORBInitializers();

  public abstract StringPair[] getORBInitialReferences();

  public abstract String getORBDefaultInitialReference();

  public abstract String[] getORBDebugFlags();

  public abstract Acceptor[] getAcceptors();

  public abstract CorbaContactInfoListFactory getCorbaContactInfoListFactory();

  public abstract String acceptorSocketType();

  public abstract boolean acceptorSocketUseSelectThreadToWait();

  public abstract boolean acceptorSocketUseWorkerThreadForEvent();

  public abstract String connectionSocketType();

  public abstract boolean connectionSocketUseSelectThreadToWait();

  public abstract boolean connectionSocketUseWorkerThreadForEvent();

  public abstract ReadTimeouts getTransportTCPReadTimeouts();

  public abstract boolean disableDirectByteBufferUse();

  public abstract boolean isJavaSerializationEnabled();

  public abstract boolean useRepId();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.orb.ORBData
 * JD-Core Version:    0.6.2
 */