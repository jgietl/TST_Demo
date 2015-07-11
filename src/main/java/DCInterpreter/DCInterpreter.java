package DCInterpreter;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import CCManager.CCManager;
import ORManager.Demo;
import BCInterpreter.BCs;
import org.apache.log4j.xml.DOMConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import BCInterpreter.BCInterpreter;
import IOManager.Log;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DCInterpreter {
// ==========================================================================================
// Application Under Test (AUT) Data Manager ADM
// We differentiate data by the attributes "Static", "Dynamic", and "Immediate"
// STATIC data is determined at test design time and must be concrete
//        (any string which is not "dynamic data" and not "immediate data")
// - DYNAMIC data is determined at the beginning of the test execution
//        (e.g. random test, relative dates, etc.)
// - IMMEDIATE data is determined at the current step within the test execution
//        (usually the data is calculated and presented by the application under test during the test execution)
// ==========================================================================================
// ***************************************************************************
//
// ==================================================================================================
// ==================================================================================================
// ==============================         Dynamic Data       ========================================
// ==================================================================================================
// ==================================================================================================
// Function ADM_ALL_DataInterpreter (ByRef s, ByVal strDataColumn)
// Please be aware that the conditions for SimpleDC, ComplexDC, and StaticData are evaluated dynamically.
// Therefore the grammar specified here can change with the functions (DCreference) currently bound at execution time.
// Data Component  (DC) Productions:
// DataExpression   ->  DynamicData+DataExpression
// 					|   StaticData+DataExpression
// DynamicData      ->  SimpleDC
// 					|   ComplexDC
// StaticData       ->  [NoDC (any string NOT containing SimpleDC or ComplexDC]
//                  |   EMPTYSTRING
// SimpleDC         ->  DCreference+CLOSEBROKET
// ComplexDC     	->  DCreference+BLANK+DataExpression+CLOSEBROKET
// DCreference		->  OPENBROKET+DCName
// DCname			->  [Valid function name which is loaded at exection time]
// OPENBROKET	 	->  [<]
// CLOSEBROKET		->  [>]
// BLANK			->  [ ]
// EMPTYSTRING		->  []
// +				->  [concatenation sign]
// =====================================================================
    public static String DataInterpreter(String s, String strDataColumn) {
        int intDCstart, intDCendCloseBroket, intDCendSPACE;
        String OPENBROKET = "<";
        String CLOSEBROKET = ">";
        String BLANK = " ";
        String EMPTYSTRING = "";
        String strLeftPart, strRightPart, strDCname, rc;
        String strDCparameter = "";
        String strDataInterpreter = "";
        MethodType mt = null;
        MethodHandle mh = null;
        MethodHandles.Lookup lookup = null;
        intDCstart = s.indexOf(OPENBROKET);
        if (intDCstart < 0) {
            return s;
        }
        intDCendCloseBroket = s.indexOf(CLOSEBROKET);
        if (intDCendCloseBroket <= 1) {
            return s;
        }
        intDCendSPACE = s.indexOf(BLANK, (intDCstart + 1));
        strLeftPart = s.substring(0, intDCstart);
// SimpleDC     <DCname>
        if ((intDCendSPACE <= 0) || (intDCendCloseBroket < intDCendSPACE)) {
            strDCname = s.substring(intDCstart + 1, intDCendCloseBroket);
            strDCname = "DDC_" + strDCname.toLowerCase();
            strDCparameter = EMPTYSTRING;
            strRightPart = s.substring(intDCendCloseBroket + 1);
            if (DDC_ActionExists(strDCname)) {
                rc = DataInterpreter(strRightPart, strDataColumn);
                strRightPart = rc;
            } else {
                strLeftPart = s.substring(0, intDCendCloseBroket + 1);
                rc = DataInterpreter(strRightPart, strDataColumn);
                return strLeftPart + rc;
            }
        }
// ComplexDC       <DCname DCparameterString>
        else {
            strDCname = s.substring(intDCstart + 1, intDCendSPACE);
            strDCname = "DDC_" + strDCname.toLowerCase();
            strRightPart = s.substring(intDCendSPACE + 1);
            if (DDC_ActionExists(strDCname)) {
                rc = DataInterpreter(strRightPart, strDataColumn);
                intDCendCloseBroket = rc.indexOf(CLOSEBROKET);
                if (intDCendCloseBroket >= 0) {
                    strDCparameter = rc.substring(0, intDCendCloseBroket);
                    strRightPart = rc.substring(intDCendCloseBroket + 1);
                } else {
                    strLeftPart = s.substring(intDCstart, intDCendSPACE + 1);
                    strRightPart = rc;
                    return strLeftPart + strRightPart;
                }
            } else {
                strLeftPart = s.substring(0, intDCendSPACE + 1);
                strRightPart = DataInterpreter(strRightPart, strDataColumn);
                return strLeftPart + strRightPart;
            }
        }
        String strResult = dynamicDCInterpreterAll(strDCname, strDCparameter, strDataColumn);
        return strLeftPart + strResult + strRightPart;
    }
    public static Boolean DDC_ActionExists(String s) {
        MethodType mt = MethodType.methodType(String.class, String.class, String.class);
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodHandle mh = null;
        try {
            mh = lookup.findStatic(DCInterpreter.class, s, mt);
        } catch (NoSuchMethodException ignore) {
        } catch (IllegalAccessException ignore) {
        }
        return mh != null;
    }
    public static Boolean IDC_ActionExists(String s) {
        MethodType mt = MethodType.methodType(void.class, Object.class, String.class, String.class);
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodHandle mh = null;
        s = "IDC_" + s;
        try {
            mh = lookup.findStatic(DCInterpreter.class, s, mt);
        } catch (NoSuchMethodException ignore) {
        } catch (IllegalAccessException ignore) {
        }
        return mh != null;
    }
// Desc: Dynamic DC Interpreter
// Documentation  The data component interpreter looks for a proper subroutine and calls it if found
    public static String dynamicDCInterpreterAll(String strDCname, String strDCparameter, String strDataColumn) {
        String strResult = "";
        MethodType mt = MethodType.methodType(String.class, String.class, String.class);
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodHandle mh = null;
        try {
            mh = lookup.findStatic(DCInterpreter.class, strDCname, mt);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (mh != null) {
            try {
                strResult = (String) mh.invoke(strDCparameter, strDataColumn);
            } catch (Throwable throwable) {
                System.out.println("Dynamic Data Component (DDC):" + strDCname + " found and executing with parameter string: " + strDCparameter + " using xls-Parameter: " + strDataColumn);
                throwable.printStackTrace();
            }
        }
        return strResult;
    }
// Desc: Immediate DC Interpreter
// Documentation  The data component interpreter looks for a proper subroutine and calls it if found
    public static void immediateDCInterpreterAll(String strDCname, Object element, String strDCparameter, String strDataColumn) {
        String strDC = "IDC_" + strDCname.toLowerCase();
        MethodType mt = MethodType.methodType(void.class, Object.class, String.class, String.class);
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodHandle mh = null;
        try {
            mh = lookup.findStatic(DCInterpreter.class, strDC, mt);
        } catch (NoSuchMethodException e) {
            System.out.println("Dynamic Data Component (DDC):" + strDCname + " found and executing with parameter string: " + strDCparameter + " using xls-Parameter: " + strDataColumn);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (mh != null) {
            try {
                mh.invoke(element, strDCparameter, strDataColumn);
                if (strDC.endsWith("AndWait")) {
                    Thread.sleep(4000);
                }
            } catch (TimeoutException e) {
                System.out.println("TimeoutException:" + strDCname);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }
    public static String immediateDCMapper(String strKeyword) {
        switch (strKeyword.toLowerCase()) {
            case "del":
                strKeyword = "delete";
                break;
            case "clear":
                strKeyword = "clear";
                break;
        }
        return strKeyword;
    }

//    @DC:Name=IDC_Check;Syntax=<Check [checktype][.][checkproperty] [expected_value]>;Example=<Check value 6.345,98 EUR> (PASSED if field value contains exact amount, else FAILED;
//    @Desc:<Check is the means for business to create checkpoints on any available (automated) control;
    public static void IDC_check(Object objCC, String strDCparameter, String strDataColumn) {
        int DefaultCheckTimeOut = 1;
        strDCparameter = strDCparameter.replaceAll("^\\s+", "");
        String strCheckType = IDC_Check_Type_Get(strDCparameter);
        String strExpectedValue = IDC_Check_ExpectedValue_Get(strDCparameter);
        IDC_waitfor(objCC, strCheckType + ":" + DefaultCheckTimeOut + " " + strExpectedValue, strDataColumn);
    }

//    @DC:Name=IDC_output;Example=<Output parname>;Syntax=<Output [parametername]>;
//    @Desc:<Output stores the output value from the AUT into a global hash table provided by the BCInterpreter
//    @Param:PName=parametername;Desc=parameter name where the output value is written to;
    public static void IDC_output(Object objCC, String strDCparameter, String strDataColumn) {
        strDCparameter = strDCparameter.replaceAll("^\\s+", "");
//        String strReturnValue = objCC.getText();
        String strReturnValue = CCManager.CCInvoke(objCC, "getText", "");
        if (strDCparameter == ""){
            System.out.println("DC Output: Parameter name missing strDCparameter");
        }
        if (BCInterpreter.objParDict.containsKey(strDCparameter)){
            BCInterpreter.objParDict.replace(strDCparameter,strReturnValue);
        }else{
            BCInterpreter.objParDict.put(strDCparameter,strReturnValue);
        }
        System.out.println("DC Output: Parameter name: " + strDCparameter + "  value: " + strReturnValue + " stored");
    }

//    @DC:Name=IDC_clear;Syntax=<Clear>;Example=<Clear>;
//    @Desc:<Clear enables the Business Tester to specify an empty string and therefore clearing a control;
    public static void IDC_clear(Object objCC, String strDCparameter, String strDataColumn){
//        objCC.clear();
        String strActualValue = CCManager.CCInvoke(objCC, "clear", "");
    }

// @DC:Name=IDC_Click;Syntax=<Click>;Example=<Click>;
// @Desc:Clicks the control;
    public static void IDC_click(Object element, String strDCparameter, String strDataColumn) {
//        assertTrue(element.isDisplayed());
//        element.click();
        CCManager.CCInvoke(element, "click", "");
    }
// @DC:Name=IDC_Hover;Syntax=<Hover>;Example=<Hover>;
// @Desc:<Hover> moves the mouse over the referenced element;
    public static void IDC_hover(Object objCC, String strDCparameter, String strDataColumn) {
//        Actions objAction = new Actions(BCInterpreter.driver);
//        objAction.moveToElement(element).build().perform();
        CCManager.CCInvoke(objCC, "hover", "");
    }
    public static void IDC_clickandwait(Object objCC, String strDCparameter, String strDataColumn){
//        assertTrue(element.isDisplayed());
//        element.click();
        CCManager.CCInvoke(objCC, "clickandwait", "");
    }

//  @DC:Name=DDC_input;Example=<Input Parametername>;Syntax=<Input [parname]>;
//  @Desc:<Input takes a parameter from the current run and writes it to the SUT
//  @Param:PName=parname;Desc=Name of the test run parameter;
    public static String DDC_input(String strParName, String strDataColumn) {
        String strValue = "";
        if (BCInterpreter.objParDict.containsKey(strParName)) {
            strValue = (String) BCInterpreter.objParDict.get(strParName);
        } else {
            System.out.println("IDC_input: Input Parameter does not exist: <Input  " + strParName + ">");
            System.out.println("Please define the parameter using <Output " + strParName + "> in your test script before using the <input component.");
        }
        return strValue;
    }
// @DC:Name=DDC_Today;Example=<Today> returns e.g. "18.11.2015" (dd.mm.yyyy) same format as your OS is using for date default;Syntax=<Today>;
// @Desc:<Today> creates todays date
    public static String DDC_today(String s, String strDataColumn) {
        return new SimpleDateFormat("dd.MM.yyyy").format(new Date());
    }
// @DC:Name=DDC_Tomorrow;Example=<Tomorrow> returns e.g. "19.11.2015" (dd.mm.yyyy) same format as your OS is using for date default;Syntax=<Tomorrow>;
// @Desc:<Tomorrow creates Tomorrow date;
    public static String DDC_tomorrow(String s, String strDataColumn) {
        Date today = new Date();
        Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));
        return new SimpleDateFormat("dd.MM.yyyy").format(tomorrow);
    }
    public static String DDC_rndtext(String s, String strDataColumn) {
        int len = Integer.parseInt(s.substring(4));
        String ABC = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ ) {
            sb.append( ABC.charAt( rnd.nextInt(ABC.length()) ) );
        }
        return sb.toString();
    }
// @DC:Name=DDC_yesterday;Example=<Yesterday> returns e.g. "19.11.2015" (dd.mm.yyyy) same format as your OS is using for date default;Syntax=<Tomorrow>;
// @Desc:<Yesterday creates yesterdays date;
    public static String DDC_yesterday(String s, String strDataColumn) {
        Date today = new Date();
        Date tomorrow = new Date(today.getTime() - (1000 * 60 * 60 * 24));
        return new SimpleDateFormat("dd.MM.yyyy").format(tomorrow);
    }
// @DC:Name=DDC_Year;Example=<Year> returns e.g. "2015" (yyyy) ;Syntax=<Year>;
// @Desc:<Year creates the year with fixed length 4;
    public static String DDC_year(String s, String strDataColumn) {
        return new SimpleDateFormat("yyyy").format(new Date());
    }
    private static String IDC_Check_Type_Get (String strCheckExpression){
        String arrExprParts[], strCheckType;
        int intPosition;
        arrExprParts = strCheckExpression.split(" ");
        intPosition = arrExprParts[0].indexOf(">");
        if (intPosition > 0) {
            strCheckType = arrExprParts[0].substring(0, intPosition - 1);
            return strCheckType;
        } else {
            return arrExprParts[0];
        }
    }
    private static String IDC_Check_ExpectedValue_Get (String strCheckExpr){
        int intFirstBlank;
        intFirstBlank = strCheckExpr.indexOf(" ") + 1;
        if (intFirstBlank == 1) {
            return "";
        }
        strCheckExpr = strCheckExpr.substring(intFirstBlank);
        return strCheckExpr;
    }
    private static int IDC_Check_Timeout_Get (String strCheckType) {
        String arrTimeOut[], strTimeOut;
        int intTimeOut = 1;
        arrTimeOut = strCheckType.split(":");
        if (arrTimeOut.length > 1) {
            if (arrTimeOut[1].substring(arrTimeOut[1].length()-1) == "s") {
                intTimeOut = Integer.parseInt(arrTimeOut[1]) * 1000;
            } else {
                intTimeOut = Integer.parseInt(arrTimeOut[1].substring(0,arrTimeOut[1].length()-1));
            }
        }
        return intTimeOut;
    }
// @DC:Name=ADM_PDC_WaitFor;Syntax=<WaitFor [checktype][.][checkproperty][:timeout] [expected_value]>;Example=<WaitFor value:300 6.345,98 EUR> (PASSED if field value contains exact amount after given timeout, else FAILED;
// @Desc:Same as Check-Component but offers a timeout;
    public static Boolean IDC_waitfor(Object objCC, String strDCparameter, String strDataColumn) {
        int DefaultWaitTimeOut = 500;
        String arrCheckType[], strCheckType, strCheckProperty;
        String strExpectedValue = "";
        String strActualValue = "";
        Boolean blnReportStatus = true;
        int intTimeOut = DefaultWaitTimeOut;
        int arrTimeOut[];
// Compatibility for check "not[ ]" & "not"
//        strDCparameter = strDCparameter.replaceFirst(" ", "");
        if (strDCparameter.substring(0, 4).toLowerCase() == "not ") {
            strDCparameter = "not" + strDCparameter.substring(3);
        }
        strExpectedValue = IDC_Check_ExpectedValue_Get(strDCparameter);
        strCheckType = IDC_Check_Type_Get(strDCparameter);
// Get Timeout
        if (strCheckType.contains(":")) {
            strCheckType = strCheckType.substring(0, strCheckType.indexOf(":"));
            intTimeOut = IDC_Check_Timeout_Get(strCheckType);
        }
// Get CheckType & Property
// TODO        strCheckProperty = ADM_CCReflection(objCC, "DefaultProperty").DefaultProperty
        strCheckProperty = "value";
        arrCheckType = strCheckType.split(".");
        if (arrCheckType.length > 0) {
            if (arrCheckType[0].toLowerCase() != "value") {
                strCheckProperty = arrCheckType[0];
            }
            strCheckType = arrCheckType[1];
        }
        strCheckType = strCheckType.toLowerCase();
        switch (strCheckType) {
            case "left":
                blnReportStatus = waitProperty(objCC, strCheckProperty, strExpectedValue, intTimeOut);
//                blnReportStatus = waitProperty(objCC, strCheckProperty, micRegExpMatch(DC_EscapeRegex(strExpectedValue) & ".*"), intTimeOut);
                break;
//            case "right":
//                blnReportStatus = objCC.WaitProperty(strCheckProperty, micRegExpMatch(".*" & DC_EscapeRegex(strExpectedValue)), intTimeOut);
//                break;
            case "mid":
                blnReportStatus = waitProperty(objCC, strCheckType, strExpectedValue, intTimeOut);
                break;
//            case "empty":
//                blnReportStatus = objCC.WaitProperty(strCheckProperty, "", intTimeOut);
//                break;
//            case "notempty":
//                blnReportStatus = objCC.WaitProperty(strCheckProperty, micNotEqual(""), intTimeOut);
//                break;
//            case "regexp":
//                blnReportStatus = objCC.WaitProperty(strCheckProperty, micRegExpMatch(strExpectedValue), intTimeOut);
//                break;
//            case "greaterthan":
//                blnReportStatus = objCC.WaitProperty(strCheckProperty, micGreaterThan(strExpectedValue), intTimeOut);
//                break;
//            case "lessthan":
//                blnReportStatus = objCC.WaitProperty(strCheckProperty, micLessThan(strExpectedValue), intTimeOut);
//                break;
//            case "notequal":
//                blnReportStatus = objCC.WaitProperty(strCheckProperty, micNotEqual(strExpectedValue), intTimeOut);
//                break;
//            case "selectedindex":
//                blnReportStatus = objCC.WaitProperty("selected item index", strExpectedValue, intTimeOut)
//                strActualValue = "[" & objCC.GetROProperty("selected item index") & "] " & objCC.GetROProperty(strCheckProperty);
//                break;
//            case "enabled":
//                if (strExpectedValue == "") {
//                    strExpectedValue = "true";
//                }
//                if (DC_StringtoBool(objCC.Disabled()) == DC_StringtoBool(strExpectedValue)) {
//                    Wait(intTimeOut / 1000);
//                    blnReportStatus = (DC_StringtoBool(objCC.Disabled()) == !(DC_StringtoBool(strExpectedValue)));
//                } else {
//                    blnReportStatus = true;
//                }
//                strActualValue = !(DC_StringtoBool(objCC.Disabled()));
//                break;
//            case "disabled":
//                if (strExpectedValue == "") {
//                    strExpectedValue = "true";
//                }
//                if (DC_StringtoBool(objCC.Disabled()) == !(DC_StringtoBool(strExpectedValue))) {
//                    Wait(intTimeOut / 1000);
//                    blnReportStatus = ((DC_StringtoBool(objCC.Disabled()) == DC_StringtoBool(strExpectedValue)));
//                } else {
//                    blnReportStatus = true;
//                }
//                if (DC_StringtoBool(objCC.Disabled())) {
//                    strActualValue = "true";
//                } else {
//                    strActualValue = "false";
//                }
//                break;
            case "exist":
                strActualValue = Exists(objCC, DefaultWaitTimeOut);
                break;
            case "notexist":
                strActualValue = (NotExists(objCC, 1));
                break;
//            case "whitespace":
//                blnReportStatus = objCC.WaitProperty(strCheckProperty, micRegExpMatch("\s*"), intTimeOut);
            default:
                //        throw new RuntimeException("Unknown Check Type");
                if (strCheckType.toLowerCase() != "value") {
                    strCheckProperty = strCheckType;
                }
                strCheckType = "";
                blnReportStatus = waitProperty(objCC, strCheckProperty, strExpectedValue, intTimeOut);
        }
        if (strActualValue == "") {
//            strActualValue = objCC.getText();
            strActualValue = CCManager.CCInvoke(objCC, "getText", null);
        }
// Report Results
        if (blnReportStatus == true) {
            String strReportStatus = "PASSED - Expected: " + strExpectedValue + "  Actual: " + strActualValue;
            System.out.println(strReportStatus);
            BCInterpreter.objReporter.addSystemOut(BCInterpreter.objReporter.getTestcase(), strReportStatus);
        }
        if (blnReportStatus == false) {
            String strReportStatus = "FAILED - Expected: " + strExpectedValue + "  Actual: " + strActualValue;
            System.out.println(strReportStatus);
            BCInterpreter.objReporter.addErr(BCInterpreter.objReporter.getTestcase(), "FAILED", "Assertion", strReportStatus);
        }
        return blnReportStatus;
    }
    private static String DC_EscapeRegex(String regex){
        String re = regex.replace("\\", "\\\\"); regex = re;
        re = regex.replace("+", "\\+"); regex = re;
        re = regex.replace("*", "\\*"); regex = re;
        re = regex.replace(".", "\\."); regex = re;
        re = regex.replace("?", "\\?"); regex = re;
        re = regex.replace("$", "\\$"); regex = re;
        re = regex.replace("^", "\\^"); regex = re;
        re = regex.replace("[", "\\["); regex = re;
        re = regex.replace("]", "\\]"); regex = re;
        re = regex.replace("{", "\\{"); regex = re;
        re = regex.replace("}", "\\}"); regex = re;
        re = regex.replace("(", "\\("); regex = re;
        re = regex.replace(")", "\\)"); regex = re;
        re = regex.replace("|", "\\|"); regex = re;
        return regex;
    }
    public static Boolean waitProperty(Object element, String strProperty, String strValidation, long intTimeOut) {
        String strActualValue = "";
        WebDriverWait objWait = new WebDriverWait(BCInterpreter.driver, intTimeOut);
//        return objSE.checkAttribute(strProperty, strValidation, intTimeOut);
        switch (strProperty) {
            case "value":
//                objWait.until(ExpectedConditions.textToBePresentInElement(element, strValidation));
//                strActualValue = element.getText();
                strActualValue = CCManager.CCInvoke(element, "getText", null);
                return strActualValue.equals(strValidation);
            case "mid":
//                objWait.until(ExpectedConditions.textToBePresentInElement(element, strValidation));
//                strActualValue = element.getText();
                strActualValue = CCManager.CCInvoke(element, "getText", null);
                return strActualValue.contains(strValidation);
            default:
                if (element != null){
//                    strActualValue = element.getAttribute(strProperty);
                    strActualValue = CCManager.CCInvoke(element, "getAttribute", strProperty);
                    if (strActualValue.equals(strValidation)){
                        return true;
                    }
                }
                return false;
        }
    }
    private static String Exists(Object element, int intTimeOut)
    {
//        return element != null;
        return CCManager.CCInvoke(element, "exist", null);
    }
    private static String NotExists(Object element, int intTimeOut)
    {
        return CCManager.CCInvoke(element, "notexist", null);
    }
// DESC: Converts given String to bool. unknown/default= false!! 1 =true
    private static Boolean DC_StringtoBool(String strString){
        Boolean blnRC = false;
        switch (strString.toLowerCase()){
            case "yes":
            case "ja":
            case "true":
            case "wahr":
            case "on":
            case "1":
            case "ein":
                blnRC = true;
                break;
        }
        return blnRC;
    }
    private static void Wait (int intSec){
        for (int i = 0; i < intSec; i++){
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                //Handle exception
            }
        }
    }

    public static void toStringWebTable(WebElement objWebTable) {
        List<WebElement> objWebTableRows = objWebTable.findElements(By.tagName("tr"));
        int row_count = objWebTableRows.size();
        for (int row = 0; row < row_count; row++){
            List<WebElement> objWebTableRowColumns = objWebTableRows.get(row).findElements(By.tagName("td"));
            int columns_count = objWebTableRowColumns.size();
            System.out.println("Number of cells In Row " + row + " are " + columns_count);
            for (int col=0; col < columns_count; col++){
                String strCell = objWebTableRowColumns.get(col).getText();
                System.out.println("Cell Value Of row number " + row + " and column number " + col+ " Is " + strCell);
            }
        }
    }

//    micGreaterThan: Specifies that QuickTest waits until the property value is greater than the specified value.
//    micLessThan: Less than; Specifies that QuickTest waits until the property value is less than the specified value.
//    micGreaterThanOrEqual: Greater than or equal to; Specifies that QuickTest waits until the property value is greater than or equal to the specified value.
//    micLessThanOrEqual: Less than or equal to; Specifies that QuickTest waits until the property value is less than or equal to the specified value.
//    micNotEqual: Not equal to; Specifies that QuickTest waits until the property value is not equal to the specified value.
//    micRegExpMatch: Regular expression; Specifies that QuickTest waits until the property value achieves a regular expression match with the specified value.
//           Regular expressions are case-sensitive and must match exactly.
//           For example, 'E.*h' matches 'Earth' but not 'The Earth' or 'earth'.
    public static void main(String[] args) {
        DOMConfigurator.configure("log4j.xml");
        Log.startTestCase("DCInterpreter");
        System.out.println(DataInterpreter("<today est <tomorrow>", ""));
        System.out.println(DataInterpreter("<today est <tomorrow>>", ""));
        System.out.println(DataInterpreter("testtestest", ""));
        System.out.println(DataInterpreter("<tomorrow>", ""));
        System.out.println(DataInterpreter("<year>", ""));
        System.out.println(DataInterpreter("<today> test <tomorrow>", ""));
        System.out.println(DataInterpreter("<todasdfay> t<today>Y>>><<<est <tomorrow>", ""));
        System.out.println(DataInterpreter("<est <tomorrow>", ""));
        Map oDict = new HashMap();
        oDict.put("Component", "Open");
        oDict.put("URL", "http://hldlx04.berner.lan:9001/bernerstorefront/de-de/p_ProductName_91952.html");
        oDict.put("Browser", "CH");
        try {
            BCInterpreter oBCI = new BCInterpreter("");
        } catch (Exception e) {
            e.printStackTrace();
        }
        BCs.open(oDict);
        oDict.put("CookiesAktivieren", "<Click>");
        BCs.open(oDict);
        toStringWebTable(Demo.tblw3schools(BCInterpreter.driver));
//        System.out.println(strTable);
        Log.endTestCase("DCInterpreter");
    }
}

