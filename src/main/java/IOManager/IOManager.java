package IOManager;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
/**
 *
 */
public class IOManager {
    public void readExcel(String filePath,String fileName,String sheetName) throws IOException {
        File objFile  = new File(filePath+"\\"+fileName);
        FileInputStream inputStream = new FileInputStream(objFile);
        Workbook objWorkbook = null;
        String fileExtensionName = fileName.substring(fileName.indexOf("."));
        if(fileExtensionName.equals(".xlsx")){
            objWorkbook = new XSSFWorkbook(inputStream);
        }
        else if(fileExtensionName.equals(".xls")){
            objWorkbook = new HSSFWorkbook(inputStream);
        }
        Sheet objSheet = objWorkbook.getSheet(sheetName);
        int rowCount = objSheet.getLastRowNum()-objSheet.getFirstRowNum();
        for (int i = 0; i < rowCount+1; i++) {
            Row objRow = objSheet.getRow(i);
            for (int j = 0; j < objRow.getLastCellNum(); j++) {
                System.out.print(objRow.getCell(j).getStringCellValue()+"|| ");
            }
            System.out.println();
        }
    }
    public void writeExcel(String filePath,String fileName,String sheetName,String[] dataToWrite) throws IOException{
        File objFile =  new File(filePath+"\\"+fileName);
        FileInputStream inputStream = new FileInputStream(objFile);
        Workbook objWorkbook = null;
        String fileExtensionName = fileName.substring(fileName.indexOf("."));
        if(fileExtensionName.equals(".xlsx")){
            objWorkbook = new XSSFWorkbook(inputStream);
        }
        else if(fileExtensionName.equals(".xls")){
            objWorkbook = new HSSFWorkbook(inputStream);
        }
        Sheet objSheet = objWorkbook.getSheet(sheetName);
        int rowCount = objSheet.getLastRowNum()-objSheet.getFirstRowNum();
        Row objRow = objSheet.getRow(0);
        Row objNewRow = objSheet.createRow(rowCount+1);
        for(int j = 0; j < objRow.getLastCellNum(); j++){
            Cell objCell = objNewRow.createCell(j);
            objCell.setCellValue(dataToWrite[j]);
        }
        inputStream.close();
        FileOutputStream outputStream = new FileOutputStream(objFile);
        objWorkbook.write(outputStream);
        outputStream.close();
    }
    public static void main(String...strings) throws IOException{
        IOManager objExcelRead = new IOManager();
        String filePath = System.getProperty("user.dir")+"\\src";
        objExcelRead.readExcel(filePath,"Test_001_TFW_Registration.xlsx","Process");
        String[] arrValuesToWrite = {"Mr. E","Noida","X"};
        IOManager objExcelWrite = new IOManager();
        objExcelWrite.writeExcel(System.getProperty("user.dir")+"\\src","ExportExcel.xlsx","Process",arrValuesToWrite);
    }
}
