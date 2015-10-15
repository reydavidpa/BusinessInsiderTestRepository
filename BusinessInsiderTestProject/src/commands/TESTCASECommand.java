package commands;

import helpers.MySQLDatabaseManager;
import java.util.ArrayList;
import org.hibernate.HibernateException;
import com.thoughtworks.selenium.*;
import core.PrinterOutput;
import core.SeleniumWowCommand;

public class TESTCASECommand extends SeleniumWowCommand<String,String>{

	private PrinterOutput po;

/**
 * The TFSID does two things for us:
 * 1) it prints out the value in the header so we can find the TFS Work Item
 * 2) it identifies the testcaseid in the database; the testcaseid is required for
 * the testcaseresults table records</p> 
 * If the script doesn't get to this command because it failed to start or it doesn't
 * have TFSID in it, then the value for testcaseid is 5 - Test did not run/No TFSID</p>
 * If there is no testCaseId for a given tfsid, then use 7 - Invalid TFSID
 * 
 * @param tfsid - Identifier for TFS Work Item ID 
 * @param testCaseResultsId - results ID used to update testcaseid
 * @param ds
 */
	public void execute(String testCaseId, String testCaseResultsId, DefaultSelenium ds)
	{
		System.out.println("Executing TESTCASE command ...");

		MySQLDatabaseManager dbm = new MySQLDatabaseManager();
		po = new PrinterOutput();
		po.add_heading("Test Case ID");

		//add TFS Id to header
		ArrayList<String> a = new ArrayList<String>();
		a.add(testCaseId);
		po.add_value(a);
		
		long testcaseidInt = -1;
		try
		{
			testcaseidInt = dbm.getTestCaseId(Integer.parseInt(testCaseId));
			if(testcaseidInt==-1)
				throw new SeleniumException("Error: test case id not found");
			else
				System.out.println("TestCaseId="+testCaseId);
			
		}
		catch (HibernateException sqle)
		{
			System.out.println("HibernateException testCaseId: "+sqle.getMessage());
		}
		
		//Update testcaseresults table with testcaseid
		try
		{
			dbm.insertTestCaseId(testcaseidInt, Integer.parseInt(testCaseResultsId));
			System.out.println("Insert testCaseId into testCaseResults");
		}
		catch (HibernateException sqle)
		{
			System.out.println("HibernateException insertTestCaseId: "+sqle.getMessage());
		}
	}

	public PrinterOutput get_output()
	{
		return(po);
	}
	public boolean is_header()
	{
		return(true);
	}
	public boolean has_output()
	{
		return(true);
	}
}
