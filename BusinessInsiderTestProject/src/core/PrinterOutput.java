package core;

import java.util.ArrayList;


public class PrinterOutput{
	@SuppressWarnings("unchecked")
	public ArrayList output_list;
	public Printer.PrinterOutputType ptype;
	public int rows;
	public int columns;
	public String title;
	public boolean use_helper_msg = false;

	public ArrayList<String> data_headings;
	public ArrayList<ArrayList<String>> data_values;
	public ArrayList<String> helper_msg;

	public PrinterOutput(String t){

		ptype = Printer.PrinterOutputType.HEADER;
		helper_msg = new ArrayList<String>();
		data_headings = new ArrayList<String>();
		data_values = new ArrayList<ArrayList<String>>();	
		rows = 1;
		columns = 1;
		title = new String(t);
	}

	public PrinterOutput(){

		ptype = Printer.PrinterOutputType.HEADER;
		helper_msg = new ArrayList<String>();
		data_headings = new ArrayList<String>();
		data_values = new ArrayList<ArrayList<String>>();	
		rows = 1;
		columns = 1;
	}

	@SuppressWarnings("unchecked")
	public PrinterOutput(Printer.PrinterOutputType p){
		ptype = p;
		helper_msg = new ArrayList<String>();
		output_list = new ArrayList();
		data_headings = new ArrayList<String>();
		data_values = new ArrayList<ArrayList<String>>();	
		rows = 1;
		columns = 1;
	}

	@SuppressWarnings("unchecked")
	public PrinterOutput(Printer.PrinterOutputType p, String _title){
		ptype = p;
		title = new String (_title);
		helper_msg = new ArrayList<String>();
		output_list = new ArrayList();
		data_headings = new ArrayList<String>();
		data_values = new ArrayList<ArrayList<String>>();	
		rows = 1;
		columns = 1;
	}

	/**
	 * 
	 * @param p
	 * @param _title
	 * @param rows_ - used for PrinterOutputType.TABLE_COLLUMN
	 * @param columns_ - used for PrinterOutputType.TABLE_ROW
	 * 				   - this sets the number of columns in the email output
	 */
	@SuppressWarnings("unchecked")
	public PrinterOutput(Printer.PrinterOutputType p, String _title, int rows_, int columns_){
		ptype = p;
		title = new String (_title);
		helper_msg = new ArrayList<String>();
		output_list = new ArrayList();
		data_headings = new ArrayList<String>();
		data_values = new ArrayList<ArrayList<String>>();	
		rows = rows_;
		columns = columns_;
	}

	public void test_this(){
		String tempy = new String("what");
		data_headings.add(tempy);

		int yku = data_headings.size();
		System.out.println("size is " + yku);
	}

	public String get_title(){
		return(title);
	}
	public void setTitle(String title)
	{
		this.title = title;
	}

	@SuppressWarnings("unchecked")
	public ArrayList get_printer_output(){
		return(this.output_list);
	}

	public ArrayList<String> get_headings(){
		return(this.data_headings);
	}

	public ArrayList<ArrayList<String>> get_values(){
		return(data_values);
	}

	public Printer.PrinterOutputType get_type(){
		return(this.ptype);
	}

	public void set_rows(int i){
		rows = i;
	}

	public int get_rows(){
		return(this.rows);
	}

	public void set_columns(int i){
		columns = i;
	}

	public int get_heading_size(){
		int size_check = data_headings.size();
		return(size_check);
	}
	public int get_columns(){
		return(columns);
	}
	public void add_heading(String h)
	{
		if(data_headings != null)
		{
			data_headings.add(h);
		}
		else
		{
			System.out.println("Couldn't add heading.");
		}
	}
	/**
	 * This method sets the table of values - anything that was in
	 * the PrinterOutput object will be overwritten.
	 * @param aas
	 */
	public void add_table_of_values(ArrayList<ArrayList<String>> aas){
		data_values = aas;
	}

	public void add_value(ArrayList<String> al){
		data_values.add(al);
	}

	/**
	 * This method appends a new table to the end of the old one.
	 * @param t
	 */
	public void addTable(ArrayList<ArrayList<String>> t)
	{
		data_values.addAll(t);
	}
	public void addValue(String value)
	{
		ArrayList<String> al = new ArrayList<String>();
		al.add(value);
		data_values.add(al);
	}

	public void add_helper_msg(String al){
		helper_msg.add(al);
		use_helper_msg = true;
	}

	public ArrayList<String> get_helper_msg(){
		return(this.helper_msg);
	}

	public boolean has_helper_msg(){
		return(use_helper_msg);
	}
	/**
	 * A convenience method for quickly adding a heading and a value. Useful for column-based tables. 
	 * @param head  the heading
	 * @param value the value
	 */
	public void addHeadAndValue(String head, String value){
		add_heading(head);
		addValue(value);
	}

	public static void main(String args[]){

		PrinterOutput pop = new PrinterOutput();
		//pop.test_this();
		//int kw;
		//System.out.println("headings has " + kw);

		ArrayList<String> ala = new ArrayList<String>();
		//ala = pop.get_headings();

		//kw = ala.size();

		String jamie = new String("JAmie");
		String house = new String("house");
		String xhouse = new String("xhouse");

		pop.add_heading(jamie);
		pop.add_heading(house);


		ala = pop.get_headings();
		int howmany;
		try{
			howmany = ala.size();
			System.out.println("list contains...." + howmany);
		}
		catch(Exception ex)
		{System.out.println(ex.getMessage());}

		pop.add_heading(xhouse);

		ala = pop.get_headings();

		String testing;

		testing = (String)ala.get(0);

		howmany = ala.size();
		System.out.println("Print all...\n");

		for(int u =0;u <howmany; u++){

			testing = (String)ala.get(u);
			System.out.println("list contains..." + testing);
			System.out.println("list contains this many..." + howmany);
		}
	}
}


