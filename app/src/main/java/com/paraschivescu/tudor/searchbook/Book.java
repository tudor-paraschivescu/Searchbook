package com.paraschivescu.tudor.searchbook;

public class Book {

    private String mTitle;
    private String[] mAuthors;
    private String mLinkToCoverResource;

    public Book(String title, String[] authors, String linkToCoverResource) {
        mTitle = title;
        mAuthors = authors;
        mLinkToCoverResource = linkToCoverResource;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthors() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mAuthors.length - 1; i++) {
            sb.append(mAuthors[i]).append(", ");
        }
        sb.append(mAuthors[mAuthors.length - 1]);
        return sb.toString();
    }

    public String getLinkToCoverResource() {
        return mLinkToCoverResource;
    }
}
