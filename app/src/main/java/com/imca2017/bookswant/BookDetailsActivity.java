package com.imca2017.bookswant;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.imca2017.bookswant.app.AppController;
import com.imca2017.bookswant.pojo.deepdata.DeepDataContainer;
import com.imca2017.bookswant.pojo.deepdata.SearchObjects;
import com.imca2017.bookswant.pojo.search.Item;
import com.imca2017.bookswant.pojo.search.SearchResults;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BookDetailsActivity extends AppCompatActivity {
    ImageView imageViewBookImage;
    TextView textViewBookTitle, textViewIsBook, textViewTextSnippet, textViewDescription, textViewAuthor,
            textViewPublishedBy, textViewPublishedDate, textViewAvailableReadingModes, textViewTotalPages,
            textViewCategories, textViewMaturityRating, textViewRatingText, textViewLanguage, textViewIsbnDetails,
            textViewPrice;
    Button buttonSearchDeepWeb, buttonReadSample, buttonBuy, buttonShare, buttonPlayInfo;

    int resultIndex;
    String bookSampleUrl, slugBookTitle, searchUrl;
    boolean htmlParseSucess = false;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        resultIndex = intent.getIntExtra("index", -1);
        setContentView(R.layout.activity_book_details);

        imageViewBookImage = (ImageView) findViewById(R.id.activity_detail_image);
        buttonReadSample = (Button) findViewById(R.id.activity_details_read_sample);
        buttonSearchDeepWeb = (Button) findViewById(R.id.activity_detail_search_deep_web);

        textViewBookTitle = (TextView) findViewById(R.id.activity_detail_book_title);
        textViewIsBook = (TextView) findViewById(R.id.activity_is_book_);
        textViewTextSnippet = (TextView) findViewById(R.id.acticity_details_text_snippet);
        textViewDescription = (TextView) findViewById(R.id.acticity_details_book_description);
        textViewAuthor = (TextView) findViewById(R.id.activity_detail_author);
        textViewPublishedBy = (TextView) findViewById(R.id.activity_detail_publisher);
        textViewPublishedDate = (TextView) findViewById(R.id.activity_detail_publish_date);
        textViewAvailableReadingModes = (TextView) findViewById(R.id.activity_detail_reading_modes);
        textViewTotalPages = (TextView) findViewById(R.id.activity_detail_pages);
        textViewCategories = (TextView) findViewById(R.id.activity_detail_category);
        textViewMaturityRating = (TextView) findViewById(R.id.activity_detail_maturity);
        textViewRatingText = (TextView) findViewById(R.id.activity_detail_rating_text);
        textViewLanguage = (TextView) findViewById(R.id.activity_detail_language);
        textViewIsbnDetails = (TextView) findViewById(R.id.activity_detail_isbn);
        textViewPrice = (TextView) findViewById(R.id.activity_detail_price);

        showContentsOnView(resultIndex);

        buttonReadSample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(bookSampleUrl));
                startActivity(browserIntent);
            }
        });

        buttonSearchDeepWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FetchDeepData(BookDetailsActivity.this).execute();
            }
        });
    }

    private class FetchDeepData extends AsyncTask<Void, Void, Void> {
        Context context;
        ProgressDialog dialog;
        FetchDeepData(Context context) {
            this.context = context;
            dialog = new ProgressDialog(context);
            dialog.setMessage("Pleasw wait while getting results ...");
        }

        @Override
        protected Void doInBackground(Void... params) {
            parseHTML();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.hide();
            startDeepWebResultActivity();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }
    }

    private void startDeepWebResultActivity() {
        if (htmlParseSucess) {
            Intent resultIntent = new Intent(BookDetailsActivity.this, DeepWebResultActivity.class);
            startActivity(resultIntent);
        }
    }

    private void parseHTML() {
        htmlParseSucess = false;
        searchUrl = "http://gen.lib.rus.ec/search.php?req=" + slugBookTitle;
        String link;
        try {
            Document document = Jsoup.connect(searchUrl).get();
            Element element = document.getElementsByClass("c").first();
            Elements tbodies = element.getElementsByTag("tbody");
            Elements tableRows = tbodies.get(0).getElementsByTag("tr");

            List<DeepDataContainer> searchObjects = new ArrayList<>();
            for (int i = 1; i < tableRows.size(); i++) {
                DeepDataContainer container = new DeepDataContainer();

                Elements tableData = tableRows.get(i).getElementsByTag("td");

                container.setAuthors("Author: " + tableData.get(1).getElementsByTag("a").text());
                container.setTitle("Title: " + tableData.get(2).getElementsByTag("a").text());
                container.setYear("Year: " + tableData.get(4).text());
                container.setPages("Pages: " + tableData.get(5).text());
                container.setSize("Size: " + tableData.get(7).text());
                container.setType("Type: " + tableData.get(8).text());
                container.setMirrorLink(tableData.get(9).getElementsByTag("a").attr("href"));
                link = null;
                try {
                    Document doc = Jsoup.connect(tableData.get(9).getElementsByTag("a").attr("href")).get();
                    link = doc.getElementsByTag("table").get(0)
                            .getElementsByTag("tbody").get(0).getElementsByTag("tr").get(0)
                            .getElementsByTag("td").get(2)
                            .getElementsByTag("a").attr("href");
                    link = "http://libgen.io" + link;
                    Log.d("|-|-|- Download -|-|-|", link + "");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                container.setDownloadLink(link);

                searchObjects.add(container);

                Log.d("--Year: ---", tableData.get(4).text() + "||");
                Log.d("--Page: ---", tableData.get(5).text() + "||");
                Log.d("--Size: ---", tableData.get(7).text() + "||");
                Log.d("--Type: ---", tableData.get(8).text() + "||");
                Log.d("--Authors--", tableData.get(1).getElementsByTag("a").text() + "||");
                Log.d("--Title--", tableData.get(2).getElementsByTag("a").text() + "||");
                Log.d("--Mirror--", tableData.get(9).getElementsByTag("a").attr("href") + "||");
            }
            SearchObjects objs = new SearchObjects();
            AppController.getInstance().initSearchObjects(objs);
            AppController.getInstance().getDeepSearchObjectses().setNodes(searchObjects);

        } catch (Exception e) {
            e.printStackTrace();
        }
        htmlParseSucess = true;
    }


    private void showContentsOnView(int index) {
        String bookTitle, printType, textSnippet, description, author, publisher, publishedOn,
                readingModes, pages, category, maturityRating, ratingText, language, isbn, price;
        String buyNowLink, imageLink;

        SearchResults results = AppController.getInstance().getmSearchResultsObject();
        Item item = results.getItems().get(index);

        bookTitle = null;
        printType = null;
        textSnippet = null;
        description = null;
        author = null;
        publisher = null;
        publishedOn = null;
        readingModes = null;
        pages = null;
        category = null;
        maturityRating = null;
        ratingText = null;
        language = null;
        isbn = null;
        price = null;
        buyNowLink = null;
        imageLink = null;
        bookSampleUrl = null;
        slugBookTitle = null;

        try {
            bookTitle = item.getVolumeInfo().getTitle();
            slugBookTitle = bookTitle.replaceAll(" ", "+");
            try {
                bookTitle = bookTitle + ": " + item.getVolumeInfo().getSubtitle();
                bookTitle = bookTitle.replaceAll("null", "");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            bookTitle = "N/A";
        }

        try {
            printType = item.getVolumeInfo().getPrintType();
        } catch (Exception e) {
            e.printStackTrace();
            printType = "N/A";
        }

        try {
            textSnippet = item.getSearchInfo().getTextSnippet();
        } catch (Exception e) {
            e.printStackTrace();
            textSnippet = "N/A";
        }

        try {
            description = item.getVolumeInfo().getDescription();
        } catch (Exception e) {
            e.printStackTrace();
            description = "N/A";
        }

        try {
            for (String authorName : item.getVolumeInfo().getAuthors()) {
                if (author == null) {
                    author = authorName;
                } else {
                    author = author + ", " + authorName;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            author = "N/A";
        }

        try {
            publisher = item.getVolumeInfo().getPublisher();
        } catch (Exception e) {
            e.printStackTrace();
            publisher = "N/A";
        }

        try {
            publishedOn = "Published on " + item.getVolumeInfo().getPublishedDate();
        } catch (Exception e) {
            e.printStackTrace();
            publishedOn = "N/A";
        }

        try {
            Boolean txt = item.getVolumeInfo().getReadingModes().getText();
            Boolean img = item.getVolumeInfo().getReadingModes().getImage();
            if (txt && img) {
                readingModes = "Text and PDF";
            } else if (txt && !img) {
                readingModes = "Text";
            } else if (!txt && img) {
                readingModes = "PDF";
            } else {
                readingModes = "N/A";
            }
        } catch (Exception e) {
            e.printStackTrace();
            readingModes = "N/A";
        }

        try {
            pages = item.getVolumeInfo().getPageCount().toString();
        } catch (Exception e) {
            e.printStackTrace();
            pages = "N/A";
        }

        try {
            category = item.getVolumeInfo().getCategories().get(0) + " category";
            try {
                category = category + " and " + item.getVolumeInfo().getCategories().get(0) + " categories";
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            category = "N/A";
        }

        try {
            maturityRating = item.getVolumeInfo().getMaturityRating().toLowerCase();
            maturityRating = maturityRating.replaceAll("_", " ");
        } catch (Exception e) {
            e.printStackTrace();
            maturityRating = "N/A";
        }

        try {
            String rate = "This book got " + item.getVolumeInfo().getAverageRating() + " rating";
            rate = rate.replaceAll("null", "NO");
            int rateCount = item.getVolumeInfo().getRatingsCount();
            if (rateCount > 1) {
                rate = rate + "and " + rateCount + "persons rate this book";
            } else if (rateCount > 0) {
                rate = rate + "and " + rateCount + "person rate this book";
            } else if (rateCount == 0) {
            }
        } catch (Exception e) {
            e.printStackTrace();
            ratingText = "N/A";
        }

        try {
            language = "Language code: " + item.getVolumeInfo().getLanguage().toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
            language = "N/A";
        }

        try {
            isbn = "ISBN-10: " + item.getVolumeInfo().getIndustryIdentifiers().get(0).getIdentifier() +
                    ", ISBN-13: " + item.getVolumeInfo().getIndustryIdentifiers().get(1).getIdentifier();
        } catch (Exception e) {
            e.printStackTrace();
            isbn = "N/A";
        }

        try {
            price = item.getSaleInfo().getRetailPrice().getAmount().toString() + " INR";
        } catch (Exception e) {
            e.printStackTrace();
            price = "N/A";
        }
        try {
            imageLink = item.getVolumeInfo().getImageLinks().getThumbnail();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            bookSampleUrl = item.getVolumeInfo().getInfoLink();
        } catch (Exception e) {
            e.printStackTrace();
        }

        textViewBookTitle.setText(bookTitle);
        textViewIsBook.setText(printType);
        textViewTextSnippet.setText(textSnippet);
        textViewDescription.setText(description);
        textViewAuthor.setText(author);
        textViewPublishedBy.setText(publisher);
        textViewPublishedDate.setText(publishedOn);
        textViewAvailableReadingModes.setText(readingModes);
        textViewTotalPages.setText(pages);
        textViewCategories.setText(category);
        textViewMaturityRating.setText(maturityRating);
        textViewRatingText.setText(ratingText);
        textViewLanguage.setText(language);
        textViewIsbnDetails.setText(isbn);
        textViewPrice.setText(price);
        Picasso.with(this).load(imageLink).into(imageViewBookImage);

    }

}
