package mappedTables;

// Generated Aug 31, 2015 10:52:13 AM by Hibernate Tools 3.4.0.CR1

/**
 * Commands generated by hbm2java
 */
public class Commands implements java.io.Serializable {

	private Integer commandid;
	private String commandname;

	public Commands() {
	}

	public Commands(String commandname) {
		this.commandname = commandname;
	}

	public Integer getCommandid() {
		return this.commandid;
	}

	public void setCommandid(Integer commandid) {
		this.commandid = commandid;
	}

	public String getCommandname() {
		return this.commandname;
	}

	public void setCommandname(String commandname) {
		this.commandname = commandname;
	}

}