package com.example.demo.service;

import com.example.demo.dto.ActualDto;
import com.example.demo.dto.CustomerDto;
import com.example.demo.dto.PriceDto;
import com.example.demo.dto.ProductDto;
import com.example.demo.entity.Actual;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Price;
import com.example.demo.entity.Product;
import com.example.demo.mapper.ReflectionMapper;
import com.example.demo.repository.ActualRepository;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.PriceRepository;
import com.example.demo.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ExcelParser {

    private final PriceRepository priceRepository;
    private final ActualRepository actualRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;


    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public ExcelParser(PriceRepository priceRepository,
                       ActualRepository actualRepository,
                       ProductRepository productRepository,
                       CustomerRepository customerRepository) {
        this.priceRepository = priceRepository;
        this.actualRepository = actualRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }
    
    @PostConstruct
    private void initialize(){
        parseAndSave("src/main/resources/BackendDeveloper.xlsx");
    } 


    public void parseAndSave(String filePath) {
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fis)) {


            parsePrice(workbook.getSheet("Price"));
            parseStores(workbook.getSheet("Customers"));
            parseProducts(workbook.getSheet("Products"));
            parseactualDtos(workbook.getSheet("Actuals"));

            System.out.println("Успешно распарсено и сохранено в БД");

        } catch (IOException e) {
            System.err.println("Ошибка чтения Excel: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void parsePrice(Sheet sheet) {
        if (sheet == null) {
            System.err.println("Price не обнаружено");
            return;
        }

        Iterator<Row> rowIterator = sheet.iterator();
        if (rowIterator.hasNext()) {
            rowIterator.next();
        }

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            try {
                PriceDto priceDto = new PriceDto();

                String chainName = getCellValueAsString(row.getCell(0));
                priceDto.setChain_name(chainName);

                Integer materialNo = getCellValueAsInteger(row.getCell(1));
                priceDto.setMaterial_No(materialNo);

                BigDecimal regularPrice = getCellValueAsBigDecimal(row.getCell(2));
                priceDto.setRegular_price_per_unit(regularPrice);

                Price price = ReflectionMapper.toEntity(priceDto, Price.class);

                priceRepository.save(price);

            } catch (Exception e) {
                System.err.println("Не получилось распарсить строку: " + row.getRowNum() + " - " + e.getMessage());
            }
        }
    }

    private void parseStores(Sheet sheet) {
        if (sheet == null) {
            System.err.println("Customers не обнаружено");
            return;
        }

        Iterator<Row> rowIterator = sheet.iterator();
        if (rowIterator.hasNext()) {
            rowIterator.next();
        }

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            try {
                CustomerDto customerDto = new CustomerDto();

                Integer shipToCode = getCellValueAsInteger(row.getCell(0));
                customerDto.setCH3_Ship_To_Code(shipToCode);

                String shipToName = getCellValueAsString(row.getCell(1));
                customerDto.setCH3_Ship_To_Name(shipToName);

                String chainName = getCellValueAsString(row.getCell(2));
                customerDto.setChain_name(chainName);

                Customer customer = ReflectionMapper.toEntity(customerDto, Customer.class);
                customerRepository.save(customer);

            } catch (Exception e) {
                System.err.println("Не получилось распарсить строку =: " + row.getRowNum() + " - " + e.getMessage());
            }
        }
    }
    
    private void parseProducts(Sheet sheet) {
        if (sheet == null) {
            System.err.println("Products не обнаружено");
            return;
        }

        Iterator<Row> rowIterator = sheet.iterator();

        if (rowIterator.hasNext()) {
            rowIterator.next();
        }

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            try {
                ProductDto productDto = new ProductDto();
                
                Integer materialNo = getCellValueAsInteger(row.getCell(0));
                productDto.setMaterial_No(materialNo);
                
                String description = getCellValueAsString(row.getCell(1));
                productDto.setMaterial_Desc_RUS(description);
                
                Integer categoryCode = getCellValueAsInteger(row.getCell(2));
                productDto.setL3_Product_Category_Code(categoryCode);
                
                String categoryName = getCellValueAsString(row.getCell(3));
                productDto.setL3_Product_Category_Name(categoryName);

                Product product = ReflectionMapper.toEntity(productDto, Product.class);
                productRepository.save(product);

            } catch (Exception e) {
                System.err.println("Не получилось распарсить строку: " + row.getRowNum() + " - " + e.getMessage());
            }
        }
    }


    private void parseactualDtos(Sheet sheet) {
        if (sheet == null) {
            System.err.println("Actuals не обнаружено");
            return;
        }

        Iterator<Row> rowIterator = sheet.iterator();
        if (rowIterator.hasNext()) {
            rowIterator.next();
        }

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            try {
                ActualDto actualDto = new ActualDto();
                
                Date date = getCellValueAsDate(row.getCell(0));
                actualDto.setDate(new java.sql.Date(date.getTime()));
                
                Integer materialNo = getCellValueAsInteger(row.getCell(1));
                actualDto.setMaterial_No(materialNo);
                
                Integer shipToCode = getCellValueAsInteger(row.getCell(2));
                actualDto.setCH3_Ship_To_Code(shipToCode);
                
                Integer volume = getCellValueAsInteger(row.getCell(3));
                actualDto.setVolume_units(volume);
                
                BigDecimal actualDtosValue = getCellValueAsBigDecimal(row.getCell(4));
                actualDto.setActual_Sales_Value(actualDtosValue);

                Actual actual = ReflectionMapper.toEntity(actualDto, Actual.class);
                actualRepository.save(actual);

            } catch (Exception e) {
                System.err.println("Не получилось распарсить строку: " + row.getRowNum() + " - " + e.getMessage());
            }
        }
    }
    
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return null;
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return dateFormat.format(cell.getDateCellValue());
                }
                return String.valueOf((long)cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                switch (cell.getCachedFormulaResultType()) {
                    case STRING:
                        return cell.getStringCellValue();
                    case NUMERIC:
                        return String.valueOf((long)cell.getNumericCellValue());
                    default:
                        return "";
                }
            default:
                return "";
        }
    }

    private BigDecimal getCellValueAsBigDecimal(Cell cell) {
        if (cell == null) {
            return null;
        }

        switch (cell.getCellType()) {
            case STRING:
                try {
                    return new BigDecimal(cell.getStringCellValue().replace(",", "."));
                } catch (NumberFormatException e) {
                    return null;
                }
            case NUMERIC:
                return BigDecimal.valueOf(cell.getNumericCellValue());
            case FORMULA:
                return BigDecimal.valueOf(cell.getNumericCellValue());
            default:
                return null;
        }
    }

    private Integer getCellValueAsInteger(Cell cell) {
        if (cell == null) {
            return null;
        }

        switch (cell.getCellType()) {
            case STRING:
                try {
                    return Integer.parseInt(cell.getStringCellValue());
                } catch (NumberFormatException e) {
                    return null;
                }
            case NUMERIC:
                return (int)cell.getNumericCellValue();
            case FORMULA:
                return (int)cell.getNumericCellValue();
            default:
                return null;
        }
    }

    private Date getCellValueAsDate(Cell cell) {
        if (cell == null) {
            return null;
        }

        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            return cell.getDateCellValue();
        } else if (cell.getCellType() == CellType.STRING) {
            try {
                return dateFormat.parse(cell.getStringCellValue());
            } catch (ParseException e) {
                return null;
            }
        }

        return null;
    }
}