package org.library;

import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;
import org.apache.hc.core5.http.message.StatusLine;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class Book {

    /**
     * when this class is invoked, automatically it should communicate with the httpReq class
     *
     */

    private ArrayList<String> bookTitleList;
    private ArrayList<String> bookAuthorList;
    private ArrayList<String> publisherList;
    private ArrayList<String> publishedDate;
    private ArrayList<String> pageCountList;
    private ArrayList<String> bookISBNList;



    private String bookTitle;
    private String bookAuthor;
    private String isbn;
    private String pageCount;
    private String publisher;
    private String publicationDate;



    public void insertQuery (String bookAuthor, String bookTitle) {
        getResponse(bookAuthor, bookTitle);
    }



    private void getResponse(String author, String bookTitle) {
        HttpReq req = new HttpReq();

//        Encode the data first
        String encodedAuthor =  encode(author);
        String encodedBookTitle = encode(bookTitle);

        req.getSendData(encodedAuthor, encodedBookTitle);
        int responseCode = req.getResponseCode();

        if (responseCode == 200) {
//            continue with extracting the important details form the JSON file
            req.setJsonData();

//            Add the book information to the lists in Book class
            loadBookInfo(req);

//            Then print it
            printBookInfo();
        } else {
            System.out.println("There has been an error. Error code: " + responseCode);
        }
    }

//    Load the book information to the lists from HttpReq
    private void loadBookInfo(HttpReq req) {
        HttpReq loadReq = new HttpReq();
//        Copying all elements into the new arrayLists in this class
        bookTitleList = req.getBookTitleList();
        bookAuthorList = req.getBookAuthorList();
        bookISBNList = req.getBookISBNList();
        publisherList = req.getPublisherList();
        publishedDate = req.getPublishedDate();
        pageCountList = req.getPageCountList();

    }

    /**
     * Loads book information from the same HttpReq instance that fetched the data
     */

    public void printBookInfo() {
        int minSize = Math.min(
                bookTitleList.size(),
                Math.min(
                        bookAuthorList.size(),
                        Math.min(
                                bookISBNList.size(),
                                Math.min(
                                        publisherList.size(),
                                        Math.min(
                                                publishedDate.size(),
                                                pageCountList.size()
                                        )
                                )
                        )
                )
        );


        for (int i = 0; i < minSize; i++) {
            // Access elements from each list
            String title = bookTitleList.get(i);
            String author = bookAuthorList.get(i);
            String isbn = bookISBNList.get(i);
            String publisher = publisherList.get(i);
            String publishedDateStr = publishedDate.get(i);
            int pageCount = Integer.parseInt(pageCountList.get(i));

            System.out.println("Title: " + title +"\n" + "Author: " + author + "\n" + "ISBN: " + isbn + "\n" +
                                "Publisher: " + publisher + "\n" + "Published Date: " + publishedDateStr + "\n" +
                                "Page Count: " + pageCount + "\n\n");
        }

    }

//    Encoding the value, in case it has space
    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

}
