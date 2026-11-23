package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class AdminEventsPage {



    Page page;
    private static final String EventTitle = "#event-title-input";
    private static final String EventDescription = "#admin-event-form textarea";
    private static final String Category = "Category";
    private static final String City = "City";
    private static final String Venue = "Venue";
    private static final String EventDateandTime = "Event Date & Time";
    private static final String Price = "Price ($)";
    private static final String TotalSeats = "Total Seats";
    private static final String EventCreated = "Event created!";
    public AdminEventsPage(Page page){
        this.page=page;
    }
    public void goToEvent(){
        page.navigate("https://eventhub.rahulshettyacademy.com/admin/events");
    }
    public void createEvent(String title, String description, String category, String city,
                            String venue, String eventdate, String price, String seats) {

        page.locator(EventTitle).fill(title, new Locator.FillOptions().setTimeout(6000));
        // Above code will make the playwright wait for 6 seconds instead of 8 seconds which is made in line number 28 or 10 seconds which is dfault wait period for action in playwright
        page.locator(EventDescription).fill(description);
        page.getByLabel(Category).selectOption(category);
        page.getByLabel(City).fill(city);
        page.getByLabel(Venue).fill(venue);
        page.getByLabel(EventDateandTime).fill(eventdate);
        page.getByLabel(Price).fill(price);
        page.getByLabel(TotalSeats).fill(seats);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("+ Add Event")).click(new Locator.ClickOptions().setTimeout(12000));
        // this above code is to make the playwright wait for 12 seconds for the button click action
        assertThat(page.getByText(EventCreated)).isVisible();
    }
}
