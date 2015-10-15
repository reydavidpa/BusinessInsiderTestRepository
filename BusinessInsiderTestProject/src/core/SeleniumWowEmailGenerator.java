package core;

import helpers.MySQLDatabaseManager;
import helpers.SeleniumWowConstants;
import helpers.TestNGHelper;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import core.Printer;
import core.PrinterOutput;
import java.util.Properties;

public class SeleniumWowEmailGenerator{


	private Queue<PrinterOutput> output_queue;
	private Queue<PrinterOutput> header_queue;
	private ArrayList<String> output_heading;
	private ArrayList<String> output_data;

	public final String html_page_start="<html><body>";
	public final String html_page_end="<hr></body></html>";
	public final String line_breaks="<br><br>";
	private String test_title = "";

	public final String style_sheet = SeleniumWowConstants.STYLE_SHEET;
	


	public int get_max_data_row_size(ArrayList<ArrayList<String>> data_list){

		int data_row_size_check=0;
		int max_data_row_size=0;

		int size_of_list = data_list.size();

		if(size_of_list > 0){
			for(int h=0;h<size_of_list;h++){
				ArrayList<String> data_values_list = new ArrayList<String>();
				data_values_list = data_list.get(h);
				data_row_size_check=data_values_list.size();

				if(data_row_size_check > max_data_row_size){
					max_data_row_size = data_row_size_check;
				}
			}
		}
		return(max_data_row_size);


	}

	public SeleniumWowEmailGenerator(Queue<PrinterOutput> headers, Queue<PrinterOutput> data, String testTitle){
		output_queue = new LinkedList<PrinterOutput>(data);
		header_queue = new LinkedList<PrinterOutput>(headers);
		output_heading = new ArrayList<String>();
		output_data = new ArrayList<String>();
		test_title = testTitle;
	}

	public void process_output_heading(){
		PrinterOutput pout = new PrinterOutput();
		ArrayList<String> headings_list;
		ArrayList<ArrayList<String>> values_capsule;
		ArrayList<String> values_list;

		String line_start="<table><tr><td class=\"heading_name_heading\">";
		String line_middle="</td><td class=\"heading_value\">";
		String line_end="</td></tr></table>";
		String heading_header="<table class=outside><table><tr><td class =\"heading_title\" colspan=2>Test Information</td></tr></table>";
		String table_end="</table>";

		String _title = line_start + "Title" + line_middle + test_title + line_end; 

		output_heading.add(heading_header);
		output_heading.add(_title);
		try{
			pout = header_queue.peek();
			while(pout != null){
				headings_list = new ArrayList<String>();
				headings_list = pout.get_headings();
				String the_heading = headings_list.get(0);

				values_capsule = new ArrayList<ArrayList<String>>();
				values_capsule = pout.get_values();
				values_list = new ArrayList<String>();
				values_list = values_capsule.get(0);
				String the_value = values_list.get(0);

				String temp = line_start.concat(the_heading);
				String temp2 = temp.concat(line_middle);
				temp = temp2.concat(the_value);
				temp2 = temp.concat(line_end);

				output_heading.add(temp2);
				header_queue.remove();
				pout=header_queue.peek();
			}	
		}
		catch(Exception e){
			e.printStackTrace(System.out);
		}

		output_heading.add(table_end);
		output_heading.add(line_breaks);
	}

	public void process_output_values(){
		PrinterOutput pout = new PrinterOutput();
		ArrayList<String> headings_list;
		ArrayList<ArrayList<String>> values_capsule;
		ArrayList<String> values_list;

		Printer.PrinterOutputType ptype;
		int number_of_columns=0;
		String the_title=null;
		String line_end="</td></tr></table>";
		String table_end="</table>";

		String heading_line_start_tc="<table><tr><td class=\"heading_name_tc\">";
		String heading_line_start="<table><tr><td class=\"heading_name\" width=\"5%\">";
		String heading_line_middle="</td><td class=\"heading_name\" width=\" ";


		String data_line_start="<table><tr><td class=\"data_value\">";
		String data_line_middle="</td><td class=\"data_value\">";



		String data_line_start_width="<table><tr><td class=\"data_value\" width=\"5%\">";
		String data_line_start_width_alt="<table><tr><td class=\"data_value_alt_color\" width=\"";
		String data_line_middle_width="</td><td class=\"data_value\" width=\" ";
		String data_line_middle_width_alt="</td><td class=\"data_value_alt_color\" width=\" ";
		String data_line_close_width="%\">";


		try{
			pout = output_queue.peek();
			while(pout != null){

				the_title = new String();
				the_title = pout.get_title();

				ptype = pout.get_type();

				String data_header="<table class=outside><table><tr><td class =\"heading_title\" colspan=\"" + number_of_columns + "\">" + the_title + " </td></tr></table>";

				output_data.add(data_header);

				headings_list = new ArrayList<String>();
				headings_list = pout.get_headings();
				String the_heading;
				String the_whole_heading=heading_line_start;
				int test;

				values_capsule = new ArrayList<ArrayList<String>>();
				values_capsule = pout.get_values();
				values_list = new ArrayList<String>();
				values_list = values_capsule != null && values_capsule.size() > 0? values_capsule.get(0):new ArrayList<String>();
				String the_value;
				int data_rows = values_capsule.size();
				String the_whole_data_row=data_line_start;

				///FIgure out if the data has more cells than the headings
				int max_data_row_size=get_max_data_row_size(values_capsule);

				int head_size=headings_list.size();

				if(head_size < max_data_row_size){
					int diff = max_data_row_size - head_size;
					for(int u = 0; u < diff; u++){
						String add_me ="";
						pout.add_heading(add_me);
					}
				}
				headings_list = pout.get_headings();
				head_size=headings_list.size();
				number_of_columns = pout.get_columns();

				boolean h_msg_test = pout.has_helper_msg();
				if(h_msg_test){
					ArrayList<String> help_msg_test = new ArrayList<String>();
					help_msg_test = pout.get_helper_msg();
					String h_msg = help_msg_test.get(0);
					String h_msg_line="<table><tr><td class =\"heading_title\" colspan=\"" + number_of_columns + "\">" + h_msg + " </td></tr></table>";
					output_data.add(h_msg_line);
				}
				switch(ptype){
				case TABLE_ROW:{
					int headings_size=headings_list.size();

					//System.out.println("Heading size is..." + headings_size);
					test=0;
					//int heading_width=95/headings_size;
					//heading_line_middle = heading_line_middle + heading_width + data_line_close_width;
					//System.out.println("Heading line middle " + heading_line_middle);

					if(headings_size > 0){			
						int heading_width=95/headings_size;
						String heading_line_middle_instance = heading_line_middle + heading_width + data_line_close_width;
						for(int t =0; t<headings_size;t++){
							the_heading=headings_list.get(t);

							the_whole_heading=the_whole_heading.concat(the_heading);
							test=t+1;
							if(test < headings_size){
								the_whole_heading=the_whole_heading.concat(heading_line_middle_instance);
							}
							//System.out.println("line is  " + the_whole_heading);

						}
					}
					the_whole_heading=the_whole_heading.concat(line_end);
					output_data.add(the_whole_heading);							

					if(data_rows > 0)
					{
						for(int h=0;h<data_rows;h++)
						{
							values_list = new ArrayList<String>();
							if (values_capsule != null && values_capsule.size() > 0)
								values_list = values_capsule.get(h);

							test = 0;	
							the_whole_data_row = data_line_start;
							int row_size = values_list.size();
							int cell_width = 95/row_size;

							Integer new_width = new Integer(cell_width);
							String cell_width_string = new_width.toString();

							the_whole_data_row = data_line_start_width;

							String the_whole_data_row_alt = data_line_start_width_alt.concat(cell_width_string);
							the_whole_data_row_alt = the_whole_data_row_alt.concat(data_line_close_width);

							String new_middle = data_line_middle_width.concat(cell_width_string);
							new_middle = new_middle.concat(data_line_close_width);


							String new_middle_alt = data_line_middle_width_alt.concat(cell_width_string);
							new_middle_alt = new_middle_alt.concat(data_line_close_width);

							if(row_size > 0)
							{
								for(int r=0; r<row_size; r++)
								{
									the_value = values_list.get(r);
									the_whole_data_row = the_whole_data_row.concat(the_value);
									test = r+1;

									if(test < row_size)
									{
										the_whole_data_row = the_whole_data_row.concat(new_middle);
									}
								}
								the_whole_data_row = the_whole_data_row.concat(line_end);
								output_data.add(the_whole_data_row);
							}
							else
							{	
								the_whole_data_row=the_whole_data_row.concat(line_end);
								output_data.add(the_whole_data_row);
							}
						}

					}
					output_data.add(table_end);
				}	
				break;
				case TABLE_COLLUMN:
					//System.out.println("processing TABLE_COLLUMN\n");
					int headings_size = headings_list.size();
					if((headings_size > 0) && (data_rows > 0))
					{
						for(int t =0; t<headings_size;t++){
							the_heading = headings_list.get(t);

							values_list = new ArrayList<String>();
							if (values_capsule != null)
								values_list = values_capsule.get(t);
								the_value=values_list.get(0);

							the_whole_heading = heading_line_start_tc;
							the_whole_heading = the_whole_heading.concat(the_heading);
							the_whole_heading = the_whole_heading.concat(data_line_middle);
							the_whole_heading = the_whole_heading.concat(the_value==null?"":the_value);
							the_whole_heading = the_whole_heading.concat(line_end);
							output_data.add(the_whole_heading);
						}
					}
					output_data.add(table_end);
					break;
				default:
					System.out.println("can't find this ... " + ptype);
				}

				output_data.add(line_breaks);
				output_queue.remove();
				pout=output_queue.peek();

			}	
		}
		catch(Exception e)
		{
			System.out.println("ERROR on process_output_values: "+e.getMessage());
			//e.printStackTrace(System.out);
		}
	}

	public String send_mail(String testerEmail) throws MessagingException, AddressException{
		Properties props = System.getProperties();
//		String osName = System.getProperty("os.name").toLowerCase();

		//String server = "192.168.22.220";
		String server = "192.168.22.41";

		props.put("mail.smtp.host", server);
		//use the email address from the XML file as the from address
		String from = testerEmail;
//		String eric = "ehartill@onewire.com";
//		String jamie = "jhuenefeld@onewire.com";
		String rey = "rpalacios@onewire.com";
/*		String wendell = "wparris@onewire.com";
		String marla = "mjoseph@onewire.com";
		String carlos = "cromero@onewire.com";
		String pedro = "prodriquez@onewire.com";
		String ginny = "gpulikkan@onewire.com";
		String alex = "achan@onewire.com";
*/
		String header_table = new String();
		String data_table = new String();
		String temp = new String();
		String messageBody = new String();		

		int header_size = output_heading.size();
		for(int k = 0; k<header_size; k++)
		{
			temp = output_heading.get(k);
			header_table = header_table.concat(temp);
		}

		temp = html_page_start.concat(style_sheet);
		messageBody = temp.concat(header_table);

		int data_size = output_data.size();
		for(int k = 0; k < data_size; k++)
		{
			temp = output_data.get(k);
			data_table = data_table.concat(temp);
		}

		temp = html_page_start.concat(style_sheet);
		messageBody = temp.concat(header_table);
		temp = messageBody.concat(data_table);
		messageBody = temp.concat(html_page_end);

		Session session = Session.getDefaultInstance(props,null);
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from));
//		message.addRecipient(Message.RecipientType.TO, new InternetAddress(eric));
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(rey));
/*		message.addRecipient(Message.RecipientType.TO, new InternetAddress(wendell));
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(marla));
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(carlos));
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(pedro));
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(ginny));
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(alex));
*/		message.addRecipient(Message.RecipientType.TO, new InternetAddress(from));
		
		message.setSubject(test_title);
		BodyPart messageBodyPart1 = new MimeBodyPart();
		Multipart multipart = new MimeMultipart();
		
		//Get the count of images
		//String path = System.getProperty("user.dir");
		//String path = "Q:\\FRN\\QA\\ScriptImagesTemp";
		String path = "\\\\\\\\192.168.22.17\\\\images\\\\";
		int images = 0;
		//String addItToHTML="";
		
		File[] files = new File(path).listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				if(files[i].getName().contains(TestNGHelper.getEng().getTestTitle())){
					//addItToHTML = addItToHTML.concat("<img style='float:right' src='cid:image"+images+"_cid'>"+line_breaks);
					images++;	
		    	}
		    } 
		}
		
		// Add the html to the body
		//messageBody = messageBody.replace("</html>", addItToHTML+"</html>");
		messageBodyPart1.setContent(messageBody, "text/html; charset=ISO-8859-1");
		multipart.addBodyPart(messageBodyPart1);
		
		// Insert images into the email body
		for (int i = 0; i < images; i++) {
			BodyPart messageBodyPart2 = new MimeBodyPart();
			DataSource ds = new FileDataSource(path+TestNGHelper.getEng().getTestTitle()+i+".jpg");
			messageBodyPart2.setDataHandler(new DataHandler(ds));
			messageBodyPart2.setFileName(TestNGHelper.getEng().getTestTitle()+i+".jpg");
			//messageBodyPart2.addHeader("Content-ID", "<image"+i+"_cid>");
			multipart.addBodyPart(messageBodyPart2);
			messageBodyPart2 = new MimeBodyPart();
		}
		
		message.setContent(multipart);

		if (SeleniumWowTestNGScript.scheduler != null || TestNGHelper.getEng().getSchedulerId()==4){
			MySQLDatabaseManager database = new MySQLDatabaseManager();
			if (database.JobExists(TestNGHelper.getEng().getSchedulerId()) != null || TestNGHelper.getEng().getSchedulerId()==4)
				Transport.send(message);
			System.out.println("Regression email was sent out");
		}
		
		return messageBody;
	}
}
