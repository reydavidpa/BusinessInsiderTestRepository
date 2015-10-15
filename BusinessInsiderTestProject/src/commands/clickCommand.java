package commands;

import com.thoughtworks.selenium.*;
import core.PrinterOutput;
import core.SeleniumWowCommand;

public class clickCommand extends SeleniumWowCommand<String,String>{

	public String _target;
	public String _value;
	boolean output_exists;
	public PrinterOutput po;

	public clickCommand(){
	}

	public void execute(String t, String v, DefaultSelenium ds){
		System.out.println("Executing click command ... Click " + t);
		ds.click(t);
	}

	public PrinterOutput get_output(){
		return(po);
	}
	public boolean is_header(){
		return(false);
	}
	public boolean has_output(){
		return(false);
	}
}
