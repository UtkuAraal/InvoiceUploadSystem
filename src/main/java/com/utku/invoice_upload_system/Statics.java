package com.utku.invoice_upload_system;

import com.utku.invoice_upload_system.dataAccess.IDatabaseDal;
import com.utku.invoice_upload_system.dataAccess.SqliteDatabase;
import com.utku.invoice_upload_system.entity.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Statics {
    public static final DecimalFormat decimalFormat =new DecimalFormat("#.##");
    public static final IDatabaseDal database = new SqliteDatabase();
    public static Invoice invoice = null;
    public static Customer customer = null;
    public static List<InvoiceItems> invoiceItemsList = new ArrayList<>();
    public static CartItem cartItem = null;
    public static List<OutputItem> outputItems = null;
    public static String Type;
    public static String Value;
}
