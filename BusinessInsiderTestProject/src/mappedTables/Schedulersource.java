package mappedTables;

// Generated Aug 31, 2015 10:52:13 AM by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;

/**
 * Schedulersource generated by hbm2java
 */
public class Schedulersource implements java.io.Serializable {

	private Integer schedulersourceId;
	private String schedulesourceName;
	private Set selwowschedulers = new HashSet(0);

	public Schedulersource() {
	}

	public Schedulersource(String schedulesourceName, Set selwowschedulers) {
		this.schedulesourceName = schedulesourceName;
		this.selwowschedulers = selwowschedulers;
	}

	public Integer getSchedulersourceId() {
		return this.schedulersourceId;
	}

	public void setSchedulersourceId(Integer schedulersourceId) {
		this.schedulersourceId = schedulersourceId;
	}

	public String getSchedulesourceName() {
		return this.schedulesourceName;
	}

	public void setSchedulesourceName(String schedulesourceName) {
		this.schedulesourceName = schedulesourceName;
	}

	public Set getSelwowschedulers() {
		return this.selwowschedulers;
	}

	public void setSelwowschedulers(Set selwowschedulers) {
		this.selwowschedulers = selwowschedulers;
	}

}