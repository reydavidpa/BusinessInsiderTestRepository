package commands;

import com.thoughtworks.selenium.*;
import commands.waitForPageToLoadCommand;
import core.Printer;
import core.PrinterOutput;
import core.SeleniumWowCommand;
import core.SeleniumWowCommandException;


public class verifyTextPresentCommand extends SeleniumWowCommand<String,String>{

	public PrinterOutput po;

	public void execute(String t, String v, DefaultSelenium ds){
		System.out.println("Executing verifyTextPresent command ...");
		
		po = new PrinterOutput(Printer.PrinterOutputType.TABLE_ROW, "VerifyTextPresent", 0, 1);
		boolean is_text = false;
		
		try{
			is_text = ds.isTextPresent(t);
		}catch(SeleniumException se){
			if (se.getMessage().contains("Couldn't access document.body")){
				new waitForPageToLoadCommand();
			}else 
				throw new SeleniumWowCommandException("Error on verifyTextPresent: "+se.getMessage());
		}
		
		if(is_text)
		{
			System.out.println("Text is present: " + t);
			po.addValue("Text \"" + t + "\" is present");
			po.setTitle(po.get_title() + ": TEST PASSED");
		}
		else{
			throw new SeleniumWowCommandException("Text is NOT present: " + t);
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
