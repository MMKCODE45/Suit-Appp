package com.example.suitsappication;

public class DataClass {

        private String dataTitle;
        private String dataDesc;
        private String dataLang;
        private String dataImage;
        private String key;



    private boolean purchased;



    public boolean isPurchased() {
        return purchased;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }
    private boolean bookmarked;

    public boolean isBookmarked() {
        return bookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        this.bookmarked = bookmarked;
    }
        public String getKey() {
            return key;
        }
        public void setKey(String key) {
            this.key = key;
        }
        public String getDataTitle() {
            return dataTitle;
        }
        public String getDataDesc() {
            return dataDesc;
        }
        public String getDataLang() {
            return dataLang;
        }
        public String getDataImage() {
            return dataImage;
        }
        public DataClass(String dataTitle, String dataDesc, String dataLang, String dataImage) {
            this.dataTitle = dataTitle;
            this.dataDesc = dataDesc;
            this.dataLang = dataLang;
            this.dataImage = dataImage;
        }
        public DataClass(){
        }
    }

