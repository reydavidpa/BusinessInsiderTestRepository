package core;

import helpers.GlobalHelper;
import helpers.MySQLDatabaseManager;
import helpers.OWConstants;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import mappedTables.Scheduledjobs;
import mappedTables.Server;

import org.openqa.selenium.server.RemoteControlConfiguration;



import com.thoughtworks.selenium.DefaultSelenium;

@SuppressWarnings("deprecation")
public class SeleniumServerControl{

	//public static SeleniumServer server;
	public static RemoteControlConfiguration rcc;
	//private static Logger logger = GlobalHelper.getLogger("");
	public static String MULTIPLE_WINDOWS = "Multiple Windows";
	public static String SINGLE_WINDOW = "Single Window";
	public static Process p = null;
	public static ProcessBuilder builder; 
	public static BufferedReader r;
	public static String line;
	private static String Password = OWConstants.ReyPassword; //Rey's computer current password - this will need to be dynamic, if other users are using the Scheduler
	private static String SeleniumRC = OWConstants.SeleniumRC;
	private static MySQLDatabaseManager dm = new MySQLDatabaseManager();
	private static Server localServer = dm.getServerFromLocalHost();
	static int[] result = new int[2];
	private static int seleniumRCrestarted=0;
	//private static boolean servernotrunning=false;
	private static int serverCheckingTimes=1;
	public static DefaultSelenium Selenium;
	
	public int[] startSeleniumServer(String schedulerId, Server server, String browser, String Arguments, Boolean newPort){
		String runInPort = null;
		
		Scheduledjobs scheduledJob = (Scheduledjobs) dm.get("from Scheduledjobs sj where sj.jobstatusid=1 and sj.selwowscheduler.schedulerid="+schedulerId, true);
		
		if(scheduledJob != null){
			if(!newPort){
				runInPort = "-Port "+scheduledJob.getPort();
				result[0] = Integer.parseInt(runInPort.replace("-Port ", ""));
				System.out.println("Existing runInPort from DB is = "+runInPort);
			}else{
				runInPort = "-Port "+availablePort();
				result[0] = Integer.parseInt(runInPort.replace("-Port ", ""));
				System.out.println("New runInPort from DB is = "+runInPort);
			}
			
		}else{
			String Port1 = "0.0.0.0:3333 "; String Port2 = "0.0.0.0:3334 "; String Port3 = "0.0.0.0:3335 "; String Port4 = "0.0.0.0:3336 "; String Port5 = "0.0.0.0:3337 "; String Port6 = "0.0.0.0:3338 "; String Port7 = "0.0.0.0:3339";
			Boolean Port1Running = false; Boolean Port2Running = false; Boolean Port3Running = false; Boolean Port4Running = false; Boolean Port5Running = false; Boolean Port6Running = false; Boolean Port7Running = false; 
		
			if(localServer.getServerip().compareTo(server.getServerip())==0){ 
				builder = new ProcessBuilder("cmd.exe", "/c", "netstat -ano | findstr \""+Port1+Port2+Port3+Port4+Port5+Port6+Port7+"\"");
				System.out.println("netstat -ano | findstr \""+Port1+Port2+Port3+Port4+Port5+Port6+Port7+"\"");
			}else{
				builder = new ProcessBuilder("cmd.exe", "/c", "psexec \\\\"+server.getServerip()+" -s netstat -ano | findstr \""+Port1+Port2+Port3+Port4+Port5+Port6+Port7+"\"");
				System.out.println("psexec \\\\"+server.getServerip()+" -s netstat -ano | findstr \""+Port1+Port2+Port3+Port4+Port5+Port6+Port7+"\"");
			}
			
	        builder.redirectErrorStream(true);
	        
	        try {
				p = builder.start();
				
	        r = new BufferedReader(new InputStreamReader(p.getInputStream()));
	        
	        while (true) {
					line = r.readLine();
	            if (line == null) { break; }
	            if(line.length()==0) continue;
	            if (line.contains(Port1)) Port1Running=true; 
	            if (line.contains(Port2)) Port2Running=true;
	            if (line.contains(Port3)) Port3Running=true;
	            if (line.contains(Port4)) Port4Running=true;
	            if (line.contains(Port5)) Port5Running=true;
	            if (line.contains(Port6)) Port6Running=true;
	            if (line.contains(Port7)) Port7Running=true;
	            System.out.println(line);
	        }
	        } catch (IOException e1) {
				e1.printStackTrace();
			}
			if(!Port1Running) runInPort="-Port 3333";
			else if (!Port2Running) runInPort="-Port 3334";
			else if (!Port3Running) runInPort="-Port 3335";
			else if (!Port4Running) runInPort="-Port 3336";
			else if (!Port5Running) runInPort="-Port 3337";
			else if (!Port6Running) runInPort="-Port 3338";
			else if (!Port7Running) runInPort="-Port 3339";	
			else
				System.err.println("No ports available.");

			result[0] = Integer.parseInt(runInPort.replace("-Port ", ""));
		}
		
		try {
			
			Server officialServer = (Server) dm.get("from Server s where s.isSelwowschedulerServer=1", true);
			
			System.out.println("localServer.getServerip()="+localServer.getServerip());
			System.out.println("server.getServerip()="+server.getServerip());
			System.out.println("server.getServerid()="+server.getServerid());
			System.out.println("officialServer.getServerid()="+officialServer.getServerid());
			System.out.println("server.getServername()="+server.getServername());
			
			if(localServer.getServerip().compareTo(server.getServerip())==0 && server.getServerid()!=officialServer.getServerid() || schedulerId.equalsIgnoreCase("4") && localServer.getServername()==server.getServername()){
				if(Arguments.equalsIgnoreCase(MULTIPLE_WINDOWS)){
					if (browser.equalsIgnoreCase("Firefox")){
						builder = new ProcessBuilder("cmd.exe", "/c", "start cmd.exe /k \"java -jar c:\\commons\\Selenium\\"+SeleniumRC +runInPort+" -firefoxprofiletemplate c:\\commons\\FirefoxProfile\"");
						System.out.println("start cmd.exe /k \"java -jar c:\\commons\\Selenium\\"+SeleniumRC+runInPort+" -firefoxprofiletemplate c:\\commons\\FirefoxProfile\"");
					}else{
						builder = new ProcessBuilder("cmd.exe", "/c", "start cmd.exe /k \"java -jar c:\\commons\\Selenium\\"+SeleniumRC+runInPort+" \"");
						System.out.println("start cmd.exe /k \"java -jar c:\\commons\\Selenium\\"+SeleniumRC+runInPort+" \"");
					}
				}else{
					if (browser.equalsIgnoreCase("Firefox")){
						builder = new ProcessBuilder("cmd.exe", "/c", "start cmd.exe /k \"java -jar c:\\commons\\Selenium\\"+SeleniumRC+runInPort+" -singlewindow -firefoxprofiletemplate c:\\commons\\FirefoxProfile\"");
						System.out.println("start cmd.exe /k \"java -jar c:\\commons\\Selenium\\"+SeleniumRC+runInPort+" -singlewindow -firefoxprofiletemplate c:\\commons\\FirefoxProfile\"");
					}else{
						builder = new ProcessBuilder("cmd.exe", "/c", "start cmd.exe /k \"java -jar c:\\commons\\Selenium\\"+SeleniumRC+runInPort+" -singlewindow\"");
						System.out.println("start cmd.exe /k \"java -jar c:\\commons\\Selenium\\"+SeleniumRC+runInPort+" -singlewindow\"");
					}
				}
		        builder.redirectErrorStream(true);
		        p = builder.start();
		        if(!schedulerId.equalsIgnoreCase("4"))
		        	GlobalHelper.pause(5000);
		        else
		        	GlobalHelper.pause(1000);
		        builder = new ProcessBuilder("cmd.exe", "/c", "netstat -ano | findstr \""+"0.0.0.0:"+runInPort.replace("-Port ", "")+"\"");
		        System.out.println("netstat -ano | findstr \""+"0.0.0.0:"+runInPort.replace("-Port ", "")+"\"");
			}else{
				if(Arguments.equalsIgnoreCase(MULTIPLE_WINDOWS)){
						if (browser.equalsIgnoreCase("Firefox")){
							builder = new ProcessBuilder("cmd.exe", "/c", "psexec \\\\"+server.getServerip()+ " -u FSVS\\rpalacios -p "+Password+" -d -i "+server.getSession()+" java -jar c:\\commons\\Selenium\\"+SeleniumRC+runInPort+" -firefoxprofiletemplate c:\\commons\\FirefoxProfile");
							System.out.println("2. psexec \\\\"+server.getServerip()+ " -u FSVS\\rpalacios -p "+Password+" -d -i "+server.getSession()+" java -jar c:\\commons\\Selenium\\"+SeleniumRC+runInPort+" -firefoxprofiletemplate c:\\commons\\FirefoxProfile");
						}else{
							builder = new ProcessBuilder("cmd.exe", "/c", "psexec \\\\"+server.getServerip()+ " -u FSVS\\rpalacios -p "+Password+" -d -i "+server.getSession()+" java -jar c:\\commons\\Selenium\\"+SeleniumRC+runInPort);
							System.out.println("3. psexec \\\\"+server.getServerip()+ " -u FSVS\\rpalacios -p "+Password+" -d -i "+server.getSession()+" java -jar c:\\commons\\Selenium\\"+SeleniumRC+runInPort);
						}
					//}
				}else{
						if (browser.equalsIgnoreCase("Firefox")){
							builder = new ProcessBuilder("cmd.exe", "/c", "psexec \\\\"+server.getServerip()+" -u FSVS\\rpalacios -p "+Password+" -d -i "+server.getSession()+" java -jar c:\\commons\\Selenium\\"+SeleniumRC+runInPort+" -singlewindow -firefoxprofiletemplate c:\\commons\\FirefoxProfile");
							System.out.println("4. psexec \\\\"+server.getServerip()+" -u FSVS\\rpalacios -p "+Password+" -d -i "+server.getSession()+" java -jar c:\\commons\\Selenium\\"+SeleniumRC+runInPort+" -singlewindow -firefoxprofiletemplate c:\\commons\\FirefoxProfile");
						}else{
							builder = new ProcessBuilder("cmd.exe", "/c", "psexec \\\\"+server.getServerip()+" -u FSVS\\rpalacios -p "+Password+" -d -i "+server.getSession()+" java -jar c:\\commons\\Selenium\\"+SeleniumRC+runInPort+" -singlewindow");
							System.out.println("5. psexec \\\\"+server.getServerip()+" -u FSVS\\rpalacios -p "+Password+" -d -i "+server.getSession()+" java -jar c:\\commons\\Selenium\\"+SeleniumRC+runInPort+" -singlewindow");
						}
				}
			}
	        builder.redirectErrorStream(true);
	        p = builder.start();
	        if(!schedulerId.equalsIgnoreCase("4"))
	        	GlobalHelper.pause(5000);
	        else
	        	GlobalHelper.pause(3000);
	        r = new BufferedReader(new InputStreamReader(p.getInputStream()));
	        line = new String();
	        while (true) {
	            line = r.readLine();
		        if (line == null) { break; }
	            if(line.length()==0) continue;
		           	if (line.contains("LISTENING"))
			           	result[1]=Integer.parseInt(line.split("LISTENING")[1].trim());		            	
				    if (line.contains("process ID"))
				       	result[1]=Integer.parseInt(line.split("ID ")[1].replace(".", ""));
	        }
	        if(scheduledJob != null){
		        scheduledJob.setProcessId(result[1]);
		        dm.update(scheduledJob);
	        }
	        
	        GlobalHelper.pause(1500);
	        			
	        if(!isSeleniumServerRunning(server)){
	        	seleniumRCrestarted++;
	        	if(seleniumRCrestarted<2)
	        		startSeleniumServer(schedulerId,server,browser,Arguments,true);
	        	else{
	        		Send_mail("reydavid_xs@hotmail.com",server.getServername()+" is down!");
	        	}
	        }
	        
	        System.out.println("Selenium RC has been started on " + server.getServername() +" in " + runInPort.replace("-", "")  + " - ProcessID = "+result[1]);

			if(!schedulerId.equalsIgnoreCase("4")){
				if(scheduledJob.getJobstatusid()==4){
					scheduledJob = (Scheduledjobs) dm.get("from Scheduledjobs sj where sj.jobstatusid=1 and sj.selwowscheduler.schedulerid="+schedulerId, true);
					
					if(scheduledJob==null || scheduledJob.getJobstatusid()==4){
						Scheduledjobs stopSJ = new Scheduledjobs();
						stopSJ.setPort(result[0]);
						stopSJ.setProcessId(result[1]);
						//TestNGHelper.executeCommand("LogOutCommand", null, null);
						stopSeleniumServer(stopSJ, server);
					}
				}
			}
			
			
			
		} catch (IOException e) {
			System.out.println("Error stating selenium RC: "+e.getMessage());
		}
		return result;
	}

	private static void Send_mail(String testerEmail, String subject) {
		Properties props = System.getProperties();

		String server = "192.168.22.111";

		props.put("mail.smtp.host", server);
		String from = testerEmail;
		String rey = "reydavid_x@hotmail.com";

		Session session = Session.getDefaultInstance(props,null);
		Message message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(rey));
			message.setSubject(subject);
			message.setText("");

			Transport.send(message);
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void stopSeleniumServer(Scheduledjobs sj, Server server) {
		
		System.out.println("Killing Job Id ... "+sj.getSchedulejobid());
		System.out.println("Process Id ... "+sj.getProcessId());
		
		if(sj.getProcessId()!=0){
			try {
				builder = new ProcessBuilder("cmd.exe", "/c", "psexec \\\\"+server.getServerip()+" -s taskkill /F /PID "+sj.getProcessId()+" /T");
					
		        builder.redirectErrorStream(true);
		        p = builder.start();
		        r = new BufferedReader(new InputStreamReader(p.getInputStream()));
		        line = new String();
		        while (true) {
		            line = r.readLine();
		            if (line == null) { break; }
		            if(line.length()==0) continue;
		            //if (line.contains("process ID")){
		            	//System.out.println(line);
		            /*	result[1]=Integer.parseInt(line.split("ID")[1]);
		            }*/
		        }
				
				System.out.println("Selenium RC has been stopped on " + server.getServername() +" with Process ID = " + sj.getProcessId());
			} catch (IOException e) {
				System.out.println("Error stating selenium RC: "+e.getMessage());
			}
		}else{
			try {
				if(localServer.getServerip().compareTo(server.getServerip())==0){
					builder = new ProcessBuilder("cmd.exe", "/c", "netstat -ano | findstr \""+sj.getPort()+"\"");
					System.out.println("netstat -ano | findstr \""+sj.getPort()+"\"");
				}else{
					builder = new ProcessBuilder("cmd.exe", "/c", "psexec \\\\"+server.getServerip()+" -s netstat -ano | findstr \""+sj.getPort()+"\"");
					System.out.println("psexec \\\\"+server.getServerip()+" -s netstat -ano | findstr \""+sj.getPort()+"\"");
				}
				
		        builder.redirectErrorStream(true);
		        p = builder.start();
		        r = new BufferedReader(new InputStreamReader(p.getInputStream()));
		        line = new String();
		        while (true) {
		            line = r.readLine();
		            if (line == null) { break; }
		            if(line.length()==0) continue;
		            if (line.contains("LISTENING")){
		            	sj.setProcessId(Integer.parseInt(line.split("LISTENING")[1].trim()));
		            	stopSeleniumServer(sj, server);
		            	break;
		            }
		        }
				
				System.out.println("Selenium RC has been stopped on " + server.getServername() +" with Process ID = " + result[1]);
			} catch (IOException e) {
				System.out.println("Error stating selenium RC: "+e.getMessage());
			}
		}
	}
	
	public int availablePort(){
		
		ArrayList<Integer> ports = (ArrayList<Integer>) new MySQLDatabaseManager().get("select s.port from Scheduledjobs s where s.jobstatusid in (0,1)", false);
		Collections.sort(ports);
		int port = 0;
		int j=0;
		//if(isSingle==1){
		for (int i=1 ; i <= 100; i++)			
			if(ports.isEmpty()){
				port=3333;
			}else{
				for(j=0; j < ports.size(); j++)
					if (ports.get(j)!=3333+j){	System.out.println("ports.get(j)="+ports.get(j)); break; }
				port = 3333+j;
				break;
			}
		
		return port;
	}
	
	private static boolean isSeleniumServerRunning(Server server) {
		 try {
	            System.out.println("Checking selenium server status [ http://" + server.getServerip() + ":" +result[0] + "]");
	            URL url = new URL("http://" + server.getServerip() + ":" + result[0] + "/selenium-server/driver/?cmd=testComplete");
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK)
	                return true;
	            else{
	            	if(serverCheckingTimes<3){
	            		System.out.println("Checking selenium Server status again");
		            	isSeleniumServerRunning(server);
		            	serverCheckingTimes++;
	            	}
	            }
	        } catch (Exception e) {
	            System.err.println("Could not check selenium server status: " + e.getMessage());
	        }
	    return false;
	}
}