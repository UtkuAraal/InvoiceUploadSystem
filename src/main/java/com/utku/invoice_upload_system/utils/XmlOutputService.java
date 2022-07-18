package com.utku.invoice_upload_system.utils;

import com.utku.invoice_upload_system.entity.Invoice;
import com.utku.invoice_upload_system.entity.OutputItem;
import com.utku.invoice_upload_system.entity.Customer;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.List;

public class XmlOutputService {


    public void XMLOutputFile(Invoice invoice, List<OutputItem> items, Customer customerInfo){
        String xmlFilePath = invoice.getNumber() + "xmlfile.xml";
        try {

            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

            Document document = documentBuilder.newDocument();


            Element root = document.createElement("uploadSystem");
            document.appendChild(root);

            // employee element
            Element customer = document.createElement("customer");

            root.appendChild(customer);

            // set an attribute to staff element
            Attr attr = document.createAttribute("type");
            attr.setValue("SAHIS");
            customer.setAttributeNode(attr);

            //you can also use staff.setAttribute("id", "1") for this

            // firstname element
            Element name = document.createElement("name");
            name.appendChild(document.createTextNode(customerInfo.getNameSurname()));
            customer.appendChild(name);

            // lastname element
            Element ssnNumber = document.createElement("ssnNumber");
            ssnNumber.appendChild(document.createTextNode(customerInfo.getSsNumber()));
            customer.appendChild(ssnNumber);

            // email element
            Element invoiceData = document.createElement("invoiceData");
            invoiceData.setAttribute("seri", invoice.getSeri());
            invoiceData.setAttribute("number", invoice.getNumber());
            root.appendChild(invoiceData);

            for(OutputItem item : items){
                Element itemElement = document.createElement("item");
                invoiceData.appendChild(itemElement);

                Element itemName = document.createElement("name");
                itemName.appendChild(document.createTextNode(item.getName()));
                itemElement.appendChild(itemName);

                Element quantity = document.createElement("quantity");
                quantity.appendChild(document.createTextNode(String.valueOf(item.getQuantity())));
                itemElement.appendChild(quantity);

                Element unitPrice = document.createElement("unitPrice");;
                unitPrice.appendChild(document.createTextNode(item.getUnitPrice()));
                itemElement.appendChild(unitPrice);

                Element amount = document.createElement("amount");;
                amount.appendChild(document.createTextNode(String.valueOf(item.getAmount())));
                itemElement.appendChild(amount);
            }

            Element totalAmount = document.createElement("totalAmount");
            totalAmount.appendChild(document.createTextNode(String.valueOf(invoice.getTotalAmount())));
            invoiceData.appendChild(totalAmount);

            Element discount = document.createElement("discount");
            discount.appendChild(document.createTextNode(String.valueOf(invoice.getDiscount())));
            invoiceData.appendChild(discount);

            Element amountToPay = document.createElement("amountToPay");
            amountToPay.appendChild(document.createTextNode(String.valueOf(invoice.getAmountToPay())));
            invoiceData.appendChild(amountToPay);


            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(xmlFilePath));

            transformer.transform(domSource, streamResult);

            System.out.println("Done creating XML File");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
