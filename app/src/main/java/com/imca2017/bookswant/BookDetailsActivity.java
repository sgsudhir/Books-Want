package com.imca2017.bookswant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.imca2017.bookswant.app.AppController;
import com.imca2017.bookswant.pojo.search.Item;
import com.imca2017.bookswant.pojo.search.SearchResults;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BookDetailsActivity extends AppCompatActivity {
    ImageView imageViewBookImage;
    TextView textViewBookTitle, textViewIsBook, textViewTextSnippet, textViewDescription, textViewAuthor,
            textViewPublishedBy, textViewPublishedDate, textViewAvailableReadingModes, textViewTotalPages,
            textViewCategories, textViewMaturityRating, textViewRatingText, textViewLanguage, textViewIsbnDetails,
            textViewPrice;
    Button buttonBuyNow, buttonReadSample, buttonBuy, buttonShare, buttonPlayInfo;

    int resultIndex;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        resultIndex = intent.getIntExtra("index", -1);
        setContentView(R.layout.activity_book_details);

        imageViewBookImage = (ImageView) findViewById(R.id.activity_detail_image);

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

        buttonBuyNow = (Button) findViewById(R.id.activity_detail_buy_now);
        buttonReadSample = (Button) findViewById(R.id.activity_details_read_sample);

/*        try {
            showContentsOnView(resultIndex);
        } catch (Exception e) {
            Toast.makeText(getApplication(), "Error while fetching book details", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            this.finish();
        }
*/
        showContentsOnView(resultIndex);
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

        try {
            bookTitle = item.getVolumeInfo().getTitle();
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
                category = category + " and " + item.getVolumeInfo().getCategories().get(1) + " categories";
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
