package core;

import org.apache.log4j.Logger;
import helpers.GlobalHelper;
import com.thoughtworks.selenium.*;
import core.PrinterOutput;

public abstract class SeleniumWowCommand<T,V>
{
	protected Logger logger = initialiseLogger(this+""); 
	public abstract void execute(T t, V v, DefaultSelenium ds);
	public abstract PrinterOutput get_output();

	public abstract boolean has_output();
	public abstract boolean is_header();
	
	public Logger initialiseLogger(String logDir)
	{
		logger= GlobalHelper.getLogger(logDir);
		return logger;
	}
			
}

