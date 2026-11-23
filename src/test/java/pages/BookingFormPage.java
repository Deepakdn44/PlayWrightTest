package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.awt.print.Book;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BookingFormPage {

    Page page;
    private static final String Name = "Full Name";
    private static final String email = "you@email.com";
    private static final String phone = "#phone";
    public BookingFormPage(Page page){
        this.page=page;
    }
    public void fillAndConfirm(String FullName, String Email, String Phone){
        page.getByLabel(Name).fill(FullName);
        page.getByPlaceholder(email).fill(Email);
        page.locator(phone).fill(Phone);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Confirm Booking")).click(new Locator.ClickOptions().setTimeout(10000));
        assertThat(page.getByText("Your tickets are reserved.")).isVisible();
    }
}
