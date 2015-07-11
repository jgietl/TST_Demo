package IOManager;

public class DTCell {
    private String cellValue;
    protected DTCell() {
        this.cellValue = "";
    }
    public void setCellValue(String cellValue) {
        this.cellValue = cellValue;
    }
    public String getCellValue() {
        return cellValue;
    }
    public String toString() {
        return this.cellValue;
    }
}