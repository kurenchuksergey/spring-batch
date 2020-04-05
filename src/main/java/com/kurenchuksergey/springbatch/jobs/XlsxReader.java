package com.kurenchuksergey.springbatch.jobs;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;


@Component
@StepScope
public class XlsxReader implements ItemReader<XSSFRow> {

        private int rowNumber = 0;
        private int sheetNumber = 0;

        @Value("#{jobParameters['path']}")
        private String path;
        private XSSFWorkbook sheets;

        @PostConstruct
        public void init() throws IOException {
            var file = Path.of(path).toFile();
            sheets = new XSSFWorkbook(new FileInputStream(file));
        }

        @Override
        public XSSFRow read() {
            if (sheets.getNumberOfSheets() <= sheetNumber) {
                return null;
            }

            var sheet = sheets.getSheetAt(sheetNumber);
            if (sheet.getLastRowNum() <= rowNumber) {
                sheetNumber++;
                rowNumber = 0;
                return read();
            }
            return sheet.getRow(rowNumber++);
        }
}
