package com.sa.tendable.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;

import org.apache.commons.io.FileUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

public class BaseTest {
	public WebDriver driver;
	public WebDriverWait wait;
	
	@BeforeTest
	@Parameters("browser")
	public void setup(String browser) {
		if (browser.equalsIgnoreCase("chrome")){
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--remote-allow-origins=*");
            options.addArguments("--start-maximized");
            options.addArguments("disable-infobars");
            driver = new ChromeDriver(options);
            System.out.println("Chrome Browser launched!");
        } else if (browser.equalsIgnoreCase("firefox")) {
            driver = new FirefoxDriver();
            System.out.println("Firefox Browser launched!");
        } else if (browser.equalsIgnoreCase("edge")) {
            EdgeOptions options = new EdgeOptions();
            options.addArguments("--remote-allow-origins=*");
            options.addArguments("--start-maximized");
            options.addArguments("disable-infobars");
            driver = new EdgeDriver(options);
            System.out.println("Edge Browser launched!");
        }
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));        
        driver.get("https://www.tendable.com/");
	}
	
	@AfterTest
	public void tearDown(){
        System.out.println("Driver going to close.");
        driver.quit();
    }
	
	public void writeBugReport() throws IOException, InvalidFormatException {
		
		String title = "No error message displayed when 'Message' field is left empty on Contact Us form\\n";
		
		TakesScreenshot screenshot = (TakesScreenshot) driver;
		File file = screenshot.getScreenshotAs(OutputType.FILE);
		try {			
			FileUtils.copyFile(file, new File(System.getProperty("user.dir")+"/Failed_Tests/"+title+".png"));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
			
		// Create blank word document
		XWPFDocument document = new XWPFDocument();
				
		// Add New Paragraph 
		XWPFParagraph p = document.createParagraph();
		XWPFRun run = p.createRun();
		run.setText(title);
		run.addCarriageReturn();
		
		//Create image file input stream
		File image = new File(String.valueOf(file));
		FileInputStream imageData = new FileInputStream(image);
		
		//Set image type and get image name
		int imageType = XWPFDocument.PICTURE_TYPE_JPEG;
		String imageFileName = image.getName();
		
		int imageWidth = 500;
		int imageHeight = 250;
		
		FileOutputStream output = new FileOutputStream(new File(System.getProperty("user.dir")+"/Failed_Tests/"+"Bug_report.docx"));
		run.addPicture(imageData, imageType, imageFileName, Units.toEMU(imageWidth) , Units.toEMU(imageHeight));
			
		document.write(output);
	}
}
