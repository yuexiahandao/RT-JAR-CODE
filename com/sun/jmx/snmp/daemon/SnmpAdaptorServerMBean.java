package com.sun.jmx.snmp.daemon;

import com.sun.jmx.snmp.InetAddressAcl;
import com.sun.jmx.snmp.SnmpIpAddress;
import com.sun.jmx.snmp.SnmpOid;
import com.sun.jmx.snmp.SnmpPduFactory;
import com.sun.jmx.snmp.SnmpPduPacket;
import com.sun.jmx.snmp.SnmpPeer;
import com.sun.jmx.snmp.SnmpStatusException;
import com.sun.jmx.snmp.SnmpTimeticks;
import com.sun.jmx.snmp.SnmpVarBindList;
import com.sun.jmx.snmp.agent.SnmpMibAgent;
import com.sun.jmx.snmp.agent.SnmpMibHandler;
import com.sun.jmx.snmp.agent.SnmpUserDataFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Vector;

public abstract interface SnmpAdaptorServerMBean extends CommunicatorServerMBean
{
  public abstract InetAddressAcl getInetAddressAcl();

  public abstract Integer getTrapPort();

  public abstract void setTrapPort(Integer paramInteger);

  public abstract int getInformPort();

  public abstract void setInformPort(int paramInt);

  public abstract int getServedClientCount();

  public abstract int getActiveClientCount();

  public abstract int getMaxActiveClientCount();

  public abstract void setMaxActiveClientCount(int paramInt)
    throws IllegalStateException;

  public abstract String getProtocol();

  public abstract Integer getBufferSize();

  public abstract void setBufferSize(Integer paramInteger)
    throws IllegalStateException;

  public abstract int getMaxTries();

  public abstract void setMaxTries(int paramInt);

  public abstract int getTimeout();

  public abstract void setTimeout(int paramInt);

  public abstract SnmpPduFactory getPduFactory();

  public abstract void setPduFactory(SnmpPduFactory paramSnmpPduFactory);

  public abstract void setUserDataFactory(SnmpUserDataFactory paramSnmpUserDataFactory);

  public abstract SnmpUserDataFactory getUserDataFactory();

  public abstract boolean getAuthTrapEnabled();

  public abstract void setAuthTrapEnabled(boolean paramBoolean);

  public abstract boolean getAuthRespEnabled();

  public abstract void setAuthRespEnabled(boolean paramBoolean);

  public abstract String getEnterpriseOid();

  public abstract void setEnterpriseOid(String paramString)
    throws IllegalArgumentException;

  public abstract String[] getMibs();

  public abstract Long getSnmpOutTraps();

  public abstract Long getSnmpOutGetResponses();

  public abstract Long getSnmpOutGenErrs();

  public abstract Long getSnmpOutBadValues();

  public abstract Long getSnmpOutNoSuchNames();

  public abstract Long getSnmpOutTooBigs();

  public abstract Long getSnmpInASNParseErrs();

  public abstract Long getSnmpInBadCommunityUses();

  public abstract Long getSnmpInBadCommunityNames();

  public abstract Long getSnmpInBadVersions();

  public abstract Long getSnmpOutPkts();

  public abstract Long getSnmpInPkts();

  public abstract Long getSnmpInGetRequests();

  public abstract Long getSnmpInGetNexts();

  public abstract Long getSnmpInSetRequests();

  public abstract Long getSnmpInTotalSetVars();

  public abstract Long getSnmpInTotalReqVars();

  public abstract Long getSnmpSilentDrops();

  public abstract Long getSnmpProxyDrops();

  public abstract SnmpMibHandler addMib(SnmpMibAgent paramSnmpMibAgent)
    throws IllegalArgumentException;

  public abstract SnmpMibHandler addMib(SnmpMibAgent paramSnmpMibAgent, SnmpOid[] paramArrayOfSnmpOid)
    throws IllegalArgumentException;

  public abstract boolean removeMib(SnmpMibAgent paramSnmpMibAgent);

  public abstract void snmpV1Trap(int paramInt1, int paramInt2, SnmpVarBindList paramSnmpVarBindList)
    throws IOException, SnmpStatusException;

  public abstract void snmpV1Trap(InetAddress paramInetAddress, String paramString, int paramInt1, int paramInt2, SnmpVarBindList paramSnmpVarBindList)
    throws IOException, SnmpStatusException;

  public abstract void snmpV1Trap(SnmpPeer paramSnmpPeer, SnmpIpAddress paramSnmpIpAddress, SnmpOid paramSnmpOid, int paramInt1, int paramInt2, SnmpVarBindList paramSnmpVarBindList, SnmpTimeticks paramSnmpTimeticks)
    throws IOException, SnmpStatusException;

  public abstract void snmpV2Trap(SnmpPeer paramSnmpPeer, SnmpOid paramSnmpOid, SnmpVarBindList paramSnmpVarBindList, SnmpTimeticks paramSnmpTimeticks)
    throws IOException, SnmpStatusException;

  public abstract void snmpV2Trap(SnmpOid paramSnmpOid, SnmpVarBindList paramSnmpVarBindList)
    throws IOException, SnmpStatusException;

  public abstract void snmpV2Trap(InetAddress paramInetAddress, String paramString, SnmpOid paramSnmpOid, SnmpVarBindList paramSnmpVarBindList)
    throws IOException, SnmpStatusException;

  public abstract void snmpPduTrap(InetAddress paramInetAddress, SnmpPduPacket paramSnmpPduPacket)
    throws IOException, SnmpStatusException;

  public abstract void snmpPduTrap(SnmpPeer paramSnmpPeer, SnmpPduPacket paramSnmpPduPacket)
    throws IOException, SnmpStatusException;

  public abstract Vector snmpInformRequest(SnmpInformHandler paramSnmpInformHandler, SnmpOid paramSnmpOid, SnmpVarBindList paramSnmpVarBindList)
    throws IllegalStateException, IOException, SnmpStatusException;

  public abstract SnmpInformRequest snmpInformRequest(InetAddress paramInetAddress, String paramString, SnmpInformHandler paramSnmpInformHandler, SnmpOid paramSnmpOid, SnmpVarBindList paramSnmpVarBindList)
    throws IllegalStateException, IOException, SnmpStatusException;

  public abstract SnmpInformRequest snmpInformRequest(SnmpPeer paramSnmpPeer, SnmpInformHandler paramSnmpInformHandler, SnmpOid paramSnmpOid, SnmpVarBindList paramSnmpVarBindList)
    throws IllegalStateException, IOException, SnmpStatusException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.daemon.SnmpAdaptorServerMBean
 * JD-Core Version:    0.6.2
 */