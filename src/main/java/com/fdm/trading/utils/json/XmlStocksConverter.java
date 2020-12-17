package com.fdm.trading.utils.json;

import com.fdm.trading.domain.Stocks;
import com.fdm.trading.domain.Transaction;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.List;

public class XmlStocksConverter implements DataConverter<Stocks, Document> {

    @Override
    public Document convert(List<Stocks> dataList) {
        Document doc = null;
        try {
            DocumentBuilderFactory dFact = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dFact.newDocumentBuilder();
            doc = builder.newDocument();

            Element root = doc.createElement("Stocks");
            doc.appendChild(root);

            for(Stocks stocks: dataList){
                Element company = doc.createElement("Company");
                company.appendChild(doc.createTextNode(stocks.getCompany()));
                root.appendChild(company);

                Element date = doc.createElement("Share Price");
                date.appendChild(doc.createTextNode(String.valueOf(stocks.getSharePrice())));
                root.appendChild(date);

                Element volume = doc.createElement("Volume");
                volume.appendChild(doc.createTextNode(String.valueOf(stocks.getVolume())));
                root.appendChild(volume);

                Element price = doc.createElement("Opening Value");
                price.appendChild(doc.createTextNode(String.valueOf(stocks.getOpeningValue())));
                root.appendChild(price);

                Element account = doc.createElement("Closing Value");
                account.appendChild(doc.createTextNode(String.valueOf(stocks.getClosingValue())));
                root.appendChild(account);

            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return doc;
    }
}
