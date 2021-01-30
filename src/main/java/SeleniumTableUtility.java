import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class SeleniumTableUtility {

    private By tableBy;
    private WebDriver driver;
    private WebElement table;

    private By headersBy = By.xpath(".//thead/tr/th");
    private By rowsBy = By.xpath(".//tbody/tr");
    private By cellsBy = By.xpath(".//tbody/tr/td");

    public SeleniumTableUtility(WebDriver driver, By table) {
        this.tableBy = table;
        this.driver = driver;
        this.table = driver.findElement(table);
    }

    public List<String> getHeaders(){
        List<String> columnHeaders = table.findElements(headersBy)
                .stream()
                .map(el -> el.getText())
                .collect(Collectors.toList());
        log.info("columnHeaders: " + columnHeaders.toString());
        return columnHeaders;
    }

    public List<WebElement> getHeaderElements(){
        return table.findElements(headersBy);
    }

    public boolean isHeaderPresent(String headerName){
        return getHeaders().contains(headerName);
    }

    public boolean isHeaderPresent(int index){
        if (index >= 0 && index <= (getHeaderElements().size()-1)){
            return true;
        }
        return false;
    }

    public int getHeaderIndex(String headerName){
        return getHeaders().indexOf(headerName);
    }

    public String getHeaderByIndex(int index){
        List<String> headers = getHeaders();
        if(index > (headers.size()-1)){
            return "";
        }
        return headers.get(index);
    }

    public void getHeaderElementByName(String headerName){

    }

    public void getHeaderElementByIndex(int index){

    }

    public void getCell(int row, int column) {

    }

    public void getCell(int row, String columnName) {

    }

    public void getCellText(int row, int column) {

    }

    public void getCellText(int row, String columnName) {

    }

    public void getCellAtRowForColumnNamed(int row, String columnName){

    }

    public void getCellTextAtRowForColumnNamed(int row, String columnName){

    }

    public void getRowIndexForColumnNamedWithCellTextEqualing(String columnName, String cellText){

    }

    public void getRows(){
        // if tbody is present we get rows from tbody, otherwise we get it from just the table
    }

    public void getRow(int index){

    }

    public void getRowCount(){

    }

    public void isTableBodyPresent(){

    }

    public void waitForNumberOfRowsToChange(){

    }

    public void waitForNumberOfColumnsToChange(){

    }

}
