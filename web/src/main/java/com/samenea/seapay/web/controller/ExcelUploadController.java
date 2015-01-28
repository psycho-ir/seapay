package com.samenea.seapay.web.controller;

import com.samenea.seapay.bank.service.cutoff.CutOffService;
import com.samenea.seapay.bank.utils.model.CutOffItem;
import com.samenea.seapay.web.model.UploadItem;
import com.samenea.seapay.web.model.View;
import com.samenea.seapay.web.util.SaderatExcelReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author: Soroosh Sarabadani
 * Date: 6/19/13
 * Time: 3:12 PM
 */
@Controller
public class ExcelUploadController {
    private Logger logger = LoggerFactory.getLogger(ExcelUploadController.class);

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    private CutOffService cutOffService;

    @Autowired
    private SaderatExcelReader saderatExcelReader;


    @RequestMapping("/admin/upload")
    public String showUploadPage(ModelMap modelMap) {
        modelMap.put("uploadItem", new UploadItem());
        return View.Transaction.UPLOAD_EXCEL;
    }

    @RequestMapping(value = "/admin/upload", method = RequestMethod.POST)
    public String upload(ModelMap modelMap, UploadItem uploadItem) throws IOException {


        List<String> duplicatedReferences = new ArrayList<String>();
        Integer succeedCounter = 0;
        MultipartFile filea = uploadItem.getFileData();

        InputStream inputStream;
        OutputStream outputStream;
        final UUID uuid = UUID.randomUUID();
        if (filea.getSize() > 0) {
            inputStream = filea.getInputStream();
            File file = new File(uploadPath + "/" + filea.getOriginalFilename() + "-" + uuid);
            outputStream = new FileOutputStream(file);
            int readBytes;
            byte[] buffer = new byte[8192];
            while ((readBytes = inputStream.read(buffer, 0, 8192)) != -1) {
                outputStream.write(buffer, 0, readBytes);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            logger.info("File:{} uploaded.", filea.getOriginalFilename());

        }
        final List<CutOffItem> cutOffItems = saderatExcelReader.convert(uploadPath + "/" + filea.getOriginalFilename() + "-" + uuid);
        for (CutOffItem item : cutOffItems) {

            final Boolean result = cutOffService.saveCutOffItem(item);
            if (result) {
                succeedCounter++;
            } else {
                logger.info("Cutoff Item with Reference Code:{} is duplicated. so was not uploaded.", item.getReferNumber());
                duplicatedReferences.add(item.getReferNumber());
            }
            modelMap.put("succeed", succeedCounter);
            modelMap.put("duplicates", duplicatedReferences);

        }
        return View.Transaction.UPLOAD_RESULT;


    }
}

