package com.sa.tendable.testcases;

import org.testng.Assert;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.NoSuchElementException;

import org.apache.commons.io.FileUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.sa.tendable.base.BaseTest;

public class TestTendable extends BaseTest{
	
	@Test(enabled = false)
	public void testTopLevelMenus() {
		
		WebElement linkHomePage = waitForElement(By.xpath("//a[@class='logo']"));
	    WebElement linkOurStory = waitForElement(By.xpath("//a[contains(text(),'Our Story')]"));
	    WebElement linkOurSolutions = waitForElement(By.xpath("//a[contains(text(), 'Our Solution')]"));
	    WebElement linkWhyTendable = waitForElement(By.xpath("//a[contains(text(), 'Why Tendable?')]"));
	    WebElement btnRequestADemo = waitForElement(By.xpath("//a[@class='button ' and contains(text(),'Request A Demo')]"));	    
	    
        if(linkHomePage.isDisplayed()) {
            System.out.println("Home page is accessible upon clicking company logo");
            Assert.assertTrue(verifyRequestDemoButton(), "'Request a Demo' button is not active on Home page");
        } else {
            Assert.assertTrue(linkHomePage.isEnabled(), "Home Page is not accessible");
        }

		if(linkOurStory.isDisplayed()) {
			linkOurStory.click();
			System.out.println("Our Story is displayed and accessible");
			wait.until(ExpectedConditions.elementToBeClickable(btnRequestADemo));
			Assert.assertTrue(verifyRequestDemoButton(), "'Request a Demo' button is not active on Our Story page");
		} else {
			Assert.assertTrue(linkOurStory.isEnabled(), "Our Story is not accessible");
		}
		
		if(linkOurSolutions.isEnabled()) {
			linkOurSolutions.click();
			System.out.println("Our Solutions is accessible");
			wait.until(ExpectedConditions.elementToBeClickable(btnRequestADemo));
			Assert.assertTrue(verifyRequestDemoButton(), "'Request a Demo' button is not active on Our Solutions page");
		} else {
			Assert.assertTrue(linkOurSolutions.isEnabled(), "Our Solutions is not accessible");
		}
		
		if(linkWhyTendable.isEnabled()) {
			linkWhyTendable.click();
			System.out.println("Why Tendable is accessible");
			wait.until(ExpectedConditions.elementToBeClickable(btnRequestADemo));
			Assert.assertTrue(verifyRequestDemoButton(), "'Request a Demo' button is not active on Why Tendable page");
		} else {
			Assert.assertTrue(linkWhyTendable.isEnabled(), "Why Tendable is not accessible");
		}
	}
	
	@Test
	public void testContactUsForm() throws InvalidFormatException, IOException, InterruptedException {
        // Navigate to Contact Us section and fill form
        waitForElement(By.linkText("Contact Us")).click();                
        waitForElement(By.xpath("//div[contains(text(),'Marketing')]/following-sibling::div/button")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".toggle-163701")));
        Thread.sleep(1000);
        waitForElement(By.name("fullName")).sendKeys("Test Name");
        waitForElement(By.name("organisationName")).sendKeys("SA Solutions Pvt. Ltd.");
        waitForElement(By.name("cellPhone")).sendKeys("9000080000");
        waitForElement(By.name("email")).sendKeys("test@example.com");
        WebElement jobRole = waitForElement(By.id("form-input-jobRole"));
        Select jobRoleDp = new Select(jobRole);
        jobRoleDp.selectByValue("Management"); 
        // Leave "Message" field empty and submit
        waitForElementToBeClickable(By.name("consentAgreed")).click();
        waitForElement(By.xpath("//button[text()='Submit']")).click();
        WebElement errorMessage = waitForElement(By.xpath("//div[@class=\"ff-form-error\"]/p"));
        Assert.assertTrue(errorMessage.isDisplayed(), "Error message is displayed as expected");
        
        // Verify error message
		/*
		 * try { WebElement errorMessage =
		 * waitForElement(By.xpath("//div[@class=\"ff-form-error\"]/p"));
		 * Assert.assertTrue(errorMessage.isDisplayed(),
		 * "Error message is displayed as expected"); } catch (NoSuchElementException e)
		 * { writeBugReport();
		 * Assert.fail("Error message was not displayed when 'Message' field is empty");
		 * }
		 */
    }	
	
	private WebElement waitForElement(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }
	
	private WebElement waitForElementToBeClickable(By locator) {
		return wait.until(ExpectedConditions.elementToBeClickable(locator));
	}
	
	private boolean verifyRequestDemoButton() {
        int attempts = 0;
        while (attempts < 3) {
            try {
                WebElement requestDemoButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@class='button ' and contains(text(),'Request A Demo')]")));
                return requestDemoButton.isDisplayed() && requestDemoButton.isEnabled();
            } catch (StaleElementReferenceException e) {
                attempts++;
                try {
                    Thread.sleep(1000); // Wait a bit before retrying
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return false;
    }
	
	/*
	 * private void writeBugReport() throws IOException, InvalidFormatException {
	 * String title =
	 * "No error message displayed when 'Message' field is left empty on Contact Us form\\n"
	 * ;
	 * 
	 * TakesScreenshot screenshot = (TakesScreenshot) driver; File file =
	 * screenshot.getScreenshotAs(OutputType.FILE); try { FileUtils.copyFile(file,
	 * new File(System.getProperty("user.dir")+"/Failed_Tests/"+title+".png")); }
	 * catch (Exception e) { // TODO: handle exception e.printStackTrace(); }
	 * 
	 * // Create blank word document XWPFDocument document = new XWPFDocument();
	 * 
	 * // Add New Paragraph XWPFParagraph p = document.createParagraph(); XWPFRun
	 * run = p.createRun(); run.setText(title); run.addCarriageReturn();
	 * 
	 * //Create image file input stream File image = new File(String.valueOf(file));
	 * FileInputStream imageData = new FileInputStream(image);
	 * 
	 * //Set image type and get image name int imageType =
	 * XWPFDocument.PICTURE_TYPE_JPEG; String imageFileName = image.getName();
	 * 
	 * int imageWidth = 500; int imageHeight = 250;
	 * 
	 * FileOutputStream output = new FileOutputStream(new
	 * File(System.getProperty("user.dir")+"/Failed_Tests/"+"Bug_report.docx"));
	 * run.addPicture(imageData, imageType, imageFileName, Units.toEMU(imageWidth) ,
	 * Units.toEMU(imageHeight));
	 * 
	 * document.write(output); }
	 */
}
