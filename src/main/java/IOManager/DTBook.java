package IOManager;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DTBook {
    protected static List<DTSheet> sheets;
    protected static int intSheetCount;
    protected static int intCurrentSheet;
    public DTBook(){
        sheets = new ArrayList<DTSheet>();
        intSheetCount = 0;
        intCurrentSheet = 0;
    }

    public static Workbook getWorkbook(String filePath, String fileName) throws IOException {
        if (filePath == "") {
            filePath = System.getProperty("user.dir") + "\\src";
        }
        File objFile = new File(filePath + "\\" + fileName);
        FileInputStream inputStream = new FileInputStream(objFile);
        Workbook objWorkbook = null;
        String fileExtensionName = fileName.substring(fileName.indexOf("."));
        if (fileExtensionName.equals(".xlsx")) {
            objWorkbook = new XSSFWorkbook(inputStream);
        } else if (fileExtensionName.equals(".xls")) {
            objWorkbook = new HSSFWorkbook(inputStream);
        }
        return objWorkbook;
    }

    public static void addSheet(String strSheetName) {
    }

    public static void deleteSheet(String strSheetName) {
    }

    public static DTSheet getSheet(DTBook book, String strSheetName) {
        DTSheet objDTSheet = null;
        for (int i = 0; i < book.getSheetCount(); i++) {
            objDTSheet = sheets.get(i);
            if ((strSheetName.compareTo(objDTSheet.getName())) == 0) {
                return objDTSheet;
            }
        }
        return objDTSheet;
    }

    public static void setCurrentSheet(String strSheetName) {
    }

    public static void exportBook(String strFilename) {
    }

    public static void exportSheet(String strFilename, String strSheetName) {
    }

    public static void getRowCount() {
    }

    public static void getRow() {
    }

    public static void setCurrentRow(String strSheetname) {
    }

    public static void main(String... strings) throws IOException {
        DTBook objDTBook = new DTBook();
        Workbook objWorkbook;
        objWorkbook = getWorkbook("", "Process_Example_ResponsiveMediathek.xlsx");
        objDTBook.importSheet(objWorkbook, "Process", "Process");
        objDTBook.importBook(objWorkbook);
        for (int i = 0; i < intSheetCount; i++) {
            DTSheet oDTSheet = sheets.get(i);
            for (int j = 0; j < oDTSheet.getRowCount(); j++) {
                DTRow oRow = oDTSheet.rows.get(j);
                for (int k = 0; k < oRow.getColCount(); k++) {
                    DTCell oCell = oRow.cells.get(k);
                    System.out.print(oCell.getCellValue() + "||");
                }
                System.out.println();
            }
        }
    }

    public int getSheetCount() {
        return intSheetCount;
    }

    public void setSheetCount(int intSheetCount) {
        DTBook.intSheetCount = intSheetCount;
    }

    public void importSheet(Workbook objWorkbook, String strSrcSheetName, String strDstSheetName) throws IOException {
        DTSheet objDTSheet = null;
        int intDTSheetCount = this.getSheetCount();
        Sheet objSheet = objWorkbook.getSheet(strSrcSheetName);
        if (intDTSheetCount > 0) {
            for (int k = 0; k < intDTSheetCount; k++) {
                objDTSheet = sheets.get(k);
                if (strDstSheetName.compareTo(objDTSheet.getName()) == 0) {
                    Log.info("importSheet NOT performed - Sheet with name: " + strDstSheetName + " already exists");
                    System.out.println("importSheet NOT performed - Sheet with name: " + strDstSheetName + " already exists");
                    return;
                }
            }
        }
        objDTSheet = new DTSheet(strDstSheetName);
        sheets.add(objDTSheet);
        this.setSheetCount(this.getSheetCount() + 1);
        for (int i = objSheet.getFirstRowNum(), intDTRow = 0; i < objSheet.getLastRowNum() + 1; i++, intDTRow++) {
            Row objRow = objSheet.getRow(i);
            objDTSheet.importRow(objRow);
        }
    }

    public void importBook(Workbook objWorkbook) throws IOException {
        int sheetCount = objWorkbook.getNumberOfSheets();
//                .getActiveSheetIndex();
        for (int i = 0; i < sheetCount; ++i) {
            String strSheetName = objWorkbook.getSheetName(i);
            importSheet(objWorkbook, strSheetName, strSheetName);
        }
    }
}