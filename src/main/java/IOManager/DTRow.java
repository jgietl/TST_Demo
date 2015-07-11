package IOManager;
import org.apache.poi.ss.usermodel.Cell;

import java.util.ArrayList;
import java.util.List;

public class DTRow {
    private int intCurrentCell;
    private int intColCount;
    public List<DTCell> cells;
    DTRow(){
        this.cells = new ArrayList<DTCell>();
        this.setColCount(0);
        this.intCurrentCell = 0;
    }
    public void importCell(Cell objCell) {
        DTCell objDTCell = new DTCell();
        this.cells.add(objDTCell);
        this.setColCount(this.getColCount() + 1);
        String strCellValue = objCell.getStringCellValue();
        objDTCell.setCellValue(strCellValue);
        this.setCurrentCell(this.getColCount());
//        System.out.print(objDTCell.getCellValue() + "||");
    }
    public int getColCount() {
        return intColCount;
    }
    public void setColCount(int intColCount) {
        this.intColCount = intColCount;
    }
    public void setCurrentCell(int intDTCell) {
    }
    public int getCurrentCell() {
        return this.intCurrentCell;
    }
    public DTCell Cell(int intCol) {
        return this.cells.get(intCol);
    }
}
