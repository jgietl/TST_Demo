package BCInterpreter;

import ORManager.Demo;
import SE.WebDriverFactory;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BCs {
    public static String PATH_TESTAUTOMATION = "C:\\\\";
    public static String PATH_NETEXPORT = PATH_TESTAUTOMATION + "NetExport";
    public static String PATH_SELENIUM = PATH_TESTAUTOMATION + "Software\\";
    public static String textNetExport;
    private boolean acceptNextAlert = true;
// Common
    public static void wait(int milliseconds){
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void wait(Map objDict) {
        Integer intSeconds;
        try {
            intSeconds = Integer.parseInt(objDict.get("Sec").toString());
        } catch (Exception e) {
            intSeconds = 1;
        }
        wait(intSeconds * 1000);
    }
    public static void open(Map objDict){
        String strBrowser = "IE";
        if (objDict.containsKey("Browser")){
            strBrowser = objDict.get("Browser").toString();
        }
        String strURL = "";
        if (objDict.containsKey("URL")){
            strURL = objDict.get("URL").toString();
        }
        WebDriverFactory driverFactory = new WebDriverFactory();
        BCInterpreter.driver = driverFactory.create(strBrowser);
        BCInterpreter.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        BCInterpreter.driver.get(strURL);
    }
    public static void close(Map objDict) throws Exception {
        if (BCInterpreter.driver.toString().contains("FirefoxDriver")) {
            Runtime.getRuntime().exec("taskkill /F /IM firefox.exe");
            Thread.sleep(5000);
            Runtime.getRuntime().exec("taskkill /F /IM plugin-container.exe");
            Runtime.getRuntime().exec("taskkill /F /IM WerFault.exe");
        } else {
            BCInterpreter.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            BCInterpreter.driver.quit();
            BCInterpreter.driver = null;
        }
    }
    public static void google_search (Map objDict){
        BCInterpreter.GetVetSet("editGoogleSearch","Search", objDict, "WebEdit");
        BCInterpreter.GetVetSet("btnGoogleSearch","Start", objDict, "WebButton");
    }
    public static void w3schools_html_tables (Map objDict){
        BCInterpreter.GetVetSet("tblw3schools","Value", objDict, "WebTable.WebTableCell");
    }
    private void waitwhileloading(){
        int intTimeout = 300;
        for (int i = 0; i < intTimeout; i++){

        }
    }
}
