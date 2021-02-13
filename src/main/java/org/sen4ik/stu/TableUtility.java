package org.sen4ik.stu;

import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.sen4ik.utils.ArrUtil;
import org.sen4ik.utils.selenium.base.SeleniumUtils;
import org.sen4ik.utils.selenium.utils.DriverUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Log4j2
public class TableUtility {

    private static Supplier supplier;
    private static By tableLocator;
    private static WebElement table;

    private static String headerRowXpath = ".//thead/tr";
    private static By headerRow = By.xpath(headerRowXpath);
    private static String headerCellXpath = ".//thead/tr/th";
    private static By headerCells = By.xpath(headerCellXpath);
    private static String rowsXpath = ".//tbody/tr";
    private static By rows = By.xpath(rowsXpath);
    private static String cellsXpath = ".//tbody/tr/td";
    private static By cells = By.xpath(cellsXpath);

    public static void init(Supplier supplier, By tableLocator) {
        setSupplier(supplier);
        TableUtility.tableLocator = tableLocator;
        SeleniumUtils.init(() -> getDriver());
    }

    private static void setSupplier(Supplier supplier) {
        TableUtility.supplier = supplier;
    }

    private static WebDriver getDriver() {
        return (WebDriver) supplier.get();
    }

    public static void setTableLocator(By tableLocator){
        TableUtility.tableLocator = tableLocator;
    }

    public static WebElement getTable() {
        table = getDriver().findElement(tableLocator);
        return table;
    }

    public static boolean isTableVisible() {
        return DriverUtil.isElementPresent(tableLocator);
    }

    /**
     * Checks if //thead is present within the table.
     * @return
     */
    public static boolean isTheadPresent(){
        return DriverUtil.isElementPresent(getTable(), By.xpath(".//thead"));
    }

    /**
     * Checks if //thead/tr is present within the table.
     * @return
     */
    public static boolean isHeaderRowPresent(){
        return DriverUtil.isElementPresent(getTable(), headerRow);
    }

    public static List<String> getHeaders(){
        List<String> columnHeaders = getTable().findElements(headerCells)
                .stream()
                .map((el) -> el.getText().trim())
                .collect(Collectors.toList());
        log.info("columnHeaders: " + columnHeaders.toString());
        return columnHeaders;
    }

    public static List<WebElement> getHeaderElements(){
        return getTable().findElements(headerCells);
    }

    public static boolean isHeaderPresent(String headerName){
        isArgumentEmptyOrNullString(headerName);
        return getHeaders().contains(headerName);
    }

    public static boolean isHeaderPresent(int index){
        isArgumentNegativeInteger(index);
        if (index >= 0 && index <= (getHeaderElements().size()-1)){
            return true;
        }
        return false;
    }

    public static int getHeaderIndex(String headerName){
        isArgumentEmptyOrNullString(headerName);
        int headerIndex = getHeaders().indexOf(headerName);
        log.info("headerIndex: " + headerIndex);
        if(headerIndex < 0){
            log.error("\"" + headerName + "\" column wasn't found");
        }
        return headerIndex;
    }

    public static String getHeaderByIndex(int index){
        isArgumentNegativeInteger(index);
        List<String> headers = getHeaders();
        if(index > (headers.size()-1)){
            return "";
        }
        return headers.get(index);
    }

    public static WebElement getHeaderElementByName(String headerName){
        isArgumentEmptyOrNullString(headerName);
        return getTable().findElement(By.xpath(headerCellXpath + "[contains(text(), \"" + headerName + "\")]"));
    }

    /**
     * Gets header WebElement using index.
     * First column index should be 0.
     * @param index
     * @return
     */
    public static WebElement getHeaderElementByIndex(int index){
        isArgumentNegativeInteger(index);
        return getTable().findElement(By.xpath(headerCellXpath + "[" + (index+1) + "]"));
    }

    public static int getColumnCount() {
        return getTable().findElements(By.xpath(rowsXpath + "[1]/td[1]")).size();
    }

    /**
     * First row index should be 0.
     * @param rowIndex
     * @param columnIndex
     * @return
     */
    public static WebElement getCell(int rowIndex, int columnIndex) {
        isArgumentNegativeInteger(rowIndex, columnIndex);
        verifyRowIndex(rowIndex);
        verifyColumnIndex(columnIndex);
        By cell = By.xpath(rowsXpath + "[" + (rowIndex+1) + "]" + "/td[" + (columnIndex+1) + "]");
        return getTable().findElement(cell);
    }

    public static WebElement getCell(int rowIndex, String columnName) {
        isArgumentNegativeInteger(rowIndex);
        isArgumentEmptyOrNullString(columnName);
        verifyRowIndex(rowIndex);
        By cell = By.xpath(rowsXpath + "[" + (rowIndex+1) + "]" + "/td[" + (getHeaderIndex(columnName)+1) + "]");
        return getTable().findElement(cell);
    }

    public static String getCellText(int rowIndex, int columnIndex) {
        return getCell(rowIndex, columnIndex).getText();
    }

    public static String getCellText(int rowIndex, String columnName) {
        return getCell(rowIndex, columnName).getText();
    }

    /**
     * If tbody is present, get rows using .//tbody/tr xpath, otherwise get rows using .//tr xpath.
     * @return
     */
    public static List<WebElement> getRows(){
        if(isTbodyPresent()){
            return getTable().findElements(rows);
        }
        else{
            return getTable().findElements(By.xpath(".//tr"));
        }
    }

    /**
     * Use 0 for first row index.
     * @param rowIndex
     * @return
     */
    public static WebElement getRow(int rowIndex){
        isArgumentNegativeInteger(rowIndex);
        verifyRowIndex(rowIndex);
        if(isTbodyPresent()){
            return getTable().findElements(rows).get(rowIndex);
        }
        else{
            return getTable().findElements(By.xpath(".//tr")).get(rowIndex);
        }
    }

    public static String getRowText(int rowIndex){
        isArgumentNegativeInteger(rowIndex);
        verifyRowIndex(rowIndex);
        return getRow(rowIndex).getText();
    }

    public static int getRowCount(){
        return getTable().findElements(By.xpath(rowsXpath)).size();
    }

    public static void getRowIndexForColumnValue(String columnName, String cellText){
        // TODO:
    }

    public static List<WebElement> getColumn(int columnIndex){
        By columnValues = By.xpath(rowsXpath + "/td[" + (columnIndex+1) + "]");
        return getTable().findElements(columnValues);
    }

    public static List<WebElement> getColumn(String headerName){
        return getColumn(getHeaderIndex(headerName));
    }

    public static List<String> getColumnText(int columnIndex){
        By columnValues = By.xpath(rowsXpath + "/td[" + (columnIndex+1) + "]");
        List<WebElement> columnElements = getTable().findElements(columnValues);
        List<String> columnTextList = columnElements
                .stream()
                .map((el) -> el.getText().trim())
                .collect(Collectors.toList());
        return columnTextList;
    }

    public static List<String> getColumnText(String headerName){
        return getColumnText(getHeaderIndex(headerName));
    }

    public static List<String> getUniqueColumnText(int columnIndex){
        List<String> columnTextList = getColumnText(columnIndex);
        return ArrUtil.arrayToUniqueList((ArrayList<String>) columnTextList);
    }

    public static List<String> getUniqueColumnText(String headerName){
        return getUniqueColumnText(getHeaderIndex(headerName));
    }

    public static boolean isValuePresentInColumn(int columnIndex, String value){
        return getColumnText(columnIndex).contains(value);
    }

    public static boolean isValuePresentInColumn(String headerName, String value){
        return getColumnText(headerName).contains(value);
    }

    /**
     * Checks if tbody is present within the table.
     * @return
     */
    public static boolean isTbodyPresent(){
        return DriverUtil.isElementPresent(getTable(), By.xpath(".//tbody"));
    }

    public static void waitForNumberOfRowsToChange(){

    }

    public static void waitForNumberOfRowsToEqual(int expectedNumOfRows){



    }

    public static void waitForColumnValuesToBeUnique(){

    }

    public static void doesColumnHaveUniqueValues(){

    }

    public static void waitForNumberOfColumnsToChange(){

    }

    private static void isArgumentNegativeInteger(int index){
        if(index < 0){
            throw new IllegalArgumentException("index must be a positive integer");
        }
    }

    private static void isArgumentNegativeInteger(int... index){
        for(int i : index){
            isArgumentNegativeInteger(i);
        }
    }

    private static void isArgumentEmptyOrNullString(String val){
        if(val == null || val == ""){
            throw new IllegalArgumentException("argument must be a non empty string");
        }
    }

    private static void isArgumentEmptyOrNullString(String... val){
        for(String v : val){
            isArgumentEmptyOrNullString(v);
        }
    }

    private static void verifyRowIndex(int rowIndex){
        if((rowIndex+1) > getRowCount()){
            throw new IllegalArgumentException("rowIndex can't be greater than the overall number of rows");
        }
    }

    private static void verifyColumnIndex(int columnIndex){
        if((columnIndex+1) > getColumnCount()){
            throw new IllegalArgumentException("columnIndex can't be greater than the overall number of columns");
        }
    }

}
