package com.mrmoin;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.relevantcodes.extentreports.ReporterType;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

/**
 * Created by Khaja on 9/1/16.
 */
public class SeleniumExtentReportsWithScreenshots {


    private static WebDriver driver;
    private static ExtentReports extent;
    private static ExtentTest test;

    @Test
    public void googleHomePageTitle_FAIL(){

        System.setProperty("webdriver.chrome.driver", "/Users/Khaja/Downloads/chromedriver");


        /**
         * Creating ExtentReports Object and working with ExtentTest Object.
         */

        extent = new ExtentReports(System.getProperty("user.dir")+"/SeleniumExtentReportWithScreenshot.html", false);
        extent.startReporter(ReporterType.DB, System.getProperty("user.dir")+File.separator+"extent.db");
        extent.loadConfig(new File("./extent-config.xml"));
        extent.addSystemInfo("Selenium", "2.53.1")
                .addSystemInfo("Environment", "Pre-Prod");
        test = extent.startTest(Thread.currentThread().getStackTrace()[1].getMethodName(), " Running a Selenium Script To Get Title!");
        test.log(LogStatus.INFO, "Launching Chrome Browser");
        driver = new ChromeDriver();
        driver.get("http://google.com");
        test.log(LogStatus.PASS, "Launched Chrome Browser and Navigated to Google.com");
        test.log(LogStatus.INFO, "Getting Google Home Page Title!");
        /*driver.navigate().to("http://google.com");*/
        String title = driver.getTitle();
        test.log(LogStatus.PASS, "Got Google Home Page Title!");

        /**
         * Finding an element not present in Google.Com Page
         */
        try {
            WebElement element = driver.findElement(By.linkText("abababa"));
        } catch (Exception e) {
            //test.log(LogStatus.FAIL, "Web Element you were looking with By.linkText is not present!");
            String fileName = "sample.png";
            takeScreenshotImage(fileName);
            test.log(LogStatus.FAIL, "Attached Screenshot Because element not found: "+test.addScreenCapture(new File("./images"+fileName).getPath()));
        }

        extent.endTest(test);
        extent.flush();
        extent.close();
    }

    public static void takeScreenshotImage(String fileNameWithExtention){
        File actualFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(actualFile, new File(System.getProperty("user.dir") + "/images" + fileNameWithExtention));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}