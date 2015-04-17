package External;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


class ResponseParser {

    public HashMap responseMap;
    
    public void parseLoginRequest(String loginResponse) {
        LoginResponseHandler handler = new LoginResponseHandler();
        parseStringWithHandler(loginResponse, handler);
    }
    
    public void parseGetBooksRequest(String getBookResponse) {
        GetBooksForUserResponseHandler handler = new GetBooksForUserResponseHandler();
        parseStringWithHandler(getBookResponse, handler);
    }
    
    public void parseGetBooksByIdRequest(String getBookResponse) {
        GetBooksByIdResponseHandler handler = new GetBooksByIdResponseHandler();
        parseStringWithHandler(getBookResponse, handler);
    }
    
    private void parseStringWithHandler(String xmlString, SAXHandler handler) {
        try {
            SAXParserFactory parserFactor = SAXParserFactory.newInstance();
            SAXParser parser = parserFactor.newSAXParser();
            InputSource inputSource = new InputSource( new StringReader( xmlString ) );
            parser.parse(inputSource, handler);
            responseMap = handler.responseMap;
        }
        catch (SAXException e) {
            System.out.println("No SAX for you tonight");
        }
        catch (ParserConfigurationException e) {
            System.out.println("I said I'm not having SAX with you");
        }
        catch (IOException e) {
            System.out.println("That's still a no");
        }
    }
}


class LoginResponseHandler extends SAXHandler {
    @Override
    public void startElement(String uri, String localName,
                             String qName, Attributes attributes)
    throws SAXException {
        if(localName == "error") {
            String errorCode = attributes.getValue("msg");
            responseMap.put("errorCode",errorCode);
        }
    }
    
    @Override
    public void endElement(String uri, String localName,
                           String qName) throws SAXException {
        if(localName == "session") {
            responseMap.put("session",content);
        }
    }
}

class GetBooksForUserResponseHandler extends SAXHandler {
    public HashMap[] booksArray;
    public HashMap oneBookHashmap;
    public int currentBook = 0;
    
    @Override
    public void startElement(String uri, String localName,
                             String qName, Attributes attributes)
    throws SAXException {
        if(localName == "keys") {
            booksArray = new HashMap[100];
        }
        else if(localName == "key") {
            oneBookHashmap = new HashMap();
        }
    }
    
    
    @Override
    public void endElement(String uri, String localName,
                           String qName) throws SAXException {
        if(localName == "code") {
            oneBookHashmap.put("code",content);
        }
        else if(localName == "cms") {
            oneBookHashmap.put("cms",content);
        }
        else if(localName == "key") {
            booksArray[currentBook] = oneBookHashmap;
            currentBook += 1;
        }
        else if(localName == "keys") {
            responseMap.put("books",booksArray);
        }
    }
}

class GetBooksByIdResponseHandler extends GetBooksForUserResponseHandler {
    
    public String[] chunksArray;
    public int currentChunk = 0;
    
    @Override
    public void startElement(String uri, String localName,
                             String qName, Attributes attributes)
    throws SAXException {
        if(localName == "ebooks") {
            booksArray = new HashMap[100];
        }
        else if(localName == "ebook") {
            oneBookHashmap = new HashMap();
        }
        else if(localName == "urlChunks") {
            chunksArray = new String[100];
            currentChunk = 0;
        }
    }
    
    
    @Override
    public void endElement(String uri, String localName,
                           String qName) throws SAXException {
        if(localName == "chunk") {
            chunksArray[currentChunk] = content;
            currentChunk++;
        }
        else if(localName == "urlChunks") {
            oneBookHashmap.put("urlChunks",chunksArray);
        }
        else if(localName == "url") {
            oneBookHashmap.put("url",content);
        }
        else if(localName == "code") {
            oneBookHashmap.put("code",content);
        }
        else if(localName == "thumbnail") {
            oneBookHashmap.put("thumbnail",content);
        }
        else if(localName == "title") {
            oneBookHashmap.put("title",content);
        }
        else if(localName == "author") {
            oneBookHashmap.put("author",content);
        }
        else if(localName == "bookType") {
            oneBookHashmap.put("bookType",content);
        }
        else if(localName == "chunksTotalSize") {
            oneBookHashmap.put("chunksTotalSize",content);
        }
        else if(localName == "ebook") {
            booksArray[currentBook] = oneBookHashmap;
            currentBook += 1;
        }
        else if(localName == "ebooks") {
            responseMap.put("books",booksArray);
        }

    }
}


class SystemOutResponseHandler extends SAXHandler {
    
    @Override
    public void startElement(String uri, String localName,
                             String qName, Attributes attributes)
    throws SAXException {
        
        
        System.out.println("++["+uri+"]["+localName+"]["+qName+"]");
        if(attributes.getLength() > 0) {
            for(int i = 0; i < attributes.getLength(); i = i + 1) {
                System.out.println("   ..["+attributes.getType(i)+"]["+attributes.getValue(i)+"]");
            }
        } else {
            System.out.println("   ..<no attributes>");
        }
        
        
    }
    
    @Override
    public void endElement(String uri, String localName,
                           String qName) throws SAXException {
        
        
        System.out.println("--["+uri+"]["+localName+"]["+qName+"]");
        System.out.println("   ...["+content+"]");

    }
}

class SAXHandler extends DefaultHandler {
    
    public HashMap responseMap;
    String content = null;
    
    public SAXHandler() {
        responseMap = new HashMap();
    }
    
    @Override
    public void startElement(String uri, String localName,
                             String qName, Attributes attributes)
    throws SAXException {
    }
    
    @Override
    public void endElement(String uri, String localName,
                           String qName) throws SAXException {
    }
    
    @Override
    public void characters(char[] ch, int start, int length)
    throws SAXException {
        content = String.copyValueOf(ch, start, length).trim();
    }
}
