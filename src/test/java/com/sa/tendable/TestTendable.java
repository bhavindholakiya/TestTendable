package com.sa.tendable;

import org.testng.Assert;
import org.testng.ITestResult;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.util.NoSuchElementException;

import org.apache.commons.io.FileUtils;
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

import com.google.common.io.Files;

public class TestTendable {
	WebDriver driver;
	WebDriverWait wait;
	
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
	public void testContactUsForm() {
        // Navigate to Contact Us section and fill form
        waitForElement(By.linkText("Contact Us")).click();                
        waitForElement(By.xpath("//div[contains(text(),'Marketing')]/following-sibling::div/button")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".toggle-163701")));
        waitForElement(By.name("fullName")).sendKeys("Test Name");
        waitForElement(By.name("organisationName")).sendKeys("SA Solutions Pvt. Ltd.");
        waitForElement(By.name("cellPhone")).sendKeys("9000080000");
        waitForElement(By.name("email")).sendKeys("test@example.com");
        WebElement jobRole = waitForElement(By.id("form-input-jobRole"));
        Select jobRoleDp = new Select(jobRole);
        jobRoleDp.selectByValue("Management"); 
        // Leave "Message" field empty and submit
        waitForElement(By.name("consentAgreed")).click();
        waitForElement(By.xpath("//button[text()='Submit']")).click();

        // Verify error message
        try {
            WebElement errorMessage = waitForElement(By.xpath("//div[@class=\"ff-form-error\"]/p"));
            Assert.assertTrue(errorMessage.isDisplayed(), "Error message is displayed as expected");
        } catch (NoSuchElementException e) {
            //writeBugReport();
            Assert.fail("Error message was not displayed when 'Message' field is empty");
        }
    }
		
	@AfterTest
	public void tearDown(){
        System.out.println("Driver going to close.");
        driver.quit();
    }
	
	private WebElement waitForElement(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
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
	
	private void writeBugReport(ITestResult result) {
		String pageTitle = driver.getTitle();
		Assert.assertEquals(pageTitle, "Contact us | Quality Improvement Solution | Tendable | Tendable", "The title is invalid");
		
		String methodName = result.getName();
		TakesScreenshot screenshot = (TakesScreenshot) driver;
		File file = screenshot.getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(file, new File("./Failed_Test/screen.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
        String bugReport = "Bug Report: Error message not displayed when 'Message' field is empty on the Contact Us form.";
        try (FileWriter writer = new FileWriter("BugReport.docx")) {
            writer.write(bugReport);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
