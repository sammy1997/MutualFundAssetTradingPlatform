package com.mutualfundtrading.fundhandling.utils;
import com.mutualfundtrading.fundhandling.dao.EntitlementDAO;
import com.mutualfundtrading.fundhandling.dao.FundDAO;
import com.mutualfundtrading.fundhandling.models.EntitlementParser;
import com.mutualfundtrading.fundhandling.models.FundParser;
import com.mutualfundtrading.fundhandling.models.ImmutableFundParser;
import com.mutualfundtrading.fundhandling.services.FundServiceModel;
import org.apache.commons.codec.binary.Base64;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import javax.ws.rs.core.Response;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ServiceUtils {
    public static String BASE_URL = "http://localhost:8762/create/";
    private static String FILE_PATH = "C:\\Users\\somchakr\\Desktop\\upload\\";
    public static String decodeJWTForUserId(String jwtToken) {

        String[] split_string = jwtToken.split("\\.");
        String base64EncodedBody = split_string[1];

        Base64 base64Url = new Base64(true);

        String body = new String(base64Url.decode(base64EncodedBody));
        String userId = null;
        try {
            userId = body.split(",")[0].split(":")[1];
            userId = userId.substring(1, userId.length()-1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userId;
    }

    private ArrayList<ArrayList<String>> readCsvFile(String fileName) {
        try {
            FileInputStream excelFile = new FileInputStream(new File(FILE_PATH + fileName));

            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = datatypeSheet.iterator();

            ArrayList<ArrayList<String>> rows = new ArrayList<>();
            int count = 0;
            while (iterator.hasNext()) {
                Row currentRow = iterator.next();
                Iterator<Cell> cellIterator = currentRow.iterator();
                ArrayList<String> row = new ArrayList<>();
                count++;
                // Ignoring first 2 rows for header space in excel file
                if(count < 2) {
                    continue;
                }
                while (cellIterator.hasNext()) {
                    Cell currentCell = cellIterator.next();

                    if (currentCell.getCellTypeEnum() == CellType.STRING) {
                        row.add(currentCell.getStringCellValue());
                    } else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                        row.add(currentCell.getNumericCellValue() + "");
                    }
                }
                rows.add(row);
            }

            return rows;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Response addFundsFromCSV(FormDataContentDisposition fileName) {
        ArrayList<ArrayList<String>> rows = readCsvFile(fileName.getFileName());
        FundDAO dao = new FundDAO();
        if (rows == null) {
            return Response.status(422).entity("Cannot process the provided file. Ensure the format and rows are correct")
                    .build();
        }
        try {
            for (ArrayList<String> row:rows) {
                if (row.size() != 8) {
                    continue;
                }
                FundParser fund = ImmutableFundParser.builder()
                        .fundNumber(String.valueOf((int)Double.parseDouble(row.get(0))))
                        .fundName(row.get(1))
                        .invManager(row.get(2))
                        .setCycle((int)Double.parseDouble(row.get(3)))
                        .nav(Float.parseFloat(row.get(4)))
                        .invCurrency(row.get(5))
                        .sAndPRating(Float.parseFloat(row.get(6)))
                        .moodysRating(Float.parseFloat(row.get(7)))
                        .build();
                boolean status = dao.insert(fund);
                if (!status) {
                    dao.update(fund);
                }
            }
        }catch (Exception e) {
            return Response.status(422).entity("Some data formats are wrong in the provided file").build();
        }
        return Response.status(200).entity("Funds successfully added").build();
    }

    public Response addEntitlementsFromCSV(FormDataContentDisposition fileName) {
        ArrayList<ArrayList<String>> rows = readCsvFile(fileName.getFileName());
        EntitlementDAO dao = new EntitlementDAO();
        if (rows == null) {
            return Response.status(422).entity("Cannot process the provided file. Ensure the format and rows are correct")
                    .build();
        }

        try {
            for (ArrayList<String> row:rows) {
                if (row.size()<2) {
                    continue;
                }
                ArrayList<String> entitledTo = new ArrayList<>();
                for (int i =1; i<row.size(); i++) {
                    entitledTo.add(String.valueOf((int)Double.parseDouble(row.get(i))));
                }
                dao.insert(row.get(0), entitledTo);
            }
        } catch (Exception e) {
            return Response.status(422).entity("Some data formats are wrong in the provided file").build();
        }
        return Response.status(200).entity("Entitlements successfully added").build();
    }

    public int fileUpload(InputStream fileInputStream, FormDataContentDisposition fileMetaData) {
        try {
            int read = 0;
            byte[] bytes = new byte[1024];
            String fileName = fileMetaData.getFileName();

            if (!(fileName.contains("xlx") || fileName.contains("xlsx")  || fileName.contains("csv"))) {
                return 404;
            }

            OutputStream out = new FileOutputStream(new File(FILE_PATH + fileName));

            while ((read = fileInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }

            out.flush();
            out.close();
            return 200;
        } catch (IOException e) {
            return 422;
        }
    }

    public static List<String> checkFunds(FundServiceModel fundService, EntitlementParser entitlement){
        List<String> temp = new ArrayList<>();
        for (String fundId : entitlement.entitledTo().get()) {
            if (fundService.getFund(fundId) != null) {
                temp.add(fundId);
            }
        }
        return temp;
    }
}
