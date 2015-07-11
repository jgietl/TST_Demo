package CCManager;

import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

import java.util.List;

public class WebTable {
    public static WebElement objSE;
    public WebElement getObjSE() {
        return objSE;
    }
    public void setObjSE(WebElement objSE) {
        this.objSE = objSE;
    }

    public static Object base;
    private String DefaultPorperty = "column names";
    public static String Base = "CC_SEBase";

    public Object WebTable(){
        objSE = null;
        base = new WebBase();
        return objSE;
    }


    public int getRowCount(){
        List<WebElement> tableRows = objSE.findElements(By.tagName("tr"));
        return tableRows.size();
    }
    public int getColumnCount(){
        List<WebElement> tableRows = objSE.findElements(By.tagName("tr"));
        WebElement headerRow = tableRows.get(0);
        List<WebElement> tableColumns = headerRow.findElements(By.tagName("td"));
        return tableColumns.size();
    }
    public WebElement getRowElement(int rowIdx){
        List<WebElement> tableRows = objSE.findElements(By.tagName("tr"));
        return tableRows.get(rowIdx);
    }
    public WebElement getCellElement(int rowIdx, int colIdx){
        List<WebElement> tableRows = objSE.findElements(By.tagName("tr"));
        WebElement dataRow = tableRows.get(rowIdx);
        List<WebElement> tableColumns = dataRow.findElements(By.tagName("td"));
        return tableColumns.get(colIdx);
    }
    public WebElement getActionLink(int rowIdx, int linkIdx){
        List<WebElement> tableRows = objSE.findElements(By.tagName("tr"));
        WebElement dataRow = tableRows.get(rowIdx);
        List<WebElement> tableColumns = dataRow.findElements(By.tagName("td"));
        WebElement actionCell = tableColumns.get(getColumnCount()-1);
        List<WebElement> actionlinks = actionCell.findElements(By.tagName("a"));
        return actionlinks.get(linkIdx-1);
    }
    public int getColNumber (String strColumnName){
        List<WebElement> listElementFirstRow = objSE.findElements(By.xpath("//tbody/tr[1]/th"));
        int i = 1;
        for(WebElement objElement : listElementFirstRow){
            String tmp = objElement.getText();
            if (!tmp.equals(strColumnName)){
                i++;
            }
            else{
                break;
            }
        }
        return i;
    }
    public void printElement(String strText){
        System.out.println(strText);
    }
}
