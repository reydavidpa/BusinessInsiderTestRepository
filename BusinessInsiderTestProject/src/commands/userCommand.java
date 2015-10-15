package commands;

import java.util.ArrayList;
import com.thoughtworks.selenium.*;
import core.PrinterOutput;
import core.SeleniumWowCommand;


public class userCommand extends SeleniumWowCommand<String,String>{

	private PrinterOutput po;

/**
 * @param t - candidate name/email 
 * @param v - second candidate name/email (optional)
 * @param ds
 */
	public void execute(String t, String v, DefaultSelenium ds){
		System.out.println("Executing candidate command ...");

		po = new PrinterOutput();
		po.add_heading("User");

		String user = new String();
		if(v != null && !"".equals(v))
		{
			user = t.concat(" | ");
			user = user.concat(v);
		}
		else
		{
			user = t;
		}

		ArrayList<String> a = new ArrayList<String>();
		a.add(user);
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
