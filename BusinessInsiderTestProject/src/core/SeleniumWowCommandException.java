package core;

import java.io.IOException;
import java.util.ArrayList;
import helpers.GlobalHelper;
import helpers.TestNGHelper;

/**
 * This is an exception for selenium wow commands; if a problem is found while
 * executing the command, throw this exception. It will be added to the errors
 * in the TestNGHelper.executeCommand() method.
 *
 * @author ehartill
 */
public class SeleniumWowCommandException extends RuntimeException
{
	org.apache.log4j.Logger logger = GlobalHelper.getLogger("");
	private static final long serialVersionUID = 1L;
	
	public SeleniumWowCommandException(String msg)
	{
		logger.error(msg);
		
		try {
			TestNGHelper.executeCommand("captureEntirePageScreenShotCommand", "ERROR: " +msg, null);
			ArrayList<String> error = new ArrayList<String>();
			error.add(msg);
			TestNGHelper.getEng().addErrors(error);
		} catch (IOException e) {
			System.out.println("Error on SeleniumWowCommandException due to captureEntirePageScreenShot");
		}
	}
	public SeleniumWowCommandException()
	{
		super();
	}
}
