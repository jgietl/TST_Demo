package CCManager;
import ORManager.Demo;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

// ==================================================================================================
// ==================================================================================================
// ===================================      Immediate Data    =======================================
// ==================================================================================================
// ==================================================================================================

public class CCManager {
    // Get a custom control object for the method.
    // Return the object or null if no object containing the method has been found.
    public static Object CCReflection(Object objCC, String strFunction) {
        Method[] methods;
        methods = objCC.getClass().getMethods();
        Object oCC = null;
        if (methods != null) {
            for (Method method : methods) {
                if (method.equals(strFunction)) {
                    oCC = objCC;
                }
            }
        }
        if (oCC == null) {
            objCC = CCWebBinding("WebBase");
            methods = objCC.getClass().getMethods();
            if (methods != null) {
                for (Method method : methods) {
                    if (method.equals(strFunction)) {
                       oCC = objCC;
                    }
                }
            }
        }
        return oCC;
    }

    public static Object CCClass(String strSE, String strCustomControlName) {
        WebElement element = Demo.getNamedElement(strSE);
        Object objCCC = CCBinding(strCustomControlName);
        Method method = null;
        try {
            method = objCCC.getClass().getMethod("setObjSE", WebElement.class);
            try {
                Object rc = method.invoke(objCCC, element);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }         } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return objCCC;
    }

    public static Object CCC_TableChild(WebElement oSE, String strMultiClass, String intRow, String intCol, int intIndex) {
        WebElement objCC_Table, objChildCC = null;
        int intError;
        String arrClass[];
        arrClass = strMultiClass.split(".");
        if (arrClass.length == 1) {
            //Err.Raise 0, "The Custom Control Type: <" & strMultiClass & "> has to be hierarchical and split by a '.' e.g. WebTable.WebElement"
        }
//        objCC_Table = CCClass(oSE, arrClass[0]);
//        objChildCC = CCClass(oSE, arrClass[1]);
//        if (objCC_Table.ControlTyp != "Table") {
//            Err.Raise 0, "The Custom Control Type: <" & arrClass(0) & "> is not a table and can't be used in this function as a parent control"
//        }
//        if (ADM_CCReflection(objChildCC, "ControlTyp").ControlTyp == "TableCell") {
//            objChildCC.Row = intRow
//            objChildCC.Col = intCol
//        } else {
//            objCC_Table.ChildItem(intRow, intCol, ADM_CCReflection(objChildCC, "BaseMicClass").BaseMicClass, intIndex).ToString;
//            intError = Err.Number;
//            if (intError == 0) {
//                WebElement objChildSE = objCC_Table.ChildItem(intRow, intCol, ADM_CCReflection(objChildCC, "BaseMicClass").BaseMicClass, intIndex);
//                if (objChildSE == null) {
//                    //Err.Raise 0, "ToString Err.Number: " & intError & ". The Custom Control Type: <" & strMultiClass & "> does not exist in row: " & intRow & " column: " & intCol & "with index: " & intIndex
//                    return null;
//                }
//                objChildCC.objSE = objChildSE;
//            } else {
//                objChildCC = null;
//            }
//        }
        return objChildCC;
    }

    public static Object CCBinding(String strControlName) {
        Object objCC = null;
        String strClassName = CCManager.class.getPackage().getName() + "." + strControlName;
        Class ccClass = null;
        if (strControlName.equals("WebElement")) {
            strControlName = "WebBase";
        } else {
            try {
                ccClass = Class.forName(strClassName);
            } catch (ClassNotFoundException e) {
                strControlName = "WebBase";
            }
        }
        objCC = CCWebBinding(strControlName);
        if (objCC == null) {
            System.out.println("FAILED TFW Error CC object:" + strControlName + " has not been created - Library might not be bound");
        }
        return objCC;
    }

    private static Object CCWebBinding(String strCustomControlName) {
        Object oCC = null;
        switch (strCustomControlName.toLowerCase()) {
            case "webbase": return new WebBase();
            case "webelement": return new WebBase();
            case "webarea": return new WebArea();
/*
            case "webbutton": return new WebButton();
            case "webedit": return new WebEdit();
            case "webfile": return new WebFile();
            case "webimage": return new WebImage();
            case "weblink": return new WebLink();
            case "weblist": return new WebList();
            case "webradiogroup": return new WebRadioGroup();
            case "webradiobutton": return new WebRadioButton();
             */
            case "webtable": return new WebTable();
            case "webtablecell": return new WebTableCell();
            default:
                throw new RuntimeException("unknown gui custom control type: " + strCustomControlName);
        }
    }

    public static String CCInvoke (Object cclass, String strMethod, String strParameter) {
        String strRC = "";
        try {
            Method method = cclass.getClass().getMethod(strMethod, String.class);
            try {
                strRC = (String) method.invoke(cclass, strParameter);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return strRC;
    }

    private static Boolean CCMethodExist (Class cclass, String strMethod) {
        Boolean blnMethodExist = false;
        Method[] methods = cclass.getMethods();
        if (methods != null) {
            for (Method method : methods) {
                if (method.getName().equals(strMethod)){
                    blnMethodExist = true;
                };
            }
        }
        return blnMethodExist;
    }

    public static void main(String[] args){
        WebDriver driver=new FirefoxDriver();
        driver.manage().window().maximize();
//        driver.get("http://money.rediff.com/");
        driver.get("http://www.w3schools.com/html/html_tables.asp");
        WebElement element=driver.findElement(By.xpath("//table"));
        System.out.println(element.toString());
        List<WebElement> headerCollection = element.findElements(By.xpath("//th"));
        int i_HeadColNum=1;
        for(WebElement headerElement:headerCollection)
        {
            System.out.println("HeaderCol "+i_HeadColNum+" Data "+headerElement.getText());
            i_HeadColNum=i_HeadColNum+1;
        }
        System.out.println("Number of cols in this table header: "+headerCollection.size());
        List<WebElement> rowCollection=element.findElements(By.xpath("//table/tbody/tr"));
        System.out.println("Number of rows in this table: "+rowCollection.size());
        int i_RowNum=1;
        for(WebElement rowElement:rowCollection)
        {
            List<WebElement> colCollection=rowElement.findElements(By.xpath("td"));
            int i_ColNum=1;
            for(WebElement colElement:colCollection)
            {
                System.out.println("Row "+i_RowNum+" Column "+i_ColNum+" Data "+colElement.getText());
                i_ColNum=i_ColNum+1;
            }
            i_RowNum=i_RowNum+1;
        }
//        driver.close();
    }

}
