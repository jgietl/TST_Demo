package CCManager;

import ORManager.Demo;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.regex.Pattern;

public class WebTableCell {
    public static WebElement objSE;

    public WebElement getObjSE() {
        return objSE;
    }

    public void setObjSE(WebElement objSE) {
        this.objSE = objSE;
    }

    public static Object base;
    private int m_row;
    private int m_col;

    public int getRow() {
        return m_row;
    }

    public void setRow(int m_row) {
        this.m_row = m_row;
    }

    public int getCol() {
        return m_col;
    }

    public void setCol(int m_col) {
        this.m_col = m_col;
    }

    private String DefaultPorperty = "innertext";

    public Object WebTableCell() {
        objSE = null;
        m_row = 1;
        m_col = 1;
        base = new WebBase();
        return objSE;
    }

    public String getDefaultProperty(String s) {
        return getCellData(m_row, m_col).toString();
    }

    public String getROProperty(String strProperty) {
        return getDefaultProperty(strProperty);
    }

    public void setDefaultProperty(String strValue) {
        objSE.click();
    }

    public Boolean checkProperty(String strProperty, Pattern objRegEx) {
        Boolean rc = false;
//    m_Obj("micclass").Value = "WebElement"
//    m_Obj("html tag").Value = "TD"
//    m_Obj("innertext").Value = getDefaultProperty()
        if (!Demo.checkElementExist(objSE, 1)) {
//          m_Obj("html tag").Value = "TH"
//          rc = objSE.checkProperty(strProperty, objRegEx)
        }
        return rc;
    }

    public Boolean waitProperty(String strProperty, Pattern objRegEx) {
        Boolean rc = false;
//    m_Obj("micclass").Value = "WebElement"
//    m_Obj("html tag").Value = "TD"
//    m_Obj("innertext").Value = getDefaultProperty()
        if (!Demo.checkElementExist(objSE, 1)) {
//          m_Obj("html tag").Value = "TH"
//          rc = objSE.waitProperty(strProperty, objRegEx)
        }
        return rc;
    }
    public WebElement getCellData(int rowIdx, int colIdx) {
        List<WebElement> tableRows = objSE.findElements(By.tagName("tr"));
        WebElement dataRow = tableRows.get(rowIdx);
        List<WebElement> tableColumns = dataRow.findElements(By.tagName("td"));
        return tableColumns.get(colIdx);
    }
}

