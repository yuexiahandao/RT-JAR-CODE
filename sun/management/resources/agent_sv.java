/*   */ package sun.management.resources;
/*   */ 
/*   */ import java.util.ListResourceBundle;
/*   */ 
/*   */ public final class agent_sv extends ListResourceBundle
/*   */ {
/*   */   protected final Object[][] getContents()
/*   */   {
/* 7 */     return new Object[][] { { "agent.err.access.file.not.readable", "Access-filen är inte läsbar" }, { "agent.err.access.file.notfound", "Access-filen hittades inte" }, { "agent.err.access.file.notset", "Åtkomstfilen har inte angetts men com.sun.management.jmxremote.authenticate=true" }, { "agent.err.access.file.read.failed", "Kunde inte läsa åtkomstfilen" }, { "agent.err.acl.file.access.notrestricted", "Läsbehörigheten för filen måste begränsas" }, { "agent.err.acl.file.not.readable", "SNMP ACL-filen är inte läsbar" }, { "agent.err.acl.file.notfound", "SNMP ACL-filen hittades inte" }, { "agent.err.acl.file.notset", "Ingen SNMP ACL-fil har angetts, men com.sun.management.snmp.acl=true" }, { "agent.err.acl.file.read.failed", "Kunde inte läsa filen SNMP ACL" }, { "agent.err.agentclass.access.denied", "Åtkomst till premain(String) nekad" }, { "agent.err.agentclass.failed", "Administrationsagentklassen utfördes inte " }, { "agent.err.agentclass.notfound", "Administrationsagentklassen hittades inte" }, { "agent.err.configfile.access.denied", "Åtkomst till konfigurationsfilen nekad" }, { "agent.err.configfile.closed.failed", "Kunde inte stänga konfigurationsfilen" }, { "agent.err.configfile.failed", "Kunde inte läsa konfigurationsfilen" }, { "agent.err.configfile.notfound", "Konfigurationsfilen hittades inte" }, { "agent.err.connector.server.io.error", "Serverkommunikationsfel för JMX-anslutning" }, { "agent.err.error", "Fel" }, { "agent.err.exception", "Agenten orsakade ett undantag " }, { "agent.err.exportaddress.failed", "Kunde inte exportera JMX-anslutningsadressen till instrumentbufferten" }, { "agent.err.file.access.not.restricted", "Filläsningsåtkomst måste begränsas" }, { "agent.err.file.not.found", "Filen hittades inte" }, { "agent.err.file.not.readable", "Filen är inte läsbar" }, { "agent.err.file.not.set", "Filen är inte angiven" }, { "agent.err.file.read.failed", "Kunde inte läsa filen" }, { "agent.err.invalid.agentclass", "Ogiltigt egenskapsvärde för com.sun.management.agent.class" }, { "agent.err.invalid.jmxremote.port", "Ogiltigt com.sun.management.jmxremote.port-nummer" }, { "agent.err.invalid.jmxremote.rmi.port", "Ogiltigt com.sun.management.jmxremote.rmi.port-nummer" }, { "agent.err.invalid.option", "Det angivna alternativet är ogiltigt" }, { "agent.err.invalid.snmp.port", "Ogiltigt com.sun.management.snmp.port-nummer" }, { "agent.err.invalid.snmp.trap.port", "Ogiltigt com.sun.management.snmp.trap-nummer" }, { "agent.err.invalid.state", "Ogiltig agentstatus" }, { "agent.err.password.file.access.notrestricted", "Läsbehörigheten för filen måste begränsas" }, { "agent.err.password.file.not.readable", "Lösenordsfilen är inte läsbar" }, { "agent.err.password.file.notfound", "Hittar inte lösenordsfilen" }, { "agent.err.password.file.notset", "Lösenordsfilen har inte angetts men com.sun.management.jmxremote.authenticate=true" }, { "agent.err.password.file.read.failed", "Kunde inte läsa lösenordsfilen" }, { "agent.err.premain.notfound", "premain(String) finns inte i agentklassen" }, { "agent.err.snmp.adaptor.start.failed", "Kunde inte starta SNMP-adaptern med adressen" }, { "agent.err.snmp.mib.init.failed", "Kunde inte initiera SNMP MIB. Returnerade felet" }, { "agent.err.unknown.snmp.interface", "Okänt SNMP-gränssnitt" }, { "agent.err.warning", "Varning" }, { "jmxremote.AdaptorBootstrap.getTargetList.adding", "Mål läggs till: {0}" }, { "jmxremote.AdaptorBootstrap.getTargetList.initialize1", "Adaptern redo." }, { "jmxremote.AdaptorBootstrap.getTargetList.initialize2", "SNMP-adaptern redo på: {0}:{1}" }, { "jmxremote.AdaptorBootstrap.getTargetList.processing", "ACL bearbetas" }, { "jmxremote.AdaptorBootstrap.getTargetList.starting", "Adapterservern startas:" }, { "jmxremote.AdaptorBootstrap.getTargetList.terminate", "avsluta {0}" }, { "jmxremote.ConnectorBootstrap.file.readonly", "Filläsningsåtkomst måste begränsas {0}" }, { "jmxremote.ConnectorBootstrap.noAuthentication", "Ingen autentisering" }, { "jmxremote.ConnectorBootstrap.password.readonly", "Läsbehörigheten för lösenordsfilen måste begränsas: {0}" }, { "jmxremote.ConnectorBootstrap.ready", "JMX-anslutning redo på: {0}" }, { "jmxremote.ConnectorBootstrap.starting", "Startar server för JMX-anslutning:" } };
/*   */   }
/*   */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.resources.agent_sv
 * JD-Core Version:    0.6.2
 */