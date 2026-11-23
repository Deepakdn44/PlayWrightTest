package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class LoginPage {

    Page page;
    String baseURL;
    private static final String email = "Email";
    private static final String password = "Password";


    public LoginPage(Page page, String baseURL){
        this.page=page;
        this.baseURL=baseURL;

    }

    @Test
    public DashBoardPage LoginToApplications() {
        page.navigate(baseURL);
        System.out.println(page.title());
        assertThat(page).hasTitle("EventHub — Discover & Book Events");
        page.getByLabel(email).fill("deepakdntest2@gmail.com");
        //page.getByPlaceholder("you@email.com").fill("deepakdn444@gmail.com");
        page.getByLabel(password).fill("DeepakTest@123");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();
        DashBoardPage dashBoardPage = new DashBoardPage(page);
        return dashBoardPage;

    }

}
