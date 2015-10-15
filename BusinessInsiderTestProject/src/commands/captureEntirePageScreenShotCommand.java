package commands;

import helpers.TestNGHelper;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import com.thoughtworks.selenium.*;
import core.Printer;
import core.PrinterOutput;
import core.SeleniumWowCommand;
import core.SeleniumWowEngine;

public class captureEntirePageScreenShotCommand extends SeleniumWowCommand<String,String>{

	boolean hasOutput=true;
	private PrinterOutput po;
	private static final int IMG_WIDTH = 1200;
	private static final int IMG_HEIGHT = 1400;

	public void execute(String message, String name, DefaultSelenium ds){
		if (SeleniumWowEngine.getErrors().size()<2){
			
		System.out.println("Executing captureEntirePageScreenShot Command ... Page: " + ds.getLocation());
			
		String path = "\\\\\\\\192.168.22.17\\\\images\\\\";
		int images = 0;
		
		if (name == null)
			name=TestNGHelper.getEng().getTestTitle();
		
		if (!name.contains("Test did not run")){		

			File[] files = new File(path).listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile()) {
					if(files[i].getName().contains(name)){
						images++;	    		  
			    	}
			    } 
			}
			try{
				ds.captureEntirePageScreenshot(path.replace("\\\\\\\\", "\\\\")+name+images+".jpg","");
				File file = new File("\\\\192.168.22.17\\images\\"+name+images+".jpg");
            	double percent = 0.9;
				resize("\\\\192.168.22.17\\images\\"+name+images+".jpg", "\\\\192.168.22.17\\images\\"+name+images+".jpg", percent);
				// Print it in the email
				po = new PrinterOutput(Printer.PrinterOutputType.TABLE_ROW, "captureEntirePageScreenShot", 0, 8);
				if(message!=null){
					if(message.contains("error"))
						po.add_helper_msg(message+" - "+ ds.getLocation());
					else po.add_helper_msg(message);
				}else
					po.add_helper_msg("null message");
				po.addValue("<img style='float:right' heigth='800' width='800' src='http:////192.168.22.17//images//"+name+images+".jpg'>");
				
				System.out.println("Screenshot saved as: " +path+name+images+".jpg");
			} catch(SeleniumException se){
				if (se.getMessage().contains("captureEntirePageScreenshot is only implemented for Firefox")){
					hasOutput=false;
					System.out.println("captureEntirePageScreenshot is only implemented for Firefox");
				}
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
		}else{
			po=null;
			hasOutput=false;
		}
		
	}
	
	public static void resize(String inputImagePath,
            String outputImagePath, double percent) throws IOException {
        File inputFile = new File(inputImagePath);
        BufferedImage inputImage = ImageIO.read(inputFile);
        int scaledWidth = (int) (inputImage.getWidth() * percent);
        int scaledHeight = (int) (inputImage.getHeight() * percent);
        resize(inputImagePath, outputImagePath, scaledWidth, scaledHeight);
    }

	public static void resize(String inputImagePath,
            String outputImagePath, int scaledWidth, int scaledHeight)
            throws IOException {
        // reads input image
        File inputFile = new File(inputImagePath);
        BufferedImage inputImage = ImageIO.read(inputFile);
 
        // creates output image
        BufferedImage outputImage = new BufferedImage(scaledWidth,
                scaledHeight, inputImage.getType());
 
        // scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();
 
        // writes to output file
        ImageIO.write(outputImage, "png", new File(outputImagePath));
    }
	
	public PrinterOutput get_output(){
		return(po);
	}
	public boolean is_header(){
		return(false);
	}
	public boolean has_output(){
		return(hasOutput);
	}
}
