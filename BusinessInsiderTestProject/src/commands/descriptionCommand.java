package commands;

import java.util.ArrayList;
import com.thoughtworks.selenium.*;
import core.PrinterOutput;
import core.SeleniumWowCommand;


public class descriptionCommand extends SeleniumWowCommand<String,String>{

	public PrinterOutput po;
	public descriptionCommand(){
	}

	public void execute(String t, String v, DefaultSelenium ds){
		System.out.println("Executing description command ...");

		po = new PrinterOutput();
		po.add_heading("Description");

		ArrayList<String> a = new ArrayList<String>();
		a.add(t);
		po.add_value(a);
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
