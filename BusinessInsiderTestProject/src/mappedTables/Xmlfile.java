package mappedTables;

// Generated Aug 31, 2015 10:52:13 AM by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;

/**
 * Xmlfile generated by hbm2java
 */
public class Xmlfile implements java.io.Serializable {

	private Integer xmlfileid;
	private String xmlfile;
	private Set selwowschedulers = new HashSet(0);

	public Xmlfile() {
	}

	public Xmlfile(String xmlfile, Set selwowschedulers) {
		this.xmlfile = xmlfile;
		this.selwowschedulers = selwowschedulers;
	}

	public Integer getXmlfileid() {
		return this.xmlfileid;
	}

	public void setXmlfileid(Integer xmlfileid) {
		this.xmlfileid = xmlfileid;
	}

	public String getXmlfile() {
		return this.xmlfile;
	}

	public void setXmlfile(String xmlfile) {
		this.xmlfile = xmlfile;
	}

	public Set getSelwowschedulers() {
		return this.selwowschedulers;
	}

	public void setSelwowschedulers(Set selwowschedulers) {
		this.selwowschedulers = selwowschedulers;
	}

}
