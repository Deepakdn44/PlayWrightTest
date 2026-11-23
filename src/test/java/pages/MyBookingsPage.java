package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class MyBookingsPage {

    private final Page page;

    public MyBookingsPage(Page page) {
        this.page = page;
    }

    public void open() {
        page.getByRole(AriaRole.LINK,
                new Page.GetByRoleOptions().setName("My Bookings").setExact(true)).first().click();
        page.waitForURL("**/bookings");
        assertThat(page.getByRole(AriaRole.HEADING,
                new Page.GetByRoleOptions().setName("My Bookings"))).isVisible();
    }

    public Locator getFirstBookingCard() {
        Locator firstCard = page.locator("#booking-card").first();
        assertThat(firstCard).isVisible();
        return firstCard;
    }

    public String getBookingReference(Locator bookingCard) {
        return bookingCard.locator(".booking-ref").innerText();
    }

    public void viewBookingDetails(Locator bookingCard) {
        bookingCard.getByRole(AriaRole.LINK,
                new Locator.GetByRoleOptions().setName("View Details").setExact(true)).click();
        assertThat(page).hasURL(java.util.regex.Pattern.compile(".*/bookings/.*"));
        assertThat(page.getByText("Event Details")).isVisible();
    }

    public void cancelBooking() {
        page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Cancel Booking").setExact(true)).first().click();
        assertThat(page.getByText("Cancel this booking?")).isVisible();
        page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Yes, cancel it").setExact(true)).click();
        assertThat(page.getByText("Booking cancelled successfully")).isVisible();
        page.waitForURL("**/bookings");
    }

    public void verifyBookingDisappeared(String bookingReference) {
        Locator canceledCard = page.locator("#booking-card")
                .filter(new Locator.FilterOptions().setHasText(bookingReference));
        assertThat(canceledCard).hasCount(0);
    }
}
