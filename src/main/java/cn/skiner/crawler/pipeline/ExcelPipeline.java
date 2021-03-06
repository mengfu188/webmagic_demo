package cn.skiner.crawler.pipeline;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @创建人 skiner
 * @创建时间 2018/8/3
 * @描述
 */
public class ExcelPipeline implements Pipeline {

    private static HSSFWorkbook WB;

    private static HSSFSheet SHEET;

    private static String FILE;

    public ExcelPipeline(String file){
        this.FILE = file;
    }

    private static AtomicInteger NUM = new AtomicInteger();

    private static AtomicBoolean FIRST = new AtomicBoolean(true);

    static{
        WB = new HSSFWorkbook();
        SHEET = WB.createSheet("sheet0");
    }



    @Override
    public void process(ResultItems resultItems, Task task) {
        Map<String,Object> map = resultItems.getAll();

        if(FIRST.get()){
            synchronized (this){
                if(FIRST.get()){
                    FIRST.set(false);
                    HSSFRow row = SHEET.createRow(NUM.getAndIncrement());
                    AtomicInteger i = new AtomicInteger();
                    map.forEach((k,v)->{
                        HSSFCell cell = row.createCell(i.getAndIncrement());
                        cell.setCellValue(k.toString());
                    });
                }
            }
        }

        HSSFRow row = SHEET.createRow(NUM.getAndIncrement());

        AtomicInteger i = new AtomicInteger();

        map.forEach((k,v)->{
            HSSFCell cell = row.createCell(i.getAndIncrement());
            cell.setCellValue(v.toString());
        });

//        if(i.get()%50==0){
//            synchronized (this){
//                if(i.get()%50==0){
//                    flush();
//                }
//            }
//        }

    }

    public static void flush(){
        try {
            WB.write(new File(FILE));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
