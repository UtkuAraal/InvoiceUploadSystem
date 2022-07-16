package com.utku.fatura_sistemi.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.utku.fatura_sistemi.entity.Invoice;
import com.utku.fatura_sistemi.entity.OutputItem;
import com.utku.fatura_sistemi.entity.customer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JsonOutputService {


    public void JSONOutputFile(Invoice invoice, List<OutputItem> items, customer customerInfo) throws IOException {
        JSONObject jsonObject = new JSONObject();
        JSONObject uploadSystem = new JSONObject();

        JSONObject customer = new JSONObject();
        customer.put("name", customerInfo.getNameSurname());
        customer.put("ssnNumber", Long.valueOf(customerInfo.getSsNumber()));

        uploadSystem.put("customer", customer);

        JSONObject invoiceData = new JSONObject();
        invoiceData.put("seri", invoice.getSeri());
        invoiceData.put("number", invoice.getNumber());

        JSONArray invoiceItems = new JSONArray();

        for(OutputItem item: items){
            JSONObject invoiceItem = new JSONObject();
            invoiceItem.put("name", item.getName());
            invoiceItem.put("quantity", item.getQuantity());
            invoiceItem.put("unitPrice", Integer.parseInt(item.getUnitPrice()));
            invoiceItem.put("amount", item.getAmount());
            invoiceItems.add(invoiceItem);
        }
        invoiceData.put("item", invoiceItems);
        invoiceData.put("totalAmount", invoice.getTotalAmount());
        invoiceData.put("discount", invoice.getDiscount());
        invoiceData.put("amountToPay", invoice.getAmountToPay());




        uploadSystem.put("invoiceData" ,invoiceData);
        jsonObject.put("uploadSystem", uploadSystem);

        FileWriter file = new FileWriter(invoice.getNumber() + "jsonfile.json");
        file.write(jsonObject.toJSONString());
        file.close();
    }
}
