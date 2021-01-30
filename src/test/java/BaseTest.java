import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class BaseTest {

    public WebDriver driver;
    private ChromeOptions options = new ChromeOptions();

    private final static String smallTableHtmlFile = "./test_data/small_table.htm";
    public static String smallTableHtmlFileAbsPath;

    public static final By testTable = By.id("mainTable");

    public BaseTest(){
        try {
            smallTableHtmlFileAbsPath = getFilePath(smallTableHtmlFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String getFilePath(String relativePath) throws FileNotFoundException {
        File f = new File(relativePath);
        if (f.exists()) {
            log.info("File exists: " + f.getAbsolutePath());
            return f.getAbsolutePath();
        } else {
            String msg = "File does not exist: " + relativePath;
            log.error(msg);
            throw new FileNotFoundException(msg);
        }
    }

    @BeforeClass
    public void bc(){
        List<String> arguments = new ArrayList<String>();
        arguments.add("--no-sandbox");
        arguments.add("--start-maximized");
        arguments.add("--disable-infobars");
        // arguments.add("--disable-gpu");
        options.addArguments(arguments);

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        options.merge(capabilities);
    }

    @BeforeTest
    public void bt() {
        // WebDriverManager.getInstance(ChromeDriver.class).setup();
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(options);
    }

    @AfterTest
    public void at() {
        if (driver != null) {
            driver.quit();
        }
    }

}
