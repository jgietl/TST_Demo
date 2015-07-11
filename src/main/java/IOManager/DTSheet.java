package IOManager;
import BCInterpreter.BCInterpreter;
import DCInterpreter.DCInterpreter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.w3c.dom.Element;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class DTSheet {
    protected List<DTRow> rows;
    protected String Name;
    protected int intRowCount;
    protected int intCurrentRow;
    public int getRowCount() {
        return intRowCount;
    }
    public void setRowCount(int intRowCount) {
        this.intRowCount = intRowCount;
    }
    public DTSheet(String strName) {
        this.rows = new ArrayList<DTRow>();
        this.setName(strName);
        intRowCount = 0;
        intCurrentRow = 0;
    }
    public void importRow(Row objRow) throws IOException {
        DTRow objDTRow = new DTRow ();
        this.rows.add(objDTRow);
        this.setRowCount(this.getRowCount() + 1);
        this.setCurrentRow(this.getRowCount());
        for (int i = 0; i < objRow.getLastCellNum(); i++) {
            Cell objCell = objRow.getCell(i);
            objDTRow.importCell(objCell);
        }
    }
    public void setName(String Name) { this.Name = Name; }
    public String getName() { return Name; }
    public void addParameter(String strParname){    }
    public void deleteParameter(String strParname) {    }
    public DTRow getRow(DTSheet sheet, int intRow) {
        DTRow oRow = sheet.rows.get(intRow);
        return oRow;
    }
    public Integer getCurrentRow() {
        return this.intCurrentRow;
    }
    public class dtParameter {
        protected String Name;
        protected String Value;
        public String getValue() {
            return Value;
        }
        public void setValue(String value) {
            Value = value;
        }
        public String getName() {
            return Name;
        }
        public void setName(String name) {
            Name = name;
        }
    }
    public String getParameter(String strParname) {
        String strParVal = "";
        if (intRowCount > 1 && intCurrentRow >= 0 && intCurrentRow < intRowCount) {
            int intColCount = this.rows.get(0).getColCount();
            for (int i = 0; i < intColCount; i++) {
                dtParameter oPar = this.getParameter(i);
                String strParamName = oPar.getName();
                if (strParamName.compareTo(strParname) == 0) {
                    strParVal = oPar.getValue();
                    break;
                }
            }
        }
        return strParVal;
    }
    public dtParameter getParameter(int intCol) {
        int intPosAssign = 0;
        String strParamName = "";
        String strParamValue = "";
        dtParameter oPar = new dtParameter();
        oPar.setName(strParamName);
        oPar.setValue(strParamValue);
        DTCell oCell;
        DTRow oRow;
        if (intRowCount > 1 && (intCurrentRow >= 0 ) && intCurrentRow <= intRowCount) {
            oRow = this.rows.get(0);
            if (intCol < oRow.getColCount()) {
                oCell = oRow.cells.get(intCol);
                strParamName = oCell.getCellValue();
                oRow = this.rows.get(intCurrentRow);
                if (intCol < oRow.getColCount()) {
                    oCell = oRow.cells.get(intCol);
                    strParamValue = oCell.getCellValue();
                    if (strParamName.toLowerCase().matches("parameter(.*)") && strParamValue.compareTo("") != 0 ) {
                        intPosAssign = strParamValue.indexOf(":=");
                        if (intPosAssign < 1) {
                            //                  Reporter.ReportEvent micDebug, "BC: " & oDataDict.item(BUSINESS_COMPONENT), _
                            //                  "The Parameter could not be interpreted: " & vbCrLf & strParamName & " " & strParamVal
                            //                  strParamVal = ""
                        }
                        strParamName = strParamValue.substring(0, intPosAssign);
                        strParamValue = strParamValue.substring(intPosAssign + 2);
                    }
                    oPar.setName(strParamName);
                    oPar.setValue(strParamValue);
                }
            }
        }
        return oPar;
    }
    public void setCurrentRow (Integer intRow) {
        this.intCurrentRow = intRow;
    }
    public void createBC_Dict(Map objDict){
//        '***************************************************************************'
//        ' Input/Output Manager:  Generate Dictionary Object for current Business Component
//        ' Output objDict
        int intCounter, i, intPosAssign;
        String strParamVal, strParamName, strElement, strMessageText;
        for (intCounter = 0; intCounter <= this.getRow(this, 0).getColCount(); intCounter++ ) {
            dtParameter oPar = this.getParameter(intCounter);
            strParamName = oPar.getName();
            strParamVal = oPar.getValue();
            strParamVal = DCInterpreter.DataInterpreter(strParamVal, strParamName);
            if (strParamVal.compareTo("") != 0 ) {
                objDict.put( strParamName, strParamVal.trim());
            }
        }
        String strLog = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new Date()) +  " \t" + objDict ;
        System.out.println(strLog);
        Element tc = BCInterpreter.objReporter.getTestcase();
        BCInterpreter.objReporter.addSystemOut(tc, strLog);
    }
}

