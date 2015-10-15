package commands;

import com.thoughtworks.selenium.*;
import core.Printer;
import core.PrinterOutput;
import core.SeleniumWowCommand;
import core.SeleniumWowCommandException;

public class verifyElementPresentCommand extends SeleniumWowCommand<String,String>{
	
	private PrinterOutput po;

	/**
	 * @param t - locator of element
	 * @param v
	 * @param ds
	 */
	public void execute(String t, String v, DefaultSelenium ds){
		System.out.println("Executing VerifyElementPresent command ...");
		boolean is_element;
		po = new PrinterOutput(Printer.PrinterOutputType.TABLE_ROW, "VerifyElementPresent", 0, 1);

		is_element = ds.isElementPresent(t);

		if(is_element)
		{
			System.out.println("Element is present: " + t);
			po.addValue("Element " + t + " is present");
			po.setTitle(po.get_title() + ": TEST PASSED");
		}
		else
		{
			//System.out.println("Element is NOT present: " + t);
			po.addValue("Element " + t + " is NOT present");
			po.setTitle(po.get_title() + ": TEST FAILED");
			throw new SeleniumWowCommandException("Element is NOT present: " + t);			
		}
	}

	public PrinterOutput get_output(){
		return(po);
	}
	public boolean is_header(){
		return(false);
	}
	public boolean has_output(){
		return(true);
	}
}
