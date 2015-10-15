package commands;

import com.thoughtworks.selenium.*;
import core.PrinterOutput;
import core.SeleniumWowCommand;

public class testItemCommand extends SeleniumWowCommand<String,String>
{

	private PrinterOutput po;

	public void execute(String t, String v, DefaultSelenium ds){
		System.out.println("Executing testItem command ...");

		po = new PrinterOutput();
		po.add_heading("Test Item");

		po.addValue(t);
	}

	public PrinterOutput get_output(){
		return(po);
	}
	public boolean is_header(){
		return(true);
	}
	public boolean has_output(){
		return(true);
	}
}
