package mappedTables;

// Generated Aug 31, 2015 10:52:13 AM by Hibernate Tools 3.4.0.CR1

/**
 * Errorsfile generated by hbm2java
 */
public class Errorsfile implements java.io.Serializable {

	private Integer errorfileid;
	private String errorfile;

	public Errorsfile() {
	}

	public Errorsfile(String errorfile) {
		this.errorfile = errorfile;
	}

	public Integer getErrorfileid() {
		return this.errorfileid;
	}

	public void setErrorfileid(Integer errorfileid) {
		this.errorfileid = errorfileid;
	}

	public String getErrorfile() {
		return this.errorfile;
	}

	public void setErrorfile(String errorfile) {
		this.errorfile = errorfile;
	}

}
