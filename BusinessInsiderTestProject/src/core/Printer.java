package core;

public class Printer
{

	public enum PrinterOutputType{
		HEADER("HEADER"), TABLE_ROW("TABLE_ROW"), TABLE_COLLUMN("TABLE_COLLUMN");
		private String output_type;

		private PrinterOutputType(String pot)
		{
			this.output_type = pot;
		}

		public String get_type()
		{
			return output_type;
		}
	}
}
