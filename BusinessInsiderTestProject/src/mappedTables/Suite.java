package mappedTables;

// Generated Aug 31, 2015 10:52:13 AM by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;

/**
 * Suite generated by hbm2java
 */
public class Suite implements java.io.Serializable {

	private Integer suiteid;
	private String suitename;
	private String suitefilepath;
	private Set buildResultses = new HashSet(0);
	private Set selwowschedulers = new HashSet(0);
	private Set groupings = new HashSet(0);

	public Suite() {
	}

	public Suite(String suitename) {
		this.suitename = suitename;
	}

	public Suite(String suitename, String suitefilepath, Set buildResultses,
			Set selwowschedulers, Set groupings) {
		this.suitename = suitename;
		this.suitefilepath = suitefilepath;
		this.buildResultses = buildResultses;
		this.selwowschedulers = selwowschedulers;
		this.groupings = groupings;
	}

	public Integer getSuiteid() {
		return this.suiteid;
	}

	public void setSuiteid(Integer suiteid) {
		this.suiteid = suiteid;
	}

	public String getSuitename() {
		return this.suitename;
	}

	public void setSuitename(String suitename) {
		this.suitename = suitename;
	}

	public String getSuitefilepath() {
		return this.suitefilepath;
	}

	public void setSuitefilepath(String suitefilepath) {
		this.suitefilepath = suitefilepath;
	}

	public Set getBuildResultses() {
		return this.buildResultses;
	}

	public void setBuildResultses(Set buildResultses) {
		this.buildResultses = buildResultses;
	}

	public Set getSelwowschedulers() {
		return this.selwowschedulers;
	}

	public void setSelwowschedulers(Set selwowschedulers) {
		this.selwowschedulers = selwowschedulers;
	}

	public Set getGroupings() {
		return this.groupings;
	}

	public void setGroupings(Set groupings) {
		this.groupings = groupings;
	}

}