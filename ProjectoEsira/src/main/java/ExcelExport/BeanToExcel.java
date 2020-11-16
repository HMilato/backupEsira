/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ExcelExport;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Vbox;

/**
 *
 * @author Ussimane
 */
public class BeanToExcel {

    protected List<?> dataList = null;
    protected List<ExcelColumns> excelColumns = null;
    protected String dataSheetName;
    private XSSFSheet sheet;
    private XSSFWorkbook workbook;

    public List<?> getDataList() {
        return dataList;
    }

    public void setDataList(List<?> dataList) {
        this.dataList = dataList;
    }

    public List<ExcelColumns> getExcelColumns() {
        return excelColumns;
    }

    public void setExcelColumns(List<ExcelColumns> excelColumns) {
        this.excelColumns = excelColumns;
    }

    public String getDataSheetName() {
        return dataSheetName;
    }

    public void setDataSheetName(String dataSheetName) {
        this.dataSheetName = dataSheetName;
    }

    public void exportToExcel() {

        try {

            // Blank workbook
            this.workbook = new XSSFWorkbook();
            // Create a blank sheet
            this.sheet = this.workbook.createSheet(this.dataSheetName);

            int numCols = this.excelColumns.size();
            int currentRow = 0;

            // Create the report header at row 0
            Row excelHeader = sheet.createRow(currentRow);
            Cell dataCell;
            // Loop over all the column beans and populate the report headers
            for (int i = 0; i < numCols; i++) {
                dataCell = excelHeader.createCell(i);
                dataCell.setCellStyle(getHeaderCellStyle(workbook));
                dataCell.setCellValue(excelColumns.get(i).getHeader());
                this.sheet.setColumnWidth(i, excelColumns.get(i).getWidth());
            }

            currentRow++;
            Row dataRow;
            for (int i = 0; i < this.dataList.size(); i++) {
                // create a row in the spreadsheet
                dataRow = sheet.createRow(currentRow++);
                // get the bean for the current row
                // get the bean for the current row
                Object bean = dataList.get(i);
                for (int y = 0; y < numCols; y++) {
                    Object value = PropertyUtils.getProperty(bean, excelColumns
                            .get(y).getMethod());
                    writeCell(dataRow, y, value, excelColumns.get(y).getType(),
                            excelColumns.get(y).getColor(), excelColumns.get(y)
                            .getFont());
                }
            }

        } catch (IllegalAccessException e) {
            System.out.println(e);
        } catch (InvocationTargetException e) {
            System.out.println(e);
        } catch (NoSuchMethodException e) {
            System.out.println(e);
        }

        Calendar now = Calendar.getInstance();
        int day = now.get(Calendar.DAY_OF_MONTH);
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);
        int second = now.get(Calendar.SECOND);
        String fileName = this.dataSheetName + "_" + day + "_" + hour + "_"
                + minute + "_" + second + "_";

        try {
            // Write the workbook in file system
            File temp = File.createTempFile(fileName, ".xlsx");
            FileOutputStream out = new FileOutputStream(temp);
            workbook.write(out);
            out.close();
            Filedownload.save(temp, null);
        } catch (Exception e) {
            System.out.println(e);

        }
    }

    private void writeCell(Row dataRow, int col, Object value,
            FormatType formatType, Short bgColor, XSSFFont font) {

        Cell dataCell;
        dataCell = dataRow.createCell(col);
        if (value == null) {
            return;
        }
//        HSSFCellStyle style = ((HSSFWorkbook)wb).createCellStyle();	
//HSSFPalette palette = ((HSSFWorkbook)wb).getCustomPalette(); 
//palette.setColorAtIndex((short)57, (byte)255, (byte)228, (byte)225); 
//style.setFillForegroundColor(palette.getColor(57).getIndex()); 
//style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND); 
//        Integer.valueOf( colorStr.substring( 1, 3 ), 16 ),
//            Integer.valueOf( colorStr.substring( 3, 5 ), 16 ),
//            Integer.valueOf( colorStr.substring( 5, 7 ), 16 ) );
        switch (formatType) {
            case TEXT:
                dataCell.setCellValue(value.toString());
                break;
            case DATE:
                dataCell.setCellStyle(getDateFormatStyle(this.workbook));
                dataCell.setCellValue((Date) value);
                break;
            case FLOAT:
                break;
            case INTEGER:
                dataCell.setCellStyle(getIntegerCellStyle(this.workbook));
                dataCell.setCellType(Cell.CELL_TYPE_NUMERIC);
                dataCell.setCellValue(((Number) value).intValue());
                break;
            case MONEY:
                dataCell.setCellStyle(getNumberCellStyle(this.workbook));
                dataCell.setCellType(Cell.CELL_TYPE_NUMERIC);
                dataCell.setCellValue(((BigDecimal) value).doubleValue());
                break;
            case PERCENTAGE:
                break;
            default:
                break;
        }

    }

    public static void createExcelHeader(XSSFWorkbook workbook,
            XSSFSheet excelSheet, String titles[]) {

        Row excelHeader = excelSheet.createRow(0);
        Cell dataCell;
        int count = 0;
        for (String caption : titles) {
            dataCell = excelHeader.createCell(count++);
            dataCell.setCellStyle(getHeaderCellStyle(workbook));
            dataCell.setCellValue(caption);
        }
    }

    public static XSSFCellStyle getHeaderCellStyle(XSSFWorkbook workbook) {
        XSSFCellStyle titleStyle = workbook.createCellStyle();

        titleStyle.setFillPattern(XSSFCellStyle.FINE_DOTS);
        titleStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        titleStyle.setAlignment(XSSFCellStyle.ALIGN_LEFT);
        titleStyle.setBorderBottom(XSSFCellStyle.BORDER_MEDIUM);
        titleStyle.setBorderLeft(XSSFCellStyle.BORDER_MEDIUM);
        titleStyle.setBorderRight(XSSFCellStyle.BORDER_MEDIUM);
        titleStyle.setBorderTop(XSSFCellStyle.BORDER_MEDIUM);

        Font headerFont = workbook.createFont();
        headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        headerFont.setFontHeightInPoints((short) 10);
        titleStyle.setFont(headerFont);

        return titleStyle;
    }

    private XSSFCellStyle getDateFormatStyle(XSSFWorkbook workbook) {
        XSSFCellStyle style = workbook.createCellStyle();
        XSSFDataFormat df = workbook.createDataFormat();
        style.setDataFormat(df.getFormat("mm/dd/yyyy"));
        return style;
    }

    private XSSFCellStyle getNumberCellStyle(XSSFWorkbook workbook) {
        XSSFCellStyle style = workbook.createCellStyle();
        XSSFDataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("0.00"));
        return style;
    }

    private XSSFCellStyle getIntegerCellStyle(XSSFWorkbook workbook) {
        XSSFCellStyle style = workbook.createCellStyle();
        XSSFDataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("#,##0"));
        return style;
    }

    public void exportExcell(Listbox lb) throws ParseException {
        List<String> color = new ArrayList<String>();
        String fonti = "";
        // Blank workbook
        this.workbook = new XSSFWorkbook();
        // Create a blank sheet
        this.sheet = this.workbook.createSheet(this.dataSheetName);
        int currentRow = 0;

        // Create the report header at row 0
        Row excelHeader = sheet.createRow(currentRow);
        Cell dataCell;
        List<Integer> lint = new ArrayList<Integer>();
        // Loop over all the column beans and populate the report headers
        List<Listheader> list = lb.getListhead().getChildren();
        int j = -1, k;
        for (int i = 0; i < list.size(); i++) {
            Listheader lh = (Listheader) list.get(i);
            if (lh.isVisible()) {
                j = j + 1;
                lint.add(lh.getColumnIndex());
                dataCell = excelHeader.createCell(i);
                dataCell.setCellStyle(getHeaderCellStyle(workbook));
                dataCell.setCellValue(lh.getLabel());
                // this.sheet.setColumnWidth(j, 3000);
            }
        }
        k = j + 1;
        int vm = k;
        currentRow++;
        Row dataRow;
        boolean b;
        List<Listitem> litem = lb.getItems();
        for (int i = 0; i < litem.size(); i++) {
            Listitem li = (Listitem) litem.get(i);
            // create a row in the spreadsheet
            b = false;
            dataRow = sheet.createRow(currentRow++);
            //   Messagebox.show(k+" "+li.getChildren().size());
            if (li.getChildren().size() < k) {
                k = 1;
                b = true;
            }
            j = -1;
            List<Listcell> llc = li.getChildren();
            for (int m = 0; m < k; m++) {
                Listcell lc = (Listcell) llc.get(lint.get(m));
                //  if (b || lc.getListheader().isVisible()) {
                if (lc.getChildren().size() > 1) {
                    j = j + 1;
                    String v = "";
                    for (Component object : lc.getChildren()) {
                        if (object instanceof Label && object.isVisible()) {
                            Label lab = (Label) object;
                            v = v + lab.getValue();
                        }
                    }
                    XSSFFont f = new XSSFFont(null);
                    writeCell(dataRow, j, v, FormatType.TEXT,
                            IndexedColors.AUTOMATIC.getIndex(), f);
                } else if (lc.getChildren().size() > 0) {
                    j = j + 1;
                    Component ob = lc.getChildren().get(0);
                    if (ob instanceof Label) {
                        Label lab = (Label) ob;
                        if (lab.getSclass() == null) {
                            XSSFFont f = new XSSFFont(null);
                            writeCell(dataRow, j, lab.getValue(), FormatType.TEXT,
                                    IndexedColors.AUTOMATIC.getIndex(), f);
                        } else if (lab.getSclass().equals("data")) {
                            Date d = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(lab.getValue());
                            XSSFFont f = new XSSFFont(null);
                            writeCell(dataRow, j, d, FormatType.DATE,
                                    IndexedColors.AUTOMATIC.getIndex(), f);

                        } else if (lab.getSclass().equals("int")) {
                            int vi = Integer.parseInt(lab.getValue());
                            XSSFFont f = new XSSFFont(null);
                            writeCell(dataRow, j, vi, FormatType.INTEGER,
                                    IndexedColors.AUTOMATIC.getIndex(), f);

                        } else if (lab.getSclass().equals("float")) {
                            float fi = Float.parseFloat(lab.getValue());
                            XSSFFont f = new XSSFFont(null);
                            writeCell(dataRow, j, fi, FormatType.FLOAT,
                                    IndexedColors.AUTOMATIC.getIndex(), f);

                        } else if (lab.getSclass().equals("double")) {
                            float fi = (float) Double.parseDouble(lab.getValue());
                            XSSFFont f = new XSSFFont(null);
                            writeCell(dataRow, j, fi, FormatType.FLOAT,
                                    IndexedColors.AUTOMATIC.getIndex(), f);
                        }
                    } else {
                        if (ob instanceof Vbox) {
                            String v = "";
                            for (Component obj : ob.getChildren()) {
                                if (obj instanceof Label && obj.isVisible()) {
                                    Label lab = (Label) obj;
                                    v = v + lab.getValue();
                                }
                            }
                            XSSFFont f = new XSSFFont(null);
                            writeCell(dataRow, j, v, FormatType.TEXT,
                                    IndexedColors.AUTOMATIC.getIndex(), f);
                        }
                    }
                }
                if (!b) {
                    this.sheet.autoSizeColumn(j, true);
                }
                //  }
            }
            if (b) {
                sheet.addMergedRegion(new CellRangeAddress(currentRow - 1, currentRow - 1, 0, vm - 1));
            }
            k = vm;
        }
        String fileName = this.dataSheetName + new SimpleDateFormat("dd_MM_yyyy_HH_mm").format(new Date());

        try {
            // Write the workbook in file system
            File temp = File.createTempFile(fileName, ".xlsx");
            FileOutputStream out = new FileOutputStream(temp);
            workbook.write(out);
            out.close();
            Filedownload.save(temp, null);
        } catch (Exception e) {
            System.out.println(e);

        }
    }
}
