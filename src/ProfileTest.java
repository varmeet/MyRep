import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;


public class ProfileTest {
	WebDriver driver=null;
	String portfolioName="Varin006";
	String newPortfolioName="Varin666";
	@Test(priority=1)
	public void createProfileTest() throws InterruptedException{
		String browser="Mozilla";

		
		if(browser.equals("Mozilla")){
			driver=new FirefoxDriver();
			}else if (browser.equals("Chrome")){
				System.setProperty("webdriver.chrome.driver", "D:\\Selenium_NEW\\chromedriver.exe");
				driver=new ChromeDriver();
			}
		
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		
		driver.get("http://in.rediff.com/");
		driver.findElement(By.xpath("//*[@id='homewrapper']/div[5]/a[3]/div/u")).click();
		driver.findElement(By.xpath("//*[@id='wrapper']/div[2]/ul/li[2]/a")).click();
		driver.findElement(By.xpath("//*[@id='useremail']")).sendKeys("varinmgh507");
		driver.findElement(By.xpath("//*[@id='emailsubmit']")).click();
		
		driver.findElement(By.xpath("//*[@id='userpass']")).sendKeys("varin31");
		driver.findElement(By.xpath("//*[@id='loginsubmit']")).click();
		
		// creating Portfolio
		Thread.sleep(5000);
        clickAndWait("//*[@id='createPortfolio']", "//*[@id='create']");
	    //driver.findElement(By.xpath("//*[@id='createPortfolio']")).click();
	    
	    driver.findElement(By.xpath("//*[@id='create']")).clear();
		driver.findElement(By.xpath("//*[@id='create']")).sendKeys(portfolioName);
		driver.findElement(By.xpath("//*[@id='createPortfolioButton']")).click();
		
		/*boolean result=isElementPresent("//select[@id='portfolioid']/option[text()='"+portfolioName+"']",5);
		Assert.assertTrue(result,"Portfolio name not present in list");*/
		
		/*WebElement dropDown=driver.findElement(By.xpath("//*[@id='portfolioid']"));
		Select dropList = new Select(dropDown);
		System.out.println(dropList.getFirstSelectedOption().getText());
		Assert.assertEquals(dropList.getFirstSelectedOption().getText(), portfolioName);*/
		
		String stockText = driver.findElement(By.xpath("//*[@id='stock']/tbody/tr/td")).getText().trim();
		Assert.assertEquals(stockText, "");
		
		String mfText = driver.findElement(By.xpath("//*[@id='mf']/tbody/tr/td")).getText().trim();
		Assert.assertEquals(mfText, "");
	}
	
	@Test(priority=2,dependsOnMethods={"createProfileTest"})
	public void renameProfileTest() throws InterruptedException{
		//select the portfolio name which you need to rename
		
		//driver.findElement(By.xpath("//*[@id='portfolioid']")).sendKeys(portfolioName);
		//driver.findElement(By.xpath("//*[@id='portfolioid']")).sendKeys(Keys.ENTER);
		Thread.sleep(5000);
		clickAndWait("//*[@id='renamePortfolio']", "//*[@id='rename']");

		
		//driver.findElement(By.xpath("//*[@id='renamePortfolio']")).click();
		// Renaming
		driver.findElement(By.xpath("//*[@id='rename']")).clear();
		driver.findElement(By.xpath("//*[@id='rename']")).sendKeys(newPortfolioName);
		driver.findElement(By.xpath("//*[@id='renamePortfolioButton']")).click();
		
		Thread.sleep(5000);
		boolean result=isElementPresent("//select[@id='portfolioid']/option[text()='"+newPortfolioName+"']",5);
		Assert.assertTrue(result,"Portfolio name not present in list");
		
		WebElement dropDown=driver.findElement(By.xpath("//*[@id='portfolioid']"));
		Select dropList = new Select(dropDown);
		System.out.println(dropList.getFirstSelectedOption().getText());
		
	}
	
	@Test(priority=3,dependsOnMethods={"createProfileTest","renameProfileTest"})
	public void deleteProfileTest() throws InterruptedException{
		System.out.println("deleteProfileTest***");
		// profile selected
		driver.findElement(By.xpath("//*[@id='portfolioid']")).sendKeys(newPortfolioName);
		driver.findElement(By.xpath("//*[@id='portfolioid']")).sendKeys(Keys.ENTER);
		
		//driver.findElement(By.xpath("//*[@id='deletePortfolio']")).click();
	
		clickAndWaitTillAlert("//*[@id='deletePortfolio']").accept();
			
	//	WebElement dropDown=driver.findElement(By.xpath("//*[@id='portfolioid']"));
		//Select dropList = new Select(dropDown);
		//System.out.println(dropList.getFirstSelectedOption().getText());
		
	}
	
	/**********************************Utility Functions***************************************/
	
	@AfterTest
	public void close() throws InterruptedException{
		Thread.sleep(5000);
		driver.quit();
	}
	
	public boolean isElementPresent(String xpathXpr, int time){
		driver.manage().timeouts().implicitlyWait(time, TimeUnit.SECONDS);
		int s = driver.findElements(By.xpath(xpathXpr)).size();
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		
		if(s>0)
			return true;
		else
			return false;
		
	}
	public boolean isElementPresent(String xpathXpr){
		int s = driver.findElements(By.xpath(xpathXpr)).size();
		
		if(s>0)
			return true;
		else
			return false;
		
	}
	
	
	public Alert clickAndWaitTillAlert(String elementToBeClicked){
		// will click 5 times  - 1 every 3 seconds
		Alert al = null;
		for(int i=0;i<5;i++){
			driver.findElement(By.xpath(elementToBeClicked)).click();
			try{
				al = driver.switchTo().alert();
				return al;
			}catch(Exception e){
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		return al;
	}
	
	public void clickAndWait(String elementToBeClicked, String elementToBeVisible){
		// will click 5 times  - 1 every 3 seconds
		System.out.println("In WaitAndClick*");
		for(int i=0;i<5;i++){
			//System.out.println("Clicking");
			driver.findElement(By.xpath(elementToBeClicked)).click();
			System.out.println(isElementPresent(elementToBeVisible));
			if(isElementPresent(elementToBeVisible) && driver.findElement(By.xpath(elementToBeVisible)).isDisplayed()) {
				return;
			}else{
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
			}
			
		}
	}
}
