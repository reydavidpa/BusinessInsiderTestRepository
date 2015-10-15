package mappedTables;

// Generated Aug 31, 2015 10:52:13 AM by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;

/**
 * Grouping generated by hbm2java
 */
public class Grouping implements java.io.Serializable {

	private Integer groupid;
	private Suite suite;
	private String groupname;
	private String groupprefix;
	private Integer criticalgroup;
	private Integer haspassed;
	private Set selwowschedulers = new HashSet(0);
	private Set testcases = new HashSet(0);

	public Grouping() {
	}

	public Grouping(Suite suite, String groupname) {
		this.suite = suite;
		this.groupname = groupname;
	}

	public Grouping(Suite suite, String groupname, String groupprefix,
			Integer criticalgroup, Integer haspassed, Set selwowschedulers,
			Set testcases) {
		this.suite = suite;
		this.groupname = groupname;
		this.groupprefix = groupprefix;
		this.criticalgroup = criticalgroup;
		this.haspassed = haspassed;
		this.selwowschedulers = selwowschedulers;
		this.testcases = testcases;
	}

	public Integer getGroupid() {
		return this.groupid;
	}

	public void setGroupid(Integer groupid) {
		this.groupid = groupid;
	}

	public Suite getSuite() {
		return this.suite;
	}

	public void setSuite(Suite suite) {
		this.suite = suite;
	}

	public String getGroupname() {
		return this.groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public String getGroupprefix() {
		return this.groupprefix;
	}

	public void setGroupprefix(String groupprefix) {
		this.groupprefix = groupprefix;
	}

	public Integer getCriticalgroup() {
		return this.criticalgroup;
	}

	public void setCriticalgroup(Integer criticalgroup) {
		this.criticalgroup = criticalgroup;
	}

	public Integer getHaspassed() {
		return this.haspassed;
	}

	public void setHaspassed(Integer haspassed) {
		this.haspassed = haspassed;
	}

	public Set getSelwowschedulers() {
		return this.selwowschedulers;
	}

	public void setSelwowschedulers(Set selwowschedulers) {
		this.selwowschedulers = selwowschedulers;
	}

	public Set getTestcases() {
		return this.testcases;
	}

	public void setTestcases(Set testcases) {
		this.testcases = testcases;
	}

}
