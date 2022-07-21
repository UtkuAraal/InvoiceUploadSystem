package com.utku.invoice_upload_system;

import com.utku.invoice_upload_system.dataAccess.IDatabaseDal;
import com.utku.invoice_upload_system.dataAccess.SqliteDatabase;

import java.text.DecimalFormat;

public class Statics {
    public static final DecimalFormat decimalFormat =new DecimalFormat("#.##");
    public static final IDatabaseDal database = new SqliteDatabase();;
}
