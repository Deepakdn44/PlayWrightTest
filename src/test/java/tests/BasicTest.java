package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BasicTest {

    Playwright playwright;
    Page page ;
    Browser browser;
    Locator eventCards;
    Locator targetCard;
    BrowserContext context;


    @BeforeMethod
    public void setup(){
        playwright = Playwright.create();
        //Browser browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        context = browser.newContext();

        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));

        //Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setChannel("chrome").setHeadless(false));
        page =  browser.newPage();


        page.navigate("https://eventhub.rahulshettyacademy.com/login");
        PlaywrightAssertions.setDefaultAssertionTimeout(7000);// This code will make Playwright to wait for 7 seconds by default instead of 5 sec
        // In Playwright default wait for action is 10 seconds.
        page.setDefaultTimeout(8000);// This code is to make the playwright wait for 8 seconds for all the actions in the page

        System.out.println(page.title());
    }
    @Test
    public  void  LoginTest() {
        assertThat(page).hasTitle("EventHub — Discover & Book Events");
        page.getByLabel("Email").fill("deepakdntest2@gmail.com");
        //page.getByPlaceholder("you@email.com").fill("deepakdn444@gmail.com");
        page.getByLabel("Password").fill("DeepakTest@123");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();
        assertThat(page.getByRole(AriaRole.LINK,
                new Page.GetByRoleOptions().setName("Browse Events →"))).isVisible();
    }

    @Test
    public  void CreateEvent()

    {
        LoginTest();
        // Step 1 - Create event from Admin page
        page.navigate("https://eventhub.rahulshettyacademy.com/admin/events");
        page.locator("#event-title-input").fill("Conference for Playwright", new Locator.FillOptions().setTimeout(6000));
        // Above code will make the playwright wait for 6 seconds instead of 8 seconds which is made in line number 28 or 10 seconds which is dfault wait period for action in playwright
        page.locator("#admin-event-form textarea").fill("Conference held to discuss about usage of Playwright ");
        page.getByLabel("Category").selectOption("Concert");
        page.getByLabel("City").fill("Banglore");
        page.getByLabel("Venue").fill("Google office, Banglore");
        page.getByLabel("Event Date & Time").fill("2027-10-25T10:30");
        page.getByLabel("Price ($)").fill("500");
        page.getByLabel("Total Seats").fill("450");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("+ Add Event")).click(new Locator.ClickOptions().setTimeout(12000));
        // this above code is to make the playwright wait for 12 seconds for the button click action
        assertThat(page.getByText("Event created!")).isVisible();
        page.waitForTimeout(5000);

    }

    @Test
    public  void  VerifyEvent(){

        LoginTest();
        //Step 2 - Verify Event created
        page.locator("#nav-events").click();
        eventCards = page.getByTestId("event-card");
        System.out.println("Total number of events available------>"+eventCards.count());
        //Visibality of the card which we have added
        targetCard = eventCards.filter(new Locator.FilterOptions().setHasText("Conference for Playwright"));
        //assertThat(targetCard).isVisible();// Playwright will wait for 5 sec for assertions by default

        assertThat(targetCard).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(10000));
        String seatsText = targetCard.getByText("seats").innerText();
        int seatNumBeforeBooking = Integer.parseInt(seatsText.split(" ")[0]);
        System.out.println(seatsText);
        targetCard.getByTestId("book-now-btn").click();
        for(int i=0 ; i<5 ;i++) {
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("+")).click();
        }
        page.getByLabel("Full Name").fill("Deepak Test");
        page.getByPlaceholder("you@email.com").fill("DeepakTest2@gmail.com");
        page.locator("#phone").fill("9876543210");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Confirm Booking")).click(new Locator.ClickOptions().setTimeout(10000));
        assertThat(page.getByText("Your tickets are reserved.")).isVisible();
        String bookingref = page.locator(".booking-ref ").innerText();
        System.out.println(bookingref);
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("View My Bookings")).click();
        // This code is to make the code wait for 10 seconds for above paricular assertion
        Locator bookingCards = page.locator("#booking-card");
        Locator targetBookingCard = bookingCards.filter(new Locator.FilterOptions().setHasText(bookingref));
        assertThat(targetBookingCard).isVisible();

        //seat count reduction check
        page.locator("#nav-events").click();
        page.waitForTimeout(2000);
        Locator eventCardsAfterBooking = page.getByTestId("event-card");
        Locator targetCardAfterBooking = eventCardsAfterBooking.filter(new Locator.FilterOptions().setHasText("Conference for Playwright"));
        String seatsAfterBooking = targetCardAfterBooking.getByText("seats").innerText();
        //443 seats available
        int seatNumAfterBooking = Integer.parseInt(seatsAfterBooking.split(" ")[0]);
        Assert.assertTrue(seatNumBeforeBooking > seatNumAfterBooking);
        System.out.println(seatsAfterBooking);
    }

    @AfterMethod
    public void tearDown(){
        context.tracing().stop(new Tracing.StopOptions()
                .setPath(Paths.get("trace.zip")));
    }

    @Test
    public void BookEvent(){




    }

    @Test
    public void AdvancedTest(){

    }


}
