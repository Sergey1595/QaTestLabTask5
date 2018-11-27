package myprojects.automation.assignment5.utils;

import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class DriverFactory {
    /**
     *
     * @param browser Driver type to use in tests.
     * @return New instance of {@link WebDriver} object.
     */
    public static WebDriver initDriver(String browser) {
        switch (browser) {
            case "firefox":
                System.setProperty(
                        "webdriver.gecko.driver",
                        new File(DriverFactory.class.getResource("/geckodriver.exe").getFile()).getPath());
                return new FirefoxDriver();
            case "ie":
            case "internet explorer":
                System.setProperty(
                        "webdriver.ie.driver",
                        new File(DriverFactory.class.getResource("/IEDriverServer.exe").getFile()).getPath());
                InternetExplorerOptions ieOptions = new InternetExplorerOptions()
                        .requireWindowFocus()
                        .setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.ACCEPT)
                        .enablePersistentHovering()
                        .destructivelyEnsureCleanSession();
                ieOptions.setCapability(InternetExplorerDriver.NATIVE_EVENTS, false);
                ieOptions.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
                return new InternetExplorerDriver(ieOptions);
            case "chrome":
            default:
                System.setProperty(
                        "webdriver.chrome.driver",
                        new File(DriverFactory.class.getResource("/chromedriver.exe").getFile()).getPath());
                return new ChromeDriver();
        }
    }

    /**
     *
     * @param browser Remote driver type to use in tests.
     * @param gridUrl URL to Grid.
     * @return New instance of {@link RemoteWebDriver} object.
     */
    public static WebDriver initDriver(String browser, String gridUrl) {
        switch (browser) {
            case "firefox":
                System.setProperty(
                        "webdriver.gecko.driver",
                        new File(DriverFactory.class.getResource("/geckodriver.exe").getFile()).getPath());
                FirefoxOptions optionsFirefox = new FirefoxOptions();
                try{
                    return new RemoteWebDriver(new URL(gridUrl), optionsFirefox);
                }catch (MalformedURLException ex){
                    ex.printStackTrace();
                }
                return null;

            case "ie":
            case "internet explorer":
                System.setProperty(
                        "webdriver.ie.driver",
                        new File(DriverFactory.class.getResource("/IEDriverServer.exe").getFile()).getPath());
                InternetExplorerOptions ieOptions = new InternetExplorerOptions().
                        requireWindowFocus().
                        setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.ACCEPT).
                        enablePersistentHovering().
                        destructivelyEnsureCleanSession();
                ieOptions.setCapability(InternetExplorerDriver.NATIVE_EVENTS, false);
                ieOptions.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
                try{
                    return new RemoteWebDriver(new URL(gridUrl), ieOptions);
                }catch (MalformedURLException ex){
                    ex.printStackTrace();
                }
                return null;
            case "android":
                System.setProperty(
                        "webdriver.chrome.driver",
                        new File(DriverFactory.class.getResource("/chromedriver.exe").getFile()).getPath());
                Map<String, String> mobileEmulation = new HashMap<>();
                mobileEmulation.put("deviceName", "Nexus 5");
                ChromeOptions optionsChromeNexus5 = new ChromeOptions();
                optionsChromeNexus5.setExperimentalOption("mobileEmulation", mobileEmulation);
                try{
                    return new RemoteWebDriver(new URL(gridUrl), optionsChromeNexus5);
                }catch (MalformedURLException ex){
                    ex.printStackTrace();
                }
                return null;
            case "chrome":
            default:
                System.setProperty(
                        "webdriver.chrome.driver",
                        new File(DriverFactory.class.getResource("/chromedriver.exe").getFile()).getPath());
                ChromeOptions optionsChrome = new ChromeOptions();
                try{
                    return new RemoteWebDriver(new URL(gridUrl), optionsChrome);
                }catch (MalformedURLException ex){
                    ex.printStackTrace();
                }
                return null;
        }
    }
}
