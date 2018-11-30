package com.amazonproject.testpackage;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;

import java.net.URL;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


public class Tests {
	AndroidDriver driver;
	WebDriverWait wait;
	TouchAction action;
	// Start appium server
	@BeforeClass
	public void launch() throws Exception {
		Runtime.getRuntime().exec("cmd.exe /c start cmd.exe /k \" appium -a 0.0.0.0 -p 4723\"");
		URL u = new URL("http://0.0.0.0:4723/wd/hub");
		Thread.sleep(5000);
		
		// Provide device details
		DesiredCapabilities dc = new DesiredCapabilities();
		dc.setCapability(CapabilityType.BROWSER_NAME, "");
		dc.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, "60");
		dc.setCapability(MobileCapabilityType.DEVICE_NAME, "6bfbeeb9");
		dc.setCapability(MobileCapabilityType.PLATFORM_NAME, "android");
		dc.setCapability(MobileCapabilityType.PLATFORM_VERSION, "9");
		dc.setCapability("appPackage", "com.amazon.mShop.android.shopping");
		dc.setCapability("appActivity", "com.amazon.mShop.splashscreen.StartupActivity");
		
		// Launch App
		while (1>0) {
			try { 
				driver = new AndroidDriver(u,dc);
				break;
			} catch (Exception e) {
			}
		}
	}
	// Proceed to sign in (skipped signin for test basis)
	@Test(priority=1) 
	public void signin() throws Exception {
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@text='Skip sign in']")).click();
	}
	// Tapping Main menu
	@Test(priority=2) 
	public void tapmainmenu() throws Exception {
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@resource-id='com.amazon.mShop.android.shopping:id/action_bar_burger_icon']")).click();
	}
	// Tapping Shop by Department and Electronics within it
	@Test(priority=3) 
	public void tapdepartment() throws Exception {
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@text='Shop by Department']")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@text='Electronics']")).click();
	}
	@Test(priority=4) 
	public void tapKindle() throws Exception {
		wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@text='Electronics and Accessories']")));
		action = new TouchAction(driver);
		int w = driver.manage().window().getSize().getWidth();
		int h = driver.manage().window().getSize().getHeight();
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		// Swipe from bottom to top
		int x = w/2; 
		int y = (int) (h*0.9);
		int temp = (int) (h*0.7);
		while (2>1){
			try {
				driver.findElement(By.xpath("//android.view.View[@text='Kindle']")).click();
				break;
			} catch(Exception ex){
				action.press(x, y).waitAction(Duration.ofMillis(2000)).moveTo(x, temp).release().perform();
			}
		}
		Thread.sleep(5000);
	}
	// Tapping on Kindle icon
	@Test(dependsOnMethods={"tapKindle"}) 
	public void validateavailability() throws Exception {
		wait = new WebDriverWait(driver, 10);
		Thread.sleep(3000);
		action = new TouchAction(driver);
		int w = driver.manage().window().getSize().getWidth();
		int h = driver.manage().window().getSize().getHeight();

		// Swipe from bottom to top
		WebElement e1ement;
		int x = w/2; 
		int y = (int) (h*0.9);
		int temp = (int) (h*0.7);
		while (2>1){
			try {
				e1ement = driver.findElement(By.xpath("//*[@text='Qty:']"));
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@text='Qty:']")));
				Thread.sleep(5000);
				if (e1ement.isDisplayed()) {
					Assert.assertTrue(true,"Title test has passed");
				} else {
					Assert.assertTrue(false,"Title test has failed");
				}
				break;
			} catch(Exception ex){
				action.press(x, y).waitAction(Duration.ofMillis(2000)).moveTo(x, temp).release().perform();
			}
		}
			
	}
	// Close app and appium server
	@AfterClass
	public void close() throws Exception {
		driver.closeApp();
		Runtime.getRuntime().exec("taskkill /F /IM node.exe");
		Runtime.getRuntime().exec("taskkill /F /IM cmd.exe");
	}
	
}
