package helpers;

import java.io.Serializable;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mappedTables.Browser;
import mappedTables.Scheduledjobs;
import mappedTables.Selwowscheduler;
import mappedTables.Selwowusers;
import mappedTables.Server;
import mappedTables.Testcase;
import mappedTables.Testcaseresults;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;


/**
 * This is a helper class to manage database calls for the Selenium Wow scheduler 
 * 
 * @author ehartill
 *
 */
public class MySQLDatabaseManager
{
	private static final String drivername = "com.mysql.jdbc.Driver";
	//private SessionFactory sf =  HibernateUtil.getSessionFactory();
	private Session session = null;
	//private Logger logger;
	//private static String xmlfilecontent;
	public Logger logger = GlobalHelper.getLogger("");
	
	/**
	 * Prepare the class in the constructor; uses the drivername
	 * instance variable.
	 */
	public MySQLDatabaseManager()
	{
		try
		{
			Class.forName(drivername);
			session = helpers.HibernateUtil.getSessionFactory().getCurrentSession();
			
			if(!session.getTransaction().isActive()){
				session.beginTransaction();
			}
			/*if (logger == null)
				logger = GlobalHelper.getLogger(null);*/
		}
		catch (ClassNotFoundException cnfe)
		{
			System.out.println("Error on MySQLDatabaseManager: " + cnfe.getMessage());
		}
	}

	/**
	 * Make a connection to the database
	 * @return 
	 */
	public Transaction connectMSSQLHibernate()
	{
		Transaction tx = null;
		try
		{
			if(!session.getSessionFactory().getCurrentSession().isOpen()){
				session = session.getSessionFactory().openSession();
			}
			else{
				session=session.getSessionFactory().getCurrentSession();
			}

			if(!session.getTransaction().isActive())
			{
				tx = session.beginTransaction();
			}
			else{
				tx = session.getTransaction();
			}
		}
		catch (Throwable sqle)
		{
			System.out.println("Initial SessionFactory MySQLDatabaseManager creation failed: " + sqle.getMessage());
			//connectMSSQLHibernate();
		}
		return tx;
	}
	
	public Session getSession(){
		return session;
	}
	/** Save the transaction and commits/closes session...*//*
	public void commitMySQLHibernate(){
		if(session.isOpen()){
			session.getTransaction().commit();
			//System.out.println("Ended hibernateMySql transactions...");
		}
		else{
			System.out.println("No active session (MySQLDatabaseManager)");
		}
	}*/

	public void close(){
		if(session != null){
			if(session.getSessionFactory().getCurrentSession().isOpen())
				session.getSessionFactory().getCurrentSession().close();
		}
	}
	/**
	 * When the test starts, this method creates a record in the
	 * testcaseresults table.
	 * 
	 * It first creates a lock on the table so that no other records
	 * can be saved before SeleniumWow gets the testcaseid for this test
	 * 
	 * Then it inserts the table, using the SQL Server time as the start time,
	 * 	schedulerid as the key for the testcasescheduler field, and a default
	 * 	testID of 7 as the key for the testcaseid
	 * 
	 * Then it gets the testcaseresultsid with a MAX function
	 * 
	 * Finally it releases the lock on the table
	 * @param readXml 
	 * @param xmlContent 
	 * 
	 * @params - schedulerId: this comes from the XML file; it's a
	 * 	required field in the testcaseresults table
	 */
	public int setStartTime(int schedulerId, int userId, boolean readXml)
	{

		int resultsId = -1;
		int testId = 5;
		Testcase tc = new Testcase();
		tc.setTestcaseid(testId);
			
		//Insert the record
		java.util.Date date= new java.util.Date();
		Testcaseresults tbean = new Testcaseresults();
		tbean.setStarttime(new Timestamp(date.getTime()));
		
		Selwowscheduler ch = new Selwowscheduler();
		ch.setSchedulerid(schedulerId);
		
		tbean.setSelwowscheduler(ch);
		tbean.setTestcase(tc);
		
		Selwowusers user = new Selwowusers();
		user.setUserid(userId);
		
		return saveReturnId(tbean);
	}

	public int getServerIdFromIp(String ip)
	{
		System.out.println("Getting Server name from IP.........."+ip);
		int serverId = 0;
		Transaction tx = connectMSSQLHibernate();
		try {
			String hql = "select serverid from Server where serverIp like :ip";
			serverId = (Integer) session.createQuery(hql).setParameter("ip", ip).uniqueResult();
			tx.commit();
		} 
		catch (HibernateException e) {
			System.out.println("Error on getServerNameFromIp: "+e.getMessage());
			tx.rollback();
		}
		finally {
		    close();
		}
		return serverId;
	}
	
	public Server getServerFromLocalHost()
	{
		//System.out.println("Getting ServerId from Local Host..........");
		InetAddress localhost = null;
		try
		{
			localhost = InetAddress.getLocalHost();
		}
		catch (UnknownHostException sqle)
		{
			System.out.println(sqle.getMessage());
		}
		return getServerFromIP(localhost.getHostAddress()+"");
	}

	public Server getServerFromIP(String ip)
	{
		//System.out.println("Getting Server from IP.........."+ip);
		Server server = null;
		Transaction tx = connectMSSQLHibernate();
		try {
			String hql = "from Server where serverip = :ip";
			server = (Server) session.createQuery(hql).setParameter("ip", ip).uniqueResult();
			tx.commit();
		} 
		catch (HibernateException e) {
			System.out.println("Error on getServerNameFromIp: "+e.getMessage());
			tx.rollback();
		}
		finally {
		    close();
		}
		return server;
	}
	
	public String getXmlFilePath(String server)
	{
		Transaction tx = connectMSSQLHibernate();
		System.out.println("Getting XML File Path.........."+server);
		String xmlFilePath = "";
		String sql = "SELECT xmlfilepath FROM xmlfilepath x\n" +
				"WHERE x.serverid = ?";

		try
		{
			Query q = session.createSQLQuery(sql).setParameter(0, server);
			xmlFilePath = (String)q.uniqueResult();
			tx.commit();
		}
		catch(HibernateException sqle)
		{
			System.out.println("Error on getXmlFilePath: " + sqle.getMessage());
			tx.rollback();
			//sqle.getMessage();
		}
		finally {
		    close();
		}	
		return xmlFilePath;
	}
	/**
	 * After the test finishes, save the results
	 * 
	 * @param resultString - the composed results; this is the same thing we send in the email
	 * @param resultsId - this is what we saved when we saved the start time; we use it to 
	 * 		identify the record for this test
	 * @param testCaseId - the test case this script is tied to; gotten from TFSIDCommand
	 * @param passed - whether or not the test passed; 0 means there were errors (passed = false)
	 * @param errors 
	 * @throws SQLException
	 */
	public void saveResults(String resultString, int resultsId, int passed, ArrayList<String> errors)
	{
		
		Transaction trans = connectMSSQLHibernate();
		String error = "";
		
		try
		{
			Testcaseresults tcr = (Testcaseresults) session.get(Testcaseresults.class, resultsId);
			tcr.setResults(resultString);
			tcr.setPassed(passed);
			tcr.setEndtime(new Date());
			if(errors.size() != 0){
				
				for (int o = 0; o < errors.size(); o++)
				//while(errors.iterator().hasNext()){
					error=error.concat(errors.get(o)+"       ");
					//error=error.concat(errors.iterator().next()+"       ");
					//errors.remove(errors.iterator().next());
				//}
				tcr.setErrors(error);	
			}
			session.update(tcr);
			System.out.println("Update TestCaseResults successful ... "+tcr.getTestcaseresultsid());
			trans.commit();
		}
		catch(HibernateException sqle)
		{
			logger.fatal("Error on saveResults: " + sqle.getMessage());
			trans.rollback();
		}
		finally{
			close();
		}
	}
	
	public int savetestcaseresults(Selwowscheduler sws, String xmlfilecontent)
	{
		int resultsId = -1;
		
		int testId = 5;
		Testcase tc = new Testcase();
		tc.setTestcaseid(testId);

		//Insert the record
		java.util.Date date= new java.util.Date();
		Testcaseresults tbean = new Testcaseresults();
		tbean.setStarttime(new Timestamp(date.getTime()));
		tbean.setSelwowscheduler(sws);
		tbean.setTestcase(tc);
		System.out.println("Launcher:"+sws.getSelwowusers().getUserid());
		Transaction trans = connectMSSQLHibernate();
		try
		{
			resultsId=(Integer) session.save(tbean);
			trans.commit();
			logger.debug("Insert into testCaseResults success.");
		}
		catch(HibernateException he)
		{
			logger.fatal("Error setting start time for TestCaseResults: " + he.getMessage());
			trans.rollback();
		}
		finally{
			close();
		}
		return resultsId;
	}

	
	/**
	 * Get the application environment's IP address from the database
	 *   uses the value from the XML file:
	 *     INT
	 *     INT-Admin
	 *     INT2
	 *     INT2-Admin
	 *     INT2-MAP
	 * 
	 * @param env - string describing the environment; comes from the XML file
	 * @return
	 * @throws SQLException
	 */
	public String getEnvironment(String env)
	{
		String environment = "";
		Transaction trans = connectMSSQLHibernate();
		try
		{
			Query query = session.createQuery("SELECT ipaddress FROM Environment WHERE environmentname like ?");
			query.setParameter(0, env);
			List<String> list = query.list();
			environment = list.size() > 0 ? list.get(0) : "";
			trans.commit();
		}
		catch(HibernateException sqle)
		{
			logger.fatal("Error on getEnvironment: " + sqle.getMessage());
			trans.rollback();
			//sqle.printStackTrace();
		}
		finally{
			close();
		}
		//System.out.println("IP Address:: " + environment);
		return environment;
	}

	
	/**
	 * Get the databaseName from the DB
	 *   uses the value from the XML file:
	 *     INT
	 *     INT2
	 *     INT2-MAP
	 * 
	 * @param environment - global environment variable - SeleniumWowTestNGScript.globalEnvironment
	 * @return
	 * @throws SQLException
	 */
	public String getSQLServerIPAddress(String environment)
	{
		
		Transaction trans = connectMSSQLHibernate();
		String ipaddress = "";
		try{
			String sql = "SELECT SQLServerIPAddress " +
					"FROM environment, miscellaneous " +
					"WHERE environment.environmentid = miscellaneous.environmentid " +
					"and environment.environmentname like :en";
			ipaddress = (String) session.createSQLQuery(sql.toLowerCase()).setParameter("en", environment).list().get(0);
			trans.commit();
		} 
		catch (HibernateException e){
			logger.fatal("Error on getSQLServerIPAddress: " + e.getMessage());
			trans.rollback();
		}
		finally{
			close();
		}
		return ipaddress;
	}
	
	/**
	 * Get the server ID from the database based on the server name; the server ID is
	 *   used to get the browser IP address
	 *   Uses the String value from the XML file
	 * 
	 * @param server - The name of the server to run the script on
	 * @return serverId
	 * @throws SQLException
	 */
	public int getServerId(String server)
	{
		Transaction trans = connectMSSQLHibernate();
		int serverId = 0;
		try
		{
			String sql = "SELECT ServerID " +
					"FROM Server " +
					"WHERE ServerName like ?";
			Query query = session.createSQLQuery(sql.toLowerCase()).setParameter(0, server);
			List<String> list = query.list();
			serverId = (Integer) (list.size() > 0 ? list.get(0) : serverId);
			trans.commit();
		}
		catch(HibernateException sqle)
		{
			logger.error("Error executing getServerId query: " + sqle.getMessage());
			trans.rollback();
		}
		finally{
			close();
		}
		return serverId;
	}
/* 	 * Get the test case ID based on the TFSID command
	 *   If the tfs id is not present in the testcase table, return 7;
	 *   this is the default value, and will display as "Invalid TFSID"
	 * 
	 * @param tfsid - from the TFSID command
	 * @return
	 * @throws SQLException
	 */
	public long getTestCaseId(long tcid)
	{
		long testCaseId = -1;
		String sql = "SELECT TestCaseId FROM TestCase WHERE TestCaseId = ?";
		
		Transaction tx = connectMSSQLHibernate();
		try
		{
			Query query = session.createSQLQuery(sql.toLowerCase()).setParameter(0, tcid);
			testCaseId = (Integer) query.uniqueResult();
			tx.commit();
		}
		catch(HibernateException sqle)
		{
			logger.error("Error on getTestCaseName: " + sqle.getMessage());
			tx.rollback();
		}
		finally{
			close();
		}
		return testCaseId;
	}
	/**
	 * Get the classes from the DB
	 * 
	 * @return ArrayList - List of classes
	 * @throws SQLException
	 */
	public ArrayList<String> getDBCommands()
	{
		String DBCommandsSql ="SELECT commandname FROM commands";
		Query DBCommandsQuery; 
		ArrayList<String> DBcommands = new ArrayList<String>();
		Transaction tx = connectMSSQLHibernate();
		try{
			DBCommandsQuery = session.createSQLQuery(DBCommandsSql);
			DBcommands = (ArrayList<String>) DBCommandsQuery.list();
			tx.commit();
			
		}catch (HibernateException sqle)
		{
			logger.error("Error executing getDBCommands query: " + sqle.getMessage());
			tx.rollback();
		}
		finally{
			close();
		}
		return DBcommands;
	}
	/**
	 * Method to convert a result set to an array list - created for getDBCommands()
	 * 
	 * @return ArrayList
	 * @throws SQLException
	 */
	ArrayList<String> ResultsSetToArrayList(ResultSet rs) throws SQLException {
		ArrayList<String> ArrayList = new ArrayList<String>();
		if (rs != null) {
			while (rs.next()) {
				String columnValue = rs.getString(1);
				//System.out.print(columnValue);
				if (columnValue == null) {
					ArrayList.add("NULL");
				} else {
					ArrayList.add(columnValue);
				}
			}
		}
		return ArrayList;
	}

	/**
	 * Update the testcaseresults with the testcaseid after the tfsid is read
	 * from TFSIDCommand
	 * 
	 * @param testCaseId
	 * @throws SQLException
	 */
	public void insertTestCaseId(long testCaseId, long testCaseResultsId)
	{
		Transaction tx = connectMSSQLHibernate();
		String sql = "UPDATE testcaseresults\n" +
				"SET testcaseid = ?\n" +
				"WHERE testcaseresultsid = ?";
		try
		{
			Query query = session.createSQLQuery(sql.toLowerCase());
			query.setParameter(0, testCaseId);
			query.setParameter(1, testCaseResultsId);
			query.executeUpdate();
			tx.commit();
		}
		catch(HibernateException sqle)
		{
			logger.error("Error executing insertTestCaseId query: " + sqle.getMessage());
			tx.rollback();
		}
		finally{
			close();
		}
	}
	/**
	 * @throws SQLException 
	 * 
	 */
	public int getUserId(String email)
	{
		Transaction tx = connectMSSQLHibernate();
		String userId = null;
		try{
			String userIdSql = "select CONVERT(userid,CHAR(15)) from selwowusers where useremail like '" + email + "'";
			Query userIdQuery = session.createSQLQuery(userIdSql);
			userId = (String) userIdQuery.uniqueResult();
			tx.commit();
		}
		catch(HibernateException sqle)
		{
			logger.error("Error executing getUserId query: " + sqle.getMessage());
			tx.rollback();
		}
		finally{
			close();
		}
		return Integer.parseInt(userId);		
	}
	
	//This is a cleanup method to get rid of all the sleeping connections on MySql. Notice this is a static method. 
	public static void cleanUpConnections(){
		String sql = "(SELECT ID FROM INFORMATION_SCHEMA.PROCESSLIST \r\n" + 
				"\r\n" + 
				"where TIME>4000\r\n" + 
				"order by ID desc)";
		String killSql = "kill :id";
		Session cleanSession=HibernateUtil.getSessionFactory().getCurrentSession();
		cleanSession.beginTransaction();
		
		List<BigInteger> q = cleanSession.createSQLQuery(sql).list();
		
		for(BigInteger I:q){
			cleanSession.createSQLQuery(killSql).setParameter("id", I).executeUpdate();
		}
		cleanSession.getTransaction().commit();
		System.out.println(q.size()+" processes killed");
	}

	public void updateResults(Selwowscheduler scheduler) {
		Transaction tx = connectMSSQLHibernate();
		
		String sql = "select tcr.starttime, tcr.endtime from Testcaseresults tcr where tcr.selwowscheduler.schedulerid = ? order by tcr.testcaseresultsid";
		
		try{
			Testcaseresults tcr = (Testcaseresults) session.createQuery(sql).setMaxResults(1);
			tx.commit();
		}
		catch(HibernateException sqle)
		{
			logger.error("Error executing updateResults query: " + sqle.getMessage());
			tx.rollback();
		}
		finally{
			close();
		}
	}
	
	public Object JobExists(int schedulerid)
	{
		Transaction tx = connectMSSQLHibernate();
		Object exists = null;
		logger.debug("Checking if job still exists...");

		String hqlSelect = "from Scheduledjobs sj\r\n"
				+ "where sj.selwowscheduler.schedulerid = :id";
		try
		{
			exists = session.createQuery(hqlSelect)
					.setParameter("id", schedulerid).uniqueResult();
			tx.commit();
		}
		catch(HibernateException sqle)
		{
			logger.fatal("Error on JobExists: " + sqle.getMessage());
			tx.rollback();
		}
		finally {
		    close();
		}
		return exists;
	}
	
	public boolean isJobKilled(int schedulerid)
	{
		Transaction tx = connectMSSQLHibernate();
		logger.debug("Checking if job has been killed...");
		int jobstatusid = -1;
		String hqlSelect = "select sj.jobstatusid from Scheduledjobs sj where sj.selwowscheduler.schedulerid=:id";
		try
		{
			jobstatusid = (int) session.createQuery(hqlSelect).setParameter("id", schedulerid).uniqueResult();
			tx.commit();
		}
		catch(HibernateException sqle)
		{
			logger.fatal("Error on JobExists: " + sqle.getMessage());
			tx.rollback();
		}
		finally {
		    close();
		}
		
		if(jobstatusid==4) return true;
		else return false;
	}
	
	public Object get(String hql, boolean unique){
		Transaction tx = connectMSSQLHibernate();
		Object o = null;
		try
		{
			Query q = session.createQuery(hql);
			if(unique){
				o = q.uniqueResult();
			}
			else{
				o = q.list();
			}
			tx.commit();
		}
		catch(HibernateException sqle)
		{
			System.out.println("Error on get1: " + sqle.getMessage());
			tx.rollback();
		}
		finally {
		    close();
		}
		return o;
	}
	
	public void updateGoodToGo(String schedulerId, int goodToGo) {
		Transaction tx = connectMSSQLHibernate();
		String hql = "from Scheduledjobs sj where sj.selwowscheduler.schedulerid="+schedulerId;
		try
		{
			Query scheduledJobQuery = session.createQuery(hql);
			Scheduledjobs scheduledJob = (Scheduledjobs) scheduledJobQuery.uniqueResult();
			if(scheduledJob!=null){
				scheduledJob.setGoodToGo(goodToGo);
				session.update(scheduledJob);
				System.out.println("SchedulerId="+schedulerId + " - ScheduledJobId="+scheduledJob.getSchedulejobid() + " - Good to Go? " + goodToGo);
				System.out.println("_________________________________________________________________________________________");
			}
			tx.commit();
		}
		catch(HibernateException sqle)
		{
			System.out.println("Error on updateGoodToGo: " + sqle.getMessage());
			tx.rollback();
		}
		finally {
		    close();
		}
	}
	
	public Server getServer(String server)
	{
		Transaction trans = connectMSSQLHibernate();
		Server Server = null;
		try{
			String sql = "from Server where ServerName=:server";
			
			Server = (mappedTables.Server) session.createQuery(sql).setParameter("server", server).list().get(0);
			trans.commit();
		}
		catch (HibernateException e){
			logger.fatal("Error on getServer: " + e.getMessage());
			trans.rollback();
		}
		finally{
			close();
		}
		return Server;
	}
	
	public Browser getBrowser(String browserName, Integer serverId)
	{
		Transaction trans = connectMSSQLHibernate();
		Browser Browser = null;
		try{
			String sql = "from Browser b where b.browsername=:browserName and b.server.serverid=:serverId";
			
			Browser = (mappedTables.Browser) session.createQuery(sql).setParameter("browserName", browserName).setParameter("serverId", serverId).list().get(0);
			trans.commit();
		}
		catch (HibernateException e){
			logger.fatal("Error on getBrowser: " + e.getMessage());
			trans.rollback();
		}
		finally{
			close();
		}
		return Browser;
	}
	
	public Scheduledjobs getScheduledJob(String schedulerId)
	{
		Transaction trans = connectMSSQLHibernate();
		Scheduledjobs ScheduledJob = new Scheduledjobs();
		try{
			String sql = "from Scheduledjobs sj where sj.selwowscheduler.schedulerid=:schedulerId";
			
			ScheduledJob = (Scheduledjobs) session.createQuery(sql).setParameter("schedulerId", Integer.parseInt(schedulerId)).uniqueResult();
			trans.commit();
		}
		catch (HibernateException e){
			logger.fatal("Error on getScheduledJob: " + e.getMessage());
			trans.rollback();
		}
		finally{
			close();
		}
		return ScheduledJob;
	}
	
	public void update(Object ob) {
		Transaction tx = connectMSSQLHibernate();
		try
		{
			session.update(ob);
			tx.commit();
		}
		catch(HibernateException sqle)
		{
			System.out.println("Error on update: " + sqle.getMessage());
			tx.rollback();
		}
		finally {
		    close();
		}
	}
	
	/**
	 * Get a single instance of a table
	 * @return an object of the time c
	 */
	public Object get(Class<? extends java.io.Serializable> c, Serializable s){
		Transaction tx = connectMSSQLHibernate();
		Object o = null;
		try
		{
			o = session.get(c, s);
			tx.commit();
		}
		catch(HibernateException sqle)
		{
			System.out.println("Error on get: " + sqle.getMessage());
			tx.rollback();
		}
		finally {
		    close();
		}
		return o;
	}
	
	public void delete(Object ob) {
		Transaction tx = connectMSSQLHibernate();
		try
		{
			session.delete(ob);
			tx.commit();
		}
		catch(HibernateException sqle)
		{
			System.out.println("Error on delete: " + sqle.getMessage());
			tx.rollback();
		}
		finally {
		    close();
		}
	}
	
	public int saveReturnId(Object ob) {
		Transaction tx = connectMSSQLHibernate();
		int id = 0;
		try
		{
			id = (Integer) session.save(ob);
			tx.commit();
		}
		catch(HibernateException sqle)
		{
			System.out.println("Error on saveReturnId: " + sqle.getMessage());
			tx.rollback();
		}
		finally {
		    close();
		}
		return id;
	}
}
