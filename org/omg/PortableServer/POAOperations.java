package org.omg.PortableServer;

import org.omg.CORBA.Policy;
import org.omg.PortableServer.POAPackage.AdapterAlreadyExists;
import org.omg.PortableServer.POAPackage.AdapterNonExistent;
import org.omg.PortableServer.POAPackage.InvalidPolicy;
import org.omg.PortableServer.POAPackage.NoServant;
import org.omg.PortableServer.POAPackage.ObjectAlreadyActive;
import org.omg.PortableServer.POAPackage.ObjectNotActive;
import org.omg.PortableServer.POAPackage.ServantAlreadyActive;
import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongAdapter;
import org.omg.PortableServer.POAPackage.WrongPolicy;

public abstract interface POAOperations
{
  public abstract POA create_POA(String paramString, POAManager paramPOAManager, Policy[] paramArrayOfPolicy)
    throws AdapterAlreadyExists, InvalidPolicy;

  public abstract POA find_POA(String paramString, boolean paramBoolean)
    throws AdapterNonExistent;

  public abstract void destroy(boolean paramBoolean1, boolean paramBoolean2);

  public abstract ThreadPolicy create_thread_policy(ThreadPolicyValue paramThreadPolicyValue);

  public abstract LifespanPolicy create_lifespan_policy(LifespanPolicyValue paramLifespanPolicyValue);

  public abstract IdUniquenessPolicy create_id_uniqueness_policy(IdUniquenessPolicyValue paramIdUniquenessPolicyValue);

  public abstract IdAssignmentPolicy create_id_assignment_policy(IdAssignmentPolicyValue paramIdAssignmentPolicyValue);

  public abstract ImplicitActivationPolicy create_implicit_activation_policy(ImplicitActivationPolicyValue paramImplicitActivationPolicyValue);

  public abstract ServantRetentionPolicy create_servant_retention_policy(ServantRetentionPolicyValue paramServantRetentionPolicyValue);

  public abstract RequestProcessingPolicy create_request_processing_policy(RequestProcessingPolicyValue paramRequestProcessingPolicyValue);

  public abstract String the_name();

  public abstract POA the_parent();

  public abstract POA[] the_children();

  public abstract POAManager the_POAManager();

  public abstract AdapterActivator the_activator();

  public abstract void the_activator(AdapterActivator paramAdapterActivator);

  public abstract ServantManager get_servant_manager()
    throws WrongPolicy;

  public abstract void set_servant_manager(ServantManager paramServantManager)
    throws WrongPolicy;

  public abstract Servant get_servant()
    throws NoServant, WrongPolicy;

  public abstract void set_servant(Servant paramServant)
    throws WrongPolicy;

  public abstract byte[] activate_object(Servant paramServant)
    throws ServantAlreadyActive, WrongPolicy;

  public abstract void activate_object_with_id(byte[] paramArrayOfByte, Servant paramServant)
    throws ServantAlreadyActive, ObjectAlreadyActive, WrongPolicy;

  public abstract void deactivate_object(byte[] paramArrayOfByte)
    throws ObjectNotActive, WrongPolicy;

  public abstract org.omg.CORBA.Object create_reference(String paramString)
    throws WrongPolicy;

  public abstract org.omg.CORBA.Object create_reference_with_id(byte[] paramArrayOfByte, String paramString);

  public abstract byte[] servant_to_id(Servant paramServant)
    throws ServantNotActive, WrongPolicy;

  public abstract org.omg.CORBA.Object servant_to_reference(Servant paramServant)
    throws ServantNotActive, WrongPolicy;

  public abstract Servant reference_to_servant(org.omg.CORBA.Object paramObject)
    throws ObjectNotActive, WrongPolicy, WrongAdapter;

  public abstract byte[] reference_to_id(org.omg.CORBA.Object paramObject)
    throws WrongAdapter, WrongPolicy;

  public abstract Servant id_to_servant(byte[] paramArrayOfByte)
    throws ObjectNotActive, WrongPolicy;

  public abstract org.omg.CORBA.Object id_to_reference(byte[] paramArrayOfByte)
    throws ObjectNotActive, WrongPolicy;

  public abstract byte[] id();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.PortableServer.POAOperations
 * JD-Core Version:    0.6.2
 */