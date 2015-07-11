package CCManager;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

import java.util.List;

//    '***************************************************************************
//    '***********************    WebArea   ************************************
//    '***************************************************************************
public class WebArea {
    private String strProperty;
    private String strDefaultProperty = "area";
    public static WebElement objSE;
    public WebElement getObjSE() {
        return objSE;
    }
    public void setObjSE(WebElement objSE) {
        this.objSE = objSE;
    }

    public String getDefaultProperty() {
        return objSE.getAttribute(strDefaultProperty);
    }
    public void setDefaultProperty(String strValue) {
        System.out.println("CC_WebBase: SetDefaultProperty(" + strValue + ") called. TFW will do nothing.");
    }
    public String getROProperty(String strProperty){
        return objSE.getAttribute(strProperty);
    }
    public String getProperty() {
        return strProperty;
    }
    public void setProperty(String property) {
        strProperty = property;
    }
    public String getControlTyp() {
        return "Area";
    }
    public String getBaseMicClass() {
        return "WebArea";
    }
    public String getDisabled() {
        return objSE.getAttribute("disabled");
    }
    public void click() {
        objSE.click();
    }

    public Object WebArea() {
        //  class Base = new WebBase();
        objSE = null;
        return objSE;
    }


}

