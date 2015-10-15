package mappedTables;

// Generated Aug 31, 2015 10:52:13 AM by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;

/**
 * Operatingsystem generated by hbm2java
 */
public class Operatingsystem implements java.io.Serializable {

	private Integer osid;
	private String osname;
	private Set servers = new HashSet(0);

	public Operatingsystem() {
	}

	public Operatingsystem(String osname) {
		this.osname = osname;
	}

	public Operatingsystem(String osname, Set servers) {
		this.osname = osname;
		this.servers = servers;
	}

	public Integer getOsid() {
		return this.osid;
	}

	public void setOsid(Integer osid) {
		this.osid = osid;
	}

	public String getOsname() {
		return this.osname;
	}

	public void setOsname(String osname) {
		this.osname = osname;
	}

	public Set getServers() {
		return this.servers;
	}

	public void setServers(Set servers) {
		this.servers = servers;
	}

}
