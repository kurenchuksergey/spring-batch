package com.kurenchuksergey.springbatch.jobs;

import com.kurenchuksergey.springbatch.entity.Car;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class RowItemProcessor implements ItemProcessor<XSSFRow, Car> {
    @Override
    public Car process(XSSFRow item) throws InterruptedException {
        String carName = item.getCell(0).getStringCellValue();
        double price = item.getCell(1).getNumericCellValue();
        return new Car(carName, price);
    }
}
