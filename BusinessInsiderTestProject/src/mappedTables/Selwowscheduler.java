package mappedTables;

// Generated Aug 31, 2015 10:52:13 AM by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Selwowscheduler generated by hbm2java
 */
public class Selwowscheduler implements java.io.Serializable {

	private Integer schedulerid;
	private Server server;
	private Grouping grouping;
	private Xmlfile xmlfile;
	private Environment environment;
	private Suite suite;
	private Schedulersource schedulersource;
	private Selwowusers selwowusers;
	private Outputfile outputfile;
	private Browser browser;
	private Date scheduleddate;
	private Set testcaseresultses = new HashSet(0);
	private Set scheduledjobses = new HashSet(0);

	public Selwowscheduler() {
	}

	public Selwowscheduler(Server server, Grouping grouping, Xmlfile xmlfile,
			Environment environment, Suite suite,
			Schedulersource schedulersource, Selwowusers selwowusers,
			Browser browser, Date scheduleddate) {
		this.server = server;
		this.grouping = grouping;
		this.xmlfile = xmlfile;
		this.environment = environment;
		this.suite = suite;
		this.schedulersource = schedulersource;
		this.selwowusers = selwowusers;
		this.browser = browser;
		this.scheduleddate = scheduleddate;
	}

	public Selwowscheduler(Server server, Grouping grouping, Xmlfile xmlfile,
			Environment environment, Suite suite,
			Schedulersource schedulersource, Selwowusers selwowusers,
			Outputfile outputfile, Browser browser, Date scheduleddate,
			Set testcaseresultses, Set scheduledjobses) {
		this.server = server;
		this.grouping = grouping;
		this.xmlfile = xmlfile;
		this.environment = environment;
		this.suite = suite;
		this.schedulersource = schedulersource;
		this.selwowusers = selwowusers;
		this.outputfile = outputfile;
		this.browser = browser;
		this.scheduleddate = scheduleddate;
		this.testcaseresultses = testcaseresultses;
		this.scheduledjobses = scheduledjobses;
	}

	public Integer getSchedulerid() {
		return this.schedulerid;
	}

	public void setSchedulerid(Integer schedulerid) {
		this.schedulerid = schedulerid;
	}

	public Server getServer() {
		return this.server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public Grouping getGrouping() {
		return this.grouping;
	}

	public void setGrouping(Grouping grouping) {
		this.grouping = grouping;
	}

	public Xmlfile getXmlfile() {
		return this.xmlfile;
	}

	public void setXmlfile(Xmlfile xmlfile) {
		this.xmlfile = xmlfile;
	}

	public Environment getEnvironment() {
		return this.environment;
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	public Suite getSuite() {
		return this.suite;
	}

	public void setSuite(Suite suite) {
		this.suite = suite;
	}

	public Schedulersource getSchedulersource() {
		return this.schedulersource;
	}

	public void setSchedulersource(Schedulersource schedulersource) {
		this.schedulersource = schedulersource;
	}

	public Selwowusers getSelwowusers() {
		return this.selwowusers;
	}

	public void setSelwowusers(Selwowusers selwowusers) {
		this.selwowusers = selwowusers;
	}

	public Outputfile getOutputfile() {
		return this.outputfile;
	}

	public void setOutputfile(Outputfile outputfile) {
		this.outputfile = outputfile;
	}

	public Browser getBrowser() {
		return this.browser;
	}

	public void setBrowser(Browser browser) {
		this.browser = browser;
	}

	public Date getScheduleddate() {
		return this.scheduleddate;
	}

	public void setScheduleddate(Date scheduleddate) {
		this.scheduleddate = scheduleddate;
	}

	public Set getTestcaseresultses() {
		return this.testcaseresultses;
	}

	public void setTestcaseresultses(Set testcaseresultses) {
		this.testcaseresultses = testcaseresultses;
	}

	public Set getScheduledjobses() {
		return this.scheduledjobses;
	}

	public void setScheduledjobses(Set scheduledjobses) {
		this.scheduledjobses = scheduledjobses;
	}

}
