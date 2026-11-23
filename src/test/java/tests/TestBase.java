package tests;

import com.microsoft.playwright.*;
import org.testng.annotations.BeforeMethod;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class TestBase {

    Playwright playwright;
    Page page ;
    Browser browser;
    String baseURL;
    Locator eventCards;
    Locator targetCard;
    BrowserContext context;


    @BeforeMethod(alwaysRun = true)
    public void setup() throws IOException {
        playwright = Playwright.create();
        //Browser browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
        FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
        Properties prop= new Properties();
        prop.load(fis);
        String Browser = System.getProperty("browser")!=null ? System.getProperty("browser") : prop.getProperty("browser");
        String envName = System.getProperty("env")!=null ? System.getProperty("env") : prop.getProperty("env");
        //String Browser = prop.getProperty("browser");
        if("safari".equals(Browser)){
            browser = playwright.webkit().launch();
        }
        else if("firefox".equals(Browser)){
            browser = playwright.firefox().launch();
        }
        else{
            browser = playwright.chromium().launch();
        }
        context = browser.newContext();
        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));
        page =  browser.newPage();

        baseURL = prop.getProperty(envName+"_baseURL");
    }
}
