import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

import java.util.concurrent.TimeUnit;

public class CartTest {


    static void addToCart(){

        WebDriver driver;
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();

        try {

            driver.get("https://www.samsung.com/pl/");

            driver.manage().window().maximize();

            driver.findElement(By.xpath("//*[@id=\"wrap\"]/div[3]/nav/div/div/div[2]/div/div[1]/ul/li[1]/a")).click();
            driver.findElement(By.xpath("//*[@id=\"content\"]/div[2]/div[2]/section/div/div/div[2]/div/div/a")).click();
            TimeUnit.SECONDS.sleep(5);
            driver.findElement(By.id("truste-consent-button")).click();  // close cookie pop up
            driver.findElement(By.xpath("/html/body/div[1]/div[3]/div[4]/div[2]/div[2]/div/div/div[4]/div/div[1]/div/div[2]")).click();

            TimeUnit.SECONDS.sleep(5);
            WebElement element = driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/main/div/div/div/div[1]/div[2]/div[1]/div[1]"));

            String message = element.getAttribute("innerText");
            Assert.assertEquals(message, "KOSZYK (1 ELEMENT)", "empty cart");
            System.out.print("\nadd to cart teset result:   ok\n");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.print("add to cart teset result: fail\n");
        }
        driver.close();
    }
}
