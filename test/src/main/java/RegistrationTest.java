import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;


import java.util.concurrent.TimeUnit;

public class RegistrationTest {

    static void correctForm() {

        WebDriver driver;
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();

        try {

            driver.get("https://account.samsung.com/accounts/v1/DCGLPL/signInGate?response_type=code&client_id=01r645nl8p&locale=pl_PL&countryCode=PL&redirect_uri=https:%2F%2Fwww.samsung.com%2Faemapi%2Fdata-login%2FafterLogin.pl.json&state=GLBoyytgn1oidn&goBackURL=https:%2F%2Fwww.samsung.com%2Fpl%2F&scope="); // open start page with login form
            driver.manage().window().maximize();

            driver.findElement(By.xpath("/html/body/div[1]/main/div/div/div[1]/form/fieldset/div[9]/div/a")).click();
            driver.findElement(By.xpath("/html/body/div[1]/main/div/div[2]/div/button")).click();
            TimeUnit.SECONDS.sleep(1);
            driver.findElement(By.id("signUpID")).sendKeys("m.e.psujek@gmail.com");
            driver.findElement(By.id("password")).sendKeys("magdalena1!");
            driver.findElement(By.id("confirmPassword")).sendKeys("magdalena1!");
            driver.findElement(By.id("givenName1")).sendKeys("Magdalena");
            driver.findElement(By.id("familyName1")).sendKeys("Psujek");
            driver.findElement(By.id("day")).sendKeys("22");
            Select selectMonth = new Select(driver.findElement(By.id("month")));
            selectMonth.selectByValue("12");
            driver.findElement(By.id("year")).sendKeys("1996");

            boolean isButtonEnabled = driver.findElement(By.cssSelector("body > div.wrapper.ng-scope > main > div > div.under-content > div > button.one-primary.one-button")).isEnabled();
            if (isButtonEnabled) System.out.print("correct form teset result:    ok\n"); //if enabled -> test passed
            else System.out.println("\ncorrect form teset result: fail"); // if !enabled -> test failed

        } catch (Exception e) {
            e.printStackTrace();
            System.out.print("correct form teset result: fail");
        }
        driver.close();
    }

    static void incorrectForm() {
        WebDriver driver;
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();

        try {

            driver.get("https://account.samsung.com/accounts/v1/DCGLPL/signInGate?response_type=code&client_id=01r645nl8p&locale=pl_PL&countryCode=PL&redirect_uri=https:%2F%2Fwww.samsung.com%2Faemapi%2Fdata-login%2FafterLogin.pl.json&state=GLBoyytgn1oidn&goBackURL=https:%2F%2Fwww.samsung.com%2Fpl%2F&scope="); // open start page with login form

            driver.manage().window().maximize();

            driver.findElement(By.xpath("/html/body/div[1]/main/div/div/div[1]/form/fieldset/div[9]/div/a")).click();  // click create account
            driver.findElement(By.xpath("/html/body/div[1]/main/div/div[2]/div/button")).click();  // click accept
            TimeUnit.SECONDS.sleep(1);

            // email
            checkField(driver, "m.e.psujek", "E-mail\nNieprawidłowy adres e-mail.", "signUpID");
            checkField(driver, "#m.e^&", "E-mail\nW adresie e-mail nie można używać znaków specjalnych innych niż .-_+.", "signUpID");
            checkField(driver, "test@test.pl", "E-mail\nKonto już istnieje.", "signUpID");
            //password
            checkField(driver, "abc", "Hasło\nUżyj co najmniej 8 znaków; w tym liter, cyfr i symboli.", "password");
            checkField(driver, "maaagdalena1!", "Hasło\nHasło nie może zawierać więcej powtórzonych lub kolejnych znaków niż 3.", "password");
            checkField(driver, "magdalena1@xy56z", "Hasło\nNiektóre starsze urządzenia nie obsługują haseł dłuższych niż 15 znaków.", "password");
            //confirm password
            driver.findElement(By.id("password")).clear();
            driver.findElement(By.id("password")).sendKeys("magdalena1!");
            TimeUnit.SECONDS.sleep(1);
            checkField(driver, "magdalena!!", "Potwierdź hasło\nPodane hasła są różne.", "confirmPassword");
            //name
            checkField(driver, "#", "Imię\nNazwy nie mogą zawierać znaków specjalnych.", "givenName1");
            //lastname
            checkField(driver, "#", "Nazwisko\nNazwy nie mogą zawierać znaków specjalnych.", "familyName1");
            //birth date
            checkField(driver, "a", "Dzień\nMiesiąc\nMiesiąc\nStyczeń\nLuty\nMarzec\nKwiecień\nMaj\nCzerwiec\nLipiec\nSierpień\nWrzesień\nPaździernik\nListopad\nGrudzień\nRok\n" +
                    "Można wprowadzać tylko cyfry.", "day");
            driver.findElement(By.id("day")).clear();
            driver.findElement(By.id("day")).sendKeys("32");
            Select selectMonth = new Select(driver.findElement(By.id("month")));
            selectMonth.selectByValue("12");
            checkField(driver, "0", "Dzień\nMiesiąc\nMiesiąc\nStyczeń\nLuty\nMarzec\nKwiecień\nMaj\nCzerwiec\nLipiec\nSierpień\nWrzesień\nPaździernik\nListopad\nGrudzień\nRok\n" +
                    "Wpisz prawidłową datę urodzenia.", "year");

            System.out.print("\nincorrect form test result:    ok");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print("tcorrect form teset result: fail");
        }
        driver.close();
    }

    private static String readMessage(WebDriver driver, String fieldID) {

        String message;
        String path = "";
        if (fieldID.equals("signUpID")) path = "/html/body/div[1]/main/div/div[1]/div[2]/div/form/fieldset/div[1]";
        if (fieldID.equals("password")) path = "/html/body/div[1]/main/div/div[1]/div[2]/div/form/fieldset/div[2]";
        if (fieldID.equals("confirmPassword"))
            path = "/html/body/div[1]/main/div/div[1]/div[2]/div/form/fieldset/div[3]";
        if (fieldID.equals("givenName1")) path = "/html/body/div[1]/main/div/div[1]/div[2]/div/form/fieldset/div[4]";
        if (fieldID.equals("familyName1")) path = "/html/body/div[1]/main/div/div[1]/div[2]/div/form/fieldset/div[5]";
        if (fieldID.equals("day") || fieldID.equals("year"))
            path = "/html/body/div[1]/main/div/div[1]/div[2]/div/form/fieldset/div[6]";

        WebElement element = driver.findElement(By.xpath(path));
        message = element.getAttribute("innerText");

        return message;
    }

    private static void checkField(WebDriver driver, String input, String expectedMessage, String fieldId) throws InterruptedException {

        WebElement element = driver.findElement(By.id(fieldId));
        element.clear();
        element.sendKeys(input);  // fill e-mail
        element.sendKeys(Keys.TAB);  // fill e-mail
        TimeUnit.SECONDS.sleep(2);
        Assert.assertEquals(readMessage(driver, fieldId), expectedMessage, "bad message");

    }
}
