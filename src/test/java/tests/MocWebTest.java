package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class MocWebTest {

    Playwright playwright;
    Page page;
    Browser browser;
    Locator eventCards;
    Locator targetCard;
    BrowserContext context;


    @BeforeMethod
    public void setup() {
        playwright = Playwright.create();
        //Browser browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        context = browser.newContext();

        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));

        //Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setChannel("chrome").setHeadless(false));
        page = browser.newPage();


        page.navigate("https://eventhub.rahulshettyacademy.com/login");
        PlaywrightAssertions.setDefaultAssertionTimeout(7000);// This code will make Playwright to wait for 7 seconds by default instead of 5 sec
        // In Playwright default wait for action is 10 seconds.
        page.setDefaultTimeout(8000);// This code is to make the playwright wait for 8 seconds for all the actions in the page

        System.out.println(page.title());
    }

    @Test
    public void LoginTest() {
        assertThat(page).hasTitle("EventHub — Discover & Book Events");
        page.getByLabel("Email").fill("deepakdntest2@gmail.com");
        //page.getByPlaceholder("you@email.com").fill("deepakdn444@gmail.com");
        page.getByLabel("Password").fill("DeepakTest@123");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();
        assertThat(page.getByRole(AriaRole.LINK,
                new Page.GetByRoleOptions().setName("Browse Events →"))).isVisible();
    }

    @Test
    public void VerifyEvent() {

        LoginTest();
        page.route("**/api/events**", route -> route.fulfill(
                new Route.FulfillOptions().setPath(Paths.get("src/test/resources/event_2.json"))
        ));
        //Step 2 - Verify Event created
        page.locator("#nav-events").click();
        page.waitForTimeout(5000);
        Locator eventCards = page.locator("#event-card");
        assertThat(eventCards.first()).isVisible();

        Assert.assertEquals(eventCards.count(), 2);
        assertThat(page.locator(".mx-1").first()).isHidden();

        page.route("**/api/events**", route -> route.fulfill(
                new Route.FulfillOptions().setPath(Paths.get("src/test/resources/event_9.json"))
        ));

        page.navigate("https://eventhub.rahulshettyacademy.com/events");
        page.waitForTimeout(5000);
        Locator eventCards1 = page.locator("#event-card");
        assertThat(eventCards1.first()).isVisible();

        Assert.assertEquals(eventCards1.count(), 9);
        assertThat(page.locator(".mx-1").first()).isVisible();

        }

        @Test
        public void resumeTest(){
            LoginTest();
            page.getByTestId("nav-bookings").click();
            page.route("**/api/bookings**", route -> route.resume(
                    new Route.ResumeOptions().setUrl("https://api.eventhub.rahulshettyacademy.com/api/bookings/4146")
            ));
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("View Details")).first().click();
            assertThat(page.getByText("Booking not found")).isVisible();
        }


    }

