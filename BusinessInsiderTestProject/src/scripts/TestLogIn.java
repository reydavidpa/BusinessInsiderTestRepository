package scripts;

import org.testng.annotations.*;
import helpers.TestNGHelper;

public class TestLogIn extends core.SeleniumWowTestNGScript
{
	private String title = "TestLogIn";

	@Test (groups = {"TestLogIn", ""}, dependsOnGroups = {})
	public void testTestLogIn() throws Exception
	{
		super.setTitle(title);

		try
		{
			System.out.println("EXECUTING TEST: " + title);

			//Script Data
			TestNGHelper.executeCommand("TESTCASECommand", "111111", null);
			TestNGHelper.executeCommand("descriptionCommand", "User logs in and logs out", null);
			TestNGHelper.executeCommand("testItemCommand", "Veridy user is able to log in", null);
			TestNGHelper.executeCommand("testItemCommand", "Verify authenticated sub trending can be open and closed", null);
			TestNGHelper.executeCommand("testItemCommand", "Veridy user is able to log out", null);
			TestNGHelper.executeCommand("testItemCommand", "Verify public search field can be open and closed", null);
			TestNGHelper.executeCommand("userCommand", "reydavid", null);
			
			//Script
			TestNGHelper.executeCommand("openCommand", "http://www.businessinsider.com/", null);
			TestNGHelper.executeCommand("logInCommand", "reydavid", "Password1");
			TestNGHelper.executeCommand("checkSubTrendingOpenCloseCommand", null, null);
			TestNGHelper.executeCommand("logOutCommand", "reydavid", null);
			TestNGHelper.executeCommand("checkSearchOpenCloseCommand", null, null);
			
		}
		catch(Exception e)
		{
			errors.add(e.getMessage());
			System.out.println("Error running " + title + " script: " + e.getMessage());
			throw e;
		}
	}
}

