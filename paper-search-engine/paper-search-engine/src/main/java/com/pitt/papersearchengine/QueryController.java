package com.pitt.papersearchengine;


import Classes.Document;
import IndexingLucene.MyIndexReader;
import PseudoRFSearch.PseudoRFRetrievalModel;
import SearchLucene.ExtractQuery;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;

@Controller
public class QueryController {

    @RequestMapping("/")
    public String showResult(Model theModel){

        // create a query object
        Query theQuery = new Query();

        // add query object to the model
        theModel.addAttribute("query",theQuery);

    return "home-page";
    }

    @RequestMapping("/processQuery")
    public String processForm(@ModelAttribute("query") Query theQuery) throws Exception {

        Result theResult = new Result();
        // log the input data
        System.out.println("theQuery: "+ theQuery.getQueryContent()
                + " || " + theQuery.getTypeOptions());

        MyIndexReader ixreader = new MyIndexReader("all");
        PseudoRFRetrievalModel PRFSearchModel = new PseudoRFRetrievalModel(ixreader);
        ExtractQuery queries = new ExtractQuery(theQuery.getQueryContent());

        // begin search
        long startTime = System.currentTimeMillis();
        while (queries.hasNext()) {
            Classes.Query aQuery = queries.next();
            List<Document> results = PRFSearchModel.RetrieveQuery(aQuery, 20, 100, 0.4);
            if (results != null) {

                System.out.println("currrent query after process: "+ aQuery.GetQueryContent());
                int rank = 1;
                for (Document result : results) {
                    theQuery.setDocId1(result.docno());
                    System.out.println(aQuery.GetTopicId() + " Q0 " + result.docno() + " " + rank + " "
                            + result.score() + " MYRUN");
                    rank++;
                }
            }
        }
        long endTime = System.currentTimeMillis();

        // output running time
        System.out.println("\n\n4 queries search time: " + (endTime - startTime) / 60000.0 + " min");
        ixreader.close();



        return "query-confirmation";
    }
}
