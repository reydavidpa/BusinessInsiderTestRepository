package helpers;

public class SeleniumWowConstants
{

	//STYLING
	/**
	 * The intention here is to be able to construct a style sheet from various parts. The public
	 * constant STYLE_SHEET can be changed by changing one of the private constants, or by creating
	 * a new private constant and adding it to STYLE_SHEET.
	 */
	//Private Constants
	private static String styleSheetHeader = "<style type=\"text/css\">";
	private static String table = "table{width:800px; border-width:1px; border-style:none; cell-spacing:0px;" +
		"cell-padding:1px; cell-spacing:1px;}";
	private static String tableOutside = "table.outside{width:800px; border-width:3px; border-style:solid;" +
		"border-color:#9900CC; background-color:#FFFFCC; cell-padding:3px; cell-spacing:2px;}";
	private static String tdHeadingTitle = "td.heading_title{color:#FFFFCC; background-color:#006600;" +
		"font-family:verdana; text-align:center; font-weight:bold; font-size:14px; width:100%;}";
	private static String tdHeadingName = "td.heading_name { color:#006600; font-family:verdana; font-weight:bold;" +
		"border-style:solid; border-color:#FFFFFF; border-width:2px; font-size:12px;}";
	private static String tdHeadingNameTc = "td.heading_name_tc {color:#FFFFCC; width:200px;" +
		"background-color:#006600; font-family:verdana; font-weight:bold; border-style:solid; border-color:#FFFFFF;" +
		"border-width:2px; font-size:12px;}";
	private static String tdHeadingNameHeading = "td.heading_name_heading {color:#FFFFCC; width:100px;" +
		"background-color:#006600; font-family:verdana; font-weight:bold; border-style:solid; border-color:#FFFFFF;" +
		"border-width:2px; font-size:12px;}";
	private static String tdHeadingValue = "td.heading_value {color:#006600; font-family:verdana; font-size:12px;" +
		"border-style:solid; border-color:#FFFFFF; border-width:2px; width:300px;}";
	private static String tdDataValue = "td.data_value { color:#006600; font-family:verdana; font-size:12px;" +
		"border-style:solid; border-color: #FFFFFF; border-width: 2px;}";
	private static String tdDataValueAltColor = "td.data_value_alt_color {background-color:#CCCCFF; color:#006600;" +
		"font-family:verdana; font-size:12px; border-style:solid; border-color:#FFFFFF; border-width:2px;}";
	//headingDiv and dataDiv are used to override the styles
	private static String dataDiv = "div.data {background-color:#FFFFCC; color:#006600; width:100%; border-width:0px; cell-padding:0px;}";
	private static String headingDiv = "div.heading {background-color:#006600; color:#FFFFCC; width:100%; border-width:0px; cell-padding:0px;}";
	private static String styleSheetFooter = "</style>";

	//style for errors
	private static String errorSpan = "span#error {color:#FF0000; }";
	private static String errorDiv = "div#error {color:white; background-color:red}";

	//style for search strings
	private static String searchSpan = "span#search {background-color:#E48400}";

	//Public Constants
	public static final String STYLE_SHEET = styleSheetHeader + table + tableOutside + tdHeadingTitle + tdHeadingName +
		tdHeadingNameTc + tdHeadingNameHeading + tdHeadingValue + tdDataValue + tdDataValueAltColor + dataDiv + headingDiv +
		errorDiv + errorSpan + searchSpan + styleSheetFooter;
}
