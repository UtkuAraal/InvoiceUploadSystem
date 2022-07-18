package com.utku.invoice_upload_system.dataAccess;

import com.utku.invoice_upload_system.entity.*;

import java.sql.SQLException;
import java.util.List;

public interface IDatabaseDal {

    public List<Customer> getCustomers();
    public Customer addNewCustomer(String nameSurname, String ssNumber);
    public List<Item> getItems();
    public void addNewItem(String name, String unitPrice);
    public void addInvoice(Invoice invoice, List<InvoiceItems> items);
    public List<Invoice> getInvoices();
    public List<OutputItem> getInvoiceItems(int invoiceId);
    public Customer getCustomerFromInvoice(int customerId);
    public Invoice findInvoiceBySeriAndNumber(String seri, String number);
    public void deleteInvoice(int invoiceId) throws SQLException;
    public void closeDatabase();
}
