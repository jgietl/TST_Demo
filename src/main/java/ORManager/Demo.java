package ORManager;

import BCInterpreter.BCInterpreter;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;


import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.concurrent.TimeUnit;

public class Demo {
    private static final int TIMEOUT_SEC = 30;
    private static WebElement objElement = null;
    private enum Selector {
        BY_CSS,
        BY_LINK_TEXT,
        BY_ID,
        BY_XPATH,
        BY_CLASSNAME,
        BY_PARTIAL_LINK_TEXT,
        BY_TAGNAME
        }
    public static WebElement waitForElement(Selector intSelector, WebDriver driver, String s) {
        Wait wait = new FluentWait<WebDriver>(driver)
                .withTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
                .pollingEvery(1,TimeUnit.SECONDS)
                .ignoring(NoSuchElementException.class);
        switch (intSelector) {
            case BY_CSS:
                objElement = driver.findElement(By.cssSelector(s));
                break;
            case BY_LINK_TEXT:
                objElement = driver.findElement(By.linkText(s));
                break;
            case BY_ID:
                objElement = driver.findElement(By.id(s));
                break;
            case BY_XPATH:
                objElement = driver.findElement(By.xpath(s));
                break;
            case BY_CLASSNAME:
                objElement = driver.findElement(By.className(s));
                break;
            case BY_PARTIAL_LINK_TEXT:
                objElement = driver.findElement(By.partialLinkText(s));
                break;
            case BY_TAGNAME:
                objElement = driver.findElement(By.tagName(s));
                break;
            default:
                throw new RuntimeException("unknown element selector type");
        }
        return objElement;
    }

    private static WebElement getElementByCSSSelector(WebDriver driver, String s) {
        try {
            return driver.findElement(By.cssSelector(s));
        } catch (org.openqa.selenium.NoSuchElementException e) {
            String strReportStatus = "FAILED - GUI Element not found - CSS Selector: " + s;
            System.out.println(strReportStatus);
            BCInterpreter.objReporter.addErr(BCInterpreter.objReporter.getTestcase(), "FAILED", "Element not found at runtime", strReportStatus);
            return null;
        }
    }
    private static WebElement getElementByLinkText(WebDriver driver, String linkText) {
        try {
            return driver.findElement(By.linkText(linkText));
        } catch (org.openqa.selenium.NoSuchElementException e) {
            String strReportStatus = "FAILED - GUI Element not found - Link Text: " + linkText;
            System.out.println(strReportStatus);
            BCInterpreter.objReporter.addErr(BCInterpreter.objReporter.getTestcase(), "FAILED", "Element not found at runtime", strReportStatus);
            return null;
        }
    }
    private static WebElement getElementByXPath(WebDriver driver, String x) {
        try {
            return driver.findElement(By.xpath(x));
        } catch (org.openqa.selenium.NoSuchElementException e) {
            String strReportStatus = "FAILED - GUI Element not found - XPath: " + x;
            System.out.println(strReportStatus);
            BCInterpreter.objReporter.addErr(BCInterpreter.objReporter.getTestcase(), "FAILED", "Element not found at runtime", strReportStatus);
            return null;
        }
    }
    private static WebElement getElementByID(WebDriver driver, String s) {
        try {
            return driver.findElement(By.id(s));
        } catch (org.openqa.selenium.NoSuchElementException e) {
            String strReportStatus = "FAILED - GUI Element not found - ID: " + s;
            System.out.println(strReportStatus);
            BCInterpreter.objReporter.addErr(BCInterpreter.objReporter.getTestcase(), "FAILED", "Element not found at runtime", strReportStatus);
            return null;
        }
    }
    private static WebElement getElementByClassName(WebDriver driver, String s) {
        try {
            return driver.findElement(By.className(s));
        } catch (org.openqa.selenium.NoSuchElementException e) {
            String strReportStatus = "FAILED - GUI Element not found - Class Name: " + s;
            System.out.println(strReportStatus);
            BCInterpreter.objReporter.addErr(BCInterpreter.objReporter.getTestcase(), "FAILED", "Element not found at runtime", strReportStatus);
            return null;
        }
    }
    private static WebElement getElementByName(WebDriver driver, String s) {
        try {
            return driver.findElement(By.name(s));
        } catch (org.openqa.selenium.NoSuchElementException e) {
            String strReportStatus = "FAILED - GUI Element not found - Name: " + s;
            System.out.println(strReportStatus);
            BCInterpreter.objReporter.addErr(BCInterpreter.objReporter.getTestcase(), "FAILED", "Element not found at runtime", strReportStatus);
            return null;
        }
    }

    public static WebElement getNamedElement(String strName) {
        MethodType mt = MethodType.methodType(WebElement.class, WebDriver.class);
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodHandle mh = null;
        WebElement element = null;
        try {
            mh = lookup.findStatic(Demo.class, strName, mt);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        try {
            element = (WebElement) mh.invoke(BCInterpreter.driver);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return element;
    }
    public static boolean checkElementExist (WebElement objElement, int intTimeOut) {
        Wait<WebDriver> wait = new FluentWait<WebDriver>(BCInterpreter.driver)
                .withTimeout(1, TimeUnit.SECONDS)
                .pollingEvery(1, TimeUnit.SECONDS)
                .ignoring(NoSuchElementException.class);
//          WebElement foo = wait.until(new CheckForElement());
        return true;
    }

    public static WebElement editGoogleSearch (WebDriver driver) {
        return getElementByName(driver, "q");
    }
    public static WebElement btnGoogleSearch (WebDriver driver) {
        return getElementByName(driver, "btnG");
    }
    public static WebElement tblw3schools (WebDriver driver) {
        return getElementByCSSSelector(driver, "table.reference");
    }

//    WebElement loginForm = findFirstDisplayable(this.driver, By.id("loginAddToFavouritesOverlayTab"));
//    WebElement loginFailed = SeleniumUtil.findFirstDisplayable(this.driver, By.className("berner-validation-badCredentials"));
    /**
     * Find an the first displayable element.
     * <p/>
     * Helpful for pages which contains different layouts for the same content.
     *
     * @param //searchContext
     * @param by
     * @return
     */
    public static WebElement findFirstDisplayable(WebDriver driver, final By by) {
        WebElement identifiedElement = null;
        for (WebElement element : driver.findElements(by)) {
            if (element.isDisplayed()) {
                identifiedElement = element;
            }
        }
        if (identifiedElement == null) {
            throw new NoSuchElementException("no displayed element found for " + by);
        }
        return identifiedElement;
    }
}

//class CheckForElement implements Function<WebDriver, WebElement> {
//    @Override
//    public WebElement apply(WebDriver driver) {
//        return BCInterpreter.driver.findElement(By.cssSelector("div.blockui-overlay.js.blockui-overlay"));
//    }
//}
