package com.utku.invoice_upload_system.dataAccess;

import com.utku.invoice_upload_system.entity.*;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqliteDatabase implements IDatabaseDal{
    public Connection databaseLink;
    public Statement statement;

    public SqliteDatabase(){
        String dbName = "upload_system.db";
        String url = "jdbc:sqlite:upload_system.db";

        try{


            File file = new File ("upload_system.db");

            if(file.exists()) //here's how to check
            {
                System.out.println("This database already exists.");
                databaseLink = DriverManager.getConnection(url);
                statement = databaseLink.createStatement();

            }
            else{
                System.out.println("Creating database!");
                databaseLink = DriverManager.getConnection(url);
                statement = databaseLink.createStatement();
                String createCustomersql = "CREATE TABLE `customer` (" +
                        "`id`INTEGER," +
                        "`name`TEXT," +
                        "`ssnNumber`TEXT," +
                        "PRIMARY KEY(`id`)" +
                        ")";

                String createInvoicesql = "CREATE TABLE 'invoice' (" +
                        "`id`INTEGER," +
                        "`seri`TEXT," +
                        "`number`NUMERIC," +
                        "`totalAmount`TEXT," +
                        "`discount`TEXT," +
                        "`amountToPay`TEXT," +
                        "`customerId`INTEGER," +
                        "PRIMARY KEY(`id`)" +
                        ")";

                String createInvoiceItemssql = "CREATE TABLE `invoiceItems` (" +
                        "`invoiceId`INTEGER," +
                        "`itemId`INTEGER," +
                        "`quantity`INTEGER," +
                        "`amount`TEXT" +
                        ")";

                String createItemsql = "CREATE TABLE `item` (" +
                        "`id`INTEGER," +
                        "`name`TEXT," +
                        "`unitPrice`TEXT," +
                        "PRIMARY KEY(`id`)" +
                        ")";

                statement.executeUpdate(createCustomersql);
                statement.executeUpdate(createInvoicesql);
                statement.executeUpdate(createInvoiceItemssql);
                statement.executeUpdate(createItemsql);
                statement.close();

            }
            System.out.println("Connected");

        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public List<Customer> getCustomers(){
        List<Customer> customerList = new ArrayList<>();
        try{
            Statement statement = databaseLink.createStatement();
            ResultSet rs = statement.executeQuery( "SELECT * FROM customer" );



            while ( rs.next() ) {
                Customer cust = new Customer();
                int id = rs.getInt("id");
                cust.setId(id);
                String name = rs.getString("name");
                cust.setNameSurname(name);
                String ssn = rs.getString("ssnNumber");
                cust.setSsNumber(ssn);
                customerList.add(cust);

            }
            rs.close();
            statement.close();


        }catch (Exception e){
            e.printStackTrace();
        }
        return customerList;
    }

    public Customer addNewCustomer(String nameSurname, String ssNumber){

        try{
            String sql = "INSERT INTO customer (name, ssnNumber) VALUES(?, ?)";
            int generatedKey = 0;

            PreparedStatement ps = null;
            ps = databaseLink.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, nameSurname);
            ps.setString(2, ssNumber);
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                generatedKey = rs.getInt(1);
            }
            return new Customer(generatedKey, nameSurname, ssNumber);

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;

    }

    public List<Item> getItems() {
        List<Item> itemList = new ArrayList<>();
        try{
            Statement statement = databaseLink.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM item");



            while (rs.next()) {
                Item item = new Item();
                int id = rs.getInt("id");
                item.setId(id);
                String name = rs.getString("name");
                item.setName(name);
                String unitPrice = rs.getString("unitPrice");
                item.setUnitPrice(Double.parseDouble(unitPrice));
                itemList.add(item);

            }
            rs.close();
            statement.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        return itemList;
    }

    public void addNewItem(String name, String unitPrice) {
        try{
            String sql = "INSERT INTO item (name, unitPrice) VALUES(?, ?)";

            PreparedStatement ps = databaseLink.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, unitPrice);
            ps.execute();
        }catch (Exception e){
            System.out.println(e);
        }


    }

    public void addInvoice(Invoice invoice, List<InvoiceItems> items) {
        try{
            String sql = "INSERT INTO invoice (seri, number, totalAmount, discount, amountToPay, customerId) VALUES(?, ?, ?, ?, ?, ?)";
            int generatedKey = 0;

            PreparedStatement ps = null;
            ps = databaseLink.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, invoice.getSeri());
            ps.setString(2, invoice.getNumber());
            ps.setString(3, String.valueOf(invoice.getTotalAmount()));
            ps.setString(4, String.valueOf(invoice.getDiscount()));
            ps.setString(5, String.valueOf(invoice.getAmountToPay()));
            ps.setInt(6, invoice.getCustomerId());
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                generatedKey = rs.getInt(1);
            }


            String itemSql = "INSERT INTO invoiceItems (invoiceId, itemId, quantity, amount) VALUES(?, ?, ?, ?)";
            for(InvoiceItems invoiceItem : items){
                ps = databaseLink.prepareStatement(itemSql);
                ps.setInt(1, generatedKey);
                ps.setInt(2, invoiceItem.getItemId());
                ps.setInt(3, invoiceItem.getQuantity());
                ps.setString(4, String.valueOf(invoiceItem.getAmount()));
                ps.execute();
            }

        }catch (Exception e){
            e.printStackTrace();
        }



    }

    public List<Invoice> getInvoices(){
        List<Invoice> invoiceList = new ArrayList<>();
        try{
            Statement statement = databaseLink.createStatement();
            ResultSet rs = statement.executeQuery( "SELECT * FROM invoice" );



            while ( rs.next() ) {
                Invoice invoice = new Invoice();
                int id = rs.getInt("id");
                invoice.setId(id);
                String seri = rs.getString("seri");
                invoice.setSeri(seri);
                String number = rs.getString("number");
                invoice.setNumber(number);
                String totalAmount = rs.getString("totalAmount");
                invoice.setTotalAmount(Double.parseDouble(totalAmount));
                String discount = rs.getString("discount");
                invoice.setDiscount(Double.parseDouble(discount));
                String amountToPay = rs.getString("amountToPay");
                invoice.setAmountToPay(Double.parseDouble(amountToPay));
                int customerId = rs.getInt("customerId");
                invoice.setCustomerId(customerId);
                invoiceList.add(invoice);

            }
            rs.close();
            statement.close();

            return invoiceList;
        }catch (Exception e){
            e.printStackTrace();
        }
        return invoiceList;

    }

    public List<OutputItem> getInvoiceItems(int invoiceId){
        List<OutputItem> itemList = new ArrayList<>();
        try{
            Statement statement = databaseLink.createStatement();
            ResultSet rs = statement.executeQuery( "SELECT item.name, item.unitPrice, invoiceItems.quantity, invoiceItems.amount FROM invoiceItems INNER JOIN item ON invoiceItems.itemId = item.id WHERE invoiceItems.invoiceId = '" + invoiceId + "'");



            while ( rs.next() ) {
                OutputItem item = new OutputItem();
                String name = rs.getString("name");
                item.setName(name);
                String unitPrice = rs.getString("unitPrice");
                item.setUnitPrice(unitPrice);
                int quantity = rs.getInt("quantity");
                item.setQuantity(quantity);
                double amount = rs.getDouble("amount");
                item.setAmount(amount);
                itemList.add(item);

            }
            rs.close();
            statement.close();

            return itemList;
        }catch (Exception e){
            e.printStackTrace();
        }
        return itemList;
    }

    public Customer getCustomerFromInvoice(int customerId){
        Customer cust = new Customer();
        try{
            Statement statement = databaseLink.createStatement();
            ResultSet rs = statement.executeQuery( "SELECT * FROM customer WHERE id = '" + customerId + "'");



            while ( rs.next() ) {
                int id = rs.getInt("id");
                cust.setId(id);
                String name = rs.getString("name");
                cust.setNameSurname(name);
                String ssNumber = rs.getString("ssnNumber");
                cust.setSsNumber(ssNumber);
            }
            rs.close();
            statement.close();

            return cust;
        }catch (Exception e){
            e.printStackTrace();
        }
        return cust;
    }

    public Invoice findInvoiceBySeriAndNumber(String seri, String number){
        Invoice invoice = new Invoice();
        try{
            Statement statement = databaseLink.createStatement();
            ResultSet rs = statement.executeQuery( "SELECT * FROM invoice WHERE seri = '" + seri + "' AND number = '" + number + "'");

            while ( rs.next() ) {
                int id = rs.getInt("id");
                invoice.setId(id);
                String seriNo = rs.getString("seri");
                invoice.setSeri(seriNo);
                String numberNo = rs.getString("number");
                invoice.setNumber(numberNo);
                String totalAmount = rs.getString("totalAmount");
                invoice.setTotalAmount(Double.parseDouble(totalAmount));
                String discount = rs.getString("discount");
                invoice.setDiscount(Double.parseDouble(discount));
                String amountToPay = rs.getString("amountToPay");
                invoice.setAmountToPay(Double.parseDouble(amountToPay));
                int customerId = rs.getInt("customerId");
                invoice.setCustomerId(customerId);
            }
            rs.close();
            statement.close();

            return invoice;
        }catch (Exception e){
            e.printStackTrace();
        }
        return invoice;
    }

    public void deleteInvoice(int invoiceId){
        try{
            Statement statement = databaseLink.createStatement();
            String deleteInvoiceSql = "DELETE FROM invoice WHERE id = '" + invoiceId + "'";
            statement.executeUpdate(deleteInvoiceSql);

            String deleteInvoiceItemSql = "DELETE FROM invoiceItems WHERE invoiceId = '" + invoiceId + "'";
            statement.executeUpdate(deleteInvoiceItemSql);
            statement.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void closeDatabase(){
        try{
            databaseLink.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }




}
