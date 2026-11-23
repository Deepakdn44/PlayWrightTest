package tests;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Tracing;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.DashBoardPage;
import pages.MyBookingsPage;

import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class MyBookingsCancellationTest extends TestBase {

    @Test
    public void cancelFirstBooking() {
        LoginPage loginPage = new LoginPage(page, baseURL);
        DashBoardPage dashBoardPage = loginPage.LoginToApplications();

        dashBoardPage.waitForEventsToLoad();
        assertThat(page).hasURL("https://eventhub.rahulshettyacademy.com/");

        MyBookingsPage myBookingsPage = new MyBookingsPage(page);
        myBookingsPage.open();

        Locator firstBookingCard = myBookingsPage.getFirstBookingCard();
        String bookingReference = myBookingsPage.getBookingReference(firstBookingCard);

        myBookingsPage.viewBookingDetails(firstBookingCard);
        myBookingsPage.cancelBooking();
        myBookingsPage.verifyBookingDisappeared(bookingReference);
    }

    @AfterMethod
    public void tearDown() {
        context.tracing().stop(new Tracing.StopOptions()
                .setPath(Paths.get("trace.zip")));
        context.close();
        browser.close();
        playwright.close();
    }
}

