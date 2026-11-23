package tests;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Tracing;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.*;
import utils.DataDriven;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class FrameWorkBuildDataDrivenTest extends TestBase{

    @DataProvider(name = "eventData")
    public Object[][] eventData() throws IOException {

        return DataDriven.jsonData("/src/test/resources/eventDetails.json");
    }

    @Test(dataProvider = "eventData", groups = {"smoke"})
    public  void  LoginTest(HashMap<String, String> data) {
        String eventTitle;
        LoginPage login = new LoginPage(page, baseURL);
        DashBoardPage dashBoardPage = login.LoginToApplications();
        dashBoardPage.waitForEventsToLoad();
        AdminEventsPage event = new AdminEventsPage(page);
        eventTitle = "Conference for Playwright";
        event.goToEvent();
        event.createEvent(data.get("eventTitle"), data.get("description"),
                data.get("category"), data.get("city"), data.get("venue"), data.get("eventDate"),
                data.get("price"), data.get("seats"));
        EventsPage eventsPage = new EventsPage(page);
        eventsPage.goTo();
        Locator targetCard = eventsPage.findEventCard(eventTitle);
        int seats = eventsPage.getSeatsCount(targetCard);
        BookingFormPage bookingFormPage= eventsPage.proceedToBooking(targetCard);
        bookingFormPage.fillAndConfirm(data.get("fullName"), data.get("email"), data.get("phone"));
    }


}
