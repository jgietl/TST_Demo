package CCManager;

import BCInterpreter.BCInterpreter;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import static org.junit.Assert.assertTrue;

import java.util.List;

public class WebBase implements WebElement {
    public static WebElement objSE;
    public static WebElement getObjSE() {
        return objSE;
    }
    public static void setObjSE(WebElement oSE) {
        WebBase.objSE = oSE;
    }
    private String strProperty;
    private String DefaultProperty = "text";
    public Object WebBase(WebElement oSE) {
        //  Public Base
        //  Public Reflection
        objSE = oSE;
        String DefaultProperty = "text";
        return objSE;
    }
    public String getProperty() {
        return strProperty;
    }
    public void setProperty(String property) {
        strProperty = property;
    }
    public void setDefaultProperty(String strValue) {
        objSE.clear();
        objSE.sendKeys(strValue);
    }
    public String exist(String s) {
        if (objSE != null) { return "True"; } else { return "False"; }
    }
    public String notexist(String s) {
        if (objSE != null) { return "False"; } else { return "True"; }
    }
    public void clear(String s) {
        objSE.clear();
    }
    public String getDefaultProperty(String s) {
        return objSE.getAttribute(DefaultProperty);
    }
    public String getROProperty(String strProperty) {
        return objSE.getAttribute(strProperty);
    }
    public void click(String s) {
        objSE.click();
    }
    public void hover(String s) {
        assertTrue(objSE.isDisplayed());
        Actions objAction = new Actions(BCInterpreter.driver);
        objAction.moveToElement(objSE).build().perform();
    }
    public String disabled(String s) {
        return objSE.getAttribute("disabled");
    }
    public void clickandwait(String s) {
        assertTrue(objSE.isDisplayed());
        objSE.click();
    }
    public String getText(String s) {
        return objSE.getText();
    }

    public void DCVerifyCheckElementPresent() {
        if (BCInterpreter.driver.findElements(By.xpath("value")).size() != 0) {
            System.out.println("Element is Present");
        } else {
            System.out.println("Element is Absent");
        }
        if (BCInterpreter.driver.findElement(By.xpath("value")) != null) {
            System.out.println("Element is Present");
        } else {
            System.out.println("Element is Absent");
        }
    }
    public void DCVerifyCheckElementVisible() {
        if (BCInterpreter.driver.findElement(By.cssSelector("a > font")).isDisplayed()) {
            System.out.println("Element is Visible");
        } else {
            System.out.println("Element is InVisible");
        }
    }
    public void DCVerifyCheckElementEnabled() {
        if (BCInterpreter.driver.findElement(By.cssSelector("a > font")).isEnabled()) {
            System.out.println("Element is Enabled");
        } else {
            System.out.println("Element is Disabled");
        }
    }
    public void DCVerifyCheck() {
        if (BCInterpreter.driver.getPageSource().contains("Text to check")) {
            System.out.println("Text is present");
        } else {
            System.out.println("Text is absent");
        }
    }
    @Override
    public void click() {
        objSE.click();
    }
    @Override
    public void submit() {
        objSE.submit();
    }
    @Override
    public void sendKeys(CharSequence... charSequences) {
        objSE.sendKeys(charSequences);
    }
    @Override
    public void clear() {
        objSE.clear();
    }
    @Override
    public String getTagName() {
        return objSE.getTagName();
    }
    @Override
    public String getAttribute(String strValue) {
        return objSE.getAttribute(strValue);
    }
    @Override
    public boolean isSelected() {
        return objSE.isSelected();
    }
    @Override
    public boolean isEnabled() {
        return objSE.isEnabled();
    }
    @Override
    public String getText() {
        return objSE.getText();
    }
    @Override
    public List<WebElement> findElements(By by) {
        return objSE.findElements(by);
    }
    @Override
    public WebElement findElement(By by) {
        return objSE.findElement(by);
    }
    @Override
    public boolean isDisplayed() {
        return objSE.isDisplayed();
    }
    @Override
    public Point getLocation() {
        return null;
    }
    @Override
    public Dimension getSize() {
        return null;
    }
    @Override
    public String getCssValue(String s) {
        return null;
    }


}