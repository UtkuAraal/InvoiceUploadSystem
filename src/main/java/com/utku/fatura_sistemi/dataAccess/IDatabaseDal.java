package com.utku.fatura_sistemi.dataAccess;

import com.utku.fatura_sistemi.entity.*;

import java.sql.SQLException;
import java.util.List;

public interface IDatabaseDal {

    public List<customer> getCustomers();
    public customer addNewCustomer(String nameSurname, String ssNumber);
    public List<Item> getItems();
    public void addNewItem(String name, String unitPrice);
    public void addInvoice(Invoice invoice, List<InvoiceItems> items);
    public List<Invoice> getInvoices();
    public List<OutputItem> getInvoiceItems(int invoiceId);
    public customer getCustomerFromInvoice(int customerId);
    public Invoice findInvoiceBySeriAndNumber(String seri, String number);
    public void deleteInvoice(int invoiceId) throws SQLException;
    public void closeDatabase();
}
