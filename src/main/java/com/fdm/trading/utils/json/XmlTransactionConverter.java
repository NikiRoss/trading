package com.fdm.trading.utils.json;

import com.fdm.trading.domain.Transaction;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.List;

public class XmlTransactionConverter implements DataConverter<Transaction, Document> {
    @Override
    public Document convert(List<Transaction> dataList) {
        Document doc = null;
        try {
            DocumentBuilderFactory dFact = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dFact.newDocumentBuilder();
            doc = builder.newDocument();

            Element root = doc.createElement("Stocks");
            doc.appendChild(root);

            for(Transaction transaction: dataList){
                Element company = doc.createElement("Company");
                company.appendChild(doc.createTextNode(transaction.getStocks().getCompany()));
                root.appendChild(company);

                Element date = doc.createElement("Date");
                date.appendChild(doc.createTextNode(String.valueOf(transaction.getDate())));
                root.appendChild(date);

                Element volume = doc.createElement("Volume");
                volume.appendChild(doc.createTextNode(String.valueOf(transaction.getVolume())));
                root.appendChild(volume);

                Element price = doc.createElement("Price");
                price.appendChild(doc.createTextNode(String.valueOf(transaction.getPrice())));
                root.appendChild(price);

                Element account = doc.createElement("AccountNumber");
                account.appendChild(doc.createTextNode(String.valueOf(transaction.getAccount().getAccountNumber())));
                root.appendChild(account);

            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return doc;
    }
}
