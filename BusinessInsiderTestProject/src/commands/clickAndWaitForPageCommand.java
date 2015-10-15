package commands;

import java.io.IOException;
import helpers.TestNGHelper;
import com.thoughtworks.selenium.*;
import core.PrinterOutput;
import core.SeleniumWowCommand;
import core.SeleniumWowCommandException;

public class clickAndWaitForPageCommand extends SeleniumWowCommand<String,String>{

	public clickAndWaitForPageCommand(){
	}

	public void execute(String t, String v, DefaultSelenium ds){
		System.out.println("Executing clickAndWaitForPageCommand ... Click " + t + " and wait for page to load");
		
		//ds.focus(t);
		ds.click(t);
		try{
			ds.waitForPageToLoad("220000");
		}catch(SeleniumWowCommandException e){
			System.out.println("Error on clickAndWaitForPageCommand: "+e.getMessage());
		}
		if(v!=null){
			try {
				TestNGHelper.executeCommand("captureEntirePageScreenShotCommand", v, null);
			} catch (IOException e) {
			}
		}
	}

	public PrinterOutput get_output(){
		return(null);
	}
	public boolean is_header(){
		return(false);
	}
	public boolean has_output(){
		return(false);
	}
}
