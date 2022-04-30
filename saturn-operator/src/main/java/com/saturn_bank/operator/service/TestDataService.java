package com.saturn_bank.operator.service;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public interface TestDataService {
    void populateDB()  throws ParserConfigurationException, IOException, SAXException;
}
