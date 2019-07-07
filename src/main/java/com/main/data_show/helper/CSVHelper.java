package com.main.data_show.helper;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import com.main.data_show.consts.SysConsts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class CSVHelper {
    @Autowired
    private Environment env;

    @Autowired
    private PointHelper pointHelper;

    @Autowired
    private LoginHelper loginHelper;

    @Autowired
    private PointDataHelper pointDataHelper;

    /*public static void main(String[] args) {
        readCSV();
    }*/
    public void readCSV(){
        //env.getProperty("");
        String filePath = "C:\\01work\\priv_work190415\\CSV_test\\";
        try {
            File file = new java.io.File(filePath); // 创建文件对象
            String[] filelist = file.list();
            for(String fileName : filelist){
                CsvReader csvReader = new CsvReader(filePath + "/" + fileName, ',', Charset.forName("GBK"));
                // 读表头
               // csvReader.readHeaders();
                //保存所有点信息 保持顺序
                Map<Integer,Integer> resultMap = new HashMap<Integer,Integer>();
                int pointIndexFlg = 1;
                boolean dateIndexFlg = false;
                // 读内容
                while (csvReader.readRecord()) {
                    // 读一整行
                    int length = csvReader.getRawRecord().length();
                    String result = csvReader.get(0);
                    if(result.startsWith("Point_")){
                        String pointName = csvReader.get(1);
                        System.out.println("pointName:"+pointName);
                        String[] split = pointName.split(":");
                        //保存点到数据库库 pointName不存在才创建
                        int pointId = pointHelper.savePoint(split[0], "normal", split[1], "#blockNo", SysConsts.DEF_SYS_USER_ID);
                        resultMap.put(pointIndexFlg,pointId);
                        pointIndexFlg++;
                    }
                    if(dateIndexFlg){
                        //这里是数据部门
                        String dateStr = csvReader.get(0);
                        String hourStr = csvReader.get(1);
                        int pointDataFlg = 2;
                        for(Map.Entry<Integer,Integer> map : resultMap.entrySet()){
                            int index = map.getKey();
                            int ponitId = map.getValue();
                            String ponitValue = csvReader.get(pointDataFlg);
                            pointDataFlg ++;
                            System.out.println("dateStr:"+dateStr+",hourStr:"+hourStr+"ponitId:"+ponitId+",ponitValue:"+ponitValue);
                            pointDataHelper.insertPoint(ponitId,dateStr,hourStr,ponitValue);
                        }
                    }
                    if("<>Date".equals(result)){
                        dateIndexFlg = true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    public static void writeCSV(){
        try {
            File file = new File("C:\\01work\\priv_work190415\\CSV_test\\test.csv");
            if(!file.exists()){
                file.createNewFile();
            }
            CsvWriter csvWriter = new CsvWriter(file.getCanonicalPath(), ',', Charset.forName("GBK"));
            csvWriter.writeRecord(new String[]{"张三", "aaa", "男"});
            csvWriter.writeRecord(new String[]{"李四", "bbbb", "vn"});
            csvWriter.close();

            /*response.setContentType("application/csv; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            InputStream in = new FileInputStream(file);
            OutputStream out = response.getOutputStream();
            int len;
            byte[] buffer = new byte[1024];
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
