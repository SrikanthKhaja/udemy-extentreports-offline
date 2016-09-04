package com.mrmoin;

import com.relevantcodes.extentreports.DisplayOrder;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by Khaja on 9/1/16.
 */
public class ExtentReportClass{

    private static ExtentReports extent;
    private static ExtentTest test;
    private static final String filePath = System.getProperty("user.dir")+"/test-emailable-image.html";

    public static void main(String[] args) throws IOException{
        extent = new ExtentReports(filePath, true, DisplayOrder.NEWEST_FIRST);
        extent.addSystemInfo("Environment", "UAT")
                .addSystemInfo("User Name", "Khaja")
                .addSystemInfo("Language", "Java")
                .addSystemInfo("Selenium", "2.53.1");
        extent.loadConfig(new File("./extent-config.xml"));
        test = extent.startTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "This is the First Test Case Using ExtentReports!");
        test.assignAuthor("Khaja", "Srikanth");

        //test.log(LogStatus.ERROR, test.addScreenCapture(new File("./images/printer-image.JPG").getPath()));
        //test.log(LogStatus.FAIL, "Some Description!");
        test.log(LogStatus.PASS, test.addBase64ScreenShot(convertToBString(new File(System.getProperty("user.dir")+"/images/printer-image.JPG"))));

        extent.endTest(test);
        extent.flush();
        extent.close();
    }

    public static String convertToBString(File file) throws IOException{
        String fileType = FilenameUtils.getExtension(file.getAbsolutePath());
        BufferedImage bufferedImage = ImageIO.read(file);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        String base64HTMLStart = "data:image/"+fileType+";base64,";
        ImageIO.write(bufferedImage, fileType, byteArrayOutputStream);
        byte[] imagesBytes = byteArrayOutputStream.toByteArray();
        BASE64Encoder encoder = new BASE64Encoder();
        byteArrayOutputStream.close();

        return (base64HTMLStart+encoder.encode(imagesBytes));
    }
}
