package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

public class MoreUIValidations {

    Playwright playwright;
    Browser browser;
    Page pageA, pageB;
    BrowserContext contextA, contextB;

    @BeforeMethod
    public void setup(){
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        contextA =  browser.newContext();

        contextA.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));


        pageA = contextA.newPage();
        pageA.navigate("https://rahulshettyacademy.com/loginpagePractise/");
    }

    @Test
    public void ChildWindowHandle(){


        Locator blinkingText = pageA.locator(".blinkingText");
        Page newPage = contextA.waitForPage(()->blinkingText.first().click());
        newPage.waitForLoadState();
        //String emailID = newPage.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("mentor@rahulshettyacademy.com")).innerText();
        // Another way to get the email from above element
        String childText = newPage.locator(".red").textContent();
        // this above code will get all the text content available in the element
        String emailID = childText.split("at ")[1].split(" ")[0];
        pageA.getByLabel("Username").fill(emailID);
        System.out.println(pageA.getByLabel("Username").inputValue());
        // inputValue method is to get the input value entered during the test which will not be available in DOM inside the textbox/Form
        pageA.waitForTimeout(5000);
    }

    @Test
    public void UIControls(){
        Locator usrradioButton = pageA.getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName("User"));
        usrradioButton.click();
        pageA.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Okay")).click();
        Assert.assertTrue(usrradioButton.isChecked());
        pageA.getByRole(AriaRole.COMBOBOX).selectOption("Teacher");
        Locator checkBox =  pageA.getByRole(AriaRole.CHECKBOX, new Page.GetByRoleOptions().setName("I Agree to the terms and conditions"));
        checkBox.check();
        Assert.assertTrue(checkBox.isChecked());
    }

    @AfterMethod
    public void tearDown(){
        contextA.tracing().stop(new Tracing.StopOptions()
                .setPath(Paths.get("trace.zip")));
    }
}
